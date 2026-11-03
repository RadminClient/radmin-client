/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.arrow.AbstractArrow
 *  net.minecraft.world.entity.projectile.arrow.Arrow
 *  net.minecraft.world.entity.projectile.arrow.SpectralArrow
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.phys.Vec3;
import radmin.client.events.render.Render3DEvent;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class Arrows
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> range;
    private final Setting<Double> size;

    public Arrows() {
        super(Categories.Render, "arrows", "Renders a visual arrow indicator on arrows and thrown projectiles.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.range = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("range")).description("Maximum range to render arrows.")).defaultValue(32.0).min(1.0).sliderRange(1.0, 128.0).build());
        this.size = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("size")).description("Size of the arrow indicator.")).defaultValue(0.4).min(0.1).sliderRange(0.1, 1.5).build());
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (this.mc.player == null || this.mc.level == null) {
            return;
        }
        double r = this.range.get();
        for (Entity entity : this.mc.level.entitiesForRendering()) {
            if (!(entity instanceof AbstractArrow)) continue;
            AbstractArrow arrow = (AbstractArrow)entity;
            if (!(entity instanceof Arrow) && !(entity instanceof SpectralArrow) || entity.isRemoved() || entity.distanceToSqr((Entity)this.mc.player) > r * r) continue;
            double x = Mth.lerp((double)event.tickDelta, (double)entity.xOld, (double)entity.getX());
            double y = Mth.lerp((double)event.tickDelta, (double)entity.yOld, (double)entity.getY()) + (double)entity.getBbHeight() / 2.0;
            double z = Mth.lerp((double)event.tickDelta, (double)entity.zOld, (double)entity.getZ());
            Vec3 motion = entity.getDeltaMovement();
            double mx = motion.x;
            double my = motion.y;
            double mz = motion.z;
            double len = Math.sqrt(mx * mx + my * my + mz * mz);
            if (len < 1.0E-4) {
                mx = 0.0;
                my = -1.0;
                mz = 0.0;
                len = 1.0;
            } else {
                mx /= len;
                my /= len;
                mz /= len;
            }
            Color color = ThemeAccent.get();
            double s = this.size.get();
            Vec3 center = new Vec3(x, y, z);
            Vec3 dir = new Vec3(mx, my, mz);
            Vec3 tip = center.add(dir.scale(s));
            Vec3 tail = center.subtract(dir.scale(s));
            event.renderer.line(tail.x, tail.y, tail.z, tip.x, tip.y, tip.z, color, color);
            Vec3 perp = this.pickPerpendicular(dir);
            Vec3 right = perp.scale(s * 0.5);
            event.renderer.line(tip.x, tip.y, tip.z, tip.subtract((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).x, tip.subtract((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).y, tip.subtract((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).z, color, color);
            event.renderer.line(tip.x, tip.y, tip.z, tip.add((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).x, tip.add((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).y, tip.add((Vec3)right).subtract((Vec3)dir.scale((double)(s * 0.6))).z, color, color);
        }
    }

    private Vec3 pickPerpendicular(Vec3 dir) {
        Vec3 up = Math.abs(dir.y) < 0.9 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        return dir.cross(up).normalize();
    }
}

