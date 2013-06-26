package net.slezok.dots;

public class Bridge{
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 180;
	public static final int DIRECTION_LEFT = 90;
	public static final int DIRECTION_RIGHT = 270;

	private int x;
	private int y;
	private int direction;
	private int width;
	private int length;
	
	
	
	public Bridge(int x, int y, int direction, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.width = width;
		this.length = height;
	}

	public Bridge() {
		x = y = direction = 0;
		width = 1; length = 5;
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

	public int getLength() {
		return length;
	}

	public void setLength(int height) {
		this.length = height;
	}
}
