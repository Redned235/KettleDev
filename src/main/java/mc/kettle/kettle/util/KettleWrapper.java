package mc.kettle.kettle.util;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class KettleWrapper<T> {
    private final T handle;

    protected KettleWrapper(T handle) {
        this.handle = checkNotNull(handle, "handle");
    }

    public T getHandle() {
        return handle;
    }

    @Override
    public int hashCode() {
        return getHandle().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KettleWrapper) {
            return ((KettleWrapper) o).getHandle().equals(getHandle());
        }

        return super.equals(o);
    }
}
