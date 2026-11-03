/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.utils.render.postprocess;

import net.minecraft.world.entity.Entity;
import radmin.client.renderer.MeshRenderer;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.ESP;
import radmin.client.utils.render.postprocess.EntityShader;
import radmin.client.utils.render.postprocess.OutlineUniforms;

public class EntityOutlineShader
extends EntityShader {
    private static ESP esp;

    public EntityOutlineShader() {
        super(MeteorRenderPipelines.POST_OUTLINE);
    }

    @Override
    protected boolean shouldDraw() {
        if (esp == null) {
            esp = Modules.get().get(ESP.class);
        }
        return esp.isShader();
    }

    @Override
    public boolean shouldDraw(Entity entity) {
        if (!this.shouldDraw()) {
            return false;
        }
        return !esp.shouldSkip(entity);
    }

    @Override
    protected void setupPass(MeshRenderer renderer) {
        renderer.uniform("OutlineData", OutlineUniforms.write(EntityOutlineShader.esp.outlineWidth.get(), EntityOutlineShader.esp.fillOpacity.get().floatValue(), EntityOutlineShader.esp.shapeMode.get().ordinal(), EntityOutlineShader.esp.glowMultiplier.get().floatValue()));
    }
}

