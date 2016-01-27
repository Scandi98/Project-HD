package server.model.players.skills.crafting;

import server.model.players.Client;
import server.model.players.PlayerAssistant;
import server.model.players.skills.LeatherMakingAction.BlackLeather;
import server.model.players.skills.LeatherMakingAction.BlueLeather;
import server.model.players.skills.LeatherMakingAction.GreenLeather;

public class LeatherCrafting {

	public static final int NEEDLE = 1733;
	public static final int KNIFE = 946;
	public static final int THREAD = 1734;

	public static boolean handleItemonItem(Client c, int itemUsed, int usedWith) {
		if (itemUsed == NEEDLE || usedWith == NEEDLE) {
			if (itemUsed == 1754 || usedWith == 1754) {// Green
				sendSkillMenu(c, "greenLeather");
				return true;
			} else if (itemUsed == 1751 || usedWith == 1751) {// blue
				sendSkillMenu(c, "blueLeather");
				return true;
			} else if (itemUsed == 1747 || usedWith == 1747) {// black
				sendSkillMenu(c, "blackLeather");
				return true;
			}
		}
		return false;
	}

	public static void sendSkillMenu(Client player, String type) {
		if (type == "greenLeather") {
			display3Item(player, 1065, 1099, 1135, "Vamb", "Chaps", "Body");
		} else if (type == "blueLeather") {
			display3Item(player, 2487, 2493, 2499, "Vamb", "Chaps", "Body");
		} else if (type == "blackLeather") {
			display3Item(player, 2491, 2497, 2503, "Vamb", "Chaps", "Body");
		}
		player.setStatedInterface(type);
	}
	
	public static boolean handleButtons(Client player, int buttonId, int amount) {
		if (player.getStatedInterface() == "greenLeather") {
			if (GreenLeather.create(player, buttonId, amount) != null) {
				GreenLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "blueLeather") {
			if (BlueLeather.create(player, buttonId, amount) != null) {
				BlueLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "blackLeather") {
			if (BlackLeather.create(player, buttonId, amount) != null) {
				BlackLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		return false;
	}

	public static void display3Item(Client player, int i1, int i2, int i3,
			String s1, String s2, String s3) {
		player.getPA().sendNewString(s1, 8889);
		player.getPA().sendNewString(s2, 8893);
		player.getPA().sendNewString(s3, 8897);
		player.getPA().sendFrame246(8883, 180, i1);
		player.getPA().sendFrame246(8884, 180, i2);
		player.getPA().sendFrame246(8885, 180, i3);
		player.getPA().sendFrame164(8880);
	}

}
