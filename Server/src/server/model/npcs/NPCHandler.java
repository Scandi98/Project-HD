package server.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import server.Config;
import server.Server;
import server.core.PlayerHandler;
import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.Location;
import server.model.content.Achievements;
import server.model.content.DuoSlayer;
import server.model.content.instance.impl.Bosses;
import server.model.minigames.FightCaves;
import server.model.npcs.pet.Pet;
import server.model.players.Client;
import server.model.players.Player;
import server.region.Region;
import server.util.Location3D;
import server.util.Misc;

public class NPCHandler {

	public static final String DEFINITION_FILE = "Data/json/npc_definitions.json";
	public static final String SPAWN_FILE = "Data/json/npc_spawns.json";

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static NPCDefinition definitions[] = new NPCDefinition[maxListedNPCs];
	public static int[] slayerReqs = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1 };

	private boolean oldFormat;

	public NPCHandler(boolean old) {
		this.oldFormat = old;
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
		}
		for (int i = 0; i < maxListedNPCs; i++) {
			definitions[i] = null;
		}
		if (old) {
			loadNPCList("Data/cfg/npc.cfg");
			loadAutoSpawn("Data/cfg/spawn-config.cfg");
		} else {
			loadNPCList(DEFINITION_FILE);
			loadAutoSpawn("Data/cfg/npc_spawns.cfg");
		}
	}

	public NPC createNPC(int id, Location location) {
		int slot = freeSlot();
		if (slot == -1)
			return null;
		return createNPC(id, slot, location);
	}

	public NPC createNPC(NPC npc, Location location) {
		npc.absX = location.getX();
		npc.absY = location.getY();
		npc.makeX = location.getX();
		npc.makeY = location.getY();
		npc.heightLevel = location.getZ();
		npc.walkingType = -1;
		npc.currentHealth = getNpcListHP(npc.npcId);
		npc.maximumHealth = npc.currentHealth;
		npcs[npc.npcSlot] = npc;
		return npc;
	}

	public NPC createNPC(int id, int slot, Location location) {
		NPC newNPC = NPC.createNPC(slot, id);
		newNPC.absX = location.getX();
		newNPC.absY = location.getY();
		newNPC.makeX = location.getX();
		newNPC.makeY = location.getY();
		newNPC.heightLevel = location.getZ();
		newNPC.walkingType = -1;
		newNPC.currentHealth = getNpcListHP(id);
		newNPC.maximumHealth = newNPC.currentHealth;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (Player.goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcId) {
		case 2551:
		case 2552:
		case 2553:
		case 2559:
		case 465:
		case 319:
		case 2560:
		case 2561:
		case 2563:
		case 2215:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
			return true;

		}

		return false;
	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (Player.goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random((int) c.getCombat().calculateMageAttack(true))) {
								int dam = Misc.random(max);
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {
							if (npcs[i].npcId == 494) {
								int random = Misc.random(3);
								if (random == 2) {
									c.dealDamage(0);
									c.handleHitMask(0);
								} else {
									int dam = Misc.random(max) / 2;
									c.dealDamage(dam);
									c.handleHitMask(dam);
								}
							} else if (npcs[i].npcId == 6609) {
								int random = Misc.random(3);
								if (random == 2) {
									c.dealDamage(0);
									c.handleHitMask(0);
								} else {
									int dam = Misc.random(max);
									c.dealDamage(dam);
									c.handleHitMask(dam);
								}
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17]) {
							int dam = Misc.random(max);
							// TODO NPC CONVERSION
							if (Misc.random(500) + 200 > Misc.random((int) c.getCombat().calculateRangeDefence(true))) {
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {

							if (npcs[i].npcId == 2215 || npcs[i].npcId == 494 ||npcs[i].npcId == 2042 || npcs[i].npcId == 3943) {
								int random = Misc.random(3);
								if (random == 2) {
									c.dealDamage(0);
									c.handleHitMask(0);
								} else {
									int dam = Misc.random(max) / 2;
									c.dealDamage(dam);
									c.handleHitMask(dam);
								}
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public boolean isReallyAggressive(int i) {
		switch (npcs[i].npcId) {
		case 2042:
		case 2044:
		case 2043:
			return true;
		case 3129:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 319:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 494:
		case 3943:
		case FightCaves.KET_ZEK:
		case FightCaves.TOK_XIL:
		case FightCaves.TZ_KEK:
		case FightCaves.TZ_KEK_SPAWN:
		case FightCaves.TZ_KIH:
		case FightCaves.TZTOK_JAD:
		case FightCaves.YT_MEJKOT:
			return true;
		}
		return false;
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].npcSize,
						npcs[i].absX, npcs[i].absY, extraDistance(i) + distanceRequired(i) + followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].npcSize,
						npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public static NPC getNpc(int npcType, int x, int y, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcId == npcType && npc.absX == x && npc.absY == y && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	public int getCloseRandomPlayer1(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null
					&& (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].npcSize,
							npcs[i].absX, npcs[i].absY, 55 + followDistance(i)) || isFightCaveNpc(i))
					&& ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
					&& PlayerHandler.players[j].heightLevel == npcs[i].heightLevel) {
				if (PlayerHandler.players[j].inGodWarsBoss()) {
					players.add(j);
				}
			}
		}
		return players.size() > 0 ? players.get(Misc.random(players.size() - 1)) : 0;
	}

	/*
	 * public int npcSize(int i) { switch (npcs[i].npcType) { case 6265: case
	 * 6263: case 6261: return 1; case 5363: return 2; case 2883: case 2882:
	 * case 2881: case 3943: return 3; case 3847: case 2745: case 2741: case
	 * 2631: case 2743: case 105: return 5; case 6260: return 4; } return 0; }
	 */

	public int extraDistance(int i) {
		switch (npcs[i].npcId) {
		case 3129:
		case 319:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 5808:
		case 7135:
		case 494:
		case 3943:
			return 20;
		default:
			return 0;
		}
	}

	public boolean isAggressive(int i) {
		if (npcs[i].npcId >= 3732 && npcs[i].npcId <= 3741) {
			return true;
		}
		if (npcs[i].npcId >= 3742 && npcs[i].npcId <= 3746) {
			return true;
		}
		if (npcs[i].npcId >= 3747 && npcs[i].npcId <= 3751) {
			return true;
		}
		if (npcs[i].npcId >= 3752 && npcs[i].npcId <= 3761) {
			return true;
		}
		if (npcs[i].npcId >= 1728 && npcs[i].npcId <= 1734) {
			return true;
		}
		if (npcs[i].npcId >= 1735 && npcs[i].npcId <= 1739) {
			return true;
		}
		switch (npcs[i].npcId) {

		case 2042:
		case 2045:
		case 2044:
		case 2043:
			return true;

		case 3164:
		case 494:
		case 3943:
		case 2550:
		case 2551:
		case 2552:
		case 2553:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2565:
		case 239:
		case 2892:
		case 2894:
		case 2265:
		case 2266:
		case 2267:
		case 2035:
			// GWD
		case 2207:// Npcs That Give ArmaKC
		case 6230:
		case 6231:
		case 6229:
		case 6232:
		case 6240:
		case 6241:
		case 6242:
		case 6233:
		case 6234:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
		case 6238:
		case 6239:
		case 3165:
		case 6625:
		case 3163:
		case 3162: // end of armadyl npcs
		case 122:// Npcs That Give BandosKC
		case 6278:
		case 6277:
		case 6276:
		case 6283:
		case 6282:
		case 6281:
		case 6280:
		case 6279:
		case 6271:
		case 6272:
		case 6273:
		case 6274:
		case 6269:
		case 6270:
		case 6268:
		case 2218:
		case 2217:
		case 2216:
		case 2215: // end of bandos npcs
		case 6221:
		case 6219:
		case 6220:
		case 6217:
		case 6216:
		case 6215:
		case 6214:
		case 6213:
		case 6212:
		case 6211:
		case 6218:
		case 3132:
		case 3131:
		case 3130:
		case 3129:
		case 6275:
		case 6257:// Npcs That Give SaraKC
		case 6255:
		case 6256:
		case 6258:
		case 6259:
		case 6254:
		case 2208:
		case 2206:
		case 2205:
			return true;
		}
		if (npcs[i].inWild() && npcs[i].maximumHealth > 0)
			return true;
		if (isFightCaveNpc(i))
			return true;
		return false;
	}

	//
	public boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcId) {
		case 2189:
		case 2191:
		case 2192:
		case 2193:
		case 3124:
		case 3125:
		case 3127:
			return true;
		}
		return false;
	}

	public boolean isUndroppableNPC(int i) {
		switch (npcs[i].npcId) {
		/* Pest Control */
		case 1747:
		case 1748:
		case 1749:
		case 1750:

			/* Fight Caves */
		case 2189:
		case 2191:
		case 2192:
		case 2193:
		case 3124:
		case 3125:
		case 3127:

			/* Barrows */
		case 1672:
		case 1673:
		case 1674:
		case 1675:
		case 1676:
		case 1677:
			return true;

		/* Kalphite */
		case 963:
			return true;
		}
		return false;
	}

	public static void spawnNpc(Client c, int npcType, int x, int y, int heightLevel) {
		int slot = freeSlot();
		if (slot == -1) {
			return;
		}
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;

		if (c != null) {
			newNPC.killerId = c.playerId;
		}
		npcs[slot] = newNPC;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	public static void spawnNpc(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = freeSlot();
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
	}

	public static NPC spawnNpc5(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = freeSlot();
		/*
		 * if (slot == -1) { // Misc.println("No Free Slot"); return; // no free
		 * slot found }
		 */
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		return npcs[slot] = newNPC;
	}

	public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		// first, search for a free slot
		int slot = freeSlot();
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public void spawnNpc5(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		// first, search for a free slot
		int slot = freeSlot();
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	/**
	 * 
	 */
	private void killedBarrow(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (npcs[i].npcId == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}

	private void killedCrypt(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowCrypt.length; o++) {
				if (npcs[i].npcId == c.barrowCrypt[o][0]) {
					c.barrowsKillCount++;
					c.sendMessage("" + c.barrowsKillCount);
					// c.getPA().sendNewString(""+c.barrowsKillCount, 16137);
				}
			}
		}
	}

	public static int getAttackEmote(int i) {
		switch (NPCHandler.npcs[i].npcId) {

		case 2042:
		case 2044:
			return 5069;
		case 2043:
			return 5806;

		case 465:// wyv
			if (npcs[i].attackType == 0)
				return 2989;
			if (npcs[i].attackType == 3)
				return 2989;
		case 494:
			if (npcs[i].attackType == 2)
				return 3991;
			else
				return 3992;
		case 2215:
			if (npcs[i].attackType == 0)
				return 7018;
			else
				return 7021;
		case 600:
			int test = Misc.random(2);
			if (test == 2) {
				return 5895;
			} else if (test == 1) {
				return 5894;
			} else {
				return 5896;
			}
		case 3127:
			if (npcs[i].attackType == 2)
				return 2656;
			else if (npcs[i].attackType == 1)
				return 2652;
			else if (npcs[i].attackType == 0)
				return 2655;

		default:
			NPC npc = NPCHandler.npcs[i];
			if (npc.getDefinition() != null)
				return npc.getDefinition().getAttackEmote();
			return 0x326;
		}
	}

	public int getDeadEmote(int i) {

		switch (npcs[i].npcId) {
		case 2042:
		case 2043:
		case 2044:
			return 5072;
		case 963:
			spawnSecondForm(i);
			return 6242;
		case 965:
			spawnFirstForm(i);
			return 6234;
		default:
			NPC npc = NPCHandler.npcs[i];
			if (npc.getDefinition() != null)
				return npc.getDefinition().getDeathEmote();
			return 2304;
		}
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcId) {
		case 498:
		case 499:
			return 2;

		case 2042:
		case 4186:
			return 8;
		case 1672:
		case 494:
		case 6609:
		case 3943:
		case 6610:
			return 7;
		case 1675:
			return 3;

		case 3127:
			return 8;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2215:
			return 6;
		// saradomin gw boss
		case 2562:
			return 2;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcId) {
		case 2265:
		case 2266:
		case 2054:
		case 2892:
		case 2894:
			return 3;

		case 3125:
		case 2193:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 2215:
			npcs[i].graardor = Misc.random(5);
			if (npcs[i].graardor == 5) {
				return 3;
			}
			return 2;

		case 3129:
			npcs[i].tsutsaroth = Misc.random(4);
			if (npcs[i].tsutsaroth == 4) {
				return 3;
			}
			return 2;

		case 2205:
			npcs[i].zilyana = Misc.random(6);
			if (npcs[i].zilyana == 6 || npcs[i].zilyana == 5 || npcs[i].zilyana == 4) {
				return 6;
			}
			return 2;
		case 494:
		case 6610:
		case 3943:
		case 6609:
			return 5;
		case 3127:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 1672:
			return 4;
		case 1675:
			return 3;
		case 498:
		case 499:
			return 2;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		NPC npc = NPCHandler.npcs[i];
		if (npc.getDefinition() != null && npc.getDefinition().getRespawnTime() > 0)
			return npc.getDefinition().getRespawnTime();
		if (npc.maximumHealth > 100) {
			return npc.maximumHealth / 2;
		}
		switch (npcs[i].npcId) {
		case 4186:
		case 4189:
			return 100;
		case 319:
			return 30;

		}

		return 25;
	}

	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType) {
		// first, search for a free slot
		int slot = freeSlot();

		if (slot == -1)
			return; // no free slot found

		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = newNPC.getDefinition().getHealth();
		newNPC.maximumHealth = newNPC.getDefinition().getHealth();
		npcs[slot] = newNPC;
	}

	// id, health, attack, defence, max hit, attack emote, defence emote, death
	// emote, respawn time
	public void newNPCList(int npcType, int health, int attack, int defence, int maxHit, int attackEmote,
			int defenceEmote, int deathEmote, int respawnTime) {
		NPCDefinition definition = definitions[npcType];

		definition.setHealth(health);
		definition.setAttackStat(attack);
		definition.setDefenceStat(defence);
		definition.setMaxHit(maxHit);
		definition.setAttackEmote(attackEmote);
		definition.setDefenceEmote(defenceEmote);
		definition.setDeathEmote(deathEmote);
		definition.setRespawnTime(respawnTime);

		definition.defined = true;
	}

	public static int freeSlot() {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		return slot;
	}

	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			NPC npc = npcs[i];
			if (npc == null)
				continue;
			npc.clearUpdateFlags();
			if (npc.npcId == 4066) { // NPC ID
				if (Misc.random2(2000) <= 3) {
					npc.updateRequired = true;
					npc.forceChat("Vote for Dragon-Age daily to recieve a vote point!");
				}
			}
			if (npc.npcId == 1835) { // NPC ID
				if (Misc.random2(1000) <= 3) {
					npc.updateRequired = true;
					npc.forceChat("Please Talk to me to start the Easter Event!");
				}
			}
			if (npc.npcId == 394) { // NPC ID
				if (Misc.random2(2000) <= 3) {
					npc.updateRequired = true;
					npc.forceChat("Talk to me to set your Bank Pin!");
				}
			}
			if (npc.npcId == 3077) {
				if (Misc.random2(2000) <= 3) {
					npc.updateRequired = true;
					npc.forceChat("Talk to me to if you're interested in becoming an Iron Man!");
				}
			}
			if (npc.npcId == 5036) {
				if (Misc.random2(2000) <= 3) {
					npc.updateRequired = true;
					npc.forceChat("Talk to me to get youre Potions Decanted!");
				}
			}
		}

		for (int i = 0; i < maxNPCs; i++) {
			NPC npc = NPCHandler.npcs[i];
			if (npc != null) {
				NPC NPC = npc;

				npc.process();

				Client petOwner = (Client) PlayerHandler.players[NPC.summonedBy];
				if (petOwner == null && NPC.summoned) {
					Pet.deletePet(NPC);
				}
				if (petOwner != null && petOwner.isDead) {
					Pet.deletePet(NPC);
				}
				if (petOwner != null && petOwner.getPetSummoned() && NPC.summoned) {
					if (Player.goodDistance(NPC.getX(), NPC.getY(), petOwner.absX, petOwner.absY, 15)) {
						Server.npcHandler.followPlayer(i, petOwner.playerId);
					} else {
						Pet.deletePet(NPC);
						Pet.summonPet(petOwner, petOwner.petID, petOwner.absX, petOwner.absY - 1, petOwner.heightLevel);
					}
				}
				if (npc.actionTimer > 0) {
					npc.actionTimer--;
				}

				if (npc.freezeTimer > 0) {
					npc.freezeTimer--;
				}

				if (npc.hitDelayTimer > 0) {
					npc.hitDelayTimer--;
				}

				if (npc.hitDelayTimer == 1) {
					npc.hitDelayTimer = 0;
					applyDamage(i);
				}

				if (npc.attackTimer > 0) {
					npc.attackTimer--;
				}

				if (npc.spawnedBy > 0) { // delete summons npc
					if (PlayerHandler.players[npc.spawnedBy] == null
							|| PlayerHandler.players[npc.spawnedBy].heightLevel != npc.heightLevel
							|| PlayerHandler.players[npc.spawnedBy].respawnTimer > 0
							|| !Player.goodDistance(npc.getX(), npc.getY(), PlayerHandler.players[npc.spawnedBy].getX(),
									PlayerHandler.players[npc.spawnedBy].getY(), followThatDistance(i))) {

						if (PlayerHandler.players[npc.spawnedBy] != null) {
							for (int o = 0; o < PlayerHandler.players[npc.spawnedBy].barrowsNpcs.length; o++) {
								if (npc.npcId == PlayerHandler.players[npc.spawnedBy].barrowsNpcs[o][0]) {
									if (PlayerHandler.players[npc.spawnedBy].barrowsNpcs[o][1] == 1)
										PlayerHandler.players[npc.spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;

				if (npc.lastX != npc.getX() || npc.lastY != npc.getY()) {
					npc.lastX = npc.getX();
					npc.lastY = npc.getY();
				}
				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npc.underAttack && !npc.isDead && !switchesAttackers(i)) {
					if (isReallyAggressive(i)) {
						npc.killerId = getCloseRandomPlayer1(i);
					} else {
						npc.killerId = getCloseRandomPlayer(i);
					}
				} /*
					 * else if (isAggressive(i) && !npc.underAttack &&
					 * !npc.isDead && switchesAttackers(i)) { if
					 * (isReallyAggressive(i)) { npc.killerId =
					 * getCloseRandomPlayer1(i); } else { npc.killerId =
					 * getCloseRandomPlayer(i); } }
					 */

				if (System.currentTimeMillis() - npc.lastDamageTaken > 5000)
					npc.underAttackBy = 0;

				if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
						&& retaliates(npcs[i].npcId)) {
					if (!npcs[i].isDead) {
						npcs[i].doCombat();
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npc.underAttack || npc.walkingHome) && npc.randomWalk && !npc.isDead) {
					npc.facePlayer(-1);
					npc.killerId = 0;
					if (npc.spawnedBy == 0) {
						if ((npc.absX > npc.makeX + Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npc.absX < npc.makeX - Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npc.absY > npc.makeY + Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npc.absY < npc.makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
							npc.walkingHome = true;
						}
					}

					if (npc.walkingHome && npc.absX == npc.makeX && npc.absY == npc.makeY) {
						npc.walkingHome = false;
					} else if (npc.walkingHome) {
						npc.moveX = GetMove(npc.absX, npc.makeX);
						npc.moveY = GetMove(npc.absY, npc.makeY);
						handleClipping(i);
						npcs[i].getNextNPCMovement(i);
						npc.updateRequired = true;
					}
					if (npc.walkingType >= 0) {
						switch (npc.walkingType) {

						case 5:
							npc.turnToLocation(npc.absX - 1, npc.absY);
							break;

						case 4:
							npc.turnToLocation(npc.absX + 1, npc.absY);
							break;

						case 3:
							npc.turnToLocation(npc.absX, npc.absY - 1);
							break;
						case 2:
							npc.turnToLocation(npc.absX, npc.absY + 1);
							break;

						default:
							if (npc.walkingType >= 0) {
								npc.turnToLocation(npc.absX, npc.absY);
							}
							break;
						}
					}
					if (npc.walkingType == 1) {
						if (Misc.random(3) == 1 && !npc.walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}

							if (MoveX == 1) {
								if (npc.absX + MoveX < npc.makeX + 1) {
									npc.moveX = MoveX;
								} else {
									npc.moveX = 0;
								}
							}

							if (MoveX == -1) {
								if (npc.absX - MoveX > npc.makeX - 1) {
									npc.moveX = MoveX;
								} else {
									npc.moveX = 0;
								}
							}

							if (MoveY == 1) {
								if (npc.absY + MoveY < npc.makeY + 1) {
									npc.moveY = MoveY;
								} else {
									npc.moveY = 0;
								}
							}

							if (MoveY == -1) {
								if (npc.absY - MoveY > npc.makeY - 1) {
									npc.moveY = MoveY;
								} else {
									npc.moveY = 0;
								}
							}

							int x = (npc.absX + npc.moveX);
							int y = (npc.absY + npc.moveY);
							int plane = npc.heightLevel;
							int size = npc.npcSize;

							handleClipping(i);
							npcs[i].getNextNPCMovement(i);
						}
					}
				}

				if (npc.isDead == true) {
					if (npc.actionTimer == 0 && npc.applyDead == false && npc.needRespawn == false) {
						npc.facePlayer(-1);
						npc.killedBy = getNpcKillerId(i);
						npc.doAnimation(getDeadEmote(i));
						npc.freezeTimer = 0;
						npc.applyDead = true;
						killedCrypt(i);
						killedBarrow(i);
						npc.actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npc.actionTimer == 0 && npc.applyDead == true && npc.needRespawn == false) {
						npc.needRespawn = true;
						npc.actionTimer = getRespawnTime(i); // respawn time
						dropItems(i); // npc drops items!
						Client c = (Client) PlayerHandler.players[npc.killedBy];
						handleBossDeath(c, i);
						appendSlayerExperience(i);
						Achievements.appendNPCKill(c);
						tzhaarDeathHandler(i);
						appendDuoSlayerExperience(i);
						if (npc.npcId == 6611) {
							spawnedHounds = false;
						}
						npc.absX = npc.makeX;
						npc.absY = npc.makeY;
						npc.currentHealth = npc.maximumHealth;
						npc.doAnimation(0x328);
						if (npc.npcId >= 2440 && npc.npcId <= 2446) {
							Server.objectManager.removeObject(npc.absX, npc.absY);
						}
						if (npc.npcId == 3127) {
							handleJadDeath(i);
						}
						npc.onDeath();
					} else if (npc.actionTimer == 0 && npc.needRespawn == true && npc.npcId != 963) {
						if (npc.spawnedBy > 0) {
							npcs[i] = null;
						} else {
							int old1 = npc.npcId;
							int old2 = npc.makeX;
							int old3 = npc.makeY;
							int old4 = npc.heightLevel;
							int old5 = npc.walkingType;

							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5);
						}
					}
				}
			}
		}
	}

	public boolean goodDistance(int npcX, int npcY, int npcSize, int playerX, int playerY, int distance) {
		return playerX >= (npcX - distance) && playerX <= (npcX + npcSize + distance) && playerY >= (npcY - distance)
				&& playerY <= (npcY + npcSize + distance);
	}

	/**
	 * Duo Slayer Experience
	 **/
	public static void appendDuoSlayerExperience(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		boolean k = false;
		if (c != null) {
			if (c.duoTask == npcs[i].npcId) {
				k = true;
			}
			if (k) {
				c.duoTaskAmount--;
				c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE, 18);
				if (c.getDuoPartner() != null)
					c.getDuoPartner().duoTaskAmount--;
				if (c.getDuoPartner() != null && c.getDuoPartner().connectedFrom != c.connectedFrom) {
					c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE, 18);
					final Client pp = (Client) c.getDuoPartner();
					pp.getPA().addSkillXP((npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE) / 2, 18);
				}
			}
			if (c.duoTaskAmount == 0) {
				final Player partner = c.getDuoPartner();
				DuoSlayer.getInstance().complete(c, partner);
				Achievements.appendDuoSlayerTask(c);
				if (partner != null)
					Achievements.appendDuoSlayerTask((Client) partner);
			}
			final Player partna = c.getDuoPartner();
			if (partna != null) {
				((Client) partna).getPA()
						.sendNewString("@or1@Duo Slayer Points: @gre@" + ((Client) partna).duoPoints + " ", 29171);
				((Client) partna).getPA().sendNewString(
						"@or1@Task: @gre@" + c.duoTaskAmount + " " + Server.npcHandler.getNpcListName(c.duoTask) + " ",
						29172);
			}
			c.getPA().sendNewString("@or1@Duo Slayer Points: @gre@" + c.duoPoints + " ", 29171);
			c.getPA().sendNewString(
					"@or1@Task: @gre@" + c.duoTaskAmount + " " + Server.npcHandler.getNpcListName(c.duoTask) + " ",
					29172);
			/*
			 * partner.getPA().sendNewString( "@or1@Task: @gre@" +
			 * c.duoTaskAmount + " " +
			 * Server.npcHandler.getNpcListName(c.duoTask) + " ", 29172);
			 */
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcId) {
		case 2550:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcId) {
		case 963: // kq
			if (npcs[i].attackType == 2)
				return true;
			else
				return false;

		case 965: // kq
			if (npcs[i].attackType == 1)
				return true;
			else
				return false;

		case 6609:
			if (npcs[i].attackType == 2)
				return true;
			else
				return false;
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == 2)
				return true;
		case 3162:
			if (npcs[i].attackType == 3)
				return true;
		case 2215:
			if (npcs[i].attackType == 1)
				return true;
			else
				return false;

		case 2550:
			if (npcs[i].attackType == 1)
				return true;
			else
				return false;
		case 494:
		case 3943:
			return true;
		case 6610:
			return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/

	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	private void tzhaarDeathHandler(int i) {
		if (isFightCaveNpc(i) && npcs[i].npcId != FightCaves.TZ_KEK)
			killedTzhaar(i);
		if (npcs[i].npcId == FightCaves.TZ_KEK_SPAWN) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Client c = (Client) PlayerHandler.players[p];
				c.tzKekSpawn += 1;
				if (c.tzKekSpawn == 2) {
					killedTzhaar(i);
					c.tzKekSpawn = 0;
				}
			}
		}

		if (npcs[i].npcId == FightCaves.TZ_KEK) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Client c = (Client) PlayerHandler.players[p];
				FightCaves.tzKekEffect(c, i);
				c.tzhaarToKill += 2;
			}
		}
	}

	private void killedTzhaar(int i) {
		final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c2.tzhaarKilled += 1;
		if (c2.tzhaarKilled == c2.tzhaarToKill) {
			c2.waveId++;
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					if (c2 != null) {
						Server.fightCaves.spawnNextWave(c2);
					}
					c.stop();
				}
			}, 7500);

		}
	}

	public void handleJadDeath(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItemToBank(6570, 1);
		c.getDH().sendDialogues(70, 2617);
		c.getPA().resetTzhaar();
		c.waveId = 300;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage("[@red@SERVER@bla@] " + c.playerName
						+ " has just completed fight caves and has claimed a fire cape!");
				c.sendMessage("@red@Congrats on killing Jad. Your Reward is in your bank");
			}
		}
	}

	public void dropItems(int i) {
		if (isUndroppableNPC(i)) {
			return;
		}
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			final double addChance = c.playerEquipment[c.playerRing] == 2572 ? 1.25 : 1;
			if (npcs[i].npcId == 912 || npcs[i].npcId == 913 || npcs[i].npcId == 914)
				c.magePoints += 1;
			if (npcs[i].inWild()) {
				if (Misc.random(29) == 1 && !c.getItems().playerOwnsItem(11941)) {
					Server.itemHandler.createGroundItem(c, 11941, c.absX, c.absY, 1, c.playerId);
					c.sendMessage("You have just gotten a Loot Bag Drop.");
				}
			}
			for (final NPCLoot loot : NPCLootTable.forID(npcs[i].npcId).getGeneratedLoot((int) addChance)) {
				for (int rare : Config.rareItems) {
					if (loot.getItemID() == rare) {
						c.sendGlobal("[@red@SERVER@bla@] " + c.playerName + " has received 1x @pur@"
								+ c.getItems().getItemName(loot.getItemID()) + "@bla@.");
					}
				}
				if (npcs[i].npcId == 494) {
					Server.itemHandler.createGroundItem(c, loot.getItemID(), 3696, 5807,
							Misc.random(loot.getMinAmount(), loot.getMaxAmount()), c.playerId);
				}
				if (npcs[i].npcId == 2042 || npcs[i].npcId == 2043 || npcs[i].npcId == 2044) {

				//	Zulrah.handleDrops(c);
				} else {
					Server.itemHandler.createGroundItem(c, loot.getItemID(), npcs[i].absX, npcs[i].absY,
							Misc.random(loot.getMinAmount(), loot.getMaxAmount()), c.playerId);
				}

			}
		}
		// System.out.println("Took: " + (System.currentTimeMillis() - start)
		// +" Milliseconds to fetch the drop");
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 3010:
		case 100:
		case 12:
		case 17:
		case 803:
		case 3103:
		case 81:
		case 2246:
		case 41:
		case 19:
		case 90:
		case 6611:
		case 75:
		case 86:
		case 4502:
		case 912:
		case 913:
		case 914:
		case 1648:
		case 443:
		case 484:
		case 423:
		case 498:
		case 499:
		case 2878:
		case 291:
		case 104:
		case 1575:
		case 3108:
		case 1341:
			return 526;
		case 2098:
			return 532;// big bones
		case 239:// drags
		case 53:
		case 252:
		case 265:
		case 260:
		case 5363:
		case 270:
		case 272:
		case 274:
			return 536;
		case 2048:
		case 415:
		case 8:
		case 2005:
		case 2054:
			return 592;
		case 2265:
		case 2266:
		case 2267:
			return 6729;
		default:
			return -1;
		}
	}

	public static void kill(int npcType, int height) {
		Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(n -> n.npcId == npcType && n.heightLevel == height)
				.forEach(npc -> npc.isDead = true);
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 3010:
				return 133 + Misc.random(100);
			case 423:
				return 1000 + Misc.random(300);
			case 484:
				return 1000 + Misc.random(300);
			case 443:
				return 1000 + Misc.random(300);
			case 412:
				return 1000 + Misc.random(1000);
			case 8:
				return 1500 + Misc.random(1250);
			case 415:
				return 3000;
			case 3103:
				return 500;
			case 2246:
				return 60;
			case 913:
			case 912:
			case 914:
				return 750 + Misc.random(500);
			case 2844:
				return 250 + Misc.random(500);
			case 1648:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 2005:
				return 1000 + Misc.random(455);
			case 241:
				return 400 + Misc.random(200);
			case 104:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 3108:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			case 21:
				return 890 + Misc.random(125);
			case 2098:
				return 500 + Misc.random(250);
			case 2167:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	/**
	 * Slayer Experience
	 **/
	public void appendSlayerExperience(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (c.slayerTask == npcs[i].npcId) {
				c.taskAmount--;
				c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE, 18);
				c.getPA().sendNewString(
						"@whi@Task: @gre@" + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + " ",
						29168);
				if (c.taskAmount <= 0) {
					if (c.combatLevel < 50) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 8) * Config.SLAYER_EXPERIENCE, 18);
						c.slayerTask = -1;
						c.slayerPoints += 5;
						c.getPA().sendNewString("@whi@Slayer Points: @gre@" + c.slayerPoints + " ", 29167);
						c.sendMessage(
								"You completed your low slayer task. Please see a slayer master to get a new one.");
						if (c.playerEquipment[c.playerHat] == 11864) {
							c.slayerPoints += 5;
							c.getPA().addSkillXP((npcs[i].maximumHealth * 10) * Config.SLAYER_EXPERIENCE, 18);
							c.sendMessage(
									"@red@You've recieved a bonus of xp and 5 slayer points because wearing slayer helmet.");
						}
					} else if (c.combatLevel >= 50 && c.combatLevel <= 90) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 12) * Config.SLAYER_EXPERIENCE, 18);
						c.slayerTask = -1;
						c.slayerPoints += 10;
						c.getPA().sendNewString("@whi@Slayer Points: @gre@" + c.slayerPoints + " ", 7339);
						c.sendMessage(
								"You completed @blu@medium@bla@ your slayer task. Please see a slayer master to get a new one.");
						if (c.playerEquipment[c.playerHat] == 11864) {
							c.slayerPoints += 5;
							c.getPA().addSkillXP((npcs[i].maximumHealth * 14) * Config.SLAYER_EXPERIENCE, 18);
							c.sendMessage(
									"@red@You've recieved a bonus of xp and 5 slayer points because wearing slayer helmet.");
						}
					} else if (c.combatLevel > 90 && c.combatLevel <= 126) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 12) * Config.SLAYER_EXPERIENCE, 18);
						c.slayerTask = -1;
						c.slayerPoints += 15;
						c.getPA().sendNewString("@whi@Slayer Points: @gre@" + c.slayerPoints + " ", 7339);
						c.sendMessage(
								"You completed your @red@high@bla@ slayer task. Please see a slayer master to get a new one.");
						if (c.playerEquipment[c.playerHat] == 11864) {
							c.slayerPoints += 5;
							c.getPA().addSkillXP((npcs[i].maximumHealth * 14) * Config.SLAYER_EXPERIENCE, 18);
							c.sendMessage(
									"@red@You've recieved a bonus of xp and 5 slayer points because wearing slayer helmet.");
						}
					} else if (c.eliteTask && c.combatLevel >= 126) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 16) * Config.SLAYER_EXPERIENCE, 18);
						c.slayerTask = -1;
						c.slayerPoints += 15;
						c.getPA().sendNewString("@whi@Slayer Points: @gre@" + c.slayerPoints + " ", 29167);
						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Client c2 = (Client) PlayerHandler.players[j];
								c2.sendMessage("[@redSERVER@bla@]" + c.playerName
										+ " has completed his @whi@Elite Slayer Task.");
							}
						}
						c.sendMessage(
								"You completed your elite slayer task. Please see a slayer master to get a new one.");
						if (c.playerEquipment[c.playerHat] == 11864) {
							c.slayerPoints += 5;
							c.getPA().addSkillXP((npcs[i].maximumHealth * 18) * Config.SLAYER_EXPERIENCE, 18);
							c.sendMessage(
									"@red@You've recieved a bonus of xp and 5 slayer points because wearing slayer helmet.");
						}
						return;
					}
					Achievements.appendSlayerTask(c);
					c.getPA().sendNewString("@or1@Slayer Points: @gre@" + c.slayerPoints + " ", 29167);
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		int[] noMove = { 2044, 2043, 2042, 1747, 1748, 1749, 1750 };
		switch (npcs[i].npcId) {
		case 2042:
		case 2043:
		case 2044:
		case 2892:
		case 2894:
			return false;
		}
		for (int no = 0; no < noMove.length; no++) {
			if (npcs[i].npcId == noMove[no]) {
				return false;
			}
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		int[] noMove = { 2044, 2043, 2042, 1747, 1748, 1749, 1750 };
		for (int no = 0; no < noMove.length; no++) {
			if (npcs[i].npcId == noMove[no]) {
				return;
			}
		}
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(-1);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}
		// System.out.println("Following Player;");
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		int npcY = npcs[i].absY;
		int npcX = npcs[i].absX;
		if (goodDistance(npcX, npcY, npcs[i].npcSize, playerX, playerY, distanceRequired(i)))
			return;
		/*
		 * if (Boundary.isInBounds(npcs[i], Zulrah.BOUNDARY) && (npcs[i].npcId
		 * >= 2042 && npcs[i].npcId <= 2044 || npcs[i].npcId == 6720)) { return;
		 * }
		 */
		if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					if (playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX > npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
							break;

						case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
							break;

						case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;

						case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}
					}
					npcs[i].facePlayer(playerId);
					handleClipping(i); // This should handle it
					npcs[i].getNextNPCMovement(i);
					npcs[i].updateRequired = true;
				}
			}
		} else {
			npcs[i].facePlayer(-1);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	boolean created = false;

	public void handleClipping(int i) {
		NPC npc = npcs[i];
			if(npc.moveX == 1 && npc.moveY == 1) {
					if((Region.getClipping(npc.absX + 1, npc.absY + 1, npc.heightLevel) & 0x12801e0) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
								npc.moveY = 1;
							else 
								npc.moveX = 1; 				
							}
					} else if(npc.moveX == -1 && npc.moveY == -1) {
						if((Region.getClipping(npc.absX - 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
								npc.moveY = -1;
							else
								npc.moveX = -1; 		
					}
					} else if(npc.moveX == 1 && npc.moveY == -1) {
							if((Region.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) & 0x1280183) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
								npc.moveY = -1;
							else
								npc.moveX = 1; 
							}
					} else if(npc.moveX == -1 && npc.moveY == 1) {
						if((Region.getClipping(npc.absX - 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
								npc.moveY = 1;
							else
								npc.moveX = -1; 
										}
					} //Checking Diagonal movement. 
					
			if (npc.moveY == -1 ) {
				if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) != 0)
                    npc.moveY = 0;
			} else if( npc.moveY == 1) {
				if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) != 0)
					npc.moveY = 0;
			} //Checking Y movement.
			if(npc.moveX == 1) {
				if((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel) & 0x1280180) != 0) 
					npc.moveX = 0;
				} else if(npc.moveX == -1) {
				 if((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) & 0x1280108) != 0)
					npc.moveX = 0;
			} //Checking X movement.
	}

	/**
	 * load spell
	 **/
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}
	}

	public static Client player;

	public void loadSpell(int i) {
		int chance = 0;
		if (Bosses.isBoss(npcs[i].npcId)) {
			Bosses.get(npcs[i].npcId).attack(npcs[i]);
			return;
		}
		switch (npcs[i].npcId) {
		case 2042:
			chance = 1;
			chance = Misc.random(chance);
			npcs[i].setFacePlayer(true);
			if (chance < 2) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 97;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 156;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;

		case 2044:
			npcs[i].setFacePlayer(true);
			if (Misc.random(3) > 0) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1046;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1044;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;

		case 2043:
			npcs[i].setFacePlayer(true);
			npcs[i].targetedLocation = new Location3D(player.getX(), player.getY(), player.heightLevel);
			npcs[i].attackType = 0;
			npcs[i].attackTimer = 9;
			npcs[i].hitDelayTimer = 6;
			npcs[i].projectileId = -1;
			npcs[i].endGfx = -1;
			break;
		case 6611:
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (goodDistance(npcs[i].getX(), npcs[i].getY(), PlayerHandler.players[j].getX(),
							PlayerHandler.players[j].getY(), 4)) {
						if (Misc.random(4) == 1) {
							npcs[i].projectileId = 1194; // gfx
							npcs[i].attackType = 2;
							npcs[i].endGfx = 1194;
						} else {
							npcs[i].attackType = 0;
							npcs[i].projectileId = -1;
						}
					} else {
						npcs[i].projectileId = 1194; // gfx
						npcs[i].attackType = 2;
						npcs[i].endGfx = 1194;
					}
				}
			}
			break;
		case 465:
			if (Misc.random(10) > 7) {
				npcs[i].projectileId = 395; // icy
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
				npcs[i].doAnimation(2989);
			} else {
				npcs[i].doAnimation(2980);
				npcs[i].attackType = 0;
			}
			break;
		case 963:// kq first form
			int kqRandom = Misc.random(3);
			if (kqRandom == 2) {
				npcs[i].projectileId = 280; // gfx
				npcs[i].attackType = 2;
				npcs[i].endGfx = 279;
			} else {
				npcs[i].attackType = 0;
			}
			break;
		case 965:// kq secondform
			int kqRandom2 = Misc.random(3);
			if (kqRandom2 == 2) {
				npcs[i].projectileId = 279; // gfx
				npcs[i].attackType = 1 + Misc.random(1);
				npcs[i].endGfx = 278;
			} else {
				npcs[i].attackType = 0;
			}
			break;
		case FightCaves.TZTOK_JAD:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;

		case FightCaves.KET_ZEK:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case FightCaves.TOK_XIL:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 260:
		case 5363:
		case 265:
		case 270:
		case 272:
		case 274:
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (goodDistance(npcs[i].getX(), npcs[i].getY(), PlayerHandler.players[j].getX(),
							PlayerHandler.players[j].getY(), 4)) {
						if (Misc.random(5) == 1) {
							npcs[i].projectileId = 393; // red
							npcs[i].endGfx = 430;
							npcs[i].attackType = 3;
						} else {
							npcs[i].projectileId = -1; // melee
							npcs[i].endGfx = -1;
							npcs[i].attackType = 0;
						}
					} else {
						npcs[i].projectileId = 393; // red
						npcs[i].endGfx = 430;
						npcs[i].attackType = 3;
					}
				}
			}
			break;
		case 239:
			int random = Misc.random(3);
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (goodDistance(npcs[i].getX(), npcs[i].getY(), PlayerHandler.players[j].getX(),
							PlayerHandler.players[j].getY(), 4)) {
						if (Misc.random(5) == 1) {
							if (random == 0) {
								npcs[i].projectileId = 393; // red
								npcs[i].endGfx = 430;
								npcs[i].attackType = 3;
							} else if (random == 1) {
								npcs[i].projectileId = 394; // green
								npcs[i].endGfx = 429;
								npcs[i].attackType = 3;
							} else if (random == 2) {
								npcs[i].projectileId = 395; // white
								npcs[i].endGfx = 431;
								npcs[i].attackType = 3;
							} else if (random == 3) {
								npcs[i].projectileId = 396; // blue
								npcs[i].endGfx = 428;
								npcs[i].attackType = 3;
							}
						} else {
							npcs[i].projectileId = -1; // melee
							npcs[i].endGfx = -1;
							npcs[i].attackType = 0;
						}
					} else {
						if (random == 0) {
							npcs[i].projectileId = 393; // red
							npcs[i].endGfx = 430;
							npcs[i].attackType = 3;
						} else if (random == 1) {
							npcs[i].projectileId = 394; // green
							npcs[i].endGfx = 429;
							npcs[i].attackType = 3;
						} else if (random == 2) {
							npcs[i].projectileId = 395; // white
							npcs[i].endGfx = 431;
							npcs[i].attackType = 3;
						} else if (random == 3) {
							npcs[i].projectileId = 396; // blue
							npcs[i].endGfx = 428;
							npcs[i].attackType = 3;
						}
					}
				}
			}
			break;
		// arma npcs
		case 2561:
			npcs[i].attackType = 0;
			break;
		case 2560:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			npcs[i].attackType = 1 + random;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = 0;
			break;
		case 2563: // star
			npcs[i].attackType = 0;
			break;
		case 2564: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
		// corp
		case 319:
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;

		case 6615: // scorpia
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;

		case 6619: // chaos fanatic
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1203;
			}
			break;

		case 6618: // archeologist
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 288;
			}
			break;

		case 2551:
			npcs[i].attackType = 0;
			break;
		case 2552:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 1672:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2215:
			switch (npcs[i].graardor) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				if (Misc.random(5) >= 4) {
					npcs[i].forceChat(npcs[i].Graardor());
				}
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				break;
			case 5:
				if (Misc.random(5) >= 3) {
					npcs[i].forceChat(npcs[i].Graardor());
				}
				npcs[i].attackType = 1;
				npcs[i].multiAttack = true;
				npcs[i].gfx0(1203);
				npcs[i].projectileId = 1202;
				break;
			}
			break;
		case 2205:
			switch (npcs[i].zilyana) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[i].attackType = 0;
				break;
			case 4:
			case 5:
			case 6:
				npcs[i].attackType = 2;
				break;
			}
			break;
		case 3162:
			npcs[i].kree = Misc.random(2);
			switch (npcs[i].kree) {
			case 0:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1199;
				break;
			case 1:
			case 2:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1198;
				break;
			}
			if (Misc.random(5) >= 3) {
				npcs[i].forceChat(npcs[i].Kree());
			}
			break;
		case 3129:
			if (Misc.random(5) >= 3) {
				npcs[i].forceChat(npcs[i].Tsutsaroth());
			}
			switch (npcs[i].tsutsaroth) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				break;
			case 4:
				npcs[i].attackType = 2;
				npcs[i].multiAttack = true;
				npcs[i].gfx0(1165);
				npcs[i].projectileId = 1166;
				break;
			}
			break;
		case 3164:
		case 6230:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6238:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 2184:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 6229:
		case 6232:
		case 6239:
		case 6237:
		case 6240:
		case 6246:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 35;
			break;

		case 6221:
			npcs[i].attackType = 2;
			npcs[i].endGfx = 78;
			break;

		case 6257:
			npcs[i].attackType = 2;
			npcs[i].endGfx = 76;
			break;

		case 6256:
		case 6220:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 326;
			break;
		case 2265:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2266:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 1675:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 2054:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 498:
		case 499:
			r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 571; // blue
				npcs[i].endGfx = 571;
			}
			if (r2 == 1) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 571;
				npcs[i].endGfx = 571;
			}
			break;
		case 494:
			r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 2;
			}
			if (r2 == 1) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 298;
			}
			break;
		case 6609:
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].killerId].absX,
							PlayerHandler.players[npcs[i].killerId].absY, 3)) {
						npcs[i].projectileId = -1;
						npcs[i].attackType = 0;
					} else if (!goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].killerId].absX,
							PlayerHandler.players[npcs[i].killerId].absY, 3)) {
						npcs[i].projectileId = 395; // blue
						npcs[i].endGfx = 428;
						npcs[i].attackType = 2;
					}
				}
			}
			break;
		case 6610:
			npcs[i].projectileId = 165; // green
			npcs[i].endGfx = 166;
			npcs[i].attackType = 2;
			if (PlayerHandler.players[npcs[i].killerId].poisonDamage == 0) {
				if (Misc.random(5) == 4) {
					Client c = (Client) PlayerHandler.players[npcs[i].killerId];
					if (c != null) {
						c.poisonDamage = 15;
						c.sendMessage("You have been Poisoned!");
					}
				}
			}
			if (Misc.random(6) == 3) {
				Client c = (Client) PlayerHandler.players[npcs[i].killerId];
				if (c != null) {
					c.playerLevel[c.playerPrayer] = (int) (c.playerLevel[c.playerPrayer] * 0.5);
					c.sendMessage("Venenatis Drains your Prayer...");
					c.getCombat().resetPrayers();
				}
			} else if (Misc.random(6) == 5) {
				Client c = (Client) PlayerHandler.players[npcs[i].killerId];
				if (c != null) {
					int dam = Misc.random(50);
					c.gfx100(80);
					c.sendMessage("Venenatis stuns you...");
					c.dealDamage(dam);
					c.handleHitMask(dam);
				}
			}
			break;
		case 3943:
			r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].projectileId = 605;
				npcs[i].endGfx = 606;
				npcs[i].attackType = 2;
			}
			if (r2 == 1) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 473;
			}
			break;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		int distanceNeeded = 1;
		switch (npcs[i].npcId) {
		case 2042:
		case 2043:
		case 2044:
			return distanceNeeded += 15;
		case 1672:
		case 1675:
			return 6;
		case 2562:
			return 2;
		case 2265:// dag kings
		case 2266:
		case 2054:// chaos ele
		case 3125:
		case 2193:
		case 3127:
		case 239:
		case 260:
		case 5363:
		case 6609:
		case 6611:
		case 265:
		case 270:
		case 272:
		case 274:
			return 8;
		case 2267:// rex
			return 1;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
		case 494:
		case 3943:
		case 6610:
			return 10;
		case 499:
		case 498:
			return 7;
		case 3162:
			return 6;
		default:
			return 1;
		}
	}

	public int distanceRequired2(int i) {
		switch (npcs[i].npcId) {
		case 3129:
			return 2;
		case 2215:
			return 10;
		case 3162:
			return 8;
		case 1672:
		case 1675:
			return 6;
		case 2562:
			return 2;
		case 2265:// dag kings
		case 2266:
		case 2054:// chaos ele
		case 3125:
		case 2193:
		case 3127:
		case 239:
		case 260:
		case 5363:
		case 6609:
		case 6611:
		case 265:
		case 270:
		case 272:
		case 274:
			return 8;
		case 2267:// rex
			return 1;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
		case 494:
		case 3943:
		case 6610:
		case 498:
		case 499:
			return 10;
		default:
			return 1;
		}
	}

	public int followThatDistance(int i) {
		switch (npcs[i].npcId) {
		case 3129:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 6829:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 494:
		case 3943:
		case 498:
		case 499:
		case 6610:
		case FightCaves.KET_ZEK:
		case FightCaves.TOK_XIL:
		case FightCaves.TZ_KEK:
		case FightCaves.TZ_KEK_SPAWN:
		case FightCaves.TZ_KIH:
		case FightCaves.TZTOK_JAD:
		case FightCaves.YT_MEJKOT:
			return 40;
		case 5363:
		case 6609:
		case 6611:
		case 6615:
			return 3;
		default:
			return Config.NPC_FOLLOW_DISTANCE;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcId) {
		case 5363:
		case 6609:
		case 6611:
		case 6615:
			return 3;
		case 2550:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2267:
			return 4;
		case 2265:
		case 2266:
			return 1;
		case 3129:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 6829:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 494:
		case 498:
		case 499:
		case 3943:
		case 6610:
		case FightCaves.KET_ZEK:
		case FightCaves.TOK_XIL:
		case FightCaves.TZ_KEK:
		case FightCaves.TZ_KEK_SPAWN:
		case FightCaves.TZ_KIH:
		case FightCaves.TZTOK_JAD:
		case FightCaves.YT_MEJKOT:
			return 20;

		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcId) {
		case 2265:
		case 2266:
		case 2054:
			return 85;

		case 3127:
		case 494:
		case 6609:
		case 6611:
		case 3943:
		case 6610:
			return 130;

		case 239:
			return 90;

		case 1672:
			return 85;

		case 1675:
			return 80;
		case 499:
		case 498:
			return 20;

		default:
			return 85;
		}
	}

	boolean spawnedHounds = false;
	int lastSmash;

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(final Client c, final int i) {
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
				return;
			}
			if (!npcs[i].inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			// c.npcIndex = npcs[i].npcType;
			npcs[i].facePlayer(c.playerId);
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), npcs[i].npcSize, c.getX(), c.getY(), distanceRequired(i))
					|| special) {
				if (c.respawnTimer <= 0) {// 1747, 1748, 1749, 1752
					if (npcs[i].npcId == 2862 || npcs[i].npcId == 1747 || npcs[i].npcId == 1748 || npcs[i].npcId == 1749
							|| npcs[i].npcId == 1750 || npcs[i].npcId == 1944 || npcs[i].npcId == 1946) {
						return;
					}
					npcs[i].facePlayer(c.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
					if (npcs[i].npcId == 6611 && Misc.random(8) == 2) {
						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY,
										npcs[i].npcSize, npcs[i].absX, npcs[i].absY,
										11 + distanceRequired(i) + followDistance(i))) {
									if ((PlayerHandler.players[j].underAttackBy <= 0
											&& PlayerHandler.players[j].underAttackBy2 <= 0)
											|| PlayerHandler.players[j].inMulti())
										if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel) {
											Client players = (Client) PlayerHandler.players[j];
											players.sendMessage(
													"Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
											int damage = Misc.random(40);
											players.handleHitMask(damage);
											players.dealDamage(damage);
										}
								}
							}
						}
					}
					if (npcs[i].npcId == 6611 && npcs[i].currentHealth < 127 && !spawnedHounds) {
						spawnedHounds = true;
						Server.npcHandler.spawnNpc5(6613, 3211, 3791, c.heightLevel, 0, 110, 11, 70, 70);
						Server.npcHandler.spawnNpc5(6613, 3212, 3792, c.heightLevel, 0, 110, 11, 70, 70);
						c.sendMessage("Vet'ion spawns 2 Skeleton Hellhounds..");
					}
					if (npcs[i].npcId == 6609 && npcs[i].attackType == 0) {
						int randomm = Misc.random(6);
						if (randomm == 3) {
							EventManager.getSingleton().addEvent(new Event() {

								@Override
								public void execute(EventContainer container) {
									for (int j = 0; j < PlayerHandler.players.length; j++) {
										if (PlayerHandler.players[j] != null) {
											if (goodDistance(PlayerHandler.players[j].absX,
													PlayerHandler.players[j].absY, npcs[i].absX, npcs[i].absY,
													2 + distanceRequired(i) + followDistance(i))) {
												if ((PlayerHandler.players[j].underAttackBy <= 0
														&& PlayerHandler.players[j].underAttackBy2 <= 0)
														|| PlayerHandler.players[j].inMulti())
													if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel) {
														Client players = (Client) PlayerHandler.players[j];
														players.getPA().getCallisto(npcs[i].npcId, c.absX, c.absY);
														c.sendMessage("Callisto's roar throws you back.");
													}
											}
										}
									}

									container.stop();
								}

							}, 2300);
						}
					}
					if (special)
						loadSpell2(i);
					else
						loadSpell(i);
					if (npcs[i].attackType == 3)
						npcs[i].hitDelayTimer += 2;
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						npcs[i].doAnimation(getAttackEmote(i));
						npcs[i].oldIndex = c.playerId;
						return;
					}
					if (npcs[i].projectileId > 0) {
						int nX = NPCHandler.npcs[i].getX() + offset(i);
						int nY = NPCHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
					}
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.playerId;
					npcs[i].doAnimation(getAttackEmote(i));
					c.getPA().removeAllWindows();
				}
			}
		}
	}

	public int offset(int i) {
		switch (npcs[i].npcId) {

		case 2042:
		case 2043:
		case 2044:
			return 5068;
		case 239:
		case 5363:
		case 6609:
		case 6611:
			return 2;
		case 2265:
		case 2266:
			return 1;
		case 3127:
		case 3125:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Client c, int i) { // responsible for npcs that
		// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Client c = (Client) PlayerHandler.players[npcs[i].oldIndex];
			if (c == null) {
				return;
			}
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			// npcs[i].totalAttacks++;
			if (c.respawnTimer <= 0) {
				int damage = 0;
				if (npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					// c.sendGlobal("Prior Damage, " +damage);
					// TODO NPC CONVERSION
					if (c.prayerActive[18]) {
						if (npcs[i].npcId == 963 || npcs[i].npcId == 965)
							damage = (damage / 2);
						else
							damage = 0;
						if (npcs[i].npcId == 1677) {
							damage = Misc.random(getMaxHit(i));
						}
						if (npcs[i].npcId == 2215 || npcs[i].npcId == 2205 || npcs[i].npcId == 3129) {
							int damageDivider = Misc.random(35);
							int random = Misc.random(5);
							if (random == 2) {
								damage = 0;
							} else {
								damage = (int) damageDivider;
							}
						}

						if (npcs[i].npcId == 3132 || npcs[i].npcId == 3130 || npcs[i].npcId == 3131
								|| npcs[i].npcId == 2208 || npcs[i].npcId == 2207 || npcs[i].npcId == 2206
								|| npcs[i].npcId == 6829 || npcs[i].npcId == 2218 || npcs[i].npcId == 2217
								|| npcs[i].npcId == 2216 || npcs[i].npcId == 260 || npcs[i].npcId == 5363
								|| npcs[i].npcId == 50 || npcs[i].npcId == 55 || npcs[i].npcId == 270
								|| npcs[i].npcId == 272 || npcs[i].npcId == 274 || npcs[i].npcId == 6609) {
							int damageDivider = Misc.random(npcs[i].maxHit / (int) 2.5);
							int random = Misc.random(3);
							if (random == 2) {
								damage = 0;
							} else {
								if (npcs[i].npcId == 6609) {
									damage = Misc.random(10);
								} else
									damage = (int) damageDivider;
							}
						} else if (npcs[i].npcId == 3165 || npcs[i].npcId == 3163) {
							if (10 + Misc.random((int) c.getCombat().calculateMeleeDefence(true)) < Misc
									.random(NPCHandler.npcs[i].attack) && Misc.random(3) == 1) {
								damage = Misc.random(10);
							} else
								damage = 0;
						} else {
							damage = 0;
						}
					}
					if (10 + Misc.random((int) c.getCombat().calculateMeleeDefence(true)) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					// c.sendGlobal("After Damage, " +damage);
				}

				if (npcs[i].attackType == 1) { // range
					if (npcs[i].npcId == 3162) {
						damage = Misc.random(getMaxHit(i));
					} else
						damage = Misc.random(npcs[i].maxHit);
					// TODO NPC CONVERSION
					if (npcs[i].npcId == 1675 || npcs[i].npcId == 498 || npcs[i].npcId == 499) {
						if (Misc.random(2) == 1) {
							damage = Misc.random(getMaxHit(i));
						} else {
							damage = 0;
						}
					}
					/*
					 * if (npcs[i].npcId == 2042 || npcs[i].npcId == 2044) { if
					 * (c.isSusceptibleToVenom()) { c.setVenomDamage((byte) 6);
					 * } }
					 */
					if (c.prayerActive[17]) {
						if (npcs[i].npcId == 2215 || npcs[i].npcId == 3129 || npcs[i].npcId == 2205
								|| npcs[i].npcId == 3132 || npcs[i].npcId == 3130 || npcs[i].npcId == 3131
								|| npcs[i].npcId == 2208 || npcs[i].npcId == 2207 || npcs[i].npcId == 2206
								|| npcs[i].npcId == 6829 || npcs[i].npcId == 2218 || npcs[i].npcId == 2217
								|| npcs[i].npcId == 2216 || npcs[i].npcId == 3163 || npcs[i].npcId == 3165) {
							int damageDivider = Misc.random(npcs[i].maxHit / (int) 2.5);
							int random = Misc.random(3);
							if (random == 2) {
								damage = 0;
							} else {
								damage = (int) damageDivider;
							}
						} else if (npcs[i].npcId == 498 || npcs[i].npcId == 499) {
							if (Misc.random(2) == 1) {
								damage = Misc.random(getMaxHit(i));
							} else {
								damage = 0;
							}
						} else if (npcs[i].npcId == 3164) {
							if (10 + Misc.random((int) c.getCombat().calculateRangeDefence(true)) < Misc
									.random(NPCHandler.npcs[i].attack) && Misc.random(3) == 1) {
								damage = Misc.random(10);
							} else
								damage = 0;
						} else if (npcs[i].npcId == 3162) {
							if (10 + Misc.random((int) c.getCombat().calculateRangeDefence(true)) < Misc
									.random(NPCHandler.npcs[i].attack) && Misc.random(3) == 1) {
								damage = Misc.random(20);
							} else
								damage = 0;

						} else {
							damage = 0;
						}
					}
					if (10 + Misc.random((int) c.getCombat().calculateRangeDefence(true)) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 2) { // magic
					if (npcs[i].npcId == 3162) {
						damage = Misc.random(getMaxHit(i));
					} else
						damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (npcs[i].npcId == 1672 || npcs[i].npcId == 498 || npcs[i].npcId == 499) {
						if (Misc.random(2) == 1) {
							damage = Misc.random(getMaxHit(i));
							magicFailed = false;
						} else {
							damage = 0;
							magicFailed = false;
						}
					}
					if (npcs[i].npcId == 6609) {
						damage = Misc.random(getMaxHit(i));
					}
					if (c.prayerActive[16]) {
						if (npcs[i].npcId == 963) {
							damage = (damage / 2);
						} else if (npcs[i].npcId == 498 || npcs[i].npcId == 499) {
							if (Misc.random(2) == 1) {
								damage = Misc.random(getMaxHit(i));
								magicFailed = false;
							}
							magicFailed = false;
						} else {
							damage = 0;
							magicFailed = true;
						}
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
					if (npcs[i].npcId == 3943) {
						int randomPoison = Misc.random(5);
						if (randomPoison == 3 && !c.isPoisoned) {
							c.getPA().appendPoison(10);
						}
					}
					if (10 + Misc.random((int) c.getCombat().calculateMageDefence(true)) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}
				}

				if (npcs[i].attackType == 3) { // fire breath
					int anti = c.getPA().antiFire();
					if (anti == 0) {
						damage = Misc.random(30) + 10;
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (anti == 1)
						if (Misc.random(2) == 1)
							damage = Misc.random(15);
						else
							damage = 0;
					else if (anti == 2)
						if (npcs[i].npcId == 50 || npcs[i].npcId == 5363)
							damage = Misc.random(10);
						else
							damage = Misc.random(0);
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					c.gfx100(npcs[i].endGfx);
				}
				if (c.vengOn) {
					c.getCombat().appendVengeanceNpc(i, damage);
				}
				handleSpecialEffects(c, i, damage);
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				// c.setHitDiff(damage);
				if (damage > 0)
					applyRecoil(c, damage, i);
				c.handleHitMask(damage);
				c.playerLevel[3] -= damage;
				c.getPA().refreshSkill(3);
				FightCaves.tzKihEffect(c, i, damage);
				c.updateRequired = true;
				// c.setHitUpdateRequired(true);
			}
		}
	}

	public void handleSpecialEffects(Client c, int i, int damage) {
		if (npcs[i].npcId == 2892 || npcs[i].npcId == 2894 || npcs[i].npcId == 963 || npcs[i].npcId == 965) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcId) {
		case 5816:
			return 3;
		case 499:
		case 498:
			return 8;
		case 465:
			if (npcs[i].attackType == 0)
				return 15;
			else
				return 50;
		case 6610:
			return 50;
		case 963:
			if (npcs[i].attackType == 2)
				return 30;
			else
				return 30;
		case 965:
			if (npcs[i].attackType == 2)
				return 30;
			else
				return 30;
		case 1673:
			return 65;
		case 1677:
			return 25;
		case 1672: // ahrim
			return 25;
		case 1676:
			return 23;
		case 1674:
			return 24;
		case 1675:
			return 25;
		case 239:
			if (npcs[i].attackType == 0)
				return 26;
		case 5363:
			if (npcs[i].attackType == 0)
				return 28;
		case 260:
			if (npcs[i].attackType == 0)
				return 8;
		case 265:
			if (npcs[i].attackType == 0)
				return 10;
		case 270:
			if (npcs[i].attackType == 0)
				return 12;
		case 272:
			if (npcs[i].attackType == 0)
				return 20;
		case 274:
			if (npcs[i].attackType == 0)
				return 22;
		case 2215:
			if (npcs[i].attackType == 1)
				return 35;
			else
				return 60;
		case 494:
			if (npcs[i].attackType == 2)
				return 37;
			else
				return 33;
		case 3943:
			if (npcs[i].attackType == 0)
				return 27;
			else
				return 23;
		case 3162:
			if (npcs[i].attackType == 0) {
				return 26;
			} else if (npcs[i].attackType == 1) {
				return 71;
			} else if (npcs[i].attackType == 2)
				return 21;
		case 2558:
			if (npcs[i].attackType == 2)
				return 28;
			else
				return 68;
		case 2562:
			return 31;
		case 2550:
			return 36;
		case 6609:
			return 60;
		}
		return 1;
	}

	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					if (!oldFormat) {
						newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
								Integer.parseInt(token3[3]), Integer.parseInt(token3[4]));
					} else {
						// id, x, y, z, walkingType
						newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
								Integer.parseInt(token3[3]), Integer.parseInt(token3[4]));
						definitions[Integer.parseInt(token3[0])].setMaxHit(Integer.parseInt(token3[5]));
						definitions[Integer.parseInt(token3[0])].setAttackStat(Integer.parseInt(token3[6]));
						definitions[Integer.parseInt(token3[0])].setDefenceStat(Integer.parseInt(token3[7]));

					}
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public int getNpcListHP(int npcId) {
		if (npcId < 0 || npcId >= definitions.length)
			return 0;
		NPCDefinition def = definitions[npcId];
		if (def != null)
			return def.getHealth();
		return 0;
	}

	public String getNpcListName(int npcId) {
		if (npcId < 0 || npcId >= definitions.length)
			return "nothing";
		NPCDefinition def = definitions[npcId];
		if (def != null)
			return def.getName();
		return "nothing";
	}

	public boolean loadNPCList(String FileName) {
		if (!oldFormat) {
			try {
				NPCDefinition[] loadDefinitions = (NPCDefinition[]) new Gson()
						.fromJson(new BufferedReader(new FileReader(FileName)), NPCDefinition[].class);
				NPCDefinition[] newDefinitions = new NPCDefinition[10000];
				for (int i = 0; i < loadDefinitions.length; i++) {
					int npcId = loadDefinitions[i].getId();
					if (newDefinitions[npcId] != null) {
						throw new IllegalStateException("Duplicate entry " + npcId + " in npc file!");
					}
					newDefinitions[npcId] = loadDefinitions[i];
				}
				NPCHandler.definitions = newDefinitions;
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		}
		for (int i = 0; i < definitions.length; i++) {
			definitions[i] = new NPCDefinition(i);
		}

		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), Integer.parseInt(token3[3]), 0, 0, 0, 0, 0, 0, 0);
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public void spawnNpc3(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
		// first, search for a free slot
		int slot = freeSlot();
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		newNPC.underAttack = true;
		newNPC.facePlayer(c.playerId);
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (summonFollow) {
			newNPC.summoner = true;
			newNPC.summonedBy = c.playerId;
			c.summonId = npcType;
			c.hasNpc = true;
		}
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
	}

	public int summonItemId(int itemId) {
		if (itemId == 1555)
			return 761;
		if (itemId == 1556)
			return 762;
		if (itemId == 1557)
			return 763;
		if (itemId == 1558)
			return 764;
		if (itemId == 1559)
			return 765;
		if (itemId == 1560)
			return 766;
		if (itemId == 1561)
			return 768;
		if (itemId == 1562)
			return 769;
		if (itemId == 1563)
			return 770;
		if (itemId == 1564)
			return 771;
		if (itemId == 1565)
			return 772;
		if (itemId == 1566)
			return 773;
		if (itemId == 7585)
			return 3507;
		if (itemId == 7584)
			return 3506;
		if (itemId == 7583)
			return 3505;
		if (itemId == 12649)
			return 4005;
		if (itemId == 12653)
			return 4000;
		if (itemId == 12650)
			return 4001;
		if (itemId == 12651)
			return 4007;
		if (itemId == 12652)
			return 4006;
		if (itemId == 12645)
			return 4010;
		if (itemId == 12644)
			return 4009;
		if (itemId == 12643)
			return 4008;
		return 0;
	}

	public void handleBossDeath(Client c, int i) {
		if (c != null) {
			switch (npcs[i].npcId) {
			case 6609:
				c.callistoKills++;
				c.sendMessage("Your Callisto kill count is: @red@" + c.callistoKills);
				break;
			case 3943:
				c.sendMessage("Your Sea Snake kill count is: @red@" + c.seaSnakeKills);
				c.seaSnakeKills++;
				break;
			case 2215:
				c.bandosKills++;
				c.sendMessage("Your Bandos kill count is: @red@" + c.bandosKills);
				break;
			case 3129:
				c.zammyKills++;
				c.sendMessage("Your Zammy kill count is: @red@" + c.zammyKills);
				break;
			case 2205:
				c.saraKills++;
				c.sendMessage("Your Sara kill count is: @red@" + c.saraKills);
				break;
			case 3162:
				c.armaKills++;
				c.sendMessage("Your Armadyl kill count is: @red@" + c.armaKills);
				break;
			case 2265:
				c.supremeKills++;
				c.sendMessage("Your Dagganoth Supreme kill count is: @red@" + c.supremeKills);
				break;
			case 2266:
				c.primeKills++;
				c.sendMessage("Your Dagganoth Prime kill count is: @red@" + c.primeKills);
				break;
			case 2267:
				c.rexKills++;
				c.sendMessage("Your Dagganoth Rex kill count is: @red@" + c.rexKills);
				break;
			case 239:
				c.kbdKills++;
				c.sendMessage("Your King Black Dragon kill count is: @red@" + c.kbdKills);
				break;
			case 494:
				c.seatrollKills++;
				c.sendMessage("Your Kraken kill count is: @red@" + c.seatrollKills);
				break;
			case 5363:
				c.mithKills++;
				c.sendMessage("Your Mithril Dragon kill count is: @red@" + c.mithKills);
				break;
			case 965:
				c.kalphiteKills++;
				c.sendMessage("Your Kalphite Queen kill count is: @red@" + c.kalphiteKills);
				break;
			case 6610:
				c.venenatisKills++;
				c.sendMessage("Your Venenatis kill count is: @red@" + c.venenatisKills);
				break;
			case 6611:
				c.vetionKills++;
				c.sendMessage("Your Vet'ion kill count is: @red@" + c.vetionKills);
				break;
			case 499:
				c.smokeKills++;
				c.sendMessage("Your Thermonuclear Smoke Devil kill count is: @red@" + c.smokeKills);
				break;
			case 2042:
			case 2043:
			case 2044:
				c.zulrahKills++;
				c.sendMessage("Your Zulrah kill count is: @red@" + c.zulrahKills);
				break;
			case 2054:
				c.chaosKills++;
				c.sendMessage("Your Chaos Elemntal kill count is: @red@" + c.chaosKills);
				break;
			}
		}
	}

	private void spawnSecondForm(final int i) {
		// npcs[i].gfx0(1055);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer e) {

				spawnNpc2(965, npcs[i].absX, npcs[i].absY, 0, 1, 230, 45, 500, 300);
				e.stop();
			}
		}, 3000);
	}

	/**
	 * kq respawn first form
	 */
	private void spawnFirstForm(final int i) {
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer e) {

				spawnNpc2(963, npcs[i].absX, npcs[i].absY, 0, 1, 230, 45, 500, 300);
				e.stop();

			}

		}, 10000);
	}

	public boolean checkSlayerHelm(Client c, int i) {
		if (c.slayerTask == npcs[i].npcId || c.duoTask == npcs[i].npcId && c.playerEquipment[c.playerHat] == 11864) {
			return true;
		}
		return false;
	}

	public void removeNPC(int slot) {
		npcs[slot] = null;
	}

	public void applyRecoil(Client c, int damage, int i) {
		if (damage > 0 && c.playerEquipment[c.playerRing] == 2550) {
			NPC n = npcs[i];
			if (n == null)
				return;
			int recDamage = damage / 10 + 1;
			/*
			 * if (!n.getHitUpdateRequired()) { n.setHitDiff(recDamage);
			 * n.setHitUpdateRequired(true); } else if
			 * (!n.getHitUpdateRequired2()) { n.setHitDiff2(recDamage);
			 * n.setHitUpdateRequired2(true); }
			 */
			n.handleHitMask(recDamage);
			n.dealDamage(recDamage);
			n.updateRequired = true;
			c.recoilDamage -= recDamage;
			if (c.recoilDamage <= 0) {
				c.playerEquipment[c.playerRing] = -1;
				c.playerEquipmentN[c.playerRing] = 0;
				c.getItems().updateSlot(c.playerRing);
				c.sendMessage("Your ring of recoil shatters into dust.");
			}
		}
	}

	public static NPC spawnNpc(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon, int anim, String chat) {
		// first, search for a free slot
		int slot = freeSlot();
		/*
		 * if (slot == -1) { // Misc.println("No Free Slot"); return slot; // no
		 * free slot found }
		 */
		NPC newNPC = NPC.createNPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		newNPC.doAnimation(anim);
		newNPC.forceChat(chat);
		npcs[slot] = newNPC;
		return newNPC;
	}

	public static NPC spawnZulrah(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon, int anim, String chat) {
		// first, search for a free slot
		int slot = freeSlot();

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.currentHealth = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		newNPC.doAnimation(anim);
		newNPC.forceChat(chat);
		npcs[slot] = newNPC;
		return newNPC;
	}
}
