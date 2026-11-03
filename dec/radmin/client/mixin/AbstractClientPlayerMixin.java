/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.AbstractClientPlayer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.utils.misc.FakeClientPlayer;

@Mixin(value={AbstractClientPlayer.class})
public abstract class AbstractClientPlayerMixin {
    @Inject(method={"getPlayerInfo"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetPlayerListEntry(CallbackInfoReturnable<PlayerInfo> cir) {
        if (MeteorClient.mc.getConnection() == null) {
            cir.setReturnValue((Object)FakeClientPlayer.getPlayerListEntry());
        }
    }
}

