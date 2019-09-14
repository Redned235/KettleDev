package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import mc.kettle.kettle.converter.data.block.type.LeavesDataConverter.DecayableDataValue;
import mc.kettle.kettle.converter.data.block.type.LogDataConverter.TreeDataValue;
import org.spongepowered.api.data.manipulator.mutable.block.DecayableData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;

import static org.spongepowered.api.data.type.TreeTypes.ACACIA;
import static org.spongepowered.api.data.type.TreeTypes.DARK_OAK;

public final class Leaves2DataConverter extends DataTypeConverter {
    private Leaves2DataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new TreeDataValue(ACACIA), (byte) 0)
                        .put(new TreeDataValue(DARK_OAK), (byte) 1)
                        .build(),
                (byte) 2
        );
        applicableTypes.add(TreeData.class);
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new DecayableDataValue(false), (byte) 0)
                        .put(new DecayableDataValue(true), (byte) 1)
                        .build(),
                (byte) 1
        );
        applicableTypes.add(DecayableData.class);
    }
}