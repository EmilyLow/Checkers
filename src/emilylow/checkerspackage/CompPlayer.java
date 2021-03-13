package emilylow.checkerspackage;

public class CompPlayer {

	
	Board realBoard;
	int iterations;
	
	public CompPlayer(Board sourceBoard) {
		// TODO Auto-generated constructor stub
		
		realBoard = sourceBoard;
		iterations = 5;
	}
	
	public void testTriggerTurn() {
		
		int[][] move = {{0,0}, {0, 0}};
		
		realBoard.attemptAction(move[0]);
		//Insert delay
		realBoard.attemptAction(move[1]);
	}
	
	
	public void triggerTurn() {
		
		MockBoard baseMockBoard = realBoard.makeMockBoard(iterations);
		
		int[][] bestMove = baseMockBoard.findBestMove();
		
		realBoard.attemptAction(bestMove[0]);
		//Insert delay
		realBoard.attemptAction(bestMove[1]);
	}

}
