/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.client.renderer.RenderPipelines
 *  net.minecraft.client.renderer.state.MapRenderState
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.item.MapItem
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.saveddata.maps.MapId
 *  net.minecraft.world.level.saveddata.maps.MapItemSavedData
 */
package radmin.client.utils.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import radmin.client.MeteorClient;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.BetterTooltips;
import radmin.client.utils.tooltip.MeteorTooltipData;

public class MapTooltipComponent
implements ClientTooltipComponent,
MeteorTooltipData {
    private static final Identifier TEXTURE_MAP_BACKGROUND = Identifier.parse((String)"textures/map/map_background.png");
    private final int mapId;
    private final MapRenderState mapRenderState = new MapRenderState();

    public MapTooltipComponent(int mapId) {
        this.mapId = mapId;
    }

    public int getHeight(Font textRenderer) {
        double scale = Modules.get().get(BetterTooltips.class).mapsScale.get();
        return (int)(144.0 * scale) + 2;
    }

    public int getWidth(Font textRenderer) {
        double scale = Modules.get().get(BetterTooltips.class).mapsScale.get();
        return (int)(144.0 * scale);
    }

    @Override
    public ClientTooltipComponent getComponent() {
        return this;
    }

    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        float scale = Modules.get().get(BetterTooltips.class).mapsScale.get().floatValue();
        int size = (int)(144.0f * scale);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_MAP_BACKGROUND, x, y, 0.0f, 0.0f, size, size, size, size);
        MapItemSavedData mapState = MapItem.getSavedData((MapId)new MapId(this.mapId), (Level)MeteorClient.mc.level);
        if (mapState == null) {
            return;
        }
        graphics.pose().pushMatrix();
        graphics.pose().translate((float)x, (float)y);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(8.0f, 8.0f);
        MeteorClient.mc.getMapRenderer().extractRenderState(new MapId(this.mapId), mapState, this.mapRenderState);
        graphics.map(this.mapRenderState);
        graphics.pose().popMatrix();
    }
}

