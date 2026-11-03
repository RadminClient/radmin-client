/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.block.Blocks
 */
package radmin.client.systems.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.world.BlockUtils;

public class AntiAnvil
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> swing;
    private final Setting<Boolean> rotate;

    public AntiAnvil() {
        super(Categories.Combat, "anti-anvil", "Automatically prevents Auto Anvil by placing between you and the anvil.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.swing = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("swing")).description("Swings your hand client-side when placing.")).defaultValue(true)).build());
        this.rotate = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("rotate")).description("Makes you rotate when placing.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        BlockPos pos;
        int i = 0;
        while (!(!((double)i <= this.mc.player.blockInteractionRange()) || this.mc.level.getBlockState(pos = this.mc.player.blockPosition().offset(0, i + 3, 0)).getBlock() == Blocks.ANVIL && this.mc.level.getBlockState(pos.below()).isAir() && BlockUtils.place(pos.below(), InvUtils.findInHotbar(Items.OBSIDIAN), this.rotate.get(), 15, this.swing.get(), true))) {
            ++i;
        }
    }
}

