package emilylow.checkerspackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	JLabel p2Label;
	JLabel turnLabel;
	JLabel feedbackLabel;
	

	JCheckBox compCheck;
	

	
	StatusWindow(){
		claimedOne = 0;
		claimedTwo = 0;
		
		setLayout(new BorderLayout());
		

		
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
		
			
		 
		 JLabel p1Label = new JLabel("Player 1's score:");
		  p1Score = new JLabel("" + claimedOne);
		 
		 p2Label = new JLabel("Player 2's score:");
		  p2Score = new JLabel("" + claimedTwo);
		  
		 turnLabel = new JLabel("Player 1's turn");
		 
		 feedbackLabel = new JLabel("");

		 compCheck = new JCheckBox("Play Computer", false);
		
		 leftTop.add(resetButton);

		 leftBottom.add(compCheck);
		
		 
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
		 var playerAction = new PlayerAction();
		
		 resetButton.addActionListener(resetAction);

		 compCheck.addActionListener(playerAction);


	}
 

	 public Dimension getPreferredSize() 
	   {  
		 
	      return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); 
	   }  

	  
	
	public void setBoard(Board passedBoard) {
		board = passedBoard;
	
		turn = 1;
		pvp = false;
		
	}
	
	public void setTurn(int set, boolean compTurn) {
		turn = set;
//		
	
		claimedOne = board.getPlayerOneTotal();
		claimedTwo = board.getPlayerTwoTotal();
		
		p1Score.setText("" + claimedOne);
		p2Score.setText("" + claimedTwo);
		
		if(turn ==1) {
			turnLabel.setText("Player 1's turn");
		} else {
			if(compTurn) {
				turnLabel.setText("Computer's turn");
			} else {
				turnLabel.setText("Player 2's turn");
			}
			
		}
		
		
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
	
	public void compPlayerToggled() {
		board.toggleCompOn();
		turn = 1;
		claimedOne = 0;
		claimedTwo = 0;
		
		p1Score.setText("" + 0);
		p2Score.setText("" + 0);
		turnLabel.setText("Player 1's turn");
		
		if(board.getCompOn()) {
			p2Label.setText("Computer's score:");
		} else {
			p2Label.setText("Player 2's score:");
		}
		
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
			
			newGame();
		}
		
	}
	
private class PlayerAction implements ActionListener {
		
		public PlayerAction() {
			
		}
		
		public void actionPerformed(ActionEvent event) {

			compPlayerToggled();
			
		}
		
	}

}
