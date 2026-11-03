/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.GameType
 */
package radmin.client.utils.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import radmin.client.MeteorClient;
import radmin.client.systems.friends.Friends;
import radmin.client.utils.Utils;
import radmin.client.utils.entity.EntityUtils;
import radmin.client.utils.entity.SortPriority;
import radmin.client.utils.entity.fakeplayer.FakePlayerEntity;
import radmin.client.utils.entity.fakeplayer.FakePlayerManager;
import radmin.client.utils.player.PlayerUtils;

public class TargetUtils {
    private static final List<Entity> ENTITIES = new ArrayList<Entity>();

    private TargetUtils() {
    }

    @Nullable
    public static Entity get(Predicate<Entity> isGood, SortPriority sortPriority) {
        ENTITIES.clear();
        TargetUtils.getList(ENTITIES, isGood, sortPriority, 1);
        if (!ENTITIES.isEmpty()) {
            return (Entity)ENTITIES.getFirst();
        }
        return null;
    }

    public static void getList(List<Entity> targetList, Predicate<Entity> isGood, SortPriority sortPriority, int maxCount) {
        targetList.clear();
        for (Entity entity : MeteorClient.mc.level.entitiesForRendering()) {
            if (entity == null || !isGood.test(entity)) continue;
            targetList.add(entity);
        }
        FakePlayerManager.forEach(fp -> {
            if (fp != null && isGood.test((Entity)fp)) {
                targetList.add((Entity)fp);
            }
        });
        targetList.sort(sortPriority);
        if (targetList.size() > maxCount) {
            targetList.subList(maxCount, targetList.size()).clear();
        }
    }

    @Nullable
    public static Player getPlayerTarget(double range, SortPriority priority) {
        if (!Utils.canUpdate()) {
            return null;
        }
        return (Player)TargetUtils.get(entity -> {
            Player player;
            block8: {
                block7: {
                    if (!(entity instanceof Player)) break block7;
                    player = (Player)entity;
                    if (entity != MeteorClient.mc.player) break block8;
                }
                return false;
            }
            if (player.isDeadOrDying() || player.getHealth() <= 0.0f) {
                return false;
            }
            if (!PlayerUtils.isWithin(entity, range)) {
                return false;
            }
            if (!Friends.get().shouldAttack(player)) {
                return false;
            }
            if (entity instanceof FakePlayerEntity) {
                FakePlayerEntity fakePlayer = (FakePlayerEntity)((Object)entity);
                return !fakePlayer.noHit;
            }
            return EntityUtils.getGameMode(player) == GameType.SURVIVAL;
        }, priority);
    }

    public static boolean isBadTarget(Player target, double range) {
        if (target == null) {
            return true;
        }
        return !PlayerUtils.isWithin((Entity)target, range) || !target.isAlive() || target.isDeadOrDying() || target.getHealth() <= 0.0f;
    }
}

