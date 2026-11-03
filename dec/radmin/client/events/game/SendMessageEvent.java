/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.game;

import radmin.client.events.Cancellable;

public class SendMessageEvent
extends Cancellable {
    private static final SendMessageEvent INSTANCE = new SendMessageEvent();
    public String message;

    public static SendMessageEvent get(String message) {
        INSTANCE.setCancelled(false);
        SendMessageEvent.INSTANCE.message = message;
        return INSTANCE;
    }
}

