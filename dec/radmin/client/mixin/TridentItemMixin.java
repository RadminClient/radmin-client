/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TridentItem
 *  net.minecraft.world.level.Level
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import radmin.client.MeteorClient;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.TridentBoost;
import radmin.client.utils.Utils;

@Mixin(value={TridentItem.class})
public abstract class TridentItemMixin {
    @Inject(method={"releaseUsing"}, at={@At(value="HEAD")})
    private void onReleaseUsingHead(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
        if (entity == MeteorClient.mc.player) {
            Utils.isReleasingTrident = true;
        }
    }

    @Inject(method={"releaseUsing"}, at={@At(value="TAIL")})
    private void onReleaseUsingTail(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
        if (entity == MeteorClient.mc.player) {
            Utils.isReleasingTrident = false;
        }
    }

    @ModifyArgs(method={"releaseUsing"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;push(DDD)V"))
    private void modifyVelocity(Args args) {
        TridentBoost tridentBoost = Modules.get().get(TridentBoost.class);
        args.set(0, (Object)((Double)args.get(0) * tridentBoost.getMultiplier()));
        args.set(1, (Object)((Double)args.get(1) * tridentBoost.getMultiplier()));
        args.set(2, (Object)((Double)args.get(2) * tridentBoost.getMultiplier()));
    }

    @ModifyExpressionValue(method={"use"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z")})
    private boolean isInWaterUse(boolean original) {
        TridentBoost tridentBoost = Modules.get().get(TridentBoost.class);
        return tridentBoost.allowOutOfWater() || original;
    }

    @ModifyExpressionValue(method={"releaseUsing"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z")})
    private boolean isInWaterPostUse(boolean original) {
        TridentBoost tridentBoost = Modules.get().get(TridentBoost.class);
        return tridentBoost.allowOutOfWater() || original;
    }
}

