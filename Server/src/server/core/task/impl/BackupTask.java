package server.core.task.impl;

import java.util.logging.Logger;

import server.core.GameEngine;
import server.core.task.Task;
import server.model.content.PlayerBackup;

public class BackupTask implements Task {

	@Override
	public void execute(GameEngine context) {
		PlayerBackup backup = new PlayerBackup();
		backup.zipFile();
		Logger.getAnonymousLogger().info("Successfully Created Character Backup.");
	}

}
