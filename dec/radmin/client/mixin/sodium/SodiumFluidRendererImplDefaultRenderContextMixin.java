/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.sodium.client.model.color.ColorProvider
 *  net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView
 *  net.caffeinemc.mods.sodium.client.world.LevelSlice
 *  net.minecraft.client.color.block.BlockTintSource
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  org.jspecify.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin.sodium;

import java.util.Arrays;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Xray;
import radmin.client.systems.modules.world.Ambience;
import radmin.client.utils.render.color.Color;

@Mixin(targets={"net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl$DefaultRenderContext"}, remap=false)
public abstract class SodiumFluidRendererImplDefaultRenderContextMixin {
    @Unique
    private Ambience ambience;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        this.ambience = Modules.get().get(Ambience.class);
    }

    @Inject(method={"getColorProvider"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetColorProvider(Fluid fluid, @Nullable BlockTintSource blockTintSource, CallbackInfoReturnable<ColorProvider<FluidState>> cir) {
        if (this.ambience.isActive() && this.ambience.customLavaColor.get().booleanValue() && fluid.defaultFluidState().is(FluidTags.LAVA)) {
            cir.setReturnValue(this::lavaColorProvider);
        }
    }

    @Unique
    private void lavaColorProvider(LevelSlice slice, BlockPos pos, BlockPos.MutableBlockPos scratchPos, FluidState state, ModelQuadView quad, int[] output, boolean smooth) {
        Color c = this.ambience.lavaColor.get();
        int alpha = Xray.getFluidAlpha(state, pos);
        Arrays.fill(output, Color.fromRGBA(c.r, c.g, c.b, alpha != -1 ? alpha : c.a));
    }
}

