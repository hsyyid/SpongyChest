package io.github.hsyyid.spongychest.listeners;

import io.github.hsyyid.spongychest.data.isspongychest.IsSpongyChestData;
import io.github.hsyyid.spongychest.data.isspongychest.SpongeIsSpongyChestData;
import io.github.hsyyid.spongychest.data.uuidchest.UUIDChestData;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public class HitBlockListener
{
	@Listener
	public void onPlayerHitBlock(InteractBlockEvent.Primary event, @First Player player)
	{
		if (event.getTargetBlock().getState().getType() == BlockTypes.CHEST && event.getTargetBlock().getLocation().isPresent() && event.getTargetBlock().getLocation().get().getTileEntity().isPresent())
		{
			Chest chest = (Chest) event.getTargetBlock().getLocation().get().getTileEntity().get();

			if (chest.get(IsSpongyChestData.class).isPresent() && chest.get(IsSpongyChestData.class).get().isSpongyChest().get())
			{
				UUID uuid = chest.get(UUIDChestData.class).get().uuid().get();

				if (uuid.equals(player.getUniqueId()) && player.hasPermission("spongychest.shop.destroy"))
				{
					player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, "Successfully deleted shop!"));
					chest.offer(new SpongeIsSpongyChestData(false));
				}
				else
				{
					event.setCancelled(true);
				}
			}
		}
	}
}
