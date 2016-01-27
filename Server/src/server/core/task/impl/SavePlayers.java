package server.core.task.impl;

import server.core.GameEngine;
import server.core.PlayerHandler;
import server.core.World;
import server.core.task.Task;
import server.model.players.Client;

/**
 * 
 * @author Jinrake
 * PvP Planet
 *
 */

public class SavePlayers implements Task {

	@Override
	public void execute(GameEngine context) {
		try {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c = (Client)PlayerHandler.players[j];
					server.model.players.PlayerSave.saveGame(c);			
				}
			}
		} catch (Exception e) {
			World.getWorld().handleError(e);
		}		
	}	

}
