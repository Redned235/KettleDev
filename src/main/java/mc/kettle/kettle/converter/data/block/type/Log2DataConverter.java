package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import org.spongepowered.api.data.manipulator.mutable.block.AxisData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;
import org.spongepowered.api.util.Axis;

import static org.spongepowered.api.data.type.TreeTypes.ACACIA;
import static org.spongepowered.api.data.type.TreeTypes.DARK_OAK;

public final class Log2DataConverter extends DataTypeConverter {
    private Log2DataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new LogDataConverter.TreeDataValue(ACACIA), (byte) 0)
                        .put(new LogDataConverter.TreeDataValue(DARK_OAK), (byte) 1)
                        .build(),
                (byte) 2
        );
        applicableTypes.add(TreeData.class);
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new LogDataConverter.AxisDataValue(Axis.Y), (byte) 0)
                        .put(new LogDataConverter.AxisDataValue(Axis.X), (byte) 1)
                        .put(new LogDataConverter.AxisDataValue(Axis.Z), (byte) 2)
                        .put(new LogDataConverter.AxisDataValue(null), (byte) 3)
                        .build(),
                (byte) 2
        );
        applicableTypes.add(AxisData.class);
    }
}