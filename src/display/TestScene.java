package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import characters.Fighter;
import characters.GameObject;
import characters.Ryu;
import inputs.InputHandler;
import inputs.KeyboardSource;
import main.LogicThread;
import physics.CollisionAreas;
import physics.PhysicsEngine;
import stages.DefaultStage;
import stages.Stage;

public class TestScene extends Scene {
	
	private Point mouseLoc;
	private InputHandler inputHandler;
	private long tick;
	public static List<Fighter> fighters;
	public static List<GameObject> objects;
	public static Stage stage;
	public KeyboardSource keyboard;
	private PhysicsEngine physics;
	
	public TestScene() {
		mouseLoc = new Point(0,0);
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseLoc = e.getPoint();
			}
		});
		keyboard = new KeyboardSource();
		inputHandler = new InputHandler();
		inputHandler.addPlayer(keyboard);
		fighters = new ArrayList<>();
		fighters.add(new Ryu(InputHandler.getPlayer(0), "Me"));
		fighters.add(new Ryu(null, "Dummy"));
		fighters.get(0).setX(525);
		fighters.get(1).setX(500);
		objects = new ArrayList<>();
		stage = new DefaultStage();
		physics = new PhysicsEngine();
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(0, 0, getWidth(), getHeight());
		stage.draw(g);
		for (Fighter f : fighters) {
			f.draw(g);
		}
		for (GameObject g2 : objects) {
			g2.draw(g);
		}
		g.setColor(Color.RED);
		g.fillRect(10, 10, 200, 30);
		g.fillRect(10, 50, 200, 30);
		g.setColor(Color.GREEN);
		g.fillRect(10, 10, (int) (200 * fighters.get(0).getHitpoiintPercent()), 30);
		g.fillRect(10, 50, (int) (200 * fighters.get(1).getHitpoiintPercent()), 30);
		g.setColor(Color.BLACK);
		g.drawString(mouseLoc.toString(), 10, 20);
	}
	
	@Override
	public void tick() {
		InputHandler.updatePlayerInputs(LogicThread.getTick());
		physics.engineTick(fighters, objects, stage);
	}

}
