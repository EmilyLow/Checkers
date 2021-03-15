package emilylow.checkerspackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class CompPlayer {

	
	Board realBoard;
	int iterations;
	Timer timer;
	
	public CompPlayer(Board sourceBoard) {
		// TODO Auto-generated constructor stub
		
		realBoard = sourceBoard;
		iterations = 1;
		timer = new Timer(1000, new WaitAction());
		timer.setRepeats(false);
	}
	
	public void testTriggerTurn() {
		
//		int[][] move = {{1,2}, {2,3}};
//		
//		realBoard.attemptAction(move[0]);
//		//Insert delay
//		
//		realBoard.attemptAction(move[1]);
		timer.start();
	}
	
	public void afterTimer() {
		int[][] move = {{1,2}, {2,3}};
		
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

	private class WaitAction implements ActionListener {
		@Override
	    public void actionPerformed(ActionEvent e)
	    {
	        afterTimer();
	    }
		
	}
}
