package server.model.items;

import server.model.players.Client;
import server.model.players.skills.cooking.Cooking;
import server.model.players.skills.crafting.GemCrafting;
import server.model.players.skills.crafting.GemCutting;
import server.model.players.skills.crafting.GemData;
import server.model.players.skills.crafting.LeatherCrafting;
import server.model.players.skills.firemaking.FireHandler;
import server.model.players.skills.firemaking.Firemaking;
import server.model.players.skills.fletching.BowHandler;
import server.model.players.skills.fletching.DartFletching;
import server.model.players.skills.fletching.StringingHandler;
import server.model.content.Blowpipe;
import server.model.content.ItemKit;
import server.model.content.PotionMaking;
import server.model.content.RunePouch;
import server.model.minigames.CrystalChest;
import server.util.Misc;
import server.Config;

/**
 * 
 * @author Ryan / Lmctruck30
 * 
 */

public class UseItem {

	public static void ItemonObject(Client c, int objectID, int objectX,
			int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		switch (objectID) {
//		case 11744:
//		case 21301:
//			if (itemId == 11941) {
//				c.depositLootBag();
//			}
//			break;
		case 14921:
		case 9390:
		case 2781:
		case 2785:
		case 2966:
		case 3044:
		case 3294:
		case 3413:
		case 4304:
		case 4305:
		case 6189:
		case 6190:
		case 11009:
		case 11010:
		case 11666:
		case 12100:
		case 10082:
		case 24009:
		case 12809:
			if (itemId == GemCrafting.GOLD_BAR) {
				GemCrafting.openInterface(c);
			}
			break;
		case 8151:
		case 8389:
		case 8132:
		case 7848: // /flower patch catherby
		case 7517:
			c.getFarming().checkItemOnObject(itemId);
			break;
		case 2783:
		case 2031:
		case 2097:
			c.getSmithingInt().showSmithInterface(itemId);
			break;
		case 2728:// cooking
		case 2732:
		case 9682:
		case 114:
		case 9374:
		case 25465:
		case 11404:
		case 11405:
		case 11406:
			Cooking.cookThisFood(c, itemId, objectID);
			break;
		case 409:
			if (c.getPrayer().isBone(itemId) && c.usingAltar == false) {
				c.usingAltar = true;
				c.getPA().refreshSkill(5);
				// c.startAnimation(645);
				c.getOutStream().createFrame(27);
				c.altarItemId = itemId;
			}
			break;
		case 75:
			if (itemId == CrystalChest.KEY)
				CrystalChest.searchChest(c, objectID, objectX, objectY);
			break;
		default:
			if (c.playerRights == 3)
				Misc.println("Player At Object id: " + objectID
						+ " with Item id: " + itemId);
			break;
		}

	}

	public static void ItemonItem(Client c, int itemUsed, int useWith) {
		c.logID = -1;
		c.isSkilling[9] = false;
		if (c.fireMaking) {
			c.fireMaking = false;
		}
		c.isSkilling[c.playerCrafting] = false;
		c.isSkilling[c.playerHerblore] = false;
		Cooking.resetCooking(c);
		if (LeatherCrafting.handleItemonItem(c, itemUsed, useWith))
			return;
		for (int i = 0; i < GemData.stringItems.length; i++) {
			if (GemData.stringItems[i][0] == itemUsed || GemData.stringItems[i][0] == useWith) {
				GemCrafting.string(c, i);
				return;
			}
		}
		ItemKit.handleZulrahItems(c, itemUsed, useWith);
		ItemKit.handleInfinityKit(c, itemUsed, useWith);
		ItemKit.handleWardKit(c, itemUsed, useWith);
		ItemKit.handleDPickKit(c, itemUsed, useWith);
		ItemKit.handleWhipColorKit(c, itemUsed, useWith);
		ItemKit.handleSaraTear(c, itemUsed, useWith);
		ItemKit.handleDragonChainKit(c, itemUsed, useWith);
		ItemKit.handleDragonPlateLegsKit(c, itemUsed, useWith);
		PotionMaking.createAntiVenom(c, itemUsed, useWith);
		//RunePouch.runeOnPouch(c, itemUsed, useWith);
		if (itemUsed == 2440 && useWith == 2436 || itemUsed == 2440
				&& useWith == 2442 || itemUsed == 2440 && useWith == 2998
				|| itemUsed == 2436 && useWith == 2440 || itemUsed == 2436
				&& useWith == 2442 || itemUsed == 2436 && useWith == 2998
				|| itemUsed == 2442 && useWith == 2436 || itemUsed == 2442
				&& useWith == 2440 || itemUsed == 2442 && useWith == 2998
				|| itemUsed == 2998 && useWith == 2436 || itemUsed == 2998
				&& useWith == 2440 || itemUsed == 2998 && useWith == 2442) {
			if (c.ironDonator(c.playerName) || c.playerRights >= 6
					&& c.playerRights <= 9)
				c.combatPot();
			else
				c.sendMessage("You must be a donator to make this!");
		}
		if (itemUsed >= 11931 && itemUsed <= 11933 && useWith >= 11931
				&& useWith <= 11933) {
			if (c.getItems().hasAllWShards()) {
				c.getItems().makeWshield();
			}
		}
		if (itemUsed == 4151 && useWith == 12004 || itemUsed == 12004
				&& useWith == 4151) {
			c.getDH().sendDialogues(324, -1);
		}
		if (itemUsed == 6585 && useWith == 12526 || itemUsed == 12526
				&& useWith == 6585) {
			c.getDH().sendDialogues(326, -1);
		}
		if (itemUsed >= 11928 && itemUsed <= 11930 && useWith >= 11928
				&& useWith <= 11930) {
			if (c.getItems().hasAllOShards()) {
				c.getItems().makeOshield();
			}
		}
		if (itemUsed == CrystalChest.toothHalf()
				&& useWith == CrystalChest.loopHalf()
				|| itemUsed == CrystalChest.loopHalf()
				&& useWith == CrystalChest.toothHalf()) {
			CrystalChest.makeKey(c);
		}
		if (itemUsed == Blowpipe.BLOWPIPE && useWith == Blowpipe.BRONZE_DART
				|| useWith == Blowpipe.IRON_DART
				|| useWith == Blowpipe.STEEL_DART
				|| useWith == Blowpipe.MITH_DART
				|| useWith == Blowpipe.ADDY_DART
				|| useWith == Blowpipe.RUNE_DART
				|| useWith == Blowpipe.DRAGON_DART
				|| itemUsed == Blowpipe.BRONZE_DART
				|| itemUsed == Blowpipe.IRON_DART
				|| itemUsed == Blowpipe.STEEL_DART
				|| itemUsed == Blowpipe.MITH_DART
				|| itemUsed == Blowpipe.ADDY_DART
				|| itemUsed == Blowpipe.RUNE_DART
				|| itemUsed == Blowpipe.DRAGON_DART
				&& useWith == Blowpipe.BLOWPIPE)
			Blowpipe.addDart(c, itemUsed, useWith);
		if (itemUsed == 314 && useWith == DartFletching.BRONZE_TIP
				|| useWith == DartFletching.IRON_TIP
				|| useWith == DartFletching.STEEL_TIP
				|| useWith == DartFletching.MITH_TIP
				|| useWith == DartFletching.ADDY_TIP
				|| useWith == DartFletching.RUNE_TIP
				|| itemUsed == DartFletching.BRONZE_TIP
				|| itemUsed == DartFletching.IRON_TIP
				|| itemUsed == DartFletching.STEEL_TIP
				|| itemUsed == DartFletching.MITH_TIP
				|| itemUsed == DartFletching.ADDY_TIP
				|| itemUsed == DartFletching.RUNE_TIP && useWith == 314)
			DartFletching.makeDarts(c, itemUsed, useWith);
		/*if (itemUsed == 227 || useWith == 227)
			c.getHerblore().handlePotMaking(itemUsed, useWith);*/
		if (c.getItems().getItemName(itemUsed).contains("(")
				&& c.getItems().getItemName(useWith).contains("("))
			c.getPotMixing().mixPotion2(itemUsed, useWith);
		/*if (itemUsed == 1755 || useWith == 1755)
			c.getCrafting().handleChisel(itemUsed, useWith);*/
		if ((itemUsed == 1540 && useWith == 11286)
				|| (itemUsed == 11286 && useWith == 1540)) {
			if (c.playerLevel[c.playerSmithing] >= 95) {
				c.getItems()
						.deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286),
						1);
				c.getItems().addItem(11284, 1);
				c.sendMessage("You combine the two materials to create a dragonfire shield.");
				c.getPA().addSkillXP(500 * Config.SMITHING_EXPERIENCE,
						c.playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		if (itemUsed >= 11818 && itemUsed <= 11822 && useWith >= 11818
				&& useWith <= 11822) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366
				&& useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
		}

		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11798) {
				c.getItems().makeGodsword(hilt);
			}
		}

		switch (itemUsed) {
		case 1511:
		case 1521:
		case 1519:
		case 1517:
		case 1515:
		case 1513:
		case 590:
			Firemaking.attemptFire(c, itemUsed, useWith, c.absX, c.absY, false);
			break;

		default:
			if (c.playerRights == 3)
				Misc.println("Player used Item id: " + itemUsed
						+ " with Item id: " + useWith);
			break;
		}
		BowHandler.handleInterface(c, useWith, itemUsed);
		StringingHandler.useItemInterface(c, useWith, itemUsed);
		GemCutting.useItemInterface(c, useWith, itemUsed);
		PotionMaking.useItemInterface(c, useWith, itemUsed);
	}

	public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
		switch (itemId) {

		default:
			if (c.playerRights == 3)
				Misc.println("Player used Item id: " + itemId
						+ " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
