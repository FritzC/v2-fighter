package characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import display.Sprite;
import main.Main;
import physics.CollisionAreas;
import physics.CollisionBox;
import physics.Vector;

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

	public static Map<String, Animation> loadAnimations(File f) {
		Map<String, Animation> anims = new HashMap<>();
		try (BufferedReader reader = Files.newBufferedReader(f.toPath(), Charset.defaultCharset())) {
			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				if (lineFromFile.startsWith("Animation")) {
					String name = lineFromFile.split("\"")[1];
					boolean repeat = Boolean.parseBoolean(stripText(reader.readLine()).replace("Repeat: ", ""));
					List<AnimationStep> steps = new ArrayList<>();
					while (!(lineFromFile = reader.readLine()).contains("}")) {
						if (lineFromFile.contains("[Step ")) {
							String sName = stripText(lineFromFile).split(" ")[1].replace("]", "");
							BufferedImage image = ImageIO.read(new ByteArrayInputStream(
									Base64.decode(stripText(reader.readLine()).replace("Image: ", ""))));
							int framesToDisplay = Integer.parseInt(stripText(reader.readLine()).replace("FramesToDisplay: ", ""));
							boolean interuptable = Boolean.parseBoolean(stripText(reader.readLine()).replace("Interuptable: ", ""));
							boolean specialCancelable = Boolean.parseBoolean(stripText(reader.readLine()).replace("SpecialCancelable: ", ""));
							List<CollisionBox> boxes = new ArrayList<>();
							while (!(lineFromFile = reader.readLine()).contains("}")) {
								if (!lineFromFile.contains("CollisionBoxes")) {
									String bName = stripText(lineFromFile).replace("[", "").replace("] {", "");
									Color c = new Color(Integer.parseInt(stripText(reader.readLine()).replace("Color: ", "")));
									int x = Integer.parseInt(stripText(reader.readLine()).replace("X: ", ""));
									int y = Integer.parseInt(stripText(reader.readLine()).replace("Y: ", ""));
									int width = Integer.parseInt(stripText(reader.readLine()).replace("Width: ", ""));
									int height = Integer.parseInt(stripText(reader.readLine()).replace("Height: ", ""));
									float rotation = Float.parseFloat(stripText(reader.readLine()).replace("Rotation: ", ""));
									int damage = Integer.parseInt(stripText(reader.readLine()).replace("Damage: ", ""));
									Vector v = Vector.parseVector(stripText(reader.readLine()).replace("Trajectory: ", ""));
									boxes.add(new CollisionBox(bName, c, x, y, width, height, rotation, false, damage, v));	
									reader.readLine();
								}
							}
							steps.add(new AnimationStep(new Sprite(image, sName), interuptable, specialCancelable, framesToDisplay, new CollisionAreas(boxes)));
							reader.readLine();
						
						}
					}
					anims.put(name, new Animation(name, repeat, steps));
				}
			}
		} catch (IOException e) {
			Main.errorMsg("Failed to load animations file " + f.getName());
			e.printStackTrace();
		}
		return anims;
	}
	
	public static String stripText(String s) {
		return s.replaceAll("	", "");
	}
}
