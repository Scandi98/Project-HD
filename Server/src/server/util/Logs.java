package server.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import server.cache.RSItemDefinition;
import server.core.PlayerHandler;
import server.model.items.Item;
import server.model.players.Client;

public class Logs {

	private Client c;

	public Logs(Client Client) {
		this.c = Client;
	}

	/**
	 * Will write what kind of item a player has received. MONTH = 0 = January
	 * DAY OF MONTH = 30 || 31
	 */
	public void tradeReceived(String itemName, int itemAmount) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/trades/Trades Recieved/"
							+ c.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Received " + itemAmount + " " + itemName
						+ " From " + o.playerName);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void duelReceived(String itemName, int itemAmount) {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/duels/Duels Recieved/"
							+ c.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Received " + itemAmount + " " + itemName
						+ " From " + o.playerName);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Will write what kind of item a player has traded with another player.
	 * MONTH = 0 = January DAY OF MONTH = 30 || 31
	 */
	public void tradeGive(String itemName, int itemAmount) {
		Client o = (Client) PlayerHandler.players[c.tradeWith];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/trades/Trades Gave/"
							+ c.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Gave " + itemAmount + " " + itemName + " To "
						+ o.playerName);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void duelWin(String itemName, int itemAmount) {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/duels/spoils/"
							+ c.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Won " + itemAmount + " " + itemName + " From "
						+ o.playerName);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void depositPC(int id, int itemAmount) {
		checkDateAndTime();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/price/add/"
							+ c.playerName + ".txt", true));
			try {
				bItem.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
						+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: Deposited " + itemAmount + "x " + RSItemDefinition.valueOf(id).name);
				bItem.newLine();
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void takePC(int id, int itemAmount) {
		checkDateAndTime();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/price/remove/"
							+ c.playerName + ".txt", true));
			try {
				bItem.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
						+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: Took " + itemAmount + "x " + RSItemDefinition.valueOf(id).name);
				bItem.newLine();
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void duelLost(Client client, Client o, String itemName,
			int itemAmount) {
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/duels/lost/"
							+ client.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Lost " + itemAmount + " " + itemName + " to "
						+ o.playerName);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playerKills() {
		Client o = (Client) PlayerHandler.players[c.killerId];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/kills/Players Killed/"
							+ o.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("" + o.playerName + " killed " + c.playerName
						+ "in pvp.");
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void duelKills() {
		Client o = (Client) PlayerHandler.players[c.duelingWith];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(new FileWriter(
					System.getProperty("user.home")
							+ "/Dropbox/Server Logs/duels/Players Killed/"
							+ o.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : "
						+ C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("" + o.playerName + " killed " + c.playerName
						+ " in the duel arena.");
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeKillLog(Client opponent,String data, int ID, int itemX, int itemY) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/PK Drops/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: Died To "+opponent.playerName
					+ " For "
					+ "" + data + " " + " " + Item.getItemName(ID) + " ("
					+ itemX + "/" + itemY + ").");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
	
	public void writeLootBagRemove(String name, int amount) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Loot Bag/remove/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "Removed Item " + amount + "x " +name+" to looting bag.");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
	
	public void writeLootItem(String data, int ID, int itemX, int itemY) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Pickup/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "" + data + " " + " " + Item.getItemName(ID) + " ("
					+ itemX + "/" + itemY + ").");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			//ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
	
	public void writeLootBagItem(String name) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Loot Bag/add/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "Added Item "+name+" to looting bag.");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
	
	public void writeLootBagLog(String data, int ID, int itemX, int itemY) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Loot Bag/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "" + data + " " + " " + Item.getItemName(ID) + " ("
					+ itemX + "/" + itemY + ").");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}

	public void writeDropLog(String data, int ID, int itemX, int itemY) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Drop Center/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "" + data + " " + " " + Item.getItemName(ID) + " ("
					+ itemX + "/" + itemY + ").");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			//ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}

	public void writePMLog(String data) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Pm Center/" + c.playerName
				+ ".txt";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.connectedFrom + "]: "
					+ "" + data + " ");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}

	public void writeCommandLog(String command) {
		checkDateAndTime();
		String filePath = System.getProperty("user.home")
				+ "/Dropbox/Server Logs/logs/Command Center/Commands.txt";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.date + "]" + "-" + "[" + c.currentTime + " "
					+ checkTimeOfDay() + "]: " + "[" + c.playerName + "]: "
					+ "[" + c.connectedFrom + "] " + "::" + command);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public void checkDateAndTime() {
		Calendar cal = new GregorianCalendar();
		String day, month, hour, minute, second;

		int YEAR = cal.get(Calendar.YEAR);
		int MONTH = cal.get(Calendar.MONTH) + 1;
		int DAY = cal.get(Calendar.DAY_OF_MONTH);
		int HOUR = cal.get(Calendar.HOUR_OF_DAY);
		int MIN = cal.get(Calendar.MINUTE);
		int SECOND = cal.get(Calendar.SECOND);

		day = DAY < 10 ? "0" + DAY : "" + DAY;
		month = MONTH < 10 ? "0" + MONTH : "" + MONTH;
		hour = HOUR < 10 ? "0" + HOUR : "" + HOUR;
		minute = MIN < 10 ? "0" + MIN : "" + MIN;
		second = SECOND < 10 ? "0" + SECOND : "" + SECOND;

		c.date = day + "" + month + "" + YEAR;
		c.currentTime = hour + ":" + minute + ":" + second;
	}

	public String checkTimeOfDay() {
		Calendar cal = new GregorianCalendar();
		int TIME_OF_DAY = cal.get(Calendar.AM_PM);
		return TIME_OF_DAY > 0 ? "PM" : "AM";
	}
}