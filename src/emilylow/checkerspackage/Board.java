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



public class Board {
	
	
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
	
	private Display display;
	
	


	
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

		
		display = new Display(squares, selected, this);
		
	}
	
	public Display getDisplay() {
		return display;
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
		
		display.updateDisplay(squares, selected);
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
					
					//!! Change to return true and have calling function call move
					//!! Or something
//					repaint();
					display.updateDisplay(squares, selected);
					
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
					
					//!!Make return true and have calling function take action (I think)
					display.updateDisplay(squares, selected);
					
//					repaint();
					
					
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
	
	public void reportClick(MouseEvent event) {
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

						//!!Edit what this does, where logic is stored
						display.updateDisplay(squares, selected);
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


					
					//!!Edit what this does, where logic is stored
				
					display.updateDisplay(squares, selected);
				}
			}
		}
	}
	
	


}
