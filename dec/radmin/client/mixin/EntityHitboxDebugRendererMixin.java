/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.combat.Hitboxes;

@Mixin(value={EntityHitboxDebugRenderer.class})
public abstract class EntityHitboxDebugRendererMixin {
    @ModifyExpressionValue(method={"showHitboxes"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;", ordinal=0)})
    private AABB meteor$createHitbox(AABB original, Entity entity, float partialTicks, boolean isServerEntity) {
        double v = Modules.get().get(Hitboxes.class).getEntityValue(entity);
        if (v == 0.0) {
            return original;
        }
        return original.inflate(v);
    }
}

