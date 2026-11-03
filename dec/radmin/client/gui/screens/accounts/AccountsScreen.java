/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.gui.screens.accounts;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.screens.accounts.AddAccountScreen;
import radmin.client.gui.screens.accounts.AddAlteningAccountScreen;
import radmin.client.gui.screens.accounts.AddCrackedAccountScreen;
import radmin.client.gui.screens.accounts.AddMicrosoftAccountScreen;
import radmin.client.gui.screens.accounts.AddSessionAccountScreen;
import radmin.client.gui.widgets.WAccount;
import radmin.client.gui.widgets.containers.WContainer;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.Accounts;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;

public class AccountsScreen
extends WindowScreen {
    public AccountsScreen(GuiTheme theme) {
        super(theme, "Accounts");
    }

    @Override
    public void initWidgets() {
        for (Account<?> account : Accounts.get()) {
            WAccount wAccount = this.add(this.theme.account(this, account)).expandX().widget();
            wAccount.refreshScreenAction = this::reload;
        }
        WHorizontalList l = this.add(this.theme.horizontalList()).expandX().widget();
        this.addButton(l, "Cracked", () -> MeteorClient.mc.setScreen((Screen)new AddCrackedAccountScreen(this.theme, this)));
        this.addButton(l, "Altening", () -> MeteorClient.mc.setScreen((Screen)new AddAlteningAccountScreen(this.theme, this)));
        this.addButton(l, "Session", () -> MeteorClient.mc.setScreen((Screen)new AddSessionAccountScreen(this.theme, this)));
        this.addButton(l, "Microsoft", () -> MeteorClient.mc.setScreen((Screen)new AddMicrosoftAccountScreen(this.theme, this)));
    }

    private void addButton(WContainer c, String text, Runnable action) {
        WButton button = c.add(this.theme.button(text)).expandX().widget();
        button.action = action;
    }

    public static void addAccount(@Nullable AddAccountScreen screen, AccountsScreen parent, Account<?> account) {
        if (screen != null) {
            screen.locked = true;
        }
        MeteorExecutor.execute(() -> {
            if (!account.fetchInfo()) {
                MeteorClient.mc.execute(() -> {
                    if (screen != null) {
                        screen.locked = false;
                    }
                });
                return;
            }
            Accounts.get().add(account);
            if (account.login()) {
                if (account.getType() != AccountType.Cracked) {
                    account.getCache().loadHead(parent::reload);
                }
                Accounts.get().save();
            }
            MeteorClient.mc.execute(() -> {
                if (screen != null) {
                    screen.locked = false;
                    screen.onClose();
                }
                parent.reload();
            });
        });
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Accounts.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Accounts.get());
    }
}

