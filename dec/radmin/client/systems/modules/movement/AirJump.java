/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.systems.modules.movement;

import meteordevelopment.orbit.EventHandler;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Freecam;
import radmin.client.utils.misc.input.KeyAction;

public class AirJump
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> maintainLevel;
    private int level;

    public AirJump() {
        super(Categories.Movement, "air-jump", "Lets you jump in the air.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.maintainLevel = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("maintain-level")).description("Maintains your current Y level when holding the jump key.")).defaultValue(false)).build());
    }

    @Override
    public void onActivate() {
        this.level = this.mc.player.blockPosition().getY();
    }

    @EventHandler
    private void onKey(KeyInputEvent event) {
        if (Modules.get().isActive(Freecam.class) || this.mc.screen != null || this.mc.player.onGround()) {
            return;
        }
        if (event.action != KeyAction.Press) {
            return;
        }
        if (this.mc.options.keyJump.matches(event.input)) {
            this.level = this.mc.player.blockPosition().getY();
            this.mc.player.jumpFromGround();
        } else if (this.mc.options.keyShift.matches(event.input)) {
            --this.level;
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (Modules.get().isActive(Freecam.class) || this.mc.player.onGround()) {
            return;
        }
        if (this.maintainLevel.get().booleanValue() && this.mc.player.blockPosition().getY() == this.level && this.mc.options.keyJump.isDown()) {
            this.mc.player.jumpFromGround();
        }
    }
}

