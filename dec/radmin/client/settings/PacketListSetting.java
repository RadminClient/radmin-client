/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.PacketFlow
 *  net.minecraft.network.protocol.PacketType
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.settings;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import org.jetbrains.annotations.NotNull;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;
import radmin.client.utils.network.PacketUtils;

public class PacketListSetting
extends Setting<Set<PacketType<? extends Packet<?>>>> {
    public final Predicate<PacketType<? extends @NotNull Packet<?>>> filter;

    public PacketListSetting(String name, String description, Set<PacketType<? extends @NotNull Packet<?>>> defaultValue, Consumer<Set<PacketType<? extends @NotNull Packet<?>>>> onChanged, Consumer<Setting<Set<PacketType<? extends @NotNull Packet<?>>>>> onModuleActivated, Predicate<PacketType<? extends @NotNull Packet<?>>> filter, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.filter = filter;
    }

    @Override
    public void resetImpl() {
        this.value = new ObjectOpenHashSet((Collection)this.defaultValue);
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    @Override
    protected Set<PacketType<? extends @NotNull Packet<?>>> parseImpl(String str) {
        String[] values = str.split(",");
        @NotNull ObjectOpenHashSet packets = new ObjectOpenHashSet(values.length);
        try {
            for (String value : values) {
                PacketType<? extends @NotNull Packet<?>> packet = PacketUtils.getPacket(value.trim());
                if (packet == null || this.filter != null && !this.filter.test(packet)) continue;
                packets.add(packet);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return packets;
    }

    @Override
    protected boolean isValueValid(Set<PacketType<? extends @NotNull Packet<?>>> value) {
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        return this.filter == null ? PacketUtils.getPackets().stream().map(PacketType::toString).toList() : PacketUtils.getPackets().stream().filter(this.filter).map(PacketType::toString).toList();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag valueTag = new ListTag();
        for (PacketType packet : (Set)this.get()) {
            valueTag.add((Object)StringTag.valueOf((String)packet.toString()));
        }
        tag.put("value", (Tag)valueTag);
        return tag;
    }

    @Override
    public Set<PacketType<? extends @NotNull Packet<?>>> load(CompoundTag tag) {
        ((Set)this.get()).clear();
        Tag valueTag = tag.get("value");
        if (valueTag instanceof ListTag) {
            ListTag listTag = (ListTag)valueTag;
            for (Tag t : listTag) {
                PacketType<? extends @NotNull Packet<?>> packet = PacketUtils.getPacket(t.asString().orElse(""));
                if (packet == null || this.filter != null && !this.filter.test(packet)) continue;
                ((Set)this.get()).add(packet);
            }
        }
        return (Set)this.get();
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, Set<PacketType<? extends Packet<?>>>, PacketListSetting> {
        private Predicate<PacketType<? extends @NotNull Packet<?>>> filter;

        public Builder() {
            super(new ObjectOpenHashSet(0));
        }

        public Builder filter(Predicate<PacketType<? extends @NotNull Packet<?>>> filter) {
            this.filter = filter;
            return this;
        }

        public Builder clientbound() {
            return this.filter(type -> type.flow() == PacketFlow.CLIENTBOUND);
        }

        public Builder serverbound() {
            return this.filter(type -> type.flow() == PacketFlow.SERVERBOUND);
        }

        @Override
        public PacketListSetting build() {
            return new PacketListSetting(this.name, this.description, (Set)this.defaultValue, this.onChanged, this.onModuleActivated, this.filter, this.visible);
        }
    }
}

