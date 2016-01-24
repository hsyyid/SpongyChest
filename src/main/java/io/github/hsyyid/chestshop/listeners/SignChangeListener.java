package io.github.hsyyid.chestshop.listeners;

import io.github.hsyyid.chestshop.SpongyChest;
import io.github.hsyyid.chestshop.utils.ChestShop;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SignChangeListener
{
	@Listener
	public void onSignChange(ChangeSignEvent event, @First Player owner)
	{
		Sign sign = event.getTargetTile();
		Location<World> signLocation = sign.getLocation();
		double y = signLocation.getY() - 1;
		Location<World> chestLocation = new Location<World>(signLocation.getExtent(), signLocation.getX(), y, signLocation.getZ());
		SignData signData = event.getText();

		String line0 = signData.getValue(Keys.SIGN_LINES).get().get(0).toPlain();
		String line1 = signData.getValue(Keys.SIGN_LINES).get().get(1).toPlain();
		String line2 = signData.getValue(Keys.SIGN_LINES).get().get(2).toPlain();
		String line3 = signData.getValue(Keys.SIGN_LINES).get().get(3).toPlain();

		if (line0.equals("[SpongyChest]") && owner.hasPermission("spongychest.shop.create"))
		{
			if (chestLocation.getBlock() != null && chestLocation.getBlock().getType().equals(BlockTypes.CHEST))
			{
				signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_BLUE, "[SpongyChest]")));
				int itemAmount = Integer.parseInt(line1);
				double price = Double.parseDouble(line2);
				String itemName = line3;
				ChestShop shop = new ChestShop(itemAmount, price, itemName, signLocation, owner.getUniqueId().toString());
				SpongyChest.chestShops.add(shop);
				owner.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, "ChestShop successfully created!"));
			}
			else
			{
				signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_RED, "[SpongyChest]")));
			}
		}
	}
}
