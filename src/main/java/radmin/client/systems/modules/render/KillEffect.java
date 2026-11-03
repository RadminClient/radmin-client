/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import radmin.client.events.entity.player.AttackEntityEvent;
import radmin.client.events.render.Render3DEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class KillEffect
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> size;
    private final Setting<Double> fadeInTime;
    private final Setting<Double> displayTime;
    private final Setting<Double> fadeOutTime;
    private final Setting<Boolean> glow;
    private final Setting<Boolean> circle;
    private Vec3 effectPosition;
    private long effectStartTime;

    public KillEffect() {
        super(Categories.Render, "kill-effect", "Renders a visual effect at the position you kill an entity.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.size = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("size")).description("Size of the effect.")).defaultValue(0.4).min(0.1).sliderRange(0.1, 1.5).build());
        this.fadeInTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("fade-in-time")).description("Fade in time in milliseconds.")).defaultValue(100.0).min(50.0).sliderRange(50.0, 500.0).build());
        this.displayTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("display-time")).description("Display time in milliseconds.")).defaultValue(500.0).min(100.0).sliderRange(100.0, 1500.0).build());
        this.fadeOutTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("fade-out-time")).description("Fade out time in milliseconds.")).defaultValue(300.0).min(50.0).sliderRange(50.0, 700.0).build());
        this.glow = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("glow")).description("Renders a glow around the effect.")).defaultValue(true)).build());
        this.circle = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("circle")).description("Renders a circle around the cross.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onAttack(AttackEntityEvent event) {
        LivingEntity living;
        if (this.mc.player == null || this.mc.level == null || event.entity == null) {
            return;
        }
        Entity entity = event.entity;
        if (!(entity instanceof LivingEntity) || !(living = (LivingEntity)entity).isDeadOrDying()) {
            return;
        }
        this.effectPosition = new Vec3(event.entity.getX(), event.entity.getY() + (double)event.entity.getBbHeight() / 2.0, event.entity.getZ());
        this.effectStartTime = System.currentTimeMillis();
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        long total;
        if (this.mc.player == null || this.effectPosition == null) {
            return;
        }
        long now = System.currentTimeMillis();
        long age = now - this.effectStartTime;
        if (age > (total = this.fadeInTime.get().longValue() + this.displayTime.get().longValue() + this.fadeOutTime.get().longValue())) {
            this.effectPosition = null;
            return;
        }
        double fadeIn = this.fadeInTime.get();
        double display = this.displayTime.get();
        double fadeOut = this.fadeOutTime.get();
        double alpha = Math.min(1.0, (double)age / fadeIn) * (1.0 - Math.max(0.0, ((double)age - fadeIn - display) / fadeOut));
        if (alpha <= 0.0) {
            return;
        }
        double s = this.size.get();
        Color accent = ThemeAccent.get(alpha);
        if (this.glow.get().booleanValue()) {
            this.drawGlow(event, s * 1.5, alpha);
        }
        this.drawCross(event, s, accent);
        if (this.circle.get().booleanValue()) {
            this.drawCircle(event, s * 1.8, accent);
        }
    }

    private void drawCross(Render3DEvent event, double s, Color color) {
        Vec3 cam = this.mc.gameRenderer.getMainCamera().position();
        Vec3 dir = this.effectPosition.subtract(cam);
        double dist = dir.length();
        if (dist < 1.0E-4) {
            return;
        }
        Vec3 n = dir.scale(1.0 / dist);
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        Vec3 right = n.cross(up).normalize().scale(s);
        Vec3 upV = right.cross(n).normalize().scale(s);
        Vec3 a = this.effectPosition.add(right).add(upV);
        Vec3 b = this.effectPosition.subtract(right).subtract(upV);
        Vec3 c = this.effectPosition.subtract(right).add(upV);
        Vec3 d = this.effectPosition.add(right).subtract(upV);
        event.renderer.line(a.x, a.y, a.z, b.x, b.y, b.z, color, color);
        event.renderer.line(c.x, c.y, c.z, d.x, d.y, d.z, color, color);
    }

    private void drawCircle(Render3DEvent event, double r, Color color) {
        double x = this.effectPosition.x;
        double y = this.effectPosition.y;
        double z = this.effectPosition.z;
        int segments = 40;
        for (int i = 0; i < segments; ++i) {
            double a1 = (double)i / (double)segments * Math.PI * 2.0;
            double a2 = (double)(i + 1) / (double)segments * Math.PI * 2.0;
            event.renderer.line(x + Math.cos(a1) * r, y, z + Math.sin(a1) * r, x + Math.cos(a2) * r, y, z + Math.sin(a2) * r, color, color);
        }
    }

    private void drawGlow(Render3DEvent event, double s, double alpha) {
        Vec3 cam = this.mc.gameRenderer.getMainCamera().position();
        Vec3 dir = this.effectPosition.subtract(cam);
        double dist = dir.length();
        if (dist < 1.0E-4) {
            return;
        }
        Vec3 n = dir.scale(1.0 / dist);
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        Vec3 right = n.cross(up).normalize().scale(s);
        Vec3 upV = right.cross(n).normalize().scale(s);
        Vec3 a = this.effectPosition.add(right).add(upV);
        Vec3 b = this.effectPosition.subtract(right).subtract(upV);
        Vec3 c = this.effectPosition.subtract(right).add(upV);
        Vec3 d = this.effectPosition.add(right).subtract(upV);
        Color inner = ThemeAccent.get(alpha * 0.5);
        Color outer = ThemeAccent.get(0.0);
        event.renderer.line(a.x, a.y, a.z, b.x, b.y, b.z, inner, outer);
        event.renderer.line(c.x, c.y, c.z, d.x, d.y, d.z, inner, outer);
    }
}

