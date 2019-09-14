package mc.kettle.kettle.plugin;

import com.google.common.base.MoreObjects;
import mc.kettle.kettle.util.KettleWrapper;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class KettlePluginContainer extends KettleWrapper<Plugin> implements PluginContainer {
    public KettlePluginContainer(Plugin handle) {
        super(handle);
    }

    @Override
    public String getId() {
        return getHandle().getName();
    }

    @Override
    public String getName() {
        return getHandle().getName();
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.ofNullable(getHandle().getDescription().getVersion());
    }

    @Override
    public Optional<?> getInstance() {
        return Optional.of(getHandle());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("KettlePlugin").add("name", getName()).add("version", getVersion()).add("instance", getHandle()).toString();
    }
}