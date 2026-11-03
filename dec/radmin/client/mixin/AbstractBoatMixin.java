/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.entity.vehicle.boat.AbstractBoat
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.EntityControl;

@Mixin(value={AbstractBoat.class})
public abstract class AbstractBoatMixin {
    @ModifyExpressionValue(method={"controlBoat"}, at={@At(value="FIELD", target="Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;inputLeft:Z", opcode=180)})
    private boolean modifyPressingLeft(boolean original) {
        if (Modules.get().isActive(EntityControl.class) && Modules.get().get(EntityControl.class).lockYaw.get().booleanValue()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(method={"controlBoat"}, at={@At(value="FIELD", target="Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;inputRight:Z", opcode=180)})
    private boolean modifyPressingRight(boolean original) {
        if (Modules.get().isActive(EntityControl.class) && Modules.get().get(EntityControl.class).lockYaw.get().booleanValue()) {
            return false;
        }
        return original;
    }
}

