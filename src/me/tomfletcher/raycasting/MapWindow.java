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
	private static final int RAY_LENGTH = 50;
	
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
		for(int i = 0; i < GameWindow.WIDTH; i++) {
			// TODO: Only calculate rayAngleDiff values once
			double viewportX = (GameWindow.VIEWPORT_WIDTH/GameWindow.WIDTH)*i - GameWindow.VIEWPORT_WIDTH/2;
			double rayAngleDiff = Math.atan2(viewportX, GameWindow.DISTANCE_TO_VIEWPORT);
			
			double rayAngle = (angle + rayAngleDiff) % (Math.PI*2);
			
			double rayX = playerX+Math.sin(rayAngle)*RAY_LENGTH;
			double rayY = playerY-Math.cos(rayAngle)*RAY_LENGTH;
			
			// Get horizontal collision
			int yDir = (rayAngle > Math.PI/2) && (rayAngle < Math.PI*3/2) ? 1 : -1;
			boolean collision = false;
			double rayLength = Double.MAX_VALUE;
			
			double playerYOffset;
			double xDiff;
			int yTile = (int)playerY + yDir;
			
			if(yDir == 1) {
				playerYOffset = 1-(playerY % 1);
				xDiff = Math.tan(Math.PI-rayAngle);
			} else {
				playerYOffset = playerY % 1;
				xDiff = Math.tan(rayAngle);
			}
			
			double x = playerX + xDiff*playerYOffset;
			
			while(!collision) {
				int xTile = (int) x;
				
				if(map.getWallAt(xTile, yTile) == 1) {
					collision = true;
					rayX = x;
					if(yDir == 1) {
						rayY = yTile;
					} else {
						rayY = yTile + 1;
					}
					
					double dx = rayX - playerX;
					double dy = rayY - playerY;
					rayLength = Math.sqrt(dx*dx + dy*dy);
				}
				
				x += xDiff;
				yTile += yDir;
			}
			
			
			// Get vertical collision
			int xDir = (rayAngle < Math.PI) ? 1 : -1;
			collision = false;
			
			double playerXOffset;
			double yDiff;
			int xTile = (int)playerX + xDir;
			
			if(xDir == 1) {
				playerXOffset = 1-(playerX % 1);
				yDiff = Math.tan(rayAngle-Math.PI/2);
			} else {
				playerXOffset = playerX % 1;
				yDiff = Math.tan(Math.PI*3/2-rayAngle);
			}
			
			double y = playerY + yDiff*playerXOffset;
			
			while(!collision) {
				yTile = (int) y;
				
				if(map.getWallAt(xTile, yTile) == 1) {
					collision = true;
					double newRayY = y;
					double newRayX;
					if(xDir == 1) {
						newRayX = xTile;
					} else {
						newRayX = xTile + 1;
					}
					
					double dx = newRayX - playerX;
					double dy = newRayY - playerY;
					double dist = Math.sqrt(dx*dx + dy*dy);
					
					if(dist < rayLength) {
						rayX = newRayX;
						rayY = newRayY;
					}
					
				}
				
				y += yDiff;
				xTile += xDir;
			}
			
			
			int rayPX = (int)(rayX*TILE_SIZE);
			int rayPY = (int)(rayY*TILE_SIZE);
			g.drawLine(playerPX, playerPY, rayPX, rayPY);
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
