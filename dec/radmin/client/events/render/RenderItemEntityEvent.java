/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.state.ItemEntityRenderState
 *  net.minecraft.client.renderer.item.ItemModelResolver
 *  net.minecraft.world.entity.item.ItemEntity
 *  org.jspecify.annotations.Nullable
 */
package radmin.client.events.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.item.ItemEntity;
import org.jspecify.annotations.Nullable;
import radmin.client.events.Cancellable;
import radmin.client.mixininterface.IEntityRenderState;

public class RenderItemEntityEvent
extends Cancellable {
    private static final RenderItemEntityEvent INSTANCE = new RenderItemEntityEvent();
    public @Nullable ItemEntity itemEntity;
    public ItemEntityRenderState renderState;
    public float tickDelta;
    public PoseStack matrixStack;
    public MultiBufferSource vertexConsumerProvider;
    public int light;
    public ItemModelResolver itemModelManager;
    public SubmitNodeCollector renderCommandQueue;

    public static RenderItemEntityEvent get(ItemEntityRenderState renderState, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, ItemModelResolver itemModelManager, SubmitNodeCollector renderCommandQueue) {
        INSTANCE.setCancelled(false);
        RenderItemEntityEvent.INSTANCE.itemEntity = (ItemEntity)((IEntityRenderState)renderState).meteor$getEntity();
        RenderItemEntityEvent.INSTANCE.renderState = renderState;
        RenderItemEntityEvent.INSTANCE.tickDelta = tickDelta;
        RenderItemEntityEvent.INSTANCE.matrixStack = matrixStack;
        RenderItemEntityEvent.INSTANCE.vertexConsumerProvider = vertexConsumerProvider;
        RenderItemEntityEvent.INSTANCE.light = light;
        RenderItemEntityEvent.INSTANCE.itemModelManager = itemModelManager;
        RenderItemEntityEvent.INSTANCE.renderCommandQueue = renderCommandQueue;
        return INSTANCE;
    }
}

