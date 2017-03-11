package me.tomfletcher.raycasting.input;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Mouse extends MouseMotionAdapter {
	
	private static int originX, originY;
	public static int dx, dy;

	@Override
	public void mouseMoved(MouseEvent e) {
		dx = e.getX()-originX;
		dy = e.getY()-originY;
	}
	
	public static void moveMouse(int windowX, int windowY, int x, int y) {
		originX = x;
		originY = y;
		
		try {
			Robot r = new Robot();
			r.mouseMove(windowX+x, windowY+y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
