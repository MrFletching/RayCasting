package me.tomfletcher.raycasting;

public class Game implements Runnable {
	
	private static final int FPS = 60;
	private static final int FRAME_TIME_NS = 1000000000 / FPS;
	
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
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		long startTimeNS = System.nanoTime();
		long lastStatsTimeNS = startTimeNS;
		
		int frames = 0;
		
		while(running) {
			gameWindow.update();
			world.update();
			
			mapWindow.render();
			gameWindow.render();
			frames++;
			
			long endTimeNS = System.nanoTime();
			long diffTimeNS = endTimeNS - startTimeNS;
			
			long sleepTimeNS = FRAME_TIME_NS - diffTimeNS;
			
			if(sleepTimeNS > 0) {
				try {
					Thread.sleep(sleepTimeNS / 1000000, (int)(sleepTimeNS % 1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			startTimeNS = System.nanoTime();
			
			if(endTimeNS > lastStatsTimeNS + 1000000000) {
				System.out.println("FPS: "+frames);
				
				lastStatsTimeNS += 1000000000;
				frames = 0;
			}
		}
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
