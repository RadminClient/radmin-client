/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.SoundInstance
 */
package radmin.client.events.world;

import net.minecraft.client.resources.sounds.SoundInstance;
import radmin.client.events.Cancellable;

public class PlaySoundEvent
extends Cancellable {
    private static final PlaySoundEvent INSTANCE = new PlaySoundEvent();
    public SoundInstance sound;

    public static PlaySoundEvent get(SoundInstance sound) {
        INSTANCE.setCancelled(false);
        PlaySoundEvent.INSTANCE.sound = sound;
        return INSTANCE;
    }
}

