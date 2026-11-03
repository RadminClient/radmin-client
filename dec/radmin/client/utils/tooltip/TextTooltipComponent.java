/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.FormattedCharSequence
 */
package radmin.client.utils.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import radmin.client.utils.tooltip.MeteorTooltipData;

public class TextTooltipComponent
extends ClientTextTooltip
implements MeteorTooltipData {
    public TextTooltipComponent(FormattedCharSequence text) {
        super(text);
    }

    public TextTooltipComponent(Component text) {
        this(text.getVisualOrderText());
    }

    public ClientTextTooltip getComponent() {
        return this;
    }
}

