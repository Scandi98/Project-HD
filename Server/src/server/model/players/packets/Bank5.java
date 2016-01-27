package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.skills.crafting.GemCrafting;
import server.model.players.skills.crafting.GemData;

/**
 * Bank 5 Items
 **/
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readSignedWordBigEndianA();
		int removeId = c.getInStream().readSignedWordBigEndianA();
		int removeSlot = c.getInStream().readSignedWordBigEndian();

		switch (interfaceId) {
		
		case 4233 : // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 5, 0);
			break;
		case 4239 : // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 5, 1);
			break;
		case 4245 : // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 5, 2);
			break;

		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 1);
			break;

		case 3823:
			 if(!c.getItems().playerHasItem(removeId))
                 return;
			c.getShops().sellItem(removeId, removeSlot, 1);
			break;

		case 5064:
			 if(!c.getItems().playerHasItem(removeId))
                 return;
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			c.getItems().bankItem(removeId, removeSlot, 5);
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 5);
			break;

		case 3322:
			if (c.duelStatus > 0) {
				c.getDuel().stakeItem(removeId, removeSlot, 5);
				return;
			}
			c.getTradeHandler().tradeItem(removeId, removeSlot, 5);
			break;

		case 3415:
			if (c.duelStatus > 0) {
				return;
			}
			c.getTradeHandler().fromTrade(
					c.getTradeHandler().getOffer()[removeSlot] - 1, removeSlot,
					5);
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 5);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 5);
			break;

		}
	}

}
