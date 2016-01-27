package server.model.players.packets;
import server.Server;
/**
 * @author Ryan / Lmctruck30
 */

import server.model.content.PotionMaking;
import server.model.items.UseItem;
import server.model.players.Client;
import server.model.players.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.usedWithSlot = c.getInStream().readUnsignedWord();
		c.itemUsedSlot = c.getInStream().readUnsignedWordA();
		c.useWith = c.playerItems[c.usedWithSlot] - 1;
		c.itemUsed = c.playerItems[c.itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(c.useWith, 1, c.usedWithSlot)
				|| !c.getItems().playerHasItem(c.itemUsed, 1, c.itemUsedSlot)) {
			return;
		}
		/*if (PotionMaking.createPotion(c, itemUsed, useWith, itemUsedSlot, usedWithSlot)) {
			return;
		}
		if (PotionMaking.combineDose(c, itemUsed, useWith, itemUsedSlot, usedWithSlot)) {
			return;
		}*/
//		if (c.itemUsed == 11941 || c.useWith == 11941) {
//			if (c.itemUsed != 11941) {
//				c.bagItem = c.itemUsed;
//				c.bagItemSlot = c.itemUsedSlot;
//			} else if (c.useWith != 11941) {
//				c.bagItem = c.useWith;
//				c.bagItemSlot = c.usedWithSlot;
//			}
//			c.getDH().sendDialogues(11941, -1);
//		}
		UseItem.ItemonItem(c, c.itemUsed, c.useWith);
		//Server.godbooks.itemOnItemHandle(c, useWith, itemUsed);
	}

}
