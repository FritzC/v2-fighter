package physics;

import java.awt.Graphics;
import java.util.List;

import characters.GameObject;

public class CollisionAreas {
	
	private GameObject owner;

	private List<CollisionBox> boxes;
	
	public CollisionAreas(List<CollisionBox> boxes) {
		this.boxes = boxes;
	}
	
	public void draw(int x, int y, Graphics g) {
		for (CollisionBox b : boxes) {
			b.draw(x, y, g);
		}
	}
	
	public int getX() {
		if (owner != null) {
			return (int) owner.getX();
		}
		return 0;
	}
	
	public int getY() {
		if (owner != null) {
			return (int) owner.getY();
		}
		return 0;
	}
	
	public void setOwner(GameObject owner) {
		this.owner = owner;
	}
	
	public boolean collision(CollisionAreas b) {
/*		for (Rectangle r : boxes) {
			for (Rectangle r2 : b.boxes) {
				if (r.getMinX() + offX <= r2.getMinX() + b.offX
						&& r.getMaxX() + offX >= r2.getMinX() + b.offX
						&& r.getMinY() + offY <= r2.getMinY() + b.offY
						&& r.getMaxY() + offY >= r2.getMinY() + b.offY) {
					return true;
				}
				if (r.getMinX() + offX <= r2.getMaxX() + b.offX
						&& r.getMaxX() + offX >= r2.getMaxX() + b.offX
						&& r.getMinY() + offY <= r2.getMinY() + b.offY
						&& r.getMaxY() + offY >= r2.getMinY() + b.offY) {
					return true;
				}
				if (r.getMinX() + offX <= r2.getMinX() + b.offX
						&& r.getMaxX() + offX >= r2.getMinX() + b.offX
						&& r.getMinY() + offY <= r2.getMaxY() + b.offY
						&& r.getMaxY() + offY >= r2.getMaxY() + b.offY) {
					return true;
				}
				if (r.getMinX() + offX <= r2.getMaxX() + b.offX
						&& r.getMaxX() + offX >= r2.getMaxX() + b.offX
						&& r.getMinY() + offY <= r2.getMaxY() + b.offY
						&& r.getMaxY() + offY >= r2.getMaxY() + b.offY) {
					return true;
				}
				if (r2.getMinX() + b.offX <= r.getMinX() + offX
						&& r2.getMaxX() + b.offX >= r.getMinX() + offX
						&& r2.getMinY() + b.offY <= r.getMinY() + offY
						&& r2.getMaxY() + b.offY >= r.getMinY() + offY) {
					return true;
				}
				if (r2.getMinX() + b.offX <= r.getMaxX() + offX
						&& r2.getMaxX() + b.offX >= r.getMaxX() + offX
						&& r2.getMinY() + b.offY <= r.getMinY() + offY
						&& r2.getMaxY() + b.offY >= r.getMinY() + offY) {
					return true;
				}
				if (r2.getMinX() + b.offX <= r.getMinX() + offX
						&& r2.getMaxX() + b.offX >= r.getMinX() + offX
						&& r2.getMinY() + b.offY <= r.getMaxY() + offY
						&& r2.getMaxY() + b.offY >= r.getMaxY() + offY) {
					return true;
				}
				if (r2.getMinX() + b.offX <= r.getMaxX() + offX
						&& r2.getMaxX() + b.offX >= r.getMaxX() + offX
						&& r2.getMinY() + b.offY <= r.getMaxY() + offY
						&& r2.getMaxY() + b.offY >= r.getMaxY() + offY) {
					return true;
				}
			}
		}*/
		
		return false;
	}
	
	public List<CollisionBox> getBoxes() {
		return boxes;
	}
}
