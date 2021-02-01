package emilylow.checkerspackage;

public class StatusWindow {
	
	private Board board; 
	
	//Player 1 or Player 2, 1 or 2. Does not distinguish between a computer or person being 2.'
	//Better as boolean?
	private int turn;
	
	//If false, computer is player 2. 
	private boolean pvp;
	
	StatusWindow(){
		
	}
	
	public void setBoard(Board passedBoard) {
		board = passedBoard;
	}
	
	public void updateTurn() {
		
	}
	
	public void newGame() {
		
	}
}
