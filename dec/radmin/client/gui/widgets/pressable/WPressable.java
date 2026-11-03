/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.MouseButtonEvent
 */
package radmin.client.gui.widgets.pressable;

import net.minecraft.client.input.MouseButtonEvent;
import radmin.client.gui.widgets.WWidget;

public abstract class WPressable
extends WWidget {
    public Runnable action;
    protected boolean pressed;

    @Override
    public boolean onMouseClicked(MouseButtonEvent click, boolean doubled) {
        if (this.mouseOver && (click.button() == 0 || click.button() == 1)) {
            this.pressed = true;
        }
        return this.pressed;
    }

    @Override
    public boolean onMouseReleased(MouseButtonEvent click) {
        if (this.pressed) {
            this.onPressed(click.button());
            if (this.action != null) {
                this.action.run();
            }
            this.pressed = false;
        }
        return false;
    }

    protected void onPressed(int button) {
    }
}

