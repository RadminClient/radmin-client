/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer
 *  net.caffeinemc.mods.sodium.client.util.FogParameters
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package radmin.client.mixin.sodium;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={SodiumWorldRenderer.class})
public abstract class SodiumWorldRendererMixin {
    @Unique
    private static final FogParameters DISABLED_FOG = new FogParameters(0.0f, 0.0f, 0.0f, 0.0f, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

    @ModifyVariable(method={"setupTerrain"}, at=@At(value="HEAD"), argsOnly=true, name={"fogParameters"})
    private FogParameters modifyFogParameters(FogParameters fogParameters) {
        if (Modules.get() == null) {
            return fogParameters;
        }
        if (Modules.get().get(NoRender.class).noFog()) {
            return DISABLED_FOG;
        }
        return fogParameters;
    }
}

