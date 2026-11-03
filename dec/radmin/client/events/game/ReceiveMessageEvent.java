/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.chat.GuiMessageTag
 *  net.minecraft.network.chat.Component
 */
package radmin.client.events.game;

import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import radmin.client.events.Cancellable;

public class ReceiveMessageEvent
extends Cancellable {
    private static final ReceiveMessageEvent INSTANCE = new ReceiveMessageEvent();
    private Component message;
    private GuiMessageTag indicator;
    private boolean modified;
    public int id;

    public static ReceiveMessageEvent get(Component message, GuiMessageTag indicator, int id) {
        INSTANCE.setCancelled(false);
        ReceiveMessageEvent.INSTANCE.message = message;
        ReceiveMessageEvent.INSTANCE.indicator = indicator;
        ReceiveMessageEvent.INSTANCE.modified = false;
        ReceiveMessageEvent.INSTANCE.id = id;
        return INSTANCE;
    }

    public Component getMessage() {
        return this.message;
    }

    public GuiMessageTag getIndicator() {
        return this.indicator;
    }

    public void setMessage(Component message) {
        this.message = message;
        this.modified = true;
    }

    public void setIndicator(GuiMessageTag indicator) {
        this.indicator = indicator;
        this.modified = true;
    }

    public boolean isModified() {
        return this.modified;
    }
}

