/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.systems.modules.world;

import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.FindItemResult;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.player.PlayerUtils;
import radmin.client.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;

public class AutoShearer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> distance = sgGeneral.add(new DoubleSetting.Builder()
        .name("distance")
        .description("The maximum distance the sheep have to be to be sheared.")
        .min(0.0)
        .defaultValue(5.0)
        .build()
    );

    private final Setting<Boolean> antiBreak = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-break")
        .description("Prevents shears from being broken.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Automatically faces towards the animal being sheared.")
        .defaultValue(true)
        .build()
    );

    private Entity entity;
    private InteractionHand hand;

    public AutoShearer() {
        super(Categories.World, "auto-shearer", "Automatically shears sheep.");
    }

    @Override
    public void onDeactivate() {
        entity = null;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        entity = null;

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof Sheep sheep) || sheep.isSheared() || sheep.isBaby() || !PlayerUtils.isWithin(entity, distance.get()))
                continue;

            FindItemResult findShear = InvUtils.findInHotbar(itemStack -> itemStack.getItem() == Items.SHEARS && (!antiBreak.get() || itemStack.getDamageValue() < itemStack.getMaxDamage() - 1));
            if (!InvUtils.swap(findShear.slot(), true)) return;

            this.hand = findShear.getHand();
            this.entity = entity;

            if (rotate.get())
                Rotations.rotate(Rotations.getYaw(entity), Rotations.getPitch(entity), -100, this::interact);
            else interact();

            return;
        }
    }

    private void interact() {
        EntityHitResult location = new EntityHitResult(entity, entity.getBoundingBox().getCenter());
        mc.gameMode.interact(mc.player, entity, location, hand);
        InvUtils.swapBack();
    }
}
