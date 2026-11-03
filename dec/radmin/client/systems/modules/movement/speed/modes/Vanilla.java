/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.movement.speed.modes;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import radmin.client.events.entity.player.PlayerMoveEvent;
import radmin.client.mixininterface.IVec3;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Anchor;
import radmin.client.systems.modules.movement.speed.SpeedMode;
import radmin.client.systems.modules.movement.speed.SpeedModes;
import radmin.client.utils.player.PlayerUtils;

public class Vanilla
extends SpeedMode {
    public Vanilla() {
        super(SpeedModes.Vanilla);
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Anchor anchor;
        Vec3 vel = PlayerUtils.getHorizontalVelocity(this.settings.vanillaSpeed.get());
        double velX = vel.x();
        double velZ = vel.z();
        if (this.mc.player.hasEffect(MobEffects.SPEED)) {
            double value = (double)(this.mc.player.getEffect(MobEffects.SPEED).getAmplifier() + 1) * 0.205;
            velX += velX * value;
            velZ += velZ * value;
        }
        if ((anchor = Modules.get().get(Anchor.class)).isActive() && anchor.controlMovement) {
            velX = anchor.deltaX;
            velZ = anchor.deltaZ;
        }
        ((IVec3)event.movement).meteor$set(velX, event.movement.y, velZ);
    }
}

