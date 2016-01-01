package characters;

import java.awt.Graphics;

import display.Sprite;
import main.Main;
import physics.CollisionAreas;
import physics.Vector;

public class AnimationStep {
	
	/**
	 * Sprite to draw
	 */
	private Sprite sprite;
	
	/**
	 * Whether the animation can be interrupted by input on this step
	 */
	private boolean interuptable;
	
	/**
	 * Whether the animation can be interrupted by a special input on this step
	 */
	private boolean specialInteruptable;
	
	/**
	 * Number of frames this step is displayed
	 */
	private int framesToDisplay;

	/**
	 * Whether fighter can be hit out of this step
	 */
	private boolean hitInvincible;

	/**
	 * Whether fighter can be hit out of this step by a normal
	 */
	private boolean normalInvincible;
	
	/**
	 * Whether fighter can be grabbed out of this step
	 */
	private boolean grabInvincible;
	
	/**
	 * Whether fighter can be hit out of this step by a projectile
	 */
	private boolean projectileInvincible;
	
	/**
	 * Number of hits of armor that the animation has this step
	 */
	private int armorAmount;
	
	/**
	 * Whether gravity is ignored for this step
	 */
	private boolean ignoresGravity;
	
	/**
	 * Velocity of character during this step
	 */
	private Vector velocity;
	
	/**
	 * Whether or not this step sets player velocity
	 */
	private boolean setVelocity;

	/**
	 * Hitboxes for this step
	 */
	private CollisionAreas collisionAreas;

	public AnimationStep(Sprite sprite, boolean interuptable, boolean specialInteruptable, int framesToDisplay,
			boolean hitInvincible, boolean normalInvincible, boolean grabInvincible, boolean projectileInvincible,
			int armorAmount, boolean ignoresGravity, Vector velocity, boolean setVelocity, CollisionAreas areas) {
		this.sprite = sprite;
		this.interuptable = interuptable;
		this.specialInteruptable = specialInteruptable;
		this.framesToDisplay = framesToDisplay;
		this.hitInvincible = hitInvincible;
		this.grabInvincible = grabInvincible;
		this.projectileInvincible = projectileInvincible;
		this.collisionAreas = areas;
		this.normalInvincible = normalInvincible;
		this.armorAmount = armorAmount;
		this.velocity = velocity;
		this.setVelocity = setVelocity;
		this.ignoresGravity = ignoresGravity;
	}

	/**
	 * Draw this animation step
	 * @param x - X location to draw step
	 * @param y - Y location to draw step
	 * @param g = Graphics to use to draw step
	 * @param flipped - If the step is flipped
	 */
	public void draw(int x, int y, Graphics g, boolean flipped) {
		sprite.draw(x, y, g, flipped);
		if (Main.DEBUG) {
			collisionAreas.draw(x, y, g, flipped, sprite.getImage().getWidth());
		}
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean isInteruptable() {
		return interuptable;
	}

	public void setInteruptable(boolean interuptable) {
		this.interuptable = interuptable;
	}

	public boolean isSpecialInteruptable() {
		return specialInteruptable;
	}

	public void setSpecialInteruptable(boolean specialInteruptable) {
		this.specialInteruptable = specialInteruptable;
	}

	public int getFramesToDisplay() {
		return framesToDisplay;
	}

	public void setFramesToDisplay(int framesToDisplay) {
		this.framesToDisplay = framesToDisplay;
	}

	public CollisionAreas getCollisions() {
		return collisionAreas;
	}

	public void setCollisions(CollisionAreas collisions) {
		this.collisionAreas = collisions;
	}
	
	public String toString() {
		return sprite.getName();
	}
	
	public boolean isHitInvincible() {
		return hitInvincible;
	}

	public void setHitInvincible(boolean hitInvincible) {
		this.hitInvincible = hitInvincible;
	}

	public boolean isGrabInvincible() {
		return grabInvincible;
	}

	public void setGrabInvincible(boolean grabInvincible) {
		this.grabInvincible = grabInvincible;
	}

	public boolean isProjectileInvincible() {
		return projectileInvincible;
	}

	public void setProjectileInvincible(boolean projectileInvicible) {
		this.projectileInvincible = projectileInvicible;
	}

	public int getArmorAmount() {
		return armorAmount;
	}

	public void setArmorAmount(int armorAmount) {
		this.armorAmount = armorAmount;
	}

	public CollisionAreas getCollisionAreas() {
		return collisionAreas;
	}

	public void setCollisionAreas(CollisionAreas collisionAreas) {
		this.collisionAreas = collisionAreas;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public boolean setsVelocity() {
		return setVelocity;
	}

	public void setSetVelocity(boolean setVelocity) {
		this.setVelocity = setVelocity;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public boolean isNormalInvincible() {
		return normalInvincible;
	}

	public void setNormalInvincible(boolean normalInvincible) {
		this.normalInvincible = normalInvincible;
	}

	public boolean isIgnoresGravity() {
		return ignoresGravity;
	}

	public void setIgnoresGravity(boolean ignoresGravity) {
		this.ignoresGravity = ignoresGravity;
	}

}
