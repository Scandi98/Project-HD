package server.model.players.packets;

import server.Config;
import server.Connection;
import server.Server;
import server.core.PlayerHandler;
import server.core.World;
import server.model.content.Achievements;
import server.model.content.Decanting;
import server.model.content.DiceHandler;
import server.model.content.DuoSlayer;
import server.model.content.PotionMaking;
import server.model.content.QuickCurses;
import server.model.content.QuickPrayers;
import server.model.content.instance.InstanceManager;
import server.model.content.price.PC;
import server.model.content.price.PriceChecker;
import server.model.items.GameItem;
import server.model.items.Item;
import server.model.npcs.pet.Pet;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.skills.cooking.Cooking;
import server.model.players.skills.crafting.GemCutting;
import server.model.players.skills.crafting.LeatherCrafting;
import server.model.players.skills.fletching.ArrowHandler;
import server.model.players.skills.fletching.BowHandler;
import server.model.players.skills.fletching.StringingHandler;
import server.model.players.skills.smithing.Smelting;
import server.tick.Tickable;
import server.util.Misc;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		// int actionButtonId = c.getInStream().readShort();
		if (c.isDead)
			return;
		if (c.playerRights == 3)
			Misc.println(c.playerName + " - actionbutton: " + actionButtonId);
		if (actionButtonId > 34100 && actionButtonId < 34220) {
			BowHandler.handleFletchingButtons(c, actionButtonId);
			// System.out.println("here");
		}
		if (actionButtonId >= 67050 && actionButtonId <= 67075) {
			QuickPrayers.clickPray(c, actionButtonId);
		}
		if (InstanceManager.instanceManager.canCreateInstance(c, actionButtonId)) {
			InstanceManager.createInstance(c, actionButtonId);
			c.sendMessage("amam");
		}
		if (LeatherCrafting.handleButtons(c, actionButtonId, 0)) {
			return;
		}
		PriceChecker.handleButton(c, actionButtonId);
		Smelting.getBar(c, actionButtonId);
		c.sendMessage("" + actionButtonId);
		switch (actionButtonId) {
		
		case 2202:
			c.sendMessage("You have a total of " + c.moneyPouch + " coins in your pouch.");
			break;

		// Zulrah metamorphosis
		case 88174:
			c.getPA().closeAllWindows();
			break;
		case 87230:
			c.getPA().closeAllWindows();
			break;
		case 88074:
			c.getPA().closeAllWindows();
			break;
		case 89018:
			c.getPA().closeAllWindows();
			break;

		// green
		/*
		 * case 89025: c.getPA().showInterface(22700); break; case 88081:
		 * c.getPA().showInterface(22700); break;
		 */
		// red
		/*
		 * case 89028: c.getPA().showInterface(22600); break; case 88184:
		 * c.getPA().showInterface(22600); break;
		 */
		case 87237:
		case 174065:
			c.getPA().showInterface(22700);
			c.setColorSelect("green");
			break;
		case 87240:
			c.getPA().showInterface(44600);
			c.setColorSelect("red");
			break;
		case 87234:
		case 174062:
			c.getPA().showInterface(22800);
			c.setColorSelect("blue");
			break;
		case 89035:
		case 88191:
		case 88091:
		case 174075:
			int newId = 0;
			switch (c.getColorSelect()) {
			case "none":
			case "":
				newId = c.petID;
				break;
			case "green":
				newId = 2130;
				break;
			case "red":
				newId = 2131;
				break;
			case "blue":
				newId = 2132;
				break;
			}
			Pet.morph(c, newId);
			c.sendMessage("Your newly morphed pet begins to follow you delightfully.");
			break;
		case 114122:
			c.sendMessage("You have fished " + c.sharksFished + " Sharks.");
			break;
		case 114123:
			c.sendMessage("You have crafted " + c.gloriesCrafted + " Glories.");
			break;
		case 114124:
			c.sendMessage("You have chopped " + c.magicsCut + " Magic logs.");
			break;
		case 114125:
			c.sendMessage("You have burned " + c.magicsBurned + " Magic logs.");
			break;
		case 114126:
			c.sendMessage("You have fletched " + c.bowsMade + " Magic longbows.");
			break;
		case 114127:
			c.sendMessage("You have grown " + c.herbsFarmed + " Herbs.");
			break;
		case 114083:
			c.setSidebarInterface(2, 638);
			break;
		case 113228:
			Achievements.loadInterface(c);
			break;
		case 109098:
		case 111086:
			c.restoreTabs();
			break;
		case 72038:
			c.handleBhTeleport();
			break;
		case 55096:// This is the button id
			c.getPA().removeAllWindows();// Choosing No will remove all the
			// windows
			c.droppedItem = -1;
			break;

		case 55095:// This is the button id
			c.getPA().destroyItem(c.droppedItem);// Choosing Yes will delete the
			// item and make it
			// dissapear
			c.droppedItem = -1;
			break;

		/*
		 * case 104186: AuctionHouse.removeOffer(c, 0); break; case 104187:
		 * AuctionHouse.removeOffer(c, 1); break; case 104188:
		 * AuctionHouse.removeOffer(c, 2); break; case 104189:
		 * AuctionHouse.removeOffer(c, 3); break; case 104190:
		 * AuctionHouse.removeOffer(c, 4); break; case 104191:
		 * AuctionHouse.removeOffer(c, 5); break;
		 * 
		 * case 104095: AuctionHouse.collectItem(c); break; case 58174:
		 * c.getPA().removeAllWindows(); break;
		 * 
		 * case 104097: AuctionHouse.collectGold(c); break;
		 * 
		 * case 104119: AuctionHouse.openEntries(c); break;
		 * 
		 * case 104178: AuctionHouse.openMarket(c); break;
		 */

		case 19136: // Toggle quick prayers

			if (c.quickPray || c.quickCurse) {
				QuickCurses.turnOffQuicks(c);
				return;
			}
			if (c.duelRule[7]) {
				c.sendMessage("Prayer has been disabled in this duel!");
				return;
			}
			QuickCurses.turnOnQuicks(c);

			break;

		case 19137: // Select quick prayers
			if (c.duelRule[7]) {
				c.sendMessage("Prayer has been disabled in this duel!");
				return;
			}
			QuickCurses.selectQuickInterface(c);
			break;

		case 67079: // quick curse confirm
			if (c.duelRule[7]) {
				c.sendMessage("Prayer has been disabled in this duel!");
				return;
			}
			QuickCurses.clickConfirm(c);
			break;

		/*
		 * case 70082: //select your quick prayers/curses
		 * QuickCurses.selectQuickInterface(c); c.getPA().sendFrame106(5);
		 * break;
		 */
		case 23132:
			c.setSidebarInterface(1, 3917);
			c.setSidebarInterface(2, 638);
			c.setSidebarInterface(3, 3213);
			c.setSidebarInterface(4, 1644);
			c.setSidebarInterface(5, 5608);
			if (c.playerMagicBook == 0) {
				c.setSidebarInterface(6, 1151);
			} else if (c.playerMagicBook == 1) {
				c.setSidebarInterface(6, 12855);
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 29999);
			}
			c.setSidebarInterface(7, 18128);
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, 2449);
			c.setSidebarInterface(11, 904);
			c.setSidebarInterface(12, 147);
			c.setSidebarInterface(13, 962);
			c.setSidebarInterface(0, 2423);
			if (c.playerEquipment[c.playerRing] == 7927) {
				c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
				if (c.getItems().freeSlots() > 0) {
					c.getItems().addItem(7927, 1);
				} else {
					c.getItems().addItemToBank(7927, 1);
					c.sendMessage("Youre ring has been added to youre bank");
				}
			}
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.getPA().requestUpdates();
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon],
					c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
			c.getItems().updateSlot(c.playerRing);
			c.getPA().resetAnimation();
			break;
		case 113245:
			/*
			 * c.getPA().sendFrame126(Config.SERVER_NAME + " - Boss Log", 8144);
			 * c.getPA().sendFrame126( "@blu@Boss list:", 8145);
			 * c.getPA().sendFrame126( "@dre@Boss list:", 8147);
			 * c.getPA().showInterface(8134); c.flushOutStream();
			 */
			c.openBossLog();
			break;
		case 70209:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				return;
			}
			/*
			 * if (c.inGlobalCC && !c.inDicingCC) c.joinDicingCC(); else if
			 * (c.inDicingCC && !c.inGlobalCC) c.joinGlobalCC(); //
			 * c.getPA().removeAllWindows();
			 */break;
		// Clan Chat - Join Clan
		case 62137:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				break;
			}
			/*
			 * if (c.getOutStream() != null) {
			 * c.getOutStream().createFrame(187); c.flushOutStream(); }
			 */
			break;
		/* Bank tabs buttons */
		// Deposit worn items.
		case 89236:
			if (c.bankCheck) {
				boolean deposit = false;
				for (int i = 0; i < c.playerEquipment.length; i++)
					if (c.playerEquipment[i] > 0)
						deposit = true;
				if (!deposit) {
					c.sendMessage("You don't have any wornn items to deposit.");
					return;
				}
				for (int i = 0; i < c.playerEquipment.length; i++) {
					if (c.playerEquipment[i] > 0 && c.playerEquipmentN[i] > 0)
						c.getItems().addItemToBank(c.playerEquipment[i], c.playerEquipmentN[i]);
					c.getItems().replaceEquipment(i, -1);
				}
				// c.getPA().openUpBank(0);
				c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
				c.getCombat()
						.getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.getItems().resetBonus();
				c.getItems().writeBonus();
			}
			break;
		case 144139:// pvm
		case 152091:
			c.setSidebarInterface(13, 38000);
			break;
		case 144138:// pvp
		case 148114:
		case 152090:
			c.setSidebarInterface(13, 37000);
			break;
		case 144140:// minigame
		case 148116:
			c.setSidebarInterface(13, 39000);
			break;
		// pvp
		case 144141:
			c.getPA().spellTeleport(3291, 3918, 0);
			break;
		case 144142:
			c.getPA().spellTeleport(3086, 3516, 0);
			break;
		case 144143:
			c.getPA().spellTeleport(3344, 3667, 0);
			break;
		case 144144:
			c.getPA().spellTeleport(2539, 4716, 0);
			break;
		case 144145:
			c.getPA().spellTeleport(3017, 3631, 0);
			break;

		// pvm
		case 148118:
			c.getPA().spellTeleport(3030, 3844, 0);
			break;
		case 148117:
			c.getPA().spellTeleport(2581, 9451, 0);
			break;
		case 148119:
			c.getPA().spellTeleport(3242, 3783, 0);
			break;
		case 148120:
			c.getPA().spellTeleport(3212, 3936, 0);
			break;
		case 148121:
			c.getPA().spellTeleport(2871, 5317, 2);
			break;
		// minigame
		case 152093:
			c.getPA().spellTeleport(3565, 3314, 0);
			break;
		case 152094:
			c.getPA().spellTeleport(2605, 3153, 0);
			break;
		case 152095:
			c.getPA().spellTeleport(3372, 3267, 0);
			break;
		case 152096:
			c.getPA().spellTeleport(2444, 5170, 0);
			break;
		// Deposit carried items.
		/*
		 * case 86000: if (c.bankCheck) { boolean depositItems = false; for (int
		 * i = 0; i < c.playerItems.length; i++) if (c.playerItems[i] > 0)
		 * depositItems = true; if (!depositItems) { c.sendMessage(
		 * "You don't have any items to deposit."); return; } for (int i = 0; i
		 * < c.playerItems.length; i++) { if (c.playerItems[i] > 0 &&
		 * c.playerItemsN[i] > 0) { c.getItems().bankItem(c.playerItems[i], i,
		 * c.getItems().itemAmount(c.playerItems[i])); } } } break;
		 */

		/* Bank tabs. */
		case 86008:
			c.getPA().openBank(0);
			break;
		case 86009:
			if (c.bankItems1[0] > 0) {
				c.getPA().openBank(1);
			}
			break;
		case 86010:
			if (c.bankItems2[0] > 0) {
				c.getPA().openBank(2);
			}
			break;
		case 86011:
			if (c.bankItems3[0] > 0) {
				c.getPA().openBank(3);
			}
			break;
		case 86012:
			if (c.bankItems4[0] > 0) {
				c.getPA().openBank(4);
			}
			break;
		case 86013:
			if (c.bankItems5[0] > 0) {
				c.getPA().openBank(5);
			}
			break;
		case 86014:
			if (c.bankItems6[0] > 0) {
				c.getPA().openBank(6);
			}
			break;
		case 86015:
			if (c.bankItems7[0] > 0) {
				c.getPA().openBank(7);
			}
			break;
		case 86016:
			if (c.bankItems8[0] > 0) {
				c.getPA().openBank(8);
			}
			break;

		case 93196: // Bank Searching
			c.sendMessage("Coming Soon...");
			/*
			 * c.isSearching = true; c.isSearching2 = !c.isSearching2; if
			 * (!c.isSearching2) { if (c.getInStream() != null & c != null) {
			 * c.getOutStream().createFrame(187); c.flushOutStream();
			 * 
			 * } c.isSearching = true; c.isSearching2 = false;
			 * 
			 * } else { c.isSearching = false; c.isSearching2 = true; }
			 */

			break;
		case 31194:
			c.insert = 0;
			break;
		case 31195:
			c.insert = 1;
			break;
		case 21011:
			c.takeAsNote = false;
			break;

		case 22005:
			if (c.bankWithdraw != 0) {
				c.takeAsNote = false;
				c.bankWithdraw -= 1;
			} else if (c.bankWithdraw == 0) {
				c.bankWithdraw += 1;
				c.takeAsNote = true;
			}

			break;
		case 10239:
			if (c.isArrowing) {
				ArrowHandler.fletchArrow(c, 1);
				return;
			} else if (c.isStringing) {
				StringingHandler.stringBow(c, c.stringu, 1);
				return;
			} else if (c.isCutting) {
				GemCutting.cutGem(c, c.stringu, 1);
				return;
			} else if (c.isMakingPots) {
				PotionMaking.createPotion(c, c.itemUsed, c.useWith, c.itemUsedSlot, c.usedWithSlot, 1);
				return;
			}
			break;
		case 10238:
			if (c.isArrowing) {
				ArrowHandler.fletchArrow(c, 5);
				return;
			} else if (c.isStringing) {
				StringingHandler.stringBow(c, c.stringu, 5);
				return;
			} else if (c.isCutting) {
				GemCutting.cutGem(c, c.stringu, 5);
				return;
			} else if (c.isMakingPots) {
				PotionMaking.createPotion(c, c.itemUsed, c.useWith, c.itemUsedSlot, c.usedWithSlot, 5);
				return;
			}
			break;
		case 6212:
			if (c.isArrowing && !c.isStringing) {
				ArrowHandler.fletchArrow(c, 10);
				return;
			} else if (c.isStringing && !c.isArrowing) {
				StringingHandler.stringBow(c, c.stringu, 10);
				return;
			} else if (c.isCutting) {
				GemCutting.cutGem(c, c.stringu, 10);
				return;
			} else if (c.isMakingPots) {
				PotionMaking.createPotion(c, c.itemUsed, c.useWith, c.itemUsedSlot, c.usedWithSlot, 10);
				return;
			}
			break;
		case 6211:
			if (c.isArrowing) {
				ArrowHandler.fletchArrow(c, 25);
				c.sendMessage("You will fletch until you can't fletch no more.");
				return;
			} else if (c.isStringing) {
				StringingHandler.stringBow(c, c.stringu, 25);
				c.sendMessage("You will only fletch this up to 25 times.");
				return;
			} else if (c.isCutting) {
				GemCutting.cutGem(c, c.stringu, 25);
				c.sendMessage("You will only cut this up to 25 times.");
				return;
			} else if (c.isMakingPots) {
				PotionMaking.createPotion(c, c.itemUsed, c.useWith, c.itemUsedSlot, c.usedWithSlot, 25);
				return;
			}
			break;
		case 89223: // Bank All
			for (int i = 0; i < c.playerItems.length; i++) {
				c.getItems().bankItem(c.playerItems[i], i, c.playerItemsN[i]);
			}
			break;
		case 58074:
			c.getBankPin().close();
			break;

		case 58025:
		case 58026:
		case 58027:
		case 58028:
		case 58029:
		case 58030:
		case 58031:
		case 58032:
		case 58033:
		case 58034:
			c.getBankPin().pinEnter(actionButtonId);
			break;
		case 118098:
			c.getPA().castVeng();
			break;
		// crafting + fletching interface:
		case 150:
			if (c.autoRet == 0)
				c.autoRet = 1;
			else
				c.autoRet = 0;
			break;
		// 1st tele option
		case 9190:
			if (c.dialogueAction == 5733 && c.isStaff() && c.otherPlayerId != -1) {
				Connection.addIpToBanList(PlayerHandler.players[c.otherPlayerId].connectedFrom);
				Connection.addIpToFile(PlayerHandler.players[c.otherPlayerId].connectedFrom);

				c.sendMessage("You have IP banned the user: " + PlayerHandler.players[c.otherPlayerId].playerName
						+ " with the host: " + PlayerHandler.players[c.otherPlayerId].connectedFrom);
				PlayerHandler.players[c.otherPlayerId].disconnected = true;
				c.getPA().removeAllWindows();
				return;
			}
			if (c.teleAction == 1337) {
				c.getPA().startTeleport(2604, 3414, 0, "modern");
			} else if (c.teleAction == 12) {
				c.getPA().spellTeleport(3302, 9361, 0);
			} else if (c.teleAction == 11) {
				c.getPA().spellTeleport(3228, 9392, 0);
			} else if (c.teleAction == 10) {
				c.getPA().spellTeleport(2705, 9487, 0);
			} else if (c.teleAction == 9) {
				c.getPA().spellTeleport(3226, 3263, 0);
			} else if (c.teleAction == 8) {
				c.getPA().spellTeleport(3293, 3178, 0);
			} else if (c.teleAction == 7) {
				c.getPA().spellTeleport(3118, 9851, 0);
			} else if (c.teleAction == 35) {
				c.getPA().spellTeleport(2900, 4449, 0);
				break;
			} else if (c.teleAction == 1) {
				// rock crabs
				c.getPA().spellTeleport(2678, 3718, 0);
			} else if (c.teleAction == 2) {
				// barrows
				// c.getPA().spellTeleport(3565, 3314, 0);
				// c.getItems().addItem(952, 1);
			} else if (c.teleAction == 3) {
				// kbd
				c.getPA().spellTeleport(2273, 4687, 0);
			} else if (c.teleAction == 4) {
				// varrock wildy
				c.getPA().spellTeleport(2539, 4716, 0);
			} else if (c.teleAction == 5) {
				c.getPA().spellTeleport(3046, 9779, 0);
			} else if (c.teleAction == 6) {
				c.getPA().spellTeleport(3565, 3305, 0);
			} else if (c.teleAction == 69) {
				c.getPA().spellTeleport(2723, 3454, 0);
			} else if (c.teleAction == 20) {
				// lum
				c.getPA().spellTeleport(3222, 3218, 0);// 3222 3218
			} else {
				DiceHandler.handleClick(c, actionButtonId);
			}

			if (c.dialogueAction == 10) {
				c.getPA().spellTeleport(2845, 4832, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getPA().spellTeleport(2574, 4848, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.getPA().spellTeleport(2398, 4841, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 1338) {
				c.titleColor = 0;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
			}
			break;
		// mining - 3046,9779,0
		// smithing - 3079,9502,0

		// 2nd tele option
		case 9191:
			if (c.dialogueAction == 5733 && c.isStaff() && c.otherPlayerId != -1) {
				Connection.addNameToBanList(PlayerHandler.players[c.otherPlayerId].playerName);
				Connection.addNameToFile(PlayerHandler.players[c.otherPlayerId].playerName);
				if (PlayerHandler.players[c.otherPlayerId] != null) {
					if (PlayerHandler.players[c.otherPlayerId].playerName
							.equalsIgnoreCase(PlayerHandler.players[c.otherPlayerId].playerName)) {
						PlayerHandler.players[c.otherPlayerId].disconnected = true;
						c.getPA().removeAllWindows();
						return;
					}
				}
			}
			if (c.teleAction == 1337) {
				c.getPA().startTeleport(2469, 3435, 0, "modern");
			}
			if (c.teleAction == 6) {
				c.getPA().spellTeleport(2444, 5172, 0);
			}
			if (c.teleAction == 69) {
				c.getPA().spellTeleport(2480, 3437, 0);
			}
			if (c.teleAction == 35) {
				c.getPA().spellTeleport(3698, 5804, 0);
			}
			if (c.teleAction == 12) {
				c.getPA().spellTeleport(2908, 9694, 0);
			}
			if (c.teleAction == 11) {
				c.getPA().spellTeleport(3237, 9384, 0);
			}
			if (c.teleAction == 10) {
				c.getPA().spellTeleport(3219, 9366, 0);
			}
			if (c.teleAction == 9) {
				c.getPA().spellTeleport(2916, 9800, 0);
			}
			if (c.teleAction == 8) {
				c.getPA().spellTeleport(2903, 9849, 0);
			}
			if (c.teleAction == 7) {
				c.getPA().spellTeleport(2859, 9843, 0);
			}

			/*
			 * if (c.teleAction == 1) { //rock crabs
			 * c.getPA().spellTeleport(2676, 3715, 0); } else if (c.teleAction
			 * == 2) { //taverly dungeon c.getPA().spellTeleport(2884, 9798, 0);
			 * } else if (c.teleAction == 3) { //kbd
			 * c.getPA().spellTeleport(3007, 3849, 0); } else if (c.teleAction
			 * == 4) { //west lv 10 wild c.getPA().spellTeleport(2979, 3597, 0);
			 * } else if (c.teleAction == 5) {
			 * c.getPA().spellTeleport(3079,9502,0); }
			 */
			if (c.teleAction == 1) {
				// slay dungeon
				c.getPA().spellTeleport(2908, 9736, 0);
				return;
			} else if (c.teleAction == 2) {
				// pest control
				// c.getPA().spellTeleport(3252, 3894, 0);
			} else if (c.teleAction == 3) {
				// kbd
				// c.sendMessage("King Black Dragon has been disabled.");
				c.getPA().spellTeleport(3279, 3910, 0);
				// c.getPA().closeAllWindows();
			} else if (c.teleAction == 4) {
				// graveyard
				c.getPA().spellTeleport(3001, 3625, 0);
				c.getPA().resetAutocast();
			} else if (c.teleAction == 5) {
				c.getPA().spellTeleport(3079, 9502, 0);

			} else if (c.teleAction == 20) {
				c.getPA().spellTeleport(3210, 3424, 0);// 3210 3424
			}
			if (c.dialogueAction == 10) {
				c.getPA().spellTeleport(2791, 4832, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getPA().spellTeleport(2527, 4833, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.getPA().spellTeleport(2464, 4834, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 1338) {
				c.titleColor = 1;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				return;
			}
			// 3rd tele option

		case 53152:
			Cooking.getAmount(c, 1);
			break;
		case 53151:
			Cooking.getAmount(c, 5);
			break;
		case 53150:
			Cooking.getAmount(c, 10);
			break;
		case 53149:
			Cooking.getAmount(c, 28);
			break;

		case 9192:
			if (c.dialogueAction == 5733 && c.isStaff() && c.otherPlayerId != -1) {
				Connection.addNameToMuteList(PlayerHandler.players[c.otherPlayerId].playerName);
				if (PlayerHandler.players[c.otherPlayerId] != null) {
					if (PlayerHandler.players[c.otherPlayerId].playerName
							.equalsIgnoreCase(PlayerHandler.players[c.otherPlayerId].playerName)) {
						Client c2 = (Client) PlayerHandler.players[c.otherPlayerId];
						c2.sendMessage("You have been muted by: " + c.playerName);
						c.sendMessage("You have muted" + c2.playerName);
						c.getPA().removeAllWindows();
						return;
					}
				}
			}
			if (c.teleAction == 1337) {
				// c.getPA().spellTeleport(2816, 3463, 0);
				c.getPA().removeAllWindows();
				c.sendMessage("Farming is currently being re-developed.");
			}
			if (c.teleAction == 12) {
				c.getPA().spellTeleport(2739, 5088, 0);
			}
			if (c.teleAction == 11) {
				c.getPA().spellTeleport(3280, 9372, 0);
			}
			if (c.teleAction == 10) {
				c.getPA().spellTeleport(3241, 9364, 0);
			}
			if (c.teleAction == 9) {
				c.getPA().spellTeleport(3159, 9895, 0);
			}
			if (c.teleAction == 8) {
				c.getPA().spellTeleport(2912, 9831, 0);
			}
			if (c.teleAction == 7) {
				c.getPA().spellTeleport(2843, 9555, 0);
			}
			if (c.teleAction == 6) {
				c.getPA().spellTeleport(2663, 2652, 0);
			}
			if (c.teleAction == 35) {
				c.getPA().spellTeleport(2776, 9195, 0);
				break;
			}
			/*
			 * if (c.teleAction == 1) { //experiments
			 * c.getPA().spellTeleport(3555, 9947, 0); } else if (c.teleAction
			 * == 2) { //brimhavem dung c.getPA().spellTeleport(2709, 9564, 0);
			 * } else if (c.teleAction == 3) { //dag kings
			 * c.getPA().spellTeleport(2479, 10147, 0); } else if (c.teleAction
			 * == 4) { //easts lv 18 c.getPA().spellTeleport(3351, 3659, 0); }
			 * else if (c.teleAction == 5) {
			 * c.getPA().spellTeleport(2813,3436,0); }
			 */
			if (c.teleAction == 1) {
				// slayer tower
				// c.getPA().spellTeleport(2859, 9843, 0);
				c.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons", "Chaos Dwarf", "-- Next Page --");
				c.teleAction = 7;
			} else if (c.teleAction == 2) {
				// tzhaar
				// c.getPA().spellTeleport(2438, 5168, 0);
				// c.sendMessage("To fight Jad, enter the cave.");
			} else if (c.teleAction == 3) {
				// dag kings
				// c.getPA().spellTeleport(3565, 3314, 0);
				c.getPA().spellTeleport(2523, 3044, 0);
				// c.sendMessage("Climb down the ladder to get into the lair.");
			} else if (c.teleAction == 4) {
				// Hillz
				c.getPA().spellTeleport(3351, 3659, 0);
				c.getPA().resetAutocast();
			} else if (c.teleAction == 5) {
				c.getPA().spellTeleport(2813, 3436, 0);
			} else if (c.teleAction == 20) {
				c.getPA().spellTeleport(2757, 3477, 0);
			}
			if (c.dialogueAction == 10) {
				c.getPA().spellTeleport(2713, 4836, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getPA().spellTeleport(2162, 4833, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.getPA().spellTeleport(2207, 4836, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 1338) {
				c.titleColor = 2;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
			}

			break;
		// 4th tele option
		case 9193:
			if (c.dialogueAction == 5733 && c.isStaff() && c.otherPlayerId != -1) {
				if (PlayerHandler.players[c.otherPlayerId] != null) {
					if (PlayerHandler.players[c.otherPlayerId].playerName
							.equalsIgnoreCase(PlayerHandler.players[c.otherPlayerId].playerName)) {
						PlayerHandler.players[c.otherPlayerId].disconnected = true;
						c.getPA().removeAllWindows();
						return;
					}
				}
			}
			if (c.teleAction == 12) {
				c.getPA().spellTeleport(3062, 9539, 0);
				return;
			}
			if (c.teleAction == 11) {
				c.getDH().sendOption5("Black Demon", "Dust Devils", "Nechryael", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 10;
				break;
			}
			if (c.teleAction == 10) {
				c.getDH().sendOption5("Goblins", "Baby blue dragon", "Moss Giants", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 9;
				break;
			}
			if (c.teleAction == 9) {
				c.getDH().sendOption5("Al-kharid warrior", "Ghosts", "Giant Bats", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 8;
				break;
			}
			if (c.teleAction == 35) {
				c.getPA().spellTeleport(2974, 4384, 2);
				/*
				 * c.sendMessage(
				 * "This is a Bank use the chests to open your bank.");
				 * c.sendMessage(
				 * "Run East along the water for the Giant Sea Snake");
				 */
				// c.sendMessage("This feature is currently disabled.");
				c.sendMessage("@red@Have fun!");
				break;
			}
			if (c.teleAction == 8) {
				c.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons", "Chaos Dwarf", "-- Next Page --");
				c.teleAction = 7;
				break;
			}
			if (c.teleAction == 7) {
				c.getPA().spellTeleport(2923, 9759, 0);
			}
			if (c.teleAction == 1) {
				// brimhaven dungeon
				c.getPA().spellTeleport(2710, 9466, 0);
			} else if (c.teleAction == 2) {
				// duel arena
				c.getPA().spellTeleport(3366, 3266, 0);
			} else if (c.teleAction == 3) {
				// Godwars
				// c.getPA().spellTeleport(3565, 3314, 0);
				c.getDH().sendOption4("Armadyl", "Bandos", "Zamorak", "Saradomin");
				c.teleAction = 13; // godwars teleaction13
			} else if (c.teleAction == 20) {
				c.getPA().spellTeleport(2966, 3379, 0);
				// c.sendMessage("@red@Once you step out of the bank It's
				// PvP!");
			} else if (c.teleAction == 4) {
				// Fala
				c.getPA().resetAutocast();
				c.getPA().spellTeleport(3201, 3855, 0);
			} else if (c.teleAction == 5) {
				c.getPA().spellTeleport(2724, 3484, 0);
				c.sendMessage("For magic logs, try north of the duel arena.");
			} else if (c.teleAction == 6) {
				c.getPA().startTeleport(3368, 3267, 0, "modern");
				// } else if (c.teleAction == 300) {
				// c.getPA().startTeleport(3211, 3779, 0, "modern");
			}
			if (c.dialogueAction == 10) {
				c.getPA().spellTeleport(2660, 4839, 0);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				c.getPA().spellTeleport(2153, 3868, 0);
				// c.getRunecrafting().craftRunes(2489);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				c.sendMessage("Blood runes cannot be crafted.");
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 1338) {
				c.titleColor = 3;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
			}
			break;
		// 5th tele option
		case 9194:// troll
			if (c.dialogueAction == 5733 && c.isStaff() && c.otherPlayerId != -1) {
				if (PlayerHandler.players[c.otherPlayerId] != null) {
					if (PlayerHandler.players[c.otherPlayerId].playerName
							.equalsIgnoreCase(PlayerHandler.players[c.otherPlayerId].playerName)) {
						Client c2 = (Client) PlayerHandler.players[c.otherPlayerId];
						if (c2.inWild()) {
							c.sendMessage("You cant jail a player while he is in the wilderness.");
							return;
						}
						if (c2.duelStatus == 5) {
							c.sendMessage("You cant jail a player when he is during a duel.");
							return;
						}
						c2.teleportToX = 2095;
						c2.teleportToY = 4428;
						c2.sendMessage("You have been jailed by " + c.playerName + " .");
						c.sendMessage("Successfully Jailed " + c2.playerName + ".");
						c.getPA().removeAllWindows();
						return;
					}
				}
				return;
			}
			if (c.teleAction == 12) {
				c.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee", "-- Previous Page --", "-- Next Page --");
				c.teleAction = 11;
				break;
			}
			if (c.teleAction == 20) {
				c.getPA().spellTeleport(2663, 3307, 0);
				return;
			}
			if (c.teleAction == 8) {
				c.getDH().sendOption5("Goblins", "Baby blue dragon", "Moss Giants", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 9;
				break;
			}
			if (c.teleAction == 9) {
				c.getDH().sendOption5("Black Demon", "Dust Devils", "Nechryael", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 10;
				break;
			}
			if (c.teleAction == 11) {
				c.getDH().sendOption5("Infernal Mage", "Dark Beasts", "Abyssal Demon", "Skeletal Wyvern",
						"-- Previous Page --");
				c.teleAction = 12;
				break;
			}
			if (c.dialogueAction == 1338) {
				c.getDH().sendOption4("Black", "Yellow", "Cyan", "White");
				c.dialogueAction = 1339;
				break;
			}
			if (c.teleAction == 10) {
				c.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee", "-- Previous Page --", "-- Next Page --");
				c.teleAction = 11;
				break;
			}
			if (c.teleAction == 7) {
				c.getDH().sendOption5("Al-kharid warrior", "Ghosts", "Giant Bats", "-- Previous Page --",
						"-- Next Page --");
				c.teleAction = 8;
				break;
			}
			if (c.teleAction == 35) {
				c.getDH().sendOption3("Kalphite Queen", "Venenatis @red@(Wild)", "Callisto @red@Wild");
				c.teleAction = 300;
			}
			/*
			 * "Vet'ion @red@(Wild) if (c.teleAction == 1) { //island
			 * c.getPA().spellTeleport(2895, 2727, 0); } else if (c.teleAction
			 * == 2) { //last minigame spot c.sendMessage(
			 * "Suggest something for this spot on the forums!");
			 * c.getPA().closeAllWindows(); } else if (c.teleAction == 3) {
			 * //last monster spot c.sendMessage(
			 * "Suggest something for this spot on the forums!");
			 * c.getPA().closeAllWindows(); } else if (c.teleAction == 4) {
			 * //dark castle multi easts c.getPA().spellTeleport(3037, 3652, 0);
			 * } else if (c.teleAction == 5) {
			 * c.getPA().spellTeleport(2812,3463,0); }
			 */
			if (c.teleAction == 1) {
				// traverly
				// c.getPA().spellTeleport(3297, 9824, 0);
				// c.sendMessage("@red@There's just frost dragons, if you want
				// to kill green dragons you must go wilderness.");
				c.getPA().startTeleport(2884, 9798, 0, "modern");
				// 2884 9798
			} else if (c.teleAction == 2) {
				// last minigame spot
				// c.sendMessage("Suggest something for this spot on the
				// forums!");
				// c.getPA().closeAllWindows();
				// c.getPA().spellTeleport(2876, 3546, 0);
			} else if (c.teleAction == 3) {
				c.getDH().sendOption5("Dagganoth Kings", "Kraken", "Smoke Devil", "Corporeal Beast", "-- Next Page --");
				c.teleAction = 35;
			} else if (c.teleAction == 4) {
				// ardy lever
				c.getPA().spellTeleport(3689, 3468, 0);
				c.sendMessage("@cr9@<shad=0>@red@THIS AREA IS PVP, ONCE YOU LEAVE THE BANK IT IS DNAGEROUS!");
				c.getPA().resetAutocast();
			} else if (c.teleAction == 5) {
				c.getPA().spellTeleport(2812, 3463, 0);
			}
			if (c.dialogueAction == 10 || c.dialogueAction == 11) {
				c.dialogueId++;
				c.getDH().sendDialogues(c.dialogueId, 0);
			} else if (c.dialogueAction == 12) {
				c.dialogueId = 17;
				c.getDH().sendDialogues(c.dialogueId, 0);
			}
			break;

		/*
		 * case 71074: if (c.clanId >= 0) { if
		 * (Server.clanChat.clans[c.clanId].owner
		 * .equalsIgnoreCase(c.playerName)) { Server.clanChat
		 * .sendLootShareMessage( c.clanId, "Lootshare has been toggled to " +
		 * (!Server.clanChat.clans[c.clanId].lootshare ? "on" : "off") +
		 * " by the clan leader."); Server.clanChat.clans[c.clanId].lootshare =
		 * !Server.clanChat.clans[c.clanId].lootshare; } else c.sendMessage(
		 * "Only the owner of the clan has the power to do that." ); } break;
		 */
		/*
		 * case 34185: case 34184: case 34183: case 34182: case 34189: case
		 * 34188: case 34187: case 34186: case 34193: case 34192: case 34191:
		 * case 34190: if (c.craftingLeather)
		 * c.getCrafting().handleCraftingClick(actionButtonId); if
		 * (c.getFletching().fletching)
		 * c.getFletching().handleFletchingClick(actionButtonId); break;
		 */

		case 15147:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15151:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15159:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29017:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29022:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29026:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 58253:
			// c.getPA().showInterface(15106);
			c.getItems().writeBonus();
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;

		/*
		 * case 70212: if (c.clanId > -1) Server.clanChat.leaveClan(c.playerId,
		 * c.clanId); else c.sendMessage("You are not in a clan."); break;
		 */

		case 9178:
			if (c.dialogueAction == 11941) {
				c.getBag().addItem(c, c.bagItem, 1);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.teleAction == 13) {
				c.getPA().spellTeleport(2839, 5296, 2);
				c.sendMessage("You must know it's not easy, get a team to own that boss!");
				break;
			}
			if (c.dialogueAction == 1339) {
				c.titleColor = 4;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				break;
			}
			if (c.dialogueAction == 8203) {
				DuoSlayer.getInstance().assignDuo(c);
				return;
			}
			if (c.dialogueAction == 1658) {
				if (!c.getItems().playerHasItem(995, 2230000)) {
					c.sendMessage("You must have 2,230,000 coins to buy this package.");
					c.getPA().removeAllWindows();
					c.dialogueAction = 0;
					break;
				}
				c.dialogueAction = 0;
				c.getItems().addItemToBank(560, 4000);
				c.getItems().addItemToBank(565, 2000);
				c.getItems().addItemToBank(555, 6000);
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 2230000);
				c.sendMessage("@red@The runes has been added to your bank.");
				c.getPA().removeAllWindows();
				break;
			}
			if (c.usingGlory) {
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
				return;
			}
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(3428, 3538, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(3565, 3314, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 4, "modern");
				c.killCount = 0;
				break;
			}
			if (c.dialogueAction == 1338) {
				c.titleColor = 0;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				break;
			}

			break;

		case 9179:
			if (c.dialogueAction == 11941) {
				c.getBag().addItem(c, c.bagItem, 5);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1658) {
				if (!c.getItems().playerHasItem(995, 912000)) {
					c.sendMessage("You must have 912,000 coins to buy this package.");
					c.getPA().removeAllWindows();
					c.dialogueAction = 0;
					break;
				}
				c.dialogueAction = 0;
				c.getItems().addItemToBank(560, 2000);
				c.getItems().addItemToBank(9075, 4000);
				c.getItems().addItemToBank(557, 10000);
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 912000);
				c.sendMessage("@red@The runes has been added to your bank.");
				c.getPA().removeAllWindows();
				break;
			}

			if (c.teleAction == 13) {
				c.getPA().spellTeleport(2864, 5354, 2);
				c.sendMessage("You must know it's not easy, get a team to own that boss!");
				break;
			}
			if (c.dialogueAction == 8203) {
				DuoSlayer.cancelTask(c);
				return;
			}
			if (c.dialogueAction == 1339) {
				c.titleColor = 5;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				break;
			}
			if (c.usingGlory) {
				c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0, "modern");
				return;
			}
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2884, 3395, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(3243, 3513, 0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(2444, 5170, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 12, "modern");
				c.killCount = 0;
			}
			if (c.dialogueAction == 1338) {
				c.titleColor = 1;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				return;
			}
			break;

		case 9180:
			if (c.dialogueAction == 11941) {
				c.getBag().addItem(c, c.bagItem, 10);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.teleAction == 13) {
				c.getPA().spellTeleport(2925, 5331, 2);
				c.sendMessage("You must know it's not easy, get a team to own that boss!");
				break;
			}
			if (c.dialogueAction == 1658) {
				if (!c.getItems().playerHasItem(995, 1788000)) {
					c.sendMessage("You must have 1,788,000 coins to buy this package.");
					c.getPA().removeAllWindows();
					c.dialogueAction = 0;
					break;
				}
				c.dialogueAction = 0;
				c.getItems().addItemToBank(556, 1000);
				c.getItems().addItemToBank(554, 1000);
				c.getItems().addItemToBank(558, 1000);
				c.getItems().addItemToBank(557, 1000);
				c.getItems().addItemToBank(555, 1000);
				c.getItems().addItemToBank(560, 1000);
				c.getItems().addItemToBank(565, 1000);
				c.getItems().addItemToBank(566, 1000);
				c.getItems().addItemToBank(9075, 1000);
				c.getItems().addItemToBank(562, 1000);
				c.getItems().addItemToBank(561, 1000);
				c.getItems().addItemToBank(563, 1000);
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 1788000);
				c.sendMessage("@red@The runes has been added to your bank.");
				c.getPA().removeAllWindows();
				break;
			}
			if (c.dialogueAction == 8203) {
				c.getShops().openShop(49);
				c.sendMessage("You currently have @red@" + c.duoPoints + " @bla@duo slayer points.");
				return;
			}
			if (c.usingGlory) {
				c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0, "modern");
				return;
			}
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2471, 10137, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(3363, 3676, 0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(2659, 2676, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 8, "modern");
				c.killCount = 0;
			}
			if (c.dialogueAction == 1339) {
				c.titleColor = 6;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				break;
			}
			break;

		case 9181:
			if (c.dialogueAction == 11941) {
				c.getBag().addItem(c, c.bagItem, c.getItems().getItemAmount(c.bagItem));
				c.getPA().removeAllWindows();
				return;
			}
			if (c.teleAction == 13) {
				c.getPA().spellTeleport(2907, 5266, 0);
				c.sendMessage("You must know it's not easy, get a team to own that boss!");
				break;
			}
			if (c.dialogueAction == 1658) {
				c.getShops().openShop(5);
				c.dialogueAction = 0;
				break;
			}
			if (c.dialogueAction == 8203) {
				c.getItems().addItem(4155, 1);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.usingGlory) {
				c.getPA().startTeleport(Config.MAGEBANK_X, Config.MAGEBANK_Y, 0, "modern");
				return;
			}
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2669, 3714, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(2540, 4716, 0, "modern");
			if (c.dialogueAction == 4) {
				c.getPA().startTeleport(3366, 3266, 0, "modern");
				c.sendMessage("Dueling is at your own risk. Refunds will not be given for items lost due to glitches.");
			}
			if (c.dialogueAction == 20) {
				// c.getPA().startTeleport(3366, 3266, 0, "modern");
				// c.killCount = 0;
				c.sendMessage("This will be added shortly");
				break;
			}
			if (c.dialogueAction == 1339) {
				c.titleColor = 7;
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
				c.getPA().removeAllWindows();
				break;
			}
			break;

		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 11791
							|| c.playerEquipment[c.playerWeapon] == 12904 || c.playerEquipment[c.playerWeapon] == 6914
							|| c.playerEquipment[c.playerWeapon] == 12422)
						c.setSidebarInterface(0, 1689);
					else
						c.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[c.playerWeapon] == 4170) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;
		case 59097:
			if (c.inTrade) {
				return;
			}
			c.getPA().showInterface(15106);
			// c.getItems().writeBonus();
			break;
		case 59098: // items kept on death
			if (c.inTrade) {
				return;
			}
			c.getPA().sendFrame126("Items Kept on Death", 17103);
			c.StartBestItemScan(c);
			c.EquipStatus = 0;
			for (int k = 0; k < 4; k++)
				c.getPA().sendFrame34a(10494, -1, k, 1);
			for (int k = 0; k < 39; k++)
				c.getPA().sendFrame34a(10600, -1, k, 1);
			if (c.WillKeepItem1 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
			if (c.WillKeepItem2 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
			if (c.WillKeepItem3 > 0)
				c.getPA().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
			if (c.WillKeepItem4 > 0 && c.prayerActive[10])
				c.getPA().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
			for (int ITEM = 0; ITEM < 28; ITEM++) {
				if (c.playerItems[ITEM] - 1 > 0
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt2) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt3) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)
						&& c.playerItemsN[ITEM] > 1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
					c.EquipStatus += 1;
				}
			}
			for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
				if (c.playerEquipment[EQUIP] > 0
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - 1);
					c.EquipStatus += 1;
				}
			}
			c.ResetKeepItems();
			c.getPA().showInterface(17100);
			break;
		case 9157:// barrows tele to tunnels
			if (c.dialogueAction == 324) {
				if (c.getItems().playerHasItem(12004, 1) && c.getItems().playerHasItem(4151, 1)) {
					c.getItems().deleteItem(12004, c.getItems().getItemSlot(12004), 1);
					c.getItems().deleteItem(4151, c.getItems().getItemSlot(4151), 1);
					c.getItems().addItem(12006, 1);
					c.sendMessage("You have successfully added the Kraken Tentacle to your Abyssal Whip!");
					c.getPA().removeAllWindows();
				}
			}
			if (c.dialogueAction == 3439) {
				if (c.getItems().playerHasItem(995, 5000000)) {
					if (c.getItems().addItem(15086, 1)) {
						c.getItems().deleteItem(995, 5000000);
						c.sendMessage("You purchased a cape for 5M gold.");
					} else {
						c.sendMessage("Error completing transaction.");
					}
				} else {
					c.sendMessage("Not enough gold to make this purchase.");
				}
			}
			if (c.dialogueAction == 326) {
				if (c.getItems().playerHasItem(6585, 1) && c.getItems().playerHasItem(12526, 1)) {
					c.getItems().deleteItem(6585, c.getItems().getItemSlot(6585), 1);
					c.getItems().deleteItem(12526, c.getItems().getItemSlot(12526), 1);
					c.getItems().addItem(12436, 1);
					c.sendMessage("You have successfully added the Ornament Kit to the Fury!");
					c.getPA().removeAllWindows();
				}
			}
			if (c.dialogueAction == 3516) {
				c.getItems().addItem(15086, 1);
				c.sendMessage("You've obtained the Completionist Cape!");
			}
			if (c.dialogueAction == 879) {
				if (c.getPotentialPartner() != null)
					DuoSlayer.getInstance().accept(c, c.getPotentialPartner());
				else
					c.sendMessage("You do not have a open request.");
				return;
			}
			if (c.dialogueAction == 322) {
				c.getBarrows().checkCoffins();
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1) {
				int r = 4;
				// int r = Misc.random(3);
				switch (r) {
				case 0:
					c.getPA().movePlayer(3534, 9677, 0);
					break;

				case 1:
					c.getPA().movePlayer(3534, 9712, 0);
					break;

				case 2:
					c.getPA().movePlayer(3568, 9712, 0);
					break;

				case 3:
					c.getPA().movePlayer(3568, 9677, 0);
					break;
				case 4:
					c.getPA().movePlayer(3551, 9694, 0);
					break;
				}
			} else if (c.dialogueAction == 2) {
				c.getPA().movePlayer(2507, 4717, 0);
			} else if (c.dialogueAction == 6) {
				c.getSlayer().giveTask2();
				break;
			} else if (c.dialogueAction == 7) {
				c.getPA().startTeleport(3088, 3933, 0, "modern");
				c.sendMessage("NOTE: You are now in the wilderness...");
			} else if (c.dialogueAction == 8) {
				c.getPA().resetBarrows();
				c.sendMessage("Your barrows have been reset.");
			} else if (c.dialogueAction == 494) {
				if (!c.setPin) {
					c.getBankPin().open();
					return;
				} else {
					c.getPA().removeAllWindows();
					c.sendMessage("You currently already have a Bank Pin.");
				}
			} else if (c.dialogueAction == 1523) {
				if (c.playerRights == 0 && c.getItems().getItemAmount(621) >= 1) {
					c.getItems().deleteItem(621, 1);
					c.playerRights = 6;
					c.donPoints += 500;
					c.sendMessage("Thank you for Donating, enjoy the benefits!");
					c.disconnected = true;
				}
			} else if (c.dialogueAction == 9999) {
				c.getDH().sendOption2("Are you Really sure? You'll be reset!", "No! Never mind");
				c.dialogueAction = 9997;
				return;
			} else if (c.dialogueAction == 9997) {
				if (c.playerEquipment[c.playerHat] > 0 || c.playerEquipment[c.playerCape] > 0
						|| c.playerEquipment[c.playerAmulet] > 0 || c.playerEquipment[c.playerArrows] > 0
						|| c.playerEquipment[c.playerChest] > 0 || c.playerEquipment[c.playerLegs] > 0
						|| c.playerEquipment[c.playerRing] > 0 || c.playerEquipment[c.playerFeet] > 0
						|| c.playerEquipment[c.playerHands] > 0 || c.playerEquipment[c.playerWeapon] > 0
						|| c.playerEquipment[c.playerShield] > 0) {
					c.getPA().removeAllWindows();
					c.sendMessage("You can not be wearing Items while doing this.");
					return;
				}
				if (!c.inWild() && c.underAttackBy <= 0 && c.underAttackBy2 <= 0 && !c.inTrade) {
					for (int i = 0; i < c.bankItems[i]; i++) {
						c.bankItemsN[i] = -1;
						c.bankItems[i] = -1;
					}
					for (int i = 0; i < c.bankItems1[i]; i++) {
						c.bankItems1N[i] = -1;
						c.bankItems1[i] = -1;
					}
					for (int i = 0; i < c.bankItems2[i]; i++) {
						c.bankItems2N[i] = -1;
						c.bankItems2[i] = -1;
					}
					for (int i = 0; i < c.bankItems3[i]; i++) {
						c.bankItems3N[i] = -1;
						c.bankItems3[i] = -1;
					}
					for (int i = 0; i < c.bankItems4[i]; i++) {
						c.bankItems4N[i] = -1;
						c.bankItems4[i] = -1;
					}
					for (int i = 0; i < c.bankItems5[i]; i++) {
						c.bankItems5N[i] = -1;
						c.bankItems5[i] = -1;
					}
					for (int i = 0; i < c.bankItems6[i]; i++) {
						c.bankItems6N[i] = -1;
						c.bankItems6[i] = -1;
					}
					for (int i = 0; i < c.bankItems7[i]; i++) {
						c.bankItems7N[i] = -1;
						c.bankItems7[i] = -1;
					}
					for (int i = 0; i < c.bankItems8[i]; i++) {
						c.bankItems8N[i] = -1;
						c.bankItems8[i] = -1;
					}
					c.getItems().removeAllItems();
					for (int i = 0; i < 22; i++) {
						c.playerLevel[i] = 1;
						c.playerXP[i] = c.getPA().getXPForLevel(1);
						c.getPA().refreshSkill(i);
					}
					c.playerLevel[3] = 10;
					c.playerXP[3] = c.getPA().getXPForLevel(11);
					c.getPA().requestUpdates();
					c.playerRights = 10;
					c.slayerPoints = 0;
					c.duoPoints = 0;
					c.pkp = 0;
					c.hunterPoints = 0;
					c.totalXp = 0;
					c.petID = 0;
					c.darts = 0;
					c.dartType = 0;
					c.pcPoints = 0;
					c.disconnected = true;
					return;
				}
			} else if (c.dialogueAction == 639) {
				if (c.getItems().playerHasItem(995, 500000)) {
					c.getItems().deleteItem(995, 500000);
					Decanting.startDecanting(c);
				} else {
					c.sendMessage("@red@You must have 500k Cash to Decant Potions!");
				}
			} else if (c.dialogueAction == 1837) {
				c.getDH().sendDialogues(1838, c.npcType);
				c.easterStage = 1;
				c.startedEvent = true;
				return;
			} else if (c.dialogueAction == 1840 && c.easterStage == 2) {
				if (c.hasEggs()) {
					for (int i = 0; i < c.easterEggs.length; i++) {
						int itemId = c.easterEggs[i];
						c.getItems().deleteItem3(itemId, 1);
					}
				}
				if (c.getItems().freeSlots() > 1) {
					c.getItems().addItem(7927, 1);
					c.getItems().addItem(1961, 1);
					c.sendMessage(
							"You have Completed the Event and have received 1x @red@Easter Egg@bla@ and 1x @red@Easter Ring@bla@.");
				} else {
					c.getItems().addItemToBank(7927, 1);
					c.getItems().addItemToBank(1961, 1);
					c.sendMessage(
							"You have Completed the Event and 1x @red@Easter Egg@bla@ and 1x @red@Easter Ring@bla@ has been banked.");
				}
			}
			c.dialogueAction = 0;
			c.getPA().removeAllWindows();
			break;

		case 9158:
			if (c.dialogueAction == 879) {
				DuoSlayer.getInstance().decline(c, c.getPotentialPartner());
				return;
			}
			if (c.dialogueAction == 8) {
				c.getPA().fixAllBarrows();
			} else {
				c.dialogueAction = 0;
				c.getPA().removeAllWindows();
			}
			break;
		case 9167:
			if (c.dialogueAction == 1758) {
				c.calculateTotalPKP();
				c.getItems().deleteItem(12746, 28);
				c.getItems().deleteItem(12748, 28);
				c.getItems().deleteItem(12749, 28);
				c.getItems().deleteItem(12748, 28);
				c.getItems().deleteItem(12750, 28);
				c.getItems().deleteItem(12751, 28);
				c.getItems().deleteItem(12752, 28);
				c.getItems().deleteItem(12753, 28);
				c.getItems().deleteItem(12754, 28);
				c.getItems().deleteItem(12755, 28);
				c.getItems().deleteItem(12756, 28);
				if (c.pkpTotal != 0)
					c.pkPoints += c.pkpTotal;
				c.sendMessage("@red@You have gained " + c.pkpTotal + " PK Points");
				c.pkpTotal = 0;
				c.getPA().closeAllWindows();
				break;
			}
			if (c.teleAction == 300) {
				c.getPA().spellTeleport(3503, 9494, 0);
				break;
			}
			if (c.dialogueAction == 2245) {
				c.getPA().startTeleport(2158, 3860, 0, "modern");
				c.getPA().closeAllWindows();
			}
			if (c.dialogueAction == 5) {
				c.getSlayer().giveTask();
				break;
			}
			if (c.dialogueAction == 2040) {
			//	Zulrah.init(c);
				// if (InstanceManager.instanceManager.canCreateInstance(c,
				// actionButtonId)) {
				// InstanceManager.createInstance(c, actionButtonId);
				// }
			} else {
				c.getPA().closeAllWindows();
				c.dialogueAction = -1;
			}
			c.dialogueAction = -1;
		case 9168:
			if (c.dialogueAction == 1758) {
				c.calculateTotal();
				c.getItems().deleteItem(12746, 28);
				c.getItems().deleteItem(12748, 28);
				c.getItems().deleteItem(12749, 28);
				c.getItems().deleteItem(12748, 28);
				c.getItems().deleteItem(12750, 28);
				c.getItems().deleteItem(12751, 28);
				c.getItems().deleteItem(12752, 28);
				c.getItems().deleteItem(12753, 28);
				c.getItems().deleteItem(12754, 28);
				c.getItems().deleteItem(12755, 28);
				c.getItems().deleteItem(12756, 28);
				if (c.bountyTotal != 0)
					c.getItems().addItem(995, c.bountyTotal);
				c.sendMessage("@red@You have gained " + c.bountyTotal + " Gold");
				c.getPA().closeAllWindows();
				break;
			}
			if (c.teleAction == 300) {
				c.getPA().spellTeleport(3264, 3735, 0);
				return;
			}
			if (c.dialogueAction == 2245) {
				c.getPA().startTeleport(3233, 2887, 0, "modern");
				c.getPA().closeAllWindows();
				break;
			}
			if (c.dialogueAction == 5) {
				// System.out.println("here");
				c.getSlayer().giveElite();
				break;
			}
			if (c.dialogueAction == 2040) {
				c.getPA().removeAllWindows();
				c.getPA().returnZulrahItems();
			}
			c.dialogueAction = -1;
			break;
		case 9169:
			if (c.teleAction == 300) {
				c.getPA().spellTeleport(3245, 3783, 0);
				c.sendMessage("Run north for Callisto!");
				return;
			}
			if (c.dialogueAction == 2245) {
				c.getPA().closeAllWindows();
			}
			if (c.dialogueAction == 1552) {
				c.getPA().closeAllWindows();
			}
			if (c.dialogueAction == 2040) {
				c.getPA().closeAllWindows();
			}
			break;
		/** Specials **/
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
			// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			c.specBarId = 7486;
			/*
			 * if (c.specAmount >= 5) { c.attackTimer = 0;
			 * c.getCombat().attackPlayer(c.playerIndex); c.usingSpecial = true;
			 * c.specAmount -= 5; }
			 */
			if (c.playerEquipment[c.playerWeapon] == 4153) {
				c.specBarId = 7486;
				c.getCombat().handleGmaul();
			} else {
				c.specBarId = 7486;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;

		case 29063:
			if (c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
				c.gfx0(246);
				c.forcedChat("Raarrrrrgggggghhhhhhh!");
				c.startAnimation(1056);
				c.playerLevel[2] = c.getLevelForXP(c.playerXP[2]) + (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.duelSlot = -1;
			c.getDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getDuel().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getDuel().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getDuel().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getDuel().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getDuel().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getDuel().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getDuel().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getDuel().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getDuel().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getDuel().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getDuel().selectRule(21);
			break;

	
		case 26018:
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				c.getDuel().declineDuel();
				return;
			}
			if (!c.inDuelArena() || !o.inDuelArena()) {
				c.getDuel().declineDuel();
				o.getDuel().declineDuel();
				return;
			}
			if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
				c.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			c.duelStatus = 2;
			if (c.duelStatus == 2) {
				c.getPA().sendFrame126("Waiting for other player...", 6684);
				o.getPA().sendFrame126("Other player has accepted.", 6684);
			}
			if (o.duelStatus == 2) {
				o.getPA().sendFrame126("Waiting for other player...", 6684);
				c.getPA().sendFrame126("Other player has accepted.", 6684);
			}

			if (c.duelStatus == 2 && o.duelStatus == 2) {
				c.canOffer = false;
				o.canOffer = false;
				c.duelStatus = 3;
				o.duelStatus = 3;
				c.getDuel().confirmDuel();
				o.getDuel().confirmDuel();
			}
			break;

		case 25120:
			if (c.duelStatus == 5) {
				break;
			}
			Client o1 = (Client) PlayerHandler.players[c.duelingWith];
			if (o1 == null) {
				c.getDuel().declineDuel();
				return;
			}
			if (!c.inDuelArena() || !o1.inDuelArena()) {
				c.getDuel().declineDuel();
				o1.getDuel().declineDuel();
				return;
			}
			c.duelStatus = 4;
			if (o1.duelStatus == 4 && c.duelStatus == 4) {
				c.getDuel().startDuel();
				o1.getDuel().startDuel();
				o1.duelCount = 4;
				c.duelCount = 4;
				c.duelDelay = System.currentTimeMillis();
				o1.duelDelay = System.currentTimeMillis();
			} else {
				c.getPA().sendFrame126("Waiting for other player...", 6571);
				o1.getPA().sendFrame126("Other player has accepted", 6571);
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(Client.MAGIC_SPELLS[48][3]);
			c.startAnimation(Client.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 28164: // item kept on death
			break;

		case 152:
			if (c.runEnergy < 1) {
				c.isRunning = false;
				c.getPA().sendFrame36(173, 0);
				break;
			}
			c.isRunning2 = !c.isRunning2;
			int frame = c.isRunning2 == true ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;

		case 9154:
			c.logout();
			break;

		/**
		 * @ Notes
		 */
		case 85248:
			// c.getPA().setConfig(22004, 1);//freidns on and wanting to do it.
			// id and aopkss?
			c.takeAsNote = true;
			break;

		case 21010:
			c.takeAsNote = true;
			break;

		// home teleports
		case 4171:
		case 50056:
		case 117048:
			c.randomTeleport();
			break;

		case 50235:
		case 4140:
		case 117112:
			// c.getPA().startTeleport(Config.LUMBY_X, Config.LUMBY_Y, 0,
			// "modern");
			c.getDH().sendOption5("Rock Crabs", "Hill Giants", "Slayer Monsters", "Brimhaven Dungeon",
					"Traverly Dungeon");
			c.teleAction = 1;
			break;

		case 4143:
		case 50245:
		case 117123:
			c.getDH().sendOption5("Woodcutting", "Agility", "Mining", "Fishing", "farming");
			c.teleAction = 69;
			// c.getPA().startTeleport(3369, 3267, 0, "modern");
			break;

		case 50253:
		case 117131:
		case 4146:
			c.getDH().sendOption5("King Black Dragon", "Chaos Elemental @red@(Wild)", "Barrelchest Boss", "Godwars",
					"-- Next Page --");
			c.teleAction = 3;
			break;

		case 51005:
		case 117154:
		case 4150:
			c.getDH().sendOption5("Mage Bank @gre@(Safe)", "West Dragons @red@(14 Wild)", "East Dragons @red@(18 Wild)",
					"Lava Dragons @red@(44 Wild)", "Port Phasmatys @red@(PvP)");
			c.teleAction = 4;
			break;
		case 51023:
		case 117186:
		case 6005:
			c.getDH().sendOption5("Barrows", "Fight Caves", "Pest control", "Duel Arena", "Coming Soon");
			c.teleAction = 6;
			break;
		case 51013:
		case 6004:
		case 117162:
			c.getDH().sendOption5("Lumbridge", "Varrock", "Camelot", "Falador", "Ardougne");
			c.teleAction = 20;
			break;

		// case 4131:
		// case 50245:
		// case 117123:
		// case 51031:
		// case 29031:
		// case 117194:
		// c.getDH().sendOption5("Fishing", "Agility", "Farming",
		// "Mining", "Woodcutting");
		// c.teleAction = 1337;
		// break;

		// case 72038:
		case 51039:
			// c.getDH().sendOption5("Option 18", "Option 2", "Option 3",
			// "Option 4", "Option 5");
			// c.teleAction = 8;
			break;

		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 22230: // unarmed
			c.fightMode = 0;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 18078: // block (spear)
		case 33018: // jab (hally)
		case 8235: // block (dagger)
			// case 22231: //unarmed
		case 22228: // unarmed
			c.fightMode = 1;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
		case 33020: // swipe (hally)
			c.fightMode = 3;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33019: // fend (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
		case 22229: // unarmed
			c.fightMode = 2;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		/** Prayers **/
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 70080: // range
			c.getCombat().activatePrayer(3);
			break;
		case 70082: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 70088: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 70090: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // retrui
			if (!c.inClanWarsGame)
				c.getCombat().activatePrayer(21);
			else
				c.sendMessage("You cant use this Prayer in this minigame!");
			break;
		case 2172: // redem
			if (!c.inClanWarsGame)
				c.getCombat().activatePrayer(22);
			else
				c.sendMessage("You cant use this Prayer in this minigame!");
			break;
		case 2173: // smite
			if (!c.inClanWarsGame)
				c.getCombat().activatePrayer(23);
			else
				c.sendMessage("You cant use this Prayer in this minigame!");
			break;
		case 70092: // chiv
			if (!c.inClanWarsGame)
				c.getCombat().activatePrayer(24);
			else
				c.sendMessage("You cant use this Prayer in this minigame!");
			break;
		case 70094: // piety
			if (!c.inClanWarsGame)
				c.getCombat().activatePrayer(25);
			else
				c.sendMessage("You cant use this Prayer in this minigame!");
			break;

		case 13092:
			c.getTradeHandler().acceptStage1();
			break;

		case 13218:
			c.getTradeHandler().acceptStage2();
			break;
			
		case 86000:
			if (c.bankCheck) {
				boolean depositItems = false;
				for (int i = 0; i < c.playerItems.length; i++)
					if (c.playerItems[i] > 0)
						depositItems = true;
				if (!depositItems) {
					c.sendMessage("You don't have any items to deposit.");
					return;
				}
				for (int i = 0; i < c.playerItems.length; i++) {
					if (c.playerItems[i] > 0 && c.playerItemsN[i] > 0) {
						c.getItems().bankItem(c.playerItems[i], i, c.getItems().itemAmount(c.playerItems[i]));
					}
				}
			}
			break;

		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		case 168:
			c.startAnimation(855);
			break;
		case 169:
			c.startAnimation(856);
			break;
		case 162:
			c.startAnimation(857);
			break;
		case 164:
			c.startAnimation(858);
			break;
		case 165:
			c.startAnimation(859);
			break;
		case 161:
			c.startAnimation(860);
			break;
		case 170:
			c.startAnimation(861);
			break;
		case 171:
			c.startAnimation(862);
			break;
		case 163:
			c.startAnimation(863);
			break;
		case 167:
			c.startAnimation(864);
			break;
		case 172:
			c.startAnimation(865);
			break;
		case 166:
			c.startAnimation(866);
			break;
		case 52050:
			c.startAnimation(2105);
			break;
		case 52051:
			c.startAnimation(2106);
			break;
		case 52052:
			c.startAnimation(2107);
			break;
		case 52053:
			c.startAnimation(2108);
			break;
		case 52054:
			c.startAnimation(2109);
			break;
		case 52055:
			c.startAnimation(2110);
			break;
		case 52056:
			c.startAnimation(2111);
			break;
		case 52057:
			c.startAnimation(2112);
			break;
		case 52058:
			c.startAnimation(2113);
			break;
		case 43092:
			c.startAnimation(0x558);
			break;
		case 2155:
			c.startAnimation(0x46B);
			break;
		case 25103:
			c.startAnimation(0x46A);
			break;
		case 25106:
			c.startAnimation(0x469);
			break;
		case 2154:
			c.startAnimation(0x468);
			break;
		case 52071:
			c.startAnimation(0x84F);
			break;
		case 52072:
			c.startAnimation(0x850);
			break;
		case 59062:
			c.startAnimation(2836);
			break;
		case 72032:
			c.startAnimation(3544);
			break;
		case 72033:
			c.startAnimation(3543);
			break;
		case 72254:
			c.startAnimation(6111);
			break;
		/* END OF EMOTES */
		case 28166:

			break;
		case 33206:
			// c.getPA().vengMe();
			// SkillMenu.openInterface(c, -1)
			// SkillMenu.openInterface(c,0);
			break;
		case 33212:
			// SkillMenu.openInterface(c, 1);
			break;
		case 33209:
			// SkillMenu.openInterface(c,2);
			break;
		case 33215:
			// SkillMenu.openInterface(c, 4);
			break;

		case 47130:
			c.forcedText = "I must slay another " + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask)
					+ ".";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			break;

		case 24017:
			c.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon],
					c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
	}
}
