/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.SpawnerRenderer
 *  net.minecraft.client.renderer.blockentity.state.SpawnerRenderState
 *  net.minecraft.world.level.block.entity.SpawnerBlockEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={SpawnerRenderer.class})
public abstract class SpawnerRendererMixin
implements BlockEntityRenderer<SpawnerBlockEntity, SpawnerRenderState> {
    @Inject(method={"submitEntityInSpawner"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onRenderDisplayEntity(CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).noMobInSpawner()) {
            ci.cancel();
        }
    }
}

