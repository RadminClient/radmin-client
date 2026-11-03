/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import radmin.client.MeteorClient;
import radmin.client.events.game.GameJoinedEvent;
import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.profiles.Profile;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.NbtUtils;

public class Profiles
extends System<Profiles>
implements Iterable<Profile> {
    public static final File FOLDER = new File(MeteorClient.FOLDER, "profiles");
    private List<Profile> profiles = new ArrayList<Profile>();

    public Profiles() {
        super("profiles");
    }

    public static Profiles get() {
        return Systems.get(Profiles.class);
    }

    public void add(Profile profile) {
        if (!this.profiles.contains(profile)) {
            this.profiles.add(profile);
        }
        profile.save();
        this.save();
    }

    public void remove(Profile profile) {
        if (this.profiles.remove(profile)) {
            profile.delete();
        }
        this.save();
    }

    public Profile get(String name) {
        for (Profile profile : this) {
            if (!profile.name.get().equalsIgnoreCase(name)) continue;
            return profile;
        }
        return null;
    }

    public List<Profile> getAll() {
        return this.profiles;
    }

    @Override
    public File getFile() {
        return new File(FOLDER, "profiles.nbt");
    }

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        for (Profile profile : this) {
            if (!profile.loadOnJoin.get().contains(Utils.getWorldName())) continue;
            profile.load();
        }
    }

    public boolean isEmpty() {
        return this.profiles.isEmpty();
    }

    @Override
    @NotNull
    public Iterator<Profile> iterator() {
        return this.profiles.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("profiles", (Tag)NbtUtils.listToTag(this.profiles));
        return tag;
    }

    @Override
    public Profiles fromTag(CompoundTag tag) {
        this.profiles = NbtUtils.listFromTag(tag.getListOrEmpty("profiles"), Profile::new);
        for (File file : FOLDER.listFiles()) {
            if (!file.isDirectory() || this.get(file.getName()) != null) continue;
            Profile p = new Profile();
            p.name.set(file.getName());
            boolean add = false;
            for (File f : file.listFiles()) {
                if (f.getName().equals("hud.nbt")) {
                    add = true;
                    p.hud.set(true);
                    continue;
                }
                if (f.getName().equals("macros.nbt")) {
                    add = true;
                    p.macros.set(true);
                    continue;
                }
                if (f.getName().equals("modules.nbt")) {
                    add = true;
                    p.modules.set(true);
                    continue;
                }
                if (!f.getName().endsWith(".nbt")) continue;
                add = true;
                p.waypoints.set(true);
            }
            if (!add) continue;
            this.add(p);
        }
        return this;
    }
}

