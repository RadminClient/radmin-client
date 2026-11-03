/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package radmin.client.addons;

import javax.annotation.Nullable;
import radmin.client.utils.network.Http;

public record GithubRepo(String owner, String name, String branch, @Nullable String accessToken) {
    public GithubRepo(String owner, String name, @Nullable String accessToken) {
        this(owner, name, "master", accessToken);
    }

    public GithubRepo(String owner, String name) {
        this(owner, name, "master", null);
    }

    public String getOwnerName() {
        return this.owner + "/" + this.name;
    }

    public void authenticate(Http.Request request) {
        if (this.accessToken != null && !this.accessToken.isBlank()) {
            request.bearer(this.accessToken);
        } else {
            String personalAuthToken = System.getenv("meteor.github.authorization");
            if (personalAuthToken != null && !personalAuthToken.isBlank()) {
                request.bearer(personalAuthToken);
            }
        }
    }
}

