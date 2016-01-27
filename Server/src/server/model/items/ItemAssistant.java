package server.model.items;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.util.Misc;

public class ItemAssistant {

	private Client c;

	public ItemAssistant(Client client) {
		this.c = client;
	}

	/**
	 * Items
	 **/
	public int[] Nests = { 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298,
			5299, 5300, 5301, 5302, 5303, 5304 };

	public void handleNests(int itemId) {
		int reward = Nests[Misc.random(14)];
		addItem(reward, 3 + Misc.random(5));
		deleteItem(itemId, 1);
		c.sendMessage("You search the nest");
	}

	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4974 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public void resetItems(int WriteFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.playerItems.length);
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1) {
				count += c.playerItemsN[j];
			}
		}
		return count;
	}

	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {
			if (c.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + c.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -"
						+ java.lang.Math.abs(c.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendNewString(send, (1675 + i + offset));
		}

	}

	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (Item.itemIsNote[itemID + 1]) {
				if (itemID + 2 == c.playerItems[j])
					count += c.playerItemsN[j];
			}
			if (!Item.itemIsNote[itemID + 1]) {
				if (itemID + 1 == c.playerItems[j]) {
					count += c.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] == itemID + 1) {
				count += c.bankItemsN[j];
			}
		}
		return count;
	}

	public void sendItemsKept() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.itemKeptId.length);
			for (int i = 0; i < c.itemKeptId.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
				}
				if (c.itemKeptId[i] > 0) {
					c.getOutStream().writeWordBigEndianA(c.itemKeptId[i] + 1);
				} else {
					c.getOutStream().writeWordBigEndianA(0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Item kept on death
	 **/

	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
		//	if (specialItem(c.playerItems[i])) {
		//		continue;
		//	}
			if (c.playerItems[i] - 1 > 0) {
				int inventoryItemValue = c.getShops().getItemShopValue(c.playerItems[i] - 1);
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			//if (specialItem(c.playerEquipment[i1])) {
		//		continue;
			//}
			if (c.playerEquipment[i1] > 0) {
				int equipmentItemValue = c.getShops().getItemShopValue(c.playerEquipment[i1]);
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.playerItems[slotId] - 1,getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}

	/**
	 * Reset items kept on death
	 **/

	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * delete all items
	 **/

	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			deleteItem(c.playerItems[i] - 1, getItemSlot(c.playerItems[i] - 1),
					c.playerItemsN[i]);
		}
	}

	/**
	 * Drop all items for your killer
	 **/

	public void dropAllItems() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
			//	if (specialItem(c.playerItems[i])) {
				//	continue;
			//	}
				if (tradeable(c.playerItems[i] - 1)) {
					if (c.killerId != c.playerId) {
						if (c.playerItems[i] == -1 || c.playerItems[i] == 0) {
						} else {
							c.getLogs().writeKillLog(o,
									c.playerItemsN[i] + "x", c.playerItems[i],
									c.getX(), c.getY());
						}
					}
					Server.itemHandler.createGroundItem(o,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.killerId);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerItems[i] - 1),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
			//	if (specialItem(c.playerEquipment[e])) {
			//		continue;
			//	}
				if (tradeable(c.playerEquipment[e])) {
					if (c.killerId != c.playerId) {
						if (c.playerEquipment[e] == -1
								|| c.playerEquipment[e] == 0) {

						} else {
							c.getLogs().writeKillLog(o,
									c.playerEquipmentN[e] + "x",
									c.playerEquipment[e], c.getX(), c.getY());
						}
					}
					Server.itemHandler.createGroundItem(o,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.killerId);
				} else {
					if (specialCase(c.playerEquipment[e]))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerEquipment[e]),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		c.getBag().handleBagOnDeath(c);
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(), 1,
					c.killerId);
		}
	}

	public void dropUntradables() {
		Client o = (Client) PlayerHandler.players[c.killerId];

		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
				if (specialCase(c.playerItems[i] - 1))
					Server.itemHandler.createGroundItem(o, 995, c.getX(),
							c.getY(), getUntradePrice(c.playerItems[i] - 1),
							c.killerId);
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
				if (specialCase(c.playerEquipment[e]))
					Server.itemHandler.createGroundItem(o, 995, c.getX(),
							c.getY(), getUntradePrice(c.playerEquipment[e]),
							c.killerId);
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(), 1,
					c.killerId);
		}
	}

	public void dropAllIronMan() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
				if (!tradeable(c.playerItems[i])) {
					Server.itemHandler.createGroundItem(c,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
				if (!tradeable(c.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(c,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(), 1,
					c.killerId);
		}
	}

	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	public void handleSpecialPickup(int itemId) {
		// c.sendMessage("My " + getItemName(itemId) +
		// " has been recovered. I should talk to the void knights to get it back.");
		// c.getItems().addToVoidList(itemId);
	}

	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			c.voidStatus[0]++;
			break;
		case 2520:
			c.voidStatus[1]++;
			break;
		case 2522:
			c.voidStatus[2]++;
			break;
		case 2524:
			c.voidStatus[3]++;
			break;
		case 2526:
			c.voidStatus[4]++;
			break;
		}
	}

	public boolean tradeable(int itemId) {
		for (int j = 0; j < Config.ITEM_TRADEABLE.length; j++) {
			if (itemId == Config.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}
	
	public boolean specialItem(int itemId) {
		for (int j = 0; j < Config.ITEMS_KEPT_ON_DEATH.length; j++) {
			if (itemId == Config.ITEMS_KEPT_ON_DEATH[j])
				return false;
		}
		return true;
	}

	/**
	 * Add Item
	 **/
	public boolean addItem(int item, int amount) {
		// synchronized(c) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1))
						&& Item.itemStackable[item] && (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT)
							&& ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.sendMessage("Your inventory is to full for this.");
						return false;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}

	public void createGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getItemY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getItemX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(item.getItemId());
		c.getOutStream().writeWord(item.getItemAmount());
		c.getOutStream().writeByte(0);
		c.flushOutStream();
	}

	public void removeGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getItemY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getItemX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(item.getItemId());
		c.flushOutStream();
	}

	public boolean addItem(int item, int slot, int amount) {
		// synchronized(c) {
		if (amount < 1) {
			amount = 1;
		}
		if (item < 0 || slot < 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			if ((c.playerItems[slot] == (item + 1)) && Item.itemStackable[item]
					&& (c.playerItems[slot] > 0)) {
				c.playerItems[slot] = (item + 1);
				if (((c.playerItemsN[slot] + amount) < Config.MAXITEM_AMOUNT)
						&& ((c.playerItemsN[slot] + amount) > -1)) {
					c.playerItemsN[slot] += amount;
				} else {
					c.playerItemsN[slot] = Config.MAXITEM_AMOUNT;
				}
				if (c.getOutStream() != null && c != null) {
					c.getOutStream().createFrameVarSizeWord(34);
					c.getOutStream().writeWord(3214);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(c.playerItems[slot]);
					if (c.playerItemsN[slot] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord(c.playerItemsN[slot]);
					} else {
						c.getOutStream().writeByte(c.playerItemsN[slot]);
					}
					c.getOutStream().endFrameVarSizeWord();
					c.flushOutStream();
				}
				slot = 30;
				return true;
			}
			if (c.playerItems[slot] <= 0) {
				c.playerItems[slot] = item + 1;
				if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
					c.playerItemsN[slot] = 1;
					if (amount > 1) {
						c.getItems().addItem(item, amount);
						return true;
					}
				} else {
					c.playerItemsN[slot] = Config.MAXITEM_AMOUNT;
				}
				resetItems(3214);
				return true;
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}

	public String itemType(int item) {
		if (Item.playerCape(item)) {
			return "cape";
		}
		if (Item.playerBoots(item)) {
			return "boots";
		}
		if (Item.playerGloves(item)) {
			return "gloves";
		}
		if (Item.playerShield(item)) {
			return "shield";
		}
		if (Item.playerAmulet(item)) {
			return "amulet";
		}
		if (Item.playerArrows(item)) {
			return "arrows";
		}
		if (Item.playerRings(item)) {
			return "ring";
		}
		if (Item.playerHats(item)) {
			return "hat";
		}
		if (Item.playerLegs(item)) {
			return "legs";
		}
		if (Item.playerBody(item)) {
			return "body";
		}
		return "weapon";
	}

	/**
	 * Bonuses
	 **/

	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic",
			"Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength",
			"Prayer" };

	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				for (int j = 0; j < Config.ITEM_LIMIT; j++) {
					if (Server.itemHandler.ItemList[j] != null) {
						if (Server.itemHandler.ItemList[j].itemId == c.playerEquipment[i]) {
							for (int k = 0; k < c.playerBonus.length; k++) {
								c.playerBonus[k] += Server.itemHandler.ItemList[j].Bonuses[k];
							}
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Check all slots and determine whether or
	 * not a slot is accompanied by that item
	 */
	public boolean isWearingItem(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check all slots and determine the amount
	 * of said item worn in that slot
	 */
	public int getWornItemAmount(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return c.playerEquipmentN[i];
			}
		}
		return 0;
	}

	/**
	 * Wear Item
	 **/

	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");// it doesnt do it for zamorakian
		WeaponName2 = WeaponName2.replaceAll("Zamorakian", "");
		WeaponName2 = WeaponName2.trim();
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendNewString(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")
				|| WeaponName.endsWith("Abyssal tentacle")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")
				|| WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff")
				|| WeaponName.endsWith("staff") || WeaponName.endsWith("wand")
				|| WeaponName.startsWith("Toxic staff")) {
			c.setSidebarInterface(0, 328); // spike, impale, smash, block
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart")
				|| WeaponName2.startsWith("knife")
				|| WeaponName2.startsWith("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")
				|| WeaponName.startsWith("Toxic")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger")
				|| WeaponName2.contains("sword")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe")
				|| WeaponName2.startsWith("battleaxe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 8463);
		} else if (WeaponName2.startsWith("spear")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 3799);

		} else if (c.playerEquipment[c.playerWeapon] == 4153
				|| c.playerEquipment[c.playerWeapon] == 4755
				|| c.playerEquipment[c.playerWeapon] == 13902) {
			c.setSidebarInterface(0, 425); // war hamer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 428);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 2426);
		}

	}

	/**
	 * Weapon Requirements
	 **/

	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}

		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;
		}
		if (itemName.contains("trident")) {
			c.magicLevelReq = 75;
		}
		if (itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("rune c'bow")) {
			c.rangeLevelReq = 61;
		}
		if (itemName.contains("ranger boots")) {
			c.rangeLevelReq = 60;
		}
		if (itemName.contains("black d'hide")) {
			c.rangeLevelReq = 70;
		}

		if (itemName.contains("anchor")) {
			c.strengthLevelReq = 50;
			c.attackLevelReq = 50;
		}
		if (itemName.contains("red d'hide")) {
			c.rangeLevelReq = 60;
		}
		if (itemName.contains("blue d'hide")) {
			c.rangeLevelReq = 50;
		}
		if (itemName.contains("green d'hide")) {
			c.rangeLevelReq = 40;
		}
		if (itemName.contains("initiate")) {
			c.defenceLevelReq = 20;
		}
		if (itemName.contains("slayer")) {
			c.defenceLevelReq = 10;
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("d'hide")) {
			if (!itemName.contains("chaps")) {
				if (!itemName.contains("black") && !itemName.contains("blue")
						&& !itemName.contains("red")) {
					c.defenceLevelReq = 40;
					c.rangeLevelReq = 40;
				} else if (!itemName.contains("black")
						&& !itemName.contains("red")) {
					c.defenceLevelReq = 40;
					c.rangeLevelReq = 50;
				} else if (!itemName.contains("blue")
						&& !itemName.contains("black")) {
					c.defenceLevelReq = 40;
					c.rangeLevelReq = 60;
				} else if (!itemName.contains("blue")
						&& !itemName.contains("red")) {
					c.defenceLevelReq = 40;
					c.rangeLevelReq = 70;
				}
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("granite shield")) {
			if (!itemName.contains("maul")) {
				c.defenceLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("granite maul")) {
			if (!itemName.contains("shield")) {
				c.attackLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("warrior")) {
			if (!itemName.contains("ring")) {
				c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragonfire")) {

			c.defenceLevelReq = 75;

		}
		if (itemName.contains("dragon kite")) {

			c.defenceLevelReq = 75;
		}
		if (itemName.contains("enchanted")) {

			c.defenceLevelReq = 40;

		}
		if (itemName.contains("guilded")) {

			c.defenceLevelReq = 40;

		}
		if (itemName.contains("adam")) {

			c.defenceLevelReq = 30;

		}
		if (itemName.contains("dragon dagger")) {

			c.attackLevelReq = 60;
		}
		if (itemName.contains("drag dagger")) {

			c.attackLevelReq = 60;
		}
		if (itemName.contains("lime whip")) {

			c.attackLevelReq = 70;
		}
		if (itemName.contains("ancient")) {

			c.attackLevelReq = 50;
		}
		if (itemName.contains("hardleather")) {

			c.defenceLevelReq = 10;
		}
		if (itemName.contains("dragon chain")) {

			c.defenceLevelReq = 60;
		}

		if (itemName.contains("studded")) {

			c.defenceLevelReq = 20;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("body")
					&& !itemName.contains("shield")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 70;
				c.attackLevelReq = 70;
			} else {
				c.magicLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("armadyl")) {
			if (itemName.contains("godsword")) {
				c.attackLevelReq = 75;
			} else {
				c.rangeLevelReq = c.defenceLevelReq = 65;
			}
		}
		if (itemName.contains("saradomin")) {
			if (itemName.contains("sword")) {
				c.attackLevelReq = 70;
			}
			if (itemName.contains("crozier")) {
				c.attackLevelReq = 1;
				if (itemName.contains("robe")) {
					c.attackLevelReq = 1;

				} else {
					c.defenceLevelReq = 40;

				}
			}
		}
		if (itemName.contains("3rd age")) {
			c.defenceLevelReq = 65;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 12612:
			c.magicLevelReq = 10;
			break;
		case 6528:
			c.strengthLevelReq = 60;
			break;
		case 11037:
			c.attackLevelReq = 40;
			break;
		case 12440:
			c.attackLevelReq = 60;
			c.defenceLevelReq = 60;
			break;
		case 11283:
			c.defenceLevelReq = 75;
			break;
		case 11804:
			c.attackLevelReq = 75;
			break;
		case 13899:
		case 13902:
			c.attackLevelReq = 78;
			break;
		case 13887:
		case 13893:
		case 13884:
		case 13890:
		case 13896:
			c.defenceLevelReq = 78;
			break;
		case 12006:
			c.attackLevelReq = 75;
			break;
		case 12817:
		case 12821:
		case 12825:
			c.defenceLevelReq = 75;
			return;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			c.defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			c.rangeLevelReq = 60;
			break;
		case 6524:
		case 15006:
		case 15007:
			c.defenceLevelReq = 60;
			break;
		case 10380:
		case 10372:
		case 10368:
		case 10382:
		case 10384:
		case 10374:
		case 10388:
		case 10390:
			c.rangeLevelReq = 40;
			c.defenceLevelReq = 70;
			break;
		case 12569:
		case 2577:
		case 2581:
			c.rangeLevelReq = 40;
			break;
		case 12931:
			c.defenceLevelReq = 78;
			break;
		case 12926:
			c.rangeLevelReq = 75;
			break;
		case 6128:
		case 6130:
		case 6145:
		case 2129:
			c.defenceLevelReq = 40;
			return;
		case 11284:
			c.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.magicLevelReq = 60;
			break;
		case 861:
			c.rangeLevelReq = 50;
			break;
		case 10828:
			c.defenceLevelReq = 55;
			break;
		case 11832:
		case 11834:
		case 11836:
			c.defenceLevelReq = 65;
			break;
		case 6328:
			c.defenceLevelReq = 30;
			c.rangeLevelReq = 30;
			break;
		case 4097:
		case 4107:
		case 4117:
			c.defenceLevelReq = 25;
			c.magicLevelReq = 40;
			break;
		case 2412:
		case 2413:
		case 2414:
			c.magicLevelReq = 60;
			break;
		case 3751:
		case 3753:
			c.defenceLevelReq = 45;
			break;
		case 3749:
		case 3755:
			c.defenceLevelReq = 45;
			break;
		case 10400:
		case 10402:
		case 2635:
			c.defenceLevelReq = 0;
			break;
		case 7462:
		case 7461:
			c.defenceLevelReq = 40;
			break;
		case 8846:
			c.defenceLevelReq = 5;
			break;
		case 8847:
			c.defenceLevelReq = 10;
			break;
		case 8848:
			c.defenceLevelReq = 20;
			break;
		case 8849:
			c.defenceLevelReq = 30;
			break;
		case 8850:
			c.defenceLevelReq = 40;

		case 7460:
			c.defenceLevelReq = 20;
			break;
		case 868:
			c.rangeLevelReq = 61;
			break;
		case 2530:
			c.rangeLevelReq = 70;
			break;
		case 837:
		case 9185:
			c.rangeLevelReq = 61;
			break;
		case 15000:
		case 15001:
		case 15002:
		case 15003:
		case 15004:
		case 4151: // if you don't want to use names
			c.attackLevelReq = 70;
			return;
		case 11791:
		case 12904:
			c.attackLevelReq = 75;
			c.magicLevelReq = 75;
			return;
		case 6724: // seercull
			c.rangeLevelReq = 60; // idk if that is correct
			return;
		case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * two handed weapon check
	 **/
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil")
				|| itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow")
				|| itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("anchor")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 5608:
		case 5609:
		case 12809:
		case 7671:
		case 7673:
		case 6724: // seercull
		case 11838:
		case 4153:
		case 6528:
		case 14484:
		case 10887:
		case 12926:
			return true;
		}
		return false;
	}

	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and
	 * removes the spec bars from weapons which don't require them
	 **/

	public void addSpecialBar(int weapon) {
		switch (weapon) {
		case 15000:
		case 15001:
		case 15002:
		case 15003:
		case 15004:
		case 4151: // whip
		case 12006:
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 11235:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;

		case 4587: // dscimmy
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 3204: // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;

		case 4153: // gmaul
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 1249: // dspear
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 11802:
		case 11806:
		case 11808:
		case 11838:
		case 11804:
		case 12809:
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 1434: // dragon mace
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;
		case 11785:
			/*
			 * c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendNewString(WeaponName, 1767);
			 */
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Specials bar filling amount
	 **/

	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		c.sendMessage(specAmount + ":specialattack:");
		// try that maybe
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text and what to highlight or blackout
	 **/

	public void updateSpecialBar() {
		if (c.usingSpecial) {
			c.getPA()
					.sendNewString(
							""
									+ (c.specAmount >= 2 ? "@yel@S P"
											: "@bla@S P")
									+ ""
									+ (c.specAmount >= 3 ? "@yel@ E"
											: "@bla@ E")
									+ ""
									+ (c.specAmount >= 4 ? "@yel@ C I"
											: "@bla@ C I")
									+ ""
									+ (c.specAmount >= 5 ? "@yel@ A L"
											: "@bla@ A L")
									+ ""
									+ (c.specAmount >= 6 ? "@yel@  A"
											: "@bla@  A")
									+ ""
									+ (c.specAmount >= 7 ? "@yel@ T T"
											: "@bla@ T T")
									+ ""
									+ (c.specAmount >= 8 ? "@yel@ A"
											: "@bla@ A")
									+ ""
									+ (c.specAmount >= 9 ? "@yel@ C"
											: "@bla@ C")
									+ ""
									+ (c.specAmount >= 10 ? "@yel@ K"
											: "@bla@ K"), c.specBarId);
		} else {
			c.getPA().sendNewString("@bla@S P E C I A L  A T T A C K",
					c.specBarId);
		}
	}

	/**
	 * Wear Item
	 **/

	public boolean wearItem(int wearID, int slot) {
		if (!c.getItems().playerHasItem(wearID, 1, slot)) {
			// add a method here for logging cheaters(If you want)
			return false;
		}
		// synchronized(c) {
		int targetSlot = 0;
		boolean canWearItem = true;
		if (wearID == 747 && c.playerRights != 3) {
			c.sendMessage("Nope.");
			return false;
		}
		if (wearID == 5733) {
			return false;
		}
		if (c.playerItems[slot] == (wearID + 1)) {
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			targetSlot = Item.targetSlots[wearID];
			switch (wearID) {
			case 12373:
			case 15005:
			case 15004:
			case 15003:
			case 15002:
			case 15001:
			case 15000:
			case 15103:
			case 12006:
			case 12008:
			case 13899:
			case 13902:
			case 12926:
			case 7671:
			case 7673:
			case 12607:
			case 12439:
			case 12426:
			case 12422:
			case 12797:
			case 12809:
				targetSlot = 3;// main hand
				break;
			/* Gloves */
			case 12498:
			case 12506:
			case 12490:
			case 10376:
			case 10336:
			case 10368:
			case 9922:
			case 8842:
			case 11118:
				targetSlot = 9;
				break;
			/* Rings */
			case 12601:
			case 12605:
			case 12603:
			case 15220:
			case 15020:
			case 15018:
			case 15019:
			case 12785:
				targetSlot = 12;
				break;
			/* Arrows */
			case 9144:
				targetSlot = 13;
				break;
			/* Capes */
			case 5607:
			case 13123:
			case 13122:
			case 13121:
			case 13124:
			case 12437:
			case 15086:
			case 12742:
			case 4514:
			case 4516:
			case 9813:
			case 9747:
			case 9748:
			case 9750:
			case 9790:
			case 9751:
			case 9753:
			case 9754:
			case 9756:
			case 9757:
			case 9759:
			case 9760:
			case 9762:
			case 9763:
			case 9765:
			case 9766:
			case 9768:
			case 9769:
			case 9771:
			case 9772:
			case 9774:
			case 9775:
			case 10446:
			case 10448:
			case 10450:
			case 9777:
			case 9778:
			case 9780:
			case 9781:
			case 9783:
			case 9784:
			case 9786:
			case 9787:
			case 9792:
			case 9793:
			case 9795:
			case 9796:
			case 9798:
			case 9799:
			case 9801:
			case 9802:
			case 9804:
			case 9805:
			case 9807:
			case 9808:
			case 9810:
			case 9811:
			case 10499:
				targetSlot = 1;
				break;

			/* Arrows */
			case 9244:
				targetSlot = 13;
				break;

			/* Boots */
			case 6666:
			case 15037:
			case 14605:
			case 11019:
			case 9921:
			case 11728:
			case 10839:
				targetSlot = 10;
				break;

			/* Legs */
			case 1033:
			case 12482:
			case 12484:
			case 12462:
			case 12464:
			case 12472:
			case 12474:
			case 12510:
			case 12502:
			case 12494:
			case 12416:
			case 12415:
			case 12459:
			case 12421:
			case 10396:
			case 4510:
			case 4505:
			case 4070:
			case 11726:
			case 11722:
			case 9678:
			case 9923:
			case 9676:
			case 10394:
			case 13893:
			case 8840:
			case 15035:
			case 10332:
			case 15036:
			case 14603:
			case 14938:
			case 14077:
			case 10346:
			case 10372:
			case 10838:
			case 11022:
			case 10388:
			case 10380:
			case 10340:
			case 15425:
			case 13360:
			case 13352:
			case 13346:
			case 13890:
			case 12395:
				targetSlot = 7;
				break;

			/* Amulets */
			case 15008:
			case 10362:
			case 10364:
			case 10366:
			case 6861:
			case 6859:
			case 6863:
			case 9470:
			case 6857:
			case 10344:
			case 11128:
			case 12436:
				targetSlot = 2;
				break;

			/* Shields */
			case 12488:
			case 12468:
			case 12478:
			case 12612:
			case 12829:
			case 12831:
			case 12806:
			case 12807:
			case 12440:
			case 15006:
			case 15007:
			case 2667:
			case 3844:
			case 8850:
			case 8849:
			case 8848:
			case 8847:
			case 8846:
			case 8845:
			case 8844:
			case 11283:
			case 10352:
			case 12825:
			case 12821:
			case 12817:
				targetSlot = 5;
				break;

			/* Bodies */
			case 12480:
			case 12460:
			case 12470:
			case 12508:
			case 1035:
			case 12500:
			case 12492:
			case 12414:
			case 12458:
			case 12420:
			case 12393:
			case 9944:
			case 6129:
			case 7390:
			case 6176:
			case 2661:
			case 12596:
			case 10378:
			case 4504:
			case 4069:
			case 13884:
			case 2662:
			case 10551:
			case 10348:
			case 9674:
			case 4509:
			case 10837:
			case 14936:
			case 7392:
			case 15034:
			case 10386:
			case 10370:
			case 11720:
			case 10330:
			case 15423:
			case 14076:
			case 11020:
			case 14595:
			case 8839:
			case 10338:
			case 13348:
			case 13354:
			case 13358:
			case 9924:
			case 11724:
			case 13887:
				targetSlot = 4;
				break;

			/* Helms */
			case 12486:
			case 12466:
			case 12476:
			case 12504:
			case 12512:
			case 12457:
			case 12419:
			case 2635:
			case 9946:
			case 9945:
			case 4502:
			case 10452:
			case 10454:
			case 10456:
			case 2649:
			case 2645:
			case 2647:
			case 2633:
			case 2636:
			case 2637:
			case 10392:
			case 10398:
			case 10382:
			case 2665:
			case 13263:
			case 9920:
			case 10507:
			case 10836:
			case 10828:
			case 9672:
			case 10334:
			case 10350:
			case 10390:
			case 11718:
			case 10374:
			case 11021:
			case 15422:
			case 12399:
			case 11919:
			case 12251:
			case 15033:
			case 9925:
			case 13362:
			case 11663:
			case 11664:
			case 11665:
			case 13355:
			case 13350:
			case 10342:
			case 1037:
			case 11335:
			case 10548:
			case 9749:
			case 9752:
			case 9755:
			case 9758:
			case 9761:
			case 9764:
			case 9767:
			case 9770:
			case 9773:
			case 9776:
			case 9779:
			case 9782:
			case 9785:
			case 9788:
			case 9791:
			case 9794:
			case 9797:
			case 9800:
			case 9803:
			case 9806:
			case 9809:
			case 9812:
			case 11862:
			case 11863:
			case 11847:
			case 12397:
				targetSlot = 0;
				break;

			/* Boots */
			case 11732:
				targetSlot = 10;
				break;
			}
			/*
			 * if(itemType(wearID).equalsIgnoreCase("cape")) { targetSlot=1; }
			 * else if(itemType(wearID).equalsIgnoreCase("hat")) { targetSlot=0;
			 * } else if(itemType(wearID).equalsIgnoreCase("amulet")) {
			 * targetSlot=2; } else
			 * if(itemType(wearID).equalsIgnoreCase("arrows")) { targetSlot=13;
			 * } else if(itemType(wearID).equalsIgnoreCase("body")) {
			 * targetSlot=4; } else
			 * if(itemType(wearID).equalsIgnoreCase("shield")) { targetSlot=5; }
			 * else if(itemType(wearID).equalsIgnoreCase("legs")) {
			 * targetSlot=7; } else
			 * if(itemType(wearID).equalsIgnoreCase("gloves")) { targetSlot=9; }
			 * else if(itemType(wearID).equalsIgnoreCase("boots")) {
			 * targetSlot=10; } else
			 * if(itemType(wearID).equalsIgnoreCase("ring")) { targetSlot=12; }
			 * else { targetSlot = 3; }
			 */

			if (c.duelRule[11] && targetSlot == 0) {
				c.sendMessage("Wearing hats has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[12] && targetSlot == 1) {
				c.sendMessage("Wearing capes has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[13] && targetSlot == 2) {
				c.sendMessage("Wearing amulets has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[14] && targetSlot == 3) {
				c.sendMessage("Wielding weapons has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[15] && targetSlot == 4) {
				c.sendMessage("Wearing bodies has been disabled in this duel!");
				return false;
			}
			if ((c.duelRule[16] && targetSlot == 5)
					|| (c.duelRule[16] && is2handed(getItemName(wearID)
							.toLowerCase(), wearID))) {
				c.sendMessage("Wearing shield has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[17] && targetSlot == 7) {
				c.sendMessage("Wearing legs has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[18] && targetSlot == 9) {
				c.sendMessage("Wearing gloves has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[19] && targetSlot == 10) {
				c.sendMessage("Wearing boots has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[20] && targetSlot == 12) {
				c.sendMessage("Wearing rings has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[21] && targetSlot == 13) {
				c.sendMessage("Wearing arrows has been disabled in this duel!");
				return false;
			}
			if (c.dueling == true && targetSlot == 12) {
				c.sendMessage("Wearing rings has been disabled in duel mode!");
				return false;
			}
			if (Config.itemRequirements) {
				if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5
						|| targetSlot == 4 || targetSlot == 0
						|| targetSlot == 9 || targetSlot == 10) {
					if (c.defenceLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
							c.sendMessage("You need a defence level of "
									+ c.defenceLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
				}
				if (targetSlot == 3) {
					if (c.attackLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[0]) < c.attackLevelReq) {
							c.sendMessage("You need an attack level of "
									+ c.attackLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.strengthLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[2]) < c.strengthLevelReq) {
							c.sendMessage("You need an strength level of "
									+ c.strengthLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
				}
			}

			if (!canWearItem) {
				return false;
			}

			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}

			if (targetSlot == c.playerWeapon) {
				c.autocasting = false;
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);
			}

			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				if (toRemove == 12926) {
					c.rangeItemUsed = 0;
				}
				int toRemoveN = c.playerEquipmentN[targetSlot];
				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					c.playerItems[slot] = 0;
					c.playerItemsN[slot] = 0;
					if (toRemove > 0 && toRemoveN > 0)
						if (c.isArrow(toRemove)) {
							addItem(toRemove, toRemoveN);
						} else {
							addItem(toRemove, slot, toRemoveN);
						}
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(
							getItemName(c.playerEquipment[c.playerWeapon])
									.toLowerCase(),
							c.playerEquipment[c.playerWeapon]);
					if (wearing2h) {
						toRemove = c.playerEquipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.playerEquipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						updateSlot(c.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(),
							wearID);
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (freeSlots() > 0) {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(c.playerEquipment[c.playerShield],
										c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							updateSlot(c.playerShield);
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				resetItems(3214);
			}
			if (targetSlot == 3) {
				c.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}//here
			
			sendWeapon(c.playerEquipment[c.playerWeapon],
					getItemName(c.playerEquipment[c.playerWeapon]));
			resetBonus();
			getBonus();
			writeBonus();
			c.getCombat().getPlayerAnimIndex(
					c.getItems().getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}

	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);

			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else {
				c.getOutStream().writeByte(wearAmount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = wearAmount;
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getCombat().getPlayerAnimIndex(
					c.getItems().getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
		// }
	}

	public void updateSlot(int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
			if (c.playerEquipmentN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	/**
	 * Remove Item
	 **/
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {
				if (wearID == 4516 || wearID == 4514 && c.inClanWarsGame) {
					c.sendMessage("You cannot remove your cape in this minigame!");
					return;
				}
				if (wearID == 7927 || wearID == 6583) {
					updateSlot(c.playerRing);
				}
				if (wearID == 12926) {
					c.rangeItemUsed = 0;
				}
				if (wearID == 11907) {
					c.autocasting = false;
					c.spellId = 0;
					c.autocastId = 0;
					c.usingMagic = false;
				}
				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[c.playerWeapon],
							getItemName(c.playerEquipment[c.playerWeapon]));
					resetBonus();
					getBonus();
					writeBonus();
					updateSlot(slot);
					c.getCombat().getPlayerAnimIndex(
							c.getItems()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				}
			}
		}
	}

	/**
	 * BANK
	 */

	public void rearrangeBank() {
		int highestSlot = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankingItems[i] != 0) {
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}
		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankingItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.bankingItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankingItems[j - spots] = c.bankingItems[j];
							c.bankingItemsN[j - spots] = c.bankingItemsN[j];
							stop = true;
							c.bankingItems[j] = 0;
							c.bankingItemsN[j] = 0;
						}
					}
				}
			}
		}
	}

	public void itemOnInterface(int id, int amount) {
		// synchronized(c) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(2274);
		c.getOutStream().writeWord(1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord_v2(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().writeWordBigEndianA(id);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void resetBank() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382);
			c.getOutStream().writeWord(Config.BANK_SIZE);
			for (int i = 0; i < Config.BANK_SIZE; i++) {
				if (c.bankingItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.bankingItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.bankingItemsN[i]);
				}
				if (c.bankingItemsN[i] < 1) {
					c.bankingItems[i] = 0;
				}
				if (c.bankingItems[i] > Config.ITEM_LIMIT || c.bankingItems[i] < 0) {
					c.bankingItems[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.bankingItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.getPA().sendNewString(Integer.toString(calculateRemaindingBankSlots()), 22033);
			c.getPA().sendNewString(Integer.toString(Config.BANK_SIZE), 22034);
		}
	}

	public void resetBankSearch() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382); // bank
			c.getOutStream().writeWord(Config.BANK_SIZE);
			for (int i = 0; i < Config.BANK_SIZE; i++) {
				if (c.foundItemsAmount[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.foundItemsAmount[i]);
				} else {
					c.getOutStream().writeByte(c.foundItemsAmount[i]);
				}
				if (c.foundItemsAmount[i] < 1) {
					c.foundItemsId[i] = 0;
				}
				if (c.foundItemsId[i] > Config.ITEM_LIMIT
						|| c.foundItemsId[i] < 0) {
					c.foundItemsId[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.foundItemsId[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void fromBankSearch(int newSlot, int oldSlot, int amount) {
		if (amount > 0) {
			if (oldSlot > 0) {
				if (!c.takeAsNote) {
					if (c.foundItemsAmount[newSlot] >= amount) {
						if (addItem((c.foundItemsId[newSlot]) - 1, amount)) {
							c.foundItemsAmount[newSlot] -= amount;
							c.bankItemsN[oldSlot - 1] -= amount;
							resetBankSearch();
							c.getItems().resetItems(5064);
						}
					}
				} else {
					if (c.foundItemsAmount[newSlot] >= amount) {
						if (addItem((c.foundItemsId[newSlot]), amount)) {
							c.foundItemsAmount[newSlot] -= amount;
							c.bankItemsN[oldSlot - 1] -= amount;
						}
					}
					resetBankSearch();
					c.getItems().resetItems(5064);
				}
			} else {
				if (!c.takeAsNote) {
					if (c.foundItemsAmount[newSlot] >= amount) {
						if (addItem((c.foundItemsId[newSlot]) - 1, amount)) {
							c.foundItemsAmount[newSlot] -= amount;
							c.bankItemsN[oldSlot] -= amount;
							resetBankSearch();
							c.getItems().resetItems(5064);
						}
					}
				} else {
					if (c.foundItemsAmount[newSlot] >= amount) {
						if (addItem((c.foundItemsId[newSlot]), amount)) {
							c.foundItemsAmount[newSlot] -= amount;
							c.bankItemsN[oldSlot] -= amount;
							resetBankSearch();
							c.getItems().resetItems(5064);
						}

					}

				}
			}
		}
	}

	public int calculateRemaindingBankSlots() {
		int tab0 = 0;
		int tab1 = 0;
		int tab2 = 0;
		int tab3 = 0;
		int tab4 = 0;
		int tab5 = 0;
		int tab6 = 0;
		int tab7 = 0;
		int tab8 = 0;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0) {
				tab0++;
			}
		}
		tab0 = Config.BANK_SIZE - tab0;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems1[i] <= 0) {
				tab1++;
			}
		}
		tab1 = Config.BANK_SIZE - tab1;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems2[i] <= 0) {
				tab2++;
			}
		}
		tab2 = Config.BANK_SIZE - tab2;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems3[i] <= 0) {
				tab3++;
			}
		}
		tab3 = Config.BANK_SIZE - tab3;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems4[i] <= 0) {
				tab4++;
			}
		}
		tab4 = Config.BANK_SIZE - tab4;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems5[i] <= 0) {
				tab5++;
			}
		}
		tab5 = Config.BANK_SIZE - tab5;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems6[i] <= 0) {
				tab6++;
			}
		}
		tab6 = Config.BANK_SIZE - tab6;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems7[i] <= 0) {
				tab7++;
			}
		}
		tab7 = Config.BANK_SIZE - tab7;

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems8[i] <= 0) {
				tab8++;
			}
		}
		tab8 = Config.BANK_SIZE - tab8;
		return tab0 + tab1 + tab2 + tab3 + tab4 + tab5 + tab6 + tab7 + tab8;
	}

	public void resetTempItems() {// whats a temp item.. idk but who cares 
		// synchronized(c) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > -1) {
				itemCount = i;
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5064);
		c.getOutStream().writeWord(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (c.playerItemsN[i] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
			} else {
				if (c.playerItemsN[i] != -1)
					c.getOutStream().writeByte(c.playerItemsN[i]);
			}
			if (c.playerItems[i] > Config.ITEM_LIMIT || c.playerItems[i] < 0) {
				c.playerItems[i] = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public boolean bankSearchItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade)
			return false;
		if (c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				int toBankSearchSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.foundItemsId[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSearchSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.foundItemsId[i] <= 0) {
							toBankSearchSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = c.playerItems[fromSlot];

					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
						c.foundItemsAmount[toBankSearchSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBankSearch();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
						c.foundItemsAmount[toBankSearchSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBankSearch();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				int toBankSearchSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.foundItemsId[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSearchSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.foundItemsId[i] <= 0) {
							toBankSearchSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = c.playerItems[firstPossibleSlot];
							c.foundItemsId[toBankSearchSlot] = c.playerItems[firstPossibleSlot];
							c.bankItemsN[toBankSlot] += 1;
							c.foundItemsAmount[toBankSearchSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBankSearch();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							c.foundItemsAmount[toBankSearchSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBankSearch();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				int toBankSearchSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.foundItemsId[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.foundItemsId[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
						c.foundItemsAmount[toBankSearchSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBankSearch();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
						c.foundItemsAmount[toBankSearchSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBankSearch();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				int toBankSearchSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.foundItemsId[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSearchSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}

					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.foundItemsId[i] <= 0) {
							toBankSearchSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.foundItemsId[toBankSearchSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.bankItemsN[toBankSlot] += 1;
							c.foundItemsAmount[toBankSearchSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBankSearch();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							c.foundItemsAmount[toBankSearchSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBankSearch();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (!c.bankCheck && !(c.playerRights != 2)) {
			c.sendMessage("Go to a bank to bank items!");
			c.getPA().closeAllWindows();
			return false;
		}
		for (int i = 0; i < c.easterEggs.length; i++) {
			if (itemID == c.easterEggs[i]) {
				c.sendMessage("You cannot bank this item");
				return false;
			}
		}
		if (itemID == 11941 && c.getBag().getSizeOfLootingBag() > 0) {
			c.sendMessage("You can't bank your bag with items in it.");
			return false;
		}
		c.getPA().openUpBank(getTabforItem(itemID)); // Move to tab item is
		// in
		// before adding
		if (c.inTrade) {
			c.sendMessage("You can't store items while trading!");
			return false;
		}
		if (c.getPA().getBankItems(c.bankingTab) >= 350) {
			c.sendMessage("You can't store any more items in this tab!");
			return false;
		}
		if (c.playerItems[fromSlot] <= 0 || c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankingItems[toBankSlot] = c.playerItems[fromSlot];
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItems[toBankSlot] = c.playerItems[firstPossibleSlot];
							c.bankingItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankingItems[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItems[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.bankingItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItemsN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}
	
	public boolean bankAll(int itemID, int fromSlot, int amount) {
		if (!c.bankCheck && !(c.playerRights != 2)) {
			c.sendMessage("Go to a bank to bank items!");
			c.getPA().closeAllWindows();
			return false;
		}
		for (int i = 0; i < c.easterEggs.length; i++) {
			if (itemID == c.easterEggs[i]) {
				c.sendMessage("You cannot bank this item");
				return false;
			}
		}
		if (itemID == 11941 && c.getBag().getSizeOfLootingBag() > 0) {
			c.sendMessage("You can't bank your bag with items in it.");
			return false;
		}
		c.getPA().openUpBank(getTabforItem(itemID)); // Move to tab item is
		// in
		// before adding
		if (c.inTrade) {
			c.sendMessage("You can't store items while trading!");
			return false;
		}
		if (c.getPA().getBankItems(c.bankingTab) >= 350) {
			c.sendMessage("You can't store any more items in this tab!");
			return false;
		}
		if (c.playerItems[fromSlot] <= 0 || c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankingItems[toBankSlot] = c.playerItems[fromSlot];
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItemBank((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItemBank((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItems[toBankSlot] = c.playerItems[firstPossibleSlot];
							c.bankingItemsN[toBankSlot] += 1;
							deleteItemBank((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItemsN[toBankSlot] += 1;
							deleteItemBank((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					c.bankingItems[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItemBank((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankingItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (c.bankingItemsN[toBankSlot] + amount) > -1) {
						c.bankingItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItemBank((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (c.bankingItems[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (c.bankingItems[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItems[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.bankingItemsN[toBankSlot] += 1;
							deleteItemBank((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankingItemsN[toBankSlot] += 1;
							deleteItemBank((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}
	
	public boolean bankItemExists(int id, int amount) {
		for (int i = 0; i < c.bankItems.length; i++) {
			if (c.bankItems[i] == id && c.bankItemsN[i] == amount) {
				return true;
			}
		}
		return false;
	}

	public int getTabforItem(int itemID) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] == itemID || c.bankItems[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems[i] == itemID - 1)
				return 0;
			else if (c.bankItems1[i] == itemID || c.bankItems1[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems1[i] == itemID - 1)
				return 1;
			else if (c.bankItems2[i] == itemID || c.bankItems2[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems2[i] == itemID - 1)
				return 2;
			else if (c.bankItems3[i] == itemID || c.bankItems3[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems3[i] == itemID - 1)
				return 3;
			else if (c.bankItems4[i] == itemID || c.bankItems4[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems4[i] == itemID - 1)
				return 4;
			else if (c.bankItems5[i] == itemID || c.bankItems5[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems5[i] == itemID - 1)
				return 5;
			else if (c.bankItems6[i] == itemID || c.bankItems6[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems6[i] == itemID - 1)
				return 6;
			else if (c.bankItems7[i] == itemID || c.bankItems7[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems7[i] == itemID - 1)
				return 7;
			else if (c.bankItems8[i] == itemID || c.bankItems8[i] == itemID + 1
					|| Item.itemIsNote[itemID] && c.bankItems8[i] == itemID - 1)
				return 8;
		}
		return c.bankingTab; // if not in bank add to current tab
	}

	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void fromBank(int itemID, int fromSlot, int amount) {
		if (!c.bankCheck && !(c.playerRights != 2)) {
			c.sendMessage("Go to a bank to bank items!");
			c.getPA().closeAllWindows();
			return;
		}
		
		
		int tempT = c.bankingTab;
		int collect = amount;
		for (int i = 0; i < c.getPA().tempItems.length; i++) {
			if (c.getPA().tempItems[i] == itemID + 1
					|| c.getPA().tempItems[i] == itemID) {
				int count = Math.min(c.getPA().tempItemsN[i], collect);
				if (collect == -1)
					count = c.getPA().tempItemsN[i];
				c.bankingTab = (c.getPA().tempItemsT[i]);
				if (c.bankingTab == 0) {
					c.bankingItems = c.bankItems;
					c.bankingItemsN = c.bankItemsN;
				}
				if (c.bankingTab == 1) {
					c.bankingItems = c.bankItems1;
					c.bankingItemsN = c.bankItems1N;
				}
				if (c.bankingTab == 2) {
					c.bankingItems = c.bankItems2;
					c.bankingItemsN = c.bankItems2N;
				}
				if (c.bankingTab == 3) {
					c.bankingItems = c.bankItems3;
					c.bankingItemsN = c.bankItems3N;
				}
				if (c.bankingTab == 4) {
					c.bankingItems = c.bankItems4;
					c.bankingItemsN = c.bankItems4N;
				}
				if (c.bankingTab == 5) {
					c.bankingItems = c.bankItems5;
					c.bankingItemsN = c.bankItems5N;
				}
				if (c.bankingTab == 6) {
					c.bankingItems = c.bankItems6;
					c.bankingItemsN = c.bankItems6N;
				}
				if (c.bankingTab == 7) {
					c.bankingItems = c.bankItems7;
					c.bankingItemsN = c.bankItems7N;
				}
				if (c.bankingTab == 8) {
					c.bankingItems = c.bankItems8;
					c.bankingItemsN = c.bankItems8N;
				}//have to let it see what tab ur in and set the bankitems word
				if (!bankItemExists(itemID, count)) {
					return;
				}
				fromBank(itemID + 1, c.getPA().tempItemsS[i], count);
				collect -= count;
			}
		}
		c.bankingTab = tempT;
		if (amount > 0) {
			if (c.bankingItems[fromSlot] > 0) {
				if (!c.takeAsNote) {
					if (Item.itemStackable[c.bankingItems[fromSlot] - 1]) {
						if (c.bankingItemsN[fromSlot] > amount) {
							if (addItem((c.bankingItems[fromSlot] - 1), amount)) {
								c.bankingItemsN[fromSlot] -= amount;
								resetBank();
								resetItems(5064);
							}
						} else {
							if (addItem((c.bankingItems[fromSlot] - 1),
									c.bankingItemsN[fromSlot])) {
								c.bankingItems[fromSlot] = 0;
								c.bankingItemsN[fromSlot] = 0;
								resetBank();
								resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankingItemsN[fromSlot] > 0) {
								if (addItem((c.bankingItems[fromSlot] - 1), 1)) {
									c.bankingItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						resetItems(5064);
					}
				} else if (c.takeAsNote
						&& Item.itemIsNote[c.bankingItems[fromSlot]]) {
					if (c.bankingItemsN[fromSlot] > amount) {
						if (addItem(c.bankingItems[fromSlot], amount)) {
							c.bankingItemsN[fromSlot] -= amount;
							resetBank();
							resetItems(5064);
						}
					} else {
						if (addItem(c.bankingItems[fromSlot],
								c.bankingItemsN[fromSlot])) {
							c.bankingItems[fromSlot] = 0;
							c.bankingItemsN[fromSlot] = 0;
							resetBank();
							resetItems(5064);
						}
					}
				} else {
					c.sendMessage("This item can't be withdrawn as a note.");
					if (Item.itemStackable[c.bankingItems[fromSlot] - 1]) {
						if (c.bankingItemsN[fromSlot] > amount) {
							if (addItem((c.bankingItems[fromSlot] - 1), amount)) {
								c.bankingItemsN[fromSlot] -= amount;
								resetBank();
								resetItems(5064);
							}
						} else {
							if (addItem((c.bankingItems[fromSlot] - 1),
									c.bankingItemsN[fromSlot])) {
								c.bankingItems[fromSlot] = 0;
								c.bankingItemsN[fromSlot] = 0;
								resetBank();
								resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankingItemsN[fromSlot] > 0) {
								if (addItem((c.bankingItems[fromSlot] - 1), 1)) {
									c.bankingItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						resetItems(5064);
					}
				}
			}
		}
		c.getPA().openUpBank(c.bankingTab);
		c.getPA().sendTabs();
	}

	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				tempAmount += c.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	public boolean isStackable(int itemID) {
		return Item.itemStackable[itemID];
	}

	/**
	 * Update Equip tab
	 **/

	public void setEquipment(int wearID, int amount, int targetSlot) {
		// synchronized(c) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(targetSlot);
		c.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.playerEquipment[targetSlot] = wearID;
		c.playerEquipmentN[targetSlot] = amount;
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void swapBankItem(int from, int to) {
		if (c.bankingTab == 0) {
			int tempI = c.bankItems[from];
			int tempN = c.bankItemsN[from];
			c.bankItems[from] = c.bankItems[to];
			c.bankItemsN[from] = c.bankItemsN[to];
			c.bankItems[to] = tempI;
			c.bankItemsN[to] = tempN;
		} else if (c.bankingTab == 1) {
			int tempI = c.bankItems1[from];
			int tempN = c.bankItems1N[from];
			c.bankItems1[from] = c.bankItems1[to];
			c.bankItems1N[from] = c.bankItems1N[to];
			c.bankItems1[to] = tempI;
			c.bankItems1N[to] = tempN;
		} else if (c.bankingTab == 2) {
			int tempI = c.bankItems2[from];
			int tempN = c.bankItems2N[from];
			c.bankItems2[from] = c.bankItems2[to];
			c.bankItems2N[from] = c.bankItems2N[to];
			c.bankItems2[to] = tempI;
			c.bankItems2N[to] = tempN;
		} else if (c.bankingTab == 3) {
			int tempI = c.bankItems3[from];
			int tempN = c.bankItems3N[from];
			c.bankItems3[from] = c.bankItems3[to];
			c.bankItems3N[from] = c.bankItems3N[to];
			c.bankItems3[to] = tempI;
			c.bankItems3N[to] = tempN;
		} else if (c.bankingTab == 4) {
			int tempI = c.bankItems4[from];
			int tempN = c.bankItems4N[from];
			c.bankItems4[from] = c.bankItems4[to];
			c.bankItems4N[from] = c.bankItems4N[to];
			c.bankItems4[to] = tempI;
			c.bankItems4N[to] = tempN;
		} else if (c.bankingTab == 5) {
			int tempI = c.bankItems5[from];
			int tempN = c.bankItems5N[from];
			c.bankItems5[from] = c.bankItems5[to];
			c.bankItems5N[from] = c.bankItems5N[to];
			c.bankItems5[to] = tempI;
			c.bankItems5N[to] = tempN;
		} else if (c.bankingTab == 6) {
			int tempI = c.bankItems6[from];
			int tempN = c.bankItems6N[from];
			c.bankItems6[from] = c.bankItems6[to];
			c.bankItems6N[from] = c.bankItems6N[to];
			c.bankItems6[to] = tempI;
			c.bankItems6N[to] = tempN;
		} else if (c.bankingTab == 7) {
			int tempI = c.bankItems7[from];
			int tempN = c.bankItems7N[from];
			c.bankItems7[from] = c.bankItems7[to];
			c.bankItems7N[from] = c.bankItems7N[to];
			c.bankItems7[to] = tempI;
			c.bankItems7N[to] = tempN;
		} else if (c.bankingTab == 8) {
			int tempI = c.bankItems8[from];
			int tempN = c.bankItems8N[from];
			c.bankItems8[from] = c.bankItems8[to];
			c.bankItems8N[from] = c.bankItems8N[to];
			c.bankItems8[to] = tempI;
			c.bankItems8N[to] = tempN;
		}
	}

	/**
	 * Move Items
	 **/

	public void moveItems(int from, int to, int moveWindow, byte insert) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to]; // ?login
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}
		if (moveWindow == 5382 && from >= 0 && to >= 0
				&& from < Config.BANK_SIZE && to < Config.BANK_SIZE
				&& to < Config.BANK_SIZE) {
			if (insert == 0) {
				int tempI;
				int tempN;
				tempI = c.bankingItems[from];
				tempN = c.bankingItemsN[from];
				c.bankingItems[from] = c.bankingItems[to];
				c.bankingItemsN[from] = c.bankingItemsN[to];
				c.bankingItems[to] = tempI;
				c.bankingItemsN[to] = tempN;
				c.getPA().openUpBank(c.bankingTab);
			} else if (insert == 1) {
				int tempFrom = from;
				for (int tempTo = to; tempFrom != tempTo;)
					if (tempFrom > tempTo) {
						swapBankItem(tempFrom, tempFrom - 1);
						tempFrom--;
					} else if (tempFrom < tempTo) {
						swapBankItem(tempFrom, tempFrom + 1);
						tempFrom++;
					}
				c.getPA().openUpBank(c.bankingTab);
			}
		}
		if (moveWindow == 5382) // try and do banking now
		{
			resetBank();
		}
		if (moveWindow == 18579 || moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}
	}

	public boolean updateInventory = false;

	public void updateInventory() {
		updateInventory = false;
		resetItems(3214);
	}

	/**
	 * delete Item
	 **/

	public void deleteEquipment(int i, int j) {
		// synchronized(c) {
		if (PlayerHandler.players[c.playerId] == null) {
			return;
		}
		if (i < 0) {
			return;
		}

		c.playerEquipment[j] = -1;
		c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
		c.getOutStream().createFrame(34);
		c.getOutStream().writeWord(6);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(j);
		c.getOutStream().writeWord(0);
		c.getOutStream().writeByte(0);
		getBonus();
		if (j == c.playerWeapon) {
			sendWeapon(-1, "Unarmed");
		}
		resetBonus();
		getBonus();
		writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public boolean addItem2(int item, int amount) {
		if (amount < 1)
			amount = 1;
		if (item <= 0)
			return false;
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == item + 1 && Item.itemStackable[item]
						&& c.playerItems[i] > 0) {
					c.playerItems[i] = (item + 1);
					if (c.playerItemsN[i] + amount < Config.MAXITEM_AMOUNT
							&& c.playerItemsN[i] + amount > -1)
						c.playerItemsN[i] += amount;
					else
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else
							c.getOutStream().writeByte(c.playerItemsN[i]);
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if (amount < Config.MAXITEM_AMOUNT && amount > -1) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}

	public void deleteItem3(int id, int amount) {
		if (id <= 0)
			return;

		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (c.playerItems[j] == id + 1) {
				c.playerItems[j] = 0;
				c.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}// 
			resetItems(3214);// see every time you delete an item the inventory gets reset
			// so for the deleteItemBank just refresh the interface at the end of deposit method.
		}
	}
	
	public void deleteItemBank(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}// see every time you delete an item the inventory gets reset
		}
	}

	public void removeItemSlot(int slot) {
		if (slot < 0) {
			return;
		}
		c.playerItems[slot] = 0;
		c.playerItemsN[slot] = 0;
		resetItems(3214);
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[i] == (id + 1)) {
				if (c.playerItemsN[i] > amount) {
					c.playerItemsN[i] -= amount;
					break;
				} else {
					c.playerItems[i] = 0;
					c.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	public void deleteItem2Slot(int id, int slot, int amount) {
		int am = amount;
		if (am == 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItems[slot] = 0;
				c.playerItemsN[slot] = 0;
				am--;
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete Arrows
	 **/
	public void deleteArrow() {
		// synchronized(c) {
		if (c.playerEquipment[c.playerWeapon] == 12926)
			return;
		if ((c.playerEquipment[c.playerCape] == 10499 || c.playerEquipment[c.playerCape] == 15086)
				&& Misc.random(4) != 1
				&& c.playerEquipment[c.playerArrows] != 4740)
			return;
		if (c.playerEquipmentN[c.playerArrows] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[c.playerArrows],
					c.playerArrows);
		}
		if (c.playerEquipmentN[c.playerArrows] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerArrows);
			c.getOutStream().writeWord(c.playerEquipment[c.playerArrows] + 1);
			if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(
						c.playerEquipmentN[c.playerArrows] - 1);
			} else {
				c.getOutStream().writeByte(
						c.playerEquipmentN[c.playerArrows] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerArrows] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteEquipment() {
		// synchronized(c) {
		if (c.playerEquipment[c.playerWeapon] == 12926) {
			return;
		}
		if (c.playerEquipmentN[c.playerWeapon] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[c.playerWeapon],
					c.playerWeapon);
		}
		if (c.playerEquipmentN[c.playerWeapon] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerWeapon);
			c.getOutStream().writeWord(c.playerEquipment[c.playerWeapon] + 1);
			if (c.playerEquipmentN[c.playerWeapon] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(
						c.playerEquipmentN[c.playerWeapon] - 1);
			} else {
				c.getOutStream().writeByte(
						c.playerEquipmentN[c.playerWeapon] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerWeapon] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Dropping Arrows
	 **/

	public void dropArrowNpc() {
		if (c.playerEquipment[c.playerWeapon] == 12926)
			return;
		if (c.playerEquipment[c.playerCape] == 10499
				|| c.playerEquipment[c.playerCape] == 15086)
			return;
		int enemyX = NPCHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.oldNpcIndex].getY();
		if (c.getPA().inZulrah()) {
			enemyX = c.absX;
			enemyY = c.absY;
		}
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed,
					enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName,
					c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName,
						c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	public void dropArrowPlayer() {
		if (c.playerEquipment[c.playerWeapon] == 12926)
			return;
		int enemyX = PlayerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.oldPlayerIndex].getY();
		if (c.playerEquipment[c.playerCape] == 10499
				|| c.playerEquipment[c.playerCape] == 15086)
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed,
					enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName,
					c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName,
						c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	public String getItemName(int ItemID) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					return Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		return "Unarmed";
	}

	public int getItemId(String itemName) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName
						.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		/*
		 * if (c.pouch1 == itemID && c.pouch1N > 0) { return true; } else if
		 * (c.pouch2 == itemID && c.pouch2N > 0) { return true; } else if
		 * (c.pouch3 == itemID && c.pouch3N > 0) { return true; }
		 */
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		/*
		 * if (c.pouch1 == itemID && c.pouch1N > 0) { return true; } else if
		 * (c.pouch2 == itemID && c.pouch2N > 0) { return true; } else if
		 * (c.pouch3 == itemID && c.pouch3N > 0) { return true; }
		 */
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		/*
		 * if (c.pouch1 > 0 && c.pouch1N > 0) { if (c.pouch1 == itemID) return
		 * true; } if (c.pouch2 > 0 && c.pouch2N > 0) { if (c.pouch2 == itemID)
		 * return true; } if (c.pouch3 > 0 && c.pouch3N > 0) { if (c.pouch3 ==
		 * itemID) return true; }
		 */
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName == NotedName) {
					if (Server.itemHandler.ItemList[i].itemDescription
							.startsWith("Swap this note at any bank for a") == false) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	/**
	 * Drop Item
	 **/

	public void createGroundItem(int itemID, int itemX, int itemY,
			int itemAmount) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(itemID);
		c.getOutStream().writeWord(itemAmount);
		c.getOutStream().writeByte(0);
		c.flushOutStream();
	}

	/**
	 * Pickup Item
	 **/

	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		if (c == null) {
			return;
		}
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(itemID);
		c.flushOutStream();
	}

	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1)
				|| c.getItems().playerHasItem(2413, 1)
				|| c.getItems().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413
					|| c.bankItems[j] == 2414)
				return true;
		}
		if (c.playerEquipment[c.playerCape] == 2413
				|| c.playerEquipment[c.playerCape] == 2414
				|| c.playerEquipment[c.playerCape] == 2415)
			return true;
		return false;
	}

	public boolean hasAllOShards() {
		return playerHasItem(11928, 1) && playerHasItem(11929, 1)
				&& playerHasItem(11930, 1);
	}

	public void makeOshield() {
		deleteItem(11928, 1);
		deleteItem(11929, 1);
		deleteItem(11930, 1);
		addItem(11926, 1);
		c.sendMessage("You combine the shards to make the Odium Ward.");
	}
	
	public boolean hasAllWShards() {
		return playerHasItem(11931, 1) && playerHasItem(11932, 1)
				&& playerHasItem(11933, 1);
	}

	public void makeWshield() {
		deleteItem(11931, 1);
		deleteItem(11932, 1);
		deleteItem(11933, 1);
		addItem(11924, 1);
		c.sendMessage("You combine the shards to make the Malediction Ward.");
	}
	
	public boolean hasAllShards() {
		return playerHasItem(11818, 1) && playerHasItem(11820, 1)
				&& playerHasItem(11822, 1);
	}

	public void makeBlade() {
		deleteItem(11818, 1);
		deleteItem(11820, 1);
		deleteItem(11822, 1);
		addItem(11798, 1);
		c.sendMessage("You combine the shards to make a blade.");
	}

	public void makeGodsword(int i) {
		if (playerHasItem(11798) && playerHasItem(i)) {
			deleteItem(11798, 1);
			deleteItem(i, 1);
			addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	public boolean isHilt(int i) {
		return i >= 11810 && i <= 11816 && i % 2 == 0;
	}

	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0 || c.bankItems[i] == itemId + 1
					&& c.bankItemsN[i] + amount > 0
					&& c.bankItemsN[i] < Config.MAXITEM_AMOUNT) {
				c.bankItems[i] = itemId + 1;
				c.bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}

	public void addItemToBank2(int itemId, int amount) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0 || c.bankItems[i] == itemId + 1
					&& c.bankItemsN[i] + amount > 0
					&& c.bankItemsN[i] < Config.MAXITEM_AMOUNT) {
				c.bankItems[i] = itemId + 1;
				c.bankItemsN[i] += amount;
				resetBank();
				return;
			} else {
				c.sendMessage("Bank Full");
				return;
			}
		}
	}

	public boolean bankItem(int itemID, int fromSlot, int amount, int[] array,
			int[] arrayN) {
		if (c.inTrade) {
			c.sendMessage("You can't store items while trading!");
			return false;
		}
		if (c.playerItems[fromSlot] <= 0 || c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (array[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					array[toBankSlot] = c.playerItems[fromSlot];
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((arrayN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((arrayN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (array[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							array[toBankSlot] = c.playerItems[firstPossibleSlot];
							arrayN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							arrayN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (array[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					array[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((arrayN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((arrayN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT
							&& (arrayN[toBankSlot] + amount) > -1) {
						arrayN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Config.BANK_SIZE; i++) {
					if (array[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Config.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Config.BANK_SIZE; i++) {
						if (array[i] <= 0) {
							toBankSlot = i;
							i = Config.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							array[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							arrayN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							arrayN[toBankSlot] += 1;
							deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public void toTab(int tab, int fromSlot) {
		if (tab == c.bankingTab)
			return;
		if (tab > c.getPA().getTabCount() + 1)
			return;
		if (c.searchTerm != "N/A")
			return;
		if (c.getPA().getBankItems(tab) >= 350) {
			c.sendMessage("You can't store any more items in this tab!");
			return;
		}
		int id = c.bankingItems[fromSlot];
		/*
		 * if(getTotalCount(id) == 0) return;//player doesn't have item!
		 */
		int amount = c.bankingItemsN[fromSlot];
		int[] invItems = new int[28];
		int[] invItemsN = new int[28];
		for (int i = 0; i < c.playerItems.length; i++) {
			invItems[i] = c.playerItems[i];
			invItemsN[i] = c.playerItemsN[i];
			c.playerItems[i] = 0;
			c.playerItemsN[i] = 0;
		}
		c.playerItems[0] = id;
		c.playerItemsN[0] = amount;
		c.bankingItems[fromSlot] = -1;
		c.bankingItemsN[fromSlot] = 0;
		if (tab == 0)
			bankItem(id, 0, amount, c.bankItems, c.bankItemsN);
		else if (tab == 1)
			bankItem(id, 0, amount, c.bankItems1, c.bankItems1N);
		else if (tab == 2)
			bankItem(id, 0, amount, c.bankItems2, c.bankItems2N);
		else if (tab == 3)
			bankItem(id, 0, amount, c.bankItems3, c.bankItems3N);
		else if (tab == 4)
			bankItem(id, 0, amount, c.bankItems4, c.bankItems4N);
		else if (tab == 5)
			bankItem(id, 0, amount, c.bankItems5, c.bankItems5N);
		else if (tab == 6)
			bankItem(id, 0, amount, c.bankItems6, c.bankItems6N);
		else if (tab == 7)
			bankItem(id, 0, amount, c.bankItems7, c.bankItems7N);
		else if (tab == 8)
			bankItem(id, 0, amount, c.bankItems8, c.bankItems8N);
		for (int i = 0; i < invItems.length; i++) {
			c.playerItems[i] = invItems[i];
			c.playerItemsN[i] = invItemsN[i];
		}
		c.getPA().openUpBank(c.bankingTab); // refresh
		c.getPA().openUpBank(c.bankingTab); // refresh twice to ensure
		// update
		updateInventory = true;
		updateInventory();

	}

	public void replaceEquipment(int slot, int replaceItem) {
		if (c.playerEquipment[slot] > 0) {
			c.playerEquipment[slot] = replaceItem;
			if (replaceItem <= 0) {
				c.playerEquipmentN[slot] = 0;
				c.updateRequired = true;
				c.getPA().requestUpdates();
				c.setAppearanceUpdateRequired(true);
			}
			updateSlot(slot);
			c.getPA().requestUpdates();
		}
	}

	public boolean hasFreeSlots(int i) {
		return freeSlots() >= i;
	}

	public boolean playerOwnsItem(int itemID) {
		itemID++;
		/*
		 * if (c.pouch1 == itemID && c.pouch1N > 0) { return true; } else if
		 * (c.pouch2 == itemID && c.pouch2N > 0) { return true; } else if
		 * (c.pouch3 == itemID && c.pouch3N > 0) { return true; }
		 */
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems.length; i++) {
			if (c.bankItems[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems1.length; i++) {
			if (c.bankItems1[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems2.length; i++) {
			if (c.bankItems2[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems3.length; i++) {
			if (c.bankItems3[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems4.length; i++) {
			if (c.bankItems4[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems5.length; i++) {
			if (c.bankItems5[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems6.length; i++) {
			if (c.bankItems6[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems7.length; i++) {
			if (c.bankItems7[i] == itemID)
				return true;
		}
		for (int i = 0; i < c.bankItems8.length; i++) {
			if (c.bankItems8[i] == itemID)
				return true;
		}
		return false;
	}
	
	public boolean hasClueScroll(int itemId) {
		for (int i = 2705; i <= 2713; i++)
			if (itemId == i && playerOwnsItem(i))
				return true;
		for (int i = 2698; i <= 2704; i++)
			if (itemId == i && playerOwnsItem(i))
				return true;
		for (int i = 2688; i <= 2697; i++)
			if (itemId == i && playerOwnsItem(i))
				return true;
		return false;
	}

	public void addItemOrDrop(int itemId, int amount) {
		if (!addItem(itemId, amount)) {
			Server.itemHandler.createGroundItem(c,
					itemId, c.getX(), c.getY(),
					amount, c.playerId);
		}
	}

}