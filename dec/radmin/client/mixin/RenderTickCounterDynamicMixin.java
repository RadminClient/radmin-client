/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.DeltaTracker$Timer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.Timer;

@Mixin(value={DeltaTracker.Timer.class})
public abstract class RenderTickCounterDynamicMixin {
    @Shadow
    private float deltaTicks;

    @Inject(method={"advanceGameTime(J)I"}, at={@At(value="FIELD", target="Lnet/minecraft/client/DeltaTracker$Timer;lastMs:J", opcode=181)})
    private void onBeingRenderTick(long currentMs, CallbackInfoReturnable<Integer> cir) {
        this.deltaTicks *= (float)Modules.get().get(Timer.class).getMultiplier();
    }
}

