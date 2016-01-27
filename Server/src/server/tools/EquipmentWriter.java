package server.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EquipmentWriter {

	private final static File file = new File("./Data/data/newFormat.dat");

	public static void main(String[] args) throws IOException {
		/*
		 * FileOutputStream out = new FileOutputStream(); for (int i = 0; i <
		 * newFormat.length; i++) { String s = newFormat[i][0] + ":" +
		 * newFormat[i][1]; byte[] content = s.getBytes(); out.write(content);
		 * System.out.println(content); } out.flush(); out.close();
		 */
		FileInputStream in = new FileInputStream(file);
		byte fileContent[] = new byte[(int) file.length()];
		in.read(fileContent);
		String s = new String(fileContent);
		System.out.println("File Content: " + s);
	}

	private static int[][] newFormat = { { 11802, 3 }, { 11804, 3 },
			{ 11806, 3 }, { 11808, 3 }, { 11824, 3 }, { 11826, 0 },
			{ 11828, 4 }, { 11830, 7 }, { 11832, 4 }, { 11834, 7 },
			{ 11836, 10 }, { 11838, 3 }, { 11840, 10 }, { 12002, 2 },
			{ 11791, 3 }, { 11907, 3 }, { 11770, 12 }, { 11771, 12 },
			{ 11772, 12 }, { 12899, 3 }, { 11773, 12 }, { 12904, 3 },
			{ 11864, 0 }, { 12954, 5 }, { 11791, 3 }, { 12002, 2 },
			{ 11926, 5 }, { 11924, 5 }, { 11850, 0 }, { 11852, 1 },
			{ 11854, 4 }, { 11856, 7 }, { 11858, 9 }, { 11860, 10 },
			{ 12205, 4 }, { 12207, 7 }, { 12209, 7 }, { 12211, 0 },
			{ 12213, 5 }, { 12215, 4 }, { 12217, 7 }, { 12219, 7 },
			{ 12221, 0 }, { 12223, 5 }, { 12225, 4 }, { 12227, 7 },
			{ 12229, 7 }, { 12231, 0 }, { 12233, 5 }, { 12235, 4 },
			{ 12237, 7 }, { 12239, 7 }, { 12241, 0 }, { 12243, 4 },
			{ 12445, 7 }, { 12447, 7 }, { 12449, 4 }, { 12451, 4 },
			{ 12453, 0 }, { 12455, 0 }, { 12253, 4 }, { 12255, 7 },
			{ 12257, 2 }, { 12259, 0 }, { 12261, 1 }, { 12263, 3 },
			{ 12265, 4 }, { 12267, 7 }, { 12269, 2 }, { 12271, 0 },
			{ 12273, 1 }, { 12275, 3 }, { 12193, 4 }, { 12195, 7 },
			{ 12197, 2 }, { 12199, 0 }, { 12201, 1 }, { 12203, 3 },
			{ 12277, 4 }, { 12279, 7 }, { 12281, 5 }, { 12283, 0 },
			{ 12285, 7 }, { 12287, 4 }, { 12289, 7 }, { 12291, 5 },
			{ 12293, 0 }, { 12295, 7 }, { 12598, 10 }, { 12315, 4 },
			{ 12317, 7 }, { 12339, 4 }, { 12341, 7 }, { 12343, 4 },
			{ 12345, 7 }, { 12347, 4 }, { 12349, 7 }, { 12335, 5 },
			{ 12432, 0 }, { 12441, 4 }, { 12443, 7 }, { 12351, 0 },
			{ 12337, 0 }, { 12355, 0 }, { 12391, 10 }, { 12389, 3 },
			{ 12353, 0 }, { 12540, 0 }, { 12381, 4 }, { 12383, 7 },
			{ 12385, 4 }, { 12387, 4 }, { 12430, 0 }, { 12357, 3 },
			{ 12514, 1 }, { 12363, 0 }, { 12365, 0 }, { 12367, 0 },
			{ 12369, 0 }, { 12371, 0 }, { 12518, 0 }, { 12520, 0 },
			{ 12522, 0 }, { 12524, 0 }, };

}
