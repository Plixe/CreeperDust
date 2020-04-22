package fr.plixe.creeperdust.assets.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.assets.items.ItemsAPI;
import fr.plixe.creeperdust.utils.ServerUtils;

/*
 * Check if a player use custom items with a vanilla recipe
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class CustomItemsCraftEvent implements Listener {

	@EventHandler
	public void onCustomItemsUseInCraft(PrepareItemCraftEvent event) {

		if (MainCreeperDust.settings.getConfig().getBoolean("extra-settings.disable-vanilla-crafts")
				& containCustomItem(event.getInventory())) {

			if (event.getRecipe() != null && !event.getRecipe().getResult().isSimilar(ItemsAPI.creeperEgg())) {
				ServerUtils.debugMessage("Trying set the PrepareItemCraftEvent inventory result to null ...");
				event.getInventory().setResult(null);
			}

		}

	}

	private boolean containCustomItem(Inventory testedInventory) {
		StringBuilder testedItemDebugMessage = new StringBuilder("The craft grid ");
		if (testedInventory.contains(ItemsAPI.creeperDust()) | testedInventory.contains(ItemsAPI.creeperEgg())) {
			testedItemDebugMessage.append("contain a Custom Item.");
			ServerUtils.debugMessage(testedItemDebugMessage.toString());
			return true;
		} else {
			testedItemDebugMessage.append("doesn't contain a Custom Item.");
			ServerUtils.debugMessage(testedItemDebugMessage.toString());
			return false;
		}

	}

}
