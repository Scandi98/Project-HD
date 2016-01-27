package server.core.event.impl;

import server.core.PlayerHandler;
import server.core.event.Event;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.Player;


/**
 * Click Processing
 * @author Jinrake
 *
 */

public class ActionProcessor extends Event {

	public ActionProcessor() {
		super(600);
	}

	@Override
	public void execute() {
		if (PlayerHandler.playerCount <= 0) {
			return;
		}
		synchronized (PlayerHandler.players) {
			for(Player p : PlayerHandler.players) {
				if (p != null) {
					Client c = (Client) p;
					if(p.clickObjectType > 0 && p.goodDistance(p.objectX + p.objectXOffset, p.objectY + p.objectYOffset, p.getX(), p.getY(), p.objectDistance)) {
						if(p.clickObjectType == 1) {
							c.getActions().firstClickObject(p.objectId, p.objectX, p.objectY);
						}
						if(p.clickObjectType == 2) {
							c.getActions().secondClickObject(p.objectId, p.objectX, p.objectY);
						}
						if(p.clickObjectType == 3) {
							c.getActions().thirdClickObject(p.objectId, p.objectX, p.objectY);
						}
					}
					
					if((p.clickNpcType > 0) && NPCHandler.npcs[p.npcClickIndex] != null) {			
						if(p.goodDistance(p.getX(), p.getY(), NPCHandler.npcs[p.npcClickIndex].getX(), NPCHandler.npcs[p.npcClickIndex].getY(), 1)) {
							if(p.clickNpcType == 1) {
								p.turnPlayerTo(NPCHandler.npcs[p.npcClickIndex].getX(), NPCHandler.npcs[p.npcClickIndex].getY());
								NPCHandler.npcs[p.npcClickIndex].facePlayer(p.playerId);
								c.getActions().firstClickNpc(p.npcType);
							}
							if(p.clickNpcType == 2) {
								p.turnPlayerTo(NPCHandler.npcs[p.npcClickIndex].getX(), NPCHandler.npcs[p.npcClickIndex].getY());
								NPCHandler.npcs[p.npcClickIndex].facePlayer(p.playerId);
								c.getActions().secondClickNpc(p.npcType);
							}
							if(p.clickNpcType == 3) {
								p.turnPlayerTo(NPCHandler.npcs[p.npcClickIndex].getX(), NPCHandler.npcs[p.npcClickIndex].getY());
								NPCHandler.npcs[p.npcClickIndex].facePlayer(p.playerId);
								c.getActions().thirdClickNpc(p.npcType);
							}
						}
					}
				}
			}
		}
	}
}
