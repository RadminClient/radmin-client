/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.world.InteractionHand
 */
package radmin.client.events.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;

public class HeldItemRendererEvent {
    private static final HeldItemRendererEvent INSTANCE = new HeldItemRendererEvent();
    public InteractionHand hand;
    public PoseStack matrix;

    public static HeldItemRendererEvent get(InteractionHand hand, PoseStack matrices) {
        HeldItemRendererEvent.INSTANCE.hand = hand;
        HeldItemRendererEvent.INSTANCE.matrix = matrices;
        return INSTANCE;
    }
}

