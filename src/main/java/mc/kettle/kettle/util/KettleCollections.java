package mc.kettle.kettle.util;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class KettleCollections {
    private KettleCollections() {
    }

    public static <F, T> Collection<T> transform(Collection<F> from, Function<? super F, T> function) {
        if (from instanceof List) {
            return from.stream().map(function).collect(Collectors.toList());
        }
        return Collections2.transform(from, function);
    }

    public static <F, T> List<T> transformToList(Collection<F> from, Function<? super F, ? extends T> function) {
        if (from instanceof List) {
            return Lists.transform((List<F>) from, function);
        }

        List<T> result = Lists.newArrayListWithCapacity(from.size());
        result.addAll(from.stream().map((java.util.function.Function<F, T>) function::apply).collect(Collectors.toList()));
        return result;
    }
}
