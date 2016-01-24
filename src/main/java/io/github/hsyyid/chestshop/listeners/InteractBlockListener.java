package io.github.hsyyid.chestshop.listeners;

import io.github.hsyyid.chestshop.SpongyChest;
import io.github.hsyyid.chestshop.utils.ChestShop;
import io.github.hsyyid.chestshop.utils.ChestShopModifier;
import io.github.hsyyid.chestshop.utils.ItemUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class InteractBlockListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent.Secondary event, @First Player player)
	{
		if (event.getTargetBlock().getLocation().get() != null && event.getTargetBlock().getState().getType() == BlockTypes.WALL_SIGN)
		{
			ChestShop thisShop = null;

			for (ChestShop chestShop : SpongyChest.chestShops)
			{
				if (chestShop.getSignLocation().getX() == event.getTargetBlock().getLocation().get().getX() && chestShop.getSignLocation().getY() == event.getTargetBlock().getLocation().get().getY() && chestShop.getSignLocation().getZ() == event.getTargetBlock().getLocation().get().getZ())
				{
					thisShop = chestShop;
				}
			}

			if (thisShop != null)
			{
				int itemAmount = thisShop.getItemAmount();
				double price = thisShop.getPrice();
				String itemName = thisShop.getItemName();
				ItemType itemType = Sponge.getRegistry().getType(ItemType.class, itemName).orElse(null);
				Location<World> chestLocation = new Location<World>(event.getTargetBlock().getLocation().get().getExtent(), event.getTargetBlock().getLocation().get().getX(), event.getTargetBlock().getLocation().get().getY() - 1, event.getTargetBlock().getLocation().get().getZ());

				if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
				{
					Chest chest = (Chest) chestLocation.getTileEntity().get();
					ChestShopModifier foundChestShopModifier = null;

					for (ChestShopModifier chestShopModifier : SpongyChest.chestShopModifiers)
					{
						if (chestShopModifier.getUuid().equals(player.getUniqueId()))
						{
							foundChestShopModifier = chestShopModifier;
							break;
						}
					}

					if (foundChestShopModifier != null && player.getUniqueId().toString().equals(thisShop.getOwnerUUID()))
					{
						thisShop.setItemName(foundChestShopModifier.getItemName());
						thisShop.setMeta(foundChestShopModifier.getMeta());
						SpongyChest.chestShopModifiers.remove(foundChestShopModifier);
						player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, "Set item id on ChestShop."));
					}

					if (itemType != null && chest.getInventory().contains(itemType) && ItemUtils.canRemoveItemsFromInventory(chest.getInventory(), itemType, thisShop.getMeta(), itemAmount))
					{
						if (thisShop.getMeta() != -1)
							ItemUtils.removeItemsFromInventory(chest.getInventory(), itemType, thisShop.getMeta(), itemAmount);

						UniqueAccount uniqueAccount = null;
						UniqueAccount ownerAccount = null;
						BigDecimal amount = new BigDecimal(price);

						if (SpongyChest.economyService.getAccount(player.getUniqueId()).isPresent())
						{
							uniqueAccount = SpongyChest.economyService.getAccount(player.getUniqueId()).get();
						}
						else
						{
							uniqueAccount = SpongyChest.economyService.createAccount(player.getUniqueId()).orElse(null);
						}

						if (SpongyChest.economyService.getAccount(UUID.fromString(thisShop.getOwnerUUID())).isPresent())
						{
							ownerAccount = SpongyChest.economyService.getAccount(UUID.fromString(thisShop.getOwnerUUID())).get();
						}
						else
						{
							ownerAccount = SpongyChest.economyService.createAccount(UUID.fromString(thisShop.getOwnerUUID())).orElse(null);
						}

						if (uniqueAccount.getBalance(SpongyChest.economyService.getDefaultCurrency()).compareTo(amount) >= 0)
						{
							uniqueAccount.withdraw(SpongyChest.economyService.getDefaultCurrency(), amount, Cause.of(Sponge.getPluginManager().getPlugin("SpongyChest").get()));
							player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, "You have just bought " + itemAmount + " " + itemName + " for " + price + " dollars."));
							ItemStack stack = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(Sponge.getRegistry().getType(ItemType.class, itemName).orElse(null)).quantity(itemAmount).build();
							player.getInventory().offer(stack);
							ownerAccount.deposit(SpongyChest.economyService.getDefaultCurrency(), amount, Cause.of(Sponge.getPluginManager().getPlugin("SpongyChest").get()));

							Optional<Player> owner = Sponge.getServer().getPlayer(UUID.fromString(thisShop.getOwnerUUID()));

							if (owner.isPresent())
							{
								owner.get().sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, player.getName() + " has just bought " + itemAmount + " " + itemName + " from you!"));
							}
						}
						else
						{
							player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You don't have enough money to do that!"));
						}
					}
					else
					{
						player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This shop appears to be out of stock!"));
					}
				}
			}
		}
	}
}
