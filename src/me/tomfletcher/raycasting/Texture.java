package me.tomfletcher.raycasting;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	private BufferedImage image;

	public Texture(String filename) {
		try {
			image = ImageIO.read(this.getClass().getResource("/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPixel(int x, int y) {
		return image.getRGB(x, y);
	}
	
}
