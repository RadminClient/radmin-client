/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.AntiPacketKick;

@Mixin(targets={"net/minecraft/network/protocol/BundlerInfo$1$1"})
public abstract class PacketBundlingMixin {
    @ModifyExpressionValue(method={"addPacket"}, at={@At(value="CONSTANT", args={"intValue=4096"})})
    private int add(int value) {
        if (Modules.get().get(AntiPacketKick.class).isActive()) {
            return Integer.MAX_VALUE;
        }
        return value;
    }
}

