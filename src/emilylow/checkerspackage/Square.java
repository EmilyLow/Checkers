package emilylow.checkerspackage;

import java.awt.geom.Rectangle2D;

public class Square {

	private static int[] coord;
	private boolean hasToken;
	private int player;
	private Rectangle2D rect;
	
	
	public Square(int[] position) {
		coord = position;
	}
	
	public int[] getCoord() {
		return coord;
	}
	
	public boolean hasToken() {
		return hasToken;
	}
	
	public int getToken() {
		return player;
	}
	
	public Rectangle2D getRect() {
		return rect;
	}
}
