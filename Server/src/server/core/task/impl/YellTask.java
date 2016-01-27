package server.core.task.impl;

import server.core.GameEngine;
import server.core.PlayerHandler;
import server.core.task.Task;
import server.model.players.Client;
import server.util.Misc;

public class YellTask implements Task {

	private String[] MESSAGES = {
			"Hope you're enjoying Dragon-Age, tell your friends!",
			"Make sure you sign up on our ::forums!!",
			"If you have any suggestions feel free to post them! ::forums",
			"Make sure you ::vote! it helps us grow.",
			"Want to help the server? ::vote",
			"IT'S CHRISTMAS DO ::xmas TO RECEIVE A FREE XMAS PRESENT!" };

	@Override
	public void execute(GameEngine context) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				int random = Misc.random(MESSAGES.length - 1);
				String message = MESSAGES[random];
				c2.sendMessage("@cr9@<shad=0>@cya@" + message);
			}
		}
	}
}
