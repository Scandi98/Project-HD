package server.model.players.packets;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.Player;
import server.world.ItemHandler;

/**
 * Magic on floor items
 **/
public class MagicOnFloorItems implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		final int itemY = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readUnsignedWord();
		final int itemX = c.getInStream().readSignedWordBigEndian();
		c.getInStream().readUnsignedWordA();

		if (!ItemHandler.itemExists(itemId, itemX, itemY)) {
			c.stopMovement();
			return;
		}
		c.usingMagic = true;
		if (!c.getCombat().checkMagicReqs(51)) {
			c.stopMovement();
			return;
		}
		if(Player.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
	         int offY = (c.getX() - itemX) * -1;
	         int offX = (c.getY() - itemY) * -1;
	         c.teleGrabX = itemX;
	         c.teleGrabY = itemY;
	         c.teleGrabItem = itemId;
	         c.turnPlayerTo(itemX, itemY);
	         c.teleGrabDelay = System.currentTimeMillis();
	         c.startAnimation(Player.MAGIC_SPELLS[51][2]);
	         c.gfx100(Player.MAGIC_SPELLS[51][3]);
	         c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
	         c.getPA().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, Player.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
	         c.getPA().refreshSkill(6);
	         c.stopMovement();
	         }
	}

}
