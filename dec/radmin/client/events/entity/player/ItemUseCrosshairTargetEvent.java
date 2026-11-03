/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.HitResult
 */
package radmin.client.events.entity.player;

import net.minecraft.world.phys.HitResult;

public class ItemUseCrosshairTargetEvent {
    private static final ItemUseCrosshairTargetEvent INSTANCE = new ItemUseCrosshairTargetEvent();
    public HitResult target;

    public static ItemUseCrosshairTargetEvent get(HitResult target) {
        ItemUseCrosshairTargetEvent.INSTANCE.target = target;
        return INSTANCE;
    }
}

