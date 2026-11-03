/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.screens.accounts;

import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.TokenAccount;
import radmin.client.utils.render.color.Color;

public class AccountInfoScreen
extends WindowScreen {
    private final Account<?> account;

    public AccountInfoScreen(GuiTheme theme, Account<?> account) {
        super(theme, account.getUsername() + " details");
        this.account = account;
    }

    @Override
    public void initWidgets() {
        TokenAccount e = (TokenAccount)((Object)this.account);
        WHorizontalList l = this.add(this.theme.horizontalList()).expandX().widget();
        Object tokenLabel = String.valueOf((Object)this.account.getType()) + " token:";
        if (this.account.getType() == AccountType.Session) {
            tokenLabel = "";
        }
        WButton copy = this.theme.button("Copy");
        copy.action = () -> MeteorClient.mc.keyboardHandler.setClipboard(e.getToken());
        l.add(this.theme.label((String)tokenLabel));
        l.add(this.theme.label(this.account.getType() == AccountType.Session ? "Click to copy Token" : e.getToken()).color(Color.GRAY)).pad(5.0);
        l.add(copy);
    }
}

