package me.tomfletcher.raycasting;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	private BufferedImage image;
	private int[] pixels;
	private final int width;
	private final int height;

	public Texture(String filename) {
		try {
			image = ImageIO.read(this.getClass().getResource("/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		pixels = new int[width*height*3];
		
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	public int getPixel(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return 0;
		}
		
		return pixels[y*width+x];
	}
	
}
