/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import radmin.client.commands.Command;
import radmin.client.utils.Utils;

public class EnderChestCommand
extends Command {
    public EnderChestCommand() {
        super("ender-chest", "Allows you to preview memory of your ender chest.", "ec", "echest");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            Utils.openContainer(Items.ENDER_CHEST.getDefaultInstance(), new ItemStack[27], true);
            return 1;
        });
    }
}

