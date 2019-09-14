package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

/**
 * Represents events within a world
 */
public abstract class WorldEvent extends Event {
    private final World world;

    public WorldEvent(final World world) {
        this.world = world;
    }

    protected WorldEvent(final Chunk chunk) {
        this.world = isKettleEvent() ? null : chunk.getWorld();
    }

    protected WorldEvent(final Location location) {
        this.world = isKettleEvent() ? null : location.getWorld();
    }

    /**
     * Gets the world primarily involved with this event
     *
     * @return World which caused this event
     */
    public World getWorld() {
        return world;
    }
}
