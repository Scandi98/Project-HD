package server.model.content;

import server.model.players.Client;

/*
 * Author @Tim
 */
public class Blowpipe {

	public static final int BLOWPIPE = 12926;
	public static final int BRONZE_DART = 806;
	public static final int IRON_DART = 807;
	public static final int STEEL_DART = 808;
	public static final int MITH_DART = 809;
	public static final int ADDY_DART = 810;
	public static final int RUNE_DART = 811;
	public static final int DRAGON_DART = 11230;

	public static void addDart(Client c, int usedId, int usedWith) {
		if ((c.darts != 0 && c.dartType == getDartType(usedId) || c.dartType == getDartType(usedWith)) || c.darts == 0) {
			if (usedWith == BRONZE_DART || usedId == BRONZE_DART) {
				handleDartCreation(c, "bronze");
			}
			if (usedWith == IRON_DART || usedId == IRON_DART) {
				handleDartCreation(c, "iron");
			}
			if (usedWith == STEEL_DART || usedId == STEEL_DART) {
				handleDartCreation(c, "steel");
			}
			if (usedWith == MITH_DART || usedId == MITH_DART) {
				handleDartCreation(c, "mith");
			}
			if (usedWith == ADDY_DART || usedId == ADDY_DART) {
				handleDartCreation(c, "addy");
			}
			if (usedWith == RUNE_DART || usedId == RUNE_DART) {
				handleDartCreation(c, "rune");
			}
			if (usedWith == DRAGON_DART || usedId == DRAGON_DART) {
				handleDartCreation(c, "dragon");
			}
		} else {
			c.sendMessage("You must remove the current darts to add more!");
			return;
		}
	/*	if (c.dartType != 0 && c.darts != 0) {
			c.sendMessage("You must remove the current darts to add more!");
			return;
		} else {
			if (usedWith == BRONZE_DART || usedId == BRONZE_DART) {
				handleDartCreation(c, "bronze");
			}
			if (usedWith == IRON_DART || usedId == IRON_DART) {
				handleDartCreation(c, "iron");
			}
			if (usedWith == STEEL_DART || usedId == STEEL_DART) {
				handleDartCreation(c, "steel");
			}
			if (usedWith == MITH_DART || usedId == MITH_DART) {
				handleDartCreation(c, "mith");
			}
			if (usedWith == ADDY_DART || usedId == ADDY_DART) {
				handleDartCreation(c, "addy");
			}
			if (usedWith == RUNE_DART || usedId == RUNE_DART) {
				handleDartCreation(c, "rune");
			}
			if (usedWith == DRAGON_DART || usedId == DRAGON_DART) {
				handleDartCreation(c, "dragon");
			}
		}*/
	}
	
	public static final int getDartType(int itemId) {
		switch (itemId) {
		case BRONZE_DART:
			return 1;
		case IRON_DART:
			return 2;
		case STEEL_DART:
			return 3;
		case MITH_DART:
			return 4;
		case ADDY_DART:
			return 5;
		case RUNE_DART:
			return 6;
		case DRAGON_DART:
			return 7;
		}
		return 0;
	}

	public static void handleDartCreation(Client c, String type) {
		switch (type) {
		case "bronze":
			if (c.getItems().playerHasItem(BRONZE_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(BRONZE_DART)
						+ "x Bronze Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(BRONZE_DART);
				c.getItems().deleteItem2(BRONZE_DART,
						c.getItems().getItemCount(BRONZE_DART));

				c.dartType = 1;
			}
			break;
		case "iron":
			if (c.getItems().playerHasItem(IRON_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(IRON_DART)
						+ "x Iron Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(IRON_DART);
				c.getItems().deleteItem2(IRON_DART,
						c.getItems().getItemCount(IRON_DART));
				c.dartType = 2;
			}
			break;
		case "steel":
			if (c.getItems().playerHasItem(STEEL_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(STEEL_DART)
						+ "x Steel Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(STEEL_DART);
				c.getItems().deleteItem2(STEEL_DART,
						c.getItems().getItemCount(STEEL_DART));

				c.dartType = 3;
			}
			break;
		case "mith":
			if (c.getItems().playerHasItem(MITH_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(MITH_DART)
						+ "x Mith Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(MITH_DART);
				c.getItems().deleteItem2(MITH_DART,
						c.getItems().getItemCount(MITH_DART));

				c.dartType = 4;
			}
			break;
		case "addy":
			if (c.getItems().playerHasItem(ADDY_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(ADDY_DART)
						+ "x Addy Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(ADDY_DART);
				c.getItems().deleteItem2(ADDY_DART,
						c.getItems().getItemCount(ADDY_DART));

				c.dartType = 5;
			}
			break;
		case "rune":
			if (c.getItems().playerHasItem(RUNE_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(RUNE_DART)
						+ "x Rune Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(RUNE_DART);
				c.getItems().deleteItem2(RUNE_DART,
						c.getItems().getItemCount(RUNE_DART));

				c.dartType = 6;
			}
			break;
		case "dragon":
			if (c.getItems().playerHasItem(DRAGON_DART)
					&& c.getItems().playerHasItem(BLOWPIPE)) {
				c.sendMessage("You have added "
						+ c.getItems().getItemCount(DRAGON_DART)
						+ "x Dragon Darts to your Blowpipe.");
				c.darts += c.getItems().getItemCount(DRAGON_DART);
				c.getItems().deleteItem2(DRAGON_DART,
						c.getItems().getItemCount(DRAGON_DART));

				c.dartType = 7;
			}
			break;
		}
	}

	public static void handleOperate(Client c) {
		if (c.darts == 0) {
			c.sendMessage("You current don't have any darts loaded!");
		} else if (c.dartType == 1) {
			c.sendMessage("You currently have " + c.darts
					+ "x Bronze Darts loaded.");
		} else if (c.dartType == 2) {
			c.sendMessage("You currently have " + c.darts
					+ "x Iron Darts loaded.");
		} else if (c.dartType == 3) {
			c.sendMessage("You currently have " + c.darts
					+ "x Steel Darts loaded.");
		} else if (c.dartType == 4) {
			c.sendMessage("You currently have " + c.darts
					+ "x Mith Darts loaded.");
		} else if (c.dartType == 5) {
			c.sendMessage("You currently have " + c.darts
					+ "x Addy Darts loaded.");
		} else if (c.dartType == 6) {
			c.sendMessage("You currently have " + c.darts
					+ "x Rune Darts loaded.");
		} else if (c.dartType == 7) {
			c.sendMessage("You currently have " + c.darts
					+ "x Dragon Darts loaded.");
		}
	}
	
	public static final int getDart(Client c) {
		if (c.dartType == 1) {
			return BRONZE_DART;
		} else if (c.dartType == 2) {
			return IRON_DART;
		} else if (c.dartType == 3) {
			return STEEL_DART;
		} else if (c.dartType == 4) {
			return MITH_DART;
		} else if (c.dartType == 5) {
			return ADDY_DART;
		} else if (c.dartType == 6) {
			return RUNE_DART;
		} else if (c.dartType == 7) {
			return DRAGON_DART;
		}
		return -1;
	}
	
	public static final String getType(Client c) {
		if (c.dartType == 1) {
			return "Bronze Darts";
		} else if (c.dartType == 2) {
			return "Iron Darts";
		} else if (c.dartType == 3) {
			return "Steel Darts";
		} else if (c.dartType == 4) {
			return "Mith Darts";
		} else if (c.dartType == 5) {
			return "Addy Darts";
		} else if (c.dartType == 6) {
			return "Rune Darts";
		} else if (c.dartType == 7) {
			return"Dragon Darts";
		}
		return "";
	}
	
	public static void handleEmpty(Client c) {
		if (c.darts > 0 && c.dartType != 0) {
			if (c.getItems().freeSlots() > 0) {
				c.getItems().addItem(getDart(c), c.darts);
				c.sendMessage("You have removed "+c.darts+"x "+getType(c)+" from your quiver.");
				c.darts = 0;
				c.dartType = 0;
			} else {
				c.sendMessage("You don't have enough inventory space to do this.");
				return;
			}
		}
	}

}
