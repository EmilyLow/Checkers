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
	
	
	//Possibly make the declaration of this more obvious, and not just implied by if a statusWindow is given
	boolean primary;
	
	Board(StatusWindow statusWindow) {
		
		primary = true;
		
		
		//This is temporary, before Computer is implemented
		pvp = false;
		
		selected = null; 
		turn = 1;
		squares = new Square[8][8];
		oneTotal = 0;
		twoTotal = 0;
		
		window = statusWindow;
		window.setBoard(this);
		gameOver = false;
		
		setUpBoard();

		display = new Display(squares, selected, this);
		
	}
	
	/* Clone constructor. Should this be done differently? Like, making and then setting after? 
	 * Or passing in a single board and using get on all of its elements? */
	Board(Square[][] squares, int turn, int oneTotal, int twoTotal) {
		primary = false;
		
		//!!! Temp
		pvp = false;
		
		//!!! Consider if it should be able to start a board with a pre-selected piece
		selected = null;
	
		this.turn = turn;
		this.squares = squares;
		this.oneTotal = oneTotal;
		this.twoTotal = twoTotal;
		
		gameOver = false;
		
		window = null;
		display = null;
		
		
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
		
		display.newGame(squares);
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
	
	
	

	
	
	
	//!Rename?
	//Incomplete
	public boolean attemptGridSelect(int[] coord) {
		Square chosen = squares[coord[0]][coord[1]];
		
		if(chosen.hasToken()) {
			
			//Check if it needs to deselect(?)
			//But that might return weirdness if the computer 
			//is relying on getting a true from this in order to try next moves?
			// If selected == true, test deselect? 
			
			
			if(chosen.getPlayer() == turn) {
				selected = chosen;
				if(display != null) {
					display.updateDisplay(selected);
				}
				return true;
			} else {
				return false;
			}
			
		} else {
			return false;
		}
	}
	
	public boolean attemptDeselect(int[] coord) {
		
		if(coord.equals(selected.getCoord())) {
			
			selected = null;

			//!!Edit what this does, where logic is stored
			if(window != null) {
				display.updateDisplay(selected);
			}
			
			return true;
		} else {
			return false;
		}
	}

	
	
	public boolean attemptMove(int[] coord) {
		
			//Check that DESTINATION is an active square and empty
			//Do this earlier on? 
		
			Square destSquare = squares[coord[0]][coord[1]];
			
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
					if (display != null) {
						display.updateDisplay(selected);
					}
					
					return true;
					
				} else {
					return attemptJump(coord);
				}
				
		
			} else {
				
				if(window != null) {
					window.updateMessage("Invalid move");
				}
				
				return false;
			}
			
	
	}
	
	public boolean attemptJump(int[] coord) {
		//Checks for single jump initially, with potential ability to iterate later
		
		
		
		int[] currentCoord = selected.getCoord();
		int[] destCoord = coord;
		Square destSquare = squares[coord[0]][coord[1]];
		
		//Check direction
		if(allowedDirection(currentCoord, destCoord, selected.getKing()) ) {
			
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
					
					
					if(display!= null) {
						display.updateDisplay(selected);
					}

					return true;
					
				} else {
					//!! If there's an error it might be here
					return false;
				}
				
				
			} else {
				//Else, do nothing. Do I want to return something for a failed move?
				if (window != null) {
					window.updateMessage("Invalid move");
				}
				
				return false;
			}

			
		} else {
			
			if (window != null) {
				window.updateMessage("Invalid move");
			}
			
			return false;
		}
		
		
		
	}
	
	public boolean attemptDoubleJump() {
		//To attempt after jumping returns true
		return false;
	}
	
	public void attemptKing(Square potQ) {
		int[] coord = potQ.getCoord();
		
		if(coord[1] == 0 && potQ.getPlayer() == 1) {
			potQ.makeKing();
		} else if (coord[1] == (squares.length - 1) && potQ.getPlayer() == 2) {
			potQ.makeKing();
		} //else, not a king so do nothing
	}
	
	

	
	
	public boolean attemptAction(int[] coord) {
		
		//May need to change how gameover works. 
		if(gameOver) {
			
			if (window != null) {
				window.updateMessage("Game over. Start new game.");
			}
			
			return false;
		} else {
			
			if(window != null) {
				window.clearMessage();
			}
			//!! Null check currently exists for StatusWindow. May remove
			if(coord != null) {
				
				if(selected == null) {
				
					return attemptGridSelect(coord);
					
				} else {
					
					//? Bad form to change status in first part of if statement?
					if (attemptDeselect(coord)) {
						return true;
					} else {
						return attemptMove(coord);
					}
					
				}
				
				
			} else {
				if(window != null) {
					window.updateMessage("Invalid destination");
				}
				return false;
			}
		}
		
		
	}
	
	
	public Display getDisplay() {
		return display;
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


}
