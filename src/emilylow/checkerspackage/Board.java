package emilylow.checkerspackage;


import java.awt.geom.Rectangle2D;


public class Board {

	private static final int SIDE_LENGTH = 100;
	private static final int OFFSET = 50;


	private Square[][] squares;
	private StatusWindow window;
	private boolean compOn;
	private Square selected;
	
	/* Designates whether it is player 1 or player 2's turn */
	private int turn;
	
	/* Boolean for computer's turn. Computer is always player 2. */
	private boolean compTurn;
	private boolean extendedJump;
	
	private int oneTotal;
	private int twoTotal;
	private int oneKingTotal;
	private int twoKingTotal;

	private boolean gameOver;

	private Display display;
	private CompPlayer compPlayer;


	

	Board(StatusWindow statusWindow) {

	

		
		compOn = false;
		compTurn = false;
		
		extendedJump = false;

		selected = null;
		turn = 1;
		squares = new Square[8][8];
		oneTotal = 0;
		twoTotal = 0;
		oneKingTotal = 0;
		twoKingTotal = 0;

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
	 * @param selected, can be null
	 */
	Board(Square[][] squares, Square selected, int turn, int oneTotal, int twoTotal, int oneKingTotal, int twoKingTotal, boolean compOn, boolean extendedJump) {

		// !!! Temp
		//Is this causing errors?
//		compOn = false;
		
		//Shouldn't this always be true for a mockboard?
		//Or does it not matter?
		this.compOn = compOn;

		// !!! Consider if it should be able to start a board with a pre-selected piece
		this.selected = selected;

		this.turn = turn;
		this.squares = squares;
		this.oneTotal = oneTotal;
		this.twoTotal = twoTotal;
		this.oneKingTotal = oneKingTotal;
		this.twoKingTotal = twoKingTotal; 
		this.extendedJump = extendedJump;

		gameOver = false;

		window = null;
		display = null;
		//Test adding
		compPlayer = null;

	}

	
	public MockBoard makeMockBoard(int iter) {
		
		Square[][]  copySquares = copySquares(squares);
		Square copySelected;
		if(selected != null) {
			 int[] coord = selected.getCoord();
			 copySelected = copySquares[coord[0]][coord[1]];
		} else {
			copySelected = null;
		}
		
		

		MockBoard mockBoard = new MockBoard(copySquares(squares), copySelected, turn, oneTotal, twoTotal, oneKingTotal, twoKingTotal, compOn, extendedJump, iter);

		return mockBoard;
	}

	private Square[][] copySquares(Square[][] startSquares) {
		Square[][] newSquares = new Square[8][8];

		for (int y = 0; y < startSquares.length; y++) {
			for (int x = 0; x < startSquares.length; x++) {
				newSquares[x][y] = startSquares[x][y].copySquare();
			}
		}

		return newSquares;
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

	private void nextTurn() {
		if (!checkWin()) {
			
			if(extendedJump) {
				if(turn == 2 && compOn) {
					
					compTurn = true;
					if(compPlayer != null) {
						System.out.println("Turn triggered");
						compPlayer.triggerTurn();
						
					}
					
				}
				
				
			} else {
				if (turn == 1) {
					turn = 2;
					if (compOn) {
						compTurn = true;
						
						// Alerts computer to take turn
						if(compPlayer != null) {
							compPlayer.triggerTurn();
						}

					}
				} else {
					turn = 1;
					if (compOn) {
						compTurn = false;
					}
				}
				
				
			}
			if(window != null) {
				window.setTurn(turn, compTurn);
			}
			
		}
		
		
		
		
	}


	
	public boolean checkWin() {
		if (oneTotal == 12) {
			if(window != null) {
				window.showWinner(1);
			}
			
		
			gameOver = true;
			return true;
		} else if (twoTotal == 12) {
			if(window != null) {
				window.showWinner(2);
			}
			
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
		} else { // turn == 2

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
	
private int[][] getJumpCoords(int[] startCoord) {
		
		
		
		int x = startCoord[0];
		int y = startCoord[1];
		
		int[][] jumpCoords = {{x-2, y-2}, {x+2, y-2}, {x-2, y+2}, {x+2, y+2}};
		
		return jumpCoords;
	}


	private void attemptKing(Square potQ) {
		int[] coord = potQ.getCoord();

		if (coord[1] == 0 && potQ.getPlayer() == 1) {
			potQ.makeKing();
			oneKingTotal++;
		} else if (coord[1] == (squares.length - 1) && potQ.getPlayer() == 2) {
			potQ.makeKing();
			twoKingTotal++;
		} // else, not a king so do nothing
	}

	/*
	 * Called when human player makes an action, to determine what type of action
	 * was made. Comp player calls this also when making final decided move.
	 */
	//TODO More complicated clean up. 
	public void attemptAction(int[] coord) {
		
	
		if (gameOver) {

			if (window != null) {
				window.updateMessage("Game over. Start new game.");
			}

	
		} else {

			if (window != null) {
				window.clearMessage();
			}

			if (coord != null) {

				
				if (allowedSelect(coord)) {
					select(coord);
				} else if(allowedDeselect(coord)) {
					deselect();
				} else if (selected != null) {
					
					if (allowedMove(selected.getCoord(), coord)) {
						move(coord);

					} else if (allowedJump(selected.getCoord(), coord)) {
						jump(coord);

						
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
	
	public boolean allowedAction(int[] startCoord, int[] endCoord) {

		
		if(!outOfBounds(startCoord) && !outOfBounds(endCoord)) {
			if(allowedSelect(startCoord) || extendedJump) {

				if(allowedJump(startCoord, endCoord)) {
					return true;
				} else if (allowedMove(startCoord, endCoord)) {
					return true;
				} else {

					return false;
				}
				
				
			} else {
				return false;
			}
		} else {

			return false;
		}
		
	}
	
	public void takeAction(int[] startCoord, int[] endCoord) {
		if(extendedJump) {
			jump(endCoord);
		} else {
			selected = squares[startCoord[0]][startCoord[1]];
			if(allowedJump(startCoord, endCoord)) {
				jump(endCoord);
			} else if (allowedMove(startCoord, endCoord)) {
				move(endCoord);
			} else {
				System.out.println("Bug in takeAction(). Neither jump or move.");
			}
		}
	}

	/*
	 * Tests if move if permissible, without considering other pieces on the board.
	 */
	private boolean validMove(int[] start, int[] end, boolean king) {
	
		//Points are broken down into individual ints to make them easier to work with.
		int startX = start[0];
		int startY = start[1];
	
		int endX = end[0];
		int endY = end[1];
	
		if (!outOfBounds(end)) {
	
			boolean direction = allowedDirection(start, end, king);
	
		
	
			boolean adjacency;
			if (Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1) {
				adjacency = true;
			} else {
				adjacency = false;
			}
	
			return direction && adjacency;
	
		} else {
			

			return false;
		}
	}


	private boolean allowedMove(int[] startCoord, int[] endCoord) {

		if(extendedJump) {
			return false;
		} else {
			
			Square startSquare = squares[startCoord[0]][startCoord[1]];
			
			boolean validMove = validMove(startCoord, endCoord, startSquare.getKing());
			
			if(validMove) {

				Square destSquare = squares[endCoord[0]][endCoord[1]];
				if (destSquare.getActive() && !destSquare.hasToken()) {
					
					return true;
		
				} 	else {
		
				return false;
				}
				
			} else {
				return false;
			}	
			
			
		}
	
		
		
	}

	private void move(int[] endCoord) {
		
		Square destSquare = squares[endCoord[0]][endCoord[1]];
		int tokenOwner = selected.getPlayer();
		
		// Informs destination it has token
		destSquare.placeToken(tokenOwner);

		if (selected.getKing()) {
			destSquare.makeKing();
		} else {
			attemptKing(destSquare);
			
		}
		
		// Informs selected it is empty
		selected.clear();
		selected = null;
		
		nextTurn();
		
	}

	private boolean validJump(int[] start, int[] end, boolean king) {
		
		// Breaking points into individual ints so they're easier to work with
		int startX = start[0];
		int startY = start[1];
	
		int endX = end[0];
		int endY = end[1];
	
		if (!outOfBounds(end)) {
	
			boolean direction = allowedDirection(start, end, king);
	
			// Checks for + 1 adjacency
			boolean adjacencyPlus;
			if (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 2) {
				adjacencyPlus = true;
			} else {
				adjacencyPlus = false;
			}
	
			return direction && adjacencyPlus;
	
		} else {
			
			return false;
		}
		
	}

	private boolean allowedJump(int[] startCoord, int[] endCoord) {

			Square startSquare = squares[startCoord[0]][startCoord[1]];

	
			if (validJump(startCoord, endCoord, startSquare.getKing())) {
				
				
				//Finds coordinates of the square being jumped over
				int[] btCoord = findBtCoord(startCoord, endCoord);

				Square btSquare = squares[btCoord[0]][btCoord[1]];
				Square endSquare = squares[endCoord[0]][endCoord[1]];

				if (btSquare.hasToken() && btSquare.getPlayer() != startSquare.getPlayer() && !endSquare.hasToken()) {

					return true;

				} else {

					return false;
				}

			} else {

				return false;
			}

	}

	private void jump(int[] endCoord) {
		Square destSquare = squares[endCoord[0]][endCoord[1]];
		boolean jumpAgain = false;
		boolean madeKing = false;
		
		// Informs destination it has token
		destSquare.placeToken(turn);


		if (selected.getKing()) {
			destSquare.makeKing();
		} else {
			attemptKing(destSquare);
			if(destSquare.getKing()) {
				madeKing = true;
				
				
			}
		}

		
		int[] btCoord = findBtCoord(selected.getCoord(), endCoord);
		Square btSquare = squares[btCoord[0]][btCoord[1]];
		
		if(btSquare.getKing() ) {
			if(btSquare.getPlayer() == 1) {
				oneKingTotal--;
			} else {
				twoKingTotal--;
			}
		}
		
		// Informs between square that it is empty
		btSquare.clear();
		
		
		// Informs selected it is empty
			selected.clear();
			selected = null;
		
		//Marks destSquare as selected for use in checking for multi-jumps
		selected = destSquare;
		
		if(!madeKing) {
			int[][] potentialJumps = getJumpCoords(endCoord);
			for(int i = 0; i < potentialJumps.length; i++) {
				if(allowedJump(selected.getCoord(), potentialJumps[i])) {
					System.out.println("Jump again = true");
					jumpAgain = true;
					
					
				}
				
			}
			
			
			
		}		
		
		
	
			if (turn == 1) {
				oneTotal++;
			} else {
				twoTotal++;
			}
		
			if(jumpAgain) {
				extendedJump = true; 
				System.out.println("Extended jump triggered");
			} else {
				extendedJump = false;
			
				selected = null;
			}
				

	
		nextTurn();

	
	}


	private boolean allowedSelect(int[] coord) {
		Square chosen = squares[coord[0]][coord[1]];
		
		if (selected == null && chosen.hasToken() && chosen.getPlayer() == turn && !extendedJump) {
			return true;
		} else {
			return false;
		}
		
	}
	


	private void select(int[] coord) {
		Square chosen = squares[coord[0]][coord[1]];
		
		selected = chosen;
		
	}

	private boolean allowedDeselect(int[] coord) {

		if(selected != null && coord.equals(selected.getCoord()) && !extendedJump) {
			return true;
		} else {
			return false;
		}
	}

	private void deselect() {
		selected = null; 

	}

	public int getBoardScore() {
	
		int num = (twoTotal - oneTotal) + 3*(twoKingTotal - oneKingTotal); 
	
		return num;
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

	
	public void updateDisplay() {
		display.updateDisplay(selected, compTurn);
	}
	
	public boolean getExtendedJump() {
		return extendedJump;
	}
	
	public Square getSelected() {
		return selected; 
	}
}