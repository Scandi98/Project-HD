package server.model.players.packets;

import server.core.PlayerHandler;
import server.model.minigames.bh.BountyHunter;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.Player;
import server.model.players.skills.SkillHandler;
import server.model.players.skills.agility.GnomeCourse;
import server.model.players.skills.cooking.Cooking;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.walkingToItem = false;
		c.clickNpcType = 0;
		c.clickObjectType = 0;
		c.myShopId = 0;
		c.resetAllActions();
		c.followId = 0;
		c.isSkilling[c.playerFletching] = false;
		c.isSkilling[c.playerCrafting] = false;
		c.isSkilling[c.playerHerblore] = false;
		c.playerSkilling[10] = false;
		Cooking.resetCooking(c);
		c.getPA().resetFollow();
		if (c.lootingBagOpen) {
			c.restoreTabs();
		}
		
		if (!c.canWalk) {
			return;
		}
		if (c.getTradeHandler().getCurrentTrade() != null) {
			if (c.getTradeHandler().getCurrentTrade().isOpen()) {
				c.getTradeHandler().decline();
			}
		}
		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (Player.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1)
						&& packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
				return;
			}
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (c.jumpingDitch)
			return;
		if (c.doingAgility) {
			GnomeCourse.resetAgilityWalk(c);
		}
		if (c.playerEquipment[c.playerRing] == 7927/* c.wearId == 7927 == true */) {
			c.sendMessage("Unmorph before you can start walking again!");
			return;
		}
		if (c.playerEquipment[c.playerRing] == 6583) {
			c.sendMessage("Unmorph before you can start walking again!");
			return;
		}
		c.interfaceOpen = false;
		c.isShopping = false;
		c.isBanking = false;
		SkillHandler.isSkilling[12] = false;
		if (c.walkingToObject)
			c.walkingToObject = false;

		if (c.duelStatus > 0 && c.duelStatus < 4) {
			Client o = ((Client) PlayerHandler.players[c.duelingWith]);
			if (o == null) {
				c.getDuel().declineDuel();
				return;
			}
			c.getDuel().declineDuel();
			o.getDuel().declineDuel();
			return;
		}
		/*
		 * if(!c.isBanking) { c.bankCheck = false; }
		 */
		c.bankCheck = false;
		if (c.usingAltar)
			c.usingAltar = false;
		if (!c.isBanking) {
			c.getPA().removeAllWindows();
		}
		if(c.isBanking) {
			c.sendMessage("Please use the ´Close window´ Option.");
			return;
		}
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null) {
				if (!Player.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.duelingWith].getX(),
						PlayerHandler.players[c.duelingWith].getY(), 1)
						|| c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
			c.stopPlayerSkill = false;
		}
		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0)
				c.getPA().resetFollow();
		}
		if (c.stunTimer > 0 || System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You're stunned!");
			c.playerIndex = 0;
			return;
		}

		if (packetType == 98) {
			c.walkingToObject = true;
		}

		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null
					&& (!Player.goodDistance(c.getX(), c.getY(),
							PlayerHandler.players[c.duelingWith].getX(),
							PlayerHandler.players[c.duelingWith].getY(), 1) || c.attackTimer == 0)) {
				c.sendMessage("Walking has been disabled in this duel.");
			}
			c.playerIndex = 0;
			return;
		}

		if ((c.duelStatus >= 1 && c.duelStatus <= 4) || c.duelStatus == 6) {
			if (c.duelStatus == 6) {
				c.getDuel().claimStakedItems();
			}
			return;
		}
		
//		GameObject obj = Server.gameObjectManager.objectExists(c.absX, c.absY, c.heightLevel);
//		
//		if (obj != null) {
//			switch(obj.objectId) {
//			case 11700:
//				c.poisonMask = 3;
//				c.handleHitMask(5);
//				c.dealDamage(5);
//				break;
//			}
//		}
		
		if (c.inTrade) {
			/*
			 * Client other = (Client)
			 * PlayerHandler.players[c.tradeWith]; if (other == null) {
			 * c.sendMessage("Internal Error"); return; } c.tradeResetNeeded =
			 * true; other.getTradeAndDuel().declineTrade();
			 * other.getTradeAndDuel().declineDuel();
			 * c.getTradeAndDuel().declineTrade();
			 * c.getTradeAndDuel().declineDuel();
			 */
			return;
		}
		if (c.inWild() && !c.EP_ACTIVE) 
			BountyHunter.handleEP(c);
		/*
		 * if (c.inWild()) BountyHunter.handleBH(c);
		 */
		if (c.respawnTimer > 3) {
			return;
		}
		if (packetType == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}

		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}

		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;

		int firstStepX = c.getInStream().readSignedWordBigEndianA()
				- c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		int firstStepY = c.getInStream().readSignedWordBigEndian()
				- c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.finalDestX = c.getNewWalkCmdX()[i1] += firstStepX;
			c.finalDestY = c.getNewWalkCmdY()[i1] += firstStepY;
			// c.otherDirection = Misc.direction1(c.absX, c.absY,
			// c.getNewWalkCmdX()[i1] + firstStepX + c.getMapRegionX()*8,
			// c.getNewWalkCmdY()[i1] + firstStepY + c.getMapRegionY()*8);
		}
	}

}
