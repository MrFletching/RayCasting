package me.tomfletcher.raycasting;

public class Map {
	
	private static final int WIDTH = 16;
	private static final int HEIGHT = 12;
	
	private static final int[][] walls = {
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
		{1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1},
		{1,0,0,1,1,0,0,1,0,0,0,0,0,0,0,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
		{1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
		{1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
	};
	
	public Map() {
		
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getWallAt(int x, int y) {
		if(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
			return 0;
		}
		
		return walls[y][x];
	}
}
