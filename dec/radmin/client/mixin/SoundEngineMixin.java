/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.TickableSoundInstance
 *  net.minecraft.client.sounds.SoundEngine
 *  net.minecraft.client.sounds.SoundEngine$PlayResult
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.world.PlaySoundEvent;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.SoundBlocker;

@Mixin(value={SoundEngine.class})
public abstract class SoundEngineMixin {
    @Shadow
    public abstract void stop(SoundInstance var1);

    @Inject(method={"play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlay(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        PlaySoundEvent event = (PlaySoundEvent)MeteorClient.EVENT_BUS.post((ICancellable)PlaySoundEvent.get(instance));
        if (event.isCancelled()) {
            cir.cancel();
        }
    }

    @Inject(method={"tickInGameSound()V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/resources/sounds/TickableSoundInstance;tick()V", ordinal=0)})
    private void onTick(CallbackInfo ci, @Local(name={"instance"}) TickableSoundInstance instance) {
        if (Modules.get().get(SoundBlocker.class).shouldBlock((SoundInstance)instance)) {
            this.stop((SoundInstance)instance);
        }
    }
}

