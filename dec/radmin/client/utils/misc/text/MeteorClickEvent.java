/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$Action
 */
package radmin.client.utils.misc.text;

import net.minecraft.network.chat.ClickEvent;

public class MeteorClickEvent
implements ClickEvent {
    public final String value;

    public MeteorClickEvent(String value) {
        this.value = value;
    }

    public ClickEvent.Action action() {
        return ClickEvent.Action.RUN_COMMAND;
    }
}

