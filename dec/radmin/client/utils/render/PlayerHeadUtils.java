/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package radmin.client.utils.render;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.UUID;
import radmin.client.MeteorClient;
import radmin.client.systems.accounts.TexturesJson;
import radmin.client.systems.accounts.UuidToProfileResponse;
import radmin.client.utils.PostInit;
import radmin.client.utils.network.Http;
import radmin.client.utils.render.PlayerHeadTexture;

public class PlayerHeadUtils {
    public static PlayerHeadTexture STEVE_HEAD;

    private PlayerHeadUtils() {
    }

    @PostInit
    public static void init() {
        STEVE_HEAD = new PlayerHeadTexture();
    }

    public static byte[] fetchHead(UUID id) {
        if (id == null) {
            return null;
        }
        String url = PlayerHeadUtils.getSkinUrl(id);
        if (url == null) {
            return null;
        }
        try {
            return PlayerHeadTexture.downloadHead(url);
        }
        catch (IOException e) {
            MeteorClient.LOG.error("Could not fetch player head for {}.", (Object)id, (Object)e);
            return null;
        }
    }

    public static String getSkinUrl(UUID id) {
        UuidToProfileResponse res2 = (UuidToProfileResponse)Http.get("https://sessionserver.mojang.com/session/minecraft/profile/" + String.valueOf(id)).exceptionHandler(e -> MeteorClient.LOG.error("Could not contact mojang session servers.", (Throwable)e)).sendJson((Type)((Object)UuidToProfileResponse.class));
        if (res2 == null) {
            return null;
        }
        String base64Textures = res2.getPropertyValue("textures");
        if (base64Textures == null) {
            return null;
        }
        TexturesJson textures = (TexturesJson)new Gson().fromJson(new String(Base64.getDecoder().decode(base64Textures)), TexturesJson.class);
        if (textures.textures.SKIN == null) {
            return null;
        }
        return textures.textures.SKIN.url;
    }
}

