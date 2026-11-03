/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.systems.modules.player;

import meteordevelopment.orbit.EventHandler;
import radmin.client.events.entity.player.BlockBreakingCooldownEvent;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.misc.input.KeyAction;

public class BreakDelay
extends Module {
    SettingGroup sgGeneral;
    private final Setting<Integer> cooldown;
    private final Setting<Boolean> noInstaBreak;
    private boolean breakBlockCooldown;

    public BreakDelay() {
        super(Categories.Player, "break-delay", "Changes the delay between breaking blocks.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.cooldown = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("cooldown")).description("Block break cooldown in ticks.")).defaultValue(0)).min(0).sliderMax(5).build());
        this.noInstaBreak = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("no-insta-break")).description("Prevents you from misbreaking blocks if you can instantly break them.")).defaultValue(false)).build());
        this.breakBlockCooldown = false;
    }

    @EventHandler
    private void onBlockBreakingCooldown(BlockBreakingCooldownEvent event) {
        if (this.breakBlockCooldown) {
            event.cooldown = 5;
            this.breakBlockCooldown = false;
        } else {
            event.cooldown = this.cooldown.get();
        }
    }

    @EventHandler
    private void onClick(MouseClickEvent event) {
        if (event.action == KeyAction.Press && this.noInstaBreak.get().booleanValue()) {
            this.breakBlockCooldown = true;
        }
    }

    public boolean preventInstaBreak() {
        return this.isActive() && this.noInstaBreak.get() != false;
    }
}

