package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketHandler;
import server.model.players.PacketType;

public class MusicPacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int id = c.getInStream().readDWord();
	}

}
