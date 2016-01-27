package server.cache;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apollo.fs.Cache;
import org.apollo.fs.archive.Archive;
import org.apollo.util.ByteBufferUtil;

public class RSItemDefinition {

	public static RSItemDefinition[] items;

	public int id;

	public static int[] positions;
	public static ByteBuffer idxBuffer;
	public static ByteBuffer datBuffer;

	private static BufferedReader reader;

	public static HashMap<Integer, Double> prices = new HashMap<>();

	public static void parse() throws FileNotFoundException, IOException {
		Archive config = Archive.decode(Cache.fileSystem.getFile(0, 2));


		if (reader == null) {
			reader = new BufferedReader(new FileReader("./Data/cfg/pricedata.txt"));//i haz idea. kk
			try {
				String line;

				while ((line = reader.readLine()) != null) {
					String[] args = line.split(":");

					int item = Integer.parseInt(args[0]);
					double value = Double.parseDouble(args[1]);

					prices.put(item, value);
				}
			} finally {
				reader.close();
			}
		}

		datBuffer = config.getEntry("obj.dat").getBuffer();
		idxBuffer = config.getEntry("obj.idx").getBuffer();

		positions = new int[idxBuffer.getShort() & 0xFFFF];
		items = new RSItemDefinition[positions.length];

		int off = 2;
		for (int i = 0; i < positions.length; i++) {
			positions[i] = off;
			off += idxBuffer.getShort() & 0xFFFF;
		}
		/*BufferedWriter writer = new BufferedWriter(new FileWriter("./actions.txt"));

		for (int i = 0; i < items.length; i++) {
			RSItemDefinition obj = items[i] = valueOf(i);
			for (int action = 0; action < obj.actions.length; action++) {
				if (obj.actions[action] != null) {
					if (obj.actions[action].equals("Uncharge")) {
						writer.write(obj.id + ", ");
					}
				}
			}
		}*/

		for (int i = 0; i < items.length; i++) {
			RSItemDefinition item = items[i] = valueOf(i);

			if (prices.containsKey((int) i)) {
				item.price = prices.get(i);
			}
		}

		//writer.close();
		Logger.getAnonymousLogger().info("Cached "+positions.length+" Item Definitions.");
	}

	public void singleParse() {
		while(true) {
			int i = datBuffer.get() & 0xFF;
			if (i == 0)
				return;
			if (i == 1)
				modelID = datBuffer.getShort() & 0xFFFF;
			else if (i == 2)
				name = ByteBufferUtil.readString(datBuffer);
			else if (i == 3)
				description = ByteBufferUtil.readString(datBuffer);
			else if (i == 4)
				zoom2d = datBuffer.getShort() & 0xFFFF;
			else if (i == 5)
				xan2d = datBuffer.getShort() & 0xFFFF;
			else if (i == 6)
				yan2d = datBuffer.getShort() & 0xFFFF;
			else if (i == 7) {
				xof2d = datBuffer.getShort() & 0xFFFF;
				if (xof2d > 32767)
					xof2d -= 0x10000;
			} else if (i == 8) {
				yof2d = datBuffer.getShort() & 0xFFFF;
				if (yof2d > 32767)
					yof2d -= 0x10000;
			} else if (i == 10)
				datBuffer.getShort();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				cost = datBuffer.getInt();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				manwear = datBuffer.getShort() & 0xFFFF;
				aByte205 = datBuffer.get();
			} else if (i == 24)
				manwear2 = datBuffer.getShort() & 0xFFFF;
			else if (i == 25) {
				femalewear = datBuffer.getShort() & 0xFFFF;
				aByte154 = datBuffer.get();
			} else if (i == 26)
				femalewear2 = datBuffer.getShort() & 0xFFFF;
			else if (i >= 30 && i < 35) {
				if (iop == null)
					iop = new String[5];
				iop[i - 30] = ByteBufferUtil.readString(datBuffer);
				if (iop[i - 30].equalsIgnoreCase("hidden"))
					iop[i - 30] = null;
			} else if (i >= 35 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 35] = ByteBufferUtil.readString(datBuffer);
			} else if (i == 40) {
				int j = datBuffer.get() & 0xFF;
				recol_s = new int[j];
				recol_d = new int[j];
				for (int k = 0; k < j; k++) {
					recol_s[k] = datBuffer.getShort() & 0xFFFF;
					recol_d[k] = datBuffer.getShort() & 0xFFFF;
				}

			} else if (i == 78)
				malesymbol = datBuffer.getShort() & 0xFFFF;
			else if (i == 79)
				femalesymbol = datBuffer.getShort() & 0xFFFF;
			else if (i == 90)
				manhead = datBuffer.getShort() & 0xFFFF;
			else if (i == 91)
				womanhead = datBuffer.getShort() & 0xFFFF;
			else if (i == 92)
				manhead2 = datBuffer.getShort() & 0xFFFF;
			else if (i == 93)
				womanhead2 = datBuffer.getShort() & 0xFFFF;
			else if (i == 95)
				zan2d = datBuffer.getShort() & 0xFFFF;
			else if (i == 97)
				certlink = datBuffer.getShort() & 0xFFFF;
			else if (i == 98)
				certtemplate = datBuffer.getShort() & 0xFFFF;
			else if (i == 100) {
				int length = datBuffer.get() & 0xFF;
				stackIDs = new int [length];
				stackAmounts = new int[length];
				for (int i2 = 0; i2< length; i2++) {
					stackIDs[i2] = datBuffer.getShort() & 0xFFFF;
					stackAmounts[i2] = datBuffer.getShort() & 0xFFFF;
				}
			} else if (i == 110)
				resizex = datBuffer.getShort() & 0xFFFF;
			else if (i == 111)
				resizey = datBuffer.getShort() & 0xFFFF;
			else if (i == 112)
				resizez = datBuffer.getShort() & 0xFFFF;
			else if (i == 113)
				ambient = datBuffer.get();
			else if (i == 114)
				contrast = datBuffer.get() * 5;
			else if (i == 115)
				team = datBuffer.get() & 0xFF;
		}
	}

	public static RSItemDefinition valueOf(int id) {

		
		if (id < 0 || id >= items.length) {
			return null;
		}
		
		if (items[id] != null) {
			return items[id];
		}

		RSItemDefinition item = items[id] = new RSItemDefinition();

		datBuffer.position(positions[id]);

		item.id = id;
		item.singleParse();

		return item;
	}

	public RSItemDefinition() {
		id = -1;
	}

	private byte aByte154;
	public int cost;// anInt155
	public int[] recol_d;// newModelColor
	public int[] recol_s;
	public boolean membersObject;// aBoolean161
	private int femalesymbol;
	public int certtemplate;
	public int femalewear2;// femArmModel
	public int manwear;// maleWieldModel
	private int manhead2;
	private int resizex;
	public String iop[];
	public int xof2d;
	public String name;// itemName
	private int womanhead2;
	public int modelID;// dropModel
	public int manhead;
	public boolean stackable;// itemStackable
	public String description;// itemExamine
	public int certlink;
	public int zoom2d;
	public static boolean isMembers = true;
	private int contrast;
	private int malesymbol;
	public int manwear2;// maleArmModel
	public String actions[];// itemMenuOption
	public int xan2d;// modelRotateUp
	private int resizez;
	private int resizey;
	public int[] stackIDs;// modelStack
	public int yof2d;//
	private static int[] streamIndices;
	private int ambient;
	public int womanhead;
	public int yan2d;// modelRotateRight
	public int femalewear;// femWieldModel
	public int[] stackAmounts;// itemAmount
	public int team;
	public static int totalItems;
	public int zan2d;// modelPositionUp
	private byte aByte205;

	public double price;

	public String getName() {
		return name;
	}

}