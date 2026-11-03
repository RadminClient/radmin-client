/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Anchor;
import radmin.client.systems.modules.movement.Scaffold;

@Mixin(value={ServerPlayer.class})
public abstract class ServerPlayerMixin
extends LivingEntity {
    protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method={"jumpFromGround"}, at={@At(value="HEAD")}, cancellable=true)
    public void dontJump(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            return;
        }
        Anchor module = Modules.get().get(Anchor.class);
        if (module.isActive() && module.cancelJump) {
            ci.cancel();
        } else if (Modules.get().get(Scaffold.class).towering()) {
            ci.cancel();
        }
    }
}

