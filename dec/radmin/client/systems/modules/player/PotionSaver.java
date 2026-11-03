/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffects
 */
package radmin.client.systems.modules.player;

import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.StatusEffectListSetting;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.PlayerUtils;

public class PotionSaver
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<List<MobEffect>> effects;
    public final Setting<Boolean> onlyWhenStationary;

    public PotionSaver() {
        super(Categories.Player, "potion-saver", "Stops potion effects ticking when you stand still.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.effects = this.sgGeneral.add(((StatusEffectListSetting.Builder)((StatusEffectListSetting.Builder)new StatusEffectListSetting.Builder().name("effects")).description("The effects to preserve.")).defaultValue((MobEffect)MobEffects.STRENGTH.value(), (MobEffect)MobEffects.ABSORPTION.value(), (MobEffect)MobEffects.RESISTANCE.value(), (MobEffect)MobEffects.FIRE_RESISTANCE.value(), (MobEffect)MobEffects.SPEED.value(), (MobEffect)MobEffects.HASTE.value(), (MobEffect)MobEffects.REGENERATION.value(), (MobEffect)MobEffects.WATER_BREATHING.value(), (MobEffect)MobEffects.SATURATION.value(), (MobEffect)MobEffects.LUCK.value(), (MobEffect)MobEffects.SLOW_FALLING.value(), (MobEffect)MobEffects.DOLPHINS_GRACE.value(), (MobEffect)MobEffects.CONDUIT_POWER.value(), (MobEffect)MobEffects.HERO_OF_THE_VILLAGE.value()).build());
        this.onlyWhenStationary = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("only-when-stationary")).description("Only freezes effects when you aren't moving.")).defaultValue(false)).build());
    }

    public boolean shouldFreeze(MobEffect effect) {
        return this.isActive() && (this.onlyWhenStationary.get() == false || !PlayerUtils.isMoving()) && !this.mc.player.getActiveEffects().isEmpty() && this.effects.get().contains(effect);
    }
}

