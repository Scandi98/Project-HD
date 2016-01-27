package server.model.content;

import server.model.players.Client;

public class ItemKit {
	
	public static void handleWhipColorKit(Client c, int itemUsed, int useWith) {
		if (itemUsed == 1763 && useWith == 4151 || itemUsed == 4151
				&& useWith == 1763) {
			if (c.getItems().playerHasItem(1763)
					&& c.getItems().playerHasItem(4151)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(1763, 1);
				c.getItems().addItem(15004, 1);
				c.sendMessage("You add the Dye to your whip.");
			}
		} else if (itemUsed == 1765 && useWith == 4151 || itemUsed == 4151
				&& useWith == 1765) {
			if (c.getItems().playerHasItem(1765)
					&& c.getItems().playerHasItem(4151)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(1765, 1);
				c.getItems().addItem(15002, 1);
				c.sendMessage("You add the Dye to your whip.");
			}
		} else if (itemUsed == 1767 && useWith == 4151 || itemUsed == 4151
				&& useWith == 1767) {
			if (c.getItems().playerHasItem(1767)
					&& c.getItems().playerHasItem(4151)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(1767, 1);
				c.getItems().addItem(15003, 1);
				c.sendMessage("You add the Dye to your whip.");
			}
		} else if (itemUsed == 1771 && useWith == 4151 || itemUsed == 4151
				&& useWith == 1771) {
			if (c.getItems().playerHasItem(1771)
					&& c.getItems().playerHasItem(4151)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(1771, 1);
				c.getItems().addItem(15000, 1);
				c.sendMessage("You add the Dye to your whip.");
			}
		} else if (itemUsed == 1773 && useWith == 4151 || itemUsed == 4151
				&& useWith == 1773) {
			if (c.getItems().playerHasItem(1773)
					&& c.getItems().playerHasItem(4151)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(1773, 1);
				c.getItems().addItem(15001, 1);
				c.sendMessage("You add the Dye to your whip.");
			}
		}
	}

	private static int kitItem[] = { 12797, 12457, 12458, 12459, 12419, 12420, 12421,
			12807, 12806 };

	public static boolean isItemKit(int itemId) {
		for (int i = 0; i < kitItem.length; i++) {
			if (kitItem[i] == itemId) {
				return true;
			}
		}
		return false;
	}

	public static void handleDPickKit(Client c, int itemUsed, int usedWith) {
		if (itemUsed == 12800 && usedWith == 11920 || itemUsed == 11920
				&& usedWith == 12800) {
			if (c.getItems().playerHasItem(12800)
					&& c.getItems().playerHasItem(11920)) {
				c.getItems().deleteItem(11920, 1);
				c.getItems().deleteItem(12800, 1);
				c.getItems().addItem(12797, 1);
				c.sendMessage("You combine your Kit with your Dragon Pickaxe.");
			}
		}
	}

	public static void handleWardKit(Client c, int itemUsed, int usedWith) {
		if (itemUsed == 12802 && usedWith == 11926 || itemUsed == 11926
				&& usedWith == 12802) {// Odium
			if (c.getItems().playerHasItem(12802)
					&& c.getItems().playerHasItem(11926)) {
				c.getItems().deleteItem(12802, 1);
				c.getItems().deleteItem(11926, 1);
				c.getItems().addItem(12807, 1);
				c.sendMessage("You combine your Kit with your Odium.");
			}
		} else if (itemUsed == 12802 && usedWith == 11928 || itemUsed == 11928
				&& usedWith == 12802) {// Malediction
			if (c.getItems().playerHasItem(12802)
					&& c.getItems().playerHasItem(11928)) {
				c.getItems().deleteItem(12802, 1);
				c.getItems().deleteItem(11928, 1);
				c.getItems().addItem(12806, 1);
				c.sendMessage("You combine your Kit with your Malediction.");
			}
		}
	}

	public static void handleSaraTear(Client c, int itemUsed, int useWith) {
		if (itemUsed == 12804 && useWith == 11730 || itemUsed == 11730
				&& useWith == 12804) {
			if (c.getItems().playerHasItem(12804)
					&& c.getItems().playerHasItem(11730)) {
				c.getItems().deleteItem(12804, 1);
				c.getItems().deleteItem(11730, 1);
				c.getItems().addItem(12809, 1);
				c.sendMessage("You combine your Tear with your Sara Sword.");
			}
		}
	}

	public static void handleDragonPlateLegsKit(Client c, int itemUsed, int useWith) {
		if (itemUsed == 12536 || useWith == 12536) {
			if (itemUsed == 4087 || useWith == 4087) {
				if (c.getItems().playerHasItem(12536)
						&& c.getItems().playerHasItem(4087)) {
					c.getItems().deleteItem(12536, 1);
					c.getItems().deleteItem(4087, 1);
					c.getItems().addItem(12415, 1);
					c.sendMessage("You combine your Kit with your Platelegs.");
				}
			} else if (itemUsed == 4585 || useWith == 4585) {
				if (c.getItems().playerHasItem(12536)
						&& c.getItems().playerHasItem(4585)) {
					c.getItems().deleteItem(12536, 1);
					c.getItems().deleteItem(4585, 1);
					c.getItems().addItem(12416, 1);
					c.sendMessage("You combine your Kit with your Plateskirt.");
				}
			}
		}
	}

	public static void handleDragonChainKit(Client c, int itemUsed, int useWith) {
		if (itemUsed == 12534 && useWith == 3140 || itemUsed == 3140
				&& useWith == 12534) {
			if (c.getItems().playerHasItem(12534)
					&& c.getItems().playerHasItem(3140)) {
				c.getItems().deleteItem(12534, 1);
				c.getItems().deleteItem(3140, 1);
				c.getItems().addItem(12414, 1);
				c.sendMessage("You combine your Kit with your Chainbody.");
			}
		}
	}

	public static void handleInfinityKit(Client c, int itemUsed, int usedWith) {
		// 12530, 12528
		if (itemUsed == 12530 || usedWith == 12530) {
			for (int i = 0; i < c.infinity.length; i++) {
				if (itemUsed == c.infinity[i] || usedWith == c.infinity[i]) {
					if (c.getItems().playerHasItem(6924)
							&& c.getItems().playerHasItem(6918)
							&& c.getItems().playerHasItem(6916)) {
						c.getItems().deleteItem(6924, 1);
						c.getItems().deleteItem(6918, 1);
						c.getItems().deleteItem(6916, 1);
						c.getItems().deleteItem(12530, 1);
						c.getItems().addItem(12421, 1);
						c.getItems().addItem(12420, 1);
						c.getItems().addItem(12419, 1);
						c.sendMessage("You combine your Light Infinity Kit with your Infinity Robes.");
					}
				}
			}
		} else if (itemUsed == 12528 || usedWith == 12528) {
			for (int i = 0; i < c.infinity.length; i++) {
				if (itemUsed == c.infinity[i] || usedWith == c.infinity[i]) {
					if (c.getItems().playerHasItem(6924)
							&& c.getItems().playerHasItem(6918)
							&& c.getItems().playerHasItem(6916)) {
						c.getItems().deleteItem(6924, 1);
						c.getItems().deleteItem(6918, 1);
						c.getItems().deleteItem(6916, 1);
						c.getItems().deleteItem(12528, 1);
						c.getItems().addItem(12459, 1);
						c.getItems().addItem(12457, 1);
						c.getItems().addItem(12458, 1);
						c.sendMessage("You combine your Dark Infinity Kit with your Infinity Robes.");
					}
				}
			}
		}
	}
	
	public static void handleZulrahItems(Client c, int itemUsed, int useWith) {
		if (itemUsed == 11791 && useWith == 12932 || itemUsed == 12932
				&& useWith == 11791) {
			if (c.getItems().playerHasItem(11791) && c.getItems().playerHasItem(12932) && c.playerLevel[c.playerCrafting] >= 59) {
				c.getItems().deleteItem(11791, 1);
				c.getItems().deleteItem(12932, 1);
				c.getItems().addItem(12904, 1);
				c.sendMessage("You add the Magic Fang to your Staff of the Dead.");
			} else if (c.playerLevel[c.playerCrafting] < 59) {
				c.sendMessage("You need a crafting level of 59 to do this.");
				return;
			}
		}
		if (itemUsed == 11907 && useWith == 12932 || itemUsed == 12932
				&& useWith == 11907) {
			if (c.getItems().playerHasItem(11907) && c.getItems().playerHasItem(12932) && c.playerLevel[c.playerCrafting] >= 59) {
				c.getItems().deleteItem(11907, 1);
				c.getItems().deleteItem(12932, 1);
				c.getItems().addItem(12899, 1);
				c.sendMessage("You add the Magic Fang to your Trident of the Seas.");
			} else if (c.playerLevel[c.playerCrafting] < 59) {
				c.sendMessage("You need a crafting level of 59 to do this.");
				return;
			}
		}
		if (itemUsed == 12922 && useWith == 1755 || itemUsed == 1755 && useWith == 12922) {
			if (c.getItems().playerHasItem(12922) && c.getItems().playerHasItem(1755) && c.playerLevel[c.playerCrafting] >= 53) {
				c.getItems().deleteItem(12922, 1);
				c.getItems().addItem(12926, 1);
				c.sendMessage("You chisel the fang to make a Toxic Blowpipe.");
			} else if (c.playerLevel[c.playerCrafting] < 53) {
				c.sendMessage("You need a crafting level of 53 to do this.");
				return;
			}
		}
		if (itemUsed == 12927 && useWith == 1755 || itemUsed == 1755 && useWith == 12927) {
			if (c.getItems().playerHasItem(12927) && c.getItems().playerHasItem(1755) && c.playerLevel[c.playerCrafting] >= 52) {
				c.getItems().deleteItem(12927, 1);
				c.getItems().addItem(12931, 1);
				c.sendMessage("You chisel the fang to make a Serpentine Helm.");
			} else if (c.playerLevel[c.playerCrafting] < 52) {
				c.sendMessage("You need a crafting level of 52 to do this.");
				return;
			}
		}
		if (itemUsed == 13201 && useWith == 12931 || itemUsed == 12931 && useWith == 13201) {
			if (c.getItems().playerHasItem(13201) && c.getItems().playerHasItem(12931)) {
				c.getItems().deleteItem(13201, 1);
				c.getItems().deleteItem(12931, 1);
				c.getItems().addItem(13199, 1);
				c.sendMessage("You add the mutagen to your helmet.");
			}
		}
		if (itemUsed == 13200 && useWith == 12931 || itemUsed == 12931 && useWith == 13200) {
			if (c.getItems().playerHasItem(13200) && c.getItems().playerHasItem(12931)) {
				c.getItems().deleteItem(13200, 1);
				c.getItems().deleteItem(12931, 1);
				c.getItems().addItem(13197, 1);
				c.sendMessage("You add the mutagen to your helmet.");
			}
		}
	}

	public static boolean handleRevertKit(Client c, int itemId) {
		if (itemId == 12797) {
			if (c.getItems().playerHasItem(12797) && c.getItems().freeSlots() >= 1) {
				c.getItems().deleteItem(12797, 1);
				c.getItems().addItem(11920, 1);
				c.getItems().addItem(12800, 1);
				c.sendMessage("You revert your Dragon Pickaxe to normal.");
				return true;
			} else if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Not enough space in your inventory.");
				return false;
			}
		} else if (itemId == 12459 || itemId == 12457 || itemId == 12458) {
			if (c.getItems().playerHasItem(12459)
					&& c.getItems().playerHasItem(12457)
					&& c.getItems().playerHasItem(12458)
					&& c.getItems().freeSlots() >= 1) {
				c.getItems().deleteItem(12459, 1);
				c.getItems().deleteItem(12457, 1);
				c.getItems().deleteItem(12458, 1);
				c.getItems().addItem(6924, 1);
				c.getItems().addItem(6918, 1);
				c.getItems().addItem(6916, 1);
				c.getItems().addItem(12528, 1);
				c.sendMessage("You revert your Dark Infinity to normal.");
				return true;
			} else if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Not enough space in your inventory.");
				return false;
			}
		} else if (itemId == 12419 || itemId == 12420 || itemId == 12421) {
			if (c.getItems().playerHasItem(12419)
					&& c.getItems().playerHasItem(12420)
					&& c.getItems().playerHasItem(12421)
					&& c.getItems().freeSlots() >= 1) {
				c.getItems().deleteItem(12419, 1);
				c.getItems().deleteItem(12420, 1);
				c.getItems().deleteItem(12421, 1);
				c.getItems().addItem(6924, 1);
				c.getItems().addItem(6918, 1);
				c.getItems().addItem(6916, 1);
				c.getItems().addItem(12530, 1);
				c.sendMessage("You revert your Light Infinity to normal.");
				return true;
			} else if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Not enough space in your inventory.");
				return false;
			}
		} else if (itemId == 12806) {
			if (c.getItems().playerHasItem(12806) && c.getItems().freeSlots() >= 1) {
				c.getItems().deleteItem(12806, 1);
				c.getItems().addItem(12802, 1);
				c.getItems().addItem(11928, 1);
				c.sendMessage("You revert your Malediction ward.");
			} else if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Not enough space in your inventory.");
				return false;
			}
		} else if (itemId == 12807) {
			if (c.getItems().playerHasItem(12807) && c.getItems().freeSlots() >= 1) {
				c.getItems().deleteItem(12807, 1);
				c.getItems().addItem(12802, 1);
				c.getItems().addItem(11926, 1);
				c.sendMessage("You revert your Odium ward.");
			} else if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Not enough space in your inventory.");
				return false;
			}
		}
		return false;
	}

}
