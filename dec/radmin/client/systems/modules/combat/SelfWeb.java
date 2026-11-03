/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.world.item.Items
 */
package radmin.client.systems.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.item.Items;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.entity.SortPriority;
import radmin.client.utils.entity.TargetUtils;
import radmin.client.utils.player.FindItemResult;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.world.BlockUtils;

public class SelfWeb
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;
    private final Setting<Integer> range;
    private final Setting<Boolean> doubles;
    private final Setting<Boolean> turnOff;
    private final Setting<Boolean> rotate;

    public SelfWeb() {
        super(Categories.Combat, "self-web", "Automatically places webs on you.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("mode")).description("The mode to use for selfweb.")).defaultValue(Mode.Normal)).build());
        this.range = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("range")).description("How far away the player has to be from you to place webs. Requires Mode to Smart.")).defaultValue(3)).min(1).sliderRange(1, 7).visible(() -> this.mode.get() == Mode.Smart)).build());
        this.doubles = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("double-place")).description("Places webs in your upper hitbox as well.")).defaultValue(false)).build());
        this.turnOff = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("auto-toggle")).description("Toggles off after placing the webs.")).defaultValue(true)).build());
        this.rotate = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("rotate")).description("Forces you to rotate downwards when placing webs.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        switch (this.mode.get().ordinal()) {
            case 0: {
                this.placeWeb();
                break;
            }
            case 1: {
                if (TargetUtils.getPlayerTarget(this.range.get().intValue(), SortPriority.LowestDistance) == null) break;
                this.placeWeb();
            }
        }
    }

    private void placeWeb() {
        FindItemResult web = InvUtils.findInHotbar(Items.COBWEB);
        BlockUtils.place(this.mc.player.blockPosition(), web, this.rotate.get(), 0, false);
        if (this.doubles.get().booleanValue()) {
            BlockUtils.place(this.mc.player.blockPosition().offset(0, 1, 0), web, this.rotate.get(), 0, false);
        }
        if (this.turnOff.get().booleanValue()) {
            this.toggle();
        }
    }

    public static enum Mode {
        Normal,
        Smart;

    }
}

