package server.model.players.skills.slayer;

import server.model.players.Client;
import server.Server;
import server.util.Misc;

/**
 * Slayer.java
 * 
 * @author Sanity
 * 
 **/

public class Slayer {

	private Client c;

	public Slayer(Client c) {
		this.c = c;
	}
//BLOODVELD 1618
	//TODO 1648 CRAWLING HANDS
	public int[] lowTasks = { 414, 2098, 100, 85, 2834, 291, 3103, 2246,
			100, 2878 };
	public int[] lowReqs = { 15, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	public int[] medTasks = { 443, 484, 260, 291, 2005, 241, 414, 2098, 100,
			891 };
	public int[] medReqs = { 45, 50, 1, 1, 1, 1, 15, 1, 1, 1, 1 };
	public int[] highTasks = { 423, 412, 8, 415, 265, 2048, 104, 484, 260,
			2005, 4005, 498 };
	public int[] highReqs = { 65, 75, 80, 85, 1, 1, 1, 50, 1, 1, 90, 93 };
	
	public int[] eliteTasks = {6609, 239, 270, 272, 274, 3162,
			3129, 2205, 2215, 600, 2054, 499};

	public void giveTask() {
		if (c.combatLevel < 50)
			giveTask(1);
		else if (c.combatLevel >= 50 && c.combatLevel <= 90)
			giveTask(2);
		else if (c.combatLevel > 90 && c.combatLevel <= 126)
			giveTask(3);
		else
			giveTask(2);
	}

	public void giveTask2() {
		for (int j = 0; j < lowTasks.length; j++) {
			if (lowTasks[j] == c.slayerTask) {
				c.sendMessage("You already have an easy task... to kill "
						+ c.taskAmount + " "
						+ Server.npcHandler.getNpcListName(c.slayerTask) + ".");
				return;
			}
		}
		giveTask();
	}
	
	public void giveElite() {
		if (c.playerLevel[c.playerSlayer] >= 85) {
			int random = (int) (Math.random() * (eliteTasks.length - 1));
			int given = eliteTasks[random];
			c.slayerTask = given;
			c.taskAmount = Misc.random(5) + 4;
			c.sendMessage("You have been assigned to kill " + c.taskAmount + " "
					+ Server.npcHandler.getNpcListName(given)
					+ " as a slayer task.");
			c.getPA().loadQuests();
			c.getPA().removeAllWindows();
			c.eliteTask = true;
		} else {
			c.sendMessage("You must have 85 slayer to do an Elite Task");
			c.getPA().removeAllWindows();
			return;
		}
	}

	public void giveTask(int taskLevel) {
		int given = 0;
		int random = 0;
		if (taskLevel == 1) {
			random = (int) (Math.random() * (lowTasks.length - 1));
			given = lowTasks[random];
		} else if (taskLevel == 2) {
			random = (int) (Math.random() * (medTasks.length - 1));
			given = medTasks[random];
		} else if (taskLevel == 3) {
			random = (int) (Math.random() * (highTasks.length - 1));
			given = highTasks[random];
		}
		if (!canDoTask(taskLevel, random)) {
			giveTask(taskLevel);
			return;
		}
		c.slayerTask = given;
		c.taskAmount = Misc.random(15) + 15;
		c.sendMessage("You have been assigned to kill " + c.taskAmount + " "
				+ Server.npcHandler.getNpcListName(given)
				+ " as a slayer task.");
		c.getPA().loadQuests();
		c.getPA().removeAllWindows();
	}

	public boolean canDoTask(int taskLevel, int random) {
		if (taskLevel == 1) {
			return c.playerLevel[c.playerSlayer] >= lowReqs[random];
		} else if (taskLevel == 2) {
			return c.playerLevel[c.playerSlayer] >= medReqs[random];
		} else if (taskLevel == 3) {
			return c.playerLevel[c.playerSlayer] >= highReqs[random];
		}
		return false;
	}
}