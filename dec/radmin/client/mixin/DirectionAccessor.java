/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Direction.class})
public interface DirectionAccessor {
    @Accessor(value="BY_2D_DATA")
    public static Direction[] meteor$getHorizontal() {
        throw new AssertionError();
    }
}

