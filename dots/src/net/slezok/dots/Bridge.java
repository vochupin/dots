package net.slezok.dots;

public class Bridge{
	
	private final int DIRECTION_VERTICAL = 0;
	private final int DIRECTION_HORIZONTAL = 90;

	private int x;
	private int y;
	
	private int direction;
	
	public Bridge(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public Bridge() {
		x = y = direction = 0;
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
}
