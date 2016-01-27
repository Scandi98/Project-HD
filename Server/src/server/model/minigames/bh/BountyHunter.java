package server.model.minigames.bh;

import server.core.PlayerHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.util.Misc;

public class BountyHunter {

	/**
	 * Handles the interfaces
	 * 
	 * @param player
	 *            player sending the interfaces
	 */
	public static void handleInterfaces(Client player) {
		player.getPA().walkableInterface(27300);
	}

	/**
	 * calculates the difference
	 * 
	 * @param player
	 * @param up
	 *            if you want to calculate up or down
	 * @return level difference
	 */
	static int getLevelDifference(Client player, boolean up) {
		int difference = 0;
		if (up)
			difference = (int) player.combatLevel + player.wildLevel;
		else
			difference = (int) player.combatLevel - player.wildLevel;
		return difference < 3 ? 3 : difference > 126 && up ? 126 : difference;
	}

	/**
	 * Checks if you have a target
	 * 
	 * @param player
	 * @return
	 */
	static boolean playerHasTarget(Client player) {
		return player.targetIndex != 0
				&& (player.targetName != "" || player.targetName != null);
	}

	/**
	 * Assigns a target for
	 * 
	 * @param player
	 */
	public static void assignTarget(Client player) {
		for (Player players : PlayerHandler.players) {
			if (players != null) {
				Client p = (Client) players;
				if (p.logoutTimer <= 0
						&& player.logoutTimer <= 0
						&& p.playerId != player.playerId
						&& !playerHasTarget(p)
						&& !playerHasTarget(player)
						&& player.inWild()
						&& p.inWild()
						&& p.targetPercentage > 3
						&& (p.combatLevel == player.combatLevel
								|| p.combatLevel >= player.combatLevel + 5 || p.combatLevel >= player.combatLevel  - 5)) {
					if (player == p) {
						player.sendMessage("Error assigning Target, Re-trying in 60 Seconds");
						return;
					}
					setTarget(player, p.playerId, p.playerName);
					setTarget(p, player.playerId, player.playerName);
				}
			}
		}
	}

	/**
	 * Sets a player target
	 * 
	 * @param player
	 * @param targetPlayerId
	 * @param targetName
	 */
	static void setTarget(Client c, int targetPlayerId, String targetName) {
		if (!playerHasTarget(c)) {
			c.targetIndex = targetPlayerId;
			c.targetName = targetName;
			if (PlayerHandler.players[targetPlayerId] != null) {
				Client o = (Client) PlayerHandler.players[targetPlayerId];
				c.getPA().createPlayerHints(10, o.playerId);
				o.getPA().createPlayerHints(10, c.playerId);
				c.getPA().sendNewString(Misc.formatPlayerName(o.playerName),
						27307);
				o.getPA().sendNewString(Misc.formatPlayerName(c.playerName),
						27307);
				c.getPA().sendNewString(getWildernessLevel(c, o), 27308);
				o.getPA().sendNewString(getWildernessLevel(o, c), 27308);
				findSkull(c, o);
				findSkull(o, c);
				c.getPA().sendNewString(findWealth(o), 27305);
				o.getPA().sendNewString(findWealth(c), 27305);
			}
		}
	}

	public static void findSkull(Client c, Client o) {
		if (getWealth(o) >= 0 && getWealth(o) <= 150000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(877, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 150000 && getWealth(o) <= 400000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(878, 1);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 400000 && getWealth(o) <= 800000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(879, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 800000 && getWealth(o) <= 1500000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(880, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(881, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(877, 0);
		} else if (getWealth(o) > 1500000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(881, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(880, 0);
		}
	}

	public static String getWildernessLevel(Client c, Client o) {
		if (!o.inWild()) {
			return "@gre@Safe, Cmb " + o.combatLevel;
		}
		int wildernessDifference = o.wildLevel + 3;// what color?
		String color = "";
		if (c.distanceToPoint(o.absX, o.absY) <= 15) {
			color = "@gre@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 15
				&& c.distanceToPoint(o.absX, o.absY) <= 60) {
			color = "@bro@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 60) {
			color = "@bl2@";
		}
		return color + "Lvl " + o.wildLevel + "-" + wildernessDifference
				+ ", Cmb " + o.combatLevel;
	}

	public static int getWealth(Client c) {
		return c.getWealth();
	}

	public static String findWealth(Client c) {

		if (getWealth(c) >= 0 && getWealth(c) <= 150000) {
			return "Wealth: V. Low";
		} else if (getWealth(c) > 150000 && getWealth(c) <= 400000) {
			return "Wealth: Low";
		} else if (getWealth(c) > 400000 && getWealth(c) <= 800000) {
			return "Wealth: Medium";
		} else if (getWealth(c) > 800000 && getWealth(c) <= 1500000) {
			return "Wealth: High";
		} else if (getWealth(c) > 1500000) {
			return "Wealth: V. High";
		}
		return "";
	}

	/**
	 * Checks if the target is a null
	 * 
	 * @param targetName
	 *            players target name
	 * @return null or not
	 */
	static boolean targetIsNull(String targetName) {
		for (Player p : PlayerHandler.players)
			if (p != null && p.playerName.equalsIgnoreCase(targetName))
				return false;
		return true;
	}

	/**
	 * Handles player to gain earned potential
	 * 
	 * @param player
	 */
	public static void handleEP(final Client player) {
		player.EP_ACTIVE = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			int timer = 0;

			@Override
			public void execute(CycleEventContainer container) {
				// player.sendMessage(""+player.logoutTimer);
				// player.sendMessage(""+targetIsNull(player.targetName));
				if (player.logoutTimer >= 0 && targetIsNull(player.targetName))
					player.logoutTimer--;
				if (player.logoutTimer == 1 && targetIsNull(player.targetName)) {
					player.sendMessage("Your target has logged out, You will be assigned a new target soon.");
					player.getPA().createPlayerHints(-1, player.targetIndex);
					setTarget(player, 0, null);
					player.cleanInterface();
				}
				/*
				 * ep system
				 */
				timer++;
				updateInterface(player,
						(Client) PlayerHandler.players[player.targetIndex]);
				if (timer == 100) {
					if (player.inWild()) {
						if (player.targetPercentage <= 3) {
							if (player.targetPercentage <= 3) {
								player.getPA().sendNewString(
										(3 - player.targetPercentage) + " min",
										27308);
							}
							player.targetPercentage++;
						}
					}
					timer = 0;
				}
				// player.sendMessage("Current timer: "+timer+" total minutes: "+player.EP_MINUTES);
				// player.sendMessage("target percentage: "+player.targetPercentage+" total EP: "+player.EP);
				// player.sendMessage("logout timer: "+player.logoutTimer);
				if (!playerHasTarget(player) && player.targetPercentage > 3)
					assignTarget(player);
				if (!player.inWild()) {
					if (!playerHasTarget(player))
						container.stop();
					else {
						player.safeTimer--;
						if (player.safeTimer == 0) {
							player.sendMessage("You were too long in a safe area and lost your target.");
							if (((Client) PlayerHandler.players[player.targetIndex]) != null) {
								((Client) PlayerHandler.players[player.targetIndex])
										.sendMessage("Your target became inactive and you will be assigned a new one.");
								((Client) PlayerHandler.players[player.targetIndex])
								.cleanInterface();
							}
							player.cleanInterface();
						
							resetTarget(player);
						}
						if (player.safeTimer == 300 || player.safeTimer == 200
								|| player.safeTimer == 100) {
							player.sendMessage("You must leave the safe zone in  "
									+ player.safeTimer
									/ 100
									+ " minutes or you will lose your target!");
						}
					}
				}
			}

			@Override
			public void stop() {
				player.EP_ACTIVE = false;
			}
		}, 1);
	}

	public static void handleKilled(Client killer, Client victim) {
		if (PlayerHandler.players[killer.targetIndex] == victim) {
			// killer.sendMessage("[TARGET]:  <shad=65280>You killed your target and you receive 1 bounty point.");
			// killer.bountyPoints ++;
			killer.sendMessage("@red@You killed your target and you receive 2 extra pk points and a Hunter Point.");
			killer.pkPoints += 2;
			killer.targetIndex = 0;
			victim.targetIndex = 0;
			victim.targetPercentage = 0;
			killer.sendMessage("@red@You will be assigned a new target soon.");
			victim.getPA().createPlayerHints(10, -1);
			killer.getPA().createPlayerHints(10, -1);
			killer.hunterPoints += 1;
			killer.cleanInterface();
			victim.cleanInterface();
			killer.hunterStreak++;
			killer.hunterRecord++;
			killer.getPA().sendNewString("" + killer.hunterStreak, 27311);
			killer.getPA().sendNewString("" + killer.hunterRecord, 27313);
			if (victim.hunterStreak > victim.hunterRecord) {
				victim.hunterRecord = victim.hunterStreak;
			}
			victim.hunterStreak = 0;
		} else {
			killer.rogueStreak++;
			killer.rogueRecord++;
			killer.getPA().sendNewString("" + killer.rogueStreak, 27310);
			killer.getPA().sendNewString("" + killer.rogueRecord, 27312);
			if (victim.rogueStreak > victim.rogueRecord) {
				victim.rogueRecord = victim.rogueStreak;
			}
			victim.rogueStreak = 0;
		}
		updateRecordInterface(killer, victim);
	}

	/**
	 * Resets the target
	 * 
	 * @param player
	 */
	static void resetTarget(Client player) {
		player.getPA().createPlayerHints(-1, player.targetIndex);
		if (((Client) PlayerHandler.players[player.targetIndex]) != null) {
			((Client) PlayerHandler.players[player.targetIndex]).getPA()
					.createPlayerHints(-1, player.playerId);
			PlayerHandler.players[player.targetIndex].targetIndex = 0;
			PlayerHandler.players[player.targetIndex].safeTimer = 300;
		}
		player.targetIndex = 0;
		player.targetPercentage = 0;
		player.safeTimer = 300;
	}

	/**
	 * Handles the logout
	 * 
	 * @param player
	 */
	public static void handleLogout(Client player) {
		Client target = (Client) PlayerHandler.players[player.targetIndex];
		if (target != null) {
			if (target.logoutTimer <= 0)
				target.logoutTimer = 2;
			target.targetIndex = 0;
		}
	}

	/**
	 * Handles the login
	 * 
	 * @param player
	 *            player loggin in
	 */
	public static void handleLogin(Client player) {
		Client target = (Client) PlayerHandler.players[getTargetIndex(player.targetName)];
		if (target != null && target.logoutTimer > 0) {
			target.sendMessage("Your target has logged in and you resigned him as a target!");
			setTarget(target, player.playerId, player.playerName);
			setTarget(player, target.playerId, target.playerName);
		} else {
			setTarget(player, 0, null);
		}
		if (target != null && target.logoutTimer <= 0) {
			player.targetPercentage = 0;
		}
		if (player.inWild())
			handleEP(player);
		player.cleanInterface();
	}

	public static void updateInterface(Client c, Client o) {
		if (o != null) {
			c.getPA().sendNewString(Misc.formatPlayerName(o.playerName), 27307);
			o.getPA().sendNewString(Misc.formatPlayerName(c.playerName), 27307);
			c.getPA().sendNewString(getWildernessLevel(c, o), 27308);
			o.getPA().sendNewString(getWildernessLevel(o, c), 27308);
			c.getPA().sendNewString(findWealth(o), 27305);
			o.getPA().sendNewString(findWealth(c), 27305);
			c.getPA().sendNewString("" + c.rogueStreak, 27310);
			c.getPA().sendNewString("" + c.hunterStreak, 27312);
			c.getPA().sendNewString("" + c.rogueRecord, 27311);
			c.getPA().sendNewString("" + c.hunterRecord, 27313);
			o.getPA().sendNewString("" + o.hunterStreak, 27313);
			o.getPA().sendNewString("" + o.rogueStreak, 27311);
			findSkull(c, o);
			findSkull(o, c);
		} else {
			c.getPA().sendNewString("None", 27307);
			c.getPA().sendNewString("---", 27305);
			c.getPA().sendNewString("" + c.rogueStreak, 27310);
			c.getPA().sendNewString("" + c.hunterStreak, 27312);
			c.getPA().sendNewString("" + c.rogueRecord, 27311);
			c.getPA().sendNewString("" + c.hunterRecord, 27313);
			for (int i = 0; i < 6; i++) {
				c.getPA().sendConfig(876 + i, 0);
			}
		}
	}
	
	public static void updateRecordInterface(Client c, Client o) {
		if (o != null) {
			c.getPA().sendNewString("" + c.rogueStreak, 27310);
			c.getPA().sendNewString("" + c.hunterStreak, 27312);
			c.getPA().sendNewString("" + c.rogueRecord, 27311);
			c.getPA().sendNewString("" + c.hunterRecord, 27313);
			o.getPA().sendNewString("" + o.hunterStreak, 27313);
			o.getPA().sendNewString("" + o.rogueStreak, 27311);
		}
	}

	/**
	 * Gets the target's player index by
	 * 
	 * @param name
	 * @return target player index
	 */
	public static int getTargetIndex(String name) {
		for (Player p : PlayerHandler.players)
			if (p != null && p.playerName.equalsIgnoreCase(name))
				return p.playerId;
		return 0;
	}
}