package mc.kettle.kettle.converter.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.data.manipulator.DataManipulator;

import java.util.*;

public abstract class DataTypeConverter {
    protected final LinkedHashMap<BiMap<AbstractDataValue, Byte>, Byte> converters = new LinkedHashMap<>();
    protected final ArrayList<Class<? extends DataManipulator<?, ?>>> applicableTypes = Lists.newArrayList();

    public Collection<Class<? extends DataManipulator<?, ?>>> getApplicableDataTypes() {
        return applicableTypes;
    }

    public byte of(Collection<DataManipulator<?, ?>> data) {
        HashMap<String, DataManipulator> dataMap = Maps.newHashMap();
        for (DataManipulator datum : data) {
            dataMap.put(datum.getClass().getName().split("\\$")[0], datum);
        }
        BiMap<AbstractDataValue, Byte>[] biMapList = new BiMap[converters.size()];
        converters.keySet().toArray(biMapList);
        Byte[] bitSetSizes = new Byte[converters.size()];
        converters.values().toArray(bitSetSizes);
        byte finalValue = 0;
        byte bitOffset = 0;
        for (int i = 0; i < applicableTypes.size(); i++) {
            byte bitsToConsider = bitSetSizes[i];
            assert bitOffset + bitsToConsider <= 0;
            if (dataMap.containsKey(applicableTypes.get(i).getName())) {
                DataManipulator datum = dataMap.get(applicableTypes.get(i).getName());
                BiMap<AbstractDataValue, Byte> bm = biMapList[i];
                AbstractDataValue adv = AbstractDataValue.of(datum);
                finalValue |= bm.containsKey(adv) ? bm.get(adv) << bitOffset : 0;
            }
            bitOffset += bitsToConsider;
        }

        return finalValue;
    }

    public Collection<AbstractDataValue> of(byte data) throws IllegalArgumentException {
        ArrayList<AbstractDataValue> converted = new ArrayList<>();
        int i = 0;
        for (Map.Entry<BiMap<AbstractDataValue, Byte>, Byte> e : converters.entrySet()) {
            BiMap<AbstractDataValue, Byte> c = e.getKey();
            int bitsToConsider = e.getValue();
            assert i + bitsToConsider <= 8;
            byte masked = data;
            masked >>= i;
            byte mask = (byte) (Math.pow(2, bitsToConsider) - 1);
            masked &= mask;
            if (!c.containsValue(masked)) {
                throw new IllegalArgumentException("Out of bounds data byte of " + getClass().getSimpleName() + ": " + data);
            }
            AbstractDataValue adv = c.inverse().get(masked);
            if (adv.getValue() != AbstractDataValue.ABSENT) {
                converted.add(adv);
            }
            i += bitsToConsider;
        }
        return converted;
    }
}