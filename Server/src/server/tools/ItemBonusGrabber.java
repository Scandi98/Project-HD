package server.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import server.cache.RSItemDefinition;

public class ItemBonusGrabber {

	private static String LINK = "http://2007.runescape.wikia.com/wiki/";

	public static void main(String args[]) throws FileNotFoundException,
			IOException {
		//RSItemDefinition.singleParse();
		for (int i = 0; i < RSItemDefinition.totalItems; i++) {
			try {
				String name = RSItemDefinition.valueOf(i).getName()
						.toLowerCase().replace(" ", "_");
				URL url = new URL(LINK + "" + name);
				try {
					System.out.println("Connecting to " + url);
					URLConnection connection = url.openConnection();
					connection
							.setRequestProperty("User-Agent",
									"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(
										connection.getInputStream()));
						BufferedWriter writer = null;
						String nextLine = null;
						while ((nextLine = reader.readLine()) != null) {
							if (nextLine
									.contains("<td style=\"text-align: center; width: 35px;\">")
									|| nextLine
											.contains("<td style=\"text-align: center; width: 30px;\">")) {
								String[] lineRegex = nextLine.split("center;");
								String title = lineRegex[1]
										.replace("width: 35px;\">", "")
										.replace("width: 30px;\">", "")
										.replace("%", "");
								if (RSItemDefinition.valueOf(i).actions != null) {
									System.out.println(name
											+ " does not have a valid action.");
									continue;
								}
								writer = new BufferedWriter(new FileWriter(
										"./bonusfiles/" + i + ".txt", true));
								// writer.newLine();
								writer.write(title);
								writer.newLine();
								writer.close();
								System.out.println(title);
							}
						}
					} catch (FileNotFoundException file) {
						System.out.println("Page was not found.");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
