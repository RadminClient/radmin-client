/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Stack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.resources.Identifier
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  org.joml.Matrix3x2fStack
 */
package radmin.client.gui.renderer;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.renderer.GuiRenderOperation;
import radmin.client.gui.renderer.Scissor;
import radmin.client.gui.renderer.operations.TextOperation;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.renderer.packer.TexturePacker;
import radmin.client.gui.widgets.WWidget;
import radmin.client.renderer.Renderer2D;
import radmin.client.renderer.Texture;
import radmin.client.utils.PostInit;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.Pool;
import radmin.client.utils.render.RenderUtils;
import radmin.client.utils.render.color.Color;

public class GuiRenderer {
    private static final Color WHITE = new Color(255, 255, 255);
    private static final TexturePacker TEXTURE_PACKER = new TexturePacker();
    public static Texture TEXTURE;
    public static GuiTexture CIRCLE;
    public static GuiTexture CIRCLE_SOFT;
    public static GuiTexture TRIANGLE;
    public static GuiTexture EDIT;
    public static GuiTexture RESET;
    public static GuiTexture FAVORITE_NO;
    public static GuiTexture FAVORITE_YES;
    public static GuiTexture COPY;
    public static GuiTexture PASTE;
    public static GuiTexture LOGO;
    public static GuiTexture LOGO_BLUE;
    public static GuiTexture LOGO_WHITE;
    public GuiTheme theme;
    private final Renderer2D r = new Renderer2D(false);
    private final Renderer2D rTex = new Renderer2D(true);
    private final Pool<Scissor> scissorPool = new Pool<Scissor>(Scissor::new);
    private final Stack<Scissor> scissorStack = new ObjectArrayList();
    private final Pool<TextOperation> textPool = new Pool<TextOperation>(TextOperation::new);
    private final List<TextOperation> texts = new ObjectArrayList();
    private final List<Runnable> postTasks = new ObjectArrayList();
    public String tooltip;
    public String lastTooltip;
    public WWidget tooltipWidget;
    private double tooltipAnimProgress;
    private GuiGraphicsExtractor graphics;

    public static GuiTexture addTexture(Identifier id) {
        return TEXTURE_PACKER.add(id);
    }

    @PostInit
    public static void init() {
        CIRCLE = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/circle.png"));
        CIRCLE_SOFT = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/circle_soft.png"));
        TRIANGLE = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/triangle.png"));
        EDIT = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/edit.png"));
        RESET = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/reset.png"));
        FAVORITE_NO = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/favorite_no.png"));
        FAVORITE_YES = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/favorite_yes.png"));
        COPY = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/copy.png"));
        PASTE = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/gui/paste.png"));
        LOGO = GuiRenderer.addTexture(MeteorClient.identifier("icon.png"));
        LOGO_BLUE = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/logo_blue.png"));
        LOGO_WHITE = GuiRenderer.addTexture(MeteorClient.identifier("textures/icons/logo_white.png"));
        TEXTURE = TEXTURE_PACKER.pack();
    }

    public void begin(GuiGraphicsExtractor graphics) {
        this.graphics = graphics;
        this.graphics.nextStratum();
        Matrix3x2fStack matrices = graphics.pose();
        matrices.pushMatrix();
        matrices.scale(1.0f / (float)MeteorClient.mc.getWindow().getGuiScale());
        this.scissorStart(0.0, 0.0, Utils.getWindowWidth(), Utils.getWindowHeight());
    }

    public void end() {
        this.scissorEnd();
        for (Runnable task : this.postTasks) {
            task.run();
        }
        this.postTasks.clear();
        this.graphics.pose().popMatrix();
        this.graphics.nextStratum();
    }

    public void beginRender() {
        this.r.begin();
        this.rTex.begin();
    }

    public void endRender() {
        this.endRender(null);
    }

    public void endRender(Scissor scissor) {
        if (scissor != null) {
            scissor.push();
        }
        this.r.end();
        this.rTex.end();
        this.r.render();
        this.rTex.render("u_Texture", TEXTURE.getTextureView(), TEXTURE.getSampler());
        this.theme.textRenderer().begin(this.theme.scale(1.0));
        for (TextOperation text : this.texts) {
            if (text.title) continue;
            text.run(this.textPool);
        }
        this.theme.textRenderer().end();
        this.theme.textRenderer().begin(this.theme.scale(1.25));
        for (TextOperation text : this.texts) {
            if (!text.title) continue;
            text.run(this.textPool);
        }
        this.theme.textRenderer().end();
        this.texts.clear();
        if (scissor != null) {
            scissor.pop();
        }
    }

    public void scissorStart(double x, double y, double width, double height) {
        if (!this.scissorStack.isEmpty()) {
            Scissor parent = (Scissor)this.scissorStack.top();
            if (x < (double)parent.x) {
                x = parent.x;
            } else if (x + width > (double)(parent.x + parent.width)) {
                width -= x + width - (double)(parent.x + parent.width);
            }
            if (y < (double)parent.y) {
                y = parent.y;
            } else if (y + height > (double)(parent.y + parent.height)) {
                height -= y + height - (double)(parent.y + parent.height);
            }
            this.endRender(parent);
        }
        this.scissorStack.push((Object)this.scissorPool.get().set(x, y, width, height));
        this.graphics.enableScissor((int)x, (int)y, (int)(x + width), (int)(y + height));
        this.beginRender();
    }

    public void scissorEnd() {
        Scissor scissor = (Scissor)this.scissorStack.pop();
        this.endRender(scissor);
        scissor.push();
        for (Runnable task : scissor.postTasks) {
            task.run();
        }
        scissor.pop();
        this.graphics.disableScissor();
        if (!this.scissorStack.isEmpty()) {
            this.beginRender();
        }
        this.scissorPool.free(scissor);
    }

    public boolean renderTooltip(GuiGraphicsExtractor graphics, double mouseX, double mouseY, double delta) {
        this.tooltipAnimProgress += (double)(this.tooltip != null ? 1 : -1) * delta * 14.0;
        this.tooltipAnimProgress = Mth.clamp((double)this.tooltipAnimProgress, (double)0.0, (double)1.0);
        boolean toReturn = false;
        if (this.tooltipAnimProgress > 0.0) {
            if (this.tooltip != null && !this.tooltip.equals(this.lastTooltip)) {
                this.tooltipWidget = this.theme.tooltip(this.tooltip);
                this.tooltipWidget.init();
            }
            double deltaX = -this.tooltipWidget.x + mouseX + 12.0;
            double deltaY = -this.tooltipWidget.y + mouseY + 12.0;
            if (mouseX + 12.0 + this.tooltipWidget.width > (double)Utils.getWindowWidth()) {
                deltaX = -this.tooltipWidget.x + (double)Utils.getWindowWidth() - this.tooltipWidget.width;
            }
            if (mouseY + 12.0 + this.tooltipWidget.height > (double)Utils.getWindowHeight()) {
                deltaY = -this.tooltipWidget.y + (double)Utils.getWindowHeight() - this.tooltipWidget.height;
            }
            this.tooltipWidget.move(deltaX, deltaY);
            this.setAlpha(this.tooltipAnimProgress);
            this.begin(graphics);
            this.tooltipWidget.render(this, mouseX, mouseY, delta);
            this.end();
            this.setAlpha(1.0);
            this.lastTooltip = this.tooltip;
            toReturn = true;
        }
        this.tooltip = null;
        return toReturn;
    }

    public void setAlpha(double a) {
        this.r.setAlpha(a);
        this.rTex.setAlpha(a);
        this.theme.textRenderer().setAlpha(a);
    }

    public void tooltip(String text) {
        this.tooltip = text;
    }

    public void quad(double x, double y, double width, double height, Color cTopLeft, Color cTopRight, Color cBottomRight, Color cBottomLeft) {
        this.r.quad(x, y, width, height, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
    }

    public void quad(double x, double y, double width, double height, Color colorLeft, Color colorRight) {
        this.quad(x, y, width, height, colorLeft, colorRight, colorRight, colorLeft);
    }

    public void quad(double x, double y, double width, double height, Color color) {
        this.quad(x, y, width, height, color, color);
    }

    public void quad(WWidget widget, Color color) {
        this.quad(widget.x, widget.y, widget.width, widget.height, color);
    }

    public void quad(double x, double y, double width, double height, GuiTexture texture, Color color) {
        this.rTex.texQuad(x, y, width, height, texture.get(width, height), color);
    }

    public void rotatedQuad(double x, double y, double width, double height, double rotation, GuiTexture texture, Color color) {
        this.rTex.texQuad(x, y, width, height, rotation, texture.get(width, height), color);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        this.r.triangle(x1, y1, x2, y2, x3, y3, color);
    }

    public void text(String text, double x, double y, Color color, boolean title) {
        this.texts.add(this.getOp(this.textPool, x, y, color).set(text, this.theme.textRenderer(), title));
    }

    public void texture(double x, double y, double width, double height, double rotation, Texture texture) {
        this.post(() -> {
            this.rTex.begin();
            this.rTex.texQuad(x, y, width, height, rotation, 0.0, 0.0, 1.0, 1.0, WHITE);
            this.rTex.end();
            this.rTex.render(texture.getTextureView(), texture.getSampler());
        });
    }

    public void post(Runnable task) {
        ((Scissor)this.scissorStack.top()).postTasks.add(task);
    }

    public void item(ItemStack itemStack, int x, int y, float scale, boolean overlay) {
        RenderUtils.drawItem(this.graphics, itemStack, x, y, scale, overlay, null, false);
    }

    public void absolutePost(Runnable task) {
        this.postTasks.add(task);
    }

    private <T extends GuiRenderOperation<T>> T getOp(Pool<T> pool, double x, double y, Color color) {
        GuiRenderOperation op = (GuiRenderOperation)pool.get();
        op.set(x, y, color);
        return (T)op;
    }
}

