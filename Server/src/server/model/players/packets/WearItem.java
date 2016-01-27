package server.model.players.packets;

import server.model.content.Pouches;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		if (!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
			return;
		}
		if ((c.playerIndex > 0 || c.npcIndex > 0) && c.wearId != 4153)
			c.getCombat().resetPlayerAttack();
		for (int[] element : Pouches.POUCHES) {
			if (c.wearId == element[0]) {
				Pouches.emptyEssencePouch(c, c.wearId);
				return;
			}
		}
		if (c.wearId >= 5509 && c.wearId <= 5515) {
			int pouch = -1;
			int a = c.wearId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().emptyPouch(pouch);
			return;
		}
		if (c.wearId == 6583 && !c.inWild() && !c.inPvP() && !c.inTrade && !c.inDuel && !c.interfaceOpen && !c.isShopping && c.duelStatus == 0) {
			c.setSidebarInterface(1, 6014);
			c.setSidebarInterface(2, 6014);
			c.setSidebarInterface(3, 6014);
			c.setSidebarInterface(4, 6014);
			c.setSidebarInterface(5, 6014);
			c.setSidebarInterface(6, 6014);
			c.setSidebarInterface(7, 6014);
			c.setSidebarInterface(8, 6014);
			c.setSidebarInterface(9, 6014);
			c.setSidebarInterface(10, 6014);
			c.setSidebarInterface(11, 6014);
			c.setSidebarInterface(12, 6014);
			c.setSidebarInterface(13, 6014);
			c.setSidebarInterface(0, 6014);
			c.sendMessage("As you put on the ring you turn into a rock!");
			c.dueling = false;
			c.npcId2 = 2626;
			c.isNpc = true;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}

		if (c.wearId == 7927 && !c.inWild() && !c.inPvP() && !c.inTrade && !c.inDuel && !c.interfaceOpen && !c.isShopping && c.duelStatus == 0) {
			c.setSidebarInterface(1, 6014);
			c.setSidebarInterface(2, 6014);
			c.setSidebarInterface(3, 6014);
			c.setSidebarInterface(4, 6014);
			c.setSidebarInterface(5, 6014);
			c.setSidebarInterface(6, 6014);
			c.setSidebarInterface(7, 6014);
			c.setSidebarInterface(8, 6014);
			c.setSidebarInterface(9, 6014);
			c.setSidebarInterface(10, 6014);
			c.setSidebarInterface(11, 6014);
			c.setSidebarInterface(12, 6014);
			c.setSidebarInterface(13, 6014);
			c.setSidebarInterface(0, 6014);
			c.sendMessage("As you put on the ring you turn into an egg!");
			c.dueling = false;
			c.npcId2 = 3689 + Misc.random(5);
			c.isNpc = true;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
		// c.attackTimer = oldCombatTimer;
		if (!c.inTrade) {
			c.getItems().wearItem(c.wearId, c.wearSlot);
		}
	}

}
