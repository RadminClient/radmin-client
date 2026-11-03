/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.border.WorldBorder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.Collisions;

@Mixin(value={WorldBorder.class})
public abstract class WorldBorderMixin {
    @Inject(method={"isInsideCloseToBorder"}, at={@At(value="HEAD")}, cancellable=true)
    private void canCollide(CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(Collisions.class).ignoreBorder()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"isWithinBounds(Lnet/minecraft/core/BlockPos;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void contains(CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(Collisions.class).ignoreBorder()) {
            cir.setReturnValue((Object)true);
        }
    }
}

