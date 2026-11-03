/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.entity.player;

import radmin.client.events.Cancellable;

public class DoAttackEvent
extends Cancellable {
    private static final DoAttackEvent INSTANCE = new DoAttackEvent();

    public static DoAttackEvent get() {
        return INSTANCE;
    }
}

