package me.tomfletcher.raycasting;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GameWindow {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static final double DISTANCE_TO_VIEWPORT = 1;
	public static final double VIEWPORT_WIDTH = 1;
	public static final double VIEWPORT_HEIGHT = VIEWPORT_WIDTH*HEIGHT/WIDTH;
	
	private JFrame frame;
	private Canvas canvas;
	
	private World world;
	
	public GameWindow(World world) {
		this.world = world;
		
		this.canvas = new Canvas();
		canvas.setSize(WIDTH, HEIGHT);
		
		frame = new JFrame("Ray Casting Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		
		Player player = world.getPlayer();
		
		double playerX = player.getX();
		double playerY = player.getY();
		double angle = player.getAngle();
		
		int playerTileX = (int) playerX;
		int playerTileY = (int) playerY;
		
		Map map = world.getMap();
		
		
		for(int i = 0; i < WIDTH; i++) {
			// TODO: Only calculate rayAngleDiff values once
			double viewportX = (VIEWPORT_WIDTH/WIDTH)*i - VIEWPORT_WIDTH/2;
			double rayAngleDiff = Math.atan2(viewportX, DISTANCE_TO_VIEWPORT);
			
			double rayAngle = (angle + rayAngleDiff) % Math.PI*2;
			
			// Get horizontal collision
			boolean dirDown = (rayAngle > Math.PI/2) && (rayAngle < Math.PI*3/2);
			
			if(dirDown) {
				
			}
			
		}
		
		
		g.dispose();
		bs.show();
	}
	
}
