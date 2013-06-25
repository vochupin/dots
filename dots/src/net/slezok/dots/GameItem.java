package net.slezok.dots;

public class GameItem{
	String itemType;
	float x;
	float y;
	float width;
	float height;
	
	public GameItem(String itemType, float x, float y, float width, float height) {
		this.itemType = itemType;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GameItem() {
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
