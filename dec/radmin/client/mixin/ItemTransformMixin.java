/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.resources.model.cuboid.ItemTransform
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.render.ApplyTransformationEvent;

@Mixin(value={ItemTransform.class})
public abstract class ItemTransformMixin {
    @Inject(method={"apply"}, at={@At(value="HEAD")}, cancellable=true)
    private void onApply(boolean applyLeftHandFix, PoseStack.Pose pose, CallbackInfo ci) {
        ApplyTransformationEvent event = (ApplyTransformationEvent)MeteorClient.EVENT_BUS.post((ICancellable)ApplyTransformationEvent.get((ItemTransform)this, applyLeftHandFix));
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

