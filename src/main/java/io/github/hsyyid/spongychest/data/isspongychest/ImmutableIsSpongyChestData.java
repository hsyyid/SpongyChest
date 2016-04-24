package io.github.hsyyid.spongychest.data.isspongychest;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableIsSpongyChestData extends ImmutableDataManipulator<ImmutableIsSpongyChestData, IsSpongyChestData>
{
	ImmutableValue<Boolean> isSpongyChestData();
}
