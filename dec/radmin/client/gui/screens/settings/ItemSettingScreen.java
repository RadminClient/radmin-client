/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  org.apache.commons.lang3.Strings
 */
package radmin.client.gui.screens.settings;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.Strings;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.WItemWithLabel;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.settings.ItemSetting;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.DisplayItemUtils;

public class ItemSettingScreen
extends WindowScreen {
    private final ItemSetting setting;
    private WTable table;
    private WTextBox filter;
    private String filterText = "";

    public ItemSettingScreen(GuiTheme theme, ItemSetting setting) {
        super(theme, "Select item");
        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        this.filter = this.add(this.theme.textBox("")).minWidth(400.0).expandX().widget();
        this.filter.setFocused(true);
        this.filter.action = () -> {
            this.filterText = this.filter.get().trim();
            this.table.clear();
            this.initTable();
        };
        this.table = this.add(this.theme.table()).expandX().widget();
        this.initTable();
    }

    public void initTable() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (this.setting.filter != null && !this.setting.filter.test(item) || item == Items.AIR) continue;
            WItemWithLabel itemLabel = this.theme.itemWithLabel(DisplayItemUtils.toStack(item), Names.get(item));
            if (!this.filterText.isEmpty() && !Strings.CI.contains((CharSequence)itemLabel.getLabelText(), (CharSequence)this.filterText)) continue;
            this.table.add(itemLabel);
            WButton select = this.table.add(this.theme.button("Select")).expandCellX().right().widget();
            select.action = () -> {
                this.setting.set(item);
                this.onClose();
            };
            this.table.row();
        }
    }
}

