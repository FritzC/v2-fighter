package display;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import characters.GameObject;


public abstract class Scene extends JPanel {
	
	private List<GameObject> drawables;
	private List<GameObject> remove;
	
	public Scene() {
		drawables = new ArrayList<>();
		remove = new ArrayList<>();
	}
	
	public abstract void draw(Graphics g);
	
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        for (GameObject d : drawables)  {
        	if (d.needsRemoval()) {
        		remove.add(d);
        	}
        }
        for (GameObject d : remove) {
        	drawables.remove(d);
        }
        remove.clear();
        for (GameObject d : drawables) {
        	d.draw(g);
        }
	}
	
	public void addDrawable(GameObject d) {
		drawables.add(d);
	}
	
	public void removeDrawable(GameObject d) {
		drawables.remove(d);
	}
	
}
