package server.model.objects;

import server.model.Location;

public class GameObject {

	public int objectId;
	public Location location;
	public int objectFace;
	public int objectType;
	public int objectTicks;
	public int decayTicks;
	
	public GameObject(int id, Location loc, int face, int type) {
		this.objectId = id;
		this.location = loc;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = 1;
	}
	
	public GameObject(int id, int x, int y, int height, int face, int type) {
		this.objectId = id;
		this.location = Location.create(x, y, height);
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = 1;
	}

	public GameObject(int id, int x, int y, int height, int face, int type, int ticks) {
		this.objectId = id;
		this.location = Location.create(x, y, height);
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
	}

	public int getId() {
		return this.objectId;
	}

	public int getX() {
		return location.getX();
	}

	public int getY() {
		return location.getY();
	}

	public int getZ() {
		return location.getZ();
	}

	public int getOrientation() {
		return this.objectFace;
	}

	public int getType() {
		return this.objectType;
	}

	public Location getLocation() {
		return location;
	}

}