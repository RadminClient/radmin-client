/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap
 *  meteordevelopment.orbit.EventHandler
 */
package radmin.client.systems;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import meteordevelopment.orbit.EventHandler;
import radmin.client.MeteorClient;
import radmin.client.events.game.GameLeftEvent;
import radmin.client.systems.System;
import radmin.client.systems.accounts.Accounts;
import radmin.client.systems.config.Config;
import radmin.client.systems.friends.Friends;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.macros.Macros;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.profiles.Profiles;
import radmin.client.systems.proxies.Proxies;
import radmin.client.systems.waypoints.Waypoints;

public class Systems {
    private static final Map<Class<? extends System>, System<?>> systems = new Reference2ReferenceOpenHashMap();
    private static final List<Runnable> preLoadTasks = new ArrayList<Runnable>(1);

    public static void addPreLoadTask(Runnable task) {
        preLoadTasks.add(task);
    }

    public static void init() {
        Systems.add(new Modules());
        Config config = new Config();
        System<?> configSystem = Systems.add(config);
        configSystem.init();
        configSystem.load();
        config.settings.registerColorSettings(null);
        Systems.add(new Macros());
        Systems.add(new Friends());
        Systems.add(new Accounts());
        Systems.add(new Waypoints());
        Systems.add(new Profiles());
        Systems.add(new Proxies());
        Systems.add(new Hud());
        MeteorClient.EVENT_BUS.subscribe(Systems.class);
    }

    public static System<?> add(System<?> system) {
        systems.put(system.getClass(), system);
        MeteorClient.EVENT_BUS.subscribe(system);
        system.init();
        return system;
    }

    @EventHandler
    private static void onGameLeft(GameLeftEvent event) {
        Systems.save();
    }

    public static void save(File folder) {
        long start = java.lang.System.currentTimeMillis();
        MeteorClient.LOG.info("Saving");
        for (System<?> system : systems.values()) {
            system.save(folder);
        }
        MeteorClient.LOG.info("Saved in {} milliseconds.", (Object)(java.lang.System.currentTimeMillis() - start));
    }

    public static void save() {
        Systems.save(null);
    }

    public static void load(File folder) {
        long start = java.lang.System.currentTimeMillis();
        MeteorClient.LOG.info("Loading");
        for (Runnable runnable : preLoadTasks) {
            runnable.run();
        }
        for (System system : systems.values()) {
            system.load(folder);
        }
        MeteorClient.LOG.info("Loaded in {} milliseconds", (Object)(java.lang.System.currentTimeMillis() - start));
    }

    public static void load() {
        Systems.load(null);
    }

    public static <T extends System<?>> T get(Class<T> klass) {
        return (T)systems.get(klass);
    }
}

