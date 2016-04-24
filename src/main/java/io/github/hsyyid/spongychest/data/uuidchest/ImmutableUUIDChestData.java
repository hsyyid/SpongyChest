package io.github.hsyyid.spongychest.data.uuidchest;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.UUID;

public interface ImmutableUUIDChestData extends ImmutableDataManipulator<ImmutableUUIDChestData, UUIDChestData>
{
	ImmutableValue<UUID> uuid();
}
