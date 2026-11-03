/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.renderer.operations;

import radmin.client.gui.renderer.GuiRenderOperation;
import radmin.client.renderer.text.TextRenderer;

public class TextOperation
extends GuiRenderOperation<TextOperation> {
    private String text;
    private TextRenderer renderer;
    public boolean title;

    public TextOperation set(String text, TextRenderer renderer, boolean title) {
        this.text = text;
        this.renderer = renderer;
        this.title = title;
        return this;
    }

    @Override
    protected void onRun() {
        this.renderer.render(this.text, this.x, this.y, this.color);
    }
}

