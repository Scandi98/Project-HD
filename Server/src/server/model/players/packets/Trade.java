package server.model.players.packets;

import server.Config;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Trading
 */
public class Trade implements PacketType {
	
	public static final int REQUEST = 73, ANSWER = 139;

	@Override
	public void processPacket(Client client, int packetType, int packetSize) {
		if (client.isBanking || client.isShopping || client.duelStatus > 0) {
			client.sendMessage("You are far too busy right now.");
			return;
		}
		if (packetType == REQUEST) {
			int trade = client.inStream.readSignedWordBigEndian();
			if (trade < 0 || trade >= Config.MAX_PLAYERS)
				return;
			if (PlayerHandler.players[trade] != null) {
				Client c = (Client) PlayerHandler.players[trade];
				client.getTradeHandler().requestTrade(c);
			}
			client.println_debug("Trade Request to: " + trade);
		} else if (packetType == ANSWER) {
			int trade = client.inStream.readSignedWordBigEndian();
			if (trade < 0 || trade >= Config.MAX_PLAYERS)
				return;
			if (PlayerHandler.players[trade] != null) {
				Client c = (Client) PlayerHandler.players[trade];
				client.getTradeHandler().answerTrade(c);
			}
			client.println_debug("Trade Answer to: " + trade);
		}
	}

}
