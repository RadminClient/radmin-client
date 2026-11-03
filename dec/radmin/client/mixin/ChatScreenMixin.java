/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.screens.ChatScreen
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.BetterChat;

@Mixin(value={ChatScreen.class}, priority=1001)
public abstract class ChatScreenMixin {
    @Shadow
    protected EditBox input;

    @Inject(method={"init"}, at={@At(value="RETURN")})
    private void onInit(CallbackInfo ci) {
        if (Modules.get().get(BetterChat.class).isInfiniteChatBox()) {
            this.input.setMaxLength(Integer.MAX_VALUE);
        }
    }
}

