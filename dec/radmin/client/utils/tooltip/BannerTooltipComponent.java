/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.object.banner.BannerFlagModel
 *  net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.BannerItem
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BannerPatternLayers
 */
package radmin.client.utils.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import radmin.client.MeteorClient;
import radmin.client.mixin.GuiGraphicsExtractorAccessor;
import radmin.client.utils.render.CustomBannerGuiElementRenderState;
import radmin.client.utils.tooltip.MeteorTooltipData;

public class BannerTooltipComponent
implements MeteorTooltipData,
ClientTooltipComponent {
    private final DyeColor color;
    private final BannerPatternLayers patterns;
    private final BannerFlagModel bannerFlag;

    public BannerTooltipComponent(ItemStack banner) {
        this.color = ((BannerItem)banner.getItem()).getColor();
        this.patterns = (BannerPatternLayers)banner.getOrDefault(DataComponents.BANNER_PATTERNS, (Object)BannerPatternLayers.EMPTY);
        ModelPart modelPart = MeteorClient.mc.getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
        this.bannerFlag = new BannerFlagModel(modelPart);
    }

    public BannerTooltipComponent(DyeColor color, BannerPatternLayers patterns) {
        this.color = color;
        this.patterns = patterns;
        ModelPart modelPart = MeteorClient.mc.getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
        this.bannerFlag = new BannerFlagModel(modelPart);
    }

    @Override
    public ClientTooltipComponent getComponent() {
        return this;
    }

    public int getHeight(Font textRenderer) {
        return 80;
    }

    public int getWidth(Font textRenderer) {
        return 40;
    }

    public void extractImage(Font textRenderer, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        int centerX = width / 2 - this.getWidth(null) / 2;
        GuiGraphicsExtractorAccessor contextAccessor = (GuiGraphicsExtractorAccessor)graphics;
        contextAccessor.getGuiRenderState().addPicturesInPictureState((PictureInPictureRenderState)new CustomBannerGuiElementRenderState(this.bannerFlag, this.color, this.patterns, centerX + x, y, centerX + x + this.getWidth(null), y + this.getHeight(null), contextAccessor.getScissorStack().peek(), 32.0f));
    }
}

