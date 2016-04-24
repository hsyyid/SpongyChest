package io.github.hsyyid.spongychest.data.pricechest;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutablePriceChestData extends ImmutableDataManipulator<ImmutablePriceChestData, PriceChestData>
{
	ImmutableValue<Double> price();
}
