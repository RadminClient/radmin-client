/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.PointerBuffer
 *  org.lwjgl.system.MemoryUtil
 *  org.lwjgl.util.tinyfd.TinyFileDialogs
 */
package radmin.client.gui.screens;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.screens.EditSystemScreen;
import radmin.client.gui.screens.ProxiesImportScreen;
import radmin.client.gui.widgets.WLabel;
import radmin.client.gui.widgets.containers.WContainer;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WCheckbox;
import radmin.client.gui.widgets.pressable.WConfirmedButton;
import radmin.client.gui.widgets.pressable.WMinus;
import radmin.client.settings.Settings;
import radmin.client.systems.proxies.Proxies;
import radmin.client.systems.proxies.Proxy;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.network.MeteorExecutor;

public class ProxiesScreen
extends WindowScreen {
    private final List<WCheckbox> checkboxes = new ArrayList<WCheckbox>();
    private final WButton refreshButton = this.theme.button("Refresh");
    private final WConfirmedButton cleanButton = this.theme.confirmedButton("Cleanup", "Confirm");
    private Map<Proxy, WLabel> statuses = new HashMap<Proxy, WLabel>();
    private int timer = 0;

    public ProxiesScreen(GuiTheme theme) {
        super(theme, "Proxies");
    }

    @Override
    public void initWidgets() {
        WTable table = this.add(this.theme.table()).expandX().minWidth(400.0).widget();
        this.initTable(table);
        this.add(this.theme.horizontalSeparator()).expandX();
        WHorizontalList l = this.add(this.theme.horizontalList()).expandX().widget();
        WButton newBtn = l.add(this.theme.button("New")).expandX().widget();
        newBtn.action = () -> MeteorClient.mc.setScreen((Screen)new EditProxyScreen(this.theme, null, this::reload));
        PointerBuffer filters = BufferUtils.createPointerBuffer((int)1);
        ByteBuffer txtFilter = MemoryUtil.memASCII((CharSequence)"*.txt");
        filters.put(txtFilter);
        filters.rewind();
        WButton importBtn = l.add(this.theme.button("Import")).expandX().widget();
        importBtn.action = () -> {
            String selectedFile = TinyFileDialogs.tinyfd_openFileDialog((CharSequence)"Import Proxies", null, (PointerBuffer)filters, null, (boolean)false);
            if (selectedFile != null) {
                File file = new File(selectedFile);
                MeteorClient.mc.setScreen((Screen)new ProxiesImportScreen(this.theme, file));
            }
        };
        l.add(this.refreshButton).expandX();
        this.refreshButton.action = () -> Proxies.get().checkProxies(true);
        l.add(this.cleanButton).expandX();
        this.cleanButton.action = () -> {
            if (Proxies.get().refreshing) {
                return;
            }
            Proxies.get().clean();
            this.initTable(table);
        };
        WButton configButton = l.add(this.theme.button(GuiRenderer.EDIT)).widget();
        configButton.action = () -> MeteorClient.mc.setScreen((Screen)new ConfigScreen(this.theme));
        configButton.tooltip = "Proxies Config";
    }

    private void initTable(WTable table) {
        table.clear();
        if (Proxies.get().isEmpty()) {
            return;
        }
        this.statuses = new HashMap<Proxy, WLabel>(Proxies.get().size(), 1.0f);
        for (Proxy proxy : Proxies.get()) {
            WCheckbox enabled = table.add(this.theme.checkbox(proxy.enabled.get())).widget();
            this.checkboxes.add(enabled);
            enabled.action = () -> {
                boolean checked = enabled.checked;
                Proxies.get().setEnabled(proxy, checked);
                for (WCheckbox checkbox : this.checkboxes) {
                    checkbox.checked = false;
                }
                enabled.checked = checked;
            };
            WLabel name = table.add(this.theme.label(proxy.name.get())).widget();
            name.color = this.theme.textColor();
            WLabel type = table.add(this.theme.label("(" + String.valueOf((Object)proxy.type.get()) + ")")).widget();
            type.color = this.theme.textSecondaryColor();
            WHorizontalList ipList = table.add(this.theme.horizontalList()).expandCellX().widget();
            ipList.spacing = 0.0;
            ipList.add(this.theme.label(proxy.address.get()));
            ipList.add(this.theme.label((String)":")).widget().color = this.theme.textSecondaryColor();
            ipList.add(this.theme.label(Integer.toString(proxy.port.get())));
            Object s = proxy.status == Proxy.Status.ALIVE ? proxy.latency + "ms" : proxy.status.toString();
            WLabel status = table.add(this.theme.label((String)s)).widget();
            status.color = proxy.status.getColor();
            this.statuses.put(proxy, status);
            WButton refresh = table.add(this.theme.button(GuiRenderer.RESET)).widget();
            refresh.action = () -> MeteorExecutor.execute(proxy::checkStatus);
            refresh.tooltip = "Refresh";
            WButton edit = table.add(this.theme.button(GuiRenderer.EDIT)).widget();
            edit.action = () -> MeteorClient.mc.setScreen((Screen)new EditProxyScreen(this.theme, proxy, this::reload));
            WMinus remove = table.add(this.theme.minus()).widget();
            remove.action = () -> {
                Proxies.get().remove(proxy);
                this.reload();
            };
            table.row();
        }
    }

    public void tick() {
        if (Proxies.get().refreshing) {
            if (this.cleanButton.getText().equals("Cleanup")) {
                this.cleanButton.set("---", "---");
            }
            if (this.timer > 2) {
                this.refreshButton.set(this.getNext(this.refreshButton));
                this.timer = 0;
            } else {
                ++this.timer;
            }
        } else {
            if (!this.refreshButton.getText().equals("Refresh")) {
                this.refreshButton.set("Refresh");
            }
            if (!this.cleanButton.getText().equals("Cleanup")) {
                this.cleanButton.set("Cleanup", "Confirm");
            }
        }
        for (Map.Entry<Proxy, WLabel> entry : this.statuses.entrySet()) {
            Proxy proxy = entry.getKey();
            WLabel label = entry.getValue();
            if (label.get().equals(proxy.status.toString())) continue;
            label.set((String)(proxy.status == Proxy.Status.ALIVE ? proxy.latency + "ms" : proxy.status.toString()));
            label.color = proxy.status.getColor();
        }
    }

    private String getNext(WButton b) {
        return switch (b.getText()) {
            case "Refresh", "oo0" -> "ooo";
            case "ooo" -> "0oo";
            case "0oo" -> "o0o";
            case "o0o" -> "oo0";
            default -> "Refresh";
        };
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Proxies.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Proxies.get());
    }

    protected static class EditProxyScreen
    extends EditSystemScreen<Proxy> {
        public EditProxyScreen(GuiTheme theme, Proxy value, Runnable reload) {
            super(theme, value, reload);
        }

        @Override
        public Proxy create() {
            return new Proxy.Builder().build();
        }

        @Override
        public boolean save() {
            MeteorExecutor.execute(((Proxy)this.value)::checkStatus);
            return ((Proxy)this.value).resolveAddress() && (!this.isNew || Proxies.get().add((Proxy)this.value));
        }

        @Override
        public Settings getSettings() {
            return ((Proxy)this.value).settings;
        }
    }

    private static class ConfigScreen
    extends WindowScreen {
        private WContainer settingsContainer;

        public ConfigScreen(GuiTheme theme) {
            super(theme, "Proxies Config");
        }

        @Override
        public void initWidgets() {
            this.settingsContainer = this.add(this.theme.verticalList()).expandX().minWidth(400.0).widget();
            this.settingsContainer.add(this.theme.settings(Proxies.get().settings)).expandX();
        }

        public void tick() {
            Proxies.get().settings.tick(this.settingsContainer, this.theme);
        }
    }
}

