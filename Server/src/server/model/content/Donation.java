package server.model.content;

import java.sql.ResultSet;

import server.model.players.Client;
import server.model.players.Player;

public class Donation implements Runnable {

	public static final String HOST_ADDRESS = "store.dragon-ages.org";
	public static final String USERNAME = "dragonah_central";
	public static final String PASSWORD = "y{sAZ!_?@brp";
	public static final String DATABASE = "dragonah_central";
	
	private Player player;
	Client c = (Client)player;

	
	@Override
	public void run () {
		try {
			Database db = new Database(HOST_ADDRESS, USERNAME, PASSWORD, DATABASE);
			
			if (!db.init()) {
				System.err.println("[Donation] Failed to connect to database!");
				return;
			}
			String name = player.playerName.replace("_", " ");
			ResultSet rs = db.executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND claimed=0");
			
			while(rs.next()) {
				String item_name = rs.getString("item_name");
				int item_number = rs.getInt("item_number");
				double amount = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				
				ResultSet result = db.executeQuery("SELECT * FROM products WHERE item_id="+item_number+" LIMIT 1");
				
				if (result == null || !result.next()
						|| !result.getString("item_name").equalsIgnoreCase(item_name)
						|| result.getDouble("item_price") != amount
						|| quantity < 1 || quantity > Integer.MAX_VALUE) {
					System.out.println("[Donation] Invalid purchase for "+name+" (item: "+item_name+", id: "+item_number+")");
					continue;
				}
				
				handleItems(item_number);
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleItems(int productId) {
		switch(productId) {
		case 0:
			c.sendMessage("You've received your Donation reward! Congratulations! Look in Bank!");
			c.getItems().addItemToBank(13192, 1);
			// handle item stuff, like adding items, points, etc.
			break;
		}
	}
	
	public Donation(Player player) {
		this.player = player;
	}
}
