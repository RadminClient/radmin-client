/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.multiplayer.prediction.PredictiveAction
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
 *  net.minecraft.network.protocol.game.ServerboundPlayerActionPacket$Action
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerInput
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.entity.DropItemsEvent;
import radmin.client.events.entity.player.AttackEntityEvent;
import radmin.client.events.entity.player.BlockBreakingCooldownEvent;
import radmin.client.events.entity.player.BreakBlockEvent;
import radmin.client.events.entity.player.InteractBlockEvent;
import radmin.client.events.entity.player.InteractEntityEvent;
import radmin.client.events.entity.player.InteractItemEvent;
import radmin.client.events.entity.player.StartBreakingBlockEvent;
import radmin.client.mixininterface.IMultiPlayerGameMode;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.player.BreakDelay;
import radmin.client.systems.modules.player.SpeedMine;
import radmin.client.utils.world.BlockUtils;

@Mixin(value={MultiPlayerGameMode.class})
public abstract class MultiPlayerGameModeMixin
implements IMultiPlayerGameMode {
    @Shadow
    private int destroyDelay;

    @Shadow
    protected abstract void ensureHasSentCarriedItem();

    @Shadow
    public abstract boolean destroyBlock(BlockPos var1);

    @Shadow
    public abstract void startPrediction(ClientLevel var1, PredictiveAction var2);

    @Inject(method={"handleContainerInput"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleInventoryMouseClick(int containerId, int slotNum, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        if (containerInput == ContainerInput.THROW && slotNum >= 0 && slotNum < player.containerMenu.slots.size()) {
            if (((DropItemsEvent)MeteorClient.EVENT_BUS.post((ICancellable)DropItemsEvent.get(((Slot)player.containerMenu.slots.get(slotNum)).getItem()))).isCancelled()) {
                ci.cancel();
            }
        } else if (slotNum == -999 && ((DropItemsEvent)MeteorClient.EVENT_BUS.post((ICancellable)DropItemsEvent.get(player.containerMenu.getCarried()))).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"startDestroyBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onStartDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (((StartBreakingBlockEvent)MeteorClient.EVENT_BUS.post((ICancellable)StartBreakingBlockEvent.get(pos, direction))).isCancelled()) {
            cir.cancel();
        } else {
            SpeedMine sm = Modules.get().get(SpeedMine.class);
            BlockState state = MeteorClient.mc.level.getBlockState(pos);
            if (!sm.instamine() || !sm.filter(state.getBlock())) {
                return;
            }
            if (state.getDestroyProgress((Player)MeteorClient.mc.player, (BlockGetter)MeteorClient.mc.level, pos) > 0.5f) {
                this.destroyBlock(pos);
                this.startPrediction(MeteorClient.mc.level, sequence -> new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence));
                this.startPrediction(MeteorClient.mc.level, sequence -> new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, direction, sequence));
                cir.setReturnValue((Object)true);
            }
        }
    }

    @Inject(method={"useItemOn"}, at={@At(value="HEAD")}, cancellable=true)
    public void useItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir) {
        if (((InteractBlockEvent)MeteorClient.EVENT_BUS.post((ICancellable)InteractBlockEvent.get(player.getMainHandItem().isEmpty() ? InteractionHand.OFF_HAND : hand, blockHit))).isCancelled()) {
            cir.setReturnValue((Object)InteractionResult.FAIL);
        }
    }

    @Inject(method={"attack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttack(Player player, Entity entity, CallbackInfo ci) {
        if (((AttackEntityEvent)MeteorClient.EVENT_BUS.post((ICancellable)AttackEntityEvent.get(entity))).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"interact"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInteract(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (((InteractEntityEvent)MeteorClient.EVENT_BUS.post((ICancellable)InteractEntityEvent.get(entity, hand))).isCancelled()) {
            cir.setReturnValue((Object)InteractionResult.FAIL);
        }
    }

    @Inject(method={"handleCreativeModeItemDrop"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleCreativeModeItemDrop(ItemStack clicked, CallbackInfo ci) {
        if (((DropItemsEvent)MeteorClient.EVENT_BUS.post((ICancellable)DropItemsEvent.get(clicked))).isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method={"continueDestroyBlock"}, at=@At(value="FIELD", target="Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode=181, ordinal=1))
    private void creativeBreakDelayChange(MultiPlayerGameMode interactionManager, int value) {
        BlockBreakingCooldownEvent event = (BlockBreakingCooldownEvent)MeteorClient.EVENT_BUS.post((Object)BlockBreakingCooldownEvent.get(value));
        this.destroyDelay = event.cooldown;
    }

    @Redirect(method={"continueDestroyBlock"}, at=@At(value="FIELD", target="Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode=181, ordinal=2))
    private void survivalBreakDelayChange(MultiPlayerGameMode interactionManager, int value) {
        BlockBreakingCooldownEvent event = (BlockBreakingCooldownEvent)MeteorClient.EVENT_BUS.post((Object)BlockBreakingCooldownEvent.get(value));
        this.destroyDelay = event.cooldown;
    }

    @Redirect(method={"startDestroyBlock"}, at=@At(value="FIELD", target="Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode=181))
    private void creativeBreakDelayChange2(MultiPlayerGameMode interactionManager, int value) {
        BlockBreakingCooldownEvent event = (BlockBreakingCooldownEvent)MeteorClient.EVENT_BUS.post((Object)BlockBreakingCooldownEvent.get(value));
        this.destroyDelay = event.cooldown;
    }

    @ModifyExpressionValue(method={"continueDestroyBlock"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F")})
    private float modifyBlockBreakingDelta(float original) {
        if (Modules.get().get(BreakDelay.class).preventInstaBreak() && original >= 1.0f) {
            BlockBreakingCooldownEvent event = (BlockBreakingCooldownEvent)MeteorClient.EVENT_BUS.post((Object)BlockBreakingCooldownEvent.get(this.destroyDelay));
            this.destroyDelay = event.cooldown;
            return 0.0f;
        }
        return original;
    }

    @Inject(method={"destroyBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (((BreakBlockEvent)MeteorClient.EVENT_BUS.post((ICancellable)BreakBlockEvent.get(pos))).isCancelled()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"useItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onUseItem(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        InteractItemEvent event = (InteractItemEvent)MeteorClient.EVENT_BUS.post((Object)InteractItemEvent.get(hand));
        if (event.toReturn != null) {
            cir.setReturnValue((Object)event.toReturn);
        }
    }

    @Inject(method={"stopDestroyBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onStopDestroyBlock(CallbackInfo ci) {
        if (BlockUtils.breaking) {
            ci.cancel();
        }
    }

    @Override
    public void meteor$syncSelected() {
        this.ensureHasSentCarriedItem();
    }
}

