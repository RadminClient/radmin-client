/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.gui.screens.DeathScreen
 */
package radmin.client.systems.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.DeathScreen;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.WaypointsModule;

public class AutoRespawn
extends Module {
    public AutoRespawn() {
        super(Categories.Player, "auto-respawn", "Automatically respawns after death.");
    }

    @EventHandler(priority=100)
    private void onOpenScreenEvent(OpenScreenEvent event) {
        if (!(event.screen instanceof DeathScreen)) {
            return;
        }
        Modules.get().get(WaypointsModule.class).addDeath(this.mc.player.position());
        this.mc.player.respawn();
        event.cancel();
    }
}

