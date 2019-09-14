package mc.kettle.kettle.converter.type.cause;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.bukkit.event.entity.EntityDamageEvent;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;

public class DamageCauseConverter {
    private DamageCauseConverter() {
    }

    private static final Converter<EntityDamageEvent.DamageCause, DamageSource> CONVERTER = TypeConverter.builder(EntityDamageEvent.DamageCause.class, DamageSource.class)
            .add(EntityDamageEvent.DamageCause.FALL, DamageSources.FALLING)
            .add(EntityDamageEvent.DamageCause.FIRE_TICK, DamageSources.FIRE_TICK)
            .add(EntityDamageEvent.DamageCause.MELTING, DamageSources.MELTING)
            .add(EntityDamageEvent.DamageCause.DROWNING, DamageSources.DROWNING)
            .add(EntityDamageEvent.DamageCause.VOID, DamageSources.VOID)
            .add(EntityDamageEvent.DamageCause.STARVATION, DamageSources.STARVATION)
            .add(EntityDamageEvent.DamageCause.POISON, DamageSources.POISON)
            .add(EntityDamageEvent.DamageCause.MAGIC, DamageSources.MAGIC)
            .add(EntityDamageEvent.DamageCause.WITHER, DamageSources.WITHER)
            .add(EntityDamageEvent.DamageCause.CUSTOM, DamageSources.GENERIC)
            .build();
    private static final Converter<EntityDamageEvent.DamageCause, DamageType> CONVERTER1 = TypeConverter.builder(EntityDamageEvent.DamageCause.class, DamageType.class)
            .add(EntityDamageEvent.DamageCause.CONTACT, DamageTypes.CONTACT)
            .add(EntityDamageEvent.DamageCause.PROJECTILE, DamageTypes.PROJECTILE)
            .add(EntityDamageEvent.DamageCause.SUFFOCATION, DamageTypes.SUFFOCATE)
            .add(EntityDamageEvent.DamageCause.FALL, DamageTypes.FALL)
            .add(EntityDamageEvent.DamageCause.FIRE, DamageTypes.FIRE)
            .add(EntityDamageEvent.DamageCause.DROWNING, DamageTypes.DROWN)
            .add(EntityDamageEvent.DamageCause.VOID, DamageTypes.VOID)
            .add(EntityDamageEvent.DamageCause.STARVATION, DamageTypes.HUNGER)
            .add(EntityDamageEvent.DamageCause.MAGIC, DamageTypes.MAGIC)
            .add(EntityDamageEvent.DamageCause.CUSTOM, DamageTypes.GENERIC)
            .add(EntityDamageEvent.DamageCause.HOT_FLOOR, DamageTypes.MAGMA)
            .build();

    public static EntityDamageEvent.DamageCause of(DamageSource type) {
        try {
            return CONVERTER.reverse().convert(type);
        } catch (UnsupportedOperationException e) {
            try {
                return CONVERTER1.reverse().convert(type.getType());
            } catch (UnsupportedOperationException ex) {
                return EntityDamageEvent.DamageCause.CUSTOM;
            }
        }
    }
}
