/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringDecomposer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package radmin.client.mixin;

import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.player.NameProtect;

@Mixin(value={StringDecomposer.class})
public abstract class StringDecomposerMixin {
    @ModifyArg(at=@At(value="INVOKE", target="Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", ordinal=0), method={"iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"}, index=0)
    private static String adjustText(String text) {
        if (Modules.get() != null) {
            return Modules.get().get(NameProtect.class).replaceName(text);
        }
        return text;
    }
}

