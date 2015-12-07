package physics;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
		for (CollisionBox b : boxes) {
			b.setOwner(owner);
		}
	}
	
	public List<CollisionBox> getCollisions(CollisionAreas b, boolean hitboxes) {
		List<CollisionBox> collisions = new ArrayList<>();
		for (CollisionBox myBox : boxes) {
			for (CollisionBox oBox : b.getBoxes()) {
				if (!hitboxes) {
					if (myBox.getDamage() != -1 && oBox.getDamage() == -1
							|| myBox.getDamage() == -1 && oBox.getDamage() != -1) {
						continue;
					}
				} else {
					if (myBox.getDamage() != -1 || oBox.getDamage() == -1) {
						continue;
					}
				}
				Shape myShape = new Rectangle2D.Double(myBox.getX(), myBox.getY(), myBox.getWidth(), myBox.getHeight());
				Shape oShape = new Rectangle2D.Double(oBox.getX(), oBox.getY(), oBox.getWidth(), oBox.getHeight());
				AffineTransform at = new AffineTransform();
				at.rotate(Math.toRadians(myBox.getAngle()), myBox.getX(), myBox.getY());
				myShape = at.createTransformedShape(myShape);
				at = new AffineTransform();
				at.rotate(Math.toRadians(oBox.getAngle()), oBox.getX(), oBox.getY());
				oShape = at.createTransformedShape(oShape);
				Area a = new Area(myShape);
				Area a2 = new Area(oShape);
				a.intersect(a2);
				if (!a.isEmpty()) {
					collisions.add(oBox);
				}
			}
		}
		return collisions;
	}
	
	public List<CollisionBox> getBoxes() {
		return boxes;
	}
}
