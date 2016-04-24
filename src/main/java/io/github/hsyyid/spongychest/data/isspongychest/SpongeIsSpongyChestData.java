package io.github.hsyyid.spongychest.data.isspongychest;

import com.google.common.base.Preconditions;
import io.github.hsyyid.spongychest.SpongyChest;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class SpongeIsSpongyChestData extends AbstractBooleanData<IsSpongyChestData, ImmutableIsSpongyChestData> implements IsSpongyChestData
{
	public SpongeIsSpongyChestData(boolean value)
	{
		super(value, SpongyChest.IS_SPONGY_CHEST, false);
	}

	public SpongeIsSpongyChestData()
	{
		this(false);
	}

	@Override
	public Value<Boolean> isSpongyChest()
	{
		return getValueGetter();
	}

	@Override
	public Optional<IsSpongyChestData> fill(DataHolder dataHolder, MergeFunction overlap)
	{
		IsSpongyChestData chestRewardData = Preconditions.checkNotNull(overlap).merge(copy(), from(dataHolder.toContainer()).orElse(null));
		return Optional.of(set(SpongyChest.IS_SPONGY_CHEST, chestRewardData.get(SpongyChest.IS_SPONGY_CHEST).get()));
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(SpongyChest.IS_SPONGY_CHEST.getQuery(), this.getValue());
	}

	@Override
	public Optional<IsSpongyChestData> from(DataContainer container)
	{
		boolean value = (Boolean) container.get(DataQuery.of("IsSpongyChest")).orElse(false);

		if (value)
			return Optional.of(new SpongeIsSpongyChestData(value));
		else
			return Optional.empty();
	}

	@Override
	public IsSpongyChestData copy()
	{
		return new SpongeIsSpongyChestData(getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public ImmutableIsSpongyChestData asImmutable()
	{
		return new ImmutableSpongeIsSpongyChestData(getValue());
	}

}
