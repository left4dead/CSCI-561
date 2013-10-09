import java.io.*;
import java.util.*;

public class Go {
	boolean pruning;
	PlayerNode board[][];
	static int maxDepth;
	int depth;
	String currentPlayer;
	int visitedNodes = 0;
	
	public Go() {}
	
	public Go(boolean pruning) {
		board = new PlayerNode[6][6];
		depth = 0;
		maxDepth = 4;
		currentPlayer = "B";
		this.pruning = pruning;
		GetNodeContents(board);
	}
	
	// Create the initial board using the configuration provided by input file
	void GetNodeContents (PlayerNode[][]  board) {
		try {
			BufferedReader input = new BufferedReader (new FileReader("input.txt"));
			
			try {
				String line  = null;
				String[] nodeMembers;
				int i=0,j=0;
				String playerType;
				
				while ((line = input.readLine()) != null) {
					nodeMembers = line.split(",");
					i = Integer.parseInt(nodeMembers[0]) - 1;
					j = Integer.parseInt(nodeMembers[1]) - 1;
					
					if (nodeMembers.length == 2)
						playerType = "";
					else
						playerType = nodeMembers[2].toString();
					
					board[i][j] = new PlayerNode(playerType, i, j);
				}
			}
			
			catch (IOException e) {
				throw e;
			}
			finally {
				input.close();
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Displays the best path output
	void DisplayResult (ChosenGameState state) {
		int value = (int)state.value;
		System.out.println("Best Strategy:");
		while (state != null) {
			System.out.println("Depth "+depth+": Player "+state.player.playerType+" places stone at ("+(state.player.columnIndex+1)+","+(state.player.rowIndex+1)+").");
			state = state.childState;
			depth++;
		}
		System.out.println("Depth "+depth+": Utility value of current board configuration "+value+".");
	}
	
	// Algorithm entering point: MAX (Black) player's turn
	int GetMinMax (PlayerNode[][] board) {
		double alpha=0, beta=0;
		
		if (pruning)
		{ alpha = Double.NEGATIVE_INFINITY; beta = Double.POSITIVE_INFINITY; }
		
		visitedNodes++;
		
		ChosenGameState state = GetMaxValue(board, alpha, beta, depth);
		
		if (pruning)
			DisplayResult(state);
		
		return 1;
	}
	
	// Get the Maximum value state for MAX player
	ChosenGameState GetMaxValue(PlayerNode[][] board, double alpha, double beta, int depth) {
		boolean isPruned = false;
		
		if (currentPlayer.equals("B")) {
			if (depth == maxDepth){
				currentPlayer = "W";
				ChosenGameState s = new ChosenGameState();
				s.value = getUtilityValue(board);
				return s;
			}
			else {
				ChosenGameState state = new ChosenGameState();
				state.value = Double.NEGATIVE_INFINITY;
				for (int i=0; i<6; i++) {
					if (isPruned == true) break;
					for (int j=0; j<6; j++) {
						if (board[i][j].playerType == "") {
							visitedNodes++;
							PlayerNode[][] newBoard = new PlayerNode[6][6];
							newBoard = CloneArray(board);
							newBoard[i][j].playerType = "B";

							CaptureNodes(newBoard, currentPlayer);
							
							currentPlayer = "W";
							ChosenGameState chosenState = GetMinValue(newBoard, alpha, beta, depth+1);
							state = GetMaxState(state, chosenState);
							
							// Update the previous state to new state
							if (state == chosenState) {
								ChosenGameState v = new ChosenGameState();
								v.player = chosenState.player;
								v.value = chosenState.value;
								v.childState = chosenState.childState;
								state.childState = v;
								state.player = newBoard[i][j];
							}
							
							if (pruning) {
								alpha = Math.max(alpha, state.value);
								if (alpha >= beta) {
									isPruned = true;
									break;
								}
							}
			
							newBoard = null;
						}
					}
				}
				currentPlayer = "W";
				return state;
			}
		}
		return null;
	}
	
	// Get the Minimum value state for MIN player
	ChosenGameState GetMinValue (PlayerNode[][] board, double alpha, double beta, int depth) {
		boolean isPruned = false;
		if (currentPlayer.equals("W")) {
			if (depth == maxDepth) {
				currentPlayer = "B";
				ChosenGameState s = new ChosenGameState();
				s.value = getUtilityValue(board);
				return s;
			}
			else {
				ChosenGameState state = new ChosenGameState();
				state.value = Double.POSITIVE_INFINITY;
				for (int i=0; i<6; i++) {
					if (isPruned == true) break;
					for (int j=0; j<6; j++) {
						if (board[i][j].playerType == "") {
							visitedNodes++;
							PlayerNode[][] newBoard = new PlayerNode[6][6];
							newBoard = CloneArray(board);
							newBoard[i][j].playerType = "W";

							CaptureNodes(newBoard, currentPlayer);
							currentPlayer = "B";
							
							ChosenGameState	chosenState = GetMaxValue(newBoard, alpha, beta, depth+1);
							state = GetMinState(state, chosenState);
							
							// Update the previous state to new state
							if (chosenState != null && state == chosenState) {
								if (depth != maxDepth-1) {
									ChosenGameState v = new ChosenGameState();
									v.player = chosenState.player;
									v.value = chosenState.value;
									v.childState = chosenState.childState;
									state.childState = v;
								}
								state.player = newBoard[i][j];
							}
							
							if (pruning) {
								beta = Math.min(state.value, beta);
								if (alpha >= beta) {
									isPruned = true;
									break;
								}
							}
							
							newBoard = null;
						}
					}
				}
				currentPlayer = "B";
				return state;
			}
		}
		return  null;
	}
	
	// Get the maximum state based on value of state
	ChosenGameState GetMaxState (ChosenGameState state1, ChosenGameState state2) {
		if (state1.value >= state2.value)
			return state1;
		else
			return state2;
	}
	
	// Get the minimum state based on value of state
	ChosenGameState GetMinState (ChosenGameState state1, ChosenGameState state2) {
		if (state1.value <= state2.value)
			return state1;
		else
			return state2;
	}
	
	// Evaluate the utility value
	double getUtilityValue (PlayerNode[][] board) {
		double blackCount = 0, whiteCount = 0;
		for (int i=0; i<6; i++) {
			for (int j=0; j<6; j++) {
				if (board[i][j].playerType.equals("B"))
					blackCount++;
				else if (board[i][j].playerType.equals("W"))
					whiteCount++;
				else
					continue;
			}
		}
		return (blackCount-whiteCount);
	}
	
	// Capture the opponent player node if it has liberty value as zero
	void CaptureNodes (PlayerNode[][] board, String currentPlayer) {
		int liberty=0;
		String OpponentPlayer;
		if (currentPlayer.equals("B"))
			OpponentPlayer = "W";
		else
			OpponentPlayer = "B";
		
		for (int i=0; i<6; i++) {
			for (int j=0; j<6; j++) {
				if (!board[i][j].playerType.equals(OpponentPlayer))
						continue;
				liberty = GetLiberty (board, i, j, OpponentPlayer);
				if (liberty == 0) {
					board[i][j].playerType = "Invalid";
				}
				ResetVisitedFlag (board);
			}
		}
	}
	
	// Get liberty value
	// If greater than one, then return the value
	int GetLiberty (PlayerNode[][] nBoard, int i, int j, String player) {
		int liberty=0;
		ArrayList<PlayerNode> adjacentNodes = new ArrayList<PlayerNode>();

		//Get liberties from bottom of the current position
		if (i!=5) {
			if (nBoard[i+1][j].playerType.equals("") && nBoard[i+1][j].visited == false)
				return ++liberty;
			else if (nBoard[i+1][j].playerType.equals(player) && nBoard[i+1][j].visited == false)
				adjacentNodes.add(nBoard[i+1][j]);
			
			nBoard[i+1][j].visited = true;
		}
		
		//Get liberties from right of the current position
		if (j!=5) {
			if (nBoard[i][j+1].playerType.equals("") && nBoard[i][j+1].visited == false)
				return ++liberty;
			else if (nBoard[i][j+1].playerType.equals(player) && nBoard[i][j+1].visited == false)
				adjacentNodes.add(nBoard[i][j+1]);
			
			nBoard[i][j+1].visited = true;
		}
		//Get liberties from top of the current position
		if (i!=0) {
			if (nBoard[i-1][j].playerType.equals("") && nBoard[i-1][j].visited == false)
				return ++liberty;
			else if (nBoard[i-1][j].playerType.equals(player) && nBoard[i-1][j].visited == false)
				adjacentNodes.add(nBoard[i-1][j]);

			nBoard[i-1][j].visited = true;
		}
		//Get liberties from left of the current position
		if (j!=0) {
			if (nBoard[i][j-1].playerType.equals("") && nBoard[i][j-1].visited == false)
				return ++liberty;
			else if (nBoard[i][j-1].playerType.equals(player) && nBoard[i][j-1].visited == false)
				adjacentNodes.add(nBoard[i][j-1]);

			nBoard[i][j-1].visited = true;
		}
		
		nBoard[i][j].visited = true;
		
		for (PlayerNode node : adjacentNodes) {
			liberty += GetLiberty(nBoard, node.rowIndex, node.columnIndex, player);
		}
		
		return liberty;
	}
	
	// Reset the visited flags in the board for each node
	void ResetVisitedFlag (PlayerNode[][] board) {
		for (int i=0; i<6; i++) {
			for (int j=0; j<6; j++) {
				board[i][j].visited = false;
			}
		}
	}
	
	// Clones the provided source array
	public static PlayerNode[][] CloneArray(PlayerNode[][] src) {
		int length = src.length;
		PlayerNode[][] target = new PlayerNode[length][src[0].length];
		
		for(int i=0; i<src.length; i++) {
			for(int j=0; j<src[i].length; j++) {
				target[i][j] = new PlayerNode(src[i][j].playerType, src[i][j].rowIndex, src[i][j].columnIndex);
			}
		}

	    return target;
	}

}