package mc.kettle.kettle.craftkettle.scheduler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import mc.kettle.kettle.Kettle;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class KettleBukkitScheduler implements BukkitScheduler {
    private static final AtomicInteger id = new AtomicInteger();
    private final Map<Integer, Task> tasks = new MapMaker().weakValues().makeMap();

    private static Scheduler getScheduler() {
        return Kettle.getGame().getScheduler();
    }

    private static Task.Builder newTask() {
        return getScheduler().createTaskBuilder();
    }

    private static void validate(Plugin plugin, Object task) {
        Preconditions.checkState(plugin != null, "Plugin is null");
        Preconditions.checkNotNull(task, "task");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        validate(plugin, task);
        KettleFuture<T> future = new KettleFuture<>(task);
        future.handle = newTask().execute(future).submit(Kettle.getPlugin(plugin));
        return future;
    }

    @Override
    public void cancelTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        getScheduler().getScheduledTasks().forEach(Task::cancel);
    }

    @Override
    public void cancelAllTasks() {
        getScheduler().getScheduledTasks().forEach(Task::cancel);
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public boolean isQueued(int taskId) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return ImmutableList.of();
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        throw new NotImplementedException("TODO");
    }

    private BukkitTask register(KettleBukkitTask task) {
        tasks.put(task.getTaskId(), task.getHandle());
        return task;
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable task) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().async().execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().async().execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().delayTicks(delay).execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().async().delayTicks(delay).execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().delayTicks(delay).intervalTicks(period).execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        validate(plugin, task);
        return register(new KettleBukkitTask(newTask().async().delayTicks(delay).intervalTicks(period).execute(task).submit(Kettle.getPlugin(plugin)), id.incrementAndGet()));
    }

    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return runTask(plugin, (Runnable) task);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return runTaskAsynchronously(plugin, (Runnable) task);
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return runTaskLater(plugin, (Runnable) task, delay);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return runTaskLaterAsynchronously(plugin, (Runnable) task, delay);
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return runTaskTimer(plugin, (Runnable) task, delay, period);
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return runTaskTimerAsynchronously(plugin, (Runnable) task, delay, period);
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return runTask(plugin, task).getTaskId();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return runTaskLater(plugin, task, delay).getTaskId();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        return runTaskLater(plugin, task, delay).getTaskId();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        return runTask(plugin, task).getTaskId();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        return runTaskTimer(plugin, task, delay, period).getTaskId();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
        return runTaskTimer(plugin, task, delay, period).getTaskId();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return runTaskAsynchronously(plugin, task).getTaskId();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return runTaskLaterAsynchronously(plugin, task, delay).getTaskId();
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        return runTaskTimerAsynchronously(plugin, task, delay, period).getTaskId();
    }
}