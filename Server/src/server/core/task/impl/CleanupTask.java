package server.core.task.impl;

import server.core.GameEngine;
import server.core.task.Task;

/**
 * Performs garbage collection and finalization.
 * @author Graham Edgecombe
 *
 */
public class CleanupTask implements Task {

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			public void run() {
				System.runFinalization();
				//System.gc();
			}
		});
	}

}
