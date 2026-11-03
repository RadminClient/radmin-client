/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.world.AmbientOcclusionEvent;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={BlockBehaviour.class})
public abstract class BlockBehaviourMixin {
    @Inject(method={"getShadeBrightness"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetAmbientOcclusionLightLevel(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        AmbientOcclusionEvent event = (AmbientOcclusionEvent)MeteorClient.EVENT_BUS.post((Object)AmbientOcclusionEvent.get());
        if (event.lightLevel != -1.0f) {
            cir.setReturnValue((Object)Float.valueOf(event.lightLevel));
        }
    }

    @Inject(method={"getSeed"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderingSeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Long> cir) {
        if (Modules.get().get(NoRender.class).noTextureRotations()) {
            cir.setReturnValue((Object)0L);
        }
    }
}

