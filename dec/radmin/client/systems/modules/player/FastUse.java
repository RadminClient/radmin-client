/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.systems.modules.player;

import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.ItemListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class FastUse
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;
    private final Setting<List<Item>> items;
    private final Setting<Boolean> blocks;
    private final Setting<Integer> cooldown;

    public FastUse() {
        super(Categories.Player, "fast-use", "Allows you to use items at very high speeds.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("Which items to fast use.")).defaultValue(Mode.All)).build());
        this.items = this.sgGeneral.add(((ItemListSetting.Builder)((ItemListSetting.Builder)((ItemListSetting.Builder)new ItemListSetting.Builder().name("items")).description("Which items should fast place work on in \"Some\" mode.")).visible(() -> this.mode.get() == Mode.Some)).build());
        this.blocks = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("blocks")).description("Fast-places blocks if the mode is \"Some\" mode.")).visible(() -> this.mode.get() == Mode.Some)).defaultValue(false)).build());
        this.cooldown = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("cooldown")).description("Fast-use cooldown in ticks.")).defaultValue(0)).min(0).sliderMax(4).build());
    }

    public int getItemUseCooldown(ItemStack itemStack) {
        if (this.mode.get() == Mode.All || this.shouldWorkSome(itemStack)) {
            return this.cooldown.get();
        }
        return 4;
    }

    private boolean shouldWorkSome(ItemStack itemStack) {
        return this.blocks.get() != false && itemStack.getItem() instanceof BlockItem || this.items.get().contains(itemStack.getItem());
    }

    public static enum Mode {
        All,
        Some;

    }
}

