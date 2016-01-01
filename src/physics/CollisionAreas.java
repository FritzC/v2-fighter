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
	
	public void draw(int x, int y, Graphics g, boolean flipped, int off) {
		for (CollisionBox b : boxes) {
			b.draw(x, y, g, flipped, off);
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
				float myAngle = myBox.getAngle();
				float oAngle = oBox.getAngle();
				int myX = myBox.getX();
				int oX = oBox.getX();
				if (owner.isFlipped()) {
					myX -= (myBox.getLocalX() + myBox.getWidth()  / 2 - owner.getWidth() / 2) * 2 - myBox.getWidth();
					myShape = new Rectangle2D.Double(myX, myBox.getY(), myBox.getHeight(), myBox.getWidth());
					myAngle = -myBox.getAngle() + 90;
				}
				if (oBox.getOwner().isFlipped()) {
					oX -= (oBox.getLocalX() + oBox.getWidth()  / 2 - oBox.getOwner().getWidth() / 2) * 2 -  oBox.getWidth();
					oShape = new Rectangle2D.Double(oX, oBox.getY(), oBox.getHeight(), oBox.getWidth());
					oAngle = -oBox.getAngle() + 90;
				}
				AffineTransform at = new AffineTransform();
				at.rotate(Math.toRadians(myAngle), myX, myBox.getY());
				myShape = at.createTransformedShape(myShape);
				at = new AffineTransform();
				at.rotate(Math.toRadians(oAngle), oX, oBox.getY());
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
