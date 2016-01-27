package server.model.content;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.model.items.ItemAssistant;
import server.model.players.Client;

public class LootingBag {

	public static final int LootingBagID = 11941;
	public static final int[] UNSTOREABLE = { LootingBagID, 0 };

	public Map<Integer, Integer> LootingBagContents = new HashMap<Integer, Integer>();

	public LootingBag() {

	}

	public void addItem(Client c, int item, int amountToAdd) {
		if (c.inWild()) {
			if (!c.getItems().isStackable(item)) {
				for (int i : UNSTOREABLE) {
					if (i == item) {
						c.sendMessage(i == LootingBagID ? "You may be surprised to learn that bagception is not permitted"
								: "You're not allowed to do that.");
						return;
					}
				}
				for (int i : Config.ITEM_TRADEABLE) {
					if (i == item) {
						c.sendMessage("You can't deposit this item.");
						return;
					}
				}
				if (getSizeOfLootingBag() >= 28) {
					c.sendMessage("Your bag is full.");
					return;
				}
				if (amountToAdd + getSizeOfLootingBag() >= 28) {
					c.sendMessage("Your bag cannot hold that many items.");
					amountToAdd = (amountToAdd - ((amountToAdd + getSizeOfLootingBag()) - 28));
				}
				if (c.getItems().getItemAmount(item) < amountToAdd) {
					amountToAdd = c.getItems().getItemAmount(item);
				}
				int AmountToBag = (LootingBagContents.get(item) == null ? 0
						: LootingBagContents.get(item)) + amountToAdd;
				for (int i = 0; i < amountToAdd; i++)
					if (c.getItems().playerHasItem(item, 1)) {
						c.getItems().deleteItem2(item, 1);
					} else {
						LootingBagContents.put(item,
								(i + (LootingBagContents.get(item))));
						showContents(c);
						System.out.println(LootingBagContents);
						return;
					}
				LootingBagContents.put(item, AmountToBag);
				showContents(c);
				System.out.println(LootingBagContents);
				c.bagItem = 0;
				c.bagItemSlot = -1;
				c.getLogs().writeLootBagItem(c.getItems().getItemName(item));
				return;
			} else {
				c.sendMessage("Storing stackable items is not permitted.");
				return;
			}
		} else {
			c.sendMessage("You must be in the wild to do this.");
			return;
		}
	}

	public void removeItem(Client c, int item, int amountToRemove) {
		// YOURJOB! ( I could easily do this, but think of this as an anti-noob.
		// )
	}

	public int getSizeOfLootingBag() {
		int sum = 0;
		for (int f : LootingBagContents.values())
			sum += f;
		return sum;
	}

	public void showContents(Client c) {
		if (!LootingBagContents.isEmpty()) {
			/*
			 * for (Map.Entry<Integer, Integer> entry :
			 * LootingBagContents.entrySet()) { if(entry.getValue() > 0)
			 * //c.sendMessage("You have <col=255>"+ entry.getValue() +" "
			 * +c.getItems().getItemName(entry.getKey()) +
			 * "(s) <col=0>in your Looting Bag"); }
			 */
		} else {
			c.sendMessage("Your Looting Bag is empty.");
		}
	}

	public void loadLootingBag(int itemid, int amount) {
		LootingBagContents.put(itemid, amount);
	}

	public void saveLootingBag(Client c, BufferedWriter characterfile) {
		try {
			characterfile.newLine();

			characterfile.write("[LOOTING-BAG]");
			characterfile.newLine();
			for (Entry<Integer, Integer> entry : LootingBagContents.entrySet()) {
				if (entry != null) {
					if (entry.getValue() > 0) {
						characterfile.write("BAGITEM = ", 0, 10);
						characterfile.write(Integer.toString(entry.getKey()),
								0, Integer.toString(entry.getKey()).length());
						characterfile.write("	", 0, 1);
						characterfile.write(Integer.toString(entry.getValue()),
								0, Integer.toString(entry.getValue()).length());
						characterfile.newLine();/*
												 * characterfile.write("BAGITEM = "
												 * +entry.getKey().toString() +
												 * " : "+entry.getValue());
												 * characterfile.newLine();
												 */
					}
				}
			}

			characterfile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleBagOnDeath(Client c) {
		if (!LootingBagContents.isEmpty()) {
			Client o = (Client) PlayerHandler.players[c.killerId];
			for (Map.Entry<Integer, Integer> entry : LootingBagContents
					.entrySet()) {
				int value = entry.getValue();
				if (value > 0) {
					for (int i = 0; i < value; i++) {
						if (c.getItems().tradeable(entry.getKey())) {
							c.getLogs().writeLootBagLog(entry.getValue() + "x",
									entry.getKey(), c.getX(), c.getY());
							Server.itemHandler.createGroundItem(o,
									entry.getKey(), c.getX(), c.getY(), 1,
									c.killerId);
						} else {
							// doWhatever with Tradeables
						}
					}
				}
			}
			c.sendMessage("The Contents of your Looting Bag was dropped.");
		}
		c.getItems().deleteItem(c.getItems().getItemSlot(LootingBagID), 1);
		deleteBagItems();
	}

	public void emptyBag(Client c) {
		if (!LootingBagContents.isEmpty()) {
			if (c.getItems().playerHasItem(LootingBagID)) {
				for (Map.Entry<Integer, Integer> entry : LootingBagContents
						.entrySet()) {
					int Value = entry.getValue();
					for (int i = 0; i < Value; i++) {
						if (c.getItems().hasFreeSlots(1)) {
							c.getItems().addItem(entry.getKey(), 1);
							LootingBagContents.put(entry.getKey(),
									(entry.getValue() - 1));
						} else {
							c.sendMessage(i > 0 ? "You could not remove all your items!"
									: "Your inventory is full!");
							return;
						}
					}
				}
			}
		} else {
			c.sendMessage("Your Looting Bag is Empty!");
		}
	}

	public void deleteBagItems() {
		if (!LootingBagContents.isEmpty()) {
			for (Map.Entry<Integer, Integer> entry : LootingBagContents
					.entrySet()) {
				int Value = entry.getValue();
				for (int i = 0; i < Value; i++) {
					LootingBagContents.put(entry.getKey(),
							(entry.getValue() - 1));
				}
			}
		}
	}
}
