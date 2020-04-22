package fr.plixe.creeperdust.assets;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.assets.items.CraftAPI;
import fr.plixe.creeperdust.assets.items.ItemsAPI;
import fr.plixe.creeperdust.utils.ColorUtils;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("creeperdust") || commandLabel.equalsIgnoreCase("cdust")) {

			if (args.length == 0) {

				if (ColorUtils.hasPermissions(commandSender, "creeperdust.help"))
					showHelpMessages(commandSender);

			} else if (args.length == 1) {

				if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {

					if (ColorUtils.hasPermissions(commandSender, "creeperdust.help"))
						showHelpMessages(commandSender);

				} else if (args[0].equalsIgnoreCase("reload")) {

					if (ColorUtils.hasPermissions(commandSender, "creeperdust.reload")) {
						MainCreeperDust.messages.reloadFile(commandSender);
						MainCreeperDust.settings.reloadFile(commandSender);
						CraftAPI.initializeRecipes();
					}

				} else if (args[0].equalsIgnoreCase("items")) {

					if (ColorUtils.hasPermissions(commandSender, "creeperdust.items")
							&& commandSender instanceof Player)
						openCustomItemsInventory((Player) commandSender);

				} else {
					ColorUtils.sendColoredUnknowCommandMessage(commandSender);
				}

			} else {
				ColorUtils.sendColoredUnknowCommandMessage(commandSender);
			}

		}

		return true;
	}

	private void showHelpMessages(CommandSender commandSender) {

		List<String> helpMessages = MainCreeperDust.messages.getConfig().getStringList("commands-messages.help");

		for (int i = 0; i < helpMessages.size(); i++)
			ColorUtils.sendColoredMessage(commandSender, helpMessages.get(i));

	}

	private void openCustomItemsInventory(Player targetPlayer) {
		Inventory inventory = Bukkit.getServer().createInventory(null, 9 * 3, "CreeperDust items");
		inventory.setItem(12, ItemsAPI.creeperDust());
		inventory.setItem(14, ItemsAPI.creeperEgg());
		targetPlayer.openInventory(inventory);
	}

}
