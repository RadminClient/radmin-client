/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import radmin.client.MeteorClient;
import radmin.client.events.entity.player.AttackEntityEvent;
import radmin.client.events.render.Render3DEvent;
import radmin.client.renderer.MeshBuilder;
import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.WorldTexturedRenderer;
import radmin.client.utils.render.color.Color;

public class HitMarker
extends Module {
    private static final Identifier CROSS_TEXTURE = MeteorClient.identifier("textures/cross/cross.png");
    private final SettingGroup sgGeneral;
    private final Setting<Double> size;
    private final Setting<Double> fadeInTime;
    private final Setting<Double> displayTime;
    private final Setting<Double> fadeOutTime;
    private final Setting<Boolean> glow;
    private final Setting<Boolean> scale;
    private final List<Marker> markers;

    public HitMarker() {
        super(Categories.Render, "hit-marker", "Renders a marker at the position you hit an entity.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.size = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("size")).description("Size of the marker in blocks.")).defaultValue(0.5).min(0.1).sliderRange(0.1, 2.0).build());
        this.fadeInTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("fade-in-time")).description("Fade in time in milliseconds.")).defaultValue(100.0).min(50.0).sliderRange(50.0, 500.0).build());
        this.displayTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("display-time")).description("Display time in milliseconds.")).defaultValue(300.0).min(100.0).sliderRange(100.0, 1000.0).build());
        this.fadeOutTime = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("fade-out-time")).description("Fade out time in milliseconds.")).defaultValue(200.0).min(50.0).sliderRange(50.0, 500.0).build());
        this.glow = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("glow")).description("Renders a glow around the marker.")).defaultValue(true)).build());
        this.scale = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("scale-animation")).description("Scales the marker in when it appears.")).defaultValue(true)).build());
        this.markers = new ArrayList<Marker>();
    }

    @Override
    public void onDeactivate() {
        this.markers.clear();
    }

    @EventHandler
    private void onAttack(AttackEntityEvent event) {
        if (this.mc.player == null || this.mc.level == null || event.entity == null) {
            return;
        }
        this.markers.add(new Marker(this.resolveHitPosition((Entity)this.mc.player, event.entity)));
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (this.mc.player == null || this.markers.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        this.markers.removeIf(marker -> now - marker.birthTime > this.fadeInTime.get().longValue() + this.displayTime.get().longValue() + this.fadeOutTime.get().longValue());
        if (this.markers.isEmpty()) {
            return;
        }
        MeshBuilder mesh = new MeshBuilder(MeteorRenderPipelines.WORLD_TEXTURED);
        mesh.begin();
        for (Marker marker2 : this.markers) {
            float alpha = marker2.getAlpha(this.fadeInTime.get(), this.displayTime.get(), this.fadeOutTime.get());
            if (alpha <= 0.0f) continue;
            double scaleMultiplier = marker2.getScaleMultiplier(this.fadeInTime.get(), this.displayTime.get(), this.fadeOutTime.get(), this.scale.get());
            double s = this.size.get() * scaleMultiplier;
            if (s <= 0.0) continue;
            Color color = ThemeAccent.get(alpha);
            if (this.glow.get().booleanValue()) {
                Color glowColor = ThemeAccent.get((double)alpha * 0.35);
                WorldTexturedRenderer.addBillboard(mesh, marker2.position, s * 1.5, s * 1.5, 0.0, glowColor);
            }
            WorldTexturedRenderer.addBillboard(mesh, marker2.position, s, s, 0.0, color);
        }
        if (mesh.getIndicesCount() > 0) {
            MeshRenderer.begin().attachments(this.mc.getMainRenderTarget()).pipeline(MeteorRenderPipelines.WORLD_TEXTURED).sampler("u_Texture", this.mc.getTextureManager().getTexture(CROSS_TEXTURE).getTextureView(), this.mc.getTextureManager().getTexture(CROSS_TEXTURE).getSampler()).mesh(mesh, event.matrices).end();
        } else {
            mesh.end();
        }
    }

    private Vec3 resolveHitPosition(Entity attacker, Entity target) {
        EntityHitResult hit;
        HitResult hitResult = this.mc.hitResult;
        if (hitResult instanceof EntityHitResult && (hit = (EntityHitResult)hitResult).getEntity() == target) {
            return hit.getLocation();
        }
        Vec3 fallback = new Vec3(target.getX(), target.getY() + (double)target.getBbHeight() / 2.0, target.getZ());
        Vec3 eyePos = attacker.getEyePosition(1.0f);
        Vec3 lookVec = attacker.getViewVector(1.0f);
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        double distance = Math.max(eyePos.distanceTo(targetCenter) + 1.0, 6.0);
        Vec3 reachPos = eyePos.add(lookVec.scale(distance));
        Optional hitPos = target.getBoundingBox().clip(eyePos, reachPos);
        return hitPos.orElseGet(() -> eyePos.add(lookVec.scale(eyePos.distanceTo(targetCenter))));
    }

    private static class Marker {
        private final Vec3 position;
        private final long birthTime;

        private Marker(Vec3 position) {
            this.position = position;
            this.birthTime = System.currentTimeMillis();
        }

        private float getAlpha(double fadeInTime, double displayTime, double fadeOutTime) {
            long elapsed = System.currentTimeMillis() - this.birthTime;
            if ((double)elapsed < fadeInTime) {
                return Marker.easeOutCubic((float)((double)elapsed / fadeInTime));
            }
            if ((double)elapsed < fadeInTime + displayTime) {
                return 1.0f;
            }
            float progress = Math.min(1.0f, (float)(((double)elapsed - fadeInTime - displayTime) / fadeOutTime));
            return 1.0f - Marker.easeInCubic(progress);
        }

        private double getScaleMultiplier(double fadeInTime, double displayTime, double fadeOutTime, boolean scale) {
            if (!scale) {
                return 1.0;
            }
            long elapsed = System.currentTimeMillis() - this.birthTime;
            if ((double)elapsed < fadeInTime) {
                float progress = (float)((double)elapsed / fadeInTime);
                return 0.5 + 0.5 * (double)Marker.easeOutBack(progress);
            }
            if ((double)elapsed < fadeInTime + displayTime) {
                return 1.0;
            }
            float progress = Math.min(1.0f, (float)(((double)elapsed - fadeInTime - displayTime) / fadeOutTime));
            return 1.0 - 0.3 * (double)Marker.easeInCubic(progress);
        }

        private static float easeOutCubic(float x) {
            return 1.0f - (float)Math.pow(1.0 - (double)x, 3.0);
        }

        private static float easeInCubic(float x) {
            return x * x * x;
        }

        private static float easeOutBack(float x) {
            float c1 = 1.70158f;
            float c3 = c1 + 1.0f;
            return 1.0f + c3 * (float)Math.pow((double)x - 1.0, 3.0) + c1 * (float)Math.pow((double)x - 1.0, 2.0);
        }
    }
}

