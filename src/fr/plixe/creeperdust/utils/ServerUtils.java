package fr.plixe.creeperdust.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import fr.plixe.creeperdust.MainCreeperDust;
import fr.plixe.creeperdust.utils.ColorUtils;

/*
 * API to help interactions with minecraft serveur.
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class ServerUtils {

	public static Class<?> getNMSClass(String name) {

		try {
			StringBuilder classNameBuilder = new StringBuilder("net.minecraft.server.");
			classNameBuilder.append(getVersionName());
			classNameBuilder.append(".");
			classNameBuilder.append(name);
			return Class.forName(classNameBuilder.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getVersionName() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public static String getVersionNumber() {
		String translatedVersionName = getVersionName().substring(0, 5).replace("v", "").replace("_", ".");

		if (translatedVersionName.endsWith(".")) {
			translatedVersionName = translatedVersionName.substring(0, translatedVersionName.length() - 1);
		}

		return translatedVersionName;
	}

	public static void debugMessage(String debugMessage) {

		if (MainCreeperDust.settings.getConfig().getBoolean("debug")) {
			StringBuilder debugMessageBuilder = new StringBuilder(ColorUtils.getColoredPluginName());
			debugMessageBuilder.append("Debug -> ");
			debugMessageBuilder.append(debugMessage);

			ColorUtils.sendColoredMessage(MainCreeperDust.creeperDustPluginInstance.getServer().getConsoleSender(),
					debugMessageBuilder.toString());
		}

	}

	public static void errorMessage(String errorMessage) {
		StringBuilder errorMessageBuilder = new StringBuilder(ColorUtils.getColoredPluginName());
		errorMessageBuilder.append("&cError -> ");
		errorMessageBuilder.append(errorMessage);

		ColorUtils.sendColoredMessage(MainCreeperDust.creeperDustPluginInstance.getServer().getConsoleSender(),
				errorMessageBuilder.toString());
	}

	public static boolean useOldSpigotAPI() {

		StringBuilder versionNumberDebugBuilder = new StringBuilder("Minecraft version: ");
		versionNumberDebugBuilder.append(ServerUtils.getVersionNumber());
		ServerUtils.debugMessage(versionNumberDebugBuilder.toString());

		if (ServerUtils.getVersionNumber().equals("1.7") || ServerUtils.getVersionNumber().equals("1.8")
				|| ServerUtils.getVersionNumber().equals("1.9") || ServerUtils.getVersionNumber().equals("1.10")
				|| ServerUtils.getVersionNumber().equals("1.11") || ServerUtils.getVersionNumber().equals("1.12")) {
			ServerUtils.debugMessage("Use api 1.13+: false");
			return true;
		} else {
			ServerUtils.debugMessage("Use api 1.13+: true");
			return false;
		}

	}

}
