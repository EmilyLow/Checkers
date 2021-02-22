package emilylow.checkerspackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

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
	JLabel feedbackLabel;
	
	JToggleButton compToggle;
	

	//TO DO: Make New Game work
	StatusWindow(){
		claimedOne = 0;
		claimedTwo = 0;
		
		setLayout(new BorderLayout());
		
		//Test, works fine (or not)
//		setBackground(Color.orange);
		
		leftPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());
		rightPanel = new JPanel(new BorderLayout());
		
		
		
		

		 
		
		 add(leftPanel, BorderLayout.WEST);
		 add(centerPanel, BorderLayout.CENTER);
		 add(rightPanel, BorderLayout.EAST);
		 
		 var resetButton = new JButton("New Game");
		 

		JPanel leftContents = new JPanel(new BorderLayout());
		JPanel centerContents = new JPanel(new BorderLayout());
		JPanel rightContents = new JPanel(new BorderLayout());
		
		
		JPanel leftTop = new JPanel();
		JPanel leftBottom = new JPanel();
		
		leftContents.add(leftTop, BorderLayout.NORTH);
		leftContents.add(leftBottom, BorderLayout.SOUTH);
		
		
		JPanel centerTop = new JPanel();
		JPanel centerMiddle = new JPanel();
		JPanel centerBottom = new JPanel();
		
		centerContents.add(centerTop, BorderLayout.NORTH);
		centerContents.add(centerMiddle, BorderLayout.CENTER);
		centerContents.add(centerBottom, BorderLayout.SOUTH);
		
		
		
		

		JPanel rightTop = new JPanel();
		JPanel rightBottom = new JPanel();
		
		rightContents.add(rightTop, BorderLayout.NORTH);
		rightContents.add(rightBottom, BorderLayout.SOUTH);
		
		
//		leftContents.setLayout(new GridLayout(2, 1));
//		buttonPanel.setLayout(new GridLayout(2, 1));
//		rightContents.setLayout(new GridLayout(2, 1));
			
		 
		 JLabel p1Label = new JLabel("Player 1's score:");
		  p1Score = new JLabel("" + claimedOne);
		 
		 JLabel p2Label = new JLabel("Player 2's score:");
		  p2Score = new JLabel("" + claimedTwo);
		  
		 turnLabel = new JLabel("Player 1's turn");
		 
		 feedbackLabel = new JLabel("");
		 compToggle = new JToggleButton("Play Computer", false);
		 
		 //First layout
//		 leftContents.add(p1Label);
//		 leftContents.add(p1Score);
//		
//		 
//		 centerTop.add(resetButton);
//		 centerMiddle.add(turnLabel);
//		 centerBottom.add(feedbackLabel);
//		 
//		 rightContents.add(p2Label);
//		 rightContents.add(p2Score);
		 
		 //Second layout
		 leftTop.add(resetButton);
		 leftBottom.add(compToggle);
		
		 
		 centerTop.add(turnLabel);
		 centerBottom.add(feedbackLabel);
		 
		 rightTop.add(p1Label);
		 rightTop.add(p1Score);
		 rightBottom.add(p2Label);
		 rightBottom.add(p2Score);
		 
		 
		 
		 leftPanel.add(leftContents);
		 centerPanel.add(centerContents);
		 rightPanel.add(rightContents);
		 
		 var resetAction = new ResetAction();
		
		 resetButton.addActionListener(resetAction);


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
		
		if(turn ==1) {
			turnLabel.setText("Player 1's turn");
		} else {
			turnLabel.setText("Player 2's turn");
		}
		
		//?? Reset info label here?
	}
	
	public void newGame() {
		board.restart();
		turn = 1;
		claimedOne = 0;
		claimedTwo = 0;
		
		p1Score.setText("" + 0);
		p2Score.setText("" + 0);
		turnLabel.setText("Player 1's turn");
		
	}
	
	public void showWinner(int player) {
		turnLabel.setText("Game over! Player " + player + " wins!");
	}
	
	public void clearMessage() {
		feedbackLabel.setText("");
	}
	
	public void updateMessage(String text) {
		feedbackLabel.setText(text);
	}
	

	private class ResetAction implements ActionListener {
		
		public ResetAction() {
			
		}
		
		public void actionPerformed(ActionEvent event) {
//			System.out.println("Click");
			newGame();
		}
		
	}

}
