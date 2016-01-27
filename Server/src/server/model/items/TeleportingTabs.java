package server.model.items;

import server.Config;
import server.core.World;
import server.core.event.Event;
import server.model.players.Client;

public class TeleportingTabs {

	final static int ANIM = 4731, GFX = 678;

	public enum TabData {
		VARROCK(8007, 3210, 3424), LUMBRIDGE(8008, 3222, 3218), FALADOR(8009,
				2964, 3378), CAMELOT(8010, 2747, 3477), TROLLHEIM(8013, 2413, 3805), ARDOUGNE(8011, 2662,
				3305), WATCHTOWER(8012, 2549, 3112);

		private int itemId, posX, posY;

		TabData(int itemId, int posX, int posY) {
			this.itemId = itemId;
			this.posX = posX;
			this.posY = posY;
		}

		public int getItemId() {
			return itemId;
		}

		public int getPosX() {
			return posX;
		}

		public int getPosY() {
			return posY;
		}

		public static TabData forId(int itemId) {
			for (TabData data : TabData.values()) {
				if (data.itemId == itemId)
					return data;
			}
			return null;
		}
	}

	public void useTeleTab(final Client c, int slot, final TabData data) {
		if (c.isTeleporting)
			return;
		//if (System.currentTimeMillis() - c.logoutDelay < 10000) {
		//	c.sendMessage("You must wait a few seconds out of combat before you can teleport.");
		//	return;
		//}
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.inPcGame() || c.inPcBoat() || c.inClanWarsGame
				|| c.inClanWarsWait) {
			c.startAnimation(-1);
			c.sendMessage("You can't teleport from this location!");
			return;
		}
		if (c.isInJail()) {
			c.sendMessage("You cannot teleport out of jail.");
			return;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "
					+ Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		c.isTeleporting = true;
		c.startAnimation(ANIM);
		c.gfx0(GFX);
		c.getItems().deleteItem(data.getItemId(), slot, 1);
		World.getWorld().submit(new Event(1000) {


			@Override
			public void execute() {
				c.getPA().movePlayer(data.posX, data.posY, 0);
				c.isTeleporting = false;
				stop();
			}
		});
	}
}
