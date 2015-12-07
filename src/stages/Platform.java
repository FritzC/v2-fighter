package stages;

import java.awt.Rectangle;

public class Platform {
	
	private int x, y, height, width;

	public Platform(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public boolean contains(float f, float g) {
		return new Rectangle(x, y, width, height).contains(f, g);
	}
}
