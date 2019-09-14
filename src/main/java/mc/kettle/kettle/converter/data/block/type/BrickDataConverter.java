package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import org.spongepowered.api.data.manipulator.mutable.block.BrickData;
import org.spongepowered.api.data.type.BrickType;

import static org.spongepowered.api.data.type.BrickTypes.*;

public final class BrickDataConverter extends DataTypeConverter {
    private BrickDataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new BrickDataValue(DEFAULT), (byte) 0)
                        .put(new BrickDataValue(MOSSY), (byte) 1)
                        .put(new BrickDataValue(CRACKED), (byte) 2)
                        .put(new BrickDataValue(CHISELED), (byte) 3)
                        .build(),
                (byte) 2);
        applicableTypes.add(BrickData.class);
    }

    static class BrickDataValue extends AbstractDataValue<BrickData, BrickType> {
        BrickDataValue(BrickType value) {
            super(BrickData.class, value);
        }
    }
}
