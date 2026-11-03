/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.OutlineBufferSource
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.SubmitNodeStorage
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModelPart
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
 *  net.minecraft.client.renderer.feature.FeatureRenderDispatcher
 *  net.minecraft.client.renderer.rendertype.RenderTypes
 *  net.minecraft.client.resources.model.geometry.BakedQuad
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3fc
 */
package radmin.client.utils.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import radmin.client.MeteorClient;
import radmin.client.utils.render.IVertexConsumerProvider;
import radmin.client.utils.render.NoopImmediateVertexConsumerProvider;
import radmin.client.utils.render.NoopOutlineVertexConsumerProvider;
import radmin.client.utils.render.WrapperImmediateVertexConsumerProvider;

public abstract class SimpleBlockRenderer {
    private static final PoseStack MATRICES = new PoseStack();
    private static final List<BlockStateModelPart> PARTS = new ArrayList<BlockStateModelPart>();
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final RandomSource RANDOM = RandomSource.create();
    private static final SubmitNodeStorage renderCommandQueue = new SubmitNodeStorage();
    private static MultiBufferSource provider;
    private static final FeatureRenderDispatcher renderDispatcher;

    private SimpleBlockRenderer() {
    }

    public static void renderWithBlockEntity(BlockEntity blockEntity, float tickDelta, IVertexConsumerProvider vertexConsumerProvider) {
        vertexConsumerProvider.setOffset(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ());
        SimpleBlockRenderer.render(blockEntity.getBlockPos(), blockEntity.getBlockState(), vertexConsumerProvider);
        BlockEntityRenderer renderer = MeteorClient.mc.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
        if (renderer != null && blockEntity.hasLevel() && blockEntity.getType().isValid(blockEntity.getBlockState())) {
            provider = vertexConsumerProvider;
            BlockEntityRenderState state = renderer.createRenderState();
            renderer.extractRenderState(blockEntity, state, tickDelta, MeteorClient.mc.gameRenderer.getMainCamera().position(), null);
            renderer.submit(state, MATRICES, (SubmitNodeCollector)renderCommandQueue, MeteorClient.mc.gameRenderer.getGameRenderState().levelRenderState.cameraRenderState);
            renderDispatcher.renderAllFeatures();
            renderCommandQueue.endFrame();
            provider = null;
        }
        vertexConsumerProvider.setOffset(0, 0, 0);
    }

    public static void render(BlockPos pos, BlockState state, MultiBufferSource consumerProvider) {
        if (state.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        VertexConsumer consumer = consumerProvider.getBuffer(RenderTypes.solidMovingBlock());
        BlockStateModel model = MeteorClient.mc.getModelManager().getBlockStateModelSet().get(state);
        model.collectParts(RANDOM, PARTS);
        Vec3 offset = state.getOffset(pos);
        float offsetX = (float)offset.x;
        float offsetY = (float)offset.y;
        float offsetZ = (float)offset.z;
        for (BlockStateModelPart part : PARTS) {
            for (Direction direction : DIRECTIONS) {
                List quads = part.getQuads(direction);
                if (quads.isEmpty()) continue;
                SimpleBlockRenderer.renderQuads(quads, offsetX, offsetY, offsetZ, consumer);
            }
            List quads = part.getQuads(null);
            if (quads.isEmpty()) continue;
            SimpleBlockRenderer.renderQuads(quads, offsetX, offsetY, offsetZ, consumer);
        }
        PARTS.clear();
    }

    private static void renderQuads(List<BakedQuad> quads, float offsetX, float offsetY, float offsetZ, VertexConsumer consumer) {
        for (BakedQuad quad : quads) {
            for (int j = 0; j < 4; ++j) {
                Vector3fc vec = quad.position(j);
                consumer.addVertex(offsetX + vec.x(), offsetY + vec.y(), offsetZ + vec.z());
            }
        }
    }

    static {
        renderDispatcher = new FeatureRenderDispatcher(renderCommandQueue, MeteorClient.mc.getModelManager(), (MultiBufferSource.BufferSource)new WrapperImmediateVertexConsumerProvider(() -> provider), MeteorClient.mc.getAtlasManager(), (OutlineBufferSource)NoopOutlineVertexConsumerProvider.INSTANCE, (MultiBufferSource.BufferSource)NoopImmediateVertexConsumerProvider.INSTANCE, MeteorClient.mc.font, MeteorClient.mc.gameRenderer.getGameRenderState());
    }
}

