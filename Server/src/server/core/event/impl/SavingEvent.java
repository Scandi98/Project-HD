package server.core.event.impl;


import server.core.PlayerHandler;
import server.core.World;
import server.core.event.Event;
import server.core.task.impl.SavePlayers;

public class SavingEvent extends Event {

	public SavingEvent() {
		super(120000);
	}

	@Override
	public void execute() {
		if (PlayerHandler.playerCount <= 0) {
			return;
		}
		World.getWorld().submit(new SavePlayers());
	}

}
