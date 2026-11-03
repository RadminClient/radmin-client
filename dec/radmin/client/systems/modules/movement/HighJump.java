/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.systems.modules.movement;

import meteordevelopment.orbit.EventHandler;
import radmin.client.events.entity.player.JumpVelocityMultiplierEvent;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class HighJump
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> multiplier;

    public HighJump() {
        super(Categories.Movement, "high-jump", "Makes you jump higher than normal.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.multiplier = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("jump-multiplier")).description("Jump height multiplier.")).defaultValue(1.0).min(0.0).build());
    }

    @EventHandler
    private void onJumpVelocityMultiplier(JumpVelocityMultiplierEvent event) {
        event.multiplier = (float)((double)event.multiplier * this.multiplier.get());
    }
}

