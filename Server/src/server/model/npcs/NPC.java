package server.model.npcs;

import server.Server;
import server.core.PlayerHandler;
import server.model.Entity;
import server.model.players.Client;
import server.util.Location3D;
import server.util.Misc;
import server.util.Stream;

public class NPC extends Entity {

	public int npcSlot;
	public int npcId;
	public int currentNpcId;
	public int makeX, makeY, moveX, moveY, direction, walkingType;
	public int spawnX, spawnY;
	public int viewX, viewY;

	public int stage = 0;
	public int chance = 0;

	public int transformId;

	public void requestTransform(int Id) {
		transformId = Id;
		transformUpdateRequired = true;
		updateRequired = true;
	}
	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, currentHealth,
	maximumHealth, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public int attack, defence, maxHit;
	public boolean applyDead, isDead, needRespawn;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
	underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public boolean transformUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	private boolean facePlayer = true;
	public int defenceAnimation = -1;
	public boolean canFacePlayer() {
		return facePlayer;
	}
	
	/**
	 * Makes the npcs either able or unable to face other players
	 * @param facePlayer	{@code true} if the npc can face players
	 */
	public void setFacePlayer(boolean facePlayer) {
		this.facePlayer = facePlayer;
	}
	
	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}
	public NPC(int _npcId, int _npcType) {
		npcId = _npcId;
		currentNpcId = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}
	public static NPC createNPC(int slot, int npcId) {
		NPC npc = null;

		switch (npcId) {
		default:
			npc = new NPC(npcId);
			break;
		}
		npc.npcSlot = slot;
		return npc;
	}

	public NPC(int _npcType) {
		npcId = _npcType;
		currentNpcId = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
		
		
		this.defenceAnimation = getDefinition().getDefenceEmote();
		this.attack = getDefinition().getAttackStat();
		this.defence = getDefinition().getDefenceStat();
		this.maxHit = getDefinition().getMaxHit();
		this.npcSize = getDefinition().getSize();
	}

	@Override
	public int getSize() {
		return npcSize;
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * Graphics
	 **/

	public int graphicId = 0;
	public int mask80var2 = 0;
	protected boolean mask80update = false;

	public void appendMask80Update(Stream str) {
		str.writeWord(graphicId);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		graphicId = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		graphicId = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	/**
	 * 
	 Face
	 * 
	 **/

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnToLocation(int x, int y) {
		FocusPointX = 2 * x + 1;
		FocusPointY = 2 * y + 1;
		updateRequired = true;
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	
	public void facePlayer2(int id) {
		if (id == 65535 || id == -1) {
			face = -1;
		} else {
			face = id;
		}
		dirUpdateRequired = true;
		updateRequired = true;
	}
	public void facePlayer(int player) {
		if (player == 65535 || player == -1) {
			face = -1;
		} else {
			face = player + 32768;
		}
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void doTransform(int id) {
		currentNpcId = id;
		transformUpdateRequired  = true;
		updateRequired = true;
	}


	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(currentNpcId);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired)
			return;
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (mask80update)
			updateMask |= 0x80;
		if (dirUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (FocusPointX != -1)
			updateMask |= 4;
		if (transformUpdateRequired) 
			updateMask |= 2;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str);
		if (mask80update)
			appendMask80Update(str);
		if (dirUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatRequired)
			str.writeString(forcedText);
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if (transformUpdateRequired)
			appendTransformUpdate(str);
		if (FocusPointX != -1)
			appendSetFocusDestination(str);

	}

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double)i / (double)i1;
		return (int)Math.round(x*i2);
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}


	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	public Location3D targetedLocation;
	public void appendHitUpdate(Stream str) {
		if (currentHealth <= 0) {
			isDead = true;
		}
		//str.writeByteC(hitDiff);
		str.writeWord(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}
		str.writeWord(getCurrentHP(currentHealth, maximumHealth, maximumHealth));
		str.writeWord(maximumHealth);
		//str.writeByteS(getCurrentHP(HP, MaxHP, 100));
		//str.writeByteC(MaxHP);
	}

	public boolean hitUpdateRequired2 = false;
	public boolean multiAttack;
	public int kree, zilyana, graardor, tsutsaroth;
	public int lastX, lastY;
	public boolean summoner;
	public int summonedBy;
	/**
	 * True, if the NPC has been summoned by a player.
	 */
	public boolean summoned;
	public int npcSize = 1;
	public boolean respawns;

	public void appendHitUpdate2(Stream str) {
		if (currentHealth <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeByteA(currentHealth);
		str.writeByte(maximumHealth);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public void process() {
		super.process();
	}

	public void doCombat() {
		int p = killerId;
		if (PlayerHandler.players[p] != null) {
			Client c = (Client) PlayerHandler.players[p];
			facePlayer(c.playerId);
			Server.npcHandler.followPlayer(npcSlot, c.playerId);
			if (attackTimer == 0) {
				Server.npcHandler.attackPlayer(c, npcSlot);
			}
		} else {
			killerId = 0;
			underAttack = false;
			facePlayer(-1);
		}
	}

	public boolean inMulti() {
		//(absX >= 2530 && absX <= 2502 && absY >= 3059 && absY <= 3024)
		if ((absX >= 2255 && absX <= 2279 && absY >= 3066 && absY <= 3081)
				|| (absX >= 2975 && absX <= 3000 && absY >= 4365 && absY <= 4400)
				|| (absX >= 2502 && absX <= 2530 && absY >= 3024 && absY <= 3059)
				|| (absX >= 2323 && absX <= 2369 && absY >= 3686 && absY <= 3715)
				|| (absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public String Graardor() {
		int quote = Misc.random(9);
		switch (quote) {
		case 0:
			return "Death to our enemies!";
		case 1:
			return "Brargh!";
		case 2:
			return "Break their bones!";
		case 3:
			return "For the glory of the Big High War God!";
		case 4:
			return "Split their skulls!";
		case 5:
			return "We feast on the bones of our enemies tonight!";
		case 6:
			return "CHAAARGE!";
		case 7:
			return "Crush them underfoot!";
		case 8:
			return "All glory to Bandos!";
		case 9:
			return "GRAAAAAAAAAR!";
		}
		return "";
	}

	public String Tsutsaroth() {
		int quote = Misc.random(8);
		switch (quote) {
		case 0:
			return "Attack them, you dogs!";
		case 1:
			return "Forward!";
		case 2:
			return "Death to Saradomin's dogs!";
		case 3:
			return "Kill them you cowards!";
		case 4:
			return "The Dark One will have their souls!";
		case 5:
			return "Zamorak curse them!";
		case 6:
			return "Rend them limb from limb!";
		case 7:
			return "No retreat!";
		case 8:
			return "Flay them all!!";
		}
		return "";
	}

	public String Zilyana() {
		int quote = Misc.random(9);
		switch (quote) {
		case 0:
			return "Death to the enemies of the light!";
		case 1:
			return "Slay the evil ones!";
		case 2:
			return "Saradomin lend me strength!";
		case 3:
			return "By the power of Saradomin!";
		case 4:
			return "May Saradomin be my sword!";
		case 5:
			return "Good will always triumph!";
		case 6:
			return "Forward! Our allies are with us!";
		case 7:
			return "Saradomin is with us!";
		case 8:
			return "In the name of Saradomin!";
		case 9:
			return "Attack! Find the Godsword!";
		}
		return "";
	}

	public String Kree() {
		int quote = Misc.random(6);
		switch (quote) {
		case 0:
			return "Attack with your talons!";
		case 1:
			return "Face the wratch of Armadyl";
		case 2:
			return "SCCCRREEEEEEEEEECHHHHH";
		case 3:
			return "KA KAW KA KAW";
		case 4:
			return "Fight my minions!";
		case 5:
			return "Good will always triumph!";
		case 6:
			return "Attack! Find the Godsword!";
		}
		return "";
	}

	public NPCDefinition getDefinition() {
		return NPCHandler.definitions[npcId];
	}

	public void dealDamage(int damage) {
		if (damage > currentHealth) {
			damage = currentHealth;
		}
		currentHealth -= damage;
	}

	public Entity getAttacking() {
		return PlayerHandler.players[killerId];
	}

	public void doAnimation(int id) {
		animNumber = id;
		animUpdateRequired = true;
		updateRequired = true;
	}

}
