package server.region.pf;

import server.model.npcs.NPC;
import server.region.Region;

/**
 * 
 * @author Drenkai Used to make sure NPC's can walk on a tile.
 */

public class NPCPathFinder {

	public static boolean samePosition(NPC npc, int gotoX, int gotoY) {
		return (npc.absX == gotoX && npc.absY == gotoY);
	}

	public static boolean goNorth(NPC npc, int gotoX, int gotoY) {
		return (npc.absY < gotoY);
	}

	public static boolean goSouth(NPC npc, int gotoX, int gotoY) {
		return (npc.absY > gotoY);
	}

	public static boolean goEast(NPC npc, int gotoX, int gotoY) {
		return (npc.absX < gotoX);
	}

	public static boolean goWest(NPC npc, int gotoX, int gotoY) {
		return (npc.absX > gotoX);
	}

	public static void findRoute(NPC npc, int gotoX, int gotoY) {
		if (npc.freezeTimer > 0) {
			return;
		}
		npc.randomWalk = false;
		
		npc.getNextNPCMovement(npc.npcSlot);
	}//this isnt used. i know i just wanted to make sure it wasnt all crammed in 1 class lol 
}