package server.tick.test;

import server.core.PlayerHandler;
import server.core.World;
import server.model.players.Client;
import server.model.players.Player;

public class InstanceFloorReset extends ScheduledTask {

	public InstanceFloorReset() {
		super(600);
	}

	@Override
	public void execute() {
		for (Player player : PlayerHandler.players)
			if(player != null) {
				//player.instanceFloorReset();
			}
	}
}
