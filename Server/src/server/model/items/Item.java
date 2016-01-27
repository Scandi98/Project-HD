package server.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import server.Config;
import server.Server;
import server.cache.RSItemDefinition;
import server.model.players.Client;

public class Item {

	public static boolean playerCape(int itemId) {
		String[] data = { "TokHaar-Kal", "Ava's accumulator", "cloak", "cape",
				"Ava's attractor" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBoots(int itemId) {
		String[] data = { "Glaiven boots", "Ragefire boots", "Steadfast boots",
				"Flippers", "Shoes", "shoes", "boots", "Boots" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerGloves(int itemId) {
		String[] data = { "Combat bracelet (4)", "Combat bracelet", "Bracelet",
				"Gloves", "gloves", "glove", "Glove", "gauntlets", "Gauntlets",
				"vamb" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerShield(int itemId) {
		String[] data = { "Toktz-ket-xil", "defender", "kiteshield", "Book",
				"book", "Kiteshield", "shield", "Shield", "Kite", "kite",
				"spirit", "Mages' book" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerAmulet(int itemId) {
		String[] data = { "Gnome scarf", "scarf", "Phoenix necklace",
				"necklace", "amulet", "Amulet", "Pendant", "pendant", "Symbol",
				"symbol", "Arcane stream" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerArrows(int itemId) {
		String[] data = { "Arrows", "arrows", "Arrow", "arrow", "Bolts",
				"bolts", "Bolt rack" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerRings(int itemId) {
		String[] data = { "ring", "rings", "Ring", "Rings", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerHats(int itemId) {
		String[] data = { "Fez", "Bearhead", "Wolf mask", "Bat mask",
				"Penguin mask", "Cat mask", "Guthix mitre", "Saradomin mitre",
				"Zamorak mitre", "mitre", "Feather headdress", "boater",
				"cowl", "peg", "coif", "helm", "coif", "mask", "hat",
				"headband", "hood", "disguise", "cavalier", "full", "tiara",
				"helmet", "Hat", "ears", "partyhat", "helm(t)", "helm(g)",
				"beret", "facemask", "sallet", "hat(g)", "hat(t)", "bandana",
				"Helm", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerLegs(int itemId) {
		String[] data = { "tassets", "chaps", "bottoms", "gown", "trousers",
				"platelegs", "robe", "plateskirt", "legs", "leggings",
				"shorts", "Skirt", "skirt", "cuisse", "Trousers", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if ((item.endsWith(data[i]) || item.contains(data[i]))
					&& (!item.contains("top") && (!item.contains("robe (g)") && (!item
							.contains("robe (t)"))))) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBody(int itemId) {
		String[] data = { "body", "top", "Priest gown", "apron", "shirt",
				"platebody", "robetop", "body(g)", "body(t)",
				"Wizard robe (g)", "Wizard robe (t)", "body", "brassard",
				"blouse", "tunic", "leathertop", "Saradomin plate",
				"chainbody", "hauberk", "Shirt", "torso", "chestplate",
				"Saradomin", "Guthix", "Zamorak" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (int i = 0; i < data.length; i++) {
			if (item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	private static String[] fullbody = { "Armadyl d'hide", "Bandos d'hide",
			"Ancient d'hide", "Royal gown top", "Wizard robe", "tunic",
			"Platebody", "Chainbody", "plate", "top", "chestplate", "shirt",
			"platebody", "Ahrims robetop", "Karils leathertop", "brassard",
			"Robe top", "robetop", "platebody (t)", "platebody (g)",
			"chestplate", "torso", "hauberk", "Dragon chainbody", "plate",
			"Rock-shell plate", "Morrigan's leather body", "Graceful top",
			"Black wizard robe (g)" };

	private static String[] fullhat = { "Armadyl coif", "Bandos coif",
			"Ancient coif", "Black h'ween mask", "Wolf mask", "Bat mask",
			"Penguin mask", "Cat mask", "med helm", "coif", "Dharok's helm",
			"hood", "Initiate helm", "Coif", "Helm of neitiznot",
			"Armadyl helmet", "Berserker helm", "Archer helm", "Farseer helm",
			"Warrior helm", "Void", "Graceful hood" };

	private static String[] fullmask = { "Serpentine helmet", "Cow mask",
			"Goblin mask", "Wolf mask", "Bat mask", "Penguin mask", "Cat mask",
			"Fully slayer helmet", "Slayer helmet", "Full slayer helmet",
			"full helm", "mask", "Verac's helm", "Guthan's helm",
			"Karil's coif", "mask", "Torag's helm", "sallet", "Saradomin helm",
			"Magma helm", "Tanzanite helm", "Graceful hood" };

	private static int[] fullB = { 12449, 12451, 12253, 12265, 12193, 12315,
			12343, 12441, 12347 };
	private static int[] fullH = { 12211, 12221, 12231, 12241, 12283, 12293 };

	public static boolean isFullBody(int itemId) {
		for (int i = 0; i < fullB.length; i++) {
			if (itemId == fullB[i]) {
				return true;
			}
		}
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullbody.length; i++) {
			if (weapon.endsWith(fullbody[i]) || weapon.contains(fullbody[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullHelm(int itemId) {
		for (int i = 0; i < fullH.length; i++) {
			if (itemId == fullH[i]) {
				return true;
			}
		}
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullhat.length; i++) {
			if ((weapon.endsWith(fullhat[i]) && itemId != 2631)
					|| itemId == 11663 || itemId == 11664 || itemId == 11665) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullmask.length; i++) {
			if (weapon.endsWith(fullmask[i]) && itemId != 2631) {
				return true;
			}
		}
		return false;
	}

	public static String getItemName(int id) {
		for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
			if (Server.itemHandler.ItemList[j] != null)
				if (Server.itemHandler.ItemList[j].itemId == id)
					return Server.itemHandler.ItemList[j].itemName;
		}
		return null;
	}

	public static String getCacheName(int id) {
		for (int i = 0; i < RSItemDefinition.totalItems; i++) {
			if (RSItemDefinition.valueOf(i) != null) {
				if (RSItemDefinition.valueOf(i).id == id) {
					return RSItemDefinition.valueOf(i).name;
				}
			}
		}
		return null;
	}

	public static boolean[] itemStackable = new boolean[Config.ITEM_LIMIT];
	public static boolean[] itemIsNote = new boolean[Config.ITEM_LIMIT];
	public static int[] targetSlots = new int[Config.ITEM_LIMIT];

	public static void load() {
		int counter = 0;
		int c;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.LOAD_DIRECTORY + "data/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemStackable[15243] = true;
					itemStackable[15263] = true;
					itemStackable[12437] = true;
					itemStackable[12434] = true;
					itemStackable[12435] = true;
					itemStackable[12437] = true;
					itemStackable[11338] = true;
					itemStackable[15273] = true;
					itemStackable[11935] = true;
					itemStackable[11937] = true;
					itemStackable[11939] = true;
					itemStackable[12934] = true;
					itemStackable[12696] = true;
					itemStackable[12698] = true;
					itemStackable[12700] = true;
					itemStackable[12702] = true;
					itemStackable[12906] = true;
					itemStackable[12908] = true;
					itemStackable[12910] = true;
					itemStackable[12912] = true;
					itemStackable[12914] = true;
					itemStackable[12916] = true;
					itemStackable[12918] = true;
					itemStackable[12920] = true;
					itemStackable[11849] = true;
					itemStackable[counter] = false;
				} else {
					itemStackable[counter] = true;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out
					.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.LOAD_DIRECTORY + "data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			itemIsNote[15273] = true;
			itemIsNote[11935] = true;
			itemIsNote[11937] = true;
			itemIsNote[11939] = true;
			itemIsNote[12696] = true;
			itemIsNote[12698] = true;
			itemIsNote[12700] = true;
			itemIsNote[12702] = true;
			itemIsNote[12906] = true;
			itemIsNote[12908] = true;
			itemIsNote[12910] = true;
			itemIsNote[12912] = true;
			itemIsNote[12914] = true;
			itemIsNote[12916] = true;
			itemIsNote[12918] = true;
			itemIsNote[12920] = true;
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}

		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File(
					Config.LOAD_DIRECTORY + "data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
				loadNewFormat();
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
	}

	private static int[][] newFormat = { { 11802, 3 }, { 11804, 3 },
			{ 11806, 3 }, { 11808, 3 }, { 11824, 3 }, { 11826, 0 },
			{ 11828, 4 }, { 11830, 7 }, { 11832, 4 }, { 11834, 7 },
			{ 11836, 10 }, { 11838, 3 }, { 11840, 10 }, { 12002, 2 },
			{ 11791, 3 }, { 11907, 3 }, { 11770, 12 }, { 11771, 12 },
			{ 11772, 12 }, { 12899, 3 }, { 11773, 12 }, { 12904, 3 },
			{ 11864, 0 }, { 12954, 5 }, { 11791, 3 }, { 12002, 2 },
			{ 11926, 5 }, { 11924, 5 }, { 11850, 0 }, { 11852, 1 },
			{ 11854, 4 }, { 11856, 7 }, { 11858, 9 }, { 11860, 10 },
			{ 12205, 4 }, { 12207, 7 }, { 12209, 7 }, { 12211, 0 },
			{ 12213, 5 }, { 12215, 4 }, { 12217, 7 }, { 12219, 7 },
			{ 12221, 0 }, { 12223, 5 }, { 12225, 4 }, { 12227, 7 },
			{ 12229, 7 }, { 12231, 0 }, { 12233, 5 }, { 12235, 4 },
			{ 12237, 7 }, { 12239, 7 }, { 12241, 0 }, { 12243, 4 },
			{ 12445, 7 }, { 12447, 7 }, { 12449, 4 }, { 12451, 4 },
			{ 12453, 0 }, { 12455, 0 }, { 12253, 4 }, { 12255, 7 },
			{ 12257, 2 }, { 12259, 0 }, { 12261, 1 }, { 12263, 3 },
			{ 12265, 4 }, { 12267, 7 }, { 12269, 2 }, { 12271, 0 },
			{ 12273, 1 }, { 12275, 3 }, { 12193, 4 }, { 12195, 7 },
			{ 12197, 2 }, { 12199, 0 }, { 12201, 1 }, { 12203, 3 },
			{ 12277, 4 }, { 12279, 7 }, { 12281, 5 }, { 12283, 0 },
			{ 12285, 7 }, { 12287, 4 }, { 12289, 7 }, { 12291, 5 },
			{ 12293, 0 }, { 12295, 7 }, { 12598, 10 }, { 12315, 4 },
			{ 12317, 7 }, { 12339, 4 }, { 12341, 7 }, { 12343, 4 },
			{ 12345, 7 }, { 12347, 4 }, { 12349, 7 }, { 12335, 5 },
			{ 12432, 0 }, { 12441, 4 }, { 12443, 7 }, { 12351, 0 },
			{ 12337, 0 }, { 12355, 0 }, { 12391, 10 }, { 12389, 3 },
			{ 12353, 0 }, { 12540, 0 }, { 12381, 4 }, { 12383, 7 },
			{ 12385, 4 }, { 12387, 4 }, { 12430, 0 }, { 12357, 3 },
			{ 12514, 1 }, { 12363, 0 }, { 12365, 0 }, { 12367, 0 },
			{ 12369, 0 }, { 12371, 0 }, { 12518, 0 }, { 12520, 0 },
			{ 12522, 0 }, { 12524, 0 }, { 12424, 3 } };

	private static void loadNewFormat() {
		/*
		 * 0 = Hat, 1 = Cape, 2 = Amulet, 3 = Weapon, 4 = Body, 5 = Shield, 7 =
		 * Legs, 9 = Gloves, 10 = Boots, 12 = Ring, 13 = Arrows
		 */
		for (int i = 0; i < newFormat.length; i++) {
			targetSlots[newFormat[i][0]] = newFormat[i][1];
		}
	}
}