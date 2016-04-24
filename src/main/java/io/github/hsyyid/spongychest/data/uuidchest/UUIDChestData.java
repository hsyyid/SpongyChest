package io.github.hsyyid.spongychest.data.uuidchest;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.UUID;

public interface UUIDChestData extends DataManipulator<UUIDChestData, ImmutableUUIDChestData>
{
	Value<UUID> uuid();
}
