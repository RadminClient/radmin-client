/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.world;

import radmin.client.events.Cancellable;

public class ChunkOcclusionEvent
extends Cancellable {
    private static final ChunkOcclusionEvent INSTANCE = new ChunkOcclusionEvent();

    public static ChunkOcclusionEvent get() {
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }
}

