package net.slezok.dots;

public class Bridge{
	private int x;
	private int y;
	
	public Bridge(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Bridge() {
		x = y = 0;
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
}
