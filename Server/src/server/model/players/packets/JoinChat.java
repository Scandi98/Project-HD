package server.model.players.packets;

import server.Server;
import server.clan.Clan;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

public class JoinChat implements PacketType {

	@Override
	public void processPacket(Client paramClient, int packetType, int packetSize) {
		String str = Misc.longToPlayerName2(
				paramClient.getInStream().readQWord()).replaceAll("_", " ");
		if ((str != null) && (str.length() > 0) && (paramClient.clan == null)) {
			Clan localClan = Server.clanManager.getClan(str);
			if (localClan != null)
				localClan.addMember(paramClient);
			else if (str.equalsIgnoreCase(paramClient.playerName))
				Server.clanManager.create(paramClient);
			else {
				paramClient.sendMessage(Misc.formatPlayerName(str)
						+ " has not created a clan yet.");
			}
			paramClient.getPA().refreshSkill(21);
			paramClient.getPA().refreshSkill(22);
			paramClient.getPA().refreshSkill(23);
		}
	}

}