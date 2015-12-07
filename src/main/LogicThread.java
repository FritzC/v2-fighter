package main;

import java.util.ArrayList;
import java.util.List;

import characters.Fighter;
import characters.GameObject;
import characters.Ryu;
import display.Window;
import inputs.InputHandler;
import inputs.KeyboardSource;
import physics.PhysicsEngine;
import stages.DefaultStage;
import stages.Stage;

public class LogicThread implements Runnable {

	private long lastDraw;
	private Window window;
	private InputHandler inputHandler;
	private long tick;
	public static List<Fighter> fighters;
	public static List<GameObject> objects;
	public static Stage stage;
	public KeyboardSource keyboard;
	
	public LogicThread() {
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
		window = new Window();
	}
	
	@Override
	public void run() {
		while (true) {
			if (System.currentTimeMillis() - lastDraw >= 1000 / Window.FRAMES_PER_SECOND) {
				//System.out.println("Game tick:" + tick);
				lastDraw = System.currentTimeMillis();
				InputHandler.updatePlayerInputs(tick);
				PhysicsEngine.engineTick(fighters, objects, stage);
				window.repaint();
				tick++;
			}
		}
	}

}
