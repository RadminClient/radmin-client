/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.MapTextureManager
 *  net.minecraft.client.resources.MapTextureManager$MapInstance
 *  net.minecraft.world.level.saveddata.maps.MapId
 *  net.minecraft.world.level.saveddata.maps.MapItemSavedData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package radmin.client.mixin;

import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={MapTextureManager.class})
public interface MapTextureManagerAccessor {
    @Invoker(value="getOrCreateMapInstance")
    public MapTextureManager.MapInstance meteor$invokeGetOrCreateMapInstance(MapId var1, MapItemSavedData var2);
}

