/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.entity.player;

import radmin.client.events.Cancellable;

public class DoItemUseEvent
extends Cancellable {
    private static final DoItemUseEvent INSTANCE = new DoItemUseEvent();

    public static DoItemUseEvent get() {
        return INSTANCE;
    }
}

