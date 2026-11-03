/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.gui.screens.settings;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.settings.PotionSetting;
import radmin.client.utils.misc.MyPotion;

public class PotionSettingScreen
extends WindowScreen {
    private final PotionSetting setting;

    public PotionSettingScreen(GuiTheme theme, PotionSetting setting) {
        super(theme, "Select Potion");
        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        WTable table = this.add(this.theme.table()).expandX().widget();
        for (MyPotion potion : MyPotion.values()) {
            ItemStack stack = potion.potion.get();
            table.add(this.theme.itemWithLabel(stack, I18n.get((String)stack.getItem().getDescriptionId(), (Object[])new Object[0])));
            WButton select = table.add(this.theme.button("Select")).widget();
            select.action = () -> {
                this.setting.set(potion);
                this.onClose();
            };
            table.row();
        }
    }
}

