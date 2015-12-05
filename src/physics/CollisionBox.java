package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import characters.GameObject;

public class CollisionBox {

	private int x, y, width, height;
	private float angle;
	private String name;
	private Color color;
	private boolean hitbox;
	private int damage;
	private Vector trajectory;
	private int hitstunFrames;
	private boolean knockdown;
	private GameObject owner;
	
	public CollisionBox(String name, Color color, int x, int y, int width, int height, float angle, boolean hitbox,
			int damage, int hitstunFrames, boolean knockdown, Vector trajectory) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.color = color;
		this.hitbox = hitbox;
		this.damage = damage;
		this.trajectory = trajectory;
		this.hitstunFrames = hitstunFrames;
		this.knockdown = knockdown;
	}
	
	public void draw(int offX, int offY, Graphics g) {
		 Graphics2D g2d = (Graphics2D) g;
		 Rectangle r = new Rectangle(offX + x, offY + y, width, height);
		 g2d.rotate(Math.toRadians(angle), offX + x, offY + y);
		 g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
		 g2d.draw(r);
		 g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
		 g2d.fill(r);
		 g2d.rotate(Math.toRadians(-angle), offX + x, offY + y);
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getX() {
		if (owner != null) {
			return (int) (x + owner.getX());
		}
		return x;
	}

	public int getY() {
		if (owner != null) {
			return (int) (y + owner.getY());
		}
		return y;
	}
	
	public int getLocalX() {
		return x;
	}
	
	public int getLocalY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getAngle() {
		return angle;
	}

	public String toString() {
		return name;
	}
	
	public boolean isHitbox() {
		return hitbox;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public Vector getTrajectory() {
		return trajectory;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void setHitbox(boolean h) {
		hitbox = h;
	}
	
	public void setDamage(int dmg) {
		damage = dmg;
	}
	
	public void setTrajectory(Vector v) {
		trajectory = v;
	}

	public int getHitstunFrames() {
		return hitstunFrames;
	}

	public void setHitstunFrames(int hitstunFrames) {
		this.hitstunFrames = hitstunFrames;
	}
	
	public void setKnockdown(boolean b) {
		knockdown = b;
	}
	
	public boolean knocksDown() {
		return knockdown;
	}

	public GameObject getOwner() {
		return owner;
	}

	public void setOwner(GameObject owner) {
		this.owner = owner;
	}
}
