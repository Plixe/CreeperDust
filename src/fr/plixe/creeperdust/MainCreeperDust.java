package fr.plixe.creeperdust;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.plixe.creeperdust.assets.CommandManager;
import fr.plixe.creeperdust.assets.events.CustomItemsCraftEvent;
import fr.plixe.creeperdust.assets.events.DroppersDeathEvent;
import fr.plixe.creeperdust.assets.items.CraftAPI;
import fr.plixe.creeperdust.utils.PluginConfigFile;

/*
 * Main class of the Plugin
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class MainCreeperDust extends JavaPlugin {

	public static Plugin creeperDustPluginInstance;

	private File settingsFile, messagesFile;
	private FileConfiguration settingsConf, messagesConf;

	public static PluginConfigFile settings, messages;

	public void onEnable() {

		// INITIALIZE

		// Plugin
		creeperDustPluginInstance = this;

		// Plugin Configs
		settings = new PluginConfigFile(creeperDustPluginInstance, settingsFile, settingsConf, "settings.yml");
		messages = new PluginConfigFile(creeperDustPluginInstance, messagesFile, messagesConf, "messages.yml");
		settings.initializeFile();
		messages.initializeFile();

		// Custom Recipes
		CraftAPI.initializeRecipes();

		// Command
		getCommand("creeperdust").setExecutor(new CommandManager());

		// Listeners
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new DroppersDeathEvent(), creeperDustPluginInstance);
		pluginManager.registerEvents(new CustomItemsCraftEvent(), creeperDustPluginInstance);

	}

}
