package emilylow.checkerspackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusWindow extends JPanel{
	
 
	//Generally, board should inform window of the state of the game.
	//Window only informs board of button clicks.
	
	public static final int MESSAGE_X = 75;
	public static final int MESSAGE_Y = 100;

    private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 120;
	
	private Board board; 
	//1 or 2, does not distinguish computer
	private int turn;
	
	//If false, computer is player 2. 
	private boolean pvp;
	private int claimedOne;
	private int claimedTwo;
	
	private JPanel leftPanel;
	private JPanel centerPanel;
	private JPanel rightPanel;
	
	JLabel p1Score;
	JLabel p2Score;
	JLabel turnLabel;
	
	
	StatusWindow(){
		claimedOne = 0;
		claimedTwo = 0;
		
		setLayout(new BorderLayout());
		
		//Test, works fine
		setBackground(Color.orange);
		
		leftPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());
		rightPanel = new JPanel(new BorderLayout());
		
		
		

		 
		
		 add(leftPanel, BorderLayout.WEST);
		 add(centerPanel, BorderLayout.CENTER);
		 add(rightPanel, BorderLayout.EAST);
		 
		 var button = new JButton("New Game");
		 
		 JLabel p1Label = new JLabel("Player 1's score:");
		  p1Score = new JLabel("" + claimedOne);
		 
		 JLabel p2Label = new JLabel("Player 2's score:");
		  p2Score = new JLabel("" + claimedTwo);
		  
		 turnLabel = new JLabel("Player 1's turn");
		 
		 leftPanel.add(p1Label, BorderLayout.NORTH);
		 leftPanel.add(p1Score, BorderLayout.SOUTH);
		
		 
		 centerPanel.add(button, BorderLayout.NORTH);
		 centerPanel.add(turnLabel, BorderLayout.SOUTH);
		 
		 rightPanel.add(p2Label, BorderLayout.NORTH);
		 rightPanel.add(p2Score, BorderLayout.SOUTH);
		 
		 
		 
		
		 


	}
//	
//	public void paintComponent(Graphics g) {
//		
//		
//
//
//		
////		
////		//Basic version
//		g.drawString("Player " + turn + "'s turn", MESSAGE_X, MESSAGE_Y);
//	      g.drawString("Pieces won:", MESSAGE_X + 200, MESSAGE_Y -40);
//	      g.drawString("Player 1: " + board.getPlayerOneTotal(), MESSAGE_X + 200, MESSAGE_Y -20);
//	      g.drawString("Player 2: " + board.getPlayerTwoTotal(), MESSAGE_X + 200, MESSAGE_Y );
//
//	      
//	     
//		
//	   
//	    
//	   }  

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
		//Possibly simplify and remove local var
		claimedOne = board.getPlayerOneTotal();
		claimedTwo = board.getPlayerTwoTotal();
		//
		p1Score.setText("" + claimedOne);
		p2Score.setText("" + claimedTwo);
//		repaint();
	}
	
	public void newGame() {
		
	}
	
	public void showWinner(int player) {
		
	}
	



}
