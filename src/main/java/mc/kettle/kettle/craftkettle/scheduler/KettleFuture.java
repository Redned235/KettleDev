package mc.kettle.kettle.craftkettle.scheduler;

import com.google.common.util.concurrent.AbstractFuture;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.Callable;

class KettleFuture<T> extends AbstractFuture<T> implements Runnable {
    private final Callable<T> callable;
    Task handle;

    KettleFuture(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            set(callable.call());
        } catch (Exception e) {
            setException(e);
        }
    }

    @Override
    protected void interruptTask() {
        handle.cancel();
    }
}