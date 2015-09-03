package io.github.hsyyid.chestshop;

import io.github.hsyyid.chestshop.utils.ChestShop;
import io.github.hsyyid.chestshop.utils.LocationAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.BreakBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

import com.erigitic.config.AccountManager;
import com.erigitic.main.TotalEconomy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

@Plugin(id = "ChestShop", name = "ChestShop", version = "0.2", dependencies = "required-after:TotalEconomy")
public class Main
{
	public static Game game = null;
	public static ConfigurationNode config = null;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static TeleportHelper helper;
	public static ArrayList<ChestShop> chestShops = new ArrayList<ChestShop>();
	private Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).create();

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

	@Listener
	public void onServerStart(GameStartedServerEvent event)
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

		String json = null;
		
		try
		{
			json = readFile("ChestShops.json", StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			getLogger().error("Could not read JSON file!");
		}
		
		if(json != null)
		{
			chestShops = new ArrayList<ChestShop>(Arrays.asList(gson.fromJson(json, ChestShop[].class)));
		}
		else
		{
			getLogger().error("No JSON data read.");
		}
		
		getLogger().info("-----------------------------");
		getLogger().info("ChestShop was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("ChestShop loaded!");
	}
	
	static String readFile(String path, Charset encoding) 
		  throws IOException 
		{
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, encoding);
		}
	
	@Listener
	
	public void onServerStoppingEvent(GameStoppingServerEvent event)
	{
		String json = gson.toJson(chestShops);
		try
		{
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter("ChestShops.json");

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(json);

			bufferedWriter.flush();
			// Always close files.
			bufferedWriter.close();
		}
		catch (IOException ex)
		{
			getLogger().error("Could not save JSON file!");
		}
	}
	
	@Listener
	public void onSignChange(ChangeSignEvent.SourcePlayer event)
	{
		Player owner = event.getSourceEntity();
		Sign sign = event.getTargetTile();
		Location<World> signLocation = sign.getLocation();
		double y = signLocation.getY() - 1;
		Location<World> chestLocation = new Location<World>(signLocation.getExtent(), signLocation.getX(), y, signLocation.getZ());
		SignData signData = event.getText();
		
		String line0 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(0));
        String line1 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(1));
        String line2 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(2));
        String line3 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(3));

		if (line0.equals("[ChestShop]"))
		{
			if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
			{
			    signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_BLUE, "[ChestShop]")));
				if (owner != null)
				{
					int itemAmount = Integer.parseInt(line1);
					double price = Double.parseDouble(line2);
					String itemName = line3;
					ChestShop shop = new ChestShop(itemAmount, price, itemName, signLocation, owner.getUniqueId().toString());
					chestShops.add(shop);
					owner.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, "ChestShop successfully created!"));

				}
			}
			else
			{
				signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_RED, "[ChestShop]")));
			}
		}
	}

	@Listener
	public void onPlayerBreakBlock(BreakBlockEvent.SourcePlayer event)
	{
		for(BlockTransaction transaction : event.getTransactions())
		{
		if (transaction.getFinalReplacement().getState().getType() != null && transaction.getFinalReplacement().getState().getType() == BlockTypes.WALL_SIGN)
		{
			ChestShop thisShop = null;
			for (ChestShop chestShop : chestShops)
			{
				if (chestShop.getSignLocation().getX() == event.getSourceTransform().getLocation().getX() && chestShop.getSignLocation().getY() == event.getSourceTransform().getLocation().getY() && chestShop.getSignLocation().getZ() == event.getSourceTransform().getLocation().getZ())
				{
					thisShop = chestShop;
				}
			}

			if (thisShop != null)
			{
				Player owner = null;
				for(Player p : game.getServer().getOnlinePlayers())
				{
					if(p.getUniqueId().toString().equals(thisShop.getOwnerUUID()))
					{
						owner = p;
						break;
					}
				}
				if(owner != null)
				{
					owner.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, " ChestShop successfully deleted!"));
				}
				chestShops.remove(thisShop);
			}
		}
		}
	}

	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent.SourcePlayer event)
	{
		if (event.getTargetLocation() != null && event.getTargetBlock().getState().getType() == BlockTypes.WALL_SIGN)
		{
			ChestShop thisShop = null;
			for (ChestShop chestShop : chestShops)
			{
				if (chestShop.getSignLocation().getX() == event.getTargetLocation().getX() && chestShop.getSignLocation().getY() == event.getTargetLocation().getY() && chestShop.getSignLocation().getZ() == event.getTargetLocation().getZ())
				{
					thisShop = chestShop;
				}
			}

			if (thisShop != null)
			{
				if (thisShop.getOwnerUUID().equals(event.getSourceEntity().getUniqueId().toString()))
				{
					event.getSourceEntity().sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot purchase things from yourself!"));
				}
				else
				{
					int itemAmount = thisShop.getItemAmount();
					double price = thisShop.getPrice();
					String itemName = thisShop.getItemName();
					Location<World> chestLocation = new Location<World>(event.getTargetLocation().getExtent(), event.getTargetLocation().getX(), event.getTargetLocation().getY() - 1, event.getTargetLocation().getZ());

					if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
					{
						// TODO: Get chest and check if Item is in there - cannot be done until InventoryAPI is implemented..
						Player player = event.getSourceEntity();
						TotalEconomy totalEconomy = (TotalEconomy) game.getPluginManager().getPlugin("TotalEconomy").get().getInstance();
						AccountManager accountManager = totalEconomy.getAccountManager();
						BigDecimal amount = new BigDecimal(price);

						if (accountManager.getBalance(player.getUniqueId()).intValue() > amount.intValue())
						{
							accountManager.removeFromBalance(player.getUniqueId(), amount);
							player.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, "You have just bought " + itemAmount + " " + itemName + " for " + price + " dollars."));
							game.getCommandDispatcher().process(game.getServer().getConsole(), "give" + " " + player.getName() + " " + itemName + " " + itemAmount);
							accountManager.addToBalance(UUID.fromString(thisShop.getOwnerUUID()), amount, true);

							Player owner = null;
							for(Player p : game.getServer().getOnlinePlayers())
							{
								if(p.getUniqueId().toString().equals(thisShop.getOwnerUUID()))
								{
									owner = p;
									break;
								}
							}
							if (owner != null)
							{
								owner.sendMessage(Texts.of(TextColors.BLUE, "[ChestShop]: ", TextColors.GREEN, player.getName() + " has just bought " + itemAmount + " " + itemName + " from you!"));
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
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
