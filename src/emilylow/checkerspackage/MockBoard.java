package emilylow.checkerspackage;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class MockBoard extends Board {
	

	int pathScore;
	/*
	 * [[x, y], [x2, y2]
	 *  x = [0][0] y = [0][1] x2 = [1][0] y2 = [1][1]
	 */
	int[][] chosenMove;
	int iter;

	public MockBoard(StatusWindow statusWindow) {
		super(statusWindow);
		// TODO Auto-generated constructor stub
	}

	public MockBoard(Square[][] squares, Square selected, int turn, int oneTotal, int twoTotal, boolean compOn, boolean extendedJump, int iter) {
		super(squares, selected, turn, oneTotal, twoTotal, compOn, extendedJump);
		// TODO Auto-generated constructor stub
		this.iter = iter;
	
	}
	
	
	//Start as finding one move ahead
	//Note! At some point, add edge case for no move found
	public int findBestMove() {
		System.out.println("findBestMove for player" + getTurn());
		System.out.println("Super Get extended jump: " + super.getExtendedJump());
		System.out.println(" Get extended jump: " + getExtendedJump());
		Square[][] squares = super.getSquares();
		int[][] bestMove = new int[2][2];
		
		
		int bestScore = -1000; //Star int negative to account for the possibility of no good moves
		
		//!!! Figure out iteration here in a minute
		if(getExtendedJump()) {
			bestMove = findExtendedJump();
			bestScore = 10;
		}
		
		for (int y = 0; y < squares.length; y++) {
			for(int x = 0; x < squares[0].length; x++) {
				
				Square currentSquare = squares[x][y];
				
				if(allowedSelect(currentSquare.getCoord())) {
//					System.out.println("Allowed in if");
					//Temp
					//To start with, if move is possible, return move. No recursion yet
					
					
					int[] sqCoord = currentSquare.getCoord();
					select(sqCoord);
					boolean startKing = currentSquare.getKing();
					
					
					//Check each potential jump for current piece
					int[][] potentialJumps = getJumpCoords(sqCoord);
					for(int i = 0; i < potentialJumps.length; i++) {
						if(allowedJump(potentialJumps[i])) {
							int currentScore = 5; 
							
							
							//ITERATION HERE
						
								MockBoard iterBoard = this.makeMockBoard(iter -1);
								iterBoard.jump(potentialJumps[i]);
								
								Square endedOn = getSquares()[potentialJumps[i][0]][potentialJumps[i][1]];
						
								//Check if jump made a king
								if(endedOn.getKing() && !startKing) {
									currentScore += 10;
								}
								
								//Check if jump won the game
								if(iterBoard.checkWin()) {
									currentScore += 100;
								}
								
								
								
							if (iter >= 0) {	
								iterBoard.toggleTurn();
								currentScore = currentScore - iterBoard.findBestMove();
							}
							
							if(currentScore > bestScore) {
								bestScore = currentScore;
								bestMove[0] = sqCoord.clone();
								bestMove[1] = potentialJumps[i].clone();
							}
							
						} 
					}
					
					//Check each potential move for current piece
					int[][] potentialMoves = getMoveCoords(sqCoord);
					for(int i = 0; i < potentialMoves.length; i++) {
						if(allowedMove(potentialMoves[i])) {
							//!! Temp, make the move
							int currentScore = 0; 
							if(!currentSquare.getKing()) {
								currentScore++; 
							}
							
							//ITERATION HERE
							
								MockBoard iterBoard = this.makeMockBoard(iter -1);
								iterBoard.move(potentialMoves[i]);
								
								Square endedOn = getSquares()[potentialMoves[i][0]][potentialMoves[i][1]];
								
								//Check if jump made a king
								if(endedOn.getKing() && !startKing) {
									currentScore += 10;
								}
								
								//Check if jump won the game
								if(iterBoard.checkWin()) {
									currentScore += 100;
								}
								
								
								
							if (iter >= 0) {
								iterBoard.toggleTurn();
								currentScore = currentScore - iterBoard.findBestMove();
							}
							
							
							if(currentScore > bestScore) {
								bestScore = currentScore;
								bestMove[0] = sqCoord.clone();
								bestMove[1] = potentialMoves[i].clone();
							}
							
							
						}
					}
				
					
					
					
					
				}
				
				//Right place?
				deselect();
			}
			
		}
		
		System.out.println("Best score: " + bestScore);
//		System.out.println("Possible move found");
//		System.out.println(Arrays.deepToString(bestMove));
		
		chosenMove = bestMove.clone();
		
		return bestScore;
	}
	
	private int[][]  findExtendedJump() {
		int[][] bestMove = new int[2][2];
		int bestScore = -1000;
		
		bestMove[0] = super.getSelected().getCoord();
		int[][] potentialJumps = getJumpCoords(bestMove[0]);
		for(int i = 0; i < potentialJumps.length; i++) {
			if(allowedJump(potentialJumps[i])) {
				int currentScore = 5; 
				
				if(currentScore > bestScore) {
					bestScore = currentScore;
					bestMove[1] = potentialJumps[i].clone();
				}
				
			} 
		}
		
		return bestMove;
	}
	
	
	
	/*
	 * Returns the four potential move destinations for a coord.
	 * Destinations may be out of bounds. This is checked in validMove()
	 */
	private int[][] getMoveCoords(int[] startCoord) {
		
		int x = startCoord[0];
		int y = startCoord[1];
		
		
		int[][] moveCoords = {{x-1, y-1}, {x+1, y-1}, {x-1, y+1}, {x+1, y+1}};
		
		//Check
//		System.out.println(Arrays.deepToString(moveCoords));
		
		
		return moveCoords;
	}
	
	/*
	 * Calculates four potential jump destinations. 
	 * They may be out of bounds. This is checked in validJump();
	 *  !! Not implemented
	 */
	private int[][] getJumpCoords(int[] startCoord) {
		
		
		
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
	
	public int[][] getChosenMove() {
		return chosenMove;
	}

}
