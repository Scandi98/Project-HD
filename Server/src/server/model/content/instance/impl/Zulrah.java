/*
package server.model.content.instance.impl;

import java.util.Random;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.players.Boundary;
import server.model.players.Client;
import server.util.Misc;

public class Zulrah extends NPC {
	
	static int type;
	private static final int RANGE = 2042;
	private static final int MELEE = 2043;
	private static final int MAGE = 2044;
	private static final int SNAKELING = 2045;
	@Override
	public void attack(NPC npc) {
		npc.attackType = Misc.random(1) == 0 ? 1 : 0;
		if (npc.stage > 15) {
			npc.attackType = 2;
			switch (npc.attackType) {
			case 2:
				npc.projectileId = 162;
				npc.endGfx = 477;
				break;
			}
		}
		switch (npc.attackType) {
		case 2: //Mage
			npc.projectileId = 162;
			npc.endGfx = 477;
		break;
		case 1: // Range
			npc.projectileId = 475;
			break;
		}
		npc.stage++;
		if (npc.stage == 15) {
			npc.doTransform(2043);
			npc.currentHealth += 100;
			npc.forceChat("I cannot be defeated!");
		}
		if (npc.stage == 30) {
			npc.doTransform(2044);
			npc.forceChat("Feel my wrath!");
			npc.stage = 0;
		}
	}
	public Zulrah(int npcSlot, int npcType) {
		super(npcSlot, npcType);
		int type = Misc.random(3);
		switch (type) {
		case 1:
			stage = RANGE;
			break;
		case 2:
			stage = MAGE;
			break;
		case 3:
			stage = MELEE;
			break;
		default:
		case 0:
			stage = SNAKELING;
			break;
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
				//player.zulrah = NPCHandler.spawnZulrah(player, 2042, 2266, 3072, player.playerId * 4, -1, 500, 35, 300, 300, 5071, "Ssss..", true);
				
				player.zulrah = (Zulrah) NPCHandler.spawnNpc(player,
						2042,
						2266,
						3072,
						player.playerId * 4,
						0, 500, 41, 500, 500, 
						true, false, 5071, "Ssss...");
				
				//player.zulrah = NPCHandler.spawnZulrah(player, 2042, 2266, 3072, player.playerId * 4, -1, 500, 41, 500, 500, false, false);
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
				//destructClouds(player);
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
	static NPC snake[] = new NPC[5];
	private static void constructMinions(Client player) {
		if (player.zulrah.isDead == true)
			return;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				snake[0] = (NPC) NPCHandler.spawnSnakeling(player, 2045, 2263, 3075, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[1] = (NPC) NPCHandler.spawnSnakeling(player, 2045, 2263, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[2] = (NPC) NPCHandler.spawnSnakeling(player, 2045, 2268, 3069, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[3] = (NPC) NPCHandler.spawnSnakeling(player, 2045, 2273, 3071, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				snake[4] = (NPC) NPCHandler.spawnSnakeling(player, 2045, 2273, 3077, player.heightLevel, 0, 1, 1, 999, 1, "Ayy lmao");
				

				NPCHandler.spawnNpc(player, 2045, 2263, 3075, player.heightLevel, 0, 1,
						11, 100, 50, true, false);
				NPCHandler.spawnNpc(player, 2045, 2263, 3071, player.heightLevel, 0, 1,
						11, 100, 50, true, false);
				NPCHandler.spawnNpc(player, 2045, 2268, 3069, player.heightLevel, 0, 1,
						11, 100, 50, true, false);
				NPCHandler.spawnNpc(player, 2045, 2273, 3071, player.heightLevel, 0, 1,
						11, 100, 50, true, false);
				NPCHandler.spawnNpc(player, 2045, 2273, 3077, player.heightLevel, 0, 1,
						11, 100, 50, true, false);
				container.stop();
			}

			@Override
			public void stop() {
				System.out.println(player.playerName + ": Zulrah destruct.");
				
			}
		}, 1);
	}

	public static final Boundary BOUNDARY = new Boundary(2248, 3059, 2283, 3084);
	
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
				
			}
		}, 1);
	}
	public static void move(Client player) {
		NPC z = player.zulrah;
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
				z.doAnimation(5071);
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
	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
		case MELEE:
			return damage / 2;
		case RANGE:
			return damage / 2;
		case MAGE:
			return damage / 2;

		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 2 ? 50 : attackType == 1 ? 40 : 45;
	}

	@Override
	public int distanceRequired() {
		return 8;
	}

	@Override
	public int getAttackEmote() {
		return 5068;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 5072;
	}

	@Override
	public boolean isAggressive() {
		return true;
	}

	@Override
	public boolean switchesAttackers() {
		return true;
	}

}
*/