/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.modules.player;

import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class NoRotate
extends Module {
    public NoRotate() {
        super(Categories.Player, "no-rotate", "Attempts to block rotations sent from server to client.");
    }
}

