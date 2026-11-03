/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Environment
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.util.UndashedUuid
 *  net.minecraft.client.User
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 */
package radmin.client.systems.accounts.types;

import com.mojang.authlib.Environment;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UndashedUuid;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.User;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import radmin.client.MeteorClient;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.TokenAccount;
import radmin.client.utils.misc.NbtException;
import radmin.client.utils.network.Http;

public class TheAlteningAccount
extends Account<TheAlteningAccount>
implements TokenAccount {
    private static final Environment ENVIRONMENT = new Environment("http://sessionserver.thealtening.com", "http://authserver.thealtening.com", "https://api.mojang.com", "The Altening");
    private static final YggdrasilAuthenticationService SERVICE = new YggdrasilAuthenticationService(MeteorClient.mc.getProxy(), ENVIRONMENT);
    private String token;
    private String accessToken;

    public TheAlteningAccount(String token) {
        super(AccountType.TheAltening, token);
        this.token = token;
    }

    @Override
    public boolean fetchInfo() {
        try {
            AuthResponse res = this.authenticate();
            if (res == null || res.accessToken == null || res.selectedProfile == null) {
                MeteorClient.LOG.error("Invalid TheAltening credentials.");
                return false;
            }
            this.accessToken = res.accessToken;
            this.cache.username = res.selectedProfile.name;
            this.cache.uuid = res.selectedProfile.id;
            this.cache.loadHead();
            return true;
        }
        catch (Exception exception) {
            MeteorClient.LOG.error("Failed to fetch info for TheAltening account!");
            return false;
        }
    }

    @Override
    public boolean login() {
        if (this.accessToken == null || this.cache.username.isEmpty() || this.cache.uuid.isEmpty()) {
            return false;
        }
        TheAlteningAccount.applyLoginEnvironment(SERVICE);
        try {
            TheAlteningAccount.setSession(new User(this.cache.username, UndashedUuid.fromStringLenient((String)this.cache.uuid), this.accessToken, Optional.empty(), Optional.empty()));
            return true;
        }
        catch (Exception exception) {
            MeteorClient.LOG.error("Failed to login with TheAltening.");
            return false;
        }
    }

    private AuthResponse authenticate() {
        return (AuthResponse)Http.post(ENVIRONMENT.servicesHost() + "/authenticate").bodyJson(new AuthRequest("MINECRAFT", this.token, "Meteor on Crack!", UUID.randomUUID().toString(), true)).sendJson((Type)((Object)AuthResponse.class));
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", this.type.name());
        tag.putString("name", this.name);
        tag.putString("token", this.token);
        tag.put("cache", (Tag)this.cache.toTag());
        return tag;
    }

    @Override
    public TheAlteningAccount fromTag(CompoundTag tag) {
        if (tag.getString("name").isEmpty() || tag.getCompound("cache").isEmpty() || tag.getString("token").isEmpty()) {
            throw new NbtException();
        }
        this.name = (String)tag.getString("name").get();
        this.token = (String)tag.getString("token").get();
        this.cache.fromTag((CompoundTag)tag.getCompound("cache").get());
        return this;
    }

    private static class AuthResponse {
        public String accessToken;
        public AuthProfile selectedProfile;

        private AuthResponse() {
        }
    }

    private static class AuthProfile {
        public String id;
        public String name;

        private AuthProfile() {
        }
    }

    private record AuthRequest(String agent, String username, String password, String clientToken, boolean requestUser) {
    }
}

