package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.skills.crafting.GemCrafting;
import server.model.players.skills.crafting.GemData;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndian();
		int removeId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		switch (interfaceId) {
		case 4233 : // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 10, 0);
			break;
		case 4239 : // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 10, 1);
			break;
		case 4245 : // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[removeSlot], 10, 2);
			break;
		
		case 1688:
			c.getPA().useOperate(removeId);
			break;//wtf is that? thats how it >? i didnt type anything
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			 if(!c.getItems().playerHasItem(removeId))
                 return;
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;

		case 5064:
			if(!c.getItems().playerHasItem(removeId))
                return;
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			c.getItems().bankItem(removeId, removeSlot, 10);
			break;

		case 5382:
			c.getItems().fromBank(removeId, removeSlot, 10);
			break;

		case 3322:
			if (c.duelStatus > 0) {
				c.getDuel().stakeItem(removeId, removeSlot, 10);
				return;
			}
			c.getTradeHandler().tradeItem(removeId, removeSlot, 10);
			break;

		case 3415:
			if (c.duelStatus > 0) {
				return;
			}
			c.getTradeHandler().fromTrade(removeId, removeSlot, 10);
			break;

		case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 10);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
		case 2031:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 10);
			break;
		}
	}

}
