/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.CameraType
 */
package radmin.client.events.game;

import net.minecraft.client.CameraType;
import radmin.client.events.Cancellable;

public class ChangePerspectiveEvent
extends Cancellable {
    private static final ChangePerspectiveEvent INSTANCE = new ChangePerspectiveEvent();
    public CameraType perspective;

    public static ChangePerspectiveEvent get(CameraType perspective) {
        INSTANCE.setCancelled(false);
        ChangePerspectiveEvent.INSTANCE.perspective = perspective;
        return INSTANCE;
    }
}

