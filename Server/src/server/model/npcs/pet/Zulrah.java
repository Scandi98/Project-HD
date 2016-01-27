package server.model.npcs.pet;
/*package server.model.npcs;

import java.util.ArrayList;
import java.util.Random;

import server.Server;
import server.event.test.CycleEvent;
import server.event.test.CycleEventContainer;
import server.event.test.CycleEventHandler;
import server.model.players.Client;
import server.tick.Task;
import server.tick.TaskHandler;
import server.util.Misc;

@SuppressWarnings("unused")
public class Zulrah extends NPC {
	static int type;
	static int move;
	ArrayList<Zulrah> ActiveZulrah = new ArrayList<Zulrah>();
	private static int HITPOINTS = 500,
			ATTACK_VALUE = 300,
			MAX_HIT = 35,
			DEFENSE_VALUE= 300;
	private enum STAGE {
		MAGE,RANGE,MINION,MELEE;
	}
	private STAGE stage;
	private static final int RANGE = 2042;
	private static final int MELEE = 2043;
	private static final int MAGE = 2044;
	private static final int SNAKELING = 2045;

	private static final int SPAWN_ANIM = 5071;
	private static final int SPAWN_BELOW = 5073;

	public Zulrah (int npcSlot, int npcType) {
		super(npcSlot, npcType);
		type = Misc.random(3);
		switch (type) {
		case 1:
			stage = STAGE.RANGE;
			break;
		case 2:
			stage = STAGE.MAGE;
			break;
		case 3:
			stage = STAGE.MELEE;
			break;
		default:
		case 0:
			stage = STAGE.MINION;
			break;
		}
	}
	static long startTime;
	public static void setupTimer(Client player) {
		startTime = System.currentTimeMillis();
		player.getPA().walkableInterface(8888);
		handleTimer(player);
	}
	static String finalTime;
	private static void handleTimer(Client player) {
		TaskHandler.submit(new Task(0, true) {
			@Override
			public void execute() {
				if (player.zulrah.isDead)
					return;
				long elapsedTime = System.currentTimeMillis() - startTime;
				long elapsedSeconds = elapsedTime / 1000;
				long secondsDisplay = elapsedSeconds % 60;
				long elapsedMinutes = elapsedSeconds / 60;
				finalTime = elapsedMinutes+":"+secondsDisplay;
				player.getPA().walkableInterface(8888);
				int meleeInterface = 8888;
				int magicInterface = 8900;
				int rangeInterface = 9000;
				switch(player.zulrah.npcId) {
				case MELEE:
					//player.getPA().walkableInterface(meleeInterface);
					//player.getPA().sendFrame126("Time: "+finalTime, 8889);
					break;
				case RANGE:
					//player.getPA().walkableInterface(rangeInterface);
					//player.getPA().sendFrame126("Time: "+finalTime, 9002);
					break;
				case MAGE:
					//player.getPA().walkableInterface(magicInterface);
					//player.getPA().sendFrame126("Time: "+finalTime, 8902);
					break;
				}
			}
		});		
	}
	public static void endTimer(Client player) {
		String displayFinalTime = null;
		if (player.zulrah.isDead) {
			//displayFinalTime = finalTime;
			player.sendMessage("@dre@Final time: "+displayFinalTime);
			//player.getPA().walkableInterface(8888);
			//player.getPA().sendFrame126(displayFinalTime+"", 8889);
		}
	}
	public static void init(Client player) {
		Zulrah z;
		player.getPA().startTeleport(2267, 3070, player.playerId * 4, "modern");
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.sendMessage("@dre@You teleport to Zulrah's Shrine.");
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": teleport to Zulrah.");
			}
		}, 1);
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.zulrah = (Zulrah) NPCHandler.spawnZulrah(player, 2042, 2266, 3072, player.playerId * 4, 0, HITPOINTS, 35, ATTACK_VALUE, DEFENSE_VALUE, SPAWN_ANIM, "Ssss..");
				
				Server.npcHandler.spawnNpc(player, 2042, 2266, 3072, player.playerId * 4, -1, 500, 41, 500, 500, false, false);
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah spawned.");
			}
		}, 5);
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {

				move(player);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 50);
		z = player.zulrah;
	}
	public static void destruct(Client player) {
		Zulrah z = player.zulrah;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.zulrah.absX = 0;
				player.zulrah.absY = 0;
				player.zulrah.updateRequired = true;
				player.zulrah.npcId = -1;
				player.zulrah.isDead = true;
				player.zulrah.updateRequired = true;
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah destruct.");
			}
		}, 1);
		//player.sendMessage("@lre@Zulrah took notice of your cowardly efforts..");
	}
	static NPC snake[] = new NPC[5];
	private static void constructMinions(Client player) {
		if (player.zulrah.isDead == true)
			return;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				snake[0] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2263, 3075, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[1] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2263, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[2] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2268, 3069, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[3] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2273, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[4] = (NPC) NPCHandler.spawnSnakeling(player, SNAKELING, 2273, 3077, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				container.stop();
			}

			@Override
			public void stop() {
				player.sendMessage("Zulrah has spawned her minions!");
			}
		}, 1);
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				destructMinions(player);
				container.stop();
			}

			@Override
			public void stop() {
				player.sendMessage("Zulrah has spawned her minions!");
			}
		}, 100);
	}
	private static void destructMinions(Client player) {
		if (player.zulrah.isDead == true)
			return;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				for (int i = 0; i < snake.length; i++) {
					snake[i].absX = 0;
					snake[i].absY = 0;
					snake[i].isDead = true;
					snake[i].needRespawn = false;
					snake[i].respawns = false;
					snake[i].updateRequired = true;
				}
				container.stop();
			}

			@Override
			public void stop() {
				player.sendMessage("Zulrah has spawned her minions!");
			}
		}, 1);
	}
	private static void constructClouds(Client player) {
		if (player.zulrah.isDead == true)
			return;
		int cloud = 11700;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getPA().object(cloud, 2263, 3075, 1, 10);
				player.getPA().object(cloud, 2263, 3071, 1, 10);
				player.getPA().object(cloud, 2268, 3069, 1, 10);
				player.getPA().object(cloud, 2273, 3071, 1, 10);
				player.getPA().object(cloud, 2273, 3077, 1, 10);
				player.getPA().object(cloud, 2273, 3075, 1, 10);
				
				container.stop();
			}

			@Override
			public void stop() {
				player.sendMessage("Zulrah has excreted poisonous clouds!");
			}
		}, 1);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				destructClouds(player);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 75);

	}
	private static void destructClouds(Client player) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getPA().object(-1, 2263, 3075, 1, 10);
				player.getPA().object(-1, 2263, 3071, 1, 10);
				player.getPA().object(-1, 2268, 3069, 1, 10);
				player.getPA().object(-1, 2273, 3071, 1, 10);
				player.getPA().object(-1, 2273, 3077, 1, 10);
				player.getPA().object(-1, 2273, 3075, 1, 10);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 1);
	}
	public static void move(Client player) {
		Zulrah z = player.zulrah;
		if (player.zulrah.isDead == true)
			return;
		int possibleX[] = {2266, 2256, 2276, 2273};
		int possibleY[] = {3072, 3073, 3074, 3064};
		Random r = new Random();
		int next = r.nextInt(possibleX.length);
		System.out.println(next);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {

				z.forceChat("Sssss...");
				z.doAnimation(5072);
				player.sendMessage("Zulrah dives into the swamp..");
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah move method called.");
			}
		}, 1);
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				z.absX = 0;
				z.absY = 0;
				z.updateRequired = true;
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah has moved.");
			}
		}, 6);
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				z.absX = possibleX[next];
				z.absY = possibleY[next];
				z.makeX = possibleX[next];
				z.makeY = possibleY[next];
				z.heightLevel = player.heightLevel;
				z.updateRequired = true;
				z.npcId = getNextForm();
				z.turnNpc(player.absX, player.absY);
				z.doAnimation(SPAWN_ANIM);
				container.stop();
			}

			private int getNextForm() {
				Random r = new Random();
				int next = r.nextInt(3) + 1;
				switch (next) {
				case 1:
					System.out.println(player.playerName + " [Zulrah] MELEE");
					constructClouds(player);
					beginMoveTimer();
					return MELEE;
				case 2:
					System.out.println(player.playerName + " [Zulrah] MAGE");
					constructMinions(player);
					beginMoveTimer();
					return MAGE;
				case 3:
					System.out.println(player.playerName + " [Zulrah] RANGE");
					beginMoveTimer();
					return RANGE;
				}
				return 0;
			}
			private void beginMoveTimer() {
				if (!player.inZulrahShrine()) {
					Zulrah.destruct(player);
					return;
				}
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {

						move(player);
						container.stop();
					}

					@Override
					public void stop() {
						System.out.println(player.playerName + ": Zulrah move began.");
					}
				}, 100);
				

			}
			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah has reappeared on the map.");
			}
		}, 8);
		
	}
	public static void handleDrops(Client player) {
		Random r = new Random();
		int teleportAmt = r.nextInt(5);		
		int uniqueList[] = {12922, 12932, 12927, 6571};
		int unique = Misc.random(328);
		int unique2 = Misc.random(328);
		if (unique == 1)
			Server.itemHandler.createGroundItem(player, uniqueList[Misc.random(uniqueList.length) + 1], player.absX, player.absY, 1, player.getId());
		if (unique2 == 1)
			Server.itemHandler.createGroundItem(player, uniqueList[Misc.random(uniqueList.length) + 1], player.absX, player.absY, 1, player.getId());
		Server.itemHandler.createGroundItem(player, 12934, player.absX, player.absY, Misc.random(200) + 100, player.getId());
		Server.itemHandler.createGroundItem(player, 12938, player.absX, player.absY, teleportAmt, player.getId());
		cleanUp(player);
		handlePetChance(player);
	}
	private static void cleanUp(Client player) {
		player.zulrah.absX = 0;
		player.zulrah.absY = 0;
		player.zulrah.respawns = false;
		player.zulrah.isDead = true;
		player.zulrah.updateRequired = true;
		destructClouds(player);
		destructMinions(player);
		System.out.println("Zulrah clean-up complete.");

	}
	public static void handlePetChance(Client player) {
		int petChance = Misc.random(900);
		if (petChance == 900) {
			player.getItems().addItemToBank(12921, 1);
			if (player.getItems().hasPetItem(12921))
				return;
			else {
				player.getItems().addItemToBank(12921, 1);
				player.sendMessage("Your pet Snakeling has been added to your bank!");
				player.getwM().globalMessage(player.playerName + " has received a @dre@Pet Zulrah @bla@drop! Congratulations.");
			}
		}
	}
}
*/