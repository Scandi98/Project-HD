package server.model.players.packets;

import server.model.content.Blowpipe;
import server.model.content.Pouches;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		Pouches.checkEssencePouch(c, itemId);
		switch (itemId) {
		case 11694:
			c.sendMessage("Disabled due to duping");
			break;
		case 11696:
			c.sendMessage("Disabled due to duping.");
			break;
		case 11698:
			c.sendMessage("Disabled due to duping.");
			break;
		case 11700:
			c.sendMessage("Diabled due to duping.");
			break;
		case 12926:
			Blowpipe.handleOperate(c);
			break;
//		case 11941:
//			if (c.inTrade || c.inDuel || c.isShopping) {
//				return;
//			}
//			c.showLootingBagInterface();
//			break;
		default:
			if (c.playerRights == 3)
				Misc.println(c.playerName + " - Item2Option: " + itemId);
			break;
		}

	}

}
