package emilylow.checkerspackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Timer;

public class CompPlayer {

	
	Board realBoard;
	int iterations;
	Timer timer;
	
	public CompPlayer(Board sourceBoard) {
		
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
		
		
		
		if(!realBoard.checkWin()) {
			MockBoard baseMockBoard = realBoard.makeMockBoard(iterations);

			baseMockBoard.findBestMove();


			int[][] chosenMove  = baseMockBoard.getChosenMove();

			
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
