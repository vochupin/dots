package net.slezok.dots;

public class Bridge{
	
	private final int DIRECTION_VERTICAL = 0;
	private final int DIRECTION_HORIZONTAL = 270;

	private int x;
	private int y;
	private int direction;
	private int width;
	private int height;
	
	
	
	public Bridge(int x, int y, int direction, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.width = width;
		this.height = height;
	}

	public Bridge() {
		x = y = direction = 0;
		width = 1; height = 5;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
