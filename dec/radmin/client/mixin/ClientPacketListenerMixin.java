/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.llamalad7.mixinextras.sugar.Share
 *  com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef
 *  com.llamalad7.mixinextras.sugar.ref.LocalFloatRef
 *  com.llamalad7.mixinextras.sugar.ref.LocalRef
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  it.unimi.dsi.fastutil.ints.IntListIterator
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.CommonListenerCookie
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
 *  net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
 *  net.minecraft.network.protocol.game.ClientboundExplodePacket
 *  net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
 *  net.minecraft.network.protocol.game.ClientboundLoginPacket
 *  net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
 *  net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
 *  net.minecraft.network.protocol.game.ClientboundSoundPacket
 *  net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket
 *  net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.level.chunk.LevelChunk
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.commands.Commands;
import radmin.client.events.entity.EntityDestroyEvent;
import radmin.client.events.entity.player.PickItemsEvent;
import radmin.client.events.game.GameJoinedEvent;
import radmin.client.events.game.GameLeftEvent;
import radmin.client.events.game.SendMessageEvent;
import radmin.client.events.packets.ContainerSlotUpdateEvent;
import radmin.client.events.packets.InventoryEvent;
import radmin.client.events.packets.PlaySoundPacketEvent;
import radmin.client.events.world.ChunkDataEvent;
import radmin.client.mixininterface.IClientboundExplodePacket;
import radmin.client.pathing.BaritoneUtils;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Velocity;
import radmin.client.systems.modules.player.NoRotate;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.utils.player.ChatUtils;

@Mixin(value={ClientPacketListener.class})
public abstract class ClientPacketListenerMixin
extends ClientCommonPacketListenerImpl {
    @Shadow
    private ClientLevel level;

    protected ClientPacketListenerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method={"handleAddEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleAddEntity(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        if (packet != null && packet.getType() != null && Modules.get().get(NoRender.class).noEntity(packet.getType()) && Modules.get().get(NoRender.class).getDropSpawnPacket()) {
            ci.cancel();
        }
    }

    @Inject(method={"handleLogin"}, at={@At(value="HEAD")})
    private void onHandleLoginHead(ClientboundLoginPacket packet, CallbackInfo ci, @Share(value="worldNotNull") LocalBooleanRef worldNotNull) {
        worldNotNull.set(this.level != null);
    }

    @Inject(method={"handleLogin"}, at={@At(value="TAIL")})
    private void onHandleLoginTail(ClientboundLoginPacket packet, CallbackInfo ci, @Share(value="worldNotNull") LocalBooleanRef worldNotNull) {
        if (worldNotNull.get()) {
            MeteorClient.EVENT_BUS.post((Object)GameLeftEvent.get());
        }
        MeteorClient.EVENT_BUS.post((Object)GameJoinedEvent.get());
    }

    @Inject(method={"handleConfigurationStart"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift=At.Shift.AFTER)})
    private void onHandleConfigurationStart(ClientboundStartConfigurationPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)GameLeftEvent.get());
    }

    @Inject(method={"handleSoundEvent"}, at={@At(value="HEAD")})
    private void onHandleSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)PlaySoundPacketEvent.get(packet));
    }

    @Inject(method={"handleLevelChunkWithLight"}, at={@At(value="TAIL")})
    private void onHandleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
        LevelChunk chunk = this.minecraft.level.getChunk(packet.getX(), packet.getZ());
        MeteorClient.EVENT_BUS.post((Object)new ChunkDataEvent(chunk));
    }

    @Inject(method={"handleContainerSetSlot"}, at={@At(value="TAIL")})
    private void onHandleContainerSetSlot(ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)ContainerSlotUpdateEvent.get(packet));
    }

    @Inject(method={"handleContainerContent"}, at={@At(value="TAIL")})
    private void onHandleContainerContent(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)InventoryEvent.get(packet));
    }

    @Inject(method={"handleRemoveEntities"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/protocol/game/ClientboundRemoveEntitiesPacket;getEntityIds()Lit/unimi/dsi/fastutil/ints/IntList;")})
    private void onHandleRemoveEntities(ClientboundRemoveEntitiesPacket packet, CallbackInfo ci) {
        IntListIterator intListIterator = packet.getEntityIds().iterator();
        while (intListIterator.hasNext()) {
            int id = (Integer)intListIterator.next();
            MeteorClient.EVENT_BUS.post((Object)EntityDestroyEvent.get(this.minecraft.level.getEntity(id)));
        }
    }

    @Inject(method={"handleExplosion"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift=At.Shift.AFTER)})
    private void onHandleExplosionVelocity(ClientboundExplodePacket packet, CallbackInfo ci) {
        Velocity velocity = Modules.get().get(Velocity.class);
        if (!velocity.explosions.get().booleanValue()) {
            return;
        }
        IClientboundExplodePacket explosionPacket = (IClientboundExplodePacket)packet;
        explosionPacket.meteor$setVelocityX((float)(packet.playerKnockback().orElse(Vec3.ZERO).x * velocity.getHorizontal(velocity.explosionsHorizontal)));
        explosionPacket.meteor$setVelocityY((float)(packet.playerKnockback().orElse(Vec3.ZERO).y * velocity.getVertical(velocity.explosionsVertical)));
        explosionPacket.meteor$setVelocityZ((float)(packet.playerKnockback().orElse(Vec3.ZERO).z * velocity.getHorizontal(velocity.explosionsHorizontal)));
    }

    @Inject(method={"handleTakeItemEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/ClientLevel;getEntity(I)Lnet/minecraft/world/entity/Entity;", ordinal=0)})
    private void onHandleTakeItemEntity(ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        Entity itemEntity = this.minecraft.level.getEntity(packet.getItemId());
        Entity entity = this.minecraft.level.getEntity(packet.getPlayerId());
        if (itemEntity instanceof ItemEntity) {
            ItemEntity item = (ItemEntity)itemEntity;
            if (entity == this.minecraft.player) {
                MeteorClient.EVENT_BUS.post((Object)PickItemsEvent.get(item.getItem(), packet.getAmount()));
            }
        }
    }

    @Inject(method={"handleMovePlayer"}, at={@At(value="HEAD")})
    private void onHandleMovePlayerHead(ClientboundPlayerPositionPacket packet, CallbackInfo ci, @Share(value="noRotateYaw") LocalFloatRef yawRef, @Share(value="noRotatePitch") LocalFloatRef pitchRef) {
        NoRotate noRotate = Modules.get().get(NoRotate.class);
        if (!noRotate.isActive() || this.minecraft.player == null) {
            return;
        }
        yawRef.set(this.minecraft.player.getYRot());
        pitchRef.set(this.minecraft.player.getXRot());
    }

    @Inject(method={"handleMovePlayer"}, at={@At(value="RETURN")})
    private void onHandleMovePlayerReturn(ClientboundPlayerPositionPacket packet, CallbackInfo ci, @Share(value="noRotateYaw") LocalFloatRef yawRef, @Share(value="noRotatePitch") LocalFloatRef pitchRef) {
        NoRotate noRotate = Modules.get().get(NoRotate.class);
        if (!noRotate.isActive() || this.minecraft.player == null) {
            return;
        }
        float savedYaw = yawRef.get();
        float savedPitch = pitchRef.get();
        this.minecraft.player.setYRot(savedYaw + 1.0E-6f);
        this.minecraft.player.setXRot(savedPitch + 1.0E-6f);
        this.minecraft.player.yHeadRot = savedYaw;
        this.minecraft.player.yBodyRot = savedYaw;
    }

    @Inject(method={"sendChat"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendChatMessage(String message, CallbackInfo ci, @Local(argsOnly=true, name={"content"}) LocalRef<String> messageRef) {
        if (!(message.startsWith(Config.get().prefix.get()) || BaritoneUtils.IS_AVAILABLE && message.startsWith(BaritoneUtils.getPrefix()))) {
            SendMessageEvent event = (SendMessageEvent)MeteorClient.EVENT_BUS.post((ICancellable)SendMessageEvent.get(message));
            if (!event.isCancelled()) {
                messageRef.set((Object)event.message);
            } else {
                ci.cancel();
            }
            return;
        }
        if (message.startsWith(Config.get().prefix.get())) {
            try {
                Commands.dispatch(message.substring(Config.get().prefix.get().length()));
            }
            catch (CommandSyntaxException e) {
                ChatUtils.error(e.getMessage(), new Object[0]);
            }
            this.minecraft.gui.getChat().addRecentChat(message);
            ci.cancel();
        }
    }
}

