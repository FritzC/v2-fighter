package stages;

import java.awt.Color;
import java.awt.Graphics;

import physics.CollisionAreas;
import physics.CollisionBox;

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

	public CollisionAreas boundingBoxes() {
		return /*new CollisionAreas(new CollisionBox("Stage", Color.GRAY, platforms[0].getX(), platforms[0].getY(), platforms[0].getWidth(),
				platforms[0].getHeight(), 0.0f))*/null;
	}
	
}
