/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.IVec3;

@Mixin(value={Vec3.class})
public abstract class Vec3Mixin
implements IVec3 {
    @Shadow
    @Final
    @Mutable
    public double x;
    @Shadow
    @Final
    @Mutable
    public double y;
    @Shadow
    @Final
    @Mutable
    public double z;

    @Override
    public Vec3 meteor$set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return (Vec3)this;
    }

    @Override
    public Vec3 meteor$setXZ(double x, double z) {
        this.x = x;
        this.z = z;
        return (Vec3)this;
    }

    @Override
    public Vec3 meteor$setY(double y) {
        this.y = y;
        return (Vec3)this;
    }
}

