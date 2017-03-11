package me.tomfletcher.raycasting.input;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

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
	
	public static Cursor createBlankCursor() {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		
		return blankCursor;
	}
}
