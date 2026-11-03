/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.player;

import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Multitask
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> attackingEntities;

    public Multitask() {
        super(Categories.Player, "multitask", "Lets you use items and attack at the same time.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.attackingEntities = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("attacking-entities")).description("Lets you attack entities while using an item.")).defaultValue(true)).build());
    }

    public boolean attackingEntities() {
        return this.isActive() && this.attackingEntities.get() != false;
    }
}

