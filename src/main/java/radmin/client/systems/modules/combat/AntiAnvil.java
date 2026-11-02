/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.systems.modules.combat;

import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class AntiAnvil extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> swing = sgGeneral.add(new BoolSetting.Builder()
        .name("swing")
        .description("Swings your hand client-side when placing.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Makes you rotate when placing.")
        .defaultValue(true)
        .build()
    );

    public AntiAnvil() {
        super(Categories.Combat, "anti-anvil", "Automatically prevents Auto Anvil by placing between you and the anvil.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        for (int i = 0; i <= mc.player.blockInteractionRange(); i++) {
            BlockPos pos = mc.player.blockPosition().offset(0, i + 3, 0);

            if (mc.level.getBlockState(pos).getBlock() == Blocks.ANVIL && mc.level.getBlockState(pos.below()).isAir()) {
                if (BlockUtils.place(pos.below(), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 15, swing.get(), true))
                    break;
            }
        }
    }
}
