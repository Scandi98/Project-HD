package server.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apollo.fs.Cache;
import org.apollo.fs.IndexedFileSystem;

import server.cache.RSNPCDefinition;
import server.model.npcs.NPC;
import server.model.npcs.NPCDefinition;
import server.model.npcs.NPCHandler;
import server.util.Misc;

public class NPCConverter {

	static NPC[] npcs;

	public static void main(String[] args) throws Exception {
		Cache.fileSystem = new IndexedFileSystem(new File("cache"), true);
		RSNPCDefinition.fullParse();

		new NPCHandler(true);
		npcs = NPCHandler.npcs;

		NPCDefinition[] newDefinitions = new NPCDefinition[NPCHandler.definitions.length];
		for (int i = 0; i < NPCHandler.definitions.length; i++) {
			newDefinitions[i] = NPCHandler.definitions[i];
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Data/cfg/npc_translation.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] npcData = line.split("-");
				int npcId = Integer.parseInt(npcData[0]);

				NPCDefinition n = NPCHandler.definitions[npcId];
				if (n == null) {
					n = new NPCDefinition(npcId);
					NPCHandler.definitions[npcId] = n;
				}
				n.translationId = Integer.parseInt(npcData[1]);
			}

			BufferedWriter notUpdated = new BufferedWriter(new FileWriter("not_updated.txt"));
			List<Integer> accountedFor = new ArrayList<Integer>();
			List<NPCDefinition> toWrite = new ArrayList<NPCDefinition>();
			/*for (int i = 0; i < NPCHandler.definitions.length; i++) {
				NPCDefinition n = NPCHandler.definitions[i];
				if (!n.defined)
					continue;
				NPC npc = null;
				for (int k = 0; k < NPCHandler.npcs.length; k++) {
					if (NPCHandler.npcs[k] != null && NPCHandler.npcs[k].npcId == i) {
						npc = NPCHandler.npcs[k];
					}
				}

				if (!n.hasCacheDefinition()) {
					System.err.println(n.getId() + " no longer exists");
				}
				if (n.translationId == n.getId()) {
					notUpdated.write(n.getId() + "(" + n.getName() + ") (Lvl. " + n.getCombatLevel() + ") has not been updated");
					notUpdated.newLine();
					continue;
				}
				if (n != null && n.hasCacheDefinition() && n.defined || n.translationId != n.getId()) {
					if (npc != null) {
						NPCDefinition def = newDefinitions[n.translationId] = new NPCDefinition(n.translationId);
						
						def.setHealth(n.getHealth());
						def.setAttackStat(npc.attack);
						def.setDefenceStat(npc.defence);
						def.setMaxHit(npc.maxHit);
						def.setAttackEmote(getAttackEmote(npc));
						def.setDefenceEmote(-1);
						def.setDeathEmote(getDeadEmote(npc));
						def.setRespawnTime(getRespawnTime(npc));
						def.updated = true;
						accountedFor.add(n.translationId);
					} else {
						toWrite.add(n);
					}
				}
			}
			for (NPCDefinition n : toWrite) {
				if (!accountedFor.contains(n.getId())) {
					System.out.println(n.getId());
					NPC n2 = NPC.createNPC(0, n.getId());
					NPCDefinition def = newDefinitions[n.translationId] = new NPCDefinition(n.translationId);
					
					def.setHealth(n.getHealth());
					def.setAttackStat(n2.attack);
					def.setDefenceStat(n2.defence);
					def.setMaxHit(n2.maxHit);
					def.setAttackEmote(getAttackEmote(n2));
					def.setDefenceEmote(-1);
					def.setDeathEmote(getDeadEmote(n2));
					def.setRespawnTime(getRespawnTime(n2));
					def.updated = false;
				}
			}
			notUpdated.flush();*/

			BufferedWriter writer = new BufferedWriter(new FileWriter("Data/cfg/npc_spawns.cfg"));

			writer.write("// id, x, y, z, walking type");
			writer.newLine();
			for (int i2 = 0; i2 < NPCHandler.npcs.length; i2++) {
				NPC n1 = NPCHandler.npcs[i2];
				if (n1 == null)
					continue;
				NPCDefinition nl = n1.getDefinition();
				writer.write("spawn\t=\t" + nl.translationId + "\t" + n1.absX + "\t" + n1.absY + "\t" + n1.heightLevel + "\t" + n1.walkingType);
				writer.newLine();
			}
			writer.write("[ENDOFSPAWNLIST]");
			writer.flush();

			reader.close();
			

			/*String string = new GsonBuilder().setPrettyPrinting().create().toJson(newDefinitions);
			writer = new BufferedWriter(new FileWriter("Data/json/npc_definitions.json"));
			writer.write(string);
			writer.flush();*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getAttackEmote(NPC npc) {
		if (npc.npcId >= 3732 && npc.npcId <= 3741) {
			return 3901;
		}
		if (npc.npcId >= 3742 && npc.npcId <= 3746) {
			return 3915;
		}
		if (npc.npcId >= 3747 && npc.npcId <= 3751) {
			return 3908;
		}
		if (npc.npcId >= 3752 && npc.npcId <= 3761) {
			return 3880;
		}
		if (npc.npcId >= 3762 && npc.npcId <= 3771) {
			return 3920;
		}
		if (npc.npcId >= 3772 && npc.npcId <= 3776) {
			return 3896;
		}
		switch (npc.npcId) {
		case 5529:
			return 5782;
		case 3151:
			return 5485;
		case 4056:
			return 5804;
		case 3068:// wyv
			if (npc.attackType == 0)
				return 2989;
			if (npc.attackType == 3)
				return 2989;
		case 4173:
			return 5327;
		case 3847:
			if (npc.attackType == 2)
				return 3991;
			else
				return 3992;
		case 1158: // kalphite
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 6223;
		case 1160: // kalphite
			return 6235;
		case 105:
			return 4922;
		case 3943:
			return 1067;
		case 1618:
			return 1552;
		case 419: // zombie
		case 420:
		case 421:
		case 422:
		case 423:
		case 424:
			return 5568;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 94;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 284;
		case 413:
		case 414:
		case 415:
		case 416:
		case 417:// rock golem
		case 418:
			return 153;
		case 112: // moss
			return 4658;
		case 103: // ghost
			return 5540;
		case 78: // bats
			return 4915;
		case 1612: // banshee
			return 1523;
		case 2783: // dark beast
			return 2731;
		case 6248:
			return 6376;
		case 6250:
			return 7018;
		case 6261:
		case 6263:
		case 6265:
			return 6154;
		case 6204:
		case 6206:
		case 6208:
			return 6945;
		case 49:
		case 1575:
			return 6579;
		case 6247:
			return 6964;
		case 6203:
			return 6945;
		case 6223:
		case 6225:
		case 6227:
			return 6953;
		case 6222:
			return 6976;
		case 6267:
			return 359;
		case 6268:
			return 2930;
		case 6269:
			return 4652;
		case 6270:
			return 4652;
		case 6271:
			return 4320;
		case 6272:
			return 4320;
		case 6273:
			return 4320;
		case 6274:
			return 4320;
		case 1459:
			return 1402;
		case 6260:
			if (npc.attackType == 0)
				return 7060;
			else
				return 7063;
		case 86:
		case 87:
			return 4933;
		case 871:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 359;

		case 2892:
		case 2894:
			return 2868;
		case 2627:
			return 2621;
		case 2630:
			return 2625;
		case 2631:
			return 2633;
		case 2741:
			return 2637;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
		case 5247:
			return 5411;
		case 13: // wizards
			return 711;

		case 655:
			return 5532;

		case 2640:
		case 3153:
			return 1626;
		case 1624:
			return 1557;

		case 1648:
			return 1590;

		case 1615: // abby demon
			return 1537;

		case 1613: // nech
			return 1528;

		case 1610:
		case 1611: // garg
			return 1519;

		case 1616: // basilisk
			return 1546;

			// case 459: //skele
			// return 260;

		case 50:// drags
			return 80;
		case 53:
		case 54:
		case 55:
		case 941:
		case 5363:
		case 1590:
		case 1591:
		case 1592:
			return 80;
		case 124: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 134:
			return 143;

		case 106:// Bear
			return 41;

		case 412:
			// case 78:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 1769:
		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101: // goblin
			return 6184;

		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5849;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:

			return 1341;

		case 19: // white knight
			return 406;

		case 110:
		case 111: // ice giant
		case 117:
			return 4651;

		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 119:
			return 99;

		case 82:// Lesser Demon
		case 83:// Greater Demon
		case 84:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 1265:
			return 1312;

		case 125: // ice warrior
		case 178:
			return 451;

		case 123:
		case 122:
			return 164;

		case 2028: // karil
			return 2075;

		case 2025: // ahrim
			return 729;

		case 2026: // dharok
			return 2067;

		case 2027: // guthan
			return 2080;

		case 2029: // torag
			return 0x814;

		case 2030: // verac
			return 2062;

		case 2881: // supreme
			return 2855;

		case 2882: // prime
			return 2854;

		case 2883: // rex
			return 2851;
		case 5666:
			int test = Misc.random(2);
			if (test == 2) {
				return 5895;
			} else if (test == 1) {
				return 5894;
			} else {
				return 5896;
			}

		case 3200:
			return 3146;

		case 2745:
			if (npc.attackType == 2)
				return 2656;
			else if (npc.attackType == 1)
				return 2652;
			else if (npc.attackType == 0)
				return 2655;

		default:
			return 0x326;
		}
	}

	public static int getDeadEmote(NPC npc) {
		if (npc.npcId >= 3732 && npc.npcId <= 3741) {
			return 3903;
		}
		if (npc.npcId >= 3742 && npc.npcId <= 3746) {
			return 3917;
		}
		if (npc.npcId >= 3747 && npc.npcId <= 3751) {
			return 3909;
		}
		if (npc.npcId >= 3752 && npc.npcId <= 3761) {
			return 3881;
		}
		if (npc.npcId >= 3762 && npc.npcId <= 3771) {
			return 3922;
		}
		if (npc.npcId >= 3772 && npc.npcId <= 3776) {
			return 3894;
		}
		switch (npc.npcId) {
		case 5529:
			return 5784;
		case 3151:
			return 5491;
		case 3068:// wyv
			return 2987;// wyv death
		case 4173:
			return 5329;
		case 1158:
			return 6242;
		case 1160:
			return 6234;
		case 3847:
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 6230;
		case 3493:
			return 1067;
		case 871:// Ogre Shaman
			return 361;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 97;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 287;
		case 413:
		case 414:
		case 415:
		case 416:
		case 417:// rock golem
		case 418:
			return 156;
		case 419:// zombie
		case 420:
		case 421:
		case 422:
		case 423:
		case 424:
			return 5569;
			// begin new updates
		case 112: // moss
			return 4659;
		case 103: // ghost
			return 5542;
		case 78: // bats
			return 4917;
			// end
		case 6223:
		case 6225:
		case 6227:
			return 6956;
		case 6248:
			return 6377;
		case 6250:
			return 7016;
		case 6261:
		case 6263:
		case 6265:
			return 6156;
		case 6204:
		case 6206:
		case 6208:
			return 6946;
		case 6142:
			return 1;
		case 6143:
			return 1;
		case 6144:
			return 1;
		case 6145:
			return 1;
		case 1265:// RockCrab
			return 1314;
		case 914:// Battle Mage
			return 196;
		case 3742:// Ravager
		case 3743:
		case 3744:
		case 3745:
		case 3746:
			return 3916;
		case 3772:// Brawler
		case 3773:
		case 3774:
		case 3775:
		case 3776:
			return 3894;
		case 49:
		case 1575:
			return 6576;
		case 6247:
			return 6965;
		case 6203:
			return 6945;
		case 6260:
			return 7062;
		case 6222:
			return 6975;
		case 6267:
			return 357;
		case 6268:
			return 2938;
		case 6269:
			return 4653;
		case 6270:
			return 4653;
		case 6271:
			return 4321;
		case 6272:
			return 4321;
		case 6273:
			return 4321;
		case 6274:
			return 4321;
		case 117:
			return 4651;
		case 1459:
			return 1404;
		case 1612: // banshee
			return 1524;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
			return 2627;
		case 2631:
			return 2630;
		case 2738:
			return 2627;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			return 2654;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;
		case 5666:
			return 5898;
		case 3200:
			return 3147;
		case 2035: // spider
			return 146;
		case 2033: // rat
			return 141;
		case 2031: // bloodvel
			return 2073;
		case 1769:
		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101: // goblin
			return 6182;
		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5851;
		case 41: // chicken
			return 57;
		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;
		case 2881:
		case 2882:
		case 2883:
			return 2856;
		case 111: // ice giant
			return 131;
		case 125: // ice warrior
			return 843;
		case 751:// Zombies!!
			return 302;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;
		case 1616: // basilisk
			return 1548;
		case 1653: // hand
			return 1590;
		case 82:// demons
		case 83:
		case 84:
			return 67;
		case 1605:// abby spec
			return 1508;
		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;
		case 1610:
		case 1611:
			return 1518;
		case 1618:
		case 1619:
			return 1553;
		case 1620:
		case 1621:
			return 1563;
		case 2783:
			return 2732;
		case 1615:
			return 1538;
		case 2640:
		case 3153:
			return -1;
		case 1624:
			return 1558;
		case 1613:
			return 1530;
		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;
		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;
		case 100:
		case 102:
			return 313;
		case 105:
		case 106:
			return 4930;
		case 412:
			// case 78:
			return 36;
		case 122:
		case 123:
			return 167;
		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 134:
			return 146;
			// case 103:
		case 104:
			return 5534;
		case 118:
		case 119:
			return 102;
		case 50:// drags
			return 92;
		case 53:
		case 54:
		case 55:
		case 941:
		case 5363:
		case 1590:
		case 1591:
		case 1592:
			return 92;
		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 **/
	public static int getNpcDelay(NPC npc) {
		switch (npc.npcId) {
		case 2640:
		case 3153:
			return 2;
		case 2025:
		case 2028:
		case 3847:
		case 105:
		case 3943:
		case 4173:
			return 7;

		case 2745:
			return 8;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 6260:
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
	public static int getHitDelay(NPC npc) {
		switch (npc.npcId) {
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
			return 3;

		case 2743:
		case 2631:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 6260:
			npc.graardor = Misc.random(5);
			if (npc.graardor == 5) {
				return 3;
			}
			return 2;

		case 6203:
			npc.tsutsaroth = Misc.random(4);
			if (npc.tsutsaroth == 4) {
				return 3;
			}
			return 2;

		case 6247:
			npc.zilyana = Misc.random(6);
			if (npc.zilyana == 6 || npc.zilyana == 5
					|| npc.zilyana == 4) {
				return 6;
			}
			return 2;
		case 3847:
		case 4173:
		case 3943:
		case 105:
			return 5;
		case 2745:
			if (npc.attackType == 1 || npc.attackType == 2)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;
		case 2640:
		case 3153:
			return 2;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public static int getRespawnTime(NPC npc) {
		switch (npc.npcId) {
		case 1575:
			return -1;
		case 6142:
		case 6143:
		case 6144:
		case 6145:
			return -1;
		case 1158:
		case 1160:
			return -1;
		case 2881:
		case 2882:
		case 2883:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 6260:
		case 6222:
		case 6203:
		case 6247:
		case 3847:
		case 4173:
		case 105:
		case 3943:
		case 3068:
		case 5666:
			return 60;

		case 2640:
		case 3153:
			return 20;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return 500;
		default:
			if (npc.maximumHealth > 100) {
				return npc.maximumHealth / 2;
			}
			return 25;
		}
	}

}
