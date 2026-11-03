/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.world.entity.player.PlayerSkin
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.player.NameProtect;

@Mixin(value={PlayerInfo.class})
public abstract class PlayerInfoMixin {
    @Shadow
    public abstract GameProfile getProfile();

    @Inject(method={"getSkin"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetTexture(CallbackInfoReturnable<PlayerSkin> cir) {
        if (this.getProfile().name().equals(Minecraft.getInstance().getUser().getName()) && Modules.get().get(NameProtect.class).skinProtect()) {
            cir.setReturnValue((Object)DefaultPlayerSkin.get((GameProfile)this.getProfile()));
        }
    }
}

