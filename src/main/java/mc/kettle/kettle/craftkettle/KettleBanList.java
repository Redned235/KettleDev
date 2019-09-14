package mc.kettle.kettle.craftkettle;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import mc.kettle.kettle.Kettle;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.util.ban.Ban;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class KettleBanList<T extends Ban> implements BanList {
    private final Cache<String, KettleBanEntry> cache = CacheBuilder.newBuilder().weakValues().build();

    @Override
    public BanEntry getBanEntry(String target) {
        return getBanFor(target).map(this::loadBanEntry).orElse(null);
    }

    @Override
    public BanEntry addBan(String target, String reason, Date expires, String source) {
        try {
            BanEntry entry = cache.get(target, () -> new KettleBanEntry(target, this));
            entry.setCreated(new Date());
            entry.setReason(reason);
            entry.setExpiration(expires);
            entry.setSource(source);
            entry.save();
            return entry;
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        return getBans().stream().map(this::loadBanEntry).collect(Collectors.toSet());
    }

    @Override
    public boolean isBanned(String target) {
        return getBanFor(target).isPresent();
    }

    private BanEntry loadBanEntry(T ban) {
        try {
            return cache.get(getTarget(ban), () -> new KettleBanEntry(ban, this));
        } catch (ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }

    protected abstract Optional<T> getBanFor(String target);

    protected abstract boolean setTarget(Ban.Builder builder, String target);

    protected abstract Collection<T> getBans();

    protected abstract String getTarget(T ban);

    protected static BanService getBanService() {
        return Kettle.getGame().getServiceManager().provideUnchecked(BanService.class);
    }
}
