package net.slezok.dots;

public class Level{
	
	private int startX;
	private int startY;
	private String levelFile;
	private float[] x;
	private float[] y;
	private float[] direction;
	
	public Level(int startX, int startY) {
		super();
		this.startX = startX;
		this.startY = startY;
	}

	public Level() {
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public String getLevelFile() {
		return levelFile;
	}

	public void setLevelFile(String levelFile) {
		this.levelFile = levelFile;
	}

	public float[] getX() {
		return x;
	}

	public void setX(float[] x) {
		this.x = x;
	}

	public float[] getY() {
		return y;
	}

	public void setY(float[] y) {
		this.y = y;
	}

	public float[] getDirection() {
		return direction;
	}

	public void setDirection(float[] direction) {
		this.direction = direction;
	}
}
