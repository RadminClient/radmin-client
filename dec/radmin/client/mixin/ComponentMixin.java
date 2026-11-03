/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  org.spongepowered.asm.mixin.Mixin
 */
package radmin.client.mixin;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import radmin.client.mixininterface.IComponent;

@Mixin(value={Component.class})
public interface ComponentMixin
extends IComponent {
    @Override
    default public void meteor$invalidateCache() {
    }
}

