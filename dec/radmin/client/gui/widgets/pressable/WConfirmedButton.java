/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.MouseButtonEvent
 */
package radmin.client.gui.widgets.pressable;

import net.minecraft.client.input.MouseButtonEvent;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.widgets.pressable.WButton;

public abstract class WConfirmedButton
extends WButton {
    protected boolean pressedOnce = false;
    protected String confirmText;

    public WConfirmedButton(String text, String confirmText, GuiTexture texture) {
        super(text, texture);
        this.confirmText = confirmText;
    }

    @Override
    public boolean onMouseClicked(MouseButtonEvent click, boolean doubled) {
        boolean pressed = super.onMouseClicked(click, doubled);
        if (!pressed) {
            this.pressedOnce = false;
            this.invalidate();
        }
        return pressed;
    }

    @Override
    public boolean onMouseReleased(MouseButtonEvent click) {
        if (this.pressed && this.pressedOnce) {
            super.onMouseReleased(click);
        }
        this.pressedOnce = this.pressed;
        this.invalidate();
        this.pressed = false;
        return false;
    }

    @Override
    public String getText() {
        return this.pressedOnce ? this.confirmText : this.text;
    }

    public void set(String text, String confirmText) {
        super.set(text);
        this.confirmText = confirmText;
    }
}

