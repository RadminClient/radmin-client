/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import radmin.client.events.render.Render3DEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class TargetESP
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> radius;
    private final Setting<Double> lineHeight;
    private final Setting<Boolean> circle;
    private final Setting<Boolean> line;

    public TargetESP() {
        super(Categories.Render, "target-esp", "Renders a marker on the entity you are looking at.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.radius = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("radius")).description("Radius of the circle.")).defaultValue(0.4).min(0.1).sliderRange(0.1, 1.5).build());
        this.lineHeight = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("line-height")).description("Height of the line above the target.")).defaultValue(1.0).min(0.0).sliderRange(0.0, 3.0).build());
        this.circle = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("circle")).description("Renders a circle under the target.")).defaultValue(true)).build());
        this.line = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("line")).description("Renders a vertical line through the target.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (this.mc.player == null || this.mc.level == null || this.mc.hitResult == null) {
            return;
        }
        HitResult hitResult = this.mc.hitResult;
        if (!(hitResult instanceof EntityHitResult)) {
            return;
        }
        EntityHitResult hitResult2 = (EntityHitResult)hitResult;
        Entity target = hitResult2.getEntity();
        if (target == null || target.isRemoved()) {
            return;
        }
        double x = Mth.lerp((double)event.tickDelta, (double)target.xOld, (double)target.getX());
        double y = Mth.lerp((double)event.tickDelta, (double)target.yOld, (double)target.getY());
        double z = Mth.lerp((double)event.tickDelta, (double)target.zOld, (double)target.getZ());
        Color color = ThemeAccent.get();
        double r = this.radius.get();
        if (this.circle.get().booleanValue()) {
            int segments = 40;
            for (int i = 0; i < segments; ++i) {
                double a1 = (double)i / (double)segments * Math.PI * 2.0;
                double a2 = (double)(i + 1) / (double)segments * Math.PI * 2.0;
                event.renderer.line(x + Math.cos(a1) * r, y + 0.02, z + Math.sin(a1) * r, x + Math.cos(a2) * r, y + 0.02, z + Math.sin(a2) * r, color, color);
            }
        }
        if (this.line.get().booleanValue()) {
            double top = y + (double)target.getBbHeight() + this.lineHeight.get();
            event.renderer.line(x, y, z, x, top, z, color, color);
        }
    }
}

