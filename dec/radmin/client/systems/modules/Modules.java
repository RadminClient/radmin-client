/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.util.Tuple
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.systems.modules;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.Nullable;
import radmin.client.MeteorClient;
import radmin.client.events.game.GameJoinedEvent;
import radmin.client.events.game.GameLeftEvent;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.events.meteor.ActiveModulesChangedEvent;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.events.meteor.ModuleBindChangedEvent;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.pathing.BaritoneUtils;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.System;
import radmin.client.systems.Systems;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Category;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.combat.AnchorAura;
import radmin.client.systems.modules.combat.AntiAnvil;
import radmin.client.systems.modules.combat.AntiBed;
import radmin.client.systems.modules.combat.ArrowDodge;
import radmin.client.systems.modules.combat.AttributeSwap;
import radmin.client.systems.modules.combat.AutoAnvil;
import radmin.client.systems.modules.combat.AutoArmor;
import radmin.client.systems.modules.combat.AutoCity;
import radmin.client.systems.modules.combat.AutoEXP;
import radmin.client.systems.modules.combat.AutoLog;
import radmin.client.systems.modules.combat.AutoTotem;
import radmin.client.systems.modules.combat.AutoTrap;
import radmin.client.systems.modules.combat.AutoWeapon;
import radmin.client.systems.modules.combat.AutoWeb;
import radmin.client.systems.modules.combat.BedAura;
import radmin.client.systems.modules.combat.BowAimbot;
import radmin.client.systems.modules.combat.BowSpam;
import radmin.client.systems.modules.combat.Burrow;
import radmin.client.systems.modules.combat.Criticals;
import radmin.client.systems.modules.combat.CrystalAura;
import radmin.client.systems.modules.combat.Hitboxes;
import radmin.client.systems.modules.combat.HoleFiller;
import radmin.client.systems.modules.combat.KillAura;
import radmin.client.systems.modules.combat.Offhand;
import radmin.client.systems.modules.combat.Quiver;
import radmin.client.systems.modules.combat.SelfAnvil;
import radmin.client.systems.modules.combat.SelfTrap;
import radmin.client.systems.modules.combat.SelfWeb;
import radmin.client.systems.modules.combat.Surround;
import radmin.client.systems.modules.misc.AntiPacketKick;
import radmin.client.systems.modules.misc.AutoReconnect;
import radmin.client.systems.modules.misc.BetterBeacons;
import radmin.client.systems.modules.misc.BetterChat;
import radmin.client.systems.modules.misc.BookBot;
import radmin.client.systems.modules.misc.DiscordPresence;
import radmin.client.systems.modules.misc.InventoryTweaks;
import radmin.client.systems.modules.misc.MessageAura;
import radmin.client.systems.modules.misc.Notebot;
import radmin.client.systems.modules.misc.Notifier;
import radmin.client.systems.modules.misc.PacketCanceller;
import radmin.client.systems.modules.misc.PacketLogger;
import radmin.client.systems.modules.misc.ServerSpoof;
import radmin.client.systems.modules.misc.SoundBlocker;
import radmin.client.systems.modules.misc.Spam;
import radmin.client.systems.modules.misc.swarm.Swarm;
import radmin.client.systems.modules.movement.AirJump;
import radmin.client.systems.modules.movement.Anchor;
import radmin.client.systems.modules.movement.AntiVoid;
import radmin.client.systems.modules.movement.AutoJump;
import radmin.client.systems.modules.movement.AutoWalk;
import radmin.client.systems.modules.movement.AutoWasp;
import radmin.client.systems.modules.movement.Blink;
import radmin.client.systems.modules.movement.ClickTP;
import radmin.client.systems.modules.movement.ElytraBoost;
import radmin.client.systems.modules.movement.EntityControl;
import radmin.client.systems.modules.movement.FastClimb;
import radmin.client.systems.modules.movement.Flight;
import radmin.client.systems.modules.movement.GUIMove;
import radmin.client.systems.modules.movement.HighJump;
import radmin.client.systems.modules.movement.Jesus;
import radmin.client.systems.modules.movement.LongJump;
import radmin.client.systems.modules.movement.NoFall;
import radmin.client.systems.modules.movement.NoSlow;
import radmin.client.systems.modules.movement.Parkour;
import radmin.client.systems.modules.movement.ReverseStep;
import radmin.client.systems.modules.movement.SafeWalk;
import radmin.client.systems.modules.movement.Scaffold;
import radmin.client.systems.modules.movement.Slippy;
import radmin.client.systems.modules.movement.Sneak;
import radmin.client.systems.modules.movement.Spider;
import radmin.client.systems.modules.movement.Sprint;
import radmin.client.systems.modules.movement.Step;
import radmin.client.systems.modules.movement.TridentBoost;
import radmin.client.systems.modules.movement.Velocity;
import radmin.client.systems.modules.movement.elytrafly.ElytraFly;
import radmin.client.systems.modules.movement.speed.Speed;
import radmin.client.systems.modules.player.AirPlace;
import radmin.client.systems.modules.player.AntiAFK;
import radmin.client.systems.modules.player.AntiHunger;
import radmin.client.systems.modules.player.AutoClicker;
import radmin.client.systems.modules.player.AutoEat;
import radmin.client.systems.modules.player.AutoFish;
import radmin.client.systems.modules.player.AutoGap;
import radmin.client.systems.modules.player.AutoMend;
import radmin.client.systems.modules.player.AutoReplenish;
import radmin.client.systems.modules.player.AutoRespawn;
import radmin.client.systems.modules.player.AutoTool;
import radmin.client.systems.modules.player.BreakDelay;
import radmin.client.systems.modules.player.ChestSwap;
import radmin.client.systems.modules.player.EXPThrower;
import radmin.client.systems.modules.player.FakePlayer;
import radmin.client.systems.modules.player.FastUse;
import radmin.client.systems.modules.player.GhostHand;
import radmin.client.systems.modules.player.InstantRebreak;
import radmin.client.systems.modules.player.LiquidInteract;
import radmin.client.systems.modules.player.MiddleClickExtra;
import radmin.client.systems.modules.player.Multitask;
import radmin.client.systems.modules.player.NameProtect;
import radmin.client.systems.modules.player.NoInteract;
import radmin.client.systems.modules.player.NoMiningTrace;
import radmin.client.systems.modules.player.NoRotate;
import radmin.client.systems.modules.player.NoStatusEffects;
import radmin.client.systems.modules.player.Portals;
import radmin.client.systems.modules.player.PotionSaver;
import radmin.client.systems.modules.player.Reach;
import radmin.client.systems.modules.player.Rotation;
import radmin.client.systems.modules.player.SpeedMine;
import radmin.client.systems.modules.render.Arrows;
import radmin.client.systems.modules.render.BetterTab;
import radmin.client.systems.modules.render.BetterTooltips;
import radmin.client.systems.modules.render.BlockSelection;
import radmin.client.systems.modules.render.Blur;
import radmin.client.systems.modules.render.BossStack;
import radmin.client.systems.modules.render.Breadcrumbs;
import radmin.client.systems.modules.render.BreakIndicators;
import radmin.client.systems.modules.render.CameraTweaks;
import radmin.client.systems.modules.render.Chams;
import radmin.client.systems.modules.render.CityESP;
import radmin.client.systems.modules.render.ESP;
import radmin.client.systems.modules.render.EntityOwner;
import radmin.client.systems.modules.render.FireworkESP;
import radmin.client.systems.modules.render.FreeLook;
import radmin.client.systems.modules.render.Freecam;
import radmin.client.systems.modules.render.Fullbright;
import radmin.client.systems.modules.render.HandView;
import radmin.client.systems.modules.render.HitBubbles;
import radmin.client.systems.modules.render.HitMarker;
import radmin.client.systems.modules.render.HoleESP;
import radmin.client.systems.modules.render.ItemHighlight;
import radmin.client.systems.modules.render.ItemPhysics;
import radmin.client.systems.modules.render.JumpCircle;
import radmin.client.systems.modules.render.KillEffect;
import radmin.client.systems.modules.render.LightOverlay;
import radmin.client.systems.modules.render.LogoutSpots;
import radmin.client.systems.modules.render.Nametags;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.systems.modules.render.PopChams;
import radmin.client.systems.modules.render.SeeInvisibles;
import radmin.client.systems.modules.render.StorageESP;
import radmin.client.systems.modules.render.TargetESP;
import radmin.client.systems.modules.render.TimeChanger;
import radmin.client.systems.modules.render.Tracers;
import radmin.client.systems.modules.render.Trail;
import radmin.client.systems.modules.render.Trajectories;
import radmin.client.systems.modules.render.TunnelESP;
import radmin.client.systems.modules.render.VoidESP;
import radmin.client.systems.modules.render.WallHack;
import radmin.client.systems.modules.render.WaypointsModule;
import radmin.client.systems.modules.render.WeatherChanger;
import radmin.client.systems.modules.render.Xray;
import radmin.client.systems.modules.render.Zoom;
import radmin.client.systems.modules.render.blockesp.BlockESP;
import radmin.client.systems.modules.render.marker.Marker;
import radmin.client.systems.modules.world.Ambience;
import radmin.client.systems.modules.world.AutoBreed;
import radmin.client.systems.modules.world.AutoBrewer;
import radmin.client.systems.modules.world.AutoMount;
import radmin.client.systems.modules.world.AutoNametag;
import radmin.client.systems.modules.world.AutoShearer;
import radmin.client.systems.modules.world.AutoSign;
import radmin.client.systems.modules.world.AutoSmelter;
import radmin.client.systems.modules.world.BuildHeight;
import radmin.client.systems.modules.world.Collisions;
import radmin.client.systems.modules.world.EChestFarmer;
import radmin.client.systems.modules.world.EndermanLook;
import radmin.client.systems.modules.world.Excavator;
import radmin.client.systems.modules.world.Flamethrower;
import radmin.client.systems.modules.world.HighwayBuilder;
import radmin.client.systems.modules.world.InfinityMiner;
import radmin.client.systems.modules.world.LiquidFiller;
import radmin.client.systems.modules.world.NoGhostBlocks;
import radmin.client.systems.modules.world.Nuker;
import radmin.client.systems.modules.world.PacketMine;
import radmin.client.systems.modules.world.SpawnProofer;
import radmin.client.systems.modules.world.StashFinder;
import radmin.client.systems.modules.world.Timer;
import radmin.client.systems.modules.world.VeinMiner;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.misc.ValueComparableMap;
import radmin.client.utils.misc.input.Input;
import radmin.client.utils.misc.input.KeyAction;

public class Modules
extends System<Modules> {
    private static final List<Category> CATEGORIES = new ArrayList<Category>();
    private final Map<Class<? extends Module>, Module> moduleInstances = new Reference2ReferenceOpenHashMap();
    private final Map<Category, List<Module>> groups = new Reference2ReferenceOpenHashMap();
    private final List<Module> active = new ArrayList<Module>();
    private Module moduleToBind;
    private boolean awaitingKeyRelease = false;

    public Modules() {
        super("modules");
    }

    public static Modules get() {
        return Systems.get(Modules.class);
    }

    @Override
    public void init() {
        this.initCombat();
        this.initPlayer();
        this.initMovement();
        this.initRender();
        this.initWorld();
        this.initMisc();
    }

    @Override
    public void load(File folder) {
        for (Module module : this.getAll()) {
            for (SettingGroup group : module.settings) {
                for (Setting<?> setting : group) {
                    setting.reset();
                }
            }
        }
        super.load(folder);
    }

    public void sortModules() {
        for (List<Module> modules : this.groups.values()) {
            modules.sort(Comparator.comparing(o -> o.title));
        }
    }

    public static void registerCategory(Category category) {
        if (!Categories.REGISTERING) {
            throw new RuntimeException("Modules.registerCategory - Cannot register category outside of onRegisterCategories callback.");
        }
        CATEGORIES.add(category);
    }

    public static Iterable<Category> loopCategories() {
        return CATEGORIES;
    }

    @Nullable
    public <T extends Module> T get(Class<T> klass) {
        return (T)this.moduleInstances.get(klass);
    }

    public <T extends Module> Optional<T> getOptional(Class<T> klass) {
        return Optional.ofNullable(this.get(klass));
    }

    @Nullable
    public Module get(String name) {
        for (Module module : this.moduleInstances.values()) {
            if (!module.name.equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = this.get(klass);
        return module != null && module.isActive();
    }

    public List<Module> getGroup(Category category2) {
        return this.groups.computeIfAbsent(category2, category -> new ArrayList());
    }

    public Collection<Module> getAll() {
        return this.moduleInstances.values();
    }

    public int getCount() {
        return this.moduleInstances.size();
    }

    public List<Module> getActive() {
        return this.active;
    }

    public List<Tuple<Module, String>> searchTitles(String text) {
        HashMap<Tuple, Integer> modules = new HashMap<Tuple, Integer>();
        for (Module module : this.moduleInstances.values()) {
            Object title = module.title;
            int score = Utils.searchLevenshteinDefault((String)title, text, false);
            if (Config.get().moduleAliases.get().booleanValue()) {
                for (String alias : module.aliases) {
                    int aliasScore = Utils.searchLevenshteinDefault(alias, text, false);
                    if (aliasScore >= score) continue;
                    title = module.title + " (" + alias + ")";
                    score = aliasScore;
                }
            }
            modules.put(new Tuple((Object)module, title), score);
        }
        ArrayList<Tuple<Module, String>> l = new ArrayList<Tuple<Module, String>>(modules.keySet());
        l.sort(Comparator.comparingInt(modules::get));
        return l;
    }

    public Set<Module> searchSettingTitles(String text) {
        ValueComparableMap modules = new ValueComparableMap(Comparator.naturalOrder());
        for (Module module : this.moduleInstances.values()) {
            int lowest = Integer.MAX_VALUE;
            for (SettingGroup sg : module.settings) {
                for (Setting<?> setting : sg) {
                    int score = Utils.searchLevenshteinDefault(setting.title, text, false);
                    if (score >= lowest) continue;
                    lowest = score;
                }
            }
            modules.put(module, modules.getOrDefault(module, 0) + lowest);
        }
        return modules.keySet();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void addActive(Module module) {
        List<Module> list = this.active;
        synchronized (list) {
            if (!this.active.contains(module)) {
                this.active.add(module);
                MeteorClient.EVENT_BUS.post((Object)ActiveModulesChangedEvent.get());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void removeActive(Module module) {
        List<Module> list = this.active;
        synchronized (list) {
            if (this.active.remove(module)) {
                MeteorClient.EVENT_BUS.post((Object)ActiveModulesChangedEvent.get());
            }
        }
    }

    public void setModuleToBind(Module moduleToBind) {
        this.moduleToBind = moduleToBind;
    }

    public void awaitKeyRelease() {
        this.awaitingKeyRelease = true;
    }

    public boolean isBinding() {
        return this.moduleToBind != null;
    }

    @EventHandler(priority=200)
    private void onKeyBinding(KeyInputEvent event) {
        if (event.action == KeyAction.Release && this.onBinding(true, event.key(), event.modifiers())) {
            event.cancel();
        }
    }

    @EventHandler(priority=200)
    private void onButtonBinding(MouseClickEvent event) {
        if (event.action == KeyAction.Release && this.onBinding(false, event.button(), 0)) {
            event.cancel();
        }
    }

    private boolean onBinding(boolean isKey, int value, int modifiers) {
        if (!this.isBinding()) {
            return false;
        }
        if (this.awaitingKeyRelease) {
            if (!isKey || value != 257 && value != 335) {
                return false;
            }
            this.awaitingKeyRelease = false;
            return false;
        }
        if (this.moduleToBind.keybind.canBindTo(isKey, value, modifiers)) {
            this.moduleToBind.keybind.set(isKey, value, modifiers);
            this.moduleToBind.info("Bound to (highlight)%s(default).", this.moduleToBind.keybind);
        } else if (value == 256) {
            this.moduleToBind.keybind.set(Keybind.none());
            this.moduleToBind.info("Removed bind.", new Object[0]);
        } else {
            return false;
        }
        MeteorClient.EVENT_BUS.post((Object)ModuleBindChangedEvent.get(this.moduleToBind));
        this.moduleToBind = null;
        return true;
    }

    @EventHandler(priority=100)
    private void onKey(KeyInputEvent event) {
        if (event.action == KeyAction.Repeat) {
            return;
        }
        this.onAction(true, event.key(), event.modifiers(), event.action == KeyAction.Press);
    }

    @EventHandler(priority=100)
    private void onMouseClick(MouseClickEvent event) {
        if (event.action == KeyAction.Repeat) {
            return;
        }
        this.onAction(false, event.button(), 0, event.action == KeyAction.Press);
    }

    private void onAction(boolean isKey, int value, int modifiers, boolean isPress) {
        if (MeteorClient.mc.screen != null || Input.isKeyPressed(292)) {
            return;
        }
        for (Module module : this.moduleInstances.values()) {
            if (!module.keybind.matches(isKey, value, modifiers) || !isPress && (!module.toggleOnBindRelease || !module.isActive())) continue;
            module.toggle();
            module.sendToggledMsg();
        }
    }

    @EventHandler(priority=201)
    private void onOpenScreen(OpenScreenEvent event) {
        if (!Utils.canUpdate()) {
            return;
        }
        for (Module module : this.moduleInstances.values()) {
            if (!module.toggleOnBindRelease || !module.isActive()) continue;
            module.toggle();
            module.sendToggledMsg();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        List<Module> list = this.active;
        synchronized (list) {
            for (Module module : this.getAll()) {
                if (!module.isActive() || module.runInMainMenu) continue;
                MeteorClient.EVENT_BUS.subscribe((Object)module);
                module.onActivate();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        List<Module> list = this.active;
        synchronized (list) {
            for (Module module : this.getAll()) {
                if (!module.isActive() || module.runInMainMenu) continue;
                MeteorClient.EVENT_BUS.unsubscribe((Object)module);
                module.onDeactivate();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void disableAll() {
        List<Module> list = this.active;
        synchronized (list) {
            for (Module module : this.getAll()) {
                module.disable();
            }
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ListTag modulesTag = new ListTag();
        for (Module module : this.getAll()) {
            CompoundTag moduleTag = module.toTag();
            if (moduleTag == null) continue;
            modulesTag.add((Object)moduleTag);
        }
        tag.put("modules", (Tag)modulesTag);
        return tag;
    }

    @Override
    public Modules fromTag(CompoundTag tag) {
        this.disableAll();
        ListTag modulesTag = tag.getListOrEmpty("modules");
        for (Tag moduleTagI : modulesTag) {
            CompoundTag moduleTag = (CompoundTag)moduleTagI;
            Module module = this.get(moduleTag.getStringOr("name", ""));
            if (module == null) continue;
            module.fromTag(moduleTag);
        }
        return this;
    }

    public void add(Module module) {
        if (!CATEGORIES.contains(module.category)) {
            throw new RuntimeException("Modules.addModule - Module's category was not registered.");
        }
        AtomicReference removedModule = new AtomicReference();
        if (this.moduleInstances.values().removeIf(module1 -> {
            if (module1.name.equals(module.name)) {
                removedModule.set(module1);
                module1.settings.unregisterColorSettings();
                return true;
            }
            return false;
        })) {
            this.getGroup(((Module)removedModule.get()).category).remove(removedModule.get());
        }
        this.moduleInstances.put(module.getClass(), module);
        this.getGroup(module.category).add(module);
        module.settings.registerColorSettings(module);
    }

    private void initCombat() {
        this.add(new AnchorAura());
        this.add(new AntiAnvil());
        this.add(new AntiBed());
        this.add(new ArrowDodge());
        this.add(new AttributeSwap());
        this.add(new AutoAnvil());
        this.add(new AutoArmor());
        this.add(new AutoCity());
        this.add(new AutoEXP());
        this.add(new AutoLog());
        this.add(new AutoTotem());
        this.add(new AutoTrap());
        this.add(new AutoWeapon());
        this.add(new AutoWeb());
        this.add(new BedAura());
        this.add(new BowAimbot());
        this.add(new BowSpam());
        this.add(new Burrow());
        this.add(new Criticals());
        this.add(new CrystalAura());
        this.add(new Hitboxes());
        this.add(new HoleFiller());
        this.add(new KillAura());
        this.add(new Offhand());
        this.add(new Quiver());
        this.add(new SelfAnvil());
        this.add(new SelfTrap());
        this.add(new SelfWeb());
        this.add(new Surround());
    }

    private void initPlayer() {
        this.add(new AirPlace());
        this.add(new AntiAFK());
        this.add(new AntiHunger());
        this.add(new AutoEat());
        this.add(new AutoClicker());
        this.add(new AutoFish());
        this.add(new AutoGap());
        this.add(new AutoMend());
        this.add(new AutoReplenish());
        this.add(new AutoRespawn());
        this.add(new AutoTool());
        this.add(new BreakDelay());
        this.add(new ChestSwap());
        this.add(new EXPThrower());
        this.add(new FakePlayer());
        this.add(new FastUse());
        this.add(new GhostHand());
        this.add(new InstantRebreak());
        this.add(new LiquidInteract());
        this.add(new MiddleClickExtra());
        this.add(new Multitask());
        this.add(new NameProtect());
        this.add(new NoInteract());
        this.add(new NoMiningTrace());
        this.add(new NoRotate());
        this.add(new NoStatusEffects());
        this.add(new Portals());
        this.add(new PotionSaver());
        this.add(new Reach());
        this.add(new Rotation());
        this.add(new SpeedMine());
    }

    private void initMovement() {
        this.add(new AirJump());
        this.add(new Anchor());
        this.add(new AntiVoid());
        this.add(new AutoJump());
        this.add(new AutoWalk());
        this.add(new AutoWasp());
        this.add(new Blink());
        this.add(new ClickTP());
        this.add(new ElytraBoost());
        this.add(new ElytraFly());
        this.add(new EntityControl());
        this.add(new FastClimb());
        this.add(new Flight());
        this.add(new GUIMove());
        this.add(new HighJump());
        this.add(new Jesus());
        this.add(new LongJump());
        this.add(new NoFall());
        this.add(new NoSlow());
        this.add(new Parkour());
        this.add(new ReverseStep());
        this.add(new SafeWalk());
        this.add(new Scaffold());
        this.add(new Slippy());
        this.add(new Sneak());
        this.add(new Speed());
        this.add(new Spider());
        this.add(new Sprint());
        this.add(new Step());
        this.add(new TridentBoost());
        this.add(new Velocity());
    }

    private void initRender() {
        this.add(new Arrows());
        this.add(new BetterTab());
        this.add(new BetterTooltips());
        this.add(new BlockESP());
        this.add(new BlockSelection());
        this.add(new Blur());
        this.add(new BossStack());
        this.add(new Breadcrumbs());
        this.add(new BreakIndicators());
        this.add(new CameraTweaks());
        this.add(new Chams());
        this.add(new CityESP());
        this.add(new EntityOwner());
        this.add(new ESP());
        this.add(new FireworkESP());
        this.add(new Freecam());
        this.add(new FreeLook());
        this.add(new Fullbright());
        this.add(new HandView());
        this.add(new HitBubbles());
        this.add(new HitMarker());
        this.add(new HoleESP());
        this.add(new ItemPhysics());
        this.add(new ItemHighlight());
        this.add(new JumpCircle());
        this.add(new KillEffect());
        this.add(new LightOverlay());
        this.add(new LogoutSpots());
        this.add(new Marker());
        this.add(new Nametags());
        this.add(new NoRender());
        this.add(new PopChams());
        this.add(new SeeInvisibles());
        this.add(new StorageESP());
        this.add(new TargetESP());
        this.add(new TimeChanger());
        this.add(new Tracers());
        this.add(new Trail());
        this.add(new Trajectories());
        this.add(new TunnelESP());
        this.add(new VoidESP());
        this.add(new WallHack());
        this.add(new WaypointsModule());
        this.add(new WeatherChanger());
        this.add(new Xray());
        this.add(new Zoom());
    }

    private void initWorld() {
        this.add(new Ambience());
        this.add(new AutoBreed());
        this.add(new AutoBrewer());
        this.add(new AutoMount());
        this.add(new AutoNametag());
        this.add(new AutoShearer());
        this.add(new AutoSign());
        this.add(new AutoSmelter());
        this.add(new BuildHeight());
        this.add(new Collisions());
        this.add(new EChestFarmer());
        this.add(new EndermanLook());
        this.add(new Flamethrower());
        this.add(new HighwayBuilder());
        this.add(new LiquidFiller());
        this.add(new NoGhostBlocks());
        this.add(new Nuker());
        this.add(new PacketMine());
        this.add(new StashFinder());
        this.add(new SpawnProofer());
        this.add(new Timer());
        this.add(new VeinMiner());
        if (BaritoneUtils.IS_AVAILABLE) {
            this.add(new Excavator());
            this.add(new InfinityMiner());
        }
    }

    private void initMisc() {
        this.add(new AntiPacketKick());
        this.add(new AutoReconnect());
        this.add(new BetterBeacons());
        this.add(new BetterChat());
        this.add(new BookBot());
        this.add(new DiscordPresence());
        this.add(new InventoryTweaks());
        this.add(new MessageAura());
        this.add(new Notebot());
        this.add(new Notifier());
        this.add(new PacketCanceller());
        this.add(new PacketLogger());
        this.add(new ServerSpoof());
        this.add(new SoundBlocker());
        this.add(new Spam());
        this.add(new Swarm());
    }
}

