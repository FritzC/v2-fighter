package physics;

import java.awt.geom.Area;

public class Collision {
	
	private CollisionBox myBox, oBox;
	private Area intersection;
	
	public Collision(CollisionBox myBox, CollisionBox oBox, Area intersection) {
		this.myBox = myBox;
		this.oBox = oBox;
		this.intersection = intersection;
	}

	public CollisionBox getMyBox() {
		return myBox;
	}

	public CollisionBox getoBox() {
		return oBox;
	}

	public Area getIntersection() {
		return intersection;
	}
	
	
}
