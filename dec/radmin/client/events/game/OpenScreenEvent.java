/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.events.game;

import net.minecraft.client.gui.screens.Screen;
import radmin.client.events.Cancellable;

public class OpenScreenEvent
extends Cancellable {
    private static final OpenScreenEvent INSTANCE = new OpenScreenEvent();
    public Screen screen;

    public static OpenScreenEvent get(Screen screen) {
        INSTANCE.setCancelled(false);
        OpenScreenEvent.INSTANCE.screen = screen;
        return INSTANCE;
    }
}

