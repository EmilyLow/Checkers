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
	
	
	StatusWindow(){
		claimedOne = 0;
		claimedTwo = 0;
		
		leftPanel = new JPanel();
		centerPanel = new JPanel();
		rightPanel = new JPanel();
		
		
		

		 
		
		 add(leftPanel, BorderLayout.WEST);
		 add(centerPanel, BorderLayout.CENTER);
		 add(rightPanel, BorderLayout.EAST);
		 
		 var button = new JButton("New Game");
		 
		 JLabel p1Label = new JLabel("Player 1's score:");
		 JLabel p1Score = new JLabel("" + claimedOne);
		 
		 JLabel p2Label = new JLabel("Player 2's score:");
		 JLabel p2Score = new JLabel("" + claimedTwo);
		 
		 leftPanel.add(p1Label);
		 leftPanel.add(p1Score);
		
		 
		 centerPanel.add(button);
		 
		 rightPanel.add(p2Label);
		 rightPanel.add(p2Score);
		 
		
		 


	}
	
//	public void paintComponent(Graphics g) {
//		
//		
//
//
//		
////		
////		//Basic version
////		g.drawString("Player " + turn + "'s turn", MESSAGE_X, MESSAGE_Y);
////	      g.drawString("Pieces won:", MESSAGE_X + 200, MESSAGE_Y -40);
////	      g.drawString("Player 1: " + board.getPlayerOneTotal(), MESSAGE_X + 200, MESSAGE_Y -20);
////	      g.drawString("Player 2: " + board.getPlayerTwoTotal(), MESSAGE_X + 200, MESSAGE_Y );
////	      
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
		repaint();
	}
	
	public void newGame() {
		
	}
	
	public void showWinner(int player) {
		
	}
	
	//I don't know if this is close even
	class LeftPanel extends JPanel {
		
		LeftPanel() {
			 setPreferredSize(new Dimension(100,100));
		}
		
		 @Override
	        public void paintComponent(Graphics g) {
			 Graphics2D g2 = (Graphics2D) g;
	            super.paintComponent(g);
	            this.setOpaque(true);
	    		this.setBackground(new Color(224, 224, 224));
	    		g2.drawOval(10, 10, 10, 10);

	          
	        }
		
	}
class CenterPanel extends JPanel {
		
		CenterPanel() {
			 setPreferredSize(new Dimension(100,100));
		}
		
		 @Override
	        public void paintComponent(Graphics g) {
			 Graphics2D g2 = (Graphics2D) g;
	            super.paintComponent(g);
	            this.setOpaque(true);
	    		this.setBackground(new Color(224, 224, 224));
	    		g2.drawOval(10, 10, 10, 10);

	          
	        }
		
	}
class RightPanel extends JPanel {
	
	RightPanel() {
		 setPreferredSize(new Dimension(100,100));
	}
	
	 @Override
        public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);
            this.setOpaque(true);
    		this.setBackground(new Color(224, 224, 224));
    		g2.drawOval(10, 10, 10, 10);

          
        }
	
}

}
