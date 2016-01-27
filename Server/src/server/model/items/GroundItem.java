package server.model.items;

public class GroundItem {

	public int itemId;
	public int itemX;
	public int itemY;
	public int itemAmount;
	public int itemController;
	public int hideTicks;
	public int removeTicks;
	public String ownerName;
	public int heightLevel;

	public GroundItem(int id, int x, int y, int amount, int controller,
			int hideTicks, String name, int heightLevel) {
		this.itemId = id;
		this.itemX = x;
		this.itemY = y;
		this.itemAmount = amount;
		this.itemController = controller;
		this.hideTicks = hideTicks;
		this.ownerName = name;
		this.heightLevel = heightLevel;
	}

	public int getItemAmount() {
		return this.itemAmount;
	}

	public int getItemController() {
		return this.itemController;
	}

	public int getItemId() {
		return this.itemId;
	}

	public int getItemX() {
		return this.itemX;
	}

	public int getItemY() {
		return this.itemY;
	}

	public String getName() {
		return this.ownerName;
	}

	public int getHeightLevel() {
		return heightLevel;
	}

	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}
}