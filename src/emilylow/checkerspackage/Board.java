package emilylow.checkerspackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;



public class Board extends javax.swing.JComponent {
	
	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 1000;
	
	private static final int SIDE_LENGTH = 100;
	private static final int OFFSET = 50;
	private static final int TOKEN_SHRINK = 5;
	
	
	private Square[][] squares;
	private StatusWindow window;
	private boolean pvp;
	private Square selected; 
	//Either 1 or 2
	private int turn;
	
	Board(StatusWindow statusWindow) {
		
		
		
		
		//This is temporary, before Computer is implemented
		pvp = false;
		
		selected = null; 
		turn = 1;
		squares = new Square[8][8];
		
		//!! This feels like potentially bad practice?
		window = statusWindow;
		window.setBoard(this);
		
		
		
		setUpBoard();
		addMouseListener(new ClickHandler());
		repaint();
		
	}
	
	//!Look at the details here later;
	public Dimension getPreferredSize() {
		
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public void paintComponent(Graphics g) {
		
		var g2 = (Graphics2D) g;

		
		for (int y = 0; y < squares.length; y++) {
			for(int x = 0; x < squares[0].length; x++) {

				Square drawSquare = squares[x][y];

				g2.setColor(drawSquare.getSquareColor());
				g2.fill(drawSquare.getRect());
				
				if(drawSquare.hasToken()) {
//					
					g2.setColor(drawSquare.getTokenColor());
					g2.fill(drawPotentialToken(drawSquare));
				}
				
			}
		}
		
		if(selected != null) {
			g2.setColor(Color.orange);
			g2.fill(drawPotentialToken(selected));
		}
	}
	
	
	public void setUpBoard() {
		for (int y = 0; y < squares.length; y++) {
			for(int x = 0; x < squares[0].length; x++) {
				int[] gridCoord = {x, y};
				int[] pixelPoint = convertGridtoPixel(gridCoord);
				
				Rectangle2D.Double newRect = new Rectangle2D.Double(pixelPoint[0], pixelPoint[1], SIDE_LENGTH, SIDE_LENGTH);
				Square tempSquare = new Square(gridCoord, newRect);
				squares[x][y] = tempSquare;
				
			}
		}
	}
	//Generate token for given square if needed, run this on each to simplify draw code
	public Ellipse2D drawPotentialToken(Square sq) {

		int[] pixelPoint = convertGridtoPixel(sq.getCoord());
		
		Ellipse2D newToken = new Ellipse2D.Double(pixelPoint[0] + TOKEN_SHRINK/2, pixelPoint[1] + TOKEN_SHRINK/2 , SIDE_LENGTH - TOKEN_SHRINK, SIDE_LENGTH - TOKEN_SHRINK);
		
		return newToken;
	}
	
	public int[] convertGridtoPixel(int[] gridCoord) {
		int gridX = gridCoord[0];
		int gridY = gridCoord[1];
		
		int xPoint = SIDE_LENGTH * gridX + OFFSET;
		int yPoint = SIDE_LENGTH * gridY + OFFSET;
		
	
		int[] pixelPoint = {xPoint, yPoint};
		return pixelPoint;
	}
	
	public void restart() {
		
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int[] drawMath() {
		
		return null;
	}
	
	
	
	public boolean validMove() {
		
		return false;
	}
	
	public void nextTurn() {
		if(turn == 1) {
			turn = 2;
		} else {
			turn = 1;
		}
		window.setTurn(turn);
	}
	
	public boolean checkWin() {
		return false;
	}
	
	//Check if this should be using an existing copy method
	public Board deepCopy() {
			
		return null;
	}
	
	public void attemptSelect(Point2D p) {
		Square clicked = findSquareAtPoint(p);
		
		if(clicked != null && clicked.hasToken() && clicked.getPlayer() == turn) {
			selected = clicked;
		}
		
	}
	

	
	public void attemptMove(Square destSquare) {
		
			//Check that DESTINATION is an active square and empty
			//Do this earlier on? 
			
			if(destSquare.getActive() && destSquare.hasToken() == false) {
				
				
				//TO DO: Implement turns
				
				int currentPlayer = selected.getPlayer();
				
				//Inform selected it is empty
				selected.clear();
				
				//Inform destination it has token
				destSquare.placeToken(currentPlayer);
			
				//Change selected to null
				selected = null;
				
				//Go to next turn
				nextTurn();
				
				repaint();
				
			}
			
	
	}
	
	public Square findSquareAtPoint(Point2D point) {
		for(int y = 0; y < squares.length; y++) {
			for (int x = 0; x < squares[0].length; x++) {
				
				Square checking = squares[x][y];
				
				if(checking.getRect().contains(point)) return checking;
			}
		}
		return null;
		
	}
	
	
	private class ClickHandler extends MouseAdapter 
	{
		
		//TO DO: Override equals? 
		public void mousePressed(MouseEvent event)
		{
			Point2D point = event.getPoint();
			//Check if a token has already been selected
			if(selected != null) {
				//Find clicked token
				Square clicked = findSquareAtPoint(point);
				if (clicked != null) {
					
					//Check if they are deselecting current token
					if(clicked.getCoord().equals(selected.getCoord())) {
						//deselect
						selected = null;
						repaint();
					} else {
						attemptMove(clicked);
					}
					
				} //Else, invalid destination so do nothing
				
			}
			else {
				//Attempt to select token at clicked point
				attemptSelect(point);
				
				if(selected != null) {
					//Token selected, so repaint. 
					repaint();
				}
			}
		}
		
		public int[] convertCoord() {
			
			return null;
		}
	}
}
