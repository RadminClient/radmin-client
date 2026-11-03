/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.PacketType
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import org.jetbrains.annotations.NotNull;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.PacketListSetting;
import radmin.client.settings.Setting;
import radmin.client.utils.network.PacketUtils;

public class PacketBoolSettingScreen
extends CollectionListSettingScreen<PacketType<? extends Packet<?>>> {
    public PacketBoolSettingScreen(GuiTheme theme, Setting<Set<PacketType<? extends @NotNull Packet<?>>>> setting) {
        super(theme, "Select Packets", setting, (Collection)setting.get(), PacketUtils.getPackets());
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    @Override
    protected boolean includeValue(PacketType<? extends @NotNull Packet<?>> value) {
        Predicate<PacketType<@NotNull Packet<?>>> filter = ((PacketListSetting)this.setting).filter;
        if (filter == null) {
            return true;
        }
        return filter.test(value);
    }

    @Override
    protected WWidget getValueWidget(PacketType<? extends @NotNull Packet<?>> value) {
        return this.theme.label(value.toString());
    }

    @Override
    protected String[] getValueNames(PacketType<? extends @NotNull Packet<?>> value) {
        return new String[]{value.toString()};
    }
}

