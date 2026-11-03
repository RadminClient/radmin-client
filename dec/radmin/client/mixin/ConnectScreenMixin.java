/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.ConnectScreen
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.TransferState
 *  net.minecraft.client.multiplayer.resolver.ServerAddress
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.world.ServerConnectBeginEvent;

@Mixin(value={ConnectScreen.class})
public abstract class ConnectScreenMixin {
    @Inject(method={"connect(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/resolver/ServerAddress;Lnet/minecraft/client/multiplayer/ServerData;Lnet/minecraft/client/multiplayer/TransferState;)V"}, at={@At(value="HEAD")})
    private void tryConnectEvent(Minecraft minecraft, ServerAddress hostAndPort, ServerData server, TransferState transferState, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)ServerConnectBeginEvent.get(hostAndPort, server));
    }
}

