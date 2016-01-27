package server.model.players.skills.fletching;

import server.model.players.Client;

/**
 * 
 * @author Tim
 *
 */
public class DartFletching {
	
	public static final int BRONZE_TIP = 819;
	public static final int IRON_TIP = 820;
	public static final int STEEL_TIP = 821;
	public static final int MITH_TIP = 822;
	public static final int ADDY_TIP = 823;
	public static final int RUNE_TIP = 824;
	public static final int FEATHER = 314;
	
	public static void makeDarts(Client c, int usedId, int usedWith) {
		if (usedId == BRONZE_TIP || usedWith == BRONZE_TIP) {
			createDart(c, "bronze");
		}
		if (usedId == IRON_TIP || usedWith == IRON_TIP) {
			createDart(c, "iron");
		}
		if (usedId == STEEL_TIP || usedWith == STEEL_TIP) {
			createDart(c, "steel");
		}
		if (usedId == MITH_TIP || usedWith == MITH_TIP) {
			createDart(c, "mith");
		}
		if (usedId == ADDY_TIP || usedWith == ADDY_TIP) {
			createDart(c, "addy");
		}
		if (usedId == RUNE_TIP || usedWith == RUNE_TIP) {
			createDart(c, "rune");
		}
	}
	
	public static void createDart(Client c, String type) {
		switch (type) {
		case "bronze":
			if (c.getItems().playerHasItem(BRONZE_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(BRONZE_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(806, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(300, c.playerFletching);
			}
			break;
		case "iron":
			if (c.getItems().playerHasItem(IRON_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(IRON_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(807, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(500, c.playerFletching);
			}
			break;
		case "steel":
			if (c.getItems().playerHasItem(STEEL_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(STEEL_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(808, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(700, c.playerFletching);
			}
			break;
		case "mith":
			if (c.getItems().playerHasItem(MITH_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(MITH_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(809, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(900, c.playerFletching);
			}
			break;
		case "addy":
			if (c.getItems().playerHasItem(ADDY_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(ADDY_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(810, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(1100, c.playerFletching);
			}
			break;
		case "rune":
			if (c.getItems().playerHasItem(RUNE_TIP, 10) && c.getItems().playerHasItem(FEATHER, 10)) {
				c.getItems().deleteItem2(RUNE_TIP, 10);
				c.getItems().deleteItem2(FEATHER, 10);
				c.getItems().addItem(811, 10);
				c.sendMessage("You finish making 10 darts.");
				c.getPA().addSkillXP(1300, c.playerFletching);
			}
			break;
		}
	}

}
