/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.notebot.decoder;

import java.io.File;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.Notebot;
import radmin.client.utils.notebot.song.Song;

public abstract class SongDecoder {
    protected Notebot notebot = Modules.get().get(Notebot.class);

    public abstract Song parse(File var1) throws Exception;
}

