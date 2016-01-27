/** Written by Keith, for DreamScape317 **/
package server.model.content;

import server.core.PlayerHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.util.Misc;

public class Target {

	// ** Max combat level difference between players **//*
	public final int MCD = 5;

	public int totalTargets = 0;

	public boolean penalized = false;

	public Client inWilds[] = new Client[200];
	
	private CycleEvent event;

	public Target() {
		process();
	}

	public void checkAfkTarget(Client c) {
		// sec i got something :P
		if (c.playerTarget != null) {
			processInterfaceChange(c, (Client) c.playerTarget);
			if (c.playerTarget.disconnected) {
				c.targetTicks = 0;
				Client o = (Client)c.playerTarget;
				o.playerTarget = null;
				c.playerTarget = null;
				c.sendMessage("[TARGET]:  <shad=65280>You no longer have a target.");
				c.cleanInterface();
				c.getPA().createPlayerHints(10, -1);
				reset();
			} else {
				if (c.targetTicks < 16) {
					if (!c.playerTarget.inWild()) {
						updateInterface(c, (Client) c.playerTarget);
						c.targetTicks++;
						if (c.targetTicks == 4) {
							Client opp = (Client) c.playerTarget;
							opp.sendMessage("You have @red@90 seconds@bla@ to return to the Wilderness!");
						} else if (c.targetTicks == 8) {
							Client opp = (Client) c.playerTarget;
							opp.sendMessage("You have @red@60 seconds@bla@ to return to the Wilderness!");
						} else if (c.targetTicks == 12) {
							Client opp = (Client) c.playerTarget;
							opp.sendMessage("You have @red@30 seconds@bla@ to return to the Wilderness!");
						}
					}
				} else {
					c.targetTicks = 0;
					Client o = (Client)c.playerTarget;
					o.getPA().createPlayerHints(10, -1);
					o.sendMessage("You failed to return to your target in time, so you have lost your target.");
					o.playerTarget = null;
					c.playerTarget = null;
					c.sendMessage("[TARGET]:  <shad=65280>You no longer have a target.");
					c.cleanInterface();
					c.getPA().createPlayerHints(10, -1);
					reset();
				}
			}
		} else {
			c.cleanInterface();
		}
	}

	public void process() {// try this in the server..java or inlizs
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			public void execute(CycleEventContainer c) {
				doProcess();
			}

			public void stop() {
			}
		}, 16);
	}

	public void processInterfaceChange(Client c, Client o) {
		c.updateInterface(c, o);
		o.updateInterface(o, c);
	}

	public void sendTargets() {
		if (getAmountInWild() <= 1) {
			return;
		}
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] != null && inWilds[i + 1] != null) {
				Client newTarget = inWilds[Misc.random(totalTargets)];
				if (newTarget != null) {
					if (canBeTargets(newTarget, inWilds[i])) {
						if (inWilds[i].playerTarget == null
								&& newTarget.playerTarget == null) {
							applyTargets(inWilds[i], newTarget);
							reset(i);
						}
					}
				}
			}
		}
		reset();
	}

	public void applyTargets(Client c, Client o) {
		if (c == o) {
			return;
		}
		hastarget = true;
		c.sendMessage("[TARGET]:  <shad=ff0000>You've been assigned a target to kill : <shad=65535>"
				+ o.playerName);
		o.sendMessage("[TARGET]:  <shad=ff0000>You've been assigned a target to kill : <shad=65535>"
				+ c.playerName);
		c.playerTarget = o;
		o.playerTarget = c;
		c.targetIndex = o.playerId;
		o.targetIndex = c.playerId;
		c.getPA().createPlayerHints(10, o.playerId);
		o.getPA().createPlayerHints(10, c.playerId);
		c.getPA().sendNewString(Misc.formatPlayerName(o.playerName), 27307);
		o.getPA().sendNewString(Misc.formatPlayerName(c.playerName), 27307);
		c.getPA().sendNewString(getWildernessLevel(c, o), 27308);
		o.getPA().sendNewString(getWildernessLevel(o, c), 27308);
		findSkull(c, o);
		findSkull(o, c);
		c.getPA().sendNewString(findWealth(o), 27305);
		o.getPA().sendNewString(findWealth(c), 27305);
	}

	public String getWildernessLevel(Client c, Client o) {
		if (!o.inWild()) {
			return "Safe, Cmb " + o.combatLevel;
		}
		int wildernessDifference = o.wildLevel + 3;// what color?
		String color = "";
		if (c.distanceToPoint(o.absX, o.absY) <= 15) {
			color = "@gre@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 15
				&& c.distanceToPoint(o.absX, o.absY) <= 30) {
			color = "@bro@";
		} else if (c.distanceToPoint(o.absX, o.absY) > 30
				&& c.distanceToPoint(o.absX, o.absY) <= 60) {
			color = "@bl2@";
		}
		return color + "Lvl " + o.wildLevel + "-" + wildernessDifference
				+ ", Cmb " + o.combatLevel;
	}

	public int getWealth(Client c) {
		return c.getWealth();
	}

	public String findWealth(Client c) {

		if (getWealth(c) >= 0 && getWealth(c) <= 20000) {
			return "Wealth: V. Low";
		} else if (getWealth(c) > 20000 && getWealth(c) <= 40000) {
			return "Wealth: Low";
		} else if (getWealth(c) > 40000 && getWealth(c) <= 80000) {
			return "Wealth: Medium";
		} else if (getWealth(c) > 80000 && getWealth(c) <= 300000) {
			return "Wealth: High";
		} else if (getWealth(c) > 300000) {
			return "Wealth: V. High";
		}
		return "";
	}

	public void findSkull(Client c, Client o) {
		if (getWealth(c) >= 0 && getWealth(c) <= 20000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(877, 1);
		} else if (getWealth(c) > 20000 && getWealth(c) <= 40000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(878, 1);
		} else if (getWealth(c) > 40000 && getWealth(c) <= 80000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(879, 1);
		} else if (getWealth(c) > 80000 && getWealth(c) <= 300000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(881, 1);
		} else if (getWealth(c) > 300000) {
			c.getPA().sendConfig(876, 1);
			c.getPA().sendConfig(880, 1);
		}
	}

	public void updateInterface(Client c, Client o) {
		c.getPA().sendNewString(Misc.formatPlayerName(o.playerName), 27307);
		o.getPA().sendNewString(Misc.formatPlayerName(c.playerName), 27307);
		c.getPA().sendNewString(getWildernessLevel(c, o), 27308);
		o.getPA().sendNewString(getWildernessLevel(o, c), 27308);
		c.getPA().sendNewString(findWealth(o), 27305);
		o.getPA().sendNewString(findWealth(c), 27305);
	}

	public void doProcess() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				checkAfkTarget((Client) PlayerHandler.players[i]);
				if (PlayerHandler.players[i].inWild()) {
					isInWild((Client) PlayerHandler.players[i]);
				}
			}
		}
		sendTargets();
	}

	public void isInWild(Client c) {
		if (c.playerTarget == null
				&& System.currentTimeMillis() - c.lastTarget >= 59000) {
			inWilds[getSlot()] = c;
			totalTargets++;
		}
	}

	public int getSlot() {
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public int getAmountInWild() {
		int amount = 0;
		for (int i = 0; i < inWilds.length; i++) {
			if (inWilds[i] != null) {
				amount++;
			}
		}
		return amount;
	}

	public void reset() {
		hastarget = false;
		for (int i = 0; i < inWilds.length; i++) {
			inWilds[i] = null;
		}
		totalTargets = 0;
	}

	public static boolean hastarget = false;

	public void reset(int j) {
		inWilds[j] = null;
		totalTargets--;
	}

	public static void handleKilled(Client killer, Client victim) {
		if (killer.playerTarget == victim) {
			// killer.sendMessage("[TARGET]:  <shad=65280>You killed your target and you receive 1 bounty point.");
			// killer.bountyPoints ++;
			killer.sendMessage("[TARGET]:  <shad=65280>You killed your target and you receive 2 extra pk points.");
			killer.pkPoints += 2;
			killer.playerTarget = null;
			victim.playerTarget = null;
			killer.lastTarget = System.currentTimeMillis();
			victim.lastTarget = System.currentTimeMillis();
			killer.sendMessage("[TARGET]:  <shad=65280>You will be able to get a new target in 1 minute");
			victim.sendMessage("[TARGET]:  <shad=65280>You will be able to get a new target in 1 minute");
			victim.getPA().createPlayerHints(10, -1);
			killer.getPA().createPlayerHints(10, -1);
			killer.hunterPoints += 1;
			killer.sendMessage("You have gained 1 Hunter Point for killing your target!");
		}
	}

	public boolean canBeTargets(Client c, Client o) {
		int ccb = c.combatLevel;
		int ocb = o.combatLevel;
		if (ccb - ocb <= MCD || ocb - ccb <= MCD) {
			return true;
		}
		return false;
	}

}