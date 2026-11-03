/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.resources.Identifier
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.render;

import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import radmin.client.MeteorClient;
import radmin.client.events.render.Render3DEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.renderer.MeshBuilder;
import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class JumpCircle
extends Module {
    private static final double MAX_LIFETIME_MS = 1850.0;
    private static final double ROTATION_SPEED = 120.0;
    private static final double PULSE_SPEED = 7.0;
    private static final double PULSE_SCALE = 0.06;
    private static final double PULSE_ALPHA = 0.12;
    private static final int MAX_CIRCLES = 8;
    private static final Identifier CIRCLE_TEXTURE = MeteorClient.identifier("textures/jumpcircle/circle.png");
    private final SettingGroup sgGeneral;
    private final Setting<Double> radius;
    private final Setting<Double> speed;
    private final Setting<Double> fadeSpeed;
    private final List<CircleData> circles;
    private boolean wasOnGround;

    public JumpCircle() {
        super(Categories.Render, "jump-circle", "Renders a circle at the position you jump from.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.radius = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("radius")).description("Radius of the circle.")).defaultValue(1.85).min(0.5).sliderRange(0.5, 4.0).build());
        this.speed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("speed")).description("Speed of the circle.")).defaultValue(1.2).min(1.0).sliderRange(1.0, 5.0).build());
        this.fadeSpeed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("fade-speed")).description("How fast the circle fades out.")).defaultValue(1.5).min(1.0).sliderRange(1.0, 5.0).build());
        this.circles = new ArrayList<CircleData>();
        this.wasOnGround = true;
    }

    @Override
    public void onActivate() {
        this.wasOnGround = this.mc.player != null && this.mc.player.onGround();
    }

    @Override
    public void onDeactivate() {
        this.circles.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (this.mc.player == null || this.mc.level == null) {
            return;
        }
        boolean isOnGround = this.mc.player.onGround();
        if (this.wasOnGround && !isOnGround && this.mc.player.getDeltaMovement().y > 0.0) {
            this.circles.add(new CircleData(new Vec3(this.mc.player.getX(), Math.floor(this.mc.player.getY()) + 0.001, this.mc.player.getZ()), System.currentTimeMillis()));
            while (this.circles.size() > 8) {
                this.circles.remove(0);
            }
        }
        this.wasOnGround = isOnGround;
        long now = System.currentTimeMillis();
        this.circles.removeIf(circle -> (double)(now - circle.startTime) > 1850.0);
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (this.mc.player == null || this.circles.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        MeshBuilder mesh = new MeshBuilder(MeteorRenderPipelines.WORLD_TEXTURED);
        mesh.begin();
        for (CircleData circle : this.circles) {
            double alpha;
            double progress = (double)(now - circle.startTime) / this.getLifeTimeMs();
            if (progress >= 1.0 || (alpha = 1.0 - Mth.clamp((double)(progress * this.fadeSpeed.get()), (double)0.0, (double)1.0)) <= 0.01) continue;
            double lifeTimeSec = (double)(now - circle.startTime) / 1000.0;
            double easedProgress = JumpCircle.easeOutCubic(progress);
            double scale = Math.min(easedProgress * this.radius.get(), this.radius.get());
            double rotation = lifeTimeSec * 120.0 * this.speed.get();
            rotation += Math.sin(progress * Math.PI * 2.0) * 30.0;
            double pulse = Math.sin(lifeTimeSec * 7.0 * this.speed.get());
            double pulseScale = 1.0 + pulse * 0.06;
            double pulseAlpha = Mth.clamp((double)(alpha * (1.0 + pulse * 0.12)), (double)0.0, (double)1.0);
            double alphaBoost = Mth.clamp((double)(pulseAlpha * 1.25), (double)0.0, (double)1.0);
            double finalScale = scale * pulseScale;
            Color colorA = ThemeAccent.get(alphaBoost);
            Color colorDark = ThemeAccent.get(Mth.clamp((double)(alphaBoost * 0.9), (double)0.0, (double)1.0), 0.65);
            double half = finalScale * 0.5;
            double thickHalf = finalScale * 1.08 * 0.5;
            this.addQuad(mesh, circle.position, half, rotation, colorA);
            this.addQuad(mesh, circle.position, thickHalf, rotation, colorDark);
        }
        if (mesh.getIndicesCount() > 0) {
            MeshRenderer.begin().attachments(this.mc.getMainRenderTarget()).pipeline(MeteorRenderPipelines.WORLD_TEXTURED).sampler("u_Texture", this.mc.getTextureManager().getTexture(CIRCLE_TEXTURE).getTextureView(), this.mc.getTextureManager().getTexture(CIRCLE_TEXTURE).getSampler()).mesh(mesh, event.matrices).end();
        } else {
            mesh.end();
        }
    }

    private void addQuad(MeshBuilder mesh, Vec3 pos, double half, double rotation, Color color) {
        mesh.ensureQuadCapacity();
        double rad = Math.toRadians(rotation);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double[][] corners = new double[][]{{-half, -half}, {-half, half}, {half, half}, {half, -half}};
        float[][] uvs = new float[][]{{0.0f, 0.0f}, {0.0f, 1.0f}, {1.0f, 1.0f}, {1.0f, 0.0f}};
        int[] idx = new int[4];
        for (int i = 0; i < 4; ++i) {
            double lx = corners[i][0];
            double lz = corners[i][1];
            double wx = lx * cos - lz * sin;
            double wz = lx * sin + lz * cos;
            idx[i] = mesh.vec3(pos.x + wx, pos.y, pos.z + wz).vec2(uvs[i][0], uvs[i][1]).color(color).next();
        }
        mesh.quad(idx[0], idx[1], idx[2], idx[3]);
    }

    private double getLifeTimeMs() {
        return 1850.0 / Math.max(0.25, this.speed.get());
    }

    private static double easeOutCubic(double t) {
        double u = 1.0 - t;
        return 1.0 - u * u * u;
    }

    private static class CircleData {
        private final Vec3 position;
        private final long startTime;

        private CircleData(Vec3 position, long startTime) {
            this.position = position;
            this.startTime = startTime;
        }
    }
}

