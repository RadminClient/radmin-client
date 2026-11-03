/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.SkyRenderer
 *  net.minecraft.client.renderer.state.level.SkyRenderState
 *  net.minecraft.world.level.dimension.DimensionType$Skybox
 *  org.joml.Vector4fc
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.world.level.dimension.DimensionType;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.Ambience;

@Mixin(value={SkyRenderer.class})
public abstract class SkyRendererMixin {
    @Inject(method={"extractRenderState"}, at={@At(value="TAIL")})
    private void updateRenderState(ClientLevel level, float partialTicks, Camera camera, SkyRenderState state, CallbackInfo ci) {
        Ambience ambience = Modules.get().get(Ambience.class);
        if (!ambience.isActive()) {
            return;
        }
        if (ambience.endSky.get().booleanValue()) {
            state.skybox = DimensionType.Skybox.END;
        }
        if (ambience.customSkyColor.get().booleanValue()) {
            state.skyColor = ambience.skyColor().getPacked();
        }
    }

    @ModifyArg(method={"renderEndSky"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
    private Vector4fc modifyEndSkyColor(Vector4fc original) {
        Ambience ambience = Modules.get().get(Ambience.class);
        if (ambience.isActive() && ambience.endSky.get().booleanValue() && ambience.customSkyColor.get().booleanValue()) {
            return ambience.skyColor().getVec4f();
        }
        return original;
    }
}

