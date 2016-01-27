package server.model.content;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Client;

public class ComboFoods {
	
	private Client c;

	public ComboFoods(Client c) {
		this.c = c;
	}

	public static enum Food {

		/**
		 * Fish Combo Foods
		 */
		KARAMBWANJI(3151, 3),

		KARAMBWANI(3144, 18),

		/**
		 * Gnome Combo Foods
		 */
		TOAD_CRUNCHIES(2217, 8),

		SPICY_CRUNCHIES(2213, 7),

		WORM_CRUNCHIES(2205, 8),

		CHOCOCHIP_CRUNCHIES(9544, 7),

		FRUIT_BATTA(2277, 11),

		TOAD_BATTA(2255, 11),

		WORM_BATTA(2253, 11),

		VEGETABLE_BATTA(2281, 11),

		CHEESE_AND_TOMATO_BATTA(9535, 11),

		WORM_HOLE(2191, 12),

		VEG_BALL(2195, 12),

		PRE_MADE_VEG_BALL(2235, 12),

		TANGLED_TOAD_LEGS(2187, 15),

		CHOCOLATE_BOMB(2185, 15);
		
		private int id;
		private int heal;
		private int newId;
		private int extraHP;
		private static Map<Integer, Food> foods = new HashMap<Integer, Food>();
		
		public static Food forId(int itemId) {
			return foods.get(itemId);
		}
		
		static {
			for (final Food food : Food.values()) {
				foods.put(food.id, food);
			}
		}

		private Food(int id, int heal) {
			this.id = id;
			this.heal = heal;
		}

		public int getId() {
			return id;
		}

		public int getHeal() {
			return heal;
		}

		public int getNewId() {
			return newId;
		}
		
		public int getExtraHP() {
			return extraHP;
		}
	}
		
	
}
