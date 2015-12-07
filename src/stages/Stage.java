package stages;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import physics.CollisionAreas;
import physics.CollisionBox;
import physics.Vector;

public abstract class Stage {

	private int width, height;
	private Platform platforms[];
	
	public Stage(int width, int height, Platform ... platforms) {
		this.width = width;
		this.height = height;
		this.platforms = platforms;
	}
	
	public void draw(Graphics g) {
		for (Platform p : platforms) {
			g.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		}
	}

	public boolean collision(float f, float g) {
		for (Platform p : platforms) {
			if (p.contains(f, g)) {
				return true;
			}
		}
		return false;
	}
	
	public int getY() {
		return platforms[0].getY();
	}
	
}
