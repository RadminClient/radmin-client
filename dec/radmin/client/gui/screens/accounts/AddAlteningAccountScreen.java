/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.screens.accounts;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.accounts.AccountsScreen;
import radmin.client.gui.screens.accounts.AddAccountScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.systems.accounts.types.TheAlteningAccount;

public class AddAlteningAccountScreen
extends AddAccountScreen {
    public AddAlteningAccountScreen(GuiTheme theme, AccountsScreen parent) {
        super(theme, "Add The Altening Account", parent);
    }

    @Override
    public void initWidgets() {
        WTable t = this.add(this.theme.table()).widget();
        t.add(this.theme.label("Token: "));
        WTextBox token = t.add(this.theme.textBox("")).minWidth(400.0).expandX().widget();
        token.setFocused(true);
        t.row();
        this.add = t.add(this.theme.button("Add")).expandX().widget();
        this.enterAction = this.add.action = () -> {
            if (!token.get().isEmpty()) {
                AccountsScreen.addAccount(this, this.parent, new TheAlteningAccount(token.get()));
            }
        };
    }
}

