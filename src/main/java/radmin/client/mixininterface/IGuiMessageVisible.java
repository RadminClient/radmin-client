/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.mixininterface;

public interface IGuiMessageVisible extends IGuiMessage {
    boolean meteor$isStartOfEntry();

    void meteor$setStartOfEntry(boolean start);
}
