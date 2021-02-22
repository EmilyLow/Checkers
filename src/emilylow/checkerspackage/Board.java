package emilylow.checkerspackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

//Make more specific?
import java.lang.*; 



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
	private int oneTotal;
	private int twoTotal;
	
	private boolean gameOver;
	
	
	//TO DO: Figure out code to allow double/triple/ect. jumps

	//TO DO: Implement ability to autoskip if no move or deliberately skip turns
	//TO DO: Implement win and new game
	
	//TO DO: Add visual for winning pieces
	//TO DO: Make status window into visual interface
	Board(StatusWindow statusWindow) {
		
		
		
		
		//This is temporary, before Computer is implemented
		pvp = false;
		
		selected = null; 
		turn = 1;
		squares = new Square[8][8];
		oneTotal = 0;
		twoTotal = 0;
		
		//!! This feels like potentially bad practice?
		window = statusWindow;
		window.setBoard(this);
		gameOver = false;
		
		
		
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
					if(drawSquare.getKing()) {
						g2.setColor(Color.YELLOW);
						
					
						g2.fillPolygon(drawTriange(drawSquare));
					}
				}
				
			}
		}
		
		if(selected != null) {
			g2.setColor(Color.orange);
			g2.fill(drawPotentialToken(selected));
			if(selected.getKing()) {
				g2.setColor(Color.white);
				g2.fillPolygon(drawTriange(selected));
			}
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
	
	public Polygon drawTriange(Square sq) {
		int[] pixelPoint = convertGridtoPixel(sq.getCoord());
		//Separating so its not unwieldy
		int startX = pixelPoint[0];
		int startY = pixelPoint[1];
		
		
		int[] xVal = {startX + (SIDE_LENGTH/2), startX + (SIDE_LENGTH/3), startX + 2*(SIDE_LENGTH/3)};
		int[] yVal = {startY + (SIDE_LENGTH/3), startY + 2*(SIDE_LENGTH/3), startY + 2*(SIDE_LENGTH/3)};
		
		return new Polygon(xVal, yVal, 3);
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
		squares = new Square[8][8];
		turn = 1; 
		oneTotal = 0;
		twoTotal = 0;
		selected = null;
		setUpBoard();
		
		repaint();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int getPlayerOneTotal() {
		return oneTotal;
	}
	
	public int getPlayerTwoTotal() {
		return twoTotal;
	}
	
	public int[] drawMath() {
		
		return null;
	}
	
	public boolean allowedDirection(int[] start, int[] end, boolean king) {
	
		int startY = start[1];
		int endY = end[1];
		
		boolean direction = false;
		//Check for moving in correct direction
		if(king) {
			direction = true;
		} else if(turn == 1) {
			
			//Check that player 1 moves up the board
			if(endY < startY) {
				direction = true;
			}
			else {
				//Unnecessary, since already false, but for clarity
				direction = false;
			}
		} else  {//turn == 2 
			 
			//Check that player 2 moves down the board
			if (endY > startY) {
				direction = true;
			}
			else {
				direction = false;
			}
		}
		
		return direction;
	}
	
	
	
	public boolean validMove(int[] start, int[] end, boolean king) {
		
		//Breaking points into individual vars so they're easier to work with
		int startX = start[0];
		int startY = start[1];
		
		int endX = end[0];
		int endY = end[1];
		
		boolean direction = allowedDirection(start, end, king);
		
		//Check for adjacency
		//! Not 100% sure this is correct
		boolean adjacency;
		if(Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1) {
			adjacency = true;
		}
		else {
			adjacency = false;
		}
		
		
		
		return direction && adjacency;
	}
	
	public void nextTurn() {
		if (!checkWin()) {
			if(turn == 1) {
				turn = 2;
			} else {
				turn = 1;
			}
			window.setTurn(turn);
		}
		
	}
	//Is it bad practice to have a function that both returns a go-ahead for another function
	//while also changing the state of the program itself?
	public boolean checkWin() {
		if(oneTotal == 12) {
			window.showWinner(1);
			System.out.println("Player one wins!");
			//Redundant?
			gameOver = true;
			return true;
		} else if (twoTotal == 12) {
			window.showWinner(2);
			System.out.println("Player two wins!");
			gameOver = true;
			return true;
		} else {
			return false;
		}
		
	}
	
	//Check if this should be using an existing copy method
	public Board deepCopy() {
			
		return null;
	}
	
	public void attemptSelect(Point2D p) {
		Square clicked = findSquareAtPoint(p);
		
		if(clicked != null && clicked.hasToken()) {
			
			
			if(clicked.getPlayer() == turn) {
				selected = clicked;
			}
			else {
				window.updateMessage("That is not your token");
			}
		}
		
	}
	

	
	public void attemptMove(Square destSquare) {
		
			//Check that DESTINATION is an active square and empty
			//Do this earlier on? 
			
			if(destSquare.getActive() && destSquare.hasToken() == false) {
				
				if(validMove(selected.getCoord(), destSquare.getCoord(), selected.getKing())) {
					
					int currentPlayer = selected.getPlayer();
					
				
					
					
				
					
					//Inform destination it has token
					destSquare.placeToken(currentPlayer);
					
					//King checks
					
					if(selected.getKing()) {
						destSquare.makeKing();
					} else {
						attemptKing(destSquare);
					}
					
					//Inform selected it is empty
					selected.clear();
					
					
					//Change selected to null
					selected = null;
					
					
					//Go to next turn
					nextTurn();
					
					repaint();
					
				} else {
					attemptJump(destSquare);
				}
				
		
			} else {
				window.updateMessage("Invalid move");
			}
			
	
	}
	
	public void attemptJump(Square destSquare) {
		//Checks for single jump initially, with potential ability to iterate later
		
		
		int[] currentCoord = selected.getCoord();
		int[] destCoord = destSquare.getCoord();
		
		//Check direction
		if(allowedDirection(selected.getCoord(), destSquare.getCoord(), selected.getKing()) ) {
			
			//Abs value of difference needs to be two
			//If difference is 2, add or subtract one from x & y to get inbetween coord
			
			//Check if it is adjacent with one square inbetween
			if(Math.abs(currentCoord[0] - destCoord[0]) == 2 && Math.abs(currentCoord[1] - destCoord[1]) == 2 ) {
				
				int[] btCoord = new int[2];
						
				//Calculate inbetween coord
				if(currentCoord[0] > destCoord[0]) {
					btCoord[0] = currentCoord[0] -1;
				} else {
					btCoord[0] = currentCoord[0] + 1;
				}
				if(currentCoord[1] > destCoord[1]) {
					btCoord[1] = currentCoord[1] -1;
				} else {
					btCoord[1] = currentCoord[1] + 1;
				}
				
				Square btSquare = squares[btCoord[0]][btCoord[1]];
				
				if(btSquare.hasToken() && btSquare.getPlayer() != turn) {
					
					//Inform destination it has token
					destSquare.placeToken(turn);
					
					//King checks
					
					if(selected.getKing()) {
						destSquare.makeKing();
					} else {
						attemptKing(destSquare);
					}
					
					//Inform selected it is empty
					 selected.clear();
					 selected = null;
					 
					//Inform between square that it is empty
					btSquare.clear();
					
					//Update score
//					window.updateClaimed(turn);
					if(turn == 1) {
						oneTotal++;
					} else {
						twoTotal++;
					}
					
					nextTurn();
					
					repaint();
					
					
				} //Else, do nothing
				
				
				
			} else {
				//Else, do nothing. Do I want to return something for a failed move?
				window.updateMessage("Invalid move");
			}
			
			
			//? Should I mark a failed move somehow? Return success? 
			
			
		
			
			
			
		} else {
			//Else, do nothing
			window.updateMessage("Invalid move");
		}
		
		
		//Later, implement ability to check for another jump and decide if computer should auto double jump when possible.
		
	}
	
	public void attemptKing(Square potQ) {
		int[] coord = potQ.getCoord();
		
		if(coord[1] == 0 && potQ.getPlayer() == 1) {
			potQ.makeKing();
		} else if (coord[1] == (squares.length - 1) && potQ.getPlayer() == 2) {
			potQ.makeKing();
		} //else, not a king so do nothing
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
		
			if(gameOver) {
				window.updateMessage("Game over. Start new game.");
				
			} else {
				window.clearMessage();
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
						
					} else {
						window.updateMessage("Invalid destination");
					}
					
				}
				else {
					//Attempt to select token at clicked point
					attemptSelect(point);
					
					if(selected != null) {
						//Token selected, so repaint. 
//						System.out.println("selected");
						repaint();
					}
				}
			}
			
		}
		
		public int[] convertCoord() {
			
			return null;
		}
	}
}
