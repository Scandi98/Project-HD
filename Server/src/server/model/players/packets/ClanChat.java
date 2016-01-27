package server.model.players.packets;

import server.util.Misc;
import server.clan.Clan;
import server.model.players.Client;
import server.model.players.PacketType;
import server.Server;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		String owner = Misc.longToPlayerName2(player.getInStream().readQWord())
				.replaceAll("_", " ");
		if(player.isSearching) {
			String textSent = owner;//shouldn't this be owner to read the clients packet?
			player.getPA().searchBank(textSent);
		    player.searchName = textSent;
		    player.getPA().searchBank(player.searchName);
		    return;
		    
		}
		if (owner != null && owner.length() > 0) {
			if (player.clan == null) {
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(player);
				} else if (owner.equalsIgnoreCase(player.playerName)) {
					Server.clanManager.create(player);
				} else {
					player.sendMessage(Misc.formatPlayerName(owner)
							+ " has not created a clan yet.");
				}
				player.getPA().refreshSkill(21);
				player.getPA().refreshSkill(22);
				player.getPA().refreshSkill(23);
			}
		}
	}
}
