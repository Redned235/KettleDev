package mc.kettle.kettle.converter.data.block;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import mc.kettle.kettle.Kettle;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataConverter;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import mc.kettle.kettle.converter.data.block.type.*;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.VariantData;
import org.spongepowered.api.world.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

public final class BlockDataConverter implements DataConverter<Location<?>> {
    public static final BlockDataConverter INSTANCE = new BlockDataConverter();
    private static final Map<Class<?>, DataTypeConverter> CONVERTER_OBJECTS = Maps.newHashMap();
    private static final Map<BlockType, DataTypeConverter> CONVERTER_MAP = ImmutableMap.<BlockType, DataTypeConverter>builder()
            .put(BlockTypes.BROWN_MUSHROOM_BLOCK, getConverter(BigMushroomDataConverter.class))
            .put(BlockTypes.RED_MUSHROOM_BLOCK, getConverter(BigMushroomDataConverter.class))
            .put(BlockTypes.LEAVES, getConverter(LeavesDataConverter.class))
            .put(BlockTypes.LEAVES2, getConverter(Leaves2DataConverter.class))
            .put(BlockTypes.LOG, getConverter(LogDataConverter.class))
            .put(BlockTypes.LOG2, getConverter(Log2DataConverter.class))
            .put(BlockTypes.PLANKS, getConverter(PlanksDataConverter.class))
            .put(BlockTypes.STONEBRICK, getConverter(BrickDataConverter.class))
            .build();

    private BlockDataConverter() {
    }

    private static DataTypeConverter getConverter(Class<?> clazz) {
        if (CONVERTER_OBJECTS.containsKey(clazz)) {
            return CONVERTER_OBJECTS.get(clazz);
        } else {
            try {
                Constructor<?> c = clazz.getDeclaredConstructors()[0];
                c.setAccessible(true);
                return (DataTypeConverter) c.newInstance();
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                Kettle.getLogger().error("Failed to instantiate " + clazz.getName());
                e.printStackTrace();
                return null;
            }
        }
    }

    public byte getDataValue(Collection<DataManipulator<?, ?>> manipulators, BlockType target) {
        final DataTypeConverter converter = getConverter(target);
        Collection<DataManipulator<?, ?>> data = Collections2.filter(manipulators, input -> {
            if (input == null) {
                return false;
            }
            try {
                Class<? extends DataManipulator<?, ?>> clazz = (Class<? extends DataManipulator<?, ?>>) Class.forName(input.getClass().getName().split("\\$")[9]);
                return converter.getApplicableDataTypes().contains(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        });
        return converter.of(data);
    }

    public byte getDataValue(BlockState target) {
        return getDataValue((Collection) target.getManipulators(), target.getType());
    }

    @Override
    public byte getDataValue(Location<?> target) {
        return getDataValue(target.getContainers(), target.getBlockType());
    }

    @Override
    public void setDataValue(Location<?> target, byte dataValue) {
        DataTypeConverter converter = getConverter(target.getBlockType());
        Collection<AbstractDataValue> data = converter.of(dataValue);
        data.stream().filter(datum -> datum.getValue() != AbstractDataValue.ABSENT).forEach(datum -> {
            DataManipulator dm = (DataManipulator) target.getOrCreate(datum.getDataClass()).get();
            if (dm instanceof VariantData) {
                ((VariantData) dm).type().set(datum.getValue());
            }
        });
    }

    private DataTypeConverter getConverter(BlockType target) {
        if (!CONVERTER_MAP.containsKey(target)) {
            throw new IllegalArgumentException("Cannot convert data for block type " + target.getName());
        }
        return CONVERTER_MAP.get(target);
    }
}