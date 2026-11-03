/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.utils.render.postprocess;

import meteordevelopment.orbit.EventHandler;
import radmin.client.MeteorClient;
import radmin.client.events.render.Render2DEvent;
import radmin.client.utils.PreInit;
import radmin.client.utils.render.postprocess.ChamsShader;
import radmin.client.utils.render.postprocess.EntityOutlineShader;
import radmin.client.utils.render.postprocess.EntityShader;
import radmin.client.utils.render.postprocess.PostProcessShader;
import radmin.client.utils.render.postprocess.StorageOutlineShader;

public class PostProcessShaders {
    public static EntityShader CHAMS;
    public static EntityShader ENTITY_OUTLINE;
    public static PostProcessShader STORAGE_OUTLINE;

    private PostProcessShaders() {
    }

    @PreInit
    public static void init() {
        CHAMS = new ChamsShader();
        ENTITY_OUTLINE = new EntityOutlineShader();
        STORAGE_OUTLINE = new StorageOutlineShader();
        MeteorClient.EVENT_BUS.subscribe(PostProcessShaders.class);
    }

    public static void beginRender() {
        CHAMS.clearTexture();
        ENTITY_OUTLINE.clearTexture();
        STORAGE_OUTLINE.clearTexture();
    }

    public static void submitEntityVertices() {
        CHAMS.submitVertices();
        ENTITY_OUTLINE.submitVertices();
    }

    @EventHandler
    private static void onRender(Render2DEvent event) {
        CHAMS.render();
        ENTITY_OUTLINE.render();
    }

    public static void onResized(int width, int height) {
        if (MeteorClient.mc == null) {
            return;
        }
        CHAMS.onResized(width, height);
        ENTITY_OUTLINE.onResized(width, height);
        STORAGE_OUTLINE.onResized(width, height);
    }
}

