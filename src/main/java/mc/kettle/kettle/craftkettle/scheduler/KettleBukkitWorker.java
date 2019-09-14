package mc.kettle.kettle.craftkettle.scheduler;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

public class KettleBukkitWorker implements BukkitWorker {
    @Override
    public int getTaskId() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Plugin getOwner() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Thread getThread() {
        throw new NotImplementedException("TODO");
    }
}