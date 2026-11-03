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
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.StatusEffectListSetting;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class NoStatusEffects
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<List<MobEffect>> blockedEffects;

    public NoStatusEffects() {
        super(Categories.Player, "no-status-effects", "Blocks specified status effects.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.blockedEffects = this.sgGeneral.add(((StatusEffectListSetting.Builder)((StatusEffectListSetting.Builder)new StatusEffectListSetting.Builder().name("blocked-effects")).description("Effects to block.")).defaultValue((MobEffect)MobEffects.LEVITATION.value(), (MobEffect)MobEffects.JUMP_BOOST.value(), (MobEffect)MobEffects.SLOW_FALLING.value(), (MobEffect)MobEffects.DOLPHINS_GRACE.value()).build());
    }

    public boolean shouldBlock(MobEffect effect) {
        return this.isActive() && this.blockedEffects.get().contains(effect);
    }
}

