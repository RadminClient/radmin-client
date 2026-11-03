/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.fabricmc.loader.api.entrypoint.EntrypointContainer
 *  net.fabricmc.loader.api.metadata.ModMetadata
 *  net.fabricmc.loader.api.metadata.Person
 */
package radmin.client.addons;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import radmin.client.MeteorClient;
import radmin.client.addons.GithubRepo;
import radmin.client.addons.MeteorAddon;

public class AddonManager {
    public static final List<MeteorAddon> ADDONS = new ArrayList<MeteorAddon>();

    public static void init() {
        MeteorClient.ADDON = new MeteorAddon(){

            @Override
            public void onInitialize() {
            }

            @Override
            public String getPackage() {
                return "radmin.client";
            }

            @Override
            public String getWebsite() {
                return "https://meteorclient.com";
            }

            @Override
            public GithubRepo getRepo() {
                return new GithubRepo("MeteorDevelopment", "radmin-client");
            }

            @Override
            public String getCommit() {
                String commit = MeteorClient.MOD_META.getCustomValue("radmin-client:commit").getAsString();
                return commit.isEmpty() ? null : commit;
            }
        };
        ModMetadata metadata = ((ModContainer)FabricLoader.getInstance().getModContainer("radmin-client").get()).getMetadata();
        MeteorClient.ADDON.name = metadata.getName();
        MeteorClient.ADDON.authors = new String[metadata.getAuthors().size()];
        if (metadata.containsCustomValue("radmin-client:color")) {
            MeteorClient.ADDON.color.parse(metadata.getCustomValue("radmin-client:color").getAsString());
        }
        int i = 0;
        for (Person author : metadata.getAuthors()) {
            MeteorClient.ADDON.authors[i++] = author.getName();
        }
        ADDONS.add(MeteorClient.ADDON);
        for (EntrypointContainer entrypoint : FabricLoader.getInstance().getEntrypointContainers("meteor", MeteorAddon.class)) {
            MeteorAddon addon;
            ModMetadata metadata2 = entrypoint.getProvider().getMetadata();
            try {
                addon = (MeteorAddon)entrypoint.getEntrypoint();
            }
            catch (Throwable throwable) {
                throw new RuntimeException("Exception during addon init \"%s\".".formatted(metadata2.getName()), throwable);
            }
            addon.name = metadata2.getName();
            if (metadata2.getAuthors().isEmpty()) {
                throw new RuntimeException("Addon \"%s\" requires at least 1 author to be defined in it's fabric.mod.json. See https://fabricmc.net/wiki/documentation:fabric_mod_json_spec".formatted(addon.name));
            }
            addon.authors = new String[metadata2.getAuthors().size()];
            if (metadata2.containsCustomValue("radmin-client:color")) {
                addon.color.parse(metadata2.getCustomValue("radmin-client:color").getAsString());
            }
            int i2 = 0;
            for (Person author : metadata2.getAuthors()) {
                addon.authors[i2++] = author.getName();
            }
            ADDONS.add(addon);
        }
    }
}

