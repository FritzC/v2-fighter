package characters;

import java.io.File;

import inputs.Input;
import inputs.PlayerInputs;
import physics.CollisionAreas;

public class Ryu extends Fighter {
	
	CollisionAreas b;

	public Ryu(PlayerInputs inputs, String player) {
		super(inputs, player);
		anims = Animation.loadAnimations(new File("ryu.anims"), this);
		setAnim(IDLE_ANIM);
	}

	@Override
	public void handleInputs() {
		if (getInputs() != null && getInputs().getPreviousInputs(1).getInputs().contains(Input.ATTACK)) {
			setAnim("shuryuken");
		}
	}
	
	@Override
	public float gravityMultiplier() {
		return 1.0f;
	}

}
