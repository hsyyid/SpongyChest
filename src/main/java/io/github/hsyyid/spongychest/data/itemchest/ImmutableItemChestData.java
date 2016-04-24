package io.github.hsyyid.spongychest.data.itemchest;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public interface ImmutableItemChestData extends ImmutableDataManipulator<ImmutableItemChestData, ItemChestData>
{
	ImmutableValue<ItemStackSnapshot> itemStackSnapshot();
}
