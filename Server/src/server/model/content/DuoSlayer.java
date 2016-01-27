package server.model.content;

import server.Server;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.util.Misc;

public class DuoSlayer {

	/**
	 * Instances the DuoSlayer class
	 */
	public static DuoSlayer INSTANCE = new DuoSlayer();

	/**
	 * This array holds the values of possible assignments.
	 */
	private static int[] tasks = { 6609, 239, 270, 272, 274, 3162,
		3129, 2205, 2215, 600, 2054, 499 };

	public static void cancelTask(Client c) {
		if (c.getDuoPartner() == null) {
			c.sendMessage("You don't have a duo partner therefore cannot do this.");
			return;
		}
		if (c.duoPoints < 10) {
			c.sendMessage("You have insufficient Duo Slayer Points to do this.");
			return;
		}
		c.duoPoints -= 10;
		c.duoTaskAmount = -1;
		c.duoTask = -1;
		final Player partner = c.getDuoPartner();
		Client other = (Client) partner;
		if (other != null) {
			other.duoTaskAmount = -1;
			other.duoTask = -1;
			other.sendMessage("Your partner canceled your duo task.");
		}
		c.sendMessage("Your task has been cancelled.");
	}

	public static void logoutTask(Client c) {
		if (c.getDuoPartner() == null) {
			c.sendMessage("You don't have a duo partner therefore cannot do this.");
			return;
		}
		if (c.duoPoints < 10) {
			c.sendMessage("You have insufficient Duo Slayer Points to do this.");
			return;
		}
		c.duoTaskAmount = -1;
		c.duoTask = -1;
		final Player partner = c.getDuoPartner();
		Client other = (Client) partner;
		if (other != null) {
			other.duoTaskAmount = -1;
			other.duoTask = -1;
			other.sendMessage("Your partner canceled your duo task.");
		}
		c.sendMessage("Your task has been cancelled.");
	}

	/**
	 * Gets the DuoSlayer instance
	 * 
	 * @return
	 */
	public static DuoSlayer getInstance() {
		return INSTANCE;
	}

	/**
	 * Accept DuoSlayer request.
	 * 
	 * @param client
	 * @param partner
	 */
	public void accept(Client c, Player partner) {
		Client other = (Client) partner;
		c.setDuoPartner(partner);
		partner.setDuoPartner(c);
		c.sendMessage("You are now doing a slayer duo with : "
				+ partner.playerName + ".");
		c.sendMessage("Please visit the Chaeldar the duo slayer master at home to start your task.");
		if (other != null) {
			other.sendMessage(c.playerName + " has accepted your request.");
			other.sendMessage("Please visit the Chaeldar the duo slayer master at home to start your task.");
		}
		c.getPA().closeAllWindows();

	}

	/**
	 * Assigns the DuoSlayer task.
	 * 
	 * @param client
	 */
	public void assignDuo(Client client) {
		if (client.duoTask > 0) {
			((Client) client)
					.sendMessage("You already have a duo slayer task.");
			((Client) client).getPA().closeAllWindows();
			return;
		}
		if (client.duoPartner == null) {
			((Client) client)
					.sendMessage("You don't have a duo partner, right click them and hit Duo Request");
			((Client) client).sendMessage("to invite him/her.");
			((Client) client).getPA().closeAllWindows();
			return;
		}
		final int randomTask = tasks[Misc.random(tasks.length - 1)];
		String npcDef = Server.npcHandler.getNpcListName(randomTask)
				.replaceAll("_", " ");
		if (npcDef == null) {
			assignDuo(client);
			return;
		}
		final Player partna = client.getDuoPartner();
		for (int i = 0; i < Server.npcHandler.slayerReqs.length; i = i + 2)
			if (Server.npcHandler.slayerReqs[i] == randomTask) {
				if (client.playerLevel[18] < Server.npcHandler.slayerReqs[i + 1]) {
					assignDuo(client);
					return;
				}
				if (partna != null) {
					if (partna.playerLevel[18] < Server.npcHandler.slayerReqs[i + 1])
						assignDuo(client);
					return;
				}
			}
		client.duoTask = randomTask;
		client.duoTaskAmount = 5 + Misc.random(7);
		if (partna != null) {
			client.getDuoPartner().duoTaskAmount = client.duoTaskAmount;
			client.getDuoPartner().duoTask = randomTask;
		}
		client.getDH().sendNpcChat2(
				"Your dual slayer task is to kill " + client.duoTaskAmount,
				"" + npcDef + ". Good luck to you and your partner.", 1598,
				"Duo Master");
		client.getPA()
				.sendNewString(
						"@or1@Duo Slayer Points: @gre@" + client.duoPoints
								+ " ", 29171);
		client.getPA().sendNewString(
				"@or1@Task: @gre@" + client.duoTaskAmount + " "
						+ Server.npcHandler.getNpcListName(client.duoTask)
						+ " ", 29172);
		if (partna != null)
			((Client) partna).getDH().sendNpcChat2(
					"Your dual slayer task is to kill "
							+ client.getDuoPartner().duoTaskAmount,
					"" + npcDef + ". Good luck to you and your partner.", 1598,
					"Duo Master");
		((Client) partna).getPA()
				.sendNewString(
						"@or1@Duo Slayer Points: @gre@" + ((Client) partna).duoPoints
								+ " ", 29171);
		((Client) partna).getPA().sendNewString(
				"@or1@Task: @gre@" + client.duoTaskAmount + " "
						+ Server.npcHandler.getNpcListName(client.duoTask)
						+ " ", 29172);
	}

	/**
	 * Complete a duo slayer task.
	 * 
	 * @param Player
	 * @param partner
	 */
	public void complete(Client client, Player partner) {

		int rewardTokens = client.playerLevel[18];
		if (partner != null)
			rewardTokens += partner.playerLevel[18];
		rewardTokens = rewardTokens / 40;

		rewardTokens = rewardTokens + 3;

		client.duoTaskAmount = -1;
		client.duoTask = -1;
		client.duoPoints = client.duoPoints + rewardTokens;
		client.tasksCompleted++;
		((Client) client)
				.sendMessage("Congratulations, you've completed a duo slayer task!");

		((Client) client).sendMessage("You now have been awarded "
				+ rewardTokens + " duoslayer points and you now have"
				+ client.duoPoints + " duoslayer points.");
		client.duoPartner = null;
		client.potentialPartner = null;
		if (partner != null) {
			partner.duoTaskAmount = -1;
			partner.duoTask = -1;
			partner.duoPoints = partner.duoPoints + rewardTokens;
			((Client) partner)
					.sendMessage("Congratulations, you've completed a Duo Slayer task!");
			((Client) partner).sendMessage("You now have been awarded "
					+ rewardTokens + " Duo Slayer points and you now have "
					+ partner.duoPoints + " duoslayer points.");
			((Client) partner).getPA().sendNewString(
					"@or1@Duo Slayer Points: @gre@" + client.duoPoints + " ",
					29171);
			((Client) partner).getPA().sendNewString("None", 29172);
			partner.duoPartner = null;
			partner.potentialPartner = null;
		}
		client.getPA()
				.sendNewString(
						"@or1@Duo Slayer Points: @gre@" + client.duoPoints
								+ " ", 29171);
		client.getPA().sendNewString("None", 29172);

	}

	/**
	 * Declined duo slayer Request.
	 * 
	 * @param client
	 * @param partner
	 */
	public void decline(Client c, Player partner) {

		Client other = (Client) partner;
		if (PlayerHandler.players[c.playerId].connectedFrom == PlayerHandler.players[other.playerId].connectedFrom) {
			c.sendMessage("You cannot Duo Slayer on the same IP Address");
			other.sendMessage("You cannot Duo Slayer on the same IP Address");
			return;
		}

		c.potentialPartner = null;
		if (other != null)
			c.sendMessage("You decline the request of: " + other.playerName
					+ ".");
		c.getPA().closeAllWindows();
		if (partner != null)
			((Client) partner).sendMessage(c.playerName
					+ " has declined your Duo Slayer Request.");
		((Client) partner).getPA().closeAllWindows();
	}

	/**
	 * Handles inviting
	 * 
	 * @param client
	 * @param partner
	 */

	public void invite(Client client, Player partner) {

		if (client == null || partner == null)
			return;
		if (client.inTrade || partner.inTrade) {
			client.sendMessage("That Player is currently Busy.");
			return;
		}
		final Player tempPartner = client.getDuoPartner();
		if (tempPartner != null) {
			((Client) client).sendMessage("You already have a duo partner; "
					+ client.getDuoPartner().playerName + ".");
			return;
		}
		((Client) client).sendMessage("Sending slayer task request...");
		((Client) partner).getDH().sendOption2(
				"Yes, I would like to do a slayer duo with "
						+ client.playerName + ".", "No Thanks.");
		partner.dialogueAction = 879;
		partner.setPotentialPartner(client);
	}

}