package server.model.content;

import server.model.players.Client;

public class Pouches {

	public static final int[][] POUCHES = {{5509, 3, 1}, {5510, 6, 25}, {5512, 9, 50}, {5514, 12, 75}};

	public static boolean fillEssencePouch(Client player, int itemId) {
		int level = player.playerLevel[player.playerRunecrafting];
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (level < POUCHES[i][2]) {
					player.sendMessage("You need " + POUCHES[i][2] + " Runecrafting to use this pouch.");
					return true;
				}
				int amount = player.getItems().getItemCount(1436);
				if (player.getPouchData(i) < POUCHES[i][1]) {
					if (amount > 0) {
						int spaceAvailable = POUCHES[i][1] - player.getPouchData(i);
						int fillAmount = 0;
						for (int i2 = 0; i2 < spaceAvailable; i2++) {
							if (amount > 0 && player.getPouchData(i) <= POUCHES[i][1]) {
								amount--;
								fillAmount++;
								player.setPouchData(i, player.getPouchData(i) + 1);
							}
						}
						player.getItems().deleteItem3(1436, fillAmount);
					} else {
						player.sendMessage("You don't have any Rune essence.");
					}
				} else {
					player.sendMessage("Your pouch is full.");
				}
				return true;
			}
		}
		return false;
	}

	public static void emptyEssencePouch(Client player, int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (player.getPouchData(i) > 0) {
					if (player.getItems().freeSlots() >= player.getPouchData(i)) {
						player.getItems().addItem(1436, player.getPouchData(i));
						player.setPouchData(i, 0);
						return;
					} else {
						player.sendMessage("Not enough space in your inventory.");
					}
				} else {
					player.sendMessage("Your pouch is empty.");
					return;
				}
			}
		}
	}

	public static void checkEssencePouch(Client player, int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (POUCHES[i][0] == itemId) {
				if (player.getPouchData(i) > 0) {
					player.sendMessage("Your pouch contains " + player.getPouchData(i) + " Rune essence.");
					return;
				} else {
					player.sendMessage("Your pouch is empty.");
					return;
				}
			}
		}
	}
}

