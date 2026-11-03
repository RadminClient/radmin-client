/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2IntMap
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.PotionContents
 *  org.apache.commons.lang3.Strings
 */
package radmin.client.gui.screens.settings;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.apache.commons.lang3.Strings;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WIntEdit;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.settings.Setting;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.DisplayItemUtils;

public class StatusEffectAmplifierMapSettingScreen
extends WindowScreen {
    private final Setting<Reference2IntMap<MobEffect>> setting;
    private WTable table;
    private String filterText = "";

    public StatusEffectAmplifierMapSettingScreen(GuiTheme theme, Setting<Reference2IntMap<MobEffect>> setting) {
        super(theme, "Modify Amplifiers");
        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        WTextBox filter = this.add(this.theme.textBox("")).minWidth(400.0).expandX().widget();
        filter.setFocused(true);
        filter.action = () -> {
            this.filterText = filter.get().trim();
            this.table.clear();
            this.initTable();
        };
        this.table = this.add(this.theme.table()).expandX().widget();
        this.initTable();
    }

    private void initTable() {
        ArrayList<MobEffect> statusEffects = new ArrayList<MobEffect>((Collection<MobEffect>)this.setting.get().keySet());
        statusEffects.sort(Comparator.comparing(Names::get));
        for (MobEffect statusEffect : statusEffects) {
            String name = Names.get(statusEffect);
            if (!Strings.CI.contains((CharSequence)name, (CharSequence)this.filterText)) continue;
            this.table.add(this.theme.itemWithLabel(this.getPotionStack(statusEffect), name)).expandCellX();
            WIntEdit level = this.theme.intEdit(this.setting.get().getInt((Object)statusEffect), 0, Integer.MAX_VALUE, true);
            level.action = () -> {
                this.setting.get().put((Object)statusEffect, level.get());
                this.setting.onChanged();
            };
            this.table.add(level).minWidth(50.0);
            this.table.row();
        }
    }

    private ItemStack getPotionStack(MobEffect effect) {
        ItemStack potion = DisplayItemUtils.toStack(Items.POTION);
        potion.set(DataComponents.POTION_CONTENTS, (Object)new PotionContents(Optional.empty(), Optional.of(effect.getColor()), List.of(), Optional.empty()));
        return potion;
    }
}

