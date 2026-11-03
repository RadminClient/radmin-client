/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.misc;

import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class AntiPacketKick
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<Boolean> catchExceptions;
    public final Setting<Boolean> logExceptions;

    public AntiPacketKick() {
        super(Categories.Misc, "anti-packet-kick", "Attempts to prevent you from being disconnected by large packets.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.catchExceptions = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("catch-exceptions")).description("Drops corrupted packets.")).defaultValue(false)).build());
        this.logExceptions = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("log-exceptions")).description("Logs caught exceptions.")).defaultValue(true)).visible(this.catchExceptions::get)).build());
    }

    public boolean catchExceptions() {
        return this.isActive() && this.catchExceptions.get() != false;
    }
}

