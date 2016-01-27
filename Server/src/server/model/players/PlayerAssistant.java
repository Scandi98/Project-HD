package server.model.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import server.Config;
import server.Connection;
import server.Server;
import server.clan.Clan;
import server.core.PlayerHandler;
import server.core.World;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.Location;
import server.model.Projectile;
import server.model.content.Blowpipe;
import server.model.door.Door;
import server.model.items.GameItem;
import server.model.items.Item;
import server.model.minigames.bh.BountyHunter;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.objects.GameObject;
import server.model.players.Attributes.A;
import server.model.players.Palette.PaletteTile;
import server.model.players.skills.SkillHandler;
import server.model.players.skills.agility.GnomeCourse;
import server.model.players.skills.cooking.Cooking;
import server.model.players.skills.crafting.GemData;
import server.region.Region;
import server.region.pf.PathFinder;
import server.tick.Tickable;
import server.util.Misc;

public class PlayerAssistant {

	private Client c;

	public PlayerAssistant(Client Client) {
		this.c = Client;
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void openXInterface(int xInterfaceId) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(27);
			c.outStream.writeByte(xInterfaceId);
			c.flushOutStream();
		}
	}

	/**
	 * Sends some information to the client about screen fading.
	 * 
	 * @param text
	 *            the text that will be displayed in the center of the screen
	 * @param state
	 *            the state should be either 0, -1, or 1.
	 * @param seconds
	 *            the amount of time in seconds it takes for the fade to
	 *            transition.
	 *            <p>
	 *            If the state is -1 then the screen fades from black to
	 *            transparent. When the state is +1 the screen fades from
	 *            transparent to black. If the state is 0 all drawing is
	 *            stopped.
	 */
	public void sendScreenFade(String text, int state, int seconds) {
		if (c == null || c.getOutStream() == null) {
			return;
		}
		if (seconds < 1) {
			throw new IllegalArgumentException("The amount of seconds cannot be less than one.");
		}
		c.getOutStream().createFrameVarSize(9);
		c.getOutStream().writeString(text);
		c.getOutStream().writeByte(state);
		c.getOutStream().writeByte(seconds);
		c.getOutStream().endFrameVarSize();
	}

	public void sendOpponentInformation(Client opponent) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(212);
			for (int i = 0; i < 7; i++) {
				if (i == 3) {
					c.outStream.writeByte(opponent.getLevelForXP(opponent.playerXP[3]));
					continue;
				}

				c.outStream.writeByte(opponent.playerLevel[i]);
			}
		}
		c.flushOutStream();
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (c.playerLevel[6] < 94) {
			c.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (c.playerLevel[1] < 40) {
			c.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!c.getItems().playerHasItem(9075, 4) || !c.getItems().playerHasItem(557, 10)
				|| !c.getItems().playerHasItem(560, 2)) {
			c.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCast < 30000) {
			c.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (c.vengOn) {
			c.sendMessage("You already have vengeance casted.");
			return;
		}
		c.startAnimation(4410);
		c.gfx100(726);// Just use c.gfx100
		c.getItems().deleteItem2(9075, 4);
		c.getItems().deleteItem2(557, 10);// For these you need to change to
		// deleteItem(item, itemslot,
		// amount);.
		c.getItems().deleteItem2(560, 2);
		addSkillXP(112, 6);
		refreshSkill(6);
		c.vengOn = true;
		c.lastCast = System.currentTimeMillis();
		c.getPA().sendNewString("vengtimer:" + (System.currentTimeMillis() - c.lastCast + 49), -1);
	}

	public void setCameraFocus(int x, int y, int height, int speed, int angle) {
		c.getOutStream().createFrame(167);
		c.getOutStream().writeByte(x / 64);
		c.getOutStream().writeByte(y / 64);
		c.getOutStream().writeWord(height);
		c.getOutStream().writeByte(speed);
		c.getOutStream().writeByte(angle);
		c.flushOutStream();// forgot to do this other night xd
	}

	/***
	 * Gets interface id selected
	 */

	public final void setConfig(int InterfaceID, int state) { // see they are
		// diffren this
		// loads it
		// perfectly in
		// myserver to
		// client.
		c.getOutStream().createFrame(36);
		c.getOutStream().writeWordBigEndian(InterfaceID);
		c.getOutStream().write3Byte(state);
	}// Ima find my method cause this is a long method to write up over
		// teamviewer :/ kk

	/**
	 * Take bank item that searched.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @param x
	 */
	public void takeOut(int itemID, int fromSlot, int amount, boolean x) {
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] > 0) {
				if (c.bankItems[j] - 1 == itemID) {
					if (x) {// means their using remove x
						if (amount > c.getItems().freeSlots() & amount > c.bankItemsN[j] & c.bankItemsN[j] >= amount) {
							c.bankItemsN[j] -= c.getItems().freeSlots();
							c.getItems().addItem(itemID, c.getItems().freeSlots());
						} else {
							c.getItems().addItem(itemID, amount);
							c.bankItemsN[j] -= amount;
							if (c.bankItemsN[j] == 0)
								c.bankItems[j] = 0;
							c.itemsN[fromSlot] = 0;
						}
					} else if (amount == -1) {// their using remove all
						if (c.bankItemsN[j] > c.getItems().freeSlots()) {
							c.bankItemsN[j] -= c.getItems().freeSlots();
							c.itemsN[fromSlot] -= c.getItems().freeSlots();
							c.getItems().addItem(itemID, c.getItems().freeSlots());
						}
						c.getItems().addItem(itemID, c.bankItemsN[j]);
						c.bankItemsN[j] = 0;
						c.bankItems[j] = 0;
						c.items[fromSlot] = 0;
						c.itemsN[fromSlot] = 0;
						break;
					} else if ((c.bankItemsN[j] - amount) > 0) {
						if (amount > c.bankItemsN[j]) {
							if (!c.getItems().addItem(itemID, c.bankItemsN[j]))
								break;
							c.bankItemsN[j] -= c.bankItemsN[j];
							c.bankItems[j] = 0;
							c.itemsN[fromSlot] -= c.bankItemsN[j];
							c.items[fromSlot] = 0;
							break;
						}
						if (!c.getItems().addItem(itemID, amount))
							break;
						c.bankItemsN[j] -= amount;
						c.itemsN[fromSlot] -= amount;
						c.items[fromSlot] = 0;
						if (c.bankItemsN[j] == 0)
							c.bankItems[j] = 0;
					} else {
						if (amount > c.bankItemsN[j]) {
							if (!c.getItems().addItem(itemID, c.bankItemsN[j]))
								break;
							c.bankItems[j] = 0;
							c.bankItemsN[j] -= c.bankItemsN[j];
							c.itemsN[fromSlot] -= c.bankItemsN[j];
							c.items[fromSlot] = 0;
							break;
						}
						if (!c.getItems().addItem(itemID, amount))
							break;
						c.bankItems[j] = 0;
						c.bankItemsN[j] -= amount;
						c.items[fromSlot] = 0;
						c.itemsN[fromSlot] -= amount;
					}
				}
			}
		}
		c.getItems().resetTempItems();
		c.lastSearch = true;
		searchBank(c.searchName);
	}

	public void destroySearch() {
		c.lastSearch = false;
		c.isSearching = false;
		c.items = new int[500];
		c.itemsN = new int[500];
		c.searchName = "";
		c.getItems().resetTempItems();
		c.getItems().rearrangeBank();
		c.getItems().resetBank();
		c.getItems().resetKeepItems();
		sendNewString("The bank of Dragon-Age.", 5383);
	}

	private void showItems(int items[], int itemsN[]) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5382);
		c.getOutStream().writeWord(Config.BANK_SIZE);
		for (int j = 0; j < items.length; j++) {
			if (items[j] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(itemsN[j]);
			} else {
				c.getOutStream().writeByte(itemsN[j]);
			}
			if (itemsN[j] < 1) {
				items[j] = 0;
			}
			if (items[j] > Config.ITEM_LIMIT | items[j] < 0) {
				items[j] = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(items[j]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void searchBank(String str) {
		sendNewString("The bank of Dragon-Age.", 5383);// didnt this change?
		c.items = new int[500];
		c.itemsN = new int[500];
		int p = 0;
		int slot = 0;
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] > 0) {
				if (c.getItems().getItemName(c.bankItems[j] - 1).toLowerCase().contains(str.toLowerCase())) {
					c.items[slot] = c.bankItems[j];
					c.itemsN[slot] = c.bankItemsN[j];
					slot++;
					p++;
				}
			}
		}
		if (p > 0) {
			sendNewString("Bank of Dragon-Age - (search: '" + str + "')", 5383);
			showItems(c.items, c.itemsN);
			c.getItems().resetTempItems();
			slot = 0;
			c.isSearching = true;
		} else {
			if (c.lastSearch & c.isSearching) {
				destroySearch();
				return;
			} else {
				sendNewString("No results were found for '" + str + "'.", 5383);
				c.isSearching = false;
			}
		}
	}

	// fuck it i hope i remember it all
	public void clearClanChat() {
		c.clanId = -1;
		c.getPA().sendNewString("Talking in: ", 18139);
		c.getPA().sendNewString("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++)
			c.getPA().sendNewString("", j);
	}

	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
	}

	public void sendFrame126(String s, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void sendNewString(String s, int id) {
		if (c != null) {
			c.getStringMap().put(id, s);
		}
	}

	public void flushNewString() {
		if (c != null) {
			for (Map.Entry<Integer, String> entry : c.stringMap.entrySet()) {
				sendFrame126(entry.getValue(), entry.getKey());
			}
			c.getStringMap().clear();
		}
	}

	public void sendLink(String s) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}

	public void sendConfig(int id, int value) {
		if (c.getOutStream() != null && c != null) {
			if ((value >= 0) && (value <= 255)) {
				c.getOutStream().createFrame(36);
				c.getOutStream().writeWordBigEndian(id);
				c.getOutStream().writeByte(value);
			} else {
				c.getOutStream().createFrame(87);
				c.getOutStream().writeWordBigEndian_dup(id);
				c.getOutStream().writeDWord_v1(value);
			}
			c.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {
		if (c.inTrade || c.inDuel) {
			return;
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();
			c.interfaceOpen = true;
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void setInterfaceVisible(int interfaceId, boolean visible) {
		c.outStream.createFrame(171);
		c.outStream.writeByte(visible ? 1 : 0);
		c.outStream.writeWord(interfaceId);
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame164(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}

	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}
	}

	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(196);
			c.getOutStream().writeQWord(name);
			c.getOutStream().writeDWord(c.lastChatId++);
			c.getOutStream().writeByte(rights);
			c.getOutStream().writeBytes(chatmessage, messagesize, 0);
			c.getOutStream().endFrameVarSize();
			c.flushOutStream();
			Misc.textUnpack(chatmessage, messagesize);
			Misc.longToPlayerName(name);
		}
	}

	public void createPlayerHints(int type, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	public void loadPM(long playerName, int world) {
		// if (c.playerName.equalsIgnoreCase("abc"))
		// new Throwable().printStackTrace();
		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Config.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}
	}

	public void removeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.interfaceOpen = false;
		}
	}

	public void closeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.interfaceOpen = false;
			c.bankCheck = false;
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith
			// screen
			c.outStream.writeWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}
	}

	public void findSlot(int slot) {
		if (c != null) {
			int id = c.playerItems[slot];
			for (int i = 0; i < GemData.gemSlot.length; i++) {
				if (GemData.gemSlot[i] == id) {
					c.setSlot(GemData.gemSlot[i]);
				}
			}
		}
	}

	public int openId;

	public void walkableInterface(int id) {
		openId = id;
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}

	public void sendUpdateItem(int slot, int inventoryId, int id, int amount) {
		/*
		 * StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		 * out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		 * out.writeShort(inventoryId); out.writeByte(slot); if(item.getId() ==
		 * 0){ out.writeShort(0); out.writeByte(0); } else {
		 * out.writeShort(item.getId() + 1); if (item.getCount() > 254) {
		 * out.writeByte(255); out.writeShort(item.getCount()); } else {
		 * out.writeByte(item.getCount()); } }
		 * out.finishVariableShortPacketHeader(); player.send(out.getBuffer());
		 * return this;
		 */
		/*
		 * c.getOutStream().createFrame(34); c.getOutStream().writeWord(6);
		 * c.getOutStream().writeWord(1688); c.getOutStream().writeByte(slot);
		 * c.getOutStream().writeWord(0); c.getOutStream().writeByte(0);
		 */
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(34);
			c.getOutStream().writeWord(inventoryId);
			c.getOutStream().writeByte(slot);
			if (id == 0) {
				c.getOutStream().writeWord(0);
				c.getOutStream().writeByte(0);
			} else {
				c.getOutStream().writeWord(id + 1);
				if (amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeByte(amount);
				} else {
					c.getOutStream().writeByte(amount);
				}
			}
			c.flushOutStream();
		}
	}

	public void sendItemOnInterface(int id, int zoom, int model) {
		/*
		 * StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		 * out.writeHeader(player.getEncryptor(), 246); out.writeShort(id == 0 ?
		 * -1 : id, StreamBuffer.ByteOrder.LITTLE); out.writeShort(zoom);
		 * out.writeShort(model); player.send(out.getBuffer());
		 */
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(id == 0 ? -1 : id);
			c.getOutStream().writeWord(zoom);
			c.getOutStream().writeWord(model);
			c.flushOutStream();
		}
	}

	public void sendCrashFrame() { // used for crashing cheat clients
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(123);
			c.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createProjectile(Projectile projectile) {
		if (projectile.isGlobal()) {
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null && person.getOutStream() != null && !person.disconnected
						&& person.distanceToPoint(projectile.getStart().getX(), projectile.getStart().getY()) <= 25) {
					person.getPA().sendProjectile(projectile);
				}
			}
		} else {
			sendProjectile(projectile);
		}
	}

	public void sendProjectile(Projectile projectile) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((projectile.getStart().getY() - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((projectile.getStart().getX() - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(projectile.getAngle());
			c.getOutStream().writeByte(projectile.getOffsetX());
			c.getOutStream().writeByte(projectile.getOffsetY());
			c.getOutStream().writeWord(projectile.getLockon());
			c.getOutStream().writeWord(projectile.getId());
			c.getOutStream().writeByte(projectile.getStartHeight());
			c.getOutStream().writeByte(projectile.getEndHeight());
			c.getOutStream().writeWord(projectile.getSlope());
			c.getOutStream().writeWord(projectile.getSpeed());
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel)
								person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}
	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		}
	}

	public boolean spawnObject;

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		spawnObject = true;
		if (spawnObject) {
			if (c.getOutStream() != null && c != null) {
				int localX = objectX - (c.getMapRegionX() * 8);
				int localY = objectY - (c.getMapRegionY() * 8);

				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC(localY);
				c.getOutStream().writeByteC(localX);

				c.getOutStream().createFrame(101);
				c.getOutStream().writeByteC((objectType << 2) + (face & 3));
				c.getOutStream().writeByte(0);
				if (objectId != -1) {
					c.getOutStream().createFrame(151);
					c.getOutStream().writeByteS(0);
					c.getOutStream().writeWordBigEndian(objectId);
					c.getOutStream().writeByteS((objectType << 2) + (face & 3));
				}
				c.flushOutStream();
				//Region.addObject(new GameObject(objectId, objectX, objectY, c.heightLevel, face, objectType));
			}
			spawnObject = false;
		}
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		spawnObject = true;
		if (spawnObject) {
			if (c.distanceToPoint(objectX, objectY) > 60)
				return;
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
				c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
				c.getOutStream().createFrame(101);
				c.getOutStream().writeByteC((objectType << 2) + (face & 3));
				c.getOutStream().writeByte(0);

				if (objectId != -1) { // removing
					c.getOutStream().createFrame(151);
					c.getOutStream().writeByteS(0);
					c.getOutStream().writeWordBigEndian(objectId);
					c.getOutStream().writeByteS((objectType << 2) + (face & 3));
				}
				c.flushOutStream();
			}
			spawnObject = false;
		}
	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";
	
	public void sendOption(String s, int pos) {
		if (c.outStream != null && c != null) {
			c.outStream.createFrameVarSize(104);
			c.outStream.writeByteC(pos);
			c.outStream.writeByteA(0);
			c.outStream.writeString(s);
			c.outStream.endFrameVarSize();
			c.flushOutStream();
		}
	}

	public void showOption(int i, int l, String s, int a) {
		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}
		}
	}

	/**
	 * Open bank
	 **/
	public void openUpBank(int tab) {
		if (c.duelSafety) {
			c.sendMessage("Please re-log before doing this.");
			return;
		}
			
		if (c.setPin) {
			if (!c.bankPin.equals("")) {
				c.getBankPin().open();
				return;
			}
		}
		if (c.takeAsNote) {
			sendFrame36(115, 1);
		} else {
			sendFrame36(115, 0);
		}
		if (c.inWild()) {
			c.isBanking = false;
			return;

		}
		if (c.getTradeHandler().getCurrentTrade() != null) {
			if (c.getTradeHandler().getCurrentTrade().isOpen()) {
				c.getTradeHandler().decline();
			}
		}
		if (c.getOutStream() != null && c != null) {
			c.isBanking = true;
			c.bankingTab = tab;
			sendTabs();
			if (c.bankingTab == 0) {// ur way doesnt work. ill brb i gotta go
									// eat supper
				c.bankingItems = c.bankItems;
				c.bankingItemsN = c.bankItemsN;
			}
			if (c.bankingTab == 1) {
				c.bankingItems = c.bankItems1;
				c.bankingItemsN = c.bankItems1N;
			}
			if (c.bankingTab == 2) {
				c.bankingItems = c.bankItems2;
				c.bankingItemsN = c.bankItems2N;
			}
			if (c.bankingTab == 3) {
				c.bankingItems = c.bankItems3;
				c.bankingItemsN = c.bankItems3N;
			}
			if (c.bankingTab == 4) {
				c.bankingItems = c.bankItems4;
				c.bankingItemsN = c.bankItems4N;
			}
			if (c.bankingTab == 5) {
				c.bankingItems = c.bankItems5;
				c.bankingItemsN = c.bankItems5N;
			}
			if (c.bankingTab == 6) {
				c.bankingItems = c.bankItems6;
				c.bankingItemsN = c.bankItems6N;
			}
			if (c.bankingTab == 7) {
				c.bankingItems = c.bankItems7;
				c.bankingItemsN = c.bankItems7N;
			}
			if (c.bankingTab == 8) {
				c.bankingItems = c.bankItems8;
				c.bankingItemsN = c.bankItems8N;
			}
			c.getItems().resetItems(5064);
			c.getItems().rearrangeBank();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(24959);// cant we use the server sided
			// method to just update item
			// when you remove a bank item?
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
	}

	public void openBank(int tab) {
		if (c.duelSafety) {
			c.sendMessage("Please re-log before doing this.");
			return;
		}
			
		if (c.takeAsNote) {
			sendFrame36(115, 1);
		} else {
			sendFrame36(115, 0);
		}
		
		if (c.inWild()) {
			c.isBanking = false;
			return;

		}
		if (c.getTradeHandler().getCurrentTrade() != null) {
			if (c.getTradeHandler().getCurrentTrade().isOpen()) {
				c.getTradeHandler().decline();
			}
		}
		if (c.getOutStream() != null && c != null) {
			c.isBanking = true;
			c.bankingTab = tab;
			sendTabs();
			if (c.bankingTab == 0) {
				c.bankingItems = c.bankItems;
				c.bankingItemsN = c.bankItemsN;
			}
			if (c.bankingTab == 1) {
				c.bankingItems = c.bankItems1;
				c.bankingItemsN = c.bankItems1N;
			}
			if (c.bankingTab == 2) {
				c.bankingItems = c.bankItems2;
				c.bankingItemsN = c.bankItems2N;
			}
			if (c.bankingTab == 3) {
				c.bankingItems = c.bankItems3;
				c.bankingItemsN = c.bankItems3N;
			}
			if (c.bankingTab == 4) {
				c.bankingItems = c.bankItems4;
				c.bankingItemsN = c.bankItems4N;
			}
			if (c.bankingTab == 5) {
				c.bankingItems = c.bankItems5;
				c.bankingItemsN = c.bankItems5N;
			}
			if (c.bankingTab == 6) {
				c.bankingItems = c.bankItems6;
				c.bankingItemsN = c.bankItems6N;
			}
			if (c.bankingTab == 7) {
				c.bankingItems = c.bankItems7;
				c.bankingItemsN = c.bankItems7N;
			}
			if (c.bankingTab == 8) {
				c.bankingItems = c.bankItems8;
				c.bankingItemsN = c.bankItems8N;
			}
			c.getItems().resetItems(5064);
			c.getItems().rearrangeBank();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(24959);
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
	}

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < Config.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Client o = (Client) p;
				if (o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				for (int i2 = 1; i2 < Config.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == c.friends[i]) {
						Client o = (Client) p;
						if (o != null) {
							if (c.playerRights >= 2 || p.privateChat == 0
									|| (p.privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
								loadPM(c.friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(c.friends[i], 0);
				}
				pmLoaded = false;
			}
		}
		for (int i1 = 1; i1 < Config.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Client o = (Client) p;
				if (o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
	}

	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Client o = (Client) p;
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privateChat == 0) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						if (o.getPA().isInPM(Misc.playerNameToInt64(c.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i] && c.playerRights < 2) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				if (l == c.friends[i]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */

	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		c.attackTimer = c.getCombat()
				.getAttackDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		if (c.duelRule[5]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the " + c.getItems().getItemName(itemId).toLowerCase() + ".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/

	public void magicOnItems(int slot, int itemId, int spellId) {
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		switch (spellId) {
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			c.getCombat().resetPlayerAttack();
			c.getMagic().enchantItem(itemId, spellId);
			break;
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getCombat().resetPlayerAttack();
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, c.getShops().getItemShopValue(itemId) / 3);
				c.startAnimation(Client.MAGIC_SPELLS[49][2]);
				c.gfx100(Client.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(Client.MAGIC_SPELLS[49][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;
		// Okay, will fix this dupe thansk

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getCombat().resetPlayerAttack();
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, (int) (c.getShops().getItemShopValue(itemId) * .75));
				c.startAnimation(Client.MAGIC_SPELLS[50][2]);
				c.gfx100(Client.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(Client.MAGIC_SPELLS[50][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;
		}
	}

	public void ditchJump() {
		c.startAnimation(6132);
		c.hadRunWhileJumping = c.isRunning;
		c.jumpingDitch = c.isRunning = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!c.ditchEventRunned) {
					c.jumpDitchChangePos = 3522 < c.absY ? -3 : 3;
					c.ditchEventRunned = true;
				}
				if (c.jumpDitchChangePos != 0)
					c.turnPlayerTo(c.absX, c.absY + (c.jumpDitchChangePos == 3 ? 1 : -1));
				if (c.jumpDitchChangePos == 0 && (c.absY == 3523 || c.absY == 3520))
					container.stop();
			}

			@Override
			public void stop() {
				c.jumpingDitch = false;
				c.ditchEventRunned = false;
			}
		}, 1);
	}

	/**
	 * Dieing
	 **/

	public void applyDead() {
		// c.getLogs().playerKills();
		//c.getDuel().stakedItems.clear();
		c.respawnTimer = 15;
		c.isDead = false;
		c.getPA().sendNewString(":quicks:off", -1);

		if (c.duelStatus != 6) {
			// c.killerId = c.getCombat().getKillerId(c.playerId);
			c.killerId = findKiller();
			Client o = (Client) PlayerHandler.players[c.killerId];
			o.poisonDamage = 0;
			o.isPoisoned = false;
			o.venomDamage = 6;
			o.venomDebuff = false;
			o.lastVenomSip = 0;
			if (o != null) {
				if (c.killerId != c.playerId)
					if (c.inWild() || c.inPvP()) {
						if (!PlayerKilling.hostOnList(o, c.connectedFrom) || c.connectedFrom != o.connectedFrom) {
							PlayerKilling.addHostToList(o, c.connectedFrom);
							c.DC++;
							o.KC++;
							if (o.playerRights >= 6 && o.playerRights <= 9)
								o.pkPoints += 2;
							else
								o.pkPoints += 1;
							o.upgradeEmblem();
							if (Misc.random(5) == 1) {
								o.dropRandomEmblem(o, c);
							}
							o.getItems().addItemToBank2(995, 50000);
							BountyHunter.handleKilled(o, c);
							o.sendMessage("You have received Pk Points after defeating @red@"
									+ Misc.optimizeText(c.playerName) + "!");
							o.sendMessage("You have recieved a extra of: @red@50,000@bla@ coins to your bank.");
							// do you want me to add where they cant kill the
							// same person for killstreak yes
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Client c2 = (Client) PlayerHandler.players[j];
									c.sendMessage("[@cr6@@red@Pking@bla@@cr6@] @blu@" + o.playerName
											+ "@bla@ has just killed @red@" + c.playerName
											+ "@bla@ In the Wilderness!");
								}
							}
							this.c.getDuel().stakedItems.clear();
							c.getPA().loadQuests();
							o.getPA().loadQuests();
							o.cleanInterface();
						} else {
							o.sendMessage("You have recently defeated @red@" + Misc.optimizeText(c.playerName)
									+ ",@bla@ you didnt recieved pk points.");
						}
					}
			} // duel stat
		} // null
		c.faceUpdate(0);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer b) {
				c.npcIndex = 0;
				c.playerIndex = 0;
				b.stop();
			}
		}, 2500);
		c.stopMovement();
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.stopMovement();
		if (c.duelStatus <= 4) {
			c.sendMessage("Oh dear you are dead!");
			c.playerIndex = 0;
		}
		PlayerSave.saveGame(c);
		resetDamageDone();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		c.lastVeng = 0;
		c.vengOn = false;
		resetFollowers();
		c.attackTimer = 10;
		removeAllWindows();
		c.tradeResetNeeded = true;
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.playerId] = 0;
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - c.lastVeng > 30000) {
			if (c.getItems().playerHasItem(557, 10) && c.getItems().playerHasItem(9075, 4)
					&& c.getItems().playerHasItem(560, 2)) {
				c.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				c.startAnimation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 10);
				c.getItems().deleteItem(560, c.getItems().getItemSlot(560), 2);
				c.getItems().deleteItem(9075, c.getItems().getItemSlot(9075), 4);
			} else {
				c.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.playerId) {
					Client c = (Client) PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}

	private Map<Integer, Integer> zulrahLostItems = new HashMap<Integer, Integer>();

	public void returnZulrahItems() {
		// List<Map.Entry<Integer, Integer>> items = new
		// ArrayList<Map.Entry<Integer, Integer>>();
		//
		// for (Map.Entry<Integer, Integer> entry : zulrahLostItems.entrySet())
		// {
		// if (entry.getValue() > 0 && entry.getKey() > 0) {
		// items.add(entry);
		// }
		// }
		// if (items.size() == 0) {
		// c.sendMessage("You currently have no items waiting for you.");
		// return;
		// }
		// if (c.getItems().freeSlots() < items.size()) {
		// c.sendMessage("Not enough free inventory space.");
		// return;
		// }
		// zulrahLostItems.clear();
		// for (Map.Entry<Integer, Integer> item : items) {
		// c.getItems().addItem(item.getKey(), item.getValue());
		// }
	}

	public void giveLife() {
		c.isDead = false;
		c.faceUpdate(-1);
		c.freezeTimer = 0;
		if (c.duelStatus <= 4 && !c.getPA().inPitsWait() && !c.inPcGame() && !c.inClanWarsGame) {
			if (!c.inPits && !c.inFightCaves() && !c.inFFAPortal()) {
				c.getItems().resetKeepItems();
				/*
				 * if ((c.playerRights == 3 && Config.ADMIN_DROP_ITEMS) ||
				 * c.playerRights != 3) {
				 */
				if (!c.isSkulled && !inZulrah() && !c.inFFAPortal()) { // what
																		// items
																		// to
																		// keep
					c.getItems().keepItem(0, true);
					c.getItems().keepItem(1, true);
					c.getItems().keepItem(2, true);
				}
				if (c.prayerActive[10] && System.currentTimeMillis() - c.lastProtItem > 700 && !inZulrah()) {
					c.getItems().keepItem(3, true);
				}
				Client o = (Client) PlayerHandler.players[c.killerId];
				if (inZulrah()) {
					// for (int i = 0; i < c.playerItems.length; i++) {
					// zulrahLostItems.put(c.playerItems[i], c.playerItemsN[i]);
					// }
					// for (int e = 0; e < c.playerEquipment.length; e++) {
					// zulrahLostItems.put(c.playerEquipment[e],
					// c.playerEquipmentN[e]);
					// }
					// c.getItems().deleteAllItems();
				} else {
					if (o.playerRights == 10 && o != c) {
						if (c.playerName != o.playerName) {
							c.getItems().dropUntradables();
						} else {
							c.getItems().dropAllIronMan();
						}
					} else {
						if (c.playerRights != 2) {
							c.getItems().dropAllItems();
						}
					}
					if (c.playerRights != 2) {
						c.getItems().deleteAllItems(); // delete all items
					}
				}

				if (!c.isSkulled) { // add the kept items once we finish
					// deleting and dropping them
					for (int i1 = 0; i1 < 3; i1++) {
						if (c.itemKeptId[i1] > 0) {
							c.getItems().addItem(c.itemKeptId[i1], 1);
						}
					}
				}
				if (c.prayerActive[10]) { // if we have protect items
					if (c.itemKeptId[3] > 0) {
						c.getItems().addItem(c.itemKeptId[3], 1);
					}
				}
				// }
				c.getItems().resetKeepItems();
				for (int item = 0; item < Config.ITEMS_KEPT_ON_DEATH.length; item++) {
					int itemId = Config.ITEMS_KEPT_ON_DEATH[item];
					int itemAmount = c.getItems().getItemAmount(itemId) + c.getItems().getWornItemAmount(itemId);
					if (c.getItems().playerHasItem(itemId) || c.getItems().isWearingItem(itemId)) {
						c.sendMessage("You kept " + itemAmount + " " + c.getItems().getItemName(itemId).toLowerCase()
								+ ", it was transferred to your bank.");
						c.getItems().addItemToBank(itemId, itemAmount);
					}
				}
			} else if (c.inPits) {
				c.duelStatus = 0;
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.pitsStatus = 1;
			} else if (c.inFightCaves()) {
				c.getPA().resetTzhaar();
			}
		}
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (c.pitsStatus == 1) {
			c.pitsStatus = 0;
			movePlayer(2399, 5173, 0);
		} else if (c.duelStatus <= 4 && !c.inPcGame() && !c.inClanWarsGame) { // if
			// we
			// are
			// not
			// in
			// a
			// duel
			// repawn
			// to
			// wildy
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
		} else if (c.inFightCaves()) {
			c.getPA().resetTzhaar();
		} else if (c.inPcGame()) {
			movePlayer(2656, 2608, 0);
		} else if (c.inClanWarsGame) {
			if (c.clanWarsTeam == 1) {
				Server.clanWars.redKilled++;
				movePlayer(2414, 3073, 0);
			} else if (c.clanWarsTeam == 2) {
				Server.clanWars.blueKilled++;
				movePlayer(2385, 3134, 0);
			}
			Server.clanWars.giveItems(c);
		} else if (c.inDuelArena() && c.duelStatus == 5) {
			c.getPA().movePlayer(Config.DUELING_RESPAWN_X, Config.DUELING_RESPAWN_Y, 0);
			Client otherClient = (Client) PlayerHandler.players[c.duelingWith];
			if (otherClient == null) {
				return;
			}
			otherClient.getDuel().duelVictory();
			if (c.duelStatus != 6) {
				c.getDuel().resetDuel();
			}
		}
		// PlayerSaving.getSingleton().requestSave(c.playerId);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.startAnimation(65535);
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.killerId = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();
		removeAllWindows();
		c.tradeResetNeeded = true;
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (c.newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, 3);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, 3);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, 3);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, 3);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, 3);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, 3);
			break;
		}
		c.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		if (c.inZulrahShrine()) {
			//Zulrah.destruct(c);
		}
		c.getPA().startTeleport(x, y, height, c.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (c.wearId == 7927 || c.wearId == 6583) {
			c.sendMessage("You cannot teleport with the ring equipped.");
			return;
		}
		c.isWc = false;
		c.getPA().resetFollow();
		c.dialogueAction = -1;
		c.teleAction = -1;
		c.playerSkilling[c.playerCrafting] = false;
		c.playerSkilling[c.playerFishing] = false;
		c.isSkilling[c.playerHerblore] = false;
		c.resetAllActions();
		c.isSkilling[c.playerFletching] = false;
		Cooking.resetCooking(c);
		if (c.doingAgility) {
			GnomeCourse.resetAgilityWalk(c);
		}
		if (c.inTrade) {
			c.sendMessage("You can't teleport while in the trade screen.");
			return;
		}
		if (c.inFFAPortal()) {
			c.sendMessage("You can't teleport out of here use the Portal.");
			return;
		}
		SkillHandler.isSkilling[12] = false;
		c.bankCheck = false;
		if (c.usingAltar)
			c.usingAltar = false;
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
			c.stopPlayerSkill = false;
		}
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.inPcGame() || c.inPcBoat() || c.inClanWarsGame || c.inClanWarsWait) {
			c.startAnimation(-1);
			c.sendMessage("You can't teleport from this location!");
			return;
		}
		if (c.isInJail()) {
			c.sendMessage("You cannot teleport out of jail.");
			return;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL && !c.usingGlory) {
			c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		} else if (c.inWild() && c.wildLevel > 30 && c.usingGlory) {
			c.sendMessage("You can't teleport above level 30 in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			if (teleportType.equalsIgnoreCase("modern")) {
				c.startAnimation(714);
				c.teleTimer = 11;
				c.teleGfx = 308;
				c.teleEndAnimation = 715;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				c.startAnimation(1979);
				c.teleGfx = 0;
				c.teleTimer = 9;
				c.teleEndAnimation = 0;
				c.gfx0(392);
			}
			c.getPA().removeAllWindows();
		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(714);
			c.teleTimer = 11;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;

		}
	}

	public void processTeleport() {
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
		if (c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
		}
	}

	public void movePlayer(int x, int y, int h) {
		c.movingPlayer = true;
		if (c.movingPlayer) {
			if (c.isInJail()) {
				c.sendMessage("You cannot teleport out of jail.");
				return;
			}
			c.resetWalkingQueue();
			c.teleportToX = x;
			c.teleportToY = y;
			c.heightLevel = h;
			requestUpdates();
			c.movingPlayer = false;
		}
	}

	/**
	 * Following
	 **/

	/*
	 * public void Player() { if(PlayerHandler.players[c.followId] == null ||
	 * PlayerHandler.players[c.followId].isDead) { c.getPA().resetFollow();
	 * return; } if(c.freezeTimer > 0) { return; } int otherX =
	 * PlayerHandler.players[c.followId].getX(); int otherY =
	 * PlayerHandler.players[c.followId].getY(); boolean withinDistance =
	 * c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2); boolean
	 * hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
	 * boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(),
	 * 6); boolean rangeWeaponDistance = c.goodDistance(otherX, otherY,
	 * c.getX(), c.getY(), 2); boolean sameSpot = (c.absX == otherX && c.absY ==
	 * otherY); if(!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
	 * c.followId = 0; c.getPA().resetFollow(); return; }
	 * c.faceUpdate(c.followId+32768); if ((c.usingBow || c.mageFollow ||
	 * c.autocastId > 0 && (c.npcIndex > 0 || c.playerIndex > 0)) && bowDistance
	 * && !sameSpot) { c.stopMovement(); return; } if (c.usingRangeWeapon &&
	 * rangeWeaponDistance && !sameSpot && (c.npcIndex > 0 || c.playerIndex >
	 * 0)) { c.stopMovement(); return; } if(c.goodDistance(otherX, otherY,
	 * c.getX(), c.getY(), 1) && !sameSpot) { return; }
	 * c.outStream.createFrame(174); boolean followPlayer = c.followId > 0; if
	 * (c.freezeTimer <= 0) if (followPlayer) c.outStream.writeWord(c.followId);
	 * else c.outStream.writeWord(c.followId2); else c.outStream.writeWord(0);
	 * 
	 * if (followPlayer) c.outStream.writeByte(1); else
	 * c.outStream.writeByte(0); if (c.usingBow && c.playerIndex > 0)
	 * c.followDistance = 5; else if (c.usingRangeWeapon && c.playerIndex > 0)
	 * c.followDistance = 3; else if (c.spellId > 0 && c.playerIndex > 0)
	 * c.followDistance = 5; else c.followDistance = 1;
	 * c.outStream.writeWord(c.followDistance); }
	 */
	long lastTime = System.currentTimeMillis();

	public void followPlayer() {
		if (PlayerHandler.players[c.followId] == null || PlayerHandler.players[c.followId].isDead) {
			c.followId = 0;
			return;
		}
		if (c.freezeTimer > 0 || c.stunTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		Player other = PlayerHandler.players[c.followId];
		int otherX = other.getX();
		int otherY = other.getY();

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			return;
		}
		boolean isRegularFollowing = c.attr(A.REGULAR_FOLLOWING);
		boolean isPostProcess = c.attr(A.POST_PROCESS_FOR_FOLLOWING);
		if (isRegularFollowing) {
			if (isPostProcess) {
				c.faceUpdate(c.followId + 32768);
				walkTo(other.getLastLocation().getX(), other.getLastLocation().getY());
				c.attr(A.POST_PROCESS_FOR_FOLLOWING, false);
			}
			return;
		}
		boolean sameSpot = (c.absX == otherX && c.absY == otherY);

		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);

		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);
		boolean mageDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 7);

		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0) && mageDistance;
		boolean playerRanging = (c.usingRangeWeapon) && rangeWeaponDistance;
		for (int i = 0; i < Client.OTHER_RANGE_WEAPONS.length; i++) {
			if (c.playerEquipment[c.playerWeapon] == i) {
				playerRanging = true;
			}
		}
		boolean playerBowOrCross = (c.usingBow) && bowDistance;
		c.faceUpdate(c.followId + 32768);
		if (!sameSpot) {
			if (!c.usingSpecial && c.inWild() || c.inPvP() || c.inFFA()) {
				if (c.usingSpecial && (playerRanging || playerBowOrCross)) {
					c.stopMovement();
					return;
				}
				if (playerRanging || playerBowOrCross) {
					// c.stopMovement();
					return;
				}
				if (castingMagic) {
					return;
				}
				if (c.getCombat().usingHally() && hallyDistance) {
					c.stopMovement();
					return;
				}
			}
		}
		if (otherX == c.absX && otherY == c.absY) {
			walkClipped(c);
		} else if (c.isRunning2) {// why 2? no idea lol
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(otherX - 1, otherY + 1);
			}
		}
		c.faceUpdate(c.followId + 32768);// test bug.
	}

	/**
	 * @param xCoord
	 *            the amount to walk to from the player's X axis co-ordination
	 * @param yCoord
	 *            the amount to walk to from the player's Y axis co-ordination
	 */
	public void goTo(int xCoord, int yCoord) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + xCoord;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yCoord;
		l -= c.mapRegionY * 8;
		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	private static void walkClipped(Client c) {
		if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
			c.getPA().walkTo3(-1, 0);
			return;
		} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
			c.getPA().walkTo3(1, 0);
			return;
		} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
			c.getPA().walkTo3(0, -1);
			return;
		} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
			c.getPA().walkTo3(0, 1);
			return;
		}
		c.getPA().walkTo(-1, 0);
	}

	public void walk(Client c) {
		if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
			c.getPA().walkTo3(-1, 0);
			return;
		} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
			c.getPA().walkTo3(1, 0);
			return;
		} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
			c.getPA().walkTo3(0, -1);
			return;
		} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
			c.getPA().walkTo3(0, 1);
			return;
		}
		c.getPA().walkTo3(-1, 0);
	}

	public void followNpc() {
		NPC following = NPCHandler.npcs[c.followId2];
		if (following == null || following.isDead) {
			c.followId2 = 0;
			return;
		}
		if (c.freezeTimer > 0 || c.stunTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		int otherX = following.getX();
		int otherY = following.getY();
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);

		boolean good = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1);
		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 8);
		// I questioned that but decided to ignore it lol
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 8);
		boolean mageDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId2 = 0;
			return;
		}
		if (good) {
			if (otherX != c.getX() && otherY != c.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((c.usingBow || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}
		boolean playerRanging = (c.usingRangeWeapon) && rangeWeaponDistance;
		for (int i = 0; i < Client.OTHER_RANGE_WEAPONS.length; i++) {
			if (c.playerEquipment[c.playerWeapon] == i) {
				playerRanging = true;
			}
		}
		boolean playerBowOrCross = (c.usingBow) && bowDistance;
		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0) && mageDistance;
		if (!sameSpot) {
			if (!c.usingSpecial && c.inWild() || c.inPvP()) {
				if (c.usingSpecial && (playerRanging || playerBowOrCross)) {
					c.stopMovement();
					return;
				}
				if (playerRanging || playerBowOrCross || c.rangeItemUsed == 12926) {
					c.stopMovement();
					return;
				}
				if (castingMagic) {
					return;
				}
				if (c.getCombat().usingHally() && hallyDistance) {
					c.stopMovement();
					return;
				}
			}
		}
		c.faceUpdate(c.followId2);
		if (otherX == c.absX && otherY == c.absY) {
			if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
				c.getPA().walkTo(-1, 0);
			} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
				c.getPA().walkTo(1, 0);
			} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
				c.getPA().walkTo(0, -1);
			} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
				c.getPA().walkTo(0, 1);
			}
		} else if (c.isRunning2 && !withinDistance) {
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(0, getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(0, getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), 0);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), 0);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1),
						getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				playerWalk(0, getMove(c.getY(), otherY - 1));
			} else if (otherY < c.getY() && otherX == c.getX()) {
				playerWalk(0, getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY == c.getY()) {
				playerWalk(getMove(c.getX(), otherX - 1), 0);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1), 0);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1));
			} else if (otherX > c.getX() && otherY > c.getY()) {
				playerWalk(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1));
			} else if (otherX < c.getX() && otherY > c.getY()) {
				playerWalk(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1));
			} else if (otherX > c.getX() && otherY < c.getY()) {
				playerWalk(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY + 1));
			}
		}
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void resetFollow() {
		// System.out.println("Stopped6.");
		c.followingPlayer = false;
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
		c.outStream.createFrame(174);
		c.outStream.writeWord(0);
		c.outStream.writeByte(0);
		c.outStream.writeWord(1);
		c.attr(A.REGULAR_FOLLOWING, false);
	}

	public void walkTo(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	public void agilityWalk(int x, int y) {
		c.addToWalkingQueue(localize(x, c.getMapRegionX()), localize(y, c.getMapRegionY()));
	}

	public int localize(int x, int mapRegion) {
		return x - 8 * mapRegion;
	}

	public void walkTo3(int i, int j) {// i literally dont u
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public boolean canWalk(int moveX, int moveY) {
		if (!clipped || Region.getClipping(c.getX() + moveX, c.getY() + moveY, c.heightLevel, moveX, moveY))
			return true;
		return false;
	}

	public boolean clipped = true;

	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		if (c.freezeTimer > 0)
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (!canWalk(xMove, yMove))
			xMove = 0;

		if (xMove == 0)
			yMove = otherY - c.getY();

		if (!canWalk(xMove, yMove))
			yMove = 0;

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759 && c.playerEquipment[c.playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return c.playerEquipment[c.playerHat] == 4724 && c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730 && c.playerEquipment[c.playerWeapon] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.startAnimation(c.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void sendTotal() {
		int totalLevel = (getLevelForXP(c.playerXP[0]) + getLevelForXP(c.playerXP[1]) + getLevelForXP(c.playerXP[2])
				+ getLevelForXP(c.playerXP[3]) + getLevelForXP(c.playerXP[4]) + getLevelForXP(c.playerXP[5])
				+ getLevelForXP(c.playerXP[6]) + getLevelForXP(c.playerXP[7]) + getLevelForXP(c.playerXP[8])
				+ getLevelForXP(c.playerXP[9]) + getLevelForXP(c.playerXP[10]) + getLevelForXP(c.playerXP[11])
				+ getLevelForXP(c.playerXP[12]) + getLevelForXP(c.playerXP[13]) + getLevelForXP(c.playerXP[14])
				+ getLevelForXP(c.playerXP[15]) + getLevelForXP(c.playerXP[16]) + getLevelForXP(c.playerXP[17])
				+ getLevelForXP(c.playerXP[18]) + getLevelForXP(c.playerXP[19]) + getLevelForXP(c.playerXP[20]));
		sendNewString("Level: " + totalLevel, 13983);
	}

	public void levelUp(int skill) {
		int totalLevel = (getLevelForXP(c.playerXP[0]) + getLevelForXP(c.playerXP[1]) + getLevelForXP(c.playerXP[2])
				+ getLevelForXP(c.playerXP[3]) + getLevelForXP(c.playerXP[4]) + getLevelForXP(c.playerXP[5])
				+ getLevelForXP(c.playerXP[6]) + getLevelForXP(c.playerXP[7]) + getLevelForXP(c.playerXP[8])
				+ getLevelForXP(c.playerXP[9]) + getLevelForXP(c.playerXP[10]) + getLevelForXP(c.playerXP[11])
				+ getLevelForXP(c.playerXP[12]) + getLevelForXP(c.playerXP[13]) + getLevelForXP(c.playerXP[14])
				+ getLevelForXP(c.playerXP[15]) + getLevelForXP(c.playerXP[16]) + getLevelForXP(c.playerXP[17])
				+ getLevelForXP(c.playerXP[18]) + getLevelForXP(c.playerXP[19]) + getLevelForXP(c.playerXP[20]));
		sendNewString("Level: " + totalLevel, 13983);
		switch (skill) {
		case 0:
			sendNewString("Congratulations, you just advanced an attack level!", 6248);
			sendNewString("Your attack level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Attack@bla@.");
					}
				}
			}
			break;

		case 1:
			sendNewString("Congratulations, you just advanced a defence level!", 6254);
			sendNewString("Your defence level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Defence@bla@.");
					}
				}
			}
			break;

		case 2:
			sendNewString("Congratulations, you just advanced a strength level!", 6207);
			sendNewString("Your strength level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Strength@bla@.");
					}
				}
			}
			break;

		case 3:
			sendNewString("Congratulations, you just advanced a hitpoints level!", 6217);
			sendNewString("Your hitpoints level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Hitpoints@bla@.");
					}
				}
			}
			break;

		case 4:
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Ranged@bla@.");
					}
				}
			}
			break;

		case 5:
			sendNewString("Congratulations, you just advanced a prayer level!", 6243);
			sendNewString("Your prayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Prayer@bla@.");
					}
				}
			}
			break;

		case 6:
			sendNewString("Congratulations, you just advanced a magic level!", 6212);
			sendNewString("Your magic level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Magic@bla@.");
					}
				}
			}
			break;

		case 7:
			sendNewString("Congratulations, you just advanced a cooking level!", 6227);
			sendNewString("Your cooking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Cooking@bla@.");
					}
				}
			}
			break;

		case 8:
			sendNewString("Congratulations, you just advanced a woodcutting level!", 4273);
			sendNewString("Your woodcutting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Woodcutting@bla@.");
					}
				}
			}
			break;

		case 9:
			sendNewString("Congratulations, you just advanced a fletching level!", 6232);
			sendNewString("Your fletching level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Fletching@bla@.");
					}
				}
			}
			break;

		case 10:
			sendNewString("Congratulations, you just advanced a fishing level!", 6259);
			sendNewString("Your fishing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Fishing@bla@.");
					}
				}
			}
			break;

		case 11:
			sendNewString("Congratulations, you just advanced a fire making level!", 4283);
			sendNewString("Your firemaking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			sendFrame164(4282);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Firemaking@bla@.");
					}
				}
			}
			break;

		case 12:
			sendNewString("Congratulations, you just advanced a crafting level!", 6264);
			sendNewString("Your crafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Crafting@bla@.");
					}
				}
			}
			break;

		case 13:
			sendNewString("Congratulations, you just advanced a smithing level!", 6222);
			sendNewString("Your smithing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Smithing@bla@.");
					}
				}
			}
			break;

		case 14:
			sendNewString("Congratulations, you just advanced a mining level!", 4417);
			sendNewString("Your mining level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Mining@bla@.");
					}
				}
			}
			break;

		case 15:
			sendNewString("Congratulations, you just advanced a herblore level!", 6238);
			sendNewString("Your herblore level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Herblore@bla@.");
					}
				}
			}
			break;

		case 16:
			sendNewString("Congratulations, you just advanced a agility level!", 4278);
			sendNewString("Your agility level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Agility@bla@.");
					}
				}
			}
			break;

		case 17:
			sendNewString("Congratulations, you just advanced a thieving level!", 4263);
			sendNewString("Your theiving level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Thieving@bla@.");
					}
				}
			}
			break;

		case 18:
			sendNewString("Congratulations, you just advanced a slayer level!", 12123);
			sendNewString("Your slayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Slayer@bla@.");
					}
				}
			}
			break;
		case 19:
			sendNewString("Congratulations, you just advanced a farming level!", 4889);
			sendNewString("Your farming level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4890);
			sendNewString("", 4891);
			c.sendMessage("Congratulations, you just advanced a farming level.");
			sendFrame246(4888, 200, 952);
			sendFrame164(4887);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Farming@bla@.");
					}
				}
			}
			break;

		case 20:
			sendNewString("Congratulations, you just advanced a runecrafting level!", 4268);
			sendNewString("Your runecrafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Client c2 = (Client) PlayerHandler.players[j];
						c2.sendMessage("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
								+ " @bla@just advanced to 99 @blu@Runecrafting@bla@.");
					}
				}
			}
			break;
		}
		if (totalLevel == 2079) {
			c.sendGlobal("[@red@NEWS@bla@]@red@ @bla@The player @blu@" + c.playerName
					+ " @bla@just @blu@Achieved 99 all Skills!");
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
	}

	public void refreshSkill(int i) {
		c.calculateCombatLevel();
		switch (i) {
		case 0:
			sendNewString("" + c.playerLevel[0] + "", 4004);
			sendNewString("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendNewString("" + c.playerXP[0] + "", 4044);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1) + "", 4045);
			break;

		case 1:
			sendNewString("" + c.playerLevel[1] + "", 4008);
			sendNewString("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendNewString("" + c.playerXP[1] + "", 4056);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			sendNewString("" + c.playerLevel[2] + "", 4006);
			sendNewString("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendNewString("" + c.playerXP[2] + "", 4050);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			sendNewString("" + c.playerLevel[3] + "", 4016);
			sendNewString("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendNewString("" + c.playerXP[3] + "", 4080);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			sendNewString("" + c.playerLevel[4] + "", 4010);
			sendNewString("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendNewString("" + c.playerXP[4] + "", 4062);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			sendNewString("" + c.playerLevel[5] + "", 4012);
			sendNewString("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendNewString("" + c.playerXP[5] + "", 4068);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1) + "", 4069);
			sendNewString("" + c.playerLevel[5] + "/" + getLevelForXP(c.playerXP[5]) + "", 687);// Prayer
																								// frame
			break;

		case 6:
			sendNewString("" + c.playerLevel[6] + "", 4014);
			sendNewString("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendNewString("" + c.playerXP[6] + "", 4074);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			sendNewString("" + c.playerLevel[7] + "", 4034);
			sendNewString("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendNewString("" + c.playerXP[7] + "", 4134);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			sendNewString("" + c.playerLevel[8] + "", 4038);
			sendNewString("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendNewString("" + c.playerXP[8] + "", 4146);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			sendNewString("" + c.playerLevel[9] + "", 4026);
			sendNewString("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendNewString("" + c.playerXP[9] + "", 4110);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			sendNewString("" + c.playerLevel[10] + "", 4032);
			sendNewString("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendNewString("" + c.playerXP[10] + "", 4128);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			sendNewString("" + c.playerLevel[11] + "", 4036);
			sendNewString("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendNewString("" + c.playerXP[11] + "", 4140);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			sendNewString("" + c.playerLevel[12] + "", 4024);
			sendNewString("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendNewString("" + c.playerXP[12] + "", 4104);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			sendNewString("" + c.playerLevel[13] + "", 4030);
			sendNewString("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendNewString("" + c.playerXP[13] + "", 4122);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			sendNewString("" + c.playerLevel[14] + "", 4028);
			sendNewString("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendNewString("" + c.playerXP[14] + "", 4116);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			sendNewString("" + c.playerLevel[15] + "", 4020);
			sendNewString("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendNewString("" + c.playerXP[15] + "", 4092);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			sendNewString("" + c.playerLevel[16] + "", 4018);
			sendNewString("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendNewString("" + c.playerXP[16] + "", 4086);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			sendNewString("" + c.playerLevel[17] + "", 4022);
			sendNewString("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendNewString("" + c.playerXP[17] + "", 4098);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			sendNewString("" + c.playerLevel[18] + "", 12166);
			sendNewString("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendNewString("" + c.playerXP[18] + "", 12171);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			sendNewString("" + c.playerLevel[19] + "", 13926);
			sendNewString("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendNewString("" + c.playerXP[19] + "", 13921);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			sendNewString("" + c.playerLevel[20] + "", 4152);
			sendNewString("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendNewString("" + c.playerXP[20] + "", 4157);
			sendNewString("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4158);
			break;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(int amount, int skill) {
		if (c.xpLock == true) {
			return false;
		}
		if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if (c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}
		if (c.playerXP[skill] == 200000000) {
			return false;
		}
		if (amount + c.playerXP[skill] >= 199999999) {
			c.playerXP[skill] = 200000000;
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.sendMessage("[@red@SERVER@bla@]: @blu@" + c.playerName + " has achieved @red@200M @bla@xp in "
							+ getSkillName(skill));
				}
			}
			return false;
		}
		amount *= Config.SERVER_EXP_BONUS;
		if (c.playerRights == 10) {
			amount *= 0.5;
		}
		if (c.getDoubleExp() || Config.DOUBLE_XP && c.playerRights != 10) {
			amount *= 2;
		} else if (Config.DOUBLE_XP && c.playerRights == 10) {
			amount *= 1.2;
		}
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		c.totalXp += amount;
		return true;
	}

	public String getSkillName(int i) {
		if (i < 0 || i > 24)
			return "";
		switch (i) {
		case 0:
			return "Attack";
		case 1:
			return "Defence";
		case 2:
			return "Strength";
		case 3:
			return "Hitpoints";
		case 4:
			return "Range";
		case 5:
			return "Prayer";
		case 6:
			return "Magic";
		case 7:
			return "Cooking";
		case 8:
			return "Woodcutting";
		case 9:
			return "Fletching";
		case 10:
			return "Fishing";
		case 11:
			return "Firemaking";
		case 12:
			return "Crafting";
		case 13:
			return "Smithing";
		case 14:
			return "Mining";
		case 15:
			return "Herblore";
		case 16:
			return "Agility";
		case 17:
			return "Thieving";
		case 18:
			return "Slayer";
		case 19:
			return "Farming";
		case 20:
			return "Runecrafting";
		}
		return "";
	}

	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734,
			4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}
	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcSlot == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(GameObject obj) {
		object(-1, obj.getX(), obj.getY(), obj.getOrientation(), obj.getType());
	}

	public void removeObject(int x, int y) {
		object(-1, x, y, 10, 10);
	}

	public void removeDoor(Door door, int xOff, int yOff) {
		Location loc = door.getLocation();
		object(-1, loc.getX() + xOff, loc.getY() + yOff, door.originalFace, door.type);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Al Kharid", "Karamja", "Mage Bank");
		c.usingGlory = true;
	}

	public void resetVariables() {
		c.getCrafting().resetCrafting();
		c.usingGlory = false;
		c.smeltInterface = false;
		c.smeltType = 0;
		c.smeltAmount = 0;
		c.woodcut[0] = c.woodcut[1] = c.woodcut[2] = 0;
		c.mining[0] = c.mining[1] = c.mining[2] = 0;
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175 && c.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public int antiFire() {
		int toReturn = 0;
		if (c.antiFirePot)
			toReturn++;
		if (c.playerEquipment[c.playerShield] == 1540 || c.prayerActive[12]
				|| c.playerEquipment[c.playerShield] == 11283 || c.prayerActive[12])
			toReturn++;
		return toReturn;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	public void addStarter() {
		if (!Connection.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.getItems().addItem(995, 250000);
			c.getItems().addItem(1381, 1);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(554, 420);
			c.getItems().addItem(558, 200);
			c.getItems().addItem(562, 50);
			c.getItems().addItem(882, 250);
			c.getItems().addItem(1731, 1);
			c.getItems().addItem(9740, 5);
			c.getItems().addItem(380, 100);
			c.getItems().addItem(8013, 15);
			Connection.addIpToStarterList1(PlayerHandler.players[c.playerId].connectedFrom);
			Connection.addIpToStarter1(PlayerHandler.players[c.playerId].connectedFrom);
			c.sendMessage("You have recieved 1 out of 2 starter packages on this IP address.");
		} else if (Connection.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& !Connection.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.getItems().addItem(995, 250000);
			c.getItems().addItem(1381, 1);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(554, 420);
			c.getItems().addItem(558, 200);
			c.getItems().addItem(562, 50);
			c.getItems().addItem(882, 250);
			c.getItems().addItem(1731, 1);
			c.getItems().addItem(9740, 5);
			c.getItems().addItem(380, 100);
			c.getItems().addItem(8013, 15);
			c.sendMessage("You have recieved 2 out of 2 starter packages on this IP address.");
			Connection.addIpToStarterList2(PlayerHandler.players[c.playerId].connectedFrom);
			Connection.addIpToStarter2(PlayerHandler.players[c.playerId].connectedFrom);
		} else if (Connection.hasRecieved1stStarter(PlayerHandler.players[c.playerId].connectedFrom)
				&& Connection.hasRecieved2ndStarter(PlayerHandler.players[c.playerId].connectedFrom)) {
			c.sendMessage("You have already recieved 2 starters!");
		}

	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {

		if (c.inTrade) {
			return;
		}
		switch (itemId) {
		case 12926:
			Blowpipe.handleOperate(c);
			break;
		case 7319:
			c.getPA().startTeleport(1824, 4831, 1, "modern");
			break;
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			handleGlory(itemId);
			break;
		case 11283:
		case 11284:
			if (c.playerIndex > 0) {
				c.getCombat().handleDfs();
			} else if (c.npcIndex > 0) {
				c.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x, y);
		c.lastSpear = System.currentTimeMillis();
	}

	public void getCallisto(int npcIndex, int playerX, int playerY) {
		try {
			if (NPCHandler.npcs[npcIndex] != null) {
				int x = NPCHandler.npcs[npcIndex].absX - playerX;
				int y = NPCHandler.npcs[npcIndex].absY - playerY;
				y = 6;
				moveCheck(x, y);
				c.lastSpear = System.currentTimeMillis();
				c.updateRequired = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void moveCheck(int xMove, int yMove) {
		int random = Misc.random(3);
		if (random == 0) {
			movePlayer(3263, 3816, c.heightLevel);
		} else if (random == 1) {
			movePlayer(3252, 3818, c.heightLevel);
		} else if (random == 2) {
			movePlayer(3222, 3791, c.heightLevel);
		} else if (random == 3) {
			movePlayer(3237, 3778, c.heightLevel);
		}
	}

	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == c.playerId)
				continue;
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune && !c.isPoisoned) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = damage;
			c.isPoisoned = true;
		}
	}

	public static void appendVenom(Client client) {
		client.venomDebuff = true;// i think this should be ok... might h
		Tickable tickable = new Tickable(34) {
			@Override
			public void execute() {
				if (System.currentTimeMillis() - client.lastVenomSip < 300000 || !client.venomDebuff
						|| client.venomDamage >= 22) {
					stop();// word should be good to go
					client.venomDamage = 6;
					return;
				}
				// need to set the opponent
				client.setHitUpdateRequired(true);
				client.setHitDiff(client.venomDamage);// i got it dw.
				client.updateRequired = true;
				client.poisonMask = 3;
				client.dealDamage(client.venomDamage);
				client.getPA().refreshSkill(3);
				client.venomDamage += 2;
			}
		};

		tickable.execute();
		World.getWorld().submit(tickable);
		client.sendMessage("You have been poisoned by venom!");
	}

	public void appendVenom() {
		appendVenom(c);
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), totalCost);
	}

	public void loadQuests() {
		sendNewString("@gre@" + PlayerHandler.getPlayerCount() + " Online @gre@", 29155);
		sendNewString("Your Player", 29161);
		sendNewString("@or1@Pk Points: @gre@" + c.pkPoints, 29162);
		if (c.KC == 0 && c.DC == 0) {
			sendNewString("@or1@KDR:@red@ " + c.KC + "@or1@/@red@" + c.DC, 29163);
		} else {
			sendNewString("@or1@KDR:@gre@ " + c.KC + "@yel@/@gre@" + c.DC, 29163);
		}
		sendNewString("@or1@Donator Points: @gre@" + c.donPoints, 29164);
		if (c.pcPoints == 0) {
			sendNewString("@or1@Pest-Control Points: @red@" + c.pcPoints + "", 29165);
		} else {
			sendNewString("@or1@Pest-Control Points: @gre@" + c.pcPoints + "", 29165);
		}
		sendNewString("@or1@Change Player Title", 29166);
		sendNewString("Other Statistics:", 663);
		sendNewString("@or1@Slayer Points: @gre@" + c.slayerPoints + " ", 29167);
		if (c.slayerTask <= 0) {
			c.getPA().sendNewString("@or1@Task: @gre@Empty ", 29168);
		} else {
			c.getPA().sendNewString(
					"@or1@Task: @gre@" + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + " ",
					29168);
		}
		if (c.time != -1) {
			sendNewString("Double Exp:" + Client.parseTime((long) c.time), 29169);
		} else {
			sendNewString("Double Exp: None", 29169);
		}
		sendNewString("@or1@Vote Points: @gre@" + c.votPoints + " ", 29170);
		/*
		 * sendFrame126("@gre@Time Played:", 29171);
		 * c.getTimePlayed().initiliseNewEnd(); c.timePlayed +=
		 * c.getTimePlayed().getCurrentSession();
		 * c.getTimePlayed().initiliseNewStart();
		 * sendFrame126("@or1@"+c.getTimePlayed().formatPlayersTime(), 29172);
		 */
		sendNewString("@or1@Donator Boss Logs", 29173);
		sendNewString("", 29174);
	}

	public void handleLoginText() {
		/* Modern Teleports */
		c.getPA().sendNewString("Training Teleport", 13037);
		c.getPA().sendNewString("Skilling Teleport", 13047);
		c.getPA().sendNewString("Boss Teleport", 13055);// train
		c.getPA().sendNewString("Wilderness Teleport", 13063);
		c.getPA().sendNewString("Ardougne Teleport", 13071);
		c.getPA().sendNewString("Training Teleport", 1300);
		c.getPA().sendNewString("Skilling Teleport", 1325);
		c.getPA().sendNewString("Boss Teleport", 1350);
		c.getPA().sendNewString("Wilderness Teleport", 1382);
		c.getPA().sendNewString("Cities Teleport", 1415);
		c.getPA().sendNewString("Minigame Teleport", 1454);
		c.getPA().sendNewString("Skilling Teleport", 7457);
		/* End Modern Teleports */

		/* Begin Ancient Teleports */
		c.getPA().sendNewString("Training Teleport", 13037);
		c.getPA().sendNewString("Skilling Teleport", 13047);
		c.getPA().sendNewString("Boss Teleport", 13055);
		c.getPA().sendNewString("Wilderness Teleport", 13063);
		c.getPA().sendNewString("Cities Teleport", 13071);
		c.getPA().sendNewString("Minigame Teleport", 13081);
		/* End Ancient Teleports */
	}

	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getPA().sendFrame36(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getPA().sendFrame36(43, 3);
		} else if (c.fightMode == 2) {
			c.getPA().sendFrame36(43, 1);
		} else if (c.fightMode == 3) {
			c.getPA().sendFrame36(43, 2);
		}
	}

	/*
	 * public void sendString(final String s, final int id) { if
	 * (c.getOutStream() != null && c != null) {
	 * c.getOutStream().createFrameVarSizeWord(126);
	 * c.getOutStream().writeString(s); c.getOutStream().writeWordA(id);
	 * c.getOutStream().endFrameVarSizeWord(); c.flushOutStream(); }
	 * 
	 * }
	 */

	/**
	 * Sets the clan information for the player's clan.
	 */
	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(c.playerName);
		if (!exists || c.clan == null) {
			sendNewString("Join chat", 18135);
			sendNewString("Talking in: Not in chat", 18139);
			sendNewString("Owner: None", 18140);
		}
		if (!exists) {
			sendNewString("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General +";
				} else if (id == 18316) {
					title = "Only me";
				}
				sendNewString(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendNewString("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendNewString("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(c.playerName);
		sendNewString(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE
								&& clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE
								&& clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE
								&& clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE
								&& clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendNewString(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			String lol = "<clan=";
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					this.sendNewString(lol + clan.ranks.get(index) + ">"
							+ clan.rankedMembers.get(index), 18323 + index);
				} else {
					this.sendNewString("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendNewString(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendNewString("", 18424 + index);
				}
			}
		}
	}

	public server.clan.Clan getClan() {
		if (Server.clanManager.clanExists(c.playerName)) {
			return Server.clanManager.getClan(c.playerName);
		}
		return null;
	}

	/* Treasure */
	/*
	 * public static int lowLevelReward[] = { 1077, 1089, 1107, 1125, 1131,
	 * 1129, 1133, 1511, 1168, 1165, 1179, 1195, 1217, 1283, 1297, 1313, 1327,
	 * 1341, 1361, 1367, 1426, 2633, 2635, 2637, 7388, 7386, 7392, 7390, 7396,
	 * 7394, 2631, 7364, 7362, 7368, 7366, 2583, 2585, 2587, 2589, 2591, 2593,
	 * 2595, 2597, 7332, 7338, 7350, 7356, 10396, 10398, 10362, 10364, 10366,
	 * 10368, 10370, 10372, 10374, 10376, 10378, 10380, 10382, 10384, 10386,
	 * 10388, 10390, 10392, 10394, 10396 }; public static int
	 * mediemLevelReward[] = { 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613,
	 * 7334, 7340, 7346, 7352, 7358, 7319, 7321, 7323, 7325, 7327, 7372, 7370,
	 * 7380, 7378, 2645, 2647, 2649, 2577, 2579, 1073, 1091, 1099, 1111, 1135,
	 * 1124, 1145, 1161, 1169, 1183, 1199, 1211, 1245, 1271, 1287, 1301, 1317,
	 * 1332, 1357, 1371, 1430, 6916, 6918, 6920, 6922, 6924, 10400, 10402,
	 * 10416, 10418, 10420, 10422, 10436, 10438, 10446, 10448, 10450, 10452,
	 * 10454, 10456, 6889, 10396, 10398, 10362, 10364, 10366, 10368, 10370,
	 * 10372, 10374, 10376, 10378, 10380, 10382, 10384, 10386, 10388, 10390,
	 * 10392, 10394, 10396, 10398, 10400, 10402, 10404, 10406, 10408, 10410,
	 * 10412, 10414, 10416, 10418, 10420, 10422, 10424, 10426, 10428, 10430,
	 * 10432, 10434, 10436, 10438, 10440, 10442, 10444, 10446, 10448, 10450,
	 * 10452, 10454, 10456, 10458, 10460, 10462, 10464, 10466, 10468, 10470,
	 * 10472, 10474 }; public static int highLevelReward[] = { 1079, 1093, 1113,
	 * 1127, 1147, 1163, 1185, 1201, 1275, 1303, 1319, 1333, 1359, 1373, 2491,
	 * 2497, 2503, 861, 859, 2581, 2577, 2651, 1079, 1093, 1113, 1127, 1147,
	 * 1163, 1185, 1201, 1275, 1303, 1319, 1333, 1359, 1373, 2491, 2497, 2503,
	 * 861, 859, 2581, 2577, 2651, 2615, 2617, 2619, 2621, 2623, 2625, 2627,
	 * 2629, 2639, 2641, 2643, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665,
	 * 2667, 2669, 2671, 2673, 2675, 7342, 7348, 7454, 7460, 7374, 7376, 7382,
	 * 7384, 7398, 7399, 7400, 3481, 3483, 3485, 3486, 3488, 1079, 1093, 1113,
	 * 1127, 1148, 1164, 1185, 1201, 1213, 1247, 1275, 1289, 1303, 1319, 1333,
	 * 1347, 1359, 1374, 1432, 2615, 2617, 2619, 2621, 2623, 10368, 10376,
	 * 10384, 10370, 10378, 10386, 10372, 10380, 10374, 10382, 10390, 10470,
	 * 10472, 10474, 10440, 10442, 10444, 6914, 12530, 12528, 12534, 12536,
	 * 12612, 12496, 12492, 12494, 12490, 12504, 12500, 12502, 12498, 12512,
	 * 12508, 12510, 12506, 12373, 12460, 12462, 12464, 12466, 12468, 12470,
	 * 12472, 12474, 12476, 12478, 12480, 12482, 12484, 12486, 12488 }; public
	 * static int rareReward[] = {10330, 10332, 10334, 10336, 10344,}; // 3A
	 * ITEMS OR EXTREMELY RARE ITEMS
	 */
	/* Treasure */
	public static int lowLevelReward[] = { 1077, 1089, 1107, 1125, 1131, 1129, 1133, 1511, 1168, 1165, 1179, 1195, 1217,
			1283, 1297, 1313, 1327, 1341, 1361, 1367, 1426, 2633, 2635, 2637, 7388, 7386, 7392, 7390, 7396, 7394, 2631,
			7364, 7362, 7368, 7366, 2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 7332, 7338, 7350, 7356, 10396,
			10398, 10362, 10364, 10366, 10368, 10370, 10372, 10374, 10376, 10378, 10380, 10382, 10384, 10386, 10388,
			10390, 10392, 10394, 10396, 12205, 12207, 12209, 12211, 12213, 12215, 12217, 12219, 12221, 12223, 12225,
			12227, 12229, 12231, 12233, 12235, 12237, 12239, 12241, 12243, 12445, 12447, 12449, 12451, 12453, 12455,
			12518, 12520, 12522 };
	public static int mediemLevelReward[] = { 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 7340, 7346, 7352,
			7358, 7319, 7321, 7323, 7325, 7327, 7372, 7370, 7380, 7378, 2645, 2647, 2649, 2577, 2579, 1073, 1091, 1099,
			1111, 1135, 1124, 1145, 1161, 1169, 1183, 1199, 1211, 1245, 1271, 1287, 1301, 1317, 1332, 1357, 1371, 1430,
			6916, 6918, 6920, 6922, 6924, 10400, 10402, 10416, 10418, 10420, 10422, 10436, 10438, 10446, 10448, 10450,
			10452, 10454, 10456, 6889, 10396, 10398, 10362, 10364, 10366, 10368, 10370, 10372, 10374, 10376, 10378,
			10380, 10382, 10384, 10386, 10388, 10390, 10392, 10394, 10396, 10398, 10400, 10402, 10404, 10406, 10408,
			10410, 10412, 10414, 10416, 10418, 10420, 10422, 10424, 10426, 10428, 10430, 10432, 10434, 10436, 10438,
			10440, 10442, 10444, 10446, 10448, 10450, 10452, 10454, 10456, 10458, 10460, 10462, 10464, 10466, 10468,
			10470, 10472, 10474, 12253, 12255, 12259, 12261, 12263, 12265, 12267, 12269, 12271, 12273, 12275, 12193,
			12195, 12197, 12199, 12201, 12203, 12598, 12277, 12279, 12281, 12283, 12285, 12287, 12289, 12291, 12293,
			12295, 12315, 12317, 12339, 12341, 12343, 12345, 12347, 12349, 12335, 12524, 12363, 12365, };
	public static int highLevelReward[] = { 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 1275, 1303, 1319, 1333,
			1359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201,
			1275, 1303, 1319, 1333, 1359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 2615, 2617, 2619, 2621,
			2623, 2625, 2627, 2629, 2639, 2641, 2643, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671,
			2673, 2675, 7342, 7348, 7454, 7460, 7374, 7376, 7382, 7384, 7398, 7399, 7400, 3481, 3483, 3485, 3486, 3488,
			1079, 1093, 1113, 1127, 1148, 1164, 1185, 1201, 1213, 1247, 1275, 1289, 1303, 1319, 1333, 1347, 1359, 1374,
			1432, 2615, 2617, 2619, 2621, 2623, 10368, 10376, 10384, 10370, 10378, 10386, 10372, 10380, 10374, 10382,
			10390, 10470, 10472, 10474, 10440, 10442, 10444, 6914, 12530, 12528, 12534, 12536, 12612, 12496, 12492,
			12494, 12490, 12504, 12500, 12502, 12498, 12512, 12508, 12510, 12506, 12373, 12460, 12462, 12464, 12466,
			12468, 12470, 12472, 12474, 12476, 12478, 12480, 12482, 12484, 12486, 12488, 12526, 12532, 12432, 12441,
			12443, 12351, 12367, 12369, 12337, 12355, 12391, 12389, 12353, 12540, 12381, 12383, 12385, 12387, 12357,
			12514, 11919 };
	public static int rareReward[] = { 10330, 10332, 10334, 10336, 10344, 12371, 10338, 10340, 10342, 10346, 10348,
			10350, 10352, 12422, 12424, 12426, 12437 }; // 3A ITEMS OR EXTREMELY
														// RARE ITEMS
	public static int lowLevelStacks[] = { 995, 380, 561, 886, };
	public static int mediumLevelStacks[] = { 995, 374, 561, 563, 890, };
	public static int highLevelStacks[] = { 995, 386, 561, 563, 560, 892, 398 };

	public static void addClueReward(Client c, int clueLevel) {
		int chanceReward = Misc.random(2);
		if (c.playerRights == 3) {
			c.sendMessage("Low Length:" + lowLevelReward.length);
			c.sendMessage("Med Length:" + mediemLevelReward.length);
			c.sendMessage("High Length:" + highLevelReward.length);
			c.sendMessage("Rare Length:" + rareReward.length);
		}
		if (clueLevel == 0) {
			switch (chanceReward) {
			case 0:
				displayReward(c, lowLevelReward[Misc.random(96)], 1, lowLevelReward[Misc.random(16)], 1,
						lowLevelStacks[Misc.random(3)], 1 + Misc.random(150));
				break;
			case 1:
				displayReward(c, lowLevelReward[Misc.random(96)], 1, lowLevelStacks[Misc.random(3)],
						1 + Misc.random(200), -1, 1);
				break;
			case 2:
				displayReward(c, lowLevelReward[Misc.random(96)], 1, lowLevelReward[Misc.random(16)], 1, -1, 1);
				break;
			}
			c.easyClue++;
			c.sendMessage("@gre@You have completed " + c.easyClue + " Easy Treasure Trails");
		} else if (clueLevel == 1) {
			switch (chanceReward) {
			case 0:
				displayReward(c, mediemLevelReward[Misc.random(167)], 1, mediemLevelReward[Misc.random(13)], 1,
						mediumLevelStacks[Misc.random(4)], 1 + Misc.random(200));
				break;
			case 1:
				displayReward(c, mediemLevelReward[Misc.random(167)], 1, mediumLevelStacks[Misc.random(4)],
						1 + Misc.random(100), -1, 1);
				break;
			case 2:
				displayReward(c, mediemLevelReward[Misc.random(167)], 1, mediemLevelReward[Misc.random(13)], 1, -1, 1);
				break;
			}
			c.medClue++;
			c.sendMessage("@bro@You have completed " + c.medClue + " Medium Treasure Trails");
		} else if (clueLevel == 2) {
			switch (chanceReward) {
			case 0:
				if (Misc.random(400) == 1) {
					displayReward(c, rareReward[Misc.random(17)], 1, highLevelReward[Misc.random(60)], 1,
							highLevelStacks[Misc.random(5)], 1 + Misc.random(200));
				} else {
					displayReward(c, highLevelReward[Misc.random(180)], 1, highLevelReward[Misc.random(60)], 1,
							highLevelStacks[Misc.random(5)], 1 + Misc.random(200));
				}
				break;
			case 1:
				displayReward(c, highLevelReward[Misc.random(52)], 1, highLevelStacks[Misc.random(5)],
						1 + Misc.random(200), -1, 1);
				break;
			case 2:
				if (Misc.random(400) == 1) {
					displayReward(c, rareReward[Misc.random(17)], 1, highLevelReward[Misc.random(60)], 1, -1, 1);
				} else {
					displayReward(c, highLevelReward[Misc.random(180)], 1, highLevelReward[Misc.random(60)], 1, -1, 1);
				}
				break;
			}
			c.hardClue++;
			c.sendMessage("@red@You have completed " + c.hardClue + " Hard Treasure Trails");
		}
	}

	public static void displayReward(Client c, int item, int amount, int item2, int amount2, int item3, int amount3) {
		int[] items = { item, item2, item3 };
		int[] amounts = { amount, amount2, amount3 };
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(6963);
		c.outStream.writeWord(items.length);
		for (int i = 0; i < items.length; i++) {
			if (c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2(amounts[i]);
			} else {
				c.outStream.writeByte(amounts[i]);
			}
			if (items[i] > 0) {
				c.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
		c.getItems().addItem(item, amount);
		c.getItems().addItem(item2, amount2);
		c.getItems().addItem(item3, amount3);
		if (c.playerName.equals("Clank1337")) {
			c.sendMessage("Item 1: " + item + "," + amount + " Item 2: " + item2 + "," + amount2 + " Item 3: " + item3
					+ "," + amount3);
		}
		c.getPA().showInterface(6960);
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	public int backupInvItems[] = new int[28];
	public int backupInvItemsN[] = new int[28];

	public boolean viewingOther = false;

	public void otherInv(Client c, Client o) {
		if (o == c || o == null || c == null)
			return;
		if (!viewingOther) {
			int[] backupItems = c.playerItems;
			int[] backupItemsN = c.playerItemsN;
			c.playerItems = o.playerItems;
			c.playerItemsN = o.playerItemsN;

			c.getItems().resetItems(3214);

			c.playerItems = backupItems;
			c.playerItemsN = backupItemsN;
		}
	}

	public int backupItems[] = new int[Config.BANK_SIZE];
	public int backupItemsN[] = new int[Config.BANK_SIZE];

	public void viewOtherBank(Client c, Client o) {
		if (o == c || o == null || c == null) {
			return;
		}

		for (int i = 0; i < o.bankItems.length; i++) {
			backupItems[i] = c.bankItems[i];
			backupItemsN[i] = c.bankItemsN[i];
			c.bankItemsN[i] = o.bankItemsN[i];
			c.bankItems[i] = o.bankItems[i];
		}
		openUpBank(0);

		for (int i = 0; i < o.bankItems.length; i++) {
			c.bankItemsN[i] = backupItemsN[i];
			c.bankItems[i] = backupItems[i];
		}
	}

	public void sendStatement(String s) {
		sendNewString(s, 357);
		sendNewString("Click here to continue", 358);
		sendFrame164(356);
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void resetTzhaar() {
		c.waveId = -1;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
		movePlayer(2438, 5168, 0);
	}

	public void enterCaves() {
		c.getPA().movePlayer(2413, 5117, c.playerId * 4);
		c.waveId = 0;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer e) {
				Server.fightCaves.spawnNextWave((Client) PlayerHandler.players[c.playerId]);
				e.stop();
			}
		}, 10000);
	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client p = (Client) PlayerHandler.players[i];
				if (p.WithinDistance(c.absX, c.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == c.heightLevel) {
						if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}

	/* BANK TABS */

	public int tempItems[] = new int[Config.BANK_SIZE];
	public int tempItemsN[] = new int[Config.BANK_SIZE];
	public int tempItemsT[] = new int[Config.BANK_SIZE];
	public int tempItemsS[] = new int[Config.BANK_SIZE];

	public static void itemOnInterface(Client c, int frame, int slot, int id, int amount) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(frame);
		c.getOutStream().writeByte(slot);
		c.getOutStream().writeWord(id + 1);
		c.getOutStream().writeByte(255);
		c.getOutStream().writeDWord(amount);
		c.getOutStream().endFrameVarSizeWord();
	}

	public void setScrollPos(int interfaceId, int scrollPos) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(79);
			c.outStream.writeWordBigEndian(interfaceId);
			c.outStream.writeWordA(scrollPos);
		}
	}

	public int getBankItems(int tab) {
		int ta = 0, tb = 0, tc = 0, td = 0, te = 0, tf = 0, tg = 0, th = 0, ti = 0;
		for (int i = 0; i < c.bankItems.length; i++)
			if (c.bankItems[i] > 0)
				ta++;
		for (int i = 0; i < c.bankItems1.length; i++)
			if (c.bankItems1[i] > 0)
				tb++;
		for (int i = 0; i < c.bankItems2.length; i++)
			if (c.bankItems2[i] > 0)
				tc++;
		for (int i = 0; i < c.bankItems3.length; i++)
			if (c.bankItems3[i] > 0)
				td++;
		for (int i = 0; i < c.bankItems4.length; i++)
			if (c.bankItems4[i] > 0)
				te++;
		for (int i = 0; i < c.bankItems5.length; i++)
			if (c.bankItems5[i] > 0)
				tf++;
		for (int i = 0; i < c.bankItems6.length; i++)
			if (c.bankItems6[i] > 0)
				tg++;
		for (int i = 0; i < c.bankItems7.length; i++)
			if (c.bankItems7[i] > 0)
				th++;
		for (int i = 0; i < c.bankItems8.length; i++)
			if (c.bankItems8[i] > 0)
				ti++;
		if (tab == 0)
			return ta;
		if (tab == 1)
			return tb;
		if (tab == 2)
			return tc;
		if (tab == 3)
			return td;
		if (tab == 4)
			return te;
		if (tab == 5)
			return tf;
		if (tab == 6)
			return tg;
		if (tab == 7)
			return th;
		if (tab == 8)
			return ti;
		return ta + tb + tc + td + te + tf + tg + th + ti; // return total

	}

	public int getTabCount() {
		// count tabs
		int tabs = 0;
		if (!checkEmpty(c.bankItems1))
			tabs++;
		if (!checkEmpty(c.bankItems2))
			tabs++;
		if (!checkEmpty(c.bankItems3))
			tabs++;
		if (!checkEmpty(c.bankItems4))
			tabs++;
		if (!checkEmpty(c.bankItems5))
			tabs++;
		if (!checkEmpty(c.bankItems6))
			tabs++;
		if (!checkEmpty(c.bankItems7))
			tabs++;
		if (!checkEmpty(c.bankItems8))
			tabs++;
		return tabs;
	}

	public boolean checkEmpty(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0)
				return false;
		}
		return true;
	}

	public void sendTabs() {
		// remove empty tab
		Client player = c;
		boolean moveRest = false;
		if (checkEmpty(player.bankItems1)) { // tab 1 empty
			player.bankItems1 = Arrays.copyOf(player.bankItems2, player.bankingItems.length);
			player.bankItems1N = Arrays.copyOf(player.bankItems2N, player.bankingItems.length);
			player.bankItems2 = new int[Config.BANK_SIZE];
			player.bankItems2N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems2) || moveRest) {
			player.bankItems2 = Arrays.copyOf(player.bankItems3, player.bankingItems.length);
			player.bankItems2N = Arrays.copyOf(player.bankItems3N, player.bankingItems.length);
			player.bankItems3 = new int[Config.BANK_SIZE];
			player.bankItems3N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems3) || moveRest) {
			player.bankItems3 = Arrays.copyOf(player.bankItems4, player.bankingItems.length);
			player.bankItems3N = Arrays.copyOf(player.bankItems4N, player.bankingItems.length);
			player.bankItems4 = new int[Config.BANK_SIZE];
			player.bankItems4N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems4) || moveRest) {
			player.bankItems4 = Arrays.copyOf(player.bankItems5, player.bankingItems.length);
			player.bankItems4N = Arrays.copyOf(player.bankItems5N, player.bankingItems.length);
			player.bankItems5 = new int[Config.BANK_SIZE];
			player.bankItems5N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems5) || moveRest) {
			player.bankItems5 = Arrays.copyOf(player.bankItems6, player.bankingItems.length);
			player.bankItems5N = Arrays.copyOf(player.bankItems6N, player.bankingItems.length);
			player.bankItems6 = new int[Config.BANK_SIZE];
			player.bankItems6N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems6) || moveRest) {
			player.bankItems6 = Arrays.copyOf(player.bankItems7, player.bankingItems.length);
			player.bankItems6N = Arrays.copyOf(player.bankItems7N, player.bankingItems.length);
			player.bankItems7 = new int[Config.BANK_SIZE];
			player.bankItems7N = new int[Config.BANK_SIZE];
			moveRest = true;
		}
		if (checkEmpty(player.bankItems7) || moveRest) {
			player.bankItems7 = Arrays.copyOf(player.bankItems8, player.bankingItems.length);
			player.bankItems7N = Arrays.copyOf(player.bankItems8N, player.bankingItems.length);
			player.bankItems8 = new int[Config.BANK_SIZE];
			player.bankItems8N = new int[Config.BANK_SIZE];
		}
		if (player.bankingTab > getTabCount())
			player.bankingTab = getTabCount();
		player.getPA().sendNewString(Integer.toString(getTabCount()), 27001);
		player.getPA().sendNewString(Integer.toString(player.bankingTab), 27002);
		PlayerAssistant.itemOnInterface(player, 22035, 0, getInterfaceModel(0, player.bankItems1, player.bankItems1N),
				getAmount(player.bankItems1[0], player.bankItems1N[0]));
		PlayerAssistant.itemOnInterface(player, 22036, 0, getInterfaceModel(0, player.bankItems2, player.bankItems2N),
				getAmount(player.bankItems2[0], player.bankItems2N[0]));
		PlayerAssistant.itemOnInterface(player, 22037, 0, getInterfaceModel(0, player.bankItems3, player.bankItems3N),
				getAmount(player.bankItems3[0], player.bankItems3N[0]));
		PlayerAssistant.itemOnInterface(player, 22038, 0, getInterfaceModel(0, player.bankItems4, player.bankItems4N),
				getAmount(player.bankItems4[0], player.bankItems4N[0]));
		PlayerAssistant.itemOnInterface(player, 22039, 0, getInterfaceModel(0, player.bankItems5, player.bankItems5N),
				getAmount(player.bankItems5[0], player.bankItems5N[0]));
		PlayerAssistant.itemOnInterface(player, 22040, 0, getInterfaceModel(0, player.bankItems6, player.bankItems6N),
				getAmount(player.bankItems6[0], player.bankItems6N[0]));
		PlayerAssistant.itemOnInterface(player, 22041, 0, getInterfaceModel(0, player.bankItems7, player.bankItems7N),
				getAmount(player.bankItems7[0], player.bankItems7N[0]));
		PlayerAssistant.itemOnInterface(player, 22042, 0, getInterfaceModel(0, player.bankItems8, player.bankItems8N),
				getAmount(player.bankItems8[0], player.bankItems8N[0]));
		player.getPA().sendNewString("1", 27000);
	}

	public static int getAmount(int itemId, int amount) {
		/*
		 * if (itemId <= 0) return 1; if (Item.itemStackable[itemId]) return
		 * amount;
		 */
		return 1;
	}

	public int getInterfaceModel(int slot, int[] array, int[] arrayN) {
		int model = array[slot] - 1;
		if (model == 995) {
			if (arrayN[slot] > 9999) {
				model = 1004;
			} else if (arrayN[slot] > 999) {
				model = 1003;
			} else if (arrayN[slot] > 249) {
				model = 1002;
			} else if (arrayN[slot] > 99) {
				model = 1001;
			} else if (arrayN[slot] > 24) {
				model = 1000;
			} else if (arrayN[slot] > 4) {
				model = 999;
			} else if (arrayN[slot] > 3) {
				model = 998;
			} else if (arrayN[slot] > 2) {
				model = 997;
			} else if (arrayN[slot] > 1) {
				model = 996;
			}
		}
		return model;
	}

	public static int getInterfaceModel(int slot) {
		int model = slot - 1;
		return model;
	}

	public void constructMapRegion(Palette palette) {
		c.outStream.createFrameVarSizeWord(241);
		c.outStream.writeWordA(c.mapRegionY + 6);
		c.outStream.initBitAccess();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					PaletteTile tile = palette.getTile(x, y, z);
					c.getOutStream().writeBits(1, tile != null ? 1 : 0);
					if (tile != null) {
						c.getOutStream().writeBits(26,
								tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
					}
				}
			}
		}
		c.getOutStream().finishBitAccess();
		c.getOutStream().writeWord(c.mapRegionX + 6);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void sendClan(String name, String message, String clan, int rights) {
		c.outStream.createFrameVarSizeWord(217);
		c.outStream.writeString(name);
		c.outStream.writeString(Misc.formatPlayerName(message));
		c.outStream.writeString(clan);
		c.outStream.writeWord(rights);
		c.outStream.endFrameVarSize();
	}

	public void movePlayerDiagonal(int i) {
		Client other = (Client) PlayerHandler.players[i];
		boolean hasMoved = false;
		int otherX = other.getX();
		int otherY = other.getY();
		if (c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (c.getX() != other.getX() && c.getY() != other.getY()) {
				if (c.getX() > other.getX() && !hasMoved) {
					if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
						hasMoved = true;
						goTo(-1, 0);
					}
				} else if (c.getX() < other.getX() && !hasMoved) {
					if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
						hasMoved = true;
						goTo(1, 0);
					}
				}

				if (c.getY() > other.getY() && !hasMoved) {
					if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
						hasMoved = true;
						goTo(0, -1);
					}
				} else if (c.getY() < other.getY() && !hasMoved) {
					if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
						hasMoved = true;
						goTo(0, 1);
					}
				}
			}
		}
		hasMoved = false;
	}

	public double getAgilityRunRestore() {
		return 2260 - (c.playerLevel[16] * 10);
	}

	public boolean inAttackZone(Client c) {
		return (c.inPvP() || c.inWild());
	}

	public void destroyInterface(int itemId) {// Destroy item created by Remco
		itemId = c.droppedItem;// The item u are dropping
		String itemName = c.getItems().getItemName(c.droppedItem);
		String[][] info = { // The info the dialogue gives
				{ "Are you sure you want to drop this item?", "14174" }, { "Yes.", "14175" }, { "No.", "14176" },
				{ "", "14177" }, { "This item is valuable, you will not", "14182" },
				{ "get it back once clicked Yes.", "14183" }, { itemName, "14184" } };
		sendFrame34(itemId, 0, 14171, 1);
		for (int i = 0; i < info.length; i++)
			sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
		sendFrame164(14170);
	}

	public void destroyItem(int itemId) {
		itemId = c.droppedItem;
		String itemName = c.getItems().getItemName(itemId);
		c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId),
				c.playerItemsN[c.getItems().getItemSlot(itemId)]);
		c.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		removeAllWindows();
	}

	public int getCarriedWealth() {
		int toReturn = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0 && Server.itemHandler.ItemList[c.playerEquipment[i]] != null)
				toReturn += (Server.itemHandler.ItemList[c.playerEquipment[i]].ShopValue * c.playerEquipmentN[i]);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > 0 && Server.itemHandler.ItemList[c.playerItems[i]] != null)
				toReturn += (Server.itemHandler.ItemList[c.playerItems[i]].ShopValue * c.playerItemsN[i]);
		}
		return toReturn;
	}

	public void writePMLog(Client o, String data) {
		String filePath;
		BufferedWriter bw = null;

		try {
			String dataToWrite;

			if (data.contains("join")) {
				dataToWrite = "[" + c.connectedFrom + "]: Other Player " + o.playerName + ": " + data + " ";
				filePath = System.getProperty("user.home") + "/Dropbox/Server Logs/pm/advertisers/" + c.playerName
						+ ".txt";
			} else {
				dataToWrite = "[" + c.connectedFrom + "]: Other Player " + o.playerName + ": " + data + " ";
				filePath = System.getProperty("user.home") + "/Dropbox/Server Logs/pm/" + c.playerName + ".txt";
			}
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write(dataToWrite);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}

	public void fadeInterface() {
		showInterface(18460);
		World.getWorld().submit(new Tickable(3) {
			private boolean faded;

			public void execute() {
				if (faded) {
					closeAllWindows();
					stop();
					return;
				}
				showInterface(18452);
				setTickDelay(2);
				faded = true;
			}
		});
	}

	public boolean inZulrah() {
		// return c.absX >= 2279 && c.absX <= 2255 && c.absY >= 3081 && c.absY
		// <= 3066;
		return c.absX >= 2255 && c.absX <= 2279 && c.absY >= 3066 && c.absY <= 3081;
	}

	public void stun(int stunTime) {
		c.stunTimer = stunTime;
		c.gfx100(80);
	}

}
