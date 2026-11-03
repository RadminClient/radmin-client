/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPipeline
 *  io.netty.handler.proxy.Socks4ProxyHandler
 *  io.netty.handler.proxy.Socks5ProxyHandler
 *  io.netty.handler.timeout.TimeoutException
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.BandwidthDebugMonitor
 *  net.minecraft.network.Connection
 *  net.minecraft.network.SkipPacketEncoderException
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.PacketFlow
 *  net.minecraft.network.protocol.game.ClientboundBundlePacket
 *  net.minecraft.server.network.EventLoopGroupHolder
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.TimeoutException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.BandwidthDebugMonitor;
import net.minecraft.network.Connection;
import net.minecraft.network.SkipPacketEncoderException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.server.network.EventLoopGroupHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.ServerConnectEndEvent;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.AntiPacketKick;
import radmin.client.systems.modules.world.HighwayBuilder;
import radmin.client.systems.proxies.Proxies;
import radmin.client.systems.proxies.Proxy;

@Mixin(value={Connection.class})
public abstract class ConnectionMixin {
    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/Connection;genericsFtw(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V", shift=At.Shift.BEFORE)}, cancellable=true)
    private void onHandlePacket(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ClientboundBundlePacket) {
            ClientboundBundlePacket bundle = (ClientboundBundlePacket)packet;
            Iterator it = bundle.subPackets().iterator();
            while (it.hasNext()) {
                if (!((PacketEvent.Receive)MeteorClient.EVENT_BUS.post((ICancellable)new PacketEvent.Receive((Packet)it.next(), (Connection)this))).isCancelled()) continue;
                it.remove();
            }
        } else if (((PacketEvent.Receive)MeteorClient.EVENT_BUS.post((ICancellable)new PacketEvent.Receive(packet, (Connection)this))).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"disconnect(Lnet/minecraft/network/chat/Component;)V"}, at={@At(value="HEAD")})
    private void disconnect(Component reason, CallbackInfo ci) {
        if (Modules.get().get(HighwayBuilder.class).isActive()) {
            MutableComponent text = Component.literal((String)"%n%n%s[%sHighway Builder%s] Statistics:%n".formatted(ChatFormatting.GRAY, ChatFormatting.BLUE, ChatFormatting.GRAY));
            text.append((Component)Modules.get().get(HighwayBuilder.class).getStatsText());
            ((MutableComponent)reason).append((Component)text);
        }
    }

    @Inject(method={"connect(Ljava/net/InetSocketAddress;Lnet/minecraft/server/network/EventLoopGroupHolder;Lnet/minecraft/network/Connection;)Lio/netty/channel/ChannelFuture;"}, at={@At(value="HEAD")})
    private static void onConnect(InetSocketAddress address, EventLoopGroupHolder eventLoopGroupHolder, Connection connection, CallbackInfoReturnable<ChannelFuture> cir) {
        MeteorClient.EVENT_BUS.post((Object)ServerConnectEndEvent.get(address));
    }

    @Inject(at={@At(value="HEAD")}, method={"send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V"}, cancellable=true)
    private void onSendPacketHead(Packet<?> packet, @Nullable ChannelFutureListener listener, CallbackInfo ci) {
        if (((PacketEvent.Send)MeteorClient.EVENT_BUS.post((ICancellable)new PacketEvent.Send(packet, (Connection)this))).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V"}, at={@At(value="TAIL")})
    private void onSendPacketTail(Packet<?> packet, @Nullable ChannelFutureListener listener, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)new PacketEvent.Sent(packet, (Connection)this));
    }

    @Inject(method={"exceptionCaught"}, at={@At(value="HEAD")}, cancellable=true)
    private void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, CallbackInfo ci) {
        AntiPacketKick apk = Modules.get().get(AntiPacketKick.class);
        if (!(cause instanceof TimeoutException) && !(cause instanceof SkipPacketEncoderException) && apk.catchExceptions()) {
            if (apk.logExceptions.get().booleanValue()) {
                apk.warning("Caught exception: %s", cause);
            }
            ci.cancel();
        }
    }

    @Inject(method={"configureSerialization"}, at={@At(value="RETURN")})
    private static void onAddHandlers(ChannelPipeline pipeline, PacketFlow inboundDirection, boolean local, BandwidthDebugMonitor monitor, CallbackInfo ci) {
        if (inboundDirection != PacketFlow.CLIENTBOUND || local) {
            return;
        }
        Proxy proxy = Proxies.get().getEnabled();
        if (proxy == null) {
            return;
        }
        switch (proxy.type.get()) {
            case Socks4: {
                pipeline.addFirst(new ChannelHandler[]{new Socks4ProxyHandler((SocketAddress)new InetSocketAddress(proxy.address.get(), (int)proxy.port.get()), proxy.username.get())});
                break;
            }
            case Socks5: {
                pipeline.addFirst(new ChannelHandler[]{new Socks5ProxyHandler((SocketAddress)new InetSocketAddress(proxy.address.get(), (int)proxy.port.get()), proxy.username.get(), proxy.password.get())});
            }
        }
    }
}

