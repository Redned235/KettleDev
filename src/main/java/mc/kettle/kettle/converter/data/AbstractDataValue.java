package mc.kettle.kettle.converter.data;

import com.google.common.base.Objects;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.mutable.VariantData;

import java.util.UUID;

public class AbstractDataValue<T extends DataManipulator<T, ?>, V> {
    public static final Object FLAG = UUID.randomUUID();
    public static final Object ABSENT = UUID.randomUUID();
    private final Class<T> clazz;
    private final V value;

    public AbstractDataValue(Class<T> clazz, V value) {
        this.clazz = clazz;
        this.value = value;
    }

    public AbstractDataValue(Class<T> clazz) {
        this.clazz = clazz;
        this.value = (V) FLAG;
    }

    public Class<T> getDataClass() {
        return this.clazz;
    }

    public V getValue() {
        return this.value;
    }

    public static <T extends DataManipulator<T, U>, U extends ImmutableDataManipulator<U, T>> AbstractDataValue of(DataManipulator<T, U> data) {
        try {
            Class<?> clazz = Class.forName(data.getClass().getName().split("\\$")[0]);
            return new AbstractDataValue(clazz, data instanceof VariantData ? ((VariantData) data).type().get() : FLAG);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> boolean nullSafeEquals(T obj1, T obj2) {
        return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AbstractDataValue && nullSafeEquals(getDataClass(), ((AbstractDataValue) o).getDataClass()) && nullSafeEquals(getValue(), ((AbstractDataValue) o).getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clazz, value);
    }
}