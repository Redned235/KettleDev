package mc.kettle.kettle.converter.type.attribute;

import com.google.common.base.Converter;
import mc.kettle.kettle.converter.type.TypeConverter;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.event.inventory.InventoryType;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;

public class InventoryTypeConverter {
    private InventoryTypeConverter() {
    }

    private static final Converter<InventoryType, InventoryArchetype> CONVERTER = TypeConverter.builder(InventoryType.class, InventoryArchetype.class)
            .add(InventoryType.CHEST, InventoryArchetypes.CHEST)
            .add(InventoryType.DISPENSER, InventoryArchetypes.DISPENSER)
            .add(InventoryType.FURNACE, InventoryArchetypes.FURNACE)
            .add(InventoryType.WORKBENCH, InventoryArchetypes.WORKBENCH)
            .add(InventoryType.CRAFTING, InventoryArchetypes.CRAFTING)
            .add(InventoryType.ENCHANTING, InventoryArchetypes.ENCHANTING_TABLE)
            .add(InventoryType.BREWING, InventoryArchetypes.BREWING_STAND)
            .add(InventoryType.PLAYER, InventoryArchetypes.PLAYER)
            .add(InventoryType.MERCHANT, InventoryArchetypes.VILLAGER)
            .add(InventoryType.ANVIL, InventoryArchetypes.ANVIL)
            .add(InventoryType.BEACON, InventoryArchetypes.BEACON)
            .add(InventoryType.HOPPER, InventoryArchetypes.HOPPER)
            .build();

    private static InventoryArchetype of(InventoryType type) {
        switch (type) {
            case ENDER_CHEST:
                return InventoryArchetypes.CHEST;
            case DROPPER:
                return InventoryArchetypes.DISPENSER;
            case CREATIVE:
                throw new NotImplementedException("Creative inventory is not available!");
            default:
                return CONVERTER.convert(type);
        }
    }

    public static InventoryType of(InventoryArchetype type) {
        try {
            return CONVERTER.reverse().convert(type);
        } catch (UnsupportedOperationException ignored) {
            return InventoryType.CHEST;
        }
    }
}
