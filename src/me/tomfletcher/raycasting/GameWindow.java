package me.tomfletcher.raycasting;

import java.awt.Canvas;
import java.awt.Color;
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
		
		// Fill background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		Player player = world.getPlayer();
		
		double playerX = player.getX();
		double playerY = player.getY();
		double angle = player.getAngle();
		
		int playerTileX = (int) playerX;
		int playerTileY = (int) playerY;
		
		Map map = world.getMap();
		
		// Draw walls
		g.setColor(Color.BLACK);
		
		for(int i = 0; i < WIDTH; i++) {
			// TODO: Only calculate rayAngleDiff values once
			double viewportX = (VIEWPORT_WIDTH/WIDTH)*i - VIEWPORT_WIDTH/2;
			double rayAngleDiff = Math.atan2(viewportX, DISTANCE_TO_VIEWPORT);
			
			double rayAngle = (angle + rayAngleDiff) % (Math.PI*2);
			if(rayAngle < 0) {
				rayAngle += Math.PI*2;
			}
			
			double rayX;
			double rayY;
			
			// Get horizontal collision
			int yDir = (rayAngle > Math.PI/2) && (rayAngle < Math.PI*3/2) ? 1 : -1;
			boolean collision = false;
			double rayLength = Double.MAX_VALUE;
			
			double playerYOffset = playerY % 1;
			double xDiff = -Math.tan(rayAngle)*yDir;
			int yTile = (int)playerY + yDir;
			
			if(yDir == 1) {
				playerYOffset = 1-playerYOffset;
			}
			
			double x = playerX + xDiff*playerYOffset;
			
			while(!collision) {
				int xTile = (int) x;
				
				if(map.getWallAt(xTile, yTile) == 1) {
					collision = true;
					rayX = x;
					rayY = yTile;
					if(yDir == -1) {
						rayY++;
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
			
			double playerXOffset = playerX % 1;
			double yDiff = Math.tan(rayAngle-Math.PI/2)*xDir;
			int xTile = (int)playerX + xDir;
			
			if(xDir == 1) {
				playerXOffset = 1-playerXOffset;
			}
			
			double y = playerY + yDiff*playerXOffset;
			
			while(!collision) {
				yTile = (int) y;
				
				if(map.getWallAt(xTile, yTile) == 1) {
					collision = true;
					double newRayY = y;
					double newRayX = xTile;
					if(xDir == -1) {
						newRayX++;
					}
					
					double dx = newRayX - playerX;
					double dy = newRayY - playerY;
					double dist = Math.sqrt(dx*dx + dy*dy);
					
					if(dist < rayLength) {
						rayLength = dist;
						rayX = newRayX;
						rayY = newRayY;
					}
					
				}
				
				y += yDiff;
				xTile += xDir;
			}
			
			// TODO: Fix magic number
			double wallHeight = 1000/rayLength;
			g.drawLine(i, (int)(HEIGHT/2-wallHeight/2), i, (int)(HEIGHT/2+wallHeight/2));
		}
		
		g.dispose();
		bs.show();
	}
	
}
