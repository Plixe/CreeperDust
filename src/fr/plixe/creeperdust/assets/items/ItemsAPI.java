package fr.plixe.creeperdust.assets.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.utils.ColorUtils;
import fr.plixe.creeperdust.utils.ServerUtils;

/*
 * API to use Items between differents versions of Minecraft
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class ItemsAPI {

	public static ItemStack creeperDust() {
		ConfigurationSection itemsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("items-settings");

		ItemStack creeperDustItem;
		boolean useCustomItem = itemsSettings.contains("creeper-dust.material");

		if (ServerUtils.useOldSpigotAPI())
			creeperDustItem = new ItemStack(Material.getMaterial("SULPHUR"));
		else
			creeperDustItem = new ItemStack(Material.getMaterial("GUNPOWDER"));

		if (useCustomItem) {
			try {
				creeperDustItem = new ItemStack(Material.getMaterial(itemsSettings.getString("creeper-dust.material")));
			} catch (IllegalArgumentException exception) {
				StringBuilder exceptionMessageBuilder = new StringBuilder("creeper-dust : This items don't exist ");
				exceptionMessageBuilder.append(itemsSettings.getString("creeper-dust.material"));
				ServerUtils.errorMessage(exceptionMessageBuilder.toString());
			}
		}

		ItemMeta creeperDustMeta = creeperDustItem.getItemMeta();

		if (itemsSettings.contains("creeper-dust.lore")) {
			List<String> settingsLore = itemsSettings.getStringList("creeper-dust.lore");
			List<String> coloredLore = new ArrayList<String>();
			for (String settingsLoreLine : settingsLore)
				coloredLore.add(ColorUtils.coloredString(settingsLoreLine));
			creeperDustMeta.setLore(coloredLore);
		}

		if (itemsSettings.contains("creeper-dust.display-name"))
			creeperDustMeta
					.setDisplayName(ColorUtils.coloredString(itemsSettings.getString("creeper-dust.display-name")));

		creeperDustItem.setItemMeta(creeperDustMeta);

		return creeperDustItem;
	}

	public static ItemStack creeperEgg() {
		ConfigurationSection itemsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("items-settings");

		ItemStack creeperEggItem;
		boolean useCustomItem = itemsSettings.contains("creeper-egg.material");

		if (ServerUtils.useOldSpigotAPI())
			creeperEggItem = new ItemStack(Material.getMaterial("MONSTER_EGG"), 1, (byte) 50);
		else
			creeperEggItem = new ItemStack(Material.getMaterial("CREEPER_SPAWN_EGG"));

		if (useCustomItem) {
			try {
				creeperEggItem = new ItemStack(Material.getMaterial(itemsSettings.getString("creeper-egg.material")));
			} catch (IllegalArgumentException exception) {
				StringBuilder exceptionMessageBuilder = new StringBuilder("creeper-egg : This items don't exist ");
				exceptionMessageBuilder.append(itemsSettings.getString("creeper-egg.material"));
				ServerUtils.errorMessage(exceptionMessageBuilder.toString());
			}
		}

		ItemMeta creeperEggMeta = creeperEggItem.getItemMeta();

		if (itemsSettings.contains("creeper-egg.lore")) {
			List<String> settingsLore = itemsSettings.getStringList("creeper-egg.lore");
			List<String> coloredLore = new ArrayList<String>();
			for (String settingsLoreLine : settingsLore)
				coloredLore.add(ColorUtils.coloredString(settingsLoreLine));
			creeperEggMeta.setLore(coloredLore);
		}

		if (itemsSettings.contains("creeper-egg.display-name"))
			creeperEggMeta
					.setDisplayName(ColorUtils.coloredString(itemsSettings.getString("creeper-egg.display-name")));

		creeperEggItem.setItemMeta(creeperEggMeta);
		return creeperEggItem;
	}

}
