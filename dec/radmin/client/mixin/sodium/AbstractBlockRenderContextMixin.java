/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={AbstractBlockRenderContext.class}, remap=false)
public abstract class AbstractBlockRenderContextMixin {
    @Shadow
    protected BlockState state;
    @Shadow
    protected BlockAndTintGetter level;
    @Shadow
    protected BlockPos pos;
    @Unique
    private Xray xray;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        this.xray = Modules.get().get(Xray.class);
    }

    @Inject(method={"shouldDrawSide"}, at={@At(value="HEAD")}, cancellable=true)
    private void meteor$forceXrayFace(Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (this.xray != null && this.xray.isActive() && !this.xray.isBlocked(this.state.getBlock(), null)) {
            cir.setReturnValue((Object)true);
        }
    }

    @ModifyReturnValue(method={"shouldDrawSide"}, at={@At(value="RETURN")})
    private boolean shouldDrawSide(boolean original, Direction facing) {
        if (this.xray.isActive()) {
            return this.xray.modifyDrawSide(this.state, (BlockGetter)this.level, this.pos, facing, original);
        }
        return original;
    }
}

