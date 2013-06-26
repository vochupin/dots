package net.slezok.dots;

public class Level{
	
	private int startX;
	private int startY;
	private String levelFile;
	private int[] directions;
	
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

	public int[] getDirections() {
		return directions;
	}

	public void setDirections(int[] direction) {
		this.directions = direction;
	}
}
