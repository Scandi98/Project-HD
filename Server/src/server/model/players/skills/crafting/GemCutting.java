package server.model.players.skills.crafting;

import server.Config;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

/*
 * Author Tim<Dark-Asylum>
 */
public class GemCutting {
	
	private final static int CHISEL = 1755;
	
	private static enum Gems {
		//Uncut, Cut, Animation, XP, Level Req
		SAPPHIRE(1623, 1607, 888, 75, 1),
		EMERALD(1621, 1605, 889, 150, 27),
		RUBY(1619, 1603, 887, 225, 34),
		DIAMOND(1617, 1601, 887, 275, 43),
		DRAGONSTONE(1631, 1615, 885, 325, 55),
		ONYX(6571, 6573, 885, 375, 55);
		
		
		private int uncutId, cutId, animation, xp, levelReq;
		
		private Gems(int uncutId, int cutId, int animation, int xp, int levelReq) {
			this.uncutId = uncutId;
			this.cutId = cutId;
			this.animation = animation;
			this.xp = xp;
			this.levelReq = levelReq;
		}
		
		private int getUncutId() {
			return uncutId;
		}
		
		private int getCutId() {
			return cutId;
		}
		
		private int getAnim() {
			return animation;
		}
		
		private int getXP() {
			return Config.CRAFTING_EXPERIENCE * xp;
		}
		
		private int getLevelReq() {
			return levelReq;
		}
		
		private static Gems getID(final int ID) {
			for (Gems s : Gems.values()) {
				if (s.getUncutId() == ID) {
					return s;
				}
			}
			return null;
		}
	}
	
	static String getLine(Client c) {
		return c.below459 ? ("\\n\\n\\n\\n") : ("\\n\\n\\n\\n\\n");
	}
	
	public static void useItemInterface(final Client c, final int useWith,
			final int itemUsed) {
		for (Gems s : Gems.values()) {
			if (s == null) {
				return;
			}
			if (useWith == CHISEL && itemUsed == s.getUncutId()
					|| useWith == s.getUncutId() && itemUsed == CHISEL) {
				handleInterface(c, s.getUncutId());
			}
		}
	}
	
	private static void handleInterface(final Client c, final int gemID) {
		Gems s = Gems.getID(gemID);
		c.getPA().sendFrame164(4429);
		c.isOnInterface = true;
		c.isCutting = true;
		c.stringu = s.getUncutId();
		boolean view190 = true;
		c.getPA().sendFrame246(1746, view190 ? 140 : 140, s.getCutId());
		c.getPA().sendFrame126(
				getLine(c) + "" + c.getItems().getItemName(s.getCutId()) + "",
				2799);
	}
	
	public static void cutGem(final Client c, final int item, final int amount) {
		final Gems s = Gems.getID(item);
		c.getPA().closeAllWindows();
		if (System.currentTimeMillis() - c.lastThieve < 750) {
			return;
		}
		c.lastThieve = System.currentTimeMillis();
		if (c.playerLevel[c.playerCrafting] < s.getLevelReq()) {
			c.sendMessage("You must have a crafting level of at least "
					+ s.getLevelReq() + " to cut this gem.");
			return;
		}
		c.startAnimation(s.getAnim());
		c.doAmount = amount;
		c.isCutting = true;
		c.isSkilling[c.playerCrafting] = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.doAmount == 0) {
					container.stop();
					return;
				}
				if (c.isSkilling[c.playerCrafting] == false) {
					container.stop();
					return;
				}
				if (!c.getItems().playerHasItem(CHISEL)
						|| !c.getItems().playerHasItem(s.getUncutId())) {
					c.sendMessage("You do not have the correct supplies to craft this.");
					container.stop();
					return;
				}
				c.startAnimation(s.getAnim());
				c.getItems().deleteItem(s.getUncutId(), 1);
				c.getItems().addItem(s.getCutId(), 1);
				c.getPA().addSkillXP(s.getXP(), c.playerCrafting);
				c.doAmount--;
			}

			@Override
			public void stop() {
				c.getPA().closeAllWindows();
				c.startAnimation(c.playerStandIndex);
				c.doAmount = 0;
				c.isCutting = false;
				c.isOnInterface = false;
				c.isSkilling[c.playerCrafting] = false;
			}
		}, 2);
	}

}
