package inputs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInputs {
	
	private final static int MOTION_BUFFER = 8;
	
	public Map<Input, Boolean> inputStatus;
	public List<Inputs> inputs;
	
	private InputSource source;
	
	public PlayerInputs() {
		inputStatus = new HashMap<>();
		Input.populate(inputStatus);
		inputs = new ArrayList<>();
	}
	
	/**
	 * Sets the input source
	 * @param source - Input source to now use
	 */
	public void setSource(InputSource source) {
		this.source = source;
	}
	
	/**
	 * Updates inputStatus map by pulling input status from input source
	 */
	public void pollSource(long gameTick) {
		if (source != null) {
			inputStatus = source.getInputsStatus();
			List<Input> l = new ArrayList<>();
			for (Input i : inputStatus.keySet()) {
				if (inputStatus.get(i)) {
					l.add(i);
				}
			}
			if (inputs.size() == 0 || !sameInputs(l)) {
				detectMotions(gameTick, l);
				inputs.add(new Inputs(gameTick, l));
				System.out.println(l);
			}
		}
	}
	
	/**
	 * Checks your last few inputs for a stick motion and inserts into list if one was made
	 * @param gameTick - Current game tick
	 * @param l - List to modify
	 */
	public void detectMotions(long gameTick, List<Input> l) {
		if (l.contains(Input.LEFT)) {
			if (gameTick - getPreviousInputs(1).getTick() < MOTION_BUFFER
					&& getPreviousInputs(1).getInputs().contains(Input.DOWN)
					&& getPreviousInputs(1).getInputs().contains(Input.LEFT)) {
				if (getPreviousInputs(2).getInputs().contains(Input.DOWN)
						&& !getPreviousInputs(2).getInputs().contains(Input.LEFT)) {
					l.add(Input.QC_L);
				} else if (getPreviousInputs(2).getInputs().contains(Input.LEFT)
						&& !getPreviousInputs(2).getInputs().contains(Input.DOWN)) {
					l.add(Input.DP_L);
				}
			}
		}
		if (l.contains(Input.RIGHT)) {
			if (gameTick - getPreviousInputs(1).getTick() < MOTION_BUFFER
					&& getPreviousInputs(1).getInputs().contains(Input.DOWN)
					&& getPreviousInputs(1).getInputs().contains(Input.RIGHT)) {
				if (getPreviousInputs(2).getInputs().contains(Input.DOWN)
						&& !getPreviousInputs(2).getInputs().contains(Input.RIGHT)) {
					l.add(Input.QC_R);
				} else if (getPreviousInputs(2).getInputs().contains(Input.RIGHT)
						&& !getPreviousInputs(2).getInputs().contains(Input.DOWN)) {
					l.add(Input.DP_R);
				}
			}
		}
	}
	
	public Inputs getPreviousInputs(int fromBack) {
		return inputs.get(inputs.size() - fromBack);
	}
	
	public boolean sameInputs(List<Input> newInputs) {
		for (Input i : getPreviousInputs(1).getInputs()) {
			if (i.isTrueInput() && !newInputs.contains(i)) {
				return false;
			}
		}
		for (Input i : newInputs) {
			if (i.isTrueInput() && !getPreviousInputs(1).getInputs().contains(i)) {
				return false;
			}
		}
		return true;
	}
}
