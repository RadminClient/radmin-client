/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.settings;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.WidgetScreen;
import radmin.client.utils.misc.IChangeable;
import radmin.client.utils.misc.ICopyable;
import radmin.client.utils.misc.ISerializable;
import net.minecraft.world.level.block.Block;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IChangeable & IBlockData<T>> {
    WidgetScreen createScreen(GuiTheme theme, Block block, BlockDataSetting<T> setting);
}
