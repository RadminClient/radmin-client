/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.User
 *  net.minecraft.core.UUIDUtil
 */
package radmin.client.systems.accounts.types;

import java.util.Optional;
import net.minecraft.client.User;
import net.minecraft.core.UUIDUtil;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;

public class CrackedAccount
extends Account<CrackedAccount> {
    public CrackedAccount(String name) {
        super(AccountType.Cracked, name);
    }

    @Override
    public boolean fetchInfo() {
        this.cache.username = this.name;
        return true;
    }

    @Override
    public boolean login() {
        super.login();
        CrackedAccount.setSession(new User(this.name, UUIDUtil.createOfflinePlayerUUID((String)this.name), "", Optional.empty(), Optional.empty()));
        return true;
    }

    public boolean equals(Object o) {
        if (!(o instanceof CrackedAccount)) {
            return false;
        }
        CrackedAccount account = (CrackedAccount)o;
        return account.getUsername().equals(this.getUsername());
    }
}

