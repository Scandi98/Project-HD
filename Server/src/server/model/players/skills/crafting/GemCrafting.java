package server.model.players.skills.crafting;

import java.util.HashMap;

import server.Config;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.content.Achievements;
import server.model.players.Client;

public class GemCrafting extends GemData {
	/*
	 * Contains all our crafting details.
	 */
	private static HashMap<Integer, gem_Data> data = new HashMap<Integer, gem_Data>();
	private static final int JEWELCRAFTING = 899;
	public static final int GOLD_BAR = 2357;

	public static gem_Data getData(int id) {
		return data.get(id);
	}
	public enum gem_Data {

		/*
		 * GOLD: Rings, Necklaces, Amulets.
		 */
		gold(2357, -1, -1, 5, 15, 1635, 6, 20, 1654, 8, 30, 1673, 1692),

		/*
		 * SAPPGHIRE: Rings, Necklaces, Amulets.
		 */
		sapphire(1607, 20, 50, 20, 40, 1637, 22, 55, 1656, 24, 65, 1675, 1694),

		/*
		 * EMERALD: Rings, Necklaces, Amulets.
		 */
		emerald(1605, 27, 67, 27, 55, 1639, 29, 60, 1658, 31, 70, 1677, 1696),

		/*
		 * RUBY: Rings, Necklaces, Amulets.
		 */
		ruby(1603, 34, 85, 34, 70, 1641, 40, 75, 1660, 50, 85, 1679, 1698),

		/*
		 * DIAMOND: Rings, Necklaces, Amulets.
		 */
		diamond(1601, 43, 107.5, 43, 85, 1643, 56, 90, 1662, 70, 100, 1681, 1700),

		/*
		 * DRAGON_STONE: Rings, Necklaces, Amulets.
		 */
		dragon_stone(1615, 55, 137.5, 55, 100, 1645, 72, 105, 1664, 80, 150, 1683, 1702),

		/*
		 * ONYX: Rings, Necklaces, Amulets.
		 */
		onyx(6573, 67, 167.5, 67, 115, 6575, 82, 120, 6577, 90, 165, 6579, 6581);

		private int[] mapLocation = new int[13];

		gem_Data(int baseElement, int cutLevel, double cutExp, int ringLevel, double ringExp, int ringFinal, int necklaceLevel, double necklaceExp, int necklaceFinal, int amuletLevel, double amuletExp, int midAmulet, int finalAmulet) {
			this.mapLocation[GEM_SLOT] = baseElement;
			this.mapLocation[GEM_CUT_LEVEL] = cutLevel;
			this.mapLocation[GEM_CUT_EXP] = (int) cutExp;
			this.mapLocation[GEM_CRAFT_RING_LEVEL] = ringLevel;
			this.mapLocation[GEM_CRAFT_RING_EXP] = (int) ringExp;
			this.mapLocation[GEM_CRAFT_RING_FINAL_PRODUCT] = ringFinal;
			this.mapLocation[GEM_CRAFT_NECKLACE_LEVEL] = necklaceLevel;
			this.mapLocation[GEM_CRAFT_NECKLACE_EXP] = (int) necklaceExp;
			this.mapLocation[GEM_CRAFT_NECKLACE_FINAL_PRODUCT] = necklaceFinal;
			this.mapLocation[GEM_CRAFT_AMULET_LEVEL] = amuletLevel;
			this.mapLocation[GEM_CRAFT_AMULET_EXP] = (int) amuletExp * Config.CRAFTING_EXPERIENCE;
			this.mapLocation[GEM_CRAFT_AMULET_MID_PRODUCT] = midAmulet;
			this.mapLocation[GEM_CRAFT_AMULET_FINAL_PRODUCT] = finalAmulet;
		}
	}
	static {
		for (gem_Data pointer : gem_Data.values()) {
			data.put(pointer.mapLocation[0x0], pointer);
		}
	}
	public static void showInterface(Client player, int face) {
		int interfaceType = (face == 1592 ? 0 : face == 1597 ? 1 : face == 1595 ? 2 : -1);
		if (interfaceType < 0)
			return;
		if (player.getItems().playerHasItem(face)) {
			for (int i = 0; i < GemData.craftInterfaceArray[interfaceType].length; i++) {
				//player.getPA().sendFrame34(interfaceFrames[interfaceType][1], i, GemData.craftInterfaceArray[interfaceType][i], 1);
				player.getPA().sendFrame34(GemData.craftInterfaceArray[interfaceType][i], i, interfaceFrames[interfaceType][1], 1);
				//player.getActionSender().sendUpdateItem(i, interfaceFrames[interfaceType][1], new Item(GemData.craftInterfaceArray[interfaceType][i], 1));
			}
			player.getPA().sendItemOnInterface(interfaceFrames[interfaceType][0], 0, -1);
			player.getPA().sendNewString("Choose an item to make.", interfaceFrames[interfaceType][1] - 3);
			//player.getActionSender().sendString("Choose an item to make.", interfaceFrames[interfaceType][1] - 3);
		} else {
			player.getPA().sendItemOnInterface(interfaceFrames[interfaceType][0], 120, 1595);
			player.getPA().sendNewString(interfaceMessage[interfaceType], interfaceFrames[interfaceType][1] - 3);
			for (int i = 0; i < GemData.craftInterfaceArray[interfaceType].length; i++) {
				//	player.getActionSender().sendUpdateItem(i, interfaceFrames[interfaceType][1], new Item(0));
				player.getPA().sendFrame34(-1, i, interfaceFrames[interfaceType][1], 1);
			}
		}
		player.getPA().sendNewString("What would you like to make?", 4226);
	}

	public static void openInterface(Client player) {
		showInterface(player, 1592);
		showInterface(player, 1595);
		showInterface(player, 1597);
		player.getPA().showInterface(4161);
	}

	public static void startCrafter(final Client player, final int gem, final int actionAmount, final int actionType) {
		if (getData(gem) == null || actionAmount < 1 || actionType < 0) {
			return;
		}
		if (!player.getItems().playerHasItem(getData(gem).mapLocation[0x0])) {
			player.getDH().sendStatement("You do not have the required items to do that.");
			return;
		}
		player.getPA().removeAllWindows();
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			private int gemType = gem;
			private int amount = actionAmount;
			private int type = actionType;

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || this.amount-- < 1) {
					container.stop();
					return;
				}
				if (getData(gemType) == null || !player.getItems().playerHasItem(getData(gemType).mapLocation[0]) || !player.getItems().playerHasItem(2357)) {
					container.stop();
					return;
				}
				container.setTick(4);
				player.startAnimation(JEWELCRAFTING);
				switch (type) {

					/*
					 * RINGS
					 */
					case 0 :
						if (player.playerLevel[player.playerCrafting] < getData(gemType).mapLocation[3]) {
							player.getDH().sendStatement("You need a crafting level of " + getData(gemType).mapLocation[3] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getItems().deleteItem2(getData(gemType).mapLocation[0], 1);
						player.getItems().deleteItem2(2357, 1);
						player.getItems().addItem(getData(gemType).mapLocation[5], 1);
						player.sendMessage("You craft a ring.");
						player.getPA().addSkillXP(getData(gemType).mapLocation[4], player.playerCrafting);
						break;
					/*
					 * NECKLACES
					 */
					case 1 :
						if (player.playerLevel[player.playerCrafting] < getData(gemType).mapLocation[6]) {
							player.getDH().sendStatement("You need a Crafting level of " + getData(gemType).mapLocation[6] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getItems().deleteItem2(getData(gemType).mapLocation[0], 1);
						player.getItems().deleteItem2(2357, 1);
						player.getItems().addItem(getData(gemType).mapLocation[8], 1);
						player.sendMessage("You craft a necklace.");
						player.getPA().addSkillXP(getData(gemType).mapLocation[7], player.playerCrafting);
						break;
					/*
					 * AMULETS
					 */
					case 2 :
						if (player.playerLevel[player.playerCrafting] < getData(gemType).mapLocation[9]) {
							player.getDH().sendStatement("You need a Crafting level of " + getData(gemType).mapLocation[9] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getItems().deleteItem2(getData(gemType).mapLocation[0], 1);
						player.getItems().deleteItem2(2357, 1);
						if (player.getItems().playerHasItem(1759)) {
							player.getItems().deleteItem2(1759, 1);
							player.getItems().addItem(getData(gemType).mapLocation[12], 1);
							player.sendMessage("You craft an amulet and attach a string to it.");
						} else {
							player.getItems().addItem(getData(gemType).mapLocation[11], 1);
							player.sendMessage("You craft an amulet.");
						}
						player.getPA().addSkillXP(getData(gemType).mapLocation[10], player.playerCrafting);
						break;
					default :
						container.stop();
						return;
				}
			}
			@Override
			public void stop() {
				player.resetAllActions();
			}
		});
		CycleEventHandler.getSingleton().addEvent(player, player.getSkilling(), 1);
	}

	public static void string(final Client player, final int gemSlot) {
		if (!player.getItems().playerHasItem(1759) || !player.getItems().playerHasItem(stringItems[gemSlot][0])) {
			return;
		}
		player.getItems().deleteItem2(stringItems[gemSlot][0], 1);
		player.getItems().deleteItem2(1759, 1);
		player.getItems().addItem(stringItems[gemSlot][1], 1);
		player.sendMessage("You attach a string to the "+player.getItems().getItemName(stringItems[gemSlot][0]).toLowerCase()+".");
	}
}

