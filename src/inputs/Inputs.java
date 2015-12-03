package inputs;

import java.util.List;

public class Inputs {

	private List<Input> inputs;
	
	private long tick;
	
	public Inputs(long tick, List<Input> inputs) {
		this.inputs = inputs;
		this.tick = tick;
	}
	
	public long getTick() {
		return tick;
	}
	
	public List<Input> getInputs() {
		return inputs;
	}
}
