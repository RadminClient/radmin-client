/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.item.ItemStackRenderState$LayerRenderState
 *  net.minecraft.client.resources.model.cuboid.ItemTransform
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ItemStackRenderState.LayerRenderState.class})
public interface LayerRenderStateAccessor {
    @Accessor(value="itemTransform")
    public ItemTransform meteor$getTransform();
}

