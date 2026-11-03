/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap
 */
package radmin.client.gui.tabs;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.builtin.ConfigTab;
import radmin.client.gui.tabs.builtin.FriendsTab;
import radmin.client.gui.tabs.builtin.GuiTab;
import radmin.client.gui.tabs.builtin.HudTab;
import radmin.client.gui.tabs.builtin.ModulesTab;
import radmin.client.gui.tabs.builtin.PathManagerTab;
import radmin.client.gui.tabs.builtin.ProfilesTab;
import radmin.client.pathing.PathManagers;
import radmin.client.utils.PreInit;

public class Tabs {
    private static final List<Tab> tabs = new ArrayList<Tab>();
    private static final Reference2ReferenceOpenHashMap<Class<? extends Tab>, Tab> tabInstances = new Reference2ReferenceOpenHashMap();

    private Tabs() {
    }

    @PreInit(dependencies={PathManagers.class})
    public static void init() {
        Tabs.add(new ModulesTab());
        Tabs.add(new ConfigTab());
        Tabs.add(new GuiTab());
        Tabs.add(new HudTab());
        Tabs.add(new FriendsTab());
        Tabs.add(new ProfilesTab());
        if (PathManagers.get().getSettings().get().sizeGroups() > 0) {
            Tabs.add(new PathManagerTab());
        }
    }

    public static void add(Tab tab) {
        tabs.add(tab);
        tabInstances.put(tab.getClass(), (Object)tab);
    }

    public static List<Tab> get() {
        return tabs;
    }

    public static Tab get(Class<? extends Tab> klass) {
        return (Tab)tabInstances.get(klass);
    }
}

