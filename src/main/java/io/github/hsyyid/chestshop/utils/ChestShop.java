package io.github.hsyyid.chestshop.utils;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class ChestShop
{
	public int itemAmount;
	public double price;
	public String itemName;
	public Location<World> signLocation;
	public String ownerUUID;
	
	public ChestShop(int itemAmount, double price, String itemName, Location<World> signLocation, String ownerUUID)
	{
		this.itemAmount = itemAmount;
		this.price = price;
		this.itemName = itemName;
		this.signLocation = signLocation;
		this.ownerUUID = ownerUUID;
	}
	
	public void setItemAmount(int itemAmount)
	{
		this.itemAmount = itemAmount;
	}
	
	public void setSignLocation(Location<World> signLocation)
	{
		this.signLocation = signLocation;
	}
	
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}
	
	public int getItemAmount()
	{
		return itemAmount;
	}
	
	public Location<World> getSignLocation()
	{
		return signLocation;
	}
	
	public String getOwnerUUID()
	{
		return ownerUUID;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public String getItemName()
	{
		return itemName;
	}
}
