package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import mc.kettle.kettle.converter.data.block.type.LogDataConverter.TreeDataValue;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;

import static org.spongepowered.api.data.type.TreeTypes.*;

public final class PlanksDataConverter extends DataTypeConverter {
    private PlanksDataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new TreeDataValue(OAK), (byte) 0)
                        .put(new TreeDataValue(SPRUCE), (byte) 1)
                        .put(new TreeDataValue(BIRCH), (byte) 2)
                        .put(new TreeDataValue(JUNGLE), (byte) 3)
                        .put(new TreeDataValue(ACACIA), (byte) 4)
                        .put(new TreeDataValue(DARK_OAK), (byte) 5)
                        .build(),
                (byte) 4
        );
        applicableTypes.add(TreeData.class);
    }
}
