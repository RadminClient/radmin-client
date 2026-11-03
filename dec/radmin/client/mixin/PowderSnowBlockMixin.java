/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.block.PowderSnowBlock
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.MeteorClient;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Jesus;

@Mixin(value={PowderSnowBlock.class})
public abstract class PowderSnowBlockMixin {
    @ModifyReturnValue(method={"canEntityWalkOnPowderSnow"}, at={@At(value="RETURN")})
    private static boolean onCanWalkOnPowderSnow(boolean original, Entity entity) {
        if (entity == MeteorClient.mc.player && Modules.get().get(Jesus.class).canWalkOnPowderSnow()) {
            return true;
        }
        return original;
    }
}

