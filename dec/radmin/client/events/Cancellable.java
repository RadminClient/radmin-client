/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.ICancellable
 */
package radmin.client.events;

import meteordevelopment.orbit.ICancellable;

public class Cancellable
implements ICancellable {
    private boolean cancelled = false;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}

