package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;
import server.Connection;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		Misc.textUnpack(c.getChatText(), c.packetSize - 2).toLowerCase();
		//System.out.println(term);
		if (!Connection.isMuted(c)) {
			c.setChatTextUpdateRequired(true);
		} else if (Connection.isMuted(c)) {
			c.sendMessage("You are muted, no one can hear you.");
			return;
		}
	}
}