package net.slezok.dots;

public class Bridge{
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_UP_RIGHT = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_DOWN_RIGHT = 3;
	public static final int DIRECTION_DOWN = 4;
	public static final int DIRECTION_DOWN_LEFT = 5;
	public static final int DIRECTION_LEFT = 6;
	public static final int DIRECTION_UP_LEFT = 7;

	//diagonal directions

	private int x;
	private int y;
	private int direction;
	private int width;
	private int length;
	
	public Bridge(int x, int y, int direction, int width, int length) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.width = width;
		this.length = length;
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

	public int getDirectionAngle() {
		switch(direction){
		case DIRECTION_UP:
			return 0;
		case DIRECTION_UP_LEFT:
			return 45;
		case DIRECTION_LEFT:
			return 90;
		case DIRECTION_DOWN_LEFT:
			return 135;
		case DIRECTION_DOWN:
			return 180;
		case DIRECTION_DOWN_RIGHT:
			return 225;
		case DIRECTION_RIGHT:
			return 270;
		case DIRECTION_UP_RIGHT:
			return 315;
		default:
			return 30; // When you will see this angle then you should debug map
		}
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
