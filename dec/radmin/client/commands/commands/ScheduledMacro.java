/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.commands.commands;

import radmin.client.MeteorClient;
import radmin.client.systems.macros.Macro;

class ScheduledMacro {
    public int delay;
    public Macro macro;

    public ScheduledMacro(int tickDelay, Macro scheduledMacro) {
        this.delay = tickDelay;
        this.macro = scheduledMacro;
    }

    public void tick() {
        --this.delay;
    }

    public boolean run() {
        if (this.delay > 0) {
            return false;
        }
        this.runMacro();
        return true;
    }

    private void runMacro() {
        if (MeteorClient.mc.player == null) {
            return;
        }
        this.macro.onAction();
    }
}

