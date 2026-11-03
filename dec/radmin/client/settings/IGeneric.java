/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.settings;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.WidgetScreen;
import radmin.client.settings.GenericSetting;
import radmin.client.utils.misc.ICopyable;
import radmin.client.utils.misc.ISerializable;

public interface IGeneric<T extends IGeneric<T>>
extends ICopyable<T>,
ISerializable<T> {
    public WidgetScreen createScreen(GuiTheme var1, GenericSetting<T> var2);
}

