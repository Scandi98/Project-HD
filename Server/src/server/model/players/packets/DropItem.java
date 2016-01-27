package server.model.players.packets;

import server.Config;
import server.Server;
import server.model.content.ItemKit;
import server.model.npcs.pet.DropPet;
import server.model.npcs.pet.PetData;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		if (System.currentTimeMillis() - c.alchDelay < 1800) {
			return;
		}
		if (ItemKit.isItemKit(itemId)) {
			if (ItemKit.handleRevertKit(c, itemId)) {
				return;
			}
			return;
		}
		if (itemId == 11941 && c.getBag().getSizeOfLootingBag() > 0) {
			c.sendMessage("Can't drop bag with items in it.");
			return;
		}
		if (itemId == 12809 || itemId == 12415 || itemId == 12416 || itemId == 12414) {
			c.sendMessage("You cannot revert this item!");
			return;
		}
		if (itemId == 12941 && c.getBag().getSizeOfLootingBag() > 0) {
			c.sendMessage("Can't drop the bag with items in it.");
			return;
		}
		if (itemId == 7928) {
			c.eggsCollected--;
			c.egg1Collected = false;
		}
		if (itemId == 7929) {
			c.eggsCollected--;
			c.egg2Collected = false;
		}
		if (itemId == 7930) {
			c.eggsCollected--;
			c.egg3Collected = false;
		}
		if (itemId == 7931) {
			c.eggsCollected--;
			c.egg4Collected = false;
		}
		if (itemId == 7932) {
			c.eggsCollected--;
			c.egg5Collected = false;
		}
		if (itemId == 7933) {
			c.eggsCollected--;
			c.egg6Collected = false;
		}
		if (itemId == 621) {
			c.sendMessage("You cannot drop this Item");
			return;
		}
		if (c.arenas()) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}

		boolean destroy = true;
		for (int i : Config.DESTROY_ITEMS) {
		if (i == itemId) {
				c.droppedItem = itemId;
				c.getPA().destroyInterface(itemId);
				return;
				}
		}
		
		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if (c.getPA().viewingOther) {
			c.sendMessage("You cannot do this while viewing another players inventory.");
			return;
		} else {
			for (int i = 0; i < PetData.petData.length; i++) {
				if (PetData.petData[i][1] == itemId) {
					if (c.getPetSummoned()) {
						droppable = false;
						break;
					}
				}
			}
			DropPet.getInstance().dropPetRequirements(c, itemId, slot);
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1
				&& c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (c.underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				c.sendMessage("Your item dissappears as it hits the ground.");
			} else {
				c.sendMessage("This items cannot be dropped.");
			}
		}

	}
}
