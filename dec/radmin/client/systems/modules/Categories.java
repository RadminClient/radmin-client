/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Items
 */
package radmin.client.systems.modules;

import net.minecraft.world.item.Items;
import radmin.client.addons.AddonManager;
import radmin.client.addons.MeteorAddon;
import radmin.client.systems.modules.Category;
import radmin.client.systems.modules.Modules;
import radmin.client.utils.render.DisplayItemUtils;

public class Categories {
    public static final Category Combat = new Category("Combat", () -> DisplayItemUtils.toStack(Items.GOLDEN_SWORD));
    public static final Category Player = new Category("Player", () -> DisplayItemUtils.toStack(Items.ARMOR_STAND));
    public static final Category Movement = new Category("Movement", () -> DisplayItemUtils.toStack(Items.DIAMOND_BOOTS));
    public static final Category Render = new Category("Render", () -> DisplayItemUtils.toStack(Items.GLASS));
    public static final Category World = new Category("World", () -> DisplayItemUtils.toStack(Items.GRASS_BLOCK));
    public static final Category Misc = new Category("Misc", () -> DisplayItemUtils.toStack(Items.LAVA_BUCKET));
    public static boolean REGISTERING;

    public static void init() {
        REGISTERING = true;
        Modules.registerCategory(Combat);
        Modules.registerCategory(Player);
        Modules.registerCategory(Movement);
        Modules.registerCategory(Render);
        Modules.registerCategory(World);
        Modules.registerCategory(Misc);
        AddonManager.ADDONS.forEach(MeteorAddon::onRegisterCategories);
        REGISTERING = false;
    }
}

