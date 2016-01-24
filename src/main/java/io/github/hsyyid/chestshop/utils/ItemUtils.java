package io.github.hsyyid.chestshop.utils;

import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

import java.util.Optional;

public class ItemUtils
{
	public static boolean canRemoveItemsFromInventory(TileEntityInventory<TileEntityCarrier> inventory, ItemType itemType, int quantity)
	{
		int removedItems = 0;

		for (Inventory slot : inventory.slots())
		{
			Optional<ItemStack> item = slot.poll();

			if (item.isPresent())
			{
				ItemStack stack = item.get();

				if (stack.getItem() == itemType && removedItems != quantity)
				{
					if (stack.getQuantity() + removedItems <= quantity)
					{
						removedItems += stack.getQuantity();
					}
					else if (stack.getQuantity() + removedItems > quantity)
					{
						removedItems += quantity - stack.getQuantity();
					}
				}
				else if (removedItems == quantity)
				{
					return true;
				}
			}
		}

		if (removedItems == quantity)
			return true;
		else
			return false;
	}

	public static void removeItemsFromInventory(TileEntityInventory<TileEntityCarrier> inventory, ItemType itemType, int quantity)
	{
		int removedItems = 0;

		for (Inventory slot : inventory.slots())
		{
			Optional<ItemStack> item = slot.poll();

			if (item.isPresent())
			{
				ItemStack stack = item.get();

				if (stack.getItem() == itemType && removedItems != quantity)
				{
					if (stack.getQuantity() + removedItems <= quantity)
					{
						removedItems += stack.getQuantity();
						stack.setQuantity(0);
					}
					else if (stack.getQuantity() + removedItems > quantity)
					{
						removedItems += quantity - stack.getQuantity();
						stack.setQuantity(quantity - stack.getQuantity());
					}
				}
				else if (removedItems == quantity)
				{
					break;
				}
			}
		}
	}
}
