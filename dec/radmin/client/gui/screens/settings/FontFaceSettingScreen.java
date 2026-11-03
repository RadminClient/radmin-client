/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Strings
 */
package radmin.client.gui.screens.settings;

import java.util.List;
import org.apache.commons.lang3.Strings;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.utils.Cell;
import radmin.client.gui.widgets.WLabel;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.containers.WView;
import radmin.client.gui.widgets.input.WDropdown;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.renderer.Fonts;
import radmin.client.renderer.text.FontFamily;
import radmin.client.renderer.text.FontInfo;
import radmin.client.settings.FontFaceSetting;

public class FontFaceSettingScreen
extends WindowScreen {
    private final FontFaceSetting setting;
    private WTable table;
    private WTextBox filter;
    private String filterText = "";

    public FontFaceSettingScreen(GuiTheme theme, FontFaceSetting setting) {
        super(theme, "Select Font");
        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        this.filter = this.add(this.theme.textBox("")).expandX().widget();
        this.filter.setFocused(true);
        this.filter.action = () -> {
            this.filterText = this.filter.get().trim();
            this.table.clear();
            this.initTable();
        };
        this.window.view.hasScrollBar = false;
        this.enterAction = () -> {
            List<Cell<?>> row = this.table.getRow(0);
            if (row == null) {
                return;
            }
            Object widget = row.get(2).widget();
            if (widget instanceof WButton) {
                WButton button = (WButton)widget;
                button.action.run();
            }
        };
        WView view = this.add(this.theme.view()).expandX().widget();
        view.maxHeight = this.window.view.maxHeight - 128.0;
        view.scrollOnlyWhenMouseOver = false;
        this.table = view.add(this.theme.table()).expandX().widget();
        this.initTable();
    }

    private void initTable() {
        for (FontFamily fontFamily : Fonts.FONT_FAMILIES) {
            String name = fontFamily.getName();
            WLabel item = this.theme.label(name);
            if (!this.filterText.isEmpty() && !Strings.CI.contains((CharSequence)name, (CharSequence)this.filterText)) continue;
            this.table.add(item);
            WDropdown<FontInfo.Type> dropdown = this.table.add(this.theme.dropdown(FontInfo.Type.Regular)).right().widget();
            WButton select = this.table.add(this.theme.button("Select")).expandCellX().right().widget();
            select.action = () -> {
                this.setting.set(fontFamily.get((FontInfo.Type)((Object)((Object)dropdown.get()))));
                this.onClose();
            };
            this.table.row();
        }
    }
}

