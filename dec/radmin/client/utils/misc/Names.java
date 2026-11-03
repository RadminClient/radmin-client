/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.client.sounds.WeighedSoundEvents
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.util.StringUtil
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.level.block.Block
 *  org.apache.commons.lang3.StringUtils
 */
package radmin.client.utils.misc;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;
import radmin.client.MeteorClient;
import radmin.client.events.game.ResourcePacksReloadedEvent;
import radmin.client.utils.PreInit;

public class Names {
    private static final Map<MobEffect, String> statusEffectNames = new Reference2ObjectOpenHashMap(16);
    private static final Map<Item, String> itemNames = new Reference2ObjectOpenHashMap(128);
    private static final Map<Block, String> blockNames = new Reference2ObjectOpenHashMap(128);
    private static final Map<ResourceKey<Enchantment>, String> enchantmentKeyNames = WeakHashMap.newWeakHashMap((int)16);
    private static final Map<Holder<Enchantment>, String> enchantmentEntryNames = new Reference2ObjectOpenHashMap(16);
    private static final Map<EntityType<?>, String> entityTypeNames = new Reference2ObjectOpenHashMap(64);
    private static final Map<ParticleType<?>, String> particleTypesNames = new Reference2ObjectOpenHashMap(64);
    private static final Map<Identifier, String> soundNames = HashMap.newHashMap((int)64);

    private Names() {
    }

    @PreInit
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe(Names.class);
    }

    @EventHandler
    private static void onResourcePacksReloaded(ResourcePacksReloadedEvent event) {
        statusEffectNames.clear();
        itemNames.clear();
        blockNames.clear();
        enchantmentEntryNames.clear();
        entityTypeNames.clear();
        particleTypesNames.clear();
        soundNames.clear();
    }

    public static String get(MobEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringUtil.stripColor((String)I18n.get((String)effect1.getDescriptionId(), (Object[])new Object[0])));
    }

    public static String get(Item item) {
        return itemNames.computeIfAbsent(item, item1 -> StringUtil.stripColor((String)I18n.get((String)item1.getDescriptionId(), (Object[])new Object[0])));
    }

    public static String get(Block block) {
        return blockNames.computeIfAbsent(block, block1 -> StringUtil.stripColor((String)I18n.get((String)block1.getDescriptionId(), (Object[])new Object[0])));
    }

    public static String get(ResourceKey<Enchantment> enchantment) {
        return enchantmentKeyNames.computeIfAbsent(enchantment, enchantment1 -> Optional.ofNullable(Minecraft.getInstance().getConnection()).map(ClientPacketListener::registryAccess).flatMap(registryManager -> registryManager.lookup(Registries.ENCHANTMENT)).flatMap(registry -> registry.get(enchantment.identifier())).map(Names::get).orElseGet(() -> {
            String key = "enchantment." + enchantment1.identifier().toLanguageKey();
            String translated = I18n.get((String)key, (Object[])new Object[0]);
            return translated == key ? enchantment1.identifier().toString() : translated;
        }));
    }

    public static String get(Holder<Enchantment> enchantment) {
        return enchantmentEntryNames.computeIfAbsent(enchantment, holder -> StringUtil.stripColor((String)((Enchantment)enchantment.value()).description().getString()));
    }

    public static String get(EntityType<?> entityType) {
        return entityTypeNames.computeIfAbsent(entityType, entityType1 -> StringUtil.stripColor((String)I18n.get((String)entityType1.getDescriptionId(), (Object[])new Object[0])));
    }

    public static String get(ParticleType<?> type) {
        return particleTypesNames.computeIfAbsent(type, particleType -> StringUtils.capitalize((String)BuiltInRegistries.PARTICLE_TYPE.getKey((Object)type).getPath().replace("_", " ")));
    }

    public static String getSoundName(Identifier id) {
        return soundNames.computeIfAbsent(id, identifier -> {
            WeighedSoundEvents soundSet = MeteorClient.mc.getSoundManager().getSoundEvent(identifier);
            if (soundSet == null) {
                return identifier.getPath();
            }
            Component text = soundSet.getSubtitle();
            if (text == null) {
                return identifier.getPath();
            }
            return StringUtil.stripColor((String)text.getString());
        });
    }

    public static String get(ItemStack stack) {
        return stack.getHoverName().getString();
    }
}

