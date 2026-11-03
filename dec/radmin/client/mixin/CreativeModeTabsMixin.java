/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.world.item.CreativeModeTabs
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.BetterTooltips;

@Mixin(value={CreativeModeTabs.class})
public abstract class CreativeModeTabsMixin {
    @ModifyReturnValue(method={"tryRebuildTabContents"}, at={@At(value="RETURN")})
    private static boolean modifyReturn(boolean original) {
        return original || Modules.get().get(BetterTooltips.class).updateTooltips();
    }
}

