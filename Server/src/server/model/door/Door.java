package server.model.door;

import server.cache.RSObjectDefinition;
import server.clip.ObjectManager;
import server.model.Location;

/**
 * 
 * @author Twelve
 *
 */
public class Door {

	RSObjectDefinition object;
	Location location;
	public int originalFace;
	public int currentFace;
	public int type;
	public int orientation;
	public enum DoorPosition {
		OPEN, CLOSED
	}

	public Door(RSObjectDefinition object, int x, int y, int z) {
		this.object = object;
		this.location = Location.create(x, y, z);
		this.originalFace = object.orientation;
		this.currentFace = originalFace;
		this.type = object.type;
	}

	public DoorPosition getDoorPosition(RSObjectDefinition obj) {
		switch(getFirstAction(obj)) {
		case "Open":
			return DoorPosition.OPEN;
		case "Close":
			return DoorPosition.CLOSED;
		default:
			return null;
		}
	}

	public static final String getFirstAction(RSObjectDefinition obj) {
		if (obj.actions == null || obj.actions[0] == null) {
			return null;
		}
		return obj.actions[0];
	}

	public RSObjectDefinition getInverseObject() {
		RSObjectDefinition obj;

		DoorPosition pos = getDoorPosition(object);

		
		if (pos == DoorPosition.OPEN) {
			obj = RSObjectDefinition.valueOf(object.getId() + 1);
		} else {
			obj = RSObjectDefinition.valueOf(object.getId() - 1);
		}

		String action = getFirstAction(obj);

		if (action != null) {
			if (isOppositeAction(action, pos)) {
				return obj;
			}
		}
		return object;// return the object itself, unsure if this is correct.
	}



	public boolean isOppositeAction(String action, DoorPosition pos) {
		return !action.toLowerCase().equals(pos.toString().toLowerCase());
	}

	public Location getLocation() {
		return location;
	}

	public RSObjectDefinition getGameObject() {
		return object;
	}

}
