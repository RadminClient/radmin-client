/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.render.postprocess;

import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.StorageESP;
import radmin.client.utils.render.postprocess.OutlineUniforms;
import radmin.client.utils.render.postprocess.PostProcessShader;

public class StorageOutlineShader
extends PostProcessShader {
    private static StorageESP storageESP;

    public StorageOutlineShader() {
        super(MeteorRenderPipelines.POST_OUTLINE);
    }

    @Override
    protected boolean shouldDraw() {
        if (storageESP == null) {
            storageESP = Modules.get().get(StorageESP.class);
        }
        return storageESP.isShader();
    }

    @Override
    protected void setupPass(MeshRenderer renderer) {
        renderer.uniform("OutlineData", OutlineUniforms.write(StorageOutlineShader.storageESP.outlineWidth.get(), (float)StorageOutlineShader.storageESP.fillOpacity.get().intValue() / 255.0f, StorageOutlineShader.storageESP.shapeMode.get().ordinal(), StorageOutlineShader.storageESP.glowMultiplier.get().floatValue()));
    }
}

