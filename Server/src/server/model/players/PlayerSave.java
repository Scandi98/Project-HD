
package server.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import server.Config;
import server.core.PlayerHandler;
import server.util.Misc;

public class PlayerSave {

	/**
	 * Loading
	 **/
	public static int loadGame(Client p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;

		try {
			characterfile = new BufferedReader(new FileReader(
					Config.LOAD_DIRECTORY + "characters/" + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (File1) {
			// new File ("./characters/"+playerName+".txt");
		} else {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210
								: Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424
								: Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						p.playerRights = Integer.parseInt(token2);
					} else if (token.equals("timePlayed")) {
						p.timePlayed = Long.parseLong(token2);
					} else if (token.equals("character-title")) {
						p.playerTitle = token2;
					} else if (token.equals("character-title-color")) {
						p.titleColor = Integer.parseInt(token2);
					} else if (token.equals("bankPin")) {
						p.bankPin = token2;
					} else if (token.equals("setPin")) {
						p.setPin = Boolean.parseBoolean(token2);
					} else if (token.equals("doubleExpTimer")) {
						p.time = Integer.parseInt(token2);
					} else if (token.equals("TotalXp")) {
						p.totalXp = Integer.parseInt(token2);
					} else if (token.equals("doubleExp")) {
						p.doubleExp = Boolean.parseBoolean(token2);
					} else if (token.equals("poison")) {
						p.poisonDamage = Integer.parseInt(token2);
					} else if (token.equals("summonId")) {
						p.summonId = Integer.parseInt(token2);
					} else if (token.equals("waveId")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("petID")) {
						p.petID = Integer.parseInt(token2);
					} else if (token.equals("petSummoned")) {
						p.setPetSummoned(Boolean.parseBoolean(token2));
					} else if (token.equals("has-npc")) {
						p.hasNpc = Boolean.parseBoolean(token2);
					} else if (token.equals("easterStage")) {
						p.easterStage = Integer.parseInt(token2);
					} else if (token.equals("startedEvent")) {
						p.startedEvent = Boolean.parseBoolean(token2);
					} else if (token.equals("eggsCollected")) {
						p.eggsCollected = Integer.parseInt(token2);
					} else if (token.equals("egg1Collected")) {
						p.egg1Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("egg2Collected")) {
						p.egg2Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("egg3Collected")) {
						p.egg3Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("egg4Collected")) {
						p.egg4Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("egg5Collected")) {
						p.egg5Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("egg6Collected")) {
						p.egg6Collected = Boolean.parseBoolean(token2);
					} else if (token.equals("tutorial-progress")) {
						p.tutorial = Integer.parseInt(token2);
					} else if (token.equals("slayerPoints")) {
						p.slayerPoints = Integer.parseInt(token2);
					} else if (token.equals("duoSlayerPoints")) {
						p.duoPoints = Integer.parseInt(token2);
					} else if (token.equals("bandosKills")) {
						p.bandosKills = Integer.parseInt(token2);
					} else if (token.equals("armaKills")) {
						p.armaKills = Integer.parseInt(token2);
					} else if (token.equals("zammyKills")) {
						p.zammyKills = Integer.parseInt(token2);
					} else if (token.equals("saraKills")) {
						p.saraKills = Integer.parseInt(token2);
					} else if (token.equals("seatrollKills")) {
						p.seatrollKills = Integer.parseInt(token2);
					} else if (token.equals("rexKills")) {
						p.rexKills = Integer.parseInt(token2);
						
					} else if (token.equals("moneyPouch")) {
						p.moneyPouch = Long.parseLong(token2);
						
						
					} else if (token.equals("primeKills")) {
						p.primeKills = Integer.parseInt(token2);
					} else if (token.equals("supremeKills")) {
						p.supremeKills = Integer.parseInt(token2);
					} else if (token.equals("kbdKills")) {
						p.kbdKills = Integer.parseInt(token2);
					} else if (token.equals("mithKills")) {
						p.mithKills = Integer.parseInt(token2);
					} else if (token.equals("callistoKills")) {
						p.callistoKills = Integer.parseInt(token2);
					} else if (token.equals("seaSnakeKills")) {
						p.seaSnakeKills = Integer.parseInt(token2);
					} else if (token.equals("kalphiteKills")) {
						p.kalphiteKills = Integer.parseInt(token2);
					} else if (token.equals("venenatisKills")) {
						p.venenatisKills = Integer.parseInt(token2);
					} else if (token.equals("vetionKills")) {
						p.vetionKills = Integer.parseInt(token2);
					} else if (token.equals("chaosKills")) {
						p.chaosKills = Integer.parseInt(token2);
					} else if (token.equals("smokeKills")) {
						p.smokeKills = Integer.parseInt(token2);
					} else if (token.equals("zulrahKills")) {
						p.zulrahKills = Integer.parseInt(token2);
					} else if (token.equals("bestZulrahTime")) {
						p.bestZulrahTime = Long.parseLong(token2);
					} else if (token.equals("bossPasses")) {
						p.bossPasses = Integer.parseInt(token2);
					} else if (token.equals("easyClue")) {
						p.easyClue = Integer.parseInt(token2);
					} else if (token.equals("medClue")) {
						p.medClue = Integer.parseInt(token2);
					} else if (token.equals("hardClue")) {
						p.hardClue = Integer.parseInt(token2);
					} else if (token.equals("cwPoints")) {
						p.cwPoints = Integer.parseInt(token2);
					} else if (token.equals("barrowsChests")) {
						p.barrowsChests = Integer.parseInt(token2);
					} else if (token.equals("darts")) {
						p.darts = Integer.parseInt(token2);
					} else if (token.equals("dartType")) {
						p.dartType = Integer.parseInt(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("hunterPoints")) {
						p.hunterPoints = Integer.parseInt(token2);
					} else if (token.equals("xpLock")) {
						p.xpLock = Boolean.parseBoolean(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer
								.parseInt(token3[1]);
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("donPoints")) {
						p.donPoints = Integer.parseInt(token2);
					} else if (token.equals("votPoints")) {
						p.votPoints = Integer.parseInt(token2);
					} else if (token.equals("pkPoints")) {
						p.pkPoints = Integer.parseInt(token2);
					} else if (line.startsWith("KC")) {
						p.KC = Integer.parseInt(token2);
					} else if (line.startsWith("DC")) {
						p.DC = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.pcPoints = Integer.parseInt(token2);
					} else if (token.equals("killStreak")) {
						p.killStreak = Integer.parseInt(token2);
					} else if (token.equals("slayerTask")) {
						p.slayerTask = Integer.parseInt(token2);
					} else if (token.equals("taskAmount")) {
						p.taskAmount = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						p.magePoints = Integer.parseInt(token2);
					} else if (token.equals("run-energy")) {
						p.runEnergy = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("barrowskillcount")) {
						p.barrowsKillCount = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						p.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("replaced")) {
						p.replaced = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						p.killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank1")) {
						p.bankItems1[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems1N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank2")) {
						p.bankItems2[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems2N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank3")) {
						p.bankItems3[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems3N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank4")) {
						p.bankItems4[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems4N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank5")) {
						p.bankItems5[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems5N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank6")) {
						p.bankItems6[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems6N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank7")) {
						p.bankItems7[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems7N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					} else if (token.equals("character-bank8")) {
						p.bankItems8[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.bankItems8N[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.friends[Integer.parseInt(token3[0])] = Long
								.parseLong(token3[1]);
					}
					break;
				case 9:
					if (token.equals("BAGITEM")) {
						p.getBag().loadLootingBag(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]));
					}
					break;
				case 10:
					if (token.equals("claimedEasy")) {
						p.claimedEasy = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("claimedMedium")) {
						p.claimedMedium = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("claimedHard")) {
						p.claimedHard = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("easyBosses")) {
						p.easyBosses = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumBosses")) {
						p.mediumBosses = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardBosses")) {
						p.hardBosses = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("easySharks")) {
						p.easySharks = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumSharks")) {
						p.mediumSharks = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardSharks")) {
						p.hardSharks = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("sharksFished")) {
						p.sharksFished = Integer.parseInt(token3[0]);
					} else if (token.equals("easyGlories")) {
						p.easyGlories = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumGlories")) {
						p.mediumGlories = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardGlories")) {
						p.hardGlories = Boolean.parseBoolean(token3[0]);
			
					} else if (token.equals("gloriesCrafted")) {
						p.gloriesCrafted = Integer.parseInt(token3[0]);
					} else if (token.equals("easyMagics")) {
						p.easyMagics = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumMagics")) {
						p.mediumMagics = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardMagics")) {
						p.hardMagics = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("magicsCut")) {
						p.magicsCut = Integer.parseInt(token3[0]);
					} else if (token.equals("easyBurned")) {
						p.easyBurned = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumBurned")) {
						p.mediumBurned = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardBurned")) {
						p.hardBurned = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("magicsBurned")) {
						p.magicsBurned = Integer.parseInt(token3[0]);
					} else if (token.equals("easyBows")) {
						p.easyBows = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumBows")) {
						p.mediumBows = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardBows")) {
						p.hardBows = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("bowsMade")) {
						p.bowsMade = Integer.parseInt(token3[0]);
					} else if (token.equals("easyHerbs")) {
						p.easyHerbs = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("mediumHerbs")) {
						p.mediumHerbs = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardHerbs")) {
						p.hardHerbs = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("herbsFarmed")) {
						p.herbsFarmed = Integer.parseInt(token3[0]);
					} else if (token.equals("mediumTasks")) {
						p.mediumTasks = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardTasks")) {
						p.hardTasks = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("slayerTasksCompleted")) {
						p.slayerTasksCompleted = Integer.parseInt(token3[0]);
					} else if (token.equals("mediumDuo")) {
						p.mediumDuo = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("hardDuo")) {
						p.hardDuo = Boolean.parseBoolean(token3[0]);
					} else if (token.equals("duoSlayerTasksCompleted")) {
						p.duoSlayerTasksCompleted = Integer.parseInt(token3[0]);
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[LOOTING-BAG]")) {
					ReadMode = 9;
				} else if (line.equals("[ACHIEVEMENTS]")) {
					ReadMode = 10;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
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
		return 13;
	}

	/**
	 * Saving
	 **/
	public static boolean saveGame(Client p) {
		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null || PlayerHandler.players[p.playerId] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(
					Config.LOAD_DIRECTORY + "characters/" + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(p.playerPass, 0, p.playerPass.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer
					.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0,
					Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0,
					Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(p.playerRights), 0, Integer
					.toString(p.playerRights).length());
			characterfile.newLine();
			
			
			
			characterfile.write("moneyPouch = ", 0, 13);
			characterfile.write(Long.toString(p.moneyPouch), 0, Long.toString(p.moneyPouch).length());
			characterfile.newLine();
			
			
			
			characterfile.write("UUID = ", 0, 7);
			characterfile.write(p.UUID, 0, p.UUID.length());
			characterfile.newLine();
			characterfile.write("MAC = ", 0, 6);
			characterfile.write(p.MAC, 0, p.MAC.length());
			characterfile.newLine();
			characterfile.write("timePlayed = ", 0, 13);
			characterfile.write(Long.toString(p.timePlayed), 0,
					Long.toString(p.timePlayed).length());
			characterfile.newLine();
			characterfile.write("character-title = ", 0, 18);
			characterfile.write(p.playerTitle, 0, p.playerTitle.length());
			characterfile.newLine();
			characterfile.write("character-title-color = ", 0, 24);
			characterfile.write(Integer.toString(p.titleColor), 0, Integer
					.toString(p.titleColor).length());
			characterfile.newLine();
			characterfile.write("bankPin = ", 0, 10);
			characterfile.write(p.bankPin, 0, p.bankPin.length());
			characterfile.newLine();
			characterfile.write("setPin = ", 0, 9);
			characterfile.write(Boolean.toString(p.setPin), 0, Boolean
					.toString(p.setPin).length());
			characterfile.newLine();
			characterfile.write("has-npc = ", 0, 10);
			characterfile.write(Boolean.toString(p.hasNpc), 0, Boolean
					.toString(p.hasNpc).length());
			characterfile.newLine();
			characterfile.write("easterStage = ", 0, 14);
			characterfile.write(Integer.toString(p.easterStage), 0, Integer
					.toString(p.easterStage).length());
			characterfile.newLine();
			characterfile.write("startedEvent = ", 0, 15);
			characterfile.write(Boolean.toString(p.startedEvent), 0, Boolean
					.toString(p.startedEvent).length());
			characterfile.newLine();
			characterfile.write("eggsCollected = ", 0, 16);
			characterfile.write(Integer.toString(p.eggsCollected), 0, Integer
					.toString(p.eggsCollected).length());
			characterfile.newLine();
			characterfile.write("egg1Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg1Collected), 0, Boolean
					.toString(p.egg1Collected).length());
			characterfile.newLine();
			characterfile.write("egg2Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg2Collected), 0, Boolean
					.toString(p.egg2Collected).length());
			characterfile.newLine();
			characterfile.write("egg3Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg3Collected), 0, Boolean
					.toString(p.egg3Collected).length());
			characterfile.newLine();
			characterfile.write("egg4Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg4Collected), 0, Boolean
					.toString(p.egg4Collected).length());
			characterfile.newLine();
			characterfile.write("egg5Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg5Collected), 0, Boolean
					.toString(p.egg5Collected).length());
			characterfile.newLine();
			characterfile.write("egg6Collected = ", 0, 16);
			characterfile.write(Boolean.toString(p.egg6Collected), 0, Boolean
					.toString(p.egg6Collected).length());
			characterfile.newLine();
			characterfile.write("doubleExpTimer = ", 0, 17);
			characterfile.write(Integer.toString((int) p.time), 0, Integer
					.toString((int) p.time).length());
			characterfile.newLine();
			characterfile.write("TotalXp = ", 0, 10);
			characterfile.write(Integer.toString((int) p.totalXp), 0, Integer
					.toString((int) p.totalXp).length());//need to add the totalxp in addskillxp kk try it
			characterfile.newLine();
			characterfile.write("doubleExp = ", 0, 12);
			characterfile.write(Boolean.toString(p.doubleExp), 0, Boolean
					.toString(p.doubleExp).length());
			characterfile.newLine();
			characterfile.write("posionDamage = ", 0, 15);
			characterfile.write(Integer.toString(p.poisonDamage), 0, Integer
					.toString(p.poisonDamage).length());
			characterfile.newLine();
			characterfile.write("summonId = ", 0, 11);
			characterfile.write(Integer.toString(p.summonId), 0, Integer
					.toString(p.summonId).length());
			characterfile.newLine();
			characterfile.write("waveId = ", 0, 9);
			characterfile.write(Integer.toString(p.waveId), 0, Integer
					.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("petSummoned = ", 0, 14);
			characterfile.write(Boolean.toString(p.getPetSummoned()), 0,
					Boolean.toString(p.getPetSummoned()).length());
			characterfile.newLine();
			characterfile.write("petID = ", 0, 8);
			characterfile.write(Integer.toString(p.petID), 0,
					Integer.toString(p.petID).length());
			characterfile.newLine();
			characterfile.write("slayerPoints = ", 0, 14);
			characterfile.write(Integer.toString(p.slayerPoints), 0, Integer
					.toString(p.slayerPoints).length());
			characterfile.newLine();
			characterfile.write("duoSlayerPoints = ", 0, 18);
			characterfile.write(Integer.toString(p.duoPoints), 0, Integer
					.toString(p.duoPoints).length());
			characterfile.newLine();
			characterfile.write("bandosKills = ", 0, 14);
			characterfile.write(Integer.toString(p.bandosKills), 0, Integer
					.toString(p.bandosKills).length());
			characterfile.newLine();
			characterfile.write("armaKills = ", 0, 12);
			characterfile.write(Integer.toString(p.armaKills), 0, Integer
					.toString(p.armaKills).length());
			characterfile.newLine();
			characterfile.write("zammyKills = ", 0, 13);
			characterfile.write(Integer.toString(p.zammyKills), 0, Integer
					.toString(p.zammyKills).length());
			characterfile.newLine();
			characterfile.write("saraKills = ", 0, 12);
			characterfile.write(Integer.toString(p.saraKills), 0, Integer
					.toString(p.saraKills).length());
			characterfile.newLine();
			characterfile.write("seatrollKills = ", 0, 16);
			characterfile.write(Integer.toString(p.seatrollKills), 0, Integer
					.toString(p.seatrollKills).length());
			characterfile.newLine();
			characterfile.write("rexKills = ", 0, 11);
			characterfile.write(Integer.toString(p.rexKills), 0, Integer
					.toString(p.rexKills).length());
			characterfile.newLine();
			characterfile.write("primeKills = ", 0, 13);
			characterfile.write(Integer.toString(p.primeKills), 0, Integer
					.toString(p.primeKills).length());
			characterfile.newLine();
			characterfile.write("supremeKills = ", 0, 15);
			characterfile.write(Integer.toString(p.supremeKills), 0, Integer
					.toString(p.supremeKills).length());
			characterfile.newLine();
			characterfile.write("kbdKills = ", 0, 11);
			characterfile.write(Integer.toString(p.kbdKills), 0, Integer
					.toString(p.kbdKills).length());
			characterfile.newLine();
			characterfile.write("mithKills = ", 0, 12);
			characterfile.write(Integer.toString(p.mithKills), 0, Integer
					.toString(p.mithKills).length());
			characterfile.newLine();
			characterfile.write("callistoKills = ", 0, 16);
			characterfile.write(Integer.toString(p.callistoKills), 0, Integer
					.toString(p.callistoKills).length());
			characterfile.newLine();
			characterfile.write("seaSnakeKills = ", 0, 16);
			characterfile.write(Integer.toString(p.seaSnakeKills), 0, Integer
					.toString(p.seaSnakeKills).length());
			characterfile.newLine();
			characterfile.write("kalphiteKills = ", 0, 16);
			characterfile.write(Integer.toString(p.kalphiteKills), 0, Integer
					.toString(p.kalphiteKills).length());
			characterfile.newLine();
			characterfile.write("venenatisKills = ", 0, 17);
			characterfile.write(Integer.toString(p.venenatisKills), 0, Integer
					.toString(p.venenatisKills).length());
			characterfile.newLine();
			characterfile.write("vetionKills = ", 0, 14);
			characterfile.write(Integer.toString(p.vetionKills), 0, Integer
					.toString(p.vetionKills).length());
			characterfile.newLine();
			characterfile.write("chaosKills = ", 0, 13);
			characterfile.write(Integer.toString(p.chaosKills), 0, Integer
					.toString(p.chaosKills).length());
			characterfile.newLine();
			characterfile.write("smokeKills = ", 0, 13);
			characterfile.write(Integer.toString(p.smokeKills), 0, Integer
					.toString(p.smokeKills).length());
			characterfile.newLine();
			characterfile.write("zulrahKills = ", 0, 14);
			characterfile.write(Integer.toString(p.zulrahKills), 0, Integer.toString(p.zulrahKills).length());
			characterfile.newLine();
			characterfile.write("bestZulrahTime = ", 0, "bestZulrahTime = ".length());
			characterfile.write(Long.toString(p.bestZulrahTime), 0, Long.toString(p.bestZulrahTime).length());
			characterfile.newLine();
			characterfile.write("bossPasses = ", 0, "bossPasses = ".length());
			characterfile.write(Integer.toString(p.bossPasses), 0, Integer.toString(p.bossPasses).length());
			characterfile.newLine();
			characterfile.write("easyClue = ", 0, 11);
			characterfile.write(Integer.toString(p.easyClue), 0, Integer
					.toString(p.easyClue).length());
			characterfile.newLine();
			characterfile.write("medClue = ", 0, 10);
			characterfile.write(Integer.toString(p.medClue), 0, Integer
					.toString(p.medClue).length());
			characterfile.newLine();
			characterfile.write("hardClue = ", 0, 11);
			characterfile.write(Integer.toString(p.hardClue), 0, Integer
					.toString(p.hardClue).length());
			characterfile.newLine();
			characterfile.write("cwPoints = ", 0, 11);
			characterfile.write(Integer.toString(p.cwPoints), 0, Integer
					.toString(p.cwPoints).length());
			characterfile.newLine();
			characterfile.write("barrowsChests = ", 0, 16);
			characterfile.write(Integer.toString(p.barrowsChests), 0, Integer
					.toString(p.barrowsChests).length());
			characterfile.newLine();
			characterfile.write("darts = ", 0, 8);
			characterfile.write(Integer.toString(p.darts), 0,
					Integer.toString(p.darts).length());
			characterfile.newLine();
			characterfile.write("dartType = ", 0, 11);
			characterfile.write(Integer.toString(p.dartType), 0,
					Integer.toString(p.dartType).length());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0,
					Integer.toString(p.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer
					.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("hunterPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.hunterPoints), 0, Integer
					.toString(p.hunterPoints).length());
			characterfile.newLine();
			characterfile.write("xpLock = ", 0, 9);
			characterfile.write(Boolean.toString(p.xpLock), 0, Boolean
					.toString(p.xpLock).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer
					.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(
						p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0)
								: Integer.toString(p.barrowsNpcs[b][1]), 0,
						Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double
					.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer
					.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("donPoints = ", 0, 12);
			characterfile.write(Integer.toString(p.donPoints), 0, Integer
					.toString(p.donPoints).length());
			characterfile.newLine();
			characterfile.write("votPoints = ", 0, 12);
			characterfile.write(Integer.toString(p.votPoints), 0, Integer
					.toString(p.votPoints).length());
			characterfile.newLine();
			characterfile.write("pkPoints = ", 0, 11);
			characterfile.write(Integer.toString(p.pkPoints), 0, Integer
					.toString(p.pkPoints).length());
			characterfile.newLine();
			characterfile.write("KC = ", 0, 4);
			characterfile.write(Integer.toString(p.KC), 0,
					Integer.toString(p.KC).length());
			characterfile.newLine();
			characterfile.write("DC = ", 0, 4);
			characterfile.write(Integer.toString(p.DC), 0,
					Integer.toString(p.DC).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0,
					Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.pcPoints), 0, Integer
					.toString(p.pcPoints).length());
			characterfile.newLine();
			characterfile.write("killStreak = ", 0, 13);
			characterfile.write(Integer.toString(p.killStreak), 0, Integer
					.toString(p.killStreak).length());
			characterfile.newLine();
			characterfile.write("slayerTask = ", 0, 13);
			characterfile.write(Integer.toString(p.slayerTask), 0, Integer
					.toString(p.slayerTask).length());
			characterfile.newLine();
			characterfile.write("taskAmount = ", 0, 13);
			characterfile.write(Integer.toString(p.taskAmount), 0, Integer
					.toString(p.taskAmount).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.magePoints), 0, Integer
					.toString(p.magePoints).length());
			characterfile.newLine();
			characterfile.write("run-energy = ", 0, 13);
			characterfile.write(Integer.toString(p.runEnergy), 0, Integer
					.toString(p.runEnergy).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer
					.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean
					.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("replaced = ", 0, 11);
			characterfile.write(Boolean.toString(p.replaced), 0, Boolean
					.toString(p.replaced).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.waveId), 0, Integer
					.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer
					.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer
					.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t"
					+ p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t"
					+ p.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0,
						Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0,
						Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0,
						Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0,
						Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer
						.toString(p.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0,
							Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0,
							Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.bankItems.length; i++) {
				if (p.bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems[i]), 0,
							Integer.toString(p.bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItemsN[i]), 0,
							Integer.toString(p.bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			for (int i = 0; i < p.bankItems1.length; i++) {
				if (p.bankItems1[i] > 0) {
					characterfile.write("character-bank1 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems1[i]), 0,
							Integer.toString(p.bankItems1[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems1N[i]), 0,
							Integer.toString(p.bankItems1N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems2.length; i++) {
				if (p.bankItems2[i] > 0) {
					characterfile.write("character-bank2 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems2[i]), 0,
							Integer.toString(p.bankItems2[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems2N[i]), 0,
							Integer.toString(p.bankItems2N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems3.length; i++) {
				if (p.bankItems3[i] > 0) {
					characterfile.write("character-bank3 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems3[i]), 0,
							Integer.toString(p.bankItems3[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems3N[i]), 0,
							Integer.toString(p.bankItems3N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems4.length; i++) {
				if (p.bankItems4[i] > 0) {
					characterfile.write("character-bank4 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems4[i]), 0,
							Integer.toString(p.bankItems4[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems4N[i]), 0,
							Integer.toString(p.bankItems4N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems5.length; i++) {
				if (p.bankItems5[i] > 0) {
					characterfile.write("character-bank5 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems5[i]), 0,
							Integer.toString(p.bankItems5[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems5N[i]), 0,
							Integer.toString(p.bankItems5N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems6.length; i++) {
				if (p.bankItems6[i] > 0) {
					characterfile.write("character-bank6 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems6[i]), 0,
							Integer.toString(p.bankItems6[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems6N[i]), 0,
							Integer.toString(p.bankItems6N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems7.length; i++) {
				if (p.bankItems7[i] > 0) {
					characterfile.write("character-bank7 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems7[i]), 0,
							Integer.toString(p.bankItems7[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems7N[i]), 0,
							Integer.toString(p.bankItems7N[i]).length());
					characterfile.newLine();
				}
			}
			for (int i = 0; i < p.bankItems8.length; i++) {
				if (p.bankItems8[i] > 0) {
					characterfile.write("character-bank8 = ", 0, 18);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems8[i]), 0,
							Integer.toString(p.bankItems8[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bankItems8N[i]), 0,
							Integer.toString(p.bankItems8N[i]).length());
					characterfile.newLine();
				}
			}

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.friends.length; i++) {
				if (p.friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write("" + p.friends[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			p.getBag().saveLootingBag(p, characterfile);
			
			/* Achievements */
			characterfile.write("[ACHIEVEMENTS]", 0, 14);
			characterfile.newLine();
			characterfile.write("claimedEasy = ", 0, 14);
			characterfile.write(Boolean.toString(p.claimedEasy), 0,
					Boolean.toString(p.claimedEasy).length());
			characterfile.newLine();
			characterfile.write("claimedMedium = ", 0, 16);
			characterfile.write(Boolean.toString(p.claimedMedium), 0,
					Boolean.toString(p.claimedMedium).length());
			characterfile.newLine();
			characterfile.write("claimedHard = ", 0, 14);
			characterfile.write(Boolean.toString(p.claimedHard), 0,
					Boolean.toString(p.claimedHard).length());
			characterfile.newLine();
			characterfile.write("easyBosses = ", 0, 13);
			characterfile.write(Boolean.toString(p.easyBosses), 0,
					Boolean.toString(p.easyBosses).length());
			characterfile.newLine();
			characterfile.write("mediumBosses = ", 0, 15);
			characterfile.write(Boolean.toString(p.mediumBosses), 0,
					Boolean.toString(p.mediumBosses).length());
			characterfile.newLine();
			characterfile.write("hardBosses = ", 0, 13);
			characterfile.write(Boolean.toString(p.hardBosses), 0,
					Boolean.toString(p.hardBosses).length());
			characterfile.newLine();
			characterfile.write("easySharks = ", 0, 13);
			characterfile.write(Boolean.toString(p.easySharks), 0,
					Boolean.toString(p.easySharks).length());
			characterfile.newLine();
			characterfile.write("mediumSharks = ", 0, 15);
			characterfile.write(Boolean.toString(p.mediumSharks), 0,
					Boolean.toString(p.mediumSharks).length());
			characterfile.newLine();
			characterfile.write("hardSharks = ", 0, 13);
			characterfile.write(Boolean.toString(p.hardSharks), 0,
					Boolean.toString(p.hardSharks).length());
			characterfile.newLine();
			characterfile.write("sharksFished = ", 0, 15);
			characterfile.write(Integer.toString(p.sharksFished), 0,
					Integer.toString(p.sharksFished).length());
			characterfile.newLine();
			characterfile.write("easyGlories = ", 0, 14);
			characterfile.write(Boolean.toString(p.easyGlories), 0,
					Boolean.toString(p.easyGlories).length());
			characterfile.newLine();
			characterfile.write("mediumGlories = ", 0, 16);
			characterfile.write(Boolean.toString(p.mediumGlories), 0,
					Boolean.toString(p.mediumGlories).length());
			characterfile.newLine();
			characterfile.write("hardGlories = ", 0, 14);
			characterfile.write(Boolean.toString(p.hardGlories), 0,
					Boolean.toString(p.hardGlories).length());
			characterfile.newLine();
			characterfile.write("gloriesCrafted = ", 0, 17);
			characterfile.write(Integer.toString(p.gloriesCrafted), 0,
					Integer.toString(p.gloriesCrafted).length());
			characterfile.newLine();
			characterfile.write("easyMagics = ", 0, 13);
			characterfile.write(Boolean.toString(p.easyMagics), 0,
					Boolean.toString(p.easyMagics).length());
			characterfile.newLine();
			characterfile.write("mediumMagics = ", 0, 15);
			characterfile.write(Boolean.toString(p.mediumMagics), 0,
					Boolean.toString(p.mediumMagics).length());
			characterfile.newLine();
			characterfile.write("hardMagics = ", 0, 13);
			characterfile.write(Boolean.toString(p.hardMagics), 0,
					Boolean.toString(p.hardMagics).length());
			characterfile.newLine();
			characterfile.write("magicsCut = ", 0, 12);
			characterfile.write(Integer.toString(p.magicsCut), 0,
					Integer.toString(p.magicsCut).length());
			characterfile.newLine();
			characterfile.write("easyBurned = ", 0, 13);
			characterfile.write(Boolean.toString(p.easyBurned), 0,
					Boolean.toString(p.easyBurned).length());
			characterfile.newLine();
			characterfile.write("mediumBurned = ", 0, 15);
			characterfile.write(Boolean.toString(p.mediumBurned), 0,
					Boolean.toString(p.mediumBurned).length());
			characterfile.newLine();
			characterfile.write("hardBurned = ", 0, 13);
			characterfile.write(Boolean.toString(p.hardBurned), 0,
					Boolean.toString(p.hardBurned).length());
			characterfile.newLine();
			characterfile.write("magicsBurned = ", 0, 15);
			characterfile.write(Integer.toString(p.magicsBurned), 0,
					Integer.toString(p.magicsBurned).length());
			characterfile.newLine();
			characterfile.write("easyBows = ", 0, 11);
			characterfile.write(Boolean.toString(p.easyBows), 0,
					Boolean.toString(p.easyBows).length());
			characterfile.newLine();
			characterfile.write("mediumBows = ", 0, 13);
			characterfile.write(Boolean.toString(p.mediumBows), 0,
					Boolean.toString(p.mediumBows).length());
			characterfile.newLine();
			characterfile.write("hardBows = ", 0, 11);
			characterfile.write(Boolean.toString(p.hardBows), 0,
					Boolean.toString(p.hardBows).length());
			characterfile.newLine();
			characterfile.write("bowsMade = ", 0, 11);
			characterfile.write(Integer.toString(p.bowsMade), 0,
					Integer.toString(p.bowsMade).length());
			characterfile.newLine();
			characterfile.write("easyHerbs = ", 0, 12);
			characterfile.write(Boolean.toString(p.easyHerbs), 0,
					Boolean.toString(p.easyHerbs).length());
			characterfile.newLine();
			characterfile.write("mediumHerbs = ", 0, 14);
			characterfile.write(Boolean.toString(p.mediumHerbs), 0,
					Boolean.toString(p.mediumHerbs).length());
			characterfile.newLine();
			characterfile.write("hardHerbs = ", 0, 12);
			characterfile.write(Boolean.toString(p.hardHerbs), 0,
					Boolean.toString(p.hardHerbs).length());
			characterfile.newLine();
			characterfile.write("herbsFarmed = ", 0, 14);
			characterfile.write(Integer.toString(p.herbsFarmed), 0,
					Integer.toString(p.herbsFarmed).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("mediumTasks = ", 0, 14);
			characterfile.write(Boolean.toString(p.mediumTasks), 0,
					Boolean.toString(p.mediumTasks).length());
			characterfile.newLine();
			characterfile.write("hardTasks = ", 0, 12);
			characterfile.write(Boolean.toString(p.hardTasks), 0,
					Boolean.toString(p.hardTasks).length());
			characterfile.newLine();
			characterfile.write("slayerTasksCompleted = ", 0, 23);
			characterfile.write(Integer.toString(p.slayerTasksCompleted), 0,
					Integer.toString(p.slayerTasksCompleted).length());
			characterfile.newLine();
			characterfile.write("mediumDuo = ", 0, 12);
			characterfile.write(Boolean.toString(p.mediumDuo), 0,
					Boolean.toString(p.mediumDuo).length());
			characterfile.newLine();
			characterfile.write("hardDuo = ", 0, 10);
			characterfile.write(Boolean.toString(p.hardDuo), 0,
					Boolean.toString(p.hardDuo).length());
			characterfile.newLine();
			characterfile.write("duoSlayerTasksCompleted = ", 0, 26);
			characterfile.write(Integer.toString(p.duoSlayerTasksCompleted), 0,
					Integer.toString(p.duoSlayerTasksCompleted).length());
			characterfile.newLine();
			
			characterfile.newLine();

			/* IGNORES */
			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine();
			 * for (int i = 0; i < ignores.length; i++) { if (ignores[i] > 0) {
			 * characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0,
			 * Integer.toString(i).length()); characterfile.write("	", 0, 1);
			 * characterfile.write(Long.toString(ignores[i]), 0,
			 * Long.toString(ignores[i]).length()); characterfile.newLine(); } }
			 * characterfile.newLine();
			 */
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();

			float kdr = 0f;
			if (p.KC == 0) {
				kdr = 0f;
			}
			if (p.DC == 0) {
				kdr = p.KC;
			}
			if (p.KC != 0 && p.DC != 0) {
				kdr = (float) (((float) p.KC) / ((float) p.DC));
			}

			// System.err.println("Kills: " + p.KC + " || Deaths: " + p.DC +
			// " || KDR: " + kdr);
			
			//	if (p.playerRights != 2 && p.playerRights != 3)
			
					//	p.getLevelForXP(p.playerXP[0]), p.playerXP[0],
						//p.getLevelForXP(p.playerXP[1]), p.playerXP[1],
				//	p.getLevelForXP(p.playerXP[2]), p.playerXP[2],
				//	p.getLevelForXP(p.playerXP[3]), p.playerXP[3],
				//	p.getLevelForXP(p.playerXP[4]), p.playerXP[4],
				//p.getLevelForXP(p.playerXP[5]), p.playerXP[5],
						//	p.getLevelForXP(p.playerXP[6]), p.playerXP[6],
				//	p.getLevelForXP(p.playerXP[7]), p.playerXP[7],
				//	p.getLevelForXP(p.playerXP[8]), p.playerXP[8],
				//	p.getLevelForXP(p.playerXP[9]), p.playerXP[9],
				//	p.getLevelForXP(p.playerXP[10]), p.playerXP[10],
				//	p.getLevelForXP(p.playerXP[11]), p.playerXP[11],
				//	p.getLevelForXP(p.playerXP[12]), p.playerXP[12],
				//	p.getLevelForXP(p.playerXP[13]), p.playerXP[13],
				//	p.getLevelForXP(p.playerXP[14]), p.playerXP[14],
				//	p.getLevelForXP(p.playerXP[15]), p.playerXP[15],
				//	p.getLevelForXP(p.playerXP[16]), p.playerXP[16],
				//	p.getLevelForXP(p.playerXP[17]), p.playerXP[17],
				//	p.getLevelForXP(p.playerXP[18]), p.playerXP[18],
				//	p.getLevelForXP(p.playerXP[19]), p.playerXP[19],
				//	p.getLevelForXP(p.playerXP[20]), p.playerXP[20],
				////	totalLevel, SQLDatabase.getTotals(p.playerXP), p.KC,
				//	p.DC, kdr, p.playerRights, p.zulrahKills, p.armaKills, p.bandosKills, p.saraKills, p.zammyKills, p.callistoKills, p.vetionKills, p.venenatisKills,
				//	p.bestZulrahTime);

		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			return false;
			//	} catch (SQLException e) {

		}
		return true;
	}

	/**
	 * Tells use whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a characters friends in the form of a long array.
	 * 
	 * @param name
	 * @return
	 */
	public static long[] getFriends(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean end = false;
		int readMode = 0;
		BufferedReader file = null;
		boolean file1 = false;
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		try {
			file = new BufferedReader(new FileReader(Config.LOAD_DIRECTORY + "characters/"
					+ name + ".txt"));
			file1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (file1) {
			new File(Config.LOAD_DIRECTORY + "characters/" + name + ".txt");
		} else {
			return null;
		}
		try {
			line = file.readLine();
		} catch (IOException ioexception) {
			return null;
		}
		while (end == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (readMode) {
				case 0:
					if (token.equals("character-friend")) {
						readFriends[Integer.parseInt(token3[0])] = Long
								.parseLong(token3[1]);
						totalFriends++;
					}
					break;
				}
			} else {
				if (line.equals("[FRIENDS]")) {
					readMode = 0;
				} else if (line.equals("[EOF]")) {
					try {
						file.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = file.readLine();
			} catch (IOException ioexception1) {
				end = true;
			}
		}
		try {
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int index = 0; index < totalFriends; index++) {
					friends[index] = readFriends[index];
				}
				return friends;
			}
			file.close();
		} catch (IOException ioexception) {
		}
		return null;
	}

	/**
	 * Tells us whether or not the player exists for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean playerExists(String name) {
		File file = new File(Config.LOAD_DIRECTORY + "characters/" + name + ".txt");
		return file.exists();
	}

}