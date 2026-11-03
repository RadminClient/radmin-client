/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.GpuTexture
 *  net.minecraft.client.renderer.Lightmap
 *  net.minecraft.client.renderer.state.LightmapRenderState
 *  net.minecraft.util.ARGB
 *  net.minecraft.util.profiling.Profiler
 *  net.minecraft.util.profiling.ProfilerFiller
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Fullbright;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={Lightmap.class})
public abstract class LightmapMixin {
    @Shadow
    @Final
    private GpuTexture texture;

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void render$fullbright(LightmapRenderState renderState, CallbackInfo ci) {
        if (Modules.get().get(Fullbright.class).getGamma() || Modules.get().isActive(Xray.class)) {
            ProfilerFiller profile = Profiler.get();
            profile.push("lightmap");
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.texture, ARGB.color((int)255, (int)255, (int)255, (int)255));
            profile.pop();
            ci.cancel();
        }
    }
}

