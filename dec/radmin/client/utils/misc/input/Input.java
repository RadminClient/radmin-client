/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 *  org.lwjgl.glfw.GLFW
 */
package radmin.client.utils.misc.input;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiKeyEvents;
import radmin.client.mixin.KeyMappingAccessor;
import radmin.client.utils.misc.CursorStyle;

public class Input {
    private static final boolean[] keys = new boolean[512];
    private static final boolean[] buttons = new boolean[16];
    private static CursorStyle lastCursorStyle = CursorStyle.Default;

    private Input() {
    }

    public static void setKeyState(int key, boolean pressed) {
        if (key >= 0 && key < keys.length) {
            Input.keys[key] = pressed;
        }
    }

    public static void setButtonState(int button, boolean pressed) {
        if (button >= 0 && button < buttons.length) {
            Input.buttons[button] = pressed;
        }
    }

    public static int getKey(KeyMapping bind) {
        return ((KeyMappingAccessor)bind).meteor$getKey().getValue();
    }

    public static void setKeyState(KeyMapping bind, boolean pressed) {
        Input.setKeyState(Input.getKey(bind), pressed);
    }

    public static boolean isPressed(KeyMapping bind) {
        return Input.isKeyPressed(Input.getKey(bind)) || Input.isButtonPressed(Input.getKey(bind));
    }

    public static boolean isKeyPressed(int key) {
        if (!GuiKeyEvents.canUseKeys) {
            return false;
        }
        if (key == -1) {
            return false;
        }
        return key < keys.length && keys[key];
    }

    public static boolean isButtonPressed(int button) {
        if (button == -1) {
            return false;
        }
        return button < buttons.length && buttons[button];
    }

    public static void setCursorStyle(CursorStyle style) {
        if (lastCursorStyle != style) {
            GLFW.glfwSetCursor((long)MeteorClient.mc.getWindow().handle(), (long)style.getGlfwCursor());
            lastCursorStyle = style;
        }
    }

    public static int getModifier(int key) {
        return switch (key) {
            case 340, 344 -> 1;
            case 341, 345 -> 2;
            case 342, 346 -> 4;
            case 343, 347 -> 8;
            default -> 0;
        };
    }
}

