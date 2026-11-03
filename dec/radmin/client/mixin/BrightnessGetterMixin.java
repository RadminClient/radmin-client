/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.LevelRenderer$BrightnessGetter
 *  net.minecraft.world.level.LightLayer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package radmin.client.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Fullbright;

@Mixin(value={LevelRenderer.BrightnessGetter.class})
public interface BrightnessGetterMixin {
    @ModifyVariable(method={"lambda$static$0"}, at=@At(value="STORE"), name={"sky"})
    private static int getLightmapCoordinatesModifySkyLight(int sky) {
        return Math.max(Modules.get().get(Fullbright.class).getLuminance(LightLayer.SKY), sky);
    }

    @ModifyVariable(method={"lambda$static$0"}, at=@At(value="STORE"), name={"block"})
    private static int getLightmapCoordinatesModifyBlockLight(int block) {
        return Math.max(Modules.get().get(Fullbright.class).getLuminance(LightLayer.BLOCK), block);
    }
}

