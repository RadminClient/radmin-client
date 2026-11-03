/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.material.Fluid
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package radmin.client.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={LivingEntity.class})
public interface LivingEntityAccessor {
    @Invoker(value="jumpInLiquid")
    public void meteor$swimUpwards(TagKey<Fluid> var1);

    @Accessor(value="jumping")
    public boolean meteor$isJumping();

    @Accessor(value="noJumpDelay")
    public int meteor$getJumpCooldown();

    @Accessor(value="noJumpDelay")
    public void meteor$setJumpCooldown(int var1);
}

