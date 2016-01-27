package server.model.players.packets;

import server.model.content.MysteryBox;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.PacketType;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int npcId = NPCHandler.npcs[i].npcId;

		if (c.isStaff()) {
			c.sendMessage("NPC ID: "+npcId);
		}
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		if (npcId == 4066) {
			MysteryBox.handleBox(c, itemId);
		}
		//UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
