package server.model.minigames;

import java.util.concurrent.CopyOnWriteArrayList;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.model.items.GameItem;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PlayerSave;
import server.util.Misc;

public class Dueling {

	Client c;

	public Dueling(Client client) {
		this.c = client;
	}

	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

	public void requestDuel(int id) {
		if (Server.UpdateServer) {
			c.sendMessage("You can't duel when the server is about to update.");
			return;
		}
		if (Config.STAKING_DISABLED) {
			c.sendMessage("This is disabled for the moment.");
			return;
		}
		Client o = (Client) PlayerHandler.players[id];
		if (o == null) {
			return;
		}
		if (c.getId() == id) {
			return;
		}
		if (!o.inDuelArena() || !c.inDuelArena()) {
			return;
		}
		if (c.distanceToPoint(o.getX(), o.getY()) > 1) {
			c.sendMessage("Please move closer to " + o.playerName + " to challenge him.");
			return;
		}
		if (c.inDuel) {
			c.sendMessage("You are already in a duel.");
			return;
		}
		if (c.getTradeHandler().getCurrentTrade() != null) {
			if (c.getTradeHandler().getCurrentTrade().isOpen()) {
				c.sendMessage("You are busy trading.");
				return;
			}
		}
		if (o.getTradeHandler().getCurrentTrade() != null) {
			if (o.getTradeHandler().getCurrentTrade().isOpen()) {
				c.sendMessage(o.playerName + " are busy trading.");
				return;
			}
		}
		if (o.isBanking || o.isShopping || o.inDuel) {
			c.sendMessage(o.playerName + " is busy at the moment.");
			return;
		}
		resetDuel();
		resetDuelItems();
		c.duelingWith = id;
		c.duelRequested = true;
		if (c.duelStatus == 0 && o.duelStatus == 0 && c.duelRequested && o.duelRequested && c.duelingWith == o.getId()
				&& o.duelingWith == c.getId()) {
			c.getDuel().openDuel();
			o.getDuel().openDuel();
		} else {
			c.sendMessage("Sending duel request...");
			o.sendMessage(c.playerName + ":duelreq:");
		}
	}

	public void openDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		c.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		c.canOffer = true;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			sendDuelEquipment(c.playerEquipment[i], c.playerEquipmentN[i], i);
		}
		c.getPA().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.combatLevel + ")", 6671);
		c.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame248(6575, 3321);
		c.getItems().resetItems(3322);
	}

	public void sendDuelEquipment(int itemId, int amount, int slot) {
		if (itemId != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(13824);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(itemId + 1);

			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void refreshduelRules() {
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().sendFrame87(286, 0);
		c.duelOption = 0;
	}

	public void refreshDuelScreen() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6669);
		c.getOutStream().writeWord(stakedItems.toArray().length);
		int current = 0;
		for (GameItem item : stakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Config.ITEM_LIMIT || item.id < 0) {
				item.id = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);

			current++;
		}

		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();

		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6670);
		c.getOutStream().writeWord(o.getDuel().stakedItems.toArray().length);
		current = 0;
		for (GameItem item : o.getDuel().stakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Config.ITEM_LIMIT || item.id < 0) {
				item.id = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}

		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount) {
		for (int i : Config.ITEM_TRADEABLE) {
			if (i == itemID) {
				c.sendMessage("You can't stake this item.");
				return false;
			}
		}
		if (amount <= 0)
			return false;
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel();
			return false;
		}
		if (!c.inDuelArena() || !o.inDuelArena()) {
			declineDuel();
			o.getDuel().declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getDuel().declineDuel();
			return false;
		}
		if (!c.canOffer) {
			return false;
		}
		changeDuelStuff();
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				}
			}
			c.getItems().resetItems(3214);
			c.getItems().resetItems(3322);
			o.getItems().resetItems(3214);
			o.getItems().resetItems(3322);
			refreshDuelScreen();
			o.getDuel().refreshDuelScreen();
			c.getPA().sendFrame126("", 6684);
			o.getPA().sendFrame126("", 6684);
		}

		if (!c.getItems().playerHasItem(itemID)) {
			return false;
		}
		if (amount <= 0) {
			return false;
		}
		if (amount > c.getItems().getItemAmount(itemID)) {
			amount = c.getItems().getItemAmount(itemID);
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getItems().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}

		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		c.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		return true;
	}

	public boolean fromDuel(int itemID, int fromSlot, int amount) {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0 || !o.inDuelArena() || !c.inDuelArena()) {
			declineDuel();
			o.getDuel().declineDuel();
			return false;
		}
		if (Item.itemStackable[itemID]) {
			if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (!c.canOffer) {
			return false;
		}

		changeDuelStuff();
		boolean goodSpace = true;
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							stakedItems.remove(item);
							c.getItems().addItem(itemID, 1);
						} else {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().addItem(itemID, amount);
							} else {
								if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								c.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					o.duelStatus = 1;
					c.duelStatus = 1;
					c.getItems().resetItems(3214);
					c.getItems().resetItems(3322);
					o.getItems().resetItems(3214);
					o.getItems().resetItems(3322);
					c.getDuel().refreshDuelScreen();
					o.getDuel().refreshDuelScreen();
					o.getPA().sendFrame126("", 6684);
				}
			}
		}

		for (GameItem item : stakedItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						c.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		c.getDuel().refreshDuelScreen();
		o.getDuel().refreshDuelScreen();
		o.getPA().sendFrame126("", 6684);
		if (!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void confirmDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			declineDuel();
			return;
		}
		String itemId = "";
		for (GameItem item : stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += c.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += c.getItems().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6516);
		itemId = "";
		for (GameItem item : o.getDuel().stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += c.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += c.getItems().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6517);
		c.getPA().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		c.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		if (c.duelRule[8]) {
			c.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
		}
		c.getPA().sendFrame126("", 8240);
		c.getPA().sendFrame126("", 8241);

		String[] rulesOption = { "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.",
				"Players cannot eat food.", "Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.duelRule[i]) {
				c.getPA().sendFrame126("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getPA().sendFrame126("", 6571);
		c.getPA().sendFrame248(6412, 197);
		// c.getPA().showInterface(6412);
	}

	public void startDuel() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			duelVictory();
		}
		c.headIconHints = 2;

		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			c.headIcon = -1;
			c.getPA().requestUpdates();
		}
		if (c.duelRule[11]) {
			c.getItems().removeItem(c.playerEquipment[0], 0);
		}
		if (c.duelRule[12]) {
			c.getItems().removeItem(c.playerEquipment[1], 1);
		}
		if (c.duelRule[13]) {
			c.getItems().removeItem(c.playerEquipment[2], 2);
		}
		if (c.duelRule[14]) {
			c.getItems().removeItem(c.playerEquipment[3], 3);
		}
		if (c.duelRule[15]) {
			c.getItems().removeItem(c.playerEquipment[4], 4);
		}
		if (c.duelRule[16]) {
			c.getItems().removeItem(c.playerEquipment[5], 5);
		}
		if (c.duelRule[17]) {
			c.getItems().removeItem(c.playerEquipment[7], 7);
		}
		if (c.duelRule[18]) {
			c.getItems().removeItem(c.playerEquipment[9], 9);
		}
		if (c.duelRule[19]) {
			c.getItems().removeItem(c.playerEquipment[10], 10);
		}
		if (c.duelRule[20]) {
			c.getItems().removeItem(c.playerEquipment[12], 12);
		}
		if (c.duelRule[21]) {
			c.getItems().removeItem(c.playerEquipment[13], 13);
		}
		c.duelStatus = 5;
		c.getPA().removeAllWindows();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		} else {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		}

		c.getPA().createPlayerHints(10, o.playerId);
		c.getPA().sendOption("Attack", 1);
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.vengOn = false;
		c.poisonDamage = 0;
		c.venomDamage = 0;
		for (GameItem item : o.getDuel().stakedItems) {
			otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		c.duelSafety = true;
		o.duelSafety = true;
		c.getPA().requestUpdates();
	}

	public void duelVictory() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o != null) {
			o.duelSafety = true;
			c.getPA().sendFrame126("" + o.combatLevel, 6839);
			c.getPA().sendFrame126(o.playerName, 6840);
			o.duelStatus = 0;
		} else {
			c.getPA().sendFrame126("", 6839);
			c.getPA().sendFrame126("", 6840);
		}
		c.duelSafety = true;
		c.duelStatus = 6;
		c.followId = -1;
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.getPA().refreshSkill(3);
		duelRewardInterface();
		c.getPA().showInterface(6733);
		c.getPA().movePlayer(Config.DUELING_RESPAWN_X + (Misc.random(Config.RANDOM_DUELING_RESPAWN)),
				Config.DUELING_RESPAWN_Y + (Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
		c.getPA().requestUpdates();
		c.getPA().sendOption("Challenge", 1);
		c.getPA().createPlayerHints(10, -1);
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public void duelRewardInterface() {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6822);
		System.out.println("1:" + stakedItems);
		System.out.println("2:" + otherStakedItems);
		c.getOutStream().writeWord(otherStakedItems.toArray().length);
		for (GameItem item : otherStakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				System.out.println("DO WE EVEN GET HERE!?1");
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				System.out.println("DO WE EVEN GET HERE!?2");
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Config.ITEM_LIMIT || item.id < 0) {
				item.id = Config.ITEM_LIMIT;
			}
			System.out.println("DO WE EVEN GET HERE!?3");
			c.getOutStream().writeWordBigEndianA(item.id + 1);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.logout();
	}

	public void claimStakedItems() {
		System.out.println("Claimed staked items.");
		PlayerSave.saveGame(c);
		for (GameItem item : otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), 1, c.getId());
						}
					}
				}
			}
		}
		for (GameItem item : stakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), 1, c.getId());
						}
					}
				}
			}
		}
		resetDuel();
		resetDuelItems();
		PlayerSave.saveGame(c);
		c.duelStatus = 0;

	}

	public void declineDuel() {
		c.canOffer = true;
		c.duelStatus = 0;
		c.duelingWith = 0;
		c.duelSpaceReq = 0;
		c.duelRequested = false;
		for (GameItem item : stakedItems) {
			if (item.amount < 1)
				continue;
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				c.getItems().addItem(item.id, 1);
			}
		}
		stakedItems.clear();
		PlayerSave.saveGame(c);
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().removeAllWindows();
	}

	public void resetDuel() {
		c.getPA().sendOption("Challenge", 1);
		c.headIconHints = 0;
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().createPlayerHints(10, -1);
		c.duelStatus = 0;
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getPA().requestUpdates();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
		PlayerSave.saveGame(c);
	}

	public void changeDuelStuff() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		o.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame126("", 6684);
	}

	public void selectRule(int i) { // rules
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		if (o == null) {
			return;
		}
		if (c.duelStatus > 2) {
			return;
		}
		if (!c.canOffer)
			return;
		changeDuelStuff();
		o.duelSlot = c.duelSlot;
		if (i >= 11 && c.duelSlot > -1) {
			if (c.playerEquipment[c.duelSlot] > 0) {
				if (!c.duelRule[i]) {
					c.duelSpaceReq++;
				} else {
					c.duelSpaceReq--;
				}
			}
			if (o.playerEquipment[o.duelSlot] > 0) {
				if (!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}

		if (i >= 11) {
			if (c.getItems().freeSlots() < (c.duelSpaceReq) || o.getItems().freeSlots() < (o.duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if (c.playerEquipment[c.duelSlot] > 0) {
					c.duelSpaceReq--;
				}
				if (o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}

		if (!c.duelRule[i]) {
			c.duelRule[i] = true;
			c.duelOption += c.DUEL_RULE_ID[i];
		} else {
			c.duelRule[i] = false;
			c.duelOption -= c.DUEL_RULE_ID[i];
		}

		c.getPA().sendFrame87(286, c.duelOption);
		o.duelOption = c.duelOption;
		o.duelRule[i] = c.duelRule[i];
		o.getPA().sendFrame87(286, o.duelOption);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		} else {
			if (c.duelRule[1]) {
				c.duelTeleX = 3335 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		}

	}

}
