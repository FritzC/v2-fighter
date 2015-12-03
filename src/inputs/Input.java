package inputs;

import java.util.Map;

public enum Input {

	UP(true),
	DOWN(true),
	LEFT(true),
	RIGHT(true),
	ATTACK(true),
	QC_R(false),
	QC_L(false),
	HC_R(false),
	HC_L(false),
	DP_R(false),
	DP_L(false);
	
	private boolean trueInput;
	
	private Input(boolean trueInput) {
		this.trueInput = trueInput;
	}
	
	public boolean isTrueInput() {
		return trueInput;
	}
	
	public static void populate(Map<Input, Boolean> map) {
		for (Input i : values()) {
			map.put(i, false);
		}
	}
}
