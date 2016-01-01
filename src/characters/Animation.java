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

	/**
	 * Name of animation
	 */
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
	 * Frames the current sprite has to still be displayed for When this is 0,
	 * switch to the next one
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
	 * Y coordinate of the ground in animation
	 */
	private int groundLocation;

	/**
	 * A character animation
	 * 
	 * @param repeat
	 *            - Whether the animation loops
	 * @param sprites
	 *            - Array of sprites that the animation is of
	 */
	public Animation(String name, boolean repeat, int groundLocation, List<AnimationStep> steps) {
		this.name = name;
		this.steps = steps;
		this.groundLocation = groundLocation;
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
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return framesToDisplay == 0 && currentStep == steps.size() - 1 && !repeat;
	}

	/**
	 * Draws the animation while advancing the frame if necessary
	 * 
	 * @param x
	 *            - x location to draw at
	 * @param y
	 *            - y location to draw at
	 * @param g
	 *            - Graphic object to draw with
	 */
	public void draw(int x, int y, Graphics g, boolean flipped) {
		try {
			steps.get(currentStep).draw(x, y, g, flipped);
		} catch (Exception e) {
			Main.errorMsg("Error drawing frame: " + currentStep + " of " + (steps.size() - 1));
			// e.printStackTrace();
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

	public int getArmorAmount() {
		return steps.get(currentStep).getArmorAmount();
	}

	/**
	 * Check if the player can act during this animation yet
	 * 
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
	
	public void setOwner(GameObject owner) {
		for (AnimationStep s : steps) {
			s.getCollisionAreas().setOwner(owner);
		}
	}

	public static Map<String, Animation> loadAnimations(File f, GameObject owner) {
		String tab = "	";
		Map<String, Animation> anims = new HashMap<>();
		try (BufferedReader reader = Files.newBufferedReader(f.toPath(), Charset.defaultCharset())) {
			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				if (lineFromFile.startsWith("Animation")) {
					String name = lineFromFile.split("\"")[1];
					boolean repeat = Boolean.parseBoolean(stripText(reader.readLine()).replace("Repeat: ", ""));
					int groundLocation = 0;
					if ((lineFromFile = reader.readLine()).contains("GroundLocation: ")) {
						groundLocation = Integer.parseInt(stripText(lineFromFile).replace("GroundLocation: ", ""));
					}
					List<AnimationStep> steps = new ArrayList<>();
					String sName = "";
					BufferedImage image = null;
					int framesToDisplay = FRAMES_PER_SPRITE;
					boolean interuptable = false, specialCancelable = false, hitInvincible = false,
							normalInvincible = false, grabInvincible = false, projectileInvincible = false,
							setVelocity = false, ignoresGravity = false;
					int armorAmount = 0;
					Vector velocity = new Vector(0, 0);
					List<CollisionBox> boxes = new ArrayList<>();
					while (!(lineFromFile = reader.readLine()).equals(tab + "}")) {
						if (lineFromFile.contains("[Step ")) {
							sName = stripText(lineFromFile).split(" ")[1].replace("]", "");
						} else if (lineFromFile.contains("Image: ")) {
							image = ImageIO.read(new ByteArrayInputStream(
									Base64.decode(stripText(lineFromFile).replace("Image: ", ""))));
						} else if (lineFromFile.contains("FramesToDisplay: ")) {
							framesToDisplay = Integer
									.parseInt(stripText(lineFromFile).replace("FramesToDisplay: ", ""));
						} else if (lineFromFile.contains("Interuptable: ")) {
							interuptable = Boolean
									.parseBoolean(stripText(lineFromFile).replace("Interuptable: ", ""));
						} else if (lineFromFile.contains("SpecialCancelable: ")) {
							specialCancelable = Boolean
									.parseBoolean(stripText(lineFromFile).replace("SpecialCancelable: ", ""));
						} else if (lineFromFile.contains("HitInvincible: ")) {
							hitInvincible = Boolean
									.parseBoolean(stripText(lineFromFile).replace("HitInvincible: ", ""));
						} else if (lineFromFile.contains("NormalInvincible: ")) {
							normalInvincible = Boolean
									.parseBoolean(stripText(lineFromFile).replace("NormalInvincible: ", ""));
						} else if (lineFromFile.contains("GrabInvincible: ")) {
							grabInvincible = Boolean
									.parseBoolean(stripText(lineFromFile).replace("GrabInvincible: ", ""));
						} else if (lineFromFile.contains("ProjectileInvincible: ")) {
							projectileInvincible = Boolean
									.parseBoolean(stripText(lineFromFile).replace("ProjectileInvincible: ", ""));
						} else if (lineFromFile.contains("ArmorAmount: ")) {
							armorAmount = Integer
									.parseInt(stripText(lineFromFile).replace("ArmorAmount: ", ""));
						} else if (lineFromFile.contains("IgnoresGravity: ")) {
							ignoresGravity = Boolean
									.parseBoolean(stripText(lineFromFile).replace("IgnoresGravity: ", ""));
						} else if (lineFromFile.contains("SetVelocity: ")) {
							setVelocity = Boolean
									.parseBoolean(stripText(lineFromFile).replace("SetVelocity: ", ""));
						} else if (lineFromFile.contains("Velocity: ")) {
							velocity = Vector.parseVector(stripText(lineFromFile).replace("Velocity: ", ""));
						} else if (lineFromFile.contains("CollisionBoxes")) {
							String bName = "";
							Color c = Color.YELLOW;
							int x = 0, y = 0, width = 0, height = 0, damage = -1, hitstun = 0;
							float rotation = 0;
							boolean knockdown = false;
							Vector v = new Vector(0, 0);
							while (!(lineFromFile = reader.readLine()).equals(tab + tab + tab + "}")) {
								if (lineFromFile.contains("] {")) {
									bName = stripText(lineFromFile).replace("[", "").replace("] {", "");
								} else if (lineFromFile.contains("Color: ")) {
									c = new Color(Integer.parseInt(stripText(lineFromFile).replace("Color: ", "")));
								} else if (lineFromFile.contains("X: ")) {
									x = Integer.parseInt(stripText(lineFromFile).replace("X: ", ""));
								} else if (lineFromFile.contains("Y: ")) {
									y = Integer.parseInt(stripText(lineFromFile).replace("Y: ", ""));
								} else if (lineFromFile.contains("Width: ")) {
									width = Integer.parseInt(stripText(lineFromFile).replace("Width: ", ""));
								} else if (lineFromFile.contains("Height: ")) {
									height = Integer.parseInt(stripText(lineFromFile).replace("Height: ", ""));
								} else if (lineFromFile.contains("Rotation: ")) {
									rotation = Float.parseFloat(stripText(lineFromFile).replace("Rotation: ", ""));
								} else if (lineFromFile.contains("Damage: ")) {
									damage = Integer.parseInt(stripText(lineFromFile).replace("Damage: ", ""));
								} else if (lineFromFile.contains("Hitstun: ")) {
									hitstun = Integer.parseInt(stripText(lineFromFile).replace("Hitstun: ", ""));
								} else if (lineFromFile.contains("Knockdown: ")) {
									knockdown = Boolean
											.parseBoolean(stripText(lineFromFile).replace("Knockdown: ", ""));
								} else if (lineFromFile.contains("Trajectory: ")) {
									v = Vector.parseVector(stripText(lineFromFile).replace("Trajectory: ", ""));
								} else if (lineFromFile.equals(tab + tab + tab + tab + "}")) {
									boxes.add(new CollisionBox(bName, c, x, y, width, height, rotation, false, damage,
											hitstun, knockdown, v));
								}
							}
						}
						if (lineFromFile.equals(tab + tab + "}")) {
							steps.add(new AnimationStep(new Sprite(image, sName), interuptable, specialCancelable,
									framesToDisplay, hitInvincible, normalInvincible, grabInvincible,
									projectileInvincible, armorAmount, ignoresGravity, velocity, setVelocity,
									new CollisionAreas(new ArrayList<CollisionBox>(boxes))));
							boxes.clear();
						}
					}
					anims.put(name, new Animation(name, repeat, groundLocation, steps));
					anims.get(name).setOwner(owner);
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

	public CollisionAreas getCollisionBoxes() {
		return steps.get(currentStep).getCollisions();
	}

	public boolean isInvincible() {
		return steps.get(currentStep).isHitInvincible();
	}

	public int getGroundLocation() {
		return groundLocation;
	}

	public void setGroundLocation(int groundLocation) {
		this.groundLocation = groundLocation;
	}

	public boolean ignoresGravity() {
		return steps.get(currentStep).isIgnoresGravity();
	}

	public boolean setsVelocity() {
		return steps.get(currentStep).setsVelocity();
	}

	public Vector getVelocity() {
		return steps.get(currentStep).getVelocity();
	}
	
	public int getWidth() {
		return steps.get(currentStep).getSprite().getImage().getWidth();
	}
}
