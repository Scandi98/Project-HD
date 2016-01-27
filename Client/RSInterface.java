public final class RSInterface {

	public void swapInventoryItems(int i, int j) {
		int k = inv[i];
		inv[i] = inv[j];
		inv[j] = k;
		k = invStackSizes[i];
		invStackSizes[i] = invStackSizes[j];
		invStackSizes[j] = k;
	}

	public Sprite enabledHover;

	public static void unpack(StreamLoader streamLoader,
			TextDrawingArea textDrawingAreas[], StreamLoader streamLoader_1) {
		aMRUNodes_238 = new MRUNodes(50000);
		Stream stream = new Stream(streamLoader.getDataForName("data"));
		int i = -1;
		stream.readUnsignedWord();
		interfaceCache = new RSInterface[61000];
		while (stream.currentOffset < stream.buffer.length) {
			int k = stream.readUnsignedWord();
			if (k == 65535) {
				i = stream.readUnsignedWord();
				k = stream.readUnsignedWord();
			}
			RSInterface rsInterface = interfaceCache[k] = new RSInterface();
			rsInterface.id = k;
			rsInterface.parentID = i;
			rsInterface.type = stream.readUnsignedByte();
			rsInterface.atActionType = stream.readUnsignedByte();
			rsInterface.contentType = stream.readUnsignedWord();
			rsInterface.width = stream.readUnsignedWord();
			rsInterface.height = stream.readUnsignedWord();
			rsInterface.aByte254 = (byte) stream.readUnsignedByte();
			rsInterface.hoverType = stream.readUnsignedByte();
			if (rsInterface.hoverType != 0)
				rsInterface.hoverType = (rsInterface.hoverType - 1 << 8)
						+ stream.readUnsignedByte();
			else
				rsInterface.hoverType = -1;
			int i1 = stream.readUnsignedByte();
			if (i1 > 0) {
				rsInterface.anIntArray245 = new int[i1];
				rsInterface.anIntArray212 = new int[i1];
				for (int j1 = 0; j1 < i1; j1++) {
					rsInterface.anIntArray245[j1] = stream.readUnsignedByte();
					rsInterface.anIntArray212[j1] = stream.readUnsignedWord();
				}

			}
			int k1 = stream.readUnsignedByte();
			if (k1 > 0) {
				rsInterface.valueIndexArray = new int[k1][];
				for (int l1 = 0; l1 < k1; l1++) {
					int i3 = stream.readUnsignedWord();
					rsInterface.valueIndexArray[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++)
						rsInterface.valueIndexArray[l1][l4] = stream
								.readUnsignedWord();

				}

			}
			if (rsInterface.type == 0) {
				rsInterface.drawsTransparent = false;
				rsInterface.scrollMax = stream.readUnsignedWord();
				rsInterface.isMouseoverTriggered = stream.readUnsignedByte() == 1;
				int i2 = stream.readUnsignedWord();
				rsInterface.children = new int[i2];
				rsInterface.childX = new int[i2];
				rsInterface.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					rsInterface.children[j3] = stream.readUnsignedWord();
					rsInterface.childX[j3] = stream.readSignedWord();
					rsInterface.childY[j3] = stream.readSignedWord();
				}
			}
			if (rsInterface.type == 1) {
				stream.readUnsignedWord();
				stream.readUnsignedByte();
			}
			if (rsInterface.type == 2) {
				rsInterface.inv = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.aBoolean259 = stream.readUnsignedByte() == 1;
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.usableItemInterface = stream.readUnsignedByte() == 1;
				rsInterface.aBoolean235 = stream.readUnsignedByte() == 1;
				rsInterface.invSpritePadX = stream.readUnsignedByte();
				rsInterface.invSpritePadY = stream.readUnsignedByte();
				rsInterface.spritesX = new int[20];
				rsInterface.spritesY = new int[20];
				rsInterface.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = stream.readUnsignedByte();
					if (k3 == 1) {
						rsInterface.spritesX[j2] = stream.readSignedWord();
						rsInterface.spritesY[j2] = stream.readSignedWord();
						String s1 = stream.readString();
						if (streamLoader_1 != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							rsInterface.sprites[j2] = method207(
									Integer.parseInt(s1.substring(i5 + 1)),
									streamLoader_1, s1.substring(0, i5));
						}
					}
				}
				rsInterface.actions = new String[5];
				for (int l3 = 0; l3 < 5; l3++) {
					rsInterface.actions[l3] = stream.readString();
					if (rsInterface.actions[l3].length() == 0)
						rsInterface.actions[l3] = null;
					if (rsInterface.parentID == 1644)
						rsInterface.actions[2] = "Operate";

					if (rsInterface.parentID == 3824)
						rsInterface.actions[4] = "Buy 100";
				}
			}
			if (rsInterface.type == 3)
				rsInterface.aBoolean227 = stream.readUnsignedByte() == 1;
			if (rsInterface.type == 4 || rsInterface.type == 1) {
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int k2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[k2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
			}
			if (rsInterface.type == 4) {
				rsInterface.message = stream.readString().replaceAll(
						"RuneScape", "" + Client.clientName);
				rsInterface.aString228 = stream.readString();
			}
			if (rsInterface.type == 1 || rsInterface.type == 3
					|| rsInterface.type == 4)
				rsInterface.textColor = stream.readDWord();
			if (rsInterface.type == 3 || rsInterface.type == 4) {
				rsInterface.anInt219 = stream.readDWord();
				rsInterface.anInt216 = stream.readDWord();
				rsInterface.anInt239 = stream.readDWord();
			}
			if (rsInterface.type == 5) {
				rsInterface.drawsTransparent = false;
				String s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int i4 = s.lastIndexOf(",");
					rsInterface.sprite1 = method207(
							Integer.parseInt(s.substring(i4 + 1)),
							streamLoader_1, s.substring(0, i4));
				}
				s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int j4 = s.lastIndexOf(",");
					rsInterface.sprite2 = method207(
							Integer.parseInt(s.substring(j4 + 1)),
							streamLoader_1, s.substring(0, j4));
				}
			}
			if (rsInterface.type == 6) {
				int l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt233 = 1;
					rsInterface.mediaID = (l - 1 << 8)
							+ stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt255 = 1;
					rsInterface.anInt256 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt257 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				else
					rsInterface.anInt257 = -1;
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt258 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				else
					rsInterface.anInt258 = -1;
				rsInterface.modelZoom = stream.readUnsignedWord();
				rsInterface.modelRotation1 = stream.readUnsignedWord();
				rsInterface.modelRotation2 = stream.readUnsignedWord();
			}
			if (rsInterface.type == 7) {
				rsInterface.inv = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int l2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[l2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
				rsInterface.textColor = stream.readDWord();
				rsInterface.invSpritePadX = stream.readSignedWord();
				rsInterface.invSpritePadY = stream.readSignedWord();
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.actions = new String[5];
				for (int k4 = 0; k4 < 5; k4++) {
					rsInterface.actions[k4] = stream.readString();
					if (rsInterface.actions[k4].length() == 0)
						rsInterface.actions[k4] = null;
				}

			}
			if (rsInterface.atActionType == 2 || rsInterface.type == 2) {
				rsInterface.selectedActionName = stream.readString();
				rsInterface.spellName = stream.readString();
				rsInterface.spellUsableOn = stream.readUnsignedWord();
			}

			if (rsInterface.type == 8)
				rsInterface.message = stream.readString();

			if (rsInterface.atActionType == 1 || rsInterface.atActionType == 4
					|| rsInterface.atActionType == 5
					|| rsInterface.atActionType == 6) {
				rsInterface.tooltip = stream.readString();
				if (rsInterface.tooltip.length() == 0) {
					if (rsInterface.atActionType == 1)
						rsInterface.tooltip = "Ok";
					if (rsInterface.atActionType == 4)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 5)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 6)
						rsInterface.tooltip = "Continue";
				}
			}
		}
		aClass44 = streamLoader;
		// Skill(textDrawingAreas);
		skillInterface(textDrawingAreas);
		constructLunar();
		prayerTab(textDrawingAreas);
		emoteTab();
		clanChatTab(textDrawingAreas);
		clanChatSetup(textDrawingAreas);
		configureLunar(textDrawingAreas);
		PVPInterface(textDrawingAreas);
		PVPInterface2(textDrawingAreas);
		quickCurses(textDrawingAreas);
		quickPrayers(textDrawingAreas);
		equipmentTab(textDrawingAreas);
		equipmentScreen(textDrawingAreas);
		itemsOnDeathDATA(textDrawingAreas);
		itemsOnDeath(textDrawingAreas);
		barrowText(textDrawingAreas);
		Pestpanel(textDrawingAreas);
		Pestpanel2(textDrawingAreas);
		optionTab(textDrawingAreas);
		questTab(textDrawingAreas);
		nightmareZone(textDrawingAreas);
		slayerInterface(textDrawingAreas);
		slayerInterfaceSub1(textDrawingAreas);
		slayerInterfaceSub2(textDrawingAreas);
		//auctionHouse(textDrawingAreas);
		//auctionHouseEntries(textDrawingAreas);
		Bank(textDrawingAreas);
		magicTab(textDrawingAreas);
		Sidebar0(textDrawingAreas);
		ancientMagicTab(textDrawingAreas);
		bountyHunter(textDrawingAreas);
		lootingBag(textDrawingAreas);
		priceChecker(textDrawingAreas);
		showLootingBagInformation(textDrawingAreas);
		// clanWarsFFA(textDrawingAreas);
		aMRUNodes_238 = null;
	}

	public static void addPriceChecker(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		rsi.centerText = true;
		rsi.aBoolean227 = false;
		rsi.aBoolean235 = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.textShadow = false;
		rsi.invSpritePadX = 57;
		rsi.invSpritePadY = 28;
		rsi.height = 5;
		rsi.width = 5;
		rsi.parentID = 26099;
		rsi.id = 4393;
		rsi.type = 2;
	}

	public static void addLootItem(int index, Boolean hasOption) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[5];
		rsi.spritesX = new int[20];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];
		rsi.spritesY = new int[20];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		rsi.hasExamine = false;

		rsi.invSpritePadX = 10;
		rsi.invSpritePadY = 0;
		rsi.height = 7;
		rsi.width = 4;
		rsi.parentID = 28500;
		rsi.id = index;
		rsi.type = 13;
	}

	public static void showLootingBagInformation(TextDrawingArea[] tda) {
		RSInterface Interface = addTabInterface(28500);
		addText(28501, "Looting bag", tda, 2, 0xFF9900, true, true);
		addButton(28502, 0, "Looting/LOOTINGBAG", "Close");
		addSprite(28503, 1, "Looting/LOOTINGBAG");
		Interface.totalChildren(31);
		Interface.child(0, 28501, 98, 3);
		Interface.child(1, 28502, 166, 0);
		Interface.child(2, 28503, 8, 22);
		int x = 20;
		int y = 28;
		/* Row 1 */
		addLootItem(28504, false);
		setBounds(28504, x, y, 3, Interface);
		x += 38;
		addLootItem(28505, false);
		setBounds(28505, x, y, 4, Interface);
		x += 38;
		addLootItem(28506, false);
		setBounds(28506, x, y, 5, Interface);
		x += 38;
		addLootItem(28507, false);
		setBounds(28507, x, y, 6, Interface);
		/* Row 2 */
		y += 31;
		x = 20;
		addLootItem(28508, false);
		setBounds(28508, x, y, 7, Interface);
		x += 38;
		addLootItem(28509, false);
		setBounds(28509, x, y, 8, Interface);
		x += 38;
		addLootItem(28510, false);
		setBounds(28510, x, y, 9, Interface);
		x += 38;
		addLootItem(28511, false);
		setBounds(28511, x, y, 10, Interface);
		/* Row 3 */
		y += 31;
		x = 20;
		addLootItem(28512, false);
		setBounds(28512, x, y, 11, Interface);
		x += 38;
		addLootItem(28513, false);
		setBounds(28513, x, y, 12, Interface);
		x += 38;
		addLootItem(28514, false);
		setBounds(28514, x, y, 13, Interface);
		x += 38;
		addLootItem(28515, false);
		setBounds(28515, x, y, 14, Interface);
		/* Row 4 */
		y += 31;
		x = 20;
		addLootItem(28516, false);
		setBounds(28516, x, y, 15, Interface);
		x += 38;
		addLootItem(28517, false);
		setBounds(28517, x, y, 16, Interface);
		x += 38;
		addLootItem(28518, false);
		setBounds(28518, x, y, 17, Interface);
		x += 38;
		addLootItem(28519, false);
		setBounds(28519, x, y, 18, Interface);
		/* Row 5 */
		y += 31;
		x = 20;
		addLootItem(28520, false);
		setBounds(28520, x, y, 19, Interface);
		x += 38;
		addLootItem(28521, false);
		setBounds(28521, x, y, 20, Interface);
		x += 38;
		addLootItem(28522, false);
		setBounds(28522, x, y, 21, Interface);
		x += 38;
		addLootItem(28523, false);
		setBounds(28523, x, y, 22, Interface);
		/* Row 6 */
		y += 31;
		x = 20;
		addLootItem(28524, false);
		setBounds(28524, x, y, 23, Interface);
		x += 38;
		addLootItem(28525, false);
		setBounds(28525, x, y, 24, Interface);
		x += 38;
		addLootItem(28526, false);
		setBounds(28526, x, y, 25, Interface);
		x += 38;
		addLootItem(28527, false);
		setBounds(28527, x, y, 26, Interface);
		/* Row 7 */
		y += 31;
		x = 20;
		addLootItem(28528, false);
		setBounds(28528, x, y, 27, Interface);
		x += 38;
		addLootItem(28529, false);
		setBounds(28529, x, y, 28, Interface);
		x += 38;
		addLootItem(28530, false);
		setBounds(28530, x, y, 29, Interface);
		x += 38;
		addLootItem(28531, false);
		setBounds(28531, x, y, 30, Interface);
	}

	public static void clanWarsFFA(TextDrawingArea[] tda) {
		/*
		 * RSInterface inter = addTabInterface(25500);
		 * addTransparentSprite(25501, 0, "CW/CW", 255); addText(25502,
		 * "Step over the line to fight!", tda, 0, 0xFFE4B5, true, true);
		 * inter.textSize(25502, tda, 0);
		 * 
		 * 
		 * inter.totalChildren(2); inter.child(0, 25501, 125, 5); inter.child(1,
		 * 25502, 145, 7);
		 */
	}

	public static void lootingBag(TextDrawingArea[] tda) {
		RSInterface Interface = addTabInterface(28000);
		addText(28001, "Add to bag", tda, 2, 0xFF9900, true, true);
		addButton(28002, 0, "Looting/LOOTINGBAG", "Close");
		addSprite(28003, 1, "Looting/LOOTINGBAG");
		Interface.totalChildren(4);
		Interface.child(0, 28001, 98, 3);
		setBounds(3213, 5, 20, 1, Interface);
		Interface.child(2, 28002, 166, 0);
		Interface.child(3, 28003, 8, 22);
		Interface = interfaceCache[3213];
		Interface.height = 334;
		Interface.width = 512;

		Interface = interfaceCache[3214];
		Interface.width = 4;
		Interface.invSpritePadX = 10;
		Interface.height = 7;
	}

	public static void bountyHunter(TextDrawingArea[] TDA) {
		RSInterface tab = addTabInterface(27300);

		addAdvancedSprite(27301, 983);
		addAdvancedSprite(27302, 982);
		addConfigSprite(27303, 984, -1, 1, 876);
		addSprite(27304, 990);
		addText(27305, "---", TDA, 0, 0xffff00, true, true);
		addText(27306, "Target:", TDA, 0, 0xffff00, true, true);
		addText(27307, "None", TDA, 1, 0xffffff, true, true);
		addText(27308, "Level: ------", TDA, 0, 0xffff00, true, true);
		addText(27309, "Current  Record", TDA, 0, 0xffff00, true, true);
		addText(27310, "0", TDA, 0, 0xffff00, true, true);
		addText(27311, "0", TDA, 0, 0xffff00, true, true);
		addText(27312, "0", TDA, 0, 0xffff00, true, true);
		addText(27313, "0", TDA, 0, 0xffff00, true, true);
		addText(27314, "Rogue:", TDA, 0, 0xffff00, true, true);
		addText(27315, "Hunter:", TDA, 0, 0xffff00, true, true);
		addConfigSprite(27316, -1, 985, 1, 877);
		addConfigSprite(27317, -1, 986, 1, 878);
		addConfigSprite(27318, -1, 987, 1, 879);
		addConfigSprite(27319, -1, 988, 1, 880);
		addConfigSprite(27320, -1, 989, 1, 881);
		tab.totalChildren(21);
		tab.child(0, 27301, 319, 8);
		tab.child(1, 27302, 339, 56);
		tab.child(2, 27303, 345, 18);
		tab.child(3, 27304, 348, 73);
		tab.child(4, 27305, 358, 41);
		tab.child(5, 27306, 455, 12);
		tab.child(6, 27307, 456, 25);
		tab.child(7, 27308, 457, 41);
		tab.child(8, 27309, 460, 59);
		tab.child(9, 27310, 438, 72);
		tab.child(10, 27311, 481, 72);
		tab.child(11, 27312, 438, 85);
		tab.child(12, 27313, 481, 85);
		tab.child(13, 27314, 393, 72);
		tab.child(14, 27315, 394, 85);
		tab.child(15, 27316, 345, 18);
		tab.child(16, 27317, 345, 18);
		tab.child(17, 27318, 345, 18);
		tab.child(18, 27319, 345, 18);
		tab.child(19, 27320, 345, 18);
		tab.child(20, 197, 2, 2);
	}

	public static void ancientMagicTab(RSFont[] tda) {
		RSInterface tab = addInterface(12855);
		addButton(12856, 1, "Interfaces/Magic/Home", "Cast @gre@Home Teleport");
		RSInterface homeButton = interfaceCache[12856];
		homeButton.hoverType = 1196;
		int[] itfChildren = { 12856, 12939, 12987, 13035, 12901, 12861, 13045,
				12963, 13011, 13053, 12919, 12881, 13061, 12951, 12999, 13069,
				12911, 12871, 13079, 12975, 13023, 13087, 12929, 12891, 13095,
				1196, 12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012,
				13054, 12920, 12882, 13062, 12952, 13000, 13070, 12912, 12872,
				13080, 12976, 13024, 13088, 12930, 12892, 13096 };
		tab.totalChildren(itfChildren.length);
		for (int i1 = 0, xPos = 18, yPos = 8; i1 < itfChildren.length; i1++, xPos += 45) {
			if (xPos > 175) {
				xPos = 18;
				yPos += 28;
			}
			if (i1 < 25)
				tab.child(i1, itfChildren[i1], xPos, yPos);
			if (i1 > 24) {
				yPos = i1 < 41 ? 181 : 1;
				tab.child(i1, itfChildren[i1], 4, yPos);
			}
		}
	}

	public static void barrowText(TextDrawingArea[] tda) {
		RSInterface tab = addScreenInterface(16128);
		addText(16129, "Barrows Brothers", tda, 2, 0xff981f, true, true);
		addText(16130, "Dharoks", tda, 1, 0x86B404, true, true);
		addText(16131, "Veracs", tda, 1, 0x86B404, true, true);
		addText(16132, "Ahrims", tda, 1, 0x86B404, true, true);
		addText(16133, "Torags", tda, 1, 0x86B404, true, true);
		addText(16134, "Guthans", tda, 1, 0x86B404, true, true);
		addText(16135, "Karils", tda, 1, 0x86B404, true, true);
		addText(16136, "Killcount:", tda, 2, 0xff981f, true, true);
		addText(16137, "#", tda, 1, 0x86B404, true, true);
		tab.totalChildren(9);
		tab.child(0, 16129, 452, 220);
		tab.child(1, 16130, 460, 240);
		tab.child(2, 16131, 460, 255);
		tab.child(3, 16132, 460, 270);
		tab.child(4, 16133, 460, 285);
		tab.child(5, 16134, 460, 300);
		tab.child(6, 16135, 460, 315);
		tab.child(7, 16136, 30, 318);
		tab.child(8, 16137, 68, 318);
	}

	public static void skillInterface(int i, int j) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 26;
		rsinterface.height = 34;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = 0;
		rsinterface.sprite1 = imageLoader(j, "Interfaces/Skill/Skill");
		rsinterface.sprite2 = imageLoader(j, "Interfaces/Skill/Skill");
	}

	public static void skillInterface(TextDrawingArea[] wid) {
		skillInterface(19746, 255);
		skillInterface(19747, 51);
		skillInterface(19748, 50);
		skillInterface(19749, 52);
		// addText(13984, "Total", wid, 0, 0xffff00, true, true);
		// addText2(13984, "Total", wid, 0, 0xffff00);
		addText(13984, "Total", wid, 0, 0xffff00, true, true);
		addText2(3985, "", wid, 0, 0xffff00);
		addText(13983, "Level: 2277", wid, 0, 0xffff00, true, true);
		for (int k = 0; k < boxIds.length; k++) {
			skillInterface(boxIds[k], 256);
		}
		RSInterface rsinterface = addTab(3917);
		rsinterface.children = new int[57];
		rsinterface.childX = new int[57];
		rsinterface.childY = new int[57];
		rsinterface.children[0] = 3918;
		rsinterface.childX[0] = 0;
		rsinterface.childY[0] = 0;
		rsinterface.children[1] = 3925;
		rsinterface.childX[1] = 0;
		rsinterface.childY[1] = 31;
		rsinterface.children[2] = 3932;
		rsinterface.childX[2] = 0;
		rsinterface.childY[2] = 62;
		rsinterface.children[3] = 3939;
		rsinterface.childX[3] = 0;
		rsinterface.childY[3] = 93;
		rsinterface.children[4] = 3946;
		rsinterface.childX[4] = 0;
		rsinterface.childY[4] = 124;
		rsinterface.children[5] = 3953;
		rsinterface.childX[5] = 0;
		rsinterface.childY[5] = 155;
		rsinterface.children[6] = 4148;
		rsinterface.childX[6] = 0;
		rsinterface.childY[6] = 186;
		rsinterface.children[7] = 19746;
		rsinterface.childX[7] = 70;
		rsinterface.childY[7] = 69;
		// rsinterface.children[8] = 19748; rsinterface.childX[8] = 1;
		// rsinterface.childY[8] = 219;
		// rsinterface.children[9] = 19747; rsinterface.childX[9] = 64;
		// rsinterface.childY[9] = 219;
		rsinterface.children[8] = 14000;
		rsinterface.childX[8] = 10;
		rsinterface.childY[8] = 219;
		rsinterface.children[9] = 19749;
		rsinterface.childX[9] = 33;
		rsinterface.childY[9] = 223;
		rsinterface.children[10] = 13983;
		rsinterface.childX[10] = 95;
		rsinterface.childY[10] = 240;
		rsinterface.children[11] = 3984;
		rsinterface.childX[11] = 300;
		rsinterface.childY[11] = 225;
		rsinterface.children[12] = 3985;
		rsinterface.childX[12] = 130;
		rsinterface.childY[12] = 238;
		// rsinterface.children[13] = 29800; rsinterface.childX[13] = 98;
		// rsinterface.childY[13] = 220;
		// rsinterface.children[14] = 29800; rsinterface.childX[14] = 107;
		// rsinterface.childY[14] = 235;
		// rsinterface.children[15] = 29801; rsinterface.childX[15] = 36;
		// rsinterface.childY[15] = 220;
		// rsinterface.children[16] = 29801; rsinterface.childX[16] = 45;
		// rsinterface.childY[16] = 235;
		rsinterface.children[13] = 4040;
		rsinterface.childX[13] = 5;
		rsinterface.childY[13] = 20;
		rsinterface.children[14] = 8654;
		rsinterface.childX[14] = 0;
		rsinterface.childY[14] = 2;
		rsinterface.children[15] = 8655;
		rsinterface.childX[15] = 64;
		rsinterface.childY[15] = 2;
		rsinterface.children[16] = 4076;
		rsinterface.childX[16] = 20;
		rsinterface.childY[16] = 20;
		rsinterface.children[17] = 8656;
		rsinterface.childX[17] = 128;
		rsinterface.childY[17] = 2;
		rsinterface.children[18] = 4112;
		rsinterface.childX[18] = 20;
		rsinterface.childY[18] = 20;
		rsinterface.children[19] = 8657;
		rsinterface.childX[19] = 0;
		rsinterface.childY[19] = 33;
		rsinterface.children[20] = 4046;
		rsinterface.childX[20] = 20;
		rsinterface.childY[20] = 50;
		rsinterface.children[21] = 8658;
		rsinterface.childX[21] = 64;
		rsinterface.childY[21] = 33;
		rsinterface.children[22] = 4082;
		rsinterface.childX[22] = 20;
		rsinterface.childY[22] = 50;
		rsinterface.children[23] = 8659;
		rsinterface.childX[23] = 128;
		rsinterface.childY[23] = 33;
		rsinterface.children[24] = 4118;
		rsinterface.childX[24] = 20;
		rsinterface.childY[24] = 50;
		rsinterface.children[25] = 8660;
		rsinterface.childX[25] = 0;
		rsinterface.childY[25] = 60 + 10;
		rsinterface.children[26] = 4052;
		rsinterface.childX[26] = 20;
		rsinterface.childY[26] = 83;
		rsinterface.children[27] = 8661;
		rsinterface.childX[27] = 65;
		rsinterface.childY[27] = 60 + 10;
		rsinterface.children[28] = 4088;
		rsinterface.childX[28] = 20;
		rsinterface.childY[28] = 83;
		rsinterface.children[29] = 8662;
		rsinterface.childX[29] = 130;
		rsinterface.childY[29] = 60 + 10;
		rsinterface.children[30] = 4124;
		rsinterface.childX[30] = 20;
		rsinterface.childY[30] = 83;
		rsinterface.children[31] = 8663;
		rsinterface.childX[31] = 0;
		rsinterface.childY[31] = 90 + 10;
		rsinterface.children[32] = 4058;
		rsinterface.childX[32] = 20;
		rsinterface.childY[32] = 120;
		rsinterface.children[33] = 8664;
		rsinterface.childX[33] = 65;
		rsinterface.childY[33] = 90 + 10;
		rsinterface.children[34] = 4094;
		rsinterface.childX[34] = 20;
		rsinterface.childY[34] = 120;
		rsinterface.children[35] = 8665;
		rsinterface.childX[35] = 130;
		rsinterface.childY[35] = 90 + 10;
		rsinterface.children[36] = 4130;
		rsinterface.childX[36] = 20;
		rsinterface.childY[36] = 120;
		rsinterface.children[37] = 8666;
		rsinterface.childX[37] = 0;
		rsinterface.childY[37] = 130;
		rsinterface.children[38] = 4064;
		rsinterface.childX[38] = 20;
		rsinterface.childY[38] = 150;
		rsinterface.children[39] = 8667;
		rsinterface.childX[39] = 65;
		rsinterface.childY[39] = 130;
		rsinterface.children[40] = 4100;
		rsinterface.childX[40] = 20;
		rsinterface.childY[40] = 150;
		rsinterface.children[41] = 8668;
		rsinterface.childX[41] = 130;
		rsinterface.childY[41] = 130;
		rsinterface.children[42] = 4136;
		rsinterface.childX[42] = 20;
		rsinterface.childY[42] = 150;
		rsinterface.children[43] = 8669;
		rsinterface.childX[43] = 0;
		rsinterface.childY[43] = 160;
		rsinterface.children[44] = 4070;
		rsinterface.childX[44] = 20;
		rsinterface.childY[44] = 180;
		rsinterface.children[45] = 8670;
		rsinterface.childX[45] = 65;
		rsinterface.childY[45] = 160;
		rsinterface.children[46] = 4106;
		rsinterface.childX[46] = 20;
		rsinterface.childY[46] = 180;
		rsinterface.children[47] = 8671;
		rsinterface.childX[47] = 130;
		rsinterface.childY[47] = 160;
		rsinterface.children[48] = 4142;
		rsinterface.childX[48] = 20;
		rsinterface.childY[48] = 180;
		rsinterface.children[49] = 8672;
		rsinterface.childX[49] = 0;
		rsinterface.childY[49] = 190;
		rsinterface.children[50] = 4160;
		rsinterface.childX[50] = 20;
		rsinterface.childY[50] = 150;
		rsinterface.children[51] = 4160;
		rsinterface.childX[51] = 20;
		rsinterface.childY[51] = 150;
		rsinterface.children[52] = 12162;
		rsinterface.childX[52] = 65;
		rsinterface.childY[52] = 190;
		rsinterface.children[53] = 2832;
		rsinterface.childX[53] = 20;
		rsinterface.childY[53] = 150;
		rsinterface.children[54] = 13928;
		rsinterface.childX[54] = 130;
		rsinterface.childY[54] = 190;
		rsinterface.children[55] = 13917;
		rsinterface.childX[55] = 20;
		rsinterface.childY[55] = 150;
		rsinterface.children[56] = 13984;
		rsinterface.childX[56] = 91;
		rsinterface.childY[56] = 227;
	}

	public static void auctionHouseEntries(TextDrawingArea[] tda) {
		RSInterface tab = addScreenInterface(26840);
		addSprite(26800, 1, "AuctionHouse/BACK");
		addText(26801, "Auction House BETA", tda, 2, 0xffffff, false, true);
		addHoverButton(26802, "AuctionHouse/BUTTON", 8, 166, 20,
				"Market screen", -1, 26841, 1);
		addText(26803, "Back to Market", tda, 0, 0xffffff, false, true);

		addText(26804, "26804", tda, 0, 0xd7dbba, false, true);
		addText(26805, "26805", tda, 0, 0xd7dbba, false, true);
		addText(26806, "26806", tda, 0, 0xd7dbba, false, true);
		addText(26807, "26807", tda, 0, 0xd7dbba, false, true);
		addText(26808, "26808", tda, 0, 0xd7dbba, false, true);
		addText(26809, "26809", tda, 0, 0xd7dbba, false, true);
		addHoverButton(26810, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26842, 1);
		addHoverButton(26811, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26843, 1);
		addHoverButton(26812, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26844, 1);
		addHoverButton(26813, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26845, 1);
		addHoverButton(26814, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26846, 1);
		addHoverButton(26815, "AuctionHouse/BUTTON", 9, 26, 52, "Cancel offer",
				-1, 26847, 1);
		tab.auctionContainer(27850, 26840);
		addText(26851, "26851", tda, 0, 0xffffff, false, true);
		addText(26852, "26852", tda, 0, 0xffffff, false, true);
		addText(26853, "26853", tda, 0, 0xffffff, false, true);
		addText(26854, "26854", tda, 0, 0xffffff, false, true);
		addText(26855, "26855", tda, 0, 0xffffff, false, true);
		addText(26856, "26856", tda, 0, 0xffffff, false, true);
		int x = 90;
		int y = 68;
		tab.totalChildren(23);
		tab.child(0, 26800, 5, 5);
		tab.child(1, 26801, 190, 28);
		tab.child(2, 26802, 15, 25);
		tab.child(3, 26803, 35, 31);

		tab.child(4, 26804, x - 3, y - 1);
		tab.child(10, 26810, x + 124, y - 6);
		tab.child(17, 26851, x, y + 30);

		tab.child(5, 26805, x + 275, y - 1);
		tab.child(11, 26811, x + 181, y - 6);
		tab.child(18, 26852, x + 275, y + 30);

		y += 65;

		tab.child(6, 26806, x - 3, y - 1);
		tab.child(12, 26812, x + 124, y - 7);
		tab.child(19, 26853, x, y + 29);

		tab.child(7, 26807, x + 275, y - 1);
		tab.child(13, 26813, x + 181, y - 7);
		tab.child(20, 26854, x + 275, y + 29);

		y += 65;

		tab.child(8, 26808, x - 3, y - 3);
		tab.child(14, 26814, x + 124, y - 8);
		tab.child(21, 26855, x, y + 28);

		tab.child(9, 26809, x + 275, y - 3);
		tab.child(15, 26815, x + 181, y - 8);
		tab.child(22, 26856, x + 275, y + 28);

		tab.child(16, 27850, 36, 74);
	}

	public void auctionContainer(int frameId, int parent) {
		RSInterface container = interfaceCache[frameId] = new RSInterface();
		container.actions = new String[5];// right clicks
		container.invStackSizes = new int[30]; // number of items again
		container.inv = new int[30]; // number of items again
		container.spritesY = new int[20];// ySprite
		container.spritesX = new int[20];// xSprite
		container.children = new int[0];// children
		container.childX = new int[0];
		container.childY = new int[0];
		container.centerText = false;
		container.aBoolean227 = true;
		container.aBoolean235 = true;
		container.usableItemInterface = false;
		container.isInventoryInterface = true;
		container.aBoolean259 = true;
		container.textShadow = false;
		container.width = 2; // how many columns (across -->)
		container.height = 3; // how many rows (down)
		container.invSpritePadX = 241; // seperation from item to item X
		container.invSpritePadY = 32; // seperation from item to item Y
		container.parentID = parent; // what interface id "owns" this component
		container.id = frameId; // id for this component
		container.type = 100;// needed to display, 2 is bank
		/**
		 * if width = 6 this is how it looks a 6 x 5 table (width x height = inv
		 * size) 1 2 3 4 5 6 1 2 3 4 5 6 1 2 3 4 5 6 1 2 3 4 5 6 //understand so
		 * far? si senior lmao 1 2 3 4 5 6
		 */

	}

	public static void auctionHouse(TextDrawingArea[] tda) {
		RSInterface tab = addScreenInterface(26780);
		addSprite(26700, 0, "AuctionHouse/BACK");// 250 is centre
		addText(26701, "Auction House BETA", tda, 2, 0xffffff, false, true);
		addText(26702, "Market list:", tda, 2, 0xffffff, false, true);
		addText(26703, "Select an item", tda, 0, 0xffffff, false, true);
		addText(26704, " ", tda, 0, 0xffffff, false, true);
		addText(26705, " ", tda, 0, 0xffffff, false, true);
		addText(26706, " ", tda, 0, 0xffffff, false, true);
		addText(26707, " ", tda, 0, 0xffffff, false, true);
		addText(26708, " ", tda, 0, 0xffffff, false, true);
		addText(26709, " ", tda, 0, 0xffffff, false, true);
		addText(26710, " ", tda, 0, 0xffffff, false, true);
		addText(26711, " ", tda, 0, 0xffffff, false, true);
		addText(26712, " ", tda, 0, 0xffffff, false, true);
		addText(26713, " ", tda, 0, 0xffffff, false, true);
		addText(26714, " ", tda, 0, 0xffffff, false, true);
		addText(26715, " ", tda, 0, 0xffffff, false, true);
		addText(26716, " ", tda, 0, 0xffffff, false, true);
		addText(26717, " ", tda, 0, 0xffffff, false, true);
		addText(26718, " ", tda, 0, 0xffffff, false, true);
		addHoverButton(26719, "AuctionHouse/BUTTON", 0, 154, 57,
				"Collect item", -1, 26761, 1);
		addText(26720, "Collect item", tda, 2, 0xffffff, false, true);
		addHoverButton(26721, "AuctionHouse/BUTTON", 1, 154, 57,
				"Collect gold", -1, 26762, 1);
		addText(26722, "Collect gold", tda, 2, 0xffffff, false, true);
		addHoverButton(26723, "AuctionHouse/BUTTON", 2, 154, 57, "Buy item",
				1346, 26763, 1);
		addText(26724, "Buy item", tda, 2, 0xffffff, false, true);
		addHoverButton(26725, "AuctionHouse/BUTTON", 3, 154, 57, "Sell item",
				1347, 26764, 1);
		addText(26726, "Sell item", tda, 2, 0xffffff, false, true);
		addHoverButton(26727, "AuctionHouse/BUTTON", 6, 124, 33, "Select item",
				1343, 26765, 1);
		addText(26728, "Select item", tda, 2, 0xffffff, false, true);
		addHoverButton(26729, "AuctionHouse/BUTTON", 4, 93, 33,
				"Change amount", 1344, 26766, 1);
		addText(26730, "Amount", tda, 2, 0xffffff, false, true);
		addHoverButton(26731, "AuctionHouse/BUTTON", 5, 93, 33, "Change price",
				1345, 26767, 1);
		addText(26732, "Price", tda, 2, 0xffffff, false, true);
		addText(26733, "None", tda, 1, 0xffffff, false, true);
		addText(26734, "1", tda, 1, 0xffffff, false, true);
		addText(26735, "1", tda, 1, 0xffffff, false, true);
		addText(26736, "Info", tda, 2, 0xffffff, false, true);
		addText(26737, "Shop value:", tda, 0, 0xffffff, false, true);
		addText(26738, "Low Alchemy value:", tda, 0, 0xffffff, false, true);
		addText(26739, "High Alchemy value:", tda, 0, 0xffffff, false, true);
		addText(26740, "Item type:", tda, 0, 0xffffff, false, true);
		addText(26741, "Tradeable:", tda, 0, 0xffffff, false, true);
		addText(26742, "Stackable:", tda, 0, 0xffffff, false, true);
		addHoverButton(15022, "AuctionHouse/CLOSE2", 1, 16, 16, "Close", -1,
				15022, 1);
		addHoveredButton(15023, "AuctionHouse/CLOSE2", 0, 16, 16, 15057);
		addHoverButton(26743, "AuctionHouse/BUTTON", 7, 166, 20,
				"Market entries", -1, 26768, 1);
		addText(26744, "Market entries", tda, 0, 0xffffff, false, true);
		int x = 25;
		int y = 67;
		tab.totalChildren(47);
		tab.child(0, 26700, 5, 5);
		tab.child(1, 26701, 190, 28);
		tab.child(2, 26702, x, y);
		y += 14;
		tab.child(3, 26703, x, y);
		y += 14;
		tab.child(4, 26704, x, y);
		y += 14;
		tab.child(5, 26705, x, y);
		y += 14;
		tab.child(6, 26706, x, y);
		y += 14;
		tab.child(7, 26707, x, y);
		y += 14;
		tab.child(8, 26708, x, y);
		y += 14;
		tab.child(9, 26709, x, y);
		y += 14;
		tab.child(10, 26710, x, y);
		y += 14;
		tab.child(11, 26711, x, y);
		y += 14;
		tab.child(12, 26712, x, y);
		y += 14;
		tab.child(13, 26713, x, y);
		y += 14;
		tab.child(14, 26714, x, y);
		y += 14;
		tab.child(15, 26715, x, y);
		y += 14;
		tab.child(16, 26716, x, y);
		y += 14;
		tab.child(17, 26717, x, y);
		y += 14;
		tab.child(18, 26718, x, y);
		y += 14;
		tab.child(19, 26719, 183, 53);
		tab.child(20, 26720, 248, 75);
		tab.child(21, 26721, 343, 53);
		tab.child(22, 26722, 408, 75);
		tab.child(23, 26723, 183, 115);
		tab.child(24, 26724, 258, 138);
		tab.child(25, 26725, 343, 115);
		tab.child(26, 26726, 418, 138);
		tab.child(27, 26727, 183, 177);
		tab.child(28, 26728, 226, 187);
		tab.child(29, 26729, 309, 177);
		tab.child(30, 26730, 349, 187);
		tab.child(31, 26731, 404, 177);
		tab.child(32, 26732, 447, 187);
		tab.child(33, 26733, 183, 214);
		tab.child(34, 26734, 349, 214);
		tab.child(35, 26735, 444, 214);

		tab.child(36, 26736, 183, 234);
		tab.child(37, 26737, 183, 254);
		tab.child(38, 26738, 183, 274);
		tab.child(39, 26739, 183, 294);
		tab.child(40, 26740, 349, 254);
		tab.child(41, 26741, 349, 274);
		tab.child(42, 26742, 349, 294);
		tab.child(43, 15022, 480, 8);// 34 //43
		tab.child(44, 15023, 480, 8);
		tab.child(45, 26743, 15, 25);
		tab.child(46, 26744, 35, 31);
	}

	public static void PVPInterface(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21200);
		addSprite(21201, 0, "PvP/NOTINWILD1");
		addText(21202, "", tda, 1, 0xff9040, true, true);
		int last = 2;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21201, 400, 285, 0, RSinterface);
		setBounds(21202, 444, 318, 1, RSinterface);
	}

	public static void PVPInterface2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21300);
		addSprite(21301, 0, "PvP/INWILD1");
		addText(21302, "", tda, 1, 0xff9040, true, true);
		int last = 2;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21301, 400, 285, 0, RSinterface);
		setBounds(21302, 444, 318, 1, RSinterface);
	}

	public boolean inventoryHover;

	public static void addButton(int i, int j, String name, int W, int H,
			String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void addButton(int id, int sid, String spriteName,
			String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.inventoryHover = true;
	}

	public static void fixEquipment() {
		RSInterface t = interfaceCache[1644];
		addHoverButton(27651, "Interfaces/Equipment/BOX", 2, 40, 40,
				"Price-checker", -1, 27652, 1);
		addHoveredButton(27652, "Interfaces/Equipment/HOVER", 2, 40, 40, 27658);
		setBounds(27651, 75, 205, 0, t);
		setBounds(27652, 75, 205, 1, t);
		addButton(15201, 1, "Equipment/BOX", "Show Equipment Stats");
		t.children[23] = 15201;
		t.childX[23] = 23;
		t.childY[23] = 205;
		addSprite(22021, 1, "sprite");
		t.children[24] = 22021;
		t.childX[24] = 41;
		t.childY[24] = 212;

		addButton(15202, 2, "Equipment/BOX", "Show Items Kept on Death");
		t.children[25] = 15202;
		t.childX[25] = 127;
		t.childY[25] = 205;
		addSprite(22023, 2, "sprite");
		t.children[26] = 22023;
		t.childX[26] = 41 + 39 + 30;
		t.childY[26] = 212;
	}

	public static void equipmentScreen(TextDrawingArea[] wid) {
		fixEquipment();
		RSInterface tab = addTabInterface(15106);
		addSprite(15107, 7, "Equipment/CUSTOM");
		addHoverButton(15210, "Equipment/CUSTOM", 8, 21, 21, "Close", 250,
				15211, 3);
		addHoveredButton(15211, "Equipment/CUSTOM", 9, 21, 21, 15212);
		addText(15111, "Equip Your Character...", wid, 2, 0xe4a146, false, true);
		addText(15112, "Attack bonus", wid, 2, 0xe4a146, false, true);
		addText(15113, "Defence bonus", wid, 2, 0xe4a146, false, true);
		addText(15114, "Other bonuses", wid, 2, 0xe4a146, false, true);
		for (int i = 1675; i <= 1684; i++) {
			textSize(i, wid, 1);
		}
		textSize(1686, wid, 1);
		textSize(1687, wid, 1);
		addChar(15125);
		tab.totalChildren(44);
		tab.child(0, 15107, 4, 20);
		tab.child(1, 15210, 476, 29);
		tab.child(2, 15211, 476, 29);
		tab.child(3, 15111, 14, 30);
		int Child = 4;
		int Y = 69;
		for (int i = 1675; i <= 1679; i++) {
			tab.child(Child, i, 20, Y);
			Child++;
			Y += 14;
		}
		tab.child(9, 1680, 20, 161);
		tab.child(10, 1681, 20, 177);
		tab.child(11, 1682, 20, 192);
		tab.child(12, 1683, 20, 207);
		tab.child(13, 1684, 20, 221);
		tab.child(14, 1686, 20, 262);
		tab.child(15, 15125, 170, 200);
		tab.child(16, 15112, 16, 55);
		tab.child(17, 1687, 20, 276);
		tab.child(18, 15113, 16, 147);
		tab.child(19, 15114, 16, 248);
		tab.child(20, 1645, 104 + 295, 149 - 52);
		tab.child(21, 1646, 399, 163);
		tab.child(22, 1647, 399, 163);
		tab.child(23, 1648, 399, 58 + 146);
		tab.child(24, 1649, 26 + 22 + 297 - 2, 110 - 44 + 118 - 13 + 5);
		tab.child(25, 1650, 321 + 22, 58 + 154);
		tab.child(26, 1651, 321 + 134, 58 + 118);
		tab.child(27, 1652, 321 + 134, 58 + 154);
		tab.child(28, 1653, 321 + 48, 58 + 81);
		tab.child(29, 1654, 321 + 107, 58 + 81);
		tab.child(30, 1655, 321 + 58, 58 + 42);
		tab.child(31, 1656, 321 + 112, 58 + 41);
		tab.child(32, 1657, 321 + 78, 58 + 4);
		tab.child(33, 1658, 321 + 37, 58 + 43);
		tab.child(34, 1659, 321 + 78, 58 + 43);
		tab.child(35, 1660, 321 + 119, 58 + 43);
		tab.child(36, 1661, 321 + 22, 58 + 82);
		tab.child(37, 1662, 321 + 78, 58 + 82);
		tab.child(38, 1663, 321 + 134, 58 + 82);
		tab.child(39, 1664, 321 + 78, 58 + 122);
		tab.child(40, 1665, 321 + 78, 58 + 162);
		tab.child(41, 1666, 321 + 22, 58 + 162);
		tab.child(42, 1667, 321 + 134, 58 + 162);
		tab.child(43, 1688, 50 + 297 - 2, 110 - 13 + 5);
		for (int i = 1675; i <= 1684; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
	}

	public static RSInterface addTab(int i) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 512;
		rsinterface.height = 334;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = 0;
		return rsinterface;
	}

	public static void addSprite(int i, int j, int k) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = 52;
		rsinterface.sprite1 = imageLoader(j, "Equipment/SPRITE");
		rsinterface.sprite2 = imageLoader(k, "Equipment/SPRITE");
	}

	public static void addChar(int ID) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.contentType = 328;
		t.width = 136;
		t.height = 168;
		t.aByte254 = 0;
		t.hoverType = 0;
		t.modelZoom = 560;
		t.modelRotation1 = 150;
		t.modelRotation2 = 0;
		t.anInt257 = -1;
		t.anInt258 = -1;
	}

	public static void addText(int id, String text, TextDrawingArea wid[],
			int idx, int color) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;
	}

	public static void addText(int id, String text, TextDrawingArea wid[],
			int idx, int color, int width, int height) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = width;
		rsinterface.height = height;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;
	}

	int textAlign;

	private static void addText(int id, String text, TextDrawingArea tda[],
			int idx, int color, int textCenter, boolean shadow) {
		RSInterface widget = addTabInterface(id);
		widget.parentID = id;
		widget.id = id;
		widget.type = 4;
		widget.atActionType = 0;
		widget.width = 0;
		widget.height = 11;
		widget.contentType = 0;
		widget.aByte254 = 0;
		widget.hoverType = -1;
		widget.textAlign = textCenter;
		widget.textShadow = shadow;
		widget.textDrawingAreas = tda[idx];
		widget.message = text;
		widget.aString228 = "";
		widget.textColor = color;
		widget.anInt219 = 0;
		widget.anInt216 = 0;
		widget.anInt239 = 0;
	}

	public static void itemsOnDeath(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		addHover(17102, 3, 0, 10601, 1, "Interfaces/Equipment/SPRITE", 17, 17,
				"Close Window");
		addHovered(10601, 3, "Interfaces/Equipment/SPRITE", 17, 17, 10602);
		addText(17103, "Items kept on death", wid, 2, 0xff981f);
		addText(17104, "Items I will keep...", wid, 1, 0xff981f);
		addText(17105, "Items I will lose...", wid, 1, 0xff981f);
		addText(17106, "Info", wid, 1, 0xff981f);
		addText(17107, "OxidePkz", wid, 1, 0xffcc33);
		addText(17108, "", wid, 1, 0xffcc33);
		rsinterface.scrollMax = 0;
		rsinterface.isMouseoverTriggered = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 17102;
		rsinterface.childX[1] = 480;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 50;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 110;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 47;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 349;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 398;
		rsinterface.childY[7] = 298;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 74;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 133;
		rsinterface.children[11] = 10601;
		rsinterface.childX[11] = 480;
		rsinterface.childY[11] = 17;
	}

	public static void itemsOnDeathDATA(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17115);
		addText(17109, "a", wid, 0, 0xff981f);
		addText(17110, "b", wid, 0, 0xff981f);
		addText(17111, "c", wid, 0, 0xff981f);
		addText(17112, "d", wid, 0, 0xff981f);
		addText(17113, "e", wid, 0, 0xff981f);
		addText(17114, "f", wid, 0, 0xff981f);
		addText(17117, "g", wid, 0, 0xff981f);
		addText(17118, "h", wid, 0, 0xff981f);
		addText(17119, "i", wid, 0, 0xff981f);
		addText(17120, "j", wid, 0, 0xff981f);
		addText(17121, "k", wid, 0, 0xff981f);
		addText(17122, "l", wid, 0, 0xff981f);
		addText(17123, "m", wid, 0, 0xff981f);
		addText(17124, "n", wid, 0, 0xff981f);
		addText(17125, "o", wid, 0, 0xff981f);
		addText(17126, "p", wid, 0, 0xff981f);
		addText(17127, "q", wid, 0, 0xff981f);
		addText(17128, "r", wid, 0, 0xff981f);
		addText(17129, "s", wid, 0, 0xff981f);
		addText(17130, "t", wid, 0, 0xff981f);
		rsinterface.parentID = 17115;
		rsinterface.id = 17115;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 130;
		rsinterface.height = 197;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = -1;
		rsinterface.scrollMax = 280;
		rsinterface.children = new int[20];
		rsinterface.childX = new int[20];
		rsinterface.childY = new int[20];
		rsinterface.children[0] = 17109;
		rsinterface.childX[0] = 0;
		rsinterface.childY[0] = 0;
		rsinterface.children[1] = 17110;
		rsinterface.childX[1] = 0;
		rsinterface.childY[1] = 12;
		rsinterface.children[2] = 17111;
		rsinterface.childX[2] = 0;
		rsinterface.childY[2] = 24;
		rsinterface.children[3] = 17112;
		rsinterface.childX[3] = 0;
		rsinterface.childY[3] = 36;
		rsinterface.children[4] = 17113;
		rsinterface.childX[4] = 0;
		rsinterface.childY[4] = 48;
		rsinterface.children[5] = 17114;
		rsinterface.childX[5] = 0;
		rsinterface.childY[5] = 60;
		rsinterface.children[6] = 17117;
		rsinterface.childX[6] = 0;
		rsinterface.childY[6] = 72;
		rsinterface.children[7] = 17118;
		rsinterface.childX[7] = 0;
		rsinterface.childY[7] = 84;
		rsinterface.children[8] = 17119;
		rsinterface.childX[8] = 0;
		rsinterface.childY[8] = 96;
		rsinterface.children[9] = 17120;
		rsinterface.childX[9] = 0;
		rsinterface.childY[9] = 108;
		rsinterface.children[10] = 17121;
		rsinterface.childX[10] = 0;
		rsinterface.childY[10] = 120;
		rsinterface.children[11] = 17122;
		rsinterface.childX[11] = 0;
		rsinterface.childY[11] = 132;
		rsinterface.children[12] = 17123;
		rsinterface.childX[12] = 0;
		rsinterface.childY[12] = 144;
		rsinterface.children[13] = 17124;
		rsinterface.childX[13] = 0;
		rsinterface.childY[13] = 156;
		rsinterface.children[14] = 17125;
		rsinterface.childX[14] = 0;
		rsinterface.childY[14] = 168;
		rsinterface.children[15] = 17126;
		rsinterface.childX[15] = 0;
		rsinterface.childY[15] = 180;
		rsinterface.children[16] = 17127;
		rsinterface.childX[16] = 0;
		rsinterface.childY[16] = 192;
		rsinterface.children[17] = 17128;
		rsinterface.childX[17] = 0;
		rsinterface.childY[17] = 204;
		rsinterface.children[18] = 17129;
		rsinterface.childX[18] = 0;
		rsinterface.childY[18] = 216;
		rsinterface.children[19] = 17130;
		rsinterface.childX[19] = 0;
		rsinterface.childY[19] = 228;
	}

	public static void removeConfig(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}
	
	public static void priceChecker(TextDrawingArea[] wid) {
		RSInterface rsi = addInterface(26000);

		addSprite(26001, 1, "Interfaces/PriceChecker/checker");
		// Close
		addHoverButton(26002, "Interfaces/PriceChecker/close", 1, 16, 21,
				"Close", -1, 26003, 1);
		addHoveredButton(26003, "Interfaces/PriceChecker/close", 2, 21, 21,
				26004);
		// Deposit all
		addHoverButton(26005, "Interfaces/PriceChecker/deposit", 1, 36, 36,
				"Add all", -1, 26006, 1);
		addHoveredButton(26006, "Interfaces/PriceChecker/deposit", 2, 36, 36,
				26007);
		// Search
		addHoverButton(26008, "Interfaces/PriceChecker/search", 1, 36, 36,
				"Search for item", -1, 26009, 1);
		addHoveredButton(26009, "Interfaces/PriceChecker/search", 2, 36, 36,
				26010);

		// Text
		addText(26011, "Grand Exchange guide prices", wid, 2, 0xFF981F, false,
				true);
		addText(26012, "Total guide price:", wid, 1, 0xFF981F, false, true);
		addText(26013, "0", wid, 1, 0xffffff, false, true);

		rsi.totalChildren(11);
		rsi.child(0, 26001, 15, 15);
		rsi.child(1, 26002, 467, 22);
		rsi.child(2, 26003, 467, 22);
		rsi.child(3, 26005, 451, 285);
		rsi.child(4, 26006, 451, 285);
		rsi.child(5, 26008, 25, 285);
		rsi.child(6, 26009, 25, 285);
		rsi.child(7, 26011, 160, 22);
		rsi.child(8, 26012, 211, 287);
		rsi.child(9, 26013, 255, 302);
		rsi.child(10, 26014, 19, 55);

		RSInterface scroll = addInterface(26014);

		// Item Slots
		addText(26015, "", wid, 0, 0xFFFFFF, true, true);
		addText(26016, "", wid, 0, 0xFFFFFF, true, true);

		addText(26018, "", wid, 0, 0xFFFFFF, true, true);
		addText(26019, "", wid, 0, 0xFFFFFF, true, true);

		addText(26021, "", wid, 0, 0xFFFFFF, true, true);
		addText(26022, "", wid, 0, 0xFFFFFF, true, true);

		addText(26024, "", wid, 0, 0xFFFFFF, true, true);
		addText(26025, "", wid, 0, 0xFFFFFF, true, true);

		addText(26027, "", wid, 0, 0xFFFFFF, true, true); // Soul rune showing
															// someone above
		addText(26028, "", wid, 0, 0xFFFFFF, true, true);

		addText(26030, "", wid, 0, 0xFFFFFF, true, true);
		addText(26031, "", wid, 0, 0xFFFFFF, true, true);

		addText(26033, "", wid, 0, 0xFFFFFF, true, true);
		addText(26034, "", wid, 0, 0xFFFFFF, true, true);

		addText(26036, "", wid, 0, 0xFFFFFF, true, true);
		addText(26037, "", wid, 0, 0xFFFFFF, true, true);

		addText(26039, "", wid, 0, 0xFFFFFF, true, true);
		addText(26040, "", wid, 0, 0xFFFFFF, true, true);

		addText(26042, "", wid, 0, 0xFFFFFF, true, true);
		addText(26043, "", wid, 0, 0xFFFFFF, true, true);

		addText(26045, "", wid, 0, 0xFFFFFF, true, true);
		addText(26046, "", wid, 0, 0xFFFFFF, true, true);

		addText(26048, "", wid, 0, 0xFFFFFF, true, true);
		addText(26049, "", wid, 0, 0xFFFFFF, true, true);

		addText(26051, "", wid, 0, 0xFFFFFF, true, true);
		addText(26052, "", wid, 0, 0xFFFFFF, true, true);

		addText(26054, "", wid, 0, 0xFFFFFF, true, true);
		addText(26055, "", wid, 0, 0xFFFFFF, true, true);

		addText(26057, "", wid, 0, 0xFFFFFF, true, true);
		addText(26058, "", wid, 0, 0xFFFFFF, true, true);

		addText(26060, "", wid, 0, 0xFFFFFF, true, true);
		addText(26061, "", wid, 0, 0xFFFFFF, true, true);

		addText(26063, "", wid, 0, 0xFFFFFF, true, true);
		addText(26064, "", wid, 0, 0xFFFFFF, true, true);

		addText(26066, "", wid, 0, 0xFFFFFF, true, true);
		addText(26067, "", wid, 0, 0xFFFFFF, true, true);

		addText(26069, "", wid, 0, 0xFFFFFF, true, true);
		addText(26070, "", wid, 0, 0xFFFFFF, true, true);

		addText(26072, "", wid, 0, 0xFFFFFF, true, true);
		addText(26073, "", wid, 0, 0xFFFFFF, true, true);

		addText(26075, "", wid, 0, 0xFFFFFF, true, true);
		addText(26076, "", wid, 0, 0xFFFFFF, true, true);

		addText(26078, "", wid, 0, 0xFFFFFF, true, true);
		addText(26079, "", wid, 0, 0xFFFFFF, true, true);

		addText(26081, "", wid, 0, 0xFFFFFF, true, true);
		addText(26082, "", wid, 0, 0xFFFFFF, true, true);

		addText(26084, "", wid, 0, 0xFFFFFF, true, true);
		addText(26085, "", wid, 0, 0xFFFFFF, true, true);

		addText(26087, "", wid, 0, 0xFFFFFF, true, true);
		addText(26088, "", wid, 0, 0xFFFFFF, true, true);

		addText(26090, "", wid, 0, 0xFFFFFF, true, true);
		addText(26091, "", wid, 0, 0xFFFFFF, true, true);

		addText(26093, "", wid, 0, 0xFFFFFF, true, true);
		addText(26094, "", wid, 0, 0xFFFFFF, true, true);

		addText(26096, "", wid, 0, 0xFFFFFF, true, true);
		addText(26097, "", wid, 0, 0xFFFFFF, true, true);

		addPriceChecker(26099); // Price Checker

		scroll.totalChildren(57);
		scroll.child(0, 26015, 49, 32);
		scroll.child(1, 26016, 49, 42);
		// scroll.child(2, 26017, 49, 52);
		scroll.child(2, 26018, 138, 32);
		scroll.child(3, 26019, 138, 42);
		// scroll.child(5, 26020, 138, 52);
		scroll.child(4, 26021, 227, 32);
		scroll.child(5, 26022, 227, 42);
		// scroll.child(8, 26023, 227, 52);
		scroll.child(6, 26024, 316, 32);
		scroll.child(7, 26025, 316, 42);
		// scroll.child(11, 26026, 316, 52);
		scroll.child(8, 26027, 405, 32);
		scroll.child(9, 26028, 405, 42);
		// scroll.child(14, 26029, 405, 52); //First Row
		scroll.child(10, 26030, 49, 92);
		scroll.child(11, 26031, 49, 102);
		// scroll.child(17, 26032, 49, 112);
		scroll.child(12, 26033, 138, 92);
		scroll.child(13, 26034, 138, 102);
		// scroll.child(20, 26035, 138, 112);
		scroll.child(14, 26036, 227, 92);
		scroll.child(15, 26037, 227, 102);
		// scroll.child(23, 26038, 227, 112);
		scroll.child(16, 26039, 316, 92);
		scroll.child(17, 26040, 316, 102);
		// scroll.child(26, 26041, 316, 112);
		scroll.child(18, 26042, 405, 92);
		scroll.child(19, 26043, 405, 102);
		// scroll.child(29, 26044, 405, 112); //Second Row
		scroll.child(20, 26045, 49, 152);
		scroll.child(21, 26046, 49, 162);
		// scroll.child(32, 26047, 49, 172);
		scroll.child(22, 26048, 138, 152);
		scroll.child(23, 26049, 138, 162);
		// scroll.child(35, 26050, 138, 172);
		scroll.child(24, 26051, 227, 152);
		scroll.child(25, 26052, 227, 162);
		// scroll.child(38, 26053, 227, 172);
		scroll.child(26, 26054, 316, 152);
		scroll.child(27, 26055, 316, 162);
		// scroll.child(41, 26056, 316, 172);
		scroll.child(28, 26057, 405, 152);
		scroll.child(29, 26058, 405, 162);
		// scroll.child(44, 26059, 405, 172); //Third Row
		scroll.child(30, 26060, 49, 212);
		scroll.child(31, 26061, 49, 222);
		// scroll.child(47, 26062, 49, 232);
		scroll.child(32, 26063, 138, 212);
		scroll.child(33, 26064, 138, 222);
		// scroll.child(50, 26065, 138, 232);
		scroll.child(34, 26066, 227, 212);
		scroll.child(35, 26067, 227, 222);
		// scroll.child(53, 26068, 227, 232);
		scroll.child(36, 26069, 316, 212);
		scroll.child(37, 26070, 316, 222);
		// scroll.child(56, 26071, 316, 232);
		scroll.child(38, 26072, 405, 212); // Forth Row
		scroll.child(39, 26073, 405, 222);
		// scroll.child(59, 26074, 405, 232);
		scroll.child(40, 26075, 49, 272);
		scroll.child(41, 26076, 49, 282);
		// roll.child(62, 26077, 49, 292);
		scroll.child(42, 26078, 138, 272);
		scroll.child(43, 26079, 138, 282);
		// scroll.child(65, 26080, 138, 292);
		scroll.child(44, 26081, 227, 272);
		scroll.child(45, 26082, 227, 282);
		// scroll.child(68, 26083, 227, 292);
		scroll.child(46, 26084, 316, 272);
		scroll.child(47, 26085, 316, 282);
		// scroll.child(71, 26086, 316, 292);
		scroll.child(48, 26087, 405, 272); // Fifth Row
		scroll.child(49, 26088, 405, 282);
		// scroll.child(74, 26089, 405, 292);
		scroll.child(50, 26090, 49, 332);
		scroll.child(51, 26091, 49, 342);
		// scroll.child(77, 26092, 49, 352);
		scroll.child(52, 26093, 138, 332);
		scroll.child(53, 26094, 138, 342);
		// scroll.child(80, 26095, 138, 352);
		scroll.child(54, 26096, 227, 332);
		scroll.child(55, 26097, 227, 342);
		// scroll.child(83, 26098, 227, 352);
		scroll.child(56, 26099, 32, 0); // PriceChecker method
		scroll.width = 480 - 30;
		scroll.height = 217;
		scroll.scrollMax = 500;
	}

	public static void equipmentTab(TextDrawingArea[] wid) {
		RSInterface Interface = interfaceCache[1644];
		addSprite(15101, 0, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15102, 1, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15109, 2, "Interfaces/Equipment/bl");// cheap hax
		removeConfig(21338);
		removeConfig(21344);
		removeConfig(21342);
		removeConfig(21341);
		removeConfig(21340);
		removeConfig(15103);
		removeConfig(15104);
		// Interface.children[23] = 15101;
		// Interface.childX[23] = 40;
		// Interface.childY[23] = 205;
		Interface.children[24] = 15102;
		Interface.childX[24] = 110;
		Interface.childY[24] = 205;
		Interface.children[25] = 15109;
		Interface.childX[25] = 39;
		Interface.childY[25] = 240;
		Interface.children[26] = 27650;
		Interface.childX[26] = 0;
		Interface.childY[26] = 0;
		Interface = addInterface(27650);

		/*addHoverButton(27651, "Interfaces/Equipment/BOX", 2, 40, 40,
				"Price-checker", -1, 27652, 1);*/
		addButton(27651, 2, "Interfaces/Equipment/BOX", "Price-checker");
	//	addHoveredButton(27652, "Interfaces/Equipment/HOVER", 2, 40, 40, 27658);

		addHoverButton(27653, "Interfaces/Equipment/BOX", 1, 40, 40,
				"Show Equipment Stats", -1, 27655, 1);
		addHoveredButton(27655, "Interfaces/Equipment/HOVER", 1, 40, 40, 27665);

		addHoverButton(27654, "Interfaces/Equipment/BOX", 3, 40, 40,
				"Show items kept on death", -1, 27657, 1);
		addHoveredButton(27657, "Interfaces/Equipment/HOVER", 3, 40, 40, 27666);

		setChildren(6, Interface);
		setBounds(27651, 75, 205, 0, Interface);
		//setBounds(27652, 75, 205, 1, Interface);
		setBounds(27653, 23, 205, 2, Interface);
		setBounds(27654, 127, 205, 3, Interface);
		setBounds(27655, 23, 205, 4, Interface);
		setBounds(27657, 127, 205, 5, Interface);
	}	

	public static void AddInterfaceButton(int id, int sid, String spriteName,
			String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}

	public static void AddInterfaceButton(int id, int sid, String spriteName,
			String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.inventoryHover = true;
	}

	public static void AddInterfaceButton(int i, int j, String name, int W,
			int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void AddInterfaceButton(int id, int sid, String spriteName,
			String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static void AddInterfaceButton(int i, int j, int hoverId,
			String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = hoverId;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	private static void AddInterfaceButton(int ID, int type, int hoverID,
			int dS, int eS, String NAME, int W, int H, String text,
			int configFrame, int configId) {
		RSInterface rsinterface = addInterface(ID);
		rsinterface.id = ID;
		rsinterface.parentID = ID;
		rsinterface.type = 5;
		rsinterface.atActionType = type;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = hoverID;
		rsinterface.sprite1 = imageLoader(dS, NAME);
		rsinterface.sprite2 = imageLoader(eS, NAME);
		rsinterface.width = W;
		rsinterface.height = H;
		rsinterface.anIntArray245 = new int[1];
		rsinterface.anIntArray212 = new int[1];
		rsinterface.anIntArray245[0] = 1;
		rsinterface.anIntArray212[0] = configId;
		rsinterface.valueIndexArray = new int[1][3];
		rsinterface.valueIndexArray[0][0] = 5;
		rsinterface.valueIndexArray[0][1] = configFrame;
		rsinterface.valueIndexArray[0][2] = 0;
		rsinterface.tooltip = text;
	}

	public static void addPrayer(int i, int configId, int configFrame,
			int requiredValues, int spriteID, String prayerName) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = 5608;
		tab.type = 5;
		tab.atActionType = 4;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = -1;
		tab.sprite1 = imageLoader(0, "PRAYERGLOW");
		tab.sprite2 = imageLoader(1, "PRAYERGLOW");
		tab.width = 34;
		tab.height = 34;
		tab.anIntArray245 = new int[1];
		tab.anIntArray212 = new int[1];
		tab.anIntArray245[0] = 1;
		tab.anIntArray212[0] = configId;
		tab.valueIndexArray = new int[1][3];
		tab.valueIndexArray[0][0] = 5;
		tab.valueIndexArray[0][1] = configFrame;
		tab.valueIndexArray[0][2] = 0;
		// tab.tooltip = "Activate@or2@ " + prayerName;
		tab.tooltip = "Select";
		RSInterface tab2 = addTabInterface(i + 1);
		tab2.id = i + 1;
		tab2.parentID = 5608;
		tab2.type = 5;
		tab2.atActionType = 0;
		tab2.contentType = 0;
		tab2.aByte254 = 0;
		tab2.hoverType = -1;
		tab2.sprite1 = imageLoader(spriteID, "Prayer/PRAYON");
		tab2.sprite2 = imageLoader(spriteID, "Prayer/PRAYOFF");
		tab2.width = 34;
		tab2.height = 34;
		tab2.anIntArray245 = new int[1];
		tab2.anIntArray212 = new int[1];
		tab2.anIntArray245[0] = 2;
		tab2.anIntArray212[0] = requiredValues + 1;
		tab2.valueIndexArray = new int[1][3];
		tab2.valueIndexArray[0][0] = 2;
		tab2.valueIndexArray[0][1] = 5;
		tab2.valueIndexArray[0][2] = 0;
	}

	public static void addBox(int id, int byte1, boolean filled, int color,
			String text) {
		RSInterface Interface = addInterface(id);
		Interface.id = id;
		Interface.parentID = id;
		Interface.type = 9;
		Interface.aByte254 = (byte) byte1;
		Interface.aBoolean227 = filled;
		Interface.hoverType = -1;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.textColor = color;
		Interface.message = text;
	}

	/**
	 * Adds your current character to an interface.
	 **/

	protected static void addOldPrayer(int id, String prayerName) {
		RSInterface rsi = interfaceCache[id];
		rsi.tooltip = "Activate@or2@ " + prayerName;
	}

	public static void addPrayerHover(int i, int hoverID, int prayerSpriteID,
			String hoverText) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.hoverType = hoverID;
		Interface.sprite2 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.sprite1 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.width = 34;
		Interface.height = 34;

		Interface = addTabInterface(hoverID);
		Interface.id = hoverID;
		Interface.parentID = 5608;
		Interface.type = 0;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.hoverType = -1;
		Interface.width = 512;
		Interface.height = 334;
		Interface.isMouseoverTriggered = true;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, Interface);
		setBounds(hoverID + 1, 0, 0, 0, Interface);
	}

	public static void magicTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(1151);
		RSInterface homeHover = addTabInterface(1196);
		RSInterface spellButtons = interfaceCache[12424];
		spellButtons.scrollMax = 0;
		spellButtons.height = 260;
		spellButtons.width = 190;
		int[] spellButton = { 1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249,
				1258, 1267, 1274, 1283, 1573, 1290, 1299, 1308, 1315, 1324,
				1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404,
				1583, 12038, 1414, 1421, 1430, 1437, 1446, 1453, 1460, 1469,
				15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503, 1512,
				1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446,
				12456, 6004, 18471 };
		tab.totalChildren(63);
		tab.child(0, 12424, 13, 24);
		for (int i1 = 0; i1 < spellButton.length; i1++) {
			int yPos = i1 > 34 ? 8 : 183;
			tab.child(1, 1195, 13, 24);
			tab.child(i1 + 2, spellButton[i1], 5, yPos);
			addButton(1195, 1, "Magic/Home", "Cast @gre@Home Teleport");
			RSInterface homeButton = interfaceCache[1195];
			homeButton.hoverType = 1196;
		}
		for (int i2 = 0; i2 < spellButton.length; i2++) {
			if (i2 < 60)
				spellButtons.childX[i2] = spellButtons.childX[i2] + 24;
			if (i2 == 6 || i2 == 12 || i2 == 19 || i2 == 35 || i2 == 41
					|| i2 == 44 || i2 == 49 || i2 == 51)
				spellButtons.childX[i2] = 0;
			spellButtons.childY[6] = 24;
			spellButtons.childY[12] = 48;
			spellButtons.childY[19] = 72;
			spellButtons.childY[49] = 96;
			spellButtons.childY[44] = 120;
			spellButtons.childY[51] = 144;
			spellButtons.childY[35] = 170;
			spellButtons.childY[41] = 192;
		}
		homeHover.isMouseoverTriggered = true;
		addText(1197, "Level 0: Home Teleport", tda, 1, 0xFE981F, true, true);
		RSInterface homeLevel = interfaceCache[1197];
		homeLevel.width = 174;
		homeLevel.height = 68;
		addText(1198, "A teleport which requires no", tda, 0, 0xAF6A1A, true,
				true);
		addText(18998, "runes and no required level that", tda, 0, 0xAF6A1A,
				true, true);
		addText(18999, "teleports you to the main land.", tda, 0, 0xAF6A1A,
				true, true);
		homeHover.totalChildren(4);
		homeHover.child(0, 1197, 3, 4);
		homeHover.child(1, 1198, 91, 23);
		homeHover.child(2, 18998, 91, 34);
		homeHover.child(3, 18999, 91, 45);

		addButton(19000, 1, "Magic/TELEPORT", "Cast @gre@Home Teleport");
		/*
		 * drawRune(19001, 7, "Death"); drawRune(19002, 9, "Chaos");
		 * drawRune(19003, 10, "Law"); addMagic3Rune(19004, 562, 560, 563, 3, 1,
		 * 9, 19002, 19001, 85, "Teleport to Target", "Teleport to BH Target",
		 * tda, 0, 0, 5); setBounds(19004, 135, 218, 63, tab);
		 */
		// tab.child(63, 19000, 135, 218);
	}

	public static void ancientMagicTab(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(12855);
		addButton(12856, 1, "Magic/Home", "Cast @gre@Home Teleport");
		RSInterface homeButton = interfaceCache[12856];
		homeButton.hoverType = 1196;
		int[] itfChildren = { 12856, 12939, 12987, 13035, 12901, 12861, 13045,
				12963, 13011, 13053, 12919, 12881, 13061, 12951, 12999, 13069,
				12911, 12871, 13079, 12975, 13023, 13087, 12929, 12891, 13095,
				1196, 12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012,
				13054, 12920, 12882, 13062, 12952, 13000, 13070, 12912, 12872,
				13080, 12976, 13024, 13088, 12930, 12892, 13096 };
		tab.totalChildren(itfChildren.length);
		for (int i1 = 0, xPos = 18, yPos = 8; i1 < itfChildren.length; i1++, xPos += 45) {
			if (xPos > 175) {
				xPos = 18;
				yPos += 28;
			}
			if (i1 < 25)
				tab.child(i1, itfChildren[i1], xPos, yPos);
			if (i1 > 24) {
				yPos = i1 < 41 ? 181 : 1;
				tab.child(i1, itfChildren[i1], 4, yPos);
			}
		}
	}

	public static void prayerTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(5608);
		RSInterface currentPray = interfaceCache[687];
		addSprite(5651, 0, "Prayer/PRAYER");
		currentPray.textColor = 0xFF981F;
		currentPray.textShadow = true;
		currentPray.message = "%1/%2";

		int[] ID1 = { 18016, 18017, 18018, 18019, 18020, 18021, 18022, 18023,
				18024, 18025, 18026, 18027, 18028, 18029, 18030, 18031, 18032,
				18033, 18034, 18035, 18036, 18037, 18038, 18039, 18040, 18041 };
		int[] X = { 8, 44, 80, 114, 150, 8, 44, 80, 116, 152, 8, 42, 78, 116,
				152, 8, 44, 80, 116, 150, 6, 44, 80, 116, 150, 6 };
		int[] Y = { 6, 6, 6, 4, 4, 42, 42, 42, 42, 42, 79, 76, 76, 78, 78, 114,
				114, 114, 114, 112, 148, 150, 150, 150, 148, 184 };

		int[] hoverIDs = { 18050, 18052, 18054, 18056, 18058, 18060, 18062,
				18064, 18066, 18068, 18070, 18072, 18074, 18076, 18078, 18080,
				18082, 18084, 18086, 18088, 18090, 18092, 18094, 18096, 18098,
				18100 };
		int[] hoverX = { 12, 8, 20, 12, 24, 2, 2, 6, 6, 50, 6, 6, 10, 6, 6, 5,
				5, 5, 5, 5, 18, 28, 28, 50, 1, 1 };
		int[] hoverY = { 42, 42, 42, 42, 42, 80, 80, 80, 80, 80, 118, 118, 118,
				118, 118, 150, 150, 150, 150, 150, 105, 80, 65, 65, 65, 110 };
		String[] hoverStrings = {
				"Level 01\nThick Skin\nIncreases your Defence by 5%",
				"Level 04\nBurst of Strength\nIncreases your Strength by 5%",
				"Level 07\nCharity of Thought\nIncreases your Attack by 5%",
				"Level 08\nSharp Eye\nIncreases your Ranged by 5%",
				"Level 09\nMystic Will\nIncreases your Magic by 5%",
				"Level 10\nRock Skin\nIncreases your Defence by 10%",
				"Level 13\nSuperhuman Strength\nIncreases your Strength by 10%",
				"Level 16\nImproved Reflexes\nIncreases your Attack by 10%",
				"Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints and Prayer",
				"Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat",
				"Level 25\nProtect Item\nKeep one extra item if you die",
				"Level 26\nHawk Eye\nIncreases your Ranged by 10%",
				"Level 27\nMystic Lore\nIncreases your Magic by 10%",
				"Level 28\nSteel Skin\nIncreases your Defence by 15%",
				"Level 31\nUltimate Strength\nIncreases your Strength by 15%",
				"Level 34\nIncredible Reflexes\nIncreases your Attack by 15%",
				"Level 37\nProtect from Magic\nProtection from magical attacks",
				"Level 40\nProtect from Missiles\nProtection from ranged attacks",
				"Level 43\nProtect from Melee\nProtection from close attacks",
				"Level 44\nEagle Eye\nIncreases your Ranged by 15%",
				"Level 45\nMystic Might\nIncreases your Magic by 15%",
				"Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die",
				"Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%",
				"Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponents Prayer",
				"Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18% and Attack by\n15%",
				"Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23% and Attack by\n20%" };

		int ID2[] = { 5609, 5610, 5611, 5612, 5613, 5614, 5615, 5616, 5617,
				5618, 5619, 5620, 5621, 5622, 5623, 683, 684, 685, 5632, 5633,
				5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643,
				5644, 686, 5645, 5649, 5647, 5648, 18000, 18001, 18002, 18003,
				18004, 18005, 18006, 18007, 18008, 18009, 18010, 18011, 18012,
				18013, 18014, 18015, 5651, 687 };
		int X2[] = { 6, 42, 78, 6, 42, 78, 114, 150, 6, 114, 150, 6, 42, 78,
				114, 42, 78, 114, 8, 44, 80, 8, 44, 80, 116, 152, 8, 116, 152,
				8, 44, 80, 116, 44, 80, 116, 114, 117, 150, 153, 42, 45, 78,
				81, 150, 153, 6, 9, 150, 157, 6, 8, 65, 14 };
		int Y2[] = { 4, 4, 4, 40, 40, 40, 40, 40, 76, 76, 76, 112, 112, 112,
				112, 148, 148, 148, 6, 6, 6, 42, 42, 42, 42, 42, 79, 78, 78,
				114, 114, 114, 114, 150, 150, 150, 4, 8, 4, 7, 76, 80, 76, 79,
				112, 116, 148, 151, 148, 151, 184, 194, 242, 244 };

		String[] oldPrayerNames = { "Thick Skin", "Burst of Strength",
				"Charity of Thought", "Rock Skin", "Superhuman Strength",
				"Improved Reflexes", "Rapid Restore", "Rapid Heal",
				"Protect Item", "Steel Skin", "Ultimate Strength",
				"Incredible Reflexes", "Protect from Magic",
				"Protect from Missiles", "Protect from Melee", "Retribution",
				"Redemption", "Smite" };
		addPrayer(18000, 0, 601, 7, 0, "Sharp Eye");
		addPrayer(18002, 0, 602, 8, 1, "Mystic Will");
		addPrayer(18004, 0, 603, 25, 2, "Hawk Eye");
		addPrayer(18006, 0, 604, 26, 3, "Mystic Lore");
		addPrayer(18008, 0, 605, 43, 4, "Eagle Eye");
		addPrayer(18010, 0, 606, 44, 5, "Mystic Might");
		addPrayer(18012, 0, 607, 59, 6, "Chivalry");
		addPrayer(18014, 0, 608, 69, 7, "Piety");

		for (int i = 0; i < 18; i++) {
			addOldPrayer(ID2[i], oldPrayerNames[i]);
		}

		for (int i = 0; i < 26; i++) {
			addPrayerHover(ID1[i], hoverIDs[i], i, hoverStrings[i]);
		}

		tab.totalChildren(106); // 54
		tab.child(52, 5651, 70, 242);
		for (int ii = 0; ii < 54; ii++) {
			tab.child(ii, ID2[ii], X2[ii], Y2[ii]);
		}

		int frame = 54;
		int frame2 = 0;
		for (int i : ID1) {
			tab.child(frame, i, X[frame2], Y[frame2]);
			frame++;
			frame2++;
		}

		int frame3 = 0;
		for (int i : hoverIDs) {
			tab.child(frame, i, hoverX[frame3], hoverY[frame3]);
			frame++;
			frame3++;
		}
	}

	public static void addAttackStyleButton2(int id, int sprite, int setconfig,
			int width, int height, String s, int hoverID, int hW, int hH,
			String hoverText, TextDrawingArea[] TDA) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.hoverType = hoverID;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
		rsi = addInterface(hoverID);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.hoverType = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addConfigSprite(int id, int spriteId, int spriteId2,
			int state, int config) {
		RSInterface widget = addTabInterface(id);
		widget.id = id;
		widget.parentID = id;
		widget.type = 5;
		widget.atActionType = 0;
		widget.contentType = 0;
		widget.width = 512;
		widget.height = 334;
		widget.aByte254 = 0;
		widget.hoverType = -1;
		widget.anIntArray245 = new int[1];
		widget.anIntArray212 = new int[1];
		widget.anIntArray245[0] = 1;
		widget.anIntArray212[0] = state;
		widget.valueIndexArray = new int[1][3];
		widget.valueIndexArray[0][0] = 5;
		widget.valueIndexArray[0][1] = config;
		widget.valueIndexArray[0][2] = 0;
		widget.sprite1 = spriteId < 0 ? null : Client.cacheSprite2[spriteId];
		widget.sprite2 = spriteId2 < 0 ? null : Client.cacheSprite2[spriteId2];
	}

	public static void addSprites(int ID, int i, int i2, String name,
			int configId, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.id = ID;
		Tab.parentID = ID;
		Tab.type = 5;
		Tab.atActionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.aByte254 = (byte) 0;
		Tab.hoverType = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];// require
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configId;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(i, name);
		Tab.sprite2 = imageLoader(i2, name);
	}

	public static void addBankHover(int interfaceID, int actionType,
			int hoverid, int spriteId, int spriteId2, String NAME, int Width,
			int Height, int configFrame, int configId, String Tooltip,
			int hoverId2, int hoverSpriteId, int hoverSpriteId2,
			String hoverSpriteName, int hoverId3, String hoverDisabledText,
			String hoverEnabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.aByte254 = 0;
		hover.hoverType = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.sprite2 = imageLoader(spriteId2, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.anIntArray245 = new int[1];
		hover.anIntArray212 = new int[1];
		hover.anIntArray245[0] = 1;
		hover.anIntArray212[0] = configId;
		hover.valueIndexArray = new int[1][3];
		hover.valueIndexArray[0][0] = 5;
		hover.valueIndexArray[0][1] = configFrame;
		hover.valueIndexArray[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.hoverType = -1;
		addSprites(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName,
				configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText,
				configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addBankHover1(int interfaceID, int actionType,
			int hoverid, int spriteId, String NAME, int Width, int Height,
			String Tooltip, int hoverId2, int hoverSpriteId,
			String hoverSpriteName, int hoverId3, String hoverDisabledText,
			int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.aByte254 = 0;
		hover.hoverType = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.hoverType = -1;
		addSprites(hoverId2, hoverSpriteId, hoverSpriteId, hoverSpriteName, 0,
				0);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText,
				hoverDisabledText, 0, 0);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addHover(int i, int aT, int cT, int hoverid, int sId,
			String NAME, int W, int H, String tip) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.id = i;
		rsinterfaceHover.parentID = i;
		rsinterfaceHover.type = 5;
		rsinterfaceHover.atActionType = aT;
		rsinterfaceHover.contentType = cT;
		rsinterfaceHover.isMouseoverTriggereds = hoverid;
		rsinterfaceHover.sprite1 = imageLoader(sId, NAME);
		rsinterfaceHover.sprite2 = imageLoader(sId, NAME);
		rsinterfaceHover.width = W;
		rsinterfaceHover.height = H;
		rsinterfaceHover.tooltip = tip;
	}

	public static void addHovered(int i, int j, String imageName, int w, int h,
			int IMAGEID) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.parentID = i;
		rsinterfaceHover.id = i;
		rsinterfaceHover.type = 0;
		rsinterfaceHover.atActionType = 0;
		rsinterfaceHover.width = w;
		rsinterfaceHover.height = h;
		rsinterfaceHover.isMouseoverTriggered = true;
		rsinterfaceHover.isMouseoverTriggereds = -1;
		addSprite(IMAGEID, j, imageName);
		setChildren(1, rsinterfaceHover);
		setBounds(IMAGEID, 0, 0, 0, rsinterfaceHover);
	}

	public boolean hasExamine = true;

	public static void addTextButton(int i, String s, String tooltip, int k,
			boolean l, boolean m, TextDrawingArea[] TDA, int j, int w) {
		RSInterface rsinterface = addInterface(i);
		rsinterface.parentID = i;
		rsinterface.id = i;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = w;
		rsinterface.height = 16;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = (byte) 0xFF981F;
		rsinterface.hoverType = -1;
		rsinterface.centerText = l;
		rsinterface.textShadow = m;
		rsinterface.textDrawingAreas = TDA[j];
		rsinterface.message = s;
		rsinterface.aString228 = "";
		rsinterface.anInt219 = 0xFF981F;
		rsinterface.textColor = 0xFF981F;
		rsinterface.tooltip = tooltip;
	}

	public static void slayerInterface(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(41000);
		addSprite(41001, 1, "Slayer/SPRITE");
		addHoverButton(41002, "Slayer/SPRITE", 4, 16, 16, "Close window", 0,
				41003, 1);
		addHoveredButton(41003, "Slayer/SPRITE", 5, 16, 16, 41004);
		addHoverButton(41005, "", 0, 85, 20, "Buy", 0, 41006, 1);
		addHoverButton(41007, "", 0, 85, 20, "Learn", 0, 41008, 1);
		addHoverButton(41009, "", 0, 85, 20, "Assignment", 0, 41010, 1);
		addText(41011, "Slayer points: ", tda, 3, 0xFF981F, true);
		addTextButton(41012,
				"Slayer experience                           (50 points)",
				"Buy Slayer Experience", 0xFF981F, false, true, tda, 1, 400);
		addTextButton(41013,
				"Slayer's respite                             (25 points)",
				"Buy Slayer's Respite", 0xFF981F, false, true, tda, 1, 401);
		addTextButton(41014,
				"Slayer darts                                     (35 points)",
				"Buy Slayer Darts", 0xFF981F, false, true, tda, 1, 402);
		addTextButton(41015,
				"Broad arrows                                    (25 points)",
				"Buy Broad Arrows", 0xFF981F, false, true, tda, 1, 403);
		setChildren(11, rsInterface);
		rsInterface.child(0, 41001, 12, 10);
		rsInterface.child(1, 41002, 473, 20);
		rsInterface.child(2, 41003, 473, 20);
		rsInterface.child(3, 41005, 21, 23);
		rsInterface.child(4, 41007, 107, 23);
		rsInterface.child(5, 41009, 193, 23);
		rsInterface.child(6, 41011, 170, 74);
		rsInterface.child(7, 41012, 124, 128);
		rsInterface.child(8, 41013, 125, 160);
		rsInterface.child(9, 41014, 125, 190);
		rsInterface.child(10, 41015, 124, 220);

	}

	public static void slayerInterfaceSub1(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(41500);
		addSprite(41501, 2, "Slayer/SPRITE");
		addHoverButton(41502, "Slayer/SPRITE", 4, 16, 16, "Close window", 0,
				41503, 1);
		addHoveredButton(41503, "Slayer/SPRITE", 5, 16, 16, 41504);
		addHoverButton(41505, "", 0, 85, 20, "Buy", 0, 41506, 1);
		addHoverButton(41507, "", 0, 85, 20, "Learn", 0, 41508, 1);
		addHoverButton(41509, "", 0, 85, 20, "Assignment", 0, 41510, 1);
		addText(41511, "Slayer points: ", tda, 3, 0xFF981F, true);
		setChildren(7, rsInterface);
		rsInterface.child(0, 41501, 12, 10);
		rsInterface.child(1, 41502, 473, 20);
		rsInterface.child(2, 41503, 473, 20);
		rsInterface.child(3, 41505, 21, 23);
		rsInterface.child(4, 41507, 107, 23);
		rsInterface.child(5, 41509, 193, 23);
		rsInterface.child(6, 41511, 170, 74);
	}

	public static void slayerInterfaceSub2(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(42000);
		addSprite(42001, 3, "Slayer/SPRITE");
		addHoverButton(42002, "Slayer/SPRITE", 4, 16, 16, "Close window", 0,
				42003, 1);
		addHoveredButton(42003, "Slayer/SPRITE", 5, 16, 16, 42004);
		addHoverButton(42005, "", 0, 85, 20, "Buy", 0, 42006, 1);
		addHoverButton(42007, "", 0, 85, 20, "Learn", 0, 42008, 1);
		addHoverButton(42009, "", 0, 85, 20, "Assignment", 0, 42010, 1);
		addText(42011, "Slayer points: ", tda, 3, 0xFF981F, true);
		addTextButton(42012, "Cancel task",
				"Temporarily cancel your current slayer task", 0xFF981F, false,
				true, tda, 1, 300);
		addTextButton(42013, "Remove task permanently",
				"Permanently remove this monster as a task", 0xFF981F, false,
				true, tda, 1, 305);
		addText(42014, "line 1", tda, 1, 0xFF981F, true);
		addText(42015, "line 2", tda, 1, 0xFF981F, true);
		addText(42016, "line 3", tda, 1, 0xFF981F, true);
		addText(42017, "line 4", tda, 1, 0xFF981F, true);
		addButton(42018, 6, "Slayer/SPRITE", "Delete removed slayer task");
		addButton(42019, 6, "Slayer/SPRITE", "Delete removed slayer task");
		addButton(42020, 6, "Slayer/SPRITE", "Delete removed slayer task");
		addButton(42021, 6, "Slayer/SPRITE", "Delete removed slayer task");
		setChildren(17, rsInterface);
		rsInterface.child(0, 42001, 12, 10);
		rsInterface.child(1, 42002, 473, 20);
		rsInterface.child(2, 42003, 473, 20);
		rsInterface.child(3, 42005, 21, 23);
		rsInterface.child(4, 42007, 107, 23);
		rsInterface.child(5, 42009, 193, 23);
		rsInterface.child(6, 42011, 170, 74);
		rsInterface.child(7, 42012, 71, 127);
		rsInterface.child(8, 42013, 71, 146);
		rsInterface.child(9, 42014, 108, 216);
		rsInterface.child(10, 42015, 108, 234);
		rsInterface.child(11, 42016, 108, 252);
		rsInterface.child(12, 42017, 108, 270);
		rsInterface.child(13, 42018, 303, 215);
		rsInterface.child(14, 42019, 303, 233);
		rsInterface.child(15, 42020, 303, 251);
		rsInterface.child(16, 42021, 303, 269);
	}

	public static void nightmareZone(TextDrawingArea[] tda) {
		RSInterface nz = addInterface(920);
		addSprite(921, 0, "Nightmare/SPRITE");
		addText(922, "0", tda, 0, 0xFF981F, true, true);
		addText(923, "Points:", tda, 0, 0xFF981F, false, true);
		nz.totalChildren(3);
		nz.child(0, 921, 460, 14);
		nz.child(1, 922, 481, 29);
		nz.child(2, 923, 465, 19);
	}

	public boolean newScroller;

	public static void addHoverText(int id, String text, String tooltip,
			TextDrawingArea tda[], int idx, int color, boolean center,
			boolean textShadowed, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = -1;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0xffffff;
		rsinterface.anInt239 = 0;
		rsinterface.tooltip = tooltip;
	}

	public static void optionTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(904);
		RSInterface energy = interfaceCache[149];
		energy.textColor = 0xff9933;
		addSprite(905, 9, "/Options/SPRITE");
		addSprite(907, 18, "/Options/SPRITE");
		addSprite(909, 29, "/Options/SPRITE");
		addSprite(951, 32, "/Options/SPRITE");
		addSprite(953, 33, "/Options/SPRITE");
		addSprite(955, 34, "/Options/SPRITE");
		addSprite(947, 36, "/Options/SPRITE");
		addSprite(949, 35, "/Options/SPRITE");
		addSprite(949, 35, "/Options/SPRITE");
		// run button here
		addConfigButton(152, 904, 30, 31, "/Options/SPRITE", 40, 40,
				"Toggle-run", 1, 5, 173);
		addConfigButton(906, 904, 10, 14, "/Options/SPRITE", 32, 16, "Dark", 1,
				5, 166);
		addConfigButton(908, 904, 11, 15, "/Options/SPRITE", 32, 16, "Normal",
				2, 5, 166);
		addConfigButton(910, 904, 12, 16, "/Options/SPRITE", 32, 16, "Bright",
				3, 5, 166);
		addConfigButton(912, 904, 13, 17, "/Options/SPRITE", 32, 16,
				"Very Bright", 4, 5, 166);
		addConfigButton(930, 904, 19, 24, "/Options/SPRITE", 26, 16,
				"Music Off", 4, 5, 168);
		addConfigButton(931, 904, 20, 25, "/Options/SPRITE", 26, 16,
				"Music Level-1", 3, 5, 168);
		addConfigButton(932, 904, 21, 26, "/Options/SPRITE", 26, 16,
				"Music Level-2", 2, 5, 168);
		addConfigButton(933, 904, 22, 27, "/Options/SPRITE", 26, 16,
				"Music Level-3", 1, 5, 168);
		addConfigButton(934, 904, 23, 28, "/Options/SPRITE", 24, 16,
				"Music Level-4", 0, 5, 168);
		addConfigButton(941, 904, 19, 24, "/Options/SPRITE", 26, 16,
				"Sound Effects Off", 4, 5, 169);
		addConfigButton(942, 904, 20, 25, "/Options/SPRITE", 26, 16,
				"Sound Effects Level-1", 3, 5, 169);
		addConfigButton(943, 904, 21, 26, "/Options/SPRITE", 26, 16,
				"Sound Effects Level-2", 2, 5, 169);
		addConfigButton(944, 904, 22, 27, "/Options/SPRITE", 26, 16,
				"Sound Effects Level-3", 1, 5, 169);
		addConfigButton(945, 904, 23, 28, "/Options/SPRITE", 24, 16,
				"Sound Effects Level-4", 0, 5, 169);
		addConfigButton(913, 904, 30, 31, "/Options/SPRITE", 40, 40,
				"Toggle-Orbs", 0, 5, 170);
		addConfigButton(915, 904, 30, 31, "/Options/SPRITE", 40, 40,
				"Toggle-Chat Effects", 0, 5, 171);
		addConfigButton(957, 904, 30, 31, "/Options/SPRITE", 40, 40,
				"Toggle-Split Private Chat", 1, 5, 287);
		addConfigButton(12464, 904, 30, 31, "/Options/SPRITE", 40, 40,
				"Toggle-Accept Aid", 0, 5, 427);
		addButton(17255, 0, "/Options/OTHER", "");
		tab.totalChildren(29);
		int x = 0;
		int y = 2;
		tab.child(0, 905, 13 + x, 10 + y);
		tab.child(1, 906, 48 + x, 18 + y);
		tab.child(2, 908, 80 + x, 18 + y);
		tab.child(3, 910, 112 + x, 18 + y);
		tab.child(4, 912, 144 + x, 18 + y);
		tab.child(5, 907, 14 + x, 55 + y);
		tab.child(6, 930, 49 + x, 61 + y);
		tab.child(7, 931, 75 + x, 61 + y);
		tab.child(8, 932, 101 + x, 61 + y);
		tab.child(9, 933, 127 + x, 61 + y);
		tab.child(10, 934, 151 + x, 61 + y);
		tab.child(11, 909, 13 + x, 99 + y);
		tab.child(12, 941, 49 + x, 104 + y);
		tab.child(13, 942, 75 + x, 104 + y);
		tab.child(14, 943, 101 + x, 104 + y);
		tab.child(15, 944, 127 + x, 104 + y);
		tab.child(16, 945, 151 + x, 104 + y);
		tab.child(17, 913, 15, 153);
		tab.child(18, 955, 19, 159);
		tab.child(19, 915, 75, 153);
		tab.child(20, 953, 79, 160);
		tab.child(21, 957, 135, 153);
		tab.child(22, 951, 139, 159);
		tab.child(23, 12464, 15, 208);
		tab.child(24, 949, 20, 213);
		tab.child(25, 152, 75, 208);
		tab.child(26, 947, 87, 212);
		tab.child(27, 149, 80, 231);
		tab.child(28, 17255, 135, 208);
	}

	public static void questTab(TextDrawingArea[] TDA) {
		RSInterface Interface = addInterface(638);
		setChildren(4, Interface);
		addText(29155, "@cr2@ Unknown", 0xFF981F, false, true, 52, TDA, 3);
		addButton(29156, 1, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Achievements", 1);
		addSprite(29157, 0, "Interfaces/QuestTab/QUEST");
		setBounds(29155, 10, 5, 0, Interface);
		setBounds(29156, 165, 5, 1, Interface);
		setBounds(29157, 3, 24, 2, Interface);
		setBounds(29160, 5, 29, 3, Interface);
		Interface = addInterface(29160);
		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 1700;
		Interface.newScroller = false;
		setChildren(105, Interface);
		addText(29161, "Player", 0xFF981F, false, true, 52, TDA, 3);
		addText(663, "Player", 0xFF981F, false, true, 52, TDA, 3);
		addHoverText(29162, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29163, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29164, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29165, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29166, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		setBounds(29161, 4, 4, 0, Interface);
		setBounds(29162, 8, 22, 1, Interface);
		setBounds(29163, 8, 35, 2, Interface);
		setBounds(29164, 8, 48, 3, Interface);
		setBounds(29165, 8, 61, 4, Interface);
		setBounds(29166, 8, 74, 5, Interface);
		setBounds(663, 4, 90, 6, Interface);
		int Ypos = 108;
		int frameID = 7;
		for (int iD = 29167; iD <= 29264; iD++) {
			addHoverText(iD, "", "View"/* "View Quest Journal, "+iD */, TDA,
					0, 0xff0000, false, true, 150);
			setBounds(iD, 8, Ypos, frameID, Interface);
			frameID++;
			Ypos += 13;
			Ypos++;
		}
		Interface = addInterface(29265);
		try {
			setChildren(4, Interface);
			addText(29266, "        Achievements", 0xFF981F, false, true, -1,
					TDA, 2);
			addButton(29267, 2, "Interfaces/QuestTab/QUEST", 18, 18,
					"Swap to Quests", 1);
			addSprite(29269, 0, "Interfaces/QuestTab/QUEST");
			setBounds(29266, 10, 5, 0, Interface);
			setBounds(29267, 165, 5, 1, Interface);
			setBounds(29269, 3, 24, 2, Interface);
			setBounds(29268, 5, 29, 3, Interface);
			Interface = addInterface(29268);
			Interface.height = 214;
			Interface.width = 165;
			Interface.scrollMax = 1700;
			Interface.newScroller = false;
			setChildren(29, Interface);
			int y = 6;
			setBounds(29295, 8, y, 0, Interface);
			y += 15;
			setBounds(29287, 8, y, 1, Interface);
			y += 15;
			setBounds(29305, 8, y, 2, Interface);
			y += 15;
			setBounds(29306, 8, y, 3, Interface);
			y += 15;
			setBounds(29307, 8, y, 4, Interface);
			y += 15;
			setBounds(29308, 8, y, 5, Interface);
			y += 15;
			setBounds(29309, 8, y, 6, Interface);
			y += 15;
			setBounds(29310, 8, y, 7, Interface);
			y += 15;
			setBounds(29311, 8, y, 8, Interface);
			y += 15;
			setBounds(29312, 8, y, 9, Interface);
			y += 15;
			setBounds(29313, 8, y, 10, Interface);
			y += 15;
			setBounds(29314, 8, y, 11, Interface);
			y += 15;
			setBounds(29315, 8, y, 12, Interface);
			y += 15;
			setBounds(29316, 8, y, 13, Interface);
			y += 15;
			setBounds(29317, 8, y, 14, Interface);
			y += 15;
			setBounds(29318, 8, y, 15, Interface);
			y += 15;
			setBounds(29319, 8, y, 16, Interface);
			y += 15;
			setBounds(29320, 8, y, 17, Interface);
			y += 15;
			setBounds(29321, 8, y, 18, Interface);
			y += 15;
			setBounds(29322, 8, y, 19, Interface);
			y += 15;
			setBounds(29323, 8, y, 20, Interface);
			y += 15;
			setBounds(29324, 8, y, 21, Interface);
			y += 15;
			setBounds(29325, 8, y, 22, Interface);
			y += 15;
			setBounds(29326, 8, y, 23, Interface);
			y += 15;
			setBounds(29327, 8, y, 24, Interface);
			y += 15;
			setBounds(29328, 8, y, 25, Interface);
			y += 15;
			setBounds(29329, 8, y, 26, Interface);
			y += 15;
			setBounds(29330, 8, y, 27, Interface);
			y += 15;
			setBounds(29331, 8, y, 28, Interface);
			y += 15;
			addHoverText(29295, "Tasks Completed 0/24", "View Achievements",
					TDA, 0, 65280, false, true, 150);
			addHoverText(29287, "Easy Tasks", "View Achievements", TDA, 0,
					0xFF981F, false, true, 150);
			addHoverText(29305, "Kill each boss 50 times", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29306, "Catch 500 Sharks", "View Achievements", TDA,
					0, 0xff0000, false, true, 150);
			addHoverText(29307, "Craft 200 Amulet of Glories",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29308, "Chop 200 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29309, "Burn 200 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29310, "Fletch 200 Magic Longbow's",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29311, "Grow 200 Herbs", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29312, "Medium Tasks", "View Achievements", TDA, 0,
					0xFF981F, false, true, 150);
			addHoverText(29313, "Kill each boss 500 times",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29314, "Catch 1000 Sharks", "View Achievements", TDA,
					0, 0xff0000, false, true, 150);
			addHoverText(29315, "Craft 1000 Amulet of Glories",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29316, "Chop 1000 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29317, "Burn 1000 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29318, "Fletch 1000 Magic Longbow's",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29319, "Grow 1000 Herbs", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29320, "Complete 25 Slayer Tasks",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29321, "Complete 15 Duo Slayer Tasks",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29322, "Hard Tasks", "View Achievements", TDA, 0,
					0xFF981F, false, true, 150);
			addHoverText(29323, "Kill each boss 1000 times",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29324, "Catch 2000 Sharks", "View Achievements", TDA,
					0, 0xff0000, false, true, 150);
			addHoverText(29325, "Craft 2000 Amulet of Glories",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29326, "Chop 2000 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29327, "Burn 2000 Magic Logs", "View Achievements",
					TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29328, "Fletch 2000 Magic Longbow's",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29329, "Grow 2000 Herbs", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29330, "Complete 50 Slayer Tasks",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
			addHoverText(29331, "Complete 30 Duo Slayer Tasks",
					"View Achievements", TDA, 0, 0xff0000, false, true, 150);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addClickableText(int id, String text, String tooltip,
			TextDrawingArea tda[], int idx, int color, boolean center,
			boolean shadow, int width) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 1;
		tab.width = width;
		tab.height = 11;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.aString228 = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.anInt216 = 0xffffff;
		tab.anInt239 = 0;
		tab.tooltip = tooltip;
	}

	public static void optionTab1(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(904);
		RSInterface energy = interfaceCache[149];
		energy.textColor = 0xff9933;
		addSprite(949, 35, "Options/SPRITE");
		addSprite(950, 36, "Options/SPRITE");
		addSprite(952, 37, "Options/SPRITE");
		addSprite(954, 38, "Options/SPRITE");
		addSprite(956, 29, "Options/SPRITE");
		addSprite(958, 39, "Options/SPRITE");
		addSprite(960, 40, "Options/SPRITE");
		addSprite(961, 41, "Options/SPRITE");
		addSprite(962, 9, "Options/SPRITE");
		addSprite(963, 17, "Options/SPRITE");
		addSprite(964, 12, "Options/SPRITE");
		addSprite(965, 11, "Options/SPRITE");
		addSprite(966, 10, "Options/SPRITE");
		addSprite(968, 42, "Options/SPRITE");
		addSprite(971, 43, "Options/SPRITE");
		addSprite(972, 44, "Options/SPRITE");
		addConfigButton(152, 904, 30, 31, "Options/SPRITE", 40, 40,
				"Toggle Run", 1, 5, 173);
		addConfigButton(12464, 904, 31, 30, "Options/SPRITE", 40, 40,
				"Toggle Accept Aid", 0, 5, 427);
		addConfigButton(951, 904, 31, 30, "Options/SPRITE", 40, 40,
				"Open House Options", 0, 5, 427);
		addConfigButton(953, 904, 30, 31, "Options/SPRITE", 40, 40, "Display",
				0, 5, 427);
		addConfigButton(955, 904, 31, 30, "Options/SPRITE", 40, 40, "Audio", 0,
				5, 427);
		addConfigButton(957, 904, 31, 30, "Options/SPRITE", 40, 40, "Chat", 0,
				5, 427);
		addConfigButton(959, 904, 31, 30, "Options/SPRITE", 40, 40, "Controls",
				0, 5, 427);
		addConfigButton(967, 904, 30, 31, "Options/SPRITE", 40, 40,
				"Toggle Data Orbs", 0, 5, 427);
		addConfigButton(969, 904, 30, 31, "Options/SPRITE", 40, 40,
				"Toggle roof-removal", 0, 5, 427);
		addConfigButton(970, 904, 31, 30, "Options/SPRITE", 40, 40,
				"Toggle 'Remaining XP'", 0, 5, 427);
		tab.totalChildren(27);
		tab.child(0, 12464, 19, 220);
		tab.child(1, 949, 23, 225);
		tab.child(2, 152, 75, 220);
		tab.child(3, 149, 80, 243);
		tab.child(4, 950, 86, 224);
		tab.child(5, 951, 131, 220);
		tab.child(6, 952, 135, 224);
		tab.child(7, 953, 6, 1);
		tab.child(8, 954, 10, 5);
		tab.child(9, 955, 52, 1);
		tab.child(10, 956, 57, 5);
		tab.child(11, 957, 98, 1);
		tab.child(12, 958, 102, 5);
		tab.child(13, 959, 144, 1);
		tab.child(14, 960, 149, 5);
		tab.child(15, 961, 0, 42);
		tab.child(16, 962, 9, 70);
		tab.child(17, 963, 149, 80);
		tab.child(18, 964, 117, 80);
		tab.child(19, 965, 85, 80);
		tab.child(20, 966, 53, 80);
		tab.child(21, 967, 19, 121);
		tab.child(22, 968, 23, 125);
		tab.child(23, 969, 75, 121);
		tab.child(24, 970, 131, 121);
		tab.child(25, 971, 79, 125);
		tab.child(26, 972, 135, 125);
	}

	public static void edgevilleHomeTeleport(TextDrawingArea[] TDA) {
		RSInterface rsi = interfaceCache[21741];
		rsi.atActionType = 1;
		rsi.tooltip = "Cast @gre@Edgeville Home Teleport";
	}

	public static void addButton(int id, int sid, String spriteName,
			String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}

	public String popupString;

	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 8;
		rsi.popupString = text;
	}

	public static void addTooltip(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.isMouseoverTriggered = true;
		rsi.hoverType = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
	}

	private static Sprite CustomSpriteLoader(int id, String s) {
		long l = (TextClass.method585(s) << 8) + (long) id;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null) {
			return sprite;
		}
		try {
			sprite = new Sprite("/Attack/" + id + s);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 512;
		rsi.height = 334;
		return rsi;
	}

	public static void addText(int id, String text, TextDrawingArea tda[],
			int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void textColor(int id, int color) {
		RSInterface rsi = interfaceCache[id];
		rsi.textColor = color;
	}

	public static void textSize(int id, TextDrawingArea tda[], int idx) {
		RSInterface rsi = interfaceCache[id];
		rsi.textDrawingAreas = tda[idx];
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2,
			String sprites) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = method207(sprite1, aClass44, sprites);
		rsi.sprite2 = method207(sprite2, aClass44, sprites);
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static void sprite1(int id, int sprite) {
		RSInterface class9 = interfaceCache[id];
		class9.sprite1 = CustomSpriteLoader(sprite, "");
	}

	public static void addActionButton(int id, int sprite, int sprite2,
			int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		if (sprite2 == sprite)
			rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		else
			rsi.sprite2 = CustomSpriteLoader(sprite2, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.atActionType = 1;
		rsi.width = width;
		rsi.hoverType = 52;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addToggleButton(int id, int sprite, int setconfig,
			int width, int height, String s) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.hoverType = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public void totalChildren(int id, int x, int y) {
		children = new int[id];
		childX = new int[x];
		childY = new int[y];
	}

	public static void removeSomething(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public static void Bank(TextDrawingArea[] wid) {
		RSInterface Interface = addTabInterface(24959);
		setChildren(46, Interface);
		addSprite(5293, 0, "Interfaces/Bank/BANK");
		setBounds(5293, 13, 1, 0, Interface);
		addHover(5384, 3, 0, 5380, 1, "Interfaces/Bank/BANK", 25, 25,
				"Close Window");
		//
		addHovered(5380, 2, "Interfaces/Bank/BANK", 25, 25, 5379);
		setBounds(5384, 472, 8, 3, Interface);
		setBounds(5380, 472, 8, 4, Interface);
		//

		addText(24049, "Insert", wid, 1, 16750623, true, true);
		setBounds(24049, 125, 307, 43, Interface);

		setBounds(8131, 89, 298, 5, Interface);// Insert
		setBounds(0, 9000, 9000, 6, Interface);
		addText(24047, "Swap", wid, 1, 16750623, true, true);
		setBounds(24047, 54, 307, 44, Interface);
		setBounds(8130, 19, 298, 7, Interface);// Swap
		setBounds(0, 9000, 9000, 8, Interface);
		setBounds(5387, 212, 298, 9, Interface);// item
		setBounds(5386, 282, 298, 10, Interface);// Note
		setBounds(0, 9000, 9000, 11, Interface);
		addText(24055, "Note", wid, 1, 16750623, true, true);
		setBounds(24055, 316, 307, 45, Interface);

		addBankHover1(22012, 5, 22013, 17, "Interfaces/Bank/BANK", 36, 36,
				"Search", 22014, 18, "Bank/BANK", 22015,
				"Empty your backpack into\nyour bank", 0, 20);
		setBounds(0, 9000, 9000, 12, Interface);
		setBounds(0, 9000, 9000, 13, Interface);

		addBankHover1(22016, 5, 22017, 19, "Interfaces/Bank/BANK", 36, 36,
				"Deposit inventory", 23018, 20, "Bank/BANK", 23019,
				"Empty the items your are\nwearing into your bank", 0, 20);
		setBounds(22016, 423, 290, 14, Interface);
		setBounds(22017, 423, 290, 15, Interface);
		addBankHover1(23020, 5, 23021, 21, "Interfaces/Bank/BANK", 36, 36,
				"Deposit worn items", 23022, 22, "Bank/BANK", 23023,
				"Empty your BoB's inventory\ninto your bank", 0, 20);
		setBounds(23020, 459, 290, 16, Interface);
		setBounds(23021, 459, 290, 17, Interface);
		setBounds(5383, 195, 11, 1, Interface);
		setBounds(5385, 27, 78, 2, Interface);

		addText(24046, "Item", wid, 1, 16750623, true, true);
		setBounds(24046, 248, 307, 42, Interface);

		addButton(24004, 1, "Interfaces/Bank/SEARCH", "Search bank for items");
		setBounds(24004, 387, 290, 41, Interface);

		addButton(22024, 0, "Interfaces/BANK/TAB", "View all items");
		setBounds(22024, 57, 36, 18, Interface);
		addButton(22025, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22025, 97, 37, 19, Interface);
		addButton(22026, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22026, 137, 37, 20, Interface);
		addButton(22027, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22027, 177, 37, 21, Interface);
		addButton(22028, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22028, 217, 37, 22, Interface);
		addButton(22029, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22029, 257, 37, 23, Interface);
		addButton(22030, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22030, 297, 37, 24, Interface);
		addButton(22031, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22031, 337, 37, 25, Interface);
		addButton(22032, 4, "Interfaces/BANK/TAB", "New tab");
		setBounds(22032, 377, 37, 26, Interface);
		addText(22033, "134", wid, 0, 16750623, true, true);
		setBounds(22033, 30, 7, 27, Interface);
		addText(22034, "496", wid, 0, 16750623, true, true);
		setBounds(22034, 30, 19, 28, Interface);
		addBankItem(22035, false);
		setBounds(22035, 102, 40, 29, Interface);
		addBankItem(22036, false);
		setBounds(22036, 142, 40, 30, Interface);
		addBankItem(22037, false);
		setBounds(22037, 182, 40, 31, Interface);
		addBankItem(22038, false);
		setBounds(22038, 222, 40, 32, Interface);
		addBankItem(22039, false);
		setBounds(22039, 262, 40, 33, Interface);
		addBankItem(22040, false);
		setBounds(22040, 302, 40, 34, Interface);
		addBankItem(22041, false);
		setBounds(22041, 342, 40, 35, Interface);
		addBankItem(22042, false);
		setBounds(22042, 382, 40, 36, Interface);

		addText(27000, "0", 16750623, false, true, 52, wid, 1);
		addText(27001, "0", 16750623, false, true, 52, wid, 1);
		addText(27002, "0", 16750623, false, true, 52, wid, 1);
		addText(22043, "__", wid, 0, 16750623, true, true);
		setBounds(22043, 30, 10, 37, Interface);
		addSprite(22044, 24, "Interfaces/Bank/BANK");
		setBounds(22044, 463, 42, 38, Interface);
		addText(22045, "Withdraw as:", wid, 1, 16750623, true, true);
		setBounds(22045, 283, 290, 39, Interface);
		addText(22046, "Rearrange mode:", wid, 1, 16750623, true, true);
		setBounds(22046, 102, 290, 40, Interface);
		Interface = interfaceCache[5385];
		Interface.height = 206;
		Interface.width = 452;
		Interface = interfaceCache[5382];
		Interface.width = 8;
		Interface.invSpritePadX = 17;
		Interface.height = 35;

	}

	public static void addBankItem(int index, Boolean hasOption) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[5];
		rsi.spritesX = new int[20];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];
		rsi.spritesY = new int[20];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		rsi.hasExamine = false;

		rsi.invSpritePadX = 24;
		rsi.invSpritePadY = 24;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = 24959;
		rsi.id = index;
		rsi.type = 13;
	}

	public static void Skill(TextDrawingArea[] TDA) {
		skillInterface(19746, 255);
		skillInterface(19747, 51);
		skillInterface(19748, 50);
		skillInterface(19749, 52);
		addText(22000, "99", 0xFFFF00, false, false, -1, TDA, 0);
		addText(22001, "99", 0xFFFF00, false, false, -1, TDA, 0);
		addText(22002, "2147", 0xFFFF00, false, false, -1, TDA, 1);
		addText(22003, "Total", 0xFFFF00, false, false, -1, TDA, 1);
		addText(22004, "", 0xFFFF00, false, false, -1, TDA, 1);
		for (int k = 0; k < boxIds.length; k++) {
			skillInterface(boxIds[k], 256);
		}
		RSInterface SK = addTabInterface(3917);
		SK.totalChildren(63);
		SK.child(0, 3918, 0, 0);
		SK.child(1, 3925, 0, 31);
		SK.child(2, 3932, 0, 62);
		SK.child(3, 3939, 0, 93);
		SK.child(4, 3946, 0, 124);
		SK.child(5, 3953, 0, 155);
		SK.child(6, 4148, 0, 186);
		SK.child(7, 19746, 70, 69);
		SK.child(8, 19748, 1, 219);
		SK.child(9, 19747, 64, 219);
		SK.child(10, 14000, 10, 219);
		SK.child(11, 19749, 128, 219);
		SK.child(12, 22003, 144, 224);
		SK.child(13, 22004, 142, 231);
		SK.child(14, 22002, 144, 237);
		SK.child(15, 22001, 98, 220);
		SK.child(16, 22001, 107, 235);
		SK.child(17, 22000, 36, 220);
		SK.child(18, 22000, 45, 235);
		SK.child(19, 4040, 5, 20);
		SK.child(20, 8654, 0, 2);
		SK.child(21, 8655, 64, 2);
		SK.child(22, 4076, 20, 20);
		SK.child(23, 8656, 128, 2);
		SK.child(24, 4112, 20, 20);
		SK.child(25, 8657, 0, 33);
		SK.child(26, 4046, 20, 50);
		SK.child(27, 8658, 64, 33);
		SK.child(28, 4082, 20, 50);
		SK.child(29, 8659, 128, 33);
		SK.child(30, 4118, 20, 50);
		SK.child(31, 8660, 0, 60 + 10);
		SK.child(32, 4052, 20, 83);
		SK.child(33, 8661, 65, 60 + 10);
		SK.child(34, 4088, 20, 83);
		SK.child(35, 8662, 130, 60 + 10);
		SK.child(36, 4124, 20, 83);
		SK.child(37, 8663, 0, 90 + 10);
		SK.child(38, 4058, 20, 120);
		SK.child(39, 8664, 65, 90 + 10);
		SK.child(40, 4094, 20, 120);
		SK.child(41, 8665, 130, 90 + 10);
		SK.child(42, 4130, 20, 120);
		SK.child(43, 8666, 0, 130);
		SK.child(44, 4064, 20, 150);
		SK.child(45, 8667, 65, 130);
		SK.child(46, 4100, 20, 150);
		SK.child(47, 8668, 130, 130);
		SK.child(48, 4136, 20, 150);
		SK.child(49, 8669, 0, 160);
		SK.child(50, 4070, 20, 180);
		SK.child(51, 8670, 65, 160);
		SK.child(52, 4106, 20, 180);
		SK.child(53, 8671, 130, 160);
		SK.child(54, 4142, 20, 180);
		SK.child(55, 8672, 0, 190);
		SK.child(56, 4160, 20, 150);
		SK.child(57, 8672, 0, 190);
		SK.child(58, 4160, 20, 150);
		SK.child(59, 12162, 65, 190);
		SK.child(60, 2832, 20, 150);
		SK.child(61, 13928, 130, 190);
		SK.child(62, 13917, 20, 150);
	}

	public static int boxIds[] = { 4041, 4077, 4113, 4047, 4083, 4119, 4053,
			4089, 4125, 4059, 4095, 4131, 4065, 4101, 4137, 4071, 4107, 4143,
			4154, 12168, 13918 };

	public void specialBar(int id, TextDrawingArea[] tda) // 7599
	{
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;
		rsi.hoverType = 40005;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;

		rsi.child(0, id - 12, 0, 0);

		rsi.child(12, id + 1, 3, 7);

		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.sprite1 = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);

		rsi = addInterface(40005);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.hoverType = -1;
		rsi.parentID = 40005;
		rsi.id = 40005;
		addBox(40006, 0, false, 0x000000,
				"Select to perform a special\nattack.");
		setChildren(1, rsi);
		setBounds(40006, 0, 0, 0, rsi);
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1,
			String text2, String text3, String text4, int str1x, int str1y,
			int str2x, int str2y, int str3x, int str3y, int str4x, int str4y,
			int img1x, int img1y, int img2x, int img2y, int img3x, int img3y,
			int img4x, int img4y, TextDrawingArea[] tda, String popupString1,
			String popupString2, String popupString3, String popupString4,
			int hoverID1, int hoverID2, int hoverID3, int hoverID4) // 4button
																	// spec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addAttackText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addAttackText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addAttackText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addAttackText(id2 + 14, text4, tda, 0, 0xff981f, false);

		rsi.specialBar(id3, tda); // 7599

		addAttackHover(id2 + 3, hoverID1, popupString1, tda);
		addAttackHover(id2 + 6, hoverID2, popupString2, tda);
		addAttackHover(id2 + 5, hoverID3, popupString3, tda);
		addAttackHover(id2 + 4, hoverID4, popupString4, tda);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(20);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, hoverID4, 108, 149);
		frame++;
		rsi.child(frame, 40005, 28, 149);
		frame++; // special bar tooltip

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0(TextDrawingArea[] tda) {
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75,
				127, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Accurate\nSlash\nAttack XP",
				"Aggressive\nSlash\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nSlash\nDefence XP", 40132, 40136, 40140, 40144); // OK

		Sidebar0a(2276, 2279, 7574, "Stab", "Lunge", "Slash", "Block", 43, 75,
				124, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Accurate\nStab\nAttack XP",
				"Aggressive\nStab\nStrength XP",
				"Aggressive\nSlash\nStrength XP",
				"Defensive\nStab\nDefence XP", 40020, 40024, 40028, 40032); // OK

		Sidebar0a(2423, 2426, 7599, "Chop", "Slash", "Lunge", "Block", 42, 75,
				125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Accurate\nSlash\nAttack XP",
				"Aggressive\nSlash\nStrength XP",
				"Controlled\nStab\nShared XP", "Defensive\nSlash\nDefence XP",
				40036, 40040, 40044, 40048); // OK

		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39,
				75, 121, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40,
				103, tda, "", "", "", "", 40052, 40056, 40060, 40064); // WTF IS
																		// THIS?!

		Sidebar0a(4679, 4682, 7674, "Lunge", "Swipe", "Pound", "Block", 40, 75,
				124, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Controlled\nStab\nShared XP",
				"Controlled\nSlash\nShared XP", "Controlled\nCrush\nShared XP",
				"Defensive\nStab\nDefence XP", 40068, 40072, 40076, 40080); // OK

		Sidebar0a(4705, 4708, 7699, "Chop", "Slash", "Smash", "Block", 42, 75,
				125, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Accurate\nSlash\nAttack XP",
				"Aggressive\nSlash\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nSlash\nDefence XP", 40084, 40088, 40092, 40096); // ???

		Sidebar0a(5570, 5573, 7724, "Spike", "Impale", "Smash", "Block", 41,
				75, 123, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40,
				103, tda, "Accurate\nStab\nAttack XP",
				"Aggressive\nStab\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nStab\nDefence XP", 40010, 40104, 40108, 40112); // OK

		Sidebar0a(7762, 7765, 7800, "Chop", "Slash", "Lunge", "Block", 42, 75,
				125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				tda, "Accurate\nSlash\nAttack XP",
				"Aggressive\nSlash\nStrength XP",
				"Controlled\nStab\nShared XP", "Defensive\nSlash\nDefence XP",
				40116, 40120, 40124, 40128); // OK

		Sidebar0b(776, 779, "Reap", "Chop", "Jab", "Block", 42, 75, 126, 75,
				46, 128, 125, 128, 122, 103, 122, 50, 40, 103, 40, 50, tda, "",
				"", "", "", 40132, 40136, 40140, 40144); // ???

		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75,
				42, 128, 40, 103, 40, 50, 122, 50, tda,
				"Accurate\nCrush\nAttack XP", "Aggressive\nCrush\nDefence XP",
				"Defensive\nCrush\nDefence XP", 40148, 40152, 40156); // OK

		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda,
				"Accurate\nRanged XP", "Rapid\nRanged XP",
				"Long range\nRanged XP\nDefence XP", 40160, 40164, 40168); // OK

		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda,
				"Accurate\nRanged XP", "Rapid\nRanged XP",
				"Long range\nRanged XP\nDefence XP", 40172, 40176, 40180); // OK

		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda,
				"Accurate\nRanged XP", "Rapid\nRanged XP",
				"Long range\nRanged XP\nDefence XP", 40184, 40188, 40192); // OK

		Sidebar0c(5855, 5857, 7749, "Punch", "Kick", "Block", 40, 75, 129, 75,
				42, 128, 40, 50, 122, 50, 40, 103, tda,
				"Accurate\nCrush\nAttack XP", "Aggressive\nCrush\nStrength XP",
				"Defensive\nCrush\nDefence XP", 40196, 40200, 40204); // OK

		Sidebar0c(6103, 6132, 6117, "Bash", "Pound", "Block", 43, 75, 124, 75,
				42, 128, 40, 103, 40, 50, 122, 50, tda,
				"Accurate\nCrush\nAttack XP", "Aggressive\nCrush\nStrength XP",
				"Defensive\nCrush\nDefence XP", 40208, 40212, 40216); // ???

		Sidebar0c(8460, 8463, 8493, "Jab", "Swipe", "Fend", 46, 75, 124, 75,
				43, 128, 40, 103, 40, 50, 122, 50, tda,
				"Controlled\nStabbed\nShared XP",
				"Aggressive\nSlash\nStrength XP",
				"Defensive\nStab\nDefence XP", 40224, 40228, 40232); // OK

		Sidebar0c(12290, 12293, 12323, "Flick", "Lash", "Deflect", 44, 75, 127,
				75, 36, 128, 40, 50, 40, 103, 122, 50, tda,
				"Accurate\nSlash\nAttack XP", "Controlled\nSlash\nShared XP",
				"Defensive\nSlash\nDefence XP", 40236, 40240, 40244); // OK

		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136,
				40, 120, 40, 50, 40, 85, tda);

		RSInterface rsi = addTabInterface(19300);
		textSize(3983, tda, 0);
		addAttackStyleButton2(
				150,
				150,
				172,
				150,
				44,
				"Auto Retaliate",
				40000,
				154,
				42,
				"When active, you will\nautomatically fight back if\nattacked.",
				tda);

		rsi.totalChildren(3);
		rsi.child(0, 3983, 52, 25); // combat level
		rsi.child(1, 150, 21, 153); // auto retaliate
		rsi.child(2, 40000, 26, 200);

		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void Sidebar0b(int id, int id2, String text1, String text2,
			String text3, String text4, int str1x, int str1y, int str2x,
			int str2y, int str3x, int str3y, int str4x, int str4y, int img1x,
			int img1y, int img2x, int img2y, int img3x, int img3y, int img4x,
			int img4y, TextDrawingArea[] tda, String popupString1,
			String popupString2, String popupString3, String popupString4,
			int hoverID1, int hoverID2, int hoverID3, int hoverID4) // 4button
																	// nospec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addAttackText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addAttackText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addAttackText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addAttackText(id2 + 14, text4, tda, 0, 0xff981f, false);

		addAttackHover(id2 + 3, hoverID1, popupString1, tda);
		addAttackHover(id2 + 6, hoverID2, popupString2, tda);
		addAttackHover(id2 + 5, hoverID3, popupString3, tda);
		addAttackHover(id2 + 4, hoverID4, popupString4, tda);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(18);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, hoverID4, 108, 149);
		frame++;

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void addAttackHover(int id, int hoverID, String hoverText,
			TextDrawingArea[] TDA) {
		RSInterface rsi = interfaceCache[id];
		rsi.hoverType = hoverID;

		rsi = addInterface(hoverID);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.hoverType = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addAttackText(int id, String text,
			TextDrawingArea tda[], int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1,
			String text2, String text3, int str1x, int str1y, int str2x,
			int str2y, int str3x, int str3y, int img1x, int img1y, int img2x,
			int img2y, int img3x, int img3y, TextDrawingArea[] tda,
			String popupString1, String popupString2, String popupString3,
			int hoverID1, int hoverID2, int hoverID3) // 3button spec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addAttackText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addAttackText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addAttackText(id2 + 11, text3, tda, 0, 0xff981f, false);

		rsi.specialBar(id3, tda); // 7599

		addAttackHover(id2 + 5, hoverID1, popupString1, tda);
		addAttackHover(id2 + 4, hoverID2, popupString2, tda);
		addAttackHover(id2 + 3, hoverID3, popupString3, tda);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(16);

		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // chop
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // slash
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // lunge

		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon

		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, 40005, 28, 149);
		frame++; // special bar tooltip

		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2,
			String text3, int str1x, int str1y, int str2x, int str2y,
			int str3x, int str3y, int img1x, int img1y, int img2x, int img2y,
			int img3x, int img3y, TextDrawingArea[] tda) // 3button nospec
															// (magic intf)
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addAttackText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addAttackText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addAttackText(id2 + 11, text3, tda, 0, 0xff981f, false);

		addAttackText(353, "Spell", tda, 0, 0xff981f, false);
		addAttackText(354, "Spell", tda, 0, 0xff981f, false);

		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");

		addToggleButton(349, 349, 108, 68, 44, "Select");
		addToggleButton(350, 350, 108, 68, 44, "Select");

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last);

		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // bash
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // pound
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // focus

		rsi.child(frame, 349, 105, 46);
		frame++; // spell1
		rsi.child(frame, 350, 104, 106);
		frame++; // spell2

		rsi.child(frame, 353, 125, 74);
		frame++; // spell
		rsi.child(frame, 354, 125, 134);
		frame++; // spell

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
	}

	public static void emoteTab() {
		RSInterface tab = addTabInterface(147);
		RSInterface scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		addButton(168, 0, "Emotes/EMOTE", "Yes");
		addButton(169, 1, "Emotes/EMOTE", "No");
		addButton(164, 2, "Emotes/EMOTE", "Bow");
		addButton(165, 3, "Emotes/EMOTE", "Angry");
		addButton(162, 4, "Emotes/EMOTE", "Think");
		addButton(163, 5, "Emotes/EMOTE", "Wave");
		addButton(13370, 6, "Emotes/EMOTE", "Shrug");
		addButton(171, 7, "Emotes/EMOTE", "Cheer");
		addButton(167, 8, "Emotes/EMOTE", "Beckon");
		addButton(170, 9, "Emotes/EMOTE", "Laugh");
		addButton(13366, 10, "Emotes/EMOTE", "Jump for Joy");
		addButton(13368, 11, "Emotes/EMOTE", "Yawn");
		addButton(166, 12, "Emotes/EMOTE", "Dance");
		addButton(13363, 13, "Emotes/EMOTE", "Jig");
		addButton(13364, 14, "Emotes/EMOTE", "Spin");
		addButton(13365, 15, "Emotes/EMOTE", "Headbang");
		addButton(161, 16, "Emotes/EMOTE", "Cry");
		addButton(11100, 17, "Emotes/EMOTE", "Blow kiss");
		addButton(13362, 18, "Emotes/EMOTE", "Panic");
		addButton(13367, 19, "Emotes/EMOTE", "Raspberry");
		addButton(172, 20, "Emotes/EMOTE", "Clap");
		addButton(13369, 21, "Emotes/EMOTE", "Salute");
		addButton(13383, 22, "Emotes/EMOTE", "Goblin Bow");
		addButton(13384, 23, "Emotes/EMOTE", "Goblin Salute");
		addButton(667, 24, "Emotes/EMOTE", "Glass Box");
		addButton(6503, 25, "Emotes/EMOTE", "Climb Rope");
		addButton(6506, 26, "Emotes/EMOTE", "Lean On Air");
		addButton(666, 27, "Emotes/EMOTE", "Glass Wall");
		addButton(18464, 28, "Emotes/EMOTE", "Zombie Walk");
		addButton(18465, 29, "Emotes/EMOTE", "Zombie Dance");
		addButton(15166, 30, "Emotes/EMOTE", "Scared");
		addButton(18686, 31, "Emotes/EMOTE", "Rabbit Hop");
		addConfigButton(154, 147, 32, 33, "Emotes/EMOTE", 41, 47,
				"Skillcape Emote", 0, 1, 700);
		scroll.totalChildren(33);
		scroll.child(0, 168, 10, 7);
		scroll.child(1, 169, 54, 7);
		scroll.child(2, 164, 98, 14);
		scroll.child(3, 165, 137, 7);
		scroll.child(4, 162, 9, 56);
		scroll.child(5, 163, 48, 56);
		scroll.child(6, 13370, 95, 56);
		scroll.child(7, 171, 137, 56);
		scroll.child(8, 167, 7, 105);
		scroll.child(9, 170, 51, 105);
		scroll.child(10, 13366, 95, 104);
		scroll.child(11, 13368, 139, 105);
		scroll.child(12, 166, 6, 154);
		scroll.child(13, 13363, 50, 154);
		scroll.child(14, 13364, 90, 154);
		scroll.child(15, 13365, 135, 154);
		scroll.child(16, 161, 8, 204);
		scroll.child(17, 11100, 51, 203);
		scroll.child(18, 13362, 99, 204);
		scroll.child(19, 13367, 137, 203);
		scroll.child(20, 172, 10, 253);
		scroll.child(21, 13369, 53, 253);
		scroll.child(22, 13383, 88, 258);
		scroll.child(23, 13384, 138, 252);
		scroll.child(24, 667, 2, 303);
		scroll.child(25, 6503, 49, 302);
		scroll.child(26, 6506, 93, 302);
		scroll.child(27, 666, 137, 302);
		scroll.child(28, 18464, 9, 352);
		scroll.child(29, 18465, 50, 352);
		scroll.child(30, 15166, 94, 356);
		scroll.child(31, 18686, 141, 353);
		scroll.child(32, 154, 5, 401);
		scroll.width = 173;
		scroll.height = 258;
		scroll.scrollMax = 450;
	}

	public static void quickCurses(TextDrawingArea[] TDA) {
		RSInterface tab = addTabInterface(17234);
		addTransparentSprite(17229, 0, "Quicks/Quickprayers", 50);
		addSprite(17201, 3, "Quicks/Quickprayers");
		addText(17230, "Select your quick prayers:", TDA, 0, 0xFF981F, false,
				true);
		for (int i = 17202, j = 630; i <= 17228 || j <= 656; i++, j++) {
			addConfigButton(i, 17200, 2, 1, "Quicks/Quickprayers", 14, 15,
					"Select", 0, 1, j);
		}
		addHoverButton(17231, "Quicks/Quickprayers", 4, 190, 24,
				"Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "Quicks/Quickprayers", 5, 190, 24, 17233);
		int frame = 0;
		setChildren(46, tab);
		setBounds(21358, 11, 8 + 20, frame++, tab);
		setBounds(21360, 50, 11 + 20, frame++, tab);
		setBounds(21362, 87, 11 + 20, frame++, tab);
		setBounds(21364, 122, 10 + 20, frame++, tab);
		setBounds(21366, 159, 11 + 20, frame++, tab);
		setBounds(21368, 12, 45 + 20, frame++, tab);
		setBounds(21370, 46, 45 + 20, frame++, tab);
		setBounds(21372, 83, 46 + 20, frame++, tab);
		setBounds(21374, 119, 45 + 20, frame++, tab);
		setBounds(21376, 157, 45 + 20, frame++, tab);
		setBounds(21378, 11, 83 + 20, frame++, tab);
		setBounds(21380, 49, 84 + 20, frame++, tab);
		setBounds(21382, 84, 83 + 20, frame++, tab);
		setBounds(21384, 123, 84 + 20, frame++, tab);
		setBounds(21386, 159, 83 + 20, frame++, tab);
		setBounds(21388, 12, 119 + 20, frame++, tab);
		setBounds(21390, 49, 119 + 20, frame++, tab);
		setBounds(21392, 88, 119 + 20, frame++, tab);
		setBounds(21394, 122, 121 + 20, frame++, tab);
		setBounds(21396, 155, 122 + 20, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);// Faded backing
		setBounds(17201, 0, 22, frame++, tab);// Split
		setBounds(17201, 0, 237, frame++, tab);// Split
		setBounds(17202, 13 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 52 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 90 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 126 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 162 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 13 - 3, 45 + 17, frame++, tab);
		setBounds(17208, 52 - 3, 45 + 17, frame++, tab);
		setBounds(17209, 90 - 3, 45 + 17, frame++, tab);
		setBounds(17210, 126 - 3, 45 + 17, frame++, tab);
		setBounds(17211, 162 - 3, 45 + 17, frame++, tab);
		setBounds(17212, 13 - 3, 80 + 17, frame++, tab);
		setBounds(17213, 52 - 3, 80 + 17, frame++, tab);
		setBounds(17214, 90 - 3, 80 + 17, frame++, tab);
		setBounds(17215, 126 - 3, 80 + 17, frame++, tab);
		setBounds(17216, 162 - 3, 80 + 17, frame++, tab);
		setBounds(17217, 13 - 3, 119 + 17, frame++, tab);
		setBounds(17218, 52 - 3, 119 + 17, frame++, tab);
		setBounds(17219, 90 - 3, 119 + 17, frame++, tab);
		setBounds(17220, 126 - 3, 119 + 17, frame++, tab);
		setBounds(17221, 162 - 3, 119 + 17, frame++, tab);
		setBounds(17230, 5, 5, frame++, tab);// text
		setBounds(17231, 0, 237, frame++, tab);// confirm
		setBounds(17232, 0, 237, frame++, tab);// Confirm hover
	}

	public static void quickPrayers(TextDrawingArea[] TDA) {
		RSInterface tab = addTabInterface(17200);
		int frame = 0;
		setChildren(58, tab);//
		setBounds(5632, 5, 8 + 20, frame++, tab);
		setBounds(5633, 44, 8 + 20, frame++, tab);
		setBounds(5634, 79, 11 + 20, frame++, tab);
		setBounds(19813, 116, 10 + 20, frame++, tab);
		setBounds(19815, 153, 9 + 20, frame++, tab);
		setBounds(5635, 5, 48 + 20, frame++, tab);
		setBounds(5636, 44, 47 + 20, frame++, tab);
		setBounds(5637, 79, 49 + 20, frame++, tab);
		setBounds(5638, 116, 50 + 20, frame++, tab);
		setBounds(5639, 154, 50 + 20, frame++, tab);
		setBounds(5640, 4, 84 + 20, frame++, tab);
		setBounds(19817, 44, 87 + 20, frame++, tab);
		setBounds(19820, 81, 85 + 20, frame++, tab);
		setBounds(5641, 117, 85 + 20, frame++, tab);
		setBounds(5642, 156, 87 + 20, frame++, tab);
		setBounds(5643, 5, 125 + 20, frame++, tab);
		setBounds(5644, 43, 124 + 20, frame++, tab);
		setBounds(686, 83, 124 + 20, frame++, tab);
		setBounds(5645, 115, 121 + 20, frame++, tab);
		setBounds(19822, 154, 124 + 20, frame++, tab);
		setBounds(19824, 5, 160 + 20, frame++, tab);
		setBounds(5649, 41, 158 + 20, frame++, tab);
		setBounds(5647, 79, 163 + 20, frame++, tab);
		setBounds(5648, 116, 158 + 20, frame++, tab);
		setBounds(19826, 161, 160 + 20, frame++, tab);
		setBounds(19828, 4, 207 + 12, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);// Faded backing
		setBounds(17201, 0, 22, frame++, tab);// Split
		setBounds(17201, 0, 237, frame++, tab);// Split

		setBounds(17202, 5 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 44 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 79 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 116 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 153 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 5 - 3, 48 + 17, frame++, tab);
		setBounds(17208, 44 - 3, 48 + 17, frame++, tab);
		setBounds(17209, 79 - 3, 48 + 17, frame++, tab);
		setBounds(17210, 116 - 3, 48 + 17, frame++, tab);
		setBounds(17211, 153 - 3, 48 + 17, frame++, tab);
		setBounds(17212, 5 - 3, 85 + 17, frame++, tab);
		setBounds(17213, 44 - 3, 85 + 17, frame++, tab);
		setBounds(17214, 79 - 3, 85 + 17, frame++, tab);
		setBounds(17215, 116 - 3, 85 + 17, frame++, tab);
		setBounds(17216, 153 - 3, 85 + 17, frame++, tab);
		setBounds(17217, 5 - 3, 124 + 17, frame++, tab);
		setBounds(17218, 44 - 3, 124 + 17, frame++, tab);
		setBounds(17219, 79 - 3, 124 + 17, frame++, tab);
		setBounds(17220, 116 - 3, 124 + 17, frame++, tab);
		setBounds(17221, 153 - 3, 124 + 17, frame++, tab);
		setBounds(17222, 5 - 3, 160 + 17, frame++, tab);
		setBounds(17223, 44 - 3, 160 + 17, frame++, tab);
		setBounds(17224, 79 - 3, 160 + 17, frame++, tab);
		setBounds(17225, 116 - 3, 160 + 17, frame++, tab);
		setBounds(17226, 153 - 3, 160 + 17, frame++, tab);
		setBounds(17227, 4 - 3, 207 + 4, frame++, tab);
		setBounds(17230, 5, 5, frame++, tab);// text
		setBounds(17231, 0, 237, frame++, tab);// confirm
		setBounds(17232, 0, 237, frame++, tab);// Confirm hover
	}

	public static void clanChatTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(18128);
		addHoverButton(18129, "/Clan Chat/SPRITE", 6, 72, 32, "Join Clan", 550,
				18130, 5);
		addHoveredButton(18130, "/Clan Chat/SPRITE", 7, 72, 32, 18131);
		addHoverButton(18132, "/Clan Chat/SPRITE", 6, 72, 32, "Clan Setup", -1,
				18133, 5);
		addHoveredButton(18133, "/Clan Chat/SPRITE", 7, 72, 32, 18134);
		addText(18135, "Join Clan", tda, 0, 0xff9b00, true, true);
		addText(18136, "Clan Setup", tda, 0, 0xff9b00, true, true);
		addSprite(18137, 37, "/Clan Chat/SPRITE");
		addText(18138, "Clan Chat", tda, 1, 0xff9b00, true, true);
		addText(18139, "Talking in: Not in clan", tda, 0, 0xff9b00, false, true);
		addText(18140, "Owner: None", tda, 0, 0xff9b00, false, true);
		addSprite(16126, 4, "/Clan Chat/SPRITE");
		tab.totalChildren(13);
		tab.child(0, 16126, 0, 221);
		tab.child(1, 16126, 0, 59);
		tab.child(2, 18137, 0, 62);
		tab.child(3, 18143, 0, 62);
		tab.child(4, 18129, 15, 226);
		tab.child(5, 18130, 15, 226);
		tab.child(6, 18132, 103, 226);
		tab.child(7, 18133, 103, 226);
		tab.child(8, 18135, 51, 237);
		tab.child(9, 18136, 139, 237);
		tab.child(10, 18138, 95, 1);
		tab.child(11, 18139, 10, 23);
		tab.child(12, 18140, 25, 38);
		/* Text area */
		RSInterface list = addTabInterface(18143);
		list.totalChildren(100);
		for (int i = 18144; i <= 18244; i++) {
			addText(i, "", tda, 0, 0xffffff, false, true);
		}
		for (int id = 18144, i = 0; id <= 18243 && i <= 99; id++, i++) {
			interfaceCache[id].actions = new String[] { "Edit Rank", "Kick",
					"Ban" };
			list.children[i] = id;
			list.childX[i] = 5;
			for (int id2 = 18144, i2 = 1; id2 <= 18243 && i2 <= 99; id2++, i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 14;
			}
		}
		list.height = 158;
		list.width = 174;
		list.scrollMax = 1405;
	}

	public Sprite disabledHover;

	public static void clanChatSetup(TextDrawingArea[] tda) {
		RSInterface rsi = addInterface(28800);
		rsi.totalChildren(12 + 15);
		int count = 0;// mess with those bank tab IDs i gotta go to bed look at
						// the time k
		// ill start bank tabs tomororw :) mk
		/* Background */
		addSprite(28801, 1, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 28801, 14, 18);
		/* Close button */
		addButton(28802, 0, "/Interfaces/Clan Chat/close", "Close");
		interfaceCache[28802].atActionType = 3;
		rsi.child(count++, 28802, 475, 26);
		/* Clan Setup title */
		addText(28803, "Clan Setup", tda, 2, 0xFF981F, true, true);
		rsi.child(count++, 28803, 256, 26);
		/* Setup buttons */
		String[] titles = { "Clan name:", "Who can enter chat?",
				"Who can talk on chat?", "Who can kick on chat?",
				"Who can ban on chat?" };
		String[] defaults = { "Chat Disabled", "Anyone", "Anyone", "Anyone",
				"Anyone" };
		String[] whoCan = { "Anyone", "Recruit", "Corporal", "Sergeant",
				"Lieutenant", "Captain", "General", "Only Me" };
		for (int index = 0, id = 28804, y = 50; index < titles.length; index++, id += 3, y += 40) {
			addButton(id, 2, "/Interfaces/Clan Chat/sprite", "");
			interfaceCache[id].atActionType = 0;
			if (index > 0) {
				interfaceCache[id].actions = whoCan;
			} else {
				interfaceCache[id].actions = new String[] { "Change title",
						"Delete clan" };
				;
			}
			addText(id + 1, titles[index], tda, 0, 0xFF981F, true, true);
			addText(id + 2, defaults[index], tda, 1, 0xFFFFFF, true, true);
			rsi.child(count++, id, 25, y);
			rsi.child(count++, id + 1, 100, y + 4);
			rsi.child(count++, id + 2, 100, y + 17);
		}
		/* Table */
		addSprite(28819, 5, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 28819, 197, 70);
		/* Labels */
		int id = 28820;
		int y = 74;
		addText(id, "Ranked Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 202, y);
		addText(id, "Banned Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 339, y);
		/* Ranked members list */
		RSInterface list = addInterface(id++);
		int lines = 100;
		list.totalChildren(lines);
		String[] ranks = { "Demote", "Recruit", "Corporal", "Sergeant",
				"Lieutenant", "Captain", "General", "Owner" };
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = ranks;
			list.children[index - id] = index;
			list.childX[index - id] = 2;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id
					- 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 199, 92);
		/* Banned members list */
		list = addInterface(id++);
		list.totalChildren(lines);
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = new String[] { "Unban" };
			list.children[index - id] = index;
			list.childX[index - id] = 0;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id
					- 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 339, 92);
		/* Table info text */
		y = 47;
		addText(id, "You can manage both ranked and banned members here.", tda,
				0, 0xFF981F, true, true);
		rsi.child(count++, id++, 337, y);
		addText(id, "Right click on a name to edit the member.", tda, 0,
				0xFF981F, true, true);
		rsi.child(count++, id++, 337, y + 11);
		/* Add ranked member button */
		y = 75;
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add ranked member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 319, y);
		/* Add banned member button */
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add banned member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 459, y);

		/* Hovers */
		int[] clanSetup = { 28802, 28804, 28807, 28810, 28813, 28816, 18426,
				18527 };
		String[] names = { "close", "sprite", "sprite", "sprite", "sprite",
				"sprite", "plus", "plus" };
		int[] ids = { 1, 3, 3, 3, 3, 3, 1, 1 };
		for (int index = 0; index < clanSetup.length; index++) {
			rsi = interfaceCache[clanSetup[index]];
			rsi.disabledHover = imageLoader(ids[index],
					"/Interfaces/Clan Chat/" + names[index]);
		}
	}

	public int transparency;

	public static void addTransparentSprite(int id, int spriteId,
			String spriteName, int transparency) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.transparency = transparency;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static void Pestpanel(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21119);
		addText(21120, "What", 0x999999, false, true, 52, tda, 1);
		addText(21121, "What", 0x33cc00, false, true, 52, tda, 1);
		addText(21122, "(Need 5 to 25 players)", 0xFFcc33, false, true, 52,
				tda, 1);
		addText(21123, "Points", 0x33ccff, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}

	public static void Pestpanel2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "Pest Control/PEST1");
		addSprite(21102, 1, "Pest Control/PEST1");
		addSprite(21103, 2, "Pest Control/PEST1");
		addSprite(21104, 3, "Pest Control/PEST1");
		addSprite(21105, 4, "Pest Control/PEST1");
		addSprite(21106, 5, "Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, tda, 1);
		addText(21108, "", 0x0000FF, false, true, 52, tda, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, tda, 1);
		addText(21110, "", 0xCC0000, false, true, 52, tda, 1);
		addText(21111, "250", 0x99FF33, false, true, 52, tda, 1);// w purp
		addText(21112, "250", 0x99FF33, false, true, 52, tda, 1);// e blue
		addText(21113, "250", 0x99FF33, false, true, 52, tda, 1);// se yel
		addText(21114, "250", 0x99FF33, false, true, 52, tda, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, tda, 1);// attacks
		addText(21116, "0", 0x99FF33, false, true, 52, tda, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, tda, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public String hoverText;

	public static void addHoverBox(int id, int ParentID, String text,
			String text2, int configId, int configFrame) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = ParentID;
		rsi.type = 8;
		rsi.aString228 = text;
		rsi.message = text2;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.anIntArray212[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}

	public static void addText2(int id, String text, TextDrawingArea wid[],
			int idx, int color) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.id = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.hoverType = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;
	}

	public static void addText(int id, String text, TextDrawingArea tda[],
			int idx, int color, boolean center, boolean shadow) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.aString228 = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.anInt216 = 0;
		tab.anInt239 = 0;
	}

	public static void addText(int i, String s, int k, boolean l, boolean m,
			int a, TextDrawingArea[] TDA, int j) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.parentID = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.atActionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = a;
		RSInterface.centerText = l;
		RSInterface.textShadow = m;
		RSInterface.textDrawingAreas = TDA[j];
		RSInterface.message = s;
		RSInterface.aString228 = "";
		RSInterface.textColor = k;
	}

	public static void addButton(int id, int sid, String spriteName,
			String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2,
			String bName, int width, int height, String tT, int configID,
			int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.hoverType = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltip = tT;
	}

	private static void addSprite(int id, int spriteId) {
		RSInterface widget = interfaceCache[id] = new RSInterface();
		widget.id = id;
		widget.parentID = id;
		widget.type = 5;
		widget.atActionType = 0;
		widget.contentType = 0;
		widget.aByte254 = (byte) 0;
		widget.hoverType = 52;

		if (spriteId != -1) {
			widget.sprite1 = Client.cacheSprite2[spriteId];
			widget.sprite2 = Client.cacheSprite2[spriteId];
		}

		widget.width = 512;
		widget.height = 334;
	}

	public static void addSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static void addHoverButton(int i, String imageName, int j,
			int width, int height, String text, int contentType, int hoverOver,
			int aT) {// hoverable
						// button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.aByte254 = 0;
		tab.hoverType = hoverOver;
		tab.sprite1 = imageLoader(j, imageName);
		tab.sprite2 = imageLoader(j, imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w,
			int h, int IMAGEID) {// hoverable
									// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.aByte254 = 0;
		tab.hoverType = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(j, name);
		tab.sprite2 = imageLoader(k, name);
	}

	public static void addTransparentSprite(int id, int spriteId,
			String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	private static final int WHITE_TEXT = 0xFFFFFF;

	public static void addAdvancedSprite(int id, int spriteId) {
		RSInterface widget = addInterface(id);
		widget.id = id;
		widget.parentID = id;
		widget.type = 5;
		widget.atActionType = 0;
		widget.contentType = 0;
		widget.hoverType = 52;
		widget.sprite1 = Client.cacheSprite2[spriteId];
		widget.sprite2 = Client.cacheSprite2[spriteId];
		widget.drawsTransparent = true;
		widget.aByte254 = 64;
		widget.width = 512;
		widget.height = 334;
	}

	public static RSInterface addScreenInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = (byte) 0;
		tab.hoverType = 0;
		return tab;
	}

	public static RSInterface addTabInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;// 250
		tab.parentID = id;// 236
		tab.type = 0;// 262
		tab.atActionType = 0;// 217
		tab.contentType = 0;
		tab.width = 512;// 220
		tab.height = 700;// 267
		tab.aByte254 = (byte) 0;
		tab.hoverType = -1;// Int 230
		return tab;
	}

	private static Sprite imageLoader(int i, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(s + " " + i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	private Model method206(int i, int j) {
		Model model = (Model) aMRUNodes_264.insertFromCache((i << 16) + j);
		if (model != null)
			return model;
		if (i == 1)
			model = Model.method462(j);
		if (i == 2)
			model = EntityDef.forID(j).method160();
		if (i == 3)
			model = Client.myPlayer.method453();
		if (i == 4)
			model = ItemDef.forID(j).method202(50);
		if (i == 5)
			model = null;
		if (model != null)
			aMRUNodes_264.removeFromCache(model, (i << 16) + j);
		return model;
	}

	private static Sprite method207(int i, StreamLoader streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;// was parameter
		int j = 5;// was parameter
		if (flag)
			return;
		aMRUNodes_264.unlinkAll();
		if (model != null && j != 4)
			aMRUNodes_264.removeFromCache(model, (j << 16) + i);
	}

	public Model method209(int j, int k, boolean flag) {
		Model model;
		if (flag)
			model = method206(anInt255, anInt256);
		else
			model = method206(anInt233, mediaID);
		if (model == null)
			return null;
		if (k == -1 && j == -1 && model.anIntArray1640 == null)
			return model;
		Model model_1 = new Model(true, Class36.method532(k)
				& Class36.method532(j), false, model);
		if (k != -1 || j != -1)
			model_1.method469();
		if (k != -1)
			model_1.method470(k);
		if (j != -1)
			model_1.method470(j);
		model_1.method479(64, 768, -50, -10, -50, true);
		return model_1;
	}

	public RSInterface() {
	}

	public static StreamLoader aClass44;
	public boolean drawsTransparent;
	public Sprite sprite1;
	public int anInt208;
	public Sprite sprites[];
	public static RSInterface interfaceCache[];
	public int anIntArray212[];
	public int contentType;// anInt214
	public int spritesX[];
	public int anInt216;
	public int atActionType;
	public String spellName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int valueIndexArray[][];
	public boolean aBoolean227;
	public String aString228;
	public int hoverType;
	public int invSpritePadX;
	public int textColor;
	public int anInt233;
	public int mediaID;
	public boolean aBoolean235;
	public int parentID;
	public int spellUsableOn;
	private static MRUNodes aMRUNodes_238;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public TextDrawingArea textDrawingAreas;
	public int isMouseoverTriggereds;
	public int invSpritePadY;
	public int anIntArray245[];
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean isInventoryInterface;
	public int id;
	public int invStackSizes[];
	public int inv[];
	public byte aByte254;
	private int anInt255;
	private int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean aBoolean259;
	public Sprite sprite2;
	public int scrollMax;
	public int type;
	public int anInt263;
	private static final MRUNodes aMRUNodes_264 = new MRUNodes(30);
	public int anInt265;
	public boolean isMouseoverTriggered;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];

	public static void addLunarSprite(int i, int j, String name) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}

	public static void drawRune(int i, int id, String runeName) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(id, "Lunar/RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID,
			TextDrawingArea[] font) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 4;
		rsInterface.atActionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.aByte254 = 0;
		rsInterface.hoverType = -1;
		rsInterface.anIntArray245 = new int[1];
		rsInterface.anIntArray212 = new int[1];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = runeAmount;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = font[0];
		rsInterface.textShadow = true;
		rsInterface.message = "%1/" + runeAmount + "";
		rsInterface.aString228 = "";
		rsInterface.textColor = 12582912;
		rsInterface.anInt219 = 49152;
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentID = 30000;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 30001;
		RSInterface.sprite1 = imageLoader(1, "Lunar/SPRITE");
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface Int = addInterface(30001);
		Int.isMouseoverTriggered = true;
		Int.hoverType = -1;
		setChildren(1, Int);
		addLunarSprite(30002, 0, "SPRITE");
		setBounds(30002, 0, 0, 0, Int);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1,
			int ra2, int rune1, int lvl, String name, String descr,
			TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[3];
		rsInterface.anIntArray212 = new int[3];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.hoverType = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 37, 35, 3, INT);// Rune
		setBounds(rune1, 112, 35, 4, INT);// Rune
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 50, 66, 5, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 123, 66, 6, INT);
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, TextDrawingArea[] TDA, int sid, int suo,
			int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addMagic3Rune(int ID, int r1, int r2, int r3, int ra1,
			int ra2, int ra3, int rune1, int rune2, int lvl, String name,
			String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Magic/BHON");
		rsInterface.sprite1 = imageLoader(sid, "Magic/BHOFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "Magic/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, TextDrawingArea[] TDA, int sid, int suo,
			int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3,
			int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
			String name, String descr, TextDrawingArea[] TDA, int sid, int suo,
			int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	public static void setChildren(int total, RSInterface i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}

	public static void configureLunar(TextDrawingArea[] TDA) {
		homeTeleport();
		constructLunar();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004,
				64, "Bake Pie", "Bake pies without a stove", TDA, 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant",
				"Cure disease on farming patch", TDA, 1, 4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65,
				"Monster Examine",
				"Detect the combat statistics of a\\nmonster", TDA, 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005,
				66, "NPC Contact", "Speak with varied NPCs", TDA, 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006,
				67, "Cure Other", "Cure poisoned players", TDA, 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003,
				67, "Humidify", "Fills certain vessels with water", TDA, 5, 0,
				5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006,
				68, "Training Teleport", "Teleports you to Training Areas",
				TDA, 6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69,
				"Duel Teleport", "Teleports players to Duel", TDA, 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006,
				70, "Boss Teleport", "Teleports you to Bosses", TDA, 8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012,
				70, "Cure Me", "Cures Poison", TDA, 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit",
				"Get a kit of hunting gear", TDA, 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004,
				71, "Wilderness Teleport", "Teleports you to Wilderness Areas",
				TDA, 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72,
				"Cities Teleport", "Teleports you to Cities", TDA, 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012,
				73, "Cure Group", "Cures Poison on players", TDA, 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74,
				"Stat Spy",
				"Cast on another player to see their\\nskill levels", TDA, 14,
				8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74,
				"Minigame Teleport", "Teleports you to Minigames", TDA, 15, 0,
				5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75,
				"Skill Teleport", "Teleports you to Skilling Areas", TDA, 16,
				0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005,
				76, "Superglass Make", "Make glass without a furnace", TDA, 17,
				16, 2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004,
				77, "Khazard Teleport", "Teleports you to Port khazard", TDA,
				18, 0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004,
				78, "Tele Group Khazard", "Teleports players to Port khazard",
				TDA, 19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78,
				"Dream", "Take a rest and restore hitpoints 3\\n times faster",
				TDA, 20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004,
				79, "String Jewellery", "String amulets without wool", TDA, 21,
				0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004,
				80, "Stat Restore Pot\\nShare",
				"Share a potion with up to 4 nearby\\nplayers", TDA, 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004,
				81, "Magic Imbue", "Combine runes without a talisman", TDA, 23,
				0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82,
				"Fertile Soil",
				"Fertilise a farming patch with super\\ncompost", TDA, 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83,
				"Boost Potion Share",
				"Shares a potion with up to 4 nearby\\nplayers", TDA, 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				84, "Fishing Guild Teleport",
				"Teleports you to the fishing guild", TDA, 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004,
				85, "Tele Group Fishing Guild",
				"Teleports players to the Fishing\\nGuild", TDA, 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010,
				85, "Plank Make", "Turn Logs into planks", TDA, 28, 16, 5);
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				86, "Catherby Teleport", "Teleports you to Catherby", TDA, 29,
				0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004,
				87, "Tele Group Catherby", "Teleports players to Catherby",
				TDA, 30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004,
				88, "Ice Plateau Teleport", "Teleports you to Ice Plateau",
				TDA, 31, 0, 5);
		addLunar3RunesLargeBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004,
				89, "Tele Group Ice Plateau",
				"Teleports players to Ice Plateau", TDA, 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90,
				"Energy Transfer",
				"Spend HP and SA energy to\\n give another SA and run energy",
				TDA, 33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91,
				"Heal Other",
				"Transfer up to 75% of hitpoints\\n to another player", TDA,
				34, 8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92,
				"Vengeance Other",
				"Allows another player to rebound\\ndamage to an opponent",
				TDA, 35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006,
				93, "Vengeance", "Rebound damage to an opponent", TDA, 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94,
				"Heal Group", "Transfer up to 75% of hitpoints\\n to a group",
				TDA, 37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95,
				"Spellbook Swap",
				"Change to another spellbook for 1\\nspell cast", TDA, 38, 0, 5);
	}

	public static void constructLunar() {
		RSInterface Interface = addTabInterface(29999);
		setChildren(80, Interface);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 9, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);// hover
		setBounds(30018, 5, 176, 41, Interface);// hover
		setBounds(30026, 5, 176, 42, Interface);// hover
		setBounds(30033, 5, 163, 43, Interface);// hover
		setBounds(30041, 5, 176, 44, Interface);// hover
		setBounds(30049, 5, 176, 45, Interface);// hover
		setBounds(30057, 5, 176, 46, Interface);// hover
		setBounds(30065, 5, 176, 47, Interface);// hover
		setBounds(30076, 5, 163, 48, Interface);// hover
		setBounds(30084, 5, 176, 49, Interface);// hover
		setBounds(30092, 5, 176, 50, Interface);// hover
		setBounds(30100, 5, 176, 51, Interface);// hover
		setBounds(30107, 5, 176, 52, Interface);// hover
		setBounds(30115, 5, 163, 53, Interface);// hover
		setBounds(30123, 5, 176, 54, Interface);// hover
		setBounds(30131, 5, 163, 55, Interface);// hover
		setBounds(30139, 5, 163, 56, Interface);// hover
		setBounds(30147, 5, 163, 57, Interface);// hover
		setBounds(30155, 5, 176, 58, Interface);// hover
		setBounds(30163, 5, 176, 59, Interface);// hover
		setBounds(30171, 5, 176, 60, Interface);// hover
		setBounds(30179, 5, 163, 61, Interface);// hover
		setBounds(30187, 5, 176, 62, Interface);// hover
		setBounds(30195, 5, 149, 63, Interface);// hover
		setBounds(30203, 5, 176, 64, Interface);// hover
		setBounds(30211, 5, 163, 65, Interface);// hover
		setBounds(30219, 5, 163, 66, Interface);// hover
		setBounds(30227, 5, 176, 67, Interface);// hover
		setBounds(30235, 5, 149, 68, Interface);// hover
		setBounds(30243, 5, 176, 69, Interface);// hover
		setBounds(30251, 5, 5, 70, Interface);// hover
		setBounds(30259, 5, 5, 71, Interface);// hover
		setBounds(30267, 5, 5, 72, Interface);// hover
		setBounds(30275, 5, 5, 73, Interface);// hover
		setBounds(30283, 5, 5, 74, Interface);// hover
		setBounds(30291, 5, 5, 75, Interface);// hover
		setBounds(30299, 5, 5, 76, Interface);// hover
		setBounds(30307, 5, 5, 77, Interface);// hover
		setBounds(30323, 5, 5, 78, Interface);// hover
		setBounds(30315, 5, 5, 79, Interface);// hover
	}

	public static void setBounds(int ID, int X, int Y, int frame,
			RSInterface RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}
}