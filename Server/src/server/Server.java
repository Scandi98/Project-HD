package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;

import org.apollo.fs.Cache;
import org.apollo.fs.IndexedFileSystem;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.rspserver.motivote.Motivote;

import server.cache.RSItemDefinition;
import server.cache.RSNPCDefinition;
import server.cache.RSObjectDefinition;
import server.clan.ClanManager;
import server.clip.ObjectDef;
import server.core.GameEngine;
import server.core.PlayerHandler;
import server.core.World;
import server.core.task.Task;
import server.event.CycleEventHandler;
import server.model.minigames.CastleWars;
import server.model.minigames.ClanWars;
import server.model.minigames.FightCaves;
import server.model.minigames.FightPits;
import server.model.minigames.KBDWars;
import server.model.minigames.PestControl;
import server.model.npcs.NPCHandler;
import server.model.npcs.NPCLootTable;
import server.net.PipelineFactory;
import server.region.Region;
import server.rspserver.motivote.RewardHandler;
import server.tick.TaskHandler;
import server.tick.Tickable;
import server.tick.TickableManager;
import server.world.GameObjectManager;
import server.world.GlobalObjects;
import server.world.ItemHandler;
import server.world.ObjectManager;
import server.world.ShopHandler;

/**
 * Server.java
 * 
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30
 * 
 */
public class Server {

	public static boolean UpdateServer = false, shutdownClientHandler;

	private static int serverlistenerPort = 43594;

	public static ItemHandler itemHandler;
	public static ClanManager clanManager = new ClanManager();
	public static PlayerHandler playerHandler = new PlayerHandler();
	public static NPCHandler npcHandler;
	public static ShopHandler shopHandler = new ShopHandler();
	public static GameObjectManager gameObjectManager = GameObjectManager.getInstance();
	public static ObjectManager objectManager = new ObjectManager();
	public static FightPits fightPits = new FightPits();
	public static ArrayList<String> antiAttack = new ArrayList<String>();
	public static FightCaves fightCaves = new FightCaves();
	public static TickableManager tickManager = new TickableManager();
	//public static MainLoader vote = new MainLoader("YOUR HOST",
			//"DB_USER", "DB_PASS", "DB_NAME");

	public static CycleEventHandler cycleeventmanager = new CycleEventHandler();

	public static boolean isAttacker(String host) {
		for (String a : antiAttack) {
			if (a.equals(host)) {
				return true;
			}
		}
		return false;
	}
	public static void addAttacker(String attacker) {
		antiAttack.add(attacker);
	}

	/**
	 * ClanChat Added by Valiant
	 */
	public static CastleWars castleWars = new CastleWars();

	public static KBDWars kbdWars = new KBDWars();


	public static PestControl pestControl = new PestControl();

	public static ClanWars clanWars = new ClanWars();

	public static CycleEventHandler getCycleEventManager() {
		return cycleeventmanager;
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			if (Boolean.parseBoolean(args[0]))
				Config.LOAD_DIRECTORY = "C:/Data/";
		}
		
		new Server().run();
	}


	boolean loaded = false;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		try {
			
			Cache.fileSystem = new IndexedFileSystem(new File("cache"), true);
			RSNPCDefinition.fullParse();
			RSItemDefinition.parse();
			RSObjectDefinition.parse();
			ObjectDef.loadConfig();
			Region.load();
			NPCLootTable.load();
			loadIronDonators();
			itemHandler = new ItemHandler();
			npcHandler = new NPCHandler(false);
			World.getWorld();
			new Motivote(new RewardHandler(), "http://vote.dragon-ages.org/", "95214352").start();
			try {
				new RS2Server().start();
			} catch (Exception ex) {
				System.out.println("Error starting the server...");
				ex.printStackTrace();
				System.exit(1);
			}
			while (true) {
				long start = System.nanoTime() / 1000000L;
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Iterator<Tickable> tickIt$ = tickManager.getTickables().iterator();
						while (tickIt$.hasNext()) {
							Tickable t = tickIt$.next();
							t.cycle();
							if (!t.isRunning())
								tickIt$.remove();
						}
						Server.playerHandler.process();
						Server.npcHandler.process();
						Server.pestControl.process();
						Server.shopHandler.process();
						CycleEventHandler.getSingleton().process();
						Server.gameObjectManager.process();
						Server.objectManager.process();
						Server.itemHandler.process();
						try {
							TaskHandler.sequence();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				long sleepTime = 600 - (System.nanoTime() / 1000000L - start);
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				} else {
					// The server has reached maximum load, players may now lag.
					System.out
					.println("[WARNING]: Server load: "
							+ (100 + (Math.abs(sleepTime) / (Config.CYCLE_TIME / 100)))
							+ "%!");
				}
				try {
					//hitbox.update();
				} catch (Throwable t) {
					t.printStackTrace();
				}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setupLoginChannels() {
		/**
		 * Accepting Connections
		 */
		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(
				new HashedWheelTimer()));
		
		while (true) {
			try {
				serverBootstrap.bind(new InetSocketAddress(serverlistenerPort));
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
				continue;
			}
			break;
		}
	}

	public static boolean playerInWorld(String name) {
		File file;
		file = new File("C:/world/" + name + ".world");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
			}
			return false;
		}
		return true;
	}

	public static void resetWorlds() {
		if (!checkStatus(43594)) {
			File loc = new File("C:/world/");
			if (loc.exists()) {
				try {
					File files[] = loc.listFiles();
					for (int i = 0; i < files.length; i++) {
						File load = files[i];
						if (load.getName().endsWith(".world")) {
							load.delete();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!checkStatus(43596)) {
			File loc = new File("C:/world/");
			if (loc.exists()) {
				try {
					File files[] = loc.listFiles();
					for (int i = 0; i < files.length; i++) {
						File load = files[i];
						if (load.getName().endsWith(".world")) {
							load.delete();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void deleteFromWorld(String name) {

		File file = new File("C:/world/" + name + ".world");
		if (!file.exists())
			return;

		boolean delete = file.delete();

		if (!delete)
			System.out.println("Failed to delete .world file: " + name);
	}

	public static boolean checkStatus(int world) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(world);
		} catch (IOException e) {
			return true;
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	private static Server server;
	public static Server getServer() {
		return server;
	}
	private static GlobalObjects globalObjects = new GlobalObjects();
	public static GlobalObjects getGlobalObjects() {
		return globalObjects;
	}
	public static ArrayList<String> ironDonatorMap = new ArrayList<String>();

	
	public static void loadIronDonators() {
		try {

			BufferedReader reader = new BufferedReader(new FileReader(new File("./Data/extras/irondonator.txt")));

			String name;
			while ((name = reader.readLine()) != null) {
				ironDonatorMap.add(name);
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
