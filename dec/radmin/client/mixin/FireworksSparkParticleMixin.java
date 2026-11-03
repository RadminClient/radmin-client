/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.client.particle.FireworkParticles$SparkParticle
 *  net.minecraft.client.particle.FireworkParticles$Starter
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.particle.FireworkParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={FireworkParticles.Starter.class})
public abstract class FireworksSparkParticleMixin {
    @Inject(method={"createParticle"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/particle/FireworkParticles$SparkParticle;setTrail(Z)V")}, cancellable=true)
    private void onAddExplosion(double x, double y, double z, double xa, double ya, double za, IntList rgbColors, IntList fadeColors, boolean trail, boolean twinkle, CallbackInfo ci, @Local(name={"sparkParticle"}) FireworkParticles.SparkParticle sparkParticle) {
        if (sparkParticle == null) {
            ci.cancel();
        }
    }
}

