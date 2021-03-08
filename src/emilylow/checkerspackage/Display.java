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



public class Display extends javax.swing.JComponent {

	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 1000;
	
	//!!!Doubled, give one source of truth
	private static final int SIDE_LENGTH = 100;
	private static final int OFFSET = 50;
	private static final int TOKEN_SHRINK = 5;
	
	
	private Square[][] displaySquares;
	private Square selected; 
	
	private Board board;
	
	
	public Display(Square[][] squares, Square chosen, Board passedBoard) {
		
		
		board = passedBoard;
		displaySquares = squares;
		selected = chosen;
		
		addMouseListener(new ClickHandler());
		
		repaint();
		
	}
	
	
	public void updateDisplay(Square chosen) {

		selected = chosen;
		repaint();
	}
	
	public void newGame(Square[][] squares) {
		selected = null;
		displaySquares = squares;
		repaint();
	}

	public void paintComponent(Graphics g) {
		
		var g2 = (Graphics2D) g;
	
		
		
		for (int y = 0; y < displaySquares.length; y++) {
			for(int x = 0; x < displaySquares[0].length; x++) {

				Square drawSquare = displaySquares[x][y];

				g2.setColor(drawSquare.getSquareColor());
				g2.fill(drawSquare.getRect());
				
				if(drawSquare.hasToken()) {
//					
					g2.setColor(drawSquare.getTokenColor());
					g2.fill(drawPotentialToken(drawSquare));
					if(drawSquare.getKing()) {
						g2.setColor(Color.YELLOW);
						
					
						g2.fillPolygon(drawTriangle(drawSquare));
					}
				}
				
			}
		}
		
		if(selected != null) {
			g2.setColor(Color.orange);
			g2.fill(drawPotentialToken(selected));
			if(selected.getKing()) {
				g2.setColor(Color.white);
				g2.fillPolygon(drawTriangle(selected));
			}
		}
	}
	
	public Dimension getPreferredSize() {
		
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public Ellipse2D drawPotentialToken(Square sq) {

		int[] pixelPoint = convertGridtoPixel(sq.getCoord());
		
		Ellipse2D newToken = new Ellipse2D.Double(pixelPoint[0] + TOKEN_SHRINK/2, pixelPoint[1] + TOKEN_SHRINK/2 , SIDE_LENGTH - TOKEN_SHRINK, SIDE_LENGTH - TOKEN_SHRINK);
		
		return newToken;
	}
	
	public Polygon drawTriangle(Square sq) {
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

	private class ClickHandler extends MouseAdapter 
	{
		
		//TO DO: Override equals? 
		public void mousePressed(MouseEvent event)
		{
		
			board.reportClick(event);
			
			
		}
		
		
	}
	
	
	
}
