/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.macros;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.macros.Macro;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.misc.input.KeyAction;

public class Macros
extends System<Macros>
implements Iterable<Macro> {
    private List<Macro> macros = new ArrayList<Macro>();

    public Macros() {
        super("macros");
    }

    public static Macros get() {
        return Systems.get(Macros.class);
    }

    public void add(Macro macro) {
        this.macros.add(macro);
        MeteorClient.EVENT_BUS.subscribe((Object)macro);
        this.save();
    }

    public Macro get(String name) {
        for (Macro macro : this.macros) {
            if (!macro.name.get().equalsIgnoreCase(name)) continue;
            return macro;
        }
        return null;
    }

    public List<Macro> getAll() {
        return this.macros;
    }

    public void remove(Macro macro) {
        if (this.macros.remove(macro)) {
            MeteorClient.EVENT_BUS.unsubscribe((Object)macro);
            this.save();
        }
    }

    @EventHandler(priority=100)
    private void onKey(KeyInputEvent event) {
        if (event.action == KeyAction.Release) {
            return;
        }
        for (Macro macro : this.macros) {
            if (!macro.onAction(true, event.key(), event.modifiers())) continue;
            return;
        }
    }

    @EventHandler(priority=100)
    private void onMouse(MouseClickEvent event) {
        if (event.action == KeyAction.Release) {
            return;
        }
        for (Macro macro : this.macros) {
            if (!macro.onAction(false, event.button(), 0)) continue;
            return;
        }
    }

    public boolean isEmpty() {
        return this.macros.isEmpty();
    }

    @Override
    @NotNull
    public Iterator<Macro> iterator() {
        return this.macros.iterator();
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("macros", (Tag)NbtUtils.listToTag(this.macros));
        return tag;
    }

    @Override
    public Macros fromTag(CompoundTag tag) {
        for (Macro macro : this.macros) {
            MeteorClient.EVENT_BUS.unsubscribe((Object)macro);
        }
        this.macros = NbtUtils.listFromTag(tag.getListOrEmpty("macros"), Macro::new);
        for (Macro macro : this.macros) {
            MeteorClient.EVENT_BUS.subscribe((Object)macro);
        }
        return this;
    }
}

