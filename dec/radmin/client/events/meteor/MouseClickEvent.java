/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.client.input.MouseButtonInfo
 */
package radmin.client.events.meteor;

import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import radmin.client.events.Cancellable;
import radmin.client.utils.misc.input.KeyAction;

public class MouseClickEvent
extends Cancellable {
    private static final MouseClickEvent INSTANCE = new MouseClickEvent();
    public MouseButtonEvent click;
    public MouseButtonInfo input;
    public KeyAction action;

    public static MouseClickEvent get(MouseButtonEvent click, KeyAction action) {
        INSTANCE.setCancelled(false);
        MouseClickEvent.INSTANCE.click = click;
        MouseClickEvent.INSTANCE.input = click.buttonInfo();
        MouseClickEvent.INSTANCE.action = action;
        return INSTANCE;
    }

    public int button() {
        return MouseClickEvent.INSTANCE.input.button();
    }
}

