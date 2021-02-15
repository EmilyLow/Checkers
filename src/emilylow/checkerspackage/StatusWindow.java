package emilylow.checkerspackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
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
	
	private JPanel leftComp;
	private JPanel centerComp;
	private JPanel rightComp;
	
	
	StatusWindow(){
		claimedOne = 0;
		claimedTwo = 0;
		

		 var button = new JButton("New Game");
		
		this.add(button);
		
//		 setMaximumSize(new Dimension(1000, 200));
		 setOpaque(true);
		 setBackground(Color.red); 
		 
		
		//One attempt
//		leftComp = new LeftPanel();
//		centerComp = new CenterPanel();
//		rightComp = new RightPanel();
//		
//		this.add(leftComp, BorderLayout.WEST);
//		this.add(centerComp, BorderLayout.CENTER);
//		this.add(rightComp, BorderLayout.EAST);
//		
//		
//		
//		 var button = new JButton("New Game");
//		 
//		 centerComp.add(button);
//		 
//		 this.setOpaque(true);
//			this.setBackground(new Color(224, 224, 224));

	}
	
	public void paintComponent(Graphics g) {
		
	      
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setColor(Color.blue);
		
		g2.fillOval(100, 50, 40, 40);
		
		g2.setColor(Color.white);
		
		g2.drawString("0", 120, 70);
		
		
		g2.setColor(Color.red);
		
		g2.fillOval(700, 50, 40, 40);
		
		g2.setColor(Color.white);
		
		g2.drawString("0", 720, 70);
		
		
//		
//		//Basic version
//		g.drawString("Player " + turn + "'s turn", MESSAGE_X, MESSAGE_Y);
//	      g.drawString("Pieces won:", MESSAGE_X + 200, MESSAGE_Y -40);
//	      g.drawString("Player 1: " + board.getPlayerOneTotal(), MESSAGE_X + 200, MESSAGE_Y -20);
//	      g.drawString("Player 2: " + board.getPlayerTwoTotal(), MESSAGE_X + 200, MESSAGE_Y );
//	      
	      
	      //In progress
		
	   
	    
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
