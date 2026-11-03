/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.chunk.ChunkAccess
 */
package radmin.client.utils.world;

import java.util.Iterator;
import net.minecraft.world.level.chunk.ChunkAccess;
import radmin.client.MeteorClient;
import radmin.client.mixin.ClientChunkCacheAccessor;
import radmin.client.mixin.ClientChunkMapAccessor;

public class ChunkIterator
implements Iterator<ChunkAccess> {
    private final ClientChunkMapAccessor map;
    private final boolean onlyWithLoadedNeighbours;
    private int i;
    private ChunkAccess chunk;

    public ChunkIterator(boolean onlyWithLoadedNeighbours) {
        this.map = (ClientChunkMapAccessor)((ClientChunkCacheAccessor)MeteorClient.mc.level.getChunkSource()).meteor$getStorage();
        this.i = 0;
        this.onlyWithLoadedNeighbours = onlyWithLoadedNeighbours;
        this.getNext();
    }

    private ChunkAccess getNext() {
        ChunkAccess prev = this.chunk;
        this.chunk = null;
        while (this.i < this.map.meteor$getChunks().length()) {
            this.chunk = (ChunkAccess)this.map.meteor$getChunks().get(this.i++);
            if (this.chunk == null || this.onlyWithLoadedNeighbours && !this.isInRadius(this.chunk)) continue;
            break;
        }
        return prev;
    }

    private boolean isInRadius(ChunkAccess chunk) {
        int x = chunk.getPos().x();
        int z = chunk.getPos().z();
        return MeteorClient.mc.level.getChunkSource().hasChunk(x + 1, z) && MeteorClient.mc.level.getChunkSource().hasChunk(x - 1, z) && MeteorClient.mc.level.getChunkSource().hasChunk(x, z + 1) && MeteorClient.mc.level.getChunkSource().hasChunk(x, z - 1);
    }

    @Override
    public boolean hasNext() {
        return this.chunk != null;
    }

    @Override
    public ChunkAccess next() {
        return this.getNext();
    }
}

