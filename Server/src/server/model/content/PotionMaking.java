package server.model.content;

import server.Config;
import server.Server;
import server.cache.RSItemDefinition;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

public class PotionMaking {// todo description items

	public static final int VIAL_OF_WATER = 227;
	public static final int COCONUT_MILK = 5935;
	private static final int UNFINISHED_ANIMATION = 363;

	public static boolean combineDose(Client c, int itemused1, int itemused2,
			int slot1, int slot2) {
		
		RSItemDefinition one = RSItemDefinition.valueOf(itemused1);
		RSItemDefinition two = RSItemDefinition.valueOf(itemused2);
		
		String itemName1 = one.name;
		String itemName2 = two.name;
		if (itemName1.contains("(4)") || itemName2.contains("(4)")) {
			return false;
		}
		try {
			if (itemName1.substring(0, itemName1.indexOf("("))
					.equalsIgnoreCase(
							itemName2.substring(0, itemName2.indexOf("(")))) {
				int amount1 = Integer
						.parseInt(itemName1.substring(
								itemName1.indexOf("(") + 1,
								itemName1.indexOf("(") + 2));
				int amount2 = Integer
						.parseInt(itemName2.substring(
								itemName2.indexOf("(") + 1,
								itemName2.indexOf("(") + 2));
				int totalAmount = amount1 + amount2;
				if (!c.getItems().playerHasItem(itemused1)
						|| !c.getItems().playerHasItem(itemused2)) {
					return false;
				}
				if (totalAmount > 4) {
					amount1 = 4;
					amount2 = totalAmount - 4;
					String item3 = itemName1.substring(0,
							itemName1.indexOf("(") + 1)
							+ amount1 + ")";
					String item4 = itemName1.substring(0,
							itemName1.indexOf("(") + 1)
							+ amount2 + ")";
					c.getItems().removeItemSlot(slot1);
					c.getItems().removeItemSlot(slot2);
					c.getItems().addItem(c.getItems().getItemId(item3), slot1,
							1);
					c.getItems().addItem(c.getItems().getItemId(item4), slot2,
							1);
					return true;
				} else {
					amount1 = totalAmount;
					String item3 = itemName1.substring(0,
							itemName1.indexOf("(") + 1)
							+ amount1 + ")";
					c.getItems().removeItemSlot(slot1);
					c.getItems().removeItemSlot(slot2);
					c.getItems().addItem(c.getItems().getItemId(item3), slot1,
							1);
					c.getItems().addItem(229, slot2, 1);
					return true;
				}
			}
			// player.getActionSender().playSound(477, 1, 0);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * Potion data.
	 * 
	 * @note herb, ingredient, unfinished, finished, exp, req
	 */
	private static final double[][] POTIONS = { { 249, 221, 91, 121, 25, 0 }, // Attack
																				// Potion.
			{ 251, 235, 93, 175, 37.5, 5 }, // Anti-Poison.
			{ 253, 225, 95, 115, 50, 12 }, // Strength Potion.
			{ 253, 592, 95, 3408, 50, 15 }, // Serum 207
			{ 255, 223, 97, 127, 62.5, 22 }, // Restore Potion.
			{ 255, 1581, 97, 1582, 80, 25 }, // Blamish Oil.
			{ 255, 1975, 97, 3010, 67.5, 26 }, // Energy Potion.
			{ 257, 239, 99, 133, 75, 30 }, // Defence Potion.
			{ 2998, 2152, 3002, 3034, 80, 34 }, // Agility Potion.
			{ 257, 231, 99, 139, 87.5, 38 }, // Prayer Potion.
			{ 259, 221, 101, 145, 100, 45 }, // Super Attack Potion.
			{ 259, 235, 101, 181, 106.3, 48 }, // Super Anti-Poison.
			{ 261, 231, 103, 151, 112.5, 50 }, // Fishing Potion.
			{ 261, 2970, 103, 3018, 117.5, 52 }, // Super Energy Potion.
			{ 263, 225, 105, 157, 125, 55 }, // Super Strength Potion.
			{ 263, 241, 105, 187, 137.5, 60 }, // Weapon Poison.
			{ 3000, 223, 3004, 3026, 142.5, 63 }, // Super Restore.
			{ 265, 239, 107, 163, 150, 66 }, // Super Defence.
			{ 2481, 241, 2483, 2454, 157.5, 69 }, // Antifire.
			{ 267, 245, 109, 169, 162.5, 72 }, // Ranging Potion.
			{ 2481, 3138, 2483, 3042, 172.5, 76 }, // Magic Potion.
			{ 2998, 6049, 5942, 5945, 175, 78 }, // antodote+ Potion.
			{ 6016, 223, 5936, 5937, 175, 78 }, // weapon+ Potion.
			{ 2398, 5106, 5939, 5940, 175, 78 }, // weapon++ Potion.
			{ 259, 6051, 101, 5954, 175, 78 }, // antidote++ Potion.
			{ 2998, 6693, 111, 6687, 175, 78 }, // sara brew Potion.
			{ 269, 247, 111, 189, 175, 78 }, // Zamorak Potion.
			{ -1, 5952, 12934, 12907, 120, 87 }, // Anti-venom
			{ -1, 12907, 269, 12915, 125, 94 }, // Anti-venom+
	};

	static String getLine(Client c) {
		return c.below459 ? ("\\n\\n\\n\\n") : ("\\n\\n\\n\\n\\n");
	}

	public static void useItemInterface(final Client c, final int useWith,
			final int itemUsed) {
		int container = VIAL_OF_WATER;
		int item = itemUsed, usedItem = useWith, herb = -1;
		double ingre = -1, unf = -1, fin = -1, exp = -1, req = -1;
		for (double[] data : POTIONS) {
			herb = (int) data[0];
			ingre = data[1];
			unf = data[2];
			fin = data[3];
			if ((itemUsed == herb && useWith == container)
					|| (itemUsed == container && useWith == herb)) {
				handleInterface(c, (int) unf);
			}
			if (itemUsed == 12934 && useWith == 5952 || itemUsed == 5952 && useWith == 12934) {
				handleFinishedInterface(c, (int) 12907);
			}
			if (itemUsed == 12907 && useWith == 269 || itemUsed == 269 && useWith == 12907) {
				handleFinishedInterface(c, (int) 12915);
				return;
			}
			if ((itemUsed == unf && useWith == ingre)
					|| (itemUsed == ingre && useWith == unf)) {
				handleFinishedInterface(c, (int) fin);
			}
		}
	}

	private static void handleFinishedInterface(final Client c, final int pot) {
		c.getPA().sendFrame164(4429);
		c.isOnInterface = true;
		c.isMakingPots = true;
		c.stringu = (int) pot;
		boolean view190 = true;
		c.getPA().sendFrame246(1746, view190 ? 140 : 140, (int) pot);
		c.getPA().sendFrame126(
				getLine(c) + "" + c.getItems().getItemName((int) pot) + "",
				2799);
	}

	private static void handleInterface(final Client c, final int unf) {
		c.getPA().sendFrame164(4429);
		c.isOnInterface = true;
		c.isMakingPots = true;
		c.stringu = (int) unf;
		boolean view190 = true;
		c.getPA().sendFrame246(1746, view190 ? 140 : 140, (int) unf);
		c.getPA().sendFrame126(
				getLine(c) + "" + c.getItems().getItemName((int) unf) + "",
				2799);
	}

	public static boolean createPotion(Client player, int useItem,
			int withItem, int slot, int slotUsed, int amount) {
		player.getPA().removeAllWindows();
		player.doAmount = amount;
		player.isMakingPots = true;
		player.isSkilling[player.playerHerblore] = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (player.doAmount == 0) {
					container.stop();
					return;
				}
				if (player.isSkilling[player.playerHerblore] == false) {
					container.stop();
					return;
				}
				int container2 = VIAL_OF_WATER;
				int item = useItem, usedItem = withItem, herb = -1;
				double ingre = -1, unf = -1, fin = -1, exp = -1, req = -1;
				for (double[] data : POTIONS) {
					herb = (int) data[0];
					ingre = data[1];
					unf = data[2];
					fin = data[3];
					exp = data[4];
					req = data[5];
					if ((item == herb && usedItem == container2)
							|| (item == container2 && usedItem == herb)) {
						if (player.playerLevel[player.playerHerblore] < req) {
							player.getPA().sendStatement(
									"You need a Herblore level of " + (int) req
											+ " in order to make this potion.");
							container.stop();
							return;
						}
						if (!player.getItems().playerHasItem(item)
								|| !player.getItems().playerHasItem(
										VIAL_OF_WATER)) {
							container.stop();
							return;
						}
						createUnfinished(player, herb, unf,
								item == container2 ? slot : slotUsed,
								item == container2 ? slotUsed : slot);
					}
					if ((item == unf && usedItem == ingre)
							|| (item == ingre && usedItem == unf)) {
						if (player.playerLevel[player.playerHerblore] < req) {
							player.getPA().sendStatement(
									"You need a Herblore level of " + (int) req
											+ " in order to make this potion.");
							container.stop();
							return;
						}
						if (!player.getItems().playerHasItem(item)
								|| !player.getItems().playerHasItem((int) unf)) {
							container.stop();
							return;
						}
						createFinished(player, ingre, unf, fin, exp,
								item == ingre ? slot : slotUsed,
								item == ingre ? slotUsed : slot);
					}
				}
			}

			@Override
			public void stop() {
				player.getPA().closeAllWindows();
				player.startAnimation(player.playerStandIndex);
				player.doAmount = 0;
				player.isCutting = false;
				player.isOnInterface = false;
				player.isSkilling[player.playerHerblore] = false;
			}

		}, 3);

		return false;
	}

	public static void createUnfinished(final Client player, int herb,
			final double unf, final int slot, final int slotUsed) {
		final int item = herb;
		// player.getActionSender().playSound(270, 1, 0);
		final int taskId = player.getTask();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(taskId)) {
					container.stop();
					return;
				}
				if (player.getItems().playerHasItem(item)
						&& player.getItems().playerHasItem(VIAL_OF_WATER)) {
					
					RSItemDefinition def = RSItemDefinition.valueOf(item);
					if (def == null) {
						player.sendMessage("Internal error.");
						container.stop();
						return;
						
					}
					int amount = 1;
					
					if (def.id == 12934) {
						amount = 5;
					}
					
					if (player.getItems().playerHasItem(def.id, amount)) {
						player.sendMessage("You put the "
								+ def.name
								.replace(" herb", "").toLowerCase()
								+ " into the vial of water.");
						player.startAnimation(UNFINISHED_ANIMATION);
						player.getItems().deleteItem(item, amount);
						player.getItems().deleteItem(VIAL_OF_WATER, 1);
						player.getItems().addItem((int) unf, 1);
					}
				}
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 1);
	}

	public static void createFinished(final Client player, final double ingre,
			final double unf, final double fin, final double exp,
			final int slot, final int slotUsed) {
		final int item = (int) ingre;

		final int taskId = player.getTask();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(taskId)) {
					container.stop();
					return;
				}
				int amount = 1;
				
				if (unf == 12934) {
					amount = 5;
				}
				/*double f = fin;
				if (ingre == 12907) {
					f = 12913;
				}*/
				if (player.getItems().playerHasItem(item)
						&& player.getItems().playerHasItem((int) unf, amount)) {
					player.startAnimation(UNFINISHED_ANIMATION);
					player.sendMessage("You mix the "
							+ player.getItems().getItemName(item).toLowerCase()
							+ " into your potion");
					player.getItems().deleteItem(item, 5);
					player.getItems().deleteItem((int) unf, amount);
					player.getItems().addItem((int) fin, 1);
					player.getPA().addSkillXP(
							(int) exp * Config.HERBLORE_EXPERIENCE,
							player.playerHerblore);
				} else {
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	public static boolean emptyPotion(Client player, int item, int slot) {
		String description = Server.itemHandler.ItemList[item].itemDescription;
		String name = player.getItems().getItemName(item).toLowerCase();
		if (description.contains("bucket") || description.contains("potion")
				|| description.contains("dose") || description.contains("jug")
				|| name.contains("jug") || description.contains("bowl")
				|| description.contains("vial") || name.contains("flour")
				|| description.contains("bucket")
				|| description.contains("cup")) {
			player.sendMessage("You empty your " + name + ".");
			player.getItems().deleteItem(item, slot, 1);
			player.getItems().addItem(getEmptyId(player, item), slot, 1);
			return true;
		}
		return false;
	}

	private static int getEmptyId(Client player, int item) {
		String description = Server.itemHandler.ItemList[item].itemDescription;
		String name = player.getItems().getItemName(item).toLowerCase();
		if (description.contains("potion") || description.contains("vial")
				|| description.contains("dose")) {
			return 229;
		}
		if (description.contains("bucket") || description.contains("compost")) {
			return 1925;
		}
		if (description.contains("bowl") || description.contains("curry")) {
			return 1923;
		}
		if (name.contains("jug") || description.contains("jug")) {
			return 1935;
		}
		if (name.contains("flour")) {
			return 1931;
		}
		if (description.contains("cup")) {
			return 1980;
		}
		return -1;
	}

	public static void createAntiVenom(Client player, int itemUsed, int useWith) {
		if (itemUsed == 5954 && useWith == 12934 || itemUsed == 12934 && useWith == 5954) {
			if (player.getItems().getItemCount(5954) > 0 && player.getItems().getItemCount(12934) >= 5) {
				player.getItems().deleteItem(5954, 1);
				player.getItems().deleteItem(12934, 5);
				player.getItems().addItem(12907, 1);
				player.sendMessage("You combine your Antidote++ with your scales.");
			}
		}
	}//wtf ;did you make this? lol nope lol and its not antidotep++ its just antidote++
}
