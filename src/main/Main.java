package main;

import display.Window;

public class Main {
	
	public final static boolean DEBUG = true;
	
	private static Thread logicThread;
	
	public static void main(String args[]) {
		logicThread = new Thread(new LogicThread());
		logicThread.start();
	}
	
	public static void errorMsg(String message) {
		System.out.println("Error: " + message);
	}
}
