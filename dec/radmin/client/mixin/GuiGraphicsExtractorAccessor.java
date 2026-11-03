/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.GuiGraphicsExtractor$ScissorStack
 *  net.minecraft.client.renderer.state.gui.GuiRenderState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GuiGraphicsExtractor.class})
public interface GuiGraphicsExtractorAccessor {
    @Accessor(value="guiRenderState")
    public GuiRenderState getGuiRenderState();

    @Accessor(value="scissorStack")
    public GuiGraphicsExtractor.ScissorStack getScissorStack();
}

