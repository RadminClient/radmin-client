/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.state.EntityRenderState
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package radmin.client.mixin;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import radmin.client.mixininterface.IEntityRenderState;

@Mixin(value={EntityRenderState.class})
public abstract class EntityRenderStateMixin
implements IEntityRenderState {
    @Unique
    private Entity entity;

    @Override
    @Nullable(value="EntityCulling mod can prevent the code that sets the entity from running")
    public @Nullable(value="EntityCulling mod can prevent the code that sets the entity from running") Entity meteor$getEntity() {
        return this.entity;
    }

    @Override
    public void meteor$setEntity(Entity entity) {
        this.entity = entity;
    }
}

