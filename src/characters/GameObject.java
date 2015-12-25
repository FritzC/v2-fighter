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
	
	private int hitpoints;
	private int maxHitpoints;
	
	private boolean grounded = false;
	private Vector vector = new Vector(0, 0);
	private float x, y;
	
	private Map<GameObject, List<CollisionBox>> recentlyHitBy;
	private List<GameObject> recentlyHit;
	
	public GameObject() {
		recentlyHitBy = new HashMap<>();
		recentlyHit = new ArrayList<>();
		maxHitpoints = hitpoints = getMaxHitpoints();
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
	
	public int getHitpoints() {
		return hitpoints;
	}
	
	public int getMaxHitpoints() {
		return maxHitpoints;
	}
	
	public void modifyHitpoints(int change) {
		hitpoints += change;
		if (hitpoints > maxHitpoints) {
			hitpoints = maxHitpoints;
		} else if (hitpoints < 0) {
			hitpoints = 0;
		}
	}
	
	public void setMaxHitpoints(int hitpoints) {
		maxHitpoints = this.hitpoints = hitpoints;
	}
	
	public float getHitpoiintPercent() {
		if (maxHitpoints > 0) {
			return hitpoints / (float) maxHitpoints;
		}
		return 0;
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
		vector.setX(v.getX());
		vector.setY(v.getY());
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
