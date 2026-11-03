/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NbtAccounter
 *  net.minecraft.network.FriendlyByteBuf
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package radmin.client.mixin;

import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.AntiPacketKick;

@Mixin(value={FriendlyByteBuf.class})
public abstract class FriendlyByteBufMixin {
    @ModifyArg(method={"readNbt(Lio/netty/buffer/ByteBuf;)Lnet/minecraft/nbt/CompoundTag;"}, at=@At(value="INVOKE", target="Lnet/minecraft/network/FriendlyByteBuf;readNbt(Lio/netty/buffer/ByteBuf;Lnet/minecraft/nbt/NbtAccounter;)Lnet/minecraft/nbt/Tag;"))
    private static NbtAccounter xlPackets(NbtAccounter sizeTracker) {
        return Modules.get().isActive(AntiPacketKick.class) ? NbtAccounter.unlimitedHeap() : sizeTracker;
    }
}

