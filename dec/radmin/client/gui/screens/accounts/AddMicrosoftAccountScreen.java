/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.screens.accounts;

import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.accounts.AccountsScreen;
import radmin.client.gui.screens.accounts.AddAccountScreen;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.systems.accounts.MicrosoftLogin;
import radmin.client.systems.accounts.types.MicrosoftAccount;

public class AddMicrosoftAccountScreen
extends AddAccountScreen {
    public AddMicrosoftAccountScreen(GuiTheme theme, AccountsScreen parent) {
        super(theme, "Add Microsoft Account", parent);
    }

    @Override
    public void initWidgets() {
        String url = MicrosoftLogin.getRefreshToken(refreshToken -> {
            if (refreshToken != null) {
                MicrosoftAccount account = new MicrosoftAccount((String)refreshToken);
                AccountsScreen.addAccount(null, this.parent, account);
            }
            this.onClose();
        });
        this.add(this.theme.label("Please select the account to log into in your browser."));
        this.add(this.theme.label("If the link does not automatically open in a few seconds, copy it into your browser."));
        WHorizontalList l = this.add(this.theme.horizontalList()).expandX().widget();
        WButton copy = l.add(this.theme.button("Copy link")).expandX().widget();
        copy.action = () -> MeteorClient.mc.keyboardHandler.setClipboard(url);
        WButton cancel = l.add(this.theme.button("Cancel")).expandX().widget();
        cancel.action = () -> {
            MicrosoftLogin.stopServer();
            this.onClose();
        };
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

