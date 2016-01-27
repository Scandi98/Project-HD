package server.model.content.price;

import java.util.HashMap;
import java.util.Map;

import server.Config;
import server.cache.RSItemDefinition;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PlayerAssistant;

/*
 * Author satan
 */
public class PC {
	// 22015- 22097

	public static final int SIZE = 72;

	public static HashMap<Integer, Integer> itemList = new HashMap<>();

	private Client c;

	public PC(Client c) {
		this.c = c;
	}

	public void addItem(int itemId, int amount) {
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		for (int i : Config.ITEM_TRADEABLE) {
			if (i == itemId) {
				c.sendMessage("You can't add this item.");
				return;
			}
		}

		int owned = c.getItems().getItemAmount(itemId);

		if (amount > owned) {
			amount = owned;
		}
		addItemIndex(itemId, amount);
		// can we do this a better way? up2u.

		RSItemDefinition item = RSItemDefinition.valueOf(itemId);
		if (item != null) {
			c.getPA().sendNewString(convertCurrency((int) totalPrice), 26013);
		}
	}

	public int openIndex = 0; //............ stattic bro ;x that would fuck it for every1 rofl
	/// itd be like every player is on the same open index at the same time lol yeah that'd be fucked.

	private double totalPrice;

	private final int[] stack = new int[24];

	public void addItemIndex(int itemId, int amount) {
		RSItemDefinition item = RSItemDefinition.valueOf(itemId);
		if (item != null) {
			if (Item.itemStackable[itemId]) {

				c.getItems().deleteItem(itemId, amount);
				if (itemList.containsKey(itemId)) {//could be easier if we just wrote it again?  probably, using new design. dont delete this code but just make a new class and copy the basics of what i just wrote, ill be right back gotta do something quick
					for (int i = 0; i < stack.length; i++) {
						if (stack[i] == itemId) {
							openIndex = i;
						}
					}

					int n = itemList.get(itemId) + amount;// lol imagine it was fixed now nah it was fucked testing by myself.

					itemList.put(itemId, n);
					stack[openIndex] = n;

				} else {
					stack[openIndex] += amount;
					itemList.put(itemId, amount);
					openIndex++;
				}
				PlayerAssistant.itemOnInterface(c, 26099, openIndex,
						itemId, stack[openIndex]);
			} else {
				if (amount == 1) {
					if (itemList.size() == 0)
						openIndex = 0;
					c.getItems().deleteItem(itemId, amount);
					if (itemList.containsKey(itemId))
						itemList.put(itemId, itemList.get(itemId) + amount);
					else
						itemList.put(itemId, amount);
					totalPrice += item.price;
					PlayerAssistant.itemOnInterface(c, 26099, openIndex++,
							itemId, 1);
				} else {
					for (int i = 0; i < amount; i++) {
						if (itemList.size() == 0)
							openIndex = 0;

						c.getItems().deleteItem(itemId, 1);
						if (itemList.containsKey(itemId))
							itemList.put(itemId, itemList.get(itemId) + 1);
						else
							itemList.put(itemId, 1);
						PlayerAssistant.itemOnInterface(c, 26099, openIndex++,
								itemId, 1);
					}
					totalPrice += item.price * amount;
				}
			}
			c.getLogs().depositPC(itemId, amount);
		} else {
			c.sendMessage("Error adding item.");
		}
	}

	public void close() {
		if (itemList.size() > 0) {

			for (Map.Entry<Integer, Integer> entry : itemList.entrySet()) {
				System.out.println("Key: " + entry.getKey() + " Value: "
						+ entry.getValue());
				c.getItems().addItem(entry.getKey(), entry.getValue());
				c.getLogs().takePC(entry.getKey(), entry.getValue());
			}
			itemList.clear();
			openIndex = 0;
			totalPrice = 0;
			c.getPA().removeAllWindows();
		} else {
			c.getPA().removeAllWindows();
		}
		cleanUp();
	}

	public void cleanUp() {// so should i clean the whole interface? ye just the
		// itmes in the interface and the value
		for (int i = 0; i < 24; i++) {
			PlayerAssistant.itemOnInterface(c, 26099, i, -1, -1);
		}
		c.getPA().sendNewString("0", 26013);
	}

	private static String convertCurrency(int amount) {

		if (amount >= 1000 && amount < 1000000) {
			return (int) amount / 1000 + "K";
		}

		if (amount >= 1000000 && amount < 1000000000) {
			return (int) amount / 1000000 + "M";
		}

		if (amount >= 1000000000) {
			int value = amount / 100000000;
			return (int) amount / 1000000000 + "." + value + "B";
		}

		return amount + "";
	}

	public void depositAll() {
		/*
		 * for (int i = 0; i < c.playerItems.length; i++) { RSItemDefinition def
		 * = RSItemDefinition.items[c.playerItems[i]]; if (def != null) { if
		 * (itemList.size() == 0) openIndex = 0;
		 * PlayerAssistant.itemOnInterface(c, 26099, openIndex,
		 * c.playerItems[i], 1); c.getItems().deleteItem(c.playerItems[i],
		 * c.playerItemsN[i]); if (itemList.containsKey(c.playerItems[i]))
		 * itemList.put(c.playerItems[i], itemList.get(c.playerItems[i]) + 1);
		 * else itemList.put(c.playerItems[i], 1); openIndex++; totalPrice +=
		 * def.price * c.getItems().getItemAmount(c.playerItemsN[i]); } }
		 */

		for (int i = 0; i < c.playerItems.length; i++) {
			int item = c.playerItems[i] - 1;
			int amount = c.playerItemsN[i];
			if (item != -1) {
				addItemIndex(item, amount);
			}
		}
		c.getPA().sendNewString(convertCurrency((int) totalPrice), 26013);
	}


	public int getIndex() {
		return openIndex;
	}

	public int setIndex(int index) {// u sure u wnt this as int? jw yer. kk
		this.openIndex = index;
		return index;
	}

}
