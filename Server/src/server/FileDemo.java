package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.util.Misc;

public class FileDemo {
	public static void write(String playerName, String email) throws IOException {
		BufferedReader bfr;
		String line;		
		File file = new File("./donations.txt");
		bfr = new BufferedReader(new FileReader(file));
		if (!file.exists()) {
			file.createNewFile();
		}
		try {
			bfr = new BufferedReader(new FileReader(file));

			while ((line = bfr.readLine()) != null) {
			}
			FileWriter fw = new FileWriter(file, true);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			fw.append("["+dateFormat.format(date)+"]"+"["+Misc.formatPlayerName(playerName)+"] used command with email ["+email+"]");
			fw.append("\n");

			while ((line = bfr.readLine()) != null) {
			}
			bfr.close();
			fw.close();

		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		}

	}
}
