/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 */
package radmin.client.systems.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import radmin.client.events.world.TickEvent;
import radmin.client.mixin.BlockBehaviourAccessor;
import radmin.client.mixininterface.IVec3;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Anchor
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Integer> maxHeight;
    private final Setting<Integer> minPitch;
    private final Setting<Boolean> cancelMove;
    private final Setting<Boolean> pull;
    private final Setting<Double> pullSpeed;
    private final BlockPos.MutableBlockPos blockPos;
    private boolean wasInHole;
    private boolean foundHole;
    private int holeX;
    private int holeZ;
    public boolean cancelJump;
    public boolean controlMovement;
    public double deltaX;
    public double deltaZ;

    public Anchor() {
        super(Categories.Movement, "anchor", "Helps you get into holes by stopping your movement completely over a hole.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.maxHeight = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("max-height")).description("The maximum height Anchor will work at.")).defaultValue(10)).range(0, 255).sliderMax(20).build());
        this.minPitch = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("min-pitch")).description("The minimum pitch at which anchor will work.")).defaultValue(0)).range(-90, 90).sliderRange(-90, 90).build());
        this.cancelMove = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("cancel-jump-in-hole")).description("Prevents you from jumping when Anchor is active and Min Pitch is met.")).defaultValue(false)).build());
        this.pull = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("pull")).description("The pull strength of Anchor.")).defaultValue(false)).build());
        this.pullSpeed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("pull-speed")).description("How fast to pull towards the hole in blocks per second.")).defaultValue(0.3).min(0.0).sliderMax(5.0).build());
        this.blockPos = new BlockPos.MutableBlockPos();
    }

    @Override
    public void onActivate() {
        this.wasInHole = false;
        this.holeZ = 0;
        this.holeX = 0;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        this.cancelJump = this.foundHole && this.cancelMove.get() != false && this.mc.player.getXRot() >= (float)this.minPitch.get().intValue();
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        int z;
        int y;
        this.controlMovement = false;
        int x = Mth.floor((double)this.mc.player.getX());
        if (this.isHole(x, y = Mth.floor((double)this.mc.player.getY()), z = Mth.floor((double)this.mc.player.getZ()))) {
            this.wasInHole = true;
            this.holeX = x;
            this.holeZ = z;
            return;
        }
        if (this.wasInHole && this.holeX == x && this.holeZ == z) {
            return;
        }
        if (this.wasInHole) {
            this.wasInHole = false;
        }
        if (this.mc.player.getXRot() < (float)this.minPitch.get().intValue()) {
            return;
        }
        this.foundHole = false;
        double holeX = 0.0;
        double holeZ = 0.0;
        for (int i = 0; i < this.maxHeight.get() && --y > this.mc.level.getMinY() && this.isAir(x, y, z); ++i) {
            if (!this.isHole(x, y, z)) continue;
            this.foundHole = true;
            holeX = (double)x + 0.5;
            holeZ = (double)z + 0.5;
            break;
        }
        if (this.foundHole) {
            this.controlMovement = true;
            this.deltaX = Mth.clamp((double)(holeX - this.mc.player.getX()), (double)-0.05, (double)0.05);
            this.deltaZ = Mth.clamp((double)(holeZ - this.mc.player.getZ()), (double)-0.05, (double)0.05);
            ((IVec3)this.mc.player.getDeltaMovement()).meteor$set(this.deltaX, this.mc.player.getDeltaMovement().y - (this.pull.get() != false ? this.pullSpeed.get() : 0.0), this.deltaZ);
        }
    }

    private boolean isHole(int x, int y, int z) {
        return this.isHoleBlock(x, y - 1, z) && this.isHoleBlock(x + 1, y, z) && this.isHoleBlock(x - 1, y, z) && this.isHoleBlock(x, y, z + 1) && this.isHoleBlock(x, y, z - 1);
    }

    private boolean isHoleBlock(int x, int y, int z) {
        this.blockPos.set(x, y, z);
        Block block = this.mc.level.getBlockState((BlockPos)this.blockPos).getBlock();
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN;
    }

    private boolean isAir(int x, int y, int z) {
        this.blockPos.set(x, y, z);
        return !((BlockBehaviourAccessor)this.mc.level.getBlockState((BlockPos)this.blockPos).getBlock()).meteor$isHasCollision();
    }
}

