package io.github.hsyyid.spongychest.utils;

import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.math.BigDecimal;
import java.util.UUID;

public class ChestShopModifier
{
	private UUID uuid;
	private ItemStackSnapshot item;
	private BigDecimal price;

	public ChestShopModifier(UUID uuid, ItemStackSnapshot item, BigDecimal price)
	{
		this.uuid = uuid;
		this.item = item;
		this.price = price;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public ItemStackSnapshot getItem()
	{
		return item;
	}
	
	public BigDecimal getPrice()
	{
		return price;
	}
}
