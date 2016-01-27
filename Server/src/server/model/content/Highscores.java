package server.model.content;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import server.model.players.Player;

public class Highscores {

	/**
	 * The lock object variable.
	 */
	private static Object lock = new Object();

	/**
	 * Update a player to the highscores.
	 * 
	 * @param player
	 *            The player reference.
	 * @param displayname
	 *            The player's display name.
	 */
	public static void highscores(final Player c, final String displayname) {
		if (c == null) {
			return;
		}
		if (c.playerRights >= 2 && c.playerRights <= 3) {// 2 and 3? yep.
			return;
		}
		if (!requirements(c)) {
			return;
		}
		final int[] levels = getLevels(c);
		final int[] xp = getXp(c);
		final int total = getTotalLevel(c);
		final String totalxp = getTotalXp(c);
		String website = "http://hiscores.dragon-ages.org/";// u made the
															// subdonain? Yea
															// because if i
															// don't it won't
															// connect due to
															// ddos protection
															// >_>ok
		if (levels == null || xp == null || totalxp == null) {
			return;
		}
		try {
			synchronized (lock) {// what ranks then? 4 = regular 5 = super 6 =
									// extreme ok mods rights? 2
				int rights = c.playerRights == 1 ? 1
						: c.playerRights == 4 ? 2 : c.playerRights == 5 ? 3 : c.playerRights == 6 ? 4 : 5;
				int gameMode = 1;
				URL url = new URL(website + "updatehighscores.php?pass=pass&username="
						+ c.playerName.replaceAll(" ", "_") + "&rights=" + rights + "&gamemode=" + gameMode + "&total="
						+ total + "&attack=" + levels[0] + "&defence=" + levels[1] + "&strength=" + levels[2] + ""
						+ "&constitution=" + levels[3] + "&ranged=" + levels[4] + "&prayer=" + levels[5] + "&magic="
						+ levels[6] + "&cooking=" + levels[7] + "&woodcutting=" + levels[8] + "" + "&fletching="
						+ levels[9] + "&fishing=" + levels[10] + "&firemaking=" + levels[11] + "&crafting=" + levels[12]
						+ "&smithing=" + levels[13] + "&mining=" + levels[14] + "" + "&herblore=" + levels[15]
						+ "&agility=" + levels[16] + "&thieving=" + levels[17] + "&slayer=" + levels[18] + "&farming="
						+ levels[19] + "&runecrafting=" + levels[20] + "" + "&totalxp=" + totalxp + "&attackxp=" + xp[0]
						+ "&defencexp=" + xp[1] + "&strengthxp=" + xp[2] + "" + "&constitutionxp=" + xp[3]
						+ "&rangedxp=" + xp[4] + "&prayerxp=" + xp[5] + "&magicxp=" + xp[6] + "&cookingxp=" + xp[7]
						+ "&woodcuttingxp=" + xp[8] + "" + "&fletchingxp=" + xp[9] + "&fishingxp=" + xp[10]
						+ "&firemakingxp=" + xp[11] + "&craftingxp=" + xp[12] + "&smithingxp=" + xp[13] + "&miningxp="
						+ xp[14] + "" + "&herblorexp=" + xp[15] + "&agilityxp=" + xp[16] + "&thievingxp=" + xp[17]
						+ "&slayerxp=" + xp[18] + "&farmingxp=" + xp[19] + "&runecraftingxp=" + xp[20] + "");
				System.out.println(url);
				url.openStream().close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if the player has the requirements to be on the highscores.
	 * 
	 * @param player
	 *            The player reference.
	 * @return If the player has the requirements {@code true}.
	 */
	private static boolean requirements(Player c) {
		/*
		 * if(getTotalLevel(c) < 300) return false;
		 */
		return true;
	}

	/**
	 * Get all the player's levels.
	 * 
	 * @param player
	 *            The player refrence.
	 * @return A short array containing all the player's levels.
	 */
	private static int[] getLevels(Player c) {
		int[] levels = new int[25];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = c.getLevelForXP(c.playerXP[i]);
		}
		return levels;
	}

	/**
	 * Get the player's total level.
	 * 
	 * @param player
	 *            The player reference.
	 * @return The player's total level.
	 */
	private static int getTotalLevel(Player c) {
		return c.getTotalLevel();
	}

	/**
	 * Get all the player's experience in a double array.
	 * 
	 * @param player
	 *            The player reference.
	 * @return All the player's experience in a double array.
	 */
	private static int[] getXp(Player c) {
		return c.playerXP;
	}

	/**
	 * Get the player's total experience.
	 * 
	 * @param player
	 *            The player reference.
	 * @return The player's total experience.
	 */
	private static String getTotalXp(Player c) {
		double totalxp = 0;
		for (double xp : c.playerXP) {
			totalxp += xp;
		}
		NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
	}
}