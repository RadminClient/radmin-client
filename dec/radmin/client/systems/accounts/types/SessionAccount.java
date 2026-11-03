/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.util.UndashedUuid
 *  net.minecraft.client.User
 *  net.minecraft.nbt.CompoundTag
 */
package radmin.client.systems.accounts.types;

import com.mojang.util.UndashedUuid;
import java.lang.reflect.Type;
import java.util.Optional;
import net.minecraft.client.User;
import net.minecraft.nbt.CompoundTag;
import radmin.client.MeteorClient;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.TokenAccount;
import radmin.client.utils.network.Http;

public class SessionAccount
extends Account<SessionAccount>
implements TokenAccount {
    private String accessToken;

    public SessionAccount(String label) {
        super(AccountType.Session, label);
        this.accessToken = label;
    }

    @Override
    public SessionAccount fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.accessToken = tag.getStringOr("token", "");
        return this;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = super.toTag();
        tag.putString("token", this.accessToken);
        return tag;
    }

    @Override
    public boolean fetchInfo() {
        ProfileResponse profile;
        if (this.accessToken == null || this.accessToken.isBlank()) {
            return false;
        }
        try {
            profile = (ProfileResponse)Http.get("https://api.minecraftservices.com/minecraft/profile").bearer(this.accessToken).sendJson((Type)((Object)ProfileResponse.class));
        }
        catch (IllegalArgumentException e) {
            MeteorClient.LOG.error("Invalid session account token", (Throwable)e);
            return false;
        }
        if (profile == null || profile.id == null || profile.name == null) {
            return false;
        }
        this.cache.username = profile.name;
        this.cache.uuid = profile.id;
        return true;
    }

    @Override
    public boolean login() {
        if (this.accessToken == null || this.accessToken.isBlank()) {
            return false;
        }
        super.login();
        SessionAccount.setSession(new User(this.cache.username, UndashedUuid.fromStringLenient((String)this.cache.uuid), this.accessToken, Optional.empty(), Optional.empty()));
        return true;
    }

    @Override
    public String getToken() {
        return this.accessToken;
    }

    public boolean equals(Object o) {
        if (!(o instanceof SessionAccount)) {
            return false;
        }
        SessionAccount account2 = (SessionAccount)o;
        return account2.name.equals(this.name);
    }

    private static class ProfileResponse {
        public String id;
        public String name;

        private ProfileResponse() {
        }
    }
}

