package io.github.hsyyid.chestshop.utils;

import java.util.UUID;

public class ChestShopModifier
{
	private UUID uuid;
	private String itemName;
	private int meta;
	
	public ChestShopModifier(UUID uuid, String itemName, int meta)
	{
		this.uuid = uuid;
		this.itemName = itemName;
		this.meta = meta;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public int getMeta()
	{
		return meta;
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}
	
	public void setMeta(int meta)
	{
		this.meta = meta;
	}
	
	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}
}
