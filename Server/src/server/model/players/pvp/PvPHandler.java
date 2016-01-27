package server.model.players.pvp;

import server.model.players.Client;

public class PvPHandler {
	
	private Client c;
	
	public PvPHandler(Client c) {
		this.c = c;
	}
	
	/*
	 * Variables
	 */
	public int lowLevel;
	public int highLevel;
	
	/*
	 * Handles Showing of Correct PvP Levels
	 */
	public void pvpLevels() {
		if (c.combatLevel < 15) {
			int lowLevel = 3;
			int highLevel = c.combatLevel + 12;
			if(c.inPvP()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21302);
			} else if(c.inSafeZone()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21202);
			}
		}
		if (c.combatLevel > 15 && c.combatLevel < 114) {
			int lowLevel = c.combatLevel - 12;
			int highLevel = c.combatLevel + 12;
			if(c.inPvP()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21302);
			} else if(c.inSafeZone()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21202);
			}
		}
		if (c.combatLevel > 114) {
			int lowLevel = c.combatLevel - 12;
			int highLevel = 126;
			if(c.inPvP()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21302);
			} else if(c.inSafeZone()) {
				c.getPA().sendFrame126(lowLevel + " - " + highLevel, 21202);
			}
		}
	}

	public static int[][] pvpPointsNeeded = {
		{11694, 30}, {11696, 15}, {11698, 15}, {11700, 15},
		{13362, 50}, {13358, 50}, {13360, 50}, {13355, 50},
		{13354, 50}, {13352, 50}, {13350, 50}, {13348, 50},
		{13346, 50}, {14484, 30}, {18335, 15}, {18349, 60},
		{18351, 60}, {18353, 60}, {18355, 60}, {18357, 60},
		{19669, 15}, {15486, 30}, {13858, 30}, {13861, 30},
		{13864, 30}, {13867, 30}, {13870, 30}, {13873, 30},
		{13876, 30}, {13884, 30}, {13887, 30}, {13890, 30},
		{13893, 30}, {13896, 30}, {13899, 30}, {13902, 30}
	};
	
	public static int[][] pvpKillsNeeded = {
		{11718, 15}, {11720, 15}, {11722, 15},{11724, 15}, {11726, 15}
	};
}