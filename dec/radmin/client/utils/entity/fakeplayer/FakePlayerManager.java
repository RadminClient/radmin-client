/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package radmin.client.utils.entity.fakeplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.player.Player;
import radmin.client.MeteorClient;
import radmin.client.utils.Utils;
import radmin.client.utils.entity.fakeplayer.FakePlayerEntity;

public class FakePlayerManager {
    private static final List<FakePlayerEntity> ENTITIES = new ArrayList<FakePlayerEntity>();

    private FakePlayerManager() {
    }

    public static List<FakePlayerEntity> getFakePlayers() {
        return ENTITIES;
    }

    public static FakePlayerEntity get(String name) {
        for (FakePlayerEntity fp : ENTITIES) {
            if (!fp.getName().getString().equals(name)) continue;
            return fp;
        }
        return null;
    }

    public static void add(String name, float health, boolean copyInv) {
        if (!Utils.canUpdate()) {
            return;
        }
        FakePlayerEntity fakePlayer = new FakePlayerEntity((Player)MeteorClient.mc.player, name, health, copyInv);
        fakePlayer.spawn();
        ENTITIES.add(fakePlayer);
    }

    public static void remove(FakePlayerEntity fp) {
        ENTITIES.removeIf(fp1 -> {
            if (fp1.getName().getString().equals(fp.getName().getString())) {
                fp1.despawn();
                return true;
            }
            return false;
        });
    }

    public static void clear() {
        if (ENTITIES.isEmpty()) {
            return;
        }
        ENTITIES.forEach(FakePlayerEntity::despawn);
        ENTITIES.clear();
    }

    public static void forEach(Consumer<FakePlayerEntity> action) {
        for (FakePlayerEntity fakePlayer : ENTITIES) {
            action.accept(fakePlayer);
        }
    }

    public static int count() {
        return ENTITIES.size();
    }

    public static Stream<FakePlayerEntity> stream() {
        return ENTITIES.stream();
    }

    public static boolean contains(FakePlayerEntity fp) {
        return ENTITIES.contains((Object)fp);
    }
}

