/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.mojang.blaze3d.pipeline.RenderTarget
 *  com.mojang.blaze3d.platform.Window
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.MouseHandler
 *  net.minecraft.client.Options
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.util.profiling.Profiler
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.HitResult
 *  org.jetbrains.annotations.Nullable
 *  org.meteordev.starscript.Script
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import java.util.concurrent.CompletableFuture;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.meteordev.starscript.Script;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.entity.player.DoAttackEvent;
import radmin.client.events.entity.player.DoItemUseEvent;
import radmin.client.events.entity.player.ItemUseCrosshairTargetEvent;
import radmin.client.events.game.GameLeftEvent;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.events.game.ResolutionChangedEvent;
import radmin.client.events.game.ResourcePacksReloadedEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.gui.WidgetScreen;
import radmin.client.mixin.KeyMappingAccessor;
import radmin.client.mixininterface.IMinecraft;
import radmin.client.mixininterface.IVec3;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.InventoryTweaks;
import radmin.client.systems.modules.movement.GUIMove;
import radmin.client.systems.modules.player.FastUse;
import radmin.client.systems.modules.player.Multitask;
import radmin.client.systems.modules.render.ESP;
import radmin.client.systems.modules.render.Freecam;
import radmin.client.systems.modules.world.HighwayBuilder;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.CPSUtils;
import radmin.client.utils.misc.MeteorStarscript;
import radmin.client.utils.network.OnlinePlayers;

@Mixin(value={Minecraft.class}, priority=1001)
public abstract class MinecraftMixin
implements IMinecraft {
    @Unique
    private boolean startUseItemCalled;
    @Unique
    private boolean rightClick;
    @Unique
    private long lastTime;
    @Unique
    private boolean firstFrame;
    @Shadow
    public ClientLevel level;
    @Shadow
    @Final
    public MouseHandler mouseHandler;
    @Shadow
    @Final
    private Window window;
    @Shadow
    public Screen screen;
    @Shadow
    @Final
    public Options options;
    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Shadow
    private int rightClickDelay;
    @Shadow
    @Nullable
    public LocalPlayer player;
    @Shadow
    @Final
    @Mutable
    private RenderTarget mainRenderTarget;
    @Unique
    private boolean freecamSet = false;
    @Final
    @Shadow
    public GameRenderer gameRenderer;
    @Unique
    private boolean isBreaking = false;

    @Shadow
    protected abstract void startUseItem();

    @Shadow
    protected abstract void continueAttack(boolean var1);

    @Shadow
    protected abstract void pick(float var1);

    @Shadow
    public abstract Entity getCameraEntity();

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        MeteorClient.INSTANCE.onInitializeClient();
        this.firstFrame = true;
    }

    @Inject(at={@At(value="HEAD")}, method={"tick"})
    private void onPreTick(CallbackInfo ci) {
        OnlinePlayers.update();
        this.startUseItemCalled = false;
        Profiler.get().push("radmin-client_pre_update");
        MeteorClient.EVENT_BUS.post((Object)TickEvent.Pre.get());
        Profiler.get().pop();
        if (this.rightClick && !this.startUseItemCalled && this.gameMode != null) {
            this.startUseItem();
        }
        this.rightClick = false;
    }

    @Inject(at={@At(value="TAIL")}, method={"tick"})
    private void onTick(CallbackInfo ci) {
        Profiler.get().push("radmin-client_post_update");
        MeteorClient.EVENT_BUS.post((Object)TickEvent.Post.get());
        Profiler.get().pop();
    }

    @Inject(method={"startAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        CPSUtils.onAttack();
        if (((DoAttackEvent)MeteorClient.EVENT_BUS.post((ICancellable)DoAttackEvent.get())).isCancelled()) {
            cir.cancel();
        }
    }

    @Inject(method={"startUseItem"}, at={@At(value="HEAD")})
    private void onStartUseItem(CallbackInfo ci) {
        this.startUseItemCalled = true;
    }

    @Inject(method={"disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V"}, at={@At(value="HEAD")})
    private void onDisconnect(Screen screen, boolean keepResourcePacks, boolean stopSound, CallbackInfo ci) {
        if (this.level != null) {
            MeteorClient.EVENT_BUS.post((Object)GameLeftEvent.get());
        }
    }

    @Inject(method={"setScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof WidgetScreen) {
            screen.mouseMoved(this.mouseHandler.xpos() * (double)this.window.getGuiScale(), this.mouseHandler.ypos() * (double)this.window.getGuiScale());
        }
        OpenScreenEvent event = OpenScreenEvent.get(screen);
        MeteorClient.EVENT_BUS.post((ICancellable)event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @WrapOperation(method={"setScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;releaseAll()V")})
    private void onSetScreenKeyBindingUnpressAll(Operation<Void> op) {
        Modules modules = Modules.get();
        if (modules == null) {
            op.call(new Object[0]);
            return;
        }
        GUIMove guimove = modules.get(GUIMove.class);
        if (guimove == null || !guimove.isActive() || guimove.skip()) {
            op.call(new Object[0]);
            return;
        }
        Options options = MeteorClient.mc.options;
        for (KeyMapping kb : KeyMappingAccessor.getKeysById().values()) {
            if (kb == options.keyUp || kb == options.keyLeft || kb == options.keyRight || kb == options.keyDown || guimove.sneak.get().booleanValue() && kb == options.keyShift || guimove.sprint.get().booleanValue() && kb == options.keySprint || guimove.jump.get().booleanValue() && kb == options.keyJump) continue;
            ((KeyMappingAccessor)kb).meteor$invokeRelease();
        }
    }

    @Inject(method={"startUseItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z")})
    private void onStartUseItemHand(CallbackInfo ci, @Local(name={"heldItem"}) ItemStack heldItem) {
        FastUse fastUse = Modules.get().get(FastUse.class);
        if (fastUse.isActive()) {
            this.rightClickDelay = fastUse.getItemUseCooldown(heldItem);
        }
    }

    @Inject(method={"startUseItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/InteractionHand;values()[Lnet/minecraft/world/InteractionHand;")}, cancellable=true)
    private void onStartUseItemBeforeHands(CallbackInfo ci) {
        if (((DoItemUseEvent)MeteorClient.EVENT_BUS.post((ICancellable)DoItemUseEvent.get())).isCancelled()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"startUseItem"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", ordinal=1, opcode=180)})
    private HitResult startUseItemMinecraftClientCrosshairTargetProxy(HitResult original) {
        return ((ItemUseCrosshairTargetEvent)MeteorClient.EVENT_BUS.post((Object)ItemUseCrosshairTargetEvent.get((HitResult)original))).target;
    }

    @ModifyReturnValue(method={"reloadResourcePacks(ZLnet/minecraft/client/Minecraft$GameLoadCookie;)Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="RETURN")})
    private CompletableFuture<Void> onReloadResourcePacksNewCompletableFuture(CompletableFuture<Void> original) {
        return original.thenRun(() -> MeteorClient.EVENT_BUS.post((Object)ResourcePacksReloadedEvent.get()));
    }

    @ModifyArg(method={"updateTitle"}, at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/platform/Window;setTitle(Ljava/lang/String;)V"))
    private String setTitle(String original) {
        String title;
        if (Config.get() == null || !Config.get().customWindowTitle.get().booleanValue()) {
            return original;
        }
        String customTitle = Config.get().customWindowTitleText.get();
        Script script = MeteorStarscript.compile(customTitle);
        if (script != null && (title = MeteorStarscript.run(script)) != null) {
            customTitle = title;
        }
        return customTitle;
    }

    @WrapWithCondition(method={"handleKeybinds"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;releaseUsingItem(Lnet/minecraft/world/entity/player/Player;)V")})
    private boolean wrapStopUsing(MultiPlayerGameMode instance, Player player) {
        return this.HB$stopUsingItem();
    }

    @Unique
    private boolean HB$stopUsingItem() {
        HighwayBuilder b = Modules.get().get(HighwayBuilder.class);
        return !b.isActive() || !b.drawingBow;
    }

    @Inject(method={"resizeGui"}, at={@At(value="TAIL")})
    private void onResizeGui(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)ResolutionChangedEvent.get());
    }

    @Inject(method={"runTick"}, at={@At(value="HEAD")})
    private void onRunTick(CallbackInfo ci) {
        long time = System.currentTimeMillis();
        if (this.firstFrame) {
            this.lastTime = time;
            this.firstFrame = false;
        }
        Utils.frameTime = (double)(time - this.lastTime) / 1000.0;
        this.lastTime = time;
    }

    @ModifyExpressionValue(method={"startUseItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;isDestroying()Z")})
    private boolean startUseItemModifyIsBreakingBlock(boolean original) {
        return !Modules.get().isActive(Multitask.class) && original;
    }

    @ModifyExpressionValue(method={"continueAttack"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")})
    private boolean continueAttackModifyIsUsingItem(boolean original) {
        return !Modules.get().isActive(Multitask.class) && original;
    }

    @ModifyExpressionValue(method={"handleKeybinds"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal=0)})
    private boolean handleKeybindsModifyIsUsingItem(boolean original) {
        return !Modules.get().get(Multitask.class).attackingEntities() && original;
    }

    @Inject(method={"handleKeybinds"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal=0, shift=At.Shift.BEFORE)})
    private void handleKeybindsInjectStopUsingItem(CallbackInfo ci) {
        if (Modules.get().get(Multitask.class).attackingEntities() && this.player.isUsingItem()) {
            if (!this.options.keyUse.isDown() && this.HB$stopUsingItem()) {
                this.gameMode.releaseUsingItem((Player)this.player);
            }
            while (this.options.keyUse.consumeClick()) {
            }
        }
    }

    @ModifyReturnValue(method={"shouldEntityAppearGlowing"}, at={@At(value="RETURN")})
    private boolean shouldEntityAppearGlowingModifyIsOutline(boolean original, Entity entity) {
        ESP esp = Modules.get().get(ESP.class);
        if (esp == null) {
            return original;
        }
        if (!esp.isGlow() || esp.shouldSkip(entity)) {
            return original;
        }
        return esp.getColor(entity) != null || original;
    }

    @WrapWithCondition(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;handleKeybinds()V")})
    private boolean wrapHandleInputEvents(Minecraft instance) {
        return !Modules.get().get(InventoryTweaks.class).frameInput();
    }

    @WrapWithCondition(method={"handleKeybinds"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;continueAttack(Z)V")})
    private boolean wrapHandleBlockBreaking(Minecraft instance, boolean down) {
        this.isBreaking = down;
        return !Modules.get().get(InventoryTweaks.class).frameInput();
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;handleKeybinds()V", shift=At.Shift.AFTER)})
    private void afterHandleInputEvents(CallbackInfo ci) {
        if (!Modules.get().get(InventoryTweaks.class).frameInput()) {
            return;
        }
        this.continueAttack(this.isBreaking);
        this.isBreaking = false;
    }

    @Override
    public void meteor$rightClick() {
        this.rightClick = true;
    }

    @Override
    public void meteor$setFramebuffer(RenderTarget framebuffer) {
        this.mainRenderTarget = framebuffer;
    }

    @Inject(method={"pick"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateTargetedEntityInvoke(float partialTicks, CallbackInfo ci) {
        Freecam freecam = Modules.get().get(Freecam.class);
        boolean highwayBuilder = Modules.get().isActive(HighwayBuilder.class);
        if ((freecam.isActive() || highwayBuilder) && this.getCameraEntity() != null && !this.freecamSet) {
            ci.cancel();
            Entity cameraE = this.getCameraEntity();
            double x = cameraE.getX();
            double y = cameraE.getY();
            double z = cameraE.getZ();
            double lastX = cameraE.xo;
            double lastY = cameraE.yo;
            double lastZ = cameraE.zo;
            float yaw = cameraE.getYRot();
            float pitch = cameraE.getXRot();
            float lastYaw = cameraE.yRotO;
            float lastPitch = cameraE.xRotO;
            if (highwayBuilder) {
                cameraE.setYRot(this.gameRenderer.getMainCamera().yRot());
                cameraE.setXRot(this.gameRenderer.getMainCamera().xRot());
            } else {
                ((IVec3)cameraE.position()).meteor$set(freecam.pos.x, freecam.pos.y - (double)cameraE.getEyeHeight(cameraE.getPose()), freecam.pos.z);
                cameraE.xo = freecam.prevPos.x;
                cameraE.yo = freecam.prevPos.y - (double)cameraE.getEyeHeight(cameraE.getPose());
                cameraE.zo = freecam.prevPos.z;
                cameraE.setYRot(freecam.yaw);
                cameraE.setXRot(freecam.pitch);
                cameraE.yRotO = freecam.lastYaw;
                cameraE.xRotO = freecam.lastPitch;
            }
            this.freecamSet = true;
            this.pick(partialTicks);
            this.freecamSet = false;
            ((IVec3)cameraE.position()).meteor$set(x, y, z);
            cameraE.xo = lastX;
            cameraE.yo = lastY;
            cameraE.zo = lastZ;
            cameraE.setYRot(yaw);
            cameraE.setXRot(pitch);
            cameraE.yRotO = lastYaw;
            cameraE.xRotO = lastPitch;
        }
    }
}

