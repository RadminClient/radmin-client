/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.locale.Language
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.IComponent;

@Mixin(value={MutableComponent.class})
public abstract class MutableComponentMixin
implements IComponent {
    @Shadow
    @Nullable
    private Language decomposedWith;

    @Override
    public void meteor$invalidateCache() {
        this.decomposedWith = null;
    }
}

