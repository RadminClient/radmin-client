/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.WidgetScreen;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WAccount;
import radmin.client.systems.accounts.Account;
import radmin.client.utils.render.color.Color;

public class WMeteorAccount
extends WAccount
implements MeteorWidget {
    public WMeteorAccount(WidgetScreen screen, Account<?> account) {
        super(screen, account);
    }

    @Override
    protected Color loggedInColor() {
        return this.theme().loggedInColor.get();
    }

    @Override
    protected Color accountTypeColor() {
        return this.theme().textSecondaryColor.get();
    }
}

