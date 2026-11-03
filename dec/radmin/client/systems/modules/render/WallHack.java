/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.irisshaders.iris.api.v0.IrisApi
 *  net.minecraft.world.level.block.Block
 */
package radmin.client.systems.modules.render;

import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.world.level.block.Block;
import radmin.client.MixinPlugin;
import radmin.client.events.world.ChunkOcclusionEvent;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.BlockListSetting;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class WallHack
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<Integer> opacity;
    public final Setting<List<Block>> blocks;
    public final Setting<Boolean> occludeChunks;

    public WallHack() {
        super(Categories.Render, "wall-hack", "Makes blocks translucent.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.opacity = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)new IntSetting.Builder().name("opacity")).description("The opacity for rendered blocks.")).defaultValue(0)).range(0, 255).sliderMax(255).onChanged(n -> {
            if (this.isActive()) {
                this.mc.levelRenderer.allChanged();
            }
        })).build());
        this.blocks = this.sgGeneral.add(((BlockListSetting.Builder)((BlockListSetting.Builder)((BlockListSetting.Builder)new BlockListSetting.Builder().name("blocks")).description("What blocks should be targeted for Wall Hack.")).defaultValue(new Block[0]).onChanged(list -> {
            if (this.isActive()) {
                this.mc.levelRenderer.allChanged();
            }
        })).build());
        this.occludeChunks = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("occlude-chunks")).description("Whether caves should occlude underground (may look wonky when on).")).defaultValue(false)).build());
    }

    @Override
    public void onActivate() {
        this.mc.levelRenderer.allChanged();
    }

    @Override
    public void onDeactivate() {
        this.mc.levelRenderer.allChanged();
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        if (MixinPlugin.isIrisPresent && IrisApi.getInstance().isShaderPackInUse()) {
            return theme.label("Warning: Due to shaders in use, opacity is overridden to 0.");
        }
        return null;
    }

    @EventHandler
    private void onChunkOcclusion(ChunkOcclusionEvent event) {
        if (!this.occludeChunks.get().booleanValue()) {
            event.cancel();
        }
    }
}

