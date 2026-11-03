/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package radmin.client.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import radmin.client.mixininterface.IServerboundMovePlayerPacket;

@Mixin(value={ServerboundMovePlayerPacket.class})
public abstract class ServerboundMovePlayerPacketMixin
implements IServerboundMovePlayerPacket {
    @Unique
    private int tag;

    @Override
    public void meteor$setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public int meteor$getTag() {
        return this.tag;
    }
}

