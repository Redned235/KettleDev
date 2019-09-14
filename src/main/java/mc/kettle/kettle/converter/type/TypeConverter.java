package mc.kettle.kettle.converter.type;

import com.google.common.base.Converter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TypeConverter<B, S> extends Converter<B, S> {
    private final BiMap<B, S> registry = HashBiMap.create();

    private TypeConverter() {
    }

    public static <B, S> TypeConverter<B, S> builder(Class<B> bukkit, Class<S> sponge) {
        return new TypeConverter<>();
    }

    public TypeConverter<B, S> build() {
        return this;
    }

    private static <T> T checkDefined(T result, Object input) {
        if (result == null) {
            throw new UnsupportedOperationException(input.toString());
        }

        return result;
    }

    @Override
    protected S doForward(B b) {
        if (b == null) {
            return null;
        }

        return checkDefined(registry.get(b), b);
    }

    @Override
    protected B doBackward(S s) {
        if (s == null) {
            return null;
        }

        return checkDefined(registry.inverse().get(s), s);
    }

    public TypeConverter<B, S> add(@NonNull B bukkit, @NonNull S sponge) {
        registry.put(bukkit, sponge);
        return this;
    }
}