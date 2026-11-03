/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.KeyEvent
 */
package radmin.client.events.meteor;

import net.minecraft.client.input.KeyEvent;
import radmin.client.events.Cancellable;
import radmin.client.utils.misc.input.KeyAction;

public class KeyInputEvent
extends Cancellable {
    private static final KeyInputEvent INSTANCE = new KeyInputEvent();
    public KeyEvent input;
    public KeyAction action;

    public static KeyInputEvent get(KeyEvent input, KeyAction action) {
        INSTANCE.setCancelled(false);
        KeyInputEvent.INSTANCE.input = input;
        KeyInputEvent.INSTANCE.action = action;
        return INSTANCE;
    }

    public int key() {
        return KeyInputEvent.INSTANCE.input.key();
    }

    public int modifiers() {
        return KeyInputEvent.INSTANCE.input.modifiers();
    }
}

