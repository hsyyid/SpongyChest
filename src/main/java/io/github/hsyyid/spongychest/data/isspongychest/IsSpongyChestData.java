package io.github.hsyyid.spongychest.data.isspongychest;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface IsSpongyChestData extends DataManipulator<IsSpongyChestData, ImmutableIsSpongyChestData>
{
	Value<Boolean> isSpongyChest();
}
