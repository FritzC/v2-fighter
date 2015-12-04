package physics;

import java.util.List;

import characters.Fighter;
import characters.GameObject;
import stages.Stage;

public class PhysicsEngine {
	
	private final static float GRAVITY_ACCEL = 0.2f;
	private final static float TERMINAL_VEL = 12;

	public static void engineTick(List<Fighter> fighters, List<GameObject> objects, Stage stage) {
		for (Fighter f : fighters) {
			f.advanceTick();
		}
		for (GameObject g : objects) {
			g.advanceTick();
		}
		gravity(fighters, objects);
		collisions(fighters, objects);
		movement(fighters, objects, stage);
	}
	
	public static void movement(List<Fighter> fighters, List<GameObject> objects, Stage stage) {
		for (Fighter f : fighters) {
			f.setX(f.getX() + f.getVelocity().getX());
			f.setY(f.getY() + f.getVelocity().getY());
			if (f.boundingBoxes().collision(stage.boundingBoxes()) != null) {
				do {
					f.setY((int) f.getY() - 1);
				} while (f.boundingBoxes().collision(stage.boundingBoxes()) != null);
				f.setGrounded(true);
				f.getVelocity().setX(0);
				f.getVelocity().setY(0);
			}
		}
		for (GameObject g : objects) {
			g.setX(g.getX() + g.getVelocity().getX());
			g.setY(g.getY() + g.getVelocity().getY());
			if (g.boundingBoxes().collision(stage.boundingBoxes()) != null) {
				do {
					g.setY((int) g.getY() - 1);
				} while (g.boundingBoxes().collision(stage.boundingBoxes()) != null);
				g.setGrounded(true);
				g.getVelocity().setX(0);
				g.getVelocity().setY(0);
			}
		}
	}
	
	public static void collisions(List<Fighter> fighters, List<GameObject> objects) {
		for (Fighter f : fighters) {
			// hitboxes of fighter moves here
			
			for (GameObject g : objects) {
				if (f.boundingBoxes().collision(g.boundingBoxes()) != null) {
					f.collisionBehavior();
					g.collisionBehavior();
					break;
				}
			}
		}
	}
	
	public static void gravity(List<Fighter> fighters, List<GameObject> objects) {
		for (Fighter f : fighters) {
			if (!f.isGrounded()) {
				f.getVelocity().transformY(GRAVITY_ACCEL * f.gravityMultiplier());
				if (f.getVelocity().getY() > TERMINAL_VEL * f.gravityMultiplier()) {
					f.getVelocity().setY(TERMINAL_VEL * f.gravityMultiplier());
				}
			}
		}
		for (GameObject g : objects) {
			if (!g.isGrounded()) {
				g.getVelocity().transformY(GRAVITY_ACCEL * g.gravityMultiplier());
				if (g.getVelocity().getY() > TERMINAL_VEL * g.gravityMultiplier()) {
					g.getVelocity().setY(TERMINAL_VEL * g.gravityMultiplier());
				}
			}
		}
	}
}
