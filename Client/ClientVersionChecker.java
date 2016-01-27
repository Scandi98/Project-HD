import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class ClientVersionChecker {

	public static final String LOCATION = "http://client.dragon-ages.org";

	public static double remote;
	
	public static boolean update() {
		try {
			File file = new File(signlink.findcachedir()+"/Version.txt");//System.getProperty("user.home")+"/LostIsleVersion.txt");


			double remote = getRemoteVersion();

			ClientVersionChecker.remote = remote;
			if (!file.exists()) {
				writeVersion();
				return true;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			double local = Double.parseDouble(reader.readLine());

			reader.close();
			return local != remote;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void writeVersion() {
		try {
			File file = new File(signlink.findcachedir()+"/Version.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			try {
				writer.write(""+remote);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void download() {
		try {
			URL url = new URL(LOCATION + "/Dragon.jar");
			OutputStream out = new BufferedOutputStream(new FileOutputStream("./Dragon-"+remote+".jar")); 

			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream(); 

			byte[] tmp = new byte[in.available()]; 

			int read;

			while((read = in.read(tmp)) != -1) {
				out.write(tmp, 0, read);
			}

			out.close();

			writeVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static double getRemoteVersion() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(LOCATION+"/version.txt").openStream()));
		return Double.parseDouble(reader.readLine());
	}

}
