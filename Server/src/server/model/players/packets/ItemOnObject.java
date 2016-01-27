package server.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import server.model.items.UseItem;
import server.model.players.Client;
import server.model.players.PacketType;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		/*
		 * a = ? b = ?
		 */

		int a = c.getInStream().readUnsignedWord();
		c.objectId = c.getInStream().readSignedWordBigEndian();
		c.objectY = c.getInStream().readSignedWordBigEndianA();
		int b = c.getInStream().readSignedWordBigEndian() & 0xFFFF;
		c.objectX = c.getInStream().readSignedWordBigEndianA();
		c.usedId = c.getInStream().readUnsignedWord();
		c.getPA().findSlot(b);
		if (c.playerRights == 3) {
			c.sendMessage(c.objectX + " " +c.objectY);
			/*try {
				ObjectPrint.write(c.objectX, c.objectY);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		if (!c.getItems().playerHasItem(c.usedId, 1)) {
			return;
		}
		if (c.destinationReached()) {
			c.turnPlayerTo(c.objectX, c.objectY);
			UseItem.ItemonObject(c, c.objectId, c.objectX, c.objectY, c.usedId);
		} else {
			c.clickObjectType = 4;
		}

	}

}
