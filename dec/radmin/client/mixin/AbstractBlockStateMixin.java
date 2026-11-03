/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={BlockBehaviour.BlockStateBase.class})
public abstract class AbstractBlockStateMixin {
    @Inject(method={"getOffset"}, at={@At(value="HEAD")}, cancellable=true)
    private void modifyPos(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        if (Modules.get() == null) {
            return;
        }
        if (Modules.get().get(NoRender.class).noTextureRotations()) {
            cir.setReturnValue((Object)Vec3.ZERO);
        }
    }
}

