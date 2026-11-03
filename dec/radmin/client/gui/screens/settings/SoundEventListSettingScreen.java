/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.sounds.SoundEvent
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Setting;

public class SoundEventListSettingScreen
extends CollectionListSettingScreen<SoundEvent> {
    public SoundEventListSettingScreen(GuiTheme theme, Setting<List<SoundEvent>> setting) {
        super(theme, "Select Sounds", setting, (Collection)setting.get(), BuiltInRegistries.SOUND_EVENT);
    }

    @Override
    protected WWidget getValueWidget(SoundEvent value) {
        return this.theme.label(value.location().getPath());
    }

    @Override
    protected String[] getValueNames(SoundEvent value) {
        return new String[]{value.location().toString(), I18n.get((String)("subtitles." + value.location().getPath()), (Object[])new Object[0])};
    }
}

