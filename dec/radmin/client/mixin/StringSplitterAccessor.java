/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.StringSplitter
 *  net.minecraft.client.StringSplitter$WidthProvider
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.client.StringSplitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={StringSplitter.class})
public interface StringSplitterAccessor {
    @Accessor(value="widthProvider")
    public StringSplitter.WidthProvider meteor$getWidthProvider();
}

