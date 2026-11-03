/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReceiver
 *  net.minecraft.client.gui.ActiveTextCollector
 *  net.minecraft.client.gui.ActiveTextCollector$Parameters
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.TextAlignment
 *  net.minecraft.util.ARGB
 *  net.minecraft.util.FormattedCharSequence
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.BetterChat;

@Mixin(targets={"net.minecraft.client.gui.components.ChatComponent$DrawingBackgroundGraphicsAccess"}, remap=false)
public abstract class ChatHudUnfocusedMixin {
    @Unique
    private static BetterChat betterChat;
    @Final
    @Shadow
    private GuiGraphicsExtractor graphics;

    @ModifyArg(method={"handleMessage"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/ActiveTextCollector;accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/client/gui/ActiveTextCollector$Parameters;Lnet/minecraft/util/FormattedCharSequence;)V"), index=1)
    private int modifyX(int x) {
        return ChatHudUnfocusedMixin.getBetterChat().modifyChatWidth(x);
    }

    @ModifyReceiver(method={"handleMessage"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/ActiveTextCollector;accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/client/gui/ActiveTextCollector$Parameters;Lnet/minecraft/util/FormattedCharSequence;)V")})
    private ActiveTextCollector onRender_beforeDrawText(ActiveTextCollector instance, TextAlignment alignment, int x, int y, ActiveTextCollector.Parameters transformation, FormattedCharSequence orderedText) {
        ChatHudUnfocusedMixin.getBetterChat().beforeDrawMessage(this.graphics, y, ARGB.white((float)transformation.opacity()));
        return instance;
    }

    @Inject(method={"handleMessage"}, at={@At(value="TAIL")})
    private void onRender_afterDrawText(int textTop, float opacity, FormattedCharSequence message, CallbackInfoReturnable<Boolean> cir) {
        ChatHudUnfocusedMixin.getBetterChat().afterDrawMessage();
    }

    @Unique
    private static BetterChat getBetterChat() {
        if (betterChat == null) {
            betterChat = Modules.get().get(BetterChat.class);
        }
        return betterChat;
    }
}

