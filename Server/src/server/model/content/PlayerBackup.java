package server.model.content;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zeroturnaround.zip.ZipUtil;

import server.Config;

/*
 * Author -_Timmeh_-
 */

public class PlayerBackup {

	static Date date = new Date();
	static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	private List<String> fileList;
	public static final String OUTPUT_FOLDER = Config.LOAD_DIRECTORY + "Backups/Backup-" + dateFormat.format(date)
			+ ".zip";
	public static final String SRC_FOLDER = Config.LOAD_DIRECTORY + "characters/";

	public PlayerBackup() {
		fileList = new ArrayList<String>();
	}

	public void zipFile() {
		ZipUtil.pack(new File(SRC_FOLDER), new File(OUTPUT_FOLDER));
		/*byte[] buffer = new byte[1024];
		String source = "";
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			try {
				source = SRC_FOLDER.substring(SRC_FOLDER.lastIndexOf("\\") + 1,
						SRC_FOLDER.length());
			} catch (Exception e) {
				source = SRC_FOLDER;
			}
			fos = new FileOutputStream(OUTPUT);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + OUTPUT);
			FileInputStream in = null;

			for (String file : this.fileList) {
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SRC_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					//in.close();
				}
			}
			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));

		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(SRC_FOLDER.length() + 1, file.length());
	}

}
