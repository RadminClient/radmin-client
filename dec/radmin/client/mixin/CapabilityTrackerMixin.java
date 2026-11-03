/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.opengl.GlStateManager$BooleanState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.ICapabilityTracker;

@Mixin(value={GlStateManager.BooleanState.class})
public abstract class CapabilityTrackerMixin
implements ICapabilityTracker {
    @Shadow
    private boolean enabled;

    @Shadow
    public abstract void setEnabled(boolean var1);

    @Override
    public boolean meteor$get() {
        return this.enabled;
    }

    @Override
    public void meteor$set(boolean state) {
        this.setEnabled(state);
    }
}

