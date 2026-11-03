/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  baritone.api.BaritoneAPI
 */
package radmin.client.pathing;

import baritone.api.BaritoneAPI;

public class BaritoneUtils {
    public static boolean IS_AVAILABLE = false;

    private BaritoneUtils() {
    }

    public static String getPrefix() {
        if (IS_AVAILABLE) {
            return (String)BaritoneAPI.getSettings().prefix.value;
        }
        return "";
    }
}

