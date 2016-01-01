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

	private static int tick;
	private long lastDraw;
	private Window window;
	
	public LogicThread() {
		window = new Window();
	}
	
	@Override
	public void run() {
		while (true) {
			if (System.currentTimeMillis() - lastDraw >= 1000 / Window.FRAMES_PER_SECOND) {
				//System.out.println("Game tick:" + tick);
				lastDraw = System.currentTimeMillis();
				window.getScene().tick();
				window.repaint();
				tick++;
			}
		}
	}
	
	public static int getTick() {
		return tick;
	}

}
