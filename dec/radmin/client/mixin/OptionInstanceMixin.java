/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.OptionInstance
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.IOptionInstance;

@Mixin(value={OptionInstance.class})
public abstract class OptionInstanceMixin
implements IOptionInstance {
    @Shadow
    private Object value;
    @Shadow
    @Final
    private Consumer<Object> onValueUpdate;

    @Override
    public void meteor$set(Object value) {
        if (!Minecraft.getInstance().isRunning()) {
            this.value = value;
        } else if (!Objects.equals(this.value, value)) {
            this.value = value;
            this.onValueUpdate.accept(this.value);
        }
    }
}

