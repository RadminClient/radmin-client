/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventBus
 *  meteordevelopment.orbit.EventHandler
 *  meteordevelopment.orbit.IEventBus
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.fabricmc.loader.api.metadata.ModMetadata
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.ChatScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.resources.Identifier
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.spongepowered.asm.mixin.MixinEnvironment
 */
package radmin.client;

import java.io.File;
import java.lang.invoke.MethodHandles;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;
import radmin.client.addons.AddonManager;
import radmin.client.addons.MeteorAddon;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.WidgetScreen;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.Tabs;
import radmin.client.systems.Systems;
import radmin.client.systems.config.Config;
import radmin.client.systems.hud.screens.AddHudElementScreen;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.systems.hud.screens.HudElementScreen;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.DiscordPresence;
import radmin.client.utils.PostInit;
import radmin.client.utils.PreInit;
import radmin.client.utils.ReflectInit;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.Version;
import radmin.client.utils.misc.input.KeyAction;
import radmin.client.utils.misc.input.KeyBinds;
import radmin.client.utils.network.OnlinePlayers;

public class MeteorClient
implements ClientModInitializer {
    public static final String MOD_ID = "radmin-client";
    public static final ModMetadata MOD_META;
    public static final String NAME;
    public static final Version VERSION;
    public static final String BUILD_NUMBER;
    public static MeteorClient INSTANCE;
    public static MeteorAddon ADDON;
    public static Minecraft mc;
    public static final IEventBus EVENT_BUS;
    public static final File FOLDER;
    public static final Logger LOG;
    private boolean wasWidgetScreen;
    private boolean wasHudHiddenRoot;

    public void onInitializeClient() {
        if (INSTANCE == null) {
            INSTANCE = this;
            return;
        }
        mc = Minecraft.getInstance();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOG.info("Force loading mixins");
            MixinEnvironment.getCurrentEnvironment().audit();
        }
        LOG.info("Initializing {}", (Object)NAME);
        if (!FOLDER.exists()) {
            FOLDER.getParentFile().mkdirs();
            FOLDER.mkdir();
            Systems.addPreLoadTask(() -> Modules.get().get(DiscordPresence.class).enable());
        }
        AddonManager.init();
        AddonManager.ADDONS.forEach(addon -> {
            try {
                EVENT_BUS.registerLambdaFactory(addon.getPackage(), (lookupInMethod, klass) -> (MethodHandles.Lookup)lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
            }
            catch (AbstractMethodError e) {
                throw new RuntimeException("Addon \"%s\" is too old and cannot be ran.".formatted(addon.name), e);
            }
        });
        ReflectInit.registerPackages();
        ReflectInit.init(PreInit.class);
        Categories.init();
        Systems.init();
        EVENT_BUS.subscribe((Object)this);
        AddonManager.ADDONS.forEach(MeteorAddon::onInitialize);
        Modules.get().sortModules();
        Systems.load();
        ReflectInit.init(PostInit.class);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            OnlinePlayers.leave();
            Systems.save();
            GuiThemes.save();
        }));
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (MeteorClient.mc.screen == null && mc.getOverlay() == null && KeyBinds.OPEN_COMMANDS.consumeClick()) {
            mc.setScreen((Screen)new ChatScreen(Config.get().prefix.get(), true));
        }
    }

    @EventHandler
    private void onKey(KeyInputEvent event) {
        if (event.action == KeyAction.Press && KeyBinds.OPEN_GUI.matches(event.input)) {
            this.toggleGui();
        }
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent event) {
        if (event.action == KeyAction.Press && KeyBinds.OPEN_GUI.matchesMouse(event.click)) {
            this.toggleGui();
        }
    }

    private void toggleGui() {
        if (Utils.canCloseGui()) {
            MeteorClient.mc.screen.onClose();
        } else if (Utils.canOpenGui()) {
            ((Tab)Tabs.get().getFirst()).openScreen(GuiThemes.get());
        }
    }

    @EventHandler(priority=-200)
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof WidgetScreen) {
            if (!this.wasWidgetScreen) {
                this.wasHudHiddenRoot = MeteorClient.mc.options.hideGui;
            }
            if (GuiThemes.get().hideHUD() || this.wasHudHiddenRoot) {
                MeteorClient.mc.options.hideGui = !(event.screen instanceof HudEditorScreen) && !(event.screen instanceof AddHudElementScreen) && !(event.screen instanceof HudElementScreen);
            }
        } else {
            if (this.wasWidgetScreen) {
                MeteorClient.mc.options.hideGui = this.wasHudHiddenRoot;
            }
            this.wasHudHiddenRoot = MeteorClient.mc.options.hideGui;
        }
        this.wasWidgetScreen = event.screen instanceof WidgetScreen;
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath((String)MOD_ID, (String)path);
    }

    static {
        EVENT_BUS = new EventBus();
        FOLDER = FabricLoader.getInstance().getGameDir().resolve(MOD_ID).toFile();
        MOD_META = ((ModContainer)FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()).getMetadata();
        NAME = MOD_META.getName();
        LOG = LoggerFactory.getLogger((String)NAME);
        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) {
            versionString = versionString.split("-")[0];
        }
        if (versionString.equals("${version}")) {
            versionString = "0.0.0";
        }
        VERSION = new Version(versionString);
        BUILD_NUMBER = MOD_META.getCustomValue("radmin-client:build_number").getAsString();
    }
}

