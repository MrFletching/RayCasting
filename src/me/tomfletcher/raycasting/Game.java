package me.tomfletcher.raycasting;

public class Game implements Runnable {
	
	private Thread thread;
	private boolean running = false;
	
	private World world;
	private MapWindow mapWindow;
	private GameWindow gameWindow;
	
	public Game() {
		world = new World();
		mapWindow = new MapWindow(world);
		gameWindow = new GameWindow(world);
		
		start();
	}
	
	private void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(running) {
			gameWindow.update();
			world.update();
			
			mapWindow.render();
			gameWindow.render();
			
			// TODO: Improve game loop
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
