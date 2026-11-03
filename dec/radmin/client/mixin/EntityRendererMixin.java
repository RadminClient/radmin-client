/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.state.EntityRenderState
 *  net.minecraft.client.renderer.entity.state.LivingEntityRenderState
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.FallingBlockEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.LightLayer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.ESP;
import radmin.client.systems.modules.render.Fullbright;
import radmin.client.systems.modules.render.Nametags;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.utils.entity.EntityUtils;
import radmin.client.utils.render.color.Color;

@Mixin(value={EntityRenderer.class})
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Unique
    private ESP esp;
    @Unique
    private NoRender noRender;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.esp = Modules.get().get(ESP.class);
        this.noRender = Modules.get().get(NoRender.class);
    }

    @Inject(method={"getNameTag"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderLabel(T entity, CallbackInfoReturnable<Component> cir) {
        if (this.noRender.noNametags()) {
            cir.setReturnValue(null);
        }
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (Modules.get().get(Nametags.class).playerNametags() && (EntityUtils.getGameMode(player) != null || !Modules.get().get(Nametags.class).excludeBots())) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method={"shouldRender"}, at={@At(value="HEAD")}, cancellable=true)
    private void shouldRender(T entity, Frustum culler, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (this.noRender.noEntity((Entity)entity)) {
            cir.setReturnValue((Object)false);
        }
        if (this.noRender.noFallingBlocks() && entity instanceof FallingBlockEntity) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"affectedByCulling"}, at={@At(value="HEAD")}, cancellable=true)
    void canBeCulled(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.esp.forceRender()) {
            cir.setReturnValue((Object)false);
        }
    }

    @ModifyReturnValue(method={"getSkyLightLevel"}, at={@At(value="RETURN")})
    private int onGetSkyLight(int original) {
        return Math.max(Modules.get().get(Fullbright.class).getLuminance(LightLayer.SKY), original);
    }

    @ModifyReturnValue(method={"getBlockLightLevel"}, at={@At(value="RETURN")})
    private int onGetBlockLight(int original) {
        return Math.max(Modules.get().get(Fullbright.class).getLuminance(LightLayer.BLOCK), original);
    }

    @ModifyExpressionValue(method={"extractRenderState"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/level/Level;getBrightness(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I")})
    private int onGetLightLevel(int original) {
        return Math.max(Modules.get().get(Fullbright.class).getLuminance(LightLayer.BLOCK), original);
    }

    @Inject(method={"extractRenderState"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/entity/state/EntityRenderState;outlineColor:I", shift=At.Shift.AFTER, opcode=181)})
    private void onGetOutlineColor(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (this.esp.isGlow() && !this.esp.shouldSkip((Entity)entity)) {
            Color color = this.esp.getColor((Entity)entity);
            if (color == null) {
                return;
            }
            ((EntityRenderState)state).outlineColor = color.getPacked();
        }
    }

    @Inject(method={"finalizeRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateShadow(Entity entity, EntityRenderState state, CallbackInfo ci) {
        if (this.noRender.noDeadEntities() && entity instanceof LivingEntity && state instanceof LivingEntityRenderState) {
            LivingEntityRenderState livingEntityRenderState = (LivingEntityRenderState)state;
            if (livingEntityRenderState.deathTime > 0.0f) {
                ci.cancel();
            }
        }
    }
}

