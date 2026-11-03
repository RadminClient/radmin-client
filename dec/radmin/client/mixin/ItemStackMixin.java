/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.entity.player.FinishUsingItemEvent;
import radmin.client.events.entity.player.StoppedUsingItemEvent;
import radmin.client.events.game.ItemStackTooltipEvent;
import radmin.client.utils.Utils;

@Mixin(value={ItemStack.class})
public abstract class ItemStackMixin {
    @ModifyReturnValue(method={"getTooltipLines"}, at={@At(value="RETURN")})
    private List<Component> onGetTooltipLines(List<Component> original) {
        if (Utils.canUpdate()) {
            ItemStackTooltipEvent event = (ItemStackTooltipEvent)MeteorClient.EVENT_BUS.post((Object)new ItemStackTooltipEvent((ItemStack)this, original));
            return event.list();
        }
        return original;
    }

    @Inject(method={"finishUsingItem"}, at={@At(value="HEAD")})
    private void onFinishUsingItem(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (livingEntity == MeteorClient.mc.player) {
            MeteorClient.EVENT_BUS.post((Object)FinishUsingItemEvent.get((ItemStack)this));
        }
    }

    @Inject(method={"releaseUsing"}, at={@At(value="HEAD")})
    private void onReleaseUsing(Level level, LivingEntity entity, int remainingTime, CallbackInfo ci) {
        if (entity == MeteorClient.mc.player) {
            MeteorClient.EVENT_BUS.post((Object)StoppedUsingItemEvent.get((ItemStack)this));
        }
    }
}

