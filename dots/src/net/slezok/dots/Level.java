package net.slezok.dots;

public class Level{
	private static int MARGIN = 2;

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
		int currX = 0, currY = 0;
		int maxX = 0, maxY = 0;
		int minX = 0, minY = 0;
		for(int direction : directions){
			switch(direction){
			case Bridge.DIRECTION_UP:
				currY++;
				break;
			case Bridge.DIRECTION_DOWN:
				currY--;
				break;
			case Bridge.DIRECTION_LEFT:
				currX--;
				break;
			case Bridge.DIRECTION_RIGHT:
				currX++;
				break;
			case Bridge.DIRECTION_UP_RIGHT:
				currX++; currY++;
				break;
			case Bridge.DIRECTION_UP_LEFT:
				currX--; currY++;
				break;
			case Bridge.DIRECTION_DOWN_RIGHT:
				currX++; currY--;
				break;
			case Bridge.DIRECTION_DOWN_LEFT:
				currX--; currY--;
				break;

			default: throw new IllegalArgumentException("Unknown move type: " + direction);
			}
			
			if(maxX < currX) maxX = currX;
			if(maxY < currY) maxY = currY;
			if(minX > currX) minX = currX;
			if(minY > currY) minY = currY;
		}
		maxX += MARGIN; maxY += MARGIN;
		minX -= MARGIN; minY -= MARGIN;
		
		width = maxX - minX;
		height = maxY - minY;

		startX = -minX; startY = -minY; 
	}
}
