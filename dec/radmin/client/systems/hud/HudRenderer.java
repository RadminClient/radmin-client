/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectIterator
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.renderer.entity.state.EntityRenderState
 *  net.minecraft.client.renderer.entity.state.LivingEntityRenderState
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 */
package radmin.client.systems.hud;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.CustomFontChangedEvent;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.renderer.Fonts;
import radmin.client.renderer.MeshBuilder;
import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.renderer.Renderer2D;
import radmin.client.renderer.Texture;
import radmin.client.renderer.text.CustomTextRenderer;
import radmin.client.renderer.text.Font;
import radmin.client.renderer.text.VanillaTextRenderer;
import radmin.client.systems.hud.Hud;
import radmin.client.utils.Utils;
import radmin.client.utils.render.RenderUtils;
import radmin.client.utils.render.color.Color;

public class HudRenderer {
    public static final HudRenderer INSTANCE = new HudRenderer();
    private static final double SCALE_TO_HEIGHT = 0.05555555555555555;
    private final Hud hud = Hud.get();
    private final List<Runnable> postTasks = new ArrayList<Runnable>();
    private final Int2ObjectMap<FontHolder> fontsInUse = new Int2ObjectOpenHashMap();
    private final LoadingCache<Integer, FontHolder> fontCache = CacheBuilder.newBuilder().maximumSize(4L).expireAfterAccess(Duration.ofMinutes(10L)).removalListener(notification -> {
        if (notification.wasEvicted()) {
            ((FontHolder)notification.getValue()).destroy();
        }
    }).build(CacheLoader.from(HudRenderer::loadFont));
    public GuiGraphicsExtractor graphics;
    public double delta;

    private HudRenderer() {
        MeteorClient.EVENT_BUS.subscribe((Object)this);
    }

    public void begin(GuiGraphicsExtractor graphics) {
        Renderer2D.COLOR.begin();
        this.graphics = graphics;
        this.delta = Utils.frameTime;
        graphics.nextStratum();
        if (!this.hud.hasCustomFont()) {
            VanillaTextRenderer.INSTANCE.scaleIndividually = true;
            VanillaTextRenderer.INSTANCE.begin();
        }
    }

    public void end() {
        Renderer2D.COLOR.render();
        if (this.hud.hasCustomFont()) {
            ObjectIterator it = this.fontsInUse.values().iterator();
            while (it.hasNext()) {
                FontHolder fontHolder = (FontHolder)it.next();
                if (fontHolder.visited) {
                    MeshRenderer.begin().attachments(MeteorClient.mc.getMainRenderTarget()).pipeline(MeteorRenderPipelines.UI_TEXT).mesh(fontHolder.getMesh()).sampler("u_Texture", fontHolder.font.texture.getTextureView(), fontHolder.font.texture.getSampler()).end();
                } else {
                    it.remove();
                    this.fontCache.put((Object)fontHolder.font.getHeight(), (Object)fontHolder);
                }
                fontHolder.visited = false;
            }
        } else {
            VanillaTextRenderer.INSTANCE.end();
            VanillaTextRenderer.INSTANCE.scaleIndividually = false;
        }
        for (Runnable task : this.postTasks) {
            task.run();
        }
        this.postTasks.clear();
        this.graphics.nextStratum();
        this.graphics = null;
    }

    public void line(double x1, double y1, double x2, double y2, Color color) {
        Renderer2D.COLOR.line(x1, y1, x2, y2, color);
    }

    public void boxLines(double x, double y, double width, double height, Color color) {
        Renderer2D.COLOR.boxLines(x, y, width, height, color);
    }

    public void enableScissor(double x, double y, double width, double height) {
        this.graphics.enableScissor((int)x, (int)y, (int)(x + width), (int)(y + height));
    }

    public void disableScissor() {
        this.graphics.disableScissor();
    }

    public void quad(double x, double y, double width, double height, Color color) {
        Renderer2D.COLOR.quad(x, y, width, height, color);
    }

    public void quad(double x, double y, double width, double height, Color cTopLeft, Color cTopRight, Color cBottomRight, Color cBottomLeft) {
        Renderer2D.COLOR.quad(x, y, width, height, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
    }

    public void quad(double x, double y, double width, double height, Color cTop, Color cBottom) {
        Renderer2D.COLOR.quad(x, y, width, height, cTop, cTop, cBottom, cBottom);
    }

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        Renderer2D.COLOR.triangle(x1, y1, x2, y2, x3, y3, color);
    }

    public void texture(Identifier id, double x, double y, double width, double height, Color color) {
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, width, height, color);
        Renderer2D.TEXTURE.render(MeteorClient.mc.getTextureManager().getTexture(id).getTextureView(), MeteorClient.mc.getTextureManager().getTexture(id).getSampler());
    }

    public void texture(Texture texture, double x, double y, double width, double height, Color color) {
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, width, height, color);
        Renderer2D.TEXTURE.render(texture.getTextureView(), texture.getSampler());
    }

    public void texture(Texture texture, double x, double y, double width, double height, double rotation, Color color) {
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, width, height, rotation, 0.0, 0.0, 1.0, 1.0, color);
        Renderer2D.TEXTURE.render(texture.getTextureView(), texture.getSampler());
    }

    public void rounded(double x, double y, double width, double height, double rTL, double rTR, double rBR, double rBL, Color color) {
        if (width <= 0.0 || height <= 0.0) {
            return;
        }
        this.quad(x + rTL, y, width - rTL - rTR, height, color);
        this.quad(x, y + rTL, rTL, height - rTL - rBL, color);
        this.quad(x + width - rTR, y + rTR, rTR, height - rTR - rBR, color);
        this.drawCorners(x, y, width, height, rTL, rTR, rBR, rBL, color, color, color, color);
    }

    public void rounded(double x, double y, double width, double height, double radius, Color color) {
        this.rounded(x, y, width, height, radius, radius, radius, radius, color);
    }

    public void roundedGradient(double x, double y, double width, double height, double rTL, double rTR, double rBR, double rBL, Color cTopLeft, Color cTopRight, Color cBottomRight, Color cBottomLeft) {
        if (width <= 0.0 || height <= 0.0) {
            return;
        }
        this.quad(x + rTL, y, width - rTL - rTR, height, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
        this.quad(x, y + rTL, rTL, height - rTL - rBL, cTopLeft, cBottomLeft);
        this.quad(x + width - rTR, y + rTR, rTR, height - rTR - rBR, cTopRight, cBottomRight);
        this.drawCorners(x, y, width, height, rTL, rTR, rBR, rBL, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
    }

    public void roundedGradient(double x, double y, double width, double height, double radius, Color top, Color bottom) {
        this.roundedGradient(x, y, width, height, radius, radius, radius, radius, top, top, bottom, bottom);
    }

    public void roundedGradient(double x, double y, double width, double height, double radius, Color cTopLeft, Color cTopRight, Color cBottomRight, Color cBottomLeft) {
        this.roundedGradient(x, y, width, height, radius, radius, radius, radius, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
    }

    private void drawCorners(double x, double y, double width, double height, double rTL, double rTR, double rBR, double rBL, Color cTL, Color cTR, Color cBR, Color cBL) {
        Renderer2D.TEXTURE.begin();
        if (rTL > 0.0) {
            this.drawCorner(x, y, rTL, width, height, cTL);
        }
        if (rTR > 0.0) {
            this.drawCorner(x + width - rTR * 2.0, y, rTR, width, height, cTR);
        }
        if (rBR > 0.0) {
            this.drawCorner(x + width - rBR * 2.0, y + height - rBR * 2.0, rBR, width, height, cBR);
        }
        if (rBL > 0.0) {
            this.drawCorner(x, y + height - rBL * 2.0, rBL, width, height, cBL);
        }
        Renderer2D.TEXTURE.render(GuiRenderer.TEXTURE.getTextureView(), GuiRenderer.TEXTURE.getSampler());
    }

    private void drawCorner(double x, double y, double r, double width, double height, Color color) {
        GuiTexture texture = r >= width / 2.0 && r >= height / 2.0 ? GuiRenderer.CIRCLE : GuiRenderer.CIRCLE_SOFT;
        Renderer2D.TEXTURE.texQuad(x, y, r * 2.0, r * 2.0, texture.get(r * 2.0, r * 2.0), color);
    }

    public double text(String text, double x, double y, Color color, boolean shadow, double scale) {
        double width;
        if (scale == -1.0) {
            scale = this.hud.getTextScale();
        }
        if (!this.hud.hasCustomFont()) {
            VanillaTextRenderer.INSTANCE.scale = scale * 2.0;
            return VanillaTextRenderer.INSTANCE.render(text, x, y, color, shadow);
        }
        FontHolder fontHolder = this.getFontHolder(scale, true);
        Font font = fontHolder.font;
        MeshBuilder mesh = fontHolder.getMesh();
        if (shadow) {
            int preShadowA = CustomTextRenderer.SHADOW_COLOR.a;
            CustomTextRenderer.SHADOW_COLOR.a = (int)((double)color.a / 255.0 * (double)preShadowA);
            width = font.render(mesh, text, x + 1.0, y + 1.0, CustomTextRenderer.SHADOW_COLOR, scale);
            font.render(mesh, text, x, y, color, scale);
            CustomTextRenderer.SHADOW_COLOR.a = preShadowA;
        } else {
            width = font.render(mesh, text, x, y, color, scale);
        }
        return width;
    }

    public double text(String text, double x, double y, Color color, boolean shadow) {
        return this.text(text, x, y, color, shadow, -1.0);
    }

    public double textWidth(String text, boolean shadow, double scale) {
        if (text.isEmpty()) {
            return 0.0;
        }
        if (this.hud.hasCustomFont()) {
            double width = this.getFont(scale).getWidth(text, text.length());
            return (width + (double)(shadow ? 1 : 0)) * (scale == -1.0 ? this.hud.getTextScale() : scale) + (double)(shadow ? 1 : 0);
        }
        VanillaTextRenderer.INSTANCE.scale = (scale == -1.0 ? this.hud.getTextScale() : scale) * 2.0;
        return VanillaTextRenderer.INSTANCE.getWidth(text, shadow);
    }

    public double textWidth(String text, boolean shadow) {
        return this.textWidth(text, shadow, -1.0);
    }

    public double textWidth(String text, double scale) {
        return this.textWidth(text, false, scale);
    }

    public double textWidth(String text) {
        return this.textWidth(text, false, -1.0);
    }

    public double textHeight(boolean shadow, double scale) {
        if (this.hud.hasCustomFont()) {
            double height = this.getFont(scale).getHeight() + 1;
            return (height + (double)(shadow ? 1 : 0)) * (scale == -1.0 ? this.hud.getTextScale() : scale);
        }
        VanillaTextRenderer.INSTANCE.scale = (scale == -1.0 ? this.hud.getTextScale() : scale) * 2.0;
        return VanillaTextRenderer.INSTANCE.getHeight(shadow);
    }

    public double textHeight(boolean shadow) {
        return this.textHeight(shadow, -1.0);
    }

    public double textHeight() {
        return this.textHeight(false, -1.0);
    }

    public void post(Runnable task) {
        this.postTasks.add(task);
    }

    public void item(ItemStack itemStack, int x, int y, float scale, boolean overlay, String countOverlay) {
        RenderUtils.drawItem(this.graphics, itemStack, x, y, scale, overlay, countOverlay, true);
    }

    public void item(ItemStack itemStack, int x, int y, float scale, boolean overlay) {
        RenderUtils.drawItem(this.graphics, itemStack, x, y, scale, overlay);
    }

    public void entity(LivingEntity entity, int x, int y, int width, int height, float yaw, float pitch) {
        float previousBodyYaw = entity.yBodyRot;
        float previousYaw = entity.getYRot();
        float previousPitch = entity.getXRot();
        float lastLastHeadYaw = entity.yHeadRotO;
        float lastHeadYaw = entity.yHeadRot;
        float tanYaw = (float)Math.atan(yaw / 40.0f);
        float tanPitch = (float)Math.atan(pitch / 40.0f);
        entity.yBodyRot = 180.0f + tanYaw * 20.0f;
        entity.setYRot(180.0f + tanYaw * 40.0f);
        entity.setXRot(-tanPitch * 20.0f);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        LivingEntityRenderState state = (LivingEntityRenderState)MeteorClient.mc.getEntityRenderDispatcher().getRenderer((Entity)entity).createRenderState((Entity)entity, 1.0f);
        entity.yBodyRot = previousBodyYaw;
        entity.setYRot(previousYaw);
        entity.setXRot(previousPitch);
        entity.yHeadRot = lastLastHeadYaw;
        entity.yHeadRotO = lastHeadYaw;
        float s = 1.0f / (float)MeteorClient.mc.getWindow().getGuiScale();
        int x1 = (int)((float)x * s);
        int y1 = (int)((float)y * s);
        int x2 = (int)((float)(x + width) * s);
        int y2 = (int)((float)(y + height) * s);
        float scale = (float)Math.max(width, height) * s / 2.0f;
        Vector3f translation = new Vector3f(0.0f, 1.0f, 0.0f);
        Quaternionf rotation = new Quaternionf().rotateZ((float)Math.PI);
        this.graphics.entity((EntityRenderState)state, scale, translation, rotation, null, x1, y1, x2, y2);
    }

    private FontHolder getFontHolder(double scale, boolean render) {
        int height;
        FontHolder fontHolder;
        if (scale == -1.0) {
            scale = this.hud.getTextScale();
        }
        if ((fontHolder = (FontHolder)this.fontsInUse.get(height = (int)Math.round(scale / 0.05555555555555555))) != null) {
            if (render) {
                fontHolder.visited = true;
            }
            return fontHolder;
        }
        if (render) {
            fontHolder = (FontHolder)this.fontCache.getIfPresent((Object)height);
            if (fontHolder == null) {
                fontHolder = HudRenderer.loadFont(height);
            } else {
                this.fontCache.invalidate((Object)height);
            }
            this.fontsInUse.put(height, (Object)fontHolder);
            fontHolder.visited = true;
            return fontHolder;
        }
        return (FontHolder)this.fontCache.getUnchecked((Object)height);
    }

    private Font getFont(double scale) {
        return this.getFontHolder((double)scale, (boolean)false).font;
    }

    @EventHandler
    private void onCustomFontChanged(CustomFontChangedEvent event) {
        for (FontHolder fontHolder : this.fontsInUse.values()) {
            fontHolder.destroy();
        }
        for (FontHolder fontHolder : this.fontCache.asMap().values()) {
            fontHolder.destroy();
        }
        this.fontsInUse.clear();
        this.fontCache.invalidateAll();
    }

    private static FontHolder loadFont(int height) {
        try {
            ByteBuffer buffer = Fonts.RENDERER.fontFace.readToDirectByteBuffer();
            return new FontHolder(new Font(buffer, height));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load font: " + String.valueOf(Fonts.RENDERER.fontFace), e);
        }
    }

    private static class FontHolder {
        public final Font font;
        public boolean visited;
        private MeshBuilder mesh;

        public FontHolder(Font font) {
            this.font = font;
        }

        public MeshBuilder getMesh() {
            if (this.mesh == null) {
                this.mesh = new MeshBuilder(MeteorRenderPipelines.UI_TEXT);
            }
            if (!this.mesh.isBuilding()) {
                this.mesh.begin();
            }
            return this.mesh;
        }

        public void destroy() {
            this.font.texture.close();
        }
    }
}

