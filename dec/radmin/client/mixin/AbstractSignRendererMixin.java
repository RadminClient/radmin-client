/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.renderer.blockentity.AbstractSignRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={AbstractSignRenderer.class})
public abstract class AbstractSignRendererMixin {
    @ModifyExpressionValue(method={"submitSignText"}, at={@At(value="CONSTANT", args={"intValue=4", "ordinal=1"})})
    private int loopTextLengthProxy(int i) {
        if (Modules.get().get(NoRender.class).noSignText()) {
            return 0;
        }
        return i;
    }
}

