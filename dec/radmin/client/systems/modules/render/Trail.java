/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.ParticleTypes
 */
package radmin.client.systems.modules.render;

import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ParticleTypeListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Trail
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<List<ParticleType<?>>> particles;
    private final Setting<Boolean> pause;

    public Trail() {
        super(Categories.Render, "trail", "Renders a customizable trail behind your player.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.particles = this.sgGeneral.add(((ParticleTypeListSetting.Builder)((ParticleTypeListSetting.Builder)new ParticleTypeListSetting.Builder().name("particles")).description("Particles to draw.")).defaultValue(new ParticleType[]{ParticleTypes.DRIPPING_OBSIDIAN_TEAR, ParticleTypes.CAMPFIRE_COSY_SMOKE}).build());
        this.pause = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("pause-when-stationary")).description("Whether or not to add particles when you are not moving.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (this.pause.get().booleanValue() && this.mc.player.getX() == this.mc.player.xo && this.mc.player.getY() == this.mc.player.yo && this.mc.player.getZ() == this.mc.player.zo) {
            return;
        }
        for (ParticleType<?> particleType : this.particles.get()) {
            this.mc.level.addParticle((ParticleOptions)particleType, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), 0.0, 0.0, 0.0);
        }
    }
}

