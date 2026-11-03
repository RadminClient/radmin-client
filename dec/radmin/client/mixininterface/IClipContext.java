/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.mixininterface;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

public interface IClipContext {
    public void meteor$set(Vec3 var1, Vec3 var2, ClipContext.Block var3, ClipContext.Fluid var4, Entity var5);
}

