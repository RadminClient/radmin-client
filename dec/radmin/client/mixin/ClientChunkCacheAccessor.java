/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientChunkCache
 *  net.minecraft.client.multiplayer.ClientChunkCache$Storage
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ClientChunkCache.class})
public interface ClientChunkCacheAccessor {
    @Accessor(value="storage")
    public ClientChunkCache.Storage meteor$getStorage();
}

