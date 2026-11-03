/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.widgets;

import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.systems.modules.Modules;
import radmin.client.utils.misc.Keybind;

public class WKeybind
extends WHorizontalList {
    public Runnable action;
    public Runnable actionOnSet;
    private WButton button;
    private final Keybind keybind;
    private final Keybind defaultValue;
    protected boolean listening;

    public WKeybind(Keybind keybind, Keybind defaultValue) {
        this.keybind = keybind;
        this.defaultValue = defaultValue;
    }

    public boolean isListening() {
        return this.listening;
    }

    @Override
    public void init() {
        this.button = this.add(this.theme.button("")).widget();
        this.button.action = () -> {
            this.listening = true;
            this.button.set("...");
            if (this.actionOnSet != null) {
                this.actionOnSet.run();
            }
        };
        this.refreshLabel();
    }

    public boolean onClear() {
        if (this.listening) {
            this.keybind.reset();
            this.reset();
            return true;
        }
        return false;
    }

    public boolean onAction(boolean isKey, int value, int modifiers) {
        if (this.listening && this.keybind.canBindTo(isKey, value, modifiers)) {
            this.keybind.set(isKey, value, modifiers);
            this.reset();
            return true;
        }
        return false;
    }

    public void resetBind() {
        this.keybind.set(this.defaultValue);
        this.reset();
    }

    public void reset() {
        this.listening = false;
        this.refreshLabel();
        if (Modules.get().isBinding()) {
            Modules.get().setModuleToBind(null);
        }
        if (this.action != null) {
            this.action.run();
        }
    }

    private void refreshLabel() {
        this.button.set(this.keybind.toString());
    }
}

