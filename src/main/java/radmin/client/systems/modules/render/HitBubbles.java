/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.resources.Identifier
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.render;

import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import radmin.client.MeteorClient;
import radmin.client.events.entity.player.AttackEntityEvent;
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
import radmin.client.utils.render.WorldTexturedRenderer;
import radmin.client.utils.render.color.Color;

public class HitBubbles
extends Module {
    private static final long LIFE_MS = 1600L;
    private static final Identifier BUBBLE_TEXTURE = MeteorClient.identifier("textures/hitbubble/bubble.png");
    private final SettingGroup sgGeneral;
    private final Setting<Double> size;
    private final List<Bubble> bubbles;

    public HitBubbles() {
        super(Categories.Render, "hit-bubbles", "Renders a bubble at the position you hit an entity.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.size = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("size")).description("Size of the bubble in blocks.")).defaultValue(1.0).min(0.25).sliderRange(0.5, 2.0).build());
        this.bubbles = new ArrayList<Bubble>();
    }

    @Override
    public void onDeactivate() {
        this.bubbles.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        long now = System.currentTimeMillis();
        this.bubbles.removeIf(b -> now - b.spawnTime >= 1600L);
    }

    @EventHandler
    private void onAttack(AttackEntityEvent event) {
        if (this.mc.player == null || this.mc.level == null || event.entity == null) {
            return;
        }
        Entity entity = event.entity;
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity)entity;
        Vec3 sideDir = this.getHitSideDirection(living, this.mc.player.getPosition(1.0f));
        Vec3 pos = this.getHitPosition(living, sideDir);
        this.bubbles.add(new Bubble(pos, System.currentTimeMillis(), (float)(Math.random() * 360.0)));
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (this.mc.player == null || this.bubbles.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        MeshBuilder mesh = new MeshBuilder(MeteorRenderPipelines.WORLD_TEXTURED);
        mesh.begin();
        for (Bubble bubble : this.bubbles) {
            double progress = (double)(now - bubble.spawnTime) / 1600.0;
            if (progress >= 1.0) continue;
            double inPhase = Mth.clamp((double)(progress / 0.22), (double)0.0, (double)1.0);
            double outPhase = Mth.clamp((double)((progress - 0.225) / 0.4), (double)0.0, (double)1.0);
            double scaleIn = inPhase * inPhase * (3.0 - 2.0 * inPhase);
            double scaleOut = 1.0 - outPhase * outPhase;
            double scale = this.size.get() * (0.02 + 1.55 * scaleIn * scaleOut);
            double alpha = 1.0 - outPhase * outPhase * outPhase;
            if (alpha <= 0.01) continue;
            double rotation = (double)(now - bubble.spawnTime) / 1.5 + bubble.spinSeed;
            Color color = ThemeAccent.get(alpha);
            WorldTexturedRenderer.addBillboard(mesh, bubble.position, scale, scale, rotation, color);
        }
        if (mesh.getIndicesCount() > 0) {
            MeshRenderer.begin().attachments(this.mc.getMainRenderTarget()).pipeline(MeteorRenderPipelines.WORLD_TEXTURED).sampler("u_Texture", this.mc.getTextureManager().getTexture(BUBBLE_TEXTURE).getTextureView(), this.mc.getTextureManager().getTexture(BUBBLE_TEXTURE).getSampler()).mesh(mesh, event.matrices).end();
        } else {
            mesh.end();
        }
    }

    private Vec3 getHitSideDirection(LivingEntity target, Vec3 attackerPos) {
        Vec3 dir = attackerPos.subtract(target.getPosition(1.0f));
        dir = new Vec3(dir.x, 0.0, dir.z);
        if (dir.length() < 0.01) {
            Vec3 fallback = target.getViewVector(1.0f);
            dir = new Vec3(fallback.x, 0.0, fallback.z);
        }
        if (dir.length() < 0.01) {
            dir = new Vec3(0.0, 0.0, 1.0);
        }
        return dir.normalize();
    }

    private Vec3 getHitPosition(LivingEntity target, Vec3 sideDir) {
        Vec3 head = new Vec3(target.getX(), target.getY() + (double)target.getBbHeight() + 0.18, target.getZ());
        return head.add(sideDir.scale(0.1));
    }

    private static class Bubble {
        private final Vec3 position;
        private final long spawnTime;
        private final double spinSeed;

        private Bubble(Vec3 position, long spawnTime, double spinSeed) {
            this.position = position;
            this.spawnTime = spawnTime;
            this.spinSeed = spinSeed;
        }
    }
}

