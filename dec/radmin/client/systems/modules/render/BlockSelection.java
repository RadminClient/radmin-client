/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import radmin.client.events.render.Render3DEvent;
import radmin.client.renderer.ShapeMode;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ColorSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.color.SettingColor;

public class BlockSelection
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> advanced;
    private final Setting<Boolean> oneSide;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private final Setting<Boolean> hideInside;

    public BlockSelection() {
        super(Categories.Render, "block-selection", "Modifies how your block selection is rendered.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.advanced = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("advanced")).description("Shows a more advanced outline on different types of shape blocks.")).defaultValue(true)).build());
        this.oneSide = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("single-side")).description("Only renders the side you are looking at.")).defaultValue(false)).build());
        this.shapeMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("shape-mode")).description("How the shapes are rendered.")).defaultValue(ShapeMode.Both)).build());
        this.sideColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("side-color")).description("The side color.")).defaultValue(new SettingColor(255, 255, 255, 50)).build());
        this.lineColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("line-color")).description("The line color.")).defaultValue(new SettingColor(255, 255, 255, 255)).build());
        this.hideInside = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("hide-when-inside-block")).description("Hide selection when inside target block.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        BlockHitResult result;
        HitResult hitResult;
        if (this.mc.hitResult == null || !((hitResult = this.mc.hitResult) instanceof BlockHitResult) || (result = (BlockHitResult)hitResult).getType() == HitResult.Type.MISS) {
            return;
        }
        if (this.hideInside.get().booleanValue() && result.isInside()) {
            return;
        }
        BlockPos bp = result.getBlockPos();
        Direction side = result.getDirection();
        VoxelShape shape = this.mc.level.getBlockState(bp).getShape((BlockGetter)this.mc.level, bp);
        if (shape.isEmpty()) {
            return;
        }
        AABB box = shape.bounds();
        if (this.oneSide.get().booleanValue()) {
            switch (side) {
                case UP: 
                case DOWN: {
                    event.renderer.sideHorizontal((double)bp.getX() + box.minX, (double)bp.getY() + (side == Direction.DOWN ? box.minY : box.maxY), (double)bp.getZ() + box.minZ, (double)bp.getX() + box.maxX, (double)bp.getZ() + box.maxZ, this.sideColor.get(), this.lineColor.get(), this.shapeMode.get());
                    break;
                }
                case SOUTH: 
                case NORTH: {
                    double z = side == Direction.NORTH ? box.minZ : box.maxZ;
                    event.renderer.sideVertical((double)bp.getX() + box.minX, (double)bp.getY() + box.minY, (double)bp.getZ() + z, (double)bp.getX() + box.maxX, (double)bp.getY() + box.maxY, (double)bp.getZ() + z, this.sideColor.get(), this.lineColor.get(), this.shapeMode.get());
                    break;
                }
                case EAST: 
                case WEST: {
                    double x = side == Direction.WEST ? box.minX : box.maxX;
                    event.renderer.sideVertical((double)bp.getX() + x, (double)bp.getY() + box.minY, (double)bp.getZ() + box.minZ, (double)bp.getX() + x, (double)bp.getY() + box.maxY, (double)bp.getZ() + box.maxZ, this.sideColor.get(), this.lineColor.get(), this.shapeMode.get());
                }
            }
        } else if (this.advanced.get().booleanValue()) {
            if (this.shapeMode.get() == ShapeMode.Both || this.shapeMode.get() == ShapeMode.Lines) {
                shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) -> event.renderer.line((double)bp.getX() + minX, (double)bp.getY() + minY, (double)bp.getZ() + minZ, (double)bp.getX() + maxX, (double)bp.getY() + maxY, (double)bp.getZ() + maxZ, this.lineColor.get()));
            }
            if (this.shapeMode.get() == ShapeMode.Both || this.shapeMode.get() == ShapeMode.Sides) {
                for (AABB b : shape.toAabbs()) {
                    this.render(event, bp, b);
                }
            }
        } else {
            this.render(event, bp, box);
        }
    }

    private void render(Render3DEvent event, BlockPos bp, AABB box) {
        event.renderer.box((double)bp.getX() + box.minX, (double)bp.getY() + box.minY, (double)bp.getZ() + box.minZ, (double)bp.getX() + box.maxX, (double)bp.getY() + box.maxY, (double)bp.getZ() + box.maxZ, this.sideColor.get(), this.lineColor.get(), this.shapeMode.get(), 0);
    }
}

