package physics;

import java.util.List;

import characters.Fighter;
import characters.GameObject;
import stages.Stage;

public class PhysicsEngine {
	
	private final static float GRAVITY_ACCEL = 1f;
	private final static float TERMINAL_VEL = 20;

	public static void engineTick(List<Fighter> fighters, List<GameObject> objects, Stage stage) {
		for (Fighter f : fighters) {
			f.advanceTick();
		}
/*		for (GameObject g : objects) {
			g.advanceTick();
		}*/
		gravity(fighters, objects);
		collisions(fighters, objects);
		movement(fighters, objects, stage);
	}
	
	public static void movement(List<Fighter> fighters, List<GameObject> objects, Stage stage) {
		for (Fighter f : fighters) {
			if (f.getCurrentAnim().setsVelocity()) {
				f.setVector(f.getCurrentAnim().getVelocity());
			}
			if (f.getVelocity().getY() < 0) {
				f.setGrounded(false);
			}
			f.setX(f.getX() + f.getVelocity().getX());
			f.setY(f.getY() + f.getVelocity().getY());
			if (f.getX() < 0) {
				f.setX(0);
			}
			if (stage.collision(f.getX(), f.getY() + f.getCurrentAnim().getGroundLocation())) {
				f.setY(stage.getY() - f.getCurrentAnim().getGroundLocation() - 1);
				f.setGrounded(true);
				f.getVelocity().setX(0);
				f.getVelocity().setY(0);
			}
		}
/*		for (GameObject g : objects) {
			g.setX(g.getX() + g.getVelocity().getX());
			g.setY(g.getY() + g.getVelocity().getY());
			List<CollisionBox> boxes;
			if (!(boxes = g.boundingBoxes().getCollisions(stage.boundingBoxes(), false)).isEmpty()) {
				do {
					g.setY((int) g.getY() - 1);
				} while ((boxes = g.boundingBoxes().getCollisions(stage.boundingBoxes(), false)).isEmpty());
				g.setGrounded(true);
				g.getVelocity().setX(0);
				g.getVelocity().setY(0);
			}
		}*/
	}
	
	public static void collisions(List<Fighter> fighters, List<GameObject> objects) {
		for (Fighter f : fighters) {
			for (Fighter f2 : fighters) {
				if (f.equals(f2)) {
					continue;
				}
				List<CollisionBox> boxes = f.boundingBoxes().getCollisions(f2.boundingBoxes(), true);
				if (!boxes.isEmpty()) {
					if (!f.isInvincible()) {
						for (CollisionBox b : boxes) {
							if (!f.wasRecentlyHitBy(f2, b)) {
								f.setVector(b.getTrajectory());
								f.addRecentlyHitBy(f2, b);
								f2.addRecentlyHit(f);
							}
						}
					}
				}
			}
			
/*			for (GameObject g : objects) {
				List<CollisionBox> boxes = f.boundingBoxes().getCollisions(g.boundingBoxes(), true);
				if (!boxes.isEmpty()) {
					f.collisionBehavior();
					g.collisionBehavior();
					break;
				}
			}*/
		}
	}
	
	public static void gravity(List<Fighter> fighters, List<GameObject> objects) {
		for (Fighter f : fighters) {
			if (!f.isGrounded() && !f.getCurrentAnim().ignoresGravity()) {
				f.getVelocity().transformY(GRAVITY_ACCEL * f.gravityMultiplier());
				if (f.getVelocity().getY() > TERMINAL_VEL * f.gravityMultiplier()) {
					f.getVelocity().setY(TERMINAL_VEL * f.gravityMultiplier());
				}
			}
		}
/*		for (GameObject g : objects) {
			if (!g.isGrounded()) {
				g.getVelocity().transformY(GRAVITY_ACCEL * g.gravityMultiplier());
				if (g.getVelocity().getY() > TERMINAL_VEL * g.gravityMultiplier()) {
					g.getVelocity().setY(TERMINAL_VEL * g.gravityMultiplier());
				}
			}
		}*/
	}
}
