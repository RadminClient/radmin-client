/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.FireworkRocketEntity
 *  org.joml.Vector3d
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.joml.Vector3d;
import radmin.client.events.render.Render2DEvent;
import radmin.client.renderer.Renderer2D;
import radmin.client.renderer.text.TextRenderer;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.Utils;
import radmin.client.utils.render.NametagUtils;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;

public class FireworkESP
extends Module {
    private static final Color BACKGROUND = new Color(0, 0, 0, 100);
    private final SettingGroup sgGeneral;
    private final Setting<Double> scale;
    private final Setting<Double> range;
    private final Vector3d pos;

    public FireworkESP() {
        super(Categories.Render, "firework-esp", "Displays a label on firework rockets.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.scale = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("scale")).description("The scale of the label.")).defaultValue(1.0).min(0.0).sliderRange(0.5, 2.0).build());
        this.range = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("range")).description("Maximum range to render firework labels.")).defaultValue(64.0).min(1.0).sliderRange(1.0, 256.0).build());
        this.pos = new Vector3d();
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (this.mc.player == null || this.mc.level == null) {
            return;
        }
        double r = this.range.get();
        for (Entity entity : this.mc.level.entitiesForRendering()) {
            FireworkRocketEntity firework;
            if (!(entity instanceof FireworkRocketEntity) || (firework = (FireworkRocketEntity)entity).isRemoved() || firework.distanceToSqr((Entity)this.mc.player) > r * r) continue;
            Utils.set(this.pos, (Entity)firework, event.tickDelta);
            this.pos.add(0.0, (double)firework.getBbHeight() + 0.75, 0.0);
            if (!NametagUtils.to2D(this.pos, this.scale.get())) continue;
            this.renderLabel(firework);
        }
    }

    private void renderLabel(FireworkRocketEntity firework) {
        TextRenderer text = TextRenderer.get();
        NametagUtils.begin(this.pos);
        text.beginBig();
        String label = "Firework";
        double w = text.getWidth(label);
        double x = -w / 2.0;
        double y = -text.getHeight();
        Color accent = ThemeAccent.get();
        Color accentDim = ThemeAccent.get(0.4);
        Renderer2D.COLOR.begin();
        Renderer2D.COLOR.quad(x - 2.0, y - 2.0, w + 4.0, text.getHeight() + 4.0, BACKGROUND);
        Renderer2D.COLOR.quad(x - 2.0, y - 2.0, w + 4.0, 1.5, accentDim);
        Renderer2D.COLOR.render();
        text.render(label, x, y, accent);
        text.end();
        NametagUtils.end();
    }
}

