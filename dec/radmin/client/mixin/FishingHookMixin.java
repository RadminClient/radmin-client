/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.FishingHook
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.MeteorClient;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Velocity;

@Mixin(value={FishingHook.class})
public abstract class FishingHookMixin {
    @WrapOperation(method={"handleEntityEvent"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/projectile/FishingHook;pullEntity(Lnet/minecraft/world/entity/Entity;)V")})
    private void preventFishingRodPull(FishingHook instance, Entity entity, Operation<Void> original) {
        Velocity velocity;
        if (!instance.level().isClientSide() || entity != MeteorClient.mc.player) {
            original.call(new Object[]{instance, entity});
        }
        if (!(velocity = Modules.get().get(Velocity.class)).isActive() || !velocity.fishing.get().booleanValue()) {
            original.call(new Object[]{instance, entity});
        }
    }
}

