
public class MinMax {

	public static void main(String[] args) {
		
		// MinMax search algorithm starts here
		boolean pruning1 = false;
		double start1 = System.currentTimeMillis();
		Go go1 = new Go(pruning1);
		go1.GetMinMax(go1.board);
		double end1 = System.currentTimeMillis();
		
		double minMaxWithoutPruning = (end1-start1)/1000;
		int visitedNodesWithoutPruning = go1.visitedNodes;
		go1 = null;
		// MinMax search algorithm ends here
		
		// MinMax with Alpha-Beta pruning search algorithm starts here
		boolean pruning2 = true;
		double start2 = System.currentTimeMillis();
		Go go2 = new Go(pruning2);
		go2.GetMinMax(go2.board);
		double end2 = System.currentTimeMillis();
		
		double alphaBetaTime = (end2-start2)/1000;
		int visitedNodesWithPruning = go2.visitedNodes;
		go2 = null;
		// MinMax with Alpha-Beta pruning search algorithm ends here
		
		System.out.println("Comparison:");
		System.out.println("MinMax with pruning: running time "+alphaBetaTime+"s;");
		System.out.println("MinMax with pruning: pruned "+(visitedNodesWithoutPruning-visitedNodesWithPruning)+" nodes;");
		System.out.println("MinMax without pruning: running time "+minMaxWithoutPruning+"s.");
	}
}