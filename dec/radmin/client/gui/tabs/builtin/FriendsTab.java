/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.gui.tabs.builtin;

import net.minecraft.client.gui.screens.Screen;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.tabs.WindowTabScreen;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WMinus;
import radmin.client.gui.widgets.pressable.WPlus;
import radmin.client.systems.friends.Friend;
import radmin.client.systems.friends.Friends;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;

public class FriendsTab
extends Tab {
    public FriendsTab() {
        super("Friends");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new FriendsScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof FriendsScreen;
    }

    private static class FriendsScreen
    extends WindowTabScreen {
        public FriendsScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
        }

        @Override
        public void initWidgets() {
            WTable table = this.add(this.theme.table()).expandX().minWidth(400.0).widget();
            this.initTable(table);
            this.add(this.theme.horizontalSeparator()).expandX();
            WHorizontalList list = this.add(this.theme.horizontalList()).expandX().widget();
            WTextBox nameW = list.add(this.theme.textBox("", (string, c) -> c != ' ')).expandX().widget();
            nameW.setFocused(true);
            WPlus add = list.add(this.theme.plus()).widget();
            this.enterAction = add.action = () -> {
                String name = nameW.get().trim();
                Friend friend = new Friend(name);
                if (Friends.get().add(friend)) {
                    nameW.set("");
                    this.initTable(table);
                    nameW.setFocused(true);
                    MeteorExecutor.execute(() -> {
                        friend.updateInfo();
                        MeteorClient.mc.execute(() -> {
                            this.initTable(table);
                            nameW.setFocused(true);
                        });
                    });
                }
            };
        }

        private void initTable(WTable table) {
            table.clear();
            if (Friends.get().isEmpty()) {
                return;
            }
            Friends.get().forEach(friend -> MeteorExecutor.execute(() -> {
                if (friend.headTextureNeedsUpdate()) {
                    friend.updateInfo();
                }
            }));
            for (Friend friend2 : Friends.get()) {
                table.add(this.theme.texture(32.0, 32.0, friend2.getHead().needsRotate() ? 90.0 : 0.0, friend2.getHead()));
                table.add(this.theme.label(friend2.getName()));
                WMinus remove = table.add(this.theme.minus()).expandCellX().right().widget();
                remove.action = () -> {
                    Friends.get().remove(friend2);
                    this.initTable(table);
                };
                table.row();
            }
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(Friends.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(Friends.get());
        }
    }
}

