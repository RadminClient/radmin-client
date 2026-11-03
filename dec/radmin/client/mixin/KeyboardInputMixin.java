/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.ClientInput
 *  net.minecraft.client.player.KeyboardInput
 *  net.minecraft.world.entity.player.Input
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Sneak;
import radmin.client.systems.modules.render.Freecam;

@Mixin(value={KeyboardInput.class})
public abstract class KeyboardInputMixin
extends ClientInput {
    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void isPressed(CallbackInfo ci) {
        if (Modules.get().get(Sneak.class).doVanilla() || Modules.get().get(Freecam.class).staySneaking()) {
            this.keyPresses = new Input(this.keyPresses.forward(), this.keyPresses.backward(), this.keyPresses.left(), this.keyPresses.right(), this.keyPresses.jump(), true, this.keyPresses.sprint());
        }
    }
}

