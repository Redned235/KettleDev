package mc.kettle.kettle.craftkettle.scheduler;

import mc.kettle.kettle.plugin.KettlePluginContainer;
import mc.kettle.kettle.util.KettleWrapper;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.spongepowered.api.scheduler.Task;

public class KettleBukkitTask extends KettleWrapper<Task> implements BukkitTask {
    private final int id;

    public KettleBukkitTask(Task handle, int id) {
        super(handle);
        this.id = id;
    }

    @Override
    public int getTaskId() {
        return this.id;
    }

    @Override
    public Plugin getOwner() {
        return ((KettlePluginContainer) getHandle().getOwner()).getHandle();
    }

    @Override
    public boolean isSync() {
        return !getHandle().isAsynchronous();
    }

    @Override
    public boolean isCancelled() {
        return getHandle().cancel();
    }

    @Override
    public void cancel() {
        if (!getHandle().cancel()) {
            throw new RuntimeException("Failed to cancel task " + getTaskId());
        }
    }
}