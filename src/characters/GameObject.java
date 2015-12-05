package characters;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import physics.CollisionAreas;
import physics.CollisionBox;
import physics.Vector;

public abstract class GameObject {
	
	private boolean grounded = false;
	private Vector vector = new Vector(0, 0);
	private float x, y;
	
	private Map<GameObject, List<CollisionBox>> recentlyHitBy;
	private List<GameObject> recentlyHit;
	
	public GameObject() {
		recentlyHitBy = new HashMap<>();
		recentlyHit = new ArrayList<>();
	}

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
	
	public void addRecentlyHit(GameObject o) {
		recentlyHit.add(o);
	}
	
	public void addRecentlyHitBy(GameObject owner, CollisionBox b) {
		if (!recentlyHitBy.containsKey(owner)) {
			recentlyHitBy.put(owner, new ArrayList<CollisionBox>());
		}
		recentlyHitBy.get(owner).add(b);
	}
	
	public boolean wasRecentlyHitBy(GameObject owner, CollisionBox b) {
		if (!recentlyHitBy.containsKey(owner)) {
			return false;
		}
		for (CollisionBox b2 : recentlyHitBy.get(owner)) {
			if (b.toString().equals(b2.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void resetOthersHitByLists() {
		for (GameObject o : recentlyHit) {
			o.resetHitByList(this);
		}
	}
	
	public void resetHitByList(GameObject o) {
		recentlyHitBy.remove(o);
	}
	
}
