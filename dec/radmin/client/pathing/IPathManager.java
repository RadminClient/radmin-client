/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.block.Block
 */
package radmin.client.pathing;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import radmin.client.settings.Setting;
import radmin.client.settings.Settings;

public interface IPathManager {
    public String getName();

    public boolean isPathing();

    public void pause();

    public void resume();

    public void stop();

    default public void moveTo(BlockPos pos) {
        this.moveTo(pos, false);
    }

    public void moveTo(BlockPos var1, boolean var2);

    public void moveInDirection(float var1);

    public void mine(Block ... var1);

    public void follow(Predicate<Entity> var1);

    public float getTargetYaw();

    public float getTargetPitch();

    public ISettings getSettings();

    public static interface ISettings {
        public Settings get();

        public Setting<Boolean> getWalkOnWater();

        public Setting<Boolean> getWalkOnLava();

        public Setting<Boolean> getStep();

        public Setting<Boolean> getNoFall();

        public void save();
    }
}

