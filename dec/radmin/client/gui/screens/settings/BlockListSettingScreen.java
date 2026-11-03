/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.BlockListSetting;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.DisplayItemUtils;

public class BlockListSettingScreen
extends CollectionListSettingScreen<Block> {
    public BlockListSettingScreen(GuiTheme theme, BlockListSetting setting) {
        super(theme, "Select Blocks", setting, (Collection)setting.get(), BuiltInRegistries.BLOCK);
    }

    @Override
    protected boolean includeValue(Block value) {
        if (BuiltInRegistries.BLOCK.getKey((Object)value).getPath().endsWith("_wall_banner")) {
            return false;
        }
        Predicate<Block> filter = ((BlockListSetting)this.setting).filter;
        if (filter == null) {
            return value != Blocks.AIR;
        }
        return filter.test(value);
    }

    @Override
    protected WWidget getValueWidget(Block value) {
        return this.theme.itemWithLabel(DisplayItemUtils.toStack(value), Names.get(value));
    }

    @Override
    protected String[] getValueNames(Block value) {
        return new String[]{Names.get(value), BuiltInRegistries.BLOCK.getKey((Object)value).toString()};
    }

    @Override
    protected Block getAdditionalValue(Block value) {
        String path = BuiltInRegistries.BLOCK.getKey((Object)value).getPath();
        if (!path.endsWith("_banner")) {
            return null;
        }
        return (Block)BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace((String)(path.substring(0, path.length() - 6) + "wall_banner")));
    }
}

