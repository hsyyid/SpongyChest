package io.github.hsyyid.spongychest.data.uuidchest;

import io.github.hsyyid.spongychest.SpongyChest;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.Optional;
import java.util.UUID;

public class UUIDChestDataBuilder implements DataManipulatorBuilder<UUIDChestData, ImmutableUUIDChestData>
{
	@Override
	public UUIDChestData create()
	{
		return new SpongeUUIDChestData();
	}

	@Override
	public Optional<UUIDChestData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<UUIDChestData> build(DataView container)
	{
		if (!container.contains(SpongyChest.UUID_CHEST.getQuery()))
		{
			return Optional.empty();
		}

		UUIDChestData data = new SpongeUUIDChestData(UUID.fromString((String) container.get(SpongyChest.UUID_CHEST.getQuery()).get()));
		return Optional.of(data);
	}
}
