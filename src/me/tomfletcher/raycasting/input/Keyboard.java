package me.tomfletcher.raycasting.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {
	
	public static boolean up, down, left, right;

	@Override
	public void keyPressed(KeyEvent e) {
		setKey(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setKey(e.getKeyCode(), false);
	}
	
	public void setKey(int keyCode, boolean b) {
		switch(keyCode) {
			case KeyEvent.VK_UP: up = b; return;
			case KeyEvent.VK_DOWN: down = b; return;
			case KeyEvent.VK_LEFT: left = b; return;
			case KeyEvent.VK_RIGHT: right = b; return;
		}
	}

	
}
