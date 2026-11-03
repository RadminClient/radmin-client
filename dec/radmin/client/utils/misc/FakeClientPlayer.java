/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.ClientLevel$ClientLevelData
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.CommonListenerCookie
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.RemotePlayer
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.PacketFlow
 *  net.minecraft.world.Difficulty
 */
package radmin.client.utils.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.Difficulty;
import radmin.client.MeteorClient;
import radmin.client.utils.PreInit;

public class FakeClientPlayer {
    private static ClientLevel world;
    private static RemotePlayer player;
    private static PlayerInfo playerListEntry;
    private static UUID lastId;
    private static boolean needsNewEntry;

    private FakeClientPlayer() {
    }

    @PreInit
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe(FakeClientPlayer.class);
    }

    public static RemotePlayer getPlayer() {
        UUID id = MeteorClient.mc.getUser().getProfileId();
        if (player == null || !id.equals(lastId)) {
            if (world == null) {
                world = new ClientLevel(new ClientPacketListener(MeteorClient.mc, new Connection(PacketFlow.CLIENTBOUND), new CommonListenerCookie(null, new GameProfile(MeteorClient.mc.getUser().getProfileId(), MeteorClient.mc.getUser().getName()), null, null, null, null, MeteorClient.mc.getCurrentServer(), null, null, null, null, null, null, false)), new ClientLevel.ClientLevelData(Difficulty.NORMAL, false, false), world.dimension(), world.dimensionTypeRegistration(), 1, 1, null, false, 0L, world.getSeaLevel());
            }
            player = new RemotePlayer(world, new GameProfile(id, MeteorClient.mc.getUser().getName()));
            lastId = id;
            needsNewEntry = true;
        }
        return player;
    }

    public static PlayerInfo getPlayerListEntry() {
        if (playerListEntry == null || needsNewEntry) {
            playerListEntry = new PlayerInfo(new GameProfile(lastId, MeteorClient.mc.getUser().getName()), false);
            needsNewEntry = false;
        }
        return playerListEntry;
    }
}

