package server.model.players.packets;

import server.Server;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType { // where is your playerhandler

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.getTradeHandler().getCurrentTrade() != null) {
			if (c.getTradeHandler().getCurrentTrade().isOpen()) {
				c.getTradeHandler().decline();
			}
		}
		if (c.myShopId != 0) {
			c.myShopId = 0;
		}
		c.isShopping = false;
		if (c.inDuelArena()) {
			if (c.duelStatus == 6) {
				c.getDuel().claimStakedItems();
			}
			if (c.duelStatus > 4) {
				return;
			}
			Client o = (Client) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				c.getDuel().declineDuel();
				return;
			}
			if (o.duelStatus > 4) {
				return;
			}
			o.getDuel().declineDuel();
		}
		if (c.isBanking) {
			c.getPA().removeAllWindows();
		}
	}

}
