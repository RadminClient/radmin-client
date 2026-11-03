/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponentGetter
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStackTemplate
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.ItemContainerContents
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.BetterTooltips;

@Mixin(value={ItemContainerContents.class})
public abstract class ItemContainerContentsMixin {
    @Shadow
    @Final
    private List<Optional<ItemStackTemplate>> items;

    @Inject(method={"addToTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAddToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components, CallbackInfo ci) {
        if (Modules.get() == null) {
            return;
        }
        BetterTooltips tooltips = Modules.get().get(BetterTooltips.class);
        if (tooltips.isActive()) {
            if (tooltips.previewShulkers()) {
                ci.cancel();
            } else if (tooltips.shulkerCompactTooltip()) {
                ci.cancel();
                tooltips.applyCompactShulkerTooltip(this.items, consumer);
            }
        }
    }
}

