/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.accounts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.accounts.AccountType;
import radmin.client.systems.accounts.types.CrackedAccount;
import radmin.client.systems.accounts.types.MicrosoftAccount;
import radmin.client.systems.accounts.types.SessionAccount;
import radmin.client.systems.accounts.types.TheAlteningAccount;
import radmin.client.utils.misc.NbtException;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;

public class Accounts
extends System<Accounts>
implements Iterable<Account<?>> {
    private List<Account<?>> accounts = new ArrayList();

    public Accounts() {
        super("accounts");
    }

    public static Accounts get() {
        return Systems.get(Accounts.class);
    }

    public void add(Account<?> account) {
        this.accounts.add(account);
        this.save();
    }

    public boolean exists(Account<?> account) {
        return this.accounts.contains(account);
    }

    public void remove(Account<?> account) {
        if (this.accounts.remove(account)) {
            this.save();
        }
    }

    public int size() {
        return this.accounts.size();
    }

    @Override
    @NotNull
    public Iterator<Account<?>> iterator() {
        return this.accounts.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("accounts", (Tag)NbtUtils.listToTag(this.accounts));
        return tag;
    }

    @Override
    public Accounts fromTag(CompoundTag tag) {
        MeteorExecutor.execute(() -> {
            this.accounts = NbtUtils.listFromTag(tag.getListOrEmpty("accounts"), tag1 -> {
                CompoundTag t = (CompoundTag)tag1;
                if (!t.contains("type")) {
                    return null;
                }
                AccountType type = AccountType.valueOf(t.getStringOr("type", ""));
                try {
                    return switch (type) {
                        default -> throw new MatchException(null, null);
                        case AccountType.Cracked -> (CrackedAccount)new CrackedAccount(null).fromTag(t);
                        case AccountType.Microsoft -> (MicrosoftAccount)new MicrosoftAccount(null).fromTag(t);
                        case AccountType.TheAltening -> new TheAlteningAccount(null).fromTag(t);
                        case AccountType.Session -> new SessionAccount(null).fromTag(t);
                    };
                }
                catch (NbtException nbtException) {
                    return null;
                }
            });
        });
        return this;
    }
}

