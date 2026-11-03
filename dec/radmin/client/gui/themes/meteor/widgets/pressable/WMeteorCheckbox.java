/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import net.minecraft.util.Mth;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.pressable.WCheckbox;

public class WMeteorCheckbox
extends WCheckbox
implements MeteorWidget {
    private double animProgress;

    public WMeteorCheckbox(boolean checked) {
        super(checked);
        this.animProgress = checked ? 1.0 : 0.0;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = this.theme();
        this.animProgress += (double)(this.checked ? 1 : -1) * delta * 14.0;
        this.animProgress = Mth.clamp((double)this.animProgress, (double)0.0, (double)1.0);
        this.renderBackground(renderer, (WWidget)this, this.pressed, this.mouseOver);
        if (this.animProgress > 0.0) {
            double cs = (this.width - theme.scale(2.0)) / 1.75 * this.animProgress;
            renderer.quad(this.x + (this.width - cs) / 2.0, this.y + (this.height - cs) / 2.0, cs, cs, theme.checkboxColor.get());
        }
    }
}

