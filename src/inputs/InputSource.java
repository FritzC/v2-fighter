package inputs;

import java.util.HashMap;
import java.util.Map;

public abstract class InputSource {
	
	protected Map<Input, Boolean> inputs;
	
	public InputSource() {
		inputs = new HashMap<>();
		Input.populate(inputs);
	}

	/**
	 * Updates the input statuses
	 */
	public abstract void poll();
	
	/**
	 * Polls the device then returns the current input statuses
	 * @return - Map of inputs statuses
	 */
	public Map<Input, Boolean> getInputsStatus() {
		poll();
		return inputs;
	}
}
