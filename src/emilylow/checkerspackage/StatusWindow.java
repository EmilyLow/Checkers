package emilylow.checkerspackage;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class StatusWindow extends JComponent{
	
 
	//Generally, board should inform window of the state of the game.
	//Window only informs board of button clicks.
	
	public static final int MESSAGE_X = 75;
	public static final int MESSAGE_Y = 100;

    private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 100;
	
	private Board board; 
	//1 or 2, does not distinguish computer
	private int turn;
	
	//If false, computer is player 2. 
	private boolean pvp;
	
	StatusWindow(){
		
		
	}
	
	public void paintComponent(Graphics g)
	   {
	      g.drawString("Player " + turn + "'s turn", MESSAGE_X, MESSAGE_Y);
	      
	   }  

	 public Dimension getPreferredSize() 
	   {  
	      return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); 
	   }  

	  
	
	public void setBoard(Board passedBoard) {
		board = passedBoard;
		//Check later
		turn = 1;
		pvp = false;
	}
	
	public void setTurn(int set) {
		turn = set;
		repaint();
	}
	
	public void newGame() {
		
	}
}
