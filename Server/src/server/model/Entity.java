package server.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import server.core.GameEngine;
import server.core.World;
import server.core.task.Task;
import server.model.players.Attributes;
import server.tick.Tickable;
import server.util.Misc;

public abstract class Entity {

    private Map<Integer, Tickable> ticks = new HashMap<Integer, Tickable>();
	private Attributes attributes = new Attributes();
    
	public int absX, absY;
	public int heightLevel;
	protected Location location;
	private boolean attackable = true;
	public boolean appearanceUpdateRequired = true;
	public int hitDiff2;
	public int hitDiff = 0;
	public boolean hitUpdateRequired2;
	public boolean hitUpdateRequired = false;
	public boolean isDead = false;
	public boolean updateRequired;
	
	public abstract void dealDamage(int damage);
	
	public int getHitDiff2() {
		return hitDiff2;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}
	
	public void setLocation(Location loc) {
		this.absX = loc.getX();
		this.absY = loc.getY();
		this.heightLevel = loc.getZ();
	}
	
	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	
	public Entity getAttacking() { // TODO Handle it here
		return null;
	}
	
	public Location getLocation() {
		if (location == null)
			location = Location.create(absX, absY, heightLevel);
		return location;
	}
	
	public void submitTimer(int id, int ticks) {
		attributes.setAttribute(id, true);
		submitTick(new Tickable(ticks) {
			public void execute() {
				attributes.setAttribute(id, false);
				stop();
			}
		});
	}
	
	public boolean hasTime(int id) {
		boolean exists = attr(id);
		return exists;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T attr(int key) {
		return (T) attributes.getAttribute(key);
	}

	public void attr(int key, Object value) {
		attributes.setAttribute(key, value);
	}
	
	public void submitTick(Tickable tickable) {
		while (!submitTick(1000 + Misc.random(10000), tickable)); // TODO A better random generator
	}
	
	public boolean submitTick(int id, Tickable tickable) {
		if (ticks.get(id) != null)
			return false;
		World.getWorld().submit(new Task() {
			public void execute(GameEngine context) {
				ticks.put(id, tickable);
			}
		});
		return true;
	}
	
	public Tickable getTick(int identifier) {
		return ticks.get(identifier);
	}

	public boolean hasTick(int identifier) {
		return ticks.containsKey(identifier);
	}

	public void processTicks() {
		if (ticks.isEmpty())
			return;
		Iterator<Entry<Integer, Tickable>> it = ticks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Tickable> entry = it.next();
			entry.getValue().cycle();
			if (!entry.getValue().isRunning()) {
				it.remove();
			}
		}
	}
	
	public void process() {
		processTicks();
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int getZ() {
		return heightLevel;
	}

	public int getSize() {
		return 0;
	}

	public boolean isAttackable() {
		return attackable;
	}

	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}
	

	public void onDeath() {
	
	}

}
