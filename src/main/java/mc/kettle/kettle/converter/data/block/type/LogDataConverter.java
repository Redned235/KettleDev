package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import org.spongepowered.api.data.manipulator.mutable.block.AxisData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.util.Axis;

import static org.spongepowered.api.data.type.TreeTypes.*;

public final class LogDataConverter extends DataTypeConverter {
    private LogDataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new TreeDataValue(OAK), (byte) 0)
                        .put(new TreeDataValue(SPRUCE), (byte) 1)
                        .put(new TreeDataValue(BIRCH), (byte) 2)
                        .put(new TreeDataValue(JUNGLE), (byte) 3)
                        .build(),
                (byte) 2
        );
        applicableTypes.add(TreeData.class);
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new AxisDataValue(Axis.Y), (byte) 0)
                        .put(new AxisDataValue(Axis.X), (byte) 1)
                        .put(new AxisDataValue(Axis.Z), (byte) 2)
                        //TODO: add mapping for data value 3 (not yet in SpongeAPI)
                        .build(),
                (byte) 2
        );
        applicableTypes.add(AxisData.class);
    }

    static class TreeDataValue extends AbstractDataValue<TreeData, TreeType> {
        TreeDataValue(TreeType value) {
            super(TreeData.class, value);
        }
    }

    static class AxisDataValue extends AbstractDataValue<AxisData, Axis> {
        AxisDataValue(Axis value) {
            super(AxisData.class, value);
        }
    }
}