package server.model.content;

import server.model.players.Client;

/**
 * 
 * @author Tim
 *
 */
public class RunePouch {

	private static final int POUCH = 12791;
	private static final int FIRE = 554;
	private static final int WATER = 555;
	private static final int AIR = 556;
	private static final int EARTH = 557;
	private static final int MIND = 558;

	public static void runeOnPouch(Client c, int itemUsed, int useWith) {
		if (itemUsed == POUCH || useWith == POUCH) {
			if (itemUsed == FIRE || useWith == FIRE) {
				if (c.pouch1 == 0) {
					c.pouch1 = FIRE;
					c.pouch1N = c.getItems().getItemAmount(FIRE);
					c.sendMessage("You add "+c.getItems().getItemAmount(FIRE)+"x Fire Runes to your Rune Pouch.");
					c.getItems().deleteItem(FIRE,
							c.getItems().getItemAmount(FIRE));
				} else if (c.pouch2 == 0) {
					c.pouch2 = FIRE;
					c.pouch2N = c.getItems().getItemAmount(FIRE);
					c.sendMessage("You add "+c.getItems().getItemAmount(FIRE)+"x Fire Runes to your Rune Pouch.");
					c.getItems().deleteItem(FIRE,
							c.getItems().getItemAmount(FIRE));
				} else if (c.pouch3 == 0) {
					c.pouch3 = FIRE;
					c.pouch3N = c.getItems().getItemAmount(FIRE);
					c.sendMessage("You add "+c.getItems().getItemAmount(FIRE)+"x Fire Runes to your Rune Pouch.");
					c.getItems().deleteItem(FIRE,
							c.getItems().getItemAmount(FIRE));
				}
			} else if (itemUsed == WATER || useWith == WATER) {
				if (c.pouch1 == 0) {
					c.pouch1 = WATER;
					c.pouch1N = c.getItems().getItemAmount(WATER);
					c.sendMessage("You add "+c.getItems().getItemAmount(WATER)+"x Water Runes to your Rune Pouch.");
					c.getItems().deleteItem(WATER,
							c.getItems().getItemAmount(WATER));
				} else if (c.pouch2 == 0) {
					c.pouch2 = WATER;
					c.pouch2N = c.getItems().getItemAmount(WATER);
					c.sendMessage("You add "+c.getItems().getItemAmount(WATER)+"x Water Runes to your Rune Pouch.");
					c.getItems().deleteItem(WATER,
							c.getItems().getItemAmount(WATER));
				} else if (c.pouch3 == 0) {
					c.pouch3 = WATER;
					c.pouch3N = c.getItems().getItemAmount(WATER);
					c.sendMessage("You add "+c.getItems().getItemAmount(WATER)+"x Water Runes to your Rune Pouch.");
					c.getItems().deleteItem(WATER,
							c.getItems().getItemAmount(WATER));
				}
			} else if (itemUsed == AIR || useWith == AIR) {
				if (c.pouch1 == 0) {
					c.pouch1 = AIR;
					c.pouch1N = c.getItems().getItemAmount(AIR);
					c.sendMessage("You add "+c.getItems().getItemAmount(AIR)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(AIR,
							c.getItems().getItemAmount(AIR));
				} else if (c.pouch2 == 0) {
					c.pouch2 = AIR;
					c.pouch2N = c.getItems().getItemAmount(AIR);
					c.sendMessage("You add "+c.getItems().getItemAmount(AIR)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(AIR,
							c.getItems().getItemAmount(AIR));
				} else if (c.pouch3 == 0) {
					c.pouch3 = AIR;
					c.pouch3N = c.getItems().getItemAmount(AIR);
					c.sendMessage("You add "+c.getItems().getItemAmount(AIR)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(AIR,
							c.getItems().getItemAmount(AIR));
				}
			} else if (itemUsed == EARTH || useWith == EARTH) {
				if (c.pouch1 == 0) {
					c.pouch1 = EARTH;
					c.pouch1N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				} else if (c.pouch2 == 0) {
					c.pouch2 = EARTH;
					c.pouch2N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				} else if (c.pouch3 == 0) {
					c.pouch3 = EARTH;
					c.pouch3N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Air Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				}
			} else if (itemUsed == EARTH || useWith == EARTH) {
				if (c.pouch1 == 0) {
					c.pouch1 = EARTH;
					c.pouch1N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Earth Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				} else if (c.pouch2 == 0) {
					c.pouch2 = EARTH;
					c.pouch2N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Earth Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				} else if (c.pouch3 == 0) {
					c.pouch3 = EARTH;
					c.pouch3N = c.getItems().getItemAmount(EARTH);
					c.sendMessage("You add "+c.getItems().getItemAmount(EARTH)+"x Earth Runes to your Rune Pouch.");
					c.getItems().deleteItem(EARTH,
							c.getItems().getItemAmount(EARTH));
				}
			} else if (itemUsed == MIND || useWith == MIND) {
				if (c.pouch1 == 0) {
					c.pouch1 = MIND;
					c.pouch1N = c.getItems().getItemAmount(MIND);
					c.sendMessage("You add "+c.getItems().getItemAmount(MIND)+"x Mind Runes to your Rune Pouch.");
					c.getItems().deleteItem(MIND,
							c.getItems().getItemAmount(MIND));
				} else if (c.pouch2 == 0) {
					c.pouch2 = MIND;
					c.pouch2N = c.getItems().getItemAmount(MIND);
					c.sendMessage("You add "+c.getItems().getItemAmount(MIND)+"x Mind Runes to your Rune Pouch.");
					c.getItems().deleteItem(MIND,
							c.getItems().getItemAmount(MIND));
				} else if (c.pouch3 == 0) {
					c.pouch3 = EARTH;
					c.pouch3N = c.getItems().getItemAmount(MIND);
					c.sendMessage("You add "+c.getItems().getItemAmount(MIND)+"x Mind Runes to your Rune Pouch.");
					c.getItems().deleteItem(MIND,
							c.getItems().getItemAmount(MIND));
				}
			}
		}
	}

}
