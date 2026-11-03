/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Streams
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 */
package radmin.client.systems.modules.movement;

import com.google.common.collect.Streams;
import java.util.stream.Stream;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Parkour
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> edgeDistance;

    public Parkour() {
        super(Categories.Movement, "parkour", "Automatically jumps at the edges of blocks.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.edgeDistance = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("edge-distance")).description("How far from the edge should you jump.")).range(0.001, 0.1).defaultValue(0.001).build());
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!this.mc.player.onGround() || this.mc.options.keyJump.isDown()) {
            return;
        }
        if (this.mc.player.isShiftKeyDown() || this.mc.options.keyShift.isDown()) {
            return;
        }
        AABB box = this.mc.player.getBoundingBox();
        AABB adjustedBox = box.move(0.0, -0.5, 0.0).inflate(-this.edgeDistance.get().doubleValue(), 0.0, -this.edgeDistance.get().doubleValue());
        Stream blockCollisions = Streams.stream((Iterable)this.mc.level.getBlockCollisions((Entity)this.mc.player, adjustedBox));
        if (blockCollisions.findAny().isPresent()) {
            return;
        }
        this.mc.player.jumpFromGround();
    }
}

