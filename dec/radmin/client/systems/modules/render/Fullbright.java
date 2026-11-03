/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.level.LightLayer
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.LightLayer;
import radmin.client.events.world.TickEvent;
import radmin.client.mixin.MobEffectInstanceAccessor;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Fullbright
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<Mode> mode;
    public final Setting<LightLayer> lightType;
    private final Setting<Integer> minimumLightLevel;

    public Fullbright() {
        super(Categories.Render, "fullbright", "Lights up your world!");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("The mode to use for Fullbright.")).defaultValue(Mode.Gamma)).onChanged(mode -> {
            if (this.isActive()) {
                if (mode != Mode.Potion) {
                    this.disableNightVision();
                }
                if (this.mc.levelRenderer != null) {
                    this.mc.levelRenderer.allChanged();
                }
            }
        })).build());
        this.lightType = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("light-type")).description("Which type of light to use for Luminance mode.")).defaultValue(LightLayer.BLOCK)).visible(() -> this.mode.get() == Mode.Luminance)).onChanged(lightLayer -> {
            if (this.mc.levelRenderer != null && this.isActive()) {
                this.mc.levelRenderer.allChanged();
            }
        })).build());
        this.minimumLightLevel = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("minimum-light-level")).description("Minimum light level when using Luminance mode.")).visible(() -> this.mode.get() == Mode.Luminance)).defaultValue(8)).range(0, 15).sliderMax(15).onChanged(n -> {
            if (this.mc.levelRenderer != null && this.isActive()) {
                this.mc.levelRenderer.allChanged();
            }
        })).build());
    }

    @Override
    public void onActivate() {
        if (this.mode.get() == Mode.Luminance) {
            this.mc.levelRenderer.allChanged();
        }
    }

    @Override
    public void onDeactivate() {
        if (this.mode.get() == Mode.Luminance) {
            this.mc.levelRenderer.allChanged();
        } else if (this.mode.get() == Mode.Potion) {
            this.disableNightVision();
        }
    }

    public int getLuminance(LightLayer type) {
        if (!this.isActive() || this.mode.get() != Mode.Luminance || type != this.lightType.get()) {
            return 0;
        }
        return this.minimumLightLevel.get();
    }

    public boolean getGamma() {
        return this.isActive() && this.mode.get() == Mode.Gamma;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (this.mc.player == null || !this.mode.get().equals((Object)Mode.Potion)) {
            return;
        }
        if (this.mc.player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((Object)((MobEffect)MobEffects.NIGHT_VISION.value())))) {
            MobEffectInstance instance = this.mc.player.getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((Object)((MobEffect)MobEffects.NIGHT_VISION.value())));
            if (instance != null && instance.getDuration() < 420) {
                ((MobEffectInstanceAccessor)instance).meteor$setDuration(420);
            }
        } else {
            this.mc.player.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((Object)((MobEffect)MobEffects.NIGHT_VISION.value())), 420, 0));
        }
    }

    private void disableNightVision() {
        if (this.mc.player == null) {
            return;
        }
        if (this.mc.player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((Object)((MobEffect)MobEffects.NIGHT_VISION.value())))) {
            this.mc.player.removeEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder((Object)((MobEffect)MobEffects.NIGHT_VISION.value())));
        }
    }

    public static enum Mode {
        Gamma,
        Potion,
        Luminance;

    }
}

