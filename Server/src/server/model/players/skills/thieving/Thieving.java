package server.model.players.skills.thieving;

import server.Config;
import server.Server;
import server.util.Misc;
import server.event.test.CycleEvent;
import server.event.test.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

/**
 * Thieving.java
 * 
 * @author Sanity
 * 
 **/

public class Thieving {

	private Client c;

	public Thieving(Client c) {
		this.c = c;
	}

	public void stealFromNPC(int id) {
		if (System.currentTimeMillis() - c.lastThieve < 2000)
			return;
		for (int j = 0; j < npcThieving.length; j++) {
			if (npcThieving[j][0] == id) {
				if (c.playerLevel[c.playerThieving] >= npcThieving[j][1]) {
					if (Misc.random(c.playerLevel[c.playerThieving] + 2
							- npcThieving[j][1]) != 1) {
						c.getPA().addSkillXP(
								npcThieving[j][2] * Config.THIEVING_EXPERIENCE,
								c.playerThieving);
						c.getItems().addItem(995, npcThieving[j][3]);
						c.startAnimation(881);
						c.lastThieve = System.currentTimeMillis();
						c.sendMessage("You thieve some money...");
						break;
					} else {
						c.setHitDiff(npcThieving[j][4]);
						c.setHitUpdateRequired(true);
						c.playerLevel[3] -= npcThieving[j][4];
						c.getPA().refreshSkill(3);
						c.lastThieve = System.currentTimeMillis() + 2000;
						c.sendMessage("You fail to thieve the NPC.");
						break;
					}
				} else {
					c.sendMessage("You need a thieving level of "
							+ npcThieving[j][1] + " to thieve from this NPC.");
				}
			}
		}
	}

	public static void stealFromStall(final Client c, final int id, int amount,
			int xp, int level, final int i, final int x, final int y,
			final int face) {
		if (System.currentTimeMillis() - c.lastThieve < 3000)
			return;
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0) {
			c.sendMessage("You can't steal from a stall while in combat!");
			return;
		}
		if (c.playerLevel[c.playerThieving] >= level) {
			if (c.getItems().addItem(id, amount)) {
				c.startAnimation(832);
				if (c.inPvP() && c.getWealth() > 500000) {
					c.getPA().addSkillXP(2 * (xp * 40), c.playerThieving);
				} else {
					c.getPA().addSkillXP(xp * 40, c.playerThieving);
				}
				c.lastThieve = System.currentTimeMillis();
				c.turnPlayerTo(c.objectX, c.objectY);
				c.sendMessage("You steal some coins.");

				/*Server.gameObjectManager.createAnObject(c, 634, x, y, face);

				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						Server.gameObjectManager.createAnObject(c, i, x, y, face);
						container.stop();

					}

					@Override
					public void stop() {

					}
				}, getRespawnTime(c, i));*/
			}
		} else {
			c.sendMessage("You must have a thieving level of " + level
					+ " to thieve from this stall.");
		}
	}

	private static int getRespawnTime(Client c, int i) {
		switch (i) {
		case 2561:
			return 3; // BAKER
		case 2560:
			return 12; // SILK
		case 7053:
			return 17; // SEED
		case 2563:
			return 5; // FUR
		case 2565:
			return 5; // SILVER
		case 2564:
			return 10; // SPICE
		case 2562:
			return 20; // GEM
		case 14011:
			return 5; // JUG
		default:
			return 5;
		}
	}

	// npc, level, exp, coin amount
	public int[][] npcThieving = { { 1, 1, 8, 200, 1 }, { 18, 25, 26, 500, 1 },
			{ 9, 40, 47, 1000, 2 }, { 26, 55, 85, 1500, 3 },
			{ 20, 70, 152, 2000, 4 }, { 21, 80, 273, 3000, 5 } };

}