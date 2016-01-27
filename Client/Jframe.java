import java.io.File;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Jframe extends Client implements ActionListener {

	private static JMenuItem menuItem;
	private JFrame frame;

	public Jframe(String args[]) {
		super();
		try {
			signlink.startpriv(InetAddress.getByName(server));
			initUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void initUI() {
		try {
			/*	UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());*/
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			frame = new JFrame(clientName);
			frame.setLayout(new BorderLayout());
			setFocusTraversalKeysEnabled(false);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel gamePanel = new JPanel();

			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));

			JButton screenshot = new JButton("Screenshot");
			screenshot.setActionCommand("Screenshot");
			screenshot.addActionListener(this);

			JMenu fileMenu = new JMenu("File");
			JMenu streamMenu = new JMenu("Streams");
			JMenu featureMenu = new JMenu("Features");

			String[] mainButtons = new String[] { "Website", "Vote", "-",
			"Exit" };

			for (String name : mainButtons) {
				JMenuItem menuItem = new JMenuItem(name);
				if (name.equalsIgnoreCase("-")) {
					fileMenu.addSeparator();
				} else {
					menuItem.addActionListener(this);
					fileMenu.add(menuItem);
				}
			}
			String[] Streams = new String[] { "Request Your Stream!", "Goku's Stream", "Rich Gecko's Stream", "Tez Stream", "-",
			"Exit" };

			for (String name : Streams) {
				JMenuItem menuItem = new JMenuItem(name);
				if (name.equalsIgnoreCase("-")) {
					streamMenu.addSeparator();
				} else {
					menuItem.addActionListener(this);
					streamMenu.add(menuItem);
				}
			}

			String[] features = new String[] { "Interpolation", "Toggle rooftops" };

			for (String name : features) {
				JCheckBox menuItem = new JCheckBox(name);
				if (name.equalsIgnoreCase("-")) {
					featureMenu.addSeparator();
				} else {
					menuItem.addActionListener(this);
					featureMenu.add(menuItem);
				}
			}

			JMenuBar menuBar = new JMenuBar();
			JMenuBar jmenubar = new JMenuBar();

			frame.add(jmenubar);
			menuBar.add(fileMenu);
			menuBar.add(streamMenu);
			menuBar.add(featureMenu);
			menuBar.add(screenshot);
			frame.getContentPane().add(menuBar, BorderLayout.NORTH);
			frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
			frame.pack();

			frame.setVisible(true); // can see the client
			frame.setResizable(false); // resizeable frame
			
			frame.setLocationRelativeTo(null);
			
			init();
			requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public URL getCodeBase() {
		try {
			return new URL("http://" + server + "/cache");
		} catch (Exception e) {
			return super.getCodeBase();
		}
	}

	public URL getDocumentBase() {
		return getCodeBase();
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	public String getParameter(String key) {
		return "";
	}

	private static void openUpWebSite(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(url));
		} catch (Exception e) {
		}
	}

	private int screenshot;

	private boolean takeScreenshot = true;

	public void screeny() {
		try {
			Window window = KeyboardFocusManager
					.getCurrentKeyboardFocusManager().getFocusedWindow();
			Point point = window.getLocationOnScreen();
			int x = (int) point.getX();
			int y = (int) point.getY();
			int w = window.getWidth();
			int h = window.getHeight();
			Robot robot = new Robot(window.getGraphicsConfiguration()
					.getDevice());
			Rectangle captureSize = new Rectangle(x, y, w, h);
			java.awt.image.BufferedImage bufferedimage = robot
					.createScreenCapture(captureSize);
			String fileExtension = "Dragon - Age";
			File dir = new File(signlink.findcachedir() + "/Screenshots/");
			if (!dir.exists()) {
				System.out.println("Directory Created at "
						+ signlink.findcachedir() + "/Screenshots/");
				dir.mkdir();
			}
			for (int i = 1; i <= 1000; i++) {
				File file = new File(signlink.findcachedir() + "/Screenshots/"
						+ fileExtension + " " + i + ".png");
				if (!file.exists()) {
					screenshot = i;
					takeScreenshot = true;
					break;
				}
			}
			File file = new File((new StringBuilder())
					.append(signlink.findcachedir() + "/Screenshots/"
							+ fileExtension + " ").append(screenshot)
							.append(".png").toString());
			if (takeScreenshot == true) {
				pushMessage("@red@" + fileExtension + " " + screenshot
						+ " was saved in your screenshot folder!", 0, "");
				ImageIO.write(bufferedimage, "png", file);
			} else {
				pushMessage("@gre@Your screenshots folder is full!", 0, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		try {
			if (cmd != null) {
				if (cmd.equalsIgnoreCase("exit")) {
					System.exit(0);
				}
				if (cmd.equalsIgnoreCase("Website")) {
					openUpWebSite("http://dragon-ages.org/");
				}
				if (cmd.equalsIgnoreCase("Vote")) {
					openUpWebSite("http://dragon-ages.org/vote");
				}
				if (cmd.equalsIgnoreCase("Request Your Stream!")) {
					openUpWebSite("http://dragon-ages.org/forums/index.php?/topic/407-request-your-stream/");
				}
				if (cmd.equalsIgnoreCase("Goku's Stream")) {
					openUpWebSite("http://hitbox.tv/gokugaming");
				}
				if (cmd.equalsIgnoreCase("Rich Gecko's Stream")) {
					openUpWebSite("http://hitbox.tv/richgeckoo");
				}
				if (cmd.equalsIgnoreCase("Tez Stream")) {
					openUpWebSite("http://hitbox.tv/taser");
				}
				if (cmd.equalsIgnoreCase("Interpolation")) {
					Client.interpolateAnimations = !Client.interpolateAnimations;
					requestFocus();
				}
				if (cmd.equalsIgnoreCase("Toggle rooftops")) {
					roofsOff = !roofsOff;
					requestFocus();
				}
				if (cmd.equalsIgnoreCase("Screenshot")) {
					screeny();
				}
			}
		} catch (Exception e) {
		}
	}
}