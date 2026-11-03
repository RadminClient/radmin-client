/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.ScreenEffectRenderer
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={ScreenEffectRenderer.class})
public abstract class ScreenEffectRendererMixin {
    @Inject(method={"renderFire"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onRenderFireOverlay(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).noFireOverlay()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderWater"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onRenderUnderwaterOverlay(Minecraft minecraft, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).noLiquidOverlay()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderTex"}, at={@At(value="HEAD")}, cancellable=true)
    private static void render(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).noInWallOverlay()) {
            ci.cancel();
        }
    }
}

