/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.util.UndashedUuid
 *  net.minecraft.client.User
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.systems.accounts.types;

import com.mojang.util.UndashedUuid;
import java.util.Optional;
import net.minecraft.client.User;
import org.jetbrains.annotations.Nullable;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.MicrosoftLogin;

public class MicrosoftAccount
extends Account<MicrosoftAccount> {
    @Nullable
    private String token;

    public MicrosoftAccount(String refreshToken) {
        super(AccountType.Microsoft, refreshToken);
    }

    @Override
    public boolean fetchInfo() {
        this.token = this.auth();
        return this.token != null;
    }

    @Override
    public boolean login() {
        if (this.token == null) {
            return false;
        }
        super.login();
        MicrosoftAccount.setSession(new User(this.cache.username, UndashedUuid.fromStringLenient((String)this.cache.uuid), this.token, Optional.empty(), Optional.empty()));
        return true;
    }

    @Nullable
    private String auth() {
        MicrosoftLogin.LoginData data = MicrosoftLogin.login(this.name);
        if (!data.isGood()) {
            return null;
        }
        this.name = data.newRefreshToken;
        this.cache.username = data.username;
        this.cache.uuid = data.uuid;
        return data.mcToken;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MicrosoftAccount)) {
            return false;
        }
        MicrosoftAccount account = (MicrosoftAccount)o;
        return account.name.equals(this.name);
    }
}

