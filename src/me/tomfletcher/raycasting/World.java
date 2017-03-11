package me.tomfletcher.raycasting;

public class World {
	
	private Map map;
	private Player player;
	
	public World() {
		map = new Map();
		player = new Player(this, 5.5, 6, 0);
	}
	
	public Map getMap() {
		return map;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void update() {
		player.update();
	}
	
	public boolean isSpaceAt(double x, double y) {
		return (map.getWallAt((int)x, (int)y) == 0);
	}
}
