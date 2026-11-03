/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.opengl.GlCommandEncoder
 *  com.mojang.blaze3d.opengl.GlDevice
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.systems.RenderPassBackend
 *  org.lwjgl.opengl.GL11C
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPassBackend;
import org.lwjgl.opengl.GL11C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.mixininterface.IGpuDevice;
import radmin.client.mixininterface.IRenderPipeline;

@Mixin(value={GlCommandEncoder.class})
public abstract class GlCommandEncoderMixin {
    @Shadow
    @Final
    private GlDevice device;

    @Inject(method={"createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalInt;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/blaze3d/systems/RenderPassBackend;"}, at={@At(value="RETURN")})
    private void createRenderPass$iGpuDevice(CallbackInfoReturnable<RenderPassBackend> cir) {
        ((IGpuDevice)this.device).meteor$onCreateRenderPass((RenderPassBackend)cir.getReturnValue());
    }

    @Inject(method={"applyPipelineState"}, at={@At(value="INVOKE", target="Lcom/mojang/blaze3d/opengl/GlStateManager;_polygonMode(II)V")})
    private void setPipelineAndApplyState$lineSmooth(RenderPipeline pipeline, CallbackInfo ci) {
        if (((IRenderPipeline)pipeline).meteor$getLineSmooth()) {
            GL11C.glEnable((int)2848);
            GL11C.glLineWidth((float)1.0f);
        } else {
            GL11C.glDisable((int)2848);
        }
    }
}

