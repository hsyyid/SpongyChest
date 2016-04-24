package io.github.hsyyid.spongychest.data.itemchest;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public interface ItemChestData extends DataManipulator<ItemChestData, ImmutableItemChestData>
{
	Value<ItemStackSnapshot> itemStackSnapshot();
}
