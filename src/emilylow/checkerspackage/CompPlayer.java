package emilylow.checkerspackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Timer;

public class CompPlayer {

	
	Board realBoard;
	int iterations;
	Timer timer;
	
	public CompPlayer(Board sourceBoard) {
		// TODO Auto-generated constructor stub
		
		realBoard = sourceBoard;
		iterations = 3;
		timer = new Timer(500, new WaitAction());
		timer.setRepeats(false);
	}
	
	public void triggerTurn() {
		

		timer.start();
	}
	
	public void testBeginTurn() {
		int[][] move = {{1,2}, {2,3}};
		
		realBoard.attemptAction(move[0]);
		
		
		realBoard.attemptAction(move[1]);
		
		
	}
	
	/*
	 * Note:  Computer "pretends" to be a human player when its taking actual turn.
	 */
	public void beginTurn() {
		
		//Might want to change how checkWin works
		
		if(!realBoard.checkWin()) {
			MockBoard baseMockBoard = realBoard.makeMockBoard(iterations);
//			System.out.println("Before base findBestMove()");
			baseMockBoard.findBestMove();
//			System.out.println("Post base findBestMove()");
			System.out.println("Computer checking: " + baseMockBoard.getExtendedJump());
			int[][] chosenMove  = baseMockBoard.getChosenMove();
			System.out.println("Chosen move: " + Arrays.deepToString(chosenMove));
			
			realBoard.attemptAction(chosenMove[0]);
			
			realBoard.attemptAction(chosenMove[1]);
		}
		
		
	}

	private class WaitAction implements ActionListener {
		@Override
	    public void actionPerformed(ActionEvent e)
	    {
	        beginTurn();
	    }
		
	}
}
