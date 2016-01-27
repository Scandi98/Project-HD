package server.core.event.impl;

import server.core.World;
import server.core.event.Event;
import server.core.task.impl.BackupTask;

public class BackupEvent extends Event {

	public BackupEvent() {
		super(3600000);
	}

	@Override
	public void execute() {
		World.getWorld().submit(new BackupTask());
	}

}
