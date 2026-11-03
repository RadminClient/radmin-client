/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.model.object.crystal.EndCrystalModel
 *  net.minecraft.client.renderer.entity.state.EndCrystalRenderState
 *  net.minecraft.util.Mth
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Chams;

@Mixin(value={EndCrystalModel.class})
public abstract class EndCrystalModelMixin {
    @ModifyExpressionValue(method={"setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/EndCrystalRenderer;getY(F)F")})
    private float setAngles$bounce(float original, EndCrystalRenderState state) {
        Chams module = Modules.get().get(Chams.class);
        if (!module.isActive() || !module.crystals.get().booleanValue()) {
            return original;
        }
        float g = Mth.sin((double)(state.ageInTicks * 0.2f)) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f * module.crystalsBounce.get().floatValue();
        return g - 1.4f;
    }

    @ModifyExpressionValue(method={"setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;ageInTicks:F", ordinal=0, opcode=180)})
    private float modifySpeed(float original) {
        Chams module = Modules.get().get(Chams.class);
        if (!module.isActive() || !module.crystals.get().booleanValue()) {
            return original;
        }
        return original * module.crystalsRotationSpeed.get().floatValue();
    }
}

