package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Represents a task being executed by the scheduler
 */
public interface BukkitTask {

    /**
     * Returns the taskId for the task.
     *
     * @return Task id number
     */
    int getTaskId();

    /**
     * Returns the Plugin that owns this task.
     *
     * @return The Plugin that owns the task
     */
    Plugin getOwner();

    /**
     * Returns true if the Task is a sync task.
     *
     * @return true if the task is run by main thread
     */
    boolean isSync();

    /**
     * Returns true if this task has been cancelled.
     *
     * @return true if the task has been cancelled
     */
    boolean isCancelled();

    /**
     * Will attempt to cancel this task.
     */
    void cancel();
}
