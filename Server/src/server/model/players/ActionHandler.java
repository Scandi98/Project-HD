package server.model.players;

import server.Config;
import server.Server;
import server.core.World;
import server.model.content.instance.InstanceManager;
import server.model.minigames.CrystalChest;
import server.model.minigames.PestControl;
import server.model.npcs.pet.Pet;
import server.model.players.Attributes.A;
import server.model.players.skills.agility.GnomeCourse;
import server.model.players.skills.fishing.Fishing;
import server.model.players.skills.mining.Mining;
import server.model.players.skills.thieving.Thieving;
import server.model.players.skills.woodcutting.ChopTree;
import server.model.players.skills.woodcutting.ChopTree.Tree;
import server.region.Region;
import server.tick.Tickable;
import server.util.Misc;
import server.util.ScriptManager;
import server.model.objects.Object;

public class ActionHandler {

	private Client c;

	public ActionHandler(Client Client) {
		this.c = Client;
	}

	public void firstClickObject(final int objectType, final int obX,
			final int obY) {
		c.clickObjectType = 0;
		/*
		 * if (c.getAgility().agilityObstacle(c, objectType)) {
		 * c.getAgility().agilityCourse(c, objectType); }
		 */

		if (c.playerRights == 3) {
			c.sendMessage("Object type: " + objectType);
		}
		c.objectX = obX;
		c.objectY = obY;
		// System.out.println(objectType);
		// Server.castleWars.handleObjects(c, objectType, obX, obY);
		if (c.agilityEmote)
			return;
		/*final Obstacle obstacle = Obstacle.forLocation(c.objectX, c.objectY, c.heightLevel);
		if (obstacle != null) {
			Agility.tackleObstacle(c, obstacle, objectType);
		}*/
		if (InstanceManager.instanceManager.canCreateInstance(c, objectType)) {
			InstanceManager.createInstance(c, objectType);
			return;
		}
		World.getWorld().submit(new Tickable(1) {

			@Override
			public void execute() {
				if (Tree.getTree(objectType) != null) {
					ChopTree.handle(c, objectType, obX, obY);
					this.stop();
					return;
				}
			}
		});
		if (Mining.miningRocks(c, objectType)) {
			Mining.attemptData(c, objectType, obX, obY);
			return;
		}
		//Agility.tackleObstacle(c, objectType);
		GnomeCourse.handleObject(objectType, c);
		switch (objectType) {
		case 7158:
			if (c.absX == 3252) {
				c.getPA().movePlayer(c.absX + 1, c.absY, c.heightLevel);
			} else if (c.absX == 3253) {
				c.getPA().movePlayer(c.absX - 1, c.absY, c.heightLevel);
			}
			break;
		case 7160:
			if (c.absX == 3252) {
				c.getPA().movePlayer(c.absX + 1, c.absY, c.heightLevel);
			} else if (c.absX == 3253) {
				c.getPA().movePlayer(c.absX - 1, c.absY, c.heightLevel);
			}
			break;
		case 26645:
			/*if (c.hasTick(A.CLAN_WARS_TROLL))
				return;
			c.getDH().sendStatement("Entering Clan Wars....");
			c.submitTick(A.CLAN_WARS_TROLL, new Tickable(2) {
				public void execute() {
					if (getTickDelay() == 3) {
						c.getDH().sendStatement("... When It Comes Out.");
						stop();
						return;
					}
					c.getPA().fadeInterface();
					setTickDelay(3);
				}
			});*/
			c.getPA().movePlayer(3327 + Misc.random(1), 4751, 0);
			break;
		case 2633:
		//	Zulrah.init(c);
			break;
			
		case 26646:
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			c.sendMessage("You recharge your prayer points and health.");
			c.getPA().refreshSkill(5);
			c.getPA().refreshSkill(3);
			c.getPA().movePlayer(2344 - Misc.random(1), 3801, 0);
			break;
		case 17384:
			if (c.playerRights >= 1 && c.playerRights <= 6 || c.playerRights == 9 || c.playerRights == 8 || c.ironDonator(c.playerName)) {
				c.getPA().movePlayer(2336, 9804, 0);
			}
			break;
		case 366:
			if (c.startedEvent) {
				if (c.egg6Collected) {
					c.sendMessage("You have already collected this egg.");
				} else {
					c.startAnimation(881);
					if (c.getItems().freeSlots() > 0) {
						c.sendMessage("You have found the Easter Egg!");
						c.egg6Collected = true;
						c.eggsCollected += 1;
						if (c.eggsCollected == 6) {
							c.easterStage = 2;
							c.sendMessage("You have Collected all 6 Easter Eggs, Please speak to the Easter Bunny");
						}
						c.getItems().addItem(7933, 1);
					} else {
						c.sendMessage("You don't have enough Inventory Space to get the Egg");
					}
				}
			}
			break;
		case 357:
			if (c.objectX == 2570 && c.objectY == 3096) {
				if (c.startedEvent) {
					c.startAnimation(881);
					c.sendMessage("You search the box.. But find nothing");
				}
			} else if (c.objectX == 2569 && c.objectY == 3094) {
				if (c.startedEvent) {
					if (c.egg5Collected) {
						c.sendMessage("You have already collected this egg.");
					} else {
						c.startAnimation(881);
						if (c.getItems().freeSlots() > 0) {
							c.sendMessage("You have found the Easter Egg!");
							c.egg5Collected = true;
							c.eggsCollected += 1;
							if (c.eggsCollected == 6) {
								c.easterStage = 2;
								c.sendMessage("You have Collected all 6 Easter Eggs, Please speak to the Easter Bunny");
							}
							c.getItems().addItem(7932, 1);
						} else {
							c.sendMessage("You don't have enough Inventory Space to get the Egg");
						}
					}
				}
			} else {
				if (c.startedEvent) {
					if (c.egg2Collected) {
						c.sendMessage("You have already collected this egg.");
					} else {
						c.startAnimation(881);
						if (c.getItems().freeSlots() > 0) {
							c.sendMessage("You have found the Easter Egg!");
							c.egg2Collected = true;
							c.eggsCollected += 1;
							if (c.eggsCollected == 6) {
								c.easterStage = 2;
								c.sendMessage("You have Collected all 6 Easter Eggs, Please speak to the Easter Bunny");
							}
							c.getItems().addItem(7929, 1);
						} else {
							c.sendMessage("You don't have enough Inventory Space to get the Egg");
						}
					}
				}
			}
			break;
		case 361:
			if (c.objectX == 2667 && c.objectY == 3297) {
				if (c.startedEvent) {
					if (c.egg4Collected) {
						c.sendMessage("You have already collected this egg.");
					} else {
						c.startAnimation(881);
						if (c.getItems().freeSlots() > 0) {
							c.sendMessage("You have found the Easter Egg!");
							c.egg4Collected = true;
							c.eggsCollected += 1;
							if (c.eggsCollected == 6) {
								c.easterStage = 2;
								c.sendMessage("You have Collected all 6 Easter Eggs, Please speak to the Easter Bunny");
							}
							c.getItems().addItem(7931, 1);
						} else {
							c.sendMessage("You don't have enough Inventory Space to get the Egg");
						}
					}
				}
			} else {
				if (c.startedEvent) {
					c.startAnimation(881);
					c.sendMessage("You search the box.. But find nothing");
				}
			}
			break;
		case 25775:
			if (c.startedEvent) {
				if (c.egg3Collected) {
					c.sendMessage("You have already collected this egg.");
				} else {
					c.startAnimation(881);
					if (c.getItems().freeSlots() > 0) {
						c.sendMessage("You have found the Easter Egg!");
						c.egg3Collected = true;
						c.eggsCollected += 1;
						c.getItems().addItem(7930, 1);
					} else {
						c.sendMessage("You don't have enough Inventory Space to get the Egg");
					}
				}
			}
			break;
		case 17101:
			if (c.startedEvent && c.objectX == 3216 && c.objectY == 3419) {
				if (c.egg1Collected) {
					c.sendMessage("You have already collected this egg.");
				} else {
					c.startAnimation(881);
					if (c.getItems().freeSlots() > 0) {
						c.sendMessage("You have found the Easter Egg!");
						c.egg1Collected = true;
						c.eggsCollected += 1;
						c.getItems().addItem(7928, 1);
					} else {
						c.sendMessage("You don't have enough Inventory Space to get the Egg");
					}
				}
			}
			break;
		case 17100:
		case 24206:
		case 24208:
		case 358:
		case 355:
		case 354:
		case 359:
		case 356:
		case 16564:
			if (c.startedEvent) {
				c.startAnimation(881);
				c.sendMessage("You search the box.. But find nothing");
			}
			break;
		/*case 23261:
		case 23267:
		case 23268:
		case 23262:
		case 23265:
		case 23263:
		case 23264:
		case 23266:*/
		case 23271:
			c.getPA().ditchJump();
			break;
		case 3515:
			if (c.playerLevel[0] >= 98 && c.playerLevel[1] >= 98
					&& c.playerLevel[2] >= 98 && c.playerLevel[3] >= 98
					&& c.playerLevel[4] >= 98 && c.playerLevel[5] >= 98
					&& c.playerLevel[6] >= 98 && c.playerLevel[7] >= 98
					&& c.playerLevel[8] >= 98 && c.playerLevel[9] >= 98
					&& c.playerLevel[10] >= 98 && c.playerLevel[11] >= 98
					&& c.playerLevel[12] >= 98 && c.playerLevel[13] >= 98
					&& c.playerLevel[14] >= 98 && c.playerLevel[15] >= 98
					&& c.playerLevel[16] >= 98 && c.playerLevel[17] >= 98
					&& c.playerLevel[18] >= 98 && c.playerLevel[19] >= 98
					&& c.playerLevel[20] >= 99) {
				c.getDH().sendDialogues(3515, 1);
			} else {
				c.sendMessage("Come back when you have achieved maxed stats!");
			}
			break;
		case 2566:
			c.bankCheck = true;
			if (c.absY == obY - 1) {
				c.getPA().openUpBank(c.bankingTab);

			}
			break;
		case 11735:
			c.bankCheck = true;
			c.getPA().openUpBank(c.bankingTab);
			break;
		case 2557:
			if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
				c.sendMessage("You may not enter this Bank while teleblocked.");
				return;
			}
			if (System.currentTimeMillis() - c.doorDelay < c.doorDelayLength) {
				return;
			}
			if (System.currentTimeMillis() - c.doorDelay > c.doorDelayLength) {
				c.doorDelay = System.currentTimeMillis();
				if (c.absX == 3190 && c.absY == 3957) {
					c.getPA().movePlayer(3190, 3958, c.heightLevel);
				} else if (c.absX == 3190 && c.absY == 3958) {
					c.getPA().movePlayer(3190, 3957, c.heightLevel);
				} else if (c.absX == 3191 && c.absY == 3962) {
					c.getPA().movePlayer(3191, 3963, c.heightLevel);
				} else if (c.absX == 3191 && c.absY == 3963) {
					c.getPA().movePlayer(3191, 3962, c.heightLevel);
				} else if (obX == 3190 && obY == 3957) {
					c.getPA().walkTo(3190, 3957);
				} else if (obX == 3191 && obY == 3963) {
					c.getPA().walkTo(3191, 3963);
				}
				c.doorDelayLength = 5000;
			}
			break;
		case 1551:
			if (c.absX == 3253) {
				c.getPA().movePlayer(3252, c.absY, 0);
			}
			break;
		case 11244:
			if (c.absX == 1823 && c.absY == 4835 && c.heightLevel == 2) {
				c.getPA().movePlayer(1823, 4833, 1);
			}
			break;
		case 11310:
			if (c.absX == 1823 && c.absY == 4833 && c.heightLevel == 1) {
				c.getPA().movePlayer(1823, 4835, 2);
			}
			break;
			
		case 16537:
				c.getPA().movePlayer(3424, 3550, 1);
			break;
			
		case 2114:
				c.getPA().movePlayer(3438, 3538, 1);
			break;
			
		case 2118:
				c.getPA().movePlayer(3438, 3538, 0);
			break;
			
		case 2119:
				c.getPA().movePlayer(3417, 3541, 2);
			break;
			
		case 2120:
				c.getPA().movePlayer(3417, 3541, 2);
			break;
			
		case 2100:
			if (c.absX == 3445 && c.absY == 3554) {
				c.getPA().movePlayer(3445, 3555, 2);
			} else if (c.absX == 3445 && c.absY == 3555) {
				c.getPA().movePlayer(3445, 3554, 2);
			}
			break;
			
		case 2102:
			if (c.absX == 3426 && c.absY == 3555) {
				c.getPA().movePlayer(3426, 3556, 1);
			} else if (c.absX == 3426 && c.absY == 3556) {
				c.getPA().movePlayer(3426, 3555, 1);
			}
			break;
			
		case 2104:
			if (c.absX == 3427 && c.absY == 3555) {
				c.getPA().movePlayer(3427, 3556, 1);
			} else if (c.absX == 3427 && c.absY == 3556) {
				c.getPA().movePlayer(3427, 3555, 1);
			}
			break;
		
		case 11289:
			if (c.absX == 1826 && c.absY == 4836) {
				c.getPA().movePlayer(1826, 4838, 2);
			} else if (c.absX == 1822 && c.absY == 4836) {
				c.getPA().movePlayer(1822, 4838, 2);
			}
			break;
		case 11290:
			if (c.absX == 1826 && c.absY == 4838) {
				c.getPA().movePlayer(1826, 4836, 1);
			} else if (c.absX == 1822 && c.absY == 4838) {
				c.getPA().movePlayer(1826, 4838, 1);
			}
			break;
		case 11309:
			if (c.absX == 1824 && c.absY == 4828 && c.heightLevel == 1) {
				c.getPA().movePlayer(1824, 4828, 0);
			}
			break;
		case 11308:
			if (c.absX == 1824 && c.absY == 4828 && c.heightLevel == 0) {
				c.getPA().movePlayer(1824, 4828, 1);
			}
			break;
		case 6481:
			c.getPA().movePlayer(3233, 9315, 0);
			break;
		case 2492:
			if (c.killCount >= 20) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin",
						"Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You need 20 kill count before teleporting to a boss chamber.");
			}
			break;

		case 1765:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(3067, 10256, 0);
			}
			break;
		case 2882:
		case 2883:
			if (c.objectX == 3268) {
				if (c.absX < c.objectX) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;
		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 1766:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(3016, 3849, 0);
			}
			break;
		case 6552:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 14911:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 2;
				c.setSidebarInterface(6, 29999);
				c.sendMessage("Lunar wisdom fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 1816:
			c.getPA().startTeleport2(2271, 4680, 0);
			break;
		case 1817:
			if (c.heightLevel == 0) {
				c.getPA().startTeleport(3006, 3848, 0, "modern");
			}
			break;
		case 1814:
			// ardy lever
			c.getPA().startTeleport(3153, 3923, 0, "modern");
			break;

		case 11833:
			c.getPA().enterCaves();
			// c.sendMessage("Temporarily removed due to bugs.");
			break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
			break;

		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
			break;

		case 11834:
			c.getPA().resetTzhaar();
			break;
		case 1553:
			c.getPA().movePlayer(3255, 3267, 0);
			break;
		case 8959:
			if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
				if (c.getPA().checkForPlayer(2490,
						c.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.objectX, c.objectY, c.heightLevel, 1,
							10, 8959, 15);
				}
			}
			break;

		case 2213:
		case 14367:
		case 3193:
		case 4483:
		case 26972:
		case 21301:
		case 11744:
		case 10661:
			c.bankCheck = true;
			c.getPA().openUpBank(0);
			break;
			
		case 75:
			if(c.getItems().playerHasItem(989)) {
				CrystalChest.searchChest(c, objectType, obX, obY);
			}
			break;

		case 10177:
			c.getPA().movePlayer(1890, 4407, 0);
			break;
		case 10230:
			c.getPA().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;
		case 2623:
			if (c.absX >= c.objectX)
				c.getPA().walkTo(-1, 0);
			else
				c.getPA().walkTo(1, 0);
			break;
		// pc boat
		case 14315:
			if (c.absX == 2657 && c.absY == 2639 && c.teleTimer <= 0)
				PestControl.addToWaitRoom(c);
			break;
		case 14314:
			if (c.inPcBoat())
				PestControl.leaveWaitingBoat(c);
			c.getPA().walkableInterface(-1);
			break;

		case 1596:
		case 1597:
			if (c.getY() >= c.objectY)
				c.getPA().walkTo3(0, -1);
			else
				c.getPA().walkTo3(0, 1);
			break;

		case 14235:
		case 14233:
			if (c.objectX == 2670)
				if (c.absX <= 2670)
					c.absX = 2671;
				else
					c.absX = 2670;
			if (c.objectX == 2643)
				if (c.absX >= 2643)
					c.absX = 2642;
				else
					c.absX = 2643;
			if (c.absX <= 2585)
				c.absY += 1;
			else
				c.absY -= 1;
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			// Server.objectHandler.startObelisk(objectType);
			Server.objectManager.startObelisk(objectType);
			break;
		case 4387:
			c.getPA().removeAllWindows();
			Server.clanWars.joinWait(c, 1);
			break;

		case 4388:
			c.getPA().removeAllWindows();
			Server.clanWars.joinWait(c, 2);
			break;

		case 4408:
			c.sendMessage("closed for now...");
			// c.getPA().removeAllWindows();
			// Server.clanWars.joinWait(c, 3);
			break;
		case 4389:
		case 4390:
			c.sendMessage("Closed for now....");
			// c.getPA().removeAllWindows();
			// Server.clanWars.removeWait(c);
			break;

		case 9369:
			if (c.getY() > 5175)
				c.getPA().movePlayer(2399, 5175, 0);
			else
				c.getPA().movePlayer(2399, 5177, 0);
			break;

		case 9368:
			if (c.getY() < 5169) {
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.getPA().movePlayer(2399, 5169, 0);
			}
			break;
		case 2632:
				c.getPA().movePlayer(3202, 3856, 0);
			break;
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4419:
		case 4420:
		case 4469:
		case 4470:
		case 4911:
		case 4912:
		case 1747:
		case 1757:
			// Server.castleWars.handleObjects(c, objectType, obX, obY);
			break;

		/*
		 * Doors
		 */
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 20667:
		case 20668:
		case 20669:
		case 20670:
		case 20671:
		case 20672:
			c.getBarrows().useStairs();
			break;
		case 20973:
			c.getBarrows().useChest();
			break;

		case 20772:
			if (c.barrowsNpcs[0][1] == 0) {
				if (c.WithinDistance(c.objectX, c.objectY, c.absX, c.absY, 3)) {
					Server.npcHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1,
							3, 0, 120, 25, 200, 200, true, true);
					c.barrowsNpcs[0][1] = 1;
				}
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20721:
			if (c.barrowsNpcs[1][1] == 0) {
				if (c.WithinDistance(c.objectX, c.objectY, c.absX, c.absY, 3)) {
					Server.npcHandler.spawnNpc(c, 1676, c.getX() + 1, c.getY(),
							3, 0, 120, 20, 200, 200, true, true);
					c.barrowsNpcs[1][1] = 1;
				}
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20771:
			if (c.barrowsNpcs[2][1] == 0) {
				if (c.WithinDistance(c.objectX, c.objectY, c.absX, c.absY, 3)) {
					Server.npcHandler.spawnNpc(c, 1675, c.getX(), c.getY() - 1,
							3, 0, 90, 17, 200, 200, true, true);
					c.barrowsNpcs[2][1] = 1;
				}
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20722:
			if (c.barrowsNpcs[3][1] == 0) {
				if (c.WithinDistance(c.objectX, c.objectY, c.absX, c.absY, 3)) {
					Server.npcHandler.spawnNpc(c, 1674, c.getX(), c.getY() - 1,
							3, 0, 120, 23, 200, 200, true, true);
					c.barrowsNpcs[3][1] = 1;
				}
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20720:
			c.getDH().sendDialogues(322, 2026);
			break;

		case 20770:
			if (c.barrowsNpcs[5][1] == 0) {
				if (c.WithinDistance(c.objectX, c.objectY, c.absX, c.absY, 3)) {
					Server.npcHandler.spawnNpc(c, 1672, c.getX() - 1, c.getY(),
							3, 0, 90, 19, 200, 200, true, true);
					c.barrowsNpcs[5][1] = 1;
				}
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 4979:
			if (c.farm[0] > 0 && c.farm[1] > 0) {
				c.getFarming().pickHerb();
			}
			break;

		// DOORS
		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
				break;
			}
		case 1530:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
			//Server.gameObjectManager.doorHandling(objectType, c.objectX, c.objectY, 0);
			break;

		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
			break;

		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1755:
			if (c.objectX == 2884 && c.objectY == 9797)
				c.getPA().movePlayer(c.absX, c.absY - 6400, 0);
			break;
		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		case 195:
			if (c.playerRights >= 1 && c.playerRights <= 6 || c.playerRights == 9 || c.playerRights == 8 || c.ironDonator(c.playerName)) {
				c.getPA().movePlayer(2887, 3511, 0);
			}
			break;
		case 411:
			c.usingRestoreAltar = true;
			if (c.playerRights >= 1 && c.playerRights <= 6 || c.playerRights == 8 || c.playerRights == 9 || c.ironDonator(c.playerName) && c.usingRestoreAltar) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
				c.specAmount = 10;
				c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
				c.runEnergy = 100;
				c.sendMessage("You recharge your prayer points, health, special, and run energy.");
				c.getPA().refreshSkill(5);
				c.getPA().refreshSkill(3);
				c.usingRestoreAltar = false;
			}
			break;
		case 409:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			}
			break;
		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			c.startAnimation(2140);
			if (obX == 2539 && obY == 4712) {
				new Object(5961, obX, obY, c.heightLevel, 3, 4, 5960, 2);
			} else {
				new Object(5961, obX, obY, c.heightLevel, 0, 4, 5960, 2);
				c.startAnimation(714);
			}
			c.getPA().startTeleport2(3090, 3956, 0);
			c.sendMessage("...and teleport out of the mage's cave.");
			break;

		case 1815:
			c.getPA().startTeleport2(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
			break;

		case 9706:
			c.getPA().startTeleport2(3105, 3951, 0);
			break;
		case 9707:
			c.getPA().startTeleport2(3105, 3956, 0);
			break;

		case 5959:
			c.startAnimation(2140);
			if (obX == 3090 && obY == 3956) {
				new Object(5961, obX, obY, c.heightLevel, 4, 4, 5959, 2);
			} else {
				new Object(5961, obX, obY, c.heightLevel, 0, 4, 5959, 2);
			}
			c.sendMessage("You pull the lever...");
			c.getPA().startTeleport2(2539, 4712, 0);
			break;

		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 16510:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
			break;

		case 16509:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;
		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;
		case 10082:
			c.getSmithing().sendSmelting();
			break;
		case 733:
			c.startAnimation(451);
			if (Misc.random(1) == 1) {
				//c.getPA().removeObject(c.objectX, c.objectY);
				if (c.objectX == 3158 && c.objectY == 3951) {
					c.getPA().checkObjectSpawn(-1, c.objectX, c.objectY, 2, 10);
				} else {
					c.getPA().checkObjectSpawn(-1, c.objectX, c.objectY, 0, 0);
				}
				c.sendMessage("You slash the web.");
			} else {
				c.sendMessage("You fail to slash the webs.");
				break;
			}
			if (c.objectX == 3158 && c.objectY == 3951) {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 1, 0,
						733, 50);
			} else {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 0, 3,
						733, 50);
			}
			break;

		default:
			ScriptManager.callFunc("objectClick1_" + objectType, c, objectType,
					obX, obY);
			break;

		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		if (c.playerRights == 3) {
			c.sendMessage("Object type: " + objectType);
		}
		switch (objectType) {
		case 2213:
		case 14367:
		case 11758:
		case 3193:
		case 26972:
		case 11744:
		case 24101:
		case 11748:
		case 25808:
			c.bankCheck = true;
			c.getPA().openUpBank(0);
			break;
		case 11666:
		case 3044:
			c.getSmithing().sendSmelting();
			break;
		/*
		 * case 2565: Thieving.stealFromStall(c, 7650, 1, 400, 50, objectType,
		 * obX, obY, 2); break; case 2564: Thieving.stealFromStall(c, 1613, 1,
		 * 500, 65, objectType, obX, obY, 0); break; case 2563:
		 * Thieving.stealFromStall(c, 1635, 1, 300, 36, objectType, obX, obY,
		 * 0); break; case 2562: Thieving.stealFromStall(c, 1611, 1, 600, 75,
		 * objectType, obX, obY, 3); break; case 2560:
		 * Thieving.stealFromStall(c, 950, 1, 24, 20, objectType, obX, obY, obX
		 * == 2662 ? 2 : 1); break;
		 */
		/*
		 * One stall that will give different amount of money depending on your
		 * thieving level, also different amount of xp.
		 */

		case 11730:
			if ((c.playerLevel[17]) >= 0 && (c.playerLevel[17] <= 20)) { // level
																			// 1
																			// of
																			// thieving
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1897, 2, 10, 1, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1897, 1, 10, 1, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 20 && (c.playerLevel[17] <= 40)) { // level
																					// 20
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 950, 2, 20, 20, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 950, 1, 20, 20, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 40 && (c.playerLevel[17] <= 60)) { // level
																					// 40
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1635, 2, 30, 40, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1635, 1, 30, 40, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 60 && (c.playerLevel[17] <= 70)) { // level
																					// 60
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 7650, 2, 40, 60, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 7650, 1, 40, 60, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 70 && (c.playerLevel[17] <= 80)) { // level
																					// 70
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1613, 2, 50, 70, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1613, 1, 50, 70, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 80 && (c.playerLevel[17] <= 90)) { // level
																					// 80
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1611, 2, 60, 80, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1611, 1, 60, 80, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 90 && (c.playerLevel[17] <= 96)) { // level
																					// 90
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1609, 2, 70, 90, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1609, 1, 70, 90, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 96 && (c.playerLevel[17] <= 98)) { // level
																					// 96
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 6814, 2, 80, 96, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 6814, 1, 80, 96, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) >= 98 && (c.playerLevel[17] <= 99)) { // level
																					// 98
																					// of
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 948, 2, 90, 96, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 948, 1, 90, 96, objectType, obX,
							obY, obX == 2667 ? 3 : 0);
			} else if ((c.playerLevel[17]) == 99) {
				if (c.playerRights >= 6 && c.playerRights <= 9)
					Thieving.stealFromStall(c, 1615, 2, 100, 99, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
				else
					Thieving.stealFromStall(c, 1615, 1, 100, 99, objectType,
							obX, obY, obX == 2667 ? 3 : 0);
			}
			break;
		/*
		 * case 14011: Thieving.stealFromStall(c, 1897, 1, 10, 1, objectType,
		 * obX, obY, 3); break; case 7053: Thieving.stealFromStall(c, 1897, 1,
		 * 18, 10, objectType, obX, obY, obX == 3079 ? 2 : 1); break;
		 */
		case 2558:
			if (System.currentTimeMillis() - c.lastLockPick < 3000
					|| c.freezeTimer > 0)
				break;
			if (c.getItems().playerHasItem(1523, 1)) {
				c.lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.objectX == 3044 && c.objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo2(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo2(1, 0);
					}

				} else if (c.objectX == 3038 && c.objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo2(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo2(-1, 0);
					}
				} else if (c.objectX == 3041 && c.objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo2(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo2(0, 1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			ScriptManager.callFunc("objectClick2_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		switch (objectType) {
		default:
			ScriptManager.callFunc("objectClick3_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}

	public void firstClickNpc(int i) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (c.playerRights == 3) {
			c.sendMessage("NPC ID: " + i + " NPC X: " + c.objectX + " NPC Y: "
					+ c.objectY);
		}
		switch (i) {
		case 3438:
			c.getDH().sendDialogues(3438, c.npcType);
			break;
		case 2040:
			c.getDH().sendDialogues(2040, c.npcType);
			break;
		case 1835:
			if (!c.startedEvent && c.easterStage == 0) {
				c.getDH().sendDialogues(1835, c.npcType);
			}else if (c.easterStage != 0) {
				if (c.easterStage == 2) {
					if (c.hasEggs()) {
						c.getDH().sendDialogues(1839, c.npcType);
					}
				} else if (c.easterStage == 1) {
					c.getDH().sendDialogues(1841, c.npcType);
				}
			}
				
			break;
		case 5045:
			c.getShops().openShop(17);
			break;
		case 531:
			c.getShops().openShop(13);
			break;
		case 5036:
			c.getDH().sendDialogues(638, c.npcType);
			break;
		case 3077:
			if (c.playerRights != 10) {
				c.getDH().sendDialogues(9998, c.npcType);
			} else {
				c.sendMessage("You can not become an Iron Man as you already are!");
				return;
			}
			break;
		case 1526:
			c.getShops().openShop(15);
			c.sendMessage("You currently have @red@" + c.cwPoints
					+ " @bla@CWars points.");
			break;
		case 1758:
			c.getShops().openShop(18);
			c.sendMessage("You currently have @red@" + c.pcPoints
					+ " @bla@Pest Control points.");
			break;
		case 404:
			c.getDH().sendDialogues(27, c.npcType);
			break;
		case 1671:
			c.getDH().sendDialogues(16, c.npcType);
			break;
		case 306:
			c.getDH().sendDialogues(2244, c.npcType);
			break;
		case 1552:
			if (c.getItems().getItemAmount(621) >= 1) {
				c.getDH().sendDialogues(1522, c.npcType);
			} else {
				c.getShops().openShop(9);
				c.sendMessage("You currently have @red@" + c.donPoints
						+ " @bla@donator points.");
				c.sendMessage("100 points = $1 ::donate");
			}
			break;
		case 731:
			c.getShops().openShop(23);
			break;
		case 3307:
			c.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons",
					"Chaos Dwarf", "-- Next Page --");
			c.teleAction = 7;
			break;
		case 706:
			c.getDH().sendDialogues(9, i);
			break;
		case 4066:
			c.getShops().openShop(12);
			c.sendMessage("You currently have @red@" + c.votPoints
					+ " @bla@Voting points.");
			// c.sendMessage("Please Vote for OxidePkz Daily for awesome rewards! ::vote");
			break;
		case 528:
			c.getShops().openShop(1);
			break;
		case 2580:
			c.getDH().sendDialogues(17, i);
			break;

		case 405:
			if (c.slayerTask <= 0) {
				c.getDH().sendDialogues(11, i);
			} else {
				c.getDH().sendDialogues(13, i);
			}
			break;

		case 1304:
			c.getDH().sendOption5("Home", "Edgeville", "Island",
					"Dagannoth Kings", "Next Page");
			c.teleAction = 1;
			break;

		case 1152:
			c.getDH().sendDialogues(16, i);
			break;

		case 394: // Banker
			c.bankCheck = true;
			c.getPA().openUpBank(0);
			break;
		case 1337:
			c.getDH()
			.sendOption2("Would you like to set a bank pin?", "Cancel");
			c.dialogueAction = 494;
			break;
			
		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 567: // Banker
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
			c.bankCheck = true;
			c.getPA().openUpBank(0);
			break;

		case 2566:
			c.getShops().openSkillCape();
			break;

		case 3789:
			c.sendMessage((new StringBuilder()).append("You currently have ")
					.append(c.pcPoints).append(" pest control points.")
					.toString());
			break;
		case 1307:
			if (!c.noEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;
		case 17010:
			if (c.playerMagicBook == 0) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 1) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			break;

		case 905:
			c.getDH().sendDialogues(5, i);
			break;

		case 460:
			c.getDH().sendDialogues(3, i);
			break;

		case 462:
			c.getDH().sendDialogues(7, i);
			break;

		case 1518: // Shrimp + Anchovies
			Fishing.attemptdata(c, 1);
			break;
		case 4928:
			Fishing.attemptdata(c, 11);
			break;
		case 6731:
			Fishing.attemptdata(c, 13);
			break;
		case 1517:
			Fishing.attemptdata(c, 12);
			break;
		case 334:
		case 313: // NET + HARPOON
			Fishing.attemptdata(c, 3);
			break;
		case 1520:
			Fishing.attemptdata(c, 5);
			break;
		case 2202:
			c.getDH().sendOption3("Can you Exchange my Emblems for PKP?",
					"Can you Exchange my Emblems for Gold?", "Exit");
			c.dialogueAction = 1758;
			break;
		case 309: // LURE
		case 310:
		case 311:
		case 314:
		case 315:
		case 317:
		case 318:
		case 328:
		case 331:
			Fishing.attemptdata(c, 4);
			break;

		case 1510:
			Fishing.attemptdata(c, 8);
			break;

		case 532:
			// c.getShops().openShop(5);
			c.getDH().sendOption4("Buy 1,000 barrage spells [2230k]",
					"Buy 1,000 vengeance spells [912k]",
					"Buy 1,000 of all runes [1788k]", "Open mage shop");
			c.dialogueAction = 1658;
			break;
		case 505://1td
			c.getShops().openShop(7);
			break;
			
		case 514:
			c.getShops().openShop(1);
			break;
			
		case 536:
			c.getShops().openShop(4);
			break;
		case 1028:
			c.getShops().openShop(6);
			break;

		case 2635:
			c.getShops().openShop(7);
			break;
		case 535:
			c.getShops().openShop(3);
			break;

		case 541:
			c.getShops().openShop(2);
			break;

		case 4397:
			c.getShops().openSkillCape();
			break;

		case 599:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;

		case 904:
			c.sendMessage((new StringBuilder()).append("You have ")
					.append(c.magePoints).append(" points.").toString());
			break;
		}
	}

	public void secondClickNpc(int i) {
		Pet.pickUpPetRequirements(c, i);
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		c.playerSkilling[10] = false;
		switch (i) {
		case 5919:
			c.getShops().openShop(19);
			break;
		case 315:
			c.getShops().openShop(16);
			c.sendMessage("@red@You Currently have "+c.hunterPoints+" Hunter Points.");
			break;
		case 541:
			c.getShops().openShop(2);
			break;

		case 528:
			c.getShops().openShop(1);
			break;

		case 514:
			c.getShops().openShop(1);
			break;
		case 2202:
			c.getShops().openShop(8);
			break;

		case 550:
			c.getShops().openShop(4);
			break;

		case 3796:
			c.getShops().openShop(6);
			break;

		case 1860:
			c.getShops().openShop(6);
			break;

		case 505:
			c.getShops().openShop(7);
			break;
		case 532:
			c.getShops().openShop(5);
			break;
		case 405:
			c.getShops().openShop(10);
			c.sendMessage("You currently have @red@" + c.slayerPoints
					+ " @bla@slayer points.");
			break;
		case 326:
		case 327:
		case 330:
		case 332:
		case 316: // BAIT + NET
			Fishing.attemptdata(c, 2);
			break;
		case 1517:
			Fishing.attemptdata(c, 9);
			break;
		case 310:
		case 311:
		case 314:
		case 317:
		case 318:
		case 328:
		case 329:
		case 331:
		case 309: // BAIT + LURE
			Fishing.attemptdata(c, 6);
			break;
		case 1510:
			Fishing.attemptdata(c, 7);
			break;
		case 1520:
			Fishing.attemptdata(c, 10);
			break;

		case 3788:
			c.getShops().openVoid();
			break;
		case 394:
			c.bankCheck = true;
			c.getPA().openUpBank(0);
			break;
		case 536://2td
			c.getShops().openShop(4);
			break;
		case 535:
			c.getShops().openShop(3);
			break;
		case 1028:
			c.getShops().openShop(6);
			break;

		case 1: // '\001'
		case 9: // '\t'
		case 18: // '\022'
		case 20: // '\024'
		case 21: // '\025'
		case 26: // '\032'
			c.getThieving().stealFromNPC(i);
			break;
		}
	}

	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		c.playerSkilling[10] = false;
		switch (npcType) {
		case 404:
			c.getShops().openShop(49);
			c.sendMessage("You currently have @red@" + c.duoPoints
					+ " @bla@Duo slayer points.");
			break;
		case 405:
			c.getShops().openShop(10);
			c.sendMessage("You currently have @red@" + c.slayerPoints
					+ " @bla@slayer points.");
			break;
		case 548:
			if (!c.noEquipment()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;

		case 836:
			c.getShops().openShop(103);
			break;
		default:
			ScriptManager.callFunc("npcClick3_" + npcType, c, npcType);
			if (c.playerRights == 3)
				Misc.println("Third Click NPC : " + npcType);
			break;

		}
	}

}