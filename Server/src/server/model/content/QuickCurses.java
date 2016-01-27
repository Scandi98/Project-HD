package server.model.content;

import server.model.players.Client;

/**
 *
 * @author g0d 0f war 
 * Old class I decided to upgrade
 */

public class QuickCurses {

	public static final int MAX_CURSES = 20;
	private static final int[][] data = { 	{67050, 630, 0}, {67051, 631, 1}, {67052, 632, 2}, {67053, 633, 3},
											{67054, 634, 4}, {67055, 635, 5}, {67056, 636, 6}, {67057, 637, 7},
											{67058, 638, 8}, {67059, 639, 9}, {67060, 640, 10}, {67061, 641, 11},
											{67062, 642, 12}, {67063, 643, 13}, {67064, 644, 14}, {67065, 645, 15},
											{67066, 646, 16}, {67067, 647, 17}, {67068, 648, 18}, {67069, 649, 19}
										};//actionID, frameID, quickCurse = true

	public static void clickCurse(Client c, int actionId) {
		canBeSelected(c, actionId);
		for (int j = 0; j < data.length; j++) {
			if (data[j][0] == actionId) {
				if (c.quickCurses[data[j][2]]) {
					c.quickCurses[data[j][2]] = false;
					c.getPA().sendFrame36(data[j][1], 0);
				} else {
					c.quickCurses[data[j][2]] = true;
					c.getPA().sendFrame36(data[j][1], 1);
				}
			}
		}
	}
	
	public static void loadCheckMarks(Client player) {
		for (int j = 0; j < data.length; j++)
			player.getPA().sendFrame36(data[j][1], player.quickCurses[data[j][2]] ? 1 : 0);
	}

	public static void canBeSelected(Client c, int actionId) {
		boolean[] curse = new boolean[MAX_CURSES];
		for (int j = 0; j < curse.length; j++)
			curse[j] = true;
		switch (actionId) {
			case 67051:
				curse[10] = false;
				curse[19] = false;
			break;

			case 67052:
				curse[11] = false;
				curse[19] = false;
			break;

			case 67053:
				curse[12] = false;
				curse[19] = false;
			break;

			case 67054:
				curse[16] = false;
			break;

			case 67057:
				curse[8] = false;
				curse[9] = false;
				curse[17] = false;
				curse[18] = false;
			break;

			case 67058:
				curse[7] = false;
				curse[9] = false;
				curse[17] = false;
				curse[18] = false;
			break;

			case 67059:
				curse[7] = false;
				curse[8] = false;
				curse[17] = false;
				curse[18] = false;
			break;

			case 67060:
				curse[1] = false;
				curse[19] = false;
			break;

			case 67061:
				curse[2] = false;
				curse[19] = false;
			break;

			case 67062:
				curse[3] = false;
				curse[19] = false;
			break;

			case 67063:
			case 67064:
			case 67065: //Leech run-energy
				curse[19] = false;
			break;

			case 67066: //Leech special
				curse[4] = false;
				curse[19] = false;
			break;

			case 67067:
				for (int i = 7; i < 10; i++)
					curse[i] = false;
				curse[18] = false;
			break;

			case 67068:
				for (int i = 7; i < 10; i++)
					curse[i] = false;
				curse[17] = false;
			break;

			case 67069:
				for (int i = 1; i < 5; i++) {
					for (int j = 10; j < 17; j++) {
						curse[i] = false;
						curse[j] = false;
					}
				}
			break;
		}
		for (int i = 0; i < MAX_CURSES; i++) {
			if (!curse[i]) {
				c.quickCurses[i] = false;
				for (int j = 0; j < data.length; j++) {
					if (i == data[j][2])
						c.getPA().sendFrame36(data[j][1], 0);
				}
			}
		}
	}

	public static void turnOnQuicks(Client c) {
			for (int i = 0; i < c.quickPrayers.length; i++) {
				if (c.quickPrayers[i] && !c.prayerActive[i]){
					c.quickPray = true;
					c.quickPrayersOn = true;
					c.getCombat().activatePrayer(i);
					c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
					c.getPA().sendNewString(":quicks:on", -1);
					c.updateRequired = true;
					c.appearanceUpdateRequired = true;
				}
				if (!c.quickPrayers[i]) {
					c.prayerActive[i] = false;
					c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
					c.getPA().requestUpdates();
				}
			}			
	}

	public static void turnOffQuicks(Client c) {	
		c.getCombat().resetPrayers();
		//c.getCurse().resetCurse();
		c.getPA().sendNewString(":quicks:off", -1);
		c.quickPray = false;
		c.quickCurse = false;
		c.headIcon = -1;
		c.getPA().requestUpdates();
		c.quickPrayersOn = false;
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
	}

	public static void selectQuickInterface(Client c) {
		QuickPrayers.loadCheckMarks(c);
		c.setSidebarInterface(5, 17200);
		c.getPA().sendFrame106(5);
	}

	public static void clickConfirm(Client c) {
		c.setSidebarInterface(5, 5608);
	}
}