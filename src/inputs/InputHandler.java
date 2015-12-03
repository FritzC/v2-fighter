package inputs;

import java.util.ArrayList;
import java.util.List;

public class InputHandler  {
	
	private static List<PlayerInputs> players;
	
	public InputHandler() {
		players = new ArrayList<>();
	}
	
	public void addPlayer(InputSource source) {
		PlayerInputs p = new PlayerInputs();
		p.setSource(source);
		players.add(p);
	}
	
	/**
	 * Polls all players input sources
	 */
	public static void updatePlayerInputs(long gameTicks) {
		for (PlayerInputs p : players) {
			p.pollSource(gameTicks);
		}
	}
	
	public static PlayerInputs getPlayer(int i) {
		return players.get(i);
	}
}
