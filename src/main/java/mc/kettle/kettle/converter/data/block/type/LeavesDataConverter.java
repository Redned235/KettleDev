package mc.kettle.kettle.converter.data.block.type;

import com.google.common.collect.ImmutableBiMap;
import mc.kettle.kettle.converter.data.AbstractDataValue;
import mc.kettle.kettle.converter.data.DataTypeConverter;
import org.spongepowered.api.data.manipulator.mutable.block.DecayableData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;

import static org.spongepowered.api.data.type.TreeTypes.*;

public final class LeavesDataConverter extends DataTypeConverter {
    private LeavesDataConverter() {
        converters.put(
                ImmutableBiMap.<AbstractDataValue, Byte>builder()
                        .put(new LogDataConverter.TreeDataValue(OAK), (byte) 0)
                        .put(new LogDataConverter.TreeDataValue(SPRUCE), (byte) 1)
                        .put(new LogDataConverter.TreeDataValue(BIRCH), (byte) 2)
                        .put(new LogDataConverter.TreeDataValue(JUNGLE), (byte) 3)
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

    static class DecayableDataValue extends AbstractDataValue<DecayableData, Object> {
        DecayableDataValue(boolean flag) {
            super(DecayableData.class, flag ? FLAG : ABSENT);
        }
    }
}
