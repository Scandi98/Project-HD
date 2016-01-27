package server.model.minigames;

import java.util.HashMap;

import server.Config;
import server.model.players.Client;
import server.model.players.PlayerSave;

public class KBDWars {

	public KBDWars() {
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
			if (c.petID != 0) {
				c.sendMessage("You may not bring Pets into the Minigame");
				return;
			}
			if (team == 1) {
				blueWait.put(c, 1);
				c.kbdWarsTeam = 1;
			} else if (team == 2) {
				redWait.put(c, 1);
				c.kbdWarsTeam = 2;
			}
			toWaitingRoom(c, c.kbdWarsTeam);
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
			c.getPA().movePlayer(3068, 10256, 4);
		} else if (team == 2) {
			c.getPA().movePlayer(3052, 10256, 4);
		}
		c.inKBDWarsWait = true;
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
				if (c.inKBDWarsGame)
					return true;
			}
		}
		for (Client c : redTeam.keySet()) {
			if (c != null) {
				if (c.inKBDWarsGame)
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
				c.getPA().sendFrame126(
						"Next Game Begins In : "
								+ ((gameStartTimer * 3) + (timeRemaining * 3))
								+ " seconds.", 6570);
				c.getPA().sendFrame126("", 6572);
				c.getPA().sendFrame126("", 6664);
				// update interface here
			}
		}
		for (Client c : redWait.keySet()) {
			if (c != null) {
				c.getPA().walkableInterface(6673);
				c.getPA().sendFrame126(
						"Next Game Begins In : " + gameStartTimer * 3
								+ " seconds.", 6570);
				c.getPA().sendFrame126("", 6572);
				c.getPA().sendFrame126("", 6664);
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
				c.getPA().movePlayer(2271, 4680, 4);
				blueTeam.put(c, 1);
				c.inKBDWarsWait = false;
				c.inKBDWarsGame = true;
				c.lastVeng = 0;
				c.vengOn = false;
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
				c.getPA().movePlayer(2271, 4711, 4);
				redTeam.put(c, 1);
				c.inKBDWarsWait = false;
				c.inKBDWarsGame = true;
				c.lastVeng = 0;
				c.vengOn = false;
				c.poisonDamage = 0;
				c.isPoisoned = false;
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
				c.getPA().sendFrame126("Time Left: " + timeRemaining, 21120);
				c.getPA().sendFrame126("Players in Game: " + count, 21121);
				c.getPA().sendFrame126("Red Players Killed " + blueKilled,
						21122);
				c.getPA().sendFrame126(
						"Blue Players Killed: " + redKilled + "", 21123);
			}
		}
		for (Client c : redTeam.keySet()) {
			if (c != null) {
				if (inGame() && getRedTeam() == 0 || getBlueTeam() == 0) {
					endGame();
				}
				c.getPA().walkableInterface(21119);
				c.getPA().sendFrame126("Time Left: " + timeRemaining, 21120);
				c.getPA().sendFrame126("Players in Game: " + count, 21121);
				c.getPA().sendFrame126("Blue Players Killed " + redKilled,
						21122);
				c.getPA().sendFrame126(
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
			c.inKBDWarsGame = false;
			c.getPA().removeAllWindows();
			c.getPA().movePlayer(Config.START_LOCATION_X,
					Config.START_LOCATION_Y, 0);
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
			c.getItems().updateSlot(3);
			c.getPA().resetAnimation();
		}
		for (Client c : redTeam.keySet()) {
			if (c == null) {
				continue;
			}
			c.getCombat().resetPlayerAttack();
			c.resetWalkingQueue();
			c.inClanWarsGame = false;
			c.getPA().removeAllWindows();
			c.getPA().movePlayer(Config.START_LOCATION_X,
					Config.START_LOCATION_Y, 0);
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
		c.inKBDWarsGame = false;
		c.absX = 2440;
		c.absY = 3088;
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
		c.inKBDWarsWait = false;
		c.absX = Config.START_LOCATION_X;
		c.absY = Config.START_LOCATION_Y;
	}

	/*
	 * Player Leaves the Wait Room via Portal
	 */
	public void removeWait(Client c) {
		if (!c.inKBDWarsGame) {
			if (redWait.containsKey(c)) {
				redWait.remove(c);
			} else if (blueWait.containsKey(c)) {
				blueWait.remove(c);
			}
			c.inKBDWarsWait = false;
			c.getPA().movePlayer(Config.START_LOCATION_X,
					Config.START_LOCATION_Y, 0);
		}
	}

}
