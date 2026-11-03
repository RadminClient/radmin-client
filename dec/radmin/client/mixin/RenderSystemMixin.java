/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.mixin.MinecraftAccessor;
import radmin.client.renderer.MeshUniforms;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.InventoryTweaks;
import radmin.client.utils.render.postprocess.ChamsShader;
import radmin.client.utils.render.postprocess.OutlineUniforms;
import radmin.client.utils.render.postprocess.PostProcessShader;

@Mixin(value={RenderSystem.class})
public abstract class RenderSystemMixin {
    @Inject(method={"flipFrame"}, at={@At(value="TAIL")})
    private static void meteor$flipFrame(CallbackInfo ci) {
        MeshUniforms.flipFrame();
        PostProcessShader.flipFrame();
        ChamsShader.flipFrame();
        OutlineUniforms.flipFrame();
        if (Modules.get() == null || MeteorClient.mc.player == null) {
            return;
        }
        if (Modules.get().get(InventoryTweaks.class).frameInput()) {
            ((MinecraftAccessor)MeteorClient.mc).meteor$handleInputEvents();
        }
    }
}

