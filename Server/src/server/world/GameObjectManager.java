package server.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.Server;
import server.core.PlayerHandler;
import server.model.objects.GameObject;
import server.model.players.Client;
import server.model.players.Player;
import server.util.Misc;

/**
 * @author Sanity
 */

public class GameObjectManager {

	public List<GameObject> globalObjects = new ArrayList<GameObject>();

	public GameObjectManager() {
		loadGlobalObjects("Data/cfg/global-objects.cfg");
		loadDoorConfig("Data/cfg/doors.cfg");
	}

	/**
	 * Adds object to list
	 **/
	public void addObject(GameObject object) {
		globalObjects.add(object);
	}

	/**
	 * Removes object from list
	 **/
	public void removeObject(GameObject object) {
		globalObjects.remove(object);
	}
	
	public void removeObjectAtIndex(int index) {
		globalObjects.remove(index);
	}

	/**
	 * Does object exist
	 **/
	public GameObject objectExists(int objectX, int objectY, int objectHeight) {
		for (GameObject o : globalObjects) {
			if (o.getX() == objectX && o.getY() == objectY && o.getZ() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Update objects when entering a new region or logging in
	 **/
	public void updateObjects(Client c) {
		for (GameObject o : globalObjects) {
			if (c != null) {
				if (c.heightLevel == o.getZ() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getX(), o.getY()) <= 60) {
						if (c.isObjectInArea(o)) {
							if (o.decayTicks == 0) {
								c.getPA().removeObject(o);
							}
						} else {
							c.getPA().object(o.getId(), o.getX(), o.getY(), o.getOrientation(), o.getType());
							c.addObjectToArea(o);
						}
					}
				}
			}
		}
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch();
		}
		if (c.distanceToPoint(2961, 3389) <= 60) {
			c.getPA().object(6552, 2961, 3389, -1, 10);
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void placeObject(GameObject o) {
		removeAllObjects(o);
		globalObjects.add(o);
	}

	public void removeAllObjects(GameObject o) {
		for (GameObject s : globalObjects) { 
			if (s.getX() == o.getX() && s.getY() == o.getY() && s.getZ() == o.getZ()) {
				globalObjects.remove(s);
				break;
			}
		}
	}

	public void process() {
		Iterator<GameObject> objects = globalObjects.iterator();
		while (objects.hasNext()) {
			GameObject o = objects.next();
			if (o != null) {
				if (o.decayTicks == 0) {
					objects.remove();
					continue;
				}
				if (o.objectTicks > 0) {
					o.objectTicks--;
				}
				if (o.decayTicks > 0) {
					o.decayTicks--;
				}
				if (o.objectTicks == 1) {
					GameObject deleteObject = objectExists(o.getX(), o.getY(), o.getZ());
					removeObject(deleteObject);
					o.objectTicks = 0;
					placeObject(o);
					removeObject(o);
					if (isObelisk(o.objectId)) {
						int index = getObeliskIndex(o.objectId);
						if (activated[index]) {
							activated[index] = false;
							teleportObelisk(index);
						}
					}
				}
			}
		}
	}

	public boolean loadGlobalObjects(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
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
				if (token.equals("object")) {
					GameObject object = new GameObject(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							Integer.parseInt(token3[5]), 0);
					addObject(object);
				}
			} else {
				if (line.equals("[ENDOFOBJECTLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 * Doors
	 **/

	public static final int MAX_DOORS = 30;
	public static int[][] doors = new int[MAX_DOORS][5];
	public static int doorFace = 0;

	public void doorHandling(int doorId, int doorX, int doorY, int doorHeight) {
		for (int i = 0; i < doors.length; i++) {
			if (doorX == doors[i][0] && doorY == doors[i][1]
					&& doorHeight == doors[i][2]) {
				if (doors[i][4] == 0) {
					doorId++;
				} else {
					doorId--;
				}
				for (Player p : PlayerHandler.players) {
					if (p != null) {
						Client person = (Client) p;
						if (person != null) {
							if (person.heightLevel == doorHeight) {
								if (person.distanceToPoint(doorX, doorY) <= 60) {
									person.getPA().object(-1, doors[i][0],
											doors[i][1], 0, 0);
									if (doors[i][3] == 0 && doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] + 1,
												-1, 0);
									} else if (doors[i][3] == -1
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0] - 1, doors[i][1],
												-2, 0);
									} else if (doors[i][3] == -2
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] - 1,
												-3, 0);
									} else if (doors[i][3] == -3
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0] + 1, doors[i][1],
												0, 0);
									} else if (doors[i][3] == 0
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0] - 1, doors[i][1],
												-3, 0);
									} else if (doors[i][3] == -1
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] - 1,
												0, 0);
									} else if (doors[i][3] == -2
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0] + 1, doors[i][1],
												-1, 0);
									} else if (doors[i][3] == -3
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] + 1,
												-2, 0);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean loadDoorConfig(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
		}
		int door = 0;
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
				if (token.equals("door")) {
					doors[door][0] = Integer.parseInt(token3[0]);
					doors[door][1] = Integer.parseInt(token3[1]);
					doors[door][2] = Integer.parseInt(token3[2]);
					doors[door][3] = Integer.parseInt(token3[3]);
					doors[door][4] = Integer.parseInt(token3[4]);
					door++;
				}
			} else {
				if (line.equals("[ENDOFDOORLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ioexception) {
					}
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 111235, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 },
			{ 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 }, { 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				GameObject obby1 = new GameObject(14825, obeliskCoords[index][0],
						obeliskCoords[index][1], 0, -1, 10, 0);
				GameObject obby2 = new GameObject(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1], 0, -1, 10, 0);
				GameObject obby3 = new GameObject(14825, obeliskCoords[index][0],
						obeliskCoords[index][1] + 4, 0, -1, 10, 0);
				GameObject obby4 = new GameObject(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, 0);
				addObject(obby1);
				addObject(obby2);
				addObject(obby3);
				addObject(obby4);
				Server.gameObjectManager.placeObject(obby1);
				Server.gameObjectManager.placeObject(obby2);
				Server.gameObjectManager.placeObject(obby3);
				Server.gameObjectManager.placeObject(obby4);
				GameObject obby5 = new GameObject(obeliskIds[index],
						obeliskCoords[index][0], obeliskCoords[index][1], 0,
						-1, 10, 10);
				GameObject obby6 = new GameObject(obeliskIds[index],
						obeliskCoords[index][0] + 4, obeliskCoords[index][1],
						0, -1, 10, 10);
				GameObject obby7 = new GameObject(obeliskIds[index],
						obeliskCoords[index][0], obeliskCoords[index][1] + 4,
						0, -1, 10, 10);
				GameObject obby8 = new GameObject(obeliskIds[index],
						obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, 10);
				addObject(obby5);
				addObject(obby6);
				addObject(obby7);
				addObject(obby8);
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
				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + 2,
							obeliskCoords[random][1] + 2, 0);
				}
			}
		}
	}

	public GameObject createAnObject(Client c, int id, int x, int y) {
		GameObject gameObj = new GameObject(id, x, y, 0, 0, 10, 0);
		if (id == -1) {
			removeObject(gameObj);
		} else {
			addObject(gameObj);
		}
		Server.gameObjectManager.placeObject(gameObj);
		return gameObj;
	}

	public void createAnObject(Client c, int id, int x, int y, int face) {
		GameObject OBJECT = new GameObject(id, x, y, 0, face, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.gameObjectManager.placeObject(OBJECT);
	}

	public void createAnObject(Client c, int id, int x, int y, int face,
			int newId, int Tick) {
		GameObject OBJECT = new GameObject(id, x, y, 0, face, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.gameObjectManager.placeObject(OBJECT);
	}

	/**
	 * The instance of this class
	 */
	private static GameObjectManager instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static GameObjectManager getInstance() {
		if (GameObjectManager.instance == null) {
			GameObjectManager.instance = new GameObjectManager();
		}
		return GameObjectManager.instance;
	}

	public GameObject getObject(int x, int y, int height) {
		for (GameObject o : globalObjects) {
			if (o.getX() == x && o.getY() == y
					&& o.getZ() == height) {
				return o;
			}
		}
		return null;
	}
}
