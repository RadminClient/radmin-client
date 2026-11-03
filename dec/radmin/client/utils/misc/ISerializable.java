/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 */
package radmin.client.utils.misc;

import net.minecraft.nbt.CompoundTag;

public interface ISerializable<T> {
    public CompoundTag toTag();

    public T fromTag(CompoundTag var1);
}

