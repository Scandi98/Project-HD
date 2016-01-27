package server.model;

import server.model.npcs.NPC;
import server.model.players.Player;

/**
 * 
 * @author Nine
 *
 */
public class Projectile {
	
	private int id;
	private Entity from;
	private Entity to;
	private Location start;
	private Location end;
	private int angle;
	private int speed;
	private int startHeight;
	private int endHeight;
	private int slope;
	
	private boolean global;
	
	public static Projectile create(int id, Entity from, Entity to, int angle, int speed, int startHeight, int endHeight, int slope) {
		return new Projectile(id, from, to, angle, speed, startHeight, endHeight, slope, false);
	}
	
	public static Projectile create(int id, Entity from, Location to, int angle, int speed, int startHeight, int endHeight, int slope) {
		return new Projectile(id, from, to, angle, speed, startHeight, endHeight, slope, false);
	}
	
	
	public static Projectile createGlobal(int id, Entity from, Location to, int angle, int speed, int startHeight, int endHeight, int slope) {
		return new Projectile(id, from, to, angle, speed, startHeight, endHeight, slope, true);
	}
	
	public static Projectile createGlobal(int id, Location from, Location to, int angle, int speed, int startHeight, int endHeight, int slope) {
		return new Projectile(id, from, to, angle, speed, startHeight, endHeight, slope, true);
	}
	
	public static Projectile createGlobal(int id, Entity from, Entity to, int angle, int speed, int startHeight, int endHeight, int slope) {
		return new Projectile(id, from, to, angle, speed, startHeight, endHeight, slope, true);
	}
	
	private Projectile(int id, Entity from, Entity to, int angle, int speed, int startHeight, int endHeight, int slope, boolean global) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.angle = angle;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.slope = slope;
		this.global = global;
		this.start = Location.create(from.getX(), from.getY(), from.getZ()).transform(from.getSize() / 2, from.getSize() / 2, 0);
		this.end = Location.create(to.getX(), to.getY(), to.getZ()).transform(to.getSize() / 2, to.getY() / 2, 0);
	}
	
	private Projectile(int id, Location from, Location end, int angle, int speed, int startHeight, int endHeight, int slope, boolean global) {
		this.id = id;
		this.from = null;// From the ground.
		this.end = end;
		this.angle = angle;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.slope = slope;
		this.global = global;
		this.start = from;
	}
	
	private Projectile(int id, Entity from, Location end, int angle, int speed, int startHeight, int endHeight, int slope, boolean global) {
		this.id = id;
		this.from = from;
		this.end = end;
		this.angle = angle;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.slope = slope;
		this.global = global;
		this.start = Location.create(from.getX(), from.getY(), from.getZ()).transform(from.getSize() / 2, from.getSize() / 2, 0);
	}
	
	public int getId() {
		return id;
	}

	public Entity getFrom() {
		return from;
	}
	
	public Location getStart() {
		return start;
	}

	public int getAngle() {
		return angle;
	}

	public int getSpeed() {
		return speed;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getSlope() {
		return slope;
	}

	public boolean isGlobal() {
		return global;
	}

	public int getOffsetX() {
		return (start.getX() - (end.getX())) * -1;
	}
	
	public int getOffsetY() {
		return ((start.getY() ) - (end.getY())) * -1;
	}
	
	public int getLockon() {
		if (to instanceof Player) {
			return -((Player) to).playerId - 1;
		} else if (to instanceof NPC) {
			return ((NPC) to).npcSlot + 1;
		}
		return 0;
	}

}
