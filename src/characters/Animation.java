package characters;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import display.Sprite;
import main.Main;

public class Animation {
	
	/**
	 * Number of frames each sprite is displayed for
	 */
	public final static int FRAMES_PER_SPRITE = 5;
	
	private String name;
	
	/**
	 * Array of sprites in the animation
	 */
	private List<AnimationStep> steps;
	
	/**
	 * Frame currently being drawn
	 */
	private int currentStep;
	
	/**
	 * Frames the current sprite has to still be displayed for
	 * When this is 0, switch to the next one
	 */
	private int framesToDisplay;
	
	/**
	 * Speed the animation is played
	 */
	private float speed;
	
	/**
	 * Whether the animation will repeat
	 */
	private boolean repeat;
	
	/**
	 * A character animation
	 * @param repeat - Whether the animation loops
	 * @param sprites - Array of sprites that the animation is of
	 */
	public Animation(String name, boolean repeat, List<AnimationStep> steps) {
		this.name = name;
		this.steps = steps;
		currentStep = 0;
		speed = 1.0f;
		if (steps.size() > 0) {
			framesToDisplay = steps.get(0).getFramesToDisplay();
		}
		this.repeat = repeat;
	}
	
	/**
	 * Resets an animation to the beginning
	 */
	public void resetAnim() {
		currentStep = 0;
		speed = 1.0f;
		framesToDisplay = steps.get(0).getFramesToDisplay();
	}
	
	/**
	 * Gets whether the animation has finished playing yet
	 * @return
	 */
	public boolean isFinished() {
		return framesToDisplay == 0 && currentStep == steps.size() - 1 && !repeat;
	}
	
	/**
	 * Draws the animation while advancing the frame if necessary
	 * @param x - x location to draw at
	 * @param y - y location to draw at
	 * @param g - Graphic object to draw with
	 */
	public void draw(int x, int y, Graphics g) {
		try {
			steps.get(currentStep).draw(x, y, g);
		} catch (Exception e) {
			Main.errorMsg("Error drawing frame: " + currentStep + " of " + (steps.size() - 1));
			//e.printStackTrace();
		}
	}
	
	/**
	 * Advances a frame if necessary
	 */
	public void advance() {
		if (framesToDisplay > 0)
			framesToDisplay--;
		else if (currentStep < steps.size() - 1) {
			currentStep++;
			framesToDisplay = steps.get(currentStep).getFramesToDisplay();
		} else if (repeat) {
			currentStep = 0;
			framesToDisplay = steps.get(currentStep).getFramesToDisplay();
		}
	}
	
	/**
	 * Extract a section of sprites from the characters map of sprites
	 * @param sprites - Map of sprites to pull from
	 * @param startId - Beginning sprite ID
	 * @param endId - Ending sprite ID
	 * @return - Array of sprites ranging from startId to endId
	 */
	public static Sprite[] extractSprites(Map<Integer, Sprite> sprites, int startId, int endId) {
		Sprite[] list = new Sprite[endId - startId];
		for (int i = startId; i < endId; i++) {
			list[i - startId] = sprites.get(i);
		}
		return list;
	}
	
	/**
	 * Check if the player can act during this animation yet
	 * @return - Whether the player can act during this animation yet
	 */
	public boolean canAct() {
		return steps.get(currentStep).isInteruptable();
	}
	
	public void setString(String s) {
		name = s;
	}
	
	public String toString() {
		return name;
	}

	public boolean repeat() {
		return repeat;
	}
	
	public List<AnimationStep> getSteps() {
		return steps;
	}

	public void setRepeat(boolean selected) {
		repeat = selected;
	}
}
