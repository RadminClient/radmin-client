/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.state.MapRenderState
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.MapItem
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.saveddata.maps.MapId
 *  net.minecraft.world.level.saveddata.maps.MapItemSavedData
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix3x2fStack
 */
package radmin.client.systems.hud.elements;

import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import radmin.client.MeteorClient;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ColorSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.HudElement;
import radmin.client.systems.hud.HudElementInfo;
import radmin.client.systems.hud.HudRenderer;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.utils.Utils;
import radmin.client.utils.player.FindItemResult;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;

public class MapHud
extends HudElement {
    public static final HudElementInfo<MapHud> INFO = new HudElementInfo<MapHud>(Hud.GROUP, "map", "Displays the contents of a map on your Hud.", MapHud::new);
    private final SettingGroup sgGeneral;
    private final SettingGroup sgVisual;
    private final Setting<Mode> mode;
    private final Setting<Integer> slotIndex;
    private final Setting<Integer> mapId;
    private final Setting<Double> scale;
    private final Setting<Boolean> background;
    private final Setting<SettingColor> backgroundColor;
    private final MapRenderState renderState;
    @Nullable
    private MapId mapComponent;
    @Nullable
    private MapItemSavedData mapState;

    public MapHud() {
        super(INFO);
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgVisual = this.settings.createGroup("Visual");
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("How to determine which map to render.")).defaultValue(Mode.Simple)).build());
        this.slotIndex = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("slot-index")).description("Which slot to grab the map from.")).visible(() -> this.mode.get() == Mode.SlotIndex)).defaultValue(0)).sliderRange(0, 40).build());
        this.mapId = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("map-id")).description("Which map id to render from. Must be in your inventory!")).visible(() -> this.mode.get() == Mode.MapId)).defaultValue(0)).noSlider().build());
        this.scale = this.sgVisual.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("scale")).description("How big to render the map.")).defaultValue(1.0).min(0.5).sliderRange(0.5, 3.0).build());
        this.background = this.sgVisual.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("background")).description("Displays background.")).defaultValue(false)).build());
        this.backgroundColor = this.sgVisual.add(((ColorSetting.Builder)((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("background-color")).description("Color used for the background.")).visible(this.background::get)).defaultValue(new SettingColor(25, 25, 25, 50)).build());
        this.renderState = new MapRenderState();
    }

    @Override
    public void tick(HudRenderer renderer) {
        double scale = this.scale.get();
        this.setSize(128.0 * scale, 128.0 * scale);
        if (!Utils.canUpdate()) {
            return;
        }
        ItemStack mapStack = ItemStack.EMPTY;
        switch (this.mode.get().ordinal()) {
            case 0: {
                mapStack = MeteorClient.mc.player.getInventory().getItem(this.slotIndex.get().intValue());
                break;
            }
            case 1: {
                FindItemResult mapResult = InvUtils.find(stack -> {
                    MapId mapIdComponent = (MapId)stack.get(DataComponents.MAP_ID);
                    return mapIdComponent != null && mapIdComponent.id() == this.mapId.get().intValue();
                });
                if (!mapResult.found()) break;
                mapStack = MeteorClient.mc.player.getInventory().getItem(mapResult.slot());
                break;
            }
            case 2: {
                FindItemResult mapResult = InvUtils.find(stack -> {
                    MapId mapIdComponent = (MapId)stack.get(DataComponents.MAP_ID);
                    return mapIdComponent != null;
                });
                if (!mapResult.found()) break;
                mapStack = MeteorClient.mc.player.getInventory().getItem(mapResult.slot());
            }
        }
        if (mapStack.isEmpty() || !mapStack.has(DataComponents.MAP_ID)) {
            this.mapComponent = null;
            this.mapState = null;
        } else {
            this.mapComponent = (MapId)mapStack.get(DataComponents.MAP_ID);
            this.mapState = MapItem.getSavedData((MapId)this.mapComponent, (Level)MeteorClient.mc.level);
        }
    }

    @Override
    public void render(HudRenderer renderer) {
        if (this.mapComponent == null || this.mapState == null) {
            if (HudEditorScreen.isOpen()) {
                renderer.quad(this.x, this.y, this.getWidth(), this.getHeight(), this.backgroundColor.get());
                renderer.line(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), Color.GRAY);
                renderer.line(this.x + this.getWidth(), this.y, this.x, this.y + this.getHeight(), Color.GRAY);
            }
            return;
        }
        if (this.background.get().booleanValue()) {
            renderer.quad(this.x, this.y, this.getWidth(), this.getHeight(), this.backgroundColor.get());
        }
        renderer.post(() -> {
            MeteorClient.mc.getMapRenderer().extractRenderState(this.mapComponent, this.mapState, this.renderState);
            Matrix3x2fStack matrices = renderer.graphics.pose();
            matrices.pushMatrix();
            matrices.scale(1.0f / (float)MeteorClient.mc.getWindow().getGuiScale());
            matrices.translate((float)this.x, (float)this.y);
            matrices.scale(this.scale.get().floatValue());
            renderer.graphics.map(this.renderState);
            matrices.popMatrix();
        });
    }

    private static enum Mode {
        SlotIndex,
        MapId,
        Simple;

    }
}

