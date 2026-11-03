/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.enchantment.Enchantment
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.DynamicRegistryListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Setting;
import radmin.client.utils.misc.Names;

public class EnchantmentListSettingScreen
extends DynamicRegistryListSettingScreen<Enchantment> {
    public EnchantmentListSettingScreen(GuiTheme theme, Setting<Set<ResourceKey<Enchantment>>> setting) {
        super(theme, "Select Enchantments", setting, (Collection)setting.get(), Registries.ENCHANTMENT);
    }

    @Override
    protected WWidget getValueWidget(ResourceKey<Enchantment> value) {
        return this.theme.label(Names.get(value));
    }

    @Override
    protected String[] getValueNames(ResourceKey<Enchantment> value) {
        return new String[]{Names.get(value), value.identifier().toString()};
    }
}

