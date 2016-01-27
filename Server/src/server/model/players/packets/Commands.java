package server.model.players.packets;

import java.io.IOException;

import server.Config;
import server.Connection;
import server.Server;
import server.core.PlayerHandler;
import server.core.World;
import server.model.Location;
import server.model.Projectile;
import server.model.content.Achievements;
import server.model.content.Decanting;
import server.model.content.Donation;
import server.model.content.Lottery;
import server.model.npcs.NPCHandler;
import server.model.npcs.NPCLootTable;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.Player;
import server.model.players.PlayerSave;
import server.model.players.TradingPost;
import server.region.Region;
import server.tick.Tickable;
import server.util.Misc;

public class Commands implements PacketType {
	
	public static int randomItem[] = { 4151, 1050, 6570, 11832, 11834, 1042, 3062, 15004, 11862, 12954, 12939, 13179, 13178, 13177, 13181, 13197, 13199, 13120, 989, 10398, 6570, 6573, 6575, 6577, 6581, 6733, 6735, 6737, 6914, 6916, 6918, 6920, 6922, 6924, 3062 };

		public static int randomStarterItem() {
			return randomItem[(int) (Math.random() * randomItem.length)];
		}

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();
		playerCommand = Misc.getFilteredInput(playerCommand);
		Misc.println(c.playerName + " playerCommand: " + playerCommand);
		if (c.duelStatus > 0) {
			c.sendMessage("You can't do that command here!");
			return;
		}
		if (c.playerRights >= 1 && c.playerRights <= 3
				&& c.isModerator(c.playerName)) {// 1
			moderatorCommands(c, playerCommand);
		}
		if (c.playerRights == 2) { // 2
			adminCommands(c, playerCommand);
		}
		if (c.playerRights == 3 || c.playerName.equalsIgnoreCase("") || c.playerName.equalsIgnoreCase("goten") || c.playerName.equalsIgnoreCase("michael")) { // 3
			ownerCommands(c, playerCommand);
		}
		if (c.playerRights == 6) {
			donatorCommands(c, playerCommand);
		}
		if (c.playerRights == 7 && c.isHelper(c.playerName)) {
			helperCommands(c, playerCommand);
		}
		if (c.playerRights == 8 && c.isTrusted(c.playerName)) {
			trustedCommands(c, playerCommand);
		}
		if (c.playerRights == 9) {
			veteranCommands(c, playerCommand);
		}
		if (c.playerRights == 10 && c.ironDonator(c.playerName)) {
			ironDonatorCommands(c, playerCommand);
		}
		if (c.playerRights == 3) {
			if (playerCommand.equalsIgnoreCase("special")) {
				c.specAmount += 10;
				c.attackTimer = 0;
			}
		}
		playerCommands(c, playerCommand);
		/*
		 * if (Config.IS_BETA && !c.isStaff()) { if
		 * (playerCommand.startsWith("item")) { try { if (c.inClanWarsGame)
		 * return; String[] args = playerCommand.split(" "); if (args.length ==
		 * 3) { int newItemID = Integer.parseInt(args[1]);// item ID int
		 * newItemAmount = Integer.parseInt(args[2]);// ITEM // AMOUNT if
		 * ((newItemID <= 25000) && (newItemID >= 0)) {
		 * c.getItems().addItem(newItemID, newItemAmount);
		 * System.out.println("Spawned: " + newItemID + " by: " + c.playerName);
		 * } else { c.sendMessage("No such item."); } } else {
		 * c.sendMessage("Use as ::item 995 200"); } } catch (Exception e) { } }
		 * if (playerCommand.startsWith("master") && !c.inWild()) { for (int i =
		 * 0; i < 22; i++) { c.playerLevel[i] = 99; c.playerXP[i] =
		 * c.getPA().getXPForLevel(100); c.getPA().refreshSkill(i); }
		 * c.getPA().requestUpdates(); } }
		 */
	}

	public static void ironDonatorCommands(Client c, String playerCommand) {
		if (playerCommand.startsWith("yell") && c.playerRights == 10) {
			String rank = "[@blu@<img=6>Iron Donator@bla@][@blu@"
					+ c.playerName + "@bla@]:@dre@";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					String text = playerCommand.substring(5);
					String[] yellreq = { "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (text.indexOf(yellreq[i]) >= 0) {
							PlayerHandler.players[i].disconnected = true;
							return;
						}
					}
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
		if (playerCommand.equals("donatorcave")) {
			c.sendMessage("Use the ladder at Donator Zone!");
		}
		if (playerCommand.equalsIgnoreCase("spells")) {
			if (c.inWild() || c.inPvP()) {
				c.sendMessage("You can't use this in the Wilderness");
				return;
			}
			if (c.playerMagicBook == 2) {
				c.sendMessage("You switch to modern magic.");
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			} else if (c.playerMagicBook == 0) {
				c.sendMessage("You switch to ancient magic.");
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			}
		}
	}

	public static void helperCommands(Client c, String playerCommand) {
		if (c.playerName.contains("artz") || c.playerName.contains("")
				|| c.playerName.contains("")
				|| c.playerName.contains("")
				|| c.playerName.contains("") || c.playerName.contains("") || c.playerName.contains("Mercy")) {
			donatorCommands(c, playerCommand);
		}
		if (playerCommand.startsWith("unbancc")) {
			String[] playerToMod = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerToMod[1])) {
						Client c2 = (Client) PlayerHandler.players[i];
						c.clan.unbanMember(c2.playerName);
						c.sendMessage("Successfully unbanned " + c2.playerName);
					}
				}
			}
		}
		if (playerCommand.startsWith("fixinv")) {
			c.getItems().resetItems(3214);
			c.getPA().viewingOther = false;
		}
		if (playerCommand.equalsIgnoreCase("spells")
				&& !c.playerName.contains("Ranged it")) {
			if (c.inWild() || c.inPvP()) {
				c.sendMessage("You can't use this in the Wilderness");
				return;
			}
			if (c.playerMagicBook == 2) {
				c.sendMessage("You switch to modern magic.");
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			} else if (c.playerMagicBook == 0) {
				c.sendMessage("You switch to ancient magic.");
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			}
		}
		if (playerCommand.equals("staffzone")) {
			c.getPA().startTeleport(2912, 5475, 0, "modern");
		}
		if (playerCommand.startsWith("checkinv")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(args[1])) {
							c.getPA().otherInv(c, o);
							c.getPA().viewingOther = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("checkbank")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(args[1])) {
							c.getPA().viewOtherBank(c, o);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("yell") && c.playerRights == 7) {
			String rank = "[@blu@<img=6>Helper@bla@][@blu@" + c.playerName
					+ "@bla@]:@dre@";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					String text = playerCommand.substring(5);
					String[] yellreq = { "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (text.indexOf(yellreq[i]) >= 0) {
							PlayerHandler.players[i].disconnected = true;
							return;
						}
					}
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
	}

	public static void veteranCommands(Client c, String playerCommand) {
		donatorCommands(c, playerCommand);
		if (playerCommand.startsWith("yell") && c.playerRights == 9) {
			String rank = "[@blu@<img=8>Veteran@bla@][@blu@" + c.playerName
					+ "@bla@]:@dre@";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					String text = playerCommand.substring(5);
					String[] yellreq = { "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (text.indexOf(yellreq[i]) >= 0) {
							PlayerHandler.players[i].disconnected = true;
							return;
						}
					}
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
	}

	public static void trustedCommands(Client c, String playerCommand) {
		donatorCommands(c, playerCommand);
		if (playerCommand.startsWith("yell") && c.playerRights == 8) {
			String rank = "[@blu@<img=7>Trusted@bla@][@blu@" + c.playerName
					+ "@bla@]:@dre@";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					String text = playerCommand.substring(5);
					String[] yellreq = { "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (text.indexOf(yellreq[i]) >= 0) {
							PlayerHandler.players[i].disconnected = true;
							return;
						}
					}
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
		if (playerCommand.equalsIgnoreCase("spells")) {
			if (c.inWild() || c.inPvP()) {
				c.sendMessage("You can't use this in the Wilderness");
				return;
			}
			if (c.playerMagicBook == 2) {
				c.sendMessage("You switch to modern magic.");
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			} else if (c.playerMagicBook == 0) {
				c.sendMessage("You switch to ancient magic.");
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			}
		}
	}

	public static void donatorCommands(Client c, String playerCommand) {
		if (playerCommand.equalsIgnoreCase("dz")) {
			c.getPA().startTeleport(2889, 3511, 0, "modern");
		}
		if (playerCommand.equalsIgnoreCase("spells") && c.playerRights == 6) {
			if (c.inWild() || c.inPvP()) {
				c.sendMessage("You can't use this in the Wilderness");
				return;
			}
			if (c.playerMagicBook == 2) {
				c.sendMessage("You switch to modern magic.");
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			} else if (c.playerMagicBook == 0) {
				c.sendMessage("You switch to ancient magic.");
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			}
		}
		if (playerCommand.startsWith("yell") && c.playerRights == 6) {
			String rank = "[@yel@Gold Donator@bla@][@blu@" + c.playerName
					+ "@bla@]:@dre@";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					String text = playerCommand.substring(5);
					String[] yellreq = { "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (text.indexOf(yellreq[i]) >= 0) {
							PlayerHandler.players[i].disconnected = true;
							return;
						}
					}
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
	}

	public static void ownerCommands(final Client c, String playerCommand) {
		testCommands(c, playerCommand);
		donatorCommands(c, playerCommand);
		if (playerCommand.startsWith("price")) {
			String[] itemId = playerCommand.split(" ");
			c.sendMessage(""
					+ Server.itemHandler.ItemList[Integer.parseInt(itemId[1])].PriceValue);
		}
		
		if (playerCommand.startsWith("mp")) {
			c.getPA().sendFrame126("" + 10000, 8135);
		}

		if (playerCommand.startsWith("item")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= Config.ITEM_LIMIT) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
					} else {
						c.sendMessage("That item ID does not exist.");
					}
				} else {
					c.sendMessage("Correct usage: [::item 995 1]");
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		if (playerCommand.startsWith("tele")) {
			String[] arg = playerCommand.split(" ");
			if (arg.length > 3) {
				c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			} else if (arg.length == 3) {
				c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
			}
		}
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("noclip")) {// TODO: Finish implementing.
			c.getPA().clipped = !c.getPA().clipped;
			System.out.println(c.getPA().clipped);
		}
		if (playerCommand.startsWith("checkuid")) {
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					c.sendMessage(PlayerHandler.players[i].playerName + " "
							+ PlayerHandler.players[i].UUID);
				}
			}
		}
		if (playerCommand.startsWith("giveiron")) {
			String playerToKick = playerCommand.substring(9);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerToKick)) {
						Client player = (Client) PlayerHandler.players[i];
						player.flushIronDonators(player.playerName);
					}
				}
			}
		}
		if (playerCommand.startsWith("reloaddonors")) {
			Server.loadIronDonators();
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (Server.ironDonatorMap
							.contains(PlayerHandler.players[i].playerName)) {
						c.ironDonator = true;
					}
				}
			}
		}
		/*
		 * Owner commands
		 */
		
		if (playerCommand.startsWith("bonus")) {
			for (int i = 0; i < c.playerBonus.length; i++) {
				c.playerBonus[i] = 99999;
				c.sendMessage("Enabled");
			}
		}

		if (playerCommand.startsWith("zu")) {
			//Zulrah.init(c);
		}
		if (playerCommand.startsWith("venom")) {
			c.venomDebuff = true;
			c.getPA().appendVenom();
		}
		if (playerCommand.startsWith("zulrahplayers")) {
			int count = 0;
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				Client player = (Client) PlayerHandler.players[i];
				if (player != null) {
					if (player.getPA().inZulrah()) {
						count++;
					}
				}
			}
			c.sendMessage("There are " + count + " players at Zulrah.");
		}
		if (playerCommand.startsWith("bgfx")) {// you could take an input range,
												// start finish this works xd
			Tickable tick = new Tickable(1) {
				int i = 200;

				@Override
				public void execute() {
					if (i < 400) {
						c.gfx0(i);
						System.out.println(i);
						i++;
					} else {
						stop();
					}
				}

			};
			tick.setTickDelay(3);
			World.getWorld().submit(tick);
		}
		if (playerCommand.startsWith("agfx")) {
			Tickable tick = new Tickable(1) {
				int i = 200;

				@Override
				public void execute() {
					if (i < 1400) {
						c.gfx0(i);
						c.sendMessage("GFX: " + i);
						i++;
					} else {
						stop();
					}
				}

			};
			tick.setTickDelay(2);
			World.getWorld().submit(tick);
		}

		if (playerCommand.startsWith("projectiles")) {
			Tickable tick = new Tickable(3) {
				int i = 80;

				@Override
				public void execute() {
					if (i < 600) {
						c.getPA().createProjectile(
								Projectile.create(i, c, Location.create(c.absX,
										c.absY - 3, c.heightLevel), 50, 100,
										60, 0, 65));
						System.out.println("Projectile: " + i);
						c.sendMessage("Projectile: " + i);
						i++;
					} else {
						stop();
					}
				}

			};
			World.getWorld().submit(tick);
		}

		if (playerCommand.startsWith("projectile")) {
			String[] args = playerCommand.split(" ");
			c.getPA().createProjectile(
					Projectile.create(Integer.parseInt(args[1]), c,
							Location.create(c.absX, c.absY - 1, c.heightLevel),
							50, 100, 60, 31, 65));
		}
		if (playerCommand.startsWith("aanim")) {
			Tickable tick = new Tickable(1) {
				int i = 5000;

				@Override
				public void execute() {
					if (i < 5991) {
						c.startAnimation(i);
						System.out.println(i);
						i++;
					} else {
						stop();
					}
				}

			};
			tick.setTickDelay(2);
			World.getWorld().submit(tick);
		}
		if (playerCommand.startsWith("clip")) {
			for (int x = -16; x <= 16; x++) {
				for (int y = -16; y <= 16; y++) {
					if (Region.getClipping(c.absX + x, c.absY + y, 0) != 0) {
						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Client c2 = (Client) PlayerHandler.players[j];
								Server.itemHandler.createGroundItem(c2, 995,
										c.absX + x, c.absY + y, 1, c2.playerId);
							}
						}
					}
				}
			}
		}
		if (playerCommand.startsWith("banim")) {
			Tickable tick = new Tickable(1) {
				int i = 7060;

				@Override
				public void execute() {
					if (i < 7200) {
						c.startAnimation(i);
						System.out.println(i);
						i++;
					}
				}

			};
			tick.setTickDelay(2);
			World.getWorld().submit(tick);
		}
		if (playerCommand.startsWith("xteleto")) {
			String[] name = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name[1])) {
						c.getPA().movePlayer(PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}

		if (playerCommand.startsWith("clip")) {
			c.sendMessage("Flags: "
					+ Region.getClipping(c.absX, c.absY, c.heightLevel));
		}

		if (playerCommand.startsWith("reloaddrops")) {
			NPCLootTable.load();
			c.sendMessage("Drops Reloaded Successfully!");
		}
		if (playerCommand.startsWith("fade")) {
			c.getPA().sendScreenFade("Test", 1, 15);
		}
		if (playerCommand.startsWith("pnpc")) {
			String args = playerCommand.substring(5);
			c.npcId2 = Integer.parseInt(args);
			c.isNpc = true;
			c.appearanceUpdateRequired = true;
			c.updateRequired = true;
		}
		if (playerCommand.startsWith("unpnpc")) {
			c.npcId2 = -1;
			c.isNpc = false;
			c.appearanceUpdateRequired = true;
			c.updateRequired = true;
		}
		if (playerCommand.startsWith("join")) {
			try {
				Lottery.enterLottery(c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("win")) {
			try {
				Lottery.pickWinner(c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("gijoe")) { // use as
			// ::kick
			// name
			try {
				String playerToKick = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToKick)) {
							Client player = (Client) PlayerHandler.players[i];
							player.herbsFarmed = 190;
							player.gloriesCrafted = 190;
							player.sharksFished = 490;
							player.bowsMade = 190;
							player.magicsBurned = 190;
							player.magicsCut = 190;
							player.callistoKills = 50;
							player.seaSnakeKills = 50;
							player.bandosKills = 50;
							player.zammyKills = 50;
							player.saraKills = 50;
							player.armaKills = 50;
							player.primeKills = 50;
							player.supremeKills = 50;
							player.rexKills = 50;
							player.kbdKills = 50;
							player.seatrollKills = 50;
							player.kalphiteKills = 50;
							player.venenatisKills = 50;
							player.vetionKills = 50;
							player.smokeKills = 50;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("@red@Use as ::kick name");
			}
		}
		if (playerCommand.startsWith("gibro")) {
			try {
				String playerToKick = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToKick)) {
							Client player = (Client) PlayerHandler.players[i];
							player.duoSlayerTasksCompleted = 29;
							player.slayerTasksCompleted = 49;
						}
					}
				}
			} catch (Exception e) {

			}
		}
		if (playerCommand.startsWith("gino")) {
			try {
				String playerToKick = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToKick)) {
							Client player = (Client) PlayerHandler.players[i];
							player.taskAmount = 1;
							player.duoTaskAmount = 1;
						}
					}
				}
			} catch (Exception e) {

			}
		}
		if (playerCommand.startsWith("gihoe")) { // use as
			// ::kick
			// name
			try {
				String playerToKick = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToKick)) {
							Client player = (Client) PlayerHandler.players[i];
							player.herbsFarmed = 1990;
							player.duoSlayerTasksCompleted = 29;
							player.slayerTasksCompleted = 49;
							player.gloriesCrafted = 1990;
							player.sharksFished = 1990;
							player.bowsMade = 1990;
							player.magicsBurned = 1990;
							player.magicsCut = 1990;
							player.callistoKills = 1000;
							player.seaSnakeKills = 1000;
							player.bandosKills = 1000;
							player.zammyKills = 1000;
							player.saraKills = 1000;
							player.armaKills = 1000;
							player.primeKills = 1000;
							player.supremeKills = 1000;
							player.rexKills = 1000;
							player.kbdKills = 1000;
							player.seatrollKills = 1000;
							player.kalphiteKills = 1000;
							player.venenatisKills = 1000;
							player.vetionKills = 1000;
							player.smokeKills = 1000;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("@red@Use as ::kick name");
			}
		}
		if (playerCommand.startsWith("achieve")) {
			c.sendMessage("You have fished a total of " + c.sharksFished + "/"
					+ Achievements.easySharks + " Sharks.");
		}
		if (playerCommand.startsWith("uidban")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)
								&& PlayerHandler.players[i].playerRights != 3) {
							Connection
									.addUidToBanList(PlayerHandler.players[i].UUID);
							Connection
									.addUidToFile(PlayerHandler.players[i].UUID);
							Connection.addNameToBanList(playerToBan);
							Connection.addNameToFile(playerToBan);
							if (c.playerRights == 3) {
								c.sendMessage("@red@["
										+ PlayerHandler.players[i].playerName
										+ "] has been UUID Banned with the UUID: "
										+ PlayerHandler.players[i].UUID);
							} else {
								c.sendMessage("@red@["
										+ PlayerHandler.players[i].playerName
										+ "] has been UUID Banned.");
							}
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		if (playerCommand.startsWith("unuidban")) {
			String player = playerCommand.substring(9);
			//Connection.getUidForUser(c, player);
		}
		if (playerCommand.startsWith("reloadbans")) {
			Connection.initialize();
			c.sendMessage("Bans Reloaded.");
		}
		if (playerCommand.startsWith("macban")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Connection
									.addMacToBanList(PlayerHandler.players[i].MAC);
							Connection
									.addMacToFile(PlayerHandler.players[i].MAC);
							Connection
									.addIpToBanList(PlayerHandler.players[i].connectedFrom);
							Connection
									.addIpToFile(PlayerHandler.players[i].connectedFrom);
							Connection.addNameToBanList(playerToBan);
							Connection.addNameToFile(playerToBan);
							if (c.playerRights == 3) {
								c.sendMessage("@red@["
										+ PlayerHandler.players[i].playerName
										+ "] has been Mac Banned with the MAC: "
										+ PlayerHandler.players[i].MAC);
							} else {
								c.sendMessage("@red@["
										+ PlayerHandler.players[i].playerName
										+ "] has been MAC Banned.");
							}
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		if (playerCommand.startsWith("unmacban")) {
			String player = playerCommand.substring(9);
			//Connection.getMacForUser(c, player);
		}
		if (playerCommand.startsWith("yell") && c.playerRights == 3) {
			String rank = "";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			/* Donators */
			if (c.playerRights == 4) {

				rank = "[@dre@Bronze Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 5) {

				rank = "[@whi@Silver Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 6) {

				rank = "[@yel@Gold Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			/* Staff */
			if (c.playerRights == 1) {
				if (c.playerName.equals("austin")) {
					rank = "[<img=0>@cya@Global Mod@bla@][@cya@" + c.playerName
							+ "@bla@]:@dre@";
				} else {
					rank = "[@blu@Moderator@bla@][@blu@" + c.playerName
							+ "@bla@]:@dre@";
				}
			}
			if (c.playerRights == 1) {
				if (c.playerName.equals("pressure")) {
					rank = "[<img=0>@cya@Global Mod@bla@][@cya@" + c.playerName
							+ "@bla@]:@dre@";
				} else {
					rank = "[@blu@Moderator@bla@][@blu@" + c.playerName
							+ "@bla@]:@dre@";
				}
			}
			if (c.playerRights == 2) {

				rank = "[@or3@Administrator@bla@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			if (c.playerRights == 3) {
				rank = "[@red@CEO & Developer@bla@] @cr2@"
						+ Misc.ucFirst(c.playerName) + ":@dre@";
			}
			if (c.playerName.equalsIgnoreCase("")) {
				rank = "[@red@Developer@bla@] @cr2@"
						+ Misc.ucFirst(c.playerName) + ":@dre@";
			}
			if (c.playerName.equalsIgnoreCase("")) {

				rank = "[@cr2@@blu@Developer@bla@@cr2@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			if (c.playerName.equalsIgnoreCase("michael") || (c.playerName.equalsIgnoreCase("goten"))) {

				rank = "[@mag@<shad=1>@cr2@"
						+ Misc.ucFirst(c.playerName) + "</shad>@bla@]:<shad=0>@mag@";
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
		if (playerCommand.startsWith("derank")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.clan.setRank(c2.playerName, -1);
							c.sendMessage("Successfully gave Rank");
							c2.sendMessage("Ranked Up");
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("highlight")) {
			c.getPA().setInterfaceVisible(1179, false);
		}
	
		if (playerCommand.startsWith("search")) {
			String item = playerCommand.substring(7).toLowerCase();
			c.getPA().searchBank(item);
		}
		if (playerCommand.startsWith("saveclan")) {
			Server.clanManager.save(c.clan);
		}
		if (playerCommand.startsWith("rank")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 5);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.clan.setRank(c2.playerName, 6);
							c.sendMessage("Successfully gave Rank");
							c2.sendMessage("Ranked Up");
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("testt")) {
			c.getItems().addItem(4151, 10, 1);
		}
		if (playerCommand.startsWith("god")) {
			c.playerLevel[3] = 10000;
			c.getPA().refreshSkill(3);
			c.sendMessage("GodMode Enabled");
		}
		if (playerCommand.startsWith("run")) {
			c.getPA().sendNewString("%50%", 149);
		}
		if (playerCommand.startsWith("spe")) {
			c.getPA().sendConfig(22004, 0);
			c.getPA().sendConfig(22005, 0);
			c.getPA().sendConfig(22006, 0);
			c.getPA().sendConfig(22007, 0);
		}
		if (playerCommand.startsWith("decant")) {
			Decanting.startDecanting(c);
		}
		if (playerCommand.startsWith("clan")) {
			c.clan.setTitle("Help CC");
			c.clan.save();
		}
		if (playerCommand.startsWith("newbank")) {
			c.getPA().sendFrame36(1011, 1);
			c.getPA().sendFrame36(1009, 1);
			c.getPA().sendFrame36(1010, 0);
			c.getPA().showInterface(5292);
		}
		if (playerCommand.startsWith("glow")) {
			String[] integer = playerCommand.split(" ", 2);
			c.getPA().sendFrame36(Integer.parseInt(integer[1]), 1);
		}
		if (playerCommand.startsWith("resetpin")) {
			c.bankPin = "";
			c.setPin = false;
			c.disconnected = true;
		}
		if (playerCommand.equalsIgnoreCase("i")) {
			c.npcId2 = 2004;
			c.isNpc = true;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
			c.sendMessage("Rogue mode activated!");
		}
		if (playerCommand.equalsIgnoreCase("u")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.sendMessage("Rogue mode disabled!");
		}
		if (playerCommand.startsWith("givehelp")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							c.sendMessage("Working");
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given helper status by "
									+ c.playerName);
							c2.playerRights = 7; // k
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("givelvl")) {
			String a[] = playerCommand.split("_");
			System.out.println(a.length);
			if (a.length == 4) {
				String playerName = a[1];
				int playerSkill = Integer.parseInt(a[2]);
				int playerLevel = Integer.parseInt(a[3]);
				System.out.println("Skill: " + playerSkill + " Level: "
						+ playerLevel);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerName)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.playerLevel[playerSkill] = playerLevel;
							c2.playerXP[playerSkill] = c.getPA().getXPForLevel(
									playerLevel);
							c.sendMessage("You have given " + c2.playerName
									+ " " + playerLevel);
							c2.sendMessage("You have been given Levels!");
							c2.getPA().refreshSkill(playerSkill);
							c2.getPA().requestUpdates();
						}
					}
				}
			}
		}
		if (playerCommand.startsWith("givexp")) {
			String a[] = playerCommand.split("_");
			System.out.println(a.length);
			if (a.length == 4) {
				String playerName = a[1];
				int playerSkill = Integer.parseInt(a[2]);
				int playerXp = Integer.parseInt(a[3]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerName)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.playerXP[playerSkill] = playerXp;
							c2.playerLevel[playerSkill] = c.getPA()
									.getLevelForXP(playerXp);
							c.sendMessage("You have given " + c2.playerName
									+ " " + playerXp + " XP.");
							c2.sendMessage("You have been given Levels!");
							c2.getPA().refreshSkill(playerSkill);
							c2.getPA().requestUpdates();
						}
					}
				}
			}
		}
		if (playerCommand.startsWith("givetrusted")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							c.sendMessage("Working");
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given trusted status by "
									+ c.playerName);
							c2.playerRights = 8; // k
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("giveextreme")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given extreme status by "
									+ c.playerName);
							c2.playerRights = 5; // k
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("giveveteran")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							c.sendMessage("Working");
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given veteran status by "
									+ c.playerName);
							c2.playerRights = 9; // k
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equals("saveall")) {
			for (Player player : PlayerHandler.players) {
				if (player != null) {
					Client c1 = (Client) player;
					if (PlayerSave.saveGame(c1)) {
						c1.sendMessage("Your character has been saved.");
					}
				}
			}
		}
		if (playerCommand.startsWith("post")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				TradingPost post = new TradingPost(c, Integer.parseInt(args[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("fence")) {
			c.getPA().object(1854, c.absX, c.absY, 0, 0);
		}
		/*
		 * if (playerCommand.startsWith("construct")) { c.getPA().movePlayer(50,
		 * 50, c.playerId*4); final Palette palette = new Palette(); Tile tile =
		 * new Tile(3222, 3222, 0);
		 * 
		 * PaletteTile paletteTile = new PaletteTile(tile.x, tile.y, 0);
		 * palette.setTile(6, 6, 0, paletteTile);
		 * 
		 * for (int j = -3; j < 3; j++) { for (int i = -3; i < 3; i++) {
		 * PaletteTile palTile = new PaletteTile(tile.x + (8 * j), tile.y + (8 *
		 * i), 0); palette.setTile(6 + j, 6 + i, 0, palTile); } }
		 * CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		 * 
		 * @Override public void execute(CycleEventContainer container) {
		 * c.getPA().constructMapRegion(palette);
		 * 
		 * }
		 * 
		 * @Override public void stop() { stop(); }
		 * 
		 * }, 1); }
		 */
		if (playerCommand.startsWith("cwtimer")) {
			String[] args = playerCommand.split(" ", 2);
			Server.clanWars.gameStartTimer = Integer.parseInt(args[1]);
		}
		if (playerCommand.startsWith("sendmeat")) {
			try {
				String playerToBan = playerCommand.substring(9);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) PlayerHandler.players[i];

						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Online.");
			}
		}
		if (playerCommand.startsWith("endcw")) {
			Server.clanWars.endGame();
		}
		if (playerCommand.startsWith("giveslay")) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					String a[] = playerCommand.split("_");
					if (a.length == 3) {
						String playerToGiveItem = a[1];
						int slayerAmount = Integer.parseInt(a[2]);
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToGiveItem)) {
								Client c2 = (Client) PlayerHandler.players[i];
								c2.slayerPoints += slayerAmount;
								c2.sendMessage("You have just been given "
										+ slayerAmount + " Slayer Points: by: "
										+ Misc.optimizeText(c.playerName));
								c.sendMessage("You have just given "
										+ slayerAmount + " Slayer Points to: "
										+ Misc.optimizeText(c2.playerName)
										+ ".");
								return;
							}
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::giveslayer_playerName_amount)(::giveslayer_player_1000))");
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("giveitem")) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					String a[] = playerCommand.split("_");
					if (a.length == 4) {
						String playerToGiveItem = a[1];
						int newItemId = Integer.parseInt(a[2]);
						int newItemAmount = Integer.parseInt(a[3]);
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToGiveItem)) {
								Client c2 = (Client) PlayerHandler.players[i];
								if (c2.getItems().freeSlots() > newItemAmount - 1) {
									c2.getItems().addItem(newItemId,
											newItemAmount);
									c2.sendMessage("You have just been given "
											+ newItemAmount
											+ " of item: "
											+ c2.getItems().getItemName(
													newItemId) + " by: "
											+ Misc.optimizeText(c.playerName));
								} else {
									c2.getItems().addItemToBank(newItemId,
											newItemAmount);
									c2.sendMessage("You have just been given "
											+ newItemAmount
											+ " of item: "
											+ c2.getItems().getItemName(
													newItemId) + " by: "
											+ Misc.optimizeText(c.playerName));
									c2.sendMessage("It is in your bank because you didn't have enough space in your inventory.");
								}
								c.sendMessage("You have just given "
										+ newItemAmount + " of item number: "
										+ c.getItems().getItemName(newItemId)
										+ ".");
								return;
							}
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::giveitem_playerName_itemId_itemAmount)(::giveitem_player_995_1))");
						return;
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("givedp")) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					String a[] = playerCommand.split("_");
					if (a.length == 3) {
						String playerToGiveItem = a[1];
						int newItemId = Integer.parseInt(a[2]);
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToGiveItem)) {
								Client c2 = (Client) PlayerHandler.players[i];
								c2.donPoints += newItemId;
								c2.sendMessage("You have just been given "
										+ newItemId + " donator points by: "
										+ Misc.optimizeText(c.playerName));
								c.sendMessage("You have just given "
										+ c2.playerName + " " + newItemId
										+ " donator points.");
								return;
							}
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::giveitem_playerName_itemId_itemAmount)(::giveitem_player_995_1))");
						return;
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equals("alltome")) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.teleportToX = c.absX;
					c2.teleportToY = c.absY;
					c2.heightLevel = c.heightLevel;
					c2.sendMessage("Mass teleport to: " + c.playerName + "");
				}
			}
		}
		if (playerCommand.startsWith("bank")) {
			c.getPA().openUpBank(0);
		}
		if (playerCommand.startsWith("remove")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				TradingPost post = new TradingPost(c);
				post.removePost();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("double")) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.doubleEXPTicket(3600);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("reloadshops")) {
			Server.shopHandler = new server.world.ShopHandler();
			Server.shopHandler.loadShops("shops.cfg");
		}
		if (playerCommand.startsWith("pos")) {
			String[] arg = playerCommand.split(" ");
			if (arg.length > 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			else if (arg.length == 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), c.heightLevel);
		}
		if (playerCommand.startsWith("object")) {
			String[] args = playerCommand.split(" ");
			c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
		}
		if (playerCommand.startsWith("find")) {
			// Region.getRegions()
		}
		if (playerCommand.startsWith("resetbarrows")) {
			c.barrowsNpcs[0][1] = 0;
			c.barrowsNpcs[1][1] = 0;
			c.barrowsNpcs[2][1] = 0;
			c.barrowsNpcs[3][1] = 0;
			c.barrowsNpcs[5][1] = 0;
			c.barrowsNpcs[4][1] = 0;
			c.barrowsKillCount = 0;
			c.sendMessage("Reset Barrows Brothers and Kill Count");
		}
		if (playerCommand.startsWith("setbarrows")) {
			c.barrowsNpcs[0][1] = 2;
			c.barrowsNpcs[1][1] = 2;
			c.barrowsNpcs[2][1] = 2;
			c.barrowsNpcs[3][1] = 2;
			c.barrowsNpcs[5][1] = 2;
			c.barrowsKillCount = 5;
			c.sendMessage("Set Barrows Brothers and Kill Count");
		}
		if (playerCommand.startsWith("skull")) {
			String username = playerCommand.substring(6);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(username)) {
						PlayerHandler.players[i].isSkulled = true;
						PlayerHandler.players[i].skullTimer = Config.SKULL_TIMER;
						PlayerHandler.players[i].headIconPk = 0;
						PlayerHandler.players[i].teleBlockDelay = System
								.currentTimeMillis();
						PlayerHandler.players[i].teleBlockLength = 300000;
						((Client) PlayerHandler.players[i]).getPA()
								.requestUpdates();
						c.sendMessage("You have skulled "
								+ PlayerHandler.players[i].playerName);
						return;
					}
				}
			}
			c.sendMessage("No such player online.");
		}
		if (playerCommand.startsWith("troll")) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage("The first person who type ::i4akosa9fUcxzij8a will recieve a @red@partyhat set@bla@!");
				}
			}
		}
		if (playerCommand.startsWith("title") && c.playerRights == 3) {
			try {
				final String[] args = playerCommand.split("-");
				c.playerTitle = args[1];
				String color = args[2].toLowerCase();
				if (color.equals("orange"))
					c.titleColor = 0;
				if (color.equals("purple"))
					c.titleColor = 1;
				if (color.equals("red"))
					c.titleColor = 2;
				if (color.equals("green"))
					c.titleColor = 3;
				c.sendMessage("You succesfully changed your title.");
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			} catch (final Exception e) {
				c.sendMessage("Use as ::title-[title]-[color]");
			}
		}
		if (playerCommand.startsWith("i4akosa9fUcxzij8a")) {
			c.getItems().addItem(4012, 1);
			c.sendMessage("@red@You got trolled.");
		}
		if (playerCommand.startsWith("smite")) {
			String targetUsr = playerCommand.substring(6);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(targetUsr)) {
						Client usr = (Client) PlayerHandler.players[i];
						usr.playerLevel[5] = 0;
						usr.getCombat().resetPrayers();
						usr.prayerId = -1;
						usr.getPA().refreshSkill(5);
						c.sendMessage("You have smited " + usr.playerName + "");
						break;
					}
				}
			}
		}
		if (playerCommand.startsWith("setlevel")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99) {
					level = 99;
				} else if (level < 0) {
					level = 1;
				}
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.getPA().levelUp(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("generateclip")) {
			System.out.println("!!");
			for (int x = -32; x <= 32; x++) {
				for (int y = -32; y <= 32; y++) {
					if (Region.getClipping(c.absX + x, c.absY + y, 0) != 0) {

						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Client c2 = (Client) PlayerHandler.players[j];
								Server.itemHandler.createGroundItem(c2, 995,
										c.absX + x, c.absY + y, 1, c2.playerId);
							}
						}
					}
				}
			}
		}
		if (playerCommand.startsWith("convert")) {
			c.replaced = false;
			c.playerEquipment[7] = 11726;
			c.playerEquipmentN[7] = 1;
			World.getWorld().submit(new Tickable(2) {

				@Override
				public void execute() {
					c.replaceFormat();
					c.getItems().resetItems(3214);
					stop();
				}

			});
		}
		if (playerCommand.startsWith("teletome")) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.teleportToX = c.absX;
							c2.teleportToY = c.absY;
							c2.heightLevel = c.heightLevel;
							c.sendMessage("You have teleported "
									+ c2.playerName + " to you.");
							c2.sendMessage("You have been teleported to "
									+ c.playerName + "");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}

		if (playerCommand.startsWith("update")) {
			String[] args = playerCommand.split(" ");
			PlayerHandler.updateSeconds = Integer.parseInt(args[1]);
			PlayerHandler.updateAnnounced = false;
			PlayerHandler.updateRunning = true;
			PlayerHandler.updateStartTime = System.currentTimeMillis();
		}
		if (playerCommand.startsWith("www")) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.getPA().sendNewString(playerCommand, 0);

				}
			}
		}
		if (playerCommand.startsWith("full")) {
			c.getPA().sendNewString(playerCommand, 0);
		}

		if (playerCommand.startsWith("givemod")) {
			try {
				String playerToMod = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given mod status by "
									+ c.playerName);
							c2.playerRights = 1;
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("giveadmin")) {
			try {
				String[] playerToMod = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod[1])) {
							c.sendMessage("Working");
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given mod status by "
									+ c.playerName);
							c2.playerRights = 3; // k
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("demote")) {
			try {
				String playerToDemote = playerCommand.substring(7);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToDemote)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been demoted by "
									+ c.playerName);
							c2.playerRights = 0;
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("query")) {
			try {
				String playerToBan = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							c.sendMessage("IP: "
									+ PlayerHandler.players[i].connectedFrom);

						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
	}

	public static void adminCommands(Client c, String playerCommand) {
		/*
		 * When a admin does a command it goes through all these commands to
		 * find a match
		 */
		moderatorCommands(c, playerCommand);
		if (playerCommand.equals("saveall")) {
			for (Player player : PlayerHandler.players) {
				if (player != null) {
					Client c1 = (Client) player;
					if (PlayerSave.saveGame(c1)) {
						c1.sendMessage("Your character has been saved.");
					}
				}
			}
		}
		if (playerCommand.startsWith("xteleto") && c.playerRights == 2) {
			String[] name = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name[1])) {
						if (c.inWild() || PlayerHandler.players[i].inWild()) {
							c.sendMessage("You cannot Teleport to someone in the Wilderness");
							return;
						}
						c.getPA().movePlayer(PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("checkuid")) {
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					c.sendMessage(PlayerHandler.players[i].playerName + " "
							+ PlayerHandler.players[i].UUID);
				}
			}
		}
		if (playerCommand.startsWith("yell") && c.playerRights == 2) {
			String rank = "";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			/* Donators */
			if (c.playerRights == 4) {

				rank = "[@dre@Bronze Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 5) {

				rank = "[@whi@Silver Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 6) {

				rank = "[@yel@Gold Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			/* Staff */
			if (c.playerRights == 1) {
				if (c.playerName.equals("")) {
					rank = "[<img=0>@cya@Global Mod@bla@][@cya@" + c.playerName
							+ "@bla@]:@dre@";
				} else {
					rank = "[@blu@Moderator@bla@][@blu@" + c.playerName
							+ "@bla@]:@dre@";
				}
			}
			if (c.playerRights == 2) {

				rank = "[@or3@Administrator@bla@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			if (c.playerRights == 3) {
				rank = "[@red@CEO & Developer@bla@] @cr2@"
						+ Misc.ucFirst(c.playerName) + ":@dre@";
			}
			if (c.playerName.equalsIgnoreCase("")) {

				rank = "[@cr2@@blu@Developer@bla@@cr2@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
		if (playerCommand.startsWith("teletome")) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.teleportToX = c.absX;
							c2.teleportToY = c.absY;
							c2.heightLevel = c.heightLevel;
							c.sendMessage("You have teleported "
									+ c2.playerName + " to you.");
							c2.sendMessage("You have been teleported to "
									+ c.playerName + "");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("item")) {
			try {
				if (c.inClanWarsGame)
					return;
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);// item ID
					int newItemAmount = Integer.parseInt(args[2]);// ITEM AMOUNT
					if ((newItemID <= 25000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
			}
		}
	}

	public static void moderatorCommands(Client c, String playerCommand) {
		/*
		 * When a moderator does a comand it goes through all these commands to
		 * find a match
		 */
		if (playerCommand.startsWith("unbancc")) {
			String[] playerToMod = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerToMod[1])) {
						Client c2 = (Client) PlayerHandler.players[i];
						c.clan.unbanMember(c2.playerName);
						c.sendMessage("Successfully unbanned " + c2.playerName);
					}
				}
			}
		}
		if (playerCommand.startsWith("xteleto") && c.playerRights == 1) {
			String[] name = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name[1])) {
						if (c.inWild() || PlayerHandler.players[i].inWild()) {
							c.sendMessage("You cannot Teleport to someone in the Wilderness");
							return;
						}
						c.getPA().movePlayer(PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("fixinv") && c.playerRights >= 1
				&& c.playerRights <= 3) {
			c.getItems().resetItems(3214);
			c.getPA().viewingOther = false;
		}
		if (playerCommand.startsWith("checkinv") && c.playerRights >= 1
				&& c.playerRights <= 3) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(args[1])) {
							if (o.inWild()) {
								c.sendMessage("You cannot view someones inventory in the wild!");
								return;
							}
							c.getPA().otherInv(c, o);
							c.getPA().viewingOther = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("checkbank")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(args[1])) {
							c.getPA().viewOtherBank(c, o);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("yell") && c.playerRights == 1) {
			String rank = "";
			String Message = playerCommand.substring(4);
			if (c.playerRights == 0) {
				c.sendMessage("Do you want access to the yell command? ::donate");
				return;
			}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			/* Donators */
			if (c.playerRights == 4) {

				rank = "[@dre@Bronze Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 5) {

				rank = "[@whi@Silver Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			if (c.playerRights == 6) {

				rank = "[@yel@Gold Donator@bla@][@blu@" + c.playerName
						+ "@bla@]:@dre@";
			}
			/* Staff */
			if (c.playerRights == 1) {
				if (c.playerName.equals("tez")) {
					rank = "[<img=0>@cya@Global Mod@bla@][@cya@" + c.playerName
							+ "@bla@]:@dre@";
				} else {
					rank = "[@blu@Moderator@bla@][@blu@" + c.playerName
							+ "@bla@]:@dre@";
				}
			}
			if (c.playerRights == 2) {

				rank = "[@or3@Administrator@bla@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			if (c.playerRights == 3) {
				rank = "[@red@CEO & Developer@bla@] @cr2@"
						+ Misc.ucFirst(c.playerName) + ":@dre@";
			}
			if (c.playerName.equalsIgnoreCase("")) {

				rank = "[@cr2@@blu@Developer@bla@@cr2@][@blu@"
						+ Misc.ucFirst(c.playerName) + "@bla@]:@dre@";
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage(rank + Message);
				}
			}
		}
		if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				Connection.addNameToBanList(playerToBan[1]);
				Connection.addNameToFile(playerToBan[1]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("jail")) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							if (c2.duelStatus == 5) {
								c.sendMessage("You cant jail a player when he is during a duel.");
								return;
							}
							c2.teleportToX = 2095;
							c2.teleportToY = 4428;
							c2.sendMessage("You have been jailed by "
									+ c.playerName + " .");
							c.sendMessage("Successfully Jailed "
									+ c2.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equalsIgnoreCase("spells")) {
			if (c.inWild() || c.inPvP()) {
				c.sendMessage("You can't use this in the Wilderness");
				return;
			}
			if (c.playerMagicBook == 2) {
				c.sendMessage("You switch to modern magic.");
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			} else if (c.playerMagicBook == 0) {
				c.sendMessage("You switch to ancient magic.");
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			}
		}
		if (playerCommand.startsWith("unjail")) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							if (c2.duelStatus == 5 || c2.inDuelArena()) {
								c.sendMessage("This player is during a duel, and not in jail.");
								return;
							}
							if (c == c2 && c.inWild()) {
								c.sendMessage("You cant unjail yourself in the Wild!");
								return;
							}
							c2.teleportToX = 3093;
							c2.teleportToY = 3493;
							c2.sendMessage("You have been unjailed by "
									+ c.playerName + ". You can now teleport.");
							c.sendMessage("Successfully unjailed "
									+ c2.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {

			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				Connection.addNameToMuteList(playerToBan[1]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unban")) {

			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				Connection.removeNameFromBanList(playerToBan[1]);
				c.sendMessage(playerToBan + " has been unbanned.");
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("ipmute")) {

			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Connection
									.addIpToMuteList(PlayerHandler.players[i].connectedFrom);
							c.sendMessage("You have IP Muted the user: "
									+ PlayerHandler.players[i].playerName);
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unipmute")) {

			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Connection
									.unIPMuteUser(PlayerHandler.players[i].connectedFrom);
							c.sendMessage("You have Un Ip-Muted the user: "
									+ PlayerHandler.players[i].playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
	}

	public static void playerCommands(Client c, String playerCommand) {
		if (playerCommand.startsWith("join")) {
			try {
				Lottery.enterLottery(c);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (playerCommand.startsWith("/")) {
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			if (c.lastYell == 0) {
				if (c.clan != null) {
					String message = playerCommand.substring(1);
					String[] yellreq = { "img=", "@cr", "@blu@", "@red@",
							"@pur@", "@cr1@", "chalreq", "duelreq", "tradereq",
							".com", "www.", "317" };
					for (int i = 0; i < yellreq.length; i++) {
						if (message.indexOf(yellreq[i]) >= 0) {
							return;
						}
					}
					c.clan.sendChat(c, message);
					c.lastYell = 5;
				} else {
					c.sendMessage("You can only do this in a clan chat..");
				}
			}
		}
		
		if (playerCommand.toLowerCase().startsWith("xmas")) {
			 try {
					int randomItem = randomStarterItem();
					if (!Connection.hasReceivedItem(PlayerHandler.players[c.playerId].MAC)) {
						c.getItems().addItem(randomItem, 1);
						c.sendMessage("You received a Christmas Present");
						Connection.addNameToItemList(PlayerHandler.players[c.playerId].MAC);
						Connection.addNametoItemList(PlayerHandler.players[c.playerId].MAC);
					} else if (Connection.hasReceivedItem(PlayerHandler.players[c.playerId].MAC)) {
						c.sendMessage("You have already recieved your free Xmas Present.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		
		if (playerCommand.toLowerCase().startsWith("withdrawmp")) {
			
			try {
				String[] args = playerCommand.split(" ");
				int amount = Integer.parseInt(args[1]);
				c.getPouch().withdrawPouch(amount);
			} catch (Exception e) {
				c.sendMessage("An error has occured.");
			}
			
		}
		
		if (playerCommand.startsWith("addPriceChecker")) {
			if (playerCommand.split(" ") == null)
				return;
			String[] args = playerCommand.split(" ");
			String itemName = args[1];
			int itemId = Integer.parseInt(itemName);
			int amount = 1;
			if (args[0].contains("5"))
				amount = 5;
			else if (args[0].contains("10"))
				amount = 10;
			else if (args[0].contains("All")) {
				amount = c.getItems().getItemAmount(itemId);
			}
			if (itemId < 1) {
				c.sendMessage("Sorry, that item could not be found in our database.");
				return;
			}
			c.getPriceChecker().addItem(itemId, amount);
		}
		/*
		 * Reset levels
		 */
		if (playerCommand.startsWith("resetdef")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 1;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resetatt")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 0;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resetstr")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 2;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resetpray")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 5;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resetrange")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 4;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resetmage")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 6;
				int level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("resethp")) {
			if (c.inWild())
				return;
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please take all your armour and weapons off before using this command.");
					return;
				}
			}
			try {
				int skill = 3;
				int level = 10;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
			}

		}
		if (playerCommand.equalsIgnoreCase("toggle")) {
			if (c.xpLock == true) {
				c.xpLock = false;
			} else {
				c.xpLock = true;
			}
			c.sendMessage("Experiance lock is: " + c.xpLock);
		}
		if (playerCommand.equalsIgnoreCase("skull")) {
			c.isSkulled = true;
			c.skullTimer = Config.SKULL_TIMER;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}
		if (playerCommand.startsWith("xteleto") && c.playerRights == 1) {
			String[] name = playerCommand.split(" ", 2);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name[1])) {
						if (c.inWild() || PlayerHandler.players[i].inWild()) {
							c.sendMessage("You cannot Teleport to someone in the Wilderness");
							return;
						}
						c.getPA().movePlayer(PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("fixinv") && c.playerRights >= 1
				&& c.playerRights <= 3) {
			c.getItems().resetItems(3214);
			c.getPA().viewingOther = false;
		}
		if (playerCommand.startsWith("checkinv") && c.playerRights >= 1
				&& c.playerRights <= 3) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					Client o = (Client) PlayerHandler.players[i];
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(args[1])) {
							if (o.inWild()) {
								c.sendMessage("You cannot view someones inventory in the wild!");
								return;
							}
							c.getPA().otherInv(c, o);
							c.getPA().viewingOther = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute") && c.playerRights == 1) {

			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				Connection.addNameToMuteList(playerToBan[1]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("ban") && c.playerRights == 1) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				Connection.addNameToBanList(playerToBan[1]);
				Connection.addNameToFile(playerToBan[1]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unmute") && c.playerRights == 1) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("jail") && c.playerRights == 1) {
			try {
				String[] playerToBan = playerCommand.split(" ", 2);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan[1])) {
							Client c2 = (Client) PlayerHandler.players[i];
							if (c2.duelStatus == 5) {
								c.sendMessage("You cant jail a player when he is during a duel.");
								return;
							}
							c2.teleportToX = 2095;
							c2.teleportToY = 4428;
							c2.sendMessage("You have been jailed by "
									+ c.playerName + " .");
							c.sendMessage("Successfully Jailed "
									+ c2.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("vote")) {
			c.getPA().sendNewString("www.Dragon-ages.org/vote", 12000);
		}
		if (playerCommand.startsWith("donate")) {
			c.getPA().sendNewString("www.rsps-pay.com/store.php?id=3298&tab=2767", 12000);
		}
		if (playerCommand.equals("draynor")) {
			c.getPA().startTeleport(3114, 3208, 0, "modern");
		}
		if (playerCommand.startsWith("hiscores")) {
			c.getPA().sendNewString("www.hiscores.dragon-ages.org", 12000);
		}
		if (playerCommand.equals("home")) {
			c.randomTeleport();
			//c.getPA().startTeleport(2412, 3803, 0, "modern");
		}
		if (playerCommand.equals("tower")) {
			c.getPA().startTeleport(3424, 3538, 0, "modern");
		}
		if (playerCommand.equals("pvp")) {
			c.getPA().startTeleport(3013, 3356, 0, "modern");
		}
		if (playerCommand.equals("dc")) {
			c.getPA().startTeleport(2956, 3216, 0, "modern");
		}
		if (playerCommand.equals("edge")) {
			c.getPA().startTeleport(3087, 3493, 0, "modern");
		}
		if (playerCommand.equals("infernals")) {
			c.getPA().startTeleport(3302, 9361, 0, "modern");
		}
		if (playerCommand.equals("hands")) {
			// c.getPA().startTeleport(3419, 3537, 0, "modern");
		}
		if (playerCommand.equals("cw")) {
			c.getPA().startTeleport(2444, 3089, 0, "modern");
		}
		if (playerCommand.startsWith("pack") && c.inClanWarsWait) {
			String[] args = playerCommand.split(" ");
			int setup = Integer.parseInt(args[1]);
			if (setup == 1) {
				c.setup = 1;
				c.sendMessage("You have set your pack to the Archers Pack.");
			} else if (setup == 2) {
				c.setup = 2;
				c.sendMessage("You have set your pack to the Mages Pack.");
			} else {
				c.setup = 0;
				c.sendMessage("You have set your pack to the Melee Pack.");
			}

		}
		if (playerCommand.equals("fishing")) {
			c.getPA().startTeleport(2604, 3414, 0, "modern");
		}
		if (playerCommand.equals("mining")) {
			c.getPA().startTeleport(3044, 9792, 0, "modern");
		}
		if (playerCommand.equals("wc")) {
			// c.getPA().startTeleport(2963, 3219, 0, "modern");
			c.sendMessage("Please use the Camelot Teleport and Woodcut Their.");
			c.getPA().removeAllWindows();
		}
		if (playerCommand.equals("dags")) {
			c.getPA().startTeleport(1907, 4367, 0, "modern");
		}
		if (playerCommand.equals("agility")) {
			c.getPA().startTeleport(2469, 3435, 0, "modern");
		}
		if (playerCommand.equals("farming")) {
			c.getPA().spellTeleport(2816, 3463, 0);
			// c.sendMessage("Farming is currently being re-worked.");
		}
		if (playerCommand.equals("crabs")) {
			c.getPA().startTeleport(2679, 3718, 0, "modern");
		}
		if (playerCommand.equals("train")) {
			// c.getPA().startTeleport(2520, 4777, 0, "modern");
			c.getPA().startTeleport(2679, 3718, 0, "modern");
		}
		if (playerCommand.equals("duel")) {
			c.getPA().startTeleport(3369, 3267, 0, "modern");
		}
		if (playerCommand.equals("dicing")) {
			c.getPA().startTeleport(2605, 3093, 0, "modern");
		}
		if (playerCommand.startsWith("master") && c.playerRights >= 2
				&& c.playerRights <= 3) {
			for (int i = 0; i < 22; i++) {
				c.playerLevel[i] = 99;
				c.playerXP[i] = c.getPA().getXPForLevel(100);
				c.getPA().refreshSkill(i);
			}
			c.getPA().requestUpdates();
		}
		if (c.playerRights >= 1 && c.playerRights <= 3) {
			if (playerCommand.equals("staffzone")) {
				c.getPA().startTeleport(2912, 5475, 0, "modern");
			}
		}
		if (playerCommand.startsWith("kick") && c.playerRights >= 1
				&& c.playerRights <= 3) { // use as
			try {
				String playerToKick = playerCommand.substring(5);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToKick)) {
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("@red@Use as ::kick name");
			}
		}
		if (playerCommand.equals("donatorcave")) {
			if (c.playerRights >= 3 && c.playerRights <= 6
					|| c.isTrusted(c.playerName)
					|| c.playerName.equalsIgnoreCase("")) {
				c.sendMessage("Use the ladder at Donator Zone!");
			}
		}
		if (playerCommand.startsWith("blaze") && c.playerRights >= 1
				&& c.playerRights <= 6) {
			c.startAnimation(884);
			c.gfx0(354);
			c.forcedChat("*cough* *cough* that's some good shit.");
		}
		if (playerCommand.equals("timeplayed")) {
			c.getTimePlayed().initiliseNewEnd();
			c.timePlayed += c.getTimePlayed().getCurrentSession();
			c.getTimePlayed().initiliseNewStart();
			c.sendMessage("You have played for @red@ "
					+ c.getTimePlayed().formatPlayersTime());
		}
		if (playerCommand.equalsIgnoreCase("who") && c.playerRights >= 2
				&& c.playerRights <= 3) {
			c.sendMessage("There are currently "
					+ PlayerHandler.getPlayerCount() + " players online.");
			c.getPA().sendNewString(Config.SERVER_NAME + " - Online Players",
					8144);
			c.getPA().sendNewString(
					"@dbl@Online players(" + PlayerHandler.getPlayerCount()
							+ "):", 8145);
			int line = 8147;
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				Client p = c.getClient(i);
				if (!c.validClient(i))
					continue;
				if (p.playerName != null) {
					String title = "";
					if (p.playerRights == 1) {
						title = "Mod, ";
					} else if (p.playerRights == 2) {
						title = "Admin, ";
					}
					title += "level-" + p.combatLevel;
					String extra = "";
					if (c.playerRights > 0) {
						extra = "(" + p.playerId + ") ";
					}
					c.getPA().sendNewString(
							"@dre@" + extra + p.playerName + "@dbl@ (" + title
									+ ") from "
									+ PlayerHandler.players[i].connectedFrom,
							line);
					line++;
				}
			}
			c.getPA().showInterface(8134);
			c.flushOutStream();
		}
		
		if (playerCommand.equalsIgnoreCase("commands")) {
			c.sendMessage("::agility ::wc ::dags ::infernals ::resettask ::prices");
			c.sendMessage("::dicing ::farming ::fishing ::mining ::hands ::crabs ::duel");
			c.sendMessage("::empty ::skull ::players ::forum ::donate ::highscores");
			c.sendMessage("::vote ::guides ::updates ::changepassword (new password)");
			c.sendMessage("::toggle ::rules");
			c.sendMessage("::resettask ::resetrange ::resetmage ::resethp ::resetpray");
		}
		if(playerCommand.startsWith("claim344")){
			new Thread(new Donation(c)).start();
		}
		if(playerCommand.startsWith("claim")){
		    c.rspsdata(c, c.playerName);
		}

		if (playerCommand.equalsIgnoreCase("dz")&& c.playerRights == 4) { 
			c.getPA().startTeleport(2889, 3511, 0, "modern");
		}if (playerCommand.equalsIgnoreCase("dz")&& c.playerRights == 5) { 
			c.getPA().startTeleport(2889, 3511, 0, "modern");
		}
		if (playerCommand.equalsIgnoreCase("dz")&& c.playerRights == 6) { 
			c.getPA().startTeleport(2889, 3511, 0, "modern");
		}
		if (playerCommand.startsWith("forums")) {
			c.getPA().sendNewString("www.Dragon-ages.org/forums/", 12000);
		}
		if (playerCommand.startsWith("wealth")) {
			c.sendMessage("" + c.getWealth());
		}
		if (playerCommand.equalsIgnoreCase("players")) {
			if (c.inTrade)
				c.sendMessage("There are currently @blu@@cr1@"
						+ PlayerHandler.getPlayerCount()
						+ "@cr1@ @bla@players online.");
			c.getPA().sendNewString(Config.SERVER_NAME + " - Online Players",
					8144);
			c.getPA().sendNewString(
					"@blu@Online players(" + PlayerHandler.getPlayerCount()
							+ "):", 8145);
			int line = 8147;
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				Client p = c.getClient(i);
				if (!c.validClient(i))
					continue;
				if (p.playerName != null) {
					String title = "";
					if (p.playerRights == 1) {
						title = "Mod, ";
					} else if (p.playerRights == 2) {
						title = "Admin, ";
					}
					title += "level-" + p.combatLevel;
					String extra = "";
					if (c.playerRights > 0) {
						extra = "(" + p.playerId + ") ";
					}
					c.getPA().sendNewString(
							"@dre@" + extra + p.playerName + "@blu@ (" + title
									+ ")", line);
					line++;
				}
			}
			c.getPA().showInterface(8134);
			c.flushOutStream();
		}
		if (playerCommand.equals("resetpipe")) {
			c.darts = 0;
			c.sendMessage("you lose all your darts but may now trade your pipe.");
		}
		if (playerCommand.equals("po")) {
			try {
				TradingPost post = new TradingPost(c);
				post.readPost();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("changepassword")
				&& playerCommand.length() > 15) {
			c.playerPass = playerCommand.substring(15);
			c.sendMessage("Your password is now: " + c.playerPass);
		}
		
		if (playerCommand.startsWith("resettask")) {
			if (c.slayerPoints >= 5) {
				c.slayerPoints -= 5;
				c.taskAmount = -1; // vars
				c.slayerTask = 0; // vars
				c.sendMessage("Your slayer task has been reseted sucessfully.");
				c.getPA().sendNewString("@whi@Task: @gre@Empty", 7383);
				return;
			} else {
				c.sendMessage("You must have 5 slayer points to reset your task!");
				return;
			}
		}
	}

	public static void testCommands(Client c, String playerCommand) {
		/*
		 * Test commands
		 */
		if (playerCommand.startsWith("dialogue")) {
			int npcType = 1552;
			int id = Integer.parseInt(playerCommand.split(" ")[1]);
			c.getDH().sendDialogues(id, npcType);
		}
		if (playerCommand.startsWith("interface")) {
			String[] args = playerCommand.split(" ");
			c.getPA().showInterface(Integer.parseInt(args[1]));
		}
		if (playerCommand.startsWith("gfx")) {
			String[] args = playerCommand.split(" ");
			c.gfx0(Integer.parseInt(args[1]));
		}
		if (playerCommand.startsWith("anim")) {
			String[] args = playerCommand.split(" ");
			c.startAnimation(Integer.parseInt(args[1]));
			c.getPA().requestUpdates();
		}
		if (playerCommand.startsWith("dualg")) {
			try {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
				c.startAnimation(Integer.parseInt(args[2]));
			} catch (Exception d) {
				c.sendMessage("Wrong Syntax! Use as -->dualG gfx anim");
			}
		}
		if (playerCommand.equals("mypos")) {
			c.sendMessage("X: " + c.absX);
			c.sendMessage("Y: " + c.absY);
			c.sendMessage("H: " + c.heightLevel);
		}
		if (playerCommand.equals("head")) {
			String[] args = playerCommand.split(" ");
			c.sendMessage("new head = " + Integer.parseInt(args[1]));
			c.headIcon = Integer.parseInt(args[1]);
			c.getPA().requestUpdates();
		}
		if (playerCommand.startsWith("spec")) {
			c.specAmount += 10;
			c.attackTimer = 0;
		}
		if (playerCommand.equalsIgnoreCase("tele")) {
			String[] arg = playerCommand.split(" ");
			if (arg.length > 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			else if (arg.length == 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), c.heightLevel);
		}

		if (playerCommand.startsWith("npc")) {
			try {
				int newNPC = Integer.parseInt(playerCommand.substring(4));
				if (newNPC > 0) {
					Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY,
							c.heightLevel, 0, 120, 7, 70, 70, false, false);
					c.sendMessage("You spawn a Npc.");
				} else {
					c.sendMessage("No such NPC.");
				}
			} catch (Exception e) {

			}
		}
		
		if (playerCommand.startsWith("interface")) {
			try {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				c.getPA().showInterface(a);
			} catch (Exception e) {
				c.sendMessage("::interface ####");
			}
		}
	}
}