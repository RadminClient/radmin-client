/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.systems.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import radmin.client.events.entity.player.AttackEntityEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.entity.DamageUtils;
import radmin.client.utils.player.InvUtils;

public class AutoWeapon
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Weapon> weapon;
    private final Setting<Integer> threshold;
    private final Setting<Boolean> antiBreak;

    public AutoWeapon() {
        super(Categories.Combat, "auto-weapon", "Finds the best weapon to use in your hotbar.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.weapon = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("weapon")).description("What type of weapon to use.")).defaultValue(Weapon.Sword)).build());
        this.threshold = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("threshold")).description("If the non-preferred weapon produces this much damage this will favor it over your preferred weapon.")).defaultValue(4)).build());
        this.antiBreak = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("anti-break")).description("Prevents you from breaking your weapon.")).defaultValue(false)).build());
    }

    @EventHandler
    private void onAttack(AttackEntityEvent event) {
        Entity entity = event.entity;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            InvUtils.swap(this.getBestWeapon(livingEntity), false);
        }
    }

    private int getBestWeapon(LivingEntity target) {
        int slotS = this.mc.player.getInventory().getSelectedSlot();
        int slotA = this.mc.player.getInventory().getSelectedSlot();
        double damageS = 0.0;
        double damageA = 0.0;
        for (int i = 0; i < 9; ++i) {
            double currentDamageA;
            ItemStack stack = this.mc.player.getInventory().getItem(i);
            if (stack.is(ItemTags.SWORDS) && (!this.antiBreak.get().booleanValue() || stack.getMaxDamage() - stack.getDamageValue() > 10)) {
                double currentDamageS = DamageUtils.getAttackDamage((LivingEntity)this.mc.player, (Entity)target, stack);
                if (!(currentDamageS > damageS)) continue;
                damageS = currentDamageS;
                slotS = i;
                continue;
            }
            if (!(stack.getItem() instanceof AxeItem) || this.antiBreak.get().booleanValue() && stack.getMaxDamage() - stack.getDamageValue() <= 10 || !((currentDamageA = (double)DamageUtils.getAttackDamage((LivingEntity)this.mc.player, (Entity)target, stack)) > damageA)) continue;
            damageA = currentDamageA;
            slotA = i;
        }
        if (this.weapon.get() == Weapon.Sword && (double)this.threshold.get().intValue() > damageA - damageS) {
            return slotS;
        }
        if (this.weapon.get() == Weapon.Axe && (double)this.threshold.get().intValue() > damageS - damageA) {
            return slotA;
        }
        if (this.weapon.get() == Weapon.Sword && (double)this.threshold.get().intValue() < damageA - damageS) {
            return slotA;
        }
        if (this.weapon.get() == Weapon.Axe && (double)this.threshold.get().intValue() < damageS - damageA) {
            return slotS;
        }
        return this.mc.player.getInventory().getSelectedSlot();
    }

    public static enum Weapon {
        Sword,
        Axe;

    }
}

