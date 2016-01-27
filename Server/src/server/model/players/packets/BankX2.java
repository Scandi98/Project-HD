package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount < 0)// this should work fine
		{
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0)
			Xamount = 1;
		/*
		 * if (c.usingAltar == true) {
		 * c.getPrayer().bonesOnAltar2(c.altarItemId, Xamount); }
		 */
		switch (c.xInterfaceId) {
		case 0:
			if (c.usingAltar == true) {
				c.getPrayer().bonesOnAltar2(c.altarItemId, Xamount);
			}
			break;
		case 5064:
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if(!c.getItems().playerHasItem(c.xRemoveId, Xamount))
                return;
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 5382:
			if (c.usingAltar == true) {
				c.getPrayer().bonesOnAltar2(c.altarItemId, Xamount);
			} else {
				
				c.getItems().fromBank(c.bankItems[c.xRemoveSlot],
						c.xRemoveSlot, Xamount);
			}
			break;

		case 3322:
			if (c.duelStatus > 0) {
				c.getDuel().stakeItem(c.playerItems[c.XremoveSlot] - 1,
						c.XremoveSlot, Xamount);
				return;
			}
			c.getTradeHandler().tradeItem(c.playerItems[c.XremoveSlot] - 1,
					c.XremoveSlot, Xamount);
			break;

		case 3415:
			c.getTradeHandler().fromTrade(
					c.getTradeHandler().getOffer()[c.XremoveSlot] - 1,
					c.XremoveSlot, Xamount);
			break;

		case 6669:
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
			c.getDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;
		}
	}
}