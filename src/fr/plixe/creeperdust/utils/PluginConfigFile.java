package fr.plixe.creeperdust.utils;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.plixe.creeperdust.utils.ColorUtils;

/*
 * API to help yaml files management.
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class PluginConfigFile {

	private Plugin plugin;
	private String yamlFileName;
	private File file;
	private FileConfiguration configuration;

	public PluginConfigFile(Plugin plugin, File file, FileConfiguration configuration, String yamlFileName) {

		this.plugin = plugin;
		this.file = file;
		this.configuration = configuration;
		this.yamlFileName = yamlFileName;
	}

	public String getFileName() {
		return yamlFileName;
	}

	public File getFile() {
		return file;
	}

	public FileConfiguration getConfig() {
		return configuration;
	}

	public void initializeFile() {

		file = new File(plugin.getDataFolder(), yamlFileName);

		if (!file.exists()) {
			file.getParentFile().mkdir();
			plugin.saveResource(yamlFileName, false);
		}

		try {
			configuration = new YamlConfiguration();
			configuration.load(file);
		} catch (InvalidConfigurationException | IOException e) {

			StringBuilder moduleInitializationFailedMessageBuilder = new StringBuilder(
					ColorUtils.getColoredPluginName());
			moduleInitializationFailedMessageBuilder.append(" &cInitialization failed !");

			ColorUtils.sendColoredMessage(Bukkit.getConsoleSender(),
					moduleInitializationFailedMessageBuilder.toString());

			e.printStackTrace();
		}

	}

	public void saveFile() {
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadFile(CommandSender sender) {
		StringBuilder reloadFileMessageBuilder = new StringBuilder();
		reloadFileMessageBuilder.append(ColorUtils.getColoredPluginName());
		reloadFileMessageBuilder.append("&7");
		reloadFileMessageBuilder.append(getFileName());
		try {
			configuration.load(file);
			reloadFileMessageBuilder.append(" &7successfully reloaded.");
			ColorUtils.sendColoredMessage(sender, reloadFileMessageBuilder.toString());
		} catch (InvalidConfigurationException | IOException exception) {
			reloadFileMessageBuilder.append(" &ccan't be reloaded. &7See console logs.");
			ColorUtils.sendColoredMessage(sender, reloadFileMessageBuilder.toString());
			exception.printStackTrace();
		}
	}

}
