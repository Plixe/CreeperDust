package fr.plixe.creeperdust.utils;

import java.lang.reflect.Constructor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.plixe.creeperdust.MainCreeperDust;
import net.md_5.bungee.api.ChatColor;

/*
 * API to easily use colors in minecraft.
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
public class ColorUtils {

	public static String getColoredPluginName() {
		StringBuilder coloredModuleNameBuilder = new StringBuilder();
		coloredModuleNameBuilder.append("&a[");
		coloredModuleNameBuilder.append(MainCreeperDust.creeperDustPluginInstance.getName());
		coloredModuleNameBuilder.append("]&r ");
		return coloredString(coloredModuleNameBuilder.toString());
	}

	public static boolean hasPermissions(CommandSender commandSender, String testePermission) {

		if (commandSender.hasPermission(testePermission)) {
			return true;
		} else {
			sendColoredMessage(commandSender,
					MainCreeperDust.messages.getConfig().getString("global-messages.no-permissions"));
			return false;
		}

	}

	public static String coloredString(String stringToColor) {

		return ChatColor.translateAlternateColorCodes('&', stringToColor);

	}

	public static void sendColoredMessage(CommandSender commandSender, String messageToSend) {

		commandSender.sendMessage(coloredString(messageToSend));

	}

	public static void sendColoredUnknowCommandMessage(CommandSender commandSender) {
		sendColoredMessage(commandSender,
				MainCreeperDust.messages.getConfig().getString("global-messages.unknow-command"));
	}

	public static void sendColoredActionBar(Player targetPlayer, String messageToSend) {

		try {

			StringBuilder messageToSendBuilder = new StringBuilder("{\"text\":\"");
			messageToSendBuilder.append(coloredString(messageToSend));
			messageToSendBuilder.append("\"}");

			Constructor<?> constructor = ServerUtils.getNMSClass("PacketPlayOutChat").getConstructor(
					ServerUtils.getNMSClass("IChatBaseComponent"), ServerUtils.getNMSClass("ChatMessageType"));

			Object ichatBaseComponent = ServerUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", String.class).invoke(null, messageToSendBuilder.toString());
			Object packet = constructor.newInstance(ichatBaseComponent,
					ServerUtils.getNMSClass("ChatMessageType").getEnumConstants()[2]);
			Object entityPlayer = targetPlayer.getClass().getMethod("getHandle").invoke(targetPlayer);
			Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

			playerConnection.getClass().getMethod("sendPacket", ServerUtils.getNMSClass("Packet"))
					.invoke(playerConnection, packet);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static void sendColoredTitle(Player targetPlayer, String titleToSend, String subtitleToSend) {

		try {

			StringBuilder messageToSendBuilder = new StringBuilder("{\"text\":\"");
			messageToSendBuilder.append(coloredString(titleToSend));
			messageToSendBuilder.append("\"}");

			Object chatTitle = ServerUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", String.class).invoke(null, messageToSendBuilder.toString());
			Constructor<?> titleConstructor = ServerUtils.getNMSClass("PacketPlayOutTitle").getConstructor(
					ServerUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
					ServerUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
			Object titlePacket = titleConstructor.newInstance(
					ServerUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null),
					chatTitle, 0, 20, 0);

			messageToSendBuilder.replace(12, titleToSend.length(), subtitleToSend);

			Object chatSubtitle = ServerUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", String.class).invoke(null, messageToSendBuilder);
			Constructor<?> subtitleConstructor = ServerUtils.getNMSClass("PacketPlayOutTitle").getConstructor(
					ServerUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
					ServerUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
			Object subtitlePacket = subtitleConstructor
					.newInstance(ServerUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]
							.getField("SUBTITLE").get(null), chatSubtitle, 0, 20, 0);

			PlayerUtils.sendPacket(targetPlayer, titlePacket);
			PlayerUtils.sendPacket(targetPlayer, subtitlePacket);

		} catch (Exception exception) {

			exception.printStackTrace();

		}

	}

}
