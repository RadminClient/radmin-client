/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.ChatScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.KeyEvent
 *  net.minecraft.network.chat.ClickEvent
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.commands.Commands;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.GUIMove;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.text.MeteorClickEvent;
import radmin.client.utils.misc.text.RunnableClickEvent;

@Mixin(value={Screen.class}, priority=500)
public abstract class ScreenMixin {
    @Inject(method={"extractTransparentBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void onExtractTransparentBackground(CallbackInfo ci) {
        if (Utils.canUpdate() && Modules.get().get(NoRender.class).noGuiBackground()) {
            ci.cancel();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Inject(method={"defaultHandleClickEvent"}, at={@At(value="INVOKE", target="Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap=false)}, cancellable=true)
    private static void onDefaultHandleClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen, CallbackInfo ci) {
        if (event instanceof RunnableClickEvent) {
            RunnableClickEvent runnableClickEvent = (RunnableClickEvent)event;
            runnableClickEvent.runnable.run();
            ci.cancel();
        } else if (event instanceof MeteorClickEvent) {
            MeteorClickEvent meteorClickEvent = (MeteorClickEvent)event;
            if (meteorClickEvent.value.startsWith(Config.get().prefix.get())) {
                try {
                    Commands.dispatch(meteorClickEvent.value.substring(Config.get().prefix.get().length()));
                }
                catch (CommandSyntaxException e) {
                    MeteorClient.LOG.error("Failed to run command", (Throwable)e);
                }
                finally {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method={"keyPressed"}, at={@At(value="HEAD")}, cancellable=true)
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof ChatScreen) {
            return;
        }
        GUIMove guiMove = Modules.get().get(GUIMove.class);
        List<Integer> arrows = List.of(Integer.valueOf(262), Integer.valueOf(263), Integer.valueOf(264), Integer.valueOf(265));
        if (guiMove.disableArrows() && arrows.contains(event.key()) || guiMove.disableSpace() && event.key() == 32) {
            cir.setReturnValue((Object)true);
        }
    }
}

