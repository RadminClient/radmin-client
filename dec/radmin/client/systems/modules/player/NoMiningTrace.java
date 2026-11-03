/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 */
package radmin.client.systems.modules.player;

import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EntityTypeListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class NoMiningTrace
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Set<EntityType<?>>> entities;
    private final Setting<Boolean> onlyWhenHoldingPickaxe;

    public NoMiningTrace() {
        super(Categories.Player, "no-mining-trace", "Allows you to mine blocks through entities.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.entities = this.sgGeneral.add(((EntityTypeListSetting.Builder)((EntityTypeListSetting.Builder)new EntityTypeListSetting.Builder().name("blacklisted-entities")).description("Entities you will interact with as normal.")).defaultValue(new EntityType[0]).build());
        this.onlyWhenHoldingPickaxe = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("only-when-holding-a-pickaxe")).description("Whether or not to work only when holding a pickaxe.")).defaultValue(true)).build());
    }

    public boolean canWork(Entity entity) {
        if (!this.isActive()) {
            return false;
        }
        return !(this.onlyWhenHoldingPickaxe.get() != false && !this.mc.player.getMainHandItem().is(ItemTags.PICKAXES) && !this.mc.player.getOffhandItem().is(ItemTags.PICKAXES) || entity != null && this.entities.get().contains(entity.getType()));
    }
}

