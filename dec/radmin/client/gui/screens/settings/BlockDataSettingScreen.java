/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.gui.screens.settings;

import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.screens.settings.base.CollectionMapSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.settings.BlockDataSetting;
import radmin.client.settings.IBlockData;
import radmin.client.settings.Setting;
import radmin.client.utils.misc.ICopyable;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.DisplayItemUtils;

public class BlockDataSettingScreen<T extends ICopyable<T> & ISerializable<T> & IBlockData<T>>
extends CollectionMapSettingScreen<Block, T> {
    private final BlockDataSetting<T> setting;
    private boolean invalidate;

    public BlockDataSettingScreen(GuiTheme theme, BlockDataSetting<T> setting) {
        super(theme, "Configure Blocks", (Setting<?>)setting, (Map)setting.get(), BuiltInRegistries.BLOCK);
        this.setting = setting;
    }

    @Override
    protected boolean includeValue(Block value) {
        return value != Blocks.AIR;
    }

    @Override
    protected WWidget getValueWidget(Block block) {
        return this.theme.itemWithLabel(DisplayItemUtils.toStack(block), Names.get(block));
    }

    @Override
    protected WWidget getDataWidget(Block block, @Nullable T blockData) {
        WButton edit = this.theme.button(GuiRenderer.EDIT);
        edit.action = () -> {
            ICopyable data = blockData;
            if (data == null) {
                data = ((ICopyable)this.setting.defaultData.get()).copy();
            }
            MeteorClient.mc.setScreen((Screen)((IBlockData)((Object)data)).createScreen(this.theme, block, this.setting));
            this.invalidate = true;
        };
        return edit;
    }

    @Override
    protected void onRenderBefore(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (this.invalidate) {
            this.invalidateTable();
            this.invalidate = false;
        }
    }

    @Override
    protected String[] getValueNames(Block block) {
        return new String[]{Names.get(block), BuiltInRegistries.BLOCK.getKey((Object)block).toString()};
    }
}

