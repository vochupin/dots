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

	private float x;
	private float y;
	private int direction;
	private float width;
	private float length;
	
	public Bridge(float x, float y, int direction, float width, float length) {
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

	public int getDirection() {
		return direction;
	}

	public float getDirectionAngle() {
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float height) {
		this.length = height;
	}
}
