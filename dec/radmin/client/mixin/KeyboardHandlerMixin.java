/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.KeyboardHandler
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.CharacterEvent
 *  net.minecraft.client.input.KeyEvent
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.CharTypedEvent;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.gui.GuiKeyEvents;
import radmin.client.gui.WidgetScreen;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.input.Input;
import radmin.client.utils.misc.input.KeyAction;

@Mixin(value={KeyboardHandler.class})
public abstract class KeyboardHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method={"keyPress"}, at={@At(value="HEAD")}, cancellable=true)
    public void onKey(long handle, int action, KeyEvent event, CallbackInfo ci) {
        int modifiers = event.modifiers();
        if (event.key() != -1) {
            if (action == 1) {
                modifiers |= Input.getModifier(event.key());
            } else if (action == 0) {
                modifiers &= ~Input.getModifier(event.key());
            }
            Screen screen = this.minecraft.screen;
            if (screen instanceof WidgetScreen) {
                WidgetScreen widgetScreen = (WidgetScreen)screen;
                if (action == 2) {
                    widgetScreen.keyRepeated(new KeyEvent(event.key(), event.scancode(), modifiers));
                }
            }
            if (GuiKeyEvents.canUseKeys) {
                Input.setKeyState(event.key(), action != 0);
                if (((KeyInputEvent)MeteorClient.EVENT_BUS.post((ICancellable)KeyInputEvent.get(new KeyEvent(event.key(), event.scancode(), modifiers), KeyAction.get(action)))).isCancelled()) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method={"charTyped"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChar(long handle, CharacterEvent event, CallbackInfo ci) {
        if (Utils.canUpdate() && !this.minecraft.isPaused() && (this.minecraft.screen == null || this.minecraft.screen instanceof WidgetScreen) && ((CharTypedEvent)MeteorClient.EVENT_BUS.post((ICancellable)CharTypedEvent.get((char)event.codepoint()))).isCancelled()) {
            ci.cancel();
        }
    }
}

