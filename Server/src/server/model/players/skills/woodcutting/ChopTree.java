package server.model.players.skills.woodcutting;

import server.Config;
import server.Server;
import server.core.World;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.content.Achievements;
import server.model.objects.GameObject;
import server.model.players.Client;
import server.model.players.skills.SkillHandler;
import server.model.players.skills.Tools;
import server.model.players.skills.Tools.Tool;
import server.tick.Tickable;
import server.util.Misc;
import server.util.Skill;
import server.world.GameObjectManager;

public class ChopTree {

	public enum Tree {
		ACHEY_TREE(new int[] { 2023 }, 1, 25, 2862, 3371, 75, 100), NORMAL_TREE(
				new int[] { 1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284,
						1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319,
						1330, 1331, 1332, 1333, 1365, 1383, 1384, 2409, 3033,
						3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904 },
				1, 25, 1511, 1342, 10, 100), OAK_TREE(new int[] { 11756 },
				15, 37.5, 1521, 1356, 14, 25), WILLOW_TREE(new int[] { 11755 }, 30, 67.5, 1519, 7399, 14, 15), TEAK_TREE(
				new int[] { 9036 }, 35, 85, 6333, 9037, 14, 20), MAPLE_TREE(
				new int[] { 11762 }, 45, 100, 1517, 1343, 50, 15), HOLLOW_TREE(
				new int[] { 2289, 4060 }, 45, 83, 3239, 2310, 59, 15), MAHOGANY_TREE(
				new int[] { 9034 }, 50, 125, 6332, 9035, 60, 10), YEW_TREE(
				new int[] { 11758 }, 60, 175, 1515, 7402, 75, 5), MAGIC_TREE(
				new int[] { 11764 }, 75, 250, 1513, 7401, 150, 5), DRAMEN_TREE(
				new int[] { 1292 }, 36, 0, 771, 1513, 59, 100), VINES(
				new int[] { 5103, 5104, 5105, 5106, 5107 }, 34, 0, -1, 1513, 2,
				100);

		private int[] id;
		private int level;
		private double xp; 
		private int log;
		private int stump;
		private int respawnTime;
		private int decayChance;

		public static Tree getTree(int id) {
			for (Tree tree : Tree.values()) {
				for (int ids : tree.getId()) {
					if (ids == id) {
						return tree;
					}
				}
			}
			return null;
		}

		private Tree(int[] id, int level, double xp, int log, int stump,
				int respawnTime, int decayChance) {
			this.id = id;
			this.level = level;
			this.xp = xp;
			this.log = log;
			this.stump = stump;
			this.respawnTime = respawnTime;
			this.decayChance = decayChance;
		}

		public int[] getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public double getXP() {
			return xp;
		}

		public int getLog() {
			return log;
		}

		public int getStump() {
			return stump;
		}

		public int getRespawnTime() {
			return respawnTime;
		}

		public int getDecayChance() {
			return decayChance;
		}
	}

	public static void handle(final Client player, final int id, final int x,
			final int y) {
		final GameObject p = GameObjectManager.getInstance().getObject(x, y,
				player.heightLevel);
		if (p != null && p.getId() != id) {
			return;
		}
		final Tree tree = Tree.getTree(id);
		if (tree == null) {
			return;
		}
		final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
		if (axe == null) {
			player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return;
		}
		if (player.playerLevel[Skill.WOODCUTTING] < tree.getLevel()) {
			player.sendMessage("You need a Woodcutting level of "
					+ tree.getLevel() + " to cut this tree.");
			return;
		}
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("Your inventory is too full to hold any more logs.");
			return;
		}
		player.sendMessage("You swing your axe at the "
				+ (tree == Tree.VINES ? "vines" : "tree") + ".");
		player.startAnimation(axe.getAnimation(), 0);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				final GameObject p = GameObjectManager.getInstance().getObject(
						x, y, player.heightLevel);
				if (p != null && p.objectId != id) {
					player.sendMessage("The tree has run out of logs.");
					container.stop();
					return;
				}
				if (player.getItems().freeSlots() == 0) {
					player.sendMessage("Your inventory is too full to hold any more logs.");
					container.stop();
					return;
				}
				if (SkillHandler.skillCheck(
						player.playerLevel[Skill.WOODCUTTING], tree.getLevel(),
						axe.getBonus())) {
					player.getPA().addSkillXP(
							(int) tree.getXP() * Config.WOODCUTTING_EXPERIENCE,
							8);
					if (tree.getLog() > 0) {
						if (player.inPvP() && player.getWealth() > 500000) {
							player.getItems().addItem(tree.getLog(), 2);
						} else {
							player.getItems().addItem(tree.getLog(), 1);
						}
						player.sendMessage("You get some "
								+ player.getItems().getItemName(tree.getLog())
								+ ".");
						if (id == 1306) {
							Achievements.appendTreeChopped(player);
						}
					}
					if (tree != Tree.DRAMEN_TREE
							&& Misc.random(100) <= tree.decayChance) {
						if (tree != Tree.VINES) {
							player.sendMessage("The tree has run out of logs.");
						}
						createStump(player, tree.getStump(), id,
								tree.getRespawnTime(), x, y);
						container.stop();
						return;
					}
				}
				player.startAnimation(axe.getAnimation(), 0);
			}

			@Override
			public void stop() {
				player.resetWalkingQueue();
				player.startAnimation(-1, 0);
			}
		});
		CycleEventHandler.getSingleton().addEvent(player, player.getSkilling(),
				4);
	}

	private static void createStump(final Client c, final int stump,
			final int id, final int respawnTime, final int x, final int y) {
		Server.gameObjectManager.createAnObject(c, stump, x, y);
		World.getWorld().submit(new Tickable(respawnTime) {

			@Override
			public void execute() {
				Server.gameObjectManager.createAnObject(c, id, x, y);
				stop();
			}

		});
	}

}
