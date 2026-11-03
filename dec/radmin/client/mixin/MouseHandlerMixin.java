/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.MouseHandler
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.client.input.MouseButtonInfo
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.platform.Window;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.events.meteor.MouseScrollEvent;
import radmin.client.utils.misc.input.Input;
import radmin.client.utils.misc.input.KeyAction;

@Mixin(value={MouseHandler.class})
public abstract class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract double getScaledXPos(Window var1);

    @Shadow
    public abstract double getScaledYPos(Window var1);

    @Inject(method={"onButton"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        Input.setButtonState(rawButtonInfo.button(), action != 0);
        MouseButtonEvent click = new MouseButtonEvent(this.getScaledXPos(this.minecraft.getWindow()), this.getScaledYPos(this.minecraft.getWindow()), rawButtonInfo);
        if (((MouseClickEvent)MeteorClient.EVENT_BUS.post((ICancellable)MouseClickEvent.get(click, KeyAction.get(action)))).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"onScroll"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        if (((MouseScrollEvent)MeteorClient.EVENT_BUS.post((ICancellable)MouseScrollEvent.get(yoffset))).isCancelled()) {
            ci.cancel();
        }
    }
}

