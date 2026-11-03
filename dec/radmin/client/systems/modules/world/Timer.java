/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.world;

import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Timer
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> multiplier;
    public static final double OFF = 1.0;
    private double override;

    public Timer() {
        super(Categories.World, "timer", "Changes the speed of everything in your game.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.multiplier = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("multiplier")).description("The timer multiplier amount.")).defaultValue(1.0).min(0.1).sliderMin(0.1).build());
        this.override = 1.0;
    }

    public double getMultiplier() {
        return this.override != 1.0 ? this.override : (this.isActive() ? this.multiplier.get() : 1.0);
    }

    public void setOverride(double override) {
        this.override = override;
    }
}

