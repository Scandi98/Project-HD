package server.core;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import server.Config;
import server.Server;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerSave;
import server.util.Misc;
import server.util.Stream;

public class PlayerHandler {

	private static PlayerHandler playerHandler = new PlayerHandler();

	public static PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public static Player players[] = new Player[Config.MAX_PLAYERS];

	public static String messageToAll = "";
	public static int playerCount = 0;
	public static String playersCurrentlyOn[] = new String[Config.MAX_PLAYERS];
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;

	static {
		for(int i = 0; i < Config.MAX_PLAYERS; i++)
			players[i] = null;
	}
	
	public static Player getPlayer(String name) {
		return Arrays.stream(players).filter(p -> p != null && p.playerName.equalsIgnoreCase(name)).findFirst().get();
	}

	public static List<Player> getPlayers() {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		return Arrays.asList(clients).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	public static int onlineCount(){
		return (int) Arrays.stream(players).filter(p -> p != null).count();
	}

	public boolean newPlayerClient(Client client1) {
		int slot = -1;
		for(int i = 1; i < Config.MAX_PLAYERS; i++) {
			if((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if(slot == -1)
			return false;
		client1.handler = this;
		client1.playerId = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		players[slot].connectedFrom = ((InetSocketAddress) client1.getSession().getRemoteAddress()).getAddress().getHostAddress();
		if(Config.SERVER_DEBUG)	
			Misc.println("Player Slot "+slot+" slot 0 "+players[0]+" Player Hit "+players[slot]);
		return true;
	}

	public void destruct() {
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(players[i] == null) 
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		return playerCount;
	}

	public void updatePlayerNames() {
		playerCount = 0;
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(players[i] != null) {
				playersCurrentlyOn[i] = players[i].playerName;
				playerCount++;
			} else {
				playersCurrentlyOn[i] = "";
			}
		}
	}

	public static boolean isPlayerOn(String playerName) {
		return Arrays.stream(playersCurrentlyOn).anyMatch(p -> p.equalsIgnoreCase(playerName));
	}

	public void process() {
		updatePlayerNames();
		if (kickAllPlayers) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] != null) {
					players[i].disconnected = true;
				}
			}
		}
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || !players[i].initialized)
				continue;
			try {
				Client player = (Client) players[i];
				if (player.disconnected
						&& (System.currentTimeMillis() - player.logoutDelay > 10000 || player.properLogout || kickAllPlayers)) {
					if (((Client) players[i]).getTradeHandler().getStage() > 0) {
						((Client) players[i]).getTradeHandler().cancelCurrentTrade();
					}
					if (((Client) players[i]).duelStatus > 0) {
						Client o1 = (Client) PlayerHandler.players[((Client) players[i]).duelingWith];
						if (o1 == null) {
							continue;
						}
						if (((Client) players[i]).duelStatus == 5) {
							o1.getDuel().duelVictory();
							continue;
						}
						((Client) players[i]).getDuel().declineDuel();
						o1.getDuel().declineDuel();
						continue;
					}
					removePlayer(player);
					players[i] = null;
					continue;
				}
				player.preProcessing();
				player.processQueuedPackets();
				player.process();
				player.postProcessing();
				player.getNextPlayerMovement();
				
				Server.gameObjectManager.updateObjects(player);
				player.doFollow(true);
			} catch (Exception e) {
				e.printStackTrace();
				World.getWorld().handleError(e);
			}
		}
		Arrays.stream(players).sequential().filter(p -> p != null && p.isActive && p.initialized).forEach(p -> p.update());
		Arrays.stream(players).sequential().filter(p -> p != null && p.isActive && p.initialized).forEach(p -> p.clearUpdateFlags());
		
		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			Server.UpdateServer = true;
		}
		if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}
	}


	public void updateNPC(Player plr, Stream str) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;

		for(int i = 0; i < size; i++) {
			if(!plr.rebuildNPCList && plr.withinDistance(plr.npcList[i]) && Server.npcHandler.npcs[plr.npcList[i].npcSlot] != null) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].npcSlot;
				plr.npcInListBitmap[id>>3] &= ~(1 << (id&7));		
				str.writeBits(1, 1);
				str.writeBits(2, 3);		
			}
		}

		int j = 0;

		for(int i = 0; i < NPCHandler.maxNPCs; i++) {
			if(NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].npcSlot;
				if (j > 5) {
					break;
				}
				if (plr.rebuildNPCList == false && (plr.npcInListBitmap[id>>3]&(1 << (id&7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
					j++;
				}
			}
		}

		plr.rebuildNPCList = false;

		if(updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);	
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		//}
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		//synchronized(plr) {
		updateBlock.currentOffset = 0;
		if(updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds*50/30);
		}
		plr.updateThisPlayerMovement(str);		
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		plr.playerListSize = 0;	
		for(int i = 0; i < size; i++) {			
			if(!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id>>3] &= ~(1 << (id&7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}
		int j = 0;
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(plr.playerListSize >= 254) break;
			if(updateBlock.currentOffset+str.currentOffset >= 4900)
				break;
			if(players[i] == null || !players[i].isActive || players[i] == plr) continue;
			int id = players[i].playerId;
			if((plr.playerInListBitmap[id>>3]&(1 << (id&7))) != 0) continue;
			if(j > 5) break;	
			if(!plr.withinDistance(players[i])) continue;		
			plr.addNewPlayer(players[i], str, updateBlock);
			j++;
		}

		if(updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);	
			str.finishBitAccess();


			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		}
		else str.finishBitAccess();

		str.endFrameVarSizeWord();
	}
	public static void executeGlobalMessage(String message) {
		Client[] clients = new Client[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		Arrays.asList(clients).stream().filter(Objects::nonNull).forEach(player -> player.sendMessage(message));
	}
	
	private void removePlayer(Player plr) {
		if(plr.privateChat != 2) { 
			for(int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] == null || players[i].isActive == false) continue;
				Client o = (Client)PlayerHandler.players[i];
				if(o != null) {
					o.getPA().updatePM(plr.playerId, 0);
				}
			}
		}
		plr.destruct();
	}

}
