package emilylow.checkerspackage;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Square {

	private int[] coord;
	private int player;
	private Rectangle2D rect;
	private Color squareColor; 
	private Color tokenColor; 
	private boolean active; 
	private boolean king;
	
	
	public Square(int[] position, Rectangle2D rectangle) {
		coord = position;
		//Is it best practice to pass in values and return them, like I'm doing, or to use local variables?
		rect = rectangle;
		king = false;
		
		setActive();
		setStartToken();
//		System.out.println("New square");
//		System.out.println(coord[0] + " " + coord[1]);
//		System.out.println("active " + active);
//		
	}
	
	public Square(int[] position, int player, boolean active, boolean king) {
		coord = position;
		this.player = player;
		this.active = active;
		this.king = king;
		
		rect = null;
		squareColor = null;
		tokenColor = null;
	}
	

	
	public void clear() {
		player = 0;
		tokenColor = null;
		king = false;
	}
	
	//Double check this code
	public void placeToken(int owner) {
		player = owner;
		setTokenColor();
	}
	
	//Not clone because only non-visual data is copied.
	public Square copySquare() {
		Square copy = new Square(coord.clone(), player, active, king);
		
		
		return copy;
	}
	
	
	public Color getSquareColor() {
		return squareColor;
	}
	public Color getTokenColor() {
		return tokenColor;
	}
	public int[] getCoord() {
		return coord;
	}
	
	public void makeKing() {
		king = true;
	}
	
	public boolean getKing() {
		return king; 
	}
	
	public boolean hasToken() {
		if (player == 0) {
			return false;
		}
		else { 
			return true;
		}
	}

	
	public int getPlayer() {
		return player;
	}
	
	public boolean getActive() {
		return active;
	}
	
	
	
	public Rectangle2D getRect() {
		return rect;
	}
	
	private void setStartToken() {
		if (!active) {
			player = 0;
			tokenColor = null;
		}
		else {
			int y = coord[1];
			
			if( y < 3) {
				player = 2;
				tokenColor = Color.BLUE;
			} else if (y > 4) {
				player = 1;
			} else {
				//No token
				player = 0; 
				
			}
		}
		setTokenColor();
	}
	
	private void setTokenColor() {
		if(player == 0) {
			tokenColor = null;
		} else if (player == 1) {
			tokenColor = Color.red;
		} else if (player ==2 ){
			tokenColor = Color.blue; 
		} else {
			//What is good practice here?
			System.out.println("Invalid player. Error.");
		}
	}
	
	public void setActive() {
		
		int x = coord[0];
		int y = coord[1];
		
		//If both are even or odd
		if(x%2 == y %2) {
			active = false;
			squareColor = Color.WHITE; 
		}
		else {
			active = true;
			squareColor = Color.GRAY; 
		}
		
		
	}
	
	public String toString() {
		
		String line1 = "Position: " + coord[0] + ":" + coord[1] + "";
		String line2 = "Player: " + player;
		String line3 = "King: " + king;
		
		return line1 + "\n" + line2 + "\n" + line3 + "\n";
	}
}
