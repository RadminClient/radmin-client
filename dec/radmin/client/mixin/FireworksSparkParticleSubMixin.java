/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.particle.FireworkParticles$OverlayParticle
 *  net.minecraft.client.particle.FireworkParticles$SparkParticle
 *  net.minecraft.client.renderer.state.level.QuadParticleRenderState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={FireworkParticles.SparkParticle.class, FireworkParticles.OverlayParticle.class})
public abstract class FireworksSparkParticleSubMixin {
    @Inject(method={"extract"}, at={@At(value="HEAD")}, cancellable=true)
    private void buildExplosionGeometry(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).noFireworkExplosions()) {
            ci.cancel();
        }
    }
}

