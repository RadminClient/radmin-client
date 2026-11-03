/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.feature.ItemFeatureRenderer
 *  net.minecraft.client.renderer.item.ItemStackRenderState$FoilType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package radmin.client.mixin;

import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={ItemFeatureRenderer.class})
public abstract class ItemRendererMixin {
    @ModifyVariable(method={"renderItem"}, at=@At(value="STORE"), name={"foilType"})
    private ItemStackRenderState.FoilType modifyEnchant(ItemStackRenderState.FoilType foilType) {
        if (Modules.get().get(NoRender.class).noEnchantGlint()) {
            return ItemStackRenderState.FoilType.NONE;
        }
        return foilType;
    }
}

