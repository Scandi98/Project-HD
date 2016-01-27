package server.model.minigames;

import java.util.HashMap;

import server.model.players.Client;
import server.model.players.PlayerSave;

public class ClanWars {

	public ClanWars() {
		/**
		 * Constructor
		 */
	}

	public static HashMap<Client, Integer> blueWait = new HashMap<Client, Integer>();
	private static HashMap<Client, Integer> redWait = new HashMap<Client, Integer>();
	private static HashMap<Client, Integer> blueTeam = new HashMap<Client, Integer>();
	private static HashMap<Client, Integer> redTeam = new HashMap<Client, Integer>();
	public boolean gameInProgress = false;
	public int blueKilled;
	public int redKilled;
	public int count;
	int properTimer = 0;

	private final int GAME_TIMER = 200; // 1500 * 600 = 900000ms = 15 minutes
	private final int GAME_START_TIMER = 20;
	public int timeRemaining = -1;
	public int gameStartTimer = GAME_START_TIMER;

	/*
	 * Used to add a Player to the Waiting Room
	 */
	public void joinWait(Client c, int team) {
		for (Client blue : blueWait.keySet()) {
			if (blue != null) {
				if (blue == c)
					return;
			}
		}
		for (Client red : redWait.keySet()) {
			if (red != null) {
				if (red == c)
					return;
			}
		}
		if (!gameInProgress) {
			if (c.playerEquipment[c.playerHat] > 0
					|| c.playerEquipment[c.playerCape] > 0
					|| c.playerEquipment[c.playerAmulet] > 0
					|| c.playerEquipment[c.playerArrows] > 0
					|| c.playerEquipment[c.playerChest] > 0
					|| c.playerEquipment[c.playerLegs] > 0
					|| c.playerEquipment[c.playerRing] > 0
					|| c.playerEquipment[c.playerFeet] > 0
					|| c.playerEquipment[c.playerHands] > 0
					|| c.playerEquipment[c.playerWeapon] > 0
					|| c.playerEquipment[c.playerShield] > 0
					&& c.getItems().freeSlots() == 0) {
				c.sendMessage("You may not wear equipment inside or bring any items.");
				return;
			}
			if (c.petID != 0) {
				c.sendMessage("You may not bring Pets into the Minigame");
				return;
			}
			if (c.playerLevel[c.playerDefence] < 40
					&& c.playerLevel[c.playerRanged] < 40
					&& c.playerLevel[c.playerAttack] < 40) {
				c.sendMessage("This minigame currently requires 40 attack, defence, ranged, ..");
				return;
			}
			if (team == 1) {
				if (getBluePlayers() > getRedPlayers()) {
					c.sendMessage("This team is currently full.");
					return;
				} else {
					blueWait.put(c, 1);
					c.clanWarsTeam = 1;
				}
			} else if (team == 2) {
				if (getRedPlayers() > getBluePlayers()) {
					c.sendMessage("This team is currently full.");
					return;
				} else {
					redWait.put(c, 1);
					c.clanWarsTeam = 2;
				}
			} else {
				if (getRedPlayers() > getBluePlayers()) {
					blueWait.put(c, 1);
					c.sendMessage("You have been added to Team 1");
					c.clanWarsTeam = 1;
				} else {
					redWait.put(c, 1);
					c.sendMessage("You have been added to Team 2");
					c.clanWarsTeam = 2;
				}
			}
			toWaitingRoom(c, c.clanWarsTeam);
		} else {
			c.sendMessage("This is Currently a Game In Progres..");
			return;
		}
	}

	/*
	 * Method to count Red Players in Waiting Room
	 */
	public int getRedPlayers() {
		int players = 0;
		for (Client player : redWait.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	/*
	 * Method to count Blue Players in Waiting Room
	 */
	public int getBluePlayers() {
		int players = 0;
		for (Client player : blueWait.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	public int getTotalWait() {
		return getBluePlayers() + getRedPlayers();
	}

	/*
	 * Method to count Red Players in Game
	 */
	public int getRedTeam() {
		int players = 0;
		for (Client player : redTeam.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	/*
	 * Method to count Blue Players in Game
	 */
	public int getBlueTeam() {
		int players = 0;
		for (Client player : blueTeam.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	/*
	 * Sends the player to the Waiting Room
	 */
	public void toWaitingRoom(Client c, int team) {
		if (team == 1) {
			c.getPA().movePlayer(2376, 9487, 0);
		} else if (team == 2) {
			c.getPA().movePlayer(2421, 9524, 0);
		}
		c.inClanWarsWait = true;
		c.sendMessage("@red@Use ::pack 0, 1, 2, The default is 0 which is a melee pack");
		c.sendMessage("@red@Pack 1 is an Archer's Pack and Pack 2 is a Mage Pack");
	}

	public void process() {
		if (properTimer > 0) {
			properTimer--;
			return;
		} else {
			properTimer = 4;
		}

		if (gameStartTimer > 0) {
			gameStartTimer--;
			updatePlayers();
		} else if (gameStartTimer == 0 && getTotalWait() > 4) {
			startGame();
		} else {
			gameStartTimer = 20;
		}
		if (timeRemaining > 0) {
			timeRemaining--;
			updateInGamePlayers();
		} else if (timeRemaining == 0) {
			endGame();
		}
		/**
		 * The process of the clan wars game - called every 3 seconds
		 */
	}

	public boolean inGame() {
		for (Client c : blueTeam.keySet()) {
			if (c != null) {
				if (c.inClanWarsGame)
					return true;
			}
		}
		for (Client c : redTeam.keySet()) {
			if (c != null) {
				if (c.inClanWarsGame)
					return true;
			}
		}
		return false;
	}

	public void updatePlayers() {
		// saradomin players
		for (Client c : blueWait.keySet()) {
			if (c != null) {
				c.getPA().walkableInterface(6673);
				c.getPA().sendNewString(
						"Next Game Begins In : "
								+ ((gameStartTimer * 3) + (timeRemaining * 3))
								+ " seconds.", 6570);
				c.getPA().sendNewString("", 6572);
				c.getPA().sendNewString("", 6664);
				// update interface here
			}
		}
		for (Client c : redWait.keySet()) {
			if (c != null) {
				c.getPA().walkableInterface(6673);
				c.getPA().sendNewString(
						"Next Game Begins In : " + gameStartTimer * 3
								+ " seconds.", 6570);
				c.getPA().sendNewString("", 6572);
				c.getPA().sendNewString("", 6664);
				// update interface here
			}
		}
	}

	public void startGame() {
		gameStartTimer = -1;
		timeRemaining = GAME_TIMER;
		gameInProgress = true;
		for (Client c : blueWait.keySet()) {
			if (c != null) {
				// put player @ coords
				c.getPA().walkableInterface(-1);
				c.getPA().movePlayer(2414, 3073, 0);
				blueTeam.put(c, 1);
				c.inClanWarsWait = false;
				c.inClanWarsGame = true;
				setPlayer(c);
				c.lastVeng = 0;
				c.vengOn = false;
				if (c.playerMagicBook != 0)
					c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			}
		}
		count += blueWait.size();
		blueWait.clear();

		for (Client c : redWait.keySet()) {
			if (c != null) {
				c.getPA().walkableInterface(-1);
				c.getPA().movePlayer(2385, 3134, 0);
				redTeam.put(c, 1);
				c.inClanWarsWait = false;
				c.inClanWarsGame = true;
				setPlayer(c); // Sets the Players Items/Equipment
				c.lastVeng = 0;
				c.vengOn = false;
				c.poisonDamage = 0;
				c.isPoisoned = false;
				if (c.playerMagicBook != 0)
					c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			}
		}
		redWait.clear();
		count = blueTeam.size() + redTeam.size();
	}

	/*
	 * Used to update all in game players Interface Overlay
	 */
	public void updateInGamePlayers() {
		for (Client c : blueTeam.keySet()) {
			if (c != null) {
				if (inGame() && getRedTeam() == 0 || getBlueTeam() == 0) {
					endGame();
				}
				c.getPA().walkableInterface(21119);
				c.getPA().sendNewString("Time Left: " + timeRemaining, 21120);
				c.getPA().sendNewString("Players in Game: " + count, 21121);
				c.getPA().sendNewString("Red Players Killed " + blueKilled,
						21122);
				c.getPA().sendNewString(
						"Blue Players Killed: " + redKilled + "", 21123);
			}
		}
		for (Client c : redTeam.keySet()) {
			if (c != null) {
				if (inGame() && getRedTeam() == 0 || getBlueTeam() == 0) {
					endGame();
				}
				c.getPA().walkableInterface(21119);
				c.getPA().sendNewString("Time Left: " + timeRemaining, 21120);
				c.getPA().sendNewString("Players in Game: " + count, 21121);
				c.getPA().sendNewString("Blue Players Killed " + redKilled,
						21122);
				c.getPA().sendNewString(
						"Red Players Killed: " + blueKilled + "", 21123);
			}

		}
	}

	/*
	 * Ends the current game session
	 */
	public void endGame() {
		for (Client c : blueTeam.keySet()) {
			if (c == null) {
				continue;
			}
			c.getCombat().resetPlayerAttack();
			c.resetWalkingQueue();
			c.inClanWarsGame = false;
			c.getPA().removeAllWindows();
			for (int i = 0; i < 14; i++) {
				c.playerEquipment[i] = -1;
				c.playerEquipmentN[i] = -1;
				c.getItems().setEquipment(-1, 1, i);
			}
			c.getItems().removeAllItems();
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().resetItems(3214);
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getPA().movePlayer(2440, 3088, 0);
			PlayerSave.saveGame(c);
			if (redKilled < blueKilled) {
				c.sendMessage("Your team has won the Clan War! Congratulations.");
				c.getItems().addItem(995, 500000);
				c.cwPoints += 2;
				c.sendMessage("You have gained @red@2 cw Points, You currently have @red@"
						+ c.cwPoints);
			} else if (blueKilled < redKilled) {
				c.sendMessage("Your team has fought valiantly but has fallen.");
				c.getItems().addItem(995, 100000);
				c.cwPoints += 1;
				c.sendMessage("You have gained @red@1 cw Points, You currently have @red@"
						+ c.cwPoints);
			} else {
				c.sendMessage("The game was a Tie!");
				c.getItems().addItem(995, 250000);
				c.cwPoints += 1;
				c.sendMessage("You have gained @red@1 cw Points, You currently have @red@"
						+ c.cwPoints);
			}
			c.getCombat().resetPrayers();
			for (int i = 0; i < 20; i++) {
				c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
				c.getPA().refreshSkill(i);
			}
			c.startAnimation(-1);
			c.getPA().requestUpdates();
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().updateSlot(3);
			c.getPA().resetAnimation();
			c.setup = 0;
		}
		for (Client c : redTeam.keySet()) {
			if (c == null) {
				continue;
			}
			c.getCombat().resetPlayerAttack();
			c.resetWalkingQueue();
			c.inClanWarsGame = false;
			c.getPA().removeAllWindows();
			for (int i = 0; i < 14; i++) {
				c.playerEquipment[i] = -1;
				c.playerEquipmentN[i] = -1;
				c.getItems().setEquipment(-1, 1, i);
			}
			c.getItems().removeAllItems();
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().resetItems(3214);
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getPA().movePlayer(2440, 3088, 0);
			PlayerSave.saveGame(c);
			if (blueKilled < redKilled) {
				c.sendMessage("Your team has won the Clan War! Congratulations.");
				c.getItems().addItem(995, 500000);
				c.cwPoints += 2;
				c.sendMessage("You have gained @red@2 cw Points, You currently have @red@"
						+ c.cwPoints);
			} else if (redKilled < blueKilled) {
				c.sendMessage("Your team has fought vali"
						+ "antly but has fallen.");
				c.getItems().addItem(995, 100000);
				c.cwPoints += 1;
				c.sendMessage("You have gained @red@1 cw Points, You currently have @red@"
						+ c.cwPoints);
			} else {
				c.sendMessage("The game was a Tie!");
				c.getItems().addItem(995, 250000);
				c.cwPoints += 1;
				c.sendMessage("You have gained @red@1 cw Points, You currently have @red@"
						+ c.cwPoints);
			}
			c.getCombat().resetPrayers();
			for (int i = 0; i < 20; i++) {
				c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
				c.getPA().refreshSkill(i);
			}
			c.startAnimation(-1);
			c.getPA().requestUpdates();
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().updateSlot(3);
			c.getPA().resetAnimation();
			c.setup = 0;
		}
		redKilled = 0;
		blueKilled = 0;
		gameInProgress = false;
		count = 0;
		redTeam.clear();
		blueTeam.clear();
		timeRemaining = -1;
		gameStartTimer = GAME_START_TIMER;
	}

	/*
	 * Player Leaves the Game via Logout
	 */
	public void removePlayer(Client c) {
		if (blueTeam.containsKey(c)) {
			blueTeam.remove(c);
			getBluePlayers();
		} else if (redTeam.containsKey(c)) {
			redTeam.remove(c);
			getRedPlayers();
		}
		count--;
		c.inClanWarsGame = false;
		c.absX = 2440;
		c.absY = 3088;
	}

	public void giveItems(Client c) {
		c.getItems().removeAllItems();
		int teamId = 0;
		if (c.clanWarsTeam == 1) {
			teamId = 4514;
		} else if (c.clanWarsTeam == 2) {
			teamId = 4516;
		}
		if (c.setup == 0) {
			int[] equip = { 1163, teamId, 1725, 1333, 1127, 1201, -1, 1079, -1,
					1059, 1061, -1, -1 };
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().addItem(1319, 1);
			c.getItems().addItem(113, 1);
			c.getItems().addItem(379, 26);
		} else if (c.setup == 1) {
			int[] equip = { 1163, teamId, 1731, 853, 1135, -1, -1, 1079, -1,
					1065, 1061, -1, 890 };
			for (int i = 0; i < 13; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
			c.playerEquipment[c.playerArrows] = 890;
			c.playerEquipmentN[c.playerArrows] = 201;
			c.getItems().deleteArrow();
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().addItem(1099, 1);
			c.getItems().addItem(1319, 1);
			c.getItems().addItem(113, 1);
			c.getItems().addItem(379, 25);
		} else if (c.setup == 2) {
			int[] equip = { 579, teamId, 1731, 1387, 577, 1201, -1, 1011, -1,
					1065, 1061, -1, -1 };
			for (int i = 0; i < 13; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().addItem(560, 1000);
			c.getItems().addItem(556, 1000);
			c.getItems().addItem(561, 1000);
			c.getItems().addItem(555, 1000);
			c.getItems().addItem(557, 1000);
			c.getItems().addItem(379, 23);
		}
		c.getItems().resetItems(3214);
		c.getItems().resetBonus();
		c.getItems().getBonus();
		c.getItems().writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/*
	 * Player Leaves the Wait Room via Logout
	 */
	public void removePlayerWait(Client c) {
		if (redWait.containsKey(c)) {
			redWait.remove(c);
		} else if (blueWait.containsKey(c)) {
			blueWait.remove(c);
		}
		c.inClanWarsWait = false;
		c.absX = 2440;
		c.absY = 3088;
	}

	/*
	 * Player Leaves the Wait Room via Portal
	 */
	public void removeWait(Client c) {
		if (!c.inClanWarsGame) {
			if (redWait.containsKey(c)) {
				redWait.remove(c);
			} else if (blueWait.containsKey(c)) {
				blueWait.remove(c);
			}
			c.inClanWarsWait = false;
			c.getPA().movePlayer(2440, 3088, 0);
		}
	}

	public void setPlayer(Client c) {
		if (blueTeam.keySet().contains(c)) {
			if (c.setup == 0) {
				int[] equip = { 1163, 4514, 1725, 1333, 1127, 1201, -1, 1079,
						-1, 1059, 1061, -1, -1 };
				for (int i = 0; i < equip.length; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(1319, 1);
				c.getItems().addItem(113, 1);
				c.getItems().addItem(379, 26);
			} else if (c.setup == 1) {
				int[] equip = { 1163, 4514, 1731, 853, 1135, -1, -1, 1079, -1,
						1065, 1061, -1, 890 };
				for (int i = 0; i < 13; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.playerEquipment[c.playerArrows] = 890;
				c.playerEquipmentN[c.playerArrows] = 201;
				c.getItems().deleteArrow();
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(1099, 1);
				c.getItems().addItem(1319, 1);
				c.getItems().addItem(113, 1);
				c.getItems().addItem(379, 25);
			} else if (c.setup == 2) {
				int[] equip = { 579, 4514, 1731, 1387, 577, 1201, -1, 1011, -1,
						1065, 1061, -1, -1 };
				for (int i = 0; i < 13; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().deleteArrow();
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(560, 1000);
				c.getItems().addItem(556, 1000);
				c.getItems().addItem(561, 1000);
				c.getItems().addItem(555, 1000);
				c.getItems().addItem(557, 1000);
				c.getItems().addItem(1333, 1);
				c.getItems().addItem(379, 22);
			}
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().resetItems(3214);
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
		} else if (redTeam.keySet().contains(c)) {
			if (c.setup == 0) {
				int[] equip = { 1163, 4516, 1725, 1333, 1127, 1201, -1, 1079,
						-1, 1059, 1061, -1, -1 };
				for (int i = 0; i < equip.length; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(1319, 1);
				c.getItems().addItem(113, 1);
				c.getItems().addItem(379, 26);
			} else if (c.setup == 1) {
				int[] equip = { 1163, 4516, 1731, 853, 1135, -1, -1, 1079, -1,
						1065, 1061, -1, 890 };
				for (int i = 0; i < 13; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.playerEquipment[c.playerArrows] = 890;
				c.playerEquipmentN[c.playerArrows] = 201;
				c.getItems().deleteArrow();
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(1099, 1);
				c.getItems().addItem(1319, 1);
				c.getItems().addItem(113, 1);
				c.getItems().addItem(379, 25);
			} else if (c.setup == 2) {
				int[] equip = { 579, 4516, 1731, 1387, 577, 1201, -1, 1011, -1,
						1065, 1061, -1, -1 };
				for (int i = 0; i < 13; i++) {
					c.playerEquipment[i] = equip[i];
					c.playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().deleteArrow();
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().addItem(560, 1000);
				c.getItems().addItem(556, 1000);
				c.getItems().addItem(561, 1000);
				c.getItems().addItem(555, 1000);
				c.getItems().addItem(557, 1000);
				c.getItems().addItem(1333, 1);
				c.getItems().addItem(379, 22);
			}
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().resetItems(3214);
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
		}
	}

}
