package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CollisionBox {

	private int x, y, width, height;
	private float angle;
	private String name;
	private Color color;
	private boolean hitbox;
	private int damage;
	
	public CollisionBox(String name, Color color, int x, int y, int width, int height, float angle, boolean hitbox, int damage) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.color = color;
		this.hitbox = hitbox;
		this.damage = damage;
	}
	
	public void draw(int offX, int offY, Graphics g) {
		 Graphics2D g2d = (Graphics2D) g;
		 Rectangle r = new Rectangle(x, y, width, height);
		 g2d.rotate(Math.toRadians(angle));
		 g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
		 g2d.draw(r);
		 g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
		 g2d.fill(r);
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
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
}
