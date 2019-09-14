package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import org.spongepowered.api.data.manipulator.mutable.block.BigMushroomData;
import org.spongepowered.api.data.type.BigMushroomType;

import static org.spongepowered.api.data.type.BigMushroomTypes.*;

public final class BigMushroomDataConverter extends DataTypeConverter {
    private BigMushroomDataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new BigMushroomDataValue(ALL_INSIDE), (byte) 0)
                        .put(new BigMushroomDataValue(NORTH_WEST), (byte) 1)
                        .put(new BigMushroomDataValue(NORTH), (byte) 2)
                        .put(new BigMushroomDataValue(NORTH_EAST), (byte) 3)
                        .put(new BigMushroomDataValue(WEST), (byte) 4)
                        .put(new BigMushroomDataValue(CENTER), (byte) 5)
                        .put(new BigMushroomDataValue(EAST), (byte) 6)
                        .put(new BigMushroomDataValue(SOUTH_WEST), (byte) 7)
                        .put(new BigMushroomDataValue(SOUTH), (byte) 8)
                        .put(new BigMushroomDataValue(SOUTH_EAST), (byte) 9)
                        .put(new BigMushroomDataValue(STEM), (byte) 10)
                        .put(new BigMushroomDataValue(ALL_OUTSIDE), (byte) 14)
                        .put(new BigMushroomDataValue(ALL_STEM), (byte) 15)
                        .build(),
                (byte) 4);
        applicableTypes.add(BigMushroomData.class);
    }

    static class BigMushroomDataValue extends AbstractDataValue<BigMushroomData, BigMushroomType> {
        BigMushroomDataValue(BigMushroomType value) {
            super(BigMushroomData.class, value);
        }
    }
}