package net.slezok.dots;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class Level{
	private static final String TAG = "Level";
	
	private static final int RLE_CODE = -1;

	private static int MARGIN = 2;

	private String id;
	
	private String name;

	private int[] directions;

	private boolean extentsCalculated = false;

	private int startX, startY, height, width;

	private int maxIdenticalSteps;

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

	public int[] getDirections() {
		return directions;
	}

	public void setDirections(int[] direction) {
		this.directions = direction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void calculateExtents() {
		int currX = 0, currY = 0;
		int maxX = 0, maxY = 0;
		int minX = 0, minY = 0;
		
		int oldDirection = -1, identicalSteps = 0;
		
		for(int direction : directions){
			
			if(oldDirection == direction){
				identicalSteps++;
			}else{
				oldDirection = direction;
				identicalSteps++;
				if(maxIdenticalSteps < identicalSteps) maxIdenticalSteps = identicalSteps;
				identicalSteps = 0;
			}
			
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

	public int getMaximumIdenticalSteps() {
		if(!extentsCalculated) calculateExtents();
		Gdx.app.log(TAG, "Max identical steps: " + maxIdenticalSteps);
		return maxIdenticalSteps;
	}

	public void unpackDirections() {
		List<Integer> unpackedDirections = new ArrayList<Integer>();
		boolean wasPacked = false;
		for(int i = 0; i < directions.length; i++){
			int dir = directions[i];
			if(dir != RLE_CODE){
				unpackedDirections.add(dir);
			}else{
				wasPacked = true;
				
				int numberOfSteps = directions[++i];
				dir = directions[++i];
				for(int j = 0; j < numberOfSteps; j++) unpackedDirections.add(dir); 
			}
		}
		
		if(wasPacked){
			directions = new int[unpackedDirections.size()];
			for(int i = 0; i < directions.length; i++) {
				directions[i] = unpackedDirections.get(i);
			}
		}
	}
}
