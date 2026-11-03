/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.BufferUtils
 */
package radmin.client.gui.screens;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import radmin.client.MeteorClient;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.renderer.MeshBuilder;
import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.renderer.text.Font;
import radmin.client.utils.files.ByteBufferUtils;
import radmin.client.utils.render.FontUtils;
import radmin.client.utils.render.color.Color;

public class FontAwesomeIcons {
    public static final int COMBAT = 61531;
    public static final int PLAYER = 61827;
    public static final int MOVEMENT = 63244;
    public static final int RENDER = 61502;
    public static final int WORLD = 61612;
    public static final int MISC = 63007;
    public static final int FALLBACK = 61842;
    public static final int FAVORITES = 61445;
    public static final int SETTINGS = 61459;
    public static final int GUI = 61948;
    public static final int CONFIGS = 62566;
    public static final int FRIENDS = 61632;
    public static final int SEARCH = 61442;
    public static final int TIMES = 61453;
    public static final int CHECK = 61452;
    public static final int PLUS = 61543;
    public static final int MINUS = 61544;
    public static final int CHEVRON_DOWN = 61560;
    public static final int CHEVRON_LEFT = 61523;
    public static final int COPY = 61637;
    public static final int PASTE = 61674;
    public static final int RESET = 61666;
    public static final int EDIT = 61508;
    private static final int[] GLYPHS = new int[]{61531, 61827, 63244, 61502, 61612, 63007, 61842, 61445, 61459, 61948, 62566, 61632, 61442, 61453, 61452, 61543, 61544, 61560, 61523, 61637, 61674, 61666, 61508};
    private static final MeshBuilder mesh = new MeshBuilder(MeteorRenderPipelines.UI_TEXT);
    private static Font font;

    private FontAwesomeIcons() {
    }

    public static int iconForCategory(String name) {
        return switch (name) {
            case "Combat" -> 61531;
            case "Player" -> 61827;
            case "Movement" -> 63244;
            case "Render" -> 61502;
            case "World" -> 61612;
            case "Misc" -> 63007;
            default -> 61842;
        };
    }

    public static void render(GuiRenderer renderer, double cx, double cy, double size, Color color, int codepoint) {
        renderer.post(() -> {
            if (font == null) {
                FontAwesomeIcons.load();
            }
            mesh.begin();
            font.renderCentered(mesh, codepoint, cx, cy, color, size / (double)font.getHeight());
            mesh.end();
            MeshRenderer.begin().attachments(Minecraft.getInstance().getMainRenderTarget()).pipeline(MeteorRenderPipelines.UI_TEXT).mesh(mesh).sampler("u_Texture", FontAwesomeIcons.font.texture.getTextureView(), FontAwesomeIcons.font.texture.getSampler()).end();
        });
    }

    private static void load() {
        try {
            int[][] ranges = new int[GLYPHS.length][];
            for (int i = 0; i < GLYPHS.length; ++i) {
                ranges[i] = new int[]{GLYPHS[i], 1};
            }
            try (InputStream stream = FontUtils.builtinFontStream("fa-solid-900");
                 ReadableByteChannel channel = Channels.newChannel(stream);){
                ByteBuffer buffer = ByteBufferUtils.readFully(channel, BufferUtils::createByteBuffer);
                font = new Font(buffer, 32, ranges);
            }
        }
        catch (Exception e) {
            MeteorClient.LOG.error("Failed to load Font Awesome font", (Throwable)e);
        }
    }
}

