
public class PlayerNode {

	String playerType;
	int rowIndex;
	int columnIndex;
	boolean visited;
	
	public PlayerNode () { }
	
	public PlayerNode (String type, int rowIndex, int columnIndex) {
		playerType = type;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		visited = false;
	}

}
