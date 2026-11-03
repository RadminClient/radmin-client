/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.meteor;

import radmin.client.events.Cancellable;

public class CharTypedEvent
extends Cancellable {
    private static final CharTypedEvent INSTANCE = new CharTypedEvent();
    public char c;

    public static CharTypedEvent get(char c) {
        INSTANCE.setCancelled(false);
        CharTypedEvent.INSTANCE.c = c;
        return INSTANCE;
    }
}

