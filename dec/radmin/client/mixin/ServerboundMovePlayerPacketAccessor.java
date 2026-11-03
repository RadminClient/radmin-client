/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ServerboundMovePlayerPacket.class})
public interface ServerboundMovePlayerPacketAccessor {
    @Mutable
    @Accessor(value="y")
    public void meteor$setY(double var1);

    @Mutable
    @Accessor(value="onGround")
    public void meteor$setOnGround(boolean var1);
}

