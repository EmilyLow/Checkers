package emilylow.checkerspackage;


import java.util.ArrayList;
import java.util.Arrays;

public class MockBoard extends Board {
	


	/*
	 * [[x, y], [x2, y2]
	 *  x = [0][0] y = [0][1] x2 = [1][0] y2 = [1][1]
	 */
	int[][] chosenMove;
	int iter;

	public MockBoard(StatusWindow statusWindow) {
		super(statusWindow);
		
	}


	public MockBoard(Square[][] squares, Square selected, int turn, int oneTotal, int twoTotal, int oneKingTotal, int twoKingTotal, boolean compOn, boolean extendedJump, int iter) {
		super(squares, selected, turn, oneTotal, twoTotal, oneKingTotal, twoKingTotal, compOn, extendedJump);
		
		this.iter = iter;
	
	}
	
	public int findBestMove() {

		ArrayList<int[]> startValues = new ArrayList<int[]>();
		
		Square[][] squares = super.getSquares();
		int[][] bestMove = new int[2][2];
		boolean baselineFound = false;

		int bestScore = 0; //Resets when baseline (first available move) is found.
		
		
			
			if(getExtendedJump()) {
				
				startValues.add(getSelected().getCoord());
				
			} else {
				for (int y = 0; y < squares.length; y++) {
					for(int x = 0; x < squares[0].length; x++) {
						
						Square currentSquare = squares[x][y];
						startValues.add(currentSquare.getCoord());
						
					}
				}
			}
			
			for(int[] startCoord : startValues) {
				int[][] endCoords = getActionCoords(startCoord);
				
				for(int[] endCoord: endCoords) {

					if(allowedAction(startCoord, endCoord)) {

						MockBoard iterBoard;
						int foundScore;
						
						if(getExtendedJump()) {
							iterBoard = makeMockBoard(iter);
						} else {
							iterBoard = makeMockBoard(iter-1);
						}
						
						iterBoard.takeAction(startCoord, endCoord);
						
						
						

						if(iter > 0) {
							foundScore = iterBoard.findBestMove();
						} else {
							foundScore = getBoardScore();
						}
						
						if(!baselineFound) {
							bestScore = foundScore;
							bestMove[0] = startCoord.clone();
							bestMove[1] = endCoord.clone();
							baselineFound = true;
						}
						

						if(getTurn() == 2 && foundScore > bestScore) {
							bestScore = foundScore;
							bestMove[0] = startCoord.clone();
							bestMove[1] = endCoord.clone();

						} else if(getTurn() == 1 && foundScore < bestScore) {
							bestScore = foundScore;
							bestMove[0] = startCoord.clone();
							bestMove[1] = endCoord.clone();
						}
						
						
					}
					
					
				}
			}
			
			chosenMove = bestMove.clone();

			return bestScore;
			
		}
		
	
	private int[][] getActionCoords(int[] startCoord) {
		

		int x = startCoord[0];
		int y = startCoord[1];
		
		int[][]  coords = {{x-2, y-2}, {x+2, y-2}, {x-2, y+2}, {x+2, y+2}, {x-1, y-1}, {x+1, y-1}, {x-1, y+1}, {x+1, y+1}};
		
		return coords;
	}
	
	public int[][] getChosenMove() {
		return chosenMove;
	}

}
