package server.world;

import java.util.ArrayList;

import server.core.PlayerHandler;
import server.model.Location;
import server.model.objects.GameObject;
import server.model.objects.Object;
import server.region.Region;
import server.util.Misc;
import server.model.players.Client;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();

	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);
		}
		toRemove.clear();
	}

	public void removeObject(int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public void updateObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public void placeObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
							o.type);
			}
		}
	}

	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
						o.type);
		}
		loadCustomSpawns(c);
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch();
		}
	}
	
	
	public static void addObjectsToRegion() {
	}

	public void loadCustomSpawns(Client client) {
		addObjectsToRegion();
		//client.getPA().checkObjectSpawn(2633, 2407, 3802, 0, 10); // FFA Portal
		client.getPA().checkObjectSpawn(3515, 2418, 3801, 0, 10); // comp
		client.getPA().checkObjectSpawn(-1, 2416, 3800, 0, 10); //bank chest
		client.getPA().checkObjectSpawn(-1, 2417, 3800, 0, 10); //bank chest
		client.getPA().checkObjectSpawn(26645, 2415, 3822, 0, 10); //bank chest
		client.getPA().checkObjectSpawn(75, 2405, 3803, 0, 10); //crystal chest
		client.getPA().checkObjectSpawn(409, 2412, 3797, 0, 10); //bank chest
		client.getPA().checkObjectSpawn(2632, 3202, 3855, 0, 10); // chest
		/* New Donator Zone */
		client.getPA().checkObjectSpawn(-1, 3201, 3855, 0, 10);
		client.getPA().checkObjectSpawn(-1, 3201, 3856, 0, 10);
		client.getPA().checkObjectSpawn(-1, 3336, 3895, 0, 10);
		client.getPA().checkObjectSpawn(-1, 3336, 3896, 0, 10);
		client.getPA().checkObjectSpawn(-1, 2895, 3511, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2895, 3510, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2898, 3509, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2898, 3512, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2898, 3507, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2898, 3514, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2895, 3513, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2894, 3517, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2895, 3517, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2896, 3517, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2886, 3506, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2883, 3508, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2883, 3512, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2886, 3514, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2886, 3510, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2883, 3511, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2885, 3515, 2, 10);
		client.getPA().checkObjectSpawn(-1, 2891, 3511, 2, 1);
		client.getPA().checkObjectSpawn(-1, 2891, 3510, 2, 1);
		
		client.getPA().checkObjectSpawn(21301, 2894, 3517, 4, 10); // Bank
		client.getPA().checkObjectSpawn(21301, 2895, 3517, 4, 10); // Bank
		client.getPA().checkObjectSpawn(21301, 2896, 3517, 4, 10); // Bank
		client.getPA().checkObjectSpawn(9682, 2892, 3513, 4, 10); // Range
		client.getPA().checkObjectSpawn(11730, 2897, 3507, 2, 10);
		client.getPA().checkObjectSpawn(411, 2892, 3508, 1, 10);
		client.getPA().checkObjectSpawn(11764, 2885, 3507, 4, 10);
		client.getPA().checkObjectSpawn(11764, 2885, 3514, 4, 10);
		for (int i = 0; i < 5; i++) {
			client.getPA().checkObjectSpawn(7418, 2883, 3509 + i, 4, 10);
		}
		client.getPA().checkObjectSpawn(13720, 2888, 3506, 4, 10);
		client.getPA().checkObjectSpawn(13720, 2887, 3506, 4, 10);
		client.getPA().checkObjectSpawn(13720, 2888, 3515, 4, 10);
		client.getPA().checkObjectSpawn(13720, 2887, 3515, 4, 10);
		client.getPA().checkObjectSpawn(195, 2336, 9805, 4, 10);
		//9682
		/*client.getPA().checkObjectSpawn(-1, 3359, 3341, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3360, 3341, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3359, 3338, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3365, 3341, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3365, 3342, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3366, 3342, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3366, 3344, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3365, 3344, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3357, 3341, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3362, 3343, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3362, 3344, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3361, 3344, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3360, 3345, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3363, 3345, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3360, 3340, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3360, 3339, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3359, 3339, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3358, 3340, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3358, 3339, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3357, 3338, 2, 10);//
		client.getPA().checkObjectSpawn(-1, 3366, 3341, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3366, 3345, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3365, 3345, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3363, 3346, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3361, 3346, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3359, 3346, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3359, 3348, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3360, 3348, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3363, 3348, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3364, 3348, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3354, 3334, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3353, 3334, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3353, 3335, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3353, 3336, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3350, 3336, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3350, 3335, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3351, 3334, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3350, 3334, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3348, 3333, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3349, 3332, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3356, 3333, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3356, 3332, 2, 10);
		client.getPA().checkObjectSpawn(-1, 3366, 3347, 2, 10);*/
		client.getPA().checkObjectSpawn(11730, 2330, 3810, 0, 10);
		client.getPA().checkObjectSpawn(10082, 3039, 9789, 0, 10);
		client.getPA().checkObjectSpawn(2031, 3041, 9793, 0, 10);
		
		client.getPA().checkObjectSpawn(2213, 3363, 3348, 0, 10);
		client.getPA().checkObjectSpawn(2213, 3362, 3348, 0, 10);
		client.getPA().checkObjectSpawn(2213, 3364, 3348, 0, 10);
		client.getPA().checkObjectSpawn(2213, 3361, 3348, 0, 10);
		client.getPA().checkObjectSpawn(2213, 3360, 3348, 0, 10);
		client.getPA().checkObjectSpawn(61, 3367, 3346, 3, 10);
		client.getPA().checkObjectSpawn(2561, 3357, 3341, 3, 10);
		client.getPA().checkObjectSpawn(3193, 3052, 3509, 0, 10);
		client.getPA().checkObjectSpawn(3193, 3051, 3509, 0, 10);
		client.getPA().checkObjectSpawn(9682, 3367, 3344, 0, 10);
		/* Godwars Clipping */
		for (int i = 0; i < 9; i++) {
			client.getPA().checkObjectSpawn(3367, 2864 + i, 5351, 2, 10);
		}
		for (int i = 0; i < 18; i++) {
			client.getPA().checkObjectSpawn(3367, 2889 + i, 5276, 2, 10);
		}

		client.getPA().checkObjectSpawn(3367, 2874, 5351, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2875, 5351, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2876, 5351, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2874, 5351, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2873, 5351, 2, 10);
		// fight caves
		client.getPA().checkObjectSpawn(3367, 2414, 5089, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2413, 5090, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2413, 5091, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2413, 5092, 2, 10);
		client.getPA().checkObjectSpawn(3367, 2413, 5093, 2, 10);
		client.getPA().checkObjectSpawn(3044, 3044, 9790, 2, 10);
		client.getPA().checkObjectSpawn(2213, 3049, 9786, 2, 10);
		client.getPA().checkObjectSpawn(2213, 2541, 3886, 2, 10);
		client.getPA().checkObjectSpawn(2213, 2542, 3886, 2, 10);
		client.getPA().checkObjectSpawn(2213, 2543, 3886, 2, 10);
		client.getPA().checkObjectSpawn(2213, 2544, 3886, 2, 10);
		client.getPA().checkObjectSpawn(2213, 2545, 3886, 2, 10);
		client.getPA().checkObjectSpawn(1309, 2976, 3216, 1, 10);// yew tree
		// home
		client.getPA().checkObjectSpawn(1309, 2977, 3212, 1, 10);// yew tree
		// home
		client.getPA().checkObjectSpawn(1306, 2966, 3219, 1, 10);// magic tree
		// home
		client.getPA().checkObjectSpawn(1307, 2972, 3212, 1, 10);// maple tree
		// home
		client.getPA().checkObjectSpawn(1307, 2972, 3210, 1, 10);// maple tree
		// home
		client.getPA().checkObjectSpawn(1307, 2972, 3214, 1, 10);// maple tree
		// home
		client.getPA().checkObjectSpawn(1306, 2963, 3225, 1, 10);// magic tree
		// home
		client.getPA().checkObjectSpawn(2213, 2977, 3241, 0, 10);// bank mining
		client.getPA().checkObjectSpawn(2213, 2976, 3241, 0, 10);// bank mining
		client.getPA().checkObjectSpawn(2213, 2975, 3241, 0, 10);// bank mining
		client.getPA().checkObjectSpawn(2213, 2974, 3241, 0, 10);// bank mining
		client.getPA().checkObjectSpawn(3044, 2971, 3241, 1, 10);// home
		// smelting
		client.getPA().checkObjectSpawn(7492, 3055, 9775, 0, 10); // Gold
		client.getPA().checkObjectSpawn(7492, 3057, 9773, 0, 10); // Gold
		client.getPA().checkObjectSpawn(7492, 3053, 9772, 0, 10); // Gold
		client.getPA().checkObjectSpawn(7492, 3052, 9769, 0, 10); // Gold
		
		client.getPA().checkObjectSpawn(13720, 3046, 9808, 0, 10); // Addy
		client.getPA().checkObjectSpawn(13720, 3049, 9812, 0, 10); // Addy
		client.getPA().checkObjectSpawn(13720, 3038, 9811, 0, 10); // Addy
		client.getPA().checkObjectSpawn(13720, 3041, 9811, 0, 10); // Addy
		
		client.getPA().checkObjectSpawn(13719, 3046, 9803, 0, 10); // Mith
		client.getPA().checkObjectSpawn(13719, 3046, 9800, 0, 10); // Mith
		client.getPA().checkObjectSpawn(13719, 3038, 9793, 0, 10); // Mith
		client.getPA().checkObjectSpawn(13719, 3046, 9792, 0, 10); // Mith
		
		client.getPA().checkObjectSpawn(7418, 3048, 9781, 0, 10); // Rune
		client.getPA().checkObjectSpawn(7418, 3047, 9779, 0, 10); // Rune
		client.getPA().checkObjectSpawn(7418, 3045, 9782, 0, 10); // Rune
		
		client.getPA().checkObjectSpawn(11764, 2725, 3504, 0, 10); // Magic tree

		client.getPA().checkObjectSpawn(21301, 3049, 9792, 1, 10); // Bank
		
		client.getPA().checkObjectSpawn(-1, 2339, 3801, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2345, 3807, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2347, 3801, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2352, 3801, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2328, 3804, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2328, 3805, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2336, 3805, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2337, 3798, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2338, 3798, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2337, 3798, 0, 4);
		client.getPA().checkObjectSpawn(-1, 2338, 3798, 0, 4);
		client.getPA().checkObjectSpawn(-1, 2336, 3809, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2331, 3808, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2332, 3808, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2337, 3809, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2337, 3804, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2332, 3809, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2338, 3807, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2335, 3807, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2332, 3810, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2326, 3802, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2407, 3807, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2407, 3806, 0, 1);
		client.getPA().checkObjectSpawn(-1, 2329, 3801, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3802, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3803, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3800, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3799, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2330, 3798, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2330, 3799, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2330, 3800, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2330, 3799, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2331, 3798, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2331, 3797, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2331, 3796, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2331, 3799, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3807, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2329, 3806, 0, 9);
		client.getPA().checkObjectSpawn(-1, 2330, 3802, 0, 10);
		client.getPA().checkObjectSpawn(-1, 2329, 3804, 0, 3);
		client.getPA().checkObjectSpawn(-1, 2329, 3805, 0, 3);

		if (client.heightLevel == 0) {
			client.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		} else {
			client.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
		}
		client.getPA().checkObjectSpawn(75, 2329, 3806, 5, 10); // Crystal Key Chest
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 },
			{ 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 }, { 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}

	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						1)) {
					c.getPA().startTeleport2(
							obeliskCoords[random][0] + xOffset,
							obeliskCoords[random][1] + yOffset, 0);
				}
			}
		}
	}

	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60
				&& c.heightLevel == o.height;
	}

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

	public boolean objectExists(final int x, final int y) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y) {
				return true;
			}
		}
		return false;
	}

}