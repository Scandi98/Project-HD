package server.model.door;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import server.cache.RSObjectDefinition;
import server.core.World;
import server.model.Location;
import server.model.door.Door.DoorPosition;
import server.model.players.Client;
import server.tick.Tickable;

public class DoorManager {

	public static final List<Door> DOORS = new ArrayList<>();

	public static Door wildDoor;
	public static Door snakeDoor;

	public static final void handle(Client client, Door door) {// when we make
																// it so only
																// one player
																// can go
																// through,
																// since the
																// door is
																// static we can
																// set a player
																// to it or
																// something, u
																// know? yeah

		if (door.getDoorPosition(door.getGameObject()) == null) {
			return;
		}

		DOORS.add(door);
		Location loc = door.getLocation();

		//System.out.println(door.getGameObject().getId());// we can just hardcode
															// it if you want
															// lol,, idc
		RSObjectDefinition inverse = door.getInverseObject();

		// this should rotate it, maybe not in proper direction tho yet
		// System.out.println(door.originalFace+"");
		// client.getPA().object(inverse.getId(), loc.getX(), loc.getY(),
		// getNextFace(door, false), 0);

		int xOff = 0;
		int yOff = 0;
		if (door == snakeDoor && client.absY == 3957 || client.absY == 3958) {
			if (door.orientation == 1) {
				door.orientation = 2;
				yOff = 1;
				client.getPA().removeDoor(door, 0, 0);
				client.canWalk = false;
				client.canClick = false;
				int yWalk = client.absY <= 3957 ? 1 : -1;
				client.getPA().requestUpdates();
				client.getPA().walkTo3(0, yWalk); // try lol
				client.submitTick(new Tickable(2) {

					@Override
					public void execute() {
						if (getTickDelay() == 1) {
							client.canWalk = true;
							client.canClick = true;
							stop();
							return;
						}
						DoorManager.handle(client, door);
						setTickDelay(1);
					}

				});
			} else {
				door.orientation = 1;
				client.getPA().removeDoor(door, 0, 1);
			}
			/*client.getPA().object(door.object.id, loc.getX() + xOff,
					loc.getY() + yOff, door.orientation, 0);*/
		}
		/*if (door == wildDoor) {
			if (door.orientation == 1) {
				door.orientation = 2;
				yOff = 1;
				client.getPA().removeDoor(door, 0, 0);
				client.canWalk = false;
				client.canClick = false;
				int yWalk = client.absY == 3944 ? 1 : -1;
				client.getPA().requestUpdates();
				client.getPA().walkTo3(0, yWalk); // try lol
				client.submitTick(new Tickable(2) {

					@Override
					public void execute() {
						System.out.println("Tick Delay: " + getTickDelay());
						if (getTickDelay() == 1) {
							client.canWalk = true;
							client.canClick = true;
							stop();
							return;
						}
						DoorManager.handle(client, door);
						setTickDelay(1);
					}

				});
			} else {
				door.orientation = 1;
				client.getPA().removeDoor(door, 0, 1);
			}
		}*/
		// try
		client.getPA().object(door.object.id, loc.getX() + xOff,
				loc.getY() + yOff, door.orientation, 0);
	}

	public static Optional<Door> getDoor(int id, Location loc) {
		return DOORS.stream().parallel()
				.filter(d -> d.object.id == id && loc.equals(d.location))
				.findFirst();
	}

	/**
	 * Gets the next door facing direction
	 *
	 * @param door
	 *            The door
	 * @return The facing direction
	 */
	private static int getNextFace(Door door, boolean right) {
		int face = door.originalFace;
		if (door.type == 0) {
			if (door.getDoorPosition(door.object) == DoorPosition.CLOSED) {
				if (door.originalFace == 0 && door.currentFace == 0) {
					face = 1;
				} else if (door.originalFace == 1 && door.currentFace == 1) {
					face = 2;
				} else if (door.originalFace == 2 && door.currentFace == 2) {
					face = 3;
				} else if (door.originalFace == 3 && door.currentFace == 3) {
					face = 0;
				} else if (door.originalFace != door.currentFace) {
					face = door.originalFace;
				}
			} else if (door.getDoorPosition(door.object) == DoorPosition.OPEN) {
				if (door.originalFace == 0 && door.currentFace == 0) {
					face = 3;
				} else if (door.originalFace == 1 && door.currentFace == 1) {
					face = 0;
				} else if (door.originalFace == 2 && door.currentFace == 2) {
					face = 1;
				} else if (door.originalFace == 3 && door.currentFace == 3) {
					face = 2;
				} else if (door.originalFace != door.currentFace) {
					face = door.originalFace;
				}
			}
		} else if (door.type == 9) {
			if (door.getDoorPosition(door.object) == DoorPosition.CLOSED) {
				if (door.originalFace == 0 && door.currentFace == 0) {
					face = 3;
				} else if (door.originalFace == 1 && door.currentFace == 1) {
					face = 2;
				} else if (door.originalFace == 2 && door.currentFace == 2) {
					face = 1;
				} else if (door.originalFace == 3 && door.currentFace == 3) {
					face = 0;
				} else if (door.originalFace != door.currentFace) {
					face = door.originalFace;
				}
			} else if (door.getDoorPosition(door.object) == DoorPosition.OPEN) {
				if (door.originalFace == 0 && door.currentFace == 0) {
					face = 3;
				} else if (door.originalFace == 1 && door.currentFace == 1) {
					face = 0;
				} else if (door.originalFace == 2 && door.currentFace == 2) {
					face = 1;
				} else if (door.originalFace == 3 && door.currentFace == 3) {
					face = 2;
				} else if (door.originalFace != door.currentFace) {
					face = door.originalFace;
				}
			}
		}
		door.currentFace = face;
		return face;
	}

	static {
		RSObjectDefinition obj = RSObjectDefinition.valueOf(26760);
		wildDoor = new Door(obj, 3184, 3944, 0);
		wildDoor.orientation = 1;// closed by default
		snakeDoor = new Door(RSObjectDefinition.valueOf(11726), 3190, 3957, 0);
		snakeDoor.orientation = 1;
	}
}
