/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.client.renderer.RenderPipelines
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.utils.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import radmin.client.MeteorClient;
import radmin.client.utils.render.RenderUtils;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.tooltip.MeteorTooltipData;

public class ContainerTooltipComponent
implements ClientTooltipComponent,
MeteorTooltipData {
    private static final Identifier TEXTURE_CONTAINER_BACKGROUND = MeteorClient.identifier("textures/container.png");
    private final ItemStack[] items;
    private final Color color;

    public ContainerTooltipComponent(ItemStack[] items, Color color) {
        this.items = items;
        this.color = color;
    }

    @Override
    public ClientTooltipComponent getComponent() {
        return this;
    }

    public int getHeight(Font textRenderer) {
        return 67;
    }

    public int getWidth(Font textRenderer) {
        return 176;
    }

    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_CONTAINER_BACKGROUND, x, y, 0.0f, 0.0f, 176, 67, 176, 67, this.color.getPacked());
        int row = 0;
        int i = 0;
        for (ItemStack itemStack : this.items) {
            RenderUtils.drawItem(graphics, itemStack, x + 8 + i * 18, y + 7 + row * 18, 1.0f, true, null, false);
            if (++i < 9) continue;
            i = 0;
            ++row;
        }
    }
}

