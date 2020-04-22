package fr.plixe.creeperdust.assets.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.assets.items.ItemsAPI;
import fr.plixe.creeperdust.utils.ServerUtils;

/*
 * Manage drops when droppers entities die 
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class DroppersDeathEvent implements Listener {

	@EventHandler
	public void onDroppersDeath(EntityDeathEvent event) {

		if (event.getEntity().getLastDamageCause() != null) {

			ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
					.getConfigurationSection("drops-settings");
			Entity deadEntity = event.getEntity();
			Entity killerEntity = event.getEntity().getKiller();

			Location entityDeathLocation = event.getEntity().getLocation();
			StringBuilder testedEntitiesDebugMessageBuilder = new StringBuilder(
					"onDroppersDeath test the entity death at x:");
			testedEntitiesDebugMessageBuilder.append(entityDeathLocation.getBlockX());
			testedEntitiesDebugMessageBuilder.append(", y:");
			testedEntitiesDebugMessageBuilder.append(entityDeathLocation.getBlockY());
			testedEntitiesDebugMessageBuilder.append(", z:");
			testedEntitiesDebugMessageBuilder.append(entityDeathLocation.getBlockZ());
			ServerUtils.debugMessage(testedEntitiesDebugMessageBuilder.toString());

			if (isADropperEntity(deadEntity) && canDropCustomItem(killerEntity)) {

				int lootingLevel = getLootingLevel(killerEntity);
				int dropRate = getDropRate(deadEntity, lootingLevel);

				int random = new Random().nextInt(100);

				if (isAlwaysPermitted(killerEntity) || random <= dropRate) {
					ServerUtils.debugMessage("Items dropped.");
					event.getDrops().add(getCustomDroppedItem(lootingLevel));
				} else {
					ServerUtils.debugMessage("Items not dropped.");
				}

			} else {
				ServerUtils.debugMessage("Items not dropped.");
			}

		}

	}

	private boolean isAlwaysPermitted(Entity testedEntity) {

		if (MainCreeperDust.settings.getConfig().getBoolean("drops-settings.use-drop-permissions")) {

			if (testedEntity.hasPermission("creeperdust.drop.always")) {
				ServerUtils.debugMessage("Killer have the always permissions.");
				return true;
			} else {
				ServerUtils.debugMessage("Killer don't have the always permissions.");
				return false;
			}

		} else {
			return false;
		}

	}

	private boolean enableChargedCreeperOption(Entity testedEntity) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");
		if (dropsSettings.getBoolean("charged-creeper.enable") && testedEntity.getType() == EntityType.CREEPER
				&& ((Creeper) testedEntity).isPowered())
			return true;
		else
			return false;

	}

	private boolean isADropperEntity(Entity testedEntity) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");

		StringBuilder dropperDebugMessageBuilder = new StringBuilder("The tested EntityType a ");
		dropperDebugMessageBuilder.append(testedEntity.getType().toString());
		ServerUtils.debugMessage(dropperDebugMessageBuilder.toString());

		List<String> droppersListSettings = dropsSettings.getStringList("droppers");
		List<EntityType> droppersType = new ArrayList<EntityType>();

		for (int i = 0; i < droppersListSettings.size(); i++)
			droppersType.add(EntityType.valueOf(droppersListSettings.get(i)));

		if (droppersType.contains(testedEntity.getType())) {
			ServerUtils.debugMessage("Dead entity is in the droppers list !");
			return true;
		} else {
			ServerUtils.debugMessage("Dead entity is not in the droppers list.");
			return false;
		}
	}

	private boolean canDropCustomItem(Entity killer) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");
		boolean canDrop = true;

		ServerUtils.debugMessage("Check if Custom Item can be dropped...");

		if (dropsSettings.getBoolean("only-dropped-by-player")) {

			ServerUtils.debugMessage("Option only-dropped-by-player enabled.");

			if (killer instanceof Player) {
				ServerUtils.debugMessage("Killer is an player.");

				if (dropsSettings.getBoolean("drop-permissions")) {
					ServerUtils.debugMessage("Option drop-permissions enabled.");

					if (killer.hasPermission("creeperdust.drop") || killer.hasPermission("creeperdust.drop.always")) {
						ServerUtils.debugMessage("Player has permissions to drops !");
						return true;
					} else {
						ServerUtils.debugMessage("Player don't have the permissions to drops.");
						return false;
					}

				} else {
					ServerUtils.debugMessage("Option drop-permissions not enabled.");
					canDrop = true;
				}

			} else {
				ServerUtils.debugMessage("Killer not a player.");
				canDrop = false;
			}
		} else {
			ServerUtils.debugMessage("Option only-dropped-by-player not enabled.");
			canDrop = true;
		}

		if (canDrop)
			ServerUtils.debugMessage("Custom Item can be dropped !");
		else
			ServerUtils.debugMessage("Custom Item can't be dropped.");

		return canDrop;
	}

	private int getDropRate(Entity deadEntity, int lootingLevel) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");

		int dropRate = dropsSettings.getInt("drop-rate");
		int lootingIncreaseNumber = lootingLevel * dropsSettings.getInt("looting-increase-by-level.drop-rate");

		if (enableChargedCreeperOption(deadEntity)) {
			int chargedCreeperDropRate = dropsSettings.getInt("charged-creeper.drop-rate");
			int chargedCreeperLootingIncreaseNumber = lootingLevel
					* dropsSettings.getInt("looting-increase-by-level.drop-rate");
			ServerUtils.debugMessage("The Creeper is charged and the option charged-creeper.enable is enabled !");
			dropRate = chargedCreeperDropRate + chargedCreeperLootingIncreaseNumber;
		} else {
			dropRate += lootingIncreaseNumber;
		}

		StringBuilder dropsDebugMessageBuilder = new StringBuilder("The drops rate is to ");
		dropsDebugMessageBuilder.append(dropRate);
		dropsDebugMessageBuilder.append("%");
		ServerUtils.debugMessage(dropsDebugMessageBuilder.toString());

		return dropRate;
	}

	private ItemStack getCustomDroppedItem(int lootingLevel) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");

		ItemStack customDroppedItem;

		if (dropsSettings.getBoolean("directly-drop-creeper-egg")) {
			ServerUtils.debugMessage("Option directly-drop-creeper-egg enabled.");
			customDroppedItem = ItemsAPI.creeperEgg();
		} else {
			ServerUtils.debugMessage("Option directly-drop-creeper-egg not enabled.");
			customDroppedItem = ItemsAPI.creeperDust();
		}
		customDroppedItem.setAmount(getDropQuantity(lootingLevel));

		return customDroppedItem;
	}

	private int getDropQuantity(int lootingLevel) {
		ConfigurationSection dropsSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("drops-settings");

		int lootingIncreaseNumber = lootingLevel * dropsSettings.getInt("looting-increase-by-level.drop-quantity");
		int minQuantity = dropsSettings.getInt("drop-quantity.min") + lootingIncreaseNumber;
		int maxQuantity = dropsSettings.getInt("drop-quantity.max") + lootingIncreaseNumber;
		int finalQuantityToDrop = minQuantity;

		if (minQuantity == maxQuantity) {
			maxQuantity = finalQuantityToDrop;
		} else {
			finalQuantityToDrop += new Random().nextInt(maxQuantity - minQuantity);
		}

		return finalQuantityToDrop;

	}

	private int getLootingLevel(Entity killer) {

		int lootingLevel = 0;

		if (killer instanceof Player) {
			Player player = ((Player) killer);

			if (ServerUtils.useOldSpigotAPI()) {
				if (player.getItemInHand().containsEnchantment(Enchantment.getByName("LOOT_BONUS_MOBS")))
					lootingLevel = player.getItemInHand().getEnchantmentLevel(Enchantment.getByName("LOOT_BONUS_MOBS"));
			} else {
				if (player.getInventory().getItemInMainHand()
						.containsEnchantment(Enchantment.getByName("LOOT_BONUS_MOBS")))
					lootingLevel = player.getInventory().getItemInMainHand()
							.getEnchantmentLevel(Enchantment.getByName("LOOT_BONUS_MOBS"));
			}

		}

		StringBuilder lootingLevelDebugMessage = new StringBuilder("Looting level: ");
		lootingLevelDebugMessage.append(lootingLevel);
		ServerUtils.debugMessage(lootingLevelDebugMessage.toString());

		return lootingLevel;

	}

}
