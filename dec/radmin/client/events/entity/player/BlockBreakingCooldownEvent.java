/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.entity.player;

public class BlockBreakingCooldownEvent {
    private static final BlockBreakingCooldownEvent INSTANCE = new BlockBreakingCooldownEvent();
    public int cooldown;

    public static BlockBreakingCooldownEvent get(int cooldown) {
        BlockBreakingCooldownEvent.INSTANCE.cooldown = cooldown;
        return INSTANCE;
    }
}

