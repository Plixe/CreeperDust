package fr.plixe.creeperdust.utils;

/*
 * API to help interactions with players.
 * @version BETA-0.1
 * @author github.com/Plixe/
 **/
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static void sendPacket(Player targetPlayer, Object packetToSend) {
		try {

			Object handle = targetPlayer.getClass().getMethod("getHandle").invoke(targetPlayer);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", ServerUtils.getNMSClass("Packet"))
					.invoke(playerConnection, packetToSend);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static void playSoundToPlayer(Player targetPlayer, Sound soundPlayed, float volume, int pitch) {
		targetPlayer.playSound(targetPlayer.getLocation(), soundPlayed, volume, pitch);
	}

	public static String playerUUIDToString(Player player) {
		return player.getUniqueId().toString().replace("-", "");
	}

}
