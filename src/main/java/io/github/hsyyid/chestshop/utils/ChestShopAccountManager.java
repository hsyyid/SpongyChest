package io.github.hsyyid.chestshop.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import com.erigitic.config.AccountManager;
import com.erigitic.main.TotalEconomy;

public class ChestShopAccountManager extends AccountManager
{
	private File accountsFile;
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	private ConfigurationNode accountConfig;

	public ChestShopAccountManager(TotalEconomy totalEconomy)
	{
		super(totalEconomy);
		accountsFile = new File(totalEconomy.getConfigDir(), "accounts.conf");
		configManager = HoconConfigurationLoader.builder().setFile(accountsFile).build();

		try
		{
			accountConfig = configManager.load();
		}
		catch (IOException e)
		{
			;
		}
	}

	public boolean hasAccount(String UUID)
	{
		if (accountConfig.getNode(UUID) != null)
			return true;
		else
			return false;
	}

	public void addToBalance(String UUID, BigDecimal amount, boolean notify)
	{
		BigDecimal newBalance = new BigDecimal(getStringBalance(UUID)).add(new BigDecimal(amount.toString()));

		if (hasAccount(UUID))
		{
			try
			{
				accountConfig.getNode(UUID, "balance").setValue(newBalance.setScale(2, BigDecimal.ROUND_DOWN).toString());
				configManager.save(accountConfig);
			}
			catch (IOException e)
			{
				;
			}
		}
	}

	public String getStringBalance(String UUID)
	{
		BigDecimal balance = new BigDecimal(0);

		if (hasAccount(UUID))
		{
			balance = new BigDecimal(accountConfig.getNode(UUID, "balance").getString());
		}

		return balance.setScale(2, BigDecimal.ROUND_DOWN).toString();
	}

}
