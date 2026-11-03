/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.food.FoodData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.NoSlow;

@Mixin(value={FoodData.class})
public abstract class FoodDataMixin {
    @ModifyExpressionValue(method={"hasEnoughFood()Z"}, at={@At(value="CONSTANT", args={"floatValue=6.0f"})})
    private float onHunger(float constant) {
        if (Modules.get().get(NoSlow.class).hunger()) {
            return -1.0f;
        }
        return constant;
    }
}

