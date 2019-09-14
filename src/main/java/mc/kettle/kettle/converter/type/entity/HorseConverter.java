package mc.kettle.kettle.converter.type.entity;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.bukkit.entity.Horse;
import org.spongepowered.api.data.type.HorseColor;
import org.spongepowered.api.data.type.HorseColors;
import org.spongepowered.api.data.type.HorseStyle;
import org.spongepowered.api.data.type.HorseStyles;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

public final class HorseConverter {
    private HorseConverter() {
    }

    private static final Converter<Horse.Variant, EntityType> CONVERTER_V = TypeConverter.builder(Horse.Variant.class, EntityType.class)
            .add(Horse.Variant.HORSE, EntityTypes.HORSE)
            .add(Horse.Variant.DONKEY, EntityTypes.DONKEY)
            .add(Horse.Variant.MULE, EntityTypes.MULE)
            .add(Horse.Variant.UNDEAD_HORSE, EntityTypes.ZOMBIE_HORSE)
            .add(Horse.Variant.SKELETON_HORSE, EntityTypes.SKELETON_HORSE)
            .add(Horse.Variant.LLAMA, EntityTypes.LLAMA)
            .build();
    private static final Converter<Horse.Color, HorseColor> CONVERTER_C = TypeConverter.builder(Horse.Color.class, HorseColor.class)
            .add(Horse.Color.WHITE, HorseColors.WHITE)
            .add(Horse.Color.CREAMY, HorseColors.CREAMY)
            .add(Horse.Color.CHESTNUT, HorseColors.CHESTNUT)
            .add(Horse.Color.BROWN, HorseColors.BROWN)
            .add(Horse.Color.BLACK, HorseColors.BLACK)
            .add(Horse.Color.GRAY, HorseColors.GRAY)
            .add(Horse.Color.DARK_BROWN, HorseColors.DARK_BROWN)
            .build();
    private static final Converter<Horse.Style, HorseStyle> CONVERTER_S = TypeConverter.builder(Horse.Style.class, HorseStyle.class)
            .add(Horse.Style.NONE, HorseStyles.NONE)
            .add(Horse.Style.WHITE, HorseStyles.WHITE)
            .add(Horse.Style.WHITEFIELD, HorseStyles.WHITEFIELD)
            .add(Horse.Style.WHITE_DOTS, HorseStyles.WHITE_DOTS)
            .add(Horse.Style.BLACK_DOTS, HorseStyles.BLACK_DOTS)
            .build();

    public static EntityType of(Horse.Variant varient) {
        return CONVERTER_V.convert(varient);
    }

    public static Horse.Variant of(EntityType type) {
        return CONVERTER_V.reverse().convert(type);
    }

    public static HorseColor of(Horse.Color color) {
        return CONVERTER_C.convert(color);
    }

    public static Horse.Color of(HorseColor color) {
        return CONVERTER_C.reverse().convert(color);
    }

    public static HorseStyle of(Horse.Style style) {
        return CONVERTER_S.convert(style);
    }

    public static Horse.Style of(HorseStyle style) {
        return CONVERTER_S.reverse().convert(style);
    }
}