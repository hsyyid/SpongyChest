package io.github.hsyyid.spongychest.data.itemchest;

import io.github.hsyyid.spongychest.SpongyChest;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class ItemChestDataBuilder implements DataManipulatorBuilder<ItemChestData, ImmutableItemChestData>
{
	@Override
	public ItemChestData create()
	{
		return new SpongeItemChestData();
	}

	@Override
	public Optional<ItemChestData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<ItemChestData> build(DataView container)
	{
		if (!container.contains(SpongyChest.ITEM_CHEST.getQuery()))
		{
			return Optional.empty();
		}

		ItemChestData data = new SpongeItemChestData(ItemStack.builder().fromContainer((MemoryDataView) container.get(SpongyChest.ITEM_CHEST.getQuery()).get()).build().createSnapshot());
		return Optional.of(data);
	}
}
