package server.model.players;

import java.util.ArrayList;

import server.Config;
import server.core.PlayerHandler;
import server.tick.Task;
import server.tick.TaskHandler;
import server.util.Misc;

public class PlayerPanel {

	
/*	public static void open(Client player) {
		
		player.getPA().showInterface(52000);
		int line = 52003;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			Client p = player.getClient(i);
			if (!player.validClient(i))
				continue;
			
			if (p.playerName != null) {
				String title = "";
				if (p.playerRights == 1) {
					title = "Mod, ";
				} else if (p.playerRights == 2) {
					title = "Admin, ";
				}
				
				player.getPA().sendNewString(
						"@dre@" + p.playerName + "@blu@ (" + title
								+ ")", line);
				line++;
			}
		}
	}*/
	public static boolean viewing = false;
	public static void open(Client player) {
		viewing = true;
		TaskHandler.submit(new Task(25, true) {
			@Override
			public void execute() {
				if(viewing != true) {
					this.cancel();
				}
				ArrayList<String> players = new ArrayList<String>();
				for (Player p : PlayerHandler.players) {
					if (p != null) {
						String title = "";
						if (p.playerRights == 1) {
							title = "@blu@" + p.playerName;
						} else if (p.playerRights == 2) {
							title = "@yel@" + p.playerName;
						} else if (p.playerRights == 3) {
							title = "@red@" +p.playerName ;
						} else {
							title = p.playerName;
						}
						
						players.add(title  + "" );
						
					}
				}
				int index = 0;
				for (String s : players) {
					player.getPA().sendFrame126(s, 52003 + index);
					index++;
				}
				for (int i = index; i < 100; i++)
					player.getPA().sendFrame126("", 52003 + i);
				player.getPA().showInterface(52000);
				
			}

			@Override
			public void onCancel() {
				player.getPA().closeAllWindows();
			}
		});
	}
}
