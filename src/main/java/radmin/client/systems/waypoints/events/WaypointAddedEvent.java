/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.systems.waypoints.events;

import radmin.client.systems.waypoints.Waypoint;

public record WaypointAddedEvent(Waypoint waypoint) {
}
