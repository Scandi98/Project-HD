package server.model.players.skills.LeatherMakingAction;

import java.util.HashMap;

import server.model.players.Client;
import server.model.players.skills.crafting.LeatherMaking;

public class BlueLeather extends LeatherMaking {

	public BlueLeather(final Client player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static BlueLeather create(final Client player, int buttonId, int manualAmount) {
		final BlueLeatherMake blueLeatherMake = BlueLeatherMake.forId(buttonId);
		if (blueLeatherMake == null || (blueLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new BlueLeather(player, blueLeatherMake.getUsed(), blueLeatherMake.getUsed2(), blueLeatherMake.getResult(), blueLeatherMake.getAmount(), manualAmount, blueLeatherMake.getLevel(), blueLeatherMake.getExperience());
	}

	public static enum BlueLeatherMake {
		VAMB(34185, 1751, 1, 2487, 1, 66, 70), VAMB5(34184, 1751, 1, 2487, 5, 66, 70), VAMB10(34183, 1751, 1, 2487, 10, 66, 70), VAMBX(34182, 1751, 1, 2487, 0, 66, 70), CHAPS(34189, 1751, 2, 2493, 1, 68, 140), CHAPS5(34188, 1751, 2, 2493, 5, 68, 140), CHAPS10(34187, 1751, 2, 2493, 10, 68, 140), CHAPSX(34186, 1751, 2, 2493, 0, 68, 140), BODY(34193, 1751, 3, 2499, 1, 71, 210), BODY5(34192, 1751, 3, 2499, 5, 71, 210), BODY10(34191, 1751, 3, 2499, 10, 71, 210), BODYX(34190, 1751, 3, 2499, 0, 71, 210);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, BlueLeatherMake> blueLeatherItems = new HashMap<Integer, BlueLeatherMake>();

		public static BlueLeatherMake forId(int id) {
			if (blueLeatherItems == null) {
				return null;
			}
			return blueLeatherItems.get(id);
		}

		static {
			for (BlueLeatherMake data : BlueLeatherMake.values()) {
				blueLeatherItems.put(data.buttonId, data);
			}
		}

		private BlueLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getUsed2() {
			return used2;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

}

