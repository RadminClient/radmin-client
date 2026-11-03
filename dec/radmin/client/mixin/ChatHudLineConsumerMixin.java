/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.multiplayer.chat.GuiMessage$Line
 *  net.minecraft.client.multiplayer.chat.GuiMessageTag
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.BetterChat;
import radmin.client.systems.modules.render.NoRender;

@Mixin(targets={"net.minecraft.client.gui.components.ChatComponent$1"}, remap=false)
public abstract class ChatHudLineConsumerMixin {
    @Inject(method={"accept"}, at={@At(value="HEAD")})
    private void setLine(GuiMessage.Line line, int lineIndex, float alpha, CallbackInfo ci) {
        Modules.get().get(BetterChat.class).line = line;
    }

    @ModifyExpressionValue(method={"accept"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;tag()Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;")})
    private GuiMessageTag onRender_modifyIndicator(GuiMessageTag indicator) {
        return Modules.get().get(NoRender.class).noMessageSignatureIndicator() ? null : indicator;
    }
}

