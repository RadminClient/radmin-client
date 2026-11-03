/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
 *  net.minecraft.world.entity.MoverType
 */
package radmin.client.systems.modules.movement.speed;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.MoverType;
import radmin.client.events.entity.player.PlayerMoveEvent;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.speed.SpeedMode;
import radmin.client.systems.modules.movement.speed.SpeedModes;
import radmin.client.systems.modules.movement.speed.modes.Strafe;
import radmin.client.systems.modules.movement.speed.modes.Vanilla;
import radmin.client.systems.modules.world.Timer;
import radmin.client.utils.player.PlayerUtils;

public class Speed
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<SpeedModes> speedMode;
    public final Setting<Double> vanillaSpeed;
    public final Setting<Double> ncpSpeed;
    public final Setting<Boolean> ncpSpeedLimit;
    public final Setting<Double> timer;
    public final Setting<Boolean> inLiquids;
    public final Setting<Boolean> whenSneaking;
    public final Setting<Boolean> vanillaOnGround;
    private SpeedMode currentMode;

    public Speed() {
        super(Categories.Movement, "speed", "Modifies your movement speed when moving on the ground.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.speedMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("The method of applying speed.")).defaultValue(SpeedModes.Vanilla)).onModuleActivated(speedModesSetting -> this.onSpeedModeChanged((SpeedModes)((Object)((Object)speedModesSetting.get()))))).onChanged(this::onSpeedModeChanged)).build());
        this.vanillaSpeed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("vanilla-speed")).description("The speed in blocks per second.")).defaultValue(5.6).min(0.0).sliderMax(20.0).visible(() -> this.speedMode.get() == SpeedModes.Vanilla)).build());
        this.ncpSpeed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("strafe-speed")).description("The speed.")).visible(() -> this.speedMode.get() == SpeedModes.Strafe)).defaultValue(1.6).min(0.0).sliderMax(3.0).build());
        this.ncpSpeedLimit = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("speed-limit")).description("Limits your speed on servers with very strict anticheats.")).visible(() -> this.speedMode.get() == SpeedModes.Strafe)).defaultValue(false)).build());
        this.timer = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("timer")).description("Timer override.")).defaultValue(1.0).min(0.01).sliderMin(0.01).sliderMax(10.0).build());
        this.inLiquids = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("in-liquids")).description("Uses speed when in lava or water.")).defaultValue(false)).build());
        this.whenSneaking = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("when-sneaking")).description("Uses speed when sneaking.")).defaultValue(false)).build());
        this.vanillaOnGround = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("only-on-ground")).description("Uses speed only when standing on a block.")).visible(() -> this.speedMode.get() == SpeedModes.Vanilla)).defaultValue(false)).build());
        this.onSpeedModeChanged(this.speedMode.get());
    }

    @Override
    public void onActivate() {
        this.currentMode.onActivate();
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(1.0);
        this.currentMode.onDeactivate();
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if (event.type != MoverType.SELF || this.stopSpeed()) {
            return;
        }
        if (this.timer.get() != 1.0) {
            Modules.get().get(Timer.class).setOverride(PlayerUtils.isMoving() ? this.timer.get() : 1.0);
        }
        this.currentMode.onMove(event);
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (this.stopSpeed()) {
            return;
        }
        this.currentMode.onTick();
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof ClientboundPlayerPositionPacket) {
            this.currentMode.onRubberband();
        }
    }

    private void onSpeedModeChanged(SpeedModes mode) {
        switch (mode) {
            case Vanilla: {
                this.currentMode = new Vanilla();
                break;
            }
            case Strafe: {
                this.currentMode = new Strafe();
            }
        }
    }

    private boolean stopSpeed() {
        if (this.mc.player.isFallFlying() || this.mc.player.onClimbable() || this.mc.player.getVehicle() != null) {
            return true;
        }
        if (!this.whenSneaking.get().booleanValue() && this.mc.player.isShiftKeyDown()) {
            return true;
        }
        if (this.vanillaOnGround.get().booleanValue() && !this.mc.player.onGround() && this.speedMode.get() == SpeedModes.Vanilla) {
            return true;
        }
        return this.inLiquids.get() == false && (this.mc.player.isInWater() || this.mc.player.isInLava());
    }

    @Override
    public String getInfoString() {
        return this.currentMode.getHudString();
    }
}

