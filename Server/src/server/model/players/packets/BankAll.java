package server.model.players.packets;

import server.model.items.GameItem;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();
		switch (interfaceId) {
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			if (!c.getItems().playerHasItem(removeId))
				return;
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (!c.getItems().playerHasItem(removeId))
				return;
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (Item.itemStackable[removeId]) {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.playerItemsN[removeSlot]);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.getItems().itemAmount(c.playerItems[removeSlot]));
			}
			break;

		case 5382:
			if (c.bankingTab == 0) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItemsN[removeSlot]);
			} else if (c.bankingTab == 1) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems1N[removeSlot]);
			} else if (c.bankingTab == 2) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems2N[removeSlot]);
			} else if (c.bankingTab == 3) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems3N[removeSlot]);
			} else if (c.bankingTab == 4) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems4N[removeSlot]);
			} else if (c.bankingTab == 5) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems5N[removeSlot]);
			} else if (c.bankingTab == 6) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems6N[removeSlot]);
			} else if (c.bankingTab == 7) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems7N[removeSlot]);
			} else if (c.bankingTab == 8) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems8N[removeSlot]);
			} else {
				c.sendMessage("Error");
				return;
			}
			break;

		case 3322:
			if (c.duelStatus > 0) {
				if(Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
					c.getDuel().stakeItem(removeId, removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getDuel().stakeItem(removeId, removeSlot, 28);
				}
				return;
			}
			if (Item.itemStackable[removeId]) {
				c.getTradeHandler().tradeItem(c.playerItems[removeSlot] - 1,
						removeSlot, c.playerItemsN[removeSlot]);
			} else {
				c.getTradeHandler().tradeItem(removeId, removeSlot, 28);
			}
			break;

		case 3415:
			c.getTradeHandler().fromTrade(removeId, removeSlot,
					c.getTradeHandler().getOfferN()[removeSlot]);
			break;

		case 6669:
			if (Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
				for (GameItem item : c.getDuel().stakedItems) {
					if (item.id == removeId) {
						c.getDuel()
								.fromDuel(
										removeId,
										removeSlot,
										c.getDuel().stakedItems
												.get(removeSlot).amount);
					}
				}

			} else {
				c.getDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		}
	}

}
