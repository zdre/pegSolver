package peg;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * PegSolve finds all possible solutions to the Peg game using recursion, and prints the first solution for each starting position.
 * There are a total of 131,448 move permutations with a 1-Peg remaining solution. 
 * @author Andrey Zolotov
 *
 */
public class PegSolve {

	private static int[][] validMoves = {{},{4,6},
		                                {7,9},{8,10},
		                      {1,6,11,13},{12,14},{1,4,13,15},
		                         {2,9},{3,10},{2,7},{3,8},
		                  {4,13},{5,14},{4,6,11,15},{5,12},{6,13}};
	private static int [] pegs = new int[16];
	private static int [] starts = {1,2,4,5};
	private static Stack<Integer> moveFrom = new Stack<Integer>();
	private static Stack<Integer> moveTo = new Stack<Integer>();
	private static int depth = 0;
	private static int solutions = 0;
	
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		for (int start: starts){
			solutions = 0;
			setUp(start);
			printBoard();
			solve();
			System.out.println(solutions + " solutions in " + (System.currentTimeMillis() - startTime) + "ms");
		}

	}

	private static void solve() {
		depth++;
		if (depth > 20) {
			System.err.println("Depth: " + (++depth) );
		}
		for (int from = 1; from < 16; from++){
			if (pegs[from] != 0){
				List<Integer> validMoves = getValidMoves(from);
				if(validMoves != null){
					for (Integer to: validMoves){
						int remove = getRemoveIndex(from,to);
						if (pegs[remove] != 0){
							int remVal = performMove(from,to,remove);
							if (solved(pegs)){
								if (solutions == 0) {
									//System.out.println(Arrays.toString(pegs));
									//printBoard();
									System.out.println("From: " + moveFrom);
									System.out.println("  To: " + moveTo);
								}
								solutions++;
							} else {
								solve();
							}
							undoMove(from,to,remove, remVal);
						}
					}
				}
			}
		}
		depth--;
	}

	private static int performMove(int from, Integer to, int remove) {
		int result = pegs[remove];
		moveFrom.push(from);
		moveTo.push(to);
		pegs[to] = pegs[from];
		pegs[from] = 0;
		pegs [remove] = 0;
		return result;
	}

	private static void printBoard() {
		System.out.println("        " + pegs[1] +
				"\n      " + pegs[2] + "  " + pegs[3] +
				"\n     " + pegs[4] + "  "+ pegs[5] + "  "+ pegs[6] + "\n    "
				+ pegs[7] + "  "+ pegs[8] + "  "+ pegs[9] + " "+ pegs[10] + "\n  "
				+ pegs[11] + " "+ pegs[12] + " "+ pegs[13] + " "+ pegs[14] + " "+ pegs[15]);
		
	}

	private static void undoMove(int from, Integer to, int remove, int remVal) {
		moveFrom.pop();
		moveTo.pop();
		pegs[remove] = remVal;
		pegs[from] = pegs[to];
		pegs[to] = 0;
	}

	private static List<Integer> getValidMoves(int from) {
		ArrayList<Integer>result = null;
		for (int moveTo: validMoves[from]){
			if (pegs[moveTo] == 0){
				if (result == null){
					result = new ArrayList<Integer>(4);
				}
				result.add(moveTo);
			}
		}
		return result;
	}

	private static int getRemoveIndex(int from, int to) {
		return (from + to)/2;
	}

	private static boolean solved(int[] pegs2) {
		int count = 0;
		for (int peg : pegs){
			if (peg > 0){
				count++;
				if (count > 1){
					return false;
				}
			}
		}
		return true;
	}

	private static void setUp(int start) {
		for (int i = 1; i< 16; i++){
			pegs[i]= (i == start? 0 : i);
		}
	}

	
}
