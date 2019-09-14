package mc.kettle.kettle.converter.type.cause;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.spongepowered.api.event.cause.entity.spawn.SpawnType;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;

public class SpawnReasonConverter {
    private SpawnReasonConverter() {
    }

    private static final Converter<SpawnType, CreatureSpawnEvent.SpawnReason> CONVERTER = TypeConverter.builder(SpawnType.class, CreatureSpawnEvent.SpawnReason.class)
            .add(SpawnTypes.BLOCK_SPAWNING, CreatureSpawnEvent.SpawnReason.SILVERFISH_BLOCK)
            .add(SpawnTypes.BREEDING, CreatureSpawnEvent.SpawnReason.BREEDING)
            .add(SpawnTypes.CHUNK_LOAD, CreatureSpawnEvent.SpawnReason.CHUNK_GEN)
            .add(SpawnTypes.CUSTOM, CreatureSpawnEvent.SpawnReason.CUSTOM)
            .add(SpawnTypes.DISPENSE, CreatureSpawnEvent.SpawnReason.DISPENSE_EGG)
            .add(SpawnTypes.MOB_SPAWNER, CreatureSpawnEvent.SpawnReason.SPAWNER)
            .add(SpawnTypes.SPAWN_EGG, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG)
            .add(SpawnTypes.WORLD_SPAWNER, CreatureSpawnEvent.SpawnReason.NATURAL)
            .build();

    public static CreatureSpawnEvent.SpawnReason of(SpawnType type) {
        try {
            return CONVERTER.convert(type);
        } catch (UnsupportedOperationException e) {
            return CreatureSpawnEvent.SpawnReason.DEFAULT;
        }
    }
}
