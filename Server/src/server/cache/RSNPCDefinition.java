package server.cache;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apollo.fs.Cache;
import org.apollo.fs.archive.Archive;
import org.apollo.util.ByteBufferUtil;

public class RSNPCDefinition {

	private static RSNPCDefinition[] npcs;
	
	public static RSNPCDefinition forId(int id) {
		if (id < 0 || id >= npcs.length)
			return null;
		return npcs[id];
	}
	
	public int id;
	
	public static int[] positions;
	public static ByteBuffer idxBuffer;
	public static ByteBuffer datBuffer;
	
	public static void fullParse() throws FileNotFoundException, IOException {
		Archive config = Archive.decode(Cache.fileSystem.getFile(0, 2));
		
		datBuffer = config.getEntry("npc.dat").getBuffer();
		idxBuffer = config.getEntry("npc.idx").getBuffer();
		
		positions = new int[idxBuffer.getShort() & 0xFFFF];
		npcs = new RSNPCDefinition[positions.length];
		BufferedWriter writer = new BufferedWriter(new FileWriter("./npcs.txt"));
		
		int off = 2;
		for (int i = 0; i < positions.length; i++) {
			positions[i] = off;
			off += idxBuffer.getShort() & 0xFFFF;
		}
		
		for (int i = 0; i < npcs.length; i++) {
			RSNPCDefinition obj = npcs[i] = valueOf(i);

			writer.write("name= "+obj.name+"_"+i+", actions= "+Arrays.toString(obj.actions)+", models="+(obj.anIntArray94 != null ? Arrays.toString(obj.anIntArray94) : "null"));
			writer.newLine();
		}

		for (int i = 0; i < npcs.length; i++) {
			npcs[i] = valueOf(i);
		}
		writer.close();
		Logger.getAnonymousLogger().info("Cached "+positions.length+" NPC Definitions.");
	}
	
	public void singleParse() {
		while(true) {
			int i = datBuffer.get() & 0xFF;
			if (i == 0)
				return;
			if (i == 1) {
				int j = datBuffer.get() & 0xFF;
				anIntArray94 = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					anIntArray94[j1] = datBuffer.getShort() & 0xFFFF;

			} else if (i == 2) {
				name = ByteBufferUtil.readString(datBuffer);
			}
			else if (i == 3) {
				description = ByteBufferUtil.readString(datBuffer);
			}
			else if (i == 12)
				npcSize = datBuffer.get();
			else if (i == 13)
				standAnim = datBuffer.getShort() & 0xFFFF;
			else if (i == 14)
				walkAnim = datBuffer.getShort() & 0xFFFF;
			else if (i == 17) {
				walkAnim = datBuffer.getShort() & 0xFFFF;
				rotateAnim180 = datBuffer.getShort() & 0xFFFF;
				rotateAnim90CW = datBuffer.getShort() & 0xFFFF;
				rotateAnim90CCW = datBuffer.getShort() & 0xFFFF;
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = ByteBufferUtil.readString(datBuffer);
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = datBuffer.get() & 0xFF;
				anIntArray76 = new int[k];
				anIntArray70 = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					anIntArray76[k1] = datBuffer.getShort() & 0xFFFF;
					anIntArray70[k1] = datBuffer.getShort() & 0xFFFF;
				}

			} else if (i == 60) {
				int l = datBuffer.get() & 0xFF;
				anIntArray73 = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					anIntArray73[l1] = datBuffer.getShort() & 0xFFFF;

			} else if (i == 90)
				datBuffer.getShort();
			else if (i == 91)
				datBuffer.getShort();
			else if (i == 92)
				datBuffer.getShort();
			else if (i == 93)
				aBoolean87 = false;
			else if (i == 95)
				combatLevel = datBuffer.getShort() & 0xFFFF;
			else if (i == 97)
				anInt91 = datBuffer.getShort() & 0xFFFF;
			else if (i == 98)
				anInt86 = datBuffer.getShort() & 0xFFFF;
			else if (i == 99)
				aBoolean93 = true;
			else if (i == 100)
				anInt85 = datBuffer.get();
			else if (i == 101)
				anInt92 = datBuffer.get() * 5;
			else if (i == 102)
				anInt75 = datBuffer.getShort() & 0xFFFF;
			else if (i == 103)
				anInt79 = datBuffer.getShort() & 0xFFFF;
			else if (i == 106) {
				switch_config = datBuffer.getShort() & 0xFFFF;
				//System.out.println(switch_config+", "+id);
				if (switch_config == 65535)
					switch_config = -1;
				anInt59 = datBuffer.getShort() & 0xFFFF;
				if (anInt59 == 65535)
					anInt59 = -1;
				int i1 = datBuffer.get() & 0xFF;
				children = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					children[i2] = datBuffer.getShort() & 0xFFFF;
					if (children[i2] == 65535)
						children[i2] = -1;
				}

			} else if (i == 107)
				aBoolean84 = false;
		}
	}
	
	private static RSNPCDefinition valueOf(int id) {
		RSNPCDefinition npc = new RSNPCDefinition();
		datBuffer.position(positions[id]);
		
		npc.id = id;
		npc.singleParse();
		
		return npc;
	}
	
	public RSNPCDefinition() {
		rotateAnim90CCW = -1;
		switch_config = -1;
		rotateAnim180 = -1;
		anInt59 = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		npcSize = 1;
		anInt75 = -1;
		standAnim = -1;
		interfaceType = -1L;
		anInt79 = 32;
		rotateAnim90CW = -1;
		aBoolean84 = true;
		anInt86 = 128;
		aBoolean87 = true;
		anInt91 = 128;
		aBoolean93 = false;
		name = "";
	}

	public int rotateAnim90CCW;
	public static int anInt56;
	public int switch_config;
	public int rotateAnim180;
	public int anInt59;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte npcSize;
	public int[] anIntArray70;
	public static int[] streamIndices;
	public int[] anIntArray73;
	public int anInt75;
	public int[] anIntArray76;
	public int standAnim;
	public long interfaceType;
	public int anInt79;
	public int rotateAnim90CW;
	public boolean aBoolean84;
	public int anInt85;
	public int anInt86;
	public boolean aBoolean87;
	public int children[];
	public String description;
	public int anInt91;
	public int anInt92;
	public boolean aBoolean93;
	public int[] anIntArray94;
	
}
