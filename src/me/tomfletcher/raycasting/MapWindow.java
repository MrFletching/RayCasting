package me.tomfletcher.raycasting;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MapWindow {
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	
	private static final int TILE_SIZE = 25;
	private static final int PLAYER_RADIUS = 5;
	
	private JFrame frame;
	private Canvas canvas;
	
	private World world;
	
	public MapWindow(World world) {
		this.world = world;
		
		canvas = new Canvas();
		canvas.setSize(WIDTH, HEIGHT);
		
		frame = new JFrame("Ray Casting Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		// Draw background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Draw map
		g.setColor(Color.BLACK);
		Map map = world.getMap();
		
		for(int y = 0; y < map.getHeight(); y++) {
			for(int x = 0; x < map.getWidth(); x++) {
				int wall = map.getWallAt(x, y);
				
				if(wall == 1) {
					int pX = x * TILE_SIZE;
					int pY = y * TILE_SIZE;
					
					g.fillRect(pX, pY, TILE_SIZE, TILE_SIZE);
				}
				
			}
		}
		
		// Draw player
		
		Player player = world.getPlayer();
		
		double playerX = player.getX();
		double playerY = player.getY();
		double angle = player.getAngle();
		
		int playerPX = (int)(playerX*TILE_SIZE);
		int playerPY = (int)(playerY*TILE_SIZE);
		
		// Draw rays
		g.setColor(Color.GREEN);
		for(double[] rayEndPoint: GameWindow.rayEndPoints) {
			int rayPx = (int)(rayEndPoint[0]*TILE_SIZE);
			int rayPy = (int)(rayEndPoint[1]*TILE_SIZE);
			
			g.drawLine(playerPX, playerPY, rayPx, rayPy);
		}
		
		// Draw player angle
		g.setColor(Color.RED);
		double angleX = playerX+Math.sin(angle)*GameWindow.DISTANCE_TO_VIEWPORT;
		double angleY = playerY-Math.cos(angle)*GameWindow.DISTANCE_TO_VIEWPORT;
		int anglePX = (int)(angleX*TILE_SIZE);
		int anglePY = (int)(angleY*TILE_SIZE);
		g.drawLine(playerPX, playerPY, anglePX, anglePY);
		
		// Draw viewport
		double viewportXDiff = Math.cos(angle)*GameWindow.VIEWPORT_WIDTH/2;
		double viewportYDiff = Math.sin(angle)*GameWindow.VIEWPORT_WIDTH/2;
		int viewportPX1 = (int)((angleX-viewportXDiff)*TILE_SIZE);
		int viewportPY1 = (int)((angleY-viewportYDiff)*TILE_SIZE);
		int viewportPX2 = (int)((angleX+viewportXDiff)*TILE_SIZE);
		int viewportPY2 = (int)((angleY+viewportYDiff)*TILE_SIZE);
		g.drawLine(viewportPX1, viewportPY1, viewportPX2, viewportPY2);
		
		// Draw player background
		g.setColor(Color.BLUE);
		g.fillOval(playerPX - PLAYER_RADIUS, playerPY - PLAYER_RADIUS, PLAYER_RADIUS*2, PLAYER_RADIUS*2);
		
		g.dispose();
		bs.show();
	}
	
}
