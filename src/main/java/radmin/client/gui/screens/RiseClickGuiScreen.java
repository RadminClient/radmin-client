/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.CharacterEvent
 *  net.minecraft.client.input.KeyEvent
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtIo
 *  net.minecraft.nbt.Tag
 *  net.minecraft.util.Tuple
 *  org.apache.commons.io.FilenameUtils
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.PointerBuffer
 *  org.lwjgl.system.MemoryUtil
 *  org.lwjgl.util.tinyfd.TinyFileDialogs
 */
package radmin.client.gui.screens;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.screens.FontAwesomeIcons;
import radmin.client.gui.screens.settings.ColorSettingScreen;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.tabs.Tabs;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.utils.CharFilter;
import radmin.client.gui.widgets.WKeybind;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.ColorSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.IntSetting;
import radmin.client.settings.KeybindSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.Settings;
import radmin.client.settings.StringSetting;
import radmin.client.systems.config.Config;
import radmin.client.systems.friends.Friend;
import radmin.client.systems.friends.Friends;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.systems.modules.Category;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.profiles.Profile;
import radmin.client.systems.profiles.Profiles;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;
import radmin.client.utils.render.prompts.OkPrompt;

public class RiseClickGuiScreen
extends TabScreen {
    private static final Color CONTAINER_BG_TOP = new Color(28, 31, 40);
    private static final Color CONTAINER_BG_BOTTOM = new Color(16, 18, 24);
    private static final Color CONTAINER_BORDER = new Color(255, 255, 255, 8);
    private static final Color SIDEBAR_BG_TOP = new Color(22, 25, 32);
    private static final Color SIDEBAR_BG_BOTTOM = new Color(13, 15, 20);
    private static final Color PANEL_BG_TOP = new Color(28, 31, 40);
    private static final Color PANEL_BG_BOTTOM = new Color(16, 18, 24);
    private static final Color NAV_COLOR = new Color(123, 133, 149);
    private static final Color NAV_COLOR_HOVERED = new Color(197, 208, 223);
    private static final Color NAV_ACTIVE_BG = new Color(26, 34, 40);
    private static final Color NAV_ACTIVE_BORDER = new Color(16, 89, 185, 77);
    private static final Color ACCENT = new Color(0, 153, 255);
    private static final Color SEARCH_BG = new Color(26, 34, 40);
    private static final Color CARD_BG_TOP = new Color(28, 32, 41);
    private static final Color CARD_BG_BOTTOM = new Color(21, 24, 31);
    private static final Color CARD_BG_HOVERED_TOP = new Color(32, 36, 46);
    private static final Color CARD_BG_HOVERED_BOTTOM = new Color(24, 28, 36);
    private static final Color CARD_BORDER = new Color(255, 255, 255, 5);
    private static final Color CARD_TITLE = new Color(226, 232, 240);
    private static final Color CARD_DESC = new Color(85, 96, 112);
    private static final Color VERSION_COLOR = new Color(82, 92, 106);
    private static final Color SCROLLBAR_COLOR = new Color(31, 37, 48);
    private static final Color SCROLLBAR_COLOR_HOVERED = new Color(16, 185, 129);
    private static final Color ENABLED_COLOR = new Color(16, 185, 129);
    private static final Color DISABLED_COLOR = new Color(31, 37, 48);
    private static final Color SEARCH_TEXT_COLOR = new Color(226, 232, 240);
    private static final PointerBuffer PROFILE_FILTERS = BufferUtils.createPointerBuffer((int)1);
    private final GuiRenderer renderer = new GuiRenderer();
    private final Color tmpColor = new Color();
    private final List<NavItem> navItems = new ArrayList<NavItem>();
    private final List<Card> cards = new ArrayList<Card>();
    private int selectedNav = -1;
    private String searchText = "";
    private boolean searchFocused;
    private double contentScroll;
    private double targetScroll;
    private double maxScroll;
    private double contentTransition = 1.0;
    private double k;
    private double containerX;
    private double containerY;
    private double containerW;
    private double containerH;
    private double sidebarW;
    private double contentX;
    private double contentW;
    private double contentLeft;
    private double contentTop;
    private double contentRight;
    private double contentBottom;
    private double contentViewW;
    private double contentViewH;
    private double logoY;
    private double searchX;
    private double searchY;
    private double searchW;
    private double searchH;
    private int mouseX;
    private int mouseY;
    private double lastMouseX;
    private double lastMouseY;
    private SpecialPage currentPage;
    private final List<RiseRow> pageRows = new ArrayList<RiseRow>();
    private double pageRowScroll;
    private double pageRowMaxScroll;
    private Settings pageSettings;
    private GuiSubPage guiSubPage = GuiSubPage.NONE;
    private Module moduleSettings;
    private final List<RiseRow> moduleSettingsRows = new ArrayList<RiseRow>();
    private double moduleSettingsScroll;
    private double moduleSettingsMaxScroll;
    private RiseRow dragRow;
    private double dragPreview = Double.NaN;
    private RiseRow editingRow;
    private String editingValue;
    private double moduleSettingsX;
    private double moduleSettingsY;
    private double moduleSettingsW;
    private double moduleSettingsH;
    private double moduleSettingsCloseX;
    private double moduleSettingsCloseY;
    private double moduleSettingsCloseW;
    private double moduleSettingsCloseH;
    private double moduleSettingsPanelW;

    public RiseClickGuiScreen(GuiTheme theme) {
        super(theme, (Tab)Tabs.get().getFirst());
        this.animProgressSpeed = 5.0;
        this.buildNav();
        this.refreshModules();
    }

    @Override
    public void initWidgets() {
    }

    @Override
    protected void onRenderBefore(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.updateAnimations(delta);
        this.computeLayout();
        this.renderer.theme = this.theme;
        this.renderer.begin(graphics);
        this.renderer.setAlpha(this.animProgress);
        this.renderBackground();
        this.renderSearchBar();
        this.renderNavItems();
        this.renderContent(delta);
        this.renderer.setAlpha(1.0);
        this.renderer.end();
    }

    private void renderBackground() {
        this.rounded(this.containerX - 1.0, this.containerY - 1.0, this.containerW + 2.0, this.containerH + 2.0, 12.0 * this.k, CONTAINER_BORDER);
        this.roundedGradient(this.containerX, this.containerY, this.containerW, this.containerH, 12.0 * this.k, CONTAINER_BG_TOP, CONTAINER_BG_BOTTOM);
        this.roundedGradient(this.containerX + 1.0, this.containerY + 1.0, this.sidebarW - 2.0, this.containerH - 2.0, 12.0 * this.k, 0.0, 0.0, 12.0 * this.k, SIDEBAR_BG_TOP, SIDEBAR_BG_TOP, SIDEBAR_BG_BOTTOM, SIDEBAR_BG_BOTTOM);
    }

    private void renderSearchBar() {
        this.rounded(this.searchX - 1.0, this.searchY - 1.0, this.searchW + 2.0, this.searchH + 2.0, 8.0 * this.k, NAV_ACTIVE_BORDER);
        this.rounded(this.searchX, this.searchY, this.searchW, this.searchH, 8.0 * this.k, SEARCH_BG);
        FontAwesomeIcons.render(this.renderer, this.searchX + 14.0 * this.k + 8.0 * this.k, this.searchY + this.searchH / 2.0, 16.0 * this.k, this.accent(), 61442);
        String shown = this.searchText;
        if (shown.isEmpty()) {
            if (!this.searchFocused) {
                this.renderer.text("Search", this.searchX + 14.0 * this.k + 16.0 * this.k + 6.0 * this.k, this.searchY + (this.searchH - this.textHeight()) / 2.0, this.accent(), false);
            }
        } else {
            double textX = this.searchX + 14.0 * this.k + 16.0 * this.k + 6.0 * this.k;
            this.renderer.text(this.truncate(shown, this.searchW - (textX - this.searchX) - 8.0 * this.k), textX, this.searchY + (this.searchH - this.textHeight()) / 2.0, SEARCH_TEXT_COLOR, false);
            if (this.searchFocused && System.currentTimeMillis() / 500L % 2L == 0L) {
                double cursorX = textX + this.theme.textRenderer().getWidth(shown);
                this.renderer.quad(cursorX, this.searchY + 8.0 * this.k, 1.0, this.searchH - 16.0 * this.k, this.accent());
            }
        }
    }

    private void renderNavItems() {
        double logoIconSize = 56.0 * this.k;
        double logoCenterX = this.containerX + this.sidebarW / 2.0;
        double logoIconX = logoCenterX - logoIconSize / 2.0;
        double logoIconY = this.logoY + 4.0 * this.k;
        this.renderer.quad(logoIconX, logoIconY, logoIconSize, logoIconSize, GuiRenderer.LOGO_BLUE, this.accent());
        this.renderer.quad(logoIconX, logoIconY, logoIconSize, logoIconSize, GuiRenderer.LOGO_WHITE, Color.WHITE);
        double titleW = this.theme.textRenderer().getWidth("Radmin") * 1.25;
        String version = "1.0 Beta";
        double versionW = this.theme.textRenderer().getWidth(version);
        double groupW = titleW + 8.0 * this.k + versionW;
        double groupX = logoCenterX - groupW / 2.0;
        double groupY = logoIconY + logoIconSize + 6.0 * this.k;
        this.renderer.text("Radmin", groupX, groupY, Color.WHITE, true);
        this.renderer.text(version, groupX + titleW + 8.0 * this.k, groupY + 2.0 * this.k, VERSION_COLOR, false);
        for (int i = 0; i < this.navItems.size(); ++i) {
            boolean active;
            NavItem item = this.navItems.get(i);
            boolean hovered = this.mouseInside(item.x, item.y, item.w, item.h);
            boolean bl = active = i == this.selectedNav && this.searchText.isBlank();
            if (active) {
                this.rounded(item.x - 1.0, item.y - 1.0, item.w + 2.0, item.h + 2.0, 8.0 * this.k, NAV_ACTIVE_BORDER);
                this.rounded(item.x, item.y, item.w, item.h, 8.0 * this.k, NAV_ACTIVE_BG);
                double r = 8.0 * this.k;
                this.renderer.quad(item.x + r, item.y, item.w - r * 2.0, item.h, this.alpha(this.accent(), 0.16), this.alpha(this.accent(), 0.0), this.alpha(this.accent(), 0.0), this.alpha(this.accent(), 0.16));
            }
            double iconX = item.x + 14.0 * this.k;
            double iconY = item.y + (item.h - 16.0 * this.k) / 2.0;
            Color color = active ? this.accent() : (hovered ? NAV_COLOR_HOVERED : NAV_COLOR);
            FontAwesomeIcons.render(this.renderer, iconX + 8.0 * this.k, iconY + 8.0 * this.k, 16.0 * this.k, color, item.icon);
            double textX = iconX + 16.0 * this.k + 12.0 * this.k;
            this.renderer.text(item.name, textX, item.y + (item.h - this.textHeight()) / 2.0, color, false);
        }
    }

    private void renderContent(float delta) {
        if (this.currentPage != null) {
            this.renderPage(delta);
        } else {
            this.renderCards();
            if (this.moduleSettings != null) {
                this.renderModuleSettings(delta);
            }
        }
    }

    private void renderPage(float delta) {
        double t = RiseClickGuiScreen.easeOutCubic(this.contentTransition);
        double slideX = (1.0 - t) * 60.0 * this.k;
        this.renderer.scissorStart(this.contentLeft, this.contentTop, this.contentViewW, this.contentViewH);
        this.renderer.setAlpha(this.animProgress * t);
        for (RiseRow row : this.pageRows) {
            if (row.y + row.h < this.contentTop || row.y > this.contentBottom) continue;
            this.renderPageRow(row);
        }
        if (this.pageRowMaxScroll > 0.0) {
            double thumbH;
            double thumbY;
            double scrollX;
            boolean hovered = this.mouseInside(scrollX = this.contentRight - 6.0 * this.k, thumbY = this.contentTop + this.pageRowScroll / this.pageRowMaxScroll * (this.contentViewH - (thumbH = Math.max(24.0 * this.k, this.contentViewH * (this.contentViewH / (this.contentViewH + this.pageRowMaxScroll))))), 4.0 * this.k, thumbH);
            this.rounded(scrollX, thumbY, 4.0 * this.k, thumbH, 2.0 * this.k, hovered ? SCROLLBAR_COLOR_HOVERED : SCROLLBAR_COLOR);
        }
        this.renderer.scissorEnd();
        this.renderer.setAlpha(1.0);
    }

    private void renderCards() {
        double t = RiseClickGuiScreen.easeOutCubic(this.contentTransition);
        double slideX = (1.0 - t) * 60.0 * this.k;
        this.renderer.scissorStart(this.contentLeft, this.contentTop, this.contentViewW, this.contentViewH);
        this.renderer.setAlpha(this.animProgress * t);
        for (Card card : this.cards) {
            if (card.y + card.h < this.contentTop || card.y > this.contentBottom) continue;
            double cx = card.x + slideX;
            boolean hovered = this.mouseInside(cx, card.y, card.w, card.h);
            this.rounded(cx - 1.0, card.y - 1.0, card.w + 2.0, card.h + 2.0, 8.0 * this.k, CARD_BORDER);
            this.roundedGradient(cx, card.y, card.w, card.h, 8.0 * this.k, hovered ? CARD_BG_HOVERED_TOP : CARD_BG_TOP, hovered ? CARD_BG_HOVERED_BOTTOM : CARD_BG_BOTTOM);
            if (card.flash > 0.0) {
                Color flash = this.tmpColor(this.accent(), card.flash * 0.16);
                this.roundedGradient(cx, card.y, card.w, card.h, 8.0 * this.k, flash, flash);
            }
            double maxTextW = card.w - 18.0 * this.k - 24.0 * this.k;
            this.renderer.text(this.truncate(card.title, maxTextW), cx + 18.0 * this.k, card.y + 14.0 * this.k, CARD_TITLE, false);
            String desc = this.truncate(card.module.description, maxTextW);
            if (!desc.isEmpty()) {
                this.renderer.text(desc, cx + 18.0 * this.k, card.y + 14.0 * this.k + 15.0 * this.k + 4.0 * this.k, CARD_DESC, false);
            }
            double dotAnim = RiseClickGuiScreen.easeOutCubic(card.anim);
            double dot = 8.0 * this.k * (0.65 + 0.35 * dotAnim);
            double dotX = cx + card.w - 18.0 * this.k - dot;
            double dotY = card.y + card.h / 2.0 - dot / 2.0;
            this.rounded(dotX, dotY, dot, dot, dot / 2.0, this.lerp(DISABLED_COLOR, ENABLED_COLOR, dotAnim));
        }
        if (this.maxScroll > 0.0) {
            double scrollX = this.contentX + this.contentW - 6.0 * this.k;
            double thumbH = Math.max(24.0 * this.k, this.contentViewH * (this.contentViewH / (this.contentViewH + this.maxScroll)));
            double thumbY = this.contentTop + this.contentScroll / this.maxScroll * (this.contentViewH - thumbH);
            boolean hovered = (double)this.mouseX >= scrollX && (double)this.mouseX <= scrollX + 4.0 * this.k && (double)this.mouseY >= thumbY && (double)this.mouseY <= thumbY + thumbH;
            this.rounded(scrollX, thumbY, 4.0 * this.k, thumbH, 2.0 * this.k, hovered ? SCROLLBAR_COLOR_HOVERED : SCROLLBAR_COLOR);
        }
        this.renderer.scissorEnd();
        this.renderer.setAlpha(1.0);
    }

    private void renderModuleSettings(float delta) {
        if (this.moduleSettings == null) {
            return;
        }
        double t = RiseClickGuiScreen.easeOutCubic(this.contentTransition);
        double slideX = (1.0 - t) * 60.0 * this.k;
        double x = this.moduleSettingsX + slideX;
        double y = this.moduleSettingsY;
        double w = this.moduleSettingsW;
        double h = this.moduleSettingsH;
        this.renderer.setAlpha(this.animProgress * t);
        this.rounded(x - 1.0, y - 1.0, w + 2.0, h + 2.0, 12.0 * this.k, CONTAINER_BORDER);
        this.roundedGradient(x, y, w, h, 12.0 * this.k, PANEL_BG_TOP, PANEL_BG_BOTTOM);
        this.renderer.quad(x + 12.0 * this.k, y, w - 24.0 * this.k, 2.0, this.alpha(this.accent(), 0.9), this.alpha(this.accent(), 0.35), this.alpha(this.accent(), 0.35), this.alpha(this.accent(), 0.9));
        double headerH = 38.0 * this.k;
        double headerY = y + 10.0 * this.k;
        double maxTextW = w - 14.0 * this.k - this.moduleSettingsCloseW - 12.0 * this.k - 10.0 * this.k;
        this.renderer.text(this.truncate(this.moduleSettings.title, maxTextW), x + 14.0 * this.k, headerY + (headerH - this.textHeight()) / 2.0, CARD_TITLE, false);
        double closeSize = 22.0 * this.k;
        double closeX = x + w - closeSize - 10.0 * this.k;
        double closeY = y + (headerH - closeSize) / 2.0;
        boolean closeHovered = this.mouseInside(closeX, closeY, closeSize, closeSize);
        this.rounded(closeX, closeY, closeSize, closeSize, 6.0 * this.k, closeHovered ? NAV_ACTIVE_BG : new Color(255, 255, 255, 6));
        FontAwesomeIcons.render(this.renderer, closeX + closeSize / 2.0, closeY + closeSize / 2.0, 14.0 * this.k, this.accent(), 61453);
        this.renderer.quad(x + 10.0 * this.k, y + headerH, w - 20.0 * this.k, 1.0, new Color(255, 255, 255, 10));
        double top = y + headerH + 8.0 * this.k;
        double bottom = y + h - 12.0 * this.k;
        this.renderer.scissorStart(x + 1.0, y + 1.0, w - 2.0, h - 2.0);
        for (RiseRow row : this.moduleSettingsRows) {
            if (row.y + row.h < top || row.y > bottom) continue;
            this.renderModuleSettingsRow(row);
        }
        if (this.moduleSettingsMaxScroll > 0.0) {
            double thumbH;
            double thumbY;
            double scrollX;
            boolean hovered = this.mouseInside(scrollX = x + w - 8.0 * this.k, thumbY = top + this.moduleSettingsScroll / this.moduleSettingsMaxScroll * (bottom - top - (thumbH = Math.max(24.0 * this.k, (bottom - top) * ((bottom - top) / (bottom - top + this.moduleSettingsMaxScroll))))), 4.0 * this.k, thumbH);
            this.rounded(scrollX, thumbY, 4.0 * this.k, thumbH, 2.0 * this.k, hovered ? SCROLLBAR_COLOR_HOVERED : SCROLLBAR_COLOR);
        }
        this.renderer.scissorEnd();
        this.renderer.setAlpha(1.0);
    }

    private void renderModuleSettingsRow(RiseRow row) {
        switch (row.type.ordinal()) {
            case 0: {
                double ty = row.y;
                for (String line : row.lines) {
                    this.renderer.text(line, row.x, ty, CARD_DESC, false);
                    ty += this.textHeight();
                }
                break;
            }
            case 1: {
                this.renderer.text(row.text, row.x, row.y + (row.h - this.textHeight()) / 2.0, this.accent(), false);
                break;
            }
            case 2: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                this.renderToggle(row.x + row.w - 34.0 * this.k, row.y + row.h / 2.0, (Boolean)row.setting.get());
                break;
            }
            case 3: {
                this.renderModuleSettingsSlider(row);
                break;
            }
            case 4: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                String value = row.setting.get().toString();
                double ew = Math.min(row.w - this.theme.textRenderer().getWidth(row.setting.title) - 20.0 * this.k, Math.max(64.0 * this.k, this.theme.textRenderer().getWidth(value) + 28.0 * this.k));
                double ex = row.x + row.w - ew;
                boolean hovered = this.mouseInside(ex, row.y + (row.h - this.pillH()) / 2.0, ew, this.pillH());
                this.renderPill(ex, row.y + (row.h - this.pillH()) / 2.0, ew, this.pillH(), hovered);
                this.renderer.text(value, ex + (ew - this.theme.textRenderer().getWidth(value)) / 2.0, row.y + (row.h - this.textHeight()) / 2.0, hovered ? this.accent() : CARD_TITLE, false);
                FontAwesomeIcons.render(this.renderer, ex + ew - 13.0 * this.k, row.y + row.h / 2.0, 9.0 * this.k, CARD_DESC, 61560);
                break;
            }
            case 5: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                SettingColor c = (SettingColor)((ColorSetting)row.setting).get();
                double sw = 22.0 * this.k;
                double sx = row.x + row.w - sw;
                double sy = row.y + (row.h - sw) / 2.0;
                boolean hovered = this.mouseInside(sx - 1.0, sy - 1.0, sw + 2.0, sw + 2.0);
                this.rounded(sx - 1.0, sy - 1.0, sw + 2.0, sw + 2.0, 7.0 * this.k, hovered ? this.alpha(this.accent(), 0.7) : new Color(255, 255, 255, 12));
                this.rounded(sx, sy, sw, sw, 6.0 * this.k, new Color(c.r, c.g, c.b, c.a));
                break;
            }
            case 6: {
                this.renderModuleSettingsKeybind(row);
                break;
            }
            case 7: {
                this.renderModuleSettingsString(row);
                break;
            }
            case 8: {
                this.renderModuleSettingsEditPill(row, row.setting.title);
                break;
            }
            case 9: {
                this.renderModuleSettingsEditPill(row, "Module options");
                break;
            }
            case 10: {
                this.renderModuleSettingsBind(row);
                break;
            }
            case 11: {
                this.renderBooleanRow(row, "Toggle on bind release", this.moduleSettings.toggleOnBindRelease);
                break;
            }
            case 12: {
                this.renderBooleanRow(row, "Chat Feedback", this.moduleSettings.chatFeedback);
                break;
            }
            case 13: {
                this.renderBooleanRow(row, "Active", this.moduleSettings.isActive());
                break;
            }
            case 14: {
                this.renderModuleSettingsButtons(row);
                break;
            }
            case 15: {
                this.renderer.quad(row.x, row.y + row.h / 2.0, row.w, 1.0, new Color(255, 255, 255, 8));
            }
        }
    }

    private void renderModuleSettingsEditPill(RiseRow row, String label) {
        this.renderer.text(this.truncate(label, row.w - 100.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double bw = 64.0 * this.k;
        double bx = row.x + row.w - bw;
        boolean hovered = this.mouseInside(bx, row.y + (row.h - this.pillH()) / 2.0, bw, this.pillH());
        this.renderPill(bx, row.y + (row.h - this.pillH()) / 2.0, bw, this.pillH(), hovered);
        FontAwesomeIcons.render(this.renderer, bx + 15.0 * this.k, row.y + row.h / 2.0, 11.0 * this.k, hovered ? this.accent() : CARD_DESC, 61508);
        this.renderer.text("Edit", bx + 26.0 * this.k, row.y + (row.h - this.textHeight()) / 2.0, hovered ? this.accent() : CARD_TITLE, false);
    }

    private void renderModuleSettingsSlider(RiseRow row) {
        Setting<?> s = row.setting;
        double displayed = row == this.dragRow ? this.dragPreview : this.sliderValue(s);
        double t = this.sliderT(s, displayed);
        String text = this.sliderValueText(s, displayed);
        this.renderer.text(this.truncate(s.title, row.w - 64.0 * this.k), row.x, row.y + 4.0 * this.k, CARD_TITLE, false);
        this.renderer.text(text, row.x + row.w - this.theme.textRenderer().getWidth(text), row.y + 4.0 * this.k, this.accent(), false);
        double trackY = row.y + row.h - 15.0 * this.k;
        double trackH = 4.0 * this.k;
        this.rounded(row.x, trackY, row.w, trackH, trackH / 2.0, new Color(255, 255, 255, 12));
        double fillW = row.w * t;
        if (fillW > 1.0) {
            this.rounded(row.x, trackY, fillW, trackH, trackH / 2.0, this.alpha(this.accent(), 0.9));
        }
        double knobD = 12.0 * this.k;
        double knobX = row.x + fillW - knobD / 2.0;
        this.rounded(knobX, trackY + trackH / 2.0 - knobD / 2.0, knobD, knobD, knobD / 2.0, Color.WHITE);
    }

    private void renderModuleSettingsKeybind(RiseRow row) {
        KeybindSetting ks = row.keybindSetting;
        boolean listening = ks.widget != null && ks.widget.isListening();
        this.renderer.text(this.truncate(ks.title, row.w - 100.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double rw = 26.0 * this.k;
        double rx = row.x + row.w - rw;
        double ry = row.y + (row.h - this.pillH()) / 2.0;
        boolean rh = this.mouseInside(rx, ry, rw, this.pillH());
        this.renderPill(rx, ry, rw, this.pillH(), rh);
        FontAwesomeIcons.render(this.renderer, rx + rw / 2.0, ry + this.pillH() / 2.0, 12.0 * this.k, rh ? this.accent() : CARD_DESC, 61666);
        String label = listening ? "..." : ((Keybind)ks.get()).toString();
        double bw = Math.min(row.w - this.theme.textRenderer().getWidth(ks.title) - 60.0 * this.k, Math.max(64.0 * this.k, this.theme.textRenderer().getWidth(label) + 24.0 * this.k));
        double bx = rx - 6.0 * this.k - bw;
        boolean bh = this.mouseInside(bx, ry, bw, this.pillH());
        this.renderPill(bx, ry, bw, this.pillH(), bh || listening);
        String shown = this.truncate(label, bw - 12.0 * this.k);
        this.renderer.text(shown, bx + (bw - this.theme.textRenderer().getWidth(shown)) / 2.0, ry + (this.pillH() - this.textHeight()) / 2.0, bh || listening ? this.accent() : CARD_TITLE, false);
    }

    private void renderModuleSettingsString(RiseRow row) {
        StringSetting ss = row.stringSetting;
        boolean focused = row == this.editingRow;
        String text = focused ? this.editingValue : (String)ss.get();
        boolean empty = text == null || text.isEmpty();
        this.renderer.text(this.truncate(ss.title, row.w - 120.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double th = 28.0 * this.k;
        double bw = Math.min(160.0 * this.k, Math.max(80.0 * this.k, row.w - this.theme.textRenderer().getWidth(ss.title) - 20.0 * this.k));
        double bx = row.x + row.w - bw;
        double by = row.y + (row.h - th) / 2.0;
        this.rounded(bx - 1.0, by - 1.0, bw + 2.0, th + 2.0, 8.0 * this.k, focused ? this.alpha(this.accent(), 0.55) : new Color(255, 255, 255, 8));
        this.rounded(bx, by, bw, th, 7.0 * this.k, focused ? this.alpha(this.accent(), 0.15) : new Color(255, 255, 255, 6));
        String shown = empty ? (ss.placeholder != null ? ss.placeholder : "") : text;
        double tx = bx + 8.0 * this.k;
        this.renderer.text(this.truncate(shown, bw - 16.0 * this.k), tx, by + (th - this.textHeight()) / 2.0, empty ? CARD_DESC : CARD_TITLE, false);
        if (focused && System.currentTimeMillis() / 500L % 2L == 0L) {
            double cursorX = tx + this.theme.textRenderer().getWidth(text);
            this.renderer.quad(cursorX, by + 6.0 * this.k, 1.0, th - 12.0 * this.k, this.accent());
        }
    }

    private void renderModuleSettingsBind(RiseRow row) {
        boolean binding = Modules.get().isBinding();
        this.renderer.text("Bind", row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double rw = 26.0 * this.k;
        double rx = row.x + row.w - rw;
        double ry = row.y + (row.h - this.pillH()) / 2.0;
        boolean rh = this.mouseInside(rx, ry, rw, this.pillH());
        this.renderPill(rx, ry, rw, this.pillH(), rh);
        FontAwesomeIcons.render(this.renderer, rx + rw / 2.0, ry + this.pillH() / 2.0, 12.0 * this.k, rh ? this.accent() : CARD_DESC, 61666);
        String label = binding ? "..." : this.moduleSettings.keybind.toString();
        double bw = 110.0 * this.k;
        double bx = rx - 6.0 * this.k - bw;
        boolean bh = this.mouseInside(bx, ry, bw, this.pillH());
        this.renderPill(bx, ry, bw, this.pillH(), bh || binding);
        String shown = this.truncate(label, bw - 12.0 * this.k);
        this.renderer.text(shown, bx + (bw - this.theme.textRenderer().getWidth(shown)) / 2.0, ry + (this.pillH() - this.textHeight()) / 2.0, bh || binding ? this.accent() : CARD_TITLE, false);
    }

    private void renderModuleSettingsButtons(RiseRow row) {
        double bw = (row.w - 8.0 * this.k) / 2.0;
        double pill = this.pillH();
        boolean ch = this.mouseInside(row.x, row.y + (row.h - pill) / 2.0, bw, pill);
        this.renderPill(row.x, row.y + (row.h - pill) / 2.0, bw, pill, ch);
        FontAwesomeIcons.render(this.renderer, row.x + 16.0 * this.k, row.y + row.h / 2.0, 12.0 * this.k, ch ? this.accent() : CARD_DESC, 61637);
        this.renderer.text("Copy config", row.x + 30.0 * this.k, row.y + (row.h - this.textHeight()) / 2.0, ch ? this.accent() : CARD_TITLE, false);
        double px = row.x + bw + 8.0 * this.k;
        boolean ph = this.mouseInside(px, row.y + (row.h - pill) / 2.0, bw, pill);
        this.renderPill(px, row.y + (row.h - pill) / 2.0, bw, pill, ph);
        FontAwesomeIcons.render(this.renderer, px + 16.0 * this.k, row.y + row.h / 2.0, 12.0 * this.k, ph ? this.accent() : CARD_DESC, 61674);
        this.renderer.text("Paste config", px + 30.0 * this.k, row.y + (row.h - this.textHeight()) / 2.0, ph ? this.accent() : CARD_TITLE, false);
    }

    private void renderBooleanRow(RiseRow row, String label, boolean on) {
        this.renderer.text(this.truncate(label, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        this.renderToggle(row.x + row.w - 34.0 * this.k, row.y + row.h / 2.0, on);
    }

    private void renderToggle(double x, double centerY, boolean on) {
        double w = 34.0 * this.k;
        double h = 18.0 * this.k;
        double y = centerY - h / 2.0;
        this.rounded(x, y, w, h, h / 2.0, on ? this.alpha(this.accent(), 0.9) : new Color(255, 255, 255, 16));
        double d = 14.0 * this.k;
        double pad = (h - d) / 2.0;
        double kx = on ? x + w - d - pad : x + pad;
        this.rounded(kx, y + pad, d, d, d / 2.0, Color.WHITE);
    }

    private void renderPill(double x, double y, double w, double h, boolean hovered) {
        this.rounded(x - 1.0, y - 1.0, w + 2.0, h + 2.0, 8.0 * this.k, hovered ? this.alpha(this.accent(), 0.45) : new Color(255, 255, 255, 6));
        this.roundedGradient(x, y, w, h, 8.0 * this.k, hovered ? CARD_BG_HOVERED_TOP : CARD_BG_TOP, hovered ? CARD_BG_HOVERED_BOTTOM : CARD_BG_BOTTOM);
    }

    private double pillH() {
        return 24.0 * this.k;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (this.locked) {
            return false;
        }
        double s = MeteorClient.mc.getWindow().getGuiScale();
        double mx = click.x() * s;
        double my = click.y() * s;
        this.computeLayout();
        if (this.moduleSettings != null && RiseClickGuiScreen.mouseInside(this.moduleSettingsX, this.moduleSettingsY, this.moduleSettingsW, this.moduleSettingsH, mx, my)) {
            if (RiseClickGuiScreen.mouseInside(this.moduleSettingsCloseX, this.moduleSettingsCloseY, this.moduleSettingsCloseW, this.moduleSettingsCloseH, mx, my)) {
                this.closeModuleSettings();
                return true;
            }
            this.commitEdit();
            if (click.button() == 0) {
                for (RiseRow row : this.moduleSettingsRows) {
                    if (!RiseClickGuiScreen.mouseInside(row.x, row.y, row.w, row.h, mx, my)) continue;
                    this.handleModuleSettingsRowClick(row, mx, my);
                    return true;
                }
            } else if (click.button() == 1) {
                for (RiseRow row : this.moduleSettingsRows) {
                    if (!RiseClickGuiScreen.mouseInside(row.x, row.y, row.w, row.h, mx, my)) continue;
                    if (row.type == RiseRowType.ENUM) {
                        this.cycleEnum(row, -1);
                    }
                    return true;
                }
            }
            return true;
        }
        if (RiseClickGuiScreen.mouseInside(this.searchX, this.searchY, this.searchW, this.searchH, mx, my)) {
            this.searchFocused = true;
            return true;
        }
        for (int i = 0; i < this.navItems.size(); ++i) {
            NavItem item = this.navItems.get(i);
            if (!RiseClickGuiScreen.mouseInside(item.x, item.y, item.w, item.h, mx, my)) continue;
            this.selectNav(i);
            return true;
        }
        if (mx >= this.contentLeft && mx <= this.contentRight && my >= this.contentTop && my <= this.contentBottom) {
            this.commitEdit();
            if (this.currentPage != null) {
                return this.handlePageClick(mx, my, click.button());
            }
            for (Card card : this.cards) {
                if (!RiseClickGuiScreen.mouseInside(card.x, card.y, card.w, card.h, mx, my)) continue;
                if (click.button() == 0) {
                    card.flash = 1.0;
                    card.module.toggle();
                } else if (click.button() == 1) {
                    this.openModuleSettings(card.module);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        if (this.locked) {
            return false;
        }
        double s = MeteorClient.mc.getWindow().getGuiScale();
        double mx = click.x() * s;
        double my = click.y() * s;
        if (this.currentPage != null && this.dragRow != null) {
            this.updateSliderDrag(mx, true);
            return true;
        }
        if (this.moduleSettings != null && RiseClickGuiScreen.mouseInside(this.moduleSettingsX, this.moduleSettingsY, this.moduleSettingsW, this.moduleSettingsH, mx, my)) {
            if (this.dragRow != null) {
                this.updateSliderDrag(mx, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (this.locked) {
            return;
        }
        double s = MeteorClient.mc.getWindow().getGuiScale();
        double mx = mouseX * s;
        double my = mouseY * s;
        if (this.dragRow != null) {
            this.updateSliderDrag(mx, false);
        }
        this.lastMouseX = mx;
        this.lastMouseY = my;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.locked) {
            return false;
        }
        double s = MeteorClient.mc.getWindow().getGuiScale();
        double mx = mouseX * s;
        double my = mouseY * s;
        this.computeLayout();
        if (this.moduleSettings != null && RiseClickGuiScreen.mouseInside(this.moduleSettingsX, this.moduleSettingsY, this.moduleSettingsW, this.moduleSettingsH, mx, my)) {
            this.moduleSettingsScroll = RiseClickGuiScreen.clamp(this.moduleSettingsScroll - verticalAmount * (30.0 * this.k), 0.0, this.moduleSettingsMaxScroll);
            return true;
        }
        if (mx >= this.contentLeft && mx <= this.contentRight && my >= this.contentTop && my <= this.contentBottom) {
            if (this.currentPage != null) {
                this.pageRowScroll = RiseClickGuiScreen.clamp(this.pageRowScroll - verticalAmount * (30.0 * this.k), 0.0, this.pageRowMaxScroll);
                return true;
            }
            this.targetScroll -= verticalAmount * (30.0 * this.k);
            this.targetScroll = RiseClickGuiScreen.clamp(this.targetScroll, 0.0, this.maxScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (this.locked) {
            return false;
        }
        if (this.searchFocused) {
            if (input.key() == 259) {
                if (!this.searchText.isEmpty()) {
                    this.searchText = this.searchText.substring(0, this.searchText.length() - 1);
                    this.refreshModules();
                }
                return true;
            }
            if (input.key() == 256 || input.key() == 257 || input.key() == 335) {
                this.searchFocused = false;
                return true;
            }
            return true;
        }
        if (this.editingRow != null) {
            if (input.key() == 259) {
                if (!this.editingValue.isEmpty()) {
                    this.editingValue = this.editingValue.substring(0, this.editingValue.length() - 1);
                    if (this.editingRow.type == RiseRowType.STRING) {
                        this.editingRow.stringSetting.set(this.editingValue);
                    }
                }
                return true;
            }
            if (input.key() == 257 || input.key() == 335) {
                if (this.editingRow.type == RiseRowType.TEXTBOX && this.editingRow.action != null) {
                    this.editingRow.text = this.editingValue;
                    this.editingRow.action.run();
                }
                this.commitEdit();
                return true;
            }
            if (input.key() == 256) {
                this.commitEdit();
                return true;
            }
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void keyRepeated(KeyEvent input) {
        if (this.locked) {
            return;
        }
        if (this.editingRow != null) {
            this.keyPressed(input);
        }
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        if (this.locked) {
            return false;
        }
        if (this.searchFocused) {
            String s = input.codepointAsString();
            if (!s.isEmpty()) {
                this.searchText = this.searchText + s;
                this.refreshModules();
            }
            return true;
        }
        if (this.editingRow != null) {
            String s = input.codepointAsString();
            if (!s.isEmpty()) {
                if (this.editingRow.type == RiseRowType.STRING) {
                    StringSetting ss = this.editingRow.stringSetting;
                    String next = this.editingValue + s;
                    if ((ss.filter == null || ss.filter.filter(next, s.charAt(0))) && ss.set(next)) {
                        this.editingValue = next;
                    }
                } else if (this.editingRow.type == RiseRowType.TEXTBOX) {
                    String next = this.editingValue + s;
                    if (this.editingRow.filter == null || this.editingRow.filter.filter(next, s.charAt(0))) {
                        this.editingValue = next;
                    }
                }
            }
            return true;
        }
        return super.charTyped(input);
    }

    public void tick() {
        boolean visible;
        boolean changed;
        super.tick();
        if (this.pageSettings != null) {
            changed = false;
            for (SettingGroup group : this.pageSettings.groups) {
                for (Setting<?> setting : group) {
                    visible = setting.isVisible();
                    if (visible != setting.lastWasVisible) {
                        changed = true;
                    }
                    setting.lastWasVisible = visible;
                }
            }
            if (changed && this.currentPage != null) {
                this.rebuildPage();
            }
        }
        if (this.moduleSettings != null) {
            changed = false;
            for (SettingGroup group : this.moduleSettings.settings.groups) {
                for (Setting<?> setting : group) {
                    visible = setting.isVisible();
                    if (visible != setting.lastWasVisible) {
                        changed = true;
                    }
                    setting.lastWasVisible = visible;
                }
            }
            if (changed) {
                this.rebuildModuleSettingsView();
            }
        }
    }

    private void buildNav() {
        boolean bl;
        String[] preferred;
        this.navItems.clear();
        ArrayList<Category> categories = new ArrayList<Category>();
        for (Category category : Modules.loopCategories()) {
            categories.add(category);
        }
        block1: for (String name : preferred = new String[]{"Combat", "Movement", "Player", "Render", "World", "Misc"}) {
            Iterator it = categories.iterator();
            while (it.hasNext()) {
                Category category = (Category)it.next();
                if (!category.name.equals(name)) continue;
                if (!Modules.get().getGroup(category).isEmpty()) {
                    this.navItems.add(new NavItem(category, category.name, FontAwesomeIcons.iconForCategory(category.name), false));
                }
                it.remove();
                continue block1;
            }
        }
        for (Category category : categories) {
            if (Modules.get().getGroup(category).isEmpty()) continue;
            this.navItems.add(new NavItem(category, category.name, FontAwesomeIcons.iconForCategory(category.name), false));
        }
        boolean bl2 = false;
        for (Module module : Modules.get().getAll()) {
            if (!module.favorite) continue;
            bl = true;
            break;
        }
        if (bl) {
            this.navItems.add(new NavItem(null, "Favorites", 61445, true));
        }
        this.navItems.add(new NavItem(null, "Settings", 61459, SpecialPage.SETTINGS));
        this.navItems.add(new NavItem(null, "GUI", 61948, SpecialPage.GUI));
        this.navItems.add(new NavItem(null, "Configs", 62566, SpecialPage.CONFIGS));
        this.navItems.add(new NavItem(null, "Friends", 61632, SpecialPage.FRIENDS));
    }

    private void refreshModules() {
        this.cards.clear();
        this.currentPage = null;
        this.pageRows.clear();
        this.pageSettings = null;
        this.pageRowScroll = 0.0;
        this.pageRowMaxScroll = 0.0;
        this.commitEdit();
        this.moduleSettings = null;
        this.moduleSettingsRows.clear();
        this.moduleSettingsScroll = 0.0;
        this.moduleSettingsMaxScroll = 0.0;
        this.dragRow = null;
        if (!this.searchText.isBlank()) {
            List<Tuple<Module, String>> found = Modules.get().searchTitles(this.searchText);
            int limit = Config.get().moduleSearchCount.get();
            int added = 0;
            for (Tuple<Module, String> tuple : found) {
                if (added < limit) {
                    if (Config.get().hiddenModules.get().contains(tuple.getA())) continue;
                    this.cards.add(new Card((Module)tuple.getA(), (String)tuple.getB()));
                    ++added;
                    continue;
                }
                break;
            }
        } else if (this.selectedNav >= 0 && this.selectedNav < this.navItems.size()) {
            NavItem item = this.navItems.get(this.selectedNav);
            if (item.page != null) {
                this.currentPage = item.page;
                this.buildPage(item.page);
                return;
            }
            if (item.favorites) {
                for (Module module : Modules.get().getAll()) {
                    if (!module.favorite || Config.get().hiddenModules.get().contains(module)) continue;
                    this.cards.add(new Card(module, module.title));
                }
                this.cards.sort(Comparator.comparing(card -> card.title));
            } else if (item.category != null) {
                for (Module module : Modules.get().getGroup(item.category)) {
                    if (Config.get().hiddenModules.get().contains(module)) continue;
                    this.cards.add(new Card(module, module.title));
                }
            }
        }
        this.contentScroll = 0.0;
        this.targetScroll = 0.0;
        this.contentTransition = 0.0;
    }

    private void selectNav(int index) {
        this.selectedNav = index;
        this.searchText = "";
        this.searchFocused = false;
        this.guiSubPage = GuiSubPage.NONE;
        this.refreshModules();
    }

    private void buildPage(SpecialPage page) {
        this.pageRows.clear();
        this.pageSettings = null;
        switch (page.ordinal()) {
            case 0: {
                this.buildSettingsPage();
                break;
            }
            case 1: {
                this.buildGuiPage();
                break;
            }
            case 2: {
                this.buildConfigsPage();
                break;
            }
            case 3: {
                this.buildFriendsPage();
            }
        }
    }

    private void rebuildPage() {
        if (this.currentPage != null) {
            this.buildPage(this.currentPage);
        }
    }

    private void addSettingsRows(Settings settings) {
        for (SettingGroup group : settings.groups) {
            boolean anyVisible = false;
            for (Setting<?> setting : group) {
                if (!setting.isVisible()) continue;
                anyVisible = true;
                break;
            }
            if (!anyVisible) continue;
            RiseRow groupRow = new RiseRow(RiseRowType.GROUP);
            groupRow.text = group.name;
            this.pageRows.add(groupRow);
            for (Setting<?> setting : group) {
                if (!setting.isVisible()) continue;
                this.pageRows.add(this.rowForSetting(setting));
            }
        }
    }

    private void buildSettingsPage() {
        Config.get().settings.onActivated();
        this.pageSettings = Config.get().settings;
        this.addSettingsRows(this.pageSettings);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
        RiseRow save = new RiseRow(RiseRowType.ACTION);
        save.text = "Save";
        save.icon = 61452;
        save.action = Config.get()::save;
        this.pageRows.add(save);
    }

    private void buildGuiPage() {
        if (this.guiSubPage == GuiSubPage.NONE) {
            RiseRow group = new RiseRow(RiseRowType.GROUP);
            group.text = "Select a section";
            this.pageRows.add(group);
            RiseRow gui = new RiseRow(RiseRowType.ACTION);
            gui.text = "GUI";
            gui.icon = 61459;
            gui.action = () -> {
                this.guiSubPage = GuiSubPage.GUI;
                this.rebuildPage();
            };
            this.pageRows.add(gui);
            RiseRow hud = new RiseRow(RiseRowType.ACTION);
            hud.text = "HUD";
            hud.icon = 61948;
            hud.action = () -> {
                this.guiSubPage = GuiSubPage.HUD;
                this.rebuildPage();
            };
            this.pageRows.add(hud);
        } else if (this.guiSubPage == GuiSubPage.GUI) {
            this.buildGuiSettingsPage();
        } else {
            this.buildHudPage();
        }
    }

    private void addPageBackButton() {
        RiseRow back = new RiseRow(RiseRowType.ACTION);
        back.text = "Back";
        back.icon = 61523;
        back.action = () -> {
            this.guiSubPage = GuiSubPage.NONE;
            this.rebuildPage();
        };
        this.pageRows.add(back);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
    }

    private void buildGuiSettingsPage() {
        this.addPageBackButton();
        this.theme.settings.onActivated();
        this.pageSettings = this.theme.settings;
        RiseRow themeRow = new RiseRow(RiseRowType.ACTION);
        themeRow.text = "Theme: " + GuiThemes.get().name;
        themeRow.icon = 61459;
        themeRow.action = () -> {
            String[] names = GuiThemes.getNames();
            String current = GuiThemes.get().name;
            int index = 0;
            for (int i = 0; i < names.length; ++i) {
                if (!names[i].equals(current)) continue;
                index = i;
                break;
            }
            GuiThemes.select(names[(index + 1) % names.length]);
            MeteorClient.mc.setScreen(null);
            ((Tab)Tabs.get().getFirst()).openScreen(GuiThemes.get());
        };
        this.pageRows.add(themeRow);
        RiseRow resetLayout = new RiseRow(RiseRowType.ACTION);
        resetLayout.text = "Reset Layout";
        resetLayout.icon = 61666;
        resetLayout.action = this.theme::clearWindowConfigs;
        this.pageRows.add(resetLayout);
        RiseRow resetColors = new RiseRow(RiseRowType.ACTION);
        resetColors.text = "Reset Colors";
        resetColors.icon = 61666;
        resetColors.action = this.theme.settings::reset;
        this.pageRows.add(resetColors);
        RiseRow copy = new RiseRow(RiseRowType.ACTION);
        copy.text = "Copy config";
        copy.icon = 61637;
        copy.action = () -> NbtUtils.toClipboard(this.theme);
        this.pageRows.add(copy);
        RiseRow paste = new RiseRow(RiseRowType.ACTION);
        paste.text = "Paste config";
        paste.icon = 61674;
        paste.action = () -> NbtUtils.fromClipboard(this.theme);
        this.pageRows.add(paste);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
        this.addSettingsRows(this.pageSettings);
    }

    private void buildHudPage() {
        this.addPageBackButton();
        Hud hud = Hud.get();
        hud.settings.onActivated();
        this.pageSettings = hud.settings;
        this.addSettingsRows(this.pageSettings);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
        RiseRow edit = new RiseRow(RiseRowType.ACTION);
        edit.text = "Edit";
        edit.icon = 61508;
        edit.action = () -> MeteorClient.mc.setScreen((Screen)new HudEditorScreen(this.theme));
        this.pageRows.add(edit);
        RiseRow active = new RiseRow(RiseRowType.TOGGLE);
        active.text = "Active";
        active.toggleValue = () -> hud.active;
        active.toggleAction = () -> {
            hud.active = !hud.active;
        };
        this.pageRows.add(active);
        RiseRow clear = new RiseRow(RiseRowType.ACTION);
        clear.text = "Clear";
        clear.icon = 61544;
        clear.action = hud::clear;
        this.pageRows.add(clear);
        RiseRow resetElements = new RiseRow(RiseRowType.ACTION);
        resetElements.text = "Reset to default elements";
        resetElements.icon = 61666;
        resetElements.action = hud::resetToDefaultElements;
        this.pageRows.add(resetElements);
    }

    private void buildConfigsPage() {
        this.pageSettings = null;
        RiseRow group = new RiseRow(RiseRowType.GROUP);
        group.text = "Profiles";
        this.pageRows.add(group);
        RiseRow create = new RiseRow(RiseRowType.TEXTBOX);
        create.placeholder = "Create profile...";
        create.filter = Utils::fileNameFilter;
        create.action = () -> {
            String name;
            String string = name = create.text == null ? "" : create.text.trim();
            if (name.isEmpty()) {
                return;
            }
            Profile profile = new Profile();
            profile.name.set(name);
            Profiles.get().add(profile);
            this.rebuildPage();
        };
        this.pageRows.add(create);
        RiseRow importBtn = new RiseRow(RiseRowType.ACTION);
        importBtn.text = "Import";
        importBtn.icon = 61637;
        importBtn.action = () -> {
            try {
                Profile imported = this.importProfile();
                if (imported != null) {
                    MeteorClient.LOG.info("Successfully imported profile '{}'.", (Object)imported.name.get());
                }
                this.rebuildPage();
            }
            catch (Exception e) {
                MeteorClient.LOG.error("Error importing profile", (Throwable)e);
                ((OkPrompt)((OkPrompt)((OkPrompt)((OkPrompt)OkPrompt.create().title("Failure importing profile")).message("There was an error importing the profile.")).message("Error: %s", e.getMessage())).dontShowAgainCheckboxVisible(false)).show();
            }
        };
        this.pageRows.add(importBtn);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
        for (Profile profile : Profiles.get()) {
            RiseRow row = new RiseRow(RiseRowType.PROFILE);
            row.text = profile.name.get();
            row.profile = profile;
            this.pageRows.add(row);
        }
    }

    private Profile importProfile() throws IOException {
        String file = TinyFileDialogs.tinyfd_openFileDialog((CharSequence)"Select profile to import", null, (PointerBuffer)PROFILE_FILTERS, null, (boolean)false);
        if (file == null) {
            return null;
        }
        File profileFile = new File(file);
        CompoundTag nbt = NbtIo.read((Path)profileFile.toPath());
        if (nbt == null) {
            return null;
        }
        Profile p = new Profile();
        Optional<String> parsedName = nbt.getString("name").filter(n -> !n.isEmpty());
        if (parsedName.filter(p.name::set).isEmpty() && !p.name.set(FilenameUtils.removeExtension((String)profileFile.getName()))) {
            throw new IllegalStateException("Imported profile does not have a valid name.");
        }
        File profileFolder = p.getFile().getCanonicalFile();
        if (!profileFolder.getParentFile().equals(Profiles.FOLDER.getCanonicalFile())) {
            throw new IllegalStateException("Imported profile does not have a valid location.");
        }
        profileFolder.mkdirs();
        nbt.remove("name");
        boolean valid = false;
        for (Map.Entry entry : nbt.entrySet()) {
            String filename = (String)entry.getKey();
            if (!filename.endsWith(".nbt") || filename.contains("/") || filename.contains("\\") || new File(filename).isAbsolute()) continue;
            switch (filename) {
                case "hud.nbt": {
                    p.hud.set(true);
                    break;
                }
                case "macros.nbt": {
                    p.macros.set(true);
                    break;
                }
                case "modules.nbt": {
                    p.modules.set(true);
                    break;
                }
                default: {
                    if (!filename.endsWith(".nbt")) break;
                    p.waypoints.set(true);
                }
            }
            File f = new File(profileFolder, filename).getCanonicalFile();
            if (!f.toPath().startsWith(profileFolder.toPath())) continue;
            valid = true;
            NbtIo.writeUnnamedTagWithFallback((Tag)((Tag)entry.getValue()), (DataOutput)new DataOutputStream(new FileOutputStream(f)));
        }
        if (!valid) {
            throw new IllegalStateException("Imported file is not a profile.");
        }
        Profiles.get().getAll().add(p);
        Profiles.get().save();
        return p;
    }

    private void buildFriendsPage() {
        this.pageSettings = null;
        RiseRow group = new RiseRow(RiseRowType.GROUP);
        group.text = "Friends";
        this.pageRows.add(group);
        RiseRow add = new RiseRow(RiseRowType.TEXTBOX);
        add.placeholder = "Add friend...";
        add.action = () -> {
            String name;
            String string = name = add.text == null ? "" : add.text.trim();
            if (name.isEmpty()) {
                return;
            }
            Friend friend = new Friend(name);
            if (Friends.get().add(friend)) {
                MeteorExecutor.execute(() -> {
                    friend.updateInfo();
                    MeteorClient.mc.execute(this::rebuildPage);
                });
                this.rebuildPage();
            }
        };
        this.pageRows.add(add);
        this.pageRows.add(new RiseRow(RiseRowType.SEPARATOR));
        if (Friends.get().isEmpty()) {
            return;
        }
        Friends.get().forEach(friend -> MeteorExecutor.execute(() -> {
            if (friend.headTextureNeedsUpdate()) {
                friend.updateInfo();
            }
        }));
        for (Friend friend2 : Friends.get()) {
            RiseRow row = new RiseRow(RiseRowType.FRIEND);
            row.text = friend2.getName();
            row.friend = friend2;
            row.onDelete = () -> {
                Friends.get().remove(friend2);
                this.rebuildPage();
            };
            this.pageRows.add(row);
        }
    }

    private void layoutPageRows() {
        double y = this.contentTop;
        for (RiseRow row : this.pageRows) {
            row.x = this.contentLeft;
            row.w = this.contentViewW;
            row.h = this.rowHeight(row);
            row.y = y - this.pageRowScroll;
            y += row.h + 4.0 * this.k;
        }
        this.pageRowMaxScroll = Math.max(0.0, y - this.contentBottom);
        this.pageRowScroll = RiseClickGuiScreen.clamp(this.pageRowScroll, 0.0, this.pageRowMaxScroll);
    }

    private void renderPageRow(RiseRow row) {
        switch (row.type.ordinal()) {
            case 0: {
                double ty = row.y;
                for (String line : row.lines) {
                    this.renderer.text(line, row.x, ty, CARD_DESC, false);
                    ty += this.textHeight();
                }
                break;
            }
            case 1: {
                this.renderer.text(row.text, row.x, row.y + (row.h - this.textHeight()) / 2.0, this.accent(), false);
                break;
            }
            case 2: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                this.renderToggle(row.x + row.w - 34.0 * this.k, row.y + row.h / 2.0, (Boolean)row.setting.get());
                break;
            }
            case 3: {
                this.renderModuleSettingsSlider(row);
                break;
            }
            case 4: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                String value = row.setting.get().toString();
                double ew = Math.min(row.w - this.theme.textRenderer().getWidth(row.setting.title) - 20.0 * this.k, Math.max(64.0 * this.k, this.theme.textRenderer().getWidth(value) + 28.0 * this.k));
                double ex = row.x + row.w - ew;
                boolean hovered = this.mouseInside(ex, row.y + (row.h - this.pillH()) / 2.0, ew, this.pillH());
                this.renderPill(ex, row.y + (row.h - this.pillH()) / 2.0, ew, this.pillH(), hovered);
                this.renderer.text(value, ex + (ew - this.theme.textRenderer().getWidth(value)) / 2.0, row.y + (row.h - this.textHeight()) / 2.0, hovered ? this.accent() : CARD_TITLE, false);
                FontAwesomeIcons.render(this.renderer, ex + ew - 13.0 * this.k, row.y + row.h / 2.0, 9.0 * this.k, CARD_DESC, 61560);
                break;
            }
            case 5: {
                this.renderer.text(this.truncate(row.setting.title, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                SettingColor c = (SettingColor)((ColorSetting)row.setting).get();
                double sw = 22.0 * this.k;
                double sx = row.x + row.w - sw;
                double sy = row.y + (row.h - sw) / 2.0;
                boolean hovered = this.mouseInside(sx - 1.0, sy - 1.0, sw + 2.0, sw + 2.0);
                this.rounded(sx - 1.0, sy - 1.0, sw + 2.0, sw + 2.0, 7.0 * this.k, hovered ? this.alpha(this.accent(), 0.7) : new Color(255, 255, 255, 12));
                this.rounded(sx, sy, sw, sw, 6.0 * this.k, new Color(c.r, c.g, c.b, c.a));
                break;
            }
            case 6: {
                this.renderModuleSettingsKeybind(row);
                break;
            }
            case 7: {
                this.renderModuleSettingsString(row);
                break;
            }
            case 8: {
                this.renderModuleSettingsEditPill(row, row.setting.title);
                break;
            }
            case 16: {
                this.renderPageTextBox(row);
                break;
            }
            case 17: {
                this.renderPageAction(row);
                break;
            }
            case 18: {
                this.renderer.text(this.truncate(row.text, row.w - 44.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
                this.renderToggle(row.x + row.w - 34.0 * this.k, row.y + row.h / 2.0, row.toggleValue != null && row.toggleValue.get() != false);
                break;
            }
            case 19: {
                this.renderProfileRow(row);
                break;
            }
            case 20: {
                this.renderFriendRow(row);
                break;
            }
            case 15: {
                this.renderer.quad(row.x, row.y + row.h / 2.0, row.w, 1.0, new Color(255, 255, 255, 8));
            }
        }
    }

    private void renderPageTextBox(RiseRow row) {
        boolean empty;
        boolean focused = row == this.editingRow;
        String text = focused ? this.editingValue : row.text;
        boolean bl = empty = text == null || text.isEmpty();
        String shown = empty ? (row.placeholder != null ? row.placeholder : "") : text;
        double th = 28.0 * this.k;
        double y = row.y + (row.h - th) / 2.0;
        this.rounded(row.x - 1.0, y - 1.0, row.w + 2.0, th + 2.0, 8.0 * this.k, focused ? this.alpha(this.accent(), 0.55) : new Color(255, 255, 255, 8));
        this.rounded(row.x, y, row.w, th, 7.0 * this.k, focused ? this.alpha(this.accent(), 0.15) : new Color(255, 255, 255, 6));
        double tx = row.x + 10.0 * this.k;
        this.renderer.text(this.truncate(shown, row.w - 20.0 * this.k), tx, y + (th - this.textHeight()) / 2.0, empty ? CARD_DESC : CARD_TITLE, false);
        if (focused && System.currentTimeMillis() / 500L % 2L == 0L) {
            double cursorX = tx + this.theme.textRenderer().getWidth(text);
            this.renderer.quad(cursorX, y + 6.0 * this.k, 1.0, th - 12.0 * this.k, this.accent());
        }
    }

    private void renderPageAction(RiseRow row) {
        double textX;
        boolean hovered = this.mouseInside(row.x, row.y + (row.h - this.pillH()) / 2.0, row.w, this.pillH());
        this.renderPill(row.x, row.y + (row.h - this.pillH()) / 2.0, row.w, this.pillH(), hovered);
        if (row.icon != 0) {
            FontAwesomeIcons.render(this.renderer, row.x + 16.0 * this.k, row.y + row.h / 2.0, 12.0 * this.k, hovered ? this.accent() : CARD_DESC, row.icon);
            textX = row.x + 30.0 * this.k;
        } else {
            textX = row.x + (row.w - this.theme.textRenderer().getWidth(row.text)) / 2.0;
        }
        this.renderer.text(this.truncate(row.text, row.w - 44.0 * this.k), textX, row.y + (row.h - this.textHeight()) / 2.0, hovered ? this.accent() : CARD_TITLE, false);
    }

    private void renderProfileRow(RiseRow row) {
        this.renderer.text(this.truncate(row.text, row.w - 180.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double x = row.x + row.w;
        double pw = 52.0 * this.k;
        double gap = 6.0 * this.k;
        double dw = 26.0 * this.k;
        double pillY = row.y + (row.h - this.pillH()) / 2.0;
        double delX = x - dw;
        double loadX = delX - gap - pw;
        double saveX = loadX - gap - pw;
        boolean sh = this.mouseInside(saveX, pillY, pw, this.pillH());
        this.renderPill(saveX, pillY, pw, this.pillH(), sh);
        this.renderer.text("Save", saveX + (pw - this.theme.textRenderer().getWidth("Save")) / 2.0, pillY + (this.pillH() - this.textHeight()) / 2.0, sh ? this.accent() : CARD_TITLE, false);
        boolean lh = this.mouseInside(loadX, pillY, pw, this.pillH());
        this.renderPill(loadX, pillY, pw, this.pillH(), lh);
        this.renderer.text("Load", loadX + (pw - this.theme.textRenderer().getWidth("Load")) / 2.0, pillY + (this.pillH() - this.textHeight()) / 2.0, lh ? this.accent() : CARD_TITLE, false);
        boolean dh = this.mouseInside(delX, pillY, dw, this.pillH());
        this.renderPill(delX, pillY, dw, this.pillH(), dh);
        FontAwesomeIcons.render(this.renderer, delX + dw / 2.0, pillY + this.pillH() / 2.0, 11.0 * this.k, dh ? this.accent() : CARD_DESC, 61453);
    }

    private void renderFriendRow(RiseRow row) {
        this.renderer.text(this.truncate(row.text, row.w - 60.0 * this.k), row.x, row.y + (row.h - this.textHeight()) / 2.0, CARD_TITLE, false);
        double dw = 26.0 * this.k;
        double delX = row.x + row.w - dw;
        double pillY = row.y + (row.h - this.pillH()) / 2.0;
        boolean dh = this.mouseInside(delX, pillY, dw, this.pillH());
        this.renderPill(delX, pillY, dw, this.pillH(), dh);
        FontAwesomeIcons.render(this.renderer, delX + dw / 2.0, pillY + this.pillH() / 2.0, 11.0 * this.k, dh ? this.accent() : CARD_DESC, 61453);
    }

    private boolean handlePageClick(double mx, double my, int button) {
        for (RiseRow row : this.pageRows) {
            if (!RiseClickGuiScreen.mouseInside(row.x, row.y, row.w, row.h, mx, my)) continue;
            this.handlePageRowClick(row, mx, my, button);
            return true;
        }
        return false;
    }

    private void handlePageRowClick(RiseRow row, double mx, double my, int button) {
        switch (row.type.ordinal()) {
            case 2: {
                BoolSetting setting = (BoolSetting)row.setting;
                setting.set((Boolean)setting.get() == false);
                break;
            }
            case 3: {
                this.startSliderDrag(row, mx);
                break;
            }
            case 4: {
                this.cycleEnum(row, button == 0 ? 1 : -1);
                break;
            }
            case 5: {
                MeteorClient.mc.setScreen((Screen)new ColorSettingScreen(this.theme, (ColorSetting)row.setting));
                break;
            }
            case 6: {
                KeybindSetting setting = row.keybindSetting;
                double rx = row.x + row.w - 26.0 * this.k;
                double ry = row.y + (row.h - this.pillH()) / 2.0;
                if (setting.widget == null) break;
                if (RiseClickGuiScreen.mouseInside(rx, ry, 26.0 * this.k, this.pillH(), mx, my)) {
                    setting.widget.resetBind();
                    break;
                }
                ((RiseKeybind)setting.widget).startListening();
                break;
            }
            case 7: {
                this.editingRow = row;
                this.editingValue = (String)row.stringSetting.get();
                break;
            }
            case 8: {
                MeteorClient.mc.setScreen((Screen)new SingleSettingScreen(this.theme, row.setting));
                break;
            }
            case 16: {
                this.editingRow = row;
                this.editingValue = row.text == null ? "" : row.text;
                break;
            }
            case 17: {
                if (row.action == null) break;
                row.action.run();
                break;
            }
            case 18: {
                if (row.toggleAction == null) break;
                row.toggleAction.run();
                break;
            }
            case 19: {
                this.handleProfileRowClick(row, mx, my);
                break;
            }
            case 20: {
                if (row.onDelete == null) break;
                row.onDelete.run();
            }
        }
    }

    private void handleProfileRowClick(RiseRow row, double mx, double my) {
        double x = row.x + row.w;
        double dw = 26.0 * this.k;
        double delX = x - dw;
        double gap = 6.0 * this.k;
        double pw = 52.0 * this.k;
        double loadX = delX - gap - pw;
        double saveX = loadX - gap - pw;
        double pillY = row.y + (row.h - this.pillH()) / 2.0;
        if (RiseClickGuiScreen.mouseInside(saveX, pillY, pw, this.pillH(), mx, my)) {
            row.profile.save();
        } else if (RiseClickGuiScreen.mouseInside(loadX, pillY, pw, this.pillH(), mx, my)) {
            row.profile.load();
        } else if (RiseClickGuiScreen.mouseInside(delX, pillY, dw, this.pillH(), mx, my)) {
            Profiles.get().remove(row.profile);
            this.rebuildPage();
        }
    }

    private void openModuleSettings(Module module) {
        this.moduleSettings = module;
        this.moduleSettingsScroll = 0.0;
        this.buildModuleSettingsView();
        this.contentTransition = 0.0;
    }

    private void closeModuleSettings() {
        this.moduleSettings = null;
        this.moduleSettingsRows.clear();
        this.moduleSettingsScroll = 0.0;
        this.moduleSettingsMaxScroll = 0.0;
        this.dragRow = null;
        this.commitEdit();
    }

    private void rebuildModuleSettingsView() {
        if (this.moduleSettings == null) {
            return;
        }
        this.buildModuleSettingsView();
    }

    private void buildModuleSettingsView() {
        WWidget widget;
        RiseRow row;
        this.moduleSettingsRows.clear();
        this.dragRow = null;
        this.commitEdit();
        Module module = this.moduleSettings;
        module.settings.onActivated();
        double contentW = this.moduleSettingsW > 0.0 ? this.moduleSettingsW - 28.0 * this.k : 200.0;
        String desc = module.description;
        if (desc != null && !desc.isEmpty()) {
            row = new RiseRow(RiseRowType.DESCRIPTION);
            row.lines = this.wrapText(desc, contentW);
            this.moduleSettingsRows.add(row);
        }
        if (module.addon != null && module.addon != MeteorClient.ADDON) {
            row = new RiseRow(RiseRowType.DESCRIPTION);
            row.text = "From: " + module.addon.name;
            row.lines = this.wrapText(row.text, contentW);
            this.moduleSettingsRows.add(row);
        }
        if ((widget = module.getWidget(this.theme)) != null) {
            RiseRow row2 = new RiseRow(RiseRowType.WIDGET);
            row2.moduleWidget = widget;
            this.moduleSettingsRows.add(row2);
        }
        for (SettingGroup group : module.settings.groups) {
            boolean anyVisible = false;
            for (Setting<?> setting : group) {
                if (!setting.isVisible()) continue;
                anyVisible = true;
                break;
            }
            if (!anyVisible) continue;
            RiseRow groupRow = new RiseRow(RiseRowType.GROUP);
            groupRow.text = group.name;
            this.moduleSettingsRows.add(groupRow);
            for (Setting<?> setting : group) {
                if (!setting.isVisible()) continue;
                this.moduleSettingsRows.add(this.rowForSetting(setting));
            }
        }
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.SEPARATOR));
        RiseRow bindHeader = new RiseRow(RiseRowType.GROUP);
        bindHeader.text = "Bind";
        this.moduleSettingsRows.add(bindHeader);
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.BIND));
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.TOBR));
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.CHAT_FEEDBACK));
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.SEPARATOR));
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.ACTIVE));
        this.moduleSettingsRows.add(new RiseRow(RiseRowType.BUTTONS));
    }

    private RiseRow rowForSetting(Setting<?> setting) {
        RiseRow row = new RiseRow(RiseRowType.OTHER);
        row.setting = setting;
        if (setting instanceof BoolSetting) {
            row.type = RiseRowType.BOOL;
        } else if (setting instanceof IntSetting) {
            IntSetting is = (IntSetting)setting;
            row.type = is.noSlider ? RiseRowType.OTHER : RiseRowType.SLIDER;
        } else if (setting instanceof DoubleSetting) {
            DoubleSetting ds = (DoubleSetting)setting;
            row.type = ds.noSlider ? RiseRowType.OTHER : RiseRowType.SLIDER;
        } else if (setting instanceof EnumSetting) {
            row.type = RiseRowType.ENUM;
        } else if (setting instanceof ColorSetting) {
            row.type = RiseRowType.COLOR;
        } else if (setting instanceof KeybindSetting) {
            KeybindSetting keybindSetting = (KeybindSetting)setting;
            row.type = RiseRowType.KEYBIND;
            row.keybindSetting = keybindSetting;
            RiseKeybind keybind = new RiseKeybind((Keybind)keybindSetting.get(), (Keybind)keybindSetting.getDefaultValue());
            keybind.action = keybindSetting::onChanged;
            keybindSetting.widget = keybind;
        } else if (setting instanceof StringSetting) {
            StringSetting stringSetting = (StringSetting)setting;
            row.type = RiseRowType.STRING;
            row.stringSetting = stringSetting;
        }
        return row;
    }

    private void handleModuleSettingsRowClick(RiseRow row, double mx, double my) {
        switch (row.type.ordinal()) {
            case 2: {
                BoolSetting setting = (BoolSetting)row.setting;
                setting.set((Boolean)setting.get() == false);
                break;
            }
            case 3: {
                this.startSliderDrag(row, mx);
                break;
            }
            case 4: {
                this.cycleEnum(row, 1);
                break;
            }
            case 5: {
                MeteorClient.mc.setScreen((Screen)new ColorSettingScreen(this.theme, (ColorSetting)row.setting));
                break;
            }
            case 6: {
                KeybindSetting setting = row.keybindSetting;
                double rx = row.x + row.w - 26.0 * this.k;
                double ry = row.y + (row.h - this.pillH()) / 2.0;
                if (setting.widget == null) break;
                if (RiseClickGuiScreen.mouseInside(rx, ry, 26.0 * this.k, this.pillH(), mx, my)) {
                    setting.widget.resetBind();
                    break;
                }
                ((RiseKeybind)setting.widget).startListening();
                break;
            }
            case 7: {
                this.editingRow = row;
                this.editingValue = (String)row.stringSetting.get();
                break;
            }
            case 8: {
                MeteorClient.mc.setScreen((Screen)new SingleSettingScreen(this.theme, row.setting));
                break;
            }
            case 9: {
                MeteorClient.mc.setScreen((Screen)new ModuleWidgetScreen(this.theme, this.moduleSettings.title, row.moduleWidget));
                break;
            }
            case 10: {
                double rx = row.x + row.w - 26.0 * this.k;
                double ry = row.y + (row.h - this.pillH()) / 2.0;
                if (RiseClickGuiScreen.mouseInside(rx, ry, 26.0 * this.k, this.pillH(), mx, my)) {
                    this.moduleSettings.keybind.set(Keybind.none());
                    break;
                }
                if (Modules.get().isBinding()) break;
                Modules.get().setModuleToBind(this.moduleSettings);
                break;
            }
            case 11: {
                this.moduleSettings.toggleOnBindRelease = !this.moduleSettings.toggleOnBindRelease;
                break;
            }
            case 12: {
                this.moduleSettings.chatFeedback = !this.moduleSettings.chatFeedback;
                break;
            }
            case 13: {
                this.moduleSettings.toggle();
                break;
            }
            case 14: {
                double bw = (row.w - 8.0 * this.k) / 2.0;
                if (RiseClickGuiScreen.mouseInside(row.x, row.y + (row.h - this.pillH()) / 2.0, bw, this.pillH(), mx, my)) {
                    this.moduleSettingsToClipboard();
                    break;
                }
                if (!this.moduleSettingsFromClipboard()) break;
                this.rebuildModuleSettingsView();
            }
        }
    }

    private void cycleEnum(RiseRow row, int direction) {
        EnumSetting setting = (EnumSetting)row.setting;
        Enum current = (Enum)setting.get();
        Enum[] values = (Enum[])current.getDeclaringClass().getEnumConstants();
        int index = 0;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] != current) continue;
            index = i;
            break;
        }
        index = (index + direction + values.length) % values.length;
        setting.set(values[index]);
    }

    private void startSliderDrag(RiseRow row, double mx) {
        this.dragRow = row;
        this.updateSliderDrag(mx, false);
    }

    private void updateSliderDrag(double mx, boolean release) {
        if (this.dragRow == null) {
            return;
        }
        Setting<?> setting = this.dragRow.setting;
        double trackW = this.dragRow.w;
        double t = RiseClickGuiScreen.clamp((mx - this.dragRow.x) / trackW, 0.0, 1.0);
        if (setting instanceof IntSetting) {
            IntSetting is = (IntSetting)setting;
            int v = (int)Math.round((double)is.sliderMin + t * (double)(is.sliderMax - is.sliderMin));
            this.dragPreview = v;
            if (!release) {
                is.set(v);
            }
        } else if (setting instanceof DoubleSetting) {
            DoubleSetting ds = (DoubleSetting)setting;
            double v = ds.sliderMin + t * (ds.sliderMax - ds.sliderMin);
            double factor = Math.pow(10.0, ds.decimalPlaces);
            this.dragPreview = v = (double)Math.round(v * factor) / factor;
            if (release || !ds.onSliderRelease) {
                ds.set(v);
            }
        }
        if (release) {
            this.dragRow = null;
            this.dragPreview = Double.NaN;
        }
    }

    private void commitEdit() {
        this.editingRow = null;
        this.editingValue = null;
    }

    private boolean moduleSettingsToClipboard() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.moduleSettings.name);
        CompoundTag settingsTag = this.moduleSettings.settings.toTag();
        if (!settingsTag.isEmpty()) {
            tag.put("settings", (Tag)settingsTag);
        }
        return NbtUtils.toClipboard(tag);
    }

    private boolean moduleSettingsFromClipboard() {
        CompoundTag tag = NbtUtils.fromClipboard();
        if (tag == null) {
            return false;
        }
        if (!tag.getStringOr("name", "").equals(this.moduleSettings.name)) {
            return false;
        }
        Optional settings = tag.getCompound("settings");
        if (settings.isPresent()) {
            this.moduleSettings.settings.fromTag((CompoundTag)settings.get());
        } else {
            this.moduleSettings.settings.reset();
        }
        return true;
    }

    private void computeLayout() {
        double y;
        double winW = Utils.getWindowWidth();
        double winH = Utils.getWindowHeight();
        double availW = Math.max(winW - 40.0, 200.0);
        double availH = Math.max(winH - 40.0, 200.0);
        double baseK = Math.min(1.0, Math.min(availW / 750.0, availH / 520.0));
        double pop = RiseClickGuiScreen.easeOutBack(RiseClickGuiScreen.clamp(this.animProgress, 0.0, 1.0));
        this.k = Math.min(baseK, baseK * (0.7 + 0.3 * pop));
        double panelGap = 14.0 * this.k;
        this.moduleSettingsPanelW = this.moduleSettings != null ? Math.min(330.0 * this.k, Math.max(availW * 0.22, 250.0)) : 0.0;
        this.containerW = 750.0 * this.k;
        this.containerH = 520.0 * this.k;
        if (this.containerW + panelGap + this.moduleSettingsPanelW > availW) {
            double scale = availW / (this.containerW + panelGap + this.moduleSettingsPanelW);
            this.containerW *= scale;
            this.moduleSettingsPanelW *= scale;
            panelGap *= scale;
        }
        this.containerX = (winW - (this.containerW + panelGap + this.moduleSettingsPanelW)) / 2.0;
        this.containerY = (winH - this.containerH) / 2.0;
        this.sidebarW = 200.0 * this.k;
        this.contentX = this.containerX + this.sidebarW;
        this.contentW = this.containerW - this.sidebarW;
        double navX = this.containerX + 12.0 * this.k;
        double navW = this.sidebarW - 24.0 * this.k;
        this.logoY = y = this.containerY + 20.0 * this.k;
        this.searchX = navX;
        this.searchW = navW;
        this.searchY = y += 96.0 * this.k;
        this.searchH = 31.0 * this.k;
        y += this.searchH + 6.0 * this.k;
        for (NavItem item : this.navItems) {
            item.x = navX;
            item.y = y;
            item.w = navW;
            item.h = 30.0 * this.k;
            y += item.h + 4.0 * this.k;
        }
        double pad = 24.0 * this.k;
        this.contentLeft = this.contentX + pad;
        this.contentTop = this.containerY + pad;
        this.contentRight = this.containerX + this.containerW - pad;
        this.contentBottom = this.containerY + this.containerH - pad;
        this.contentViewW = this.contentRight - this.contentLeft;
        this.contentViewH = this.contentBottom - this.contentTop;
        double cardGap = 12.0 * this.k;
        double cardH = 59.0 * this.k;
        int count = this.cards.size();
        double total = count == 0 ? 0.0 : (double)count * cardH + (double)(count - 1) * cardGap;
        this.maxScroll = Math.max(0.0, total - this.contentViewH);
        this.contentScroll = RiseClickGuiScreen.clamp(this.contentScroll, 0.0, this.maxScroll);
        for (int i = 0; i < count; ++i) {
            Card card = this.cards.get(i);
            card.x = this.contentLeft;
            card.w = this.contentViewW;
            card.h = cardH;
            card.y = this.contentTop + (double)i * (cardH + cardGap) - this.contentScroll;
        }
        if (this.moduleSettings != null) {
            this.moduleSettingsX = this.containerX + this.containerW + panelGap;
            this.moduleSettingsY = this.containerY;
            this.moduleSettingsW = this.moduleSettingsPanelW;
            this.moduleSettingsH = this.containerH;
            double headerH = 38.0 * this.k;
            this.moduleSettingsCloseW = 22.0 * this.k;
            this.moduleSettingsCloseH = 22.0 * this.k;
            this.moduleSettingsCloseX = this.moduleSettingsX + this.moduleSettingsW - this.moduleSettingsCloseW - 10.0 * this.k;
            this.moduleSettingsCloseY = this.moduleSettingsY + (headerH - this.moduleSettingsCloseH) / 2.0;
            this.layoutModuleSettingsRows();
        }
        if (this.currentPage != null) {
            this.layoutPageRows();
        }
    }

    private void layoutModuleSettingsRows() {
        double x = this.moduleSettingsX + 14.0 * this.k;
        double w = this.moduleSettingsW - 28.0 * this.k;
        double headerH = 38.0 * this.k;
        double y = this.moduleSettingsY + headerH + 12.0 * this.k;
        for (RiseRow row : this.moduleSettingsRows) {
            row.x = x;
            row.w = w;
            row.h = this.rowHeight(row);
            row.y = y;
            y += row.h + 4.0 * this.k;
        }
        double bottom = this.moduleSettingsY + this.moduleSettingsH - 12.0 * this.k;
        this.moduleSettingsMaxScroll = Math.max(0.0, y - bottom);
        this.moduleSettingsScroll = RiseClickGuiScreen.clamp(this.moduleSettingsScroll, 0.0, this.moduleSettingsMaxScroll);
    }

    private double rowHeight(RiseRow row) {
        switch (row.type.ordinal()) {
            case 0: {
                int lines = Math.min(row.lines.size(), 3);
                return (double)lines * this.textHeight() + 6.0 * this.k;
            }
            case 3: {
                return 48.0 * this.k;
            }
            case 1: {
                return 30.0 * this.k;
            }
            case 15: {
                return 12.0 * this.k;
            }
        }
        return 36.0 * this.k;
    }

    private double sliderValue(Setting<?> setting) {
        if (setting instanceof IntSetting) {
            IntSetting s = (IntSetting)setting;
            return ((Integer)s.get()).intValue();
        }
        if (setting instanceof DoubleSetting) {
            DoubleSetting s = (DoubleSetting)setting;
            return (Double)s.get();
        }
        return 0.0;
    }

    private double sliderT(Setting<?> setting, double value) {
        if (setting instanceof IntSetting) {
            IntSetting s = (IntSetting)setting;
            if (s.sliderMax - s.sliderMin <= 0) {
                return 0.0;
            }
            return (value - (double)s.sliderMin) / (double)(s.sliderMax - s.sliderMin);
        }
        if (setting instanceof DoubleSetting) {
            DoubleSetting s = (DoubleSetting)setting;
            if (s.sliderMax - s.sliderMin <= 0.0) {
                return 0.0;
            }
            return (value - s.sliderMin) / (s.sliderMax - s.sliderMin);
        }
        return 0.0;
    }

    private String sliderValueText(Setting<?> setting, double value) {
        if (setting instanceof IntSetting) {
            return String.valueOf((int)value);
        }
        if (setting instanceof DoubleSetting) {
            DoubleSetting s = (DoubleSetting)setting;
            return String.format("%." + s.decimalPlaces + "f", value);
        }
        return "";
    }

    private List<String> wrapText(String text, double maxWidth) {
        ArrayList<String> lines = new ArrayList<String>();
        if (text == null || text.isEmpty()) {
            return lines;
        }
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String test;
            String string = test = line.length() == 0 ? word : String.valueOf(line) + " " + word;
            if (line.length() == 0 || this.theme.textRenderer().getWidth(test) <= maxWidth) {
                line.append((String)(line.length() == 0 ? word : " " + word));
                continue;
            }
            lines.add(line.toString());
            line.setLength(0);
            line.append(word);
            if (lines.size() < 3) continue;
            return lines;
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }

    private double textHeight() {
        return 15.0 * this.k;
    }

    private Color accent() {
        GuiTheme guiTheme = this.theme;
        if (guiTheme instanceof MeteorGuiTheme) {
            MeteorGuiTheme meteorTheme = (MeteorGuiTheme)guiTheme;
            return meteorTheme.accentColor.get();
        }
        return ACCENT;
    }

    private String truncate(String text, double maxWidth) {
        if (text == null || text.isEmpty() || maxWidth <= 0.0) {
            return "";
        }
        if (this.theme.textRenderer().getWidth(text) <= maxWidth) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        double dotsWidth = this.theme.textRenderer().getWidth("...");
        for (int i = 0; i < text.length(); ++i) {
            sb.append(text.charAt(i));
            if (!(this.theme.textRenderer().getWidth(sb.toString()) > maxWidth - dotsWidth)) continue;
            sb.setLength(Math.max(0, sb.length() - 1));
            break;
        }
        sb.append("...");
        return sb.toString();
    }

    private boolean mouseInside(double x, double y, double w, double h) {
        return RiseClickGuiScreen.mouseInside(x, y, w, h, this.mouseX, this.mouseY);
    }

    private static boolean mouseInside(double x, double y, double w, double h, double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private void updateAnimations(double delta) {
        double t = Math.max(0.0, delta) / 20.0;
        this.contentTransition = Math.min(1.0, this.contentTransition + t * 8.0);
        double diff = this.targetScroll - this.contentScroll;
        this.contentScroll = Math.abs(diff) < 0.5 ? this.targetScroll : (this.contentScroll += diff * Math.min(1.0, t * 14.0));
        for (Card card : this.cards) {
            card.anim = card.anim + t * 7.0 * (double)(card.module.isActive() ? 1 : -1);
            card.anim = RiseClickGuiScreen.clamp(card.anim, 0.0, 1.0);
            if (!(card.flash > 0.0)) continue;
            card.flash = Math.max(0.0, card.flash - t * 5.0);
        }
    }

    private static double easeOutCubic(double t) {
        t = RiseClickGuiScreen.clamp(t, 0.0, 1.0);
        double inv = 1.0 - t;
        return 1.0 - inv * inv * inv;
    }

    private static double easeOutBack(double t) {
        t = RiseClickGuiScreen.clamp(t, 0.0, 1.0);
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(t - 1.0, 3.0) + c1 * Math.pow(t - 1.0, 2.0);
    }

    private Color lerp(Color a, Color b, double t) {
        t = RiseClickGuiScreen.clamp(t, 0.0, 1.0);
        this.tmpColor.set(a);
        this.tmpColor.r += (int)((double)(b.r - a.r) * t);
        this.tmpColor.g += (int)((double)(b.g - a.g) * t);
        this.tmpColor.b += (int)((double)(b.b - a.b) * t);
        this.tmpColor.a += (int)((double)(b.a - a.a) * t);
        return this.tmpColor;
    }

    private Color tmpColor(Color color, double alpha) {
        this.tmpColor.set(color);
        this.tmpColor.a = (int)((double)color.a * RiseClickGuiScreen.clamp(alpha, 0.0, 1.0));
        return this.tmpColor;
    }

    private Color alpha(Color color, double alpha) {
        return new Color(color.r, color.g, color.b, (int)((double)color.a * RiseClickGuiScreen.clamp(alpha, 0.0, 1.0)));
    }

    private void rounded(double x, double y, double w, double h, double r, Color color) {
        this.rounded(x, y, w, h, r, r, r, r, color);
    }

    private void roundedGradient(double x, double y, double w, double h, double r, Color top, Color bottom) {
        this.roundedGradient(x, y, w, h, r, r, r, r, top, top, bottom, bottom);
    }

    private void roundedGradient(double x, double y, double w, double h, double rTL, double rTR, double rBR, double rBL, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        if (w <= 0.0 || h <= 0.0) {
            return;
        }
        this.renderer.quad(x + rTL, y, w - rTL - rTR, h, topLeft, topRight, bottomRight, bottomLeft);
        this.renderer.quad(x, y + rTL, rTL, h - rTL - rBL, topLeft, bottomLeft);
        this.renderer.quad(x + w - rTR, y + rTR, rTR, h - rTR - rBR, topRight, bottomRight);
        if (rTL > 0.0) {
            this.renderer.quad(x, y, rTL * 2.0, rTL * 2.0, this.cornerTexture(rTL, w, h), topLeft);
        }
        if (rTR > 0.0) {
            this.renderer.quad(x + w - rTR * 2.0, y, rTR * 2.0, rTR * 2.0, this.cornerTexture(rTR, w, h), topRight);
        }
        if (rBR > 0.0) {
            this.renderer.quad(x + w - rBR * 2.0, y + h - rBR * 2.0, rBR * 2.0, rBR * 2.0, this.cornerTexture(rBR, w, h), bottomRight);
        }
        if (rBL > 0.0) {
            this.renderer.quad(x, y + h - rBL * 2.0, rBL * 2.0, rBL * 2.0, this.cornerTexture(rBL, w, h), bottomLeft);
        }
    }

    private void rounded(double x, double y, double w, double h, double rTL, double rTR, double rBR, double rBL, Color color) {
        if (w <= 0.0 || h <= 0.0) {
            return;
        }
        this.renderer.quad(x + rTL, y, w - rTL - rTR, h, color);
        this.renderer.quad(x, y + rTL, rTL, h - rTL - rBL, color);
        this.renderer.quad(x + w - rTR, y + rTR, rTR, h - rTR - rBR, color);
        if (rTL > 0.0) {
            this.renderer.quad(x, y, rTL * 2.0, rTL * 2.0, this.cornerTexture(rTL, w, h), color);
        }
        if (rTR > 0.0) {
            this.renderer.quad(x + w - rTR * 2.0, y, rTR * 2.0, rTR * 2.0, this.cornerTexture(rTR, w, h), color);
        }
        if (rBR > 0.0) {
            this.renderer.quad(x + w - rBR * 2.0, y + h - rBR * 2.0, rBR * 2.0, rBR * 2.0, this.cornerTexture(rBR, w, h), color);
        }
        if (rBL > 0.0) {
            this.renderer.quad(x, y + h - rBL * 2.0, rBL * 2.0, rBL * 2.0, this.cornerTexture(rBL, w, h), color);
        }
    }

    private GuiTexture cornerTexture(double r, double w, double h) {
        if (r >= w / 2.0 && r >= h / 2.0) {
            return GuiRenderer.CIRCLE;
        }
        return GuiRenderer.CIRCLE_SOFT;
    }

    static {
        ByteBuffer pngFilter = MemoryUtil.memASCII((CharSequence)"*.nbt");
        PROFILE_FILTERS.put(pngFilter);
        PROFILE_FILTERS.rewind();
    }

    private static enum GuiSubPage {
        NONE,
        GUI,
        HUD;

    }

    private static final class NavItem {
        final Category category;
        final boolean favorites;
        final String name;
        final int icon;
        final SpecialPage page;
        double x;
        double y;
        double w;
        double h;

        NavItem(Category category, String name, int icon, boolean favorites) {
            this(category, name, icon, favorites, null);
        }

        NavItem(String name, int icon, SpecialPage page) {
            this(null, name, icon, false, page);
        }

        NavItem(Category category, String name, int icon, SpecialPage page) {
            this(category, name, icon, false, page);
        }

        NavItem(Category category, String name, int icon, boolean favorites, SpecialPage page) {
            this.category = category;
            this.name = name;
            this.icon = icon;
            this.favorites = favorites;
            this.page = page;
        }
    }

    private static enum SpecialPage {
        SETTINGS,
        GUI,
        CONFIGS,
        FRIENDS;

    }

    private static final class RiseRow {
        RiseRowType type;
        Setting<?> setting;
        KeybindSetting keybindSetting;
        StringSetting stringSetting;
        WWidget moduleWidget;
        Profile profile;
        Friend friend;
        String text;
        String placeholder;
        List<String> lines;
        Runnable action;
        Runnable onDelete;
        CharFilter filter;
        Supplier<Boolean> toggleValue;
        Runnable toggleAction;
        int icon;
        double x;
        double y;
        double w;
        double h;

        RiseRow(RiseRowType type) {
            this.type = type;
        }
    }

    private static final class Card {
        final Module module;
        final String title;
        double x;
        double y;
        double w;
        double h;
        double anim;
        double flash;

        Card(Module module, String title) {
            this.module = module;
            this.title = title;
            this.anim = module.isActive() ? 1.0 : 0.0;
        }
    }

    private static enum RiseRowType {
        DESCRIPTION,
        GROUP,
        BOOL,
        SLIDER,
        ENUM,
        COLOR,
        KEYBIND,
        STRING,
        OTHER,
        WIDGET,
        BIND,
        TOBR,
        CHAT_FEEDBACK,
        ACTIVE,
        BUTTONS,
        SEPARATOR,
        TEXTBOX,
        ACTION,
        TOGGLE,
        PROFILE,
        FRIEND;

    }

    private static final class RiseKeybind
    extends WKeybind {
        RiseKeybind(Keybind keybind, Keybind defaultValue) {
            super(keybind, defaultValue);
        }

        @Override
        public void init() {
        }

        public void startListening() {
            this.listening = true;
        }

        @Override
        public void reset() {
            this.listening = false;
            if (Modules.get().isBinding()) {
                Modules.get().setModuleToBind(null);
            }
            if (this.action != null) {
                this.action.run();
            }
        }
    }

    private static final class SingleSettingScreen
    extends WindowScreen {
        private final Setting<?> setting;

        SingleSettingScreen(GuiTheme theme, Setting<?> setting) {
            super(theme, setting.title);
            this.setting = setting;
        }

        @Override
        public void initWidgets() {
            Settings settings = new Settings();
            settings.createGroup("General").add(this.setting);
            this.add(this.theme.settings(settings)).expandX();
        }
    }

    private static final class ModuleWidgetScreen
    extends WindowScreen {
        ModuleWidgetScreen(GuiTheme theme, String title, WWidget widget) {
            super(theme, title);
            this.add(widget).expandX();
        }

        @Override
        public void initWidgets() {
        }
    }
}

