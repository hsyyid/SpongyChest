package io.github.hsyyid.chestshop.listeners;

import io.github.hsyyid.chestshop.SpongyChest;
import io.github.hsyyid.chestshop.utils.ChestShop;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event, @First Player player)
	{
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			if (transaction.getFinal().getState().getType() != null && transaction.getFinal().getState().getType() == BlockTypes.WALL_SIGN)
			{
				ChestShop thisShop = null;

				for (ChestShop chestShop : SpongyChest.chestShops)
				{
					if (chestShop.getSignLocation().getX() == transaction.getFinal().getLocation().get().getX() && chestShop.getSignLocation().getY() == transaction.getFinal().getLocation().get().getY() && chestShop.getSignLocation().getZ() == transaction.getFinal().getLocation().get().getZ())
					{
						thisShop = chestShop;
					}
				}

				if (thisShop != null && (thisShop.getOwnerUUID().equals(player.getUniqueId().toString()) || player.hasPermission("spongychest.shop.destroy")))
				{
					player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, " ChestShop successfully deleted!"));
					SpongyChest.chestShops.remove(thisShop);
				}
			}
		}
	}
}
