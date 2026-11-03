/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.network.CompressionDecoder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.CompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.AntiPacketKick;

@Mixin(value={CompressionDecoder.class})
public abstract class CompressionDecoderMixin {
    @ModifyExpressionValue(method={"decode"}, at={@At(value="CONSTANT", args={"intValue=8388608"})})
    private int meteor$maximizeUncompressedPacketLimit(int original) {
        return Modules.get().isActive(AntiPacketKick.class) ? Integer.MAX_VALUE : original;
    }
}

