/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ClassInstanceMultiMap
 *  net.minecraft.world.level.entity.EntitySection
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntitySection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={EntitySection.class})
public interface EntitySectionAccessor {
    @Accessor(value="storage")
    public <T> ClassInstanceMultiMap<T> meteor$getStorage();
}

