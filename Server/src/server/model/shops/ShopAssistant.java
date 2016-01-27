package server.model.shops;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PlayerSave;
import server.world.ShopHandler;

public class ShopAssistant {

	private Client c;

	public ShopAssistant(Client client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendNewString(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true
						&& PlayerHandler.players[i].myShopId == c.myShopId
						&& i != c.playerId) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		//synchronized (c) {
			int TotalItems = 0;
			for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0) {
					TotalItems++;
				}
			}
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0
						|| i <= ShopHandler.ShopItemsStandard[ShopID]) {
					if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(
								ShopHandler.ShopItemsN[ShopID][i]);
					} else {
						c.getOutStream().writeByte(
								ShopHandler.ShopItemsN[ShopID][i]);
					}
					if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT
							|| ShopHandler.ShopItems[ShopID][i] < 0) {
						ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
					}
					c.getOutStream().writeWordBigEndianA(
							ShopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		//}
	}

	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double TotPrice = 0;
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					ShopValue = Server.itemHandler.ItemList[i].ShopValue;
				}
			}
		}

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	public int getItemShopValue(int itemId) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == itemId) {
					return (int) Server.itemHandler.ItemList[i].ShopValue;
				}
			}
		}
		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0,
				removeSlot));
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (c.myShopId == 8) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " pk points.");
			return;
		}
		if (c.myShopId == 16) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " hunter points.");
			return;
		}
		if (c.myShopId == 49) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " Duo Slayer points.");
			return;
		}
		if (c.myShopId == 10) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " slayer points.");
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecItemValue(removeId)
					+ " CWars Points.");
			return;
		}
		if (c.myShopId == 9) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " donator points.");
			return;
		}
		if (c.myShopId == 12 || c.myShopId == 13) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " Voting points.");
			return;
		}
		if (c.myShopId == 19) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " grace.");
			return;
		}
		if (c.myShopId >= 18) {
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " points.");
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage("This item current costs "
					+ c.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		if (c.playerRights == 3) {
			c.sendMessage("Item ID: "+removeId+ " "+c.getItems().getItemName(removeId) + ": currently costs "
					+ ShopValue + " coins" + ShopAdd);
		} else {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs "
					+ ShopValue + " coins" + ShopAdd);
		}
	}
	public int getSpecItemValue(int id) {
		switch (id) {
		case 15000:
		case 15001:
		case 15002:
		case 15003:
		case 15004:
			return 250;
		case 6889:
		case 6570:
			return 40;
		case 7671:
		case 7673:
			return 90;
		case 10551:
		case 7668:
			return 25;
		case 1052:
		case 7447:
		case 6188:
			return 2;
		case 7390:
		case 7392:
		case 7394:
		case 6724:
		case 6625:
		case 6617:
		case 6633:
		case 6611:
		case 7676:
			return 15;
		case 6619:
		case 6623:
		case 6629:
			return 9;
		}
		return 0;
	}
	public int getSpecialItemValue(int id) {
		/* Agility Shop */
		if (id >= 11850 && id <= 11861) {
			return 15;
		}
		switch (id) {
		case 6665:
		case 6818:
		case 9470:
		case 5607:
		case 5608:
		case 5609:
			return 1;
		case 12802:
		case 12804:
			return 20;
		case 12637:
		case 12638:
		case 12639:
			return 10;
		case 1763:
		case 1765:
		case 1767:
		case 1771:
		case 1773:
			if (c.myShopId != 10)
				return 20;
			else
				return 700;
		case 13121:
			return 5;
		case 13122:
			return 10;
		case 13123:
			return 15;
		case 13124:
			return 20;
		case 5510:
			return 1;
		case 5512:
			return 2;
		case 5514:
			return 4;
		case 12954:
			return 250;
		case 1949:
		case 10721:
		case 10722:
		case 10723:
		case 10392:
		case 10394:
		case 10774:
		case 10724:
		case 10725:
		case 10726:
		case 10727:
		case 10728:
			return 1;
		case 4675:
			return 2;
		case 7927:
			return 10;
		case 4566:
		case 7671:
		case 7673:
			return 5;
		case 1052:
			return 3;
		case 12437:
		case 12426:
		case 12422:
			return 1000;
		case 11863:
			return 1500;
		case 12006:
			return 750;
		case 11740:
			return 60;
		case 12926:
			return 2000;
		case 31802:
			return 1500;
		case 13887:
		case 13893:
			return 500;
		case 11785:
			return 2500;
			
		//Donor
		case 1046:
		case 1040:
		case 1042:
		case 1044:
		case 1048:
		case 1038:
			return 5000;
		case 11862:
		case 12399:
			return 6000;
		case 1050:
		case 11847:
			return 3000;
		case 1053:
		case 1055:
		case 1057:
		case 10493:
		case 12797:
			return 1000;
		case 1037:
		case 9920:
		case 1419:
			return 2500;
		case 12004:
		case 11907:
			return 2000;
		case 12821:
		case 12825:
		case 12809:
		case 11889:
			return 1500;
		case 12817:
			return 3500;
		case 12373:
		case 12414:
		case 12415:
			return 800;
		case 12397:
		case 12393:
		case 12395:
		case 12439:
			return 500;
		case 12927:
			return 1500;
		case 3062:
			return 200;
		case 12932:
			return 1500;
		case 11832:
		case 11834:
			return 800;
		case 11802:
			return 2500;
		case 11806:
			return 1000;
			
		/*
		 * PK STORE
		 */
		case 11212:
		case 4587:
		case 4153:
		case 10758:
		case 10760:
		case 10762:
		case 10764:
		case 10766:
		case 3040:
		case 11230:
			return 1;
		case 989:
			return 10;
		case 10330:
		case 10332:
		case 10334:
			return 700;
		case 11824:
			return 500;
		case 386:
		case 536:
		case 4333:
		case 4353:
		case 4373:
		case 4393:
		case 4413:
		case 537:
		case 761:
			return 2;
		case 4170:
		return 3;
		case 6731:
		case 6733:
		case 6735:
		case 6737:
			return 50;
		case 11864:
		return 250;
		case 4151:
		case 1959:
			return 100;
		case 11128:
		return 150;
		case 6950:
		case 6199:
		case 2573:
			return 200;
		case 7459:
		case 4502:
			return 5;
		case 405:
		case 2579:
		case 9946:
			return 25;
		case 8849:
		case 10858:
		case 9944:
		case 9945:
		case 15006:
		case 15007:
			return 15;
		case 7460:
		case 8848:
		case 15103:
		case 2379:
		case 8714: 
		case 8716: 
		case 8718: 
		case 8720: 
		case 8722: 
		case 8724:
		case 8726:
		case 8728: 
		case 8730:
		case 8732:
		case 8734:
		case 8736:
		case 8738:
		case 8740:
		case 8742:
		case 8744:
		case 7509:
			return 10;
		case 7462:
		case 8850:
		case 7398:
		case 7399:
		case 7400:
		case 732:
			return 20;
		case 10551:
		case 10548:
		case 10828:
		case 15000:
		case 15001:
		case 15002:
		case 15003:
		case 15004:
			return 40;
		case 621:
			return 2000;
		case 6585:
		case 11840:
		case 6570:
		case 6916:
		case 6918:
		case 6920:
		case 6922:
		case 6924:
		case 6914:
		case 2417:
		case 2415:
		case 11235:
		case 2416:
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
		case 15008:
			return 50;
		case 10338:
		case 10342:
		case 10340:
		case 1989:
		case 11772:
			return 500;
		case 11770:
		return 650;
		case 11730:
		case 415:
		case 1961:
		case 6666:
		case 10499:
		case 3844:
		case 3842:
		case 3840:
		case 4084:
			return 100;
		case 10352:
		case 10348:
		case 10346:
		case 10350:
		case 15084:
			return 1000;
		case 11771:
		case 11773:
		return 800;
		case 11724:
		case 11726:
		case 11698:
		case 6571:
			return 300;
		case 11700:
			return 400;
		case 2572:
			return 500;
		case 9703:
			return 700;
		case 11716:
			return 750;
		case 962:
			return 10000;
		case 15098:
			return 1000;
		case 6889:
			return 200;
		case 8845:
			return 5;
		case 8846:
			return 10;
		case 8847:
			return 15;
		case 4712:
			return 10;
		case 4714:
			return 10;

		}
		return 0;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		for (int i : Config.ITEM_SELLABLE) {
			if (i == removeId) {
				c.sendMessage("You can't sell "
						+ c.getItems().getItemName(removeId).toLowerCase()
						+ ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell "
					+ c.getItems().getItemName(removeId).toLowerCase()
					+ " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1,
					removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": shop will buy for " + ShopValue + " coins" + ShopAdd);
		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade) {
			return false;
		}
		if (c.myShopId == 14)
			return false;
		for (int i : Config.ITEM_SELLABLE) {
			if (i == itemID) {
				c.sendMessage("You can't sell "
						+ c.getItems().getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (itemID == 12941) {
			c.sendMessage("You can't sell this item");
			return false;
		}

		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell "
							+ c.getItems().getItemName(itemID).toLowerCase()
							+ " to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot]
					&& (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(c.playerItems[fromSlot] - 1)] == true)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == false
					&& Item.itemStackable[(c.playerItems[fromSlot] - 1)] == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1,
						fromSlot));
				if (c.getItems().freeSlots() > 0
						|| c.getItems().playerHasItem(995)) {
					if (Item.itemIsNote[itemID] == false) {
						c.getItems().deleteItem(itemID,
								c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995, TotPrice2);
					//addShopItem(itemID, 1);
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			PlayerSave.saveGame(c);
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (Item.itemIsNote[itemID] == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade) {
			return false;
		}
		//Fixes dupe within shop via cheat engine.
		if (!shopSellsItem(itemID) && c.myShopId != 14)
			return false;
		if (c.playerRights == 6 && itemID == 7946) {
			c.sendMessage("You cannot purchase this Item as you are an Iron Man!");
			return false;
		}
		if (c.myShopId == 14) {
			skillBuy(itemID);
			return false;
		} else if (c.myShopId == 15) {
			buyVoid(itemID);
			return false;
		}
		if (itemID != itemID) {
			return false;
		}
		if(!shopSellsItem(itemID)) {
			return false;	
		}
		if (amount > 0) {
			if (!shopSellsItem(itemID))
				return false;
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			if (amount > c.getItems().freeSlots()) {
				if (c.getItems().isStackable(itemID)
						&& c.getItems().freeSlots() < 1)
					return false;
				if (!c.getItems().isStackable(itemID)) {
					amount = c.getItems().freeSlots();
					c.sendMessage("You do not have enough inventory space to buy that many.");
				}
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot, Slot1, Slot2 = 0;// Tokkul
			if (c.myShopId == 18 || c.myShopId == 60) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 8) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 49) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 9) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 12 || c.myShopId == 13) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 10) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 16) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 19) {
				handleOtherShop(itemID);
				return false;
			}
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
						fromSlot));
				Slot = c.getItems().getItemSlot(995);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.myShopId != 29 && c.myShopId != 30
						&& c.myShopId != 31) {
					c.sendMessage("You don't have enough coins.");
					break;
				}
				if (Slot1 == -1 && c.myShopId == 29 || c.myShopId == 30
						|| c.myShopId == 31) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
							fromSlot));
					TotPrice2 *= 1.66;
				}
				if (c.playerRights == 3) {
					c.getItems().addItem(itemID, amount);
					c.sendMessage("Anything for you good sir..");
				} else {
					if (c.myShopId == 29 || c.myShopId == 30
							|| c.myShopId == 31) {
						if (c.playerItemsN[Slot1] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(6529,
										c.getItems().getItemSlot(6529),
										TotPrice2);
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								break;
							}
						} else {
							c.sendMessage("You don't have enough tokkul.");
							break;
						}
					}
					if (c.myShopId != 29 || c.myShopId != 30
							|| c.myShopId != 31) {
						if (c.playerItemsN[Slot] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(995,
										c.getItems().getItemSlot(995),
										TotPrice2);
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								break;
							}
						} else {
							c.sendMessage("You don't have enough coins.");
							break;
						}
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			PlayerSave.saveGame(c);
			return true;
		}
		return false;
	}

	public void handleOtherShop(int itemID) {
		if (itemID != itemID) {
			return;
		}
		if (!shopSellsItem(itemID))
			return;
		if (c.myShopId == 8) {
			if (c.pkPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pkPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
					c.getPA().sendNewString(
							"@whi@Pk Points: @gre@" + c.pkPoints + " ", 7333);
				}
			} else {
				c.sendMessage("You do not have enough pk points to buy this item.");
			}
		} else if (c.myShopId == 15) {
			if (c.cwPoints >= getSpecItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.cwPoints -= getSpecItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
					c.getPA()
							.sendNewString(
									"@whi@CWars Points: @gre@" + c.cwPoints
											+ " ", 7333);
				}
			} else {
				c.sendMessage("You do not have enough Cwars points to buy this item.");
			}
		} else if (c.myShopId == 9) {
			if (c.donPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.donPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 12 || c.myShopId == 13) {
			if (c.votPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.votPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Vote points to buy this item.");
			}
		} else if (c.myShopId == 10) {
			if (c.slayerPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.slayerPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 18) {
			if (c.pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough pest control points to buy this item.");
			}
		} else if (c.myShopId == 49) {
			if (c.duoPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.duoPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
					c.sendMessage("You have <col=255>" + c.duoPoints
							+ "</col> Duo points left.");
					c.getPA().sendNewString(
							"<col=FF0000>Duo Points: <col=00FF00>["
									+ c.duoPoints + "]  ", 29167);
				}
			} else {
				c.sendMessage("You do not have enough Duo points to buy this item.");
			}
		} else if (c.myShopId == 16) {
			if (c.hunterPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.hunterPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
					c.sendMessage("You have <col=255>" + c.hunterPoints
							+ "</col> Hunter points left.");
				}
			} else {
				c.sendMessage("You do not have enough Hunter points to buy this item.");
			}
		} else if (c.myShopId == 19) {
			int itemSlot = c.getItems().getItemSlot(11849);
			if (c.getItems().playerHasItem(11849) && c.getItems().getItemAmount(11849) >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(11849,
							c.getItems().getItemSlot(11849),
							getSpecialItemValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough grace to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 14;
		setupSkillCapes(capes, get99Count());
	}

	/*
	 * public int[][] skillCapes =
	 * {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680
	 * ,4359,2682},{3,2701,4341,2703
	 * },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733
	 * },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730
	 * },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727},
	 * {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361,
	 * 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */
	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
			9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786,
			9810, 9765 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < 21; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		//synchronized (c) {
			c.getItems().resetItems(3823);
			c.isShopping = true;
			c.myShopId = 14;
			c.getPA().sendFrame248(3824, 3822);
			c.getPA().sendNewString("Skillcape Shop", 3901);

			int TotalItems = 0;
			TotalItems = capes2;
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			for (int i = 0; i < 21; i++) {
				if (c.getLevelForXP(c.playerXP[i]) < 99)
					continue;
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		//}
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else { 
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void openVoid() {
	}

	public void buyVoid(int item) {
	}

}