package server.model.content;

import server.model.players.Client;
import server.util.Misc;

public class MoneyPouch {
	
	/**
	 * Player
	 */
	private final Client player;
	
	/**
	 * Money Pouch
	 * @param player
	 */
	public MoneyPouch(Client player) {
		this.player = player;
	}
	
	/**
	 * Format coins
	 * @param amount
	 * @return
	 */
	public String formatCoins(long amount) {
		if (amount >= 1_000 && amount < 1_000_000) {
			return "" + (amount / 1_000) + "K";
		}
		
		if (amount >= 1_000_000 && amount < 1_000_000_000) {
			return "" + (amount / 1_000_000) + "M";
		}
		
		if (amount >= 1_000_000_000) {
			return "" + (amount / 1_000_000_000) + "B";
		}
		return "" + amount;
	}
	
	/**
	 * Adds coins to Money Pouch
	 */
	public void addPouch() {

	
		//Check if player is in a position to add coins
		if (player.inWild()) {
			player.sendMessage("You can't do this right now!");
			return;
		}
				
		//Check if money pouch is filled
		if (player.moneyPouch == Long.MAX_VALUE) {
			player.sendMessage("Your pouch is already full!");
			return;
		}
		
		//Grabs amount of coins to store
		int amount = player.getItems().getItemAmount(995);
		
		//Checks if current stored coins + new coins to store exceed the max value
		if (player.moneyPouch + amount >= Long.MAX_VALUE) {
			player.sendMessage("You can't fit all that into your pouch!");
			return;
		}
	
		//Removes coins from inventory
		player.getItems().deleteItem(995, amount);
		
		//Adds coins to Money Pouch
		player.moneyPouch += amount;
		
		//Sends confirmation message
		player.sendMessage("@dre@You have added " + Misc.format(amount) + " coins into your pouch. Total: " + formatCoins(player.moneyPouch) + ".");
		
		//Updates string
		player.getPA().sendFrame126(player.moneyPouch + "", 8135);
	}
	
	/**
	 * Withdraw coins from Money Pouch
	 * @param amount
	 */
	public void withdrawPouch(long amount) {


		// Check if player is in a position to withdraw coins
		if (player.inWild()) {
			player.sendMessage("You can't do this right now!");
			return;
		}
		
		if (player.moneyPouch == 0) {
			player.sendMessage("You have no coins in your pouch!");
			return;
		}

		// Checks if player is withdrawing a negative amount
		if (amount <= 0) {
			player.sendMessage("You can't withdraw a negative amount!");
			return;
		}

		// Checks if player has the amount to withdraw stored
		if (player.moneyPouch < amount) {
			amount = player.moneyPouch;
		}

		// Checks if coins in inventory + amount to withdraw passes max value
		if ((long) (player.getItems().getItemAmount(995) + amount) > Integer.MAX_VALUE) {
			player.sendMessage("You don't have enough space to withdraw that many coins!");
			amount = Integer.MAX_VALUE - player.getItems().getItemAmount(995);
		}

		// Check to see if player is withdrawing more than max value
		if (amount > Integer.MAX_VALUE) {
			player.sendMessage("You can't withdraw more than 2B coins at a time!");
			return;
		}

		// Checks if player has max value of coins in inventory
		if (player.getItems().getItemAmount(995) == Integer.MAX_VALUE) {
			player.sendMessage("You can't withdraw any more coins!");
			return;
		}

		// Checks if player has space to withdraw the coins
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You do not have enough inventory spaces to withdraw coins.");
			return;
		}

		// Removes coins from pouch
		player.moneyPouch -= amount;

		// Adds coins to inventory
		player.getItems().addItem(995, (int) amount);

		// Updates string
		player.getPA().sendFrame126(player.moneyPouch + "", 8135);
	}

}
