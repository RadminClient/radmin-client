/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.movement;

import radmin.client.settings.EnumSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Sneak
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;

    public Sneak() {
        super(Categories.Movement, "sneak", "Sneaks for you");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("Which method to sneak.")).defaultValue(Mode.Vanilla)).build());
    }

    public boolean doPacket() {
        return this.isActive() && !this.mc.player.getAbilities().flying && this.mode.get() == Mode.Packet;
    }

    public boolean doVanilla() {
        return this.isActive() && !this.mc.player.getAbilities().flying && this.mode.get() == Mode.Vanilla;
    }

    public static enum Mode {
        Packet,
        Vanilla;

    }
}

