/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.misc.text;

import radmin.client.utils.misc.text.MeteorClickEvent;

public class RunnableClickEvent
extends MeteorClickEvent {
    public final Runnable runnable;

    public RunnableClickEvent(Runnable runnable) {
        super(null);
        this.runnable = runnable;
    }
}

