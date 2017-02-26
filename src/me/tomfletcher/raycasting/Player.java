package me.tomfletcher.raycasting;

public class Player {
	
	private double x;
	private double y;
	private double angle;
	
	public Player(double x, double y, double angle) {
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
		angle -= 0.01;
	}
}
