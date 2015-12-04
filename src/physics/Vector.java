package physics;

public class Vector {

	private float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void transformX(float sum) {
		x += sum;
	}
	
	public void transformY(float sum) {
		y += sum;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	public static Vector parseVector(String s) {
		Vector v = new Vector(0, 0);
		v.setX(Float.parseFloat(s.split(", ")[0].replace("[","")));
		v.setY(Float.parseFloat(s.split(", ")[1].replace("]","")));
		return v;
	}
}
