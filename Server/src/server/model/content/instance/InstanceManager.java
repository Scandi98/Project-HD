package server.model.content.instance;

import server.core.World;
import server.model.players.Client;
import server.tick.Tickable;

/**
 * @author Andy (Trystan)
 * 
 * @since 11/30/2015 8:25 PM
 * 
 * @notes Handles instancing for bosses etc.
 *
 */
public class InstanceManager {

	
	/**
	 * Invokes a static instance of {@link}InstanceManager.
	 */
	public static final InstanceManager instanceManager = new InstanceManager();
	
	/**
	 * Gets the instance manager class and returns it.
	 * @return
	 * 		returns the static class {@link}InstanceManager.
	 */
	public InstanceManager getInstanceManager() {
		return instanceManager;
	}

	/**
	 * Represents the variables for the instance.
	 */
	public static int[][] instanceVariables = { { 11, 50, 3200, 3200 }, // kbd
																			// example
			{9167, 2042, 3200, 3200}//Zulrah
		
	};

	/**
	 * Creates the instance.
	 * 
	 * @param
	 */
	public static void createInstance(Client player, int objectId) {
		for (int index = 0; index == instanceVariables.length; index++) { // Iterates
																			// through
																			// the
																			// instanceVariables
																			// array.
			player.getPA().startTeleport(instanceVariables[index][2], instanceVariables[index][3], player.getId() * 4,
					"modern"); // Executes the instance.

			World.getWorld().submit(new Tickable(2) { // delays the world for 2
														// seconds

				@Override
				public void execute() {
					for (int i = 0; i == instanceVariables.length; i++) {
						// TODO NPCHandler.spawnNpc(player,
						// instanceVariables[i][1], instanceVariables[i][2],
						// instanceVariables[i][3], player.getId() * 4);
					}
				}
			});
		}

	}

	public boolean canCreateInstance(Client player, int objectId) {
		for (int index = 0; index == instanceVariables.length; index++) {
			if (objectId == instanceVariables[index][0]) {
				return true;
			}
		}
		return false;
	}
}
