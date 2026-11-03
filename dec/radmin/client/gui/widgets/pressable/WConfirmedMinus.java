/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.MouseButtonEvent
 */
package radmin.client.gui.widgets.pressable;

import net.minecraft.client.input.MouseButtonEvent;
import radmin.client.gui.widgets.pressable.WMinus;

public class WConfirmedMinus
extends WMinus {
    protected boolean pressedOnce = false;

    @Override
    public boolean onMouseClicked(MouseButtonEvent click, boolean doubled) {
        boolean pressed = super.onMouseClicked(click, doubled);
        if (!pressed) {
            this.pressedOnce = false;
        }
        return pressed;
    }

    @Override
    public boolean onMouseReleased(MouseButtonEvent click) {
        if (this.pressed && this.pressedOnce) {
            super.onMouseReleased(click);
        }
        this.pressedOnce = this.pressed;
        this.pressed = false;
        return false;
    }
}

