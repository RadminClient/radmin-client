/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.caffeinemc.mods.sodium.client.model.color.ColorProvider
 *  net.caffeinemc.mods.sodium.client.model.light.LightPipeline
 *  net.caffeinemc.mods.sodium.client.model.quad.ModelQuadViewMutable
 *  net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing
 *  net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder
 *  net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer
 *  net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials
 *  net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material
 *  net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector
 *  net.caffeinemc.mods.sodium.client.world.LevelSlice
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.client.renderer.block.FluidModel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.FluidState
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.light.LightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadViewMutable;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={DefaultFluidRenderer.class}, remap=false)
public abstract class SodiumDefaultFluidRendererMixin {
    @Final
    @Shadow
    private int[] quadColors;
    @Unique
    private int xrayAlpha;
    @Unique
    private boolean forceXrayFluidSides;
    @Unique
    private Xray xray;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        this.xray = Modules.get().get(Xray.class);
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(LevelSlice level, BlockState blockState, FluidState fluidState, BlockPos blockPos, BlockPos offset, TranslucentGeometryCollector collector, ChunkModelBuilder meshBuilder, Material material, ColorProvider<FluidState> colorProvider, FluidModel sprites, CallbackInfo ci) {
        this.xrayAlpha = Xray.getFluidAlpha(fluidState, blockPos);
        this.forceXrayFluidSides = this.xray.isActive();
        if (this.xrayAlpha == 0) {
            this.forceXrayFluidSides = false;
            ci.cancel();
        }
    }

    @ModifyReturnValue(method={"isFullBlockFluidSideVisible"}, at={@At(value="RETURN")})
    private boolean onIsFullBlockFluidSideVisible(boolean original, BlockGetter view, BlockPos selfPos, Direction facing, FluidState fluid, @Local(name={"otherState"}) BlockState otherState) {
        if (original || !this.forceXrayFluidSides || facing.getAxis().isVertical()) {
            return original;
        }
        if (!this.xray.isBlocked(otherState.getBlock(), null)) {
            return false;
        }
        return !otherState.getFluidState().getType().isSame(fluid.getType());
    }

    @ModifyReturnValue(method={"isFluidSideExposed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;F)Z"}, at={@At(value="RETURN")})
    private boolean onIsFluidSideExposed(boolean original, BlockState ownBlockState, BlockState neighborBlockState, Direction facing, float height) {
        if (original || !this.forceXrayFluidSides || facing.getAxis().isVertical()) {
            return original;
        }
        if (!this.xray.isBlocked(neighborBlockState.getBlock(), null)) {
            return false;
        }
        return !neighborBlockState.getFluidState().getType().isSame(ownBlockState.getFluidState().getType());
    }

    @Inject(method={"getUpFaceExposureByNeighbors"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetUpFaceExposureByNeighbors(BlockAndTintGetter level, BlockPos origin, FluidState fluidState, CallbackInfoReturnable<Integer> cir) {
        if (this.forceXrayFluidSides) {
            cir.setReturnValue((Object)3);
        }
    }

    @Inject(method={"updateQuad"}, at={@At(value="TAIL")})
    private void onUpdateQuad(ModelQuadViewMutable quad, LevelSlice level, BlockPos pos, LightPipeline lighter, Direction dir, ModelQuadFacing facing, float brightness, ColorProvider<FluidState> colorProvider, FluidState fluidState, CallbackInfo ci) {
        if (this.xrayAlpha != -1) {
            for (int i = 0; i < 4; ++i) {
                this.quadColors[i] = this.quadColors[i] & 0xFFFFFF | this.xrayAlpha << 24;
            }
        }
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/DefaultFluidRenderer;writeQuad(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/buffers/ChunkModelBuilder;Lnet/caffeinemc/mods/sodium/client/render/chunk/translucent_sorting/TranslucentGeometryCollector;Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/Material;Lnet/minecraft/core/BlockPos;Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;Lnet/caffeinemc/mods/sodium/client/model/quad/properties/ModelQuadFacing;Z)V"), index=2)
    private Material modifyMaterial(Material material) {
        if (this.xrayAlpha != -1) {
            return DefaultMaterials.TRANSLUCENT;
        }
        return material;
    }
}

