package server.model.players.packets;

import server.Config;
import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.PacketType;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 128, MAGE_PLAYER = 249;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.playerIndex = 0;
		c.npcIndex = 0;
		switch (packetType) {

		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			c.playerIndex = c.getInStream().readSignedWord();
			if(c.isDead)
				return;
			if (PlayerHandler.players[c.playerIndex] == null) {
				break;
			}
			if (c.wearId == 7927 == true) {
				c.sendMessage("unmorgh before you can start walking again!");
				return;
			}
			if (c.wearId == 6583 == true) {
				c.sendMessage("unmorgh before you can start walking again!");
				return;
			}
			if (c.respawnTimer > 0) {
				break;
			}
			
			if (c.inDuelArena() && c.duelStatus != 5) {
				if (c.arenas()) {
					c.sendMessage("You can't challenge inside the arena.");
					return;
				}
				c.getDuel().requestDuel(c.playerIndex);
				c.playerIndex = 0;
				return;
			}

			if (c.duelStatus == 5 && c.duelCount > 0) {
				c.sendMessage("The duel hasn't started yet!");
				c.playerIndex = 0;
				return;
			}

			if (c.autocastId > 0)
				c.autocasting = true;

			if (!c.autocasting && c.spellId > 0) {
				c.spellId = 0;
			}
			c.mageFollow = false;
			c.spellId = 0;
			c.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185
					|| c.playerEquipment[c.playerWeapon] == 11785;
			for (int bowId : Client.BOWS) {
				if (c.playerEquipment[c.playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : Client.ARROWS) {
						if (c.playerEquipment[c.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : Client.OTHER_RANGE_WEAPONS) {
				if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}

			if ((usingBow || c.autocasting)
					&& c.goodDistance(c.getX(), c.getY(),
							PlayerHandler.players[c.playerIndex].getX(),
							PlayerHandler.players[c.playerIndex].getY(), 6)) {
				c.usingBow = true;
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(),
							PlayerHandler.players[c.playerIndex].getX(),
							PlayerHandler.players[c.playerIndex].getY(), 3)) {
				c.usingRangeWeapon = true;
				c.stopMovement();
			}
			if (!usingBow)
				c.usingBow = false;
			if (!usingOtherRangeWeapons)
				c.usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow
					&& c.playerEquipment[c.playerWeapon] < 4212
					&& c.playerEquipment[c.playerWeapon] > 4223) {
				c.sendMessage("You have run out of arrows!");
				return;
			}
			if (!c.getCombat().usingCorrectBowAndArrows(c)
					&& Config.CORRECT_ARROWS && usingBow
					&& !c.getCombat().usingCrystalBow()
					&& c.playerEquipment[c.playerWeapon] != 9185
					&& c.playerEquipment[c.playerWeapon] != 11785) {
				c.sendMessage("You can't use "
						+ c.getItems()
								.getItemName(c.playerEquipment[c.playerArrows])
								.toLowerCase()
						+ "s with a "
						+ c.getItems()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase() + ".");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if ((c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785)
					&& !c.getCombat().properBolts()) {
				c.sendMessage("You must use bolts with a crossbow.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.getCombat().checkReqs()) {
				c.followId = c.playerIndex;
				if (!c.usingMagic && !usingBow && !usingOtherRangeWeapons) {
					c.followDistance = 1;
					c.getPA().followPlayer();
				}
				if (c.attackTimer <= 0) {
					// c.sendMessage("Tried to attack...");
					// c.getCombat().attackPlayer(c.playerIndex);
					// c.attackTimer++;
				}
			}
			break;

		/**
		 * Attack player with magic
		 **/
		case MAGE_PLAYER:
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.playerIndex = c.getInStream().readSignedWordA();
			int castingSpellId = c.getInStream().readSignedWordBigEndian();
			c.usingMagic = false;
			if (PlayerHandler.players[c.playerIndex] == null) {
				break;
			}
			if (c.wearId == 7927 == true) {
				c.sendMessage("Unmorph before you can start walking again!");
				return;
			}
			if (c.wearId == 6583 == true) {
				c.sendMessage("Unmorph before you can start walking again!");
				return;
			}
			if (c.respawnTimer > 0) {
				break;
			}

			for (int i = 0; i < Client.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == Client.MAGIC_SPELLS[i][0]) {
					c.spellId = i;
					c.usingMagic = true;
					break;
				}
			}
			/*if (c.spellId == 24 || c.spellId == 23
					&& c.inClanWarsGame) {
				c.sendMessage("You cannot use these spells in this Minigame");
				c.usingMagic = false;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}*/
			if (c.autocasting)
				c.autocasting = false;

			if (!c.getCombat().checkReqs()) {
				break;
			}
			if (c.duelStatus == 5) {
				if (c.duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.playerIndex = 0;
					return;
				}
				if (c.duelRule[4]) {
					c.sendMessage("Magic has been disabled in this duel!");
					return;
				}
			}

			for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
																// spells,
																// confuse etc
				if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == Client.MAGIC_SPELLS[c.spellId][0]) {
					if ((System.currentTimeMillis() - PlayerHandler.players[c.playerIndex].reduceSpellDelay[r]) < PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[r]) {
						c.sendMessage("That player is currently immune to this spell.");
						c.usingMagic = false;
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis()
					- PlayerHandler.players[c.playerIndex].teleBlockDelay < PlayerHandler.players[c.playerIndex].teleBlockLength
					&& Client.MAGIC_SPELLS[c.spellId][0] == 12445) {
				c.sendMessage("That player is already affected by this spell.");
				c.usingMagic = false;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
			}

			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * c.getCombat().resetPlayerAttack(); break; }
			 */

			if (c.usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 7)) {
					c.stopMovement();
				}
				if (c.getCombat().checkReqs()) {
					c.followId = c.playerIndex;
					c.mageFollow = true;
					if (c.attackTimer <= 0) {
						// c.getCombat().attackPlayer(c.playerIndex);
						// c.attackTimer++;
					}
				}
			}
			break;

		}

	}

}
