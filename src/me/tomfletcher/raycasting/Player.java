package me.tomfletcher.raycasting;

import me.tomfletcher.raycasting.input.Keyboard;

public class Player {
	
	private double x;
	private double y;
	private double angle;
	private double speed = 0.1;
	
	public Player(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = 1;
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
		
		if(Keyboard.up) {
			x += Math.sin(angle)*speed;
			y -= Math.cos(angle)*speed;
		} else if(Keyboard.down) {
			x -= Math.sin(angle)*speed;
			y += Math.cos(angle)*speed;
		}
		
		if(Keyboard.right) {
			x += Math.cos(angle)*speed;
			y += Math.sin(angle)*speed;
		} else if(Keyboard.left) {
			x -= Math.cos(angle)*speed;
			y -= Math.sin(angle)*speed;
		}
	}
}
