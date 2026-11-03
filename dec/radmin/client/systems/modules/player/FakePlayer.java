/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.player;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WMinus;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.StringSetting;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.entity.fakeplayer.FakePlayerEntity;
import radmin.client.utils.entity.fakeplayer.FakePlayerManager;

public class FakePlayer
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<String> name;
    public final Setting<Boolean> copyInv;
    public final Setting<Integer> health;
    private WTable table;

    public FakePlayer() {
        super(Categories.Player, "fake-player", "Spawns a client-side fake player for testing usages. No need to be active.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.name = this.sgGeneral.add(((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)new StringSetting.Builder().name("name")).description("The name of the fake player.")).defaultValue("seasnail8169")).build());
        this.copyInv = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("copy-inv")).description("Copies your inventory to the fake player.")).defaultValue(true)).build());
        this.health = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("health")).description("The fake player's default health.")).defaultValue(20)).min(1).sliderRange(1, 100).build());
    }

    @Override
    public void onDeactivate() {
        FakePlayerManager.clear();
        if (this.table != null) {
            this.table.clear();
        }
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        this.table = theme.table();
        this.fillTable(theme, this.table);
        return this.table;
    }

    private void fillTable(GuiTheme theme, WTable table) {
        for (FakePlayerEntity fakePlayer : FakePlayerManager.getFakePlayers()) {
            table.add(theme.label(fakePlayer.getName().getString()));
            WMinus delete = table.add(theme.minus()).expandCellX().right().widget();
            delete.action = () -> {
                FakePlayerManager.remove(fakePlayer);
                table.clear();
                this.fillTable(theme, table);
            };
            table.row();
        }
        WButton spawn = table.add(theme.button("Spawn")).expandCellX().right().widget();
        spawn.action = () -> {
            FakePlayerManager.add(this.name.get(), this.health.get().intValue(), this.copyInv.get());
            table.clear();
            this.fillTable(theme, table);
        };
        WButton clear = table.add(theme.button("Clear All")).right().widget();
        clear.action = () -> {
            FakePlayerManager.clear();
            table.clear();
            this.fillTable(theme, table);
        };
    }

    @Override
    public String getInfoString() {
        return String.valueOf(FakePlayerManager.count());
    }
}

