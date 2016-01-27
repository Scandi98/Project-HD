package server.model.players.packets;

import server.Config;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

public class TitlePacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.playerRights > 2 && c.playerRights < 10
				|| c.playerName.equals("") || c.ironDonator(c.playerName)) {
			String title = Misc.longToPlayerName2(c.getInStream().readQWord())
					.substring(0).toUpperCase();
			title = title.replaceAll("_", " ");
			c.playerTitle = title;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
			c.getDH().sendDialogues(1337, 1);
		} else {
			c.sendMessage("You must be a donator to use this feature use ::donate "
					+ c.playerName);
			return;
		}
	}
}
