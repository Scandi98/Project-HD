package server.model.players.skills.agility;

import server.Config;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.model.players.Player;

public class GnomeCourse {

	/**
	 * Author: Poesy700
	 */

	/**
	 * Main method
	 */
	
	public static boolean wearingGraceful(Client c) {
		return c.playerEquipment[c.playerHat] == 11850
				&& c.playerEquipment[c.playerCape] == 11852
				&& c.playerEquipment[c.playerChest] == 11854
				&& c.playerEquipment[c.playerLegs] == 11856
				&& c.playerEquipment[c.playerHands] == 11858
				&& c.playerEquipment[c.playerFeet] == 11860;
	}
	
	public static int getExtraXP(Client c) {
		if (wearingGraceful(c))
			return 2;
		else
			return 1;
	}

	public static void handleObject(int objectId, Client p) {
		if (!isObstacle(objectId))
			return;
		switch (objectId) {

		case 23145:
			handleLog(p);
			break;

		case 23134:
			handleNet1(p);
			break;

		case 23559:
			handleBranch1(p);
			break;

		case 23557:
			handleRope(p);
			break;

		case 23560:
		//case 23560:
			handleBranch2(p);
			break;

		case 23135:
			handleNet2(p);
			break;

		case 23138:
		case 23139:
			handlePipe(p);
			break;

		}
	}

	/**
	 * Restores the player details after doing the obstacle
	 */

	public static void resetAgilityWalk(final Client c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}

	/**
	 * Moves the player to a coordinate with a asigned animation
	 */

	private static void specialMove(final Client c, final int walkAnimation,
			final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo3(x, y);
	}

	/**
	 * Checks if its a obstacle
	 */

	public static boolean isObstacle(int i) {
		switch (i) {
		case 23145: // log
		case 23134: // net1
		case 23559: // branch1
		case 23557: // rope
		case 23560: // branch2
		case 2315: // branch2
		case 23135: // net2
		case 23138: // pipe left
		case 23139: // pipe right
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has passed all obstacles
	 */

	public static boolean isFinished(Client p) {
		if (p.finishedLog && p.finishedNet1 && p.finishedBranch1
				&& p.finishedRope && p.finishedBranch2 && p.finishedNet2
				&& p.finishedPipe) {
			return true;
		}
		return false;
	}

	/**
	 * Obstacle methods
	 */

	public static void handleLog(final Client p) {
		p.doingAgility = true;
		specialMove(p, 762, 0, -7);
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (p.absY == 3429) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				resetAgilityWalk(p);
				p.finishedLog = true;
				p.doingAgility = false;
			}
		}, 1);
	}

	public static void handleNet1(final Client p) {
		p.startAnimation(828);
		p.getPA().movePlayer(p.absX, 3424, 1);
		p.getPA().addSkillXP((int) 7.5 * Config.AGILITY_EXPERIENCE,
				p.playerAgility);
		p.finishedNet1 = true;
	}

	public static void handleBranch1(final Client p) {
		p.startAnimation(828);
		p.getPA().movePlayer(2473, 3420, 2);
		p.getPA().addSkillXP(5 * Config.AGILITY_EXPERIENCE, p.playerAgility);
		p.finishedBranch1 = true;
	}

	public static void handleRope(final Client p) {
		if (p.absX != 2477 && p.absY != 3420 && p.heightLevel != 2) {
			return;
		}
		p.doingAgility = true;
		specialMove(p, 762, 6, 0);
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (p.absX == 2483) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				resetAgilityWalk(p);
				p.getPA().addSkillXP(7 * Config.AGILITY_EXPERIENCE,
						p.playerAgility);
				p.finishedRope = true;
				p.doingAgility = false;
			}
		}, 1);
	}

	public static void handleBranch2(final Client p) {
		p.startAnimation(828);
		p.getPA().movePlayer(p.absX, p.absY, 0);
		p.getPA().addSkillXP(5 * Config.AGILITY_EXPERIENCE, p.playerAgility);
		p.finishedBranch2 = true;
	}

	public static void handleNet2(final Client p) {
		if (p.absY != 3425) {
			return;
		}
		if (!p.finishedNet2) {
			p.doingAgility = true;
			p.startAnimation(828);
			CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					p.getPA().movePlayer(p.absX, 3427, 0);
					container.stop();
				}

				@Override
				public void stop() {
					p.turnPlayerTo(p.absX, 3426);
					p.getPA().addSkillXP(8 * Config.AGILITY_EXPERIENCE,
							p.playerAgility);
					p.finishedNet2 = true;
					p.doingAgility = false;
				}
			}, 1);
		} else {
			p.sendMessage("You have already completed this Obstacle");
		}
	}

	public static void handlePipe(final Client p) {
		if (p.absX != 2484 && p.absY != p.objectY - 1) {
			p.getPA().walkTo(2484 - p.absX, (p.objectY - 1) - p.absY);
			return;
		}
		if (p.absX == 2484 && p.absY == 3430) {
			p.doingAgility = true;
			specialMove(p, 844, 0, 7);
			CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (p.absY == 3437) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					p.startAnimation(844);
					resetAgilityWalk(p);
					p.finishedPipe = true;
					if (isFinished(p)) {
						p.getPA().addSkillXP(25000 * getExtraXP(p), p.playerAgility);
						p.sendMessage("You have completed the full gnome agility course.");
						p.sendMessage("You have been rewarded with 25k Agility XP!");
						if (p.getItems().hasFreeSlots(1)) {
							if (p.playerRights == 6 || p.ironDonator(p.playerName))
								p.getItems().addItem(11849, 2);
							else
								p.getItems().addItem(11849, 1);
						}
					} else {
						p.getPA().addSkillXP(7 * Config.AGILITY_EXPERIENCE,
								p.playerAgility);
					}
					p.finishedLog = false;
					p.finishedNet1 = false;
					p.finishedBranch1 = false;
					p.finishedRope = false;
					p.finishedBranch2 = false;
					p.finishedNet2 = false;
					p.finishedPipe = false;
				}
			}, 1);
		}
	}

}
