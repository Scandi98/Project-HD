package server.model.players.packets;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		c.pItemY = c.getInStream().readSignedWordBigEndian();
		c.pItemId = c.getInStream().readUnsignedWord();
		c.pItemX = c.getInStream().readSignedWordBigEndian();
		if(!Server.itemHandler.itemExists(c.pItemId, c.pItemX, c.pItemY)) {
	        c.stopMovement();
	        return;
	    }// where at
		if (c.fireMaking) {
			c.sendMessage("Cannot pickup items while lighting a fire");
			return;
		}
		if (Math.abs(c.getX() - c.pItemX) > 25
				|| Math.abs(c.getY() - c.pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		if (c.getItems().hasClueScroll(c.pItemId)) {
			c.sendMessage("");
			return;
		}
		c.getCombat().resetPlayerAttack();
		if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
			Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX,
					c.pItemY, true);
		} else {
			c.walkingToItem = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(!c.walkingToItem)
						container.stop();
					if(c.getX() == c.pItemX && c.getY() == c.pItemY) {
						Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);
						container.stop();
					}
				}
				@Override
				public void stop() {
					c.walkingToItem = false;
				}
			}, 1);
		}

	}

}
