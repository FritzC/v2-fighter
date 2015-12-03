package characters;

import java.awt.Graphics;

import physics.CollisionAreas;
import physics.Vector;

public abstract class GameObject {
	
	private boolean grounded = false;
	private Vector vector = new Vector(0, 0);
	private float x, y;

	public abstract void draw(Graphics g);
	
	public abstract void advanceTick();
	
	public abstract boolean needsRemoval();
	
	public abstract float gravityMultiplier();
	
	public abstract CollisionAreas boundingBoxes();
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public Vector getVelocity() {
		return vector;
	}
	
	public void setVector(Vector v) {
		vector = v;
	}
	
	public abstract void collisionBehavior();
	
	public boolean isGrounded() {
		return grounded;
	}
	
	public void setGrounded(boolean g) {
		grounded = g;
	}
	
}
