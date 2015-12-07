package characters;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.Sprite;
import inputs.Input;
import inputs.PlayerInputs;
import main.Main;
import physics.CollisionAreas;

public abstract class Fighter extends GameObject {

	protected Animation currentAnim;
	private PlayerInputs inputs;

	protected Map<String, Animation> anims;
	protected Map<Integer, Sprite> sprites;
	
	public String player;

	public Fighter(PlayerInputs inputs, String player) {
		this.inputs = inputs;
		this.player = player;
		sprites = new HashMap<>();
		anims = new HashMap<>();
	}

	public Animation getCurrentAnim() {
		return currentAnim;
	}

	public void draw(Graphics g) {
		getCurrentAnim().draw((int) getX(), (int) getY(), g);
	}

	public boolean needsRemoval() {
		return false;
	}

	public PlayerInputs getInputs() {
		return inputs;
	}

	@Override
	public void collisionBehavior() {

	}

	public void defaultInputs() {
		if (getInputs() != null) {
			if (!getCurrentAnim().canAct()) {
				return;
			}
			if (getInputs().getPreviousInputs(1).getInputs().contains(Input.LEFT)) {
				setAnim(WALK_F_ANIM);
			} else if (getInputs().getPreviousInputs(1).getInputs().contains(Input.RIGHT)) {
				setAnim(WALK_B_ANIM);
			} else if (getInputs().getPreviousInputs(1).getInputs().isEmpty()) {
				setAnim(IDLE_ANIM);
				getVelocity().setX(0);
			}
		}
	}

	public void setAnim(String animation) {
		Animation anim = anims.get(animation);
		if (anim == getCurrentAnim())
			return;
		resetOthersHitByLists();
		anim.resetAnim();
		currentAnim = anim;
	}

	public void advanceTick() {
		defaultInputs();
		handleInputs();
		if (getCurrentAnim().isFinished()) {
			resetOthersHitByLists();
			currentAnim = anims.get(IDLE_ANIM);
		}
		getCurrentAnim().advance();
	}
	
	@Override
	public CollisionAreas boundingBoxes() {
		return getCurrentAnim().getCollisionBoxes();
	}

	public abstract void handleInputs();

	public final static String IDLE_ANIM = "idle";
	public final static String WALK_F_ANIM = "walk_f";
	public final static String WALK_B_ANIM = "walk_b";
	public final static String JUMP_ANIM = "jump";
	public final static String ATTACK_ANIM = "attack";
	public final static String CROUCH = "crouch";
	public final static String CROUCH_IDLE = "crouch_idle";

	public boolean isInvincible() {
		return getCurrentAnim().isInvincible();
	}

}
