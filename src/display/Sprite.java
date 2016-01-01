package display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;

public class Sprite {
	
	private BufferedImage image; 
	private String name;
	
	public Sprite(String location) {
		try {
			File f = new File(location);
			image = ImageIO.read(f);
			name = f.getName();
		} catch (IOException e) {
			Main.errorMsg("Failure to load sprite: " + location);
			e.printStackTrace();
		}
	}
	
	public Sprite (BufferedImage image, String name) {
		this.image = image;
		this.name = name;
	}
	
	public void draw(int x, int y, Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	public void draw(int x, int y, Graphics g, boolean flipped) {
		if (flipped) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(image, x + image.getWidth(), y, -image.getWidth(), image.getHeight(), null);
		} else {
			draw(x, y, g);
		}
	}

	public String getName() {
		return name;
	}
	
	public BufferedImage getImage() {
		return image;
	}

}
