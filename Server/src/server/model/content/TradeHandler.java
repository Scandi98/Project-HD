package server.model.content;

import server.Config;
import server.Server;
import server.model.Trade;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PlayerSave;

public class TradeHandler {
	private Client client;
	private Trade currentTrade = null;

	public void tradeItem(int itemID, int fromSlot, int amount) {
		if (stage != 1)
			return;
		for (int id : Config.ITEM_TRADEABLE) {
			if (itemID == id) {
				client.sendMessage("You can't trade this item.");
				return;
			}
		}
		if (amount > 0 && itemID == (client.playerItems[fromSlot] - 1)) {
			if (amount > client.playerItemsN[fromSlot]) {
				amount = client.playerItemsN[fromSlot];
			}
			boolean isInTrade = false;
			for (int i = 0; i < offer.length; i++) {
				if (offer[i] == client.playerItems[fromSlot]) {
					if (Item.itemStackable[(client.playerItems[fromSlot] - 1)]
							|| Item.itemIsNote[(client.playerItems[fromSlot] - 1)]) {
						offerN[i] += amount;
						isInTrade = true;
						break;
					}
				}
			}
			if (!isInTrade) {
				for (int i = 0; i < offer.length; i++) {
					if (offer[i] <= 0) {
						offer[i] = client.playerItems[fromSlot];
						offerN[i] = amount;
						break;
					}
				}
			}
			if (client.playerItemsN[fromSlot] == amount) {
				client.playerItems[fromSlot] = 0;
			}
			client.playerItemsN[fromSlot] -= amount;
			resetMyItems(3415);
			Client other = getOther();
			if (other != null) {
				other.getTradeHandler().resetOtherItems(3416);
			}
			if (accepted || other.getTradeHandler().accepted()) {
				accepted = false;
				other.getTradeHandler().setAccepted(false);
				client.getPA().sendFrame126("", 3431);
				if (other != null) {
					other.getPA().sendFrame126("", 3431);
				}
			}
			client.getItems().resetItems(3322);
		}
	}

	public void fromTrade(int itemID, int fromSlot, int amount) {
		if (stage == 2)
			return;
		if (amount > 0 && (itemID + 1) == offer[fromSlot]) {
			if (amount > offerN[fromSlot]) {
				amount = offerN[fromSlot];
			}
			client.getItems().addItem(offer[fromSlot] - 1, amount);
			if (amount == offerN[fromSlot]) {
				offer[fromSlot] = 0;
			}
			offerN[fromSlot] -= amount;
			resetMyItems(3415);
			Client other = getOther();
			if (other != null) {
				other.getTradeHandler().resetOtherItems(3416);
			}
			if (accepted || other.getTradeHandler().accepted()) {
				accepted = false;
				other.getTradeHandler().setAccepted(false);
				client.getPA().sendFrame126("", 3431);
				if (other != null) {
					other.getPA().sendFrame126("", 3431);
				}
			}
			client.getItems().resetItems(3322);
		}
	}

	public TradeHandler(Client client) {
		this.client = client;
	}

	public void requestTrade(Client otherClient) {
		if (Server.UpdateServer) {
			client.sendMessage("You can't trade right now, the server is about to update.");
			return;
		}
		if (client.distanceToPoint(otherClient.getX(), otherClient.getY()) > 2) {
			client.sendMessage("Please move closer to the person you are trying to trade.");
			return;
		}
		if (otherClient.getTradeHandler().getCurrentTrade() != null) {
			if (otherClient.getTradeHandler().getCurrentTrade().isOpen()) {
				client.sendMessage(otherClient.playerName + " is busy right now.");
				return;
			}
		}

		//if (client.connectedFrom.equals(otherClient.connectedFrom)) {
			//client.sendMessage("You can't trade a person on your own network.");
			//return;
		//}

		if (currentTrade != null) {
			if (currentTrade.getEstablisher() == otherClient) {
				answerTrade(otherClient);
				return;
			} else {
				cancelCurrentTrade();
			}
		}
		currentTrade = new Trade(client, otherClient);
		otherClient.getTradeHandler().setCurrentTrade(currentTrade);
		otherClient.sendMessage(client.playerName + ":tradereq:");
		client.sendMessage("Sending trade request...");
	}

	public int stage = 0;
	public boolean accepted = false;

	public void answerTrade(Client otherClient) {
		if (Server.UpdateServer) {
			client.sendMessage("You can't trade when the server is about to update.");
			return;
		}
		if (!client.withinDistance(otherClient)) {
			return;
		}
		if (currentTrade == null) {
			requestTrade(otherClient);
		} else {
			if (currentTrade.isOpen()) {
				cancelCurrentTrade();
				requestTrade(otherClient);
			} else {
				currentTrade.setOpen(true);
				client.getPA().sendFrame248(3323, 3321);
				client.getItems().resetItems(3322);
				resetMyItems(3415);
				resetOtherItems(3416);
				client.getPA().sendFrame126("TradeHandler With: " + otherClient.playerName, 3417);
				client.getPA().sendFrame126("", 3431);
				otherClient.getPA().sendFrame248(3323, 3321);
				otherClient.getItems().resetItems(3322);
				otherClient.getTradeHandler().resetMyItems(3415);
				otherClient.getTradeHandler().resetOtherItems(3416);
				otherClient.getPA().sendFrame126("TradeHandler With: " + client.playerName, 3417);
				otherClient.getPA().sendFrame126("", 3431);
				accepted = false;
				PlayerSave.saveGame((Client) client);
				PlayerSave.saveGame((Client) otherClient);
				client.getTradeHandler().stage = 1;
				otherClient.getTradeHandler().stage = 1;

			}
		}
	}

	private int[] offer = new int[28];
	private int[] offerN = new int[28];

	public void resetMyItems(int frame) {
		client.outStream.createFrameVarSizeWord(53);
		client.outStream.writeWord(frame);
		client.outStream.writeWord(offer.length);
		for (int i = 0; i < offer.length; i++) {
			if (offerN[i] > 254) {
				client.outStream.writeByte(255);
				client.outStream.writeDWord_v2(offerN[i]);
			} else {
				client.outStream.writeByte(offerN[i]);
			}
			client.outStream.writeWordBigEndianA(offer[i]);
		}
		client.outStream.endFrameVarSizeWord();
	}

	public Client getOther() {
		if (currentTrade == null) {
			return null;
		}
		Client other = null;
		;
		if (currentTrade.getReceiver() != client) {
			other = currentTrade.getReceiver();
		}
		if (currentTrade.getEstablisher() != client) {
			other = currentTrade.getEstablisher();
		}
		return other;
	}

	public void resetOtherItems(int frame) {
		Client other = getOther();
		if (other == null)
			return;
		int[] offer = other.getTradeHandler().getOffer();
		int[] offerN = other.getTradeHandler().getOfferN();
		client.outStream.createFrameVarSizeWord(53);
		client.outStream.writeWord(frame);
		client.outStream.writeWord(offer.length);
		for (int i = 0; i < offer.length; i++) {
			if (offerN[i] > 254) {
				client.outStream.writeByte(255);
				client.outStream.writeDWord_v2(offerN[i]);
			} else {
				client.outStream.writeByte(offerN[i]);
			}
			client.outStream.writeWordBigEndianA(offer[i]);
		}
		client.outStream.endFrameVarSizeWord();
	}

	public int[] getOffer() {
		return offer;
	}

	public int[] getOfferN() {
		return offerN;
	}

	public void decline() {
		Client other = getOther();
		if (other != null) {
			other.sendMessage(client.playerName + " has declined the trade.");
			other.getTradeHandler().setStage(0);
		}
		client.sendMessage("You decline the trade.");
		client.getTradeHandler().setStage(0);
		cancelCurrentTrade();
	}

	public void cancelCurrentTrade() {
		if (currentTrade.isOpen()) {
			currentTrade.getReceiver().getPA().removeAllWindows();
			currentTrade.getEstablisher().getPA().removeAllWindows();
			currentTrade.getReceiver().getTradeHandler().transferOfferToInventory();
			currentTrade.getEstablisher().getTradeHandler().transferOfferToInventory();
		}
		if (currentTrade.getReceiver() != client) {
			currentTrade.getReceiver().getTradeHandler().setCurrentTrade(null);
			currentTrade.getReceiver().getTradeHandler().setAccepted(false);
		}
		if (currentTrade.getEstablisher() != client) {
			currentTrade.getEstablisher().getTradeHandler().setCurrentTrade(null);
			currentTrade.getEstablisher().getTradeHandler().setAccepted(false);
		}
		if (currentTrade.getReceiver().getTradeHandler().client != null) {
			currentTrade.getReceiver().getTradeHandler().setStage(0);
		}
		if (currentTrade.getEstablisher().getTradeHandler().client != null) {
			currentTrade.getEstablisher().getTradeHandler().setStage(0);
		}

		currentTrade = null;
		accepted = false;
	}

	public void transferOfferToInventory() {
		Client other = getOther();
		if (other == null) {
			return;
		}
		for (int i = 0; i < offer.length; i++) {
			if (offer[i] == 0)
				continue;

			client.getItems().addItem(offer[i] - 1, offerN[i]);
			offer[i] = 0;
			offerN[i] = 0;
		}
	}

	public Trade getCurrentTrade() {
		return currentTrade;
	}

	public void setCurrentTrade(Trade currentTrade) {
		this.currentTrade = currentTrade;
	}

	public int itemsTraded() {
		int itemsTraded = 0;
		for (int i = 0; i < offer.length; i++) {
			if (offer[i] != 0)
				itemsTraded++;
		}
		return itemsTraded;
	}

	public void acceptStage1() {
		if (stage != 1)
			return;
		if (currentTrade != null && currentTrade.isOpen()) {
			Client other = getOther();
			if (other == null)
				return;
			if (itemsTraded() > other.getItems().freeSlots()) {
				client.sendMessage("The other player doesn't have enough space left in their inventory.");
				return;
			}
			if (other.getTradeHandler().itemsTraded() > client.getItems().freeSlots()) {
				client.sendMessage("There is not enough space in your inventory.");
				return;
			}
			accepted = true;

			if (!other.getTradeHandler().accepted()) {
				client.getPA().sendFrame126("Waiting for other player...", 3431);
				other.getPA().sendFrame126("Other player accepted.", 3431);
			} else {
				client.getPA().sendFrame248(3443, 3213);
				client.getItems().resetItems(3214);
				other.getPA().sendFrame248(3443, 3213);
				other.getItems().resetItems(3214);
				accepted = false;
				client.getTradeHandler().setStage(2);
				other.getTradeHandler().setStage(2);
				other.getTradeHandler().setAccepted(false);
				client.getPA().sendFrame126("Are you sure you want to accept this trade?", 3535);
				other.getPA().sendFrame126("Are you sure you want to accept this trade?", 3535);
				sendOffer2();
				sendOtherOffer2();
				other.getTradeHandler().sendOffer2();
				other.getTradeHandler().sendOtherOffer2();
			}
		}
	}

	public void sendOffer2() {
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for (int i = 0; i < offer.length; i++) {
			String prefix = "";
			if (offer[i] > 0) {
				empty = false;
				if (offerN[i] >= 100 && offerN[i] < 1000000) {
					prefix = "@cya@" + (offerN[i] / 1000) + "K @whi@(" + offerN[i] + ")";
				} else if (offerN[i] >= 1000000) {
					prefix = "@gre@" + (offerN[i] / 1000000) + " million @whi@(" + offerN[i] + ")";
				} else {
					prefix = "" + offerN[i];
				}
					trade.append(client.getItems().getItemName(offer[i] - 1));
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		client.getPA().sendFrame126(trade.toString(), 3557);
	}

	public void sendOtherOffer2() {
		Client other = getOther();
		if (other == null)
			return;
		int[] offer = other.getTradeHandler().getOffer();
		int[] offerN = other.getTradeHandler().getOfferN();
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for (int i = 0; i < offer.length; i++) {
			String prefix = "";
			if (offer[i] > 0) {
				empty = false;
				if (offerN[i] >= 100 && offerN[i] < 1000000) {
					prefix = "@cya@" + (offerN[i] / 1000) + "K @whi@(" + offerN[i] + ")";
				} else if (offerN[i] >= 1000000) {
					prefix = "@gre@" + (offerN[i] / 1000000) + " million @whi@(" + offerN[i] + ")";
				} else {
					prefix = "" + offerN[i];
				}
					trade.append(client.getItems().getItemName((offer[i] - 1)));
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		client.getPA().sendFrame126(trade.toString(), 3558);
	}

	public void acceptStage2() {
		if (stage != 2)
			return;
		if (currentTrade != null && currentTrade.isOpen()) {
			Client other = getOther();
			if (other == null)
				return;

			if (!other.getTradeHandler().accepted()) {
				client.getPA().sendFrame126("Waiting for other player...", 3535);
				other.getPA().sendFrame126("Other player accepted.", 3535);
				accepted = true;
			} else {
				for (int i = 0; i < offer.length; i++) {
					if (offer[i] == 0)
						continue;
					other.getItems().addItem(offer[i] - 1, offerN[i]);
					offer[i] = 0;
					offerN[i] = 0;
				}
				int[] offer = other.getTradeHandler().getOffer();
				int[] offerN = other.getTradeHandler().getOfferN();
				for (int i = 0; i < offer.length; i++) {
					if (offer[i] == 0)
						continue;
					client.getItems().addItem(offer[i] - 1, offerN[i]);
					offer[i] = 0;
					offerN[i] = 0;
				}
				cancelCurrentTrade();
			}
		}
	}

	public boolean accepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public int getStage() {
		return this.stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
}
