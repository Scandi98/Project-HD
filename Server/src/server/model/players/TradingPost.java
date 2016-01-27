package server.model.players;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import server.Config;
/*
 * Author Tim<imfine>
 */

public class TradingPost {

	/*
	 * Client instace
	 */
	public Client c;

	public static TradingPost post;

	public TradingPost(Client c) {
		this.c = c;
	}

	/*
	 * TradingPost constructor
	 */
	public TradingPost(Client c, int itemId) throws IOException {
		this.c = c;
		BufferedReader bfr;
		String line;
		File file = new File(Config.LOAD_DIRECTORY + "post.txt");
		bfr = new BufferedReader(new FileReader(file));
		if (!file.exists()) {
			file.createNewFile();
		}
		try {
			if (c.getItems().playerHasItem(itemId)) {
				while ((line = bfr.readLine()) != null) {
					System.out.println(line);
				}
				FileWriter fw = new FileWriter(file, true);
				fw.append(c.playerName + " is selling "
						+ c.getItems().getItemAmount(itemId) + "x "
						+ c.getItems().getItemName(itemId));
				fw.append("\n");

				while ((line = bfr.readLine()) != null) {
					System.out.println(line);
				}
				bfr.close();
				fw.close();
			}

		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		}
	}

	public void readPost() throws IOException {
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(
				Config.LOAD_DIRECTORY + "post.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			c.sendMessage(line);
			c.getPA().sendNewString(line, 8146 + count);
			System.out.println(line);
			count++;
		}
		c.getPA().sendNewString("OxidePkz Trading Post", 8145);
		c.getPA().showInterface(8134);
		br.close();
	}

	public void removePost() throws IOException {
		try {
			String file = Config.LOAD_DIRECTORY + "post.txt";
			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}
			System.out.println("here?");
			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.trim().equals(c.playerName)) {
					System.out.println(line);
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
