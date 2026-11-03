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
import radmin.client.pathing.IPathManager;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.Settings;

public class NopPathManager
implements IPathManager {
    private final NopSettings settings = new NopSettings();

    @Override
    public String getName() {
        return "none";
    }

    @Override
    public boolean isPathing() {
        return false;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void moveTo(BlockPos pos, boolean ignoreY) {
    }

    @Override
    public void moveInDirection(float yaw) {
    }

    @Override
    public void mine(Block ... blocks) {
    }

    @Override
    public void follow(Predicate<Entity> entity) {
    }

    @Override
    public float getTargetYaw() {
        return 0.0f;
    }

    @Override
    public float getTargetPitch() {
        return 0.0f;
    }

    @Override
    public IPathManager.ISettings getSettings() {
        return this.settings;
    }

    private static class NopSettings
    implements IPathManager.ISettings {
        private final Settings settings = new Settings();
        private final Setting<Boolean> setting = new BoolSetting.Builder().build();

        private NopSettings() {
        }

        @Override
        public Settings get() {
            return this.settings;
        }

        @Override
        public Setting<Boolean> getWalkOnWater() {
            this.setting.reset();
            return this.setting;
        }

        @Override
        public Setting<Boolean> getWalkOnLava() {
            this.setting.reset();
            return this.setting;
        }

        @Override
        public Setting<Boolean> getStep() {
            this.setting.reset();
            return this.setting;
        }

        @Override
        public Setting<Boolean> getNoFall() {
            this.setting.reset();
            return this.setting;
        }

        @Override
        public void save() {
        }
    }
}

