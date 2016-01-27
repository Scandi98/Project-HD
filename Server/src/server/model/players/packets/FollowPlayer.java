package server.model.players.packets;

import server.core.PlayerHandler;
import server.model.players.Attributes.A;
import server.model.players.Client;
import server.model.players.PacketType;

public class FollowPlayer implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if (PlayerHandler.players[followPlayer] == null) {
			return;
		}
		if (c.wearId == 7927 == true) {
			c.sendMessage("Unmorph before you can start walking again!");
			return;
		}
		if (c.wearId == 6583 == true) {
			c.sendMessage("Unmorph before you can start walking again!");
			return;
		}
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followingPlayer = true;
		//c.followDistance = 1;
		c.followId = followPlayer;
		c.attr(A.REGULAR_FOLLOWING, true);
	}
}