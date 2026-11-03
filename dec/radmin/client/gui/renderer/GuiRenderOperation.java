/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.renderer;

import radmin.client.utils.misc.Pool;
import radmin.client.utils.render.color.Color;

public abstract class GuiRenderOperation<T extends GuiRenderOperation<T>> {
    protected double x;
    protected double y;
    protected Color color;

    public void set(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void run(Pool<T> pool) {
        this.onRun();
        pool.free(this);
    }

    protected abstract void onRun();
}

