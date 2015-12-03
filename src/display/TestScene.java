package display;

import java.awt.Graphics;

import characters.Fighter;
import characters.GameObject;
import main.LogicThread;

public class TestScene extends Scene {
	
	public TestScene() {
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(0, 0, getWidth(), getHeight());
		LogicThread.stage.draw(g);
		for (Fighter f : LogicThread.fighters) {
			f.draw(g);
		}
		for (GameObject g2 : LogicThread.objects) {
			g2.draw(g);
		}
	}

}
