/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView
 *  net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.client.renderer.chunk.ChunkSectionLayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin.indigo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={AltModelBlockRendererImpl.class})
public abstract class AltModelBlockRendererImplMixin {
    @Shadow
    private BlockState blockState;
    @Shadow
    private BlockPos pos;
    @Shadow
    private BlockAndTintGetter level;

    @ModifyReturnValue(method={"shouldCullFace"}, at={@At(value="RETURN")})
    private boolean shouldCullFace$xray(boolean original, Direction direction) {
        if (direction == null) {
            return original;
        }
        Xray xray = Modules.get().get(Xray.class);
        if (xray.isActive()) {
            return !xray.modifyDrawSide(this.blockState, (BlockGetter)this.level, this.pos, direction, !original);
        }
        return original;
    }

    @Inject(method={"transform"}, at={@At(value="RETURN")}, cancellable=true)
    private void transform$xray(MutableQuadView quad, CallbackInfoReturnable<Boolean> cir) {
        int alpha = Xray.getAlpha(this.blockState, this.pos);
        if (alpha == 0) {
            cir.setReturnValue((Object)false);
        } else if (alpha != -1) {
            if (alpha > 0 && alpha < 255) {
                quad.chunkLayer(ChunkSectionLayer.TRANSLUCENT);
            }
            for (int i = 0; i < 4; ++i) {
                quad.color(i, AltModelBlockRendererImplMixin.rewriteQuadAlpha(quad.color(i), alpha));
            }
        }
    }

    @Unique
    private static int rewriteQuadAlpha(int color, int alpha) {
        return (alpha & 0xFF) << 24 | color & 0xFFFFFF;
    }
}

