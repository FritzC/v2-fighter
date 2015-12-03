package display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import inputs.KeyStates;

public class Window extends JFrame {
	
	public final static int FRAMES_PER_SECOND = 60;

	private Scene scene; 
	
	public Window() {
		scene = new TestScene();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
		getContentPane().add(scene, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1000, 500));
        setLocationRelativeTo(null);
        addKeyListener(new KeyStates());
        pack();
        setVisible(true);
	}
	
	public Scene getScene() {
		return scene;
	}
	
}
