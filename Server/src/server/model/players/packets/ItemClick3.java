package server.model.players.packets;

import server.model.content.Blowpipe;
import server.model.content.PotionMaking;
import server.model.content.Pouches;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 =  c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (itemId == 12926) {
			Blowpipe.handleEmpty(c);
			return;
		}
		if (PotionMaking.emptyPotion(c, itemId, c.getItems().getItemSlot(itemId)))
			return;
		switch (itemId) {
		case 1712:
			c.getPA().handleGlory(itemId);
			break;
		case 995:
			c.getPouch().addPouch();
			break;

		default:
			if (c.playerRights == 3)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId
						+ " : " + itemId11 + " : " + itemId1);
			break;
		}

	}

}
