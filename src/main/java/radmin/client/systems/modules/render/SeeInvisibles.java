/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import radmin.client.events.render.Render3DEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class SeeInvisibles
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> playersOnly;

    public SeeInvisibles() {
        super(Categories.Render, "see-invisibles", "Renders a box around invisible entities so you can see them.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.playersOnly = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("players-only")).description("Only renders boxes on invisible players.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (this.mc.player == null || this.mc.level == null) {
            return;
        }
        for (Entity entity : this.mc.level.entitiesForRendering()) {
            if (entity.isRemoved() || entity == this.mc.player || !entity.isInvisible() || this.playersOnly.get().booleanValue() && !(entity instanceof Player)) continue;
            Color color = ThemeAccent.get();
            double x = Mth.lerp((double)event.tickDelta, (double)entity.xOld, (double)entity.getX());
            double y = Mth.lerp((double)event.tickDelta, (double)entity.yOld, (double)entity.getY());
            double z = Mth.lerp((double)event.tickDelta, (double)entity.zOld, (double)entity.getZ());
            double w = (double)entity.getBbWidth() / 2.0;
            double h = entity.getBbHeight();
            double minX = x - w;
            double maxX = x + w;
            double minY = y;
            double maxY = y + h;
            double minZ = z - w;
            double maxZ = z + w;
            event.renderer.line(minX, minY, minZ, maxX, minY, minZ, color, color);
            event.renderer.line(maxX, minY, minZ, maxX, minY, maxZ, color, color);
            event.renderer.line(maxX, minY, maxZ, minX, minY, maxZ, color, color);
            event.renderer.line(minX, minY, maxZ, minX, minY, minZ, color, color);
            event.renderer.line(minX, maxY, minZ, maxX, maxY, minZ, color, color);
            event.renderer.line(maxX, maxY, minZ, maxX, maxY, maxZ, color, color);
            event.renderer.line(maxX, maxY, maxZ, minX, maxY, maxZ, color, color);
            event.renderer.line(minX, maxY, maxZ, minX, maxY, minZ, color, color);
            event.renderer.line(minX, minY, minZ, minX, maxY, minZ, color, color);
            event.renderer.line(maxX, minY, minZ, maxX, maxY, minZ, color, color);
            event.renderer.line(maxX, minY, maxZ, maxX, maxY, maxZ, color, color);
            event.renderer.line(minX, minY, maxZ, minX, maxY, maxZ, color, color);
        }
    }
}

