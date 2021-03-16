package emilylow.checkerspackage;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class MockBoard extends Board {
	

	int pathScore;
	/*
	 * [[x, y], [x2, y2]
	 *  x = [0][0] y = [0][1] x2 = [1][0] y2 = [1][1]
	 */
	int[][] bestMove;
	int count;

	public MockBoard(StatusWindow statusWindow) {
		super(statusWindow);
		// TODO Auto-generated constructor stub
	}

	public MockBoard(Square[][] squares, int turn, int oneTotal, int twoTotal, int count) {
		super(squares, turn, oneTotal, twoTotal);
		// TODO Auto-generated constructor stub
		
	}
	
	
	//Start as finding one move ahead
	//Note! At some point, add edge case for no move found
	public int[][] findBestMove() {
		
		Square[][] squares = super.getSquares();
		int[][] bestMove = new int[2][2];
		int bestScore = -1; //Star int negative to account for the possibility of no good moves
		
		//TEMP
		boolean desFound = false;
		
		for (int y = 0; y < squares.length; y++) {
			for(int x = 0; x < squares[0].length; x++) {
				Square currentSquare = squares[x][y];
				
				if(allowedSelect(currentSquare)) {
					
					//To start with, if move is possible, return move. No recursion yet
					//Temporarily use break for this?
					
					
					int[] sqCoord = currentSquare.getCoord();
					//Check each potential jump
					int[][] potentialJumps = getJumpCoords(sqCoord);
					for(int i = 0; i < potentialJumps.length; i++) {
//						if(allowedJump(potentialJumps[i])) {
//							//!! Temp, make the jump
//							bestMove[0] = sqCoord.clone();
//							bestMove[1] = potentialJumps[i].clone();
//							desFound = true;
//							break;
//						}
					}
					
					//Check each potential move
					int[][] potentialMoves = getMoveCoords(sqCoord);
					for(int i = 0; i < potentialMoves.length; i++) {
//						if(allowedMove(potentialMoves[i])) {
//							//!! Temp, make the jump
//							bestMove[0] = sqCoord.clone();
//							bestMove[1] = potentialJumps[i].clone();
//							desFound = true;
//							break;
//						}
					}
				
					
					
					
					
				}
				
				//Temp!!
				if(desFound) {
					break;
				}
				
			}
			//Temp!!
			if(desFound) {
				break;
			}
		}
		
		System.out.println("Possible move found");
		System.out.println(Arrays.deepToString(bestMove));
		
		return bestMove;
	}
	
	
	
	/*
	 * Finds tokens that can potentially be selected to make a move
	 */
	//!! Should this be a parent method?
	public boolean allowedSelect(Square sq) {
		
		if(sq.getActive() && sq.hasToken() && sq.getPlayer() == super.getTurn() ) {
			return true;
		} else {
			return false;
		
		}
		
	}
	
	/*
	 * Returns the four potential move destinations for a coord.
	 * Destinations may be out of bounds. This is checked in validMove()
	 */
	public int[][] getMoveCoords(int[] startCoord) {
		
		int x = startCoord[0];
		int y = startCoord[1];
		
		
		int[][] moveCoords = {{x-1, y-1}, {x+1, y-1}, {x-1, y+1}, {x+1, y+1}};
		
		//Check
		System.out.println(Arrays.deepToString(moveCoords));
		
		
		return moveCoords;
	}
	
	/*
	 * Calculates four potential jump destinations. 
	 * They may be out of bounds. This is checked in validJump();
	 *  !! Not implemented
	 */
	public int[][] getJumpCoords(int[] startCoord) {
		
		
		
		int x = startCoord[0];
		int y = startCoord[1];
		
		int[][] jumpCoords = {{x-2, y-2}, {x+2, y-2}, {x-2, y+2}, {x+2, y+2}};
		
		return jumpCoords;
	}
	
	//I'm torn about whether the score should be statically derivable from a board layout, and not based off movement.
	//Statically derivable works easily for number of claimed tokens and number of kings
	//But works less well for advancing pieces forward, which is necessary.
	//Checking for number of claimed tokens is easy, but checking for moving forward based off the board itself requires iterating over the whole thing.
	//If instead I add to the score based off an action (moving a non-king = 1), I don't have to iterate again.
	//Bus is iterating over teh board one more time a big deal?
	
	public int getBoardScore() {
		
		
		return 0;
	}

}
