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
		int totalUpdateTime = 0;
		int totalRenderTime = 0;
		
		while(running) {
			// UPDATE
			long startUpdateTime = System.currentTimeMillis();
			
			gameWindow.update();
			world.update();
			
			totalUpdateTime += System.currentTimeMillis() - startUpdateTime;
			
			// RENDER
			long startRenderTime = System.currentTimeMillis();
			
			mapWindow.render();
			gameWindow.render();
			frames++;
			
			totalRenderTime += System.currentTimeMillis() - startRenderTime;
			
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
				System.out.println(String.format("FPS: %2d     Update Time: %3d     Render Time: %3d", frames, totalUpdateTime, totalRenderTime));
				
				lastStatsTimeNS += 1000000000;
				frames = 0;
				totalUpdateTime = 0;
				totalRenderTime = 0;
			}
		}
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
