package server.core.event.impl;

import server.core.World;
import server.core.event.Event;
import server.core.task.impl.YellTask;

public class YellEvent extends Event {

	public YellEvent() {
		super(180000);
	}

	@Override
	public void execute() {
		World.getWorld().submit(new YellTask());
	}

}
