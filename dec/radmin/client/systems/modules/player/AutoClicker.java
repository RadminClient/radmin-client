/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.systems.modules.player;

import meteordevelopment.orbit.EventHandler;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.Utils;

public class AutoClicker
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> inScreens;
    private final Setting<Mode> leftClickMode;
    private final Setting<Integer> leftClickDelay;
    private final Setting<Mode> rightClickMode;
    private final Setting<Integer> rightClickDelay;
    private int rightClickTimer;
    private int leftClickTimer;

    public AutoClicker() {
        super(Categories.Player, "auto-clicker", "Automatically clicks.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.inScreens = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("while-in-screens")).description("Whether to click while a screen is open.")).defaultValue(true)).build());
        this.leftClickMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode-left")).description("The method of clicking for left clicks.")).defaultValue(Mode.Press)).build());
        this.leftClickDelay = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("delay-left")).description("The amount of delay between left clicks in ticks.")).defaultValue(2)).min(0).sliderMax(60).visible(() -> this.leftClickMode.get() == Mode.Press)).build());
        this.rightClickMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode-right")).description("The method of clicking for right clicks.")).defaultValue(Mode.Press)).build());
        this.rightClickDelay = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("delay-right")).description("The amount of delay between right clicks in ticks.")).defaultValue(2)).min(0).sliderMax(60).visible(() -> this.rightClickMode.get() == Mode.Press)).build());
    }

    @Override
    public void onActivate() {
        this.rightClickTimer = 0;
        this.leftClickTimer = 0;
        this.mc.options.keyAttack.setDown(false);
        this.mc.options.keyUse.setDown(false);
    }

    @Override
    public void onDeactivate() {
        this.mc.options.keyAttack.setDown(false);
        this.mc.options.keyUse.setDown(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!this.inScreens.get().booleanValue() && this.mc.screen != null) {
            return;
        }
        switch (this.leftClickMode.get().ordinal()) {
            case 0: {
                break;
            }
            case 1: {
                this.mc.options.keyAttack.setDown(true);
                break;
            }
            case 2: {
                ++this.leftClickTimer;
                if (this.leftClickTimer <= this.leftClickDelay.get()) break;
                Utils.leftClick();
                this.leftClickTimer = 0;
            }
        }
        switch (this.rightClickMode.get().ordinal()) {
            case 0: {
                break;
            }
            case 1: {
                this.mc.options.keyUse.setDown(true);
                break;
            }
            case 2: {
                ++this.rightClickTimer;
                if (this.rightClickTimer <= this.rightClickDelay.get()) break;
                Utils.rightClick();
                this.rightClickTimer = 0;
            }
        }
    }

    public static enum Mode {
        Disabled,
        Hold,
        Press;

    }
}

