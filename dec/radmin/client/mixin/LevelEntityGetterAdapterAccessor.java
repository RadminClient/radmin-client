/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.entity.EntityAccess
 *  net.minecraft.world.level.entity.EntitySectionStorage
 *  net.minecraft.world.level.entity.LevelEntityGetterAdapter
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={LevelEntityGetterAdapter.class})
public interface LevelEntityGetterAdapterAccessor {
    @Accessor(value="sectionStorage")
    public <T extends EntityAccess> EntitySectionStorage<T> meteor$getSectionStorage();
}

