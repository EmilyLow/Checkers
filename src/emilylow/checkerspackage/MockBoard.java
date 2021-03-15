package emilylow.checkerspackage;

public class MockBoard extends Board {
	
	int rootPlayer; 
	int pathScore;
	/*
	 * [[x, y], [x2, y2]
	 *  x = [0][0] y = [0][1] x2 = [1][0] y2 = [1][1]
	 */
	int[][] bestMove;
	int count;

	public MockBoard(StatusWindow statusWindow) {
		super(statusWindow);
		// TODO Auto-generated constructor stub
	}

	public MockBoard(Square[][] squares, int turn, int oneTotal, int twoTotal, int count) {
		super(squares, turn, oneTotal, twoTotal);
		// TODO Auto-generated constructor stub
	}
	
	
	//Start as finding one move ahead
	public int[][] findBestMove() {
		
		
		return bestMove;
	}

}
