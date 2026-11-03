/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.renderer.fog.FogRenderer
 *  org.joml.Vector4f
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.systems.modules.world.Ambience;

@Mixin(value={FogRenderer.class})
public abstract class FogRendererMixin {
    @ModifyVariable(method={"updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"}, at=@At(value="HEAD"), argsOnly=true, name={"fogColor"})
    private Vector4f modifyFogDistance(Vector4f fogColor) {
        if (Modules.get() == null) {
            return fogColor;
        }
        Ambience ambience = Modules.get().get(Ambience.class);
        if (ambience.isActive() && ambience.customFogColor.get().booleanValue()) {
            return ambience.fogColor.get().getVec4f();
        }
        return fogColor;
    }

    @ModifyExpressionValue(method={"getBuffer"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/fog/FogRenderer;fogEnabled:Z", opcode=178)})
    private boolean modifyFogEnabled(boolean original) {
        if (Modules.get() == null) {
            return original;
        }
        return original && !Modules.get().get(NoRender.class).noFog();
    }
}

