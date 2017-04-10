package me.tomfletcher.raycasting;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;

import me.tomfletcher.raycasting.input.Keyboard;
import me.tomfletcher.raycasting.input.Mouse;

public class GameWindow {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;
	
	public static final double VIEWPORT_WIDTH = 1;
	public static final double PX_PER_UNIT = WIDTH/VIEWPORT_WIDTH;
	public static final double VIEWPORT_HEIGHT = HEIGHT*PX_PER_UNIT;
	
	public static final double DISTANCE_TO_VIEWPORT = 1;
	public static final double DISTANCE_TO_VIEWPORT_PX = DISTANCE_TO_VIEWPORT*PX_PER_UNIT;
	
	public static double[][] rayEndPoints;
	
	private JFrame frame;
	private Canvas canvas;
	private BufferedImage screen;
	private int[] pixels;
	
	private World world;
	private Texture brickTexture;
	
	private double[] rayAngleDiffs;
	
	public GameWindow(World world) {
		this.world = world;
		
		brickTexture = new Texture("brick.jpg");
		screen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		
		rayEndPoints = new double[WIDTH][2];
		
		// Calculate ray angle differences for each screen column
		rayAngleDiffs = new double[WIDTH];
		
		for(int i = 0; i < WIDTH; i++) {
			double viewportX = (VIEWPORT_WIDTH/WIDTH)*i - VIEWPORT_WIDTH/2;
			rayAngleDiffs[i] = Math.atan2(viewportX, DISTANCE_TO_VIEWPORT);
		}
		
		this.canvas = new Canvas();
		canvas.addKeyListener(new Keyboard());
		canvas.addMouseMotionListener(new Mouse());
		canvas.setSize(WIDTH, HEIGHT);
		canvas.setCursor(Mouse.createBlankCursor());
		
		frame = new JFrame("Ray Casting Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.requestFocus();
	}
	
	public void update() {
		Mouse.moveMouse(canvas.getLocationOnScreen().x, canvas.getLocationOnScreen().y, WIDTH/2, HEIGHT/2);
	}
	
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		// Clear screen
		Arrays.fill(pixels, 0);
		
		Player player = world.getPlayer();
		
		double playerX = player.getX();
		double playerY = player.getY();
		double angle = player.getAngle();
		
		Map map = world.getMap();
		
		// Draw walls
		
		for(int i = 0; i < WIDTH; i++) {
			double rayAngle = (angle + rayAngleDiffs[i]) % (Math.PI*2);
			if(rayAngle < 0) {
				rayAngle += Math.PI*2;
			}
			
			double rayX = playerX;
			double rayY = playerY;
			
			// Get collision between ray and horizontal wall
			int yDir = (rayAngle > Math.PI/2) && (rayAngle < Math.PI*3/2) ? 1 : -1;
			boolean collision = false;
			double rayLength = Double.MAX_VALUE;
			double textureOffset = 0;
			
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
					textureOffset = rayX % 1;
				}
				
				x += xDiff;
				yTile += yDir;
			}
			
			
			// Get collision between ray and vertical wall
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
						textureOffset = rayY % 1;
					}
					
				}
				
				y += yDiff;
				xTile += xDir;
			}
			
			double distanceToWall = rayLength*Math.cos(rayAngleDiffs[i]);
			
			// Store ray end points
			rayEndPoints[i][0] = rayX;
			rayEndPoints[i][1] = rayY;
			
			
			//  perceived height at viewport        real height
			// ------------------------------  =  ---------------
			//       distance to viewport          real distance
			
			double wallHeightPx = DISTANCE_TO_VIEWPORT_PX * 1/distanceToWall;
			
			int yStart = (int)(HEIGHT/2-wallHeightPx/2);
			int yEnd = (int)(HEIGHT/2+wallHeightPx/2);
			
			for(int j = yStart; j < yEnd; j++) {
				if(j < 0 || j >= HEIGHT) {
					continue;
				}
				
				double heightOffset = (j - yStart) / wallHeightPx;
				int colour = brickTexture.getPixel((int) (textureOffset * 512), (int) (heightOffset * 512));
				
				pixels[j*WIDTH+i] = colour;
			}
			
		}
		
		// Draw image to screen
		Graphics g = bs.getDrawGraphics();
		g.drawImage(screen, 0, 0, null);
		g.dispose();
		bs.show();
	}
	
}
