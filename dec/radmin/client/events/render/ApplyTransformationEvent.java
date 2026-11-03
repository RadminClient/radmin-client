/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.model.cuboid.ItemTransform
 */
package radmin.client.events.render;

import net.minecraft.client.resources.model.cuboid.ItemTransform;
import radmin.client.events.Cancellable;

public class ApplyTransformationEvent
extends Cancellable {
    private static final ApplyTransformationEvent INSTANCE = new ApplyTransformationEvent();
    public ItemTransform transformation;
    public boolean leftHanded;

    public static ApplyTransformationEvent get(ItemTransform transformation, boolean leftHanded) {
        INSTANCE.setCancelled(false);
        ApplyTransformationEvent.INSTANCE.transformation = transformation;
        ApplyTransformationEvent.INSTANCE.leftHanded = leftHanded;
        return INSTANCE;
    }
}

