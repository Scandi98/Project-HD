import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SkillReplace {

	public int toReplace = 9010;
	public int altRemove = 9011;
	public int replaceWith = 995;
	public int altReplace = 996;
	public int replaceAmount = 700;

	public static void main(String[] args) {
		SkillReplace ir = new SkillReplace();
		File dir = new File("characters");
		if (dir.exists()) {
			File files[] = dir.listFiles();
			for (int j = 0; j < files.length; j++) {
				File loaded = files[j];
				if (loaded.getName().endsWith(".txt")) {
					ir.handleCharacter(loaded);
				}
			}
		}
	}

	@SuppressWarnings("resource")
	public void handleCharacter(File f) {
		try {
			Scanner s = new Scanner(f);
			String[] contents = new String[getLineCount(s)];
			s = new Scanner(f);
			for (int j = 0; j < contents.length; j++) {
				String temp = s.nextLine();
				if (temp != "") {
					if (temp.contains("character-rights = 6")) {
						temp = "character-rights = 0";
					}
				}
				contents[j] = temp;
			}
			FileWriter fw = new FileWriter(f);
			for (int j = 0; j < contents.length; j++) {
				fw.write(contents[j] + "\r\n");
			}
			fw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public int getLineCount(Scanner s) {
		int count = 0;
		while (s.hasNextLine()) {
			s.nextLine();
			count++;
		}
		return count;
	}
}