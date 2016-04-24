package io.github.hsyyid.spongychest.data.isspongychest;

import io.github.hsyyid.spongychest.SpongyChest;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.Optional;

public class IsSpongyChestDataBuilder implements DataManipulatorBuilder<IsSpongyChestData, ImmutableIsSpongyChestData>
{
	@Override
	public IsSpongyChestData create()
	{
		return new SpongeIsSpongyChestData();
	}

	@Override
	public Optional<IsSpongyChestData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<IsSpongyChestData> build(DataView container)
	{
		if (!container.contains(SpongyChest.IS_SPONGY_CHEST.getQuery()))
		{
			return Optional.empty();
		}

		IsSpongyChestData data = new SpongeIsSpongyChestData((Boolean) container.get(SpongyChest.IS_SPONGY_CHEST.getQuery()).get());
        return Optional.of(data);
	}
}
