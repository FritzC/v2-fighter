package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import characters.Fighter;
import characters.GameObject;
import main.LogicThread;
import physics.CollisionAreas;

public class TestScene extends Scene {
	
	private Point mouseLoc;
	
	public TestScene() {
		mouseLoc = new Point(0,0);
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseLoc = e.getPoint();
			}
		});
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
		g.setColor(Color.RED);
		g.fillRect(10, 10, 200, 30);
		g.fillRect(10, 50, 200, 30);
		g.setColor(Color.GREEN);
		g.fillRect(10, 10, (int) (200 * LogicThread.fighters.get(0).getHitpoiintPercent()), 30);
		g.fillRect(10, 50, (int) (200 * LogicThread.fighters.get(1).getHitpoiintPercent()), 30);
		g.setColor(Color.BLACK);
		g.drawString(mouseLoc.toString(), 10, 20);
	}

}
