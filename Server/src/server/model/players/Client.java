package server.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.event.CycleEvent;
import server.event.CycleEventHandler;
import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.content.DuoSlayer;
import server.model.content.Food;
import server.model.content.Highscores;
import server.model.content.KillingStreak;
import server.model.content.LootingBag;
import server.model.content.MoneyPouch;
import server.model.content.Pins;
import server.model.content.PotionMixing;
import server.model.content.Potions;
import server.model.content.QuickCurses;
import server.model.content.QuickPrayers;
import server.model.content.Target;
import server.model.content.TimePlayed;
import server.model.content.TradeHandler;
import server.model.content.dialogue.DialogueHandler;
import server.model.content.price.PriceChecker;
import server.model.items.ItemAssistant;
import server.model.items.TeleportingTabs;
import server.model.items.UseItem;
import server.model.minigames.Barrows;
import server.model.minigames.Dueling;
import server.model.minigames.PestControl;
import server.model.minigames.bh.BountyHunter;
import server.model.npcs.NPC;
import server.model.npcs.pet.Pet;
import server.model.players.Attributes.A;
import server.model.players.pvp.PvPHandler;
import server.model.players.skills.cooking.Cooking;
import server.model.players.skills.crafting.Crafting;
import server.model.players.skills.farming.Farming;
import server.model.players.skills.fishing.Fishing;
import server.model.players.skills.fletching.Fletching;
import server.model.players.skills.herblore.Herblore;
import server.model.players.skills.magic.Magic;
import server.model.players.skills.mining.Mining;
import server.model.players.skills.prayer.Prayer;
import server.model.players.skills.runecrafting.Runecrafting;
import server.model.players.skills.slayer.Slayer;
import server.model.players.skills.smithing.Smithing;
import server.model.players.skills.smithing.SmithingInterface;
import server.model.players.skills.thieving.Thieving;
import server.model.shops.ShopAssistant;
import server.net.HostList;
import server.net.Packet;
import server.net.Packet.Type;
import server.net.login.RS2LoginProtocolDecoder;
import server.region.Region;
import server.util.Logs;
import server.util.Misc;
import server.util.Stream;

public class Client extends Player {
	
	private TradeHandler tradeHandler = new TradeHandler(this);

	public TradeHandler getTradeHandler() {
		return tradeHandler;
	}
	
	private Dueling duelling = new Dueling(this);

	public Dueling getDuel() {
		return this.duelling;
	}


	private Channel session;

	public int bhPenalty = 0;

	public Channel getSession() {
		return session;
	}
	
	public long moneyPouch = 0;
	
	private MoneyPouch pouch = new MoneyPouch(this);

		
	public int totalXp;
	
	public boolean usingRestoreAltar;

	public boolean easyBosses = false, mediumBosses = false,
			hardBosses = false, easySharks = false, mediumSharks = false,
			hardSharks = false, easyGlories = false, mediumGlories = false,
			hardGlories = false, easyMagics, mediumMagics, hardMagics,
			easyBurned, mediumBurned, hardBurned, easyBows, mediumBows,
			hardBows, easyHerbs, mediumHerbs, hardHerbs, mediumTasks,
			hardTasks, mediumDuo, hardDuo;
	public int sharksFished, gloriesCrafted, magicsCut, magicsBurned, bowsMade,
	herbsFarmed, slayerTasksCompleted, duoSlayerTasksCompleted;
	private String colorSelect;

	public String getColorSelect() {
		return colorSelect;
	}
	public String setColorSelect(String color) {
		return colorSelect = color;
	}
	public NPC zulrah;
	public boolean claimedEasy = false;
	public boolean claimedMedium = false;
	public boolean claimedHard = false;

	private LootingBag bag = new LootingBag();

	public LootingBag getBag() {
		return bag;
	}

	private Barrows barrows = new Barrows(this);

	public Barrows getBarrows() {
		return barrows;
	}

	public int darts = 0;
	private PvPHandler pvpHandler = new PvPHandler(this);

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	private KillingStreak killingStreak = new KillingStreak(this);

	public int usedId;

	public int pouch1;
	public int pouch1N;
	public int pouch2;
	public int pouch2N;
	public int pouch3;
	public int pouch3N;

	/***
	 * Easter Variables
	 */
	public boolean startedEvent = false;
	public int easterStage = 0;
	public int eggsCollected = 0;
	public boolean egg1Collected, egg2Collected, egg3Collected, egg4Collected,
	egg5Collected, egg6Collected, egg7Collected, egg8Collected = false;
	/**
	 * Skill instances
	 */
	private Slayer slayer = new Slayer(this);
	private Runecrafting runecrafting = new Runecrafting(this);
	private Mining mine = new Mining();
	private Cooking cooking = new Cooking();
	private Fishing fish = new Fishing();
	private Crafting crafting = new Crafting(this);
	private Smithing smith = new Smithing(this);
	private Prayer prayer = new Prayer(this);
	private Fletching fletching = new Fletching(this);
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Farming farming = new Farming(this);
	private Thieving thieving = new Thieving(this);
	private Herblore herblore = new Herblore(this);

	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;

	public Client(Channel s, int _playerId) {
		super(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected
				|| outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED,
				ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, /* 0 */-1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, -1, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 6, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, /* 0 */4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};
	public boolean playerIsWoodcutting, wcClick;
	public boolean eliteTask = false;

	public void destruct() { // k trade done
		if (underAttackBy > 0 || underAttackBy2 > 0 || session == null)
			return;
		if (this.inPcGame()) {
			PestControl.removePlayerGame(this);
			absX = 2657;
			absY = 2639;
		}
		if (inPcBoat()) {
			PestControl.removePlayerGame(this);
			PestControl.leaveWaitingBoat(this);
			absX = 2657;
			absY = 2639;
		}
		if (tradeHandler.getCurrentTrade() != null) {
			if (tradeHandler.getCurrentTrade().isOpen()) {
				tradeHandler.decline();
			}
		}
		Target.hastarget = false;
		BountyHunter.handleLogout(this);
		if (inClanWarsGame) {
			for (int i = 0; i < 14; i++) {
				playerEquipment[i] = -1;
				playerEquipmentN[i] = -1;
				getItems().setEquipment(-1, 1, i);
			}
			getItems().removeAllItems();
			Server.clanWars.removePlayer(this);
		}
		if (inClanWarsWait) {
			Server.clanWars.removePlayerWait(this);
		}
		if (duelStatus > 0 && duelStatus < 4) {
			Client o = ((Client) PlayerHandler.players[duelingWith]);
			if (o == null) {
				getDuel().declineDuel();
				return;
			}
			getDuel().declineDuel();
			o.getDuel().declineDuel();
		}
		
		inTrade = false;
		tradeAccepted = false;
		CycleEventHandler.getSingleton().stopEvents(this);
		PlayerSave.saveGame(this);
		Client partner = (Client) getDuoPartner();
		if (duoTask >= -1 && partner != null) {
			partner.sendMessage("The other member of the party has logged out");
			partner.sendMessage("The Duo Slayer task has been cancelled.");
			partner.setDuoPartner(null);
			this.setDuoPartner(null);
			DuoSlayer.logoutTask(this);
		}
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		Misc.println("[DEREGISTERED]: " + playerName + "");
		HostList.getHostList().remove(session);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}

	// my code is different. 1sec renaming.
	public int searchItemsId[] = new int[Config.BANK_SIZE];
	public int searchItemsAmount[] = new int[Config.BANK_SIZE];
	public int searchItemsSlot[] = new int[Config.BANK_SIZE];
	public int foundItemsId[] = new int[Config.BANK_SIZE];
	public int foundItemsAmount[] = new int[Config.BANK_SIZE];

	public void Search(String s) {
		int results = 0;
		int newBankSlot = 0;
		int test = 0;

		for (int i = 0; i < searchItemsId.length; i++) {
			searchItemsId[i] = 0;
			searchItemsAmount[i] = 0;
			searchItemsSlot[i] = 0;
			foundItemsId[i] = 0;
			foundItemsAmount[i] = 0;
		}

		for (int j = 0; j < bankItems.length; j++) {
			searchItemsId[j] = bankItems[j];
			searchItemsAmount[j] = bankItemsN[j];
			bankItems[j] = 0;
			bankItemsN[j] = 0;

		}
		// no it wont work the same this is diffrent.

		// put this method in client instead of player?
		// mines in player.
		for (int k = 0; k < searchItemsId.length; k++) {
			test++;
			if (getItems().getItemName(searchItemsId[k]).toLowerCase()
					.contains(s.toLowerCase())) {
				bankItems[newBankSlot] = searchItemsId[k];
				bankItemsN[newBankSlot] = searchItemsAmount[k];
				foundItemsId[newBankSlot] = searchItemsId[k];
				foundItemsAmount[newBankSlot] = searchItemsAmount[k];
				searchItemsSlot[newBankSlot] = test;
				newBankSlot++;
				results++;
			}
		}

		getPA().openUpBank(bankingTab);
		sendMessage(results + " - Results for search term '" + s + "'.");

		for (int l = 0; l < searchItemsId.length; l++) {
			bankItems[l] = searchItemsId[l];
			bankItemsN[l] = searchItemsAmount[l];
		}

		bankSearching = true;
	}

	public void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public boolean lootingBagOpen = false;

	public void showLootingBagInterface() {
		if (!lootingBagOpen) {
			setSidebarInterface(1, -1);
			setSidebarInterface(2, -1);
			setSidebarInterface(4, -1);
			setSidebarInterface(5, -1);
			setSidebarInterface(6, -1);
			setSidebarInterface(7, -1);
			setSidebarInterface(8, -1);
			setSidebarInterface(9, -1);
			setSidebarInterface(10, -1);
			setSidebarInterface(11, -1);
			setSidebarInterface(12, -1);
			setSidebarInterface(13, -1);
			setSidebarInterface(0, -1);
			setSidebarInterface(3, 28000);
			lootingBagOpen = true;
		}
	}

	public void refreshLootingInterface() {
		for (int i = 28504; i < 28504 + 28; i++) {
			PlayerAssistant.itemOnInterface(this, i, 0, -1, 0);
		}
	}

	public void depositLootBag() {
		refreshLootingInterface();
		for (Map.Entry<Integer, Integer> entry : getBag().LootingBagContents
				.entrySet()) {
			if (entry.getValue() > 0 && entry.getKey() > 0) {
				if (getItems().freeBankSlots() > getBag().getSizeOfLootingBag()) {
					sendMessage("You deposit " + entry.getValue() + "x "
							+ getItems().getItemName(entry.getKey()) + ".");
					getItems().addItemToBank(entry.getKey(), entry.getValue());
					getLogs().writeLootBagRemove(
							getItems().getItemName(entry.getKey()),
							entry.getValue());
				}
			}
		}
		getBag().deleteBagItems();
	}

	public void showLootingBagContents() {
		if (!lootingBagOpen) {
			setSidebarInterface(1, -1);
			setSidebarInterface(2, -1);
			setSidebarInterface(4, -1);
			setSidebarInterface(5, -1);
			setSidebarInterface(6, -1);
			setSidebarInterface(7, -1);
			setSidebarInterface(8, -1);
			setSidebarInterface(9, -1);
			setSidebarInterface(10, -1);
			setSidebarInterface(11, -1);
			setSidebarInterface(12, -1);
			setSidebarInterface(13, -1);
			setSidebarInterface(0, -1);
			setSidebarInterface(3, 28500);
			int start = 28504;
			for (Map.Entry<Integer, Integer> entry : getBag().LootingBagContents
					.entrySet()) {
				if (entry.getValue() > 0 && entry.getKey() > 0) {
					for (int i = 0; i < entry.getValue(); i++) {
						PlayerAssistant.itemOnInterface(this, start, 0,
								entry.getKey(), entry.getValue());
						start += 1;
					}
				}
			}
			lootingBagOpen = true;
		}
	}

	public void restoreTabs() {
		setSidebarInterface(1, 3917);
		setSidebarInterface(2, 638);
		setSidebarInterface(3, 3213);
		setSidebarInterface(4, 1644);
		setSidebarInterface(5, 5608);
		if (playerMagicBook == 0) {
			setSidebarInterface(6, 1151);
		} else if (playerMagicBook == 1) {
			setSidebarInterface(6, 12855);
		} else if (playerMagicBook == 2) {
			setSidebarInterface(6, 29999);
		}
		setSidebarInterface(7, 18128);
		setSidebarInterface(8, 5065);
		setSidebarInterface(9, 5715);
		setSidebarInterface(10, 2449);
		setSidebarInterface(11, 904);
		setSidebarInterface(12, 147);
		setSidebarInterface(13, 962);
		setSidebarInterface(0, 2423);
		lootingBagOpen = false;
	}

	public boolean achievementTab;

	public void setSidebarInterface(int menuId, int form) {
		if (form == 29265) {
			achievementTab = true;
		} else {
			achievementTab = false;
		}
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}

	public boolean itemInSearch(int itemId) {
		for (int i = 0; i < foundItemsId.length; i++) {
			if (foundItemsId[i] - 1 == itemId) {
				return true;
			}
		}
		return false;
	}

	public boolean inGlobalCC;

	public void joinGlobalCC() {
		if (clan == null) {
			server.clan.Clan localClan = Server.clanManager
					.getClan("help");
			if (localClan != null)
				localClan.addMember(this);
			else if ("help".equalsIgnoreCase(this.playerName))
				Server.clanManager.create(this);
			inGlobalCC = true;
		}
	}


	private static final String FORMAT = "%02d:%02d:%02d";

	public static String parseTime(long milliseconds) {
		return String.format(
				FORMAT,
				TimeUnit.MILLISECONDS.toHours(milliseconds * 1000),
				TimeUnit.MILLISECONDS.toMinutes(milliseconds * 1000)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(milliseconds * 1000)),
						TimeUnit.MILLISECONDS.toSeconds(milliseconds * 1000)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(milliseconds * 1000)));
	}

	public void initialize() {
		// loginScreen();
		getPA().sendFrame126(moneyPouch + "", 8135);
		duelSafety = false;
		UUID = RS2LoginProtocolDecoder.UUID;
		MAC = RS2LoginProtocolDecoder.MAC;
		getPA().sendNewString(":prayer:" + "prayers", -1);
		splitChat = true;
		getPA().sendFrame36(502, 1);
		getPA().sendFrame36(287, 1);
		getPA().sendFrame36(173, 1);
		this.getTimePlayed().initiliseNewStart();
		getPA().sendFrame126(":totalXp:" + totalXp, -1);
		Pet.ownerLoggedIn(this);
		cleanInterface();
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName
						.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int i = 0; i < 25; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}
		if (playerRights == 1 || playerRights == 3 || playerRights == 7) {
			//sendGlobal("[@cr6@@red@SERVER@cr6@@bla@] Staff Member @red@"
					//+ playerName + "@bla@ has just logged in.");
		}
		getPA().loadQuests();
		getPA().handleWeaponStyle();
		getPA().handleLoginText();
		accountFlagged = getPA().checkForFlags();
		// getPA().sendFrame36(43, fightMode-1);
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options
		setSidebarInterface(1, 3917);
		setSidebarInterface(2, 638);
		setSidebarInterface(3, 3213);
		getPA().sendNewString(runEnergy + "%", 149);
		setSidebarInterface(4, 1644);
		setSidebarInterface(5, 5608);
		if (playerMagicBook == 0) {
			setSidebarInterface(6, 1151); // modern
		} else {
			if (playerMagicBook == 2) {
				setSidebarInterface(6, 29999); // lunar
			} else {
				setSidebarInterface(6, 12855); // ancient
			}
		}
		setSidebarInterface(7, 18128);
		setSidebarInterface(8, 5065);
		setSidebarInterface(9, 5715);
		setSidebarInterface(10, 2449);
		// setSidebarInterface(11, 4445); // wrench tab
		setSidebarInterface(11, 904); // wrench tab
		setSidebarInterface(12, 147); // run tab
		setSidebarInterface(13, -1);
		setSidebarInterface(0, 2423);
		sendMessage("Welcome to @red@Dragon-Age");
		//sendMessage("<shad=0>@mag@We are currently having 20% off all donations #CyberMonday");
		if (Config.DOUBLE_XP) {
			sendMessage("@red@Double XP Weekend is Currently Active until Monday 12am EST!");
		}
		if (getDoubleExp()) {
			sendMessage("You currently have double XP for "
					+ parseTime((long) time));
		}
		correctCoordinates();
		// AuctionHouse.welcomeMessage(this);
		BountyHunter.handleLogin(this);
		getPA().sendOption("Follow", 2);
		getPA().sendOption("Trade With", 3);
		getItems().resetItems(3214);
		getItems().sendWeapon(playerEquipment[playerWeapon],
				getItems().getItemName(playerEquipment[playerWeapon]));
		calculateCombatLevel();
		getItems().resetBonus();
		getItems().getBonus();
		getItems().writeBonus();
		getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getItems().setEquipment(playerEquipment[playerArrows],
				playerEquipmentN[playerArrows], playerArrows);
		getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getItems().setEquipment(playerEquipment[playerWeapon],
				playerEquipmentN[playerWeapon], playerWeapon);
		getCombat().getPlayerAnimIndex(
				getItems().getItemName(playerEquipment[playerWeapon])
				.toLowerCase());
		getPA().logIntoPM();
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		saveTimer = Config.SAVE_TIMER;
		saveCharacter = true;
		Misc.println("[REGISTERED]: " + playerName + "");
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
		getPA().clearClanChat();
		getPA().resetFollow();
		getPA().sendTotal();
		this.joinGlobalCC();
		if (addStarter) {
			getPA().addStarter();
			//sendGlobal("[@cr6@@red@Server@bla@] New Player @blu@" + playerName
			//		+ "@bla@ has just logged in!");
		}
		if (autoRet == 1)
			getPA().sendFrame36(172, 1);
		else
			getPA().sendFrame36(172, 0);
		if (Server.ironDonatorMap.contains(playerName)) {
			System.out.println("IRON DONATOR");
			ironDonator = true;
		}
		replaceFormat();
		// cleanInterface();
		// Target target = new Target();
	}

	public void correctCoordinates() {
		if (inPcGame2()) {
			getPA().movePlayer(2657, 2639, 0);
		}
		if (inFightCaves2()) {
			getPA().movePlayer(teleportToX, teleportToY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					Server.fightCaves.spawnNextWave((Client) PlayerHandler.players[playerId]);
					c.stop();
				}
			}, 10000);

		}
		if (loginZulrah()) {
			getPA().movePlayer(2311, 3785, 0);
		}
	}

	public boolean loginZulrah() {
		return teleportToX >= 2255 && teleportToX <= 2279
				&& teleportToY >= 3066 && teleportToY <= 3081;
	}

	public void cleanInterface() {
		getPA().sendNewString("---", 27305);
		getPA().sendNewString("Target:", 27306);
		getPA().sendNewString("None", 27307);
		getPA().sendNewString("Level:", 27308);
		getPA().sendNewString("Current  Record", 27309);
		getPA().sendNewString("0", 27310);
		getPA().sendNewString("0", 27311);
		getPA().sendNewString("0", 27312);
		getPA().sendNewString("0", 27313);
		getPA().sendNewString("Rogue:", 27314);
		getPA().sendNewString("Hunter:", 27315);
		for (int i = 0; i < 6; i++) {
			getPA().sendConfig(876 + i, 0);
		}
	}

	public void randomTeleport() {
		String type = playerMagicBook == 0 ? "modern" : "ancient";
		switch (Misc.random(3)) {

		case 0:
			getPA().startTeleport(2412, 3802, 0, type);
			break;
		case 1:
			getPA().startTeleport(2413, 3802, 0, type);
			break;
		case 2:
			getPA().startTeleport(2412, 3803, 0, type);
			break;
		case 3:
			getPA().startTeleport(2413, 3803, 0, type);
			break;

		}
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}

	public void logout() {
		if (this.duelStatus > 4) {
			this.sendMessage("You can not log out in a duel!");
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			this.getTimePlayed().initiliseNewEnd();
			Highscores.highscores(this, null);
			outStream.createFrame(109);
			properLogout = true;
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	public int packetSize = 0, packetType = -1;
	public int donatorPoints = 0;

	public boolean hadRunWhileJumping, jumpingDitch, ditchEventRunned;
	public int jumpDitchChangePos;
	/**
	 * Clan Chat Variables
	 */
	public server.clan.Clan clan;
	public String clanName, properName;
	public String lastClanChat = "";
	public boolean inPublicCC;
	public int diceItem;
	public int page;
	public int slayerPoints = 0;
	public int donPoints = 0;
	public int pkp, KC, DC;
	public int votPoints;
	public int EquipStatus;
	public int pinDeleteDateRequested;
	public int[][] barrowCrypt = { { 4921, 0 }, { 2035, 0 } };

	public void updateInterface(Client c, Client o) {
		c.getPA().sendNewString(Misc.formatPlayerName(o.playerName), 27307);
		o.getPA().sendNewString(Misc.formatPlayerName(c.playerName), 27307);
		c.getPA().sendNewString(getWildernessLevel(c, o), 27308);
		o.getPA().sendNewString(getWildernessLevel(o, c), 27308);
		findSkull(c, o);
		findSkull(o, c);
		c.getPA().sendNewString(findWealth(o), 27305);
		o.getPA().sendNewString(findWealth(c), 27305);
	}

	public String getWildernessLevel(Client c, Client o) {
		if (!o.inWild()) {
			return "@gre@Safe, Cmb " + o.combatLevel;
		}
		int wildernessDifference = o.wildLevel + 3;// what color?
		String color = "";
		if (c.distanceToPoint(o.absX, o.absY) <= 15) {
			color = "@gre@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 15
				&& c.distanceToPoint(o.absX, o.absY) <= 60) {
			color = "@bro@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 60) {
			color = "@bl2@";
		}
		return color + "Lvl " + o.wildLevel + "-" + wildernessDifference
				+ ", Cmb " + o.combatLevel;
	}

	public int getWealth(Client c) {
		return c.getWealth();
	}

	public String findWealth(Client c) {

		if (getWealth(c) >= 0 && getWealth(c) <= 150000) {
			return "Wealth: V. Low";
		} else if (getWealth(c) > 150000 && getWealth(c) <= 400000) {
			return "Wealth: Low";
		} else if (getWealth(c) > 400000 && getWealth(c) <= 800000) {
			return "Wealth: Medium";
		} else if (getWealth(c) > 800000 && getWealth(c) <= 1500000) {
			return "Wealth: High";
		} else if (getWealth(c) > 1500000) {
			return "Wealth: V. High";
		}
		return "";
	}

	// 879 = green
	public void findSkull(Client c, Client o) {
		if (getWealth(o) >= 0 && getWealth(o) <= 150000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(877, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 150000 && getWealth(o) <= 400000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(878, 1);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 400000 && getWealth(o) <= 800000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(879, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(880, 0);
			c.getPA().sendConfig(881, 0);
		} else if (getWealth(o) > 800000 && getWealth(o) <= 1500000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(880, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(881, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(877, 0);
		} else if (getWealth(o) > 1500000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(881, 1);
			c.getPA().sendConfig(878, 0);
			c.getPA().sendConfig(879, 0);
			c.getPA().sendConfig(877, 0);
			c.getPA().sendConfig(880, 0);
		}
	}

	
	public void rspsdata(Client c, String username){
try{
username = username.replaceAll(" ","_");
String secret = "fa84632d742f2729dc32ce8cb5d49733"; //YOUR SECRET KEY!
String email = "destinyrecruits@yahoo.com"; //This is the one you use to login into RSPS-PAY
URL url = new URL("http://rsps-pay.com/includes/listener.php?username="+username+"&secret="+secret+"&email="+email);
BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
String results = reader.readLine();
if(results.toLowerCase().contains("!error:")){

}else{
String[] ary = results.split(",");
     for(int i = 0; i < ary.length; i++){
            switch(ary[i]){
                case "0":
                    //donation was not found tell the user that!
                break;
                    case "10094": //product ids can be found on the webstore page
                    	c.getItems().addItemToBank(13192, 1);
						c.sendMessage("You've received your Donation reward! Congratulations! Look in Bank!");//add items for the first product
                    break;
                    case "10095": //product ids can be found on the webstore page
                    	c.getItems().addItemToBank(13190, 1);
						c.sendMessage("You've received your Donation reward! Congratulations! Look in Bank!");  //add items for the second product here!
                    break;
                    case "10096": //product ids can be found on the webstore page
                    	c.getItems().addItemToBank(13187, 1);
						c.sendMessage("You've received your Donation reward! Congratulations! Look in Bank!"); //add items for the second product here!
                break;
            }
    }
}
}catch(IOException e){}
}							
	
	public void process() {// get this to print out,
		super.process();
		doFollow(false);
		if (stunTimer > 0) {
			if (stunTimer > 4)
				gfx100(80);
			stunTimer--;
		}
		if (chestDelay > 0) {
			chestDelay--;
		}
		if (claimDelay > 0) {// you got it to rpint? yes on eclipse or cd
			claimDelay--;// test melee combat follow.
		}
		if (collectDelay > 0) {
			collectDelay--;
		}
		if (lastYell > 0) {
			lastYell--;
		}
		if (time > 0) {
			getPA().sendNewString("Double Exp:" + parseTime((long) time), 29169);
			time--;
		}
		if (!inClanWarsGame && inClanWarsCoords()) {
			getPA().movePlayer(2440, 3088, 0);
		}
		if (time == 0) {
			sendMessage("Your double Experience has worn off...");
			setDoubleExp(false);
			time = -1;
		}

		/*
		 * if (runEnergy < 100) { if (System.currentTimeMillis() >
		 * getPA().getAgilityRunRestore() + lastRunRecovery) { runEnergy++;
		 * lastRunRecovery = System.currentTimeMillis();
		 * getPA().sendNewString(runEnergy + "%", 149); } }
		 */

		if (inTrade && interfaceOpen) {
			getPA().removeAllWindows();
		}
		getPA().sendNewString(
				"" + PlayerHandler.getPlayerCount()
				+ " Players Online @cr1@", 29155);
		if (clickObjectType > 0 && destinationReached()) {
			walkingToObject = false;
			turnPlayerTo(objectX, objectY);
			if (clickObjectType == 1) {
				getActions().firstClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 2) {
				getActions().secondClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 3) {
				getActions().thirdClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 4) {
				UseItem.ItemonObject(this, objectId, objectX, objectY, usedId);
			}
		}
		if (System.currentTimeMillis() - this.duelDelay > 800L && this.duelCount > 0
				&& PlayerHandler.players[this.duelingWith] != null) {
			if (duelCount != 1) {
				forcedChat("" + (--duelCount));
				duelDelay = System.currentTimeMillis();
			} else {
				damageTaken = new int[Config.MAX_PLAYERS];
				forcedChat("FIGHT!");
				duelCount = 0;
			}
		}
		if (ditchEventRunned && jumpingDitch && jumpDitchChangePos != 0) {
			getPA().walkTo3(0, jumpDitchChangePos);
			isRunning = hadRunWhileJumping;
			jumpDitchChangePos = 0;
		}
		if (System.currentTimeMillis() - lastPoison > 20000 && poisonDamage > 0) {
			int damage = poisonDamage / 2;
			if (damage > 0) {
				sendMessage("The poison damages you");
				if (!getHitUpdateRequired()) {
					setHitUpdateRequired(true);
					setHitDiff(damage);
					updateRequired = true;
					poisonMask = 1;
				} else if (!getHitUpdateRequired2()) {
					setHitUpdateRequired2(true);
					setHitDiff2(damage);
					updateRequired = true;
					poisonMask = 2;
				}
				lastPoison = System.currentTimeMillis();
				poisonDamage--;
				dealDamage(damage);
			} else {
				poisonDamage = -1;
				isPoisoned = false;
				sendMessage("You are no longer poisoned.");
			}
		}

		if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}
		getCombat().handlePrayerDrain();

		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level],
								playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level],
							playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}// ah, process void how i have missed you rofl haha
		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			BountyHunter.handleInterfaces(this);
			// getPA().walkableInterface(197);
			// getPA().walkableInterface(27300);
			// BountyHunter.handleInterface(this);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getPA().sendNewString("@yel@Level: " + wildLevel, 199);
				} else {
					getPA().sendNewString("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendNewString("@yel@Level: " + wildLevel, 199);
			}
			getPA().sendOption("Attack", 1);
		} else if (!inWild() && targetIndex != 0) {
			getPA().showOption(3, 0, "Null", 1);
			getPA().sendNewString("", 199);
			getPA().walkableInterface(27300);
			getPA().sendFrame171(197, 0);
			BountyHunter.updateInterface(this,
					(Client) PlayerHandler.players[targetIndex]);
		} else if (inSafeZone()) {
			getPA().sendOption("null", 1);
			getPA().walkableInterface(21200);
			pvpHandler.pvpLevels();
		} else if (inPvP()) {
			getPA().walkableInterface(21300);
			pvpHandler.pvpLevels();
			getPA().sendOption("Attack", 1);
		} else if (inFFA()) {
			getPA().walkableInterface(197);
			getPA().sendOption("Attack", 1);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duelStatus == 5) {
				getPA().sendOption("Attack", 1);
			} else {
				getPA().sendOption("Challenge", 1);
			}
		} else if (inPcBoat()) {
			getPA().walkableInterface(21119);
		} else if (inPcGame()) {
			getPA().walkableInterface(21100);
		} else if (inBarrows()) {
			getPA().walkableInterface(16128);
			getPA().sendNewString("" + barrowsKillCount, 16137);
			if (barrowsNpcs[2][1] == 2) {
				getPA().sendNewString("@red@Karils", 16135);
			}
			if (barrowsNpcs[3][1] == 2) {
				getPA().sendNewString("@red@Guthans", 16134);
			}
			if (barrowsNpcs[1][1] == 2) {
				getPA().sendNewString("@red@Torags", 16133);
			}
			if (barrowsNpcs[5][1] == 2) {
				getPA().sendNewString("@red@Ahrims", 16132);
			}
			if (barrowsNpcs[0][1] == 2) {
				getPA().sendNewString("@red@Veracs", 16131);
			}
			if (barrowsNpcs[4][1] == 2) {
				getPA().sendNewString("@red@Dharoks", 16130);
			}
		} else if (inClanWarsGame || inPits) {
			getPA().sendOption("Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().sendOption("null", 1);
		} else if (!inClanWarsWait) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().sendOption("null", 1);
		}

		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}

		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);// is it with all weps too
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY,
						PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}
		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}
		getPA().flushNewString();
	}

	public int finalDestX, finalDestY;
	public boolean walkingToObject;
	public int fireslit;
	public boolean usingAltar = false;
	public int altarItemId;
	public boolean fireMaking = false;
	public int boltDamage;
	public double crossbowDamage;
	public boolean ignoreDefence;
	public boolean interfaceOpen = false;
	public int tradeDelay;
	public int tradeId;
	public int otherPlayerId;
	public boolean stopPlayerSkill;
	public int[][] playerSkillProp = new int[20][15];
	public boolean[] playerSkilling = new boolean[20];
	public int doAmount;
	public boolean isWc;
	public int woodcuttingTree;
	public String donators[] = {};
	public String currentTime, date;
	public Logs logs = new Logs(this);

	/**
	 * Agility
	 */

	public boolean doingAgility = false;

	/**
	 * Obstacle Variables
	 */

	public boolean finishedLog = false;
	public boolean finishedNet1 = false;
	public boolean finishedBranch1 = false;
	public boolean finishedRope = false;
	public boolean finishedBranch2 = false;
	public boolean finishedNet2 = false;
	public boolean finishedPipe = false;

	public boolean finishedBarbRope = false;
	public boolean finishedBarbLog = false;
	public boolean finishedBarbNet = false;
	public boolean finishedBarbLedge = false;
	public boolean finishedBarbStairs = false;
	public boolean finishedBarbWall1 = false;
	public boolean finishedBarbWall2 = false;
	public boolean finishedBarbWall3 = false;

	public boolean finishedWildPipe = false;
	public boolean finishedWildRope = false;
	public boolean finishedWildStone = false;
	public boolean finishedWildLog = false;
	public boolean finishedWildRocks = false;
	public boolean agilityEmote = false;

	public boolean[] isSkilling = new boolean[25];
	public int logID, currentArrow, stringu;
	public boolean isArrowing, isOnInterface, isStringing, playerisSmelting;

	public boolean below459 = true;
	public int event, makeTimes;
	public boolean isPoisoned = false;
	public int petID;
	public boolean petSummoned;

	/**
	 * Change the state of petSummoned.
	 * 
	 * @param state
	 *            The state of petSummoned.
	 */
	public void setPetSummoned(boolean state) {
		petSummoned = state;
	}

	public boolean getPetSummoned() {
		return petSummoned;
	}

	public final int fletchAmount(final int button) {
		switch (button) {
		case 34185:
		case 34189:
		case 34170:
		case 34174:
		case 34193:
		case 34217:
		case 34205:
		case 34209:
		case 34213:
			return 1;
		case 34192:
		case 34184:
		case 34188:
		case 34169:
		case 34216:
		case 34173:
		case 34204:
		case 34208:
		case 34212:
			return 5;
		case 34191:
		case 34183:
		case 34187:
		case 34168:
		case 34215:
		case 34172:
		case 34203:
		case 34207:
		case 34211:
			return 10;
		case 34190:
		case 34182:
		case 34186:
		case 34214:
		case 34167:
		case 34171:
		case 34202:
		case 34206:
		case 34210:
			return 27;
		}
		return 0;
	}

	public Logs getLogs() {
		return logs;
	}

	public boolean destinationReached() {
		return absX - getMapRegionX() * 8 == finalDestX
				&& absY - getMapRegionY() * 8 == finalDestY && walkingToObject;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public KillingStreak getStreak() {
		return killingStreak;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	/**
	 * Skill Constructors
	 */
	public Slayer getSlayer() {
		return slayer;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}

	public Mining getMining() {
		return mine;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Fishing getFishing() {
		return fish;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public Farming getFarming() {
		return farming;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public Fletching getFletching() {
		return fletching;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public boolean processQueuedPackets() {
		synchronized (queuedPackets) {
			Packet p = null;
			while ((p = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = p.getOpcode();
				packetSize = p.getLength();
				inStream.buffer = p.getPayload().array();
				if (packetType > 0) {
					PacketHandler.processPacket(this, packetType, packetSize);
				}
			}
			return true;
		}
	}

	public boolean noEquipment() {
		if ((playerEquipment[playerHat] == -1)
				&& (playerEquipment[playerCape] == -1)
				&& (playerEquipment[playerAmulet] == -1)
				&& (playerEquipment[playerChest] == -1)
				&& (playerEquipment[playerShield] == -1)
				&& (playerEquipment[playerLegs] == -1)
				&& (playerEquipment[playerHands] == -1)
				&& (playerEquipment[playerFeet] == -1)
				&& (playerEquipment[playerWeapon] == -1)) {
			return true;
		} else {
			return false;
		}
	}

	public Client getClient(int index) {
		return (Client) PlayerHandler.players[index];
	}

	public boolean validClient(int index) {
		Client p = (Client) PlayerHandler.players[index];
		if (p != null && !p.disconnected) {
			return true;
		}
		return false;
	}

	public boolean Area(final int x1, final int x2, final int y1, final int y2) {
		return (absX >= x1 && absX <= x2 && absY >= y1 && absY <= y2);
	}

	public boolean inBank() {
		return Area(3090, 3099, 3487, 3500) || Area(2791, 3153, 2799, 3169)
				|| Area(2963, 2970, 3209, 3216) || Area(3089, 3090, 3492, 3498)
				|| Area(3248, 3258, 3413, 3428) || Area(3179, 3191, 3432, 3448)
				|| Area(2944, 2948, 3365, 3374) || Area(2942, 2948, 3367, 3374)
				|| Area(2944, 2950, 3365, 3370) || Area(3008, 3019, 3352, 3359)
				|| Area(3017, 3022, 3352, 3357) || Area(3203, 3213, 3200, 3237)
				|| Area(3212, 3215, 3200, 3235) || Area(3215, 3220, 3202, 3235)
				|| Area(3220, 3227, 3202, 3229) || Area(3227, 3230, 3208, 3226)
				|| Area(3226, 3228, 3230, 3211) || Area(3227, 3229, 3208, 3226);
	}

	public boolean isStaff() {
		return playerRights == 1 || playerRights == 2 || playerRights == 3;
	}

	public boolean isInJail() {
		if (absX >= 2065 && absX <= 2111 && absY >= 4415 && absY <= 4455) {
			return true;
		}
		return false;
	}

	public void antiFirePotion() {
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer c) {
				antiFirePot = false;
				sendMessage("@red@Your resistance to dragon fire has worn off.");
				c.stop();
			}
		}, 216000);

	}

	public void openBossLog() {
		if (playerRights >= 3) {
			getPA().sendNewString("Boss Log", 8144);
			getPA().sendNewString("Barrows chests: " + barrowsChests + ".",
					8147);
			getPA().sendNewString("Bandos Kills: " + bandosKills + ".", 8148);
			getPA().sendNewString("Armadyl kills: " + armaKills + ".", 8149);
			getPA().sendNewString("Zamorak kills: " + zammyKills + ".", 8150);
			getPA().sendNewString("Saradomin kills: " + saraKills + ".", 8151);
			getPA().sendNewString(
					"Sea Troll Queen kills: " + seatrollKills + ".", 8152);
			getPA().sendNewString("Rex kills: " + rexKills + ".", 8153);
			getPA().sendNewString("Prime kills: " + primeKills + ".", 8154);
			getPA().sendNewString("Supreme kills: " + supremeKills + ".", 8155);
			getPA().sendNewString("King Black Dragon kills: " + kbdKills + ".",
					8156);
			getPA().sendNewString("Mithril Dragon kills: " + mithKills + ".",
					8157);
			getPA().sendNewString("Callisto kills: " + callistoKills + ".",
					8158);
			getPA().sendNewString("Sea Snake kills: " + seaSnakeKills + ".",
					8159);
			getPA().sendNewString("Kalphite Kills: " + kalphiteKills + ".",
					8160);
			getPA().sendNewString("Venenatis Kills: " + venenatisKills + ".",
					8160);
			getPA().sendNewString("Chaos Elemental Kills: " + chaosKills + ".",
					8161);
			getPA().sendNewString("Vet'ion Kills: " + vetionKills + ".",
					8162);
			for (int i = 8164; i < 8200; i++) {
				getPA().sendNewString("", i);
			}
			getPA().showInterface(8134);
			flushOutStream();
		} else {
			sendMessage("You must be a Donator to use this Feature");
			return;
		}
	}

	public void combatPot() {
		if (getItems().playerHasItem(2440) && getItems().playerHasItem(2436)
				&& getItems().playerHasItem(2442)
				&& getItems().playerHasItem(2998)) {
			getItems().deleteItem(2440, 1);
			getItems().deleteItem(2436, 1);
			getItems().deleteItem(2442, 1);
			getItems().deleteItem(2998, 1);
			getPA().addSkillXP(300 * Config.HERBLORE_EXPERIENCE, playerHerblore);
			sendMessage("You combine all the potions together...");
			EventManager.getSingleton().addEvent(new Event() {

				@Override
				public void execute(EventContainer c) {
					getItems().addItem(12695, 1);
					sendMessage("And you make a Super Combat Potion (4).");
					c.stop();
				}
			}, 1000);
		}
	}

	public double time = -1;
	public boolean doubleExp;
	
	public int recoilDamage = 40;

	public boolean getDoubleExp() {
		return doubleExp;
	}

	public boolean setDoubleExp(boolean bool) {
		this.doubleExp = bool;
		return bool;
	}

	public int cyclesToMilliseconds(int cycles) {
		return cycles * 600;
	}

	public void doubleEXPTicket(double time) {
		this.time = time;
		sendMessage("You currently have double XP for: " + parseTime((long) time));
		setDoubleExp(true);
	}

	public void agilityDelay(int Emote, final int X, final int Y, final int H,
			int Req, int amtEXP, String message) {
		if (playerLevel[16] >= Req) {
			startAnimation(Emote);
			agilityEmote = true;
			getPA().addSkillXP(amtEXP, playerAgility);
			EventManager.getSingleton().addEvent(new Event() {
				@Override
				public void execute(EventContainer c) {
					getPA().movePlayer(X, Y, H);
					agilityEmote = false;
					c.stop();
				}
			}, 1000);
		} else {
			sendMessage("You Need " + Req + " Agility To Do This Obsticle");
		}
	}

	public boolean safeAreas(int x, int y, int x1, int y1) {
		return (absX >= x && absX <= x1 && absY >= y && absY <= y1);
	}

	private CycleEvent skilling;
	private int skillTask;
	private int oldSkillTask;
	private int currentSkillTask;
	public long agilityDelay;
	public int price;
	public int postItemId = 0;
	public ArrayList<Integer> itemList = new ArrayList<Integer>();
	public int setup = 0;
	public boolean isBanking = false;
	public int barrowsChests = 0;
	public boolean isTeleporting = false;

	public void setSkilling(CycleEvent event) {
		this.skilling = event;
	}

	public CycleEvent getSkilling() {
		return skilling;
	}

	public void resetAllActions() {
		setCurrentSkillTask();
		getTask();
		attr(A.REGULAR_FOLLOWING, false);
	}

	public void resetSkillActions() {
		getTask();
		setCurrentSkillTask();
	}

	public void setSkillTask(int skillTask) {
		this.skillTask = skillTask;
	}

	public int getTask() {
		skillTask++;
		if (skillTask > Integer.MAX_VALUE - 2) {
			skillTask = 0;
		}
		return skillTask;
	}

	public boolean checkTask(int task) {
		return task == skillTask;
	}

	public void setNewSkillTask() {
		currentSkillTask++;
		oldSkillTask = currentSkillTask;
		if (oldSkillTask > Integer.MAX_VALUE - 2
				|| currentSkillTask > Integer.MAX_VALUE - 2) {
			oldSkillTask = 0;
			currentSkillTask = 0;
		}
	}

	public void setCurrentSkillTask() {
		currentSkillTask++;
	}

	public boolean checkNewSkillTask() {
		return oldSkillTask == currentSkillTask;
	}

	public static int[][] bounties1 = { { 12746, 50000 }, { 12748, 100000 },
		{ 12749, 200000 }, { 12750, 400000 }, { 12751, 750000 },
		{ 12752, 1200000 }, { 12753, 1750000 }, { 12754, 2500000 },
		{ 12755, 3500000 }, { 12756, 5000000 } };

	public int bountyTotal;

	public void calculateTotal() {
		int value1 = 0;
		int value2 = 0;
		int value3 = 0;
		int value4 = 0;
		int value5 = 0;
		int value6 = 0;
		int value7 = 0;
		int value8 = 0;
		int value9 = 0;
		int value10 = 0;
		for (int i = 0; i < id.length; i++) {
			if (getItems().playerHasItem(bounties1[0][0], 1)
					|| getItems().playerHasItem(bounties1[1][0], 1)
					|| getItems().playerHasItem(bounties1[2][0], 1)
					|| getItems().playerHasItem(bounties1[3][0], 1)
					|| getItems().playerHasItem(bounties1[4][0], 1)
					|| getItems().playerHasItem(bounties1[5][0], 1)
					|| getItems().playerHasItem(bounties1[6][0], 1)
					|| getItems().playerHasItem(bounties1[7][0], 1)
					|| getItems().playerHasItem(bounties1[8][0], 1)
					|| getItems().playerHasItem(bounties1[9][0], 1)) {
				value1 = getItems().getItemAmount(bounties1[0][0])
						* bounties1[0][1];
				value2 = getItems().getItemAmount(bounties1[1][0])
						* bounties1[1][1];
				value3 = getItems().getItemAmount(bounties1[2][0])
						* bounties1[2][1];
				value4 = getItems().getItemAmount(bounties1[3][0])
						* bounties1[3][1];
				value5 = getItems().getItemAmount(bounties1[4][0])
						* bounties1[4][1];
				value6 = getItems().getItemAmount(bounties1[5][0])
						* bounties1[5][1];
				value7 = getItems().getItemAmount(bounties1[6][0])
						* bounties1[6][1];
				value8 = getItems().getItemAmount(bounties1[7][0])
						* bounties1[7][1];
				value9 = getItems().getItemAmount(bounties1[8][0])
						* bounties1[8][1];
				value10 = getItems().getItemAmount(bounties1[9][0])
						* bounties1[9][1];
			}
			bountyTotal = value1 + value2 + value3 + value4 + value5 + value6
					+ value7 + value8 + value9 + value10;
		}
	}

	public static int[] emblems = { 12746, 12748, 12749, 12750, 12751, 12752,
		12753, 12754, 12755, 12756 };
	public static int[][] pkpRewards = { { 12746, 1 }, { 12748, 2 },
		{ 12749, 3 }, { 12750, 4 }, { 12751, 6 }, { 12752, 8 },
		{ 12753, 10 }, { 12754, 12 }, { 12755, 15 }, { 12756, 20 } };

	public int pkpTotal;

	public void calculateTotalPKP() {
		int value1 = 0;
		int value2 = 0;
		int value3 = 0;
		int value4 = 0;
		int value5 = 0;
		int value6 = 0;
		int value7 = 0;
		int value8 = 0;
		int value9 = 0;
		int value10 = 0;
		for (int i = 0; i < id.length; i++) {
			if (getItems().playerHasItem(pkpRewards[0][0], 1)
					|| getItems().playerHasItem(pkpRewards[1][0], 1)
					|| getItems().playerHasItem(pkpRewards[2][0], 1)
					|| getItems().playerHasItem(pkpRewards[3][0], 1)
					|| getItems().playerHasItem(pkpRewards[4][0], 1)
					|| getItems().playerHasItem(pkpRewards[5][0], 1)
					|| getItems().playerHasItem(pkpRewards[6][0], 1)
					|| getItems().playerHasItem(pkpRewards[7][0], 1)
					|| getItems().playerHasItem(pkpRewards[8][0], 1)
					|| getItems().playerHasItem(pkpRewards[9][0], 1)) {
				value1 = getItems().getItemAmount(pkpRewards[0][0])
						* pkpRewards[0][1];
				value2 = getItems().getItemAmount(pkpRewards[1][0])
						* pkpRewards[1][1];
				value3 = getItems().getItemAmount(pkpRewards[2][0])
						* pkpRewards[2][1];
				value4 = getItems().getItemAmount(pkpRewards[3][0])
						* pkpRewards[3][1];
				value5 = getItems().getItemAmount(pkpRewards[4][0])
						* pkpRewards[4][1];
				value6 = getItems().getItemAmount(pkpRewards[5][0])
						* pkpRewards[5][1];
				value7 = getItems().getItemAmount(pkpRewards[6][0])
						* pkpRewards[6][1];
				value8 = getItems().getItemAmount(pkpRewards[7][0])
						* pkpRewards[7][1];
				value9 = getItems().getItemAmount(pkpRewards[8][0])
						* pkpRewards[8][1];
				value10 = getItems().getItemAmount(pkpRewards[9][0])
						* pkpRewards[9][1];
				pkpTotal = value1 + value2 + value3 + value4 + value5 + value6
						+ value7 + value8 + value9 + value10;
			}
		}
	}

	public int emblemTotal;

	public int calculateEmblemTotal() {
		int value1 = 0;
		int value2 = 0;
		int value3 = 0;
		int value4 = 0;
		int value5 = 0;
		int value6 = 0;
		int value7 = 0;
		int value8 = 0;
		int value9 = 0;
		int value10 = 0;
		for (int i = 0; i < id.length; i++) {
			if (getItems().playerHasItem(pkpRewards[0][0], 1)
					|| getItems().playerHasItem(pkpRewards[1][0], 1)
					|| getItems().playerHasItem(pkpRewards[2][0], 1)
					|| getItems().playerHasItem(pkpRewards[3][0], 1)
					|| getItems().playerHasItem(pkpRewards[4][0], 1)
					|| getItems().playerHasItem(pkpRewards[5][0], 1)
					|| getItems().playerHasItem(pkpRewards[6][0], 1)
					|| getItems().playerHasItem(pkpRewards[7][0], 1)
					|| getItems().playerHasItem(pkpRewards[8][0], 1)
					|| getItems().playerHasItem(pkpRewards[9][0], 1)) {
				value1 = getItems().getItemAmount(pkpRewards[0][0]);
				value2 = getItems().getItemAmount(pkpRewards[1][0]);
				value3 = getItems().getItemAmount(pkpRewards[2][0]);
				value4 = getItems().getItemAmount(pkpRewards[3][0]);
				value5 = getItems().getItemAmount(pkpRewards[4][0]);
				value6 = getItems().getItemAmount(pkpRewards[5][0]);
				value7 = getItems().getItemAmount(pkpRewards[6][0]);
				value8 = getItems().getItemAmount(pkpRewards[7][0]);
				value9 = getItems().getItemAmount(pkpRewards[8][0]);
				value10 = getItems().getItemAmount(pkpRewards[9][0]);
			}
			emblemTotal = value1 + value2 + value3 + value4 + value5 + value6
					+ value7 + value8 + value9 + value10;
		}
		return emblemTotal;
	}

	public int countItems(int id) {
		switch (id) {
		case 12746:
		case 12748:
		case 12749:
		case 12750:
		case 12751:
		case 12752:
		case 12753:
		case 12754:
		case 12755:
		case 12756:
			return getItems().getItemAmount(id);
		}
		return 0;
	}

	public int id[] = { 12746, 12748, 12749, 12750, 12751, 12752, 12753, 12754,
			12755, 12756 };

	public void dropRandomEmblem(Client o, Client c) {
		Server.itemHandler.createGroundItem(o, 12746, c.getX(), c.getY(), 1,
				c.killerId);

	}

	public void upgradeEmblem() {
		if (getItems().playerHasItem(12755)) {
			getItems().deleteItem(12755, 1);
			getItems().addItem(12756, 1);
			sendMessage("Your Emblem has been upgraded to Tier 10");
			return;
		} else if (getItems().playerHasItem(12754)) {
			getItems().deleteItem(12754, 1);
			getItems().addItem(12755, 1);
			sendMessage("Your Emblem has been upgraded to Tier 9");
			return;
		} else if (getItems().playerHasItem(12753)) {
			getItems().deleteItem(12753, 1);
			getItems().addItem(12754, 1);
			sendMessage("Your Emblem has been upgraded to Tier 8");
			return;
		} else if (getItems().playerHasItem(12752)) {
			getItems().deleteItem(12752, 1);
			getItems().addItem(12753, 1);
			sendMessage("Your Emblem has been upgraded to Tier 7");
			return;
		} else if (getItems().playerHasItem(12751, 1)) {
			getItems().deleteItem(12751, 1);
			getItems().addItem(12752, 1);
			sendMessage("Your Emblem has been upgraded to Tier 6");
			return;
		} else if (getItems().playerHasItem(12750, 1)) {
			getItems().deleteItem(12750, 1);
			getItems().addItem(12751, 1);
			sendMessage("Your Emblem has been upgraded to Tier 5");
			return;
		} else if (getItems().playerHasItem(12749, 1)) {
			getItems().deleteItem(12749, 1);
			getItems().addItem(12750, 1);
			sendMessage("Your Emblem has been upgraded to Tier 4");
			return;
		} else if (getItems().playerHasItem(12748, 1)) {
			getItems().deleteItem(12748, 1);
			getItems().addItem(12749, 1);
			sendMessage("Your Emblem has been upgraded to Tier 3");
			return;
		} else if (getItems().playerHasItem(12746, 1)) {
			getItems().deleteItem(12746, 1);
			getItems().addItem(12748, 1);
			sendMessage("Your Emblem has been upgraded to Tier 2");
			return;
		}
	}

	public boolean inClanWarsCoords() {
		return Area(2375, 2432, 3069, 3136);
	}

	private TeleportingTabs tabs = new TeleportingTabs();
	public boolean searchingBank = false;
	public boolean editingClan = false;
	public int claimDelay = 0;

	public TeleportingTabs getTablet() {
		return tabs;
	}

	private TimePlayed played = new TimePlayed(this);

	public TimePlayed getTimePlayed() {
		return played;
	}

	private Pins pins = new Pins(this);

	public int magicDamage1, maxMageDamage;

	public Pins getBankPin() {
		return pins;
	}

	public Map<Integer, String> stringMap = new HashMap<Integer, String>();

	/**
	 * Quick prayers
	 */
	public boolean[] quickCurses = new boolean[QuickCurses.MAX_CURSES];
	public boolean[] quickPrayers = new boolean[QuickPrayers.MAX_PRAYERS];
	public boolean quickCurseActive, quickPray, quickCurse, quickPrayersOn;
	public long karamDelay;
	public int chestDelay;

	public Map<Integer, String> getStringMap() {
		return stringMap;
	}

	public int getCarriedWealth() {
		int toReturn = 0;
		for (int i = 0; i < playerEquipment.length; i++) {
			if (playerEquipment[i] > 0
					&& Server.itemHandler.ItemList[playerEquipment[i]] != null)
				toReturn += (Server.itemHandler.ItemList[playerEquipment[i]].ShopValue * playerEquipmentN[i]);
		}
		for (int i = 0; i < playerItems.length; i++) {
			if (playerItems[i] > 0
					&& Server.itemHandler.ItemList[playerItems[i]] != null)
				toReturn += (Server.itemHandler.ItemList[playerItems[i]].ShopValue * playerItemsN[i]);
		}
		return toReturn;
	}

	public int getWealth() {
		int toReturn = 0;
		for (int i = 0; i < playerEquipment.length; i++) {
			if (playerEquipment[i] > 0) {
				toReturn += (getShops().getItemShopValue(playerEquipment[i]) * playerEquipmentN[i]);
			}
		}
		int toReturn2 = 0;
		for (int i = 0; i < playerItems.length; i++) {
			if (playerItems[i] > 0) {
				toReturn2 += (getShops().getItemShopValue(playerItems[i]) * playerItemsN[i]);
			}
		}
		return toReturn + toReturn2;
	}

	public void sendGlobal(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage(message);
			}
		}
	}

	private String statedInterface;

	public void setStatedInterface(String statedInterface) {
		this.statedInterface = statedInterface;
	}

	public String getStatedInterface() {
		return statedInterface;
	}

	private String[] ironDonators = { };
	private String[] owner = {  };
	private String[] moderator = {  };
	private String[] helper = {  };
	private String[] trusted = { };
	
	public boolean ironDonator(String name) {
		if (ironDonator)
			return true;
		for (int i = 0; i < ironDonators.length; i++) {
			if (ironDonators[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isOwner(String name) {
		for (int i = 0; i < owner.length; i++) {
			if (owner[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isModerator(String name) {
		for (int i = 0; i < moderator.length; i++) {
			if (moderator[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isHelper(String name) {
		for (int i = 0; i < helper.length; i++) {
			if (helper[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTrusted(String name) {
		for (int i = 0; i < trusted.length; i++) {
			if (trusted[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	private int pouchData[] = { 0, 0, 0, 0 };
	public int collectDelay;
	public byte insert;

	public int getPouchData(int i) {
		return pouchData[i];
	}

	public void setPouchData(int i, int amount) {
		pouchData[i] = amount;
	}

	public int[] easterEggs = { 7928, 7929, 7930, 7931, 7932, 7933 };
	public int droppedItem = -1;
	public boolean wonDuel = false;
	public Player playerTarget;
	public long lastTarget;
	public int targetTicks;

	public boolean hasEggs() {
		return (getItems().playerHasItem(7928)
				&& getItems().playerHasItem(7929)
				&& getItems().playerHasItem(7930)
				&& getItems().playerHasItem(7931)
				&& getItems().playerHasItem(7932) && getItems().playerHasItem(
						7933));
	}

	public void handleBhTeleport() {
		if (targetIndex != 0) {
			if (PlayerHandler.players[targetIndex].inWild() && inWild()) {
				if (getItems().playerHasItem(562, 1)
						&& getItems().playerHasItem(563, 1)
						&& getItems().playerHasItem(560, 1) && wildLevel <= 20) {
					playerTarget = PlayerHandler.players[targetIndex];
					int xDifference = playerTarget.absX - Misc.random(7);
					int yDifference = playerTarget.absY - Misc.random(7);
					if (yDifference < 3524) {
						yDifference = 3525;
					}
					getItems().deleteItem(562, 1);
					getItems().deleteItem(563, 1);
					getItems().deleteItem(560, 1);
					if (Region.getClipping(xDifference - 1, yDifference,
							playerTarget.heightLevel, -1, 0)) {
						getPA().startTeleport(xDifference - 1, yDifference,
								playerTarget.heightLevel, "modern");
					} else if (Region.getClipping(xDifference + 1, yDifference,
							playerTarget.heightLevel, -1, 0)) {
						getPA().startTeleport(xDifference + 1, yDifference,
								playerTarget.heightLevel, "modern");
					} else if (Region.getClipping(xDifference, yDifference - 1,
							playerTarget.heightLevel, -1, 0)) {
						getPA().startTeleport(xDifference, yDifference - 1,
								playerTarget.heightLevel, "modern");
					} else if (Region.getClipping(xDifference, yDifference + 1,
							playerTarget.heightLevel, -1, 0)) {
						getPA().startTeleport(xDifference, yDifference + 1,
								playerTarget.heightLevel, "modern");
					}
				} else {
					sendMessage("This Spell requires 1 death rune, 1 law rune, and 1 chaos rune.");
					return;
				}
			} else if (!PlayerHandler.players[targetIndex].inWild()) {
				sendMessage("Your target must be in the Wilderness to teleport to them!");
				return;
			} else if (!inWild()) {
				sendMessage("You must be in the Wilderness to teleport to your target!");
				return;
			} else if (wildLevel > 20) {
				sendMessage("You cannot teleport above level 20 wilderness!");
				return;
			}
		} else {
			sendMessage("You don't currently have a target.");
		}
	}

	public int hunterPoints;

	public int EP, EP_MINUTES, targetPercentage, targetIndex, safeTimer = 300,
			logoutTimer, dropWealth;
	public boolean EP_ACTIVE;
	public String targetName;

	public int hunterStreak, hunterRecord, rogueStreak, rogueRecord = 0;

	public int dartType = 0;

	public int slot;

	public int setSlot(int slot) {
		this.slot = slot;
		return slot;
	}

	public int getSlot() {
		return slot;
	}

	private Magic magic = new Magic(this);

	public Magic getMagic() {
		return magic;
	}

	/**
	 * The force walk variables.
	 */
	private int[] forceWalk;

	public boolean followingPlayer = false;

	public boolean isCutting;

	public int[] getForceWalk() {
		return forceWalk;
	}

	/*
	 * public void setForceWalk(final int[] forceWalk, final boolean
	 * removeAttribute) { this.forceWalk = forceWalk; if(forceWalk.length > 0) {
	 * CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
	 * 
	 * @Override public void execute(CycleEventContainer container) {
	 * getPA().walkTo3(i, j); this.stop(); }
	 * 
	 * @Override public void stop() { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * }, forceWalk[7]); } }
	 */

	public int[] infinity = { 6924, 6918, 6916 };

	public boolean isMakingPots = false;

	public int herbUsed;

	public int usedWithSlot, itemUsedSlot, itemUsed, useWith;

	private long lastFire;

	public long getLastFire() {
		return lastFire;
	}

	public void setLastFire(long lastFire) {
		this.lastFire = lastFire;
	}

	public boolean canMove(int x, int y) {
		return Region.canMove(getX(), getY(), getX() + x, getY() + y,
				heightLevel, 1, 1);
	}

	public int bagItem;

	public int bagItemSlot;

	public int achievements;

	public boolean justSwitched;

	public boolean stakeHax = false;

	public boolean replaced = false;

	public long venomImmunity;

	public long lastVenomSip;

	public boolean venomDebuff;

	public int venomDamage = 6;

	public boolean acbSpec;


	public boolean ironDonator = false;

	public boolean canWalk = true;
	public boolean canClick = true;

	public static int[][] format = { { 11695, 11803 }, { 11697, 11805 },
			{ 11699, 11807 }, { 11701, 11809 }, { 15221, 11774 },
			{ 15021, 11773 }, { 15020, 11772 }, { 15019, 11772 },
			{ 15052, 12932, }, { 12608, 12905 }, { 15008, 12003 },
			{ 11711, 11819 }, { 11713, 11821 }, { 11715, 11823 },
			{ 11717, 11890 }, { 11719, 11827 }, { 11721, 11829 },
			{ 11723, 11831 }, { 11725, 11833 }, { 11727, 11835 },
			{ 11729, 11837 }, { 11733, 11841 }, { 12009, 11908 },
			{ 11733, 11840 }, { 12441, 12955 }, { 15007, 11927 },
			{ 15008, 11925 }, { 15006, 11792 }, { 15009, 12003 } };

	public void replaceFormat() {//it didnt convert 4u cuz this word lol you already converted b4 xD
		//kk.
		if (!replaced) {
			replaced = true;
			for (int i = 0; i < playerItems.length; i++) {
				for (int i2 = 0; i2 < format.length; i2++) {
					if (playerItems[i] == format[i2][0]) {
						playerItems[i] = format[i2][1];
					}
				}
			}
			getItems().updateInventory();
			for (int i = 0; i < playerEquipment.length; i++) {
				for (int i2 = 0; i2 < format.length; i2++) {
					if (playerEquipment[i] == format[i2][0] - 1) {
						playerEquipment[i] = format[i2][1] - 1;
						getItems().updateSlot(i);
					}
				}
			}
			getPA().requestUpdates();
			for (int i = 0; i <= 8; i++) {
				bankingTab = i;
				convertBank();
			}
			bankingTab = 0;
			sendMessage("Conversion successful.");
			getItems().resetItems(3214);
			getItems().resetItems(5064);
		}
	}

	public void convertBank() {
		if (bankingTab == 0) {// ur way doesnt work. ill brb i gotta go eat supper
			bankingItems = bankItems;
			bankingItemsN = bankItemsN;
		}
		if (bankingTab == 1) {
			bankingItems = bankItems1;
			bankingItemsN = bankItems1N;
		}
		if (bankingTab == 2) {
			bankingItems = bankItems2;
			bankingItemsN = bankItems2N;
		}
		if (bankingTab == 3) {
			bankingItems = bankItems3;
			bankingItemsN = bankItems3N;
		}
		if (bankingTab == 4) {
			bankingItems = bankItems4;
			bankingItemsN = bankItems4N;
		}
		if (bankingTab == 5) {
			bankingItems = bankItems5;
			bankingItemsN = bankItems5N;
		}
		if (bankingTab == 6) {
			bankingItems = bankItems6;
			bankingItemsN = bankItems6N;
		}
		if (bankingTab == 7) {
			bankingItems = bankItems7;
			bankingItemsN = bankItems7N;
		}
		if (bankingTab == 8) {
			bankingItems = bankItems8;
			bankingItemsN = bankItems8N;
		}
		System.out.println(bankingItems.length);
		for (int i2 = 0; i2 < bankingItems.length; i2++) {
			for (int i3 = 0; i3 < format.length; i3++) {
				if (bankingItems[i2] == format[i3][0]) {
					bankingItems[i2] = format[i3][1];
				}
			}
		}
	}

	public void flushIronDonators(String name) {
		try {
			for (String iron : Server.ironDonatorMap) {
				if (name.equalsIgnoreCase(iron)) {
					System.out.println("Donator already exists.");
					return;
				}
			}
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new FileWriter("./Data/extras/irondonator.txt", true)));
			out.println(name + "");
			out.close();
			Server.ironDonatorMap.add(name);
			System.out.println("Successfully Flushed Donators.");// should be good now i think
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PriceChecker priceChecker = new PriceChecker(this);

	public boolean movingPlayer;
	
	public PriceChecker getPriceChecker() {
		return priceChecker;
	}
	public MoneyPouch getPouch() {
		return pouch;
	}
	public void setPouch(MoneyPouch pouch) {
		this.pouch = pouch;
	}
}
