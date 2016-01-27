package server.model.content;

import server.model.players.Client;
import server.util.Misc;

public class MysteryBox {
	
	public static void handleBox(Client c, int itemId) {
		if (itemId == 6199) {
			int mysteryReward = Misc.random(15); // Coded by Sonic chao
			if (mysteryReward == 1) {
				c.getItems().addItemToBank(9921, 1);
				c.getItems().addItemToBank(9922, 1);
				c.getItems().addItemToBank(9923, 1);
				c.getItems().addItemToBank(9924, 1);
				c.getItems().addItemToBank(9925, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A completed full skeleton!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 2) {
				c.getItems().addItemToBank(11019, 1);
				c.getItems().addItemToBank(11020, 1);
				c.getItems().addItemToBank(11021, 1);
				c.getItems().addItemToBank(11022, 1);
				c.getItems().addItemToBank(4566, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A completed full chicken!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 3) {
				c.getItems().addItemToBank(6654, 1);
				c.getItems().addItemToBank(6655, 1);
				c.getItems().addItemToBank(6656, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A completed full camo!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 4) {
				c.getItems().addItemToBank(6666, 1);
				c.getItems().addItemToBank(7003, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A flippers and a camel mask!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 5) {
				c.getItems().addItemToBank(9920, 1);
				c.getItems().addItemToBank(10507, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A jack lantern hat and a reindeer hat!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 6) {
				c.getItems().addItemToBank(1037, 1);
				c.getItems().addItemToBank(1961, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A bunny ears and a easter egg!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 7) {
				c.getItems().addItemToBank(1419, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A scythe!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 8) {
				c.getItems().addItemToBank(4565, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A basket of eggs!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 9) {
				c.getItems().addItemToBank(5607, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A grain!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 10) {
				c.getItems().addItemToBank(10836, 1);
				c.getItems().addItemToBank(10837, 1);
				c.getItems().addItemToBank(10838, 1);
				c.getItems().addItemToBank(10839, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A completed full silly jester!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 11) {
				c.getItems().addItemToBank(6858, 1);
				c.getItems().addItemToBank(6859, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A jester hat and scarf!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 12) {
				c.getItems().addItemToBank(6856, 1);
				c.getItems().addItemToBank(6857, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A bobble hat and scarf!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 13) {
				c.getItems().addItemToBank(6860, 1);
				c.getItems().addItemToBank(6861, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A tri-jester hat and scarf!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 14) {
				c.getItems().addItemToBank(6862, 1);
				c.getItems().addItemToBank(6863, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A wolly hat and scarf!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 15) {
				c.getItems().addItemToBank(9470, 1);
				c.getItems().addItemToBank(10394, 1);
				c.getItems().deleteItem(6199, 1);
				c.sendMessage("You've gained: @blu@A gnome scarf and a flared trousers!");
				c.sendMessage("@red@The items has been added to your bank.");
			}
		}
		if (itemId == 3062) { // vote box
			int mysteryReward = Misc.random(63); // Coded by Oxide
			if (mysteryReward == 1) {
				c.getItems().addItemToBank(6666, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@Flippers!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 2) {
				c.getItems().addItemToBank(4068, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Sword!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 3) {
				c.getItems().addItemToBank(4069, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platebody!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 4) {
				c.getItems().addItemToBank(6666, 1);
				c.getItems().addItemToBank(7003, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@flippers and a camel mask!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 5) {
				c.getItems().addItemToBank(4070, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platelegs!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 6) {
				c.getItems().addItemToBank(4071, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Helm!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 7) {
				c.getItems().addItemToBank(4072, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Shield!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 8) {
				c.getItems().addItemToBank(4087, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@Dragon Platelegs!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 9) {
				c.getItems().addItemToBank(4503, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Sword!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 10) {
				c.getItems().addItemToBank(4504, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platebody!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 11) {
				c.getItems().addItemToBank(4505, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platelegs!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 12) {
				c.getItems().addItemToBank(4506, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Helm!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 13) {
				c.getItems().addItemToBank(4507, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Shield!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 14) {
				c.getItems().addItemToBank(4508, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Sword!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 15) {
				c.getItems().addItemToBank(4509, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platebody!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 16) {
				c.getItems().addItemToBank(4510, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Platelegs!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 17) {
				c.getItems().addItemToBank(4511, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative helm!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 18) {
				c.getItems().addItemToBank(4512, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Decorative Shield!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 19) {
				c.getItems().addItemToBank(4514, 1);
				c.getItems().addItemToBank(4513, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Saradomin cloak and cape!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 20) {
				c.getItems().addItemToBank(4516, 1);
				c.getItems().addItemToBank(4515, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Zamorak cloak and cape!");
				c.sendMessage("@red@The items has been added to your bank.");
			} else if (mysteryReward == 21) {
				c.getItems().addItemToBank(2633, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Blue Beret!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 22) {
				c.getItems().addItemToBank(2635, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Black Beret!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 23) {
				c.getItems().addItemToBank(2637, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A white Beret!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 24) {
				c.getItems().addItemToBank(3481, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Guilded Platebody!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 25) {
				c.getItems().addItemToBank(3483, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Guilded Platelegs!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 26) {
				c.getItems().addItemToBank(3485, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Guilded Plateskirt!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 27) {
				c.getItems().addItemToBank(3485, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Guilded Plateskirt!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 28) {
				c.getItems().addItemToBank(3486, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Guilded Fullhelm!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 29) {
				c.getItems().addItemToBank(4716, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Dharoks Helm!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 30) {
				c.getItems().addItemToBank(4718, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Dharoks Axe!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 31) {
				c.getItems().addItemToBank(4720, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Dharoks Platebody!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 32) {
				c.getItems().addItemToBank(4722, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Dharoks Platelegs!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 33) {
				c.getItems().addItemToBank(6619, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A pair of White Boots!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 34) {
				c.getItems().addItemToBank(6739, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Dragon Axe!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 35) {
				c.getItems().addItemToBank(6737, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Zerker Ring!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 36) {
				c.getItems().addItemToBank(6731, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Seer's Ring!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 37) {
				c.getItems().addItemToBank(1079, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@Rune Platelegs!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 38) {
				c.getItems().addItemToBank(1127, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@Rune Platebody!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 39) {
				c.getItems().addItemToBank(995, 100000);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@100k coins!");
				c.sendMessage("@red@The coins has been added to your bank.");
			} else if (mysteryReward == 40) {
				c.getItems().addItemToBank(995, 1000000);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@1M coins!");
				c.sendMessage("@red@The coins has been added to your bank.");
			} else if (mysteryReward == 41) {
				c.getItems().addItemToBank(995, 1500000);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@1.5M coins!");
				c.sendMessage("@red@The coins has been added to your bank.");
			} else if (mysteryReward == 42) {
				c.getItems().addItemToBank(995, 2000000);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@2M coins!");
				c.sendMessage("@red@The coins has been added to your bank.");
			} else if (mysteryReward == 43) {
				c.getItems().addItemToBank(995, 3000000);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@3m coins!");
				c.sendMessage("@red@The coins has been added to your bank.");
			} else if (mysteryReward == 44) {
				c.getItems().addItemToBank(1139, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Bronze med helm!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 45) {
				c.getItems().addItemToBank(1615, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Diamond!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 46) {
				c.getItems().addItemToBank(4151, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A abyssal whip!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 47) {
				c.getItems().addItemToBank(4212, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A New Crystal Bow!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 48) {
				c.getItems().addItemToBank(4089, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 49) {
				c.getItems().addItemToBank(4091, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 50) {
				c.getItems().addItemToBank(4093, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 51) {
				c.getItems().addItemToBank(4095, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 52) {
				c.getItems().addItemToBank(4097, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 53) {
				c.getItems().addItemToBank(4099, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 54) {
				c.getItems().addItemToBank(4101, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 55) {
				c.getItems().addItemToBank(4103, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 56) {
				c.getItems().addItemToBank(4105, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 57) {
				c.getItems().addItemToBank(4107, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 58) {
				c.getItems().addItemToBank(4109, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 59) {
				c.getItems().addItemToBank(4111, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 60) {
				c.getItems().addItemToBank(4113, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 61) {
				c.getItems().addItemToBank(4115, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 62) {
				c.getItems().addItemToBank(4117, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Mystic Piece!");
				c.sendMessage("@red@The item has been added to your bank.");
			} else if (mysteryReward == 63) {
				c.getItems().addItemToBank(4119, 1);
				c.getItems().deleteItem(3062, 1);
				c.sendMessage("You've gained: @blu@A Pair of bronze boots!");
				c.sendMessage("@red@The item has been added to your bank.");
			}
		}
	}

}
