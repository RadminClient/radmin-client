/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.systems.modules.world;

import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Timer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
        .name("multiplier")
        .description("The timer multiplier amount.")
        .defaultValue(1)
        .min(0.1)
        .sliderMin(0.1)
        .build()
    );

    public static final double OFF = 1;
    private double override = 1;

    public Timer() {
        super(Categories.World, "timer", "Changes the speed of everything in your game.");
    }

    public double getMultiplier() {
        return override != OFF ? override : (isActive() ? multiplier.get() : OFF);
    }

    public void setOverride(double override) {
        this.override = override;
    }
}
