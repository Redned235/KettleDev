package mc.kettle.kettle.converter.wrapper;

import com.google.common.base.Function;
import mc.kettle.kettle.util.KettleWrapper;
import org.spongepowered.api.network.status.Favicon;

public final class WrapperConverter {
    private WrapperConverter() {}

    static final CachedWrapperConverter<KettleWrapper> converter = CachedWrapperConverter.builder(KettleWrapper)
            .register(Favicon.class, KettleCachedServerIcon.class)


            .build();

    public static <P extends KettleWrapper<?>> P of(Object handle) {
        return converter.get(handle);
    }

    public static <P extends KettleWrapper<?>> P of(Class<?> type, Object handle) {
        return converter.get(type, handle);
    }

    public static <S, P extends KettleWrapper<?>> Function<S, P> getConverter() {
        return (Function<S, P>) converter;
    }
}
