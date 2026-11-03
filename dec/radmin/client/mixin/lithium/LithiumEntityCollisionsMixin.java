/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.lithium.common.entity.LithiumEntityCollisions
 *  net.minecraft.world.level.border.WorldBorder
 *  net.minecraft.world.phys.AABB
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin.lithium;

import net.caffeinemc.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.Collisions;

@Mixin(value={LithiumEntityCollisions.class})
public abstract class LithiumEntityCollisionsMixin {
    @Inject(method={"isWithinWorldBorder"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onIsWithinWorldBorder(WorldBorder border, AABB box, CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(Collisions.class).ignoreBorder()) {
            cir.setReturnValue((Object)true);
        }
    }
}

