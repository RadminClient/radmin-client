/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.component.AttackRange
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.ToDoubleFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.combat.Hitboxes;

@Mixin(value={AttackRange.class})
public abstract class AttackRangeMixin {
    @ModifyExpressionValue(method={"isInRange(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/ToDoubleFunction;D)Z"}, at={@At(value="FIELD", target="Lnet/minecraft/world/item/component/AttackRange;hitboxMargin:F", opcode=180)})
    private float modifyHitboxMargin(float original, LivingEntity attacker, ToDoubleFunction<Vec3> distanceFunction, double extraBuffer) {
        float v = (float)Modules.get().get(Hitboxes.class).getEntityValue((Entity)attacker);
        return original + v;
    }
}

