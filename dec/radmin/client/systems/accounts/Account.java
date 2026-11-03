/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.minecraft.UserApiService
 *  com.mojang.authlib.yggdrasil.ServicesKeySet
 *  com.mojang.authlib.yggdrasil.ServicesKeyType
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  net.minecraft.client.User
 *  net.minecraft.client.gui.screens.social.PlayerSocialManager
 *  net.minecraft.client.multiplayer.ProfileKeyPairManager
 *  net.minecraft.client.multiplayer.chat.report.ReportEnvironment
 *  net.minecraft.client.multiplayer.chat.report.ReportingContext
 *  net.minecraft.client.renderer.texture.SkinTextureDownloader
 *  net.minecraft.client.resources.SkinManager
 *  net.minecraft.client.resources.SkinManager$TextureCache
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.Services
 *  net.minecraft.util.SignatureValidator
 *  net.minecraft.util.Util
 */
package radmin.client.systems.accounts;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.Services;
import net.minecraft.util.SignatureValidator;
import net.minecraft.util.Util;
import radmin.client.MeteorClient;
import radmin.client.mixin.FileCacheAccessor;
import radmin.client.mixin.MinecraftAccessor;
import radmin.client.mixin.SkinManagerAccessor;
import radmin.client.systems.accounts.AccountCache;
import radmin.client.systems.accounts.AccountType;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.misc.NbtException;

public abstract class Account<T extends Account<?>>
implements ISerializable<T> {
    protected AccountType type;
    protected String name;
    protected final AccountCache cache;

    protected Account(AccountType type, String name) {
        this.type = type;
        this.name = name;
        this.cache = new AccountCache();
    }

    public abstract boolean fetchInfo();

    public boolean login() {
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(MeteorClient.mc.getProxy());
        Account.applyLoginEnvironment(authenticationService);
        return true;
    }

    public String getUsername() {
        if (this.cache.username.isEmpty()) {
            return this.name;
        }
        return this.cache.username;
    }

    public AccountType getType() {
        return this.type;
    }

    public AccountCache getCache() {
        return this.cache;
    }

    public static void setSession(User session) {
        MinecraftAccessor mca = (MinecraftAccessor)MeteorClient.mc;
        mca.meteor$setUser(session);
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(MeteorClient.mc.getProxy());
        UserApiService apiService = yggdrasilAuthenticationService.createUserApiService(session.getAccessToken());
        mca.meteor$setUserApiService(apiService);
        mca.meteor$setPlayerSocialManager(new PlayerSocialManager(MeteorClient.mc, apiService));
        mca.meteor$setProfileKeyPairManager(ProfileKeyPairManager.create((UserApiService)apiService, (User)session, (Path)MeteorClient.mc.gameDirectory.toPath()));
        mca.meteor$setReportingContext(ReportingContext.create((ReportEnvironment)ReportEnvironment.local(), (UserApiService)apiService));
        mca.meteor$setProfileFuture(CompletableFuture.supplyAsync(() -> MeteorClient.mc.services().sessionService().fetchProfile(MeteorClient.mc.getUser().getProfileId(), true), (Executor)Util.ioPool()));
    }

    public static void applyLoginEnvironment(YggdrasilAuthenticationService authService) {
        MinecraftAccessor mca = (MinecraftAccessor)MeteorClient.mc;
        SignatureValidator.from((ServicesKeySet)authService.getServicesKeySet(), (ServicesKeyType)ServicesKeyType.PROFILE_KEY);
        SkinManager.TextureCache skinCache = ((SkinManagerAccessor)MeteorClient.mc.getSkinManager()).meteor$getSkinTextures();
        Path skinCachePath = ((FileCacheAccessor)skinCache).meteor$getRoot();
        mca.meteor$setServices(Services.create((YggdrasilAuthenticationService)authService, (File)MeteorClient.mc.gameDirectory));
        mca.meteor$setSkinManager(new SkinManager(skinCachePath, MeteorClient.mc.services(), new SkinTextureDownloader(MeteorClient.mc.getProxy(), MeteorClient.mc.getTextureManager(), (Executor)MeteorClient.mc), (Executor)MeteorClient.mc));
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", this.type.name());
        tag.putString("name", this.name);
        tag.put("cache", (Tag)this.cache.toTag());
        return tag;
    }

    @Override
    public T fromTag(CompoundTag tag) {
        if (tag.getString("name").isEmpty() || tag.getCompound("cache").isEmpty()) {
            throw new NbtException();
        }
        this.name = (String)tag.getString("name").get();
        this.cache.fromTag((CompoundTag)tag.getCompound("cache").get());
        return (T)this;
    }
}

