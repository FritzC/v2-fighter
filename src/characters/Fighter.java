package characters;

import java.awt.Graphics;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import display.Sprite;
import inputs.Input;
import inputs.PlayerInputs;
import main.Main;

public abstract class Fighter extends GameObject {

	protected Animation currentAnim;
	private String imagesLocation;
	private PlayerInputs inputs;

	protected Map<String, Animation> anims;
	protected Map<Integer, Sprite> sprites;

	public Fighter(PlayerInputs inputs, String imagesLocation) {
		this.inputs = inputs;
		this.imagesLocation = imagesLocation;
		sprites = new HashMap<>();
		loadImages();
		anims = new HashMap<>();
	}

	private void loadImages() {
		File dir = new File(imagesLocation);
		if (!dir.isDirectory()) {
			Main.errorMsg("Invalid location specified for fighter sprites (" + dir.getAbsolutePath() + ")");
			return;
		}
		File[] images = dir.listFiles();
		for (File f : images) {
			if (f.getName().endsWith(".png")) {
				int id = Integer.parseInt(f.getName().replace(".png", ""));
				Sprite s = new Sprite(f.getAbsolutePath());
				sprites.put(id, s);
			}
		}
	}

	public Animation getCurrentAnim() {
		return currentAnim;
	}

	protected String getImageLocations() {
		return imagesLocation;
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
				getVelocity().setX(-1);
			} else if (getInputs().getPreviousInputs(1).getInputs().contains(Input.RIGHT)) {
				setAnim(WALK_B_ANIM);
				getVelocity().setX(1);
			} else if (getInputs().getPreviousInputs(1).getInputs().contains(Input.ATTACK)) {
				setAnim(ATTACK_ANIM);
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
		anim.resetAnim();
		currentAnim = anim;
	}

	public void advanceTick() {
		defaultInputs();
		handleInputs();
		if (getCurrentAnim().isFinished()) {
			currentAnim = anims.get(IDLE_ANIM);
		}
		getCurrentAnim().advance();
	}

	public abstract void handleInputs();

	public final static String IDLE_ANIM = "idle";
	public final static String WALK_F_ANIM = "walk forward";
	public final static String WALK_B_ANIM = "walk back";
	public final static String JUMP_ANIM = "jump";
	public final static String ATTACK_ANIM = "attack";

}
