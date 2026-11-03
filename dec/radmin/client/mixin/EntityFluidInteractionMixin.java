/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityFluidInteraction
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.MeteorClient;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Velocity;

@Mixin(value={EntityFluidInteraction.class})
public abstract class EntityFluidInteractionMixin {
    @ModifyExpressionValue(method={"update"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/level/material/FluidState;getFlow(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;")})
    private Vec3 modifyFluidFlow(Vec3 flow, Entity entity, boolean ignoreCurrent) {
        if (entity != MeteorClient.mc.player) {
            return flow;
        }
        Velocity velocity = Modules.get().get(Velocity.class);
        if (velocity.isActive() && velocity.liquids.get().booleanValue()) {
            double h = velocity.getHorizontal(velocity.liquidsHorizontal);
            double v = velocity.getVertical(velocity.liquidsVertical);
            flow = flow.multiply(h, v, h);
        }
        return flow;
    }
}

