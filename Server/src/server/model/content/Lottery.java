package server.model.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import server.Config;
import server.Server;
import server.model.players.Client;
import server.util.Misc;

public class Lottery {

	private static final int LOTTERY_PRICE = 30000000;
	private final static File file = new File(Config.LOAD_DIRECTORY + "lottery.txt");

	@SuppressWarnings("resource")
	public static void enterLottery(Client c) throws IOException {
		if (c.getItems().playerHasItem(995, LOTTERY_PRICE)
				&& c.playerRights != 10) {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.equalsIgnoreCase(c.playerName)) {
					c.sendMessage("You cannot join the lottery twice!");
					return;
				}
			}
			c.getItems().deleteItem(995, LOTTERY_PRICE);
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(Config.LOAD_DIRECTORY + "lottery.txt", true)));
			out.println(c.playerName + "");
			out.close();
			c.sendGlobal("@bla@[@cr6@@red@Lottery@cr6@@bla@] " + c.playerName
					+ " has just entered the lottery! ::join !");
			c.sendMessage("You have just Paid 30M Coins to join the lottery.");
		} else if (!c.getItems().playerHasItem(995, LOTTERY_PRICE)) {
			c.sendMessage("It costs 30M Coins to join the lottery!");
		} else if (c.playerRights == 10) {
			c.sendMessage("Iron men cannot join the lottery!");
		}
	}

	private static List<String> nameMap = new ArrayList<String>();

	@SuppressWarnings("resource")
	public static void pickWinner(Client c) throws IOException {
		String name = "";

		Scanner names = new Scanner(file);

		while (names.hasNext()) {
			name = names.nextLine();
			nameMap.add(name);
		}
		if (nameMap.size() > 8) {
			int index = Misc.random(nameMap.size() - 1);
			String winner = nameMap.get(index);
			Client player = (Client) Server.playerHandler.getPlayer(winner);
			if (player != null) {
				player.sendMessage("You have won the lottery!");
				player.sendGlobal("@bla@[@cr6@@red@Lottery@cr6@@bla@] "
						+ player.playerName
						+ " has just won the lottery! Pot: "
						+ convertChatCurrency(nameMap.size() * 15000000));
				if (player.getItems().freeSlots() > 0) {
					player.getItems().addItem(995, nameMap.size() * 15000000);
				} else {
					player.getItems().addItemToBank(995,
							nameMap.size() * 15000000);
					player.sendMessage("Your Winnings have been added to your bank.");
				}
			} else {
				c.sendGlobal("Lottery Winner: "+winner+" Is currently offline. Pot: "+ convertChatCurrency(nameMap.size() * 15000000));
			}
			names.close();
			nameMap.clear();
			Logger.getAnonymousLogger().info("Cleaning up Lottery Class...");
			FileWriter fw = new FileWriter(Config.LOAD_DIRECTORY + "lottery.txt");
			PrintWriter pw = new PrintWriter(fw);
			pw.write("");
			pw.flush();
			pw.close();
		} else {
			c.sendMessage("Not enough lottery players, Amount in: "+nameMap.size());
		}
	}

	private static String convertChatCurrency(int amount) {
		String ShopAdd = "";
		if (amount >= 1000 && amount < 1000000) {
			ShopAdd = " " + (amount / 1000) + "K";
		} else if (amount >= 1000000) {
			ShopAdd = " " + (amount / 1000000) + " million";
		} else {
			ShopAdd = amount + "";
		}
		return ShopAdd;
	}

}
