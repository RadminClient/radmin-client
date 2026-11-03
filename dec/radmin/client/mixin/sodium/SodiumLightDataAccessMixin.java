/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.world.level.LightLayer
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin.sodium;

import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Fullbright;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={LightDataAccess.class}, remap=false)
public abstract class SodiumLightDataAccessMixin {
    @Unique
    private static final int FULL_LIGHT = 4095;
    @Shadow
    protected BlockAndTintGetter level;
    @Shadow
    @Final
    private BlockPos.MutableBlockPos pos;
    @Unique
    private Xray xray;
    @Unique
    private Fullbright fb;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        this.xray = Modules.get().get(Xray.class);
        this.fb = Modules.get().get(Fullbright.class);
    }

    @ModifyVariable(method={"compute"}, at=@At(value="TAIL"), name={"bl"})
    private int compute_modifyBL(int bl) {
        BlockState state;
        if (this.xray.isActive() && !this.xray.isBlocked((state = this.level.getBlockState((BlockPos)this.pos)).getBlock(), (BlockPos)this.pos)) {
            return 4095;
        }
        return bl;
    }

    @ModifyVariable(method={"compute"}, at=@At(value="STORE"), name={"sl"})
    private int compute_assignSL(int sl) {
        return Math.max(this.fb.getLuminance(LightLayer.SKY), sl);
    }

    @ModifyVariable(method={"compute"}, at=@At(value="STORE"), name={"bl"})
    private int compute_assignBL(int bl) {
        return Math.max(this.fb.getLuminance(LightLayer.BLOCK), bl);
    }
}

