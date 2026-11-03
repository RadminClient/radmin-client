/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.renderer.entity.ArmorStandRenderer
 *  net.minecraft.world.entity.EntityType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.ESP;

@Mixin(value={ArmorStandRenderer.class})
public abstract class ArmorStandRendererMixin {
    @Unique
    private static ESP esp;

    @ModifyExpressionValue(method={"getRenderType(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;isMarker:Z", opcode=180)})
    private boolean modifyMarkerValue(boolean original) {
        if (esp == null) {
            esp = Modules.get().get(ESP.class);
        }
        return original && (!esp.isActive() || esp.shouldSkip(EntityType.ARMOR_STAND));
    }
}

