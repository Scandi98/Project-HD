package server.model.players.packets;

import server.model.content.Cleaning;
import server.model.content.MysteryBox;
import server.model.content.Pouches;
import server.model.items.TeleportingTabs.TabData;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (!c.getItems().playerHasItem(itemId, 1)) { // , itemSlot
			return;
		}
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			Pouches.fillEssencePouch(c, itemId);
			return;
		}
//		if (itemId == 11941) {
//			c.getBag().showContents(c);
//			c.showLootingBagContents();
//		}
		TabData tabData = TabData.forId(itemId);
		if (tabData != null)
			c.getTablet().useTeleTab(c, itemSlot, tabData);
		if (itemId == 3144) {
			if (c.duelRule[6]) {
				c.sendMessage("You may not eat in this duel.");
				return;
			}
			if ((System.currentTimeMillis() - c.karamDelay >= 2000)
					&& c.playerLevel[3] > 0) {
				c.getCombat().resetPlayerAttack();
				c.attackTimer += 2;
				c.startAnimation(829);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				if (c.playerLevel[3] < c.getLevelForXP(c.playerXP[3])) {
					c.playerLevel[3] += 18;
					if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
						c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
				}
				c.karamDelay = System.currentTimeMillis();
				c.getPA().refreshSkill(3);
				c.sendMessage("You eat the karambwan.");
				return;
			}
		}
		if (itemId == 11740) {
			if (c.getItems().freeSlots() > 0
					&& c.getItems().playerHasItem(11740, 1)) {
				c.getItems().deleteItem(11740, 1);
				c.getItems().addItem(11230, 500);
				c.sendMessage("You have received 500 Dragon Darts.");
			} else if (c.getItems().freeSlots() == 0
					&& c.getItems().playerHasItem(11740, 1)) {
				c.getItems().deleteItem(11740, 1);
				c.getItems().addItemToBank(11230, 500);
				c.sendMessage("You have received 500 Dragon Darts.");
			}
		}
		if (itemId == 3801) {
			c.startAnimation(1330);
			c.forcedChat("Arghh! Matey... That's some tasty Rum!");
			c.getPA().requestUpdates();
		}
		if (itemId == 761) {
			if (c.time <= 0) {
				c.getItems().deleteItem(itemId, 1);
				c.doubleEXPTicket(1800);
			} else {
				c.sendMessage("You currently already have double experience.");
			}
		}
		if (itemId == 455) {
			c.bossPasses += 5;
			c.getItems().deleteItem(itemId, 1);
			c.sendMessage("You now have " + c.bossPasses + " free boss passes! You can use this for Zulrah and other upcoming bosses!");
		}
		/*
		 * if (itemId == 952) { c.getBarrows().spadeDigging();
		 * c.sendMessage("This is in progress.."); return; }
		 */
		// Dice bag
		if (itemId == 5073) {
			c.getItems().addItem(5075, 1);
			c.getItems().handleNests(itemId);
		}
		if (itemId == 7509 && c.playerLevel[c.playerHitpoints] >= 1) {
			c.startAnimation(829);
			c.dealDamage(1);
			c.handleHitMask(1);
			c.forcedChat("Ow! I nearly broke a tooth!");
			c.getPA().refreshSkill(3);
		}
		if (itemId == 1917) {
			c.startAnimation(2770);
			c.sendMessage("You drink the beer.");
			c.playerStandIndex = 2770;
			c.playerTurnIndex = 2769;
			c.playerWalkIndex = 2769;
			c.playerTurn180Index = 2769;
			c.playerTurn90CWIndex = 2769;
			c.playerTurn90CCWIndex = 2769;
			c.playerRunIndex = 2769;
			c.updateRequired = true;
			c.getPA().walkableInterface(14600);
			c.getItems().deleteItem(1917, 1);
		}
		/* Mystery box */
		MysteryBox.handleBox(c, itemId);
		/** Hard Clue Scroll **/
		if (itemId == 2714) { // Easy Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			c.getPA().addClueReward(c, 0);
		}
		if (itemId == 2802) { // Medium Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			c.getPA().addClueReward(c, 1);
		}
		if (itemId == 2775 || itemId == 2789) { // Hard Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			c.getPA().addClueReward(c, 2);
		}
		if (itemId == 2713) {
			c.getPA().showInterface(17537);
		}
		if (itemId == 2712) {
			c.getPA().showInterface(9043);
		}
		if (itemId == 2711) {
			c.getPA().showInterface(7271);
		}
		if (itemId == 2710) {
			c.getPA().showInterface(7045);
		}
		if (itemId == 2709) {
			c.getPA().showInterface(9275);
		}
		if (itemId == 2708) {
			c.getPA().showInterface(7113);
		}
		if (itemId == 2707) {
			c.getPA().showInterface(17634);
		}
		if (itemId == 2706) {
			c.getPA().showInterface(17620);
		}
		if (itemId == 2705) {
			c.getPA().showInterface(4305);
		}
		if (itemId == 2704) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("In a lair of a Boss lies", 6971);
			c.getPA().sendNewString("the next clue scroll!", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2703) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("I seek another cluse just", 6971);
			c.getPA().sendNewString("west of the fountain, at the origin!",
					6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2702) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("We are here lying to protect", 6971);
			c.getPA().sendNewString("the castle that we truely love!", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2701) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("This has to be bob's favorite", 6971);
			c.getPA().sendNewString("training spot in-game.", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2700) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("We all love water, especially", 6971);
			c.getPA().sendNewString("from big, clean, fountains!", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2699) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("near the wilderness", 6971);
			c.getPA().sendNewString("where the road begins to end.", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2698) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("We stall seek history within", 6971);
			c.getPA().sendNewString("the ancient museum.", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2697) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("We party with pete", 6971);
			c.getPA().sendNewString("within a city named ....", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2696) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("The Mage Of Change!", 6971);
			c.getPA().sendNewString("Go Find him at varrock!", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2695) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("These fish must be hot!", 6971);
			c.getPA().sendNewString("We shall call this, Lava Fishing", 6972);
			c.getPA().showInterface(6965);
		}
		// if(itemId == 2694) {
		// c.sendMessage("My loved one..Once murdered in front of my eyes..I couldn't save her..");
		// }
		if (itemId == 2693) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("This village contains torches,", 6971);
			c.getPA()
					.sendNewString("rocks, and some kind of stronghold.", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2692) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("We shall thieve Master Farmers!", 6971);
			c.getPA().sendNewString("I wonder where I can find them...", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2691) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("arggggghhh mate,", 6971);
			c.getPA().sendNewString("Would you like some beer?", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2690) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("Can'd fish!", 6971);
			c.getPA().sendNewString("Lies the next clue or casket matey!!",
					6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2689) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("Ew, a scorpian.", 6971);
			c.getPA().sendNewString("Why are these mines so messed up!", 6972);
			c.getPA().showInterface(6965);
		}
		if (itemId == 2688) {
			for (int i = 6968; i < 6976; i++) {
				c.getPA().sendNewString("", i);
			}
			c.getPA().sendNewString("You Can Duel.", 6971);
			c.getPA().sendNewString("But You seek Truesure at the bank?", 6972);
			c.getPA().showInterface(6965);
		}
		/** Hard Clue Scroll **/
		if (c.safeAreas(2969, 3411, 2974, 3415)
				& (c.getItems().playerHasItem(2713, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2713, c.getItems().getItemSlot(2713), 1);
			c.getItems().addItem(2712, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(2613, 3075, 2619, 3080)
				& (c.getItems().playerHasItem(2712, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2712, c.getItems().getItemSlot(2712), 1);
			c.getItems().addItem(2711, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3030, 3394, 3049, 3401)
				& (c.getItems().playerHasItem(2711, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2711, c.getItems().getItemSlot(2711), 1);
			c.getItems().addItem(2775, 1);
			c.sendMessage("You recieve a HARD Casket!");
		} else if (c.safeAreas(3285, 3371, 3291, 3375)
				& (c.getItems().playerHasItem(2710, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2710, c.getItems().getItemSlot(2710), 1);
			c.getItems().addItem(2709, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3106, 3148, 3113, 3154)
				& (c.getItems().playerHasItem(2709, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2709, c.getItems().getItemSlot(2709), 1);
			c.getItems().addItem(2775, 1);
			c.sendMessage("You recieve a HARD Casket!");
		} else if (c.safeAreas(3092, 3213, 3104, 3225)
				& (c.getItems().playerHasItem(2708, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2708, c.getItems().getItemSlot(2708), 1);
			c.getItems().addItem(2775, 1);
			c.sendMessage("You recieve a HARD Casket!");
		} else if (c.safeAreas(2721, 3337, 2724, 3342)
				& (c.getItems().playerHasItem(2707, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2707, c.getItems().getItemSlot(2707), 1);
			c.getItems().addItem(2706, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3016, 3907, 3026, 3915)
				& (c.getItems().playerHasItem(2706, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2706, c.getItems().getItemSlot(2706), 1);
			c.getItems().addItem(2705, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(2903, 3287, 2909, 3300)
				& (c.getItems().playerHasItem(2705, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2705, c.getItems().getItemSlot(2705), 1);
			c.getItems().addItem(2775, 1);
			c.sendMessage("You recieve a HARD Casket!");
			/** Easy Clue Scrolls **/
		} else if (c.safeAreas(2259, 4680, 2287, 4711)
				& (c.getItems().playerHasItem(2704, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2704, c.getItems().getItemSlot(2704), 1);
			c.getItems().addItem(2703, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3217, 3207, 3225, 3213)
				& (c.getItems().playerHasItem(2703, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2703, c.getItems().getItemSlot(2703), 1);
			c.getItems().addItem(2702, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(2962, 3331, 2987, 3351)
				& (c.getItems().playerHasItem(2702, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2702, c.getItems().getItemSlot(2702), 1);
			c.getItems().addItem(2714, 1);
			c.sendMessage("You recieve a EASY Casket!");
		} else if (c.safeAreas(3253, 3256, 3265, 3296)
				& (c.getItems().playerHasItem(2701, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2701, c.getItems().getItemSlot(2701), 1);
			c.getItems().addItem(2700, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3208, 3421, 3220, 3435)
				& (c.getItems().playerHasItem(2700, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2700, c.getItems().getItemSlot(2700), 1);
			c.getItems().addItem(2699, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3084, 3486, 3086, 3488)
				& (c.getItems().playerHasItem(2699, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2699, c.getItems().getItemSlot(2699), 1);
			c.getItems().addItem(2698, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3253, 3445, 3261, 3453)
				& (c.getItems().playerHasItem(2698, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2698, c.getItems().getItemSlot(2698), 1);
			c.getItems().addItem(2714, 1);
			c.sendMessage("You recieve a EASY Casket!");
			/** Medium Clue Scrolls **/
		} else if (c.safeAreas(2953, 3365, 2977, 3392)
				& (c.getItems().playerHasItem(2697, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2697, c.getItems().getItemSlot(2697), 1);
			c.getItems().addItem(2696, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3202, 3424, 3259, 9873)
				& (c.getItems().playerHasItem(2696, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2696, c.getItems().getItemSlot(2696), 1);
			c.getItems().addItem(2695, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(2875, 9763, 2904, 9776)
				& (c.getItems().playerHasItem(2695, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2695, c.getItems().getItemSlot(2695), 1);
			c.getItems().addItem(2802, 1);
			c.sendMessage("You recieve a MEDIUM Casket!");
		} else if (c.safeAreas(3074, 3407, 3085, 3436)
				& (c.getItems().playerHasItem(2693, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2693, c.getItems().getItemSlot(2693), 1);
			c.getItems().addItem(2692, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3074, 3245, 3085, 3255)
				& (c.getItems().playerHasItem(2692, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2692, c.getItems().getItemSlot(2692), 1);
			c.getItems().addItem(2691, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3044, 3255, 3055, 3259)
				& (c.getItems().playerHasItem(2691, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2691, c.getItems().getItemSlot(2691), 1);
			c.getItems().addItem(2802, 1);
			c.sendMessage("You recieve a MEDIUM Casket!");
		} else if (c.safeAreas(3506, 3496, 3508, 3497)
				& (c.getItems().playerHasItem(2690, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2690, c.getItems().getItemSlot(2690), 1);
			c.getItems().addItem(2689, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3032, 9756, 3056, 9804)
				& (c.getItems().playerHasItem(2689, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2689, c.getItems().getItemSlot(2689), 1);
			c.getItems().addItem(2688, 1);
			c.sendMessage("You recieve another scroll.");
		} else if (c.safeAreas(3381, 3268, 3382, 3269)
				& (c.getItems().playerHasItem(2688, 1))) {
			c.getPA().removeAllWindows();
			c.getItems().deleteItem(2688, c.getItems().getItemSlot(2688), 1);
			c.getItems().addItem(2802, 1);
			c.sendMessage("You recieve a MEDIUM Casket!");
		}
		if (itemId == 405) {
			if (c.getItems().playerHasItem(405, 1)) {
				c.getItems().deleteItem(405, 1);
				c.getItems().addItem(995, 2500000);
			}
		}
		if (itemId == 13192) {
			if (c.getItems().playerHasItem(13192, 1)) {
				c.getItems().deleteItem(13192, 1);
				c.donPoints += 1000;
				c.playerRights = 4;
				c.logout();
			}
		}
		if (itemId == 13190) {
			if (c.getItems().playerHasItem(13190, 1)) {
				c.getItems().deleteItem(13190, 1);
				c.getItems().addItemToBank(761, 1);
				c.donPoints += 2000;
				c.playerRights = 5;
				c.logout();
			}
		}
		if (itemId == 13187
) {
			if (c.getItems().playerHasItem(13187, 1)) {
				c.getItems().deleteItem(13187, 1);
				c.getItems().addItemToBank(12892, 1);
				c.getItems().addItemToBank(761, 2);
				c.donPoints += 5000;
				c.playerRights = 6;
			}
		}
		
		if (itemId == 6950) {
			if (c.playerMagicBook == 0) {
				if (c.playerLevel[6] >= 94) {
					if (System.currentTimeMillis() - c.lastVeng > 30000) {
						c.vengOn = true;
						c.lastVeng = System.currentTimeMillis();
						c.startAnimation(4410);
						c.gfx100(726);
					} else {
						c.sendMessage("You have to wait 30 seconds before you can use this spell again.");
					}
				} else {
					c.sendMessage("Your magic level has to be over 94 to use this spell.");
				}
			} else {
				c.sendMessage("You must be on the regular spellbook to use this spell.");
			}
		}

		if (itemId == 5073) {
			// c.getItems().addItem(5075, 1);
			// c.getItems().deleteItem(5073, 1);
			// c.getItems().handleNests(itemId);
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			int a = itemId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().fillPouch(pouch);
			return;
		}
		if (itemId == 15084) {
			if (System.currentTimeMillis() - c.diceDelay >= 5000
					&& c.playerRights == 3 || c.playerRights == 8) {
				/*c.clan.sendDiceChat(c, " has rolled a " + Misc.random(100)
						+ " on the percentile dice.");
				c.startAnimation(827);
				c.diceDelay = System.currentTimeMillis();*/
				c.sendMessage("This feature is currently disabled");
			}
		}
		if (Cleaning.handleCleaning(c, itemId, itemSlot))
			return;
		/*
		 * if (c.getHerblore().isUnidHerb(itemId))
		 * c.getHerblore().handleHerbClick(itemId);
		 */
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId, itemSlot);
		if (c.getPrayer().isBone(itemId))
			c.getPrayer().buryBone(itemId, itemSlot);
		if (c.getPotions().isPotion(itemId))
			c.getPotions().handlePotion(itemId, itemSlot);
		/*
		 * if (itemId == 952) { if(c.inArea(3553, 3301, 3561, 3294)) {
		 * c.teleTimer = 3; c.newLocation = 1; } else if(c.inArea(3550, 3287,
		 * 3557, 3278)) { c.teleTimer = 3; c.newLocation = 2; } else
		 * if(c.inArea(3561, 3292, 3568, 3285)) { c.teleTimer = 3; c.newLocation
		 * = 3; } else if(c.inArea(3570, 3302, 3579, 3293)) { c.teleTimer = 3;
		 * c.newLocation = 4; } else if(c.inArea(3571, 3285, 3582, 3278)) {
		 * c.teleTimer = 3; c.newLocation = 5; } else if(c.inArea(3562, 3279,
		 * 3569, 3273)) { c.teleTimer = 3; c.newLocation = 6; } }
		 */
		if (c.getHerblore().isUnidHerb(itemId))
			c.getHerblore().handleHerbClick(itemId);
		// ScriptManager.callFunc("itemClick_"+itemId, c, itemId, itemSlot);
		if (c.getPrayer().isBone(itemId))
			c.getPrayer().buryBone(itemId, itemSlot);
		if (itemId == 952) {
			c.getBarrows().spadeDigging();
			return;
		}
	}

}
