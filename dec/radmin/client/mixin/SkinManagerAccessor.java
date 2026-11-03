/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.SkinManager
 *  net.minecraft.client.resources.SkinManager$TextureCache
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package radmin.client.mixin;

import net.minecraft.client.resources.SkinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SkinManager.class})
public interface SkinManagerAccessor {
    @Accessor(value="skinTextures")
    public SkinManager.TextureCache meteor$getSkinTextures();
}

