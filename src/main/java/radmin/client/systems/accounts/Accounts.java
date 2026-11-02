/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.systems.accounts;

import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.accounts.types.CrackedAccount;
import radmin.client.systems.accounts.types.MicrosoftAccount;
import radmin.client.systems.accounts.types.SessionAccount;
import radmin.client.systems.accounts.types.TheAlteningAccount;
import radmin.client.utils.misc.NbtException;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Accounts extends System<Accounts> implements Iterable<Account<?>> {
    private List<Account<?>> accounts = new ArrayList<>();

    public Accounts() {
        super("accounts");
    }

    public static Accounts get() {
        return Systems.get(Accounts.class);
    }

    public void add(Account<?> account) {
        accounts.add(account);
        save();
    }

    public boolean exists(Account<?> account) {
        return accounts.contains(account);
    }

    public void remove(Account<?> account) {
        if (accounts.remove(account)) {
            save();
        }
    }

    public int size() {
        return accounts.size();
    }

    @Override
    public @NonNull Iterator<Account<?>> iterator() {
        return accounts.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.put("accounts", NbtUtils.listToTag(accounts));

        return tag;
    }

    @Override
    public Accounts fromTag(CompoundTag tag) {
        MeteorExecutor.execute(() -> accounts = NbtUtils.listFromTag(tag.getListOrEmpty("accounts"), tag1 -> {
            CompoundTag t = (CompoundTag) tag1;
            if (!t.contains("type")) return null;

            AccountType type = AccountType.valueOf(t.getStringOr("type", ""));

            try {
                return switch (type) {
                    case Cracked -> new CrackedAccount(null).fromTag(t);
                    case Microsoft -> new MicrosoftAccount(null).fromTag(t);
                    case TheAltening -> new TheAlteningAccount(null).fromTag(t);
                    case Session -> new SessionAccount(null).fromTag(t);
                };
            } catch (NbtException _) {
                return null;
            }
        }));

        return this;
    }
}
