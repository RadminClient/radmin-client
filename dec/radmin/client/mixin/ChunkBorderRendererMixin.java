/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.debug.ChunkBorderRenderer
 *  net.minecraft.core.SectionPos
 *  net.minecraft.util.Mth
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.ChunkBorderRenderer;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Freecam;

@Mixin(value={ChunkBorderRenderer.class})
public abstract class ChunkBorderRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyExpressionValue(method={"emitGizmos"}, at={@At(value="INVOKE", target="Lnet/minecraft/core/SectionPos;of(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/SectionPos;")})
    private SectionPos emitGizmos$getChunkPos(SectionPos original) {
        Freecam freecam = Modules.get().get(Freecam.class);
        if (!freecam.isActive()) {
            return original;
        }
        float delta = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        return SectionPos.of((int)SectionPos.posToSectionCoord((double)Mth.floor((double)freecam.getX(delta))), (int)SectionPos.posToSectionCoord((double)Mth.floor((double)freecam.getY(delta))), (int)SectionPos.posToSectionCoord((double)Mth.floor((double)freecam.getZ(delta))));
    }
}

