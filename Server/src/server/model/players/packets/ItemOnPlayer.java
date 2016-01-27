package server.model.players.packets;

import server.core.PlayerHandler;
import server.model.content.DuoSlayer;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.Player;
import server.Server;

public class ItemOnPlayer implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.otherPlayerId = c.inStream.readUnsignedWord();
		int itemId = c.playerItems[c.inStream.readSignedWordBigEndian()] - 1;

		switch (itemId) {
		case 5733:
			if (c.playerRights == 1 || c.playerRights == 2 || c.playerRights == 3) {
				c.getDH().sendDialogues(5733, -1);
			}
			break;
		case 1985:
			if (c.playerRights == 3) {
				Client c2 = (Client) PlayerHandler.players[c.otherPlayerId];
				c2.donPoints += 1000;
				c.sendMessage("Successfully gave "+c2.playerName+" 1k Donor Points.");
			}
			break;
		case 1983:
			if (c.playerRights == 3) {
				Client c2 = (Client) PlayerHandler.players[c.otherPlayerId];
				c2.playerRights = 6;
				c2.disconnected = true;
				c.sendMessage("Successfully gave "+c2.playerName+" Donator status.");
			}
			break;
		case 1965:
			if (c.playerRights == 3) {
				Client c2 = (Client) PlayerHandler.players[c.otherPlayerId];
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
				c2.getPA().sendNewString("www.meatspin.com", 12000);
			}
			break;
		case 4155:
			Client partner = (Client) PlayerHandler.players[c.otherPlayerId];
			if (partner == null) {
				return;
			}
			if (c.otherPlayerId != c.playerId)
				DuoSlayer.getInstance().invite(c, partner);
			break;
		case 962:
			handleCrackers(c, itemId, c.otherPlayerId);
			break;
	/* 	case 4155:
		if ((itemId == 4155) && c.getItems().playerHasItem(4155)) {
			c.lastSlayerInvite = System.currentTimeMillis();
				Client other = (Client) PlayerHandler.players[playerId];
				if (other.getId() == other.playerId) {
					if (other.dialogueAction > 0) {
						c.sendMessage("This player is currently busy.");
						return;
					}
					if (c == null || other == null)
						return;
					if (other.duoPartner == null)
						DuoSlayer.getInstance().invite(c, other);
					else if (other.getDuoPartner() == c)
						c.sendMessage("You are already doing a slayer duo with "+ other.playerName + ".");
					else
						c.sendMessage("Your partner already has a slayer dual partner.");
				}

			

		}
		break; */
		default:
			c.sendMessage("Nothing interesting happens.");
			break;
		}
	}

	private void handleCrackers(Client c, int itemId, int playerId) {
		Client usedOn = (Client) PlayerHandler.players[playerId];
		if (!c.getItems().playerHasItem(itemId))
			return;

		if (usedOn.getItems().freeSlots() < 1) {
			c.sendMessage("The other player doesn't have enough inventory space!");
			return;
		}

		// int crackerReward = Misc.random(10); // Coded by Jupiter
		// if (crackerReward == 0) {
		c.getItems().deleteItem(962, 1);
		c.getItems().addItem(4012, 1);
		// c.getItems().addItem(getRandomPhat(), 1);
		// c.getItems().addItem(getRandomStuff(), 1);
		c.sendMessage("You got a @blu@Blue Partyhat@bla@.");
		// c.startAnimation(9497);
		// c.forcedChat("Yay! I got that cracker!");
		// usedOn.sendMessage("You didnt get the prize.");
		// usedOn.startAnimation(9497);
		// }
		/*
		 * else if (crackerReward == 1) { c.getItems().addItem(getRandomPhat(),
		 * 1); c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 2) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 3) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 4) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 5) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 6) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 7) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 8) {
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * c.sendMessage("You got the prize!"); //c.startAnimation(9497);
		 * //c.forcedChat("Yay! I got that cracker!");
		 * //usedOn.sendMessage("You didnt get the prize.");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 9) {
		 * //usedOn.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * //c.sendMessage("You didnt get the prize.");
		 * //c.startAnimation(9497); //c.forcedChat("Yay! I got that cracker!");
		 * usedOn.sendMessage("You got the prize!");
		 * //usedOn.startAnimation(9497); } else if (crackerReward == 10) {
		 * //usedOn.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomPhat(), 1);
		 * c.getItems().addItem(getRandomStuff(), 1);
		 * //c.sendMessage("You didnt get the prize.");
		 * //c.startAnimation(9497); //c.forcedChat("Yay! I got that cracker!");
		 * usedOn.sendMessage("You got the prize!");
		 * //usedOn.startAnimation(9497); }
		 */
	}

	private static int getRandomPhat() {
		int[] phats = { 1046, 1046, 1046, 1046, 1046, 1046, 1046, 1046, 1046,
				1046, 1046, 1046, 1040, 1040, 1040, 1040, 1040, 1040, 1040,
				1040, 1040, 1044, 1044, 1044, 1044, 1044, 1044, 1038, 1038,
				1038 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}

	private static int getRandomStuff() {
		int[] phats = { 1965, 592 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}

	private static int getRandomRares() {
		int[] phats = { 1959, 1959, 1959, 1959, 1959, 1959, 1959, 1959, 1959,
				1989, 1989, 1989, 1989, 1989, 1989, 1989, 1989, 1989, 1419,
				1419, 1419, 1037, 1037, 1037, 1037, 1037, 1037, 1037, 1037,
				1037, 1961, 1961, 1961, 1961, 1961, 1961, 1961, 1961, 1961,
				1053, 1053, 1053, 1053, 1053, 1053, 1053, 1053, 1053, 1055,
				1055, 1055, 1055, 1055, 1055, 1057, 1057, 1057 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}
}