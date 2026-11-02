/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.gui.screens.settings;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.settings.PotionSetting;
import radmin.client.utils.misc.MyPotion;
import net.minecraft.client.resources.language.I18n;

public class PotionSettingScreen extends WindowScreen {
    private final PotionSetting setting;

    public PotionSettingScreen(GuiTheme theme, PotionSetting setting) {
        super(theme, "Select Potion");

        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        WTable table = add(theme.table()).expandX().widget();

        for (MyPotion potion : MyPotion.values()) {
            var stack = potion.potion.get();
            table.add(theme.itemWithLabel(stack, I18n.get(stack.getItem().getDescriptionId())));

            WButton select = table.add(theme.button("Select")).widget();
            select.action = () -> {
                setting.set(potion);
                onClose();
            };

            table.row();
        }
    }
}
