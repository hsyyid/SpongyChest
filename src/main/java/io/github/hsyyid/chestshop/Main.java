package io.github.hsyyid.chestshop;

import io.github.hsyyid.chestshop.utils.ChestShop;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerInteractBlockEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;

import com.erigitic.config.AccountManager;
import com.erigitic.main.TotalEconomy;
import com.google.inject.Inject;

@Plugin(id = "ChestShop", name = "ChestShop", version = "0.1", dependencies = "required-after:TotalEconomy")
public class Main
{
	public static Game game = null;
	public static ConfigurationNode config = null;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static TeleportHelper helper;
	public static ArrayList<ChestShop> chestShops = new ArrayList<ChestShop>();

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File dConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> confManager;

	@Subscribe
	public void onServerStart(ServerStartedEvent event)
	{
		getLogger().info("ChestShop loading...");
		game = event.getGame();
		helper = game.getTeleportHelper();
		// Config File
		try
		{
			if (!dConfig.exists())
			{
				dConfig.createNewFile();
				config = confManager.load();
				confManager.save(config);
			}
			configurationManager = confManager;
			config = confManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		getLogger().info("-----------------------------");
		getLogger().info("ChestShop was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("ChestShop loaded!");
	}

	@Subscribe
	public void onSignChange(SignChangeEvent event)
	{
		Sign sign = event.getTile();
		Location signLocation = sign.getBlock();
		double y = signLocation.getY() - 1;
		Location chestLocation = new Location(signLocation.getExtent(), signLocation.getX(), y, signLocation.getZ());
		SignData signData = event.getNewData();
		String line0 = Texts.toPlain(signData.getLine(0));
		String line1 = Texts.toPlain(signData.getLine(1));
		String line2 = Texts.toPlain(signData.getLine(2));
		String line3 = Texts.toPlain(signData.getLine(3));

		if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
		{
			Player owner = null;
			
			for (Player player : game.getServer().getOnlinePlayers())
			{
				if (player.getName().equals(line0))
				{
					owner = player;
					break;
				}
			}

			if (owner != null)
			{
				int itemAmount = Integer.parseInt(line1);
				double price = Double.parseDouble(line2);
				String itemName = line3;
				ChestShop shop = new ChestShop(itemAmount, price, itemName, signLocation, owner);
				chestShops.add(shop);
				owner.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, "ChestShop successfully created!"));

			}
		}

		event.setNewData(signData);
	}

	@Subscribe
	public void onPlayerBreakBlock(PlayerBreakBlockEvent event)
	{
		if (event.getBlock().getBlock() != null && event.getBlock().getBlock().getType() == BlockTypes.WALL_SIGN)
		{
			ChestShop thisShop = null;
			for (ChestShop chestShop : chestShops)
			{
				if (chestShop.getSignLocation().getX() == event.getBlock().getX() && chestShop.getSignLocation().getY() == event.getBlock().getY() && chestShop.getSignLocation().getZ() == event.getBlock().getZ())
				{
					thisShop = chestShop;
				}
			}

			if (thisShop != null)
			{
				thisShop.getOwner().sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, " ChestShop successfully deleted!"));
				chestShops.remove(thisShop);
			}
		}
	}
	
	@Subscribe
	public void onPlayerInteractBlock(PlayerInteractBlockEvent event)
	{
		if (event.getBlock().getBlock() != null && event.getBlock().getBlock().getType() == BlockTypes.WALL_SIGN)
		{
			ChestShop thisShop = null;
			for (ChestShop chestShop : chestShops)
			{
				if (chestShop.getSignLocation().getX() == event.getBlock().getX() && chestShop.getSignLocation().getY() == event.getBlock().getY() && chestShop.getSignLocation().getZ() == event.getBlock().getZ())
				{
					thisShop = chestShop;
				}
			}

			if (thisShop != null)
			{
				int itemAmount = thisShop.getItemAmount();
				double price = thisShop.getPrice();
				String itemName = thisShop.getItemName();
				Location chestLocation = new Location(event.getBlock().getExtent(), event.getBlock().getX(), event.getBlock().getY() - 1, event.getBlock().getZ());

				if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
				{
					// TODO: Get chest and check if Item is in there - cannot be done until InventoryAPI is implemented..
					Player player = event.getEntity();
					TotalEconomy totalEconomy = (TotalEconomy) game.getPluginManager().getPlugin("TotalEconomy").get().getInstance();
					AccountManager accountManager = totalEconomy.getAccountManager();
					BigDecimal amount = new BigDecimal(price);

					if(accountManager.getBalance(player).intValue() > amount.intValue())
					{
						accountManager.removeFromBalance(player, amount);
						player.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, "You have just bought " + itemAmount + " " + itemName + " from " + thisShop.getOwner().getName() + " for " + price + "dollars."));
						game.getCommandDispatcher().process(game.getServer().getConsole(), "give" + " " + player.getName() + " " + itemName + " " + itemAmount);
						accountManager.addToBalance(thisShop.getOwner(), amount, true);

						if (game.getServer().getOnlinePlayers().contains(thisShop.getOwner()))
						{
							thisShop.getOwner().sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, player.getName() + " has just bought " + itemAmount + " " + itemName + " from you!"));
						}
					}
					else
					{
						player.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You don't have enough money to do that!"));
					}
				}
			}
		}
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
