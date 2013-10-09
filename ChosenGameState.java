
public class ChosenGameState {

	double value;
	PlayerNode player;
	ChosenGameState childState;
	
	public ChosenGameState() { }

	public ChosenGameState(PlayerNode player, double value, ChosenGameState state) {
		this.player = player;
		this.value = value;
		childState = state;
	}
}
