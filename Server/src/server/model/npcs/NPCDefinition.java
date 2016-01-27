package server.model.npcs;

import server.cache.RSNPCDefinition;

public class NPCDefinition {

	private int id;
	private String name = "";
	private int health;
	private int attackStat;
	private int defenceStat;
	private int maxHit;
	private int attackEmote;
	private int defenceEmote;
	private int deathEmote;
	private int respawnTime;

	public boolean updated;
	public transient int translationId;
	public transient boolean defined;

	public NPCDefinition(int _npcId) {
		this.id = _npcId;
		translationId = _npcId;
		this.name = getName();
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setAttackStat(int attack) {
		this.attackStat = attack;
	}

	public void setDefenceStat(int defence) {
		this.defenceStat = defence;
	}

	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

	public void setDefence(int defence) {
		this.defenceStat = defence;
	}

	public int getAttackEmote() {
		return attackEmote;
	}

	public void setAttackEmote(int attackEmote) {
		this.attackEmote = attackEmote;
	}

	public int getDefenceEmote() {
		return defenceEmote;
	}

	public void setDefenceEmote(int defenceEmote) {
		this.defenceEmote = defenceEmote;
	}

	public int getDeathEmote() {
		return deathEmote;
	}

	public void setDeathEmote(int deathEmote) {
		this.deathEmote = deathEmote;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}

	public int getId() {
		return id;
	}

	public boolean hasCacheDefinition() {
		return getRSDefinition() != null;
	}

	public String getName() {
		RSNPCDefinition cacheDefinition = getRSDefinition();
		if (cacheDefinition != null)
			return cacheDefinition.name;
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getSize() {
		RSNPCDefinition cacheDefinition = getRSDefinition();
		if (cacheDefinition == null)
			return 1;
		return cacheDefinition.npcSize;
	}

	public int getCombatLevel() {
		RSNPCDefinition cacheDefinition = getRSDefinition();
		if (cacheDefinition == null)
			return 1;
		return cacheDefinition.combatLevel;
	}
	
	public RSNPCDefinition getRSDefinition() {
		return RSNPCDefinition.forId(id);
	}

	public int getAttackStat() {
		return attackStat;
	}

	public int getDefenceStat() {
		return defenceStat;
	}

	public int getMaxHit() {
		return maxHit;
	}
}
