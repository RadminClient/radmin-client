/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.game;

public class GameJoinedEvent {
    private static final GameJoinedEvent INSTANCE = new GameJoinedEvent();

    public static GameJoinedEvent get() {
        return INSTANCE;
    }
}

