package server.model.content;

import server.model.players.Client;
import server.util.Misc;

/*
 * Author Tim<satan>
 */
public class Achievements {

	/* NPC Kills */
	public static int easyKillCount = 50;
	public static int mediumKillCount = 500;
	public static int hardKillCount = 1000;

	/* Skilling Requirements */
	public static int easySharks = 500;
	public static int mediumSharks = 1000;
	public static int hardSharks = 2000;
	public static int easyGlories = 200;
	public static int mediumGlories = 1000;
	public static int hardGlories = 2000;

	public static int mediumtasksNeeded = 25;
	public static int hardtasksNeeded = 50;

	public static int mediumDuoTasksNeeded = 15;
	public static int hardDuoTasksNeeded = 30;

	public static void appendNPCKill(Client c) {
		if (c != null) {
			hasKilledAllBosses(c);
			completedEasy(c);
			completedMedium(c);
			completedHard(c);
		}
	}

	public static void appendSlayerTask(Client c) {
		c.slayerTasksCompleted++;
		if (c.slayerTasksCompleted == 25) {
			c.sendMessage("You have just finished completing 25 Slayer Tasks a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.slayerTasksCompleted == 50) {
			c.sendMessage("You have just finished completing 50 Slayer Tasks a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasSlayerTasksCompleted(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendDuoSlayerTask(Client c) {
		c.duoSlayerTasksCompleted++;
		if (c.duoSlayerTasksCompleted == 15) {
			c.sendMessage("You have just finished completing 15 Duo Slayer Tasks a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.duoSlayerTasksCompleted == 30) {
			c.sendMessage("You have just finished completing 30 Duo Slayer Tasks a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasDuoSlayerTasksCompleted(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void hasDuoSlayerTasksCompleted(Client c) {
		if (c.duoSlayerTasksCompleted >= mediumDuoTasksNeeded && !c.hardDuo) {
			if (c.achievementTab && !c.mediumDuo) {
				c.getPA().sendNewString(
						"@gre@" + "Complete 15 Duo Slayer Tasks", 29321);
			}
			c.mediumDuo = true;
		}
		if (c.duoSlayerTasksCompleted >= hardDuoTasksNeeded && c.mediumDuo) {
			if (c.achievementTab && !c.hardDuo) {
				c.getPA().sendNewString(
						"@gre@" + "Complete 30 Duo Slayer Tasks", 29331);
			}
			c.hardDuo = true;
		}
	}

	public static void hasSlayerTasksCompleted(Client c) {
		if (c.slayerTasksCompleted >= mediumtasksNeeded && !c.hardTasks) {
			if (c.achievementTab && !c.mediumTasks) {
				c.getPA().sendNewString("@gre@" + "Complete 25 Slayer Tasks",
						29320);
			}
			c.mediumTasks = true;
		}
		if (c.slayerTasksCompleted >= hardtasksNeeded && c.mediumTasks) {
			if (c.achievementTab && !c.hardTasks) {
				c.getPA().sendNewString("@gre@" + "Complete 50 Slayer Tasks",
						29330);
			}
			c.hardTasks = true;
		}
	}

	public static void appendSharkFished(Client c) {
		c.sharksFished++;
		if (c.sharksFished == 500) {
			c.sendMessage("You have just finished fishing 500 Sharks a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.sharksFished == 1000) {
			c.sendMessage("You have just finished fishing 1000 Sharks a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.sharksFished == 2000) {
			c.sendMessage("You have just finished fishing 2000 Sharks a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasSharksFished(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendGloriesCreated(Client c) {
		c.gloriesCrafted++;
		if (c.gloriesCrafted == 200) {
			c.sendMessage("You have just finished crafting 200 Glories a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.gloriesCrafted == 1000) {
			c.sendMessage("You have just finished crafting 1000 Glories a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.gloriesCrafted == 2000) {
			c.sendMessage("You have just finished crafting 2000 Glories a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasGloriesCrafted(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendTreeChopped(Client c) {
		c.magicsCut++;
		if (c.magicsCut == 200) {
			c.sendMessage("You have just finished chopping 200 Magic logs a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.magicsCut == 1000) {
			c.sendMessage("You have just finished chopping 1000 Magic logs a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.magicsCut == 2000) {
			c.sendMessage("You have just finished chopping 2000 Magic logs a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasTreesChopped(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendLogBurned(Client c) {
		c.magicsBurned++;
		if (c.magicsBurned == 200) {
			c.sendMessage("You have just finished burning 200 Magic logs a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.magicsBurned == 1000) {
			c.sendMessage("You have just finished burning 1000 Magic logs a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.magicsBurned == 2000) {
			c.sendMessage("You have just finished burning 2000 Magic logs a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasLogsBurned(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendMagicBow(Client c) {
		c.bowsMade++;
		if (c.bowsMade == 200) {
			c.sendMessage("You have just finished fletching 200 Magic longbows a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.bowsMade == 1000) {
			c.sendMessage("You have just finished fletching 1000 Magic longbows a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.bowsMade == 2000) {
			c.sendMessage("You have just finished fletching 2000 Magic longbows a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasBowsMade(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static void appendHerb(Client c) {
		c.herbsFarmed++;
		if (c.herbsFarmed == 200) {
			c.sendMessage("You have just finished farming 200 Herbs a requirement for,");
			c.sendMessage("The easy Achievement Diary.");
			c.achievements++;
		}
		if (c.herbsFarmed == 1000) {
			c.sendMessage("You have just finished farming 1000 Herbs a requirement for,");
			c.sendMessage("The medium Achievement Diary.");
			c.achievements++;
		}
		if (c.herbsFarmed == 2000) {
			c.sendMessage("You have just finished farming 2000 Herbs a requirement for,");
			c.sendMessage("The hard Achievement Diary.");
			c.achievements++;
		}
		hasHerbsGrown(c);
		completedEasy(c);
		completedMedium(c);
		completedHard(c);
	}

	public static boolean hasHerbsGrown(Client c) {
		if (c.herbsFarmed >= easyGlories && !c.mediumHerbs && !c.hardHerbs) {
			if (c.achievementTab && !c.easyHerbs) {
				c.getPA().sendNewString("@gre@" + "Grow 200 Herbs", 29311);
			}
			c.easyHerbs = true;
		}
		if (c.herbsFarmed >= mediumGlories && c.easyHerbs && !c.hardHerbs) {
			if (c.achievementTab && !c.mediumHerbs) {
				c.getPA().sendNewString("@gre@" + "Grow 1000 Herbs", 29319);
			}
			c.mediumHerbs = true;
		}
		if (c.herbsFarmed >= hardGlories && c.easyHerbs && c.mediumHerbs) {
			if (c.achievementTab && !c.hardHerbs) {
				c.getPA().sendNewString("@gre@" + "Grow 2000 Herbs", 29329);
			}
			c.hardHerbs = true;
		}
		return true;
	}

	public static boolean hasBowsMade(Client c) {
		if (c.bowsMade >= easyGlories && !c.mediumBows && !c.hardBows) {
			if (c.achievementTab && !c.easyBows) {
				c.getPA().sendNewString("@gre@" + "Fletch 200 Magic Longbow's",
						29310);
			}
			c.easyBows = true;
		}
		if (c.bowsMade >= mediumGlories && c.easyBows && !c.hardBows) {
			if (c.achievementTab && !c.mediumBows) {
				c.getPA().sendNewString(
						"@gre@" + "Fletch 1000 Magic Longbow's", 29318);
			}
			c.mediumBows = true;
		}
		if (c.bowsMade >= hardGlories && c.easyBows && c.mediumBows) {
			if (c.achievementTab && !c.hardBows) {
				c.getPA().sendNewString(
						"@gre@" + "Fletch 2000 Magic Longbow's", 29328);
			}
			c.hardBows = true;
		}
		return true;
	}

	public static boolean hasLogsBurned(Client c) {
		if (c.magicsBurned >= easyGlories && !c.mediumBurned && !c.hardBurned) {
			if (c.achievementTab && !c.easyBurned) {
				c.getPA().sendNewString("@gre@" + "Burn 200 Magic Logs", 29309);
			}
			c.easyBurned = true;
		}
		if (c.magicsBurned >= mediumGlories && c.easyBurned && !c.hardBurned) {
			if (c.achievementTab && !c.mediumBurned) {
				c.getPA()
						.sendNewString("@gre@" + "Burn 1000 Magic Logs", 29317);
			}
			c.mediumBurned = true;
		}
		if (c.magicsBurned >= hardGlories && c.easyBurned && c.mediumBurned) {
			if (c.achievementTab && !c.hardBurned) {
				c.getPA()
						.sendNewString("@gre@" + "Burn 2000 Magic Logs", 29327);
			}
			c.hardBurned = true;
		}
		return true;
	}

	public static boolean hasTreesChopped(Client c) {
		if (c.magicsCut >= easyGlories && !c.mediumMagics && !c.hardMagics) {
			if (c.achievementTab && !c.easyMagics) {
				c.getPA().sendNewString("@gre@" + "Chop 200 Magic Logs", 29308);
			}
			c.easyMagics = true;
		}
		if (c.magicsCut >= mediumGlories && c.easyMagics && !c.hardMagics) {
			if (c.achievementTab && !c.mediumMagics) {
				c.getPA()
						.sendNewString("@gre@" + "Chop 1000 Magic Logs", 29316);
			}
			c.mediumMagics = true;
		}
		if (c.magicsCut >= hardGlories && c.easyMagics && c.mediumMagics) {
			if (c.achievementTab && !c.hardMagics) {
				c.getPA()
						.sendNewString("@gre@" + "Chop 2000 Magic Logs", 29326);
			}
			c.hardMagics = true;
		}
		return true;
	}

	public static boolean completedEasy(Client c) {
		if (c.easyMagics && c.easyBosses && c.easyBows && c.easyBurned
				&& c.easyGlories && c.easyHerbs && c.easySharks
				&& !c.claimedEasy) {
			c.sendMessage("You have completed the Easy Achievement Diary!");
			c.sendGlobal(Misc.formatPlayerName(c.playerName)
					+ " has just completed the Easy Achievement Diary.");
			c.claimedEasy = true;
			if (c.getItems().freeSlots() > 1) {
				c.getItems().addItem(13121, 1);
				c.getItems().addItem(13122, 1);
				c.sendMessage("You have earned Ardougne Cloak 1 & 2");
			} else {
				c.getItems().addItemToBank(13121, 1);
				c.getItems().addItemToBank(13122, 1);
				c.sendMessage("You have earned Ardougne Cloak 1 & 2");
			}
			return true;
		}
		return false;
	}

	public static boolean completedMedium(Client c) {
		if (c.mediumMagics && c.mediumBosses && c.mediumBows && c.mediumBurned
				&& c.mediumGlories && c.mediumHerbs && c.mediumSharks
				&& c.mediumTasks && c.mediumDuo && !c.claimedMedium) {
			c.sendMessage("You have completed the Medium Achievement Diary!");
			c.sendGlobal(Misc.formatPlayerName(c.playerName)
					+ " has just completed the Medium Achievement Diary.");
			c.claimedMedium = true;
			if (c.getItems().freeSlots() > 1) {
				c.getItems().addItem(13123, 1);
				c.sendMessage("You have earned Ardougne Cloak 3");
			} else {
				c.getItems().addItemToBank(13123, 1);
				c.sendMessage("You have earned Ardougne Cloak 3");
			}
			return true;
		}
		return false;
	}

	public static boolean completedHard(Client c) {
		if (c.hardMagics && c.hardBosses && c.hardBows && c.hardBurned
				&& c.hardGlories && c.hardHerbs && c.hardSharks && c.hardTasks
				&& c.hardDuo && !c.claimedHard) {
			c.sendMessage("You have completed the Hard Achievement Diary!");
			c.sendGlobal(Misc.formatPlayerName(c.playerName)
					+ " has just completed the Hard Achievement Diary.");
			c.claimedHard = true;
			if (c.getItems().freeSlots() > 1) {
				c.getItems().addItem(13124, 1);
				c.sendMessage("You have earned Ardougne Cloak 4");
			} else {
				c.getItems().addItemToBank(13124, 1);
				c.sendMessage("You have earned Ardougne Cloak 4");
			}
			return true;
		}
		return false;
	}

	public static boolean hasGloriesCrafted(Client c) {
		if (c.gloriesCrafted >= easyGlories && !c.mediumGlories
				&& !c.hardGlories) {
			if (c.achievementTab && !c.easyGlories) {
				c.getPA().sendNewString(
						"@gre@" + "Craft 200 Amulet of Glories", 29307);
			}
			c.easyGlories = true;
		}
		if (c.gloriesCrafted >= mediumGlories && c.easyGlories
				&& !c.hardGlories) {
			if (c.achievementTab && !c.mediumGlories) {
				c.getPA().sendNewString(
						"@gre@" + "Craft 1000 Amulet of Glories", 29315);
			}
			c.mediumGlories = true;
		}
		if (c.gloriesCrafted >= hardGlories && c.easyGlories && c.mediumGlories) {
			if (c.achievementTab && !c.hardGlories) {
				c.getPA().sendNewString(
						"@gre@" + "Craft 2000 Amulet of Glories", 29325);
			}
			c.hardGlories = true;
		}
		return true;
	}

	public static void hasSharksFished(Client c) {
		if (c.sharksFished >= easySharks && !c.mediumSharks && !c.hardSharks) {
			if (c.achievementTab && !c.easySharks) {
				c.getPA().sendNewString("@gre@" + "Catch 500 Sharks", 29306);
			}
			c.easySharks = true;
		}
		if (c.sharksFished >= mediumSharks && c.easySharks && !c.hardSharks) {

			if (c.achievementTab && !c.mediumSharks) {
				c.getPA().sendNewString("@gre@" + "Catch 500 Sharks", 29314);
			}
			c.mediumSharks = true;
		}
		if (c.sharksFished >= hardSharks && c.easySharks && c.mediumSharks) {
			if (c.achievementTab && !c.hardSharks) {
				c.getPA().sendNewString("@gre@" + "Catch 2000 Sharks", 29324);
			}
			c.hardSharks = true;
		}
	}

	public static boolean hasKilledAllBosses(Client c) {
		if (c.callistoKills >= easyKillCount
				&& c.seaSnakeKills >= easyKillCount
				&& c.bandosKills >= easyKillCount
				&& c.zammyKills >= easyKillCount
				&& c.saraKills >= easyKillCount && c.armaKills >= easyKillCount
				&& c.primeKills >= easyKillCount
				&& c.supremeKills >= easyKillCount
				&& c.rexKills >= easyKillCount && c.kbdKills >= easyKillCount
				&& c.seatrollKills >= easyKillCount
				&& c.kalphiteKills >= easyKillCount
				&& c.venenatisKills >= easyKillCount
				&& c.vetionKills >= easyKillCount
				&& c.smokeKills >= easyKillCount && !c.easyBosses) {
			if (c.achievementTab && !c.easyBosses) {
				c.getPA().sendNewString("@gre@" + "Kill each boss 50 times",
						29305);
			}
			c.easyBosses = true;
			c.achievements++;
		}
		if (c.callistoKills >= mediumKillCount
				&& c.seaSnakeKills >= mediumKillCount
				&& c.bandosKills >= mediumKillCount
				&& c.zammyKills >= mediumKillCount
				&& c.saraKills >= mediumKillCount
				&& c.armaKills >= mediumKillCount
				&& c.primeKills >= mediumKillCount
				&& c.supremeKills >= mediumKillCount
				&& c.rexKills >= mediumKillCount
				&& c.kbdKills >= mediumKillCount
				&& c.seatrollKills >= mediumKillCount
				&& c.kalphiteKills >= mediumKillCount
				&& c.venenatisKills >= mediumKillCount
				&& c.vetionKills >= mediumKillCount
				&& c.smokeKills >= mediumKillCount && !c.mediumBosses) {
			if (c.achievementTab && c.easyBosses && !c.mediumBosses) {
				c.getPA().sendNewString("@gre@" + "Kill each boss 500 times",
						29313);
			}
			c.mediumBosses = true;
			c.achievements++;
		}
		if (c.callistoKills >= hardKillCount
				&& c.seaSnakeKills >= hardKillCount
				&& c.bandosKills >= hardKillCount
				&& c.zammyKills >= hardKillCount
				&& c.saraKills >= hardKillCount && c.armaKills >= hardKillCount
				&& c.primeKills >= hardKillCount
				&& c.supremeKills >= hardKillCount
				&& c.rexKills >= hardKillCount && c.kbdKills >= hardKillCount
				&& c.seatrollKills >= hardKillCount
				&& c.kalphiteKills >= hardKillCount
				&& c.venenatisKills >= hardKillCount
				&& c.vetionKills >= hardKillCount
				&& c.smokeKills >= hardKillCount && !c.hardBosses) {
			if (c.achievementTab && c.easyBosses && c.mediumBosses) {
				c.getPA().sendNewString("@gre@" + "Kill each boss 1000 times",
						29323);
			}
			c.hardBosses = true;
			c.achievements++;
		}
		return true;
	}

	public static int countAchievements(Client c) {
		if (c.easyBosses) {
			c.achievements++;
		}
		if (c.mediumBosses) {
			c.achievements++;
		}
		if (c.hardBosses) {
			c.achievements++;
		}
		if (c.easySharks) {
			c.achievements++;
		}
		if (c.mediumSharks) {
			c.achievements++;
		}
		if (c.hardSharks) {
			c.achievements++;
		}
		if (c.easyGlories) {
			c.achievements++;
		}
		if (c.mediumGlories) {
			c.achievements++;
		}
		if (c.hardGlories) {
			c.achievements++;
		}
		if (c.easyBows) {
			c.achievements++;
		}
		if (c.mediumBows) {
			c.achievements++;
		}
		if (c.hardBows) {
			c.achievements++;
		}
		if (c.easyMagics) {
			c.achievements++;
		}
		if (c.mediumMagics) {
			c.achievements++;
		}
		if (c.hardMagics) {
			c.achievements++;
		}
		if (c.easyBurned) {
			c.achievements++;
		}
		if (c.mediumBurned) {
			c.achievements++;
		}
		if (c.hardBurned) {
			c.achievements++;
		}
		if (c.easyHerbs) {
			c.achievements++;
		}
		if (c.mediumHerbs) {
			c.achievements++;
		}
		if (c.hardHerbs) {
			c.achievements++;
		}
		if (c.easyBows) {
			c.achievements++;
		}
		if (c.mediumTasks) {
			c.achievements++;
		}
		if (c.hardTasks) {
			c.achievements++;
		}
		if (c.mediumDuo) {
			c.achievements++;
		}
		if (c.hardDuo) {
			c.achievements++;
		}
		return c.achievements;
	}

	public static void loadInterface(Client c) {
		hasKilledAllBosses(c);
		hasBowsMade(c);
		hasSharksFished(c);
		hasLogsBurned(c);
		hasHerbsGrown(c);
		hasTreesChopped(c);
		hasGloriesCrafted(c);
		hasSlayerTasksCompleted(c);
		hasDuoSlayerTasksCompleted(c);
		c.setSidebarInterface(2, 29265);
		c.getPA().sendNewString(
				"Tasks Completed " + countAchievements(c) + "/25", 29295);
		c.getPA().sendNewString(
				(c.easyBosses ? "@gre@" : "@red@") + "Kill each boss 50 times",
				29305);
		c.getPA().sendNewString(
				(c.mediumBosses ? "@gre@" : "@red@")
						+ "Kill each boss 500 times", 29313);
		c.getPA().sendNewString(
				(c.hardBosses ? "@gre@" : "@red@")
						+ "Kill each boss 1000 times", 29323);

		c.getPA().sendNewString(
				(c.easySharks ? "@gre@" : "@red@") + "Catch 500 Sharks", 29306);

		c.getPA().sendNewString(
				(c.mediumSharks ? "@gre@" : "@red@") + "Catch 1000 Sharks",
				29314);

		c.getPA()
				.sendNewString(
						(c.hardSharks ? "@gre@" : "@red@")
								+ "Catch 2000 Sharks", 29324);

		c.getPA().sendNewString(
				(c.easyGlories ? "@gre@" : "@red@")
						+ "Craft 200 Amulet of Glories", 29307);

		c.getPA().sendNewString(
				(c.mediumGlories ? "@gre@" : "@red@")
						+ "Craft 1000 Amulet of Glories", 29315);

		c.getPA().sendNewString(
				(c.hardGlories ? "@gre@" : "@red@")
						+ "Craft 2000 Amulet of Glories", 29325);

		c.getPA()
				.sendNewString(
						(c.easyBows ? "@gre@" : "@red@")
								+ "Fletch 200 Magic Longbow's", 29310);

		c.getPA().sendNewString(
				(c.mediumBows ? "@gre@" : "@red@")
						+ "Fletch 1000 Magic Longbow's", 29318);

		c.getPA().sendNewString(
				(c.hardBows ? "@gre@" : "@red@")
						+ "Fletch 2000 Magic Longbow's", 29328);

		c.getPA().sendNewString(
				(c.easyMagics ? "@gre@" : "@red@") + "Chop 200 Magic Logs",
				29308);

		c.getPA().sendNewString(
				(c.mediumMagics ? "@gre@" : "@red@") + "Chop 1000 Magic Logs",
				29316);

		c.getPA().sendNewString(
				(c.hardMagics ? "@gre@" : "@red@") + "Chop 1000 Magic Logs",
				29336);

		c.getPA().sendNewString(
				(c.easyBurned ? "@gre@" : "@red@") + "Burn 200 Magic Logs",
				29309);

		c.getPA().sendNewString(
				(c.mediumBurned ? "@gre@" : "@red@") + "Burn 1000 Magic Logs",
				29317);

		c.getPA().sendNewString(
				(c.hardBurned ? "@gre@" : "@red@") + "Burn 2000 Magic Logs",
				29327);

		c.getPA().sendNewString(
				(c.easyHerbs ? "@gre@" : "@red@") + "Grow 200 Herbs", 29311);

		c.getPA().sendNewString(
				(c.mediumHerbs ? "@gre@" : "@red@") + "Grow 1000 Herbs", 29319);

		c.getPA().sendNewString(
				(c.hardHerbs ? "@gre@" : "@red@") + "Grow 2000 Herbs", 29329);

		c.getPA().sendNewString(
				(c.mediumTasks ? "@gre@" : "@red@")
						+ "Complete 25 Slayer Tasks", 29320);

		c.getPA().sendNewString(
				(c.hardTasks ? "@gre@" : "@red@") + "Complete 50 Slayer Tasks",
				29330);

		c.getPA().sendNewString(
				(c.mediumDuo ? "@gre@" : "@red@")
						+ "Complete 15 Duo Slayer Tasks", 29321);

		c.getPA().sendNewString(
				(c.hardDuo ? "@gre@" : "@red@")
						+ "Complete 30 Duo Slayer Tasks", 29331);
	}

	/*
	 * public boolean easyBosses = false, mediumBosses = false, hardBosses =
	 * false, easySharks = false, mediumSharks = false, hardSharks = false,
	 * easyGlories = false, mediumGlories = false, hardGlories = false,
	 * easyMagics, mediumMagics, hardMagics, easyBurned, mediumBurned,
	 * hardBurned, easyBows, mediumBows, hadBows, easyHerbs, mediumHerbs,
	 * hardHerbs;
	 */
}
