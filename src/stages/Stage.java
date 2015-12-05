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
			boundingBoxes().draw(0, 0, g);
			g.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		}
	}

	public CollisionAreas boundingBoxes() {
		Platform p = platforms[0];
		CollisionBox b = new CollisionBox("stage", Color.BLUE, p.getX(), p.getY(), p.getWidth(), p.getHeight(), 0,
				false, -1, 0, false, new Vector(0, 0));
		List<CollisionBox> b2 = new ArrayList<CollisionBox>();
		b2.add(b);
		return new CollisionAreas(b2);
	}
	
}
