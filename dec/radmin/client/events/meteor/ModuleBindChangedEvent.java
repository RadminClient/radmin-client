/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.meteor;

import radmin.client.systems.modules.Module;

public class ModuleBindChangedEvent {
    private static final ModuleBindChangedEvent INSTANCE = new ModuleBindChangedEvent();
    public Module module;

    public static ModuleBindChangedEvent get(Module module) {
        ModuleBindChangedEvent.INSTANCE.module = module;
        return INSTANCE;
    }
}

