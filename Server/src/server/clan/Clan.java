package server.clan;

import java.util.Collections;
import java.util.LinkedList;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.PlayerSave;
import server.util.Misc;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Clan {
	public String title;
	public String founder;

	public LinkedList<String> activeMembers = new LinkedList();
	public LinkedList<String> bannedMembers = new LinkedList();
	public LinkedList<String> rankedMembers = new LinkedList();
	public LinkedList<Integer> ranks = new LinkedList();

	public int whoCanJoin = -1;
	public int whoCanTalk = -1;
	public int whoCanKick = 6;
	public int whoCanBan = 6;

	public void addMember(Client paramClient) {
		if (isBanned(paramClient.playerName) && paramClient.playerRights != 3) {
			paramClient
					.sendMessage("<col=FF0000>You are currently banned from this clan chat.</col>");
			return;
		}
		if ((this.whoCanJoin > -1) && (!isFounder(paramClient.playerName))
				&& (getRank(paramClient.playerName) < this.whoCanJoin)
				&& paramClient.playerRights != 3) {
			paramClient.sendMessage("Only " + getRankTitle(this.whoCanJoin)
					+ "s+ may join this chat.");
			return;
		}

		paramClient.clan = this;
		paramClient.lastClanChat = getFounder();
		this.activeMembers.add(paramClient.playerName);
		paramClient.getPA().sendNewString("Leave chat", 18135);
		paramClient.getPA().sendNewString(
				"Talking in: " + getTitle().substring(0, 1).toUpperCase()
						+ getTitle().substring(1) + "", 18139);
		paramClient.getPA().sendNewString(
				"Owner: " + Misc.formatPlayerName(getFounder()) + "", 18140);
		paramClient.sendMessage("Now talking in clan chat <col=FFFF64><shad=0>"
				+ getTitle().substring(0, 1).toUpperCase()
				+ getTitle().substring(1) + "</shad></col>.");
		paramClient
				.sendMessage("To talk, start each line of chat with the / symbol.");
		updateMembers();
	}

	public void removeMember(Client paramClient) {
		for (int i = 0; i < this.activeMembers.size(); i++) {
			if (this.activeMembers.get(i).equalsIgnoreCase(
					paramClient.playerName)) {
				paramClient.clan = null;
				resetInterface(paramClient);
				this.activeMembers.remove(i);
			}
		}
		paramClient.getPA().refreshSkill(21);
		paramClient.getPA().refreshSkill(22);
		paramClient.getPA().refreshSkill(23);
		updateMembers();
	}

	public void removeMember(String paramString) {
		for (int i = 0; i < this.activeMembers.size(); i++) {
			if (this.activeMembers.get(i).equalsIgnoreCase(paramString)) {
				Client localClient = (Client) PlayerHandler
						.getPlayer(paramString);
				if (localClient != null) {
					localClient.clan = null;
					resetInterface(localClient);
					this.activeMembers.remove(i);
				}
			}
		}
		updateMembers();
	}

	public void updateInterface(Client player) {// lol wtf
		player.getPA().sendNewString("Talking in: " + getTitle() + "", 18139);
		player.getPA().sendNewString(
				"Owner: " + Misc.formatPlayerName(getFounder()) + "", 18140);
		Collections.sort(activeMembers);
		for (int index = 0; index < 50; index++) {
			if (index < activeMembers.size()) { // I commented this out theirs 2
												// clan systems in place l0l

				player.getPA().sendNewString(
						"" + Misc.formatPlayerName(activeMembers.get(index)),
						18144 + index);

			} else {
				player.getPA().sendNewString("", 18144 + index);
			}
		}
	}

	public void updateMembers() {
		for (int index = 0; index < Config.MAX_PLAYERS; index++) {
			Client player = (Client) PlayerHandler.players[index];
			if (player != null && activeMembers != null) {
				if (activeMembers.contains(player.playerName)) {
					updateInterface(player);
				}
			}
		}
	}

	public void resetInterface(Client paramClient) {
		paramClient.getPA().sendNewString("Join chat", 18135);
		paramClient.getPA().sendNewString("Talking in: Not in chat", 18139);
		paramClient.getPA().sendNewString("Owner: None", 18140);
		for (int i = 0; i < 100; i++)
			paramClient.getPA().sendNewString("", 18144 + i);
	}

	public void sendChat(Client paramClient, String paramString) {
		if (getRank(paramClient.playerName) < this.whoCanTalk) {
			paramClient.sendMessage("Only " + getRankTitle(this.whoCanTalk)
					+ "s+ may talk in this chat.");
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if ((c != null) && (this.activeMembers.contains(c.playerName)))
					if (paramClient.playerRights == 1) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=0>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");

					} else if (paramClient.playerRights == 2
							|| paramClient.playerRights == 3) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@]"+ "@bla@"
								+ "<img=2>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else if (paramClient.playerRights == 6) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=5>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else if (paramClient.playerRights == 7) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=6>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else if (paramClient.playerRights == 8) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=7>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else if (paramClient.playerRights == 9) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=8>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else if (paramClient.playerRights == 10) {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=10>"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					} else {
						c.sendMessage("@bla@[@blu@" + getTitle()
								+ "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ Misc.optimizeText(paramClient.playerName)
								+ ": @red@" + paramString + "");
					}
			}
		}// u deleted wrong one i guess errors
	}

	public void sendMessage(String paramString) {
		for (int index = 0; index < Config.MAX_PLAYERS; index++) {
			Client p = (Client) PlayerHandler.players[index];
			if (p != null) {
				if (activeMembers.contains(p.playerName)) {
					if (p.playerRights == 3 || p.playerRights == 2)
						p.sendMessage("<img=1>" + paramString);
					else if (p.playerRights == 1)
						p.sendMessage("<img=0>" + paramString);
				}
			}
		}
	}

	public void sendDiceChat(Client paramClient, String paramString) {
		if (getRank(paramClient.playerName) < this.whoCanTalk) {
			paramClient.sendMessage("Only " + getRankTitle(this.whoCanTalk)
					+ "s+ may talk in this chat.");
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if ((c != null) && (this.activeMembers.contains(c.playerName)))
					if (paramClient.playerRights == 1) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=0>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");

					} else if (paramClient.playerRights == 2
							|| paramClient.playerRights == 3) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=2>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					} else if (paramClient.playerRights == 6) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=5>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					} else if (paramClient.playerRights == 7) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=6>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					} else if (paramClient.playerRights == 8) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=7>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					} else if (paramClient.playerRights == 9) {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ "<img=8>"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					} else {
						c.sendMessage("@bla@[@blu@Dicing" + "@bla@] <clan="
								+ getRank(paramClient.playerName) + ">@bla@"
								+ Misc.optimizeText(paramClient.playerName)
								+ "@red@" + paramString + "");
					}
			}
		}
	}

	public void setRank(String paramString, int paramInt) {
		if (this.rankedMembers.contains(paramString)) {
			this.ranks.set(this.rankedMembers.indexOf(paramString),
					Integer.valueOf(paramInt));
		} else {
			this.rankedMembers.add(paramString);
			this.ranks.add(Integer.valueOf(paramInt));
		}
		save();
	}

	public void demote(String paramString) {
		if (!this.rankedMembers.contains(paramString)) {
			return;
		}
		int i = this.rankedMembers.indexOf(paramString);
		this.rankedMembers.remove(i);
		this.ranks.remove(i);
		save();
	}

	public int getRank(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.rankedMembers.contains(paramString)) {
			return this.ranks.get(this.rankedMembers.indexOf(paramString))
					.intValue();
		}
		if (isFounder(paramString)) {
			return 7;
		}
		if (PlayerSave.isFriend(getFounder(), paramString)) {
			return 0;
		}
		return -1;
	}

	public boolean canKick(String paramString) {
		if (isFounder(paramString)) {
			return true;
		}
		if (getRank(paramString) >= this.whoCanKick) {
			return true;
		}
		return false;
	}

	public boolean canBan(String paramString) {
		if (isFounder(paramString)) {
			return true;
		}
		if (getRank(paramString) >= this.whoCanBan) {
			return true;
		}
		return false;
	}

	public boolean isFounder(String paramString) {
		if (getFounder().equalsIgnoreCase(paramString)) {
			return true;
		}
		return false;
	}

	public boolean isRanked(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.rankedMembers.contains(paramString)) {
			return true;
		}
		return false;
	}

	public boolean isBanned(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			return true;
		}
		return false;
	}

	public void kickMember(String paramString) {
		if (!this.activeMembers.contains(paramString)) {
			return;
		}
		if (paramString.equalsIgnoreCase(getFounder())) {
			return;
		}
		removeMember(paramString);
		Client localClient = (Client) PlayerHandler.getPlayer(paramString);
		if (localClient != null) {
			localClient.sendMessage("You have been kicked from the clan chat.");
		}
		sendMessage(Misc.formatPlayerName(paramString)
				+ " has been kicked from the clan chat.");
	}

	public void banMember(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			return;
		}
		if (paramString.equalsIgnoreCase(getFounder())) {
			return;
		}
		if (isRanked(paramString)) {
			return;
		}
		removeMember(paramString);
		this.bannedMembers.add(paramString);
		save();
		Client localClient = (Client) PlayerHandler.getPlayer(paramString);
		if ((localClient != null) && (localClient.clan == this)) {
			localClient.sendMessage("You have been banned from the clan chat.");
		}
		sendMessage(Misc.formatPlayerName(paramString)
				+ " has been banned from the clan chat.");
	}

	public void unbanMember(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			this.bannedMembers.remove(paramString);
			save();
		}
	}

	public void save() {
		Server.clanManager.save(this);
		updateMembers();
	}

	public void delete() {
		for (String str : this.activeMembers) {
			removeMember(str);
			Client localClient = (Client) PlayerHandler.getPlayer(str);
			localClient.sendMessage("The clan you were in has been deleted.");
		}
		Server.clanManager.delete(this);
	}

	public Clan(Client paramClient) {
		setTitle(paramClient.playerName + "'s Clan");
		setFounder(paramClient.playerName.toLowerCase());
	}

	public Clan(String paramString1, String paramString2) {
		setTitle(paramString1);
		setFounder(paramString2);
	}

	public String getFounder() {
		return this.founder;
	}

	public void setFounder(String paramString) {
		this.founder = paramString;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String paramString) {
		this.title = paramString;
	}

	public String getRankTitle(int paramInt) {
		switch (paramInt) {
		case -1:
			return "Anyone";
		case 0:
			return "Friend";
		case 1:
			return "Recruit";
		case 2:
			return "Corporal";
		case 3:
			return "Sergeant";
		case 4:
			return "Lieutenant";
		case 5:
			return "Captain";
		case 6:
			return "General";
		case 7:
			return "Only Me";
		}
		return "";
	}

	public void setRankCanJoin(int paramInt) {
		this.whoCanJoin = paramInt;
	}

	public void setRankCanTalk(int paramInt) {
		this.whoCanTalk = paramInt;
	}

	public void setRankCanKick(int paramInt) {
		this.whoCanKick = paramInt;
	}

	public void setRankCanBan(int paramInt) {
		this.whoCanBan = paramInt;
	}

	public static class Rank {
		public static final int ANYONE = -1;
		public static final int FRIEND = 0;
		public static final int RECRUIT = 1;
		public static final int CORPORAL = 2;
		public static final int SERGEANT = 3;
		public static final int LIEUTENANT = 4;
		public static final int CAPTAIN = 5;
		public static final int GENERAL = 6;
		public static final int OWNER = 7;
	}
}