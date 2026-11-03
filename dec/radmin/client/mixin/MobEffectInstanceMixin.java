/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.player.PotionSaver;
import radmin.client.utils.Utils;

@Mixin(value={MobEffectInstance.class})
public abstract class MobEffectInstanceMixin {
    @Inject(method={"tickDownDuration"}, at={@At(value="HEAD")}, cancellable=true)
    private void tick(CallbackInfo ci) {
        if (!Utils.canUpdate()) {
            return;
        }
        if (Modules.get().get(PotionSaver.class).shouldFreeze((MobEffect)((MobEffectInstance)this).getEffect().value())) {
            ci.cancel();
        }
    }
}

