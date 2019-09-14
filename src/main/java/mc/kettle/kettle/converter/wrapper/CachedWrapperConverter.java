package mc.kettle.kettle.converter.wrapper;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;

final class CachedWrapperConverter<B> implements Function<Object, B> {
    private final LoadingCache<Object, Object> cache = CacheBuilder.newBuilder().weakKeys()
            .build(new CacheLoader<Object, Object>() {
                @Override
                public Object load(Object key) throws Exception {
                    return create(handle);
                }
            });

    final ImmutableMap<Class<?>, Converter<?, ? extends B>> registry;
}
