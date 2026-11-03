/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Sets
 *  net.minecraft.network.ProtocolInfo$DetailsProvider
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.PacketType
 *  net.minecraft.network.protocol.common.CommonPacketTypes
 *  net.minecraft.network.protocol.configuration.ConfigurationPacketTypes
 *  net.minecraft.network.protocol.configuration.ConfigurationProtocols
 *  net.minecraft.network.protocol.cookie.CookiePacketTypes
 *  net.minecraft.network.protocol.game.GamePacketTypes
 *  net.minecraft.network.protocol.game.GameProtocols
 *  net.minecraft.network.protocol.handshake.HandshakePacketTypes
 *  net.minecraft.network.protocol.handshake.HandshakeProtocols
 *  net.minecraft.network.protocol.login.LoginPacketTypes
 *  net.minecraft.network.protocol.login.LoginProtocols
 *  net.minecraft.network.protocol.ping.PingPacketTypes
 *  net.minecraft.network.protocol.status.StatusPacketTypes
 *  net.minecraft.network.protocol.status.StatusProtocols
 *  net.minecraft.resources.Identifier
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.utils.network;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.CommonPacketTypes;
import net.minecraft.network.protocol.configuration.ConfigurationPacketTypes;
import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
import net.minecraft.network.protocol.cookie.CookiePacketTypes;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.handshake.HandshakePacketTypes;
import net.minecraft.network.protocol.handshake.HandshakeProtocols;
import net.minecraft.network.protocol.login.LoginPacketTypes;
import net.minecraft.network.protocol.login.LoginProtocols;
import net.minecraft.network.protocol.ping.PingPacketTypes;
import net.minecraft.network.protocol.status.StatusPacketTypes;
import net.minecraft.network.protocol.status.StatusProtocols;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketUtils {
    private static final Map<Identifier, PacketType<? extends @NotNull Packet<?>>> CLIENTBOUND_PACKETS_MAP;
    private static final Map<Identifier, PacketType<? extends @NotNull Packet<?>>> SERVERBOUND_PACKETS_MAP;
    private static final Set<PacketType<? extends @NotNull Packet<?>>> CLIENTBOUND_PACKETS;
    private static final Set<PacketType<? extends @NotNull Packet<?>>> SERVERBOUND_PACKETS;
    private static final Map<String, PacketType<? extends @NotNull Packet<?>>> LEGACY_PACKET_MAPPINGS;

    public static Set<PacketType<? extends @NotNull Packet<?>>> getPackets() {
        return Sets.union(CLIENTBOUND_PACKETS, SERVERBOUND_PACKETS);
    }

    public static Set<PacketType<? extends @NotNull Packet<?>>> getClientboundPackets() {
        return CLIENTBOUND_PACKETS;
    }

    public static Set<PacketType<? extends @NotNull Packet<?>>> getServerboundPackets() {
        return SERVERBOUND_PACKETS;
    }

    public static @Nullable PacketType<? extends @NotNull Packet<?>> getClientboundPacket(Identifier id) {
        return CLIENTBOUND_PACKETS_MAP.get(id);
    }

    public static @Nullable PacketType<? extends @NotNull Packet<?>> getServerboundPacket(Identifier id) {
        return SERVERBOUND_PACKETS_MAP.get(id);
    }

    public static @Nullable PacketType<? extends @NotNull Packet<?>> getPacket(Identifier id) {
        @Nullable PacketType<? extends @NotNull Packet<?>> clientbound = PacketUtils.getClientboundPacket(id);
        return clientbound != null ? clientbound : PacketUtils.getServerboundPacket(id);
    }

    public static @Nullable PacketType<? extends @NotNull Packet<?>> getPacket(String name) {
        PacketType<? extends Packet<?>> type;
        if (name.startsWith("clientbound/")) {
            @Nullable Identifier identifier = Identifier.tryParse((String)name.substring(12));
            return CLIENTBOUND_PACKETS_MAP.get(identifier);
        }
        if (name.startsWith("serverbound/")) {
            @Nullable Identifier identifier = Identifier.tryParse((String)name.substring(12));
            return SERVERBOUND_PACKETS_MAP.get(identifier);
        }
        @Nullable Identifier identifier = Identifier.tryParse((String)name);
        if (identifier != null && (type = PacketUtils.getPacket(identifier)) != null) {
            return type;
        }
        return LEGACY_PACKET_MAPPINGS.get(name);
    }

    private PacketUtils() {
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    static {
        // Could not load outer class - annotation placement on inner may be incorrect
        @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull ImmutableMap.Builder clientbound = ImmutableMap.builder();
        // Could not load outer class - annotation placement on inner may be incorrect
        @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull ImmutableMap.Builder serverbound = ImmutableMap.builder();
        Stream.of(StatusProtocols.CLIENTBOUND_TEMPLATE, LoginProtocols.CLIENTBOUND_TEMPLATE, ConfigurationProtocols.CLIENTBOUND_TEMPLATE, GameProtocols.CLIENTBOUND_TEMPLATE).map(ProtocolInfo.DetailsProvider::details).forEach(details -> details.listPackets((type, n) -> clientbound.put((Object)type.id(), (Object)type)));
        Stream.of(HandshakeProtocols.SERVERBOUND_TEMPLATE, StatusProtocols.SERVERBOUND_TEMPLATE, LoginProtocols.SERVERBOUND_TEMPLATE, ConfigurationProtocols.SERVERBOUND_TEMPLATE, GameProtocols.SERVERBOUND_TEMPLATE).map(ProtocolInfo.DetailsProvider::details).forEach(details -> details.listPackets((type, n) -> serverbound.put((Object)type.id(), (Object)type)));
        CLIENTBOUND_PACKETS_MAP = clientbound.buildKeepingLast();
        SERVERBOUND_PACKETS_MAP = serverbound.buildKeepingLast();
        CLIENTBOUND_PACKETS = Set.copyOf(CLIENTBOUND_PACKETS_MAP.values());
        SERVERBOUND_PACKETS = Set.copyOf(SERVERBOUND_PACKETS_MAP.values());
        // Could not load outer class - annotation placement on inner may be incorrect
        @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull @NotNull ImmutableMap.Builder builder = ImmutableMap.builder();
        builder.put((Object)"ClientIntentionPacket", (Object)HandshakePacketTypes.CLIENT_INTENTION);
        builder.put((Object)"ServerboundMovePlayerPacket.Pos", (Object)GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS);
        builder.put((Object)"ServerboundMovePlayerPacket.PosRot", (Object)GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT);
        builder.put((Object)"ServerboundMovePlayerPacket.Rot", (Object)GamePacketTypes.SERVERBOUND_MOVE_PLAYER_ROT);
        builder.put((Object)"ServerboundAcceptCodeOfConductPacket", (Object)ConfigurationPacketTypes.SERVERBOUND_ACCEPT_CODE_OF_CONDUCT);
        builder.put((Object)"ServerboundAcceptTeleportationPacket", (Object)GamePacketTypes.SERVERBOUND_ACCEPT_TELEPORTATION);
        builder.put((Object)"ServerboundAttackPacket", (Object)GamePacketTypes.SERVERBOUND_ATTACK);
        builder.put((Object)"ServerboundBlockEntityTagQueryPacket", (Object)GamePacketTypes.SERVERBOUND_BLOCK_ENTITY_TAG_QUERY);
        builder.put((Object)"ServerboundChangeDifficultyPacket", (Object)GamePacketTypes.SERVERBOUND_CHANGE_DIFFICULTY);
        builder.put((Object)"ServerboundChangeGameModePacket", (Object)GamePacketTypes.SERVERBOUND_CHANGE_GAME_MODE);
        builder.put((Object)"ServerboundChatAckPacket", (Object)GamePacketTypes.SERVERBOUND_CHAT_ACK);
        builder.put((Object)"ServerboundChatCommandPacket", (Object)GamePacketTypes.SERVERBOUND_CHAT_COMMAND);
        builder.put((Object)"ServerboundChatCommandSignedPacket", (Object)GamePacketTypes.SERVERBOUND_CHAT_COMMAND_SIGNED);
        builder.put((Object)"ServerboundChatPacket", (Object)GamePacketTypes.SERVERBOUND_CHAT);
        builder.put((Object)"ServerboundChatSessionUpdatePacket", (Object)GamePacketTypes.SERVERBOUND_CHAT_SESSION_UPDATE);
        builder.put((Object)"ServerboundChunkBatchReceivedPacket", (Object)GamePacketTypes.SERVERBOUND_CHUNK_BATCH_RECEIVED);
        builder.put((Object)"ServerboundClientCommandPacket", (Object)GamePacketTypes.SERVERBOUND_CLIENT_COMMAND);
        builder.put((Object)"ServerboundClientInformationPacket", (Object)CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION);
        builder.put((Object)"ServerboundClientTickEndPacket", (Object)GamePacketTypes.SERVERBOUND_CLIENT_TICK_END);
        builder.put((Object)"ServerboundCommandSuggestionPacket", (Object)GamePacketTypes.SERVERBOUND_COMMAND_SUGGESTION);
        builder.put((Object)"ServerboundConfigurationAcknowledgedPacket", (Object)GamePacketTypes.SERVERBOUND_CONFIGURATION_ACKNOWLEDGED);
        builder.put((Object)"ServerboundContainerButtonClickPacket", (Object)GamePacketTypes.SERVERBOUND_CONTAINER_BUTTON_CLICK);
        builder.put((Object)"ServerboundContainerClickPacket", (Object)GamePacketTypes.SERVERBOUND_CONTAINER_CLICK);
        builder.put((Object)"ServerboundContainerClosePacket", (Object)GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE);
        builder.put((Object)"ServerboundContainerSlotStateChangedPacket", (Object)GamePacketTypes.SERVERBOUND_CONTAINER_SLOT_STATE_CHANGED);
        builder.put((Object)"ServerboundCookieResponsePacket", (Object)CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE);
        builder.put((Object)"ServerboundCustomClickActionPacket", (Object)CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION);
        builder.put((Object)"ServerboundCustomPayloadPacket", (Object)CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD);
        builder.put((Object)"ServerboundCustomQueryAnswerPacket", (Object)LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER);
        builder.put((Object)"ServerboundDebugSubscriptionRequestPacket", (Object)GamePacketTypes.SERVERBOUND_DEBUG_SUBSCRIPTION_REQUEST);
        builder.put((Object)"ServerboundEditBookPacket", (Object)GamePacketTypes.SERVERBOUND_EDIT_BOOK);
        builder.put((Object)"ServerboundEntityTagQueryPacket", (Object)GamePacketTypes.SERVERBOUND_ENTITY_TAG_QUERY);
        builder.put((Object)"ServerboundFinishConfigurationPacket", (Object)ConfigurationPacketTypes.SERVERBOUND_FINISH_CONFIGURATION);
        builder.put((Object)"ServerboundHelloPacket", (Object)LoginPacketTypes.SERVERBOUND_HELLO);
        builder.put((Object)"ServerboundInteractPacket", (Object)GamePacketTypes.SERVERBOUND_INTERACT);
        builder.put((Object)"ServerboundJigsawGeneratePacket", (Object)GamePacketTypes.SERVERBOUND_JIGSAW_GENERATE);
        builder.put((Object)"ServerboundKeepAlivePacket", (Object)CommonPacketTypes.SERVERBOUND_KEEP_ALIVE);
        builder.put((Object)"ServerboundKeyPacket", (Object)LoginPacketTypes.SERVERBOUND_KEY);
        builder.put((Object)"ServerboundLockDifficultyPacket", (Object)GamePacketTypes.SERVERBOUND_LOCK_DIFFICULTY);
        builder.put((Object)"ServerboundLoginAcknowledgedPacket", (Object)LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED);
        builder.put((Object)"ServerboundMoveVehiclePacket", (Object)GamePacketTypes.SERVERBOUND_MOVE_VEHICLE);
        builder.put((Object)"ServerboundPaddleBoatPacket", (Object)GamePacketTypes.SERVERBOUND_PADDLE_BOAT);
        builder.put((Object)"ServerboundPickItemFromBlockPacket", (Object)GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK);
        builder.put((Object)"ServerboundPickItemFromEntityPacket", (Object)GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_ENTITY);
        builder.put((Object)"ServerboundPingRequestPacket", (Object)PingPacketTypes.SERVERBOUND_PING_REQUEST);
        builder.put((Object)"ServerboundPlaceRecipePacket", (Object)GamePacketTypes.SERVERBOUND_PLACE_RECIPE);
        builder.put((Object)"ServerboundPlayerAbilitiesPacket", (Object)GamePacketTypes.SERVERBOUND_PLAYER_ABILITIES);
        builder.put((Object)"ServerboundPlayerActionPacket", (Object)GamePacketTypes.SERVERBOUND_PLAYER_ACTION);
        builder.put((Object)"ServerboundPlayerCommandPacket", (Object)GamePacketTypes.SERVERBOUND_PLAYER_COMMAND);
        builder.put((Object)"ServerboundPlayerInputPacket", (Object)GamePacketTypes.SERVERBOUND_PLAYER_INPUT);
        builder.put((Object)"ServerboundPlayerLoadedPacket", (Object)GamePacketTypes.SERVERBOUND_PLAYER_LOADED);
        builder.put((Object)"ServerboundPongPacket", (Object)CommonPacketTypes.SERVERBOUND_PONG);
        builder.put((Object)"ServerboundRecipeBookChangeSettingsPacket", (Object)GamePacketTypes.SERVERBOUND_RECIPE_BOOK_CHANGE_SETTINGS);
        builder.put((Object)"ServerboundRecipeBookSeenRecipePacket", (Object)GamePacketTypes.SERVERBOUND_RECIPE_BOOK_SEEN_RECIPE);
        builder.put((Object)"ServerboundRenameItemPacket", (Object)GamePacketTypes.SERVERBOUND_RENAME_ITEM);
        builder.put((Object)"ServerboundResourcePackPacket", (Object)CommonPacketTypes.SERVERBOUND_RESOURCE_PACK);
        builder.put((Object)"ServerboundSeenAdvancementsPacket", (Object)GamePacketTypes.SERVERBOUND_SEEN_ADVANCEMENTS);
        builder.put((Object)"ServerboundSelectBundleItemPacket", (Object)GamePacketTypes.SERVERBOUND_BUNDLE_ITEM_SELECTED);
        builder.put((Object)"ServerboundSelectKnownPacks", (Object)ConfigurationPacketTypes.SERVERBOUND_SELECT_KNOWN_PACKS);
        builder.put((Object)"ServerboundSelectTradePacket", (Object)GamePacketTypes.SERVERBOUND_SELECT_TRADE);
        builder.put((Object)"ServerboundSetBeaconPacket", (Object)GamePacketTypes.SERVERBOUND_SET_BEACON);
        builder.put((Object)"ServerboundSetCarriedItemPacket", (Object)GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM);
        builder.put((Object)"ServerboundSetCommandBlockPacket", (Object)GamePacketTypes.SERVERBOUND_SET_COMMAND_BLOCK);
        builder.put((Object)"ServerboundSetCommandMinecartPacket", (Object)GamePacketTypes.SERVERBOUND_SET_COMMAND_MINECART);
        builder.put((Object)"ServerboundSetCreativeModeSlotPacket", (Object)GamePacketTypes.SERVERBOUND_SET_CREATIVE_MODE_SLOT);
        builder.put((Object)"ServerboundSetGameRulePacket", (Object)GamePacketTypes.SERVERBOUND_SET_GAME_RULE);
        builder.put((Object)"ServerboundSetJigsawBlockPacket", (Object)GamePacketTypes.SERVERBOUND_SET_JIGSAW_BLOCK);
        builder.put((Object)"ServerboundSetStructureBlockPacket", (Object)GamePacketTypes.SERVERBOUND_SET_STRUCTURE_BLOCK);
        builder.put((Object)"ServerboundSetTestBlockPacket", (Object)GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK);
        builder.put((Object)"ServerboundSignUpdatePacket", (Object)GamePacketTypes.SERVERBOUND_SIGN_UPDATE);
        builder.put((Object)"ServerboundSpectateEntityPacket", (Object)GamePacketTypes.SERVERBOUND_SPECTATE_ENTITY);
        builder.put((Object)"ServerboundStatusRequestPacket", (Object)StatusPacketTypes.SERVERBOUND_STATUS_REQUEST);
        builder.put((Object)"ServerboundSwingPacket", (Object)GamePacketTypes.SERVERBOUND_SWING);
        builder.put((Object)"ServerboundTeleportToEntityPacket", (Object)GamePacketTypes.SERVERBOUND_TELEPORT_TO_ENTITY);
        builder.put((Object)"ServerboundTestInstanceBlockActionPacket", (Object)GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION);
        builder.put((Object)"ServerboundUseItemOnPacket", (Object)GamePacketTypes.SERVERBOUND_USE_ITEM_ON);
        builder.put((Object)"ServerboundUseItemPacket", (Object)GamePacketTypes.SERVERBOUND_USE_ITEM);
        builder.put((Object)"ServerboundMovePlayerPacket.StatusOnly", (Object)GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY);
        builder.put((Object)"ClientboundAddEntityPacket", (Object)GamePacketTypes.CLIENTBOUND_ADD_ENTITY);
        builder.put((Object)"ClientboundAnimatePacket", (Object)GamePacketTypes.CLIENTBOUND_ANIMATE);
        builder.put((Object)"ClientboundAwardStatsPacket", (Object)GamePacketTypes.CLIENTBOUND_AWARD_STATS);
        builder.put((Object)"ClientboundBlockChangedAckPacket", (Object)GamePacketTypes.CLIENTBOUND_BLOCK_CHANGED_ACK);
        builder.put((Object)"ClientboundBlockDestructionPacket", (Object)GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION);
        builder.put((Object)"ClientboundBlockEntityDataPacket", (Object)GamePacketTypes.CLIENTBOUND_BLOCK_ENTITY_DATA);
        builder.put((Object)"ClientboundBlockEventPacket", (Object)GamePacketTypes.CLIENTBOUND_BLOCK_EVENT);
        builder.put((Object)"ClientboundBlockUpdatePacket", (Object)GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE);
        builder.put((Object)"ClientboundBossEventPacket", (Object)GamePacketTypes.CLIENTBOUND_BOSS_EVENT);
        builder.put((Object)"ClientboundBundleDelimiterPacket", (Object)GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER);
        builder.put((Object)"ClientboundBundlePacket", (Object)GamePacketTypes.CLIENTBOUND_BUNDLE);
        builder.put((Object)"ClientboundChangeDifficultyPacket", (Object)GamePacketTypes.CLIENTBOUND_CHANGE_DIFFICULTY);
        builder.put((Object)"ClientboundChunkBatchFinishedPacket", (Object)GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_FINISHED);
        builder.put((Object)"ClientboundChunkBatchStartPacket", (Object)GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_START);
        builder.put((Object)"ClientboundChunksBiomesPacket", (Object)GamePacketTypes.CLIENTBOUND_CHUNKS_BIOMES);
        builder.put((Object)"ClientboundClearDialogPacket", (Object)CommonPacketTypes.CLIENTBOUND_CLEAR_DIALOG);
        builder.put((Object)"ClientboundClearTitlesPacket", (Object)GamePacketTypes.CLIENTBOUND_CLEAR_TITLES);
        builder.put((Object)"ClientboundCodeOfConductPacket", (Object)ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT);
        builder.put((Object)"ClientboundCommandSuggestionsPacket", (Object)GamePacketTypes.CLIENTBOUND_COMMAND_SUGGESTIONS);
        builder.put((Object)"ClientboundCommandsPacket", (Object)GamePacketTypes.CLIENTBOUND_COMMANDS);
        builder.put((Object)"ClientboundContainerClosePacket", (Object)GamePacketTypes.CLIENTBOUND_CONTAINER_CLOSE);
        builder.put((Object)"ClientboundContainerSetContentPacket", (Object)GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT);
        builder.put((Object)"ClientboundContainerSetDataPacket", (Object)GamePacketTypes.CLIENTBOUND_CONTAINER_SET_DATA);
        builder.put((Object)"ClientboundContainerSetSlotPacket", (Object)GamePacketTypes.CLIENTBOUND_CONTAINER_SET_SLOT);
        builder.put((Object)"ClientboundCookieRequestPacket", (Object)CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST);
        builder.put((Object)"ClientboundCooldownPacket", (Object)GamePacketTypes.CLIENTBOUND_COOLDOWN);
        builder.put((Object)"ClientboundCustomChatCompletionsPacket", (Object)GamePacketTypes.CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS);
        builder.put((Object)"ClientboundCustomPayloadPacket", (Object)CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD);
        builder.put((Object)"ClientboundCustomQueryPacket", (Object)LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY);
        builder.put((Object)"ClientboundCustomReportDetailsPacket", (Object)CommonPacketTypes.CLIENTBOUND_CUSTOM_REPORT_DETAILS);
        builder.put((Object)"ClientboundDamageEventPacket", (Object)GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT);
        builder.put((Object)"ClientboundDebugBlockValuePacket", (Object)GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE);
        builder.put((Object)"ClientboundDebugChunkValuePacket", (Object)GamePacketTypes.CLIENTBOUND_DEBUG_CHUNK_VALUE);
        builder.put((Object)"ClientboundDebugEntityValuePacket", (Object)GamePacketTypes.CLIENTBOUND_DEBUG_ENTITY_VALUE);
        builder.put((Object)"ClientboundDebugEventPacket", (Object)GamePacketTypes.CLIENTBOUND_DEBUG_EVENT);
        builder.put((Object)"ClientboundDebugSamplePacket", (Object)GamePacketTypes.CLIENTBOUND_DEBUG_SAMPLE);
        builder.put((Object)"ClientboundDeleteChatPacket", (Object)GamePacketTypes.CLIENTBOUND_DELETE_CHAT);
        builder.put((Object)"ClientboundDisconnectPacket", (Object)CommonPacketTypes.CLIENTBOUND_DISCONNECT);
        builder.put((Object)"ClientboundDisguisedChatPacket", (Object)GamePacketTypes.CLIENTBOUND_DISGUISED_CHAT);
        builder.put((Object)"ClientboundEntityEventPacket", (Object)GamePacketTypes.CLIENTBOUND_ENTITY_EVENT);
        builder.put((Object)"ClientboundEntityPositionSyncPacket", (Object)GamePacketTypes.CLIENTBOUND_ENTITY_POSITION_SYNC);
        builder.put((Object)"ClientboundExplodePacket", (Object)GamePacketTypes.CLIENTBOUND_EXPLODE);
        builder.put((Object)"ClientboundFinishConfigurationPacket", (Object)ConfigurationPacketTypes.CLIENTBOUND_FINISH_CONFIGURATION);
        builder.put((Object)"ClientboundForgetLevelChunkPacket", (Object)GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK);
        builder.put((Object)"ClientboundGameEventPacket", (Object)GamePacketTypes.CLIENTBOUND_GAME_EVENT);
        builder.put((Object)"ClientboundGameRuleValuesPacket", (Object)GamePacketTypes.CLIENTBOUND_GAME_RULE_VALUES);
        builder.put((Object)"ClientboundGameTestHighlightPosPacket", (Object)GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS);
        builder.put((Object)"ClientboundHelloPacket", (Object)LoginPacketTypes.CLIENTBOUND_HELLO);
        builder.put((Object)"ClientboundHurtAnimationPacket", (Object)GamePacketTypes.CLIENTBOUND_HURT_ANIMATION);
        builder.put((Object)"ClientboundInitializeBorderPacket", (Object)GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER);
        builder.put((Object)"ClientboundKeepAlivePacket", (Object)CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE);
        builder.put((Object)"ClientboundLevelChunkWithLightPacket", (Object)GamePacketTypes.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT);
        builder.put((Object)"ClientboundLevelEventPacket", (Object)GamePacketTypes.CLIENTBOUND_LEVEL_EVENT);
        builder.put((Object)"ClientboundLevelParticlesPacket", (Object)GamePacketTypes.CLIENTBOUND_LEVEL_PARTICLES);
        builder.put((Object)"ClientboundLightUpdatePacket", (Object)GamePacketTypes.CLIENTBOUND_LIGHT_UPDATE);
        builder.put((Object)"ClientboundLoginCompressionPacket", (Object)LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION);
        builder.put((Object)"ClientboundLoginDisconnectPacket", (Object)LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT);
        builder.put((Object)"ClientboundLoginFinishedPacket", (Object)LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED);
        builder.put((Object)"ClientboundLoginPacket", (Object)GamePacketTypes.CLIENTBOUND_LOGIN);
        builder.put((Object)"ClientboundLowDiskSpaceWarningPacket", (Object)GamePacketTypes.CLIENTBOUND_LOW_DISK_SPACE_WARNING);
        builder.put((Object)"ClientboundMapItemDataPacket", (Object)GamePacketTypes.CLIENTBOUND_MAP_ITEM_DATA);
        builder.put((Object)"ClientboundMerchantOffersPacket", (Object)GamePacketTypes.CLIENTBOUND_MERCHANT_OFFERS);
        builder.put((Object)"ClientboundMountScreenOpenPacket", (Object)GamePacketTypes.CLIENTBOUND_MOUNT_SCREEN_OPEN);
        builder.put((Object)"ClientboundMoveMinecartPacket", (Object)GamePacketTypes.CLIENTBOUND_MOVE_MINECART_ALONG_TRACK);
        builder.put((Object)"ClientboundMoveVehiclePacket", (Object)GamePacketTypes.CLIENTBOUND_MOVE_VEHICLE);
        builder.put((Object)"ClientboundOpenBookPacket", (Object)GamePacketTypes.CLIENTBOUND_OPEN_BOOK);
        builder.put((Object)"ClientboundOpenScreenPacket", (Object)GamePacketTypes.CLIENTBOUND_OPEN_SCREEN);
        builder.put((Object)"ClientboundOpenSignEditorPacket", (Object)GamePacketTypes.CLIENTBOUND_OPEN_SIGN_EDITOR);
        builder.put((Object)"ClientboundPingPacket", (Object)CommonPacketTypes.CLIENTBOUND_PING);
        builder.put((Object)"ClientboundPlaceGhostRecipePacket", (Object)GamePacketTypes.CLIENTBOUND_PLACE_GHOST_RECIPE);
        builder.put((Object)"ClientboundPlayerAbilitiesPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_ABILITIES);
        builder.put((Object)"ClientboundPlayerChatPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_CHAT);
        builder.put((Object)"ClientboundPlayerCombatEndPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_END);
        builder.put((Object)"ClientboundPlayerCombatEnterPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_ENTER);
        builder.put((Object)"ClientboundPlayerCombatKillPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_KILL);
        builder.put((Object)"ClientboundPlayerInfoRemovePacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_INFO_REMOVE);
        builder.put((Object)"ClientboundPlayerInfoUpdatePacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_INFO_UPDATE);
        builder.put((Object)"ClientboundPlayerLookAtPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_LOOK_AT);
        builder.put((Object)"ClientboundPlayerPositionPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_POSITION);
        builder.put((Object)"ClientboundPlayerRotationPacket", (Object)GamePacketTypes.CLIENTBOUND_PLAYER_ROTATION);
        builder.put((Object)"ClientboundPongResponsePacket", (Object)PingPacketTypes.CLIENTBOUND_PONG_RESPONSE);
        builder.put((Object)"ClientboundProjectilePowerPacket", (Object)GamePacketTypes.CLIENTBOUND_PROJECTILE_POWER);
        builder.put((Object)"ClientboundRecipeBookAddPacket", (Object)GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_ADD);
        builder.put((Object)"ClientboundRecipeBookRemovePacket", (Object)GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_REMOVE);
        builder.put((Object)"ClientboundRecipeBookSettingsPacket", (Object)GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_SETTINGS);
        builder.put((Object)"ClientboundRegistryDataPacket", (Object)ConfigurationPacketTypes.CLIENTBOUND_REGISTRY_DATA);
        builder.put((Object)"ClientboundRemoveEntitiesPacket", (Object)GamePacketTypes.CLIENTBOUND_REMOVE_ENTITIES);
        builder.put((Object)"ClientboundRemoveMobEffectPacket", (Object)GamePacketTypes.CLIENTBOUND_REMOVE_MOB_EFFECT);
        builder.put((Object)"ClientboundResetChatPacket", (Object)ConfigurationPacketTypes.CLIENTBOUND_RESET_CHAT);
        builder.put((Object)"ClientboundResetScorePacket", (Object)GamePacketTypes.CLIENTBOUND_RESET_SCORE);
        builder.put((Object)"ClientboundResourcePackPopPacket", (Object)CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP);
        builder.put((Object)"ClientboundResourcePackPushPacket", (Object)CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH);
        builder.put((Object)"ClientboundRespawnPacket", (Object)GamePacketTypes.CLIENTBOUND_RESPAWN);
        builder.put((Object)"ClientboundRotateHeadPacket", (Object)GamePacketTypes.CLIENTBOUND_ROTATE_HEAD);
        builder.put((Object)"ClientboundSectionBlocksUpdatePacket", (Object)GamePacketTypes.CLIENTBOUND_SECTION_BLOCKS_UPDATE);
        builder.put((Object)"ClientboundSelectAdvancementsTabPacket", (Object)GamePacketTypes.CLIENTBOUND_SELECT_ADVANCEMENTS_TAB);
        builder.put((Object)"ClientboundSelectKnownPacks", (Object)ConfigurationPacketTypes.CLIENTBOUND_SELECT_KNOWN_PACKS);
        builder.put((Object)"ClientboundServerDataPacket", (Object)GamePacketTypes.CLIENTBOUND_SERVER_DATA);
        builder.put((Object)"ClientboundServerLinksPacket", (Object)CommonPacketTypes.CLIENTBOUND_SERVER_LINKS);
        builder.put((Object)"ClientboundSetActionBarTextPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_ACTION_BAR_TEXT);
        builder.put((Object)"ClientboundSetBorderCenterPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER);
        builder.put((Object)"ClientboundSetBorderLerpSizePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE);
        builder.put((Object)"ClientboundSetBorderSizePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE);
        builder.put((Object)"ClientboundSetBorderWarningDelayPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY);
        builder.put((Object)"ClientboundSetBorderWarningDistancePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE);
        builder.put((Object)"ClientboundSetCameraPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_CAMERA);
        builder.put((Object)"ClientboundSetChunkCacheCenterPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_CENTER);
        builder.put((Object)"ClientboundSetChunkCacheRadiusPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_RADIUS);
        builder.put((Object)"ClientboundSetCursorItemPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_CURSOR_ITEM);
        builder.put((Object)"ClientboundSetDefaultSpawnPositionPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION);
        builder.put((Object)"ClientboundSetDisplayObjectivePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_DISPLAY_OBJECTIVE);
        builder.put((Object)"ClientboundSetEntityDataPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_ENTITY_DATA);
        builder.put((Object)"ClientboundSetEntityLinkPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_ENTITY_LINK);
        builder.put((Object)"ClientboundSetEntityMotionPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_ENTITY_MOTION);
        builder.put((Object)"ClientboundSetEquipmentPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_EQUIPMENT);
        builder.put((Object)"ClientboundSetExperiencePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_EXPERIENCE);
        builder.put((Object)"ClientboundSetHealthPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_HEALTH);
        builder.put((Object)"ClientboundSetHeldSlotPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_HELD_SLOT);
        builder.put((Object)"ClientboundSetObjectivePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_OBJECTIVE);
        builder.put((Object)"ClientboundSetPassengersPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_PASSENGERS);
        builder.put((Object)"ClientboundSetPlayerInventoryPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY);
        builder.put((Object)"ClientboundSetPlayerTeamPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_PLAYER_TEAM);
        builder.put((Object)"ClientboundSetScorePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_SCORE);
        builder.put((Object)"ClientboundSetSimulationDistancePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_SIMULATION_DISTANCE);
        builder.put((Object)"ClientboundSetSubtitleTextPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_SUBTITLE_TEXT);
        builder.put((Object)"ClientboundSetTimePacket", (Object)GamePacketTypes.CLIENTBOUND_SET_TIME);
        builder.put((Object)"ClientboundSetTitleTextPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_TITLE_TEXT);
        builder.put((Object)"ClientboundSetTitlesAnimationPacket", (Object)GamePacketTypes.CLIENTBOUND_SET_TITLES_ANIMATION);
        builder.put((Object)"ClientboundShowDialogPacket", (Object)CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG);
        builder.put((Object)"ClientboundSoundEntityPacket", (Object)GamePacketTypes.CLIENTBOUND_SOUND_ENTITY);
        builder.put((Object)"ClientboundSoundPacket", (Object)GamePacketTypes.CLIENTBOUND_SOUND);
        builder.put((Object)"ClientboundStartConfigurationPacket", (Object)GamePacketTypes.CLIENTBOUND_START_CONFIGURATION);
        builder.put((Object)"ClientboundStatusResponsePacket", (Object)StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE);
        builder.put((Object)"ClientboundStopSoundPacket", (Object)GamePacketTypes.CLIENTBOUND_STOP_SOUND);
        builder.put((Object)"ClientboundStoreCookiePacket", (Object)CommonPacketTypes.CLIENTBOUND_STORE_COOKIE);
        builder.put((Object)"ClientboundSystemChatPacket", (Object)GamePacketTypes.CLIENTBOUND_SYSTEM_CHAT);
        builder.put((Object)"ClientboundTabListPacket", (Object)GamePacketTypes.CLIENTBOUND_TAB_LIST);
        builder.put((Object)"ClientboundTagQueryPacket", (Object)GamePacketTypes.CLIENTBOUND_TAG_QUERY);
        builder.put((Object)"ClientboundTakeItemEntityPacket", (Object)GamePacketTypes.CLIENTBOUND_TAKE_ITEM_ENTITY);
        builder.put((Object)"ClientboundTeleportEntityPacket", (Object)GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY);
        builder.put((Object)"ClientboundTestInstanceBlockStatus", (Object)GamePacketTypes.CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS);
        builder.put((Object)"ClientboundTickingStatePacket", (Object)GamePacketTypes.CLIENTBOUND_TICKING_STATE);
        builder.put((Object)"ClientboundTickingStepPacket", (Object)GamePacketTypes.CLIENTBOUND_TICKING_STEP);
        builder.put((Object)"ClientboundTrackedWaypointPacket", (Object)GamePacketTypes.CLIENTBOUND_WAYPOINT);
        builder.put((Object)"ClientboundTransferPacket", (Object)CommonPacketTypes.CLIENTBOUND_TRANSFER);
        builder.put((Object)"ClientboundUpdateAdvancementsPacket", (Object)GamePacketTypes.CLIENTBOUND_UPDATE_ADVANCEMENTS);
        builder.put((Object)"ClientboundUpdateAttributesPacket", (Object)GamePacketTypes.CLIENTBOUND_UPDATE_ATTRIBUTES);
        builder.put((Object)"ClientboundUpdateEnabledFeaturesPacket", (Object)ConfigurationPacketTypes.CLIENTBOUND_UPDATE_ENABLED_FEATURES);
        builder.put((Object)"ClientboundUpdateMobEffectPacket", (Object)GamePacketTypes.CLIENTBOUND_UPDATE_MOB_EFFECT);
        builder.put((Object)"ClientboundUpdateRecipesPacket", (Object)GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES);
        builder.put((Object)"ClientboundUpdateTagsPacket", (Object)CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS);
        builder.put((Object)"ClientboundMoveEntityPacket.Pos", (Object)GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS);
        builder.put((Object)"ClientboundMoveEntityPacket.PosRot", (Object)GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT);
        builder.put((Object)"ClientboundMoveEntityPacket.Rot", (Object)GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT);
        LEGACY_PACKET_MAPPINGS = builder.buildOrThrow();
    }
}

