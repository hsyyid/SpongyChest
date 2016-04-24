package io.github.hsyyid.spongychest.data.pricechest;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface PriceChestData extends DataManipulator<PriceChestData, ImmutablePriceChestData>
{
	Value<Double> price();
}
