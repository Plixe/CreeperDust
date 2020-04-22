package fr.plixe.creeperdust.assets.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.utils.ServerUtils;

/*
 * API to use Recipes between differents versions of Minecraft
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class CraftAPI {

//	private static NamespacedKey creeperEggNameKey = new NamespacedKey(MainCreeperDust.creeperDustPluginInstance,
//			"cdust_creeper_egg");

	public static ShapedRecipe creeperEggRecipe() {
		ConfigurationSection craftSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("craft-settings");
		ShapedRecipe creeperEggRecipe;

		if (useDefaultPluginRecipe()) {
			creeperEggRecipe = defaultPluginRecipe();
		} else {
			creeperEggRecipe = customSettingsRecipe();
		}

		return creeperEggRecipe;
	}

	private static ShapedRecipe defaultPluginRecipe() {
		StringBuilder recipeUsedDebugMessage = new StringBuilder("Loading ");
		recipeUsedDebugMessage.append("default creeper egg recipe...");
		ServerUtils.debugMessage(recipeUsedDebugMessage.toString());

		ShapedRecipe defaultRecipe = new ShapedRecipe(ItemsAPI.creeperEgg());
		defaultRecipe.shape("DDD", "DED", "DDD");
		defaultRecipe.setIngredient('D', ItemsAPI.creeperDust().getData());
		defaultRecipe.setIngredient('E', Material.EGG);
		return defaultRecipe;
	}

	private static ShapedRecipe customSettingsRecipe() {
		ConfigurationSection craftSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("craft-settings");
		List<String> settingsCraftLines = craftSettings.getStringList("craft-lines");
		ShapedRecipe customRecipe = new ShapedRecipe(ItemsAPI.creeperEgg());

		StringBuilder recipeUsedDebugMessage = new StringBuilder("Loading ");
		recipeUsedDebugMessage.append("custom creeper egg recipe...");
		ServerUtils.debugMessage(recipeUsedDebugMessage.toString());

		if (settingsCraftLines.size() == 0) {
			customRecipe.shape(settingsCraftLines.get(0));
		} else if (settingsCraftLines.size() == 1) {
			customRecipe.shape(settingsCraftLines.get(0), settingsCraftLines.get(1));
		} else {
			customRecipe.shape(settingsCraftLines.get(0), settingsCraftLines.get(1), settingsCraftLines.get(2));
		}

		for (String ingredient : craftSettings.getConfigurationSection("craft-materials").getKeys(false)) {

			StringBuilder ingredientPathBuilder = new StringBuilder("craft-materials.");
			ingredientPathBuilder.append(ingredient);

			try {

				ItemStack ingredientItem;

				if (craftSettings.getString(ingredientPathBuilder.toString()).equalsIgnoreCase("creeper_dust"))
					ingredientItem = ItemsAPI.creeperDust();
				else
					ingredientItem = new ItemStack(
							Material.valueOf(craftSettings.getString(ingredientPathBuilder.toString())));

				customRecipe.setIngredient(ingredient.charAt(0), ingredientItem.getData());

			} catch (IllegalArgumentException ex) {
				StringBuilder exceptionMessageBuilder = new StringBuilder("craft-material : This items don't exist ");
				exceptionMessageBuilder.append(craftSettings.getString(ingredientPathBuilder.toString()));
				exceptionMessageBuilder.append(". Default recipe will be used.");
				ServerUtils.errorMessage(exceptionMessageBuilder.toString());
				customRecipe = defaultPluginRecipe();
			}

		}

		return customRecipe;

	}

	private static boolean useDefaultPluginRecipe() {
		ConfigurationSection craftSettings = MainCreeperDust.settings.getConfig()
				.getConfigurationSection("craft-settings");

		if (craftSettings != null && craftSettings.contains("craft-lines")
				&& craftSettings.contains("craft-materials")) {

			if (craftSettings.getStringList("craft-lines").size() <= 3) {
				return false;
			} else {
				ServerUtils.errorMessage("The option 'craft-lines' must have maximum 3 lines !");
				return true;
			}

		} else {
			return true;
		}

	}

	public static void initializeRecipes() {

		if (Bukkit.getServer().getRecipesFor(ItemsAPI.creeperEgg()) != null) {
			ServerUtils.debugMessage("Removed the old creeper egg recipe.");
			Bukkit.getServer().getRecipesFor(ItemsAPI.creeperEgg()).clear();
		}

		Bukkit.getServer().addRecipe(creeperEggRecipe());
	}

}
