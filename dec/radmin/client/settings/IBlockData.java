/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Block
 */
package radmin.client.settings;

import net.minecraft.world.level.block.Block;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WidgetScreen;
import radmin.client.settings.BlockDataSetting;
import radmin.client.utils.misc.ICopyable;
import radmin.client.utils.misc.ISerializable;

public interface IBlockData<T extends ICopyable<T> & ISerializable<T> & IBlockData<T>> {
    public WidgetScreen createScreen(GuiTheme var1, Block var2, BlockDataSetting<T> var3);
}

