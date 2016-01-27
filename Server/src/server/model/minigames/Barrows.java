package server.model.minigames;

import server.Server;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.util.Misc;

/**
 * The Barrows Minigame
 * 
 * @author Tyler
 */
public class Barrows {

	public Barrows(Client c) {
		this.c = c;
	}

	private Client c;

	/**
	 * Variables used for reward.
	 */
	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4759, 2775 };
	public static int Runes[] = { 554, 555, 556, 557, 558, 559, 560, 561, 562,
			563, 564, 565, 4740 };
	public static int Pots[] = { 121, 123, 125, 127, 119, 2428, 2430, 2434,
			2432, 2444 };
	public boolean cantWalk;

	/**
	 * Getting random barrow pieces.
	 * 
	 * @return
	 */
	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	/**
	 * Getting random runes.
	 * 
	 * @return
	 */
	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	/**
	 * Getting random pots.
	 * 
	 * @return
	 */
	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * All of the barrow data.
	 */
	public static int[][] barrowData = {
	/** ID Coffin X Y Stair X Y */
	{ 2026, 6771, 3556, 9716, 6703, 3574, 3297 }, /** Dharoks */
	{ 2030, 6823, 3575, 9706, 6707, 3557, 3297 }, /** Veracs */
	{ 2025, 6821, 3557, 9700, 6702, 3565, 3288 }, /** Ahrims */
	{ 2029, 6772, 3568, 9685, 6706, 3554, 3282 }, /** Torags */
	{ 2027, 6773, 3537, 9703, 6704, 3577, 3282 }, /** Guthans */
	{ 2028, 6822, 3549, 9682, 6705, 3566, 3275 } /** Karils */
	};

	/**
	 * All of the spade data
	 */
	public int[][] spadeData = {
	/** X Y X1 Y1 toX toY */
	{ 3553, 3301, 3561, 3294, 3578, 9706 },
			{ 3550, 3287, 3557, 3278, 3568, 9683 },
			{ 3561, 3292, 3568, 3285, 3557, 9703 },
			{ 3570, 3302, 3579, 3293, 3556, 9718 },
			{ 3571, 3285, 3582, 3278, 3534, 9704 },
			{ 3562, 3279, 3569, 3273, 3546, 9684 }, };

	/**
	 * Spade digging data
	 */
	public void spadeDigging() {
		if (c.inArea(spadeData[0][0], spadeData[0][1], spadeData[0][2],
				spadeData[0][3])) {
			c.getPA().movePlayer(spadeData[0][4], spadeData[0][5], 3);
		} else if (c.inArea(spadeData[1][0], spadeData[1][1], spadeData[1][2],
				spadeData[1][3])) {
			c.getPA().movePlayer(spadeData[1][4], spadeData[1][5], 3);
		} else if (c.inArea(spadeData[2][0], spadeData[2][1], spadeData[2][2],
				spadeData[2][3])) {
			c.getPA().movePlayer(spadeData[2][4], spadeData[2][5], 3);
		} else if (c.inArea(spadeData[3][0], spadeData[3][1], spadeData[3][2],
				spadeData[3][3])) {
			c.getPA().movePlayer(spadeData[3][4], spadeData[3][5], 3);
		} else if (c.inArea(spadeData[4][0], spadeData[4][1], spadeData[4][2],
				spadeData[4][3])) {
			c.getPA().movePlayer(spadeData[4][4], spadeData[4][5], 3);
		} else if (c.inArea(spadeData[5][0], spadeData[5][1], spadeData[5][2],
				spadeData[5][3])) {
			c.getPA().movePlayer(spadeData[5][4], spadeData[5][5], 3);
		}
	}

	/**
	 * Stair data
	 */
	public void useStairs() {
		switch (c.objectId) {
		//veracs 6707, ahrims, 6702, torag 6706, karil 6705, guthan 6704, dharok 6703
		case 20668:
			c.getPA().movePlayer(barrowData[0][5], barrowData[0][6], 0);
			break;
		case 20672:
			c.getPA().movePlayer(barrowData[1][5], barrowData[1][6], 0);
			break;
		case 20667:
			c.getPA().movePlayer(barrowData[2][5], barrowData[2][6], 0);
			break;
		case 20671:
			c.getPA().movePlayer(barrowData[3][5], barrowData[3][6], 0);
			break;
		case 20669:
			c.getPA().movePlayer(barrowData[4][5], barrowData[4][6], 0);
			break;
		case 20670:
			c.getPA().movePlayer(barrowData[5][5], barrowData[5][6], 0);
			break;
		}
	}

	public void checkCoffins() {
		if (c.barrowsKillCount < 5) {
			c.getPA().removeAllWindows();
			c.sendMessage("Finish killing these brothers to get to the chest room!");
			if (c.barrowsNpcs[2][1] == 0) {
				c.sendMessage("- Karils");
			}
			if (c.barrowsNpcs[3][1] == 0) {
				c.sendMessage("- Guthans");
			}
			if (c.barrowsNpcs[1][1] == 0) {
				c.sendMessage("- Torags");
			}
			if (c.barrowsNpcs[5][1] == 0) {
				c.sendMessage("- Ahrims");
			}
			if (c.barrowsNpcs[0][1] == 0) {
				c.sendMessage("- Veracs");
			}
			c.getPA().removeAllWindows();
		} else if (c.barrowsKillCount == 5) {
			c.getPA().movePlayer(3551, 9694, 0);
			c.sendMessage("You teleport to the chest.");
			// Server.npcHandler.spawnNpc(c, 2026, c.getX(), c.getY()-1, 3, 0,
			// 120, 25, 200, 200, true, true);
			// c.getPA().removeAllWindows();
		} else if (c.barrowsKillCount > 5) {
			// c.getPA().movePlayer(3551, 9694, 0);
			// c.sendMessage("You teleport to the chest.");
			c.getPA().removeAllWindows();
		}
	}

	/**
	 * Grabs the reward based on random chance depending on what your killcount
	 * is.
	 */
	public void reward() {
		c.getItems().addItem(randomRunes(), Misc.random(150) + 100);
		c.getItems().addItem(randomRunes(), Misc.random(150) + 100);
		c.getItems().addItem(randomPots(), 1);
		if (c.barrowsKillCount >= 6) {
			if (c.playerEquipment[c.playerRing] == 2752) {
				if (Misc.random(5) - 1 == 1) {
					int randomBarrows1 = randomBarrows();
					c.getItems().addItem(randomBarrows1, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Client c2 = (Client) PlayerHandler.players[j];
							c.sendMessage("[@red@SERVER@bla@] " + c.playerName
									+ " has received 1x @pur@"
									+ c.getItems().getItemName(randomBarrows1)
									+ "@bla@.");
						}
					}
				}
				if (Misc.random(25) - 1 == 14) {
					int randomBarrows2 = randomBarrows();
					c.getItems().addItem(randomBarrows2, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Client c2 = (Client) PlayerHandler.players[j];
							c.sendMessage("[@red@SERVER@bla@] " + c.playerName
									+ " has received 1x @pur@"
									+ c.getItems().getItemName(randomBarrows2)
									+ "@bla@.");
						}
					}
				}
			} else {
				if (Misc.random(5) == 1) {
					int randomBarrows1 = randomBarrows();
					c.getItems().addItem(randomBarrows1, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Client c2 = (Client) PlayerHandler.players[j];
							c.sendMessage("[@red@SERVER@bla@] " + c.playerName
									+ " has received 1x @pur@"
									+ c.getItems().getItemName(randomBarrows1)
									+ "@bla@.");
						}
					}
				}
				if (Misc.random(25) == 14) {
					int randomBarrows2 = randomBarrows();
					c.getItems().addItem(randomBarrows2, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Client c2 = (Client) PlayerHandler.players[j];
							c.sendMessage("[@red@SERVER@bla@] " + c.playerName
									+ " has received 1x @pur@"
									+ c.getItems().getItemName(randomBarrows2)
									+ "@bla@.");
						}
					}
				}
			}
		}
		c.barrowsChests++;
		c.sendMessage("@red@You have completed " + c.barrowsChests
				+ " Barrows Chests.");
	}

	/**
	 * Checking if you have killed all of the brothers.
	 * 
	 * @return
	 */
	public boolean checkBarrows() {
		if (c.barrowsNpcs[2][1] == 2 || c.barrowsNpcs[3][1] == 2
				|| c.barrowsNpcs[1][1] == 2 || c.barrowsNpcs[5][1] == 2
				|| c.barrowsNpcs[0][1] == 2 || c.barrowsNpcs[4][1] == 2) {
			return true;
		}
		return false;
	}

	/**
	 * Using the chest.
	 */
	public void useChest() {
		if (c.barrowsKillCount == 5) {
			if (c.barrowsNpcs[4][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1673, c.getX(), c.getY() - 1, 0,
						0, 120, 25, 200, 200, true, true);
			}
			c.barrowsNpcs[4][1] = 1;
			c.sendMessage("Kill the last brother for your reward!");
		}
		/*
		 * if (c.barrowsKillCount == 5) { if (c.barrowsNpcs[4][1] == 0) {
		 * Server.npcHandler.spawnNpc(c, 2026, c.getX(), c.getY()-1, 0, 0, 120,
		 * 25, 200, 200, true, true); } c.barrowsNpcs[4][1] = 1; }
		 */
		if (c.barrowsKillCount > 5 && checkBarrows()) {
			if (c.getItems().freeSlots() >= 4) {
				reward();
				resetBarrows();
			} else {
				c.sendMessage("You need more inventory slots to open the chest.");
			}
		}
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
					totalCost);
	}

	/**
	 * Resetting the minigame.
	 */
	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.getPA().sendNewString("Karils", 16135);
		c.getPA().sendNewString("Guthans", 16134);
		c.getPA().sendNewString("Torags", 16133);
		c.getPA().sendNewString("Ahrims", 16132);
		c.getPA().sendNewString("Veracs", 16131);
		c.getPA().sendNewString("Dharoks", 16130);
		c.getPA().movePlayer(3565, 3305, 0);
	}

}