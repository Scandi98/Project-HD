package server.model.players;

import server.model.npcs.NPC;

public class Boundary {

	public static final Boundary KRACKEN = new Boundary(2314, 3692, 2339, 3715);
	public static final Boundary QUARANTINE = new Boundary(2441, 4760, 2481, 4795);
	public static final Boundary ZULRAH = new Boundary(2889, 3588, 2924, 3648);
	public static final Boundary BANDOS_GODWARS = new Boundary(2864, 5351, 2876, 5369);
	public static final Boundary ARMADYL_GODWARS = new Boundary(2824, 5296, 2842, 5308);
	public static final Boundary ZAMORAK_GODWARS = new Boundary(2918, 5318, 2936, 5331);
	public static final Boundary SARADOMIN_GODWARS = new Boundary(2889, 5258, 2907, 5276);
	
	public static final Boundary[] GODWARS_BOSSROOMS = {
		BANDOS_GODWARS,
		ARMADYL_GODWARS,
		ZAMORAK_GODWARS,
		SARADOMIN_GODWARS
	};
	
	public static final Boundary RESOURCE_AREA = new Boundary(3174, 3924, 3196, 3944);
	public static final Boundary KBD_AREA = new Boundary(2251, 4675, 2296, 4719);
	public static final Boundary PEST_CONTROL_AREA = new Boundary(2650, 2635, 2675, 2655);
	public static final Boundary FIGHT_CAVE = new Boundary(2365, 5052, 2429, 5122);
	public static final Boundary EDGEVILLE_PERIMETER = new Boundary(3073, 3465, 3108, 3518);
	
	public static final Boundary[] DUEL_ARENAS = new Boundary[] {
		new Boundary(3332, 3244, 3359, 3259),
		new Boundary(3364, 3244, 3389, 3259)
	};
	
	private int x, y, xOffset, yOffset;

	public Boundary(int x, int y, int xOffset, int yOffset) {
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public static boolean isInBounds(NPC npc, Boundary boundary) {
		return npc.getX() >= boundary.x && npc.getX() <= boundary.xOffset && npc.getY() >= boundary.y && npc.getY() <= boundary.yOffset;
	}

	public static boolean isInBounds(Player player, Boundary boundary) {
		return player.getX() >= boundary.x && player.getX() <= boundary.xOffset && player.getY() >= boundary.y && player.getY() <= boundary.yOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

}
