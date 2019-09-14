package mc.kettle.kettle.util;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public final class KettleText {
    private KettleText() {
    }

    public static String convert(Text text) {
        return text != null ? TextSerializers.LEGACY_FORMATTING_CODE.serialize(text) : null;
    }

    public static Text convert(String text) {
        return text != null ? TextSerializers.LEGACY_FORMATTING_CODE.deserialize(text) : null;
    }
}
