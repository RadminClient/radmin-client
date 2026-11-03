/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.hud.elements;

import java.util.List;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ColorSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.ModuleListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.hud.Alignment;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.HudElement;
import radmin.client.systems.hud.HudElementInfo;
import radmin.client.systems.hud.HudRenderer;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.combat.AnchorAura;
import radmin.client.systems.modules.combat.BedAura;
import radmin.client.systems.modules.combat.CrystalAura;
import radmin.client.systems.modules.combat.KillAura;
import radmin.client.systems.modules.combat.Surround;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;

public class ModuleInfosHud
extends HudElement {
    public static final HudElementInfo<ModuleInfosHud> INFO = new HudElementInfo<ModuleInfosHud>(Hud.GROUP, "module-infos", "Displays if selected modules are enabled or disabled.", ModuleInfosHud::new);
    private final SettingGroup sgGeneral;
    private final Setting<List<Module>> modules;
    private final Setting<Boolean> additionalInfo;
    private final Setting<Boolean> textShadow;
    private final Setting<SettingColor> moduleColor;
    private final Setting<SettingColor> onColor;
    private final Setting<SettingColor> offColor;
    private final Setting<Alignment> alignment;

    public ModuleInfosHud() {
        super(INFO);
        this.sgGeneral = this.settings.getDefaultGroup();
        this.modules = this.sgGeneral.add(((ModuleListSetting.Builder)((ModuleListSetting.Builder)new ModuleListSetting.Builder().name("modules")).description("Which modules to display")).defaultValue(KillAura.class, CrystalAura.class, AnchorAura.class, BedAura.class, Surround.class).build());
        this.additionalInfo = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("additional-info")).description("Shows additional info from the module next to the name in the module info list.")).defaultValue(true)).build());
        this.textShadow = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("text-shadow")).description("Renders shadow behind text.")).defaultValue(true)).build());
        this.moduleColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("module-color")).description("Module color.")).defaultValue(new SettingColor()).build());
        this.onColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("on-color")).description("Color when module is on.")).defaultValue(new SettingColor(25, 225, 25)).build());
        this.offColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("off-color")).description("Color when module is off.")).defaultValue(new SettingColor(225, 25, 25)).build());
        this.alignment = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("alignment")).description("Horizontal alignment.")).defaultValue(Alignment.Auto)).build());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (Modules.get() == null || this.modules.get().isEmpty()) {
            renderer.text("Module Info", this.x, this.y, this.moduleColor.get(), this.textShadow.get());
            this.setSize(renderer.textWidth("Module Info"), renderer.textHeight());
            return;
        }
        double y = this.y;
        double width = 0.0;
        double height = 0.0;
        int i = 0;
        for (Module module : this.modules.get()) {
            double moduleWidth = renderer.textWidth(module.title) + renderer.textWidth(" ");
            String text = null;
            if (module.isActive()) {
                String info;
                if (this.additionalInfo.get().booleanValue() && (info = module.getInfoString()) != null) {
                    text = info;
                }
                if (text == null) {
                    text = "ON";
                }
            } else {
                text = "OFF";
            }
            double x = (double)this.x + this.alignX(moduleWidth += renderer.textWidth(text), this.alignment.get());
            x = renderer.text(module.title, x, y, this.moduleColor.get(), this.textShadow.get());
            renderer.text(text, x + renderer.textWidth(" "), y, module.isActive() ? (Color)this.onColor.get() : (Color)this.offColor.get(), this.textShadow.get());
            y += renderer.textHeight() + 2.0;
            width = Math.max(width, moduleWidth);
            height += renderer.textHeight();
            if (i > 0) {
                height += 2.0;
            }
            ++i;
        }
        this.setSize(width, height);
    }
}

