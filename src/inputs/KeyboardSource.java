package inputs;

import java.awt.event.KeyEvent;

public class KeyboardSource extends InputSource {

	@Override
	public void poll() {
		inputs.put(Input.UP, KeyStates.getState(KeyEvent.VK_W));
		inputs.put(Input.DOWN, KeyStates.getState(KeyEvent.VK_S));
		inputs.put(Input.RIGHT, KeyStates.getState(KeyEvent.VK_D));
		inputs.put(Input.LEFT, KeyStates.getState(KeyEvent.VK_A));
		inputs.put(Input.ATTACK, KeyStates.getState(KeyEvent.VK_NUMPAD1));
	}
}
