package io.github.hsyyid.chestshop.commands;

import io.github.hsyyid.chestshop.SpongyChest;
import io.github.hsyyid.chestshop.utils.ChestShopModifier;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetItemExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String itemId = ctx.<String> getOne("item id").get();
		int meta = ctx.<Integer> getOne("meta").orElse(-1);

		if (src instanceof Player)
		{
			Player player = (Player) src;
			ChestShopModifier chestShopModifier = new ChestShopModifier(player.getUniqueId(), itemId, meta);
			SpongyChest.chestShopModifiers.add(chestShopModifier);
			player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.GREEN, "Right click a SpongyChest sign!"));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You must be an in-game player to execute this command."));
		}

		return CommandResult.success();
	}
}
