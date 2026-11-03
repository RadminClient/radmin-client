/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.world.item.component.TooltipDisplay
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.BetterTooltips;

@Mixin(value={TooltipDisplay.class})
public abstract class TooltipDisplayMixin {
    @ModifyExpressionValue(method={"shows"}, at={@At(value="FIELD", target="Lnet/minecraft/world/item/component/TooltipDisplay;hideTooltip:Z", opcode=180)})
    private boolean modifyHideTooltip(boolean original) {
        return original && Modules.get().get(BetterTooltips.class).tooltip.get() == false;
    }

    @ModifyExpressionValue(method={"shows"}, at={@At(value="INVOKE", target="Ljava/util/SequencedSet;contains(Ljava/lang/Object;)Z")})
    private boolean modifyHiddenComponents(boolean original) {
        return original && Modules.get().get(BetterTooltips.class).additional.get() == false;
    }
}

