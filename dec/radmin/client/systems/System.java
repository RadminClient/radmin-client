/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ReportedException
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtIo
 *  org.apache.commons.io.FilenameUtils
 */
package radmin.client.systems;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import net.minecraft.ReportedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.apache.commons.io.FilenameUtils;
import radmin.client.MeteorClient;
import radmin.client.utils.files.StreamUtils;
import radmin.client.utils.misc.ISerializable;

public abstract class System<T>
implements ISerializable<T> {
    private final String name;
    private File file;
    protected boolean isFirstInit;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);

    public System(String name) {
        this.name = name;
        if (name != null) {
            this.file = new File(MeteorClient.FOLDER, name + ".nbt");
            this.isFirstInit = !this.file.exists();
        }
    }

    public void init() {
    }

    public void save(File folder) {
        File file = this.getFile();
        if (file == null) {
            return;
        }
        CompoundTag tag = this.toTag();
        if (tag == null) {
            return;
        }
        try {
            File tempFile = File.createTempFile("radmin-client", file.getName());
            NbtIo.write((CompoundTag)tag, (Path)tempFile.toPath());
            if (folder != null) {
                file = new File(folder, file.getName());
            }
            file.getParentFile().mkdirs();
            try {
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                StreamUtils.copy(tempFile, file);
            }
            tempFile.delete();
        }
        catch (IOException e) {
            MeteorClient.LOG.error("Error saving {}. Possibly corrupted?", (Object)this.name, (Object)e);
        }
    }

    public void save() {
        this.save(null);
    }

    public void load(File folder) {
        block8: {
            File file = this.getFile();
            if (file == null) {
                return;
            }
            try {
                if (folder != null) {
                    file = new File(folder, file.getName());
                }
                if (!file.exists()) break block8;
                try {
                    this.fromTag(NbtIo.read((Path)file.toPath()));
                }
                catch (ReportedException e) {
                    String backupName = FilenameUtils.removeExtension((String)file.getName()) + "-" + ZonedDateTime.now().format(DATE_TIME_FORMATTER) + ".backup.nbt";
                    File backup = new File(file.getParentFile(), backupName);
                    try {
                        Files.move(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                    }
                    catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                        StreamUtils.copy(file, backup);
                    }
                    MeteorClient.LOG.error("Error loading {}. Possibly corrupted?", (Object)this.name, (Object)e);
                    MeteorClient.LOG.info("Saved settings backup to '{}'.", (Object)backup);
                }
            }
            catch (IOException e) {
                MeteorClient.LOG.error("Error loading {}. Possibly corrupted?", (Object)this.name, (Object)e);
            }
        }
    }

    public void load() {
        this.load(null);
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public CompoundTag toTag() {
        return null;
    }

    @Override
    public T fromTag(CompoundTag tag) {
        return null;
    }
}

