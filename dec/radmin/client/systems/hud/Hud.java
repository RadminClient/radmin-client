/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.hud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.CustomFontChangedEvent;
import radmin.client.events.render.Render2DEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.gui.WidgetScreen;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ColorListSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.KeybindSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.Settings;
import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.hud.HudElement;
import radmin.client.systems.hud.HudElementInfo;
import radmin.client.systems.hud.HudGroup;
import radmin.client.systems.hud.HudRenderer;
import radmin.client.systems.hud.HudStyle;
import radmin.client.systems.hud.XAnchor;
import radmin.client.systems.hud.YAnchor;
import radmin.client.systems.hud.elements.ActiveModulesHud;
import radmin.client.systems.hud.elements.ArmorHud;
import radmin.client.systems.hud.elements.CombatHud;
import radmin.client.systems.hud.elements.CompassHud;
import radmin.client.systems.hud.elements.HoleHud;
import radmin.client.systems.hud.elements.InventoryHud;
import radmin.client.systems.hud.elements.ItemHud;
import radmin.client.systems.hud.elements.LagNotifierHud;
import radmin.client.systems.hud.elements.MapHud;
import radmin.client.systems.hud.elements.MeteorTextHud;
import radmin.client.systems.hud.elements.ModuleInfosHud;
import radmin.client.systems.hud.elements.PlayerModelHud;
import radmin.client.systems.hud.elements.PlayerRadarHud;
import radmin.client.systems.hud.elements.PotionTimersHud;
import radmin.client.systems.hud.elements.keyboard.KeyboardHud;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.render.color.SettingColor;

public class Hud
extends System<Hud>
implements Iterable<HudElement> {
    public static final HudGroup GROUP = new HudGroup("Meteor");
    public boolean active;
    public Settings settings = new Settings();
    public final Map<String, HudElementInfo<?>> infos = new TreeMap();
    private final List<HudElement> elements = new ArrayList<HudElement>();
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgEditor = this.settings.createGroup("Editor");
    private final SettingGroup sgKeybind = this.settings.createGroup("Bind");
    private final Setting<Boolean> customFont = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("custom-font")).description("Text will use custom font.")).defaultValue(true)).onChanged(bl -> {
        for (HudElement element : this.elements) {
            element.onFontChanged();
        }
    })).build());
    private final Setting<Boolean> hideInMenus = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("hide-in-menus")).description("Hides the meteor hud when in inventory screens or game menus.")).defaultValue(false)).build());
    private final Setting<Double> textScale = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("text-scale")).description("Scale of text if not overridden by the element.")).defaultValue(1.0).min(0.5).sliderRange(0.5, 3.0).build());
    public final Setting<List<SettingColor>> textColors = this.sgGeneral.add(((ColorListSetting.Builder)((ColorListSetting.Builder)((ColorListSetting.Builder)new ColorListSetting.Builder().name("text-colors")).description("Colors used for the Text element.")).defaultValue(List.of(new SettingColor(), new SettingColor(175, 175, 175), new SettingColor(25, 225, 25), new SettingColor(225, 25, 25)))).build());
    public final Setting<HudStyle> style = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("style")).description("Style used by elements that support both Default and Wave styles.")).defaultValue(HudStyle.Default)).build());
    public final Setting<Integer> border = this.sgEditor.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("border")).description("Space around the edges of the screen.")).defaultValue(4)).sliderMax(20).build());
    public final Setting<Integer> snappingRange = this.sgEditor.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("snapping-range")).description("Snapping range in editor.")).defaultValue(10)).sliderMax(20).build());
    private final Setting<Keybind> keybind = this.sgKeybind.add(((KeybindSetting.Builder)((KeybindSetting.Builder)new KeybindSetting.Builder().name("bind")).defaultValue(Keybind.none())).action(() -> {
        this.active = !this.active;
    }).build());
    private boolean resetToDefaultElements;

    public Hud() {
        super("hud");
    }

    public static Hud get() {
        return Systems.get(Hud.class);
    }

    @Override
    public void init() {
        this.settings.registerColorSettings(null);
        this.register(MeteorTextHud.INFO);
        this.register(ItemHud.INFO);
        this.register(InventoryHud.INFO);
        this.register(CompassHud.INFO);
        this.register(ArmorHud.INFO);
        this.register(HoleHud.INFO);
        this.register(PlayerModelHud.INFO);
        this.register(ActiveModulesHud.INFO);
        this.register(LagNotifierHud.INFO);
        this.register(PlayerRadarHud.INFO);
        this.register(ModuleInfosHud.INFO);
        this.register(PotionTimersHud.INFO);
        this.register(CombatHud.INFO);
        this.register(MapHud.INFO);
        this.register(KeyboardHud.INFO);
        if (this.isFirstInit) {
            this.resetToDefaultElements();
        }
    }

    public void register(HudElementInfo<?> info) {
        this.infos.put(info.name, info);
    }

    private void add(HudElement element, int x, int y, XAnchor xAnchor, YAnchor yAnchor) {
        element.box.setPos(x, y);
        if (xAnchor == null || yAnchor == null) {
            element.box.updateAnchors();
        } else {
            element.box.xAnchor = xAnchor;
            element.box.yAnchor = yAnchor;
        }
        element.settings.registerColorSettings(null);
        this.elements.add(element);
    }

    public void add(HudElementInfo<?> info, int x, int y, XAnchor xAnchor, YAnchor yAnchor) {
        this.add(info.create(), x, y, xAnchor, yAnchor);
    }

    public void add(HudElementInfo<?> info, int x, int y) {
        this.add(info, x, y, null, null);
    }

    public void add(@NotNull HudElementInfo.Preset preset, int x, int y, XAnchor xAnchor, YAnchor yAnchor) {
        HudElement element = preset.info.create();
        preset.callback.accept(element);
        this.add(element, x, y, xAnchor, yAnchor);
    }

    public void add(@NotNull HudElementInfo.Preset preset, int x, int y) {
        this.add(preset, x, y, null, null);
    }

    void remove(HudElement element) {
        element.settings.unregisterColorSettings();
        this.elements.remove(element);
    }

    public void clear() {
        this.elements.clear();
    }

    public void resetToDefaultElements() {
        this.resetToDefaultElements = true;
    }

    private void resetToDefaultElementsImpl() {
        this.elements.clear();
        int h = (int)Math.ceil(HudRenderer.INSTANCE.textHeight(true));
        this.add(MeteorTextHud.WATERMARK, 4, 4, XAnchor.Left, YAnchor.Top);
        this.add(MeteorTextHud.FPS, 4, 4 + h, XAnchor.Left, YAnchor.Top);
        this.add(MeteorTextHud.TPS, 4, 4 + h * 2, XAnchor.Left, YAnchor.Top);
        this.add(MeteorTextHud.PING, 4, 4 + h * 3, XAnchor.Left, YAnchor.Top);
        this.add(MeteorTextHud.SPEED, 4, 4 + h * 4, XAnchor.Left, YAnchor.Top);
        this.add(ActiveModulesHud.INFO, -4, 4, XAnchor.Right, YAnchor.Top);
        this.add(MeteorTextHud.POSITION, -4, -4, XAnchor.Right, YAnchor.Bottom);
        this.add(MeteorTextHud.OPPOSITE_POSITION, -4, -4 - h, XAnchor.Right, YAnchor.Bottom);
        this.add(MeteorTextHud.ROTATION, -4, -4 - h * 2, XAnchor.Right, YAnchor.Bottom);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (Utils.isLoading()) {
            return;
        }
        if (this.resetToDefaultElements) {
            this.resetToDefaultElementsImpl();
            this.resetToDefaultElements = false;
        }
        if (!this.active && !HudEditorScreen.isOpen()) {
            return;
        }
        for (HudElement element : this.elements) {
            if (!element.isActive() && !element.isInEditor()) continue;
            element.tick(HudRenderer.INSTANCE);
        }
    }

    @EventHandler
    private void onRender(Render2DEvent event) {
        if (Utils.isLoading()) {
            return;
        }
        if (!this.active && !HudEditorScreen.isOpen() || this.shouldHideHud()) {
            return;
        }
        if ((MeteorClient.mc.options.hideGui || MeteorClient.mc.debugEntries.isOverlayVisible()) && !HudEditorScreen.isOpen()) {
            return;
        }
        HudRenderer.INSTANCE.begin(event.graphics);
        for (HudElement element : this.elements) {
            element.updatePos();
            if (!element.isActive() && !element.isInEditor()) continue;
            element.render(HudRenderer.INSTANCE);
        }
        HudRenderer.INSTANCE.end();
    }

    private boolean shouldHideHud() {
        return this.hideInMenus.get() != false && MeteorClient.mc.screen != null && !(MeteorClient.mc.screen instanceof WidgetScreen);
    }

    @EventHandler
    private void onCustomFontChanged(CustomFontChangedEvent event) {
        if (this.customFont.get().booleanValue()) {
            for (HudElement element : this.elements) {
                element.onFontChanged();
            }
        }
    }

    public boolean hasCustomFont() {
        return this.customFont.get();
    }

    public double getTextScale() {
        return this.textScale.get();
    }

    @Override
    @NotNull
    public Iterator<HudElement> iterator() {
        return this.elements.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("__version__", 1);
        tag.putBoolean("active", this.active);
        tag.put("settings", (Tag)this.settings.toTag());
        tag.put("elements", (Tag)NbtUtils.listToTag(this.elements));
        return tag;
    }

    @Override
    public Hud fromTag(CompoundTag tag) {
        if (!tag.contains("__version__")) {
            this.resetToDefaultElements();
            return this;
        }
        tag.getBoolean("active").ifPresent(active1 -> {
            this.active = active1;
        });
        this.settings.fromTag(tag.getCompoundOrEmpty("settings"));
        this.elements.clear();
        for (Tag e : tag.getListOrEmpty("elements")) {
            HudElementInfo<?> info;
            CompoundTag c = (CompoundTag)e;
            if (c.getString("name").isEmpty() || (info = this.infos.get(c.getString("name").get())) == null) continue;
            HudElement element = info.create();
            element.fromTag(c);
            element.settings.registerColorSettings(null);
            this.elements.add(element);
        }
        return this;
    }
}

