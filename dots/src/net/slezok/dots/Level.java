package net.slezok.dots;

public class Level{
	
	private String levelFile;
	private String description;
	private int[] directions;
	
	private boolean extentsCalculated = false;
	
	private int startX, startY, height, width;
	
	public Level(int startX, int startY) {
		super();
	}

	public Level() {
	}

	public int getStartX() {
		if(!extentsCalculated) calculateExtents();
		return startX;
	}

	public int getStartY() {
		if(!extentsCalculated) calculateExtents();
		return startY;
	}

	public int getHeight() {
		if(!extentsCalculated) calculateExtents();
		return height;
	}

	public int getWidth() {
		if(!extentsCalculated) calculateExtents();
		return width;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private void calculateExtents() {
		startX = 0; startY = 0; height = 50; width = 130;
		
	}
}
