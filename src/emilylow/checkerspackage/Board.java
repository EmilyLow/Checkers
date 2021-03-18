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
	private boolean compOn;
	private Square selected;
	// Either 1 or 2
	private int turn;
	// Is this the right way to do this?
	private boolean compTurn;
	private int oneTotal;
	private int twoTotal;

	private boolean gameOver;

	private Display display;
	private CompPlayer compPlayer;

	// Possibly make the declaration of this more obvious, and not just implied by
	// if a statusWindow is given
	boolean primary;

	Board(StatusWindow statusWindow) {

		primary = true;

		// This is temporary, before Computer is implemented
		compOn = false;
		compTurn = false;

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
		compPlayer = new CompPlayer(this);

	}

	/*
	 * Clone constructor. Should this be done differently? Like, making and then
	 * setting after? Or passing in a single board and using get on all of its
	 * elements?
	 */
	Board(Square[][] squares, int turn, int oneTotal, int twoTotal) {
		primary = false;

		// !!! Temp
		compOn = false;

		// !!! Consider if it should be able to start a board with a pre-selected piece
		selected = null;

		this.turn = turn;
		this.squares = squares;
		this.oneTotal = oneTotal;
		this.twoTotal = twoTotal;

		gameOver = false;

		window = null;
		display = null;

	}

	public MockBoard makeMockBoard(int iter) {

		MockBoard mockBoard = new MockBoard(copySquares(squares), turn, oneTotal, twoTotal, iter);

		return mockBoard;
	}

	private Square[][] copySquares(Square[][] startSquares) {
		Square[][] newSquares = new Square[8][8];

		for (int y = 0; y < startSquares.length; y++) {
			for (int x = 0; x < startSquares.length; x++) {
				newSquares[x][y] = startSquares[x][y].copySquare();
			}
		}

		return null;
	}

	private void setUpBoard() {
		for (int y = 0; y < squares.length; y++) {
			for (int x = 0; x < squares[0].length; x++) {
				int[] gridCoord = { x, y };
				int[] pixelPoint = convertGridtoPixel(gridCoord);

				Rectangle2D.Double newRect = new Rectangle2D.Double(pixelPoint[0], pixelPoint[1], SIDE_LENGTH,
						SIDE_LENGTH);
				Square tempSquare = new Square(gridCoord, newRect);
				squares[x][y] = tempSquare;

			}
		}

	}

	private int[] convertGridtoPixel(int[] gridCoord) {
		int gridX = gridCoord[0];
		int gridY = gridCoord[1];

		int xPoint = SIDE_LENGTH * gridX + OFFSET;
		int yPoint = SIDE_LENGTH * gridY + OFFSET;

		int[] pixelPoint = { xPoint, yPoint };
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

	public void toggleCompOn() {
		compOn = !compOn;
		display.updateDisplay(selected, compTurn);
		restart();

	}

	public void nextTurn() {
		if (!checkWin()) {
			if (turn == 1) {
				turn = 2;
				if (compOn) {
					compTurn = true;
					// Alert computer to take turn

					compPlayer.triggerTurn();

				}
			} else {
				turn = 1;
				if (compOn) {
					compTurn = false;
				}
			}
			window.setTurn(turn, compTurn);
		}
		
		
	}

	// Is it bad practice to have a function that both returns a go-ahead for
	// another function
	// while also changing the state of the program itself?
	public boolean checkWin() {
		if (oneTotal == 12) {
			window.showWinner(1);
			System.out.println("Player one wins!");
			// Redundant?
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

	private boolean allowedDirection(int[] start, int[] end, boolean king) {

		int startY = start[1];
		int endY = end[1];

		boolean direction = false;
		// Check for moving in correct direction
		if (king) {
			direction = true;
		} else if (turn == 1) {

			// Check that player 1 moves up the board
			if (endY < startY) {
				direction = true;
			} else {
				// Unnecessary, since already false, but for clarity
				direction = false;
			}
		} else {// turn == 2

			// Check that player 2 moves down the board
			if (endY > startY) {
				direction = true;
			} else {
				direction = false;
			}
		}

		return direction;
	}
	
	private int[] findBtCoord(int[] start, int[] end) {
		int[] btCoord = new int[2];

		
		if (start[0] > end[0]) {
			btCoord[0] = start[0] - 1;
		} else {
			btCoord[0] = start[0] + 1;
		}
		if (start[1] > end[1]) {
			btCoord[1] = start[1] - 1;
		} else {
			btCoord[1] = start[1] + 1;
		}
		
		return btCoord;
	}

	private boolean outOfBounds(int[] coord) {

		if (0 <= coord[0] && coord[0] <= 7 && 0 <= coord[1] && coord[1] <= 7) {
			return false;
		} else {
			return true;
		}
	}

	//!! To do
	public boolean attemptDoubleJump() {
		// To attempt after jumping returns true
		return false;
	}

	public void attemptKing(Square potQ) {
		int[] coord = potQ.getCoord();

		if (coord[1] == 0 && potQ.getPlayer() == 1) {
			potQ.makeKing();
		} else if (coord[1] == (squares.length - 1) && potQ.getPlayer() == 2) {
			potQ.makeKing();
		} // else, not a king so do nothing
	}

	/*
	 * Called when human player makes an action, to determine what type of action
	 * was made. Comp player calls this also when making final decided move.
	 */
	//!! Note, may be able to edit out check for window existing, but possibly no harm to leaving in?
	public void attemptAction(int[] coord) {

		// May need to change how gameover works.
		if (gameOver) {

			if (window != null) {
				window.updateMessage("Game over. Start new game.");
			}

			// ! return false;
		} else {

			if (window != null) {
				window.clearMessage();
			}
			// !! Null check currently exists for StatusWindow. May remove
			if (coord != null) {

				
				if (allowedSelect(coord)) {
					select(coord);
				} else if(allowedDeselect(coord)) {
					deselect();
				} else if (selected != null) {
					
					if (allowedMove(coord)) {
						move(coord);
						nextTurn();
					} else if (allowedJump(coord)) {
						jump(coord);
						nextTurn();
						
					} else {
						if (window != null) {
							window.updateMessage("Invalid move");
						}
					}
					
				} else {
					if (window != null) {
						window.updateMessage("Invalid selection");
					}		
					
				}
			}
			if (display != null) {
				display.updateDisplay(selected, compTurn);
			}
		}
		
					
	}

	/*
	 * Tests if move if permissible, without considering other pieces on the board.
	 */
	private boolean validMove(int[] start, int[] end, boolean king) {
	
		// Breaking points into individual vars so they're easier to work with
		int startX = start[0];
		int startY = start[1];
	
		int endX = end[0];
		int endY = end[1];
	
		if (!outOfBounds(end)) {
	
			boolean direction = allowedDirection(start, end, king);
	
			// Check for adjacency
	
			boolean adjacency;
			if (Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1) {
				adjacency = true;
			} else {
				adjacency = false;
			}
	
			return direction && adjacency;
	
		} else {
			// Move endpoint is out of bounds, return false.
			return false;
		}
	}


	public boolean allowedMove(int[] endCoord) {

		Square destSquare = squares[endCoord[0]][endCoord[1]];
		
		boolean validMove = validMove(selected.getCoord(), endCoord, selected.getKing());
		
		if (destSquare.getActive() && !destSquare.hasToken() && validMove) {
	
				return true;
	
		} else {
	
			return false;
		}
	}

	public void move(int[] endCoord) {
		
		Square destSquare = squares[endCoord[0]][endCoord[1]];
		int tokenOwner = selected.getPlayer();
		
		// Inform destination it has token
		destSquare.placeToken(tokenOwner);

		if (selected.getKing()) {
			destSquare.makeKing();
		} else {
			attemptKing(destSquare);
		}
		
		// Inform selected it is empty
		selected.clear();

		// Change selected to null
		selected = null;
		
	}

	private boolean validJump(int[] start, int[] end, boolean king) {
		// Breaking points into individual vars so they're easier to work with
		int startX = start[0];
		int startY = start[1];
	
		int endX = end[0];
		int endY = end[1];
	
		if (!outOfBounds(end)) {
	
			boolean direction = allowedDirection(start, end, king);
	
			// Check for + 1 adjacency
			boolean adjacencyPlus;
			if (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 2) {
				adjacencyPlus = true;
			} else {
				adjacencyPlus = false;
			}
	
			return direction && adjacencyPlus;
	
		} else {
			// Endpoint is out of bounds, return false.
			return false;
		}
		
	}

	public boolean allowedJump(int[] endCoord) {
		// Checks for single jump initially, with potential ability to iterate later
		
		int[] startCoord = selected.getCoord();
		

	
			if (validJump(startCoord, endCoord, selected.getKing())) {

				//Find coordinates of the square being jumped over
				int[] btCoord = findBtCoord(startCoord, endCoord);

				Square btSquare = squares[btCoord[0]][btCoord[1]];

				if (btSquare.hasToken() && btSquare.getPlayer() != selected.getPlayer()) {

					return true;

				} else {

					return false;
				}

			} else {

				return false;
			}

	}

	public void jump(int[] endCoord) {
		Square destSquare = squares[endCoord[0]][endCoord[1]];
		
		// Inform destination it has token
		destSquare.placeToken(turn);


		if (selected.getKing()) {
			destSquare.makeKing();
		} else {
			attemptKing(destSquare);
		}

		
		int[] btCoord = findBtCoord(selected.getCoord(), endCoord);
		Square btSquare = squares[btCoord[0]][btCoord[1]];
		
		// Inform between square that it is empty
		btSquare.clear();
		
		// Inform selected it is empty
			selected.clear();
			selected = null;
				

		// Update score
		if (turn == 1) {
			oneTotal++;
		} else {
			twoTotal++;
		}

//		nextTurn();
		
	
	}

	// Note: Added null check for selected while re-factoring.
	// ! Need to remove null check from attemptAction()
	public boolean allowedSelect(int[] coord) {
		Square chosen = squares[coord[0]][coord[1]];
		
		if (selected == null && chosen.hasToken() && chosen.getPlayer() == turn) {
			return true;
		} else {
			return false;
		}
		
	}
	


	public void select(int[] coord) {
		Square chosen = squares[coord[0]][coord[1]];
		
		selected = chosen;
		
	}

	public boolean allowedDeselect(int[] coord) {

		if(selected != null && coord.equals(selected.getCoord())) {
			return true;
		} else {
			return false;
		}
	}

	public void deselect() {
		selected = null; 

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

	public boolean getCompOn() {
		return compOn;
	}

	public Square[][] getSquares() {
		return squares;
	}

	//Is this bad design?
	
	public void updateDisplay() {
		display.updateDisplay(selected, compTurn);
	}
}