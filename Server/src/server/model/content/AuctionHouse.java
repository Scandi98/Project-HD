package server.model.content;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import server.Config;
import server.Server;
import server.model.content.logging.AuctionLogger;
import server.model.players.Client;
import server.util.Misc;

/**
 * @author Linus @ Rune-Server
 * @license your_name NOT FOR RELEASE NOT FOR RESALE NOT FOR SHARING
 */

public class AuctionHouse {

	/*public static int maxMarket = 50; // how many slots do we want open?

	private static int[] PROHIBITED_ITEMS = { 7927, 7928, 7929, 7930, 7931, 7932,
			7933, 995, 12654, 12655, 10548, 15086, 2697, 2696, 2695, 2693,
			2692, 2691, 2690, 2688, 2698, 2699, 2700, 2701, 2702, 2703, 2704,
			2705, 2706, 2707, 2708, 2709, 2710, 2711, 2712, 2713, 2714, 2802,
			2775, 12653, 12649, 12650, 12651, 12652, 12645, 12644, 12643,
			13263, 12745, 15004, 8850, 10551, 8839, 8840, 8842, 11663, 11664,
			11665, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 8850,
			10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454,
			8839, 8840, 8842, 11663, 11664, 11665, 10499, 9748, 9754, 9751,
			9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796,
			9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770,
			9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794,
			9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756,
			9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774,
			9771, 9777, 9786, 9810, 9765, 8714, 8716, 8718, 8720, 8722, 8724,
			8726, 8728, 8730, 8732, 8734, 8736, 8738, 8740, 8742, 8744 }; // disallowed
																			// item
																			// ids

	public static int[] auctionItems = new int[maxMarket]; // array containing
															// item id's
	public static int[] auctionAmount = new int[maxMarket]; // array containing
															// item amounts
	public static int[] auctionPrice = new int[maxMarket]; // array containing
															// item prices
	public static String[] auctionOwner = new String[maxMarket]; // array
																	// containing
																	// item
																	// owners
	public static int itemsInMarket = 0; // how many slots are being used?

	public static String[] offerOwner = new String[maxMarket]; // array
																// containing
																// item owners
	public static int[] offerItems = new int[maxMarket]; // array containing
															// item id's
	public static int[] offerAmount = new int[maxMarket]; // array containing
															// item amounts
	public static int[] offerPrice = new int[maxMarket]; // array containing
															// item prices
	public static int offersInMarket = 0; // how many slots are being used?

	public static String[] unclaimedOwner = new String[1000]; // array
																// containing
																// item owners
																// that can
																// claim their
																// money
	public static String[] unclaimedItemName = new String[1000]; // array
																	// containing
																	// item name
																	// that can
																	// be
																	// claimed
	public static int[] unclaimedGold = new int[1000]; // array containing gold
														// amount to be claimed
	public static int unclaimedGoldCount = 0; // how many players have gold
												// waiting?

	public static String[] boughtOwner = new String[1000]; // array containing
															// item owners of
															// which can be
															// claimed
	public static int[] boughtItems = new int[1000]; // array containing items
														// that can be claimed
	public static int[] boughtAmount = new int[1000]; // array containing item
														// amounts
	public static int boughtItemsCount = 0; // how many slots are being used?

	public static String[] marketFeed = new String[16];
	public static String[] recentItems = new String[16];
	public static String toPrint = "NaN";
	public static String toPrint1 = "NaN";
	public static boolean marketOpen = true;

	// this is important if you know you have more than 1 item of any item, eg,
	// a nulled santa hat,
	public static int getOtherId(int itemId) {
		switch (itemId) {

		case 9976:
			return 10033;
		case 9977:
			return 10034;

		default:
			return itemId;
		}
	}

	public static int allowedEntries(Client c) {
		if (c.playerRights == 1) {
			return 4;
		}
		if (c.playerRights >= 2) {
			return 6;
		}
		return 2;
	}

	public static void openEntries(Client c) {
		if (!marketOpen) {
			c.sendMessage("The Auction House has been disabled for now.");
			return;
		}

		c.getPA().closeAllWindows();
		// c.getPA().closeAllWindows();
		if (c.inTrade) {
			return;
		}
		
		 * c.marketItemId = -1; c.marketItemAmount = 1; c.marketItemValue = 1;
		 * updateMarketText(c);
		 
		setEntryFeed(c);
		// c.getPA().showInterface(26840);

		// c.getPA().showInterface(26840);
	}

	public static void openMarket(Client c) {
		if (!marketOpen) {
			c.sendMessage("The Auction House has been disabled for now.");
			return;
		}

		c.getPA().closeAllWindows();
		// c.getPA().closeAllWindows();
		if (c.inTrade) {
			return;
		}
		c.marketItemId = -1;
		c.marketItemAmount = 1;
		c.marketItemValue = 1;
		updateMarketText(c);
		c.getPA().showInterface(26780);
	}

	public static void removeOffer(Client c, int slotId) {
		if (c.myMarketItemAmount < 1) {
			c.sendMessage("You don't have any more offers in the market.");
			return;
		}
		if (c.myMarketItems[slotId] < 0)
			return;

		if (auctionOwner[c.myMarketItems[slotId]] != null)
			if (auctionOwner[c.myMarketItems[slotId]]
					.equalsIgnoreCase(c.playerName)) {
				c.sendMessage("You remove your offer for "
						+ getItemName(auctionItems[c.myMarketItems[slotId]]));
				if (c.getItems().freeSlots() >= auctionAmount[c.myMarketItems[slotId]]) {
					c.getItems().addItem(auctionItems[c.myMarketItems[slotId]],
							auctionAmount[c.myMarketItems[slotId]]);
				} else if (c.getItems().freeSlots() > 0
						&& c.getItems().isStackable(
								auctionItems[c.myMarketItems[slotId]] + 1)
						&& getItemName(auctionItems[c.myMarketItems[slotId]])
								.equalsIgnoreCase(
										getItemName(auctionItems[c.myMarketItems[slotId]] + 1))) {
					c.getItems().addItem(
							auctionItems[c.myMarketItems[slotId]] + 1,
							auctionAmount[c.myMarketItems[slotId]]);
				} else {
					c.getItems().addItemToBank(
							auctionItems[c.myMarketItems[slotId]],
							auctionAmount[c.myMarketItems[slotId]]);
					c.sendMessage("The item(s) was added safely to your bank account.");
				}
				c.marketAmount--;
				rearrangeArray(0, c.myMarketItems[slotId]);
				openEntries(c);
				return;
			}

		if (offerOwner[c.myMarketItems[slotId]] != null)
			if (offerOwner[c.myMarketItems[slotId]]
					.equalsIgnoreCase(c.playerName)) {
				c.sendMessage("You remove your offer for "
						+ getItemName(offerItems[c.myMarketItems[slotId]]));
				if (c.getItems().freeSlots() > 0
						|| c.getItems().playerHasItem(995)) {
					c.getItems().addItem(
							995,
							offerAmount[c.myMarketItems[slotId]]
									* offerPrice[c.myMarketItems[slotId]]);
				} else {
					c.getItems().addItemToBank(
							995,
							offerAmount[c.myMarketItems[slotId]]
									* offerPrice[c.myMarketItems[slotId]]);
					c.sendMessage("The coins was added safely to your bank account.");
				}
				c.marketAmount--;
				rearrangeArray(1, c.myMarketItems[slotId]);
				openEntries(c);
				return;
			}
	}

	private static void setEntryFeed(Client c) {
		int lastSaved = 0;
		boolean found = false;
		int usedSlots = 0;

		c.myMarketItemAmount = 0;
		for (int i = 0; i < 6; i++) {
			c.getPA().sendFrame34a(27850, -1, i, 1);
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(26840);
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
			c.myMarketItems[i] = 0;
			toPrint = " ";
			entryFeed(c, toPrint, i, 1);
			entryFeed(c, toPrint, i, 2);
		}
		for (int j = 0; j < itemsInMarket; j++) {
			if (auctionOwner[j].equalsIgnoreCase(c.playerName)) {
				c.myMarketItems[c.myMarketItemAmount] = j;
				c.myMarketItemAmount++;
				// for (int i = usedSlots; i < 6; i++) {
				int i = usedSlots;
				found = true;
				toPrint = "Sell "
						+ Misc.capitalize(getItemName(auctionItems[j]));
				toPrint1 = auctionPrice[j] + " ea ("
						+ coolFormat(auctionPrice[j] * auctionAmount[j], 0)
						+ ")";
				// if(!lastEntry.equalsIgnoreCase(toPrint)){
				// lastEntry = toPrint;
				usedSlots++;
				entryFeed(c, toPrint, i, 1);
				entryFeed(c, toPrint1, i, 2);
				lastSaved = i;
				c.getPA().sendFrame34a(27850, auctionItems[j], i,
						auctionAmount[j]);
				c.getOutStream().createFrame(248);
				c.getOutStream().writeWordA(26840);
				c.getOutStream().writeWord(5063);
				c.flushOutStream();
				i++;
				// }
				toPrint = "None";
			}
		}

		for (int j = 0; j < offersInMarket; j++) {
			if (offerOwner[j].equalsIgnoreCase(c.playerName)) {
				c.myMarketItems[c.myMarketItemAmount] = j;
				c.myMarketItemAmount++;
				// for (int i = usedSlots; i < 6; i++) {
				int i = usedSlots;
				found = true;
				// toPrint =
				// "Buying "+offerAmount[j]+" "+getItemName(offerItems[j])+" for "+convertCurrency(offerPrice[j])+" each, for a total of "+convertCurrency(offerAmount[j]*offerPrice[j]);
				toPrint = "Buy " + Misc.capitalize(getItemName(offerItems[j]));
				toPrint1 = offerPrice[j] + " ea ("
						+ coolFormat(offerPrice[j] * offerAmount[j], 0) + ")";
				// if(!lastEntry.equalsIgnoreCase(toPrint)){
				// lastEntry = toPrint;
				usedSlots++;
				entryFeed(c, toPrint, i, 1);
				entryFeed(c, toPrint1, i, 2);
				lastSaved = i;
				c.getPA().sendFrame34a(27850, offerItems[j], i, offerAmount[j]);
				c.getOutStream().createFrame(248);
				c.getOutStream().writeWordA(26840);
				c.getOutStream().writeWord(5063);
				// c.getPA().sendFrame87(286, 0);
				c.flushOutStream();
				i++;
				// }
				toPrint = "None";
			}
		}
		c.marketAmount = c.myMarketItemAmount;
		if (!found) {
			for (int i = lastSaved; i < 6; i++) {
				entryFeed(c, "None", i, 1);
				entryFeed(c, " ", i, 2);
			}
		}
	}

	private static void entryFeed(Client c, String text, int id, int type) {
		if (id >= 7)
			return;
		if (type == 1) {
			if (text == null) {
				c.getPA().sendNewString(" ", (26804 + id));
				// return;
			} else {
				c.getPA().sendNewString(text + "", (26804 + id));
			}
		}
		if (type == 2) {
			if (text == null) {
				c.getPA().sendNewString(" ", (26851 + id));
				// return;
			} else {
				c.getPA().sendNewString(text + "", (26851 + id));
			}
		}
	}

	private static void setMarketFeed(Client c, int itemId) {
		int lastSaved = 0;
		boolean found = false;
		// String lastOwner = "none";
		int usedSlots = 0;
		String lastEntry = "";
		for (int i = 0; i < 16; i++) {
			toPrint = " ";
			marketFeed(c, toPrint, i);
		}
		for (int j = 0; j < itemsInMarket; j++) {
			if (auctionItems[j] == itemId) {
				for (int i = usedSlots; i < 16; i++) {
					// if(lastPrice != auctionPrice[j]){
					found = true;
					// lastPrice = auctionPrice[j];
					// lastOwner = auctionOwner[j];
					toPrint = "[S] " + auctionAmount[j] + " "
							+ getItemName(itemId) + " at "
							+ convertCurrency(auctionPrice[j]) + " ea";
					if (!lastEntry.equalsIgnoreCase(toPrint)) {
						lastEntry = toPrint;
						usedSlots++;
						marketFeed(c, toPrint, i);
						lastSaved = i;
					}
					toPrint = " ";
				}
			}
		}
		for (int j = 0; j < offersInMarket; j++) {
			if (offerItems[j] == itemId) {
				for (int i = usedSlots; i < 16; i++) {
					// if(lastPrice != auctionPrice[j]){
					found = true;
					// lastPrice = auctionPrice[j];
					// lastOwner = auctionOwner[j];
					toPrint = "[B] " + offerAmount[j] + " "
							+ getItemName(itemId) + " at "
							+ convertCurrency(offerPrice[j]) + " ea";
					if (!lastEntry.equalsIgnoreCase(toPrint)) {
						lastEntry = toPrint;
						usedSlots++;
						marketFeed(c, toPrint, i);
						lastSaved = i;
					}
					toPrint = " ";
				}
			}
		}
		if (!found) {
			for (int i = lastSaved; i < 16; i++) {
				toPrint = " ";
				marketFeed(c, toPrint, i);
			}
			if (itemId < 0)
				marketFeed(c, "Select an item", 0);
			else
				marketFeed(c, "No listed sales for this item", 0);
		}
		
		 * for (int j = 0; j < offersInMarket; j++) { if(offerItems[j] ==
		 * itemId){ found = true; marketFeed[i] =
		 * "[B] "+auctionAmount[j]+" "+getItemName
		 * (itemId)+" at "+auctionPrice[j]+" ea"; break; } else if(offerItems[j]
		 * == itemId+1 && c.getItems().isStackable(itemId+1) &&
		 * getItemName(itemId).equalsIgnoreCase(getItemName(itemId+1))){ found =
		 * true; marketFeed[i] =
		 * "[B] "+auctionAmount[j]+" "+getItemName(itemId)+
		 * " at "+auctionPrice[j]+" ea"; break; } }
		 

		// c.sendMessage("");
	}

	private static void marketFeed(Client c, String text, int id) {
		if (id >= 16)
			return;
		if (text == null) {
			c.getPA().sendNewString(" ", (26703 + id));
			// return;
		} else {
			c.getPA().sendNewString(text + "", (26703 + id));
		}
	}

	public static void updateMarketText(Client c) {
		int itemId = c.marketItemId;
		int itemAmount = c.marketItemAmount;
		int itemValue = c.marketItemValue;
		c.getPA().sendNewString(
				itemId < 0 ? "None" : Misc.capitalize(getItemName(itemId)),
				26733);
		c.getPA().sendNewString(itemId < 0 ? "1" : "" + itemAmount, 26734);
		c.getPA().sendNewString(
				itemId < 0 ? "1" : "" + convertCurrency(itemValue), 26735);
		updateInfoText(c, itemId);
		AuctionHouse.setMarketFeed(c, itemId);
	}

	private static void updateInfoText(Client c, int itemId) {
		// int itemId = c.marketItemId;
		int shopValue = c.getShops().getItemShopValue(itemId);
		c.getPA().sendNewString(
				itemId < 0 ? "Shop value: " : "Shop value: "
						+ convertCurrency(shopValue) + ".", 26737);
		c.getPA()
				.sendNewString(
						itemId < 0 ? "Low Alchemy value: "
								: "Low Alchemy value: "
										+ convertCurrency((int) (shopValue * .50))
										+ ".", 26738);
		c.getPA()
				.sendNewString(
						itemId < 0 ? "High Alchemy value: "
								: "High Alchemy value: "
										+ convertCurrency((int) (shopValue * .75))
										+ ".", 26739);
		c.getPA().sendNewString(
				itemId < 0 ? "Item type(beta): " : "Item type(beta): "
						+ getItemType(c, itemId) + ".", 26740); // TODO fix to
																// equipment,
																// consumable,
																// misc
		c.getPA().sendNewString(
				itemId < 0 ? "Tradeable: " : "Tradeable: "
						+ c.getItems().tradeable(itemId) + ".", 26741);
		c.getPA().sendNewString(
				itemId < 0 ? "Stackable: " : "Stackable: "
						+ c.getItems().isStackable(itemId) + ".", 26742);
	}

	public static void buyItem(Client c, int itemId, int itemAmount,
			int itemPrice) {

		itemId = getOtherId(itemId);
		if (itemId < 1)
			return;

		if (itemAmount < 1) {
			c.sendMessage("You need to purchase at-least 1 item.");
			return;
		}

		if (c.marketAmount == allowedEntries(c)) {
			c.sendMessage("You already have the max amount of offers in the market.");
			return;
		}

		if (c.inTrade) {
			return;
		}
		if (itemPrice < 1)
			itemPrice = 1;

		if (itemAmount > 1000) {
			c.sendMessage("You cannot purchase more than 1000 items at a time on the market.");
			return;
		}

		if (!canSell(itemId) || !c.getItems().tradeable(itemId)) {
			c.sendMessage("You can't purchase the item "
					+ Misc.capitalize(getItemName(itemId)) + ".");
			return;
		}

		if (!c.getItems().playerHasItem(995, itemAmount * itemPrice)) {
			c.sendMessage("You don't have enough coins to purchase those items.");
			return;
		}

		c.marketItemId = -1;
		c.marketItemAmount = 1;
		c.marketItemValue = 1;
		int newAmount = itemAmount;
		int j = 0;
		while (j < itemsInMarket) {
			if (auctionItems[j] == itemId) {
				if (auctionPrice[j] <= itemPrice) {
					if (auctionAmount[j] < newAmount) {
						if (c.getItems().playerHasItem(995,
								auctionAmount[j] * auctionPrice[j])) {
							c.getItems().deleteItem2(995,
									auctionAmount[j] * auctionPrice[j]);
							c.sendMessage("You purchase "
									+ auctionAmount[j]
									+ " "
									+ Misc.capitalize(getItemName(auctionItems[j]))
									+ "(s) for "
									+ convertChatCurrency(auctionPrice[j])
									+ " each. Sold by "
									+ Misc.capitalize(auctionOwner[j]));
							handleUnclaimedGold(auctionOwner[j],
									getItemName(auctionItems[j]),
									auctionAmount[j] * auctionPrice[j]);
							AuctionLogger.main(c, 3, itemId, auctionAmount[j],
									auctionPrice[j], auctionOwner[j]);
							if (c.getItems().freeSlots() >= auctionAmount[j]) {
								c.getItems().addItem(auctionItems[j],
										auctionAmount[j]);
							} else if (c.getItems().freeSlots() > 0
									&& c.getItems().isStackable(
											auctionItems[j] + 1)
									&& getItemName(auctionItems[j])
											.equalsIgnoreCase(
													getItemName(auctionItems[j] + 1))) {
								c.getItems().addItem(auctionItems[j] + 1,
										auctionAmount[j]);
							} else {
								c.getItems().addItemToBank(auctionItems[j],
										auctionAmount[j]);
								c.sendMessage("The item(s) you purchased was added safely to your bank account.");
							}
							updateMarketText(c);
							newAmount -= auctionAmount[j];
							rearrangeArray(0, j);
							j = 0;
							continue;
						}
					}
					if (auctionAmount[j] >= newAmount) {
						if (c.getItems().playerHasItem(995,
								newAmount * auctionPrice[j])) {
							c.getItems().deleteItem2(995,
									newAmount * auctionPrice[j]);
							c.sendMessage("You purchase "
									+ newAmount
									+ " "
									+ Misc.capitalize(getItemName(auctionItems[j]))
									+ "(s) for "
									+ convertChatCurrency(auctionPrice[j])
									+ " each. Sold by "
									+ Misc.capitalize(auctionOwner[j]));
							auctionAmount[j] -= newAmount;
							handleUnclaimedGold(auctionOwner[j],
									getItemName(auctionItems[j]), newAmount
											* auctionPrice[j]);
							AuctionLogger.main(c, 3, itemId, auctionAmount[j],
									auctionPrice[j], auctionOwner[j]);
							if (c.getItems().freeSlots() >= newAmount) {
								c.getItems()
										.addItem(auctionItems[j], newAmount);
							} else if (c.getItems().freeSlots() > 0
									&& c.getItems().isStackable(
											auctionItems[j] + 1)
									&& getItemName(auctionItems[j])
											.equalsIgnoreCase(
													getItemName(auctionItems[j] + 1))) {
								c.getItems().addItem(auctionItems[j] + 1,
										newAmount);
							} else {
								c.getItems().addItemToBank(auctionItems[j],
										newAmount);
								c.sendMessage("The item(s) you purchased was added safely to your bank account.");
							}
							updateMarketText(c);
							newAmount = 0;
							if (auctionAmount[j] < 1) {
								rearrangeArray(0, j);
								return;
							}
						}
					}
				}
			}
			j++;
		}
		if (newAmount < 1) {
			AuctionHouse.saveArrays(0);
			return;
		}

		if (offersInMarket >= maxMarket) {
			c.sendMessage("The market is full at the moment. Please wait a few minutes and then try again.");
			return;
		}
		if (newAmount * itemPrice < 0)
			return;
		if (c.getItems().playerHasItem(995, newAmount * itemPrice)) {
			c.getItems().deleteItem2(995, newAmount * itemPrice);
		} else {
			return;
		}

		offerItems[offersInMarket] = itemId;
		offerAmount[offersInMarket] = newAmount;
		offerPrice[offersInMarket] = itemPrice;
		offerOwner[offersInMarket] = c.playerName;
		c.sendMessage("You put up an offer to buy " + newAmount
				+ " of the item "
				+ Misc.capitalize(getItemName(offerItems[offersInMarket]))
				+ " for " + convertChatCurrency(offerPrice[offersInMarket])
				+ " each.");
		offersInMarket++;
		c.marketAmount++;
		updateMarketText(c);
		AuctionLogger.main(c, 3, itemId, auctionAmount[j], auctionPrice[j],
				auctionOwner[j]);
		AuctionHouse.saveArrays(1);
	}

	public static void sellItem(Client c, int itemId, int itemAmount,
			int itemPrice) {

		itemId = getOtherId(itemId);
		if (itemId < 1)
			return;

		if (itemAmount < 1) {
			c.sendMessage("You need to sell at-least 1 item.");
			return;
		}

		if (c.marketAmount == allowedEntries(c)) {
			c.sendMessage("You already have the max amount of offers in the market.");
			return;
		}

		if (c.inTrade) {
			return;
		}

		if (itemPrice < 1)
			itemPrice = 1;

		if (itemAmount > 1000) {
			c.sendMessage("You cannot sell more than 1000 items at a time on the market.");
			return;
		}

		if (!canSell(itemId) || !c.getItems().tradeable(itemId)) {
			c.sendMessage("You can't sell the item "
					+ Misc.capitalize(getItemName(itemId)) + ".");
			return;
		}

		c.marketItemId = -1;
		c.marketItemAmount = 1;
		c.marketItemValue = 1;

		int newAmount = itemAmount;

		int j = 0;
		while (j < offersInMarket) {
			if (offerItems[j] == itemId) {
				if (offerPrice[j] >= itemPrice) {
					if (offerAmount[j] < newAmount) {
						if (c.getItems().playerHasItem(itemId, offerAmount[j])) {
							c.getItems().deleteItem2(itemId, offerAmount[j]);
							c.sendMessage("You sell "
									+ offerAmount[j]
									+ " "
									+ Misc.capitalize(getItemName(offerItems[j]))
									+ "(s) for "
									+ convertChatCurrency(offerPrice[j])
									+ " each. Sold to "
									+ Misc.capitalize(offerOwner[j]));
							handleUnclaimedItem(offerOwner[j], offerItems[j],
									offerAmount[j]);
							if (c.getItems().freeSlots() > 0
									|| c.getItems().playerHasItem(995)) {
								c.getItems().addItem(995,
										offerAmount[j] * offerPrice[j]);
							} else {
								c.getItems().addItemToBank(995,
										offerAmount[j] * offerPrice[j]);
								c.sendMessage("The money from this sale was added to your bank.");
							}
							updateMarketText(c);
							newAmount -= offerAmount[j];
							rearrangeArray(1, j);
							j = 0;
							continue;
						} else if (c.getItems().playerHasItem(itemId + 1,
								offerAmount[j])) {
							c.getItems()
									.deleteItem2(itemId + 1, offerAmount[j]);
							c.sendMessage("You sell "
									+ offerAmount[j]
									+ " "
									+ Misc.capitalize(getItemName(offerItems[j]))
									+ "(s) for "
									+ convertChatCurrency(offerPrice[j])
									+ " each. Sold to "
									+ Misc.capitalize(offerOwner[j]));
							handleUnclaimedItem(offerOwner[j], offerItems[j],
									offerAmount[j]);
							if (c.getItems().freeSlots() > 0
									|| c.getItems().playerHasItem(995)) {
								c.getItems().addItem(995,
										offerAmount[j] * offerPrice[j]);
							} else {
								c.getItems().addItemToBank(995,
										offerAmount[j] * offerPrice[j]);
								c.sendMessage("The money from this sale was added to your bank.");
							}
							updateMarketText(c);
							newAmount -= offerAmount[j];
							rearrangeArray(1, j);
							j = 0;
							continue;
						}
					}
					if (offerAmount[j] >= newAmount) {
						if (c.getItems().playerHasItem(itemId, newAmount)) {
							c.getItems().deleteItem2(itemId, newAmount);
							c.sendMessage("You sell "
									+ newAmount
									+ " "
									+ Misc.capitalize(getItemName(offerItems[j]))
									+ "(s) for "
									+ convertChatCurrency(offerPrice[j])
									+ " each. Sold to "
									+ Misc.capitalize(offerOwner[j]));
							handleUnclaimedItem(offerOwner[j], offerItems[j],
									newAmount);
							if (c.getItems().freeSlots() > 0
									|| c.getItems().playerHasItem(995)) {
								c.getItems().addItem(995,
										offerAmount[j] * offerPrice[j]);
							} else {
								c.getItems().addItemToBank(995,
										offerAmount[j] * offerPrice[j]);
								c.sendMessage("The money from this sale was added to your bank.");
							}
							offerAmount[j] -= newAmount;
							updateMarketText(c);
							newAmount = 0;
							if (offerAmount[j] < 1) {
								rearrangeArray(1, j);
								return;
							}
						} else if (c.getItems().playerHasItem(itemId + 1,
								newAmount)) {
							c.getItems().deleteItem2(itemId + 1, newAmount);
							c.sendMessage("You sell "
									+ newAmount
									+ " "
									+ Misc.capitalize(getItemName(offerItems[j]))
									+ "(s) for "
									+ convertChatCurrency(offerPrice[j])
									+ " each. Sold to "
									+ Misc.capitalize(offerOwner[j]));
							offerAmount[j] -= newAmount;
							handleUnclaimedItem(offerOwner[j], offerItems[j],
									newAmount);
							if (c.getItems().freeSlots() > 0
									|| c.getItems().playerHasItem(995)) {
								c.getItems().addItem(995,
										offerAmount[j] * offerPrice[j]);
							} else {
								c.getItems().addItemToBank(995,
										offerAmount[j] * offerPrice[j]);
								c.sendMessage("The money from this sale was added to your bank.");
							}
							updateMarketText(c);
							newAmount = 0;
							if (offerAmount[j] < 1) {
								rearrangeArray(1, j);
								return;
							}
						}
					}
				}
			}
			j++;
		}
		if (newAmount < 1) {
			AuctionHouse.saveArrays(1);
			return;
		}

		if (itemsInMarket >= maxMarket) {
			c.sendMessage("The market is full at the moment. Please wait a few minutes and then try again.");
			return;
		}

		if (c.getItems().playerHasItem(itemId, newAmount)) {
			c.getItems().deleteItem2(itemId, newAmount);
		} else if (c.getItems().playerHasItem(itemId + 1, newAmount)
				&& c.getItems().isStackable(itemId + 1)
				&& getItemName(itemId)
						.equalsIgnoreCase(getItemName(itemId + 1))) {
			c.getItems().deleteItem2(itemId + 1, newAmount);
		} else {
			c.sendMessage("You dont have " + newAmount + " of that item.");
			return;
		}

		auctionItems[itemsInMarket] = itemId;
		auctionAmount[itemsInMarket] = newAmount;
		auctionPrice[itemsInMarket] = itemPrice;
		auctionOwner[itemsInMarket] = c.playerName;
		c.sendMessage("You add " + newAmount + " "
				+ Misc.capitalize(getItemName(auctionItems[itemsInMarket]))
				+ "(s) for " + convertChatCurrency(auctionPrice[itemsInMarket])
				+ " each to the market.");
		itemsInMarket++;
		c.marketAmount++;
		updateMarketText(c);
		AuctionHouse.saveArrays(0);
	}

	public static void collectGold(Client c) {
		if (c.marketTimer + 1500 > System.currentTimeMillis())
			return;
		if (c.inTrade) {
			return;
		}
		boolean found = false;
		for (int j = 0; j < unclaimedGoldCount; j++) {
			if (unclaimedOwner[j].equalsIgnoreCase(c.playerName)) {
				found = true;
				if (c.getItems().freeSlots() < 1
						&& !c.getItems().playerHasItem(995)) {
					if (c.getItems().freeBankSlots() > 0) {
						c.getItems().addItemToBank(995, unclaimedGold[j]);
					} else {
						c.sendMessage("Drop something or make room in your bank to collect.");
						return;
					}
				} else {
					c.getItems().addItem(995, unclaimedGold[j]);
				}
				c.sendMessage("You withdraw " + unclaimedGold[j]
						+ "gp for the sold "
						+ Misc.capitalize(unclaimedItemName[j]) + "(s).");
				rearrangeArray(2, j);
				j = 0;
			}
		}
		c.marketTimer = System.currentTimeMillis();
		if (!found) {
			c.sendMessage("Unfortunately there are no gold for you right now.");
		}
	}

	public static void collectItem(Client c) {
		if (c.marketTimer + 1500 > System.currentTimeMillis())
			return;
		if (c.inTrade) {
			return;
		}
		boolean found = false;
		for (int j = 0; j < boughtItemsCount; j++) {
			if (boughtOwner[j].equalsIgnoreCase(c.playerName)) {
				found = true;

				if (c.getItems().freeSlots() < boughtAmount[j]) {
					if (c.getItems().freeSlots() > 0
							&& c.getItems().isStackable(boughtItems[j] + 1)
							&& getItemName(boughtItems[j] + 1)
									.equalsIgnoreCase(
											getItemName(boughtItems[j]))) {
						c.getItems().addItem(boughtItems[j] + 1,
								boughtAmount[j]);
					} else if (c.getItems().freeBankSlots() > 0) {
						c.getItems().addItemToBank(boughtItems[j],
								boughtAmount[j]);
						c.sendMessage("Item(s) collected added to bank.");
					} else {
						c.sendMessage("Drop something or make room in your bank to collect.");
						return;
					}
				} else {
					c.getItems().addItem(boughtItems[j], boughtAmount[j]);
				}

				c.sendMessage("You withdraw " + boughtAmount[j] + " "
						+ Misc.capitalize(getItemName(boughtItems[j])) + "(s).");
				rearrangeArray(3, j);
				j = 0;
			}
		}
		c.marketTimer = System.currentTimeMillis();
		if (!found) {
			c.sendMessage("Unfortunately there are no items to collect right now.");
		}
	}

	private static boolean isUsed = false;

	*//***
	 * 
	 * @param arrayType
	 *            0 = auctionItems, 1 = offerItems, 2 = unclaimedItems, 3 =
	 *            boughtItems
	 *//*
	private static void rearrangeArrayQueue(int arrayType, int id) {
		long a = System.currentTimeMillis();
		boolean saved = false;
		if (isUsed) {
			int j = 0;
			while (j < 100) {
				if (!isUsed) {
					saved = true;
					rearrangeArray(arrayType, id);
					break;
				}
				j++;
			}
			if (!saved) {
				// ERROR
			}
			long took = System.currentTimeMillis() - a;
			System.out.println("Market: queue " + took + "ms");
		}
	}

	*//***
	 * 
	 * @param arrayType
	 *            0 = auctionItems, 1 = offerItems, 2 = unclaimedItems, 3 =
	 *            boughtItems
	 *//*
	private static void rearrangeArray(int type, int id) {
		if (isUsed) {
			rearrangeArrayQueue(type, id);
			return;
		}
		if (type == 0) {
			isUsed = true;
			for (int j2 = id; j2 < itemsInMarket; j2++) {
				if (auctionOwner[j2 + 1] == null) {
					auctionItems[j2] = -1;
					auctionAmount[j2] = -1;
					auctionPrice[j2] = -1;
					auctionOwner[j2] = null;
					itemsInMarket--;
					AuctionHouse.saveArrays(0);
					isUsed = false;
					return;
				} else {
					auctionItems[j2] = auctionItems[j2 + 1];
					auctionAmount[j2] = auctionAmount[j2 + 1];
					auctionPrice[j2] = auctionPrice[j2 + 1];
					auctionOwner[j2] = auctionOwner[j2 + 1];
				}
			}
		}
		if (type == 1) {
			isUsed = true;
			for (int j2 = id; j2 < offersInMarket; j2++) {
				if (offerAmount[j2 + 1] <= 0) {
					offerItems[j2] = -1;
					offerAmount[j2] = -1;
					offerPrice[j2] = -1;
					offerOwner[j2] = null;
					offersInMarket--;
					AuctionHouse.saveArrays(1);
					isUsed = false;
					return;
				} else {
					offerItems[j2] = offerItems[j2 + 1];
					offerAmount[j2] = offerAmount[j2 + 1];
					offerPrice[j2] = offerPrice[j2 + 1];
					offerOwner[j2] = offerOwner[j2 + 1];
				}
			}
		}
		if (type == 2) {
			isUsed = true;
			for (int j = id; j < unclaimedGoldCount; j++) {
				if (unclaimedOwner[j + 1] == null) {
					unclaimedOwner[j] = null;
					unclaimedGold[j] = -1;
					unclaimedGoldCount--;
					AuctionHouse.saveArrays(2);
					isUsed = false;
					return;
				} else {
					unclaimedOwner[j] = unclaimedOwner[j + 1];
					unclaimedGold[j] = unclaimedGold[j + 1];
				}
			}
		}
		if (type == 3) {
			isUsed = true;
			for (int j = id; j < boughtItemsCount; j++) {
				if (boughtOwner[j + 1] == null) {
					boughtItems[j] = -1;
					boughtAmount[j] = -1;
					boughtOwner[j] = null;
					boughtItemsCount--;
					AuctionHouse.saveArrays(3);
					isUsed = false;
					return;
				} else {
					boughtItems[j] = boughtItems[j + 1];
					boughtAmount[j] = boughtAmount[j + 1];
					boughtOwner[j] = boughtOwner[j + 1];
				}
			}
		}
	}

	private static void handleUnclaimedGold(String playerName, String soldItem,
			int goldAmount) {
		unclaimedOwner[unclaimedGoldCount] = playerName;
		unclaimedItemName[unclaimedGoldCount] = soldItem;
		unclaimedGold[unclaimedGoldCount] = goldAmount;
		unclaimedGoldCount++;
		System.out.println("Market: (" + unclaimedGoldCount
				+ ") unclaimed piles of gold.");
		AuctionHouse.saveArrays(2);
		return;
	}

	private static void handleUnclaimedItem(String playerName, int itemId,
			int itemAmount) {
		if (boughtItemsCount < 0)
			boughtItemsCount = 0;
		boughtOwner[boughtItemsCount] = playerName.toLowerCase();
		boughtItems[boughtItemsCount] = itemId;
		boughtAmount[boughtItemsCount] = itemAmount;
		System.out.println("Market slot: (" + boughtItemsCount + ") unclaimed");
		boughtItemsCount++;
		AuctionHouse.saveArrays(3);
		return;
	}

	public static void loadAuctionHouse() {
		// that is also how you manually reset the auction items. kk
		long a = System.currentTimeMillis();
		auctionItems = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionItems");
		auctionAmount = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionAmount");
		auctionPrice = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionPrice");
		auctionOwner = loadStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionOwner");
		itemsInMarket = loadItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/itemsInMarket");
		System.out.println("Market: (" + itemsInMarket
				+ ") item(s) listed for sale.");

		offerItems = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerItems");
		offerAmount = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerAmount");
		offerPrice = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerPrice");
		offerOwner = loadStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerOwner");
		offersInMarket = loadItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/offersInMarket");
		System.out.println("Market: (" + offersInMarket
				+ ") item(s) listed for purchase.");

		unclaimedOwner = loadStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedOwner");
		unclaimedItemName = loadStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedItemName");
		unclaimedGold = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedGold");
		unclaimedGoldCount = loadItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedGoldCount");
		System.out.println("Market: (" + unclaimedGoldCount
				+ ") unclaimed sales.");

		boughtOwner = loadStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtOwner");
		boughtItems = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtItems");
		boughtAmount = loadIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtAmount");
		boughtItemsCount = loadItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/boughtItemsCount");
		System.out.println("Market: (" + boughtItemsCount
				+ ") unclaimed items.");
		long took = System.currentTimeMillis() - a;
		System.out.println("Market: loaded in " + took + "ms");
	}

	*//***
	 * 
	 * @param type
	 *            0 = auctionItems, 1 = offerItems, 2 = unclaimedItems, 3 =
	 *            boughtItems
	 *//*
	public static void saveArrays(int type) {
		if (type == 0) {
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionItems", auctionItems);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionAmount", auctionAmount);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionPrice", auctionPrice);
			saveStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/auctionOwner", auctionOwner);
			saveItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/itemsInMarket", itemsInMarket);
		}

		if (type == 1) {
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerItems", offerItems);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerAmount", offerAmount);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerPrice", offerPrice);
			saveStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/offerOwner", offerOwner);
			saveItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/offersInMarket", offersInMarket);
		}

		if (type == 2) {
			saveStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedOwner",
					unclaimedOwner);
			saveStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedItemName",
					unclaimedItemName);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedGold", unclaimedGold);
			saveItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/unclaimedGoldCount",
					unclaimedGoldCount);

		}

		if (type == 3) {
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtItems", boughtItems);
			saveIntArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtAmount", boughtAmount);
			saveStringArray(Config.LOAD_DIRECTORY + "AuctionHouse/boughtOwner", boughtOwner);
			saveItemCount(Config.LOAD_DIRECTORY + "AuctionHouse/boughtItemsCount",
					boughtItemsCount);
		}
	}

	public static void saveItemCount(String filename, int integer) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(integer);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void saveIntArray(String filename, int[] arrayType) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(arrayType);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void saveStringArray(String filename, String[] arrayType) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(arrayType);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static int loadItemCount(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			int savedArray = (int) in.readObject();
			in.close();
			return savedArray;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public static int[] loadIntArray(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			int[] savedArray = (int[]) in.readObject();
			in.close();
			return savedArray;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static String[] loadStringArray(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			String[] savedArray = (String[]) in.readObject();
			in.close();
			return savedArray;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	private static String getItemName(int itemId) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == itemId) {
					return Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		return "Unknown";
	}

	private static String convertCurrency(int amount) {
		if (amount >= 1000 && amount < 1000000) {
			return (int) amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 1000000000) {
			return (int) amount / 1000000 + "M";
		}

		return amount + "";
	}

	private static char[] c = new char[] { 'k', 'm', 'b', 't' };

	private static String coolFormat(double n, int iteration) {
		double d = ((long) n / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;// true if the decimal part is
												// equal to 0 (then it's trimmed
												// anyway)
		String return1 = (d < 1000 ? // this determines the class, i.e. 'k', 'm'
										// etc
		((d > 99.9 || isRound || (!isRound && d > 9.99) ? // this decides
															// whether to trim
															// the decimals
		(int) d * 10 / 10
				: d + "" // (int) d * 10 / 10 drops the decimal
		) + "" + c[iteration])
				: coolFormat(d, iteration + 1));
		if (return1.startsWith("0")) {
			return (int) n + "gp";
		}
		return return1;
	}

	private static String convertChatCurrency(int amount) {
		String ShopAdd = "";
		if (amount >= 1000 && amount < 1000000) {
			ShopAdd = " (" + (amount / 1000) + "K)";
		} else if (amount >= 1000000) {
			ShopAdd = " (" + (amount / 1000000) + " million)";
		} else {
			ShopAdd = amount + "";
		}
		return ShopAdd;
	}

	private static String getItemType(Client c, int itemId) {
		if (c.getItems().itemType(itemId).equalsIgnoreCase("weapon")) {
			if (itemId >= 315 && itemId <= 400) {
				return "Consumable";
			}
			if (itemId >= 526 && itemId <= 537) {
				return "Bone";
			}
			if (itemId >= 554 && itemId <= 566) {
				return "Rune";
			}
			if (itemId >= 1511 && itemId <= 1522) {
				return "Log";
			}
			if (itemId >= 1601 && itemId <= 1634) {
				return "Gem";
			}
			if (itemId >= 1763 && itemId <= 1774) {
				return "Cosmetic";
			}
			if (itemId >= 434 && itemId <= 454) {
				return "Ore";
			}
			if (itemId >= 2349 && itemId <= 2364) {
				return "Refined ore";
			}
			if (itemId >= 2428 && itemId <= 2459 || itemId >= 3008
					&& itemId <= 3047) {
				return "Potion";
			}
		}
		if (itemId >= 1037 && itemId <= 1058 || itemId >= 962 && itemId <= 963
				|| itemId >= 981 && itemId <= 982 || itemId >= 4558
				&& itemId <= 4566 || itemId >= 1959 && itemId <= 1962
				|| itemId == 10476) {
			return "Rare";
		}

		return Misc.capitalize(c.getItems().itemType(itemId));
	}

	public static boolean canSell(int itemId) {
		// if(!c.getItems().tradeable(itemId)) return false;
		for (int j = 0; j < PROHIBITED_ITEMS.length; j++) {
			if (itemId == PROHIBITED_ITEMS[j])
				return false;
		}
		return true;
	}

	public static void welcomeMessage(Client c) {

		int amount = 0;
		boolean found = false;
		for (int j = 0; j < unclaimedGoldCount; j++) {
			if (unclaimedOwner[j].equalsIgnoreCase(c.playerName)) {
				found = true;
				amount++;
			}
		}
		if (found)
			c.sendMessage("<col=800000>You have " + amount + " unclaimed pile"
					+ (amount == 1 ? "" : "s") + " of gold.");

		boolean found1 = false;

		amount = 0;
		for (int j = 0; j < boughtItemsCount; j++) {
			if (boughtOwner[j].equalsIgnoreCase(c.playerName)) {
				found1 = true;
				amount++;
			}
		}
		if (found1)
			c.sendMessage("<col=800000>You have " + amount + " unclaimed pile"
					+ (amount == 1 ? "" : "s") + " of items.");

		if (found || found1)
			c.sendMessage("<img=6> You can collect at closest Auction House.");

		if (!found || !found1) {
			// no updates since last login.
		}
	}*/
}