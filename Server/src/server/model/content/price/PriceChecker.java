package server.model.content.price;

import java.util.HashMap;
import java.util.Map;

import server.Config;
import server.cache.RSItemDefinition;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PlayerAssistant;

public class PriceChecker {

	private Map<Integer, Map<Integer, Integer>> items = new HashMap<>();

	private Client c;
	private double total;
	private int index;
	private int[] stack = new int[24];// TODO: This is a cheap hax until i find out the solution to stackable items without using the int and while using my maps.

	public PriceChecker(Client c) {
		this.c = c;
	}


	public boolean addItem(int id, int amount) {
//		reset();
		if (!c.getItems().playerHasItem(id, 1)) {
			return false;
		}

		for (int i : Config.ITEM_TRADEABLE) {
			if (i == id) {
				c.sendMessage("You can't add this item.");
				return false;
			}
		}

		int owned = c.getItems().getItemAmount(id);

		if (amount > owned) {
			amount = owned;
		}

		RSItemDefinition item = RSItemDefinition.valueOf(id);

		if (item != null) {


			int target = getIndex(id);

			boolean stackable = Item.itemStackable[id];
			
			if (target >= 24 && !stackable && !items.containsKey(target)) {
				c.sendMessage("You cannot add any more items to the price checker.");
				return false;
			}

			Map<Integer, Integer> slot = null;


			if (items.containsKey(index)) {
				slot = items.get(index);
				amount += slot.get(id);
			} else {
				slot = new HashMap<>();
			}

			if (items.containsKey(target) && Item.itemStackable[id]) {
				
				int sum = amount + stack[target];
				slot.put(id, sum);
				PlayerAssistant.itemOnInterface(c, 26099, target, id, sum);
				items.put(target, slot);
				stack[target] += amount;
			} else {
				stack[target] = amount;
				slot.put(id, amount);
				items.put(index, slot);
				PlayerAssistant.itemOnInterface(c, 26099, index, id, amount);
				index++;
			}
			updateTotal(item);
		}
		return true;
	}

	public int getIndex(int item) {
		int i = 0;
		for (Map<Integer, Integer> entry : items.values()) {
			for (int id : entry.keySet()) {
				if (item == id) {
					return i;
				}
				i++;
			}
		}
		return index;
	}

	public void updateTotal(RSItemDefinition item) {
		total += item.price;
		c.getPA().sendNewString(convertCurrency((int) total), 26013);
	}

	public void reset(boolean copy) {
		items.clear();
		for (int i = 0; i < 24; i++) {
			PlayerAssistant.itemOnInterface(c, 26099, i, -1, -1);
		}
		c.getPA().sendNewString("0", 26013);
		
	}

	public static void handleButton(Client c, int button) {
		switch (button) {
		case 108003:
			c.getPriceChecker().reset(true);
			c.getPA().showInterface(26000);
			break;
		}
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

}
