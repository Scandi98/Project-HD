package server.model.content.instance.impl;

import server.core.PlayerHandler;
import server.model.players.Player;
import server.tick.test.ScheduledTask;

public class InstanceFloorReset extends ScheduledTask {

	public InstanceFloorReset() {
		super(600);
	}

	@Override
	public void execute() {
		for (Player player : PlayerHandler.players)
			if(player != null) {
			//	player.instanceFloorReset();
			}
	}
}
