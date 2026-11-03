/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.CameraType
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.CameraType;
import radmin.client.events.game.ChangePerspectiveEvent;
import radmin.client.events.meteor.MouseScrollEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.KeybindSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.misc.Keybind;

public class CameraTweaks
extends Module {
    private final SettingGroup sgGeneral;
    private final SettingGroup sgScrolling;
    private final Setting<Boolean> clip;
    private final Setting<Double> cameraDistance;
    private final Setting<Boolean> scrollingEnabled;
    private final Setting<Keybind> scrollKeybind;
    private final Setting<Double> scrollSensitivity;
    public double distance;

    public CameraTweaks() {
        super(Categories.Render, "camera-tweaks", "Allows modification of the third person camera.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgScrolling = this.settings.createGroup("Scrolling");
        this.clip = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("clip")).description("Allows the camera to clip through blocks.")).defaultValue(true)).build());
        this.cameraDistance = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("camera-distance")).description("The distance the third person camera is from the player.")).defaultValue(4.0).min(0.0).onChanged(value -> {
            this.distance = value;
        })).build());
        this.scrollingEnabled = this.sgScrolling.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("scrolling")).description("Allows you to scroll to change camera distance.")).defaultValue(true)).build());
        this.scrollKeybind = this.sgScrolling.add(((KeybindSetting.Builder)((KeybindSetting.Builder)((KeybindSetting.Builder)((KeybindSetting.Builder)new KeybindSetting.Builder().name("bind")).description("Binds camera distance scrolling to a key.")).visible(this.scrollingEnabled::get)).defaultValue(Keybind.fromKey(342))).build());
        this.scrollSensitivity = this.sgScrolling.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("sensitivity")).description("Sensitivity of the scroll wheel when changing the cameras distance.")).visible(this.scrollingEnabled::get)).defaultValue(1.0).min(0.01).build());
    }

    @Override
    public void onActivate() {
        this.distance = this.cameraDistance.get();
    }

    @EventHandler
    private void onPerspectiveChanged(ChangePerspectiveEvent event) {
        this.distance = this.cameraDistance.get();
    }

    @EventHandler
    private void onMouseScroll(MouseScrollEvent event) {
        if (this.mc.options.getCameraType() == CameraType.FIRST_PERSON || this.mc.screen != null || !this.scrollingEnabled.get().booleanValue() || this.scrollKeybind.get().isSet() && !this.scrollKeybind.get().isPressed()) {
            return;
        }
        if (this.scrollSensitivity.get() > 0.0) {
            this.distance -= event.value * 0.25 * (this.scrollSensitivity.get() * this.distance);
            event.cancel();
        }
    }

    public boolean clip() {
        return this.isActive() && this.clip.get() != false;
    }
}

