package emilylow.checkerspackage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class Board extends javax.swing.JComponent {
	
	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 1000;
	
	private StatusWindow window;
	private boolean pvp;
	private Square selected; 
	//Either 1 or 2
	private int turn;
	
	Board(StatusWindow statusWindow) {
		
		//!! This feels like potentially bad practice?
		window = statusWindow;
		window.setBoard(this);
		//This is temporary, before Computer is implemented
		pvp = false;
		
		selected = null; 
		turn = 1;
		
	}
	
	//!Look at the details here later
	public Dimension getPreferredSize() {
		
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public void PaintComponent(Graphics g) {
		
	}
	
	public void setUpBoard() {
		
	}
	
	public void restart() {
		
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int[] drawMath() {
		
		return null;
	}
	
	public void attemptMove() {
		
	}
	
	public boolean validMove() {
		
		return false;
	}
	
	public void nextTurn() {
		
	}
	
	public boolean checkWin() {
		return false;
	}
	
	//Check if this should be using an existing copy method
	public Board deepCopy() {
		
		return null;
	}
	private class ClickHandler extends MouseAdapter 
	{
		public void mousePressed(MouseEvent event)
		{
			
		}
		
		public int[] convertCoord() {
			
			return null;
		}
	}
}
