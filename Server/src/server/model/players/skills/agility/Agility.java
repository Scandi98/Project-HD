package server.model.players.skills.agility;

import java.util.ArrayList;
import java.util.List;

import server.core.World;
import server.event.test.CycleEvent;
import server.event.test.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.tick.Tickable;

public class Agility {
	
	/**
	 * Represents an agility obstacle.
	 * @author Michael
	 *
	 */
	public enum Obstacle {
		
		WILDERNESS_DITCH(23271, 0, 0, 0, 1, 0, "wildernessDitch"),
		
		FALADOR_WESTERN_CRUMBLING_WALL(11844, 2935, 3355, 0, 5, 0.5, "faladorCrumblingWall"),
		
		/**
		 * Gnome obstacle course
		 */
		
		GNOME_COURSE_LOG_BALANCE(2295, 2474, 3435, 0, 1, 7, "gnomeLogBalance"),
		
		GNOME_COURSE_OBSTACLE_NET_1(2285, 2471, 3425, 0, 1, 8, "gnomeObstacleNet"),
		
		GNOME_COURSE_OBSTACLE_NET_2(2285, 2473, 3425, 0, 1, 8, "gnomeObstacleNet"),
		
		GNOME_COURSE_OBSTACLE_NET_3(2285, 2475, 3425, 0, 1, 8, "gnomeObstacleNet"),
		
		GNOME_COURSE_TREE_BRANCH(2313, 2473, 3422, 1, 1, 5, "gnomeTreeBranch"),
		
		GNOME_COURSE_BALANCE_ROPE(2312, 2478, 3420, 2, 1, 7, "gnomeBalanceRope"),
		
		GNOME_COURSE_TREE_BRANCH_2(2314, 2486, 3419, 2, 1, 5, "gnomeTreeBranch2"),
		
		GNOME_COURSE_OBSTACLE_NET_4(2286, 2483, 3426, 0, 1, 8, "gnomeObstacleNet2"),
		
		GNOME_COURSE_OBSTACLE_NET_5(2286, 2485, 3426, 0, 1, 8, "gnomeObstacleNet2"),
		
		GNOME_COURSE_OBSTACLE_NET_6(2286, 2487, 3426, 0, 1, 8, "gnomeObstacleNet2"),
		
		GNOME_COURSE_OBSTACLE_PIPE_1(154, 2484, 3431, 0, 1, 8, "gnomeObstaclePipe"),
		
		GNOME_COURSE_OBSTACLE_PIPE_2(154, 2487, 3431, 0, 1, 8, "gnomeObstaclePipe"),
	
		
		/**
		 * Barbarian agility course
		 */
		
		BARBARIAN_COURSE_OBSTACLE_PIPE(2287, 2552, 3559, 0, 35, 0, "barbarianObstaclePipe"),
		
		BARBARIAN_COURSE_ROPE_SWING(2282, 2551, 3550, 0, 35, 22, "barbarianRopeSwing"),
		
		BARBARIAN_COURSE_LOG_BALANCE(2294, 2550, 3546, 0, 35, 13.7, "barbarianLogBalance"),
		
		BARBARIAN_COURSE_OBSTACLE_NET(2284, 2538, 3545, 0, 35, 8.2, "barbarianObstacleNet"),
		
		BARBARIAN_COURSE_LEDGE(2302, 2535, 3547, 1, 35, 22, "barbarianLedge"),
		
		BARBARIAN_COURSE_CRUMBLING_WALL_1(1948, 2536, 3553, 0, 35, 13.7, "barbarianCrumblingWall1"),
		
		BARBARIAN_COURSE_CRUMBLING_WALL_2(1948, 2539, 3553, 0, 35, 13.7, "barbarianCrumblingWall2"),
		
		BARBARIAN_COURSE_CRUMBLING_WALL_3(1948, 2542, 3553, 0, 35, 13.7, "barbarianCrumblingWall3"),
		
		;
		
		/**
		 * The list of obstacles.
		 */
		private static List<Obstacle> obstacles = new ArrayList<Obstacle>();
		
		/**
		 * Populates the obstacle list
		 */
		static {
			for(Obstacle obstacle : Obstacle.values()) {
				obstacles.add(obstacle);
			}
		}
		
		public Obstacle forId(int id) {
			for(Obstacle obstacle : obstacles) {
				if(obstacle.getId() == id) {
					return obstacle;
				}
			}
			return null;
		}
		
		public static Obstacle forLocation(int x, int y, int z) {
			for(Obstacle obstacle : obstacles) {
				if(obstacle.getX() == x && obstacle.getY() == y && obstacle.getZ() == z) {
					return obstacle;
				}
			}
			return null;
		}
		
		/**
		 * Object id.
		 */
		private int id;
		
		/**
		 * The x coordinate of this obstacle.
		 */
		private int x;
		
		/**
		 * The y coordinate of this obstacle.
		 */
		private int y;
		
		/**
		 * The z coordinate of this obstacle.
		 */
		private int z;
		
		/**
		 * The level required to use this obstacle.
		 */
		private int levelRequired;
		
		/**
		 * The experience granted for tackling this obstacle.
		 */
		private double experience;
		
		/**
		 * The script that is executed for this obstacle.
		 */
		private String scriptString;

		private Obstacle(int id, int x, int y, int z, int levelRequired, double experience, String scriptString) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.z = z;
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.scriptString = scriptString;
		}

		public int getId() {
			return id;
		}

		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getZ() {
			return z;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public String getScriptString() {
			return scriptString;
		}
	}
	
	public static void tackleObstacle(Client player, int objectType) {
		/*if(player.playerLevel[player.playerAgility] < obstacle.getLevelRequired()) {
			player.getPA().removeAllWindows();
			player.getDH().sendStatement("You need an Agility level of " + obstacle.getLevelRequired() + " to tackle this obstacle.");
			player.startAnimation(-1);
			return;
		};*/
		switch (objectType) {
		case 2295:
			gnomeLogBalance(player);
			break;
		case 2285:
			gnomeObstacleNet(player);
			break;
		case 2313:
			gnomeTreeBranch(player);
			break;
		case 2312:
			gnomeBalanceRope(player);
			break;
		case 2314:
			gnomeTreeBranch2(player);
			break;
		case 2286:
			gnomeObstacleNet2(player);
			break;
		case 154:
			gnomeObstaclePipe(player);
			break;
		}
	}
	
	private static void gnomeLogBalance(Client c) {
		if (c.absX != 2474) {
			return;
		}
		Agility.setRunningToggled(c, false, 7);
		Agility.forceWalkingQueue(c, 762, 0, -7, 0, 8, true);
	}
	
	private static void gnomeObstacleNet(Client c) {
		c.faceUpdate(2285);
		Agility.forceTeleport(c, 828, c.getX(), 3424, 1, 0, 2);
	}
	
	private static void gnomeTreeBranch(Client c) {
		Agility.forceTeleport(c, 828, 2473, 3420, 2, 0, 2);
	}
	
	private static void gnomeBalanceRope(Client c) {
		if (c.absX != 2477 && c.absY != 3420 && c.heightLevel != 2) {
			return;
		}
		Agility.setRunningToggled(c, false, 7);
		Agility.forceWalkingQueue(c, 762, 6, 0, 0, 7, true);
	}
	
	private static void gnomeTreeBranch2(Client c) {
		Agility.forceTeleport(c, 828, 2485, 3419, 0, 0, 2);
	}
	
	private static void gnomeObstacleNet2(Client c) {
		if (c.absY != 3425) {
			return;
		}
		c.faceUpdate(2286);
		Agility.forceTeleport(c, 828, c.getX(), 3427, 0, 0, 2);
	}
	
	private static void gnomeObstaclePipe(Client c) {
		if (c.getX() != 2484 && c.getY() != 3430 && c.heightLevel != 0 || c.getX() != 2487 && c.getY() != 3430 && c.heightLevel != 0) {
			return;
		}
		Agility.setRunningToggled(c, false, 7);
		Agility.forceWalkingQueue(c, 844, 0, 7, 0, 7, true);
	}
	
	public static void forceTeleport(final Client player, final int animation, final int newX, final int newY, final int newZ, int ticksBeforeAnim, int ticks) {
		if(animation != -1) {
			if(ticksBeforeAnim < 1) {
				player.startAnimation(animation);
			} else {
				World.getWorld().submit(new Tickable(ticksBeforeAnim) {
					@Override
					public void execute() {
						player.startAnimation(animation);
						this.stop();
					}
				});			
			}
		}
		World.getWorld().submit(new Tickable(ticks) {
			@Override
			public void execute() {
				player.teleportToX = newX;
				player.teleportToY = newY;
				player.heightLevel = newZ;
				this.stop();
			}
		});
	}
	
	/*public static void forceMovement(final Client player, final int animation, final int[] forceMovement, int ticks, final boolean removeAttribute) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.startAnimation(animation);
				player.setForceWalk(forceMovement, removeAttribute);
				this.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				
			}
			
		}, ticks);
	}*/
	
	public static void forceWalkingQueue(final Client player, final int animation, final int x, final int y, int delayBeforeMovement, final int ticks, final boolean removeAttribute) {
		final int originalWalkAnimation = player.playerWalkIndex;
		final int originalRunAnimation = player.playerRunIndex;
		final int originalStandAnimation = player.playerStandIndex;
		final int originalStandTurn = player.playerTurnIndex;
		final int originalTurn90cw = player.playerTurn90CWIndex;
		final int originalTurn90ccw = player.playerTurn90CCWIndex;
		final int originalTurn180 = player.playerTurn90CCWIndex;
		Tickable tick = new Tickable(delayBeforeMovement) {

			@Override
			public void execute() {
				if (animation != -1) {
					player.setWalkAnimation(animation);
					player.setRunAnimation(animation);
					player.setStandAnimation(animation);
					player.setStandTurnAnimation(animation);
					player.setTurn90ClockwiseAnimation(animation);
					player.setTurn90CounterClockwiseAnimation(animation);
					player.setTurn180Animation(animation);
				}
				boolean playerIsRunning = player.isRunning;
				boolean playerIsRunning2 = player.isRunning2;
				player.isRunning = false;
				player.isRunning2 = false;
				
				player.resetWalkingQueue();
				player.getPA().requestUpdates();
				player.getPA().walkTo3(x, y);
				//player.addToWalkingQueue(x, y);
				//player.getPA().agilityWalk(x, y);
				World.getWorld().submit(new Tickable(ticks) {

					@Override
					public void execute() {
						player.setWalkAnimation(originalWalkAnimation);
						player.setRunAnimation(originalRunAnimation);
						player.setStandAnimation(originalStandAnimation);
						player.setTurn90ClockwiseAnimation(originalTurn90cw);
						player.setTurn90CounterClockwiseAnimation(originalTurn90ccw);
						player.setTurn180Animation(originalTurn180);
						player.setStandTurnAnimation(originalStandTurn);
						player.getPA().requestUpdates();
						player.isRunning = playerIsRunning;
						player.isRunning2 = playerIsRunning2;
						this.stop();
					}
					
				});
				this.stop();
			}
			
		};
		if(delayBeforeMovement < 1) {
			tick.execute();
		} else {
			World.getWorld().submit(tick);
		}
	}
	
	public static void setRunningToggled(final Client player, boolean toggled, int ticks) {
		final boolean originalToggledState = player.isRunning();
		player.isRunning = toggled;
		Tickable tick = new Tickable(ticks) {

			@Override
			public void execute() {
				player.isRunning = originalToggledState;
				this.stop();
			}
			
		};
		if(tick.getTickDelay() >= 1) {
			World.getWorld().submit(tick);
		} else {
			tick.execute();
		}
	}

}
