package io.github.hsyyid.spongychest.data.isspongychest;

import io.github.hsyyid.spongychest.SpongyChest;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableSpongeIsSpongyChestData extends AbstractImmutableBooleanData<ImmutableIsSpongyChestData, IsSpongyChestData> implements ImmutableIsSpongyChestData
{
	public ImmutableSpongeIsSpongyChestData(boolean value)
	{
		super(value, SpongyChest.IS_SPONGY_CHEST, false);
	}

	@Override
	public ImmutableValue<Boolean> isSpongyChestData()
	{
		return getValueGetter();
	}

	@Override
	public <E> Optional<ImmutableIsSpongyChestData> with(Key<? extends BaseValue<E>> key, E value)
	{
		if (this.supports(key))
		{
			return Optional.of(asMutable().set(key, value).asImmutable());
		}
		else
		{
			return Optional.empty();
		}
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(SpongyChest.IS_SPONGY_CHEST.getQuery(), this.getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public IsSpongyChestData asMutable()
	{
		return new SpongeIsSpongyChestData(getValue());
	}

}
