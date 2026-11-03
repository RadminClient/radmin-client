/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 */
package radmin.client.events.world;

import net.minecraft.core.particles.ParticleOptions;
import radmin.client.events.Cancellable;

public class ParticleEvent
extends Cancellable {
    private static final ParticleEvent INSTANCE = new ParticleEvent();
    public ParticleOptions particle;

    public static ParticleEvent get(ParticleOptions particle) {
        INSTANCE.setCancelled(false);
        ParticleEvent.INSTANCE.particle = particle;
        return INSTANCE;
    }
}

