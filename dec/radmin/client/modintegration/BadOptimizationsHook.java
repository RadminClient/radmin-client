/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.modintegration;

import java.util.function.BooleanSupplier;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Fullbright;
import radmin.client.systems.modules.render.Xray;

public class BadOptimizationsHook
implements BooleanSupplier {
    private int lastState;

    @Override
    public boolean getAsBoolean() {
        Modules m = Modules.get();
        if (m == null) {
            return false;
        }
        int state = (m.get(Fullbright.class).getGamma() ? 1 : 0) | (m.isActive(Xray.class) ? 2 : 0);
        boolean changed = state != this.lastState;
        this.lastState = state;
        return changed;
    }
}

