/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.attribute.EnvironmentAttributes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 */
package radmin.client.systems.modules.movement;

import java.util.function.Predicate;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.mixin.ServerboundMovePlayerPacketAccessor;
import radmin.client.mixininterface.IServerboundMovePlayerPacket;
import radmin.client.mixininterface.IVec3;
import radmin.client.pathing.PathManagers;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.Flight;
import radmin.client.utils.Utils;
import radmin.client.utils.entity.EntityUtils;
import radmin.client.utils.player.FindItemResult;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.player.PlayerUtils;
import radmin.client.utils.player.Rotations;
import radmin.client.utils.world.BlockUtils;

public class NoFall
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;
    private final Setting<PlacedItem> placedItem;
    private final Setting<PlaceMode> airPlaceMode;
    private final Setting<Boolean> anchor;
    private final Setting<Boolean> antiBounce;
    private final Setting<Boolean> pauseOnMace;
    private boolean placedWater;
    private BlockPos targetPos;
    private int timer;
    private boolean prePathManagerNoFall;

    public NoFall() {
        super(Categories.Movement, "no-fall", "Attempts to prevent you from taking fall damage.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("The way you are saved from fall damage.")).defaultValue(Mode.Packet)).build());
        this.placedItem = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("placed-item")).description("Which block to place.")).defaultValue(PlacedItem.Bucket)).visible(() -> this.mode.get() == Mode.Place)).build());
        this.airPlaceMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("air-place-mode")).description("Whether place mode places before you die or before you take damage.")).defaultValue(PlaceMode.BeforeDeath)).visible(() -> this.mode.get() == Mode.AirPlace)).build());
        this.anchor = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("anchor")).description("Centers the player and reduces movement when using bucket or air place mode.")).defaultValue(true)).visible(() -> this.mode.get() != Mode.Packet)).build());
        this.antiBounce = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("anti-bounce")).description("Disables bouncing on slime-block and bed upon landing.")).defaultValue(true)).build());
        this.pauseOnMace = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("pause-on-mace")).description("Pauses NoFall when using a mace.")).defaultValue(true)).build());
    }

    @Override
    public void onActivate() {
        this.prePathManagerNoFall = PathManagers.get().getSettings().getNoFall().get();
        if (this.mode.get() == Mode.Packet) {
            PathManagers.get().getSettings().getNoFall().set(true);
        }
        this.placedWater = false;
    }

    @Override
    public void onDeactivate() {
        PathManagers.get().getSettings().getNoFall().set(this.prePathManagerNoFall);
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (this.mc.player == null) {
            return;
        }
        if (this.pauseOnMace.get().booleanValue() && this.mc.player.getMainHandItem().getItem() instanceof MaceItem) {
            return;
        }
        if (this.mc.player.getAbilities().instabuild || !(event.packet instanceof ServerboundMovePlayerPacket) || this.mode.get() != Mode.Packet || ((IServerboundMovePlayerPacket)event.packet).meteor$getTag() == 1337) {
            return;
        }
        if (!Modules.get().isActive(Flight.class)) {
            if (this.mc.player.isFallFlying()) {
                return;
            }
            if (this.mc.player.getDeltaMovement().y > -0.5) {
                return;
            }
            ((ServerboundMovePlayerPacketAccessor)event.packet).meteor$setOnGround(true);
        } else {
            ((ServerboundMovePlayerPacketAccessor)event.packet).meteor$setOnGround(true);
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!Utils.canUpdate()) {
            return;
        }
        if (this.timer > 20) {
            this.placedWater = false;
            this.timer = 0;
        }
        if (this.mc.player.getAbilities().instabuild) {
            return;
        }
        if (this.pauseOnMace.get().booleanValue() && this.mc.player.getMainHandItem().getItem() instanceof MaceItem) {
            return;
        }
        if (this.mode.get() == Mode.AirPlace) {
            if (!this.airPlaceMode.get().test((float)this.mc.player.fallDistance)) {
                return;
            }
            if (this.anchor.get().booleanValue()) {
                PlayerUtils.centerPlayer();
            }
            Rotations.rotate(this.mc.player.getYRot(), 90.0, Integer.MAX_VALUE, () -> {
                double preY = this.mc.player.getDeltaMovement().y;
                ((IVec3)this.mc.player.getDeltaMovement()).meteor$setY(0.0);
                BlockUtils.place(this.mc.player.blockPosition().below(), InvUtils.findInHotbar(itemStack -> itemStack.getItem() instanceof BlockItem), false, 0, true);
                ((IVec3)this.mc.player.getDeltaMovement()).meteor$setY(preY);
            });
        } else if (this.mode.get() == Mode.Place) {
            PlacedItem placedItem1;
            PlacedItem placedItem = placedItem1 = (Boolean)this.mc.level.environmentAttributes().getDimensionValue(EnvironmentAttributes.WATER_EVAPORATES) != false && this.placedItem.get() == PlacedItem.Bucket ? PlacedItem.PowderSnow : this.placedItem.get();
            if (this.mc.player.fallDistance > 3.0 && !EntityUtils.isAboveWater((Entity)this.mc.player)) {
                BlockHitResult result;
                Item item = placedItem1.item;
                FindItemResult findItemResult = InvUtils.findInHotbar(item);
                if (!findItemResult.found()) {
                    return;
                }
                if (this.anchor.get().booleanValue()) {
                    PlayerUtils.centerPlayer();
                }
                if ((result = this.mc.level.clip(new ClipContext(this.mc.player.position(), this.mc.player.position().subtract(0.0, 5.0, 0.0), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, (Entity)this.mc.player))) != null && result.getType() == HitResult.Type.BLOCK) {
                    this.targetPos = result.getBlockPos().above();
                    if (placedItem1 == PlacedItem.Bucket) {
                        this.useItem(findItemResult, true, this.targetPos, true);
                    } else {
                        this.useItem(findItemResult, placedItem1 == PlacedItem.PowderSnow, this.targetPos, false);
                    }
                }
            }
            if (this.placedWater) {
                ++this.timer;
                if (this.mc.player.getInBlockState().getBlock() == placedItem1.block) {
                    this.useItem(InvUtils.findInHotbar(Items.BUCKET), false, this.targetPos, true);
                } else if (this.mc.level.getBlockState(this.mc.player.blockPosition().below()).getBlock() == Blocks.POWDER_SNOW && this.mc.player.fallDistance == 0.0 && placedItem1.block == Blocks.POWDER_SNOW) {
                    this.useItem(InvUtils.findInHotbar(Items.BUCKET), false, this.targetPos.below(), true);
                }
            }
        }
    }

    public boolean cancelBounce() {
        return this.isActive() && this.antiBounce.get() != false;
    }

    private void useItem(FindItemResult item, boolean placedWater, BlockPos blockPos, boolean useItem) {
        if (!item.found()) {
            return;
        }
        if (useItem) {
            Rotations.rotate(Rotations.getYaw(blockPos), Rotations.getPitch(blockPos), 10, true, () -> {
                if (item.isOffhand()) {
                    this.mc.gameMode.useItem((Player)this.mc.player, InteractionHand.OFF_HAND);
                } else {
                    InvUtils.swap(item.slot(), true);
                    this.mc.gameMode.useItem((Player)this.mc.player, InteractionHand.MAIN_HAND);
                    InvUtils.swapBack();
                }
            });
        } else {
            BlockUtils.place(blockPos, item, true, 10, true);
        }
        this.placedWater = placedWater;
    }

    @Override
    public String getInfoString() {
        return this.mode.get().toString();
    }

    public static enum Mode {
        Packet,
        AirPlace,
        Place;

    }

    public static enum PlacedItem {
        Bucket(Items.WATER_BUCKET, Blocks.WATER),
        PowderSnow(Items.POWDER_SNOW_BUCKET, Blocks.POWDER_SNOW),
        HayBale(Items.HAY_BLOCK, Blocks.HAY_BLOCK),
        Cobweb(Items.COBWEB, Blocks.COBWEB),
        SlimeBlock(Items.SLIME_BLOCK, Blocks.SLIME_BLOCK);

        private final Item item;
        private final Block block;

        private PlacedItem(Item item, Block block) {
            this.item = item;
            this.block = block;
        }
    }

    public static enum PlaceMode {
        BeforeDamage(height -> height.floatValue() > 2.0f),
        BeforeDeath(height -> height.floatValue() > Math.max(PlayerUtils.getTotalHealth(), 2.0f));

        private final Predicate<Float> fallHeight;

        private PlaceMode(Predicate<Float> fallHeight) {
            this.fallHeight = fallHeight;
        }

        public boolean test(float fallheight) {
            return this.fallHeight.test(Float.valueOf(fallheight));
        }
    }
}

