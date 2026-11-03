/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleEngine
 *  net.minecraft.core.particles.ParticleOptions
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.world.ParticleEvent;

@Mixin(value={ParticleEngine.class})
public abstract class ParticleEngineMixin {
    @Inject(method={"createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onCreateParticle(ParticleOptions options, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<Particle> cir) {
        ParticleEvent event = (ParticleEvent)MeteorClient.EVENT_BUS.post((ICancellable)ParticleEvent.get(options));
        if (event.isCancelled()) {
            cir.cancel();
        }
    }
}

