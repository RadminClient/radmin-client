/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.events.render;

public class GetFovEvent {
    private static final GetFovEvent INSTANCE = new GetFovEvent();
    public float fov;

    public static GetFovEvent get(float fov) {
        GetFovEvent.INSTANCE.fov = fov;
        return INSTANCE;
    }
}

