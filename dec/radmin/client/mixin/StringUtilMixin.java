/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringUtil
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package radmin.client.mixin;

import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.BetterChat;

@Mixin(value={StringUtil.class})
public abstract class StringUtilMixin {
    @ModifyArg(method={"trimChatMessage"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/StringUtil;truncateStringIfNecessary(Ljava/lang/String;IZ)Ljava/lang/String;"), index=1)
    private static int injected(int maxLength) {
        return Modules.get().get(BetterChat.class).isInfiniteChatBox() ? Integer.MAX_VALUE : maxLength;
    }
}

