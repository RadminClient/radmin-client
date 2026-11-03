/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.gui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import radmin.client.gui.WidgetScreen;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.screens.ModuleScreen;
import radmin.client.gui.screens.NotebotSongsScreen;
import radmin.client.gui.screens.ProxiesScreen;
import radmin.client.gui.screens.RiseClickGuiScreen;
import radmin.client.gui.screens.accounts.AccountsScreen;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.utils.CharFilter;
import radmin.client.gui.utils.SettingsWidgetFactory;
import radmin.client.gui.utils.WindowConfig;
import radmin.client.gui.widgets.WAccount;
import radmin.client.gui.widgets.WHorizontalSeparator;
import radmin.client.gui.widgets.WItem;
import radmin.client.gui.widgets.WItemWithLabel;
import radmin.client.gui.widgets.WKeybind;
import radmin.client.gui.widgets.WLabel;
import radmin.client.gui.widgets.WQuad;
import radmin.client.gui.widgets.WTexture;
import radmin.client.gui.widgets.WTooltip;
import radmin.client.gui.widgets.WTopBar;
import radmin.client.gui.widgets.WVerticalSeparator;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.containers.WSection;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.containers.WVerticalList;
import radmin.client.gui.widgets.containers.WView;
import radmin.client.gui.widgets.containers.WWindow;
import radmin.client.gui.widgets.input.WBlockPosEdit;
import radmin.client.gui.widgets.input.WDoubleEdit;
import radmin.client.gui.widgets.input.WDropdown;
import radmin.client.gui.widgets.input.WIntEdit;
import radmin.client.gui.widgets.input.WSlider;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WCheckbox;
import radmin.client.gui.widgets.pressable.WConfirmedButton;
import radmin.client.gui.widgets.pressable.WConfirmedMinus;
import radmin.client.gui.widgets.pressable.WFavorite;
import radmin.client.gui.widgets.pressable.WMinus;
import radmin.client.gui.widgets.pressable.WPlus;
import radmin.client.gui.widgets.pressable.WTriangle;
import radmin.client.renderer.Texture;
import radmin.client.renderer.text.TextRenderer;
import radmin.client.settings.Settings;
import radmin.client.systems.accounts.Account;
import radmin.client.systems.modules.Module;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.color.Color;

public abstract class GuiTheme
implements ISerializable<GuiTheme> {
    public static final double TITLE_TEXT_SCALE = 1.25;
    public final String name;
    public final Settings settings = new Settings();
    public boolean disableHoverColor;
    protected SettingsWidgetFactory settingsFactory;
    protected final Map<String, WindowConfig> windowConfigs = new HashMap<String, WindowConfig>();

    public GuiTheme(String name) {
        this.name = name;
    }

    public void beforeRender() {
        this.disableHoverColor = false;
    }

    public abstract WWindow window(WWidget var1, String var2);

    public WWindow window(String title) {
        return this.window(null, title);
    }

    public abstract WLabel label(String var1, boolean var2, double var3);

    public WLabel label(String text, boolean title) {
        return this.label(text, title, 0.0);
    }

    public WLabel label(String text, double maxWidth) {
        return this.label(text, false, maxWidth);
    }

    public WLabel label(String text) {
        return this.label(text, false);
    }

    public abstract WHorizontalSeparator horizontalSeparator(String var1);

    public WHorizontalSeparator horizontalSeparator() {
        return this.horizontalSeparator(null);
    }

    public abstract WVerticalSeparator verticalSeparator();

    protected abstract WButton button(String var1, GuiTexture var2);

    public WButton button(String text) {
        return this.button(text, null);
    }

    public WButton button(GuiTexture texture) {
        return this.button(null, texture);
    }

    protected abstract WConfirmedButton confirmedButton(String var1, String var2, GuiTexture var3);

    public WConfirmedButton confirmedButton(String text, String confirmText) {
        return this.confirmedButton(text, confirmText, null);
    }

    public WConfirmedButton confirmedButton(GuiTexture texture) {
        return this.confirmedButton(null, null, texture);
    }

    public abstract WMinus minus();

    public abstract WConfirmedMinus confirmedMinus();

    public abstract WPlus plus();

    public abstract WCheckbox checkbox(boolean var1);

    public abstract WSlider slider(double var1, double var3, double var5);

    public abstract WTextBox textBox(String var1, String var2, CharFilter var3, Class<? extends WTextBox.Renderer> var4);

    public WTextBox textBox(String text, CharFilter filter, Class<? extends WTextBox.Renderer> renderer) {
        return this.textBox(text, null, filter, renderer);
    }

    public WTextBox textBox(String text, String placeholder, CharFilter filter) {
        return this.textBox(text, placeholder, filter, null);
    }

    public WTextBox textBox(String text, CharFilter filter) {
        return this.textBox(text, filter, null);
    }

    public WTextBox textBox(String text, String placeholder) {
        return this.textBox(text, placeholder, (string, c) -> true, null);
    }

    public WTextBox textBox(String text) {
        return this.textBox(text, (String string, char c) -> true, null);
    }

    public abstract <T> WDropdown<T> dropdown(T[] var1, T var2);

    public <T extends Enum<?>> WDropdown<T> dropdown(T value) {
        Class<?> klass = value.getDeclaringClass();
        Enum[] values = (Enum[])klass.getEnumConstants();
        return this.dropdown(values, value);
    }

    public abstract WTriangle triangle();

    public abstract WTooltip tooltip(String var1);

    public abstract WView view();

    public WVerticalList verticalList() {
        return this.w(new WVerticalList());
    }

    public WHorizontalList horizontalList() {
        return this.w(new WHorizontalList());
    }

    public WTable table() {
        return this.w(new WTable());
    }

    public abstract WSection section(String var1, boolean var2, WWidget var3);

    public WSection section(String title, boolean expanded) {
        return this.section(title, expanded, null);
    }

    public WSection section(String title) {
        return this.section(title, true);
    }

    public abstract WAccount account(WidgetScreen var1, Account<?> var2);

    public WWidget module(Module module) {
        return this.module(module, module.title);
    }

    public abstract WWidget module(Module var1, String var2);

    public abstract WQuad quad(Color var1);

    public abstract WTopBar topBar();

    public abstract WFavorite favorite(boolean var1);

    public WItem item(ItemStack itemStack) {
        return this.w(new WItem(itemStack));
    }

    public WItemWithLabel itemWithLabel(ItemStack stack, String name) {
        return this.w(new WItemWithLabel(stack, name));
    }

    public WItemWithLabel itemWithLabel(ItemStack stack) {
        return this.itemWithLabel(stack, Names.get(stack.getItem()));
    }

    public WTexture texture(double width, double height, double rotation, Texture texture) {
        return this.w(new WTexture(width, height, rotation, texture));
    }

    public WIntEdit intEdit(int value, int min, int max, int sliderMin, int sliderMax, boolean noSlider) {
        return this.w(new WIntEdit(value, min, max, sliderMin, sliderMax, noSlider));
    }

    public WIntEdit intEdit(int value, int min, int max, int sliderMin, int sliderMax) {
        return this.w(new WIntEdit(value, min, max, sliderMin, sliderMax, false));
    }

    public WIntEdit intEdit(int value, int min, int max, boolean noSlider) {
        return this.w(new WIntEdit(value, min, max, 0, 0, noSlider));
    }

    public WDoubleEdit doubleEdit(double value, double min, double max, double sliderMin, double sliderMax, int decimalPlaces, boolean noSlider) {
        return this.w(new WDoubleEdit(value, min, max, sliderMin, sliderMax, decimalPlaces, noSlider));
    }

    public WDoubleEdit doubleEdit(double value, double min, double max, double sliderMin, double sliderMax) {
        return this.w(new WDoubleEdit(value, min, max, sliderMin, sliderMax, 3, false));
    }

    public WDoubleEdit doubleEdit(double value, double min, double max) {
        return this.w(new WDoubleEdit(value, min, max, 0.0, 10.0, 3, false));
    }

    public WBlockPosEdit blockPosEdit(BlockPos value) {
        return this.w(new WBlockPosEdit(value));
    }

    public WKeybind keybind(Keybind keybind) {
        return this.keybind(keybind, Keybind.none());
    }

    public WKeybind keybind(Keybind keybind, Keybind defaultValue) {
        return this.w(new WKeybind(keybind, defaultValue));
    }

    public WWidget settings(Settings settings, String filter) {
        return this.settingsFactory.create(this, settings, filter);
    }

    public WWidget settings(Settings settings) {
        return this.settings(settings, "");
    }

    public TabScreen modulesScreen() {
        return new RiseClickGuiScreen(this);
    }

    public boolean isModulesScreen(Screen screen) {
        return screen instanceof RiseClickGuiScreen;
    }

    public WidgetScreen moduleScreen(Module module) {
        return new ModuleScreen(this, module);
    }

    public WidgetScreen accountsScreen() {
        return new AccountsScreen(this);
    }

    public NotebotSongsScreen notebotSongs() {
        return new NotebotSongsScreen(this);
    }

    public WidgetScreen proxiesScreen() {
        return new ProxiesScreen(this);
    }

    public abstract Color textColor();

    public abstract Color textSecondaryColor();

    public abstract Color starscriptTextColor();

    public abstract Color starscriptBraceColor();

    public abstract Color starscriptParenthesisColor();

    public abstract Color starscriptDotColor();

    public abstract Color starscriptCommaColor();

    public abstract Color starscriptOperatorColor();

    public abstract Color starscriptStringColor();

    public abstract Color starscriptNumberColor();

    public abstract Color starscriptKeywordColor();

    public abstract Color starscriptAccessedObjectColor();

    public abstract TextRenderer textRenderer();

    public abstract double scale(double var1);

    public abstract boolean categoryIcons();

    public abstract boolean hideHUD();

    public double textWidth(String text, int length, boolean title) {
        return this.scale(this.textRenderer().getWidth(text, length, false) * (title ? 1.25 : 1.0));
    }

    public double textWidth(String text) {
        return this.textWidth(text, text.length(), false);
    }

    public double textHeight(boolean title) {
        return this.scale(this.textRenderer().getHeight() * (title ? 1.25 : 1.0));
    }

    public double textHeight() {
        return this.textHeight(false);
    }

    public double pad() {
        return this.scale(6.0);
    }

    public WindowConfig getWindowConfig(String id) {
        WindowConfig config = this.windowConfigs.get(id);
        if (config != null) {
            return config;
        }
        config = new WindowConfig();
        this.windowConfigs.put(id, config);
        return config;
    }

    public void clearWindowConfigs() {
        this.windowConfigs.clear();
    }

    protected <T extends WWidget> T w(T widget) {
        widget.theme = this;
        return widget;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.name);
        tag.put("settings", (Tag)this.settings.toTag());
        CompoundTag configs = new CompoundTag();
        for (Map.Entry<String, WindowConfig> entry : this.windowConfigs.entrySet()) {
            configs.put(entry.getKey(), (Tag)entry.getValue().toTag());
        }
        tag.put("windowConfigs", (Tag)configs);
        return tag;
    }

    @Override
    public GuiTheme fromTag(CompoundTag tag) {
        tag.getCompound("settings").ifPresent(this.settings::fromTag);
        tag.getCompound("windowConfigs").ifPresent(configs -> {
            for (String id : configs.keySet()) {
                this.windowConfigs.put(id, new WindowConfig().fromTag((CompoundTag)configs.getCompound(id).get()));
            }
        });
        return this;
    }
}

