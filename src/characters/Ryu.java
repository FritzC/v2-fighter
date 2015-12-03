package characters;

import java.awt.Rectangle;

import inputs.Input;
import inputs.PlayerInputs;
import physics.CollisionAreas;

public class Ryu extends Fighter {
	
	CollisionAreas b;

	public Ryu(PlayerInputs inputs) {
		super(inputs, "./sprites/characters/ryu");
		/*b = new CollisionAreas(new Rectangle(106, 56, 79, 104));
		anims.put(IDLE_ANIM, new Animation(true, 0, Animation.extractSprites(sprites, 2593, 2602)));
		anims.put(WALK_F_ANIM, new Animation(true, 0, Animation.extractSprites(sprites, 2608, 2618)));
		anims.put(WALK_B_ANIM, new Animation(true, 0, Animation.extractSprites(sprites, 2620, 2630)));
		anims.put(ATTACK_ANIM, new Animation(false, -1, Animation.extractSprites(sprites, 3264, 3275)));
		*/setAnim(IDLE_ANIM);
	}

	@Override
	public void handleInputs() {
	}
	
	@Override
	public float gravityMultiplier() {
		return 1.0f;
	}
	
	@Override
	public CollisionAreas boundingBoxes() {
		return b;
	}

}
