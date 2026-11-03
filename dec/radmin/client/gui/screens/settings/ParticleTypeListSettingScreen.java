/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.registries.BuiltInRegistries
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.List;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Setting;
import radmin.client.utils.misc.Names;

public class ParticleTypeListSettingScreen
extends CollectionListSettingScreen<ParticleType<?>> {
    public ParticleTypeListSettingScreen(GuiTheme theme, Setting<List<ParticleType<?>>> setting) {
        super(theme, "Select Particles", setting, (Collection)setting.get(), BuiltInRegistries.PARTICLE_TYPE);
    }

    @Override
    protected WWidget getValueWidget(ParticleType<?> value) {
        return this.theme.label(Names.get(value));
    }

    @Override
    protected String[] getValueNames(ParticleType<?> value) {
        return new String[]{Names.get(value), BuiltInRegistries.PARTICLE_TYPE.getKey(value).toString()};
    }
}

