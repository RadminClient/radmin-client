/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.server.DownloadedPackSource
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.resources.server.DownloadedPackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.ServerSpoof;

@Mixin(value={DownloadedPackSource.class})
public abstract class DownloadedPackSourceMixin {
    @Inject(method={"onReloadSuccess"}, at={@At(value="TAIL")})
    private void removeInactivePacksTail(CallbackInfo ci) {
        Modules.get().get(ServerSpoof.class).silentAcceptResourcePack = false;
    }
}

