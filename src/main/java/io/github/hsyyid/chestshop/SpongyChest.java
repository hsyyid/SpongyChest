package io.github.hsyyid.chestshop;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.github.hsyyid.chestshop.commands.SetItemExecutor;
import io.github.hsyyid.chestshop.commands.SpongyChestExecutor;
import io.github.hsyyid.chestshop.listeners.BreakBlockListener;
import io.github.hsyyid.chestshop.listeners.InteractBlockListener;
import io.github.hsyyid.chestshop.listeners.SignChangeListener;
import io.github.hsyyid.chestshop.utils.ChestShop;
import io.github.hsyyid.chestshop.utils.ChestShopModifier;
import io.github.hsyyid.chestshop.utils.ConfigManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Plugin(id = "SpongyChest", name = "SpongyChest", version = "0.3")
public class SpongyChest
{
	public static Game game;
	public static EconomyService economyService;
	public static ConfigurationNode config;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static ArrayList<ChestShop> chestShops = Lists.newArrayList();
	public static ArrayList<ChestShopModifier> chestShopModifiers = Lists.newArrayList();

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
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("SpongyChest loading...");
		game = Sponge.getGame();

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

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("setitem"), CommandSpec.builder()
			.description(Text.of("Sets Item of SpongyChest"))
			.permission("spongychest.setitem.command")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.optional(GenericArguments.integer(Text.of("meta")))), GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("item id")))))
			.executor(new SetItemExecutor())
			.build());

		CommandSpec spongyChestCommandSpec = CommandSpec.builder()
			.description(Text.of("SpongyChest Command"))
			.permission("spongychest.command")
			.executor(new SpongyChestExecutor())
			.children(subcommands)
			.build();

		game.getCommandManager().register(this, spongyChestCommandSpec, "sc", "spongychest");

		game.getEventManager().registerListeners(this, new InteractBlockListener());
		game.getEventManager().registerListeners(this, new SignChangeListener());
		game.getEventManager().registerListeners(this, new BreakBlockListener());
			
		getLogger().info("-----------------------------");
		getLogger().info("SpongyChest was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("SpongyChest loaded!");
	}

	@Listener
	public void onServerStartedEvent(GameStartedServerEvent event)
	{
		ConfigManager.readChestShops();
	}

	@Listener
	public void onServerStoppingEvent(GameStoppingServerEvent event)
	{
		ConfigManager.writeChestShops();
	}

	@Listener
	public void onPostInit(GamePostInitializationEvent event)
	{
		Optional<EconomyService> optionalEconomyService = Sponge.getServiceManager().provide(EconomyService.class);

		if (optionalEconomyService.isPresent())
		{
			economyService = optionalEconomyService.get();
		}
		else
		{
			getLogger().error("No economy plugin was found! This plugin will not work correctly!");
		}
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
