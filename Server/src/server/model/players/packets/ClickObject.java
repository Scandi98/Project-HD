package server.model.players.packets;

import server.cache.RSObjectDefinition;
import server.model.door.Door;
import server.model.door.DoorManager;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.skills.agility.GnomeCourse;
import server.model.players.skills.cooking.Cooking;

public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132;
	public static final int SECOND_CLICK = 252;
	public static final int THIRD_CLICK = 70;

	public ClickObject() {
	}

	public void processPacket(final Client client, int i, int j) {
		client.clickObjectType = client.objectX = client.objectId = client.objectY = 0;
		client.objectYOffset = client.objectXOffset = 0;
		client.getPA().resetFollow();
		client.isWc = false;
		client.isArrowing = false;
		client.isStringing = false;
		client.isSkilling[client.playerFletching] = false;
		client.isSkilling[client.playerSmithing] = false;
		Cooking.resetCooking(client);
		client.getPA().resetFollow();
		client.followId = 0;
		if(client.jumpingDitch)
			return;
		if (!client.canClick)
			return;
		if(client.isDead)
			return;
		if (client.doingAgility) {
			GnomeCourse.resetAgilityWalk(client);
		}
		
		switch (i) {
		default:
			break;

		case 132:
			client.objectX = client.getInStream().readSignedWordBigEndianA();
			client.objectId = client.getInStream().readUnsignedWord();
			client.objectY = client.getInStream().readUnsignedWordA();
			/*if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
				if (Doors.getSingleton().handleDoor(client.objectId, client.objectX, client.objectY, client.heightLevel)) {
				}
			}*/
			client.turnPlayerTo(client.objectX, client.objectY);
			client.objectDistance = 1;
		/*	if (client.playerRights >= 3
					&& client.playerName.equalsIgnoreCase("Pegasus")) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			} else if (client.playerRights == 3) {
				client.sendMessage((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append(" objectX: ")
						.append(client.objectX).append(" objectY: ")
						.append(client.objectY).toString());
			}*/
			
		RSObjectDefinition obj = RSObjectDefinition.valueOf(client.objectId);
			
			if (obj == null || obj.getId() != client.objectId) {
				return;
			}
			
			if (obj.name != null) {
				String name = obj.name.toLowerCase();//i think its same id just rotated
				// probably.

				//if (name.contains("door") || name.contains("gate")) {
				/*if (obj.id == 26760) {// ah i know, checking for orientation wont work here this way, xd, yo doesnt this server have doors.cfg LOL idk
					//Door door = new Door(obj, client.objectX, client.objectY, client.heightLevel);
					//System.out.println("Door clicked at "+client.objectX+", "+client.objectY+", "+door.getDoorPosition(obj)+", "+door.getInverseObject().getId()+", "+door.getGameObject().getId());
					DoorManager.handle(client, DoorManager.wildDoor);// its closed. 
					return;
				}*/
				if (obj.id == 11726) {
					DoorManager.handle(client, DoorManager.snakeDoor);
					return;
				}
			}
			
			if (Math.abs(client.getX() - client.objectX) > 25
					|| Math.abs(client.getY() - client.objectY) > 25) {
				client.resetWalkingQueue();
			} else {
				for (int k = 0; k < client.getRunecrafting().altarID.length; k++) {
					if (client.objectId == client.getRunecrafting().altarID[k]) {
						client.getRunecrafting().craftRunes(client.objectId);
					}
				}
				switch (client.objectId) {
				case 23271:
					if(3522 < client.absY)
						client.objectDistance = 2;
				break;
				case 17010:
					if (client.playerMagicBook == 0) {
						client.sendMessage("You switch spellbook to lunar magic.");
						client.setSidebarInterface(6, 29999);
						client.playerMagicBook = 2;
						client.autocasting = false;
						client.autocastId = -1;
						client.getPA().resetAutocast();
						break;
					}
					if (client.playerMagicBook == 1) {
						client.sendMessage("You switch spellbook to lunar magic.");
						client.setSidebarInterface(6, 29999);
						client.playerMagicBook = 2;
						client.autocasting = false;
						client.autocastId = -1;
						client.getPA().resetAutocast();
						break;
					}
					if (client.playerMagicBook == 2) {
						client.setSidebarInterface(6, 1151);
						client.playerMagicBook = 0;
						client.autocasting = false;
						client.sendMessage("You feel a drain on your memory.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
						break;
					}
					break;
				case 410:
					if (client.playerMagicBook == 0) {
						client.setSidebarInterface(6, 29999);
						client.playerMagicBook = 2;
						client.autocasting = false;
						client.sendMessage("An ancient wisdomin fills your mind.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					} else {
						client.setSidebarInterface(6, 1151);
						client.playerMagicBook = 0;
						client.autocasting = false;
						client.sendMessage("You feel a drain on your memory.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					}
					break;

				case 1733:
					client.objectYOffset = 2;
					break;
					
				case 21764:
				case 3044:
					client.objectDistance = 3;
					break;

				case 245:
					client.objectYOffset = -1;
					client.objectDistance = 0;
					break;

				case 272:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 273:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 246:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 4493:
				case 4494:
				case 4495:
				case 4496:
					client.objectDistance = 5;
					break;

				case 6522:
				case 10229:
					client.objectDistance = 2;
					break;

				case 8959:
					client.objectYOffset = 1;
					break;

				case 4417:
					if (client.objectX == 2425 && client.objectY == 3074) {
						client.objectYOffset = 2;
					}
					break;

				case 4420:
					if (client.getX() >= 2383 && client.getX() <= 2385) {
						client.objectYOffset = 1;
					} else {
						client.objectYOffset = -2;
					}
					// fall through

				case 409:
				case 6552:
					client.objectDistance = 2;
					break;

				case 2878:
				case 2879:
					client.objectDistance = 3;
					break;

				case 2558:
					client.objectDistance = 0;
					if (client.absX > client.objectX && client.objectX == 3044) {
						client.objectXOffset = 1;
					}
					if (client.absY > client.objectY) {
						client.objectYOffset = 1;
					}
					if (client.absX < client.objectX && client.objectX == 3038) {
						client.objectXOffset = -1;
					}
					break;

				case 9356:
					client.objectDistance = 2;
					break;

				case 1815:
				case 1816:
				case 5959:
				case 5960:
					client.objectDistance = 0;
					break;

				case 9293:
					client.objectDistance = 2;
					break;

				case 4418:
					if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2380 && client.objectY == 3127) {
						client.objectYOffset = 2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					}
					break;

				case 9706:
					client.objectDistance = 0;
					client.objectXOffset = 1;
					break;

				case 9707:
					client.objectDistance = 0;
					client.objectYOffset = -1;
					break;

				case 4419:
				case 6707:
					client.objectYOffset = 3;
					break;

				case 6823:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6706:
					client.objectXOffset = 2;
					break;

				case 6772:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6705:
					client.objectYOffset = -1;
					break;

				case 6822:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6704:
					client.objectYOffset = -1;
					break;

				case 6773:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6703:
					client.objectXOffset = -1;
					break;

				case 6771:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6702:
					client.objectXOffset = -1;
					break;

				case 6821:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 1276:
				case 1278:
				case 1281:
				case 1306:
				case 1307:
				case 1308:
				case 1309:
					client.objectDistance = 3;
					break;

				default:
					client.objectDistance = 1;
					client.objectXOffset = 0;
					client.objectYOffset = 0;
					break;
				}
				if (client.destinationReached()) {
					client.turnPlayerTo(client.objectX, client.objectY);
					client.getActions().firstClickObject(client.objectId,
							client.objectX, client.objectY);
				} else {
					client.clickObjectType = 1;
				}
			}
			break;

		case 252:
			client.objectId = client.getInStream().readUnsignedWordBigEndianA();
			client.objectY = client.getInStream().readSignedWordBigEndian();
			client.objectX = client.getInStream().readUnsignedWordA();
			client.objectDistance = 1;
			/*if (client.playerRights >= 3) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			}*/
			switch (client.objectId) {
			case 6162:
			case 6163:
			case 6164:
			case 6165:
			case 6166:
				client.objectDistance = 2;
				break;

			default:
				client.objectDistance = 1;
				client.objectXOffset = 0;
				client.objectYOffset = 0;
				break;
			}
			if (client.destinationReached()) {
				client.turnPlayerTo(client.objectX, client.objectY);
				client.getActions().secondClickObject(client.objectId,
						client.objectX, client.objectY);
			} else {
				client.clickObjectType = 2;
			}
			break;

		case 70: // 'F'
			client.objectX = client.getInStream().readSignedWordBigEndian();
			client.objectY = client.getInStream().readUnsignedWord();
			client.objectId = client.getInStream().readUnsignedWordBigEndianA();
			/*if (client.playerRights >= 3) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			}*/
			switch (client.objectId) {
			default:
				client.objectDistance = 1;
				break;
			}
			client.objectXOffset = 0;
			client.objectYOffset = 0;
			if (client.destinationReached()) {
				client.turnPlayerTo(client.objectX, client.objectY);
				client.getActions().thirdClickObject(client.objectId,
						client.objectX, client.objectY);
			} else {
				client.clickObjectType = 3;
			}
			break;
		}
	}

	public void handleSpecialCase(Client client, int i, int j, int k) {
	}
}
