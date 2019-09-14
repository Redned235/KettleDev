package mc.kettle.kettle.converter.type.attribute;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.bukkit.event.Event;
import org.spongepowered.api.util.Tristate;

public final class EventResultConverter {
    private EventResultConverter() {
    }

    private static final Converter<Event.Result, Tristate> CONVERTER = TypeConverter.builder(Event.Result.class, Tristate.class)
            .add(Event.Result.ALLOW, Tristate.TRUE)
            .add(Event.Result.DEFAULT, Tristate.UNDEFINED)
            .add(Event.Result.DENY, Tristate.FALSE)
            .build();

    public static boolean of(Event.Result result) {
        return CONVERTER.convert(result).asBoolean();
    }

    public static Event.Result of(boolean cancelled) {
        return CONVERTER.reverse().convert(Tristate.fromBoolean(cancelled));
    }

    public static Tristate ofTristate(Event.Result result) {
        return CONVERTER.convert(result);
    }

    public static Event.Result ofTristate(Tristate result) {
        return CONVERTER.reverse().convert(result);
    }
}