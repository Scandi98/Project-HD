package server.region;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import server.cache.RSObjectDefinition;
import server.clip.Buffer;
import server.clip.ByteStream;
import server.clip.FileOperations;
import server.clip.ObjectDef;
import server.model.npcs.NPC;
import server.model.objects.GameObject;
import server.model.players.Client;
import server.model.objects.Object;

public class Region {

	private static final Logger log = Logger.getLogger(Region.class.getName());

	public static ArrayList<Object> realObjects = new ArrayList<Object>();

	public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Region region : regions) {
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	public static void removeClipping(int x, int y, int height) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		// System.out.println("X: "+regionX+" - Y: "+regionY+" - id:
		// "+regionId);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.removeClip(x, y, height);
				break;
			}
		}
	}

	private void removeClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height > 4)
			height = height - (4 * (height / 4));
		if (height == 4)
			height = 0;
		if (clips[(int) height] == null) {
			clips[(int) height] = new int[64][64];
		}
		clips[(int) height][x - regionAbsX][y - regionAbsY] = 0;
	}

	/*
	 * Deleting more objects at the same time
	 * 
	 * @param toDelete, 2D array for what needs to be deleted
	 */
	public static void removeClipping(int[][] toDelete, int height) {
		int[] regionDetails = new int[3];
		for (int i = 0; i < toDelete.length; i++) {
			regionDetails[i] = (((toDelete[i][0] >> 3) / 8) << 8) + ((toDelete[i][1] >> 3) / 8);
		}
		int alreadyFound = 0;
		for (Region r : regions) {
			for (int i = 0; i < toDelete.length; i++) {
				if (alreadyFound == toDelete.length)
					return;
				if (r.id() == regionDetails[i]) {
					r.removeClip(toDelete[i][0], toDelete[i][1], height);
					alreadyFound++;
				}
			}
		}
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height == -1)
			return 0;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public boolean blockedNorth(int x, int y, int z) {
		return (getClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public boolean blockedEast(int x, int y, int z) {
		return (getClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public boolean blockedSouth(int x, int y, int z) {
		return (getClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public boolean blockedWest(int x, int y, int z) {
		return (getClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public boolean blockedNorthEast(int x, int y, int z) {
		return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public boolean blockedNorthWest(int x, int y, int z) {
		return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public boolean blockedSouthEast(int x, int y, int z) {
		return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public boolean blockedSouthWest(int x, int y, int z) {
		return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	private static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
		}
	}

	private static Region[] regions;
	private int id;
	private int[][][] clips = new int[4][][];
	private boolean members = false;

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	public static boolean isMembers(int x, int y, int height) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.members();
			}
		}
		return false;
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addObject(int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767()) {
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (def.aBoolean767()) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.solid());
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean767()) {
				addClippingForVariableObject(x, y, height, type, direction, def.solid());
			}
		}
	}

	public static int getClipping(int x, int y, int height) {
		try {
			if (height > 3)
				height = 0;
			int regionX = x >> 3;
			int regionY = y >> 3;
			int regionId = ((regionX / 8) << 8) + (regionY / 8);
			for (Region r : regions) {
				if (r.id() == regionId) {
					return r.getClip(x, y, height);
				}
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				log.info("[FATAL ERROR]: At getClipping: " + x + ", " + y + ", " + height + ", " + moveTypeX + ", "
						+ moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	private static int mapAmount = 0;

	public static void load() {
		try {
			Buffer buffer = new Buffer(FileOperations.readFile("./Data/cache/world/map_index.dat"));
			int size = buffer.readUnsignedWord();
			regions = new Region[size];
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			boolean[] isMembers = new boolean[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = buffer.readUnsignedWord();
				mapGroundFileIds[i] = buffer.readUnsignedWord();
				mapObjectsFileIds[i] = buffer.readUnsignedWord();
				// isMembers[i] = in.readUnsignedWord() == 0;
				mapAmount++;
			}
			log.info("Loaded " + mapAmount + " maps");
			for (int i = 0; i < size; i++) {
				regions[i] = new Region(regionIds[i], isMembers[i]);
			}
			for (int i = 0; i < size; i++) {
				byte[] file1 = getBuffer(new File("./Data/cache/world/map/" + mapObjectsFileIds[i] + ".gz"));
				byte[] file2 = getBuffer(new File("./Data/cache/world/map/" + mapGroundFileIds[i] + ".gz"));
				if (file1 == null || file2 == null) {
					continue;
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Error loading map region: " + regionIds[i]);
				}
			}
			log.info("Loading clipping - please wait.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					addObject(objectId, absX + localX, absY + localY, height, type, direction);
				}
			}
		}
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists())
			return null;
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1)
				break;
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10)
			return null;
		return buffer;
	}

	public static int[] getNextStep(int baseX, int baseY, int toX, int toY, int height, int xLength, int yLength) {
		int moveX = 0;
		int moveY = 0;
		if (baseX - toX > 0) {
			moveX--;
		} else if (baseX - toX < 0) {
			moveX++;
		}
		if (baseY - toY > 0) {
			moveY--;
		} else if (baseY - toY < 0) {
			moveY++;
		}
		if (canMove(baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY + moveY };
		} else if (moveX != 0 && canMove(baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY };
		} else if (moveY != 0 && canMove(baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX, baseY + moveY };
		}
		return new int[] { baseX, baseY };
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0
								|| (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0
								|| (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static boolean canAttack(NPC a, Client b) {

		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}

	public static void findRoute(Client c, int destX, int destY, boolean moveNear, int xLength, int yLength) {
		long start = System.currentTimeMillis();
		/*
		 * if (destX == c.pathFinalX && destY == c.pathFinalY) { return; }
		 */
		if (destX == c.getX() && destY == c.getY() && !moveNear
				|| !c.goodDistance(c.getX(), c.getY(), destX, destY, 20)) {
			return;
		}
		destX = destX - 8 * c.mapRegionX;
		destY = destY - 8 * c.mapRegionY;
		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = c.absX - c.mapRegionX * 8;
		int curY = c.absY - c.mapRegionY * 8;
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int head = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			int curAbsX = c.getMapRegionX() * 8 + curX;
			int curAbsY = c.getMapRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			/*
			 * if (xLength != 0 && yLength != 0 && method422(curAbsX, curAbsY,
			 * c.heightLevel % 4, absDestX, absDestY, xLength, yLength)) {
			 * foundPath = true; break; }
			 */
			tail = (tail + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0 && via[curX][curY - 1] == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0 && via[curX - 1][curY] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1 && via[curX][curY + 1] == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && via[curX + 1][curY] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY - 1, c.heightLevel % 4) & 0x128010e) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY + 1, c.heightLevel % 4) & 0x1280138) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY - 1, c.heightLevel % 4) & 0x1280183) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY + 1, c.heightLevel % 4) & 0x12801e0) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || (i_230_ == i_223_ && (cost[x][y] < thisCost))) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					return;
				}
			} else {
				// c.lastRouteX = destX;
				// c.lastRouteY = destY;
				c.followId = -1;
				c.followId2 = -1;
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != c.getLocalX() || curY != c.getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		int size = tail--;
		c.resetWalkingQueue();
		c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		for (int i = 1; i < size; i++) {
			tail--;
			c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		}
		/*
		 * if (c.wQueueWritePtr - 1 >= 0) { c.lastRouteX =
		 * c.walkingQueueX[c.wQueueWritePtr - 1]; c.lastRouteY =
		 * c.walkingQueueY[c.wQueueWritePtr - 1]; }
		 */
		long end = System.currentTimeMillis();
		// System.out.println((end - start));
	}

	public static boolean canAttack(Client a, NPC b) {
		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}

	public static boolean canAttack(Client a, Client b) {
		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}
}