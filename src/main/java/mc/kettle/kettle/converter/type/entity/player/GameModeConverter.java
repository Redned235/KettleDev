package mc.kettle.kettle.converter.type.entity.player;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import static org.bukkit.GameMode.*;

public final class GameModeConverter {
    private GameModeConverter() {
    }

    private static final Converter<org.bukkit.GameMode, GameMode> CONVERTER = TypeConverter.builder(org.bukkit.GameMode.class, GameMode.class)
            .add(NOT_SET, GameModes.NOT_SET)
            .add(SURVIVAL, GameModes.SURVIVAL)
            .add(CREATIVE, GameModes.CREATIVE)
            .add(ADVENTURE, GameModes.ADVENTURE)
            .add(SPECTATOR, GameModes.SPECTATOR)
            .build();

    public static GameMode of(org.bukkit.GameMode mode) {
        return CONVERTER.convert(mode);
    }

    public static org.bukkit.GameMode of(GameMode mode) {
        return CONVERTER.reverse().convert(mode);
    }
}
