/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.pathing;

import java.lang.reflect.InvocationTargetException;
import radmin.client.MeteorClient;
import radmin.client.pathing.BaritonePathManager;
import radmin.client.pathing.BaritoneUtils;
import radmin.client.pathing.IPathManager;
import radmin.client.pathing.NopPathManager;
import radmin.client.utils.PreInit;

public class PathManagers {
    private static IPathManager INSTANCE = new NopPathManager();

    public static IPathManager get() {
        return INSTANCE;
    }

    @PreInit
    public static void init() {
        if (PathManagers.exists("meteordevelopment.voyager.PathManager")) {
            try {
                INSTANCE = (IPathManager)Class.forName("meteordevelopment.voyager.PathManager").getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (PathManagers.exists("baritone.api.BaritoneAPI")) {
            BaritoneUtils.IS_AVAILABLE = true;
            if (INSTANCE instanceof NopPathManager) {
                INSTANCE = new BaritonePathManager();
            }
        }
        MeteorClient.LOG.info("Path Manager: {}", (Object)INSTANCE.getName());
    }

    private static boolean exists(String name) {
        try {
            Class.forName(name);
            return true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }
}

