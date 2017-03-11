package me.tomfletcher.raycasting;

import me.tomfletcher.raycasting.input.Keyboard;
import me.tomfletcher.raycasting.input.Mouse;

public class Player {
	
	private static final double RADIUS = 0.3;
	
	private double x;
	private double y;
	private double angle;
	private double speed = 0.1;
	private double rotationSpeed = 0.005;
	
	
	private World world;
	
	public Player(World world, double x, double y, double angle) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void update() {
		
		double moveX = 0;
		double moveY = 0;
		
		if(Keyboard.up) {
			moveX = Math.sin(angle)*speed;
			moveY =-Math.cos(angle)*speed;
		} else if(Keyboard.down) {
			moveX =-Math.sin(angle)*speed;
			moveY = Math.cos(angle)*speed;
		}
		
		if(Keyboard.right) {
			moveX = Math.cos(angle)*speed;
			moveY = Math.sin(angle)*speed;
		} else if(Keyboard.left) {
		    moveX =-Math.cos(angle)*speed;
			moveY =-Math.sin(angle)*speed;
		}
		
		// Check for x collisions
		if(canMoveTo(x+moveX, y)) {
			x += moveX;
		}
		
		// Check for y collisions
		if(canMoveTo(x, y+moveY)) {
			y += moveY;
		}
		
		// Turn around
		angle += Mouse.dx*rotationSpeed;
	}
	
	private boolean canMoveTo(double newX, double newY) {
		// Check four corners are empty
		return world.isSpaceAt(newX-RADIUS, newY-RADIUS) &&
			   world.isSpaceAt(newX-RADIUS, newY+RADIUS) &&
			   world.isSpaceAt(newX+RADIUS, newY-RADIUS) &&
			   world.isSpaceAt(newX+RADIUS, newY+RADIUS);
	}
}
