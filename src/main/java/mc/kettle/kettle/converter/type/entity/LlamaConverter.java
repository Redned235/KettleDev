package mc.kettle.kettle.converter.type.entity;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.bukkit.entity.Llama;
import org.spongepowered.api.data.type.LlamaVariant;
import org.spongepowered.api.data.type.LlamaVariants;

public final class LlamaConverter {
    private static final Converter<Llama.Color, LlamaVariant> CONVERTER = TypeConverter.builder(Llama.Color.class, LlamaVariant.class)
            .add(Llama.Color.CREAMY, LlamaVariants.CREAMY)
            .add(Llama.Color.WHITE, LlamaVariants.WHITE)
            .add(Llama.Color.BROWN, LlamaVariants.BROWN)
            .add(Llama.Color.GRAY, LlamaVariants.GRAY)
            .build();

    public static LlamaVariant of(Llama.Color color) {
        return CONVERTER.convert(color);
    }

    public static Llama.Color of(LlamaVariant variant) {
        return CONVERTER.reverse().convert(variant);
    }
}