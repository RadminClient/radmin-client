/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.GpuDevice
 *  com.mojang.blaze3d.systems.GpuDeviceBackend
 *  com.mojang.blaze3d.systems.RenderPassBackend
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.GpuDeviceBackend;
import com.mojang.blaze3d.systems.RenderPassBackend;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.IGpuDevice;

@Mixin(value={GpuDevice.class})
public abstract class GpuDeviceMixin
implements IGpuDevice {
    @Shadow
    @Final
    private GpuDeviceBackend backend;

    @Override
    public void meteor$pushScissor(int x, int y, int width, int height) {
        ((IGpuDevice)this.backend).meteor$pushScissor(x, y, width, height);
    }

    @Override
    public void meteor$popScissor() {
        ((IGpuDevice)this.backend).meteor$popScissor();
    }

    @Override
    public void meteor$onCreateRenderPass(RenderPassBackend backend) {
        ((IGpuDevice)this.backend).meteor$onCreateRenderPass(backend);
    }
}

