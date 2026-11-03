/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.MouseButtonEvent
 */
package radmin.client.gui.widgets;

import net.minecraft.client.input.MouseButtonEvent;
import radmin.client.gui.widgets.pressable.WPressable;
import radmin.client.utils.render.color.Color;

public abstract class WLabel
extends WPressable {
    public Color color;
    protected String text;
    protected boolean title;

    public WLabel(String text, boolean title) {
        this.text = text;
        this.title = title;
    }

    @Override
    protected void onCalculateSize() {
        this.width = this.theme.textWidth(this.text, this.text.length(), this.title);
        this.height = this.theme.textHeight(this.title);
    }

    @Override
    public boolean onMouseClicked(MouseButtonEvent click, boolean doubled) {
        if (this.action != null) {
            return super.onMouseClicked(click, doubled);
        }
        return false;
    }

    @Override
    public boolean onMouseReleased(MouseButtonEvent click) {
        if (this.action != null) {
            return super.onMouseReleased(click);
        }
        return false;
    }

    public void set(String text) {
        if ((double)Math.round(this.theme.textWidth(text, text.length(), this.title)) != this.width) {
            this.invalidate();
        }
        this.text = text;
    }

    public String get() {
        return this.text;
    }

    public WLabel color(Color color) {
        this.color = color;
        return this;
    }
}

