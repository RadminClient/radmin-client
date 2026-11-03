/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.SplashRenderer
 *  net.minecraft.client.resources.SplashManager
 *  net.minecraft.network.chat.Component
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.config.Config;

@Mixin(value={SplashManager.class})
public abstract class SplashManagerMixin {
    @Unique
    private boolean override = true;
    @Unique
    private static final Random random = new Random();
    @Unique
    private final List<String> meteorSplashes = SplashManagerMixin.getMeteorSplashes();

    @Inject(method={"getSplash"}, at={@At(value="HEAD")}, cancellable=true)
    private void onApply(CallbackInfoReturnable<SplashRenderer> cir) {
        if (Config.get() == null || !Config.get().titleScreenSplashes.get().booleanValue()) {
            return;
        }
        if (this.override) {
            cir.setReturnValue((Object)new SplashRenderer((Component)Component.literal((String)this.meteorSplashes.get(random.nextInt(this.meteorSplashes.size())))));
        }
        this.override = !this.override;
    }

    @Unique
    private static List<String> getMeteorSplashes() {
        return List.of("Meteor on Crack!", "Star RadminVPN Client on GitHub!", "Based utility mod.", "\u00a76MineGame159 \u00a7fbased god", "\u00a74meteorclient.com", "\u00a74Meteor on Crack!", "\u00a76Meteor on Crack!");
    }
}

